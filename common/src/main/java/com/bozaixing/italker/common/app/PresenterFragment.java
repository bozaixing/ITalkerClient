package com.bozaixing.italker.common.app;

import android.support.annotation.StringRes;

import com.bozaixing.italker.factory.presenter.BaseContract;
import com.bozaixing.italker.utils.ToastUtil;

/*
 * Descr:   Presenter的Fragment碎片基类
 * Author:  bozaixing.
 * Date:    2017-06-17.
 * Email:   654152983@qq.com.
 */
public abstract class PresenterFragment<T extends BaseContract.Presenter> extends BaseFragment
                                                                    implements BaseContract.View<T> {
    /**
     * 定义泛型的Presenter操作对象
     */
    protected T mPresenter;


    @Override
    public void showError(@StringRes int resId) {
        // 弹出Toast
        ToastUtil.showToast(getContext(), resId);
    }

    @Override
    public void showLoading() {

        // TODO 根据具体业务判断是否复写该方法
    }


    /**
     * 设置初始化mPresenter对象
     * @param presenter
     */
    @Override
    public void setPresenter(T presenter) {
        mPresenter = presenter;
    }
}
