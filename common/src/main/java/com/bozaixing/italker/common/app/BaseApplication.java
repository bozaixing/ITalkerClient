package com.bozaixing.italker.common.app;

import android.app.Application;
import android.os.SystemClock;

import java.io.File;

/*
 * Descr:   
 * Author:  bozaixing.
 * Date:    2017-06-14.
 * Email:   654152983@qq.com.
 */
public class BaseApplication extends Application {

    private static BaseApplication sInstance;


    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;
    }


    /**
     * 返回当前对象（外部获取单例）
     * @return
     */
    public static BaseApplication getInstance(){

        return sInstance;
    }


    /**
     * 获取缓存文件夹的地址
     * @return      当前APP的缓存文件夹地址
     */
    public static File getCacheDirFile(){

        return sInstance.getCacheDir();
    }


    /**
     * 获取头像文件的本地缓存地址
     * @return
     */
    public static File getPortraitTempFile(){
        // 得到头像目录的缓存地址
        File dir = new File(getCacheDirFile(), "portrait");
        // 创建所有的对应的文件夹
        dir.mkdirs();

        // 删除文件夹里面一些旧的缓存文件
        File[] files = dir.listFiles();
        if (files != null && files.length > 0){
            // 循环遍历删除文件
            for (File file : files){
                file.delete();
            }
        }

        // 创建一个当前时间戳的目录文件地址
        File tempFile = new File(dir, SystemClock.uptimeMillis() + ".jpg");

        return tempFile.getAbsoluteFile();
    }


    /**
     * 获取音频文件的本地缓存地址
     * @param isTemp    是否是缓存文件，true每次返回的文件地址是一样，避免用户录音时无数次取消时产生过多的缓存文件
     * @return
     */
    public static File getAudioTempFile(boolean isTemp){
        File dir = new File(getCacheDirFile(), "audio");
        // 创建文件夹
        dir.mkdirs();

        // 删除文件夹里面一些旧的缓存文件
        File[] files = dir.listFiles();
        if (files != null && files.length > 0){
            // 循环遍历删除旧文件
            for (File file : files){
                file.delete();
            }
        }

        // 创建一个返回的缓存文件对象
        File tempFile = new File(dir, isTemp ? "temp.mp3" : SystemClock.uptimeMillis() + ".mp3");

        // 返回缓存文件的相对路径
        return tempFile.getAbsoluteFile();
    }


}
