package com.techfrontier.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.techfrontier.R;
import com.techfrontier.database.ArticleDataBase;
import com.techfrontier.model.ArticleDetail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by qiyei on 2016/6/3.
 */
public class ArticleDetailActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ProgressBar mProgressBar;
    private WebView mWebView;
    private String postId;
    private String mTitle;
    private String mJobUrl;
    private ArticleDataBase mArticleDataBase = ArticleDataBase.getInstance();

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_detail);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mProgressBar = (ProgressBar) findViewById(R.id.loading_progress_bar);
        mWebView = (WebView) findViewById(R.id.article_webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                WebSettings settings = mWebView.getSettings();
                settings.setBuiltInZoomControls(true);
                view.loadUrl(url);
                return true;
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mProgressBar.setVisibility(View.VISIBLE);
                mProgressBar.setProgress(newProgress);
                if (newProgress == 100){
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        });

        //设置标题
        mToolbar.setTitle(getResources().getString(R.string.app_name));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToPreAcrivity();
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && !bundle.containsKey("job_url")){
            postId = bundle.getString("postId");
            mTitle = bundle.getString("title");
        } else {
            mJobUrl = bundle.getString("job_url");
        }

        ArticleDetail cacheDetail = mArticleDataBase.loadArticleDetail(postId);
        if(!TextUtils.isEmpty(cacheDetail.getContent())){
            loadArticle2Webview(cacheDetail.getContent());
        } else if (!TextUtils.isEmpty(postId)){
            getArticleContent();
        } else {
            mWebView.loadUrl(mJobUrl);
        }

    }

    /**
     * 返回上一个
     */
    private void backToPreAcrivity(){
        Intent intent = new Intent(ArticleDetailActivity.this,MainActivity.class);
        startActivity(intent);
    }

    /**
     *
     */
    private void getArticleContent(){

        new AsyncTask<Void,Void,String>(){
            @Override
            protected void onPreExecute() {

            }

            @Override
            protected String doInBackground(Void... params) {
                HttpURLConnection httpURLConnection = null;
                String url =  "http://www.devtf.cn/api/v1/?type=article&post_id=" + postId;
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
                    return result;
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    httpURLConnection.disconnect();
                }

                return "";
            }

            @Override
            protected void onPostExecute(String s) {
                //显示到WebView上
                loadArticle2Webview(s);
                //存储到数据库
                mArticleDataBase.saveArticleDetail(new ArticleDetail(postId,s));
            }

        }.execute();

    }

    /**
     *
     */
    private void loadArticle2Webview(String content){
        mWebView.loadDataWithBaseURL("",wrapHtml(mTitle,content),"text/html","utf8","404");
    }

    /**
     * 包装返回的文章内容，加上一些CSS等
     * @param title
     * @param content
     * @return
     */
    private static String wrapHtml(String title,String content){
        final StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>");
        sb.append("<html dir=\"ltr\" lang=\"zh\">");
        sb.append("<head>");
        sb.append("<meta name=\"viewport\" content=\"width=100%; initial-scale=1.0; maximum-scale=1.0; user-scalable=0;\" />");
        sb.append("<link rel=\"stylesheet\" href='file:///android_asset/style.css' type=\"text/css\" media=\"screen\" />");
        sb.append("<link rel=\"stylesheet\" href='file:///android_asset/default.min.css' type=\"text/css\" media=\"screen\" />");
        sb.append("</head>");
        sb.append("<body style=\"padding:0px 8px 8px 8px;\">");
        sb.append("<div id=\"pagewrapper\">");
        sb.append("<div id=\"mainwrapper\" class=\"clearfix\">");
        sb.append("<div id=\"maincontent\">");
        sb.append("<div class=\"post\">");
        sb.append("<div class=\"posthit\">");
        sb.append("<div class=\"postinfo\">");
        sb.append("<h2 class=\"thetitle\">");
        sb.append("<a>");
        sb.append(title);
        sb.append("</a>");
        sb.append("</h2>");
        sb.append("<hr/>");
        sb.append("</div>");
        sb.append("<div class=\"entry\">");
        sb.append(content);
        sb.append("</div>");
        sb.append("</div>");
        sb.append("</div>");
        sb.append("</div>");
        sb.append("</div>");
        sb.append("</div>");
        sb.append("<script src=\'file:///android_asset/highlight.pack.js\'></script>");
        sb.append("<script>hljs.initHighlightingOnLoad();</script>");
        sb.append("</body>");
        sb.append("</html>");
        return sb.toString();
    }

}
