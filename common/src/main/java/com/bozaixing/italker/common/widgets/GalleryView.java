package com.bozaixing.italker.common.widgets;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bozaixing.italker.common.R;
import com.bozaixing.italker.common.widgets.recycler.BaseRecyclerViewAdapter;
import com.bozaixing.italker.utils.ToastUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/*
 * Descr:   自定义画廊
 * Author:  bozaixing.
 * Date:    2017-06-09.
 * Email:   654152983@qq.com.
 */
public class GalleryView extends RecyclerView {

    // 定义Loader的ID常量
    private static final int LOADER_ID = 0x0100;
    // 定义最大能选中的图片的数量
    private static final int MAX_IMAGE_COUNT = 3;
    // 定义查询返回的最小的图片大小(10KB)，如果查询结果的图片小于这个值，那么则不需要返回该图片
    private static final int MIN_IMAGE_FILE_SIZE = 10 * 1024;


    // 定义LoaderManager的回调接口对象并初始化
    private LoaderCallback mLoaderCallback = new LoaderCallback();

    // 定义适配器对象并初始化
    private Adapter mAdapter = new Adapter();

    // 定义储存所有选中的Image图片对象的集合并初始化
    private List<Image> mSelectedImages = new LinkedList<>();


    // 创建一个图片选中状态发生改变的内部监听器对象，提供给外部调用，告知外部数据改变
    private OnSelectedStateChangedListener mOnSelectedStateChangedListener;



    public GalleryView(Context context) {
        this(context, null);
    }

