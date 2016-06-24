package com.techfrontier.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.techfrontier.model.Article;
import com.techfrontier.model.ArticleDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daner on 2016/6/4.
 * 1273482124@qq.com
 */
public class ArticleDataBase {

    private static final String NAME = "article_database.db";
    private static final int VERSION = 1;

    private SQLiteDatabase db;
    private static ArticleDataBase mDataBase = null;

    private ArticleDataBase(Context context){
        ArticleOpenHelper articleOpenHelper = new ArticleOpenHelper(context,NAME,null,VERSION);
        db = articleOpenHelper.getWritableDatabase();
    }

    public static void init(Context context){
        if (mDataBase == null){
            mDataBase = new ArticleDataBase(context);
        }
    }

    /**
     * 返回相应的实例
     * @return
     */
    public static ArticleDataBase getInstance(){
        if (mDataBase == null){
            throw new NullPointerException("mDataBase is null , please init first !");
        }
        return mDataBase;
    }

    /**
     * 保存文章列表
     * @param articleList
     */
    public void saveArticleBasic(List<Article> articleList){
        for(Article article:articleList){
            ContentValues values = new ContentValues();
            values.put("post_id",article.getPostId());
            values.put("author",article.getAuthor());
            values.put("title",article.getTitle());
            values.put("publish_time",article.getPublishTime());
            values.put("category",article.getCategory());
            db.insertWithOnConflict(ArticleOpenHelper.TABLE_ARTICLE_BASIC,null,values,SQLiteDatabase.CONFLICT_REPLACE);
        }
    }

    /**
     * 从数据库加载文章列表数据
     * @return
     */
    public List<Article> loadArticleBasic(){
        Cursor cursor = db.query(ArticleOpenHelper.TABLE_ARTICLE_BASIC,null,null,null,null,null,null);
        List<Article> articleList = new ArrayList<>();
        while (cursor.moveToNext()){
            Article article = new Article();
            article.setPostId(cursor.getString(cursor.getColumnIndex("post_id")));
            article.setAuthor(cursor.getString(cursor.getColumnIndex("author")));
            article.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            article.setPublishTime(cursor.getString(cursor.getColumnIndex("publish_time")));
            article.setCategory(cursor.getInt(cursor.getColumnIndex("category")));
            articleList.add(article);
        }
        cursor.close();
        return articleList;
    }

    /**
     * 保存文章详细内容
     * @param articleDetail
     */
    public void saveArticleDetail(ArticleDetail articleDetail){
            ContentValues values = new ContentValues();
            values.put("post_id",articleDetail.getPostId());
            values.put("content",articleDetail.getContent());
            db.insertWithOnConflict(ArticleOpenHelper.TABLE_ARTICLE_CONTENT,null,values,SQLiteDatabase.CONFLICT_REPLACE);
    }

    /**
     * 根据id加载对应的文章详细内容
     * @param id
     * @return
     */
    public ArticleDetail loadArticleDetail(String id){

        Cursor cursor = db.query(ArticleOpenHelper.TABLE_ARTICLE_CONTENT,null,"content = ?",new String[]{id},null,null,null);
        String content = "";
        ArticleDetail detail = new ArticleDetail();
        while (cursor.moveToNext()){
            content = cursor.getString(1);
            detail.setPostId(id);
            detail.setContent(content);
        }
        cursor.close();
        return detail;
    }

}
