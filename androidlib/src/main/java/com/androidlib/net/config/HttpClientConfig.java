package com.androidlib.net.config;

import org.apache.http.conn.ssl.SSLSocketFactory;

/**
 * Created by daner on 2016/5/3.
 * 1273482124@qq.com
 */
public class HttpClientConfig  extends HttpConfig{

    private static HttpClientConfig sConfig = new HttpClientConfig();

    private HttpClientConfig(){

    }

    public static HttpClientConfig getConfig(){
        return sConfig;
    }

    private SSLSocketFactory mSocketFactory = null;

    /**
     * 配置https请求的SSLSocketFactory与HostnameVerifier
     *
     * @param factory
     * @param
     */
    public void setHttpsConfig(SSLSocketFactory factory){
        mSocketFactory = factory;
    }

    public SSLSocketFactory getSocketFactory(){
        return mSocketFactory;
    }



}
