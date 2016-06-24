package com.androidlib.net.core;

import com.androidlib.net.base.Request;
import com.androidlib.net.base.Response;
import com.androidlib.net.cache.Cache;
import com.androidlib.net.cache.LruMemCache;
import com.androidlib.net.httpstack.HttpStack;
import com.androidlib.utils.LogUtil;

import java.util.concurrent.BlockingQueue;

/**
 * Created by daner on 2016/5/2.
 * 从请求队列中不断的取出请求交给httpstack执行
 */
public class NetworkExecutor extends Thread {
    private final String TAG = "NetworkExecutor";

    //请求队列
    private BlockingQueue<Request<?>> requestQueue = null;
    //网络请求栈
    private HttpStack httpStack;
    //结果分发器，将结果投递到主线程
    private ResponseDelivery mResponseDelivery = new ResponseDelivery();
    //请求缓存
    public static Cache<String,Response> mRequestCahce = new LruMemCache();

    //是否停止
    private boolean isStop = false;

    public NetworkExecutor(BlockingQueue<Request<?>> queue,HttpStack stack){
        this.requestQueue = queue;
        this.httpStack = stack;
    }


    @Override
    public void run() {

        //循环从队列取出请求交给httpstack
        while (!isStop){
            try {
                Request<?> request = requestQueue.take();
                if (request.getIsCancel()){
                    LogUtil.d(TAG,"#### 已经取消！");
                    continue;
                }

                Response response = null;
                if (isUseCahce(request)){
                    //从缓存中获取
                    response = mRequestCahce.get(request.getUrl());
                } else {
                    //从网络获取
                    response = httpStack.performRequest(request);
                    //缓存数据
                    if (request.getShouldCache() && isSuccess(response)){
                        mRequestCahce.put(request.getUrl(),response);
                    }
                }

                //分发请求response
                mResponseDelivery.deliveryResponse(request,response);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private boolean isSuccess(Response response){
        return response != null && response.getStatusCode() == 200;
    }

    private boolean isUseCahce(Request<?> request){
        return request.getShouldCache() && mRequestCahce.get(request.getUrl()) != null;
    }


    /**
     * 网络请求退出
     * @param
     * @param
     */
    public void quit(){
        isStop = true;
        //调用线程的interrupt()方法中断线程运行
        interrupt();
    }

}
