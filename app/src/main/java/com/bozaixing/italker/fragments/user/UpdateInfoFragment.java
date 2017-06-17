package com.bozaixing.italker.fragments.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import com.bozaixing.factory.Factory;
import com.bozaixing.factory.net.UploadFileHelper;
import com.bozaixing.italker.R;
import com.bozaixing.italker.common.app.BaseApplication;
import com.bozaixing.italker.common.app.BaseFragment;
import com.bozaixing.italker.common.widgets.PortraitView;
import com.bozaixing.italker.fragments.media.GalleryFragment;
import com.bumptech.glide.Glide;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import static android.app.Activity.RESULT_OK;

/*
 * Descr:   更新用户信息的碎片
 * Author:  bozaixing.
 * Date:    2017-06-13.
 * Email:   654152983@qq.com.
 */
public class UpdateInfoFragment extends BaseFragment implements View.OnClickListener {


    /**
     * UI
     */
    private PortraitView mPortraitView;


    /**
     * 加载界面布局的资源ID
     * @return
     */
    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_update_info;
    }


    @Override
    protected void initWidgets(View view) {

        // init UI
        mPortraitView = (PortraitView) view.findViewById(R.id.update_info_portrait_view);
    }

    @Override
    protected void setListener() {

        // 为头像显示控件设置点击响应事件
        mPortraitView.setOnClickListener(this);


    }

    /**
     * 点击事件的处理方法
     * @param v
     */
    @Override
    public void onClick(View v) {

        // 如果点击了头像显示控件
        if (v.getId() == R.id.update_info_portrait_view){

            // 创建选择图片的碎片对象并注册监听事件
            new GalleryFragment().setOnSelectImageListener(new GalleryFragment.OnSelectImageListener() {

                @Override
                public void onSelectImageFinish(String imagePath) {

                    UCrop.Options options = new UCrop.Options();
                    // 设置处理图片的格式JPEG
                    options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
                    // 设置压缩后的图片精度
                    options.setCompressionQuality(100);

                    // 得到头像的缓存地址
                    File tempFile = BaseApplication.getPortraitTempFile();

                    // 发起裁剪的操作
                    UCrop.of(Uri.fromFile(new File(imagePath)), Uri.fromFile(tempFile))
                            // 设置宽度和高度的比例为1:1
                            .withAspectRatio(1, 1)
                            // 设置裁剪后返回图片的最大尺寸（像素）
                            .withMaxResultSize(520, 520)
                            // 加载配置的相关参数
                            .withOptions(options)
                            // 启动
                            .start(getActivity());

                }
                // show的时候建议使用getChildFragmentManager()
                // tag为GalleryFragment class的名字
            }).show(getChildFragmentManager(), GalleryFragment.class.getName());
        }

    }


    /**
     * 获取裁剪后返回的图片
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 收到从AccountActivity中传递过来的回调，然后取出其中的值进行图片加载
        // 如果是我能够处理的类型
        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK){
            // 通过UCrop得到裁剪后返回的Uri图片地址
            Uri resultUri = UCrop.getOutput(data);

            if (resultUri != null){
                // 加载头像
                loadPortraitImage(resultUri);

            }

        }else if (resultCode == UCrop.RESULT_ERROR){    // 返回裁剪图片时出现错误

            final Throwable cropError = UCrop.getError(data);

        }

    }


    /**
     * 通过Uri地址加载头像图片
     * @param uri
     */
    private void loadPortraitImage(Uri uri){

        if (uri == null){
            return;
        }

        // 使用Glide第三方图片加载框架加载图片
        Glide.with(this)
                .load(uri)
                .asBitmap()
                .centerCrop()
                .into(mPortraitView);

        // 上传头像到阿里云服务器
        final String localPath = uri.getPath();

        Log.d(TAG, "localPath = " + localPath);

        // 异步上传头像
        Factory.getInstance().runOnAsync(new Runnable() {
            @Override
            public void run() {
                String serverPath = UploadFileHelper.uploadPortrait(localPath);

                Log.d(TAG, "serverPath = " + localPath);
            }
        });


    }
}
