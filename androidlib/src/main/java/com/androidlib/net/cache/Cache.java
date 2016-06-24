package com.androidlib.net.cache;

/**
 * Created by daner on 2016/5/3.
 * 1273482124@qq.com
 * 请求缓存接口
 * @param <K> key的类型
 * @param <V> value类型
 */
public interface Cache<K,V> {

    V get(K key);
    V put(K key,V value);
    void remove(K key);
}
