package com.terry.coolweather.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.terry.coolweather.service.AutoUpdateService;

/**
 * Created by tcw10 on 2016/8/12 0012.
 */
public class AutoUpdateReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, AutoUpdateService.class);
        context.startActivity(i);
    }
}
