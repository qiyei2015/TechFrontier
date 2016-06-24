package com.techfrontier;

import android.app.Application;

import com.techfrontier.database.ArticleDataBase;

/**
 * Created by daner on 2016/6/4.
 * 1273482124@qq.com
 */
public class TechFrontier extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ArticleDataBase.init(this);
    }
}
