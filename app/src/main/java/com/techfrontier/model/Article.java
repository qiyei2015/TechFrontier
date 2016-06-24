package com.techfrontier.model;

/**
 * Created by qiyei on 2016/6/2.
 */
public class Article {

    public static final int ALL = 1;
    public static final int ANDROID = 2;
    public static final int IOS = 3;

    private String title;
    private String publishTime;
    private String author;
    private String postId;
    private int category;

    public Article(){

    }
    public Article(String id){
        postId = id;
    }

    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return this.title;
    }

    public void setPublishTime(String time){
        this.publishTime = time;
    }
    public String getPublishTime(){
        return this.publishTime;
    }

    public void setAuthor(String author){
        this.author = author;
    }
    public String getAuthor(){
        return this.author;
    }

    public void setPostId(String id){
        this.postId = id;
    }
    public String getPostId(){
        return this.postId;
    }

    public void setCategory(int category){
        this.category = category;
    }
    public int getCategory(){
        return this.category;
    }

    @Override
    public String toString() {
        return "Article [ title=" + title + ", publishTime=" + publishTime
                + ", author=" + author + ", postId=" + postId + ", category=" + category;
    }

}
