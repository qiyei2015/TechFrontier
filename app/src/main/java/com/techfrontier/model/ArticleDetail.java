package com.techfrontier.model;

/**
 * Created by qiyei on 2016/6/2.
 */
public class ArticleDetail {
    private String postId;
    private String content;

    public ArticleDetail(){

    }
    public ArticleDetail(String id,String content){
        this.postId = id;
        this.content = content;
    }

    public void setPostId(String id){
        this.postId = id;
    }
    public String getPostId(){
        return this.postId;
    }

    public void setContent(String content){
        this.content = content;
    }
    public String getContent(){
        return this.content;
    }
}
