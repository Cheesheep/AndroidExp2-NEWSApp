package com.example.newsapppage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;



public class NewsArticleContentActivity extends AppCompatActivity {
    private String articleUrl = null;
    private TextView title;
    private TextView content;
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_article_content);
        //设置标题栏内容
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            setTitle("Article");
            actionBar.setHomeButtonEnabled(true);//设置左上角是否可以点击
            actionBar.setDisplayHomeAsUpEnabled(true);//添加返回的图标
        }
        //设置文章内容
        String newsTitle = getIntent().getStringExtra("news_title");
        String newsContent = getIntent().getStringExtra("news_content");
        articleUrl = getIntent().getStringExtra("news_url");
        title = findViewById(R.id.LocalTitle);
        content = findViewById(R.id.LocalContent);
        title.setText(newsTitle);
        content.setText(newsContent);
        //设置webView内容
        isArticleDisplayed = true;//从新闻列表进入文章的时候默认标志位为真
        webView = findViewById(R.id.news_webView);
        webView.getSettings().setJavaScriptEnabled(true);


        webView.setWebViewClient(new WebViewClient(){
            //重写这个方法解决重定向的问题
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        webView.loadUrl(articleUrl);
    }
    //重写顶部菜单栏构造方法

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.article_open_browser,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //重写顶部菜单栏点击设置
    private static boolean isArticleDisplayed;
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //设置返回按钮事件
                finish();
                return true;
            case R.id.article_open_browser:
                //设置浏览器打开事件
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(articleUrl));//跳转到网页
                startActivity(intent);
                return true;
            case R.id.article_open_webView:
                //设置显示的显示网页内容
                if(isArticleDisplayed){
                    title.setVisibility(View.GONE);
                    content.setVisibility(View.GONE);
                    webView.setVisibility(View.VISIBLE);
                    item.setTitle("本地文章");
                }else {
                    title.setVisibility(View.VISIBLE);
                    content.setVisibility(View.VISIBLE);
                    webView.setVisibility(View.GONE);
                    item.setTitle("嵌入网页");
                }
                isArticleDisplayed = !isArticleDisplayed;
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //提供一个传入意图的接口，用来跳转活动
    public static void actionStart(Context context,String newsTitle,String newsContent,String newsUrl){
        Intent intent = new Intent(context,NewsArticleContentActivity.class);
        intent.putExtra("news_title",newsTitle);
        intent.putExtra("news_content",newsContent);
        intent.putExtra("news_url",newsUrl);
        context.startActivity(intent);
    }
}