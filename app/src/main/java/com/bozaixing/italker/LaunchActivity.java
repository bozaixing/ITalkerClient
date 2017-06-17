package com.bozaixing.italker;

import com.bozaixing.italker.activities.MainActivity;
import com.bozaixing.italker.common.app.BaseActivity;
import com.bozaixing.italker.fragments.assist.PermissionsFragment;

/*
 * Descr:   程序的启动界面
 * Author:  bozaixing.
 * Date:    2017-06-15.
 * Email:   654152983@qq.com.
 */
public class LaunchActivity extends BaseActivity {


    /**
     * 加载界面布局资源的ID
     * @return
     */
    @Override
    protected int getLayoutResouceId() {
        return R.layout.activity_launch;
    }


    @Override
    protected void onResume() {
        super.onResume();

        // 检查当前App的访问权限
        if (PermissionsFragment.checkIsHaveAllPerms(this, getSupportFragmentManager())){
            // 如果有相应的权限跳转至主页
            MainActivity.show(this);
        }

    }
}
