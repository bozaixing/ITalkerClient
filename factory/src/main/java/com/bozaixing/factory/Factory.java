package com.bozaixing.factory;


import com.bozaixing.italker.common.app.BaseApplication;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/*
 * Descr:   单例模式构建
 * Author:  bozaixing.
 * Date:    2017-06-15.
 * Email:   654152983@qq.com.
 */
public class Factory {


    // 创建单例对象
    private static volatile Factory sInstance;

    // 创建线程池对象，并初始化线程池对象（可变尺寸的线程池）
    private final Executor mExecutor = Executors.newCachedThreadPool();

    /**
     * 私有的构造方法
     */
    private Factory(){

    }

    /**
     * 获取单例对象
     * @return
     */
    public static Factory getInstance(){

        if (sInstance == null){

            synchronized (Factory.class){
                if (sInstance == null){
                    sInstance = new Factory();
                }
            }
        }

        return sInstance;
    }


    /**
     * 获取当前应用程序全局的Application
     * @return
     */
    public static BaseApplication getApplication(){

        return BaseApplication.getInstance();
    }


    /**
     * 异步运行的方法
     */
    public void runOnAsync(Runnable runnable){
        // 异步执行线程
        if (mExecutor != null){
            mExecutor.execute(runnable);
        }

    }


}
