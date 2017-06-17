package com.bozaixing.italker.factory.presenter;

import android.support.annotation.StringRes;

/*
 * Descr:   MVP模式中公共的基本契约接口的定义
 * Author:  bozaixing.
 * Date:    2017-06-16.
 * Email:   654152983@qq.com.
 */
public interface BaseContract {


    interface View<T extends Presenter> {

        /**
         * 显示一个字符串错误
         * @param resId
         */
        void showError(@StringRes int resId);

        /**
         * 显示进度条
         */
        void showLoading();


        /**
         * View层支持设置一个Presenter
         * @param presenter
         */
        void setPresenter(T presenter);

    }


    interface Presenter {

        /**
         * 开始时触发
         */
        void start();


        /**
         * 销毁时触发
         */
        void destroy();

    }


}
