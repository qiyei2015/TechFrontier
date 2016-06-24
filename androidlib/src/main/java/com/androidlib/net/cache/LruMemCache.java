package com.androidlib.net.cache;

import android.util.LruCache;

import com.androidlib.net.base.Response;

/**
 * Created by daner on 2016/5/3.
 * 1273482124@qq.com
 * 将请求结果缓存到内存中
 */
public class LruMemCache implements Cache<String,Response>{
    //response缓存
    private LruCache<String,Response> mResponseCache;

    public LruMemCache(){
        //计算可用最大内存
        final int maxMemory = (int) Runtime.getRuntime().maxMemory();
        //取八分之一用作缓存
        final int cacheSize = maxMemory/8;
        mResponseCache = new LruCache<String,Response>(cacheSize){
            @Override
            protected int sizeOf(String key, Response value) {
                //返回response的rawData的长度
                return value.rawData.length;
            }
        };

    }

    @Override
    public Response get(String key) {
        return mResponseCache.get(key);
    }

    @Override
    public Response put(String key, Response value) {
        return mResponseCache.put(key,value);
    }

    @Override
    public void remove(String key) {
        mResponseCache.remove(key);
    }
}
