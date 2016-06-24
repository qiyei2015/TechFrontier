package com.androidlib.net.httpstack;

import android.os.Build;


/**
 * Created by daner on 2016/5/2.
 */
public class HttpStackFactory {

    private static final int SDKVersion = 9;


    public static HttpStack createHttpStack(){
        //获得SDK版本
        int runtimeSDKVersion = Build.VERSION.SDK_INT;
        if ( runtimeSDKVersion > SDKVersion) {
            return new HttpURLConnectionStack();
        }
        return new HttpClientStack();
    }
}
