package com.androidlib.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by qiyei2015 on 2016/4/11.
 */
public abstract class BaseActivity extends AppCompatActivity {

    //protected RequestManager requestManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariables();
        initViews(savedInstanceState);
        loadData();

    }

    //初始化变量
    protected abstract void initVariables();

    //初始化控件
    protected abstract void initViews(Bundle savedInstanceState);

    //加载数据
    protected abstract void loadData();

    protected void onDestroy() {
        /**
         * 在activity销毁的时候同时设置停止请求，停止线程请求回调
         */
        /*
        if (requestManager != null) {
            requestManager.cancelRequest();
        }*/
        super.onDestroy();
    }

    protected void onPause() {
        /**
         * 在activity停止的时候同时设置停止请求，停止线程请求回调
         */
        /*
        if (requestManager != null) {
            requestManager.cancelRequest();
        }*/
        super.onPause();
    }

    /*
    public RequestManager getRequestManager() {
        return requestManager;
    }
    */
}
