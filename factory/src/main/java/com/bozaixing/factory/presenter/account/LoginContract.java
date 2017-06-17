package com.bozaixing.factory.presenter.account;

import com.bozaixing.italker.factory.presenter.BaseContract;

/*
 * Descr:   登录业务契约接口的定义
 * Author:  bozaixing.
 * Date:    2017-06-16.
 * Email:   654152983@qq.com.
 */
public interface LoginContract {



    interface View extends BaseContract.View<Presenter> {

        /**
         * 登录成功
         */
        void loginSuccess();

    }


    interface Presenter extends BaseContract.Presenter {


        /**
         * 发起一个登录
         * @param phone
         * @param password
         */
        void login(String phone, String password);


    }
}