    public GalleryView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 构造方法，初始化数据
     * @param context
     * @param attrs
     * @param defStyle
     */
    public GalleryView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // 初始化
        init();

    }

    /**
     * init
     */
    private void init(){

        setLayoutManager(new GridLayoutManager(getContext(), 4));

        // 设置适配器显示
        setAdapter(mAdapter);

        // 为适配器设置回调监听
        mAdapter.setAdapterListener(new BaseRecyclerViewAdapter.AdapterListenerImpl<Image>() {
            @Override
            public void onItemClick(BaseRecyclerViewAdapter.ViewHolder<Image> holder, Image image) {
                // Cell点击操作，如果说我们的点击是允许的，那么更新对应的Cell状态，然后更新界面
                // 同理，如果说不能允许点击（已经达到最大的选中数量的时候），那么就不刷新界面
                if (onItemClickSelected(image)){
                    // 更新数据
                    holder.updateData(image);
                }

            }
        });

    }

    /**
     * 提供给外部初始化数据的方法
     * @param loaderManager
     * @param onSelectedStateChangedListener    图片选中状态发生改变的内部监听器对象
     * @return      返回一个LOADER_ID，可用于销毁
     */
    public int setup(LoaderManager loaderManager, OnSelectedStateChangedListener onSelectedStateChangedListener){
        // 初始化监听器
        this.mOnSelectedStateChangedListener = onSelectedStateChangedListener;

        // 初始化数据加载的管理对象
        loaderManager.initLoader(LOADER_ID, null, mLoaderCallback);

        return LOADER_ID;
    }


    /**
     * Cell点击的具体逻辑
     * @param image
     * @return      返回true代表我进行了数据更改，你需要刷新，反之不刷新
     */
    private boolean onItemClickSelected(Image image){
        // 是否需要进行刷新
        boolean isNotifyRefresh = false;

        // 判断当前选中的图片是否在选中的图片集合中
        if (mSelectedImages.contains(image)){
            // 如果存在则进行移除并改变选中状态
            mSelectedImages.remove(image);
            image.isSelected = false;

            // 状态已经改变则需要刷新操作
            isNotifyRefresh = true;
        }else {
            // 如果我们选中的图片超过了最大选中图片数量的限制则不进行刷新操作
            if (mSelectedImages.size() > MAX_IMAGE_COUNT){
                // 提示用户超过最大的一个图片选中数量
                String message = getResources().getString(R.string.label_gallery_select_max_size);
                // Toast提示用户
                ToastUtil.showToast(getContext(), String.format(message, MAX_IMAGE_COUNT));
                isNotifyRefresh = false;
            }else {
                // 否则则进行添加选中的图片
                mSelectedImages.add(image);
                // 改变图片的选中状态为选中
                image.isSelected = true;
                // 状态已经改变则需要刷新操作
                isNotifyRefresh = true;
            }
        }

        // 如果数据有改变我们需要通知外部的监听者我们的数据选中改变了
        if (isNotifyRefresh){
            notifySelectedStateChanged();
        }
        return isNotifyRefresh;
    }


    /**
     * 通知选中状态改变（通知回调）
     */
    private void notifySelectedStateChanged(){
        if (mOnSelectedStateChangedListener != null){
            if (mSelectedImages != null){
                mOnSelectedStateChangedListener.onSelectedImageCountChanged(mSelectedImages.size());
            }
        }
    }


    /**
     * 更新适配器适配数据的方法
     * @param images
     */
    private void updateSourceData(List<Image> images){

        if (images != null && images.size() > 0){
            // 通知适配器更新数据，这里为替换数据
            mAdapter.replaceData(images);
        }
    }


    /**
     * 得到选中图片的所有地址
     * @return      返回一个储存图片路径的数组
     */
    public String[] getSelectedImagePaths(){
        // 验证数据的有效性
        if (mSelectedImages != null &&  mSelectedImages.size() > 0){

            // 返回所有图片的路径
            String[] paths = new String[mSelectedImages.size()];
            int index = 0;

            // 循环遍历集合添加数据到返回图片路径的数组
            for (Image image : mSelectedImages){
                paths[index++] = image.path;
            }

            return paths;
        }

        return null;
    }


    /**
     * 清空选中的图片
     */
    public void clear(){

        if (mSelectedImages != null && mSelectedImages.size() > 0){

            // 循环遍历改变图片的选中状态
            for (Image image : mSelectedImages){
                image.isSelected = false;
            }
            // 清空数据
            mSelectedImages.clear();
            // 通知适配器刷新数据
            mAdapter.notifyDataSetChanged();
        }
    }


    /**
     * 定义内部数据结构的内部类
     */
    private static class Image {
        // 图片数据的ID
        int id;
        // 图片的路径
        String path;
        // 图片创建的日期
        long date;
        // 图片是否被选中
        boolean isSelected;

        @Override
        public boolean equals(Object o) {

            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Image image = (Image) o;

            return path != null ? path.equals(image.path) : image.path == null;

        }

        @Override
        public int hashCode() {
            return path != null ? path.hashCode() : 0;
        }
    }


    /**
     * 自定义一个内部适配器实现类
     */
    private class Adapter extends BaseRecyclerViewAdapter<Image> {

        @Override
        protected ViewHolder<Image> getViewHolder(View view, int viewType) {
            return new GalleryView.ViewHolder(view);
        }

        @Override
        protected int getItemViewType(int position, Image data) {
            return R.layout.cell_gallery_view_item;
        }
    }


    private class ViewHolder extends BaseRecyclerViewAdapter.ViewHolder<Image> {

        // 显示图片的控件
        private ImageView mPhotoView;

        // 选中图片时的遮罩对象
        private View mShadeView;

        // 选中和取消选中的CheckBox
        private CheckBox mSelectedCb;


        /**
         * 构造方法
         * @param itemView
         */
        public ViewHolder(View itemView) {
            super(itemView);
            // 初始化显示控件
            mPhotoView = (ImageView) itemView.findViewById(R.id.cell_gallery_image_view);
            mShadeView = itemView.findViewById(R.id.cell_gallery_shade_view);
            mSelectedCb = (CheckBox) itemView.findViewById(R.id.cell_gallery_selected_cb);
        }


        @Override
        protected void onBind(Image image) {
            // 加载图片
            Glide.with(getContext())
                    // 需要加载图片的本地路径
                    .load(image.path)
                    // 这里加载的图片无本地图片不需要设置缓存，直接从原图加载
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    // 居中裁剪显示
                    .centerCrop()
                    // 如果图片加载耗时先设置一个默认显示的颜色
                    .placeholder(R.color.grey_200)
                    .into(mPhotoView);

            // 设置遮罩蒙版的显示状态
            mShadeView.setVisibility(image.isSelected ? View.VISIBLE : View.INVISIBLE);

            // 设置checkBox显示控件的选中状态
            mSelectedCb.setChecked(image.isSelected);

        }
    }


    /**
     * 用于实际的数据加载的Loader对象
     */
    private class LoaderCallback implements LoaderManager.LoaderCallbacks<Cursor> {

        // 定义查询需要返回的列
        private final String[] IMAGE_PROJECTON = new String[]{
                // 图片的ID
                MediaStore.Images.Media._ID,
                // 图片对应的Uri路径
                MediaStore.Images.Media.DATA,
                // 图片的创建时间
                MediaStore.Images.Media.DATE_ADDED
        };


        /**
         * 创建加载数据的Loader对象
         * @param id
         * @param args
         * @return
         */
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {

            Log.i("mmmmmmmmmmmmm", "mmmmmmmmmmmm");
            if (id == LOADER_ID){

                // 返回查询的结果
                return new CursorLoader(getContext(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        // 查询需要返回的数据列
                        IMAGE_PROJECTON,
                        null,
                        null,
                        // 根据图片创建的时间进行倒序排列
                        MediaStore.Images.Media.DATE_ADDED + " DESC");
            }

            return null;
        }

        /**
         * 加载数据完成时的回调
         * @param loader
         * @param cursor    返回一个游标对象
         */
        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            Log.i("nnnnnnnnnnnnn", "nnnnnnnnnnnnnn");

            // 获取查询的数据结果
            List<Image> images = new ArrayList<>();
            if (cursor != null){
                int count = cursor.getCount();
                // 如果有查询结果则移动游标到顶部
                if (count > 0){
                    cursor.moveToFirst();

                    // 循环读取直到没有下一条数据
                    do {
                        // id
                        int id = cursor.getInt(cursor.getColumnIndex(IMAGE_PROJECTON[0]));
                        String path = cursor.getString(cursor.getColumnIndex(IMAGE_PROJECTON[1]));
                        long dateTime = cursor.getLong(cursor.getColumnIndex(IMAGE_PROJECTON[2]));

                        File file = new File(path);

                        // 如果没有图片或者图片大小定义的最小的图片尺寸，那么跳过本次循环，不做任何操作
                        if (!file.exists() || file.length() < MIN_IMAGE_FILE_SIZE){
                            continue;
                        }
                        Image image = new Image();
                        image.id = id;
                        image.path = path;
                        image.date = dateTime;

                        // 添加进存放图片信息的集合
                        images.add(image);

                    }while (cursor.moveToNext());


                    Log.i("custom view ------", images.size() + "---" + images.get(0).path);


                }
            }

            // 通知适配器刷新数据
            updateSourceData(images);

        }


        /**
         * 当Loader销毁或者重置的时候调用的方法
         * @param loader
         */
        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

            // TODO  销毁和重新的时候调用

        }
    }


    /**
     * 定义一个图片选中状态改变的内部监听器，提供给外部调用，告知外部数据改变
     */
    public interface OnSelectedStateChangedListener {

        /**
         * 选中图片的状态发生改变时回调的方法
         * @param count     当前选中图片的数量
         */
        void onSelectedImageCountChanged(int count);
    }

}
