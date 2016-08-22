package com.terry.coolweather.util;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.terry.coolweather.constant.AppConstant;
import com.terry.coolweather.database.CoolWeatherDB;
import com.terry.coolweather.model.City;
import com.terry.coolweather.model.County;
import com.terry.coolweather.model.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by tcw10 on 2016/7/28 0028.
 */
public class Utility {
    public synchronized static boolean handleProvinceResponse(CoolWeatherDB coolWeatherDB, String response) {
        if (!TextUtils.isEmpty(response)) {
            String[] allProvinces = response.split(",");
            if (allProvinces != null && allProvinces.length > 0) {
                for (String p : allProvinces) {
                    String[] array = p.split("\\|");
                    Province province = new Province();
                    province.setProvinceCode(array[0]);
                    province.setProvinceName(array[1]);

                    coolWeatherDB.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }

    public synchronized static boolean handleCityResponse(CoolWeatherDB coolWeatherDB, String response, int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            String[] allCities = response.split(",");
            if (allCities != null && allCities.length > 0) {
                for (String c : allCities) {
                    String[] array = c.split("\\|");
                    City city = new City();
                    city.setCityCode(array[0]);
                    city.setCityName(array[1]);
                    city.setProvinceId(provinceId);

                    coolWeatherDB.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }

    public synchronized static boolean handleCountyResponse(CoolWeatherDB coolWeatherDB, String response, int cityId) {
        if (!TextUtils.isEmpty(response)) {
            String[] allCounties = response.split(",");
            if (allCounties != null && allCounties.length > 0) {
                for (String c : allCounties) {
                    String[] array = c.split("\\|");
                    County county = new County();
                    county.setCountyCode(array[0]);
                    county.setCountyName(array[1]);
                    county.setCityId(cityId);

                    coolWeatherDB.saveCounty(county);
                }
                return true;
            }
        }
        return false;
    }

    public static void handleWeatherResponse(Context context, String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            String desc = jsonResponse.getString("desc");
            int status = jsonResponse.getInt("status");
            if ("OK".equals(desc) && 1000 == status) {
                JSONObject data = jsonResponse.getJSONObject("data");
                String wendu = data.getString("wendu");
                String ganmao = data.getString("ganmao");
                JSONArray jsonArray = data.getJSONArray("forecast");
                JSONObject weatherinfo = jsonArray.getJSONObject(0);
                String fengXiang = weatherinfo.getString("fengxiang");
                String fengLi = weatherinfo.getString("fengli");
                String high = weatherinfo.getString("high");
                String type = weatherinfo.getString("type");
                String low = weatherinfo.getString("low");
                String date = weatherinfo.getString("date");
                String city = data.getString("city");
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
                editor.putBoolean(AppConstant.WeatherInfo.CITY_SELECTED, true);
                editor.putString(AppConstant.WeatherInfo.WENDU, wendu);
                editor.putString(AppConstant.WeatherInfo.GANMAO, ganmao);
                editor.putString(AppConstant.WeatherInfo.FENG_XIANG, fengXiang);
                editor.putString(AppConstant.WeatherInfo.FENG_LI, fengLi);
                editor.putString(AppConstant.WeatherInfo.HIGH, high);
                editor.putString(AppConstant.WeatherInfo.TYPE, type);
                editor.putString(AppConstant.WeatherInfo.LOW, low);
                editor.putString(AppConstant.WeatherInfo.DATE, date);
                editor.putString(AppConstant.WeatherInfo.CITY, city);
                editor.commit();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static boolean isRunningService(Context context, String className) {
        // 进程的管理者,活动的管理者
        ActivityManager activityManager = (ActivityManager)
                context.getSystemService(Context.ACTIVITY_SERVICE);
        // 获取正在运行的服务，最多获取1000个
        List<ActivityManager.RunningServiceInfo> runningServices = activityManager.getRunningServices(1000);
        // 遍历集合
        for (ActivityManager.RunningServiceInfo runningServiceInfo : runningServices) {
            ComponentName service = runningServiceInfo.service;
            if (className.equals(service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
