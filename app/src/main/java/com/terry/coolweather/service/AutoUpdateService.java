package com.terry.coolweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.terry.coolweather.constant.AppConstant;
import com.terry.coolweather.receiver.AutoUpdateReceiver;
import com.terry.coolweather.util.HttpCallbackListener;
import com.terry.coolweather.util.HttpUtil;
import com.terry.coolweather.util.Utility;

/**
 * Created by tcw10 on 2016/8/12 0012.
 */
public class AutoUpdateService extends Service{

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                updateWeather();
            }
        }).start();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 8 * 60 * 60 * 1000;
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, AutoUpdateReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    private void updateWeather() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String city = prefs.getString(AppConstant.WeatherInfo.CITY,"");
        if(!TextUtils.isEmpty(city)){
            String address = "http://wthrcdn.etouch.cn/weather_mini?city=" + city;
            HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    Utility.handleWeatherResponse(AutoUpdateService.this, response);
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

}
