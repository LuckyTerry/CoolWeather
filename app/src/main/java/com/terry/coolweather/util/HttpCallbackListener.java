package com.terry.coolweather.util;

/**
 * Created by tcw10 on 2016/7/28 0028.
 */
public interface HttpCallbackListener {
    void onFinish(String response);

    void onError(Exception e);
}
