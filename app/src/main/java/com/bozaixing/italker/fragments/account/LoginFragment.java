package com.bozaixing.italker.fragments.account;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;


import com.bozaixing.italker.R;
import com.bozaixing.italker.common.app.BaseFragment;

/**
 * 登录碎片
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends BaseFragment {

    // 创建切换Fragment碎片视图的接口监听对象
    private AccountTrigger mAccountTrigger;


    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // 拿到我们的AccountActivity的引用
        mAccountTrigger = (AccountTrigger) context;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_login;
    }


    @Override
    protected void initWidgets(View view) {
        super.initWidgets(view);

        view.findViewById(R.id.login_textView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAccountTrigger.onTriggerView();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        // 调用接口对象的方法，进行一次Fragment碎片显示视图的切换，默认切换到注册界面
//        mAccountTrigger.onTriggerView();
    }
}
