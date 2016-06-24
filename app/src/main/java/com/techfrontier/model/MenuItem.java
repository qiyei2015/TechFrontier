package com.techfrontier.model;

/**
 * Created by daner on 2016/5/31.
 * 1273482124@qq.com
 */
public class MenuItem {
    public int iconResId;
    public String text;

    public MenuItem(String text,int resId){
        this.text = text;
        iconResId = resId;
    }
}
