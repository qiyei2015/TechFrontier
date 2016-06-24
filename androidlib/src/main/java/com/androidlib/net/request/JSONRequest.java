package com.androidlib.net.request;

import com.androidlib.net.base.Request;
import com.androidlib.net.base.Response;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by daner on 2016/5/5.
 * 1273482124@qq.com
 */
public class JSONRequest extends Request<JSONObject> {


    public JSONRequest(HttpMethod method,String url,RequestListener<JSONObject> listener){
        super(method,url,listener);
    }

    @Override
    public JSONObject parseResponse(Response response) {
        String string = new String(response.getRawData());
        try {
            return new JSONObject(string);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
