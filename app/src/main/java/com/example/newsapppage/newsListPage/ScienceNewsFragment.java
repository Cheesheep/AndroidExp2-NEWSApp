package com.example.newsapppage.newsListPage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.newsapppage.MainActivity;
import com.example.newsapppage.NewsArticleContentActivity;
import com.example.newsapppage.NewsBean;
import com.example.newsapppage.NewsUtils;
import com.example.newsapppage.R;

import java.util.ArrayList;


public class ScienceNewsFragment extends Fragment {

    private ListView lv;
    private ArrayList<NewsBean> mList;
    private View ScienceView;
    private MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 获取view，并且连接到主活动上面
        ScienceView = inflater.inflate(R.layout.fragment_science_news, container, false);
        mainActivity =(MainActivity) getActivity();
        initUI();
        initData();
        initAdapter();
        return ScienceView;
    }

    private void initAdapter() {
        lv.setAdapter(new ScienceNewsFragment.NewsAdapter());
    }

    private void initData() {
        mList = NewsUtils.getScienceList(mainActivity);
    }

    private void initUI() {
        lv = (ListView) ScienceView.findViewById(R.id.scienceList);
        //设置点击事件监听
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsBean bean = mList.get(position);
                NewsArticleContentActivity.actionStart(mainActivity,bean.title,bean.news_content,bean.news_url);
            }
        });
    }
    private static int countArticle = 0;
    private class NewsAdapter extends BaseAdapter {
        //适配器处理新闻列表

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public NewsBean getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ScienceNewsFragment.ViewHolder holder;
            if (convertView == null) {//获取卡片的具体内容
                holder = new ScienceNewsFragment.ViewHolder();
                if((countArticle++ % 3) == 0)//采用多种列表展现
                    convertView = View.inflate(mainActivity.getApplicationContext(), R.layout.listview_item2, null);
                else
                    convertView = View.inflate(mainActivity.getApplicationContext(), R.layout.listview_item3, null);
                countArticle %= 12; //防止count变得过大
                holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                holder.tv_des = (TextView) convertView.findViewById(R.id.tv_des);
                holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                convertView.setTag(holder);
            } else {
                holder = (ScienceNewsFragment.ViewHolder) convertView.getTag();
            }
            NewsBean item = getItem(position);
            holder.tv_title.setText(item.title);
            holder.tv_des.setText(item.des);
            holder.iv_icon.setImageDrawable(item.icon);
            return convertView;
        }
    }

    private static class ViewHolder {
        TextView tv_title;
        TextView tv_des;
        ImageView iv_icon;
    }


}