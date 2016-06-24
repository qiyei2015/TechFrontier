package com.techfrontier.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.techfrontier.R;

/**
 * Created by daner on 2016/5/31.
 * 1273482124@qq.com
 */
public class MenuViewHolder extends RecyclerView.ViewHolder{
    public ImageView userImageView;
    public TextView userTextView;

    public MenuViewHolder(View itemView){
        super(itemView);
        userImageView = (ImageView) itemView.findViewById(R.id.menu_icon_imageview);
        userTextView = (TextView) itemView.findViewById(R.id.menu_icon_textview);
    }
}
