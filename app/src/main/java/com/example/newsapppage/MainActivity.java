package com.example.newsapppage;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;



public class MainActivity extends AppCompatActivity  {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private MyFragmentPagerAdapter myFragmentPagerAdapter;

    private Toolbar myToolbar;
    private DrawerLayout drawer_layout;
    private NavigationView mNavigationView;//侧边菜单项
    private MenuItem mPreMenuItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化视图
        initViews();
    }

    private void initViews() {
        //设置首页工具栏内容以及样式
        initToolBarView();
        //将页面绑定viewPager，进行设置
        bindViewPager();
        //初始化layout的设置，例如图标，定位
        initTabLayoutView();
    }

    private void initTabLayoutView() {
        //将TabLayout与ViewPager绑定在一起
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mTabLayout.setupWithViewPager(mViewPager);

        //指定Tab的位置
        TabLayout.Tab one = mTabLayout.getTabAt(0);
        TabLayout.Tab two = mTabLayout.getTabAt(1);
        TabLayout.Tab three = mTabLayout.getTabAt(2);
        TabLayout.Tab four = mTabLayout.getTabAt(3);

        //设置Tab的图标，假如不需要则把下面的代码删去
        one.setIcon(R.drawable.global);
        two.setIcon(R.drawable.soccer);
        three.setIcon(R.drawable.life);
        four.setIcon(R.drawable.science);
    }

    private void bindViewPager() {
        //使用适配器将ViewPager与Fragment绑定在一起
        mViewPager= (ViewPager) findViewById(R.id.viewPager);
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(myFragmentPagerAdapter);
    }

    private void initToolBarView() {
        myToolbar = findViewById(R.id.my_toolbar);
        drawer_layout = findViewById(R.id.drawer_layout);
        myToolbar.inflateMenu(R.menu.toolbar_menu);
        myToolbar.setTitle("Hello News");
        myToolbar.setNavigationIcon(R.drawable.left_nav);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer_layout.openDrawer(GravityCompat.START);
            }
        });
        //ToolBar的菜单的点击事件
        myToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    default:
                        Toast.makeText(MainActivity.this, "菜单栏功能尚未开发", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });



        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            //侧滑栏中菜单的点击事件
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    default:
                        Toast.makeText(MainActivity.this, "功能尚未开发，敬请期待", Toast.LENGTH_SHORT).show();
                        break;
                }
                //关闭抽屉即关闭侧换此时已经跳转到其他界面，自然要关闭抽屉
                drawer_layout.closeDrawer(Gravity.LEFT);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                //打开侧边栏
                drawer_layout.openDrawer(GravityCompat.START);
                break;
            default:break;
        }
        return true;
    }

}
