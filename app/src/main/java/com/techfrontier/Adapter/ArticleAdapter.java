package com.techfrontier.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.techfrontier.R;
import com.techfrontier.listener.OnItemClickListener;
import com.techfrontier.model.Article;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daner on 2016/6/2.
 * 1273482124@qq.com
 */
public class ArticleAdapter extends RecyclerView.Adapter<ArticleViewHolder>{

    List<Article> mArticleList = new ArrayList<>();
    OnItemClickListener<Article> mItemClickListener;

    public ArticleAdapter(List<Article> list){
        mArticleList = list;
    }

    @Override
    public int getItemCount() {
        return mArticleList.size();
    }

    public Article getItem(int position){
        return mArticleList.get(position);
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_view_article_item,parent,false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {

        Article article = getItem(position);
        holder.titleTv.setText(article.getTitle());
        holder.publishTimeTv.setText(article.getPublishTime());
        holder.authorTv.setText(article.getAuthor());
        setupOnItemClickListener(holder,article);
    }

    public void setOnItemClickListener(OnItemClickListener<Article> onItemClickListener){
        this.mItemClickListener = onItemClickListener;
    }

    private void setupOnItemClickListener(ArticleViewHolder holder, final Article article){
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null){
                    mItemClickListener.click(article);
                }
            }
        });
    }

}
