package com.androidlib.net.entry;

import com.androidlib.net.core.RequestQueue;
import com.androidlib.net.httpstack.HttpStack;

/**
 * Created by daner on 2016/5/5.
 * 1273482124@qq.com
 * SimpleNet的入口
 */
public final class SimpleNet {

    /***
     *
     * @return 返回创建的队列
     */
    public static RequestQueue newRequestQueue(){
        return newRequestQueue(RequestQueue.CORE_NUM);
    }

    /***
     *
     * @param num 队列线程数
     * @return
     */
    public static RequestQueue newRequestQueue(int num){
        return newRequestQueue(num,null);
    }

    /**
     *
     * @param num 队列线程数
     * @param httpStack http请求栈
     * @return
     */
    public static RequestQueue newRequestQueue(int num, HttpStack httpStack){
        RequestQueue queue = new RequestQueue(Math.max(0,num),httpStack);
        queue.start();
        return queue;
    }

}
