package com.techfrontier.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.techfrontier.R;
import com.techfrontier.listener.OnItemClickListener;
import com.techfrontier.model.MenuItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daner on 2016/5/31.
 * 1273482124@qq.com
 */
public class MenuAdapter extends RecyclerView.Adapter<MenuViewHolder> {

    List<MenuItem> mItemList = new ArrayList<>();
    OnItemClickListener<MenuItem> mItemClickListener;

    public MenuAdapter(List<MenuItem> list){
        mItemList = list;
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    protected MenuItem getItem(int position){
        return mItemList.get(position);
    }

    //绑定数据,将position处的数据绑定到MenuViewHolder中
    @Override
    public void onBindViewHolder(MenuViewHolder holder, int position) {

        MenuItem menuItem = getItem(position);

        holder.userImageView.setImageResource(menuItem.iconResId);
        holder.userTextView.setText(menuItem.text);

        //设置监听器
        setupItemViewClickListener(holder,menuItem);

    }

    //创建MenuViewHolder
    @Override
    public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        //加载指定布局
        View view = layoutInflater.inflate(R.layout.menu_item,parent,false);

        return new MenuViewHolder(view);
    }

    public void setOnItemClickListener(OnItemClickListener<MenuItem> onItemClickListener){
        this.mItemClickListener = onItemClickListener;
    }

    protected void setupItemViewClickListener(MenuViewHolder viewHolder,final MenuItem item){
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null){
                    mItemClickListener.click(item);
                }
            }
        });
    }

}
