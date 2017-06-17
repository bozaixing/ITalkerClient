package com.bozaixing.italker.utils;

import android.content.Context;
import android.widget.Toast;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

/*
 * Descr:   Toast提示工具封装类
 * Author:  bozaixing.
 * Date:    2017-06-14.
 * Email:   654152983@qq.com.
 */
public class ToastUtil {


    /**
     * 显示提示消息
     * @param context   上下文对象
     * @param message   需要显示的提示消息
     */
    public static void showToast(final Context context, final String message){

        // 异步消息提示，调用的第三方框架实现
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });

    }


    /**
     * 显示提示消息
     * @param context   上下文对象
     * @param resId     需要显示的提示消息ID
     */
    public static void showToast(Context context, int resId){
        showToast(context, context.getResources().getString(resId));
    }


}
