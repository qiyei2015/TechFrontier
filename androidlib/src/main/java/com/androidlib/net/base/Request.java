package com.androidlib.net.base;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by daner on 2016/4/30.
 */
public abstract class Request<T> implements Comparable<Request<T>> {

    /**
     * http 请求方法
     *
     */
    public static enum HttpMethod{
        //一共4种
        GET("GET"),POST("POST"),PUT("PUT"),DELETE("DELETE");

        private String method = "";

        private HttpMethod(String method){
            this.method = method;
        }

        @Override
        public String toString() {
            return method;
        }
    }

    /**
     *  请求优先级枚举
     */
    public static enum Priority{
        LOW,
        NORMAL,
        HIGH,
        IMMEDIATE
    }

    /**
     * 网络请求Listener
     *
     * @author qiyei2015
     * @param <T> 请求的response类型
     */
    public static interface RequestListener<T> {

        void onComplete(int stCode,T response,String errMsg);
    }

    //请求的默认编码
    private static final String DEFAULT_ENCODING = "utf-8";
    /**
     * Default Content-type
     */
    public final static String HEADER_CONTENT_TYPE = "Content-Type";

    //请求序列号
    protected int mSerialNum = 0;
    //默认的优先级
    protected Priority mPriority = Priority.NORMAL;
    //是否取消该请求
    protected boolean isCancel = false;
    //请求是否应该有缓存
    protected boolean mShouldCache = true;
    //请求Listener
    protected RequestListener<T> mRequestListener;
    //请求的URL
    private String mUrl = "";
    //请求的方法
    HttpMethod mHttpMethod = HttpMethod.GET;
    //请求的header
    private Map<String,String> mHeader = new HashMap<>();
    //请求参数
    private Map<String,String> mBodyParam = new HashMap<>();

    /**
     * @param method
     * @param url
     * @param listener
     */
    public Request(HttpMethod method,String url,RequestListener<T> listener){
        this.mHttpMethod = method;
        this.mUrl = url;
        this.mRequestListener = listener;
    }

    //解析返回的响应，子类复写
    public abstract T parseResponse(Response response);
    //处理Response ,该函数需要在UI现成运行
    public final void deliveryResponse(Response response) throws Exception{

        T result = parseResponse(response);
        if (mRequestListener != null){
            int stCode = -1;
            String msg = "unkown error";
            if ( response != null){
                stCode = response.getStatusCode();
                msg = response.getMessage();

            }
            mRequestListener.onComplete(stCode,result,msg);
            return;
        }
        throw new Exception("mRequestListener is null !");
    }

    //返回Http请求体内容类型
    public String getBodyContentType(){
        return "application/x-www-form-urlencoded; charset=" + getDefaultEncoding();
    }

    //判断是否是https 协议
    public boolean isHttps(){
        return mUrl.startsWith("https");
    }

    //添加http头
    public void addHeader(String name,String value){
        mHeader.put(name,value);
    }

    //返回http请求post,put的body参数字节数组
    public byte[] getBody(){
        Map<String,String> params = getBodyParam();
        if (params != null && (params.size() > 0)){
            return encodeParameter(params,getDefaultEncoding());
        }
        return null;
    }

    //将参数转化为URL编码的参数串，格式为key1=value1&&key2=value2
    public byte[] encodeParameter(Map<String,String> param,String encode){
        StringBuilder ecodeParam = new StringBuilder();

        try {
            for(Map.Entry<String,String> entity:param.entrySet()){
                ecodeParam.append(URLEncoder.encode(entity.getKey(),encode));
                ecodeParam.append('=');
                ecodeParam.append(URLEncoder.encode(entity.getValue(),encode));
                ecodeParam.append('&');
            }
            return ecodeParam.toString().getBytes(encode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

    }


    //比较两个对象，用于RequestQueue队列排序
    @Override
    public int compareTo(Request<T> another) {
        Priority priority = this.mPriority;
        Priority anotherPriority = another.getPriority();
        int result = 0;

        //如果相等，比较序列号，不相等,就比较序数，就是比较优先级了
        if (priority.equals(anotherPriority)){
            result = this.mSerialNum - another.getSerialNum();
        } else {
            result = priority.ordinal() - anotherPriority.ordinal();
        }
        return result;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mHeader == null) ? 0 : mHeader.hashCode());
        result = prime * result + ((mHttpMethod == null) ? 0 : mHttpMethod.hashCode());
        result = prime * result + ((mBodyParam == null) ? 0 : mBodyParam.hashCode());
        result = prime * result + ((mPriority == null) ? 0 : mPriority.hashCode());
        result = prime * result + (mShouldCache ? 1231 : 1237);
        result = prime * result + ((mUrl == null) ? 0 : mUrl.hashCode());
        return result;

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Request<?> other = (Request<?>) obj;
        if (mHeader == null) {
            if (other.mHeader != null)
                return false;
        } else if (!mHeader.equals(other.mHeader))
            return false;
        if (mHttpMethod != other.mHttpMethod)
            return false;
        if (mBodyParam == null) {
            if (other.mBodyParam != null)
                return false;
        } else if (!mBodyParam.equals(other.mBodyParam))
            return false;
        if (mPriority != other.mPriority)
            return false;
        if (mShouldCache != other.mShouldCache)
            return false;
        if (mUrl == null) {
            if (other.mUrl != null)
                return false;
        } else if (!mUrl.equals(other.mUrl))
            return false;
        return true;
    }

    //以下是变量的setter/getter方法

    public String getDefaultEncoding(){
        return DEFAULT_ENCODING;
    }

    public HttpMethod getHttpMethod(){
        return mHttpMethod;
    }

    public void setSerialNum(int num){
        mSerialNum = num;
    }
    public int getSerialNum(){
        return mSerialNum;
    }

    public void setPriority(Priority priority){
        mPriority = priority;
    }
    public Priority getPriority(){
        return mPriority;
    }

    public void setIsCancel(boolean cancel){
        this.isCancel = cancel;
    }
    public boolean getIsCancel(){
        return isCancel;
    }

    public void setShouldCache( boolean cache){
        this.mShouldCache = cache;
    }
    public boolean getShouldCache(){
        return mShouldCache;
    }

    public void setUrl(String url){
        this.mUrl = url;
    }
    public String getUrl(){
        return mUrl;
    }

    public void setHeader(Map<String,String> header){
        this.mHeader = header;
    }
    public Map<String,String> getHeader(){
        return mHeader;
    }

    public void setBodyParam(Map<String,String> bodyParam){
        this.mBodyParam = bodyParam;
    }
    public Map<String,String> getBodyParam(){
        return mBodyParam;
    }

}
