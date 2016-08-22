package com.terry.coolweather.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by tcw10 on 2016/7/28 0028.
 */
public class HttpUtil {
    public static void sendHttpRequest(final String address, final HttpCallbackListener httpCallbackListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection httpURLConnection = null;
                try {
                    URL url = new URL(address);
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(8000);
                    httpURLConnection.setReadTimeout(8000);
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line = null;
                    StringBuilder response = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    inputStream.close();
                    if (httpCallbackListener != null) {
                        httpCallbackListener.onFinish(response.toString());
                    }
                } catch (Exception e) {
                    if (httpCallbackListener != null) {
                        httpCallbackListener.onError(e);
                    }
                    e.printStackTrace();
                } finally {
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                }
            }
        }).start();
    }
}
