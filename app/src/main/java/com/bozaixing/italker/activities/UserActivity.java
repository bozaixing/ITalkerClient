package com.bozaixing.italker.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.bozaixing.italker.R;
import com.bozaixing.italker.common.app.BaseActivity;
import com.bozaixing.italker.fragments.user.UpdateInfoFragment;

/*
 * Descr:   
 * Author:  bozaixing.
 * Date:    2017-06-15.
 * Email:   654152983@qq.com.
 */
public class UserActivity extends BaseActivity {

    // 当前显示的Fragment碎片对象
    private Fragment mCurrentFragment;


    /**
     * UserActivity的显示入口
     * @param context   上下文对象
     */
    public static void show(Context context){
        context.startActivity(new Intent(context, UserActivity.class));
    }


    @Override
    protected int getLayoutResouceId() {
        return R.layout.activity_user;
    }

    @Override
    protected void initWidgets() {

        mCurrentFragment = new UpdateInfoFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.user_container_layout, mCurrentFragment)
                .commit();

    }


    /**
     * 当前加载UpdateInfoFragment碎片的UserActivity收到裁剪图片的回调
     * 并将回调后得到的值直接传递给UpdateInfoFragment碎片对象
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 强制触发
        mCurrentFragment.onActivityResult(requestCode, resultCode, data);
    }






}
