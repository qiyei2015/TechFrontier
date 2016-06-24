package com.androidlib.net.core;

import android.os.Handler;
import android.os.Looper;

import com.androidlib.net.base.Request;
import com.androidlib.net.base.Response;

import java.util.concurrent.Executor;


/**
 * Created by daner on 2016/5/2.
 */
public class ResponseDelivery implements Executor {

    /**
     * 主线程的Handler
     */
    private Handler mHandler = new Handler(Looper.getMainLooper());

    /***
     *  处理请求结果,将其执行在UI线程
     * @param request http 请求
     * @param response http响应
     */
    public void deliveryResponse(final Request<?> request, final Response response){

        Runnable responseRunable = new Runnable() {
            @Override
            public void run() {
                try {
                    request.deliveryResponse(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        execute(responseRunable);
    }

    @Override
    public void execute(Runnable command) {
        //handler投递到主线程去
        mHandler.post(command);
    }
}
