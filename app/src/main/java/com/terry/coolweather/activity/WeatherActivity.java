package com.terry.coolweather.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.terry.coolweather.R;
import com.terry.coolweather.constant.AppConstant;
import com.terry.coolweather.model.County;
import com.terry.coolweather.service.AutoUpdateService;
import com.terry.coolweather.util.HttpCallbackListener;
import com.terry.coolweather.util.HttpUtil;
import com.terry.coolweather.util.Utility;

public class WeatherActivity extends AppCompatActivity {
    private static final String EXTRA_COUNTY = "extra_county";

    private String mCountyName;
    private Button mSwitchCity;
    private Button mRefreshWeather;
    private TextView mWenDu;
    private TextView mGanMao;
    private TextView mFengXiang;
    private TextView mFengLi;
    private TextView mHigh;
    private TextView mType;
    private TextView mLow;
    private TextView mDate;
    private TextView mCity;
    private TextView mDu;
    private MenuItem mMenuItem;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, WeatherActivity.class);
        return intent;
    }

    public static Intent newIntent(Context context, County county) {
        Intent intent = new Intent(context, WeatherActivity.class);
        intent.putExtra(EXTRA_COUNTY, county.getCountyName());
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        mWenDu = (TextView) findViewById(R.id.tv_wendu);
        mGanMao = (TextView) findViewById(R.id.tv_ganmao);
        mFengXiang = (TextView) findViewById(R.id.tv_feng_xiang);
        mFengLi = (TextView) findViewById(R.id.tv_feng_li);
        mHigh = (TextView) findViewById(R.id.tv_high);
        mType = (TextView) findViewById(R.id.tv_type);
        mLow = (TextView) findViewById(R.id.tv_low);
        mDate = (TextView) findViewById(R.id.tv_date);
        mCity = (TextView) findViewById(R.id.tv_city);
        mSwitchCity = (Button) findViewById(R.id.switch_city);
        mRefreshWeather = (Button) findViewById(R.id.refresh_weather);
        mDu = (TextView) findViewById(R.id.tv_du);
        mMenuItem = (MenuItem) findViewById(R.id.menu_setting);

        mCountyName = getIntent().getStringExtra(EXTRA_COUNTY);
        if (!TextUtils.isEmpty(mCountyName)) {
            mCity.setText("同步中...");
            queryWeatherInfo(mCountyName);
        } else {
            showWeatherInfo();
        }

        mSwitchCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WeatherActivity.this, MainActivity.class);
                intent.putExtra(AppConstant.IS_FROM_WEATHER_ACTIVITY, true);
                startActivity(intent);
                finish();
            }
        });
        mRefreshWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCity.setText("同步中...");
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
                String city = prefs.getString(AppConstant.WeatherInfo.CITY, "");
                if (!TextUtils.isEmpty(city)) {
                    queryWeatherInfo(city);
                } else {
                    queryWeatherInfo(mCountyName);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting, menu);
        return true;
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        boolean isRuning = Utility.isRunningService(this, "com.terry.coolweather.service.AutoUpdateService");
        if (isRuning) {
            menu.getItem(0).setTitle("关闭自动更新");

        } else {
            menu.getItem(0).setTitle("开启自动更新");
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, AutoUpdateService.class);
        boolean isRuning = Utility.isRunningService(this, "com.terry.coolweather.service.AutoUpdateService");
        if (isRuning) {
            stopService(intent);
        } else {
            startService(intent);
        }
        return true;
    }

    private void queryWeatherInfo(String city) {
        String address = "http://wthrcdn.etouch.cn/weather_mini?city=" + city;
        queryFromServer(address);
    }

    private void queryFromServer(String address) {
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Utility.handleWeatherResponse(WeatherActivity.this, response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showWeatherInfo();
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mCity.setText("同步失败");
                    }
                });
            }
        });
    }

    private void showWeatherInfo() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
        mCity.setText(prefs.getString(AppConstant.WeatherInfo.CITY, ""));
        mType.setText(prefs.getString(AppConstant.WeatherInfo.TYPE, ""));
        mWenDu.setText(prefs.getString(AppConstant.WeatherInfo.WENDU, ""));
        mGanMao.setText(prefs.getString(AppConstant.WeatherInfo.GANMAO, ""));
        mDate.setText(prefs.getString(AppConstant.WeatherInfo.DATE, ""));
        mFengXiang.setText(prefs.getString(AppConstant.WeatherInfo.FENG_XIANG, ""));
        mFengLi.setText(prefs.getString(AppConstant.WeatherInfo.FENG_LI, ""));
        mLow.setText(prefs.getString(AppConstant.WeatherInfo.LOW, "").substring(2, 5));
        mHigh.setText(prefs.getString(AppConstant.WeatherInfo.HIGH, "").substring(2, 5));
        mDu.setText("°");
    }
}
