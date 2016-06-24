package com.androidlib.net.request;

import com.androidlib.net.base.Request;
import com.androidlib.net.base.Response;

/**
 * Created by daner on 2016/5/5.
 * 1273482124@qq.com
 */
public class StringRequest extends Request<String> {

    public StringRequest(HttpMethod method,String url,RequestListener<String> listener){
        super(method,url,listener);
    }


    @Override
    public String parseResponse(Response response) {
        return new String(response.getRawData());
    }
}
