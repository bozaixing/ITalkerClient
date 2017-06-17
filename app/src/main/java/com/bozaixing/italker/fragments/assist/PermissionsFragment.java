package com.bozaixing.italker.fragments.assist;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bozaixing.italker.R;
import com.bozaixing.italker.fragments.media.GalleryFragment;
import com.bozaixing.italker.utils.ToastUtil;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/*
 * Descr:   权限申请弹出框
 * Author:  bozaixing.
 * Date:    2017-06-14.
 * Email:   654152983@qq.com.
 */
public class PermissionsFragment extends BottomSheetDialogFragment implements EasyPermissions.PermissionCallbacks {

    /**
     * 定义申请访问权限回调的标识
     */
    private static final int REQUEST_PERMISSIONS_FLAG = 0x0100;


    /**
     * 创建Dialog的显示对象
     * @param savedInstanceState
     * @return
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 返回去除状态栏变黑的dialog对象
        return new GalleryFragment.TransStatusBottomSheetDialog(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_permissions, container, false);

        // 获取授权申请按钮，并注册点击响应事件
        root.findViewById(R.id.permissions_submit_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击授权按钮时进行申请权限
                requestPermissions();
            }
        });

        return root;
    }


    @Override
    public void onResume() {
        super.onResume();
        // 界面显示的时候进行刷新控件的显示状态
        refreshUiImageViewShowState(getView());
    }

    /**
     * 通过判断我们当前的App是否具有相关的访问权限来刷新判断是否显示ImageView控件
     * @param view
     */
    private void refreshUiImageViewShowState(View view){
        // 判断界面是否加载显示
        if (view == null){
            return;
        }

        view.findViewById(R.id.permissions_state_network_view)
                .setVisibility(isHaveNetworkPerms(getContext()) ? View.VISIBLE : View.GONE);

        view.findViewById(R.id.permissions_state_read_external_storage_view)
                .setVisibility(isHaveReadExternalStoragePerms(getContext()) ? View.VISIBLE : View.GONE);

        view.findViewById(R.id.permissions_state_write_external_storage_view)
                .setVisibility(isHaveWriteExternalStoragePerms(getContext()) ? View.VISIBLE : View.GONE);

        view.findViewById(R.id.permissions_state_record_audio_view)
                .setVisibility(isHaveRecordAudioPerms(getContext()) ? View.VISIBLE : View.GONE);

    }


    /**
     * 判断当前App是否具有网络访问权限
     * @return      返回true，表示有，否则相反
     */
    private static boolean isHaveNetworkPerms(Context context){
        // 准备需要检查的网络权限
        String[] perms = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE
        };

        return EasyPermissions.hasPermissions(context, perms);
    }

    /**
     * 判断当前App是否具有外部存储的读取权限
     * @return      返回true，表示有，否则相反
     */
    private static boolean isHaveReadExternalStoragePerms(Context context){
        // 准备需要检查的外部存储的读取权限
        String[] perms = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
        };

        return EasyPermissions.hasPermissions(context, perms);
    }

    /**
     * 判断当前App是否具有外部存储的写入权限
     * @return      返回true，表示有，否则相反
     */
    private static boolean isHaveWriteExternalStoragePerms(Context context){
        // 准备需要检查的外部存储的写入权限
        String[] perms = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
        };

        return EasyPermissions.hasPermissions(context, perms);
    }


    /**
     * 判断当前App是否具有录音权限
     * @return      返回true，表示有，否则相反
     */
    private static boolean isHaveRecordAudioPerms(Context context){
        // 准备需要检查的录音权限
        String[] perms = new String[]{
                Manifest.permission.RECORD_AUDIO,
        };

        return EasyPermissions.hasPermissions(context, perms);
    }


    /**
     * 私有的show()当前PermissionsFragment方法
     */
    private static void show(FragmentManager fragmentManager){

        // 调用BottomSheetDialogFragment碎片已经准备好的显示方法
        new PermissionsFragment()
                .show(fragmentManager, PermissionsFragment.class.getName());

    }


    /**
     * 检查当前App是否具有所有的访问权限
     * @return      是否具有所有的访问权限，true表示有
     */
    public static boolean checkIsHaveAllPerms(Context context, FragmentManager fragmentManager){


        for (int i = 0; i< 100 ;i++){
            Log.i("ffffffffffffffffff", "sdddd");
        }

        // 检查当前App是否具有所有的访问权限
        boolean isHaveAllPerms = isHaveNetworkPerms(context)
                && isHaveReadExternalStoragePerms(context)
                && isHaveWriteExternalStoragePerms(context)
                && isHaveRecordAudioPerms(context);

        // 如果没有则显示当前申请权限的界面
        if (!isHaveAllPerms){

            show(fragmentManager);
        }

        return isHaveAllPerms;
    }


    /**
     * 实现申请权限的方法
     */
    @AfterPermissionGranted(REQUEST_PERMISSIONS_FLAG)   // 当request值对应的权限申请通过的话会自动调用该方法
    private void requestPermissions(){

        // 准备好需要申请的权限
        String[] perms = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        };

        // 首先判断是否已经具有所有权限
        if (EasyPermissions.hasPermissions(getContext(), perms)){
            // 提示用户用户授权成功
            ToastUtil.showToast(getContext(), R.string.label_permission_ok);
            // 刷新显示控件的显示状态
            refreshUiImageViewShowState(getView());
        }else {

            // 如果没有所有的访问权限则进行申请访问权限的操作
            EasyPermissions.requestPermissions(this,
                    getResources().getString(R.string.title_assist_permissions),
                    REQUEST_PERMISSIONS_FLAG,
                    perms);
        }

    }


    /**
     * 申请访问权限成功的回调方法
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    /**
     * 申请访问权限失败的回调方法
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

        // 如果有没有申请成功的权限存在，则提示用户手动点击到设置界面进行打开访问权限
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)){
            // 弹出提示框
            new AppSettingsDialog.Builder(this)
                    .build()
                    .show();
        }
    }


    /**
     * 权限申请的时候回调的方法，在这个方法中把对应的权限申请状态交给EasyPermissions框架处理
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // 传递对应的参数，并且告知接收权限的处理者是我自己
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
