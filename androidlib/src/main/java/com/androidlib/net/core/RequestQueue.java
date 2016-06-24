package com.androidlib.net.core;

import com.androidlib.net.base.Request;
import com.androidlib.net.httpstack.HttpStack;
import com.androidlib.net.httpstack.HttpStackFactory;
import com.androidlib.utils.LogUtil;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by daner on 2016/5/2.
 */
public class RequestQueue {
    //线程安全请求队列
    private BlockingQueue<Request<?>> requestQueue = new PriorityBlockingQueue<Request<?>>();
    //请求序列化生成器
    private AtomicInteger serialNumGenerator = new AtomicInteger(0);
    //默认的CPU数
    public static final int CORE_NUM = Runtime.getRuntime().availableProcessors() + 1;
    //分发任务数
    private int dispatcherNum = CORE_NUM;
    //NetworkExecutor,执行的网络线程
    private NetworkExecutor[] dispatcher = null;
    //HttpStack ,执行Http真正的请求者
    private HttpStack httpStack;

    private final String TAG = "RequestQueue";

    /**
     * @param num 线程核心数
     * @param stack http执行器
     */
    public RequestQueue(int num,HttpStack stack){
        dispatcherNum  = num;
        if (stack != null){
            httpStack = stack;
        } else {
            httpStack = HttpStackFactory.createHttpStack();
        }
    }

    //序列数自动增加
    private int generateSerialNum(){
        return serialNumGenerator.incrementAndGet();
    }

    /**
     * 启动NetworkExecutor
     */
    private void startNetworkExecutor(){
        dispatcher = new NetworkExecutor[dispatcherNum];
        for (int i = 0;i < dispatcherNum; i++){
            dispatcher[i] = new NetworkExecutor(requestQueue,httpStack);
            dispatcher[i].start();
        }

    }

    /**
     * 停止NetworkExecutor
     */
    public void stop(){
        if (dispatcher != null && dispatcher.length > 0 ){
            for (int i = 0;i < dispatcher.length ; i++){
                dispatcher[i].quit();
            }
        }
    }

    /**
     * 启动
     */
    public void start(){
        stop();
        startNetworkExecutor();
    }

    /**
     * 添加request到requestQueue,不能重复添加请求
     *
     * @param request
     */
    public void addRequest(Request<?> request){
        //如果不包含，设置请求序列号
        if (!requestQueue.contains(request)){
            request.setSerialNum(this.generateSerialNum());
            requestQueue.add(request);
        } else {
            LogUtil.d(TAG,"##### 请求队列中已经含有");
        }

    }

    public void clear(){
        requestQueue.clear();
    }

    public BlockingQueue<Request<?>> getAllRequest(){
        return requestQueue;
    }

}
