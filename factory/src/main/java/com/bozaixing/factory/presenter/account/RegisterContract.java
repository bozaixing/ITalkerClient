package com.bozaixing.factory.presenter.account;

import com.bozaixing.italker.factory.presenter.BaseContract;

/*
 * Descr:   注册业务契约接口的定义
 * Author:  bozaixing.
 * Date:    2017-06-16.
 * Email:   654152983@qq.com.
 */
public interface RegisterContract {



    interface View extends BaseContract.View<Presenter> {


        /**
         * 注册成功
         */
        void registerSuccess();

    }


    interface Presenter extends BaseContract.Presenter {

        /**
         * 发起一个注册
         * @param phone     手机号码
         * @param password  用户密码
         * @param name      用户名
         */
        void register(String phone, String password, String name);


        /**
         * 检查手机号码是否合法
         * @param phone     需要检查的手机号码
         * @return          返回true表示合法，否则相反
         */
        boolean checkPhoneNumber(String phone);


    }
}
