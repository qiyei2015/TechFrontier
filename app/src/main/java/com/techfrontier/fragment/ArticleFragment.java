package com.techfrontier.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.techfrontier.Adapter.ArticleAdapter;
import com.techfrontier.R;
import com.techfrontier.activity.ArticleDetailActivity;
import com.techfrontier.database.ArticleDataBase;
import com.techfrontier.listener.OnItemClickListener;
import com.techfrontier.listener.OnLoadListener;
import com.techfrontier.model.Article;
import com.techfrontier.ui.AutoLoadRecyclerView;
import com.techfrontier.ui.RecycleViewDivider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by qiyei on 2016/6/2.
 */
public class ArticleFragment extends Fragment {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private AutoLoadRecyclerView mRecyclerView;
    private int mCategory = Article.ALL;
    private ArticleAdapter mAdapter;
    private List<Article> mArticleList = new ArrayList<>();
    //文章页面索引，用于分页加载
    private int mPageIndex = 1;

    private ArticleDataBase mArticleDataBase  = ArticleDataBase.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recycler_view,container,false);
        initRefreshView(rootView);
        initAdapter();
        mSwipeRefreshLayout.setRefreshing(true);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        //从数据库加载文章
        mArticleList.addAll(mArticleDataBase.loadArticleBasic());
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 初始化
     * @param view
     */
    private void initRefreshView(View view){
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //获取文章
                getArticle(1);
            }
        });

        mRecyclerView = (AutoLoadRecyclerView) view.findViewById(R.id.article_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //通过getActivity()获取Activity实例
        mRecyclerView.setLayoutManager(linearLayoutManager);
        //添加分割线
        mRecyclerView.addItemDecoration(new RecycleViewDivider(getContext(),linearLayoutManager.getOrientation()));
        //设置固定大小
        mRecyclerView.setHasFixedSize(true);
        //设置可见性
        mRecyclerView.setVisibility(View.VISIBLE);
        //设置加载监听器
        mRecyclerView.setOnLoadListener(new OnLoadListener() {
            @Override
            public void onLoad() {
                mSwipeRefreshLayout.setRefreshing(true);
                //加载文章
                getArticle(mPageIndex);
            }
        });
    }

    /**
     * 初始化Adapter
     */
    private void initAdapter(){
        mAdapter = new ArticleAdapter(mArticleList);
        mAdapter.setOnItemClickListener(new OnItemClickListener<Article>() {
            @Override
            public void click(Article item) {
                if (item != null){
                    //调整到详情页面查看文章内容
                    showArticleDetail(item);
                }
            }
        });
        //设置Adapter
        mRecyclerView.setAdapter(mAdapter);
        //从网络获取数据
        getArticle(1);
    }

    /**
     * 设置种类
     * @param category
     */
    public void setmArticleCategory(int category){
        mCategory = category;
    }

    /**
     * 获取文章列表
     * @param page
     */
    private void getArticle(final int page){

        new AsyncTask<Void,Void,List<Article>>(){

            @Override
            protected void onPreExecute() {
                //显示刷新进度条
                mSwipeRefreshLayout.setRefreshing(true);
            }

            @Override
            protected List<Article> doInBackground(Void... params) {
                return performRequest(page);
            }

            @Override
            protected void onPostExecute(List<Article> articles) {
                //移除已更新的数据
                articles.removeAll(mArticleList);
                //添加新数据
                mArticleList.addAll(articles);
                //通知Adapter数据发生改变
                mAdapter.notifyDataSetChanged();
                //关闭刷新进度条
                mSwipeRefreshLayout.setRefreshing(false);
                //存储文章列表到数据库
                mArticleDataBase.saveArticleBasic(articles);

                if (articles.size() > 0){
                    mPageIndex++;
                }
            }

        }.execute();
    }

    private List<Article> performRequest(int page){
        HttpURLConnection httpURLConnection = null;
        String url =  "http://www.devtf.cn/api/v1/?type=articles&page=" + mPageIndex
                + "&count=20&category=1";
        try {
            httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
            httpURLConnection.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null){
                builder.append(line + "\n");
            }
            String result = builder.toString();
            //解析为文章列表
            return parse(new JSONArray(result));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            httpURLConnection.disconnect();
        }
        return new ArrayList<>();
    }

    /**
     * 解析JSONArray 为List<Article>
     * @param jsonArray
     * @return
     */
    private List<Article> parse(JSONArray jsonArray){
        List<Article> articleList = new ArrayList<>();
        int count = jsonArray.length();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        for (int i = 0;i < count; i++){
            JSONObject object = jsonArray.optJSONObject(i);
            Article article = new Article();
            article.setTitle(object.optString("title"));
            article.setAuthor(object.optString("author"));
            article.setPostId(object.optString("post_id"));
            Date date = null;
            try {
                date = dateFormat.parse(object.optString("date"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            article.setPublishTime(dateFormat.format(date));
            int category = TextUtils.isEmpty(object.optString("category")) ? 0:Integer.parseInt(object.optString("category"));
            article.setCategory(category);
            articleList.add(article);
        }
        return articleList;
    }

    private void showArticleDetail(Article article){
        Intent intent = new Intent(getActivity(), ArticleDetailActivity.class);
        intent.putExtra("postId",article.getPostId());
        intent.putExtra("title",article.getTitle());
        startActivity(intent);
    }

}
