package com.bozaixing.italker.activities;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bozaixing.italker.R;
import com.bozaixing.italker.activities.AccountActivity;
import com.bozaixing.italker.common.app.BaseActivity;
import com.bozaixing.italker.common.widgets.PortraitView;
import com.bozaixing.italker.fragments.assist.PermissionsFragment;
import com.bozaixing.italker.fragments.main.ContactFragment;
import com.bozaixing.italker.fragments.main.GroupFragment;
import com.bozaixing.italker.fragments.main.HomeFragment;
import com.bozaixing.italker.helper.NavFragmentHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.widget.FloatActionButton;


public class MainActivity extends BaseActivity
                            implements BottomNavigationView.OnNavigationItemSelectedListener,
                                        NavFragmentHelper.OnTabChangeListener<Integer>{

    /**
     * UI
     */
    private AppBarLayout mAppbarLayout;
    private PortraitView mPortraitView;
    private TextView mTitleTxt;
    private ImageView mSearchView;

    private FrameLayout mContainerLayout;

    private FloatActionButton mFloatActionBtn;

    private BottomNavigationView mBottomNavigationView;


    /**
     * Data
     */
    // 创建底部导航栏Fragment碎片辅助类的对象
    private NavFragmentHelper<Integer> mNavFragmentHelper;


    /**
     * MainActivity 显示入口
     * @param context
     */
    public static void show(Context context){
        context.startActivity(new Intent(context, MainActivity.class));
    }



    /**
     * 加载界面的资源布局ID
     * @return
     */
    @Override
    protected int getLayoutResouceId() {
        return R.layout.activity_main;
    }


    /**
     * 初始化显示控件
     */
    @Override
    protected void initWidgets() {
        // init
        mAppbarLayout = (AppBarLayout) findViewById(R.id.main_appbar_layout);

        mPortraitView = (PortraitView) findViewById(R.id.main_portrait_view);
        mTitleTxt = (TextView) findViewById(R.id.main_title_txt);
        mSearchView = (ImageView) findViewById(R.id.main_search_view);

        mContainerLayout = (FrameLayout) findViewById(R.id.main_container_layout);

        mFloatActionBtn = (FloatActionButton) findViewById(R.id.main_float_action_btn);

        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.main_bottom_navigation_view);

    }

    /**
     * 初始化数据
     */
    @Override
    protected void initDatas() {
        // 加载标题栏的背景图片
        Glide.with(mContext)
                .load(R.drawable.bg_src_morning)
                .centerCrop().into(new ViewTarget<View, GlideDrawable>(mAppbarLayout) {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                this.view.setBackground(resource.getCurrent());
            }
        });

        // 初始化底部导航栏Fragment碎片辅助类的对象
        mNavFragmentHelper = new NavFragmentHelper<>(mContext,
                                                    R.id.main_container_layout,
                                                    getSupportFragmentManager(),
                                                    this);
        // 添加Tab切换菜单，初始化Tab菜单
        mNavFragmentHelper
                .addTab(R.id.action_home, new NavFragmentHelper.Tab<Integer>(HomeFragment.class, R.string.title_home))
                .addTab(R.id.action_group, new NavFragmentHelper.Tab<Integer>(GroupFragment.class, R.string.title_group))
                .addTab(R.id.action_contact, new NavFragmentHelper.Tab<Integer>(ContactFragment.class, R.string.title_contact));

        // 为底部导航按钮设置选择监听事件
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);

        // 从底部导航栏中接管我们的Menu菜单，然后进行手动的触发第一次点击
        Menu menu = mBottomNavigationView.getMenu();
        // 手动触发操作
        menu.performIdentifierAction(R.id.action_home, 0);






    }


    @Override
    protected void setListeners() {

        // 为FloatActionBtn悬浮按钮注册点击事件
        mFloatActionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountActivity.show(mContext);
            }
        });

    }



    /**
     * 底部导航栏点击的回调处理方法（底部导航显示控件被点击的时候触发）
     * @param item
     * @return true     代表我们能够处理这个点击
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        // 转接事件流到碎片处理的辅助类中去
        return mNavFragmentHelper.performClickMenu(item.getItemId());
    }


    /**
     * NavFragmentHelper辅助操作类切换Tab菜单的回调监听方法
     * @param oldTab    切换之前的Tab
     * @param newTab    切换之后的Tab
     */
    @Override
    public void onTabChanged(NavFragmentHelper.Tab<Integer> oldTab, NavFragmentHelper.Tab<Integer> newTab) {
        // 获取Tab菜单定义的额外字段，并设置为显示的Title
        mTitleTxt.setText(newTab.getExtra());

        // 对浮动按钮进行隐藏或显示的动画
        // Home界面不需要显示，需要进行一个平移操作的隐藏动画
        // Group和Contact界面需要更改显示的背景资源

        float translateY = 0;   // y轴上面的平移动画偏移量
        float rotate = 0;       // 旋转动画偏移量


        mFloatActionBtn.getY();
        Log.i(TAG, "x====" + mFloatActionBtn.getX());
        Log.i(TAG, "x====" + mFloatActionBtn.getY());

        Log.i(TAG, "nav-y====" + mBottomNavigationView.getY());

        // 通过判断当前的显示界面来是否显示或者隐藏界面的悬浮按钮
        switch (newTab.getExtra()){
            // home界面，为隐藏状态
            case R.string.title_home:
                // 设置平移像素为layout_marginBottom+ padding= 68 + 20
                translateY = Ui.dipToPx(getResources(), 88);
                break;
            // group界面，显示状态
            case R.string.title_group:
                // 改变背景显示资源
                mFloatActionBtn.setImageResource(R.drawable.ic_group_add);
                // 旋转偏移量
                rotate = -360;
                break;
            // contact界面，显示状态
            case R.string.title_contact:
                // 改变背景显示资源
                mFloatActionBtn.setImageResource(R.drawable.ic_contact_add);
                // 旋转偏移量
                rotate = 360;
                break;
            default:
                break;
        }

        // 开始动画
        mFloatActionBtn
                .animate()
                // 旋转
                .rotation(rotate)
                // Y轴方向上平移，隐藏的实现
                .translationY(translateY)
                // 设置有弹性效果的动画插补器
                .setInterpolator(new AnticipateOvershootInterpolator(1))
                // 动画持续的时间
                .setDuration(480)
                // 启动动画
                .start();

    }
}
