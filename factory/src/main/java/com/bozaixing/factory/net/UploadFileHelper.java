package com.bozaixing.factory.net;

import android.text.format.DateFormat;
import android.util.Log;

import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.bozaixing.factory.Factory;
import com.bozaixing.italker.utils.HashUtil;

import java.io.File;
import java.util.Date;


/*
 * Descr:   上传工具类，用于上传任意文件到阿里OSS存储
 * Author:  bozaixing.
 * Date:    2017-06-15.
 * Email:   654152983@qq.com.
 */
public class UploadFileHelper {

    private static final String TAG = UploadFileHelper.class.getSimpleName();

    // 存储区域
    private static final String ENDPOINT = "http://oss-cn-hongkong.aliyuncs.com";

    // 上传的仓库名
    private static final String BUCKET_NAME = "italker-new";



    /**
     * 与远程服务器的连接
     * @return
     */
    public static OSS getOSSClient(){

        // 明文设置secret的方式建议只在测试时使用，更多鉴权模式请参考后面的访问控制章节
        OSSCredentialProvider credentialProvider =
                new OSSPlainTextAKSKCredentialProvider("LTAIYQD07p05pHQW", "2txxzT8JXiHKEdEjylumFy6sXcDQ0G");

        return new OSSClient(Factory.getApplication(), ENDPOINT, credentialProvider);
    }


    /**
     * 上传文件的最终方法，上传成功则返回一个文件在服务器存储的地址
     * @param objKey        上传成功后，在服务器上的独立的Key
     * @param path          需要上传的文件的本地路径
     * @return              返回一个文件在服务器上存储的地址
     */
    private static String uploadFile(String objKey, String path){

        // 构造一个上传请求
        PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, objKey, path);

        // 上传文件
        try{

            // 初始化上传的Client
            OSS oss = getOSSClient();
            // 开始同步上传
            PutObjectResult putObjectResult = oss.putObject(putObjectRequest);

            // 得到一个外网访问的Url地址
            String url = oss.presignPublicObjectURL(BUCKET_NAME, objKey);

            // 打印路径的日志
            Log.d(TAG, "PublicObjectURL = " + url);

            return url;

        }catch (Exception e){
            e.printStackTrace();

            // 如果上传文件出现异常则返回空
            return null;
        }
    }


    /**
     * 获取当前年月的字符串
     * @return
     */
    private static String getDateString(){
        // 返回一个当前年月的字符串
        return DateFormat.format("yyyyMM", new Date()).toString();
    }


    /**
     * 上传普通图片
     * @param path  本地图片的文件路径
     * @return      服务器的存储地址
     */
    // image/201706/asddfgggsddfffs.jpg
    public static String uploadImage(String path){
        // 年月
        String dateString = getDateString();
        // 文件的MD5
        String fileMD5 = HashUtil.getMD5String(new File(path));
        // 计算并组合上传文件的objKey
        String objKey = String.format("image/%s/%s.jpg", dateString, fileMD5);

        return uploadFile(objKey, path);
    }


    /**
     * 上传头像
     * @param path
     * @return
     */
    // portrait/201706/asddfgggsddfffs.jpg
    public static String uploadPortrait(String path){

        // 年月
        String dateString = getDateString();
        // 文件的MD5
        String fileMD5 = HashUtil.getMD5String(new File(path));
        // 计算并组合上传文件的objKey
        String objKey = String.format("portrait/%s/%s.jpg", dateString, fileMD5);

        return uploadFile(objKey, path);

    }


    /**
     * 上传音频文件
     * @param path
     * @return
     */
    // audio/201706/asddfgggsddfffs.mp3
    public static String uploadAudio(String path){

        // 年月
        String dateString = getDateString();
        // 文件的MD5
        String fileMD5 = HashUtil.getMD5String(new File(path));
        // 计算并组合上传文件的objKey
        String objKey = String.format("audio/%s/%s.mp3", dateString, fileMD5);

        return uploadFile(objKey, path);

    }


}
