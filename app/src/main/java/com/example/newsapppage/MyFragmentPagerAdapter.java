package com.example.newsapppage;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.newsapppage.newsListPage.GlobalNewsFragment;
import com.example.newsapppage.newsListPage.LifeNewsFragment;
import com.example.newsapppage.newsListPage.ScienceNewsFragment;
import com.example.newsapppage.newsListPage.SportNewsFragment;

/**
 * Created by Carson_Ho on 16/7/22.
 * 用于TabLayout可以点击显示不同的新闻列表页面
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private String[] mTitles = new String[]{"国际", "体育", "生活","科学"};

    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        //选择不同的菜单返回不同的Fragment页面
        if (position == 1) {
            return new SportNewsFragment();
        } else if (position == 2) {
            return new LifeNewsFragment();
        }else if (position==3){
            return new ScienceNewsFragment();
        }
        return new GlobalNewsFragment();
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    //ViewPager与TabLayout绑定后，这里获取到PageTitle就是Tab的Text
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
