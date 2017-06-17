package com.bozaixing.italker.fragments.media;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.bozaixing.italker.R;
import com.bozaixing.italker.common.app.BaseFragment;
import com.bozaixing.italker.common.widgets.GalleryView;

import net.qiujuer.genius.ui.Ui;

/*
 * Descr:   图片选择界面的显示碎片
 * Author:  bozaixing.
 * Date:    2017-06-13.
 * Email:   654152983@qq.com.
 */
public class GalleryFragment extends BottomSheetDialogFragment implements GalleryView.OnSelectedStateChangedListener {

    /**
     * UI
     */
    private GalleryView mGalleryView;

    /**
     * Data
     */
    // 初始化选择图片的内部监听接口的对象
    private OnSelectImageListener mOnSelectImageListener;



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // 返回我们内部自定义的Dialog的实例对象
        return new TransStatusBottomSheetDialog(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        // 初始化GalleryView显示控件
        mGalleryView = (GalleryView) root.findViewById(R.id.gallery_gallery_view);

        // 返回显示界面
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        // 加载显示数据
        mGalleryView.setup(getLoaderManager(), this);
    }


    /**
     * GalleryView控件选择图片的回调的实现方法
     * @param count     当前选中图片的数量
     */
    @Override
    public void onSelectedImageCountChanged(int count) {
        // 如果选中一张图片
        if (count > 0){
            // 隐藏自己
            dismiss();
            // 回调监听
            if (mOnSelectImageListener != null){
                // 得到所有的选中图片的路径
                String[] paths = mGalleryView.getSelectedImagePaths();
                // 执行回调方法
                if (paths.length > 0){
                    mOnSelectImageListener.onSelectImageFinish(paths[0]);
                    // 取消和唤起者之前的引用，加快内存的回收
                    mOnSelectImageListener = null;
                }

            }
        }

    }


    /**
     * 初始化选中图片的监听对象，并返回自己
     * @param onSelectImageListener
     * @return      GalleryFragment
     */
    public GalleryFragment setOnSelectImageListener(OnSelectImageListener onSelectImageListener){

        this.mOnSelectImageListener = onSelectImageListener;

        return this;
    }



    /**
     * 定义选择图片的内部监听接口
     */
    public interface OnSelectImageListener {

        /**
         * 选中图片完成的回调方法
         * @param imagePath     当前选中图片的路径
         */
        void onSelectImageFinish(String imagePath);

    }


    /**
     * 重写BottomSheetDialog，解决顶部状态栏变黑
     */
    public static class TransStatusBottomSheetDialog extends BottomSheetDialog {


        public TransStatusBottomSheetDialog(@NonNull Context context) {
            super(context);
        }

        public TransStatusBottomSheetDialog(@NonNull Context context, @StyleRes int theme) {
            super(context, theme);
        }

        protected TransStatusBottomSheetDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
            super(context, cancelable, cancelListener);
        }

        /**
         * 重写创建的方法
         * @param savedInstanceState
         */
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            final Window window = getWindow();
            if (window == null){
                return;
            }
            // 得到屏幕的高度
            int screenHeight = getContext().getResources().getDisplayMetrics().heightPixels;
            // 得到状态栏的高度
            int statusHeight = (int) Ui.dipToPx(getContext().getResources(), 25);

            // Dialog的显示高度（这里去除了状态栏的高度）
            int dialogHeight = screenHeight - screenHeight;

            // 设置计算后的显示高度
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    dialogHeight <= 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);

        }
    }
}
