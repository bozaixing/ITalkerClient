package com.bozaixing.italker.helper;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;

/*
 * Descr:   主页底部导航的辅助操作类
 *          解决对Fragment碎片的调度和重用问题，达到最优的Fragment的切换
 * Author:  bozaixing.
 * Date:    2017-06-07.
 * Email:   654152983@qq.com.
 */
public class NavFragmentHelper<T> {

    // 创建储存所有Tab菜单的轻量级的集合对象
    private SparseArray<Tab<T>> mTabs = new SparseArray<Tab<T>>();

    // 上下文对象
    private final Context mContext;

    // 存放Fragment碎片的容器的ID
    private final int mContainerId;
    // 碎片管理对象
    private final FragmentManager mFragmentManager;

    // 创建切换Tab菜单的回调监听接口的对象
    private OnTabChangeListener<T> mOnTabChangeListener;

    // 当前选中的Tab菜单对象
    private Tab<T> mCurrentTab;


    /**
     * 构造方法，初始化相关数据
     * @param context
     * @param containerId
     * @param fragmentManager
     * @param onTabChangeListener
     */
    public NavFragmentHelper(Context context,
                             int containerId,
                             FragmentManager fragmentManager,
                             OnTabChangeListener<T> onTabChangeListener ){

        this.mContext = context;
        this.mContainerId = containerId;
        this.mFragmentManager = fragmentManager;
        this.mOnTabChangeListener = onTabChangeListener;
    }


    /**
     * 添加Tab菜单
     * @param menuId    Tab对应的菜单ID
     * @param tab       需要添加的Tab对象
     * @return
     */
    public NavFragmentHelper<T> addTab(int menuId, Tab<T> tab){

        mTabs.put(menuId, tab);

        return this;
    }


    /**
     * 获取当前点击的Tab菜单
     * @return
     */
    public Tab<T> getCurrentTab(){
        return mCurrentTab;
    }


    /**
     * 执行底部导航栏选择Tab菜单的操作
     * @param menuId    当前点击的菜单ID
     * @return          是否能够处理这个点击，返回true表示能处理，返回false表示不能处理
     */
    public boolean performClickMenu(int menuId){

        // 去集合中寻找当前选中的Tab菜单
        // 如果有则表示我们自己能够进行处理
        Tab<T> newTab = mTabs.get(menuId);
        if (newTab != null){
            // 进行真实的Tab选择操作
            doSelect(newTab);

            return true;
        }

        return false;
    }

    /**
     * 真实的Tab选择操作
     * @param newTab    选择的Tab对象
     */
    private void doSelect(Tab<T> newTab){
        // 上一次选择的Tab对象
        Tab<T> oldTab = null;

        // 如果当前选择的Tab菜单不为空则对上一次选择的Tab进行赋值操作
        if (mCurrentTab != null){
            oldTab = mCurrentTab;

            if (oldTab == newTab){
                // 如果当前选择的Tab和上次选择的Tab菜单相同
                // 则表示点击了两次（二次选中），那么我们不做处理
                notifyTabReselect(newTab);
                return;
            }
        }

        // 赋值并调用Tab的切换方法
        mCurrentTab = newTab;
        doTabChanged(oldTab, newTab);
    }

    /**
     * 实现选中Tab的切换（对Fragment碎片对象进行真实调度的方法）
     * @param oldTab
     * @param newTab
     */
    private void doTabChanged(Tab<T> oldTab, Tab<T> newTab){
        // 获取Fragment碎片事务操作的对象
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        if (oldTab != null){
            // 如果oldTab内部缓存的fragment对象不为空，则进行移除操作
            if (oldTab.fragment != null){
                // 从界面移除，但是实际还在FragmentManager的缓存空间中
                fragmentTransaction.detach(oldTab.fragment);
            }
        }

        if (newTab != null){
            if (newTab.fragment == null){
                // 创建一个新的Frgment对象（首次新建）
                Fragment newFragment = Fragment.instantiate(mContext, newTab.clazz.getName(), null);
                // 在Tab菜单对象中缓存起来
                newTab.fragment = newFragment;
                // 添加到碎片管理对象中（使用class.getName作为Tag）
                fragmentTransaction.add(mContainerId, newFragment, newTab.clazz.getName());
            }else {
                // 并不需要重新创建，从FragmentManager的缓存空间中重新加载显示在界面中
                fragmentTransaction.attach(newTab.fragment);
            }
        }
        // 提交事务
        fragmentTransaction.commit();

        // 通知界面我们已经切换，已经刷新完成
        notifyTabSelect(oldTab, newTab);

    }



    /**
     * 二次选择Tab所做的操作
     * @param tab
     */
    private void notifyTabReselect(Tab<T> tab){

    }


    /**
     * 切换完成后通知外部类回调的方法（回调监听器）
     * @param oldTab
     * @param newTab
     */
    private void notifyTabSelect(Tab<T> oldTab, Tab<T> newTab){

        // 如果切换Tab菜单的回调监听对象不为空
        if (mOnTabChangeListener != null){
            mOnTabChangeListener.onTabChanged(oldTab, newTab);
        }
    }


    /**
     * 所有的Tab菜单的基础属性的定义,使用关键字static修饰是为了避免与FragmentHelper循环使用
     * @param <T>   泛型类型的额外参数
     */
    public static class Tab<T> {

        // 当前菜单对应的Fragment碎片对象
        private Class<? extends Fragment> clazz;
        // 定义一个额外的字段，用户自己设定需要使用什么东西
        private T extra;
        // FragmentManager内部缓存对应的Fragment
        // 包命名空间（package权限）
        Fragment fragment;


        /**
         * 构造方法
         * @param clazz
         * @param extra
         */
        public Tab(Class<? extends Fragment> clazz, T extra){
            this.clazz = clazz;
            this.extra = extra;
        }

        public Class<? extends Fragment> getClazz() {
            return clazz;
        }

        public void setClazz(Class<? extends Fragment> clazz) {
            this.clazz = clazz;
        }

        public T getExtra() {
            return extra;
        }

        public void setExtra(T extra) {
            this.extra = extra;
        }
    }


    /**
     * 定义Tab菜单切换完成后的回调监听接口
     * @param <T>   泛型数据
     */
    public interface OnTabChangeListener<T> {

        /**
         * 实现切换的回调方法
         * @param oldTab    切换之前的Tab
         * @param newTab    切换之后的Tab
         */
        void onTabChanged(Tab<T> oldTab, Tab<T> newTab);
    }


}
