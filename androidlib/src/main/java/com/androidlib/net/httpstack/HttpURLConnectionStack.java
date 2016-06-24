package com.androidlib.net.httpstack;

import com.androidlib.net.base.Request;
import com.androidlib.net.base.Response;
import com.androidlib.net.config.HttpURLConnectionConfig;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;


/**
 * Created by daner on 2016/5/3.
 * 1273482124@qq.com
 */
public class HttpURLConnectionStack implements HttpStack{
    private final String TAG = "HttpURLConnectionStack";
    /**
     *  配置Https
     */
    private HttpURLConnectionConfig mConfig = HttpURLConnectionConfig.getCongif();


    @Override
    public Response performRequest(Request<?> request) {
        HttpURLConnection connection = null;
        //获取url地址
        try {
            String url = request.getUrl();
            connection = createHttpURLConnection(url);
            if (connection != null){
                //设置header参数
                setRequestHeader(connection,request);
                //设置body参数
                setRequestBodyParam(connection,request);
                //配置https
                configHttps(request);
                //返回response
                return fetchResponse(connection);
            }
        } catch (Exception e) {
            e.printStackTrace();

            if (connection != null) {
                connection.disconnect();
            }

        }
        return null;
    }

    private HttpURLConnection createHttpURLConnection(String url){

        try {
            URL mUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) mUrl.openConnection();
            //设置超时时间
            conn.setConnectTimeout(mConfig.connTimeOut);
            //设置读取超时
            conn.setReadTimeout(mConfig.soTimeOut);
            //设置需要URL输入，需要从URL读取数据
            conn.setDoInput(true);
            //设置是否使用缓存
            conn.setUseCaches(false);

            return conn;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    private void setRequestHeader(HttpURLConnection connection,Request<?> request){
        //获取http头的Map的key set
        Set<String> key = request.getHeader().keySet();
        for(String headerName:key){
            connection.addRequestProperty(headerName,request.getHeader().get(headerName));
        }
    }

    private void setRequestBodyParam(HttpURLConnection connection,Request<?> request){

        //设置请求方法
        Request.HttpMethod method = request.getHttpMethod();
        try {
            connection.setRequestMethod(method.toString());
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        byte[] body = request.getBody();
        if (body != null){
            //使能URL输出
            connection.setDoOutput(true);
            //添加内容属性
            connection.addRequestProperty(Request.HEADER_CONTENT_TYPE,request.getBodyContentType());
            //添加body参数

            try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream()))
            {
                outputStream.write(body);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private void configHttps(Request<?> request){
        if (request.isHttps()){
            SSLSocketFactory sslSocketFactory = mConfig.getSocketFactory();
            //配置https
            if (sslSocketFactory != null){
                HttpsURLConnection.setDefaultSSLSocketFactory(sslSocketFactory);
                HttpsURLConnection.setDefaultHostnameVerifier(mConfig.getVerifier());
            }

        }
    }

    private Response fetchResponse(HttpURLConnection connection){
        //初始化协议版本
        ProtocolVersion version = new ProtocolVersion("HTTP",1,1);

        int statusCode = 0;
        String responseMessage = null;
        try {
            int responseCode = connection.getResponseCode();

            if (responseCode == -1){
                throw new IOException("Could not retrieve response code from HttpUrlConnection.");
            }

            statusCode = connection.getResponseCode();
            responseMessage = connection.getResponseMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //状态行数据
        StatusLine line = new BasicStatusLine(version,statusCode,responseMessage);

        Response response = new Response(line);
        response.setEntity(entityFromHttpURLConnection(connection));
        addHeaderToResponse(response,connection);

        return response;
    }

    /**
     * 执行HTTP请求之后获取到其数据流,即返回请求结果的流
     *
     * @param connection
     * @return
     */
    private HttpEntity entityFromHttpURLConnection(HttpURLConnection connection){

        BasicHttpEntity entity = new BasicHttpEntity();
        InputStream in = null;

        try {
            in = connection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            in = connection.getErrorStream();
        }

        entity.setContent(in);
        entity.setContentLength(connection.getContentLength());
        entity.setContentEncoding(connection.getContentEncoding());
        entity.setContentType(connection.getContentType());

        return entity;
    }


    private void addHeaderToResponse(BasicHttpResponse response,HttpURLConnection connection){
        //获取HttpURLConnection header的所有fields
        Set<Map.Entry<String,List<String>>> headerSet = connection.getHeaderFields().entrySet();
        for (Map.Entry<String,List<String>> header:headerSet){
            if (header.getKey() != null){
                Header h = new BasicHeader(header.getKey(),header.getValue().get(0));
                response.addHeader(h);
            }
        }
    }

}
