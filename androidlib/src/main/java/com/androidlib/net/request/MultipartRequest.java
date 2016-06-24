package com.androidlib.net.request;

import com.androidlib.net.base.Request;
import com.androidlib.net.base.Response;
import com.androidlib.net.entity.MultipartEntity;

/**
 * Created by daner on 2016/5/8.
 * 1273482124@qq.com
 * Multipart请求 ( 只能为POST请求 ),该请求可以搭载多种类型参数,比如文本、文件等,但是文件仅限于小文件,否则会出现OOM异常.
 */
public class MultipartRequest extends Request<String> {

    public MultipartRequest(String url,RequestListener<String> listener){
        super(HttpMethod.POST,url,listener);
    }


    @Override
    public String parseResponse(Response response) {
        return null;
    }
}
