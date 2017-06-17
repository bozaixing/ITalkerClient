package com.bozaixing.italker.fragments.account;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.bozaixing.factory.presenter.account.RegisterContract;
import com.bozaixing.italker.R;
import com.bozaixing.italker.common.app.BaseFragment;
import com.bozaixing.italker.common.app.PresenterFragment;

import net.qiujuer.genius.ui.widget.Button;
import net.qiujuer.genius.ui.widget.Loading;

/**
 * 注册碎片
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends PresenterFragment<RegisterContract.Presenter> {

    /**
     * UI
     */
    private EditText mPhoneTxt;
    private EditText mPasswordTxt;
    private EditText mNameTxt;
    private FrameLayout mGoLoginLayout;
    private Button mSunbmitBtn;
    private Loading mLoadingView;


    /**
     * 创建切换碎片视图显示的对象
     */
    private AccountTrigger mAccountTrigger;




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // 拿到我们的AccountActivity对象的引用
        mAccountTrigger = (AccountTrigger) context;
    }

    /**
     * 加载布局资源的Id
     * @return
     */
    @Override
    protected int getLayoutResourceId() {

        return R.layout.fragment_register;
    }

    /**
     * 初始化显示控件
     * @param view
     */
    @Override
    protected void initWidgets(View view) {
        // init ui
        mPhoneTxt = (EditText) view.findViewById(R.id.register_phone_txt);
        mPasswordTxt = (EditText) view.findViewById(R.id.register_password_txt);
        mNameTxt = (EditText) view.findViewById(R.id.register_name_txt);
        mGoLoginLayout = (FrameLayout) view.findViewById(R.id.register_go_login_layout);
        mSunbmitBtn = (Button) view.findViewById(R.id.register_submit_btn);
        mLoadingView = (Loading) view.findViewById(R.id.register_loading_view);

    }


    @Override
    protected void setListener() {

        // 在已有账号的情况下，为提示去登录的控件注册点击响应事件
        mGoLoginLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "xxxxxxxxxxxxxxx");
                // 切换碎片的显示
                mAccountTrigger.onTriggerView();
            }
        });



        // 为提交按钮注册点击响应事件
        mSunbmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                mPresenter.register();
            }
        });



    }
}
