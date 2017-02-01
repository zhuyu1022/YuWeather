package com.example.zhuyu.yuweather.util;

import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ZHUYU on 2017/1/29 0029.
 */

public class HttpUtil {
    public static void sendOkhttpRequest(final String url, final Callback callback){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client=new OkHttpClient();
                final Request request=new Request.Builder()
                        .url(url)
                        .build();

                client.newCall(request).enqueue(callback);
            }
        }).start();

    }
}
