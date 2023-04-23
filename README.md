# Android实现一个简易的新闻列表APP（TabLayout+ViewPager+Fragment）

> 开发语言：Java
>
> 开发工具：Android Studio
>
> 用到的一些控件，功能：
>
> Tablayout，ViewPager（滑动切换菜单功能），Navigation，ToolBar
>
> **需要的外部包**：
>
> ```java
> import androidx.fragment.app.Fragment;
> import androidx.fragment.app.FragmentManager;
> import androidx.fragment.app.FragmentPagerAdapter;
> ```
>
> 注意！！！这里可能有的人版本是support-v4对应的包
>
> 添加了support-v4的包后发现build.gradle（Gradle Scripts文件夹下）有报错，
>
> 报错内容类似`Version 28 (intended for Android Pie and below) is the last version of the legacy support library`
>
> 这是因为当前的AS已经不支持这些旧的包，因此可以将support-v4迁移到AndroidX，具体方法参考下面的文章或者自行搜索：
>
> [(174条消息) Version 28 (intended for Android Pie and below) is the last version of the legacy support library_谷哥的小弟的博客-CSDN博客](https://blog.csdn.net/lfdfhl/article/details/105269551)
>
> 代码的源码仓库地址为
>
> [Cheesheep/AndroidExp2-News-App at master (github.com)](https://github.com/Cheesheep/AndroidExp2-News-App/tree/master)

## 1.实现内容以及效果

先放出最终的**实现效果**：

- 首页是国际新闻列表，总共有四个菜单

  ![image-20230422151217835](https://gitee.com/cheesheep/typora-photo-bed/raw/master/Timg/image-20230422151217835.png)

- 可以通过**滑动或点击**切换到其他菜单，可以看到不同页面的列表有一定的差异

  ![image-20230422151529248](https://gitee.com/cheesheep/typora-photo-bed/raw/master/Timg/image-20230422151529248.png)

- 点击查看其中一篇文章，可以看到本地是加载了一篇纯文本的内容

  ![image-20230422151800242](https://gitee.com/cheesheep/typora-photo-bed/raw/master/Timg/image-20230422151800242.png)

- 点击右上角的菜单列表，有另外两种查看该文章的方式，选择**阅读原文**

  ![image-20230422151907617](https://gitee.com/cheesheep/typora-photo-bed/raw/master/Timg/image-20230422151907617.png)

  接着在app内显示出原文所在的网页

  ![image-20230422152926285](https://gitee.com/cheesheep/typora-photo-bed/raw/master/Timg/image-20230422152926285.png)

  再次点击**右上角菜单**可以选择重新显示本地的文章

  如果选择浏览器打开，就会**跳转到浏览器**并且显示该网页

- 回到主页面，左划或者点击左上角即可显示滑动菜单栏

  ![image-20230422153049656](https://gitee.com/cheesheep/typora-photo-bed/raw/master/Timg/image-20230422153049656.png)

  打开后这里放了一些个人页面的信息

  ![联想截图_20230422153131](https://gitee.com/cheesheep/typora-photo-bed/raw/master/Timg/%E8%81%94%E6%83%B3%E6%88%AA%E5%9B%BE_20230422153131.png)

  不过菜单上面的功能都没有实现，只是做了样式，包括顶部的分享和个人页面按钮也是

## 2. 实现代码

### 2.1 顶部菜单栏以及侧滑栏实现（Toolbar）

> 顶部菜单栏的效果如图
>
> ![image-20230422164518062](https://gitee.com/cheesheep/typora-photo-bed/raw/master/Timg/image-20230422164518062.png)
>
> 涉及到的配置文件：
>
> - activity_main.xml ：主活动页面
>
> - toolbar_menu：菜单栏图标（右侧的分享和个人页面的logo）
>
> - nav_header,xml ：侧滑栏的头部信息（头像，昵称，联系方式）
>
> - left_drawer.xml：侧滑栏的菜单内容
>
>   ![联想截图_20230422153131](https://gitee.com/cheesheep/typora-photo-bed/raw/master/Timg/联想截图_20230422153131.png)
>
>   侧滑栏如上
>
> - Java活动文件：
>
>   **MainActivity.java** :全部逻辑功能代码都在该活动当中





#### 2.1.1 导入导航栏依赖

导入”NavigationView“的依赖

~~~xml
implementation 'com.android.support:design:29.0.1'
~~~

#### 2.1.2 配置toolbar菜单栏布局文件

关于toolbar的各种设置，可以参考以下文章

[(174条消息) Android Toolbar的使用详解_暗恋花香的博客-CSDN博客](https://blog.csdn.net/qq_42324086/article/details/117390236)

**设置为NoActionBar**：

要使用toolbar，首先要去掉系统默认设置的ActionBar

在AndroidManifest.xml文件当中可以设置活动的主题，我们新建一个NoActionBar的主题，并且给我们需要配置toolBar的页面赋予这个主题即可

**AndroidManifest.xml**

![image-20230422162635485](https://gitee.com/cheesheep/typora-photo-bed/raw/master/Timg/image-20230422162635485.png)

**themes.xml**

![image-20230422164201720](https://gitee.com/cheesheep/typora-photo-bed/raw/master/Timg/image-20230422164201720.png)

最右边是noActionBar，内容设置了toolbar的背景颜色等等

**配置toolBar**

需要在`activity_main.xml`文件当中放入toolBar，因为是在首页主活动放置toolBar

**activity_main.xml部分代码**

```xml
<!--    顶部菜单栏toolBar-->
    <androidx.appcompat.widget.Toolbar
        android:id="@+id/my_toolbar"
        android:layout_width="match_parent"
        android:layout_height="?attr/actionBarSize"
        android:background="?attr/colorPrimary"
        app:titleTextColor="@color/white"
        android:elevation="4dp"
        android:theme="@style/ThemeOverlay.AppCompat.ActionBar"
        app:popupTheme="@style/ThemeOverlay.AppCompat.Light"/>
```

除了在活动布局文件中放置toolbar，还需要一个菜单图标文件

**toolbar_menu.xml**

```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">
    <item
        android:id="@+id/tool_msg"
        android:icon="@drawable/share"
        android:orderInCategory="80"
        android:title="edit"
        app:showAsAction="ifRoom|withText" />
    <item
        android:id="@+id/tool_user"
        android:icon="@drawable/personal"
        android:orderInCategory="80"
        android:title="share"
        app:showAsAction="ifRoom|withText" />
<!--    后面有更多的item如果放不下就会进入菜单栏-->
</menu>
```

这里的配置和actionBar的menu绘制方法类似，唯一区别就是`app:showAsAction`不同，该属性是Toolbar当中很关键的属性

其中四个不同的值的作用分别如下：

1）always：这个值会使菜单项一直显示在 ToolBar上。
2）ifRoom：如果有足够的空间，这个值会使菜单项显示在 Tool Bar上。
3）never：这个值会使菜单项永远都不出现在 ToolBar上。
4）withText：这个值会使菜单项和它的图标、菜单文本一起显示。一般和ifRoom一起通过“|”使用
app:showAsAction 属性值为 ifRoom|withText，表示如果有空间，那么就连同文字一起显示在标题栏中，否则就显示在菜单栏中。
而当app:showAsAction 属性值为 never时，该项作用为Menu不显示在菜单组件中。

这个文件后面我们会在**activity的Java代码**当中写入并且连接上

**MainActivity.java**

```java
myToolbar.inflateMenu(R.menu.toolbar_menu);
```



#### 2.1.3 配置侧滑栏的xml布局文件

添加NavigationView组件

（要使用该组件需要从外部导入design包，在前面有提到导入的具体的包）

将该组件添加到toolbar所在的活动的页面当中，这里就是activity_main.xml

**activity_main.xml部分代码**

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.drawerlayout.widget.DrawerLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/drawer_layout"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    --------
    这里是该页面的其他布局文件，可以将整个布局文件代码放进去也是可以的
    
    --------
    
<!--左侧导航菜单-->
<com.google.android.material.navigation.NavigationView
    android:id="@+id/nav_view"
    android:layout_width="wrap_content"
    android:layout_height="match_parent"
    android:layout_gravity="start"
    android:background="@color/colorPrimary"
    app:headerLayout="@layout/nav_header"
    app:menu="@menu/left_drawer" />
</androidx.drawerlayout.widget.DrawerLayout>
```

这个菜单栏由于是侧滑出现的，所以放在整个布局的最外面，并且需要用DrawerLayout包裹

注意看下面两个app的属性`app:headerLayout`和`app:menu`

除此之外需要新建两个文件，一个是头部的代码，一个是菜单的部分

**nav_header.xml**

这里代码就不全部放出来了，给出效果

![image-20230422181842842](https://gitee.com/cheesheep/typora-photo-bed/raw/master/Timg/image-20230422181842842.png)

**left_drawer.xml**

```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:android="http://schemas.android.com/apk/res/android">
        <item
            android:id="@+id/navigation_item_user"
            android:title="用户中心" />
        <item
            android:id="@+id/navigation_item_setting"
            android:title="用户设置" />
        <item
            android:id="@+id/navigation_item_about"
            android:title="关于我们" />
        <item
            android:id="@+id/navigation_item_logout"
            android:title="注销" />
</menu>
```

这个是菜单栏的页面，预览的效果和最终实现的效果是**有差异的**，不需要以预览的那个效果为准。

将nav_header和left_drawer结合之后，效果图片在上面有显示。



#### 2.1.4 Java逻辑代码实现

布局文件写好后，就是在Activity活动当中去实现我们的点击事件，以及一些组件的连接，布局的设置其他等等。

这里对应的Java代码**全部都在MainActivity.java**当中

ToolBar和侧边栏的初始化都在MainActivity的`initToolBarView()`方法当中

**初始化Toolbar**：

**MainActivity.java**

```java
private void initToolBarView() {
    Toolbar myToolbar = findViewById(R.id.my_toolbar);
    drawer_layout = findViewById(R.id.drawer_layout);
    //将图标菜单文件添加到toolbar当中
    myToolbar.inflateMenu(R.menu.toolbar_menu);
    myToolbar.setTitle("Hello News");
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
```

通过`inflateMenu（）`将右侧的菜单图标添加到toolbar当中。

然后设置这些图标的点击事件，只不过这里我没有去写对应的功能，要用到的话，识别item当中的id即可。

**监听打开侧滑栏的按钮**：

```java
//配置侧滑栏，并且监听点击事件
myToolbar.setNavigationIcon(R.drawable.left_nav);
//打开侧滑栏的监听事件
myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        drawer_layout.openDrawer(GravityCompat.START);
    }
});
```

这样就可以通过点击打开侧滑栏了。

然后是侧滑栏的按钮的监听设置

```Java
//侧滑栏里面的菜单的监听事件
NavigationView mNavigationView = findViewById(R.id.nav_view);
mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //侧滑栏中菜单的点击事件
    }

});
```

这样就完成了toolbar的代码了

---

**注意事项**！：

**想让Toolbar本身的inflateMenu生效，则必须删去以下类似代码！！！**

不然会由于同时使用了ActionBar导致失效

~~~java
 //设置侧边栏
setSupportActionBar(myToolbar);
setTitle("Top News");//设置标题名称
getSupportActionBar().setDisplayHomeAsUpEnabled(true);//设置左边home键
getSupportActionBar().setHomeAsUpIndicator(R.drawable.left_nav);//更换home键样式

~~~




### 2.2 导航栏实现（TabLayout + ViewPager + Fragment）

> 导航栏使用了上述三样东西结合
>
> TabLayout：提供选项切换到不同的菜单
>
> ViewPager：用于滑动切换到不同的菜单
>
> Fragment：每个菜单显示的页面内容
>
> **涉及配置文件**：
>
> - activity_main.xml
> - selected.xml （选中菜单的颜色设置）
> - themes.xml  （设置菜单的文件大小）

#### 2.2.1 导入相关依赖

如果是旧版的话应当是导入support-v4里面的组件，这个时候可能会出现兼容问题

#### 2.2.2 界面配置文件

该菜单就是在首页展现的，因此所有布局文件也都在**activity_main.xml**当中

**Tablayout+ViewPager控件**：

```xml
<!--顶部导航栏，切换选项卡-->
    <com.google.android.material.tabs.TabLayout
        android:id="@+id/tabLayout"
        android:layout_width="match_parent"
        android:layout_height="80dp"
        app:tabIndicatorColor="#ffffff"
        app:tabIndicatorHeight="5dp"
        app:tabTextColor="@color/white"
        app:tabIconTint="@color/white"
        app:tabSelectedTextColor="@color/white"
        app:tabMode="fixed"
        app:tabBackground="@drawable/selected"
        app:tabTextAppearance="@style/MyTabLayoutTextAppearance"
        />
<!--用于实现左右滑动效果-->
    <androidx.viewpager.widget.ViewPager
        android:id="@+id/viewPager"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"/>
```

接着要在对应的页面的活动文件当中进行代码逻辑编写：

**initViewPager()**

```java
private void initViewPager() {
    mViewPager= (ViewPager) findViewById(R.id.viewPager);
    MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
    mViewPager.setAdapter(myFragmentPagerAdapter);
}
```

初始化viewPager，这里用到了一个新的类myFragmentPagerAdapter来初始化内容

该类需要继承`FragmentPagerAdapter`，不过现在的android studio当中会显示该类已经被移除了，但是实际上还能用，不报错就行，不用管那个下划线，

**FragmentPagerAdapter.java**

```java
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private String[] mTitles = new String[]{"国际", "体育", "生活","科学"};
    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    //选择不同的菜单返回不同的Fragment页面
    @Override
    public Fragment getItem(int position) {
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
    public int getCount() {return mTitles.length;}

    //ViewPager与TabLayout绑定后，这里获取到PageTitle就是Tab的Text
    @Override
    public CharSequence getPageTitle(int position) {return mTitles[position];}
}
```

该类的代码也比较简短，根据需要的菜单数量，增加String数组元素个数和页面Fragment的数量就行

**initTabLayoutView()** 

```java
private void initTabLayoutView() {
    //将TabLayout与ViewPager绑定在一起
    TabLayout mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
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
```

初始化TabLayout的设置，先将viewPager与TabLayout**绑定**在一起，然后设定对应的位置，最后设置图标，代码比较简单。



**注意事项！：**

**其他xml文件的添加**

这里说几个tablayout和viewPager要导入的一些其他的简短的xml文件

![image-20230423002236907](https://gitee.com/cheesheep/typora-photo-bed/raw/master/Timg/image-20230423002236907.png)

在themes.xml（或styles.xml）文件当中要添加`style name = MyTabLayoutTextAppearance`的样式

```xml
<!--    TabLayout的文本大小-->
    <style name="MyTabLayoutTextAppearance" parent="TextAppearance.AppCompat.Widget.ActionBar.Title">
        <item name="android:textSize">18sp</item>
        <item name="android:textColor">@color/white</item>
    </style>
```

还有在drawable文件下的 selected.xml，用于展示被选中下菜单的颜色

```xml
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:state_selected="true" android:drawable="@color/TabBackGround"/>
    <item android:drawable="@color/colorAccent"/>
</selector>
```



### 2.3 新闻列表实现

> **涉及配置文件**：
>
> - fragment_global_news.xml 以及类似的四个文件（都是只有一个ListView）
> - listView_item.xml以及相关的三种卡片布局
>
> **Java文件**：
>
> - GlobalNewsFragment.java （新闻列表的显示页面）
> - NewsUtils.java （存放新闻的数据，例如标题，内容，url）
> - NewsBean.java 新闻类（含标题，内容等）

由于四个新闻列表页面的布局是很像的，所以这里只拿**其中一个**举例

菜单的布局实现后，就要说下具体怎么实现新闻的列表内容了。

这里也是相对复杂一点的内容，因为该Fragment的代码内容比较多

但是布局文件xml是比较少的，或者说都是相似的

#### 2.3.1 布局配置文件

首先是Fragment碎片的布局

很简单，只有一个ListView在里面

**fragment_global_news.xml**

```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".newsListPage.GlobalNewsFragment">
    <ListView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:id="@+id/globalList"
        />
</FrameLayout>
```

相对的其他的碎片文件也是一样

接着是list里面每个的样式该怎么实现呢？

这就要我们自己去涉及一个布局样式

**listview_item.xml**

这里代码较长，就只给出一个样式结合以及图片了

![image-20230423003925586](https://gitee.com/cheesheep/typora-photo-bed/raw/master/Timg/image-20230423003925586.png)

其他的样式例如有

![image-20230423004808391](https://gitee.com/cheesheep/typora-photo-bed/raw/master/Timg/image-20230423004808391.png)



#### 2.3.2 Java代码实现listView与自定义新闻卡片结合

接下来是将listView与自定义的卡片xml文件结合到一起

最终效果如下：

![image-20230423005006208](https://gitee.com/cheesheep/typora-photo-bed/raw/master/Timg/image-20230423005006208.png)



**GlobalNewsFragment.java** ：

首先看看代码初始化的内容

```java
public class GlobalNewsFragment extends Fragment {
    private ListView lv;
    private ArrayList<NewsBean> mList;
    private View globalView;
    private MainActivity mainActivity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 获取view，并且连接到主活动上面
        globalView = inflater.inflate(R.layout.fragment_global_news, container, false);
        mainActivity =(MainActivity) getActivity();
        initUI();
        initData();
        initAdapter();
        return globalView;
    }
```

只需要一个onCreateVIew（）是Fragment类当中的函数重载，其他的函数都是自己写出来的了。

初始化globalVIew的时候，获取了上面写好的配置文件`fragment_global_news.xml` 。



介绍三个初始化的函数内容

- **initUI（）**：

  监听列表每个item的点击事件

  ```java
  private void initUI() {
      lv = (ListView) globalView.findViewById(R.id.globalList);
      //设置点击事件监听
      lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              //跳转到显示文章内容的活动
              NewsBean bean = mList.get(position); 		         NewsArticleContentActivity.actionStart(mainActivity,bean.title,bean.news_content,bean.news_url);
          }
      });
  }
  ```

  这里就是对每个列表的新闻卡片做了一个点击监听的事件，通过**Intent**跳转到文章的详情页面，经典的活动之间的跳转方法，不阐述了。

  listView的获取连接了之前写的xml文件`globalList.xml`当中

  该意图已经封装好一个方法，只需要传固定的参数即可，分别是标题，内容，还有url链接。

  文章的详情页面用一个Activity来实现，下一小节会讲到。

- **initData（）**：

  ```java
  private void initData() {
      mList = NewsUtils.getGlobalNews(mainActivity);
  }
  ```

  这里只调用了一个函数。

  功能是将数据存到mList当中，方便后面用适配器Adapter调用

  来看下**NewsUtils**类里面是怎么获取数据的

  ![image-20230423010309674](https://gitee.com/cheesheep/typora-photo-bed/raw/master/Timg/image-20230423010309674.png)

  代码内容是比较简单直接的，虽然这里可以使用数据库的方式会更高效。

  MewsBean里面的内容也只有简单的几个成员数据而已。

  **NewsBean**：

  ```java
  public class NewsBean {
      public String title;
      public String des;
      public Drawable icon;
      public String news_url;
      public String news_content;
  }
  ```

  

- **initAdapter（）**

  一行代码

  ```java
  private void initAdapter() {
      lv.setAdapter(new GlobalNewsFragment.NewsAdapter());
  }
  ```

  setAdapter（）方法是官方提供的，而里面的NewsAdapter（）则需要我们去自定义实现

  **NewsAdpter类**：

  ```java
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
          GlobalNewsFragment.ViewHolder holder;
          if (convertView == null) {//获取卡片的具体内容
              holder = new GlobalNewsFragment.ViewHolder();
              convertView = View.inflate(mainActivity.getApplicationContext(), R.layout.listview_item, null);
              holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
              holder.tv_des = (TextView) convertView.findViewById(R.id.tv_des);
              holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
              convertView.setTag(holder);
          } else {
              holder = (GlobalNewsFragment.ViewHolder) convertView.getTag();
          }
          NewsBean item = getItem(position);
          holder.tv_title.setText(item.title);
          holder.tv_des.setText(item.des);
          holder.iv_icon.setImageDrawable(item.icon);
          return convertView;
      }
  }
  ```

  该类继承了BaseAdapter，并对四个基本的方法进行了重载。

  前面的三个都比较简单，根据名字就可以知道该方法调用的是一些基本的参数，例如获得列表当中的item等等。

  最后的getView（）比较长，主要是将数据**传递到view**布局上面

  该方法可以看到调用了`listview_item.xml`文件，想要不同的**卡片样式**，这里可以选中不同的布局文件。

  ![image-20230423011751041](https://gitee.com/cheesheep/typora-photo-bed/raw/master/Timg/image-20230423011751041.png)

  holder类（直接定义在当前的Fragment类里面就行）

  ```java
  private static class ViewHolder {
      TextView tv_title;
      TextView tv_des;
      ImageView iv_icon;
  }
  ```



至此就完成了新闻列表样式的渲染和点击事件了。



### 2.4 新闻具体内容实现

> **相关配置文件**：
>
> - activity_news_article_content.xml
> - article_open_browser.xml （设置右上角菜单内容）
>
> **java代码**：
>
> - NewsArticleContentActivity.java

该页面对应一个Activity，实现效果如下：

![image-20230423012206793](https://gitee.com/cheesheep/typora-photo-bed/raw/master/Timg/image-20230423012206793.png)

点击阅读原文，实际上是将当前页面的标题和内容**隐藏**，然后将webView显示出来。

#### 2.4.1 布局配置文件

根据页面内容可以看出，活动文件的内容应当有标题，内容，webView。

**activity_news_article_content.xml**

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context=".NewsArticleContentActivity">
    <ScrollView
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:orientation="vertical">
<!--标题-->
            <TextView
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:gravity="center"
                android:textSize="30sp"
                android:fontFamily="sans-serif-black"
                android:id="@+id/LocalTitle"/>
<!--下划线-->
            <View
                android:layout_width="match_parent"
                android:layout_height="1dp"
                android:background="@color/cardview_dark_background"
                />
<!--文章内容-->
            <TextView
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:textSize="18sp"
                android:id="@+id/LocalContent"/>
<!-- 嵌入网页内容-->
            <WebView
                android:id="@+id/news_webView"
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:visibility="gone"
                />
        </LinearLayout>
    </ScrollView>
<!--    先隐藏嵌入的网页-->
</LinearLayout>
```

这里要用到**scorllView**来避免由于文章内容过多而页面无法下拉的问题。

一开始的WebView设置是**不可见gone**的，在运行后经过点击事件才能让其可见。

**标题栏**：

直接使用默认给出的actionBar

~~~xml
<style name="Theme.NEWSPage" parent="Theme.MaterialComponents.DayNight.DarkActionBar">
~~~

**标题当中的菜单栏**

**article_open_browser.xml** 

```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android" >
    <item
        android:id="@+id/article_open_webView"
        android:title="阅读原文"/>
    <item
        android:id="@+id/article_open_browser"
        android:title="用浏览器打开"/>
</menu>
```

![联想截图_20230423013518](https://gitee.com/cheesheep/typora-photo-bed/raw/master/Timg/%E8%81%94%E6%83%B3%E6%88%AA%E5%9B%BE_20230423013518.png)



#### 2.4.2 Java代码实现

相关代码都在**NewsArticleContentActivity.java**当中实现

首先看看初始化这个活动的时候用到的代码

```java
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
```

这里都是对`activity_news_article_content.xml`布局的内容渲染。

代码分为三个步骤

1. 通过`setHomeBUttonEnabled（）`和`setDisplayHomeAsUpEnabled（）`来设置顶部菜单栏左边的返回按钮。
2. 用`getIntent（）`获取传过来的文章标题，内容和链接urls，并且给控件的内容设置相应的文本
3. 设置WebView，加载Url链接到WebView控件上面。（这里还需要去AndroidManifest当中设置一下uses-permission和usesClearText）

关于getIntent获得的数据，我在NewsArticleContentActivity类当中写了一个`actionStart（）`方法作为接口来接收Intent，提供给外部想要进入这个活动并且传参。

```java
//提供一个传入意图的接口，用来跳转活动
public static void actionStart(Context context,String newsTitle,String newsContent,String newsUrl){
    Intent intent = new Intent(context,NewsArticleContentActivity.class);
    intent.putExtra("news_title",newsTitle);
    intent.putExtra("news_content",newsContent);
    intent.putExtra("news_url",newsUrl);
    context.startActivity(intent);
}
```

**创建右侧的顶部菜单栏**：

```java
//重写顶部菜单栏构造方法
@Override
public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.article_open_browser,menu);
    return super.onCreateOptionsMenu(menu);
}
```

这里的方法将之前写好的`article_open_browser.xml`放到menu当中

最后是重写这个菜单里面的点击触发事件，代码如下

```java
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
```

分别对返回键，显示网页内容，和跳转浏览器三个按键做了点击响应

对应下图的三个图标：

![联想截图_20230423091235](https://gitee.com/cheesheep/typora-photo-bed/raw/master/Timg/%E8%81%94%E6%83%B3%E6%88%AA%E5%9B%BE_20230423091235.png)

至此就完成了新闻文章详情页面的所有功能了。



**注意事项！！：**

webview无法正常打开的原因（这里指已经设置了uses-permission的情况）：

可以参考这篇博客文章，完美解决问题

[(174条消息) WebView出现net:ERR_CLEARTEXT_NOT_PERMITTED和net::ERR_UNKNOWN_URL_SCHEME错误的解决办法_飞鸭传书的博客-CSDN博客](https://blog.csdn.net/jppipai/article/details/124433198)

主要原因有例如现在的版本不支持http格式的协议，因此需要在AndroidManifest.xml当中新添加一行

```xml
<!--    用于允许网络连接-->
<uses-permission android:name="android.permission.INTERNET"/>
<application
   ........其他设置
    android:usesCleartextTraffic="true"
   ......其他设置
    >
```

接着会出现网页能够加载一下，然后就又发生了错误，这次提示`net::ERR_UNKNOWN_URL_SCHEME错误`,这里是重定向的问题

解决方法是重写 `shouldOverrideUrlLoading(WebView view, String url)` 方法

```
webView.setWebViewClient(new WebViewClient(){
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return false;
    }
});
```

- `return true` 表示当前url即使是重定向url也不会再执行（除了在return true之前使用webview.loadUrl(url)除外，因为这个会重新加载）

- `return false` 表示由系统执行url，直到不再执行此方法，即加载完重定向的ur（即具体的url，不再有重定向）。

或者采用上面博客给出的更加多判断的方法，可以对更多类型的页面的重定向进行判断



---

## 3. 总结

最后来说一下总结吧

本次可以说是安卓课上第一次做一个比较像App的实验了，之前的几次实验和作业也只是实现了一些简单的样式或者功能，而这次不仅要实现功能，UI也要进行一定的设计和美化。

个人认为工作量还是比较大的了，花费的时间也比较多，虽然最后做出来的功能其实并没有很多，但是因为是第一次做这种体量的安卓项目。

相比于以往接触到的小程序开发，web开发，android给人最大的不同就是需要配置大量的布局文件。在android当中，并不需要编写像html，css这样的文件去渲染页面，而是需要用xml文件来展现布局的页面内容，也并没有JavaScript这样的语言去对前端的样式，数据逻辑进行处理，而是靠Java语言来编写一个叫做活动（Activity）的东西，将数据连接到一起。

因此最初接触安卓的时候会发现这种编程方式非常地陌生，而且很不习惯，因为感觉需要处理的文件很多很杂，有时候要花好一会去想，我这个控件在哪里进行了导入，在哪里进行了设置，因为有可能是在Java文件当中进行初始化的，也有可能在xml文件当中已经提前写好了。

这次学到的新内容也非常地多，像之前了解到的也只是《第一行安卓》上的内容，例如活动、碎片、webView的使用等等，而通过这次的实验，不仅对android的开发更加熟悉，也学到了例如用Toolbar做菜单工具栏，用viewPager，Tablayout做多个菜单的切换等等，不过仍有很多需要学习改进的地方，例如这次的本地文章存储可以使用数据库来存取，而不是直接写在Java代码当中，还有如何实现文章的图片文字的穿插等等，都是需要进一步了解并且去学习的。