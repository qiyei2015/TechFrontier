package com.techfrontier.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.techfrontier.R;

/**
 * Created by qiyei on 2016/6/3.
 */
public class ArticleViewHolder extends RecyclerView.ViewHolder{

    public TextView titleTv;
    public TextView publishTimeTv;
    public TextView authorTv;

    public ArticleViewHolder(View itemView){
        super(itemView);
        titleTv = (TextView) itemView.findViewById(R.id.article_title_tv);
        publishTimeTv = (TextView)itemView.findViewById(R.id.article_publish_time_tv);
        authorTv = (TextView) itemView.findViewById(R.id.article_author_tv);
    }

}
