package com.techfrontier.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.techfrontier.Adapter.MenuAdapter;
import com.techfrontier.R;
import com.techfrontier.fragment.AboutFragment;
import com.techfrontier.fragment.ArticleFragment;
import com.techfrontier.listener.OnItemClickListener;
import com.techfrontier.model.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FragmentManager mFragmentManager;
    private Fragment mArticleFragment = new ArticleFragment();
    private Fragment mAboutFragment;

    private DrawerLayout mDrawerLayout;
    private RecyclerView mMenuRecyclerView;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(getResources().getString(R.string.app_name));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_left);
        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,mToolbar,R.string.open,R.string.close);

        mDrawerToggle.syncState();
        //mDrawerLayout.setDrawerListener(mDrawerToggle);//已过期
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        mMenuRecyclerView = (RecyclerView) findViewById(R.id.menu_recycler_view);
        mMenuRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<MenuItem> menuItems = new ArrayList<>();
        //添加菜单项
        menuItems.add(new MenuItem(getString(R.string.article),R.drawable.home));
        menuItems.add(new MenuItem(getString(R.string.about_menu),R.drawable.about));
        menuItems.add(new MenuItem(getString(R.string.exit),R.drawable.exit));
        MenuAdapter menuAdapter = new MenuAdapter(menuItems);
        menuAdapter.setOnItemClickListener(new OnItemClickListener<MenuItem>() {
            @Override
            public void click(MenuItem item) {
                clickMenuItem(item);
            }
        });

        mMenuRecyclerView.setAdapter(menuAdapter);
        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction().add(R.id.article_container,mArticleFragment).commitAllowingStateLoss();
    }

    //MenuAdapter的点击处理函数
    private void clickMenuItem(MenuItem menuItem){
        //关闭左侧菜单显示
        mDrawerLayout.closeDrawers();
        switch (menuItem.iconResId){
            case R.drawable.home:   //全部
                mFragmentManager.beginTransaction().replace(R.id.article_container,mArticleFragment)
                        .commit();
                break;
            case R.drawable.about:   //关于
                if (mAboutFragment == null){
                    mAboutFragment = new AboutFragment();
                }
                mFragmentManager.beginTransaction().replace(R.id.article_container,mAboutFragment)
                        .commit();
                break;
            case R.drawable.exit:   //退出
                isQuit();
                break;
            default:
                break;
        }

    }

    //退出函数
    private boolean isQuit(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("退出");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();

        return true;
    }

}
