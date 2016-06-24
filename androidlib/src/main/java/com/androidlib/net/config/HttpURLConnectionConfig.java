package com.androidlib.net.config;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;


/**
 * Created by daner on 2016/5/3.
 * 1273482124@qq.com
 * 采用单例模式
 */
public class HttpURLConnectionConfig extends HttpConfig{

    private static HttpURLConnectionConfig sConfig = new HttpURLConnectionConfig();
    //创建 SSLSocket的Factory
    private SSLSocketFactory mSocketFactory = null;
    /**
     * 此类是用于主机名验证的基接口。在握手期间，如果 URL 的主机名和服务器的标识主机名不匹配，
     * 则验证机制可以回调此接口的实现程序来确定是否应该允许此连接。
     * 策略可以是基于证书的或依赖于其他验证方案。当验证 URL 主机名使用的默认规则失败时使用这些回调
     */
    private HostnameVerifier mVerifier = null;

    private HttpURLConnectionConfig(){

    }

    //返回配置
    public static HttpURLConnectionConfig getCongif(){
        return sConfig;
    }

    /**
     * 配置https请求的SSLSocketFactory与HostnameVerifier
     *
     * @param factory
     * @param verifier
     */
    public void setHttpsConfig(SSLSocketFactory factory,HostnameVerifier verifier){
        this.mSocketFactory = factory;
        this.mVerifier = verifier;
    }

    public SSLSocketFactory getSocketFactory(){
        return mSocketFactory;
    }

    public HostnameVerifier getVerifier(){
        return mVerifier;
    }

}
