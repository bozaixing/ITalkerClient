package com.bozaixing.italker.common.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/*
 * Descr:   android.support.v4.app.Fragment的基类
 * Author:  bozaixing.
 * Date:    2017-05-19.
 * Email:   654152983@qq.com.
 */
public abstract class BaseFragment extends Fragment {

    // 打印目标
    protected String TAG;

    /**
     * 布局视图显示对象
     */
    protected View mContentView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // 初始化打印目标
        TAG = this.getClass().getSimpleName();

        // 初始化参数
        initArgs(getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (mContentView == null){
            // 加载布局文件
            mContentView = inflater.inflate(getLayoutResourceId(), container, false);
            // 初始化控件
            initWidgets(mContentView);

        }else {
            if (mContentView.getParent() != null){
                // 把当前view从其父控件中移除
                ((ViewGroup) mContentView.getParent()).removeView(mContentView);
            }
        }

        return mContentView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 当View创建完成后初始化数据
        initDatas();
        setListener();
    }

    /**
     * 初始化窗口
     */
    protected void initWindows(){
    }


    /**
     * 初始化相关参数
     * @param bundle
     */
    protected void initArgs(Bundle bundle){
    }

    /**
     * 获取当前界面布局资源的Id
     * @return
     */
    protected abstract int getLayoutResourceId();


    /**
     * 初始化控件
     */
    protected void initWidgets(View view){
    }


    /**
     * 初始化数据
     */
    protected void initDatas(){
    }


    /**
     * 设置监听
     */
    protected void setListener(){
    }


    /**
     * 返回按键触发时的调用
     * @return      返回true代表我自己已经处理返回逻辑，Activity不用自己finsh();
     *              返回false表示我自己没有处理返回逻辑，Activity自己走自己的逻辑;
     *              默认返回为false
     */
    public boolean onBackPressed(){

        return false;
    }

}
