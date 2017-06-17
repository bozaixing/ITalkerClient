package com.bozaixing.italker.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.widget.ImageView;

import com.bozaixing.italker.R;
import com.bozaixing.italker.common.app.BaseActivity;
import com.bozaixing.italker.fragments.account.AccountTrigger;
import com.bozaixing.italker.fragments.account.LoginFragment;
import com.bozaixing.italker.fragments.account.RegisterFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;

import net.qiujuer.genius.ui.compat.UiCompat;

/*
 * Descr:   账户信息操作
 * Author:  bozaixing.
 * Date:    2017-06-09.
 * Email:   654152983@qq.com.
 */
public class AccountActivity extends BaseActivity implements AccountTrigger {

    /**
     * UI
     */
    private ImageView mBgImageview;



    // 当前显示的Fragment碎片对象
    private Fragment mCurrentFragment;

    // 登录显示的碎片对象
    private Fragment mLoginFragment;

    // 注册显示的碎片对象
    private Fragment mRegisterFragment;


    /**
     * AccountActivity的显示入口
     * @param context   上下文对象
     */
    public static void show(Context context){
        context.startActivity(new Intent(context, AccountActivity.class));
    }

    /**
     * 加载布局资源的ID
     * @return
     */
    @Override
    protected int getLayoutResouceId() {
        return R.layout.activity_account;
    }

    @Override
    protected void initWidgets() {
        // init Ui
        mBgImageview = (ImageView) findViewById(R.id.account_bg_view);

        // 初始化背景
        Glide.with(this)
                .load(R.drawable.bg_src_tianjin)
                .centerCrop()   // 居中剪切
                .into(new ViewTarget<ImageView, GlideDrawable>(mBgImageview) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        // 拿到glide的Drawable
                        Drawable drawable = resource.getCurrent();

                        // 使用适配类对Drawable对象进行包装
                        drawable = DrawableCompat.wrap(drawable);
                        // 进行着色，设置着色的效果和颜色，蒙版模式
                        drawable.setColorFilter(UiCompat.getColor(getResources(), R.color.colorAccent),
                                PorterDuff.Mode.SCREEN);

                        // 设置控件显示着色后的drawable对象
                        this.view.setImageDrawable(drawable);

                    }
                });


    }

    @Override
    protected void initDatas() {

        // 初始化登录碎片对象
        mCurrentFragment = mLoginFragment = new LoginFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.account_container_layout, mLoginFragment)
                .commit();

    }

    /**
     * 切换Fragment碎片的回调方法
     */
    @Override
    public void onTriggerView() {

        // 创建需要切换显示的碎片对象
        Fragment fragment;

        if (mCurrentFragment == mLoginFragment){

            if (mRegisterFragment == null){
                // 第一次切换Fragment显示碎片的时候，
                // RegisterFragment默认情况下应该为空
                mRegisterFragment = new RegisterFragment();
            }
            // 给切换后需要显示的碎片对象赋值
            fragment = mRegisterFragment;
        }else {
            // 因为默认情况下已经赋值，无需判断是否为空
            fragment = mLoginFragment;

            Log.i(TAG, "AAAAAAAAAAAAAAAAAA");
        }

        // 重新赋值当前正在显示的Fragment碎片对象
        mCurrentFragment = fragment;

        // 切换显示
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.account_container_layout, fragment)
                .commit();

    }
}
