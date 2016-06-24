package com.androidlib.net.base;

import org.apache.http.HttpEntity;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Entity;

import java.io.IOException;

/**
 * Created by daner on 2016/5/1.
 * 请求结果类,继承自BasicHttpResponse,将结果存储在rawData中.
 */
public class Response extends BasicHttpResponse{

    public byte[] rawData = new byte[0];

    public Response(StatusLine line){
        super(line);
    }

    public Response(ProtocolVersion version,int code,String reason){
        super(version,code,reason);

    }

    @Override
    public void setEntity(HttpEntity entity) {
        super.setEntity(entity);
        rawData = entityToBytes(entity);
    }

    public byte[] getRawData(){
        return rawData;
    }

    public int getStatusCode(){
        return getStatusLine().getStatusCode();
    }

    public String getMessage(){
        return getStatusLine().getReasonPhrase();
    }

    private byte[] entityToBytes(HttpEntity entity){
        try {
            return EntityUtils.toByteArray(entity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

}
