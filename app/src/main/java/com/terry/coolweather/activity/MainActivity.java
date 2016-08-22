package com.terry.coolweather.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.terry.coolweather.R;
import com.terry.coolweather.constant.AppConstant;
import com.terry.coolweather.database.CoolWeatherDB;
import com.terry.coolweather.model.City;
import com.terry.coolweather.model.County;
import com.terry.coolweather.model.Province;
import com.terry.coolweather.util.HttpCallbackListener;
import com.terry.coolweather.util.HttpUtil;
import com.terry.coolweather.util.Utility;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int PROVINCE_LEVEL = 0;
    private static final int CITY_LEVEL = 1;
    private static final int COUNTY_LEVEL = 2;

    private RecyclerView mRecyclerView;


    private List<Province> mProvinceList;
    private List<City> mCityList;
    private List<County> mCountyList;

    private MyAdapter mMyAdapter;
    private List<String> mNameList = new ArrayList<>();
    private int mCurrentLevel;
    private Province mSelectedProvince;
    private City mSelectedCity;

    private CoolWeatherDB mCoolWeatherDB;

    private ProgressDialog mProgressDialog;
    private TextView mTitleText;

    private boolean mIsFromWeatherActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mIsFromWeatherActivity = getIntent().getBooleanExtra(AppConstant.IS_FROM_WEATHER_ACTIVITY,false);
        SharedPreferences prefs = PreferenceManager.
                getDefaultSharedPreferences(this);
        if (prefs.getBoolean(AppConstant.WeatherInfo.CITY_SELECTED, false) && !mIsFromWeatherActivity) {
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        mTitleText = (TextView) findViewById(R.id.tv_title);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_location);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMyAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mMyAdapter);

        mCoolWeatherDB = CoolWeatherDB.getInstance(this);
        queryProvinces();
    }

    private void queryProvinces() {
        mProvinceList = mCoolWeatherDB.loadProvinces();

        if(mProvinceList.size() > 0){
            mNameList.clear();
            for (Province province : mProvinceList){
                mNameList.add(province.getProvinceName());
            }
            mMyAdapter.notifyDataSetChanged();
            mCurrentLevel = PROVINCE_LEVEL;
            mTitleText.setText("中国");
        }else{
            queryFromServer(null,"province");
        }
    }

    private void queryCities() {
        mCityList = mCoolWeatherDB.loadCities(mSelectedProvince.getProvinceId());

        if(mCityList.size() > 0){
            mNameList.clear();
            for (City city : mCityList){
                mNameList.add(city.getCityName());
            }
            mMyAdapter.notifyDataSetChanged();
            mCurrentLevel = CITY_LEVEL;
            mTitleText.setText("" + mSelectedProvince.getProvinceName());
        }else{
            queryFromServer(mSelectedProvince.getProvinceCode(),"city");
        }
    }

    private void queryCounties() {
        mCountyList = mCoolWeatherDB.loadCounties(mSelectedCity.getCityId());

        if(mCountyList.size() > 0){
            mNameList.clear();
            for (County county : mCountyList){
                mNameList.add(county.getCountyName());
            }
            mMyAdapter.notifyDataSetChanged();
            mCurrentLevel = COUNTY_LEVEL;
            mTitleText.setText("" + mSelectedCity.getCityName());
        }else{
            queryFromServer(mSelectedCity.getCityCode(),"county");
        }
    }

    private void queryFromServer(final String code, final String type){
        final String address;
        if(!TextUtils.isEmpty(code)){
            address = "http://www.weather.com.cn/data/list3/city" + code + ".xml";
        }else{
            address = "http://www.weather.com.cn/data/list3/city.xml";
        }
        //显示进度对话框
        showProgressDialog();
        //发送网络请求
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                boolean result = false;
                if ("province".equals(type)) {
                    result = Utility.handleProvinceResponse(mCoolWeatherDB,response);
                }else if("city".equals(type)){
                    result = Utility.handleCityResponse(mCoolWeatherDB,response,mSelectedProvince.getProvinceId());
                }else if("county".equals(type)){
                    result = Utility.handleCountyResponse(mCoolWeatherDB,response,mSelectedCity.getCityId());
                }
                if(result){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //关闭进度对话框
                            closeProgressDialog();
                            //再次查询。这次是从本地
                            if ("province".equals(type)) {
                                queryProvinces();
                            }else if("city".equals(type)){
                                queryCities();
                            }else if("county".equals(type)){
                                queryCounties();
                            }
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //关闭对话框
                        closeProgressDialog();
                        //加载失败提示
                        Toast.makeText(MainActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MainActivity.this).inflate(android.R.layout.simple_list_item_1,parent,false);

            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.bindName(position);
        }

        @Override
        public int getItemCount() {
            return mNameList.size();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView textView;
        private int position;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView;
            textView.setOnClickListener(this);
        }

        public void bindName(int position){
            this.position = position;
            textView.setText(mNameList.get(position));
        }

        @Override
        public void onClick(View view) {
            if(mCurrentLevel == PROVINCE_LEVEL){//加载City数据


                mSelectedProvince = mProvinceList.get(position);
                queryCities();
            }else if(mCurrentLevel == CITY_LEVEL){//加载County数据

                mSelectedCity = mCityList.get(position);
                queryCounties();
            }else if(mCurrentLevel == COUNTY_LEVEL){
                Intent intent = WeatherActivity.newIntent(MainActivity.this,mCountyList.get(position));
                startActivity(intent);
                finish();
            }
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("正在加载...");
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mProgressDialog.show();
    }
    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        if(mCurrentLevel == COUNTY_LEVEL){
            queryCities();
        }else if(mCurrentLevel == CITY_LEVEL){
            queryProvinces();
        }else{
            if (mIsFromWeatherActivity) {
                Intent intent = WeatherActivity.newIntent(this);
                startActivity(intent);
            }
            finish();
        }
    }
}
