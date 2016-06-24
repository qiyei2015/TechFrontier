package com.techfrontier.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by daner on 2016/6/4.
 * 1273482124@qq.com
 */
public class ArticleOpenHelper extends SQLiteOpenHelper {

    //article_basic表
    public static final String TABLE_ARTICLE_BASIC = "article_basic";
    //article_content表
    public static final String TABLE_ARTICLE_CONTENT = "article_content";
    //创建basic表语句
    private static final String CREATE_TABLE_BASIC = "CREATE TABLE " + TABLE_ARTICLE_BASIC + "("
            + "post_id INTEGER PRIMARY KEY UNIQUE, "
            + "author VARCHAR(30) NOT NULL, "
            + "title VARCHAR(100) NOT NULL, "
            + "publish_time VARCHAR(100) NOT NULL, "
            + "category INTEGER "
            +")";
    //创建content表语句
    private static final String CREATE_TABLE_CONTENT = "CREATE TABLE " + TABLE_ARTICLE_CONTENT + "("
            + "post_id INTEGER PRIMARY KEY UNIQUE, "
            + "content TEXT NOT NULL "
            +")";

    public ArticleOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_BASIC);
        db.execSQL(CREATE_TABLE_CONTENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TABLE_ARTICLE_BASIC);
        db.execSQL("DROP TABLE " + TABLE_ARTICLE_CONTENT);
        onCreate(db);
    }

}
