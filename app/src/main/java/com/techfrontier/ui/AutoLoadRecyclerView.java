package com.techfrontier.ui;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.techfrontier.listener.OnLoadListener;


/**
 * Created by qiyei on 2016/6/2.
 */
public class AutoLoadRecyclerView extends RecyclerView {

    OnLoadListener mLoadListener;
    boolean isLoading = false;
    boolean isValidDelay = true;

    public AutoLoadRecyclerView(Context context){
        super(context);
        if (isInEditMode()){
            return;
        }
        init();
    }

    public AutoLoadRecyclerView(Context context, AttributeSet set){
        super(context,set);
        if (isInEditMode()){
            return;
        }
        init();
    }

    public AutoLoadRecyclerView(Context context, AttributeSet set,int defStyle){
        super(context,set,defStyle);
        if (isInEditMode()){
            return;
        }
        init();
    }

    public void setOnLoadListener(OnLoadListener listener){
        mLoadListener = listener;
    }

    public void setLoading(boolean loading){
        this.isLoading = loading;
    }

    //执行初始化
    private void init(){
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                checkLoadMore(dx,dy);
            }
        });
    }

    //检查更多
    private void checkLoadMore(int dx,int dy){
        if (isBottom(dx,dy) && !isLoading && isValidDelay && mLoadListener != null){
            isValidDelay = false;
            mLoadListener.onLoad();
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    isValidDelay = true;
                }
            },1000);
        }
    }

    private boolean isBottom(int dx , int dy){

        LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
        int totalItemItem = layoutManager.getItemCount();
        int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
        return lastVisibleItem > totalItemItem - 4 && dy > 0;
    }


}
