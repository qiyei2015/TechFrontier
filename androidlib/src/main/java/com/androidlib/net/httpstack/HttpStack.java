package com.androidlib.net.httpstack;

import com.androidlib.net.base.Request;
import com.androidlib.net.base.Response;

/**
 * Created by daner on 2016/5/2.
 * 执行网络请求的接口
 */
public interface HttpStack {
    /**
     * 执行Http请求
     *
     * @param request 待执行的请求
     * @return response 返回的响应
     */
    Response performRequest(Request<?> request);
}
