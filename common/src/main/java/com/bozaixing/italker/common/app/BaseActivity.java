package com.bozaixing.italker.common.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

/*
 * Descr:   AppCompatActivity的基类
 * Author:  bozaixing.
 * Date:    2017-05-19.
 * Email:   654152983@qq.com.
 */
public abstract class BaseActivity extends AppCompatActivity {

    // 创建打印目标
    protected String TAG;
    // 创建上下文对象
    protected Context mContext;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init
        TAG = this.getClass().getSimpleName();
        mContext = this;



        // 初始化窗口
        initWindows();

        // 如果参数正确则初始化控件和数据
        if (initArgs(getIntent().getExtras())){
            setContentView(getLayoutResouceId());
            initWidgets();
            initDatas();
            setListeners();
        }else {
            // 参数错误则关闭页面
            finish();
        }

    }


    /**
     * 初始化窗口
     */
    protected void initWindows(){
    }


    /**
     * 初始化相关参数
     * @param bundle    参数Bundle
     * @return          如果参数正确返回true,参数错误返回false
     */
    protected boolean initArgs(Bundle bundle){
        return true;
    }

    /**
     * 获取布局文件的资源Id
     * @return
     */
    protected abstract int getLayoutResouceId();

    /**
     * 初始化控件
     */
    protected void initWidgets(){
    }

    /**
     * 初始化数据
     */
    protected void initDatas(){
    }

    /**
     * 为显示控件设置监听事件
     */
    protected void setListeners(){
    }


    /**
     * 点击导航栏上面的返回按钮
     * @return
     */
    @Override
    public boolean onSupportNavigateUp(){

        // 当点击界面导航返回时，关闭当前界面
        finish();
        return super.onSupportNavigateUp();
    }

    /**
     * 点击手机上面返回的物理按键
     */
    @Override
    public void onBackPressed() {
        // 得到当前Activity下的所有的Fragment碎片对象
        List<android.support.v4.app.Fragment> fragments = getSupportFragmentManager().getFragments();
        // 判断是否为空
        if (fragments != null && fragments.size() > 0){
            for (Fragment fragment : fragments){
                // 判断是否为我们能够处理的Fragment类型
                if (fragment instanceof BaseFragment){
                    // 判断是否拦截了返回按钮，返回true表示fragment自己处理
                    if (((BaseFragment) fragment).onBackPressed()){
                        // 如果有直接return，当前Activity不作任何处理
                        return;
                    }
                }
            }
        }

        // 关闭页面
        finish();

    }
}
