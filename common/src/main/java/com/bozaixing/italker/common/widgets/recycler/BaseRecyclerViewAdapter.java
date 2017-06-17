package com.bozaixing.italker.common.widgets.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bozaixing.italker.common.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


/*
 * Descr:   RecyclerView自定义适配器的基类
 * Author:  bozaixing.
 * Date:    2017-05-19.
 * Email:   654152983@qq.com.
 */
public abstract class BaseRecyclerViewAdapter<T>
                        extends RecyclerView.Adapter<BaseRecyclerViewAdapter.ViewHolder<T>>
                        implements View.OnClickListener,
                                    View.OnLongClickListener,
                                    AdapterCallback<T> {

    private List<T> mDatas;

    // 创建点击和长按事件的回调接口对象
    private AdapterListener<T> mAdapterListener;


    public BaseRecyclerViewAdapter(){
        this(null);
    }

    public BaseRecyclerViewAdapter(AdapterListener<T> adapterListener){
        this(new ArrayList<T>(), adapterListener);
    }

    /**
     * 构造方法，初始化数据
     * @param datas
     * @param adapterListener
     */
    public BaseRecyclerViewAdapter(List<T> datas, AdapterListener<T> adapterListener){
        this.mDatas = datas;
        this.mAdapterListener = adapterListener;
    }



    /**
     * 创建一个ViewHolder
     * @param parent    RecyclerView
     * @param viewType  界面的类型，简化约定为XML布局的ID
     * @return
     */
    @Override
    public ViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {

        // 创建布局加载器的对象
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        // 加载布局
        View view = inflater.inflate(viewType, parent, false);

        // 通过子类必须实现的方法得到一个ViewHolder对象
        ViewHolder<T> holder = getViewHolder(view, viewType);
        // 初始化数据更新的回调接口对象
        holder.setAdapterCallback(this);


        // 设置view的Tag为ViewHolder，进行双向绑定
        view.setTag(R.id.tag_recycler_holder, holder);

        // 设置事件点击
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);

        return holder;
    }

    /**
     * 复写默认的item类型的方法
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {

        return getItemViewType(position, mDatas.get(position));
    }

    /**
     * 定义创建ViewHolder对象的抽象方法，子类实现则必须复写
     * @param view      根布局
     * @param viewType  布局类型，其实就是XML的布局ID
     * @return
     */
    protected abstract ViewHolder<T> getViewHolder(View view, int viewType);


    /**
     * 得到布局的类型
     * @param position
     * @param data      当前item类型对应的数据
     * @return          XML布局文件的ID，用于创建ViewHolder
     */
    protected abstract int getItemViewType(int position, T data);


    /**
     * 绑定数据到一个Holder上
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder<T> holder, int position) {
        // 获取需要绑定的数据
        T data = mDatas.get(position);
        // 触发holder的绑定方法，进行数据绑定
        holder.bindData(data);

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    /**
     * 添加数据
     * @param data
     */
    public void addData(T data){
        mDatas.add(data);
        // 通知更新插入的数据
        notifyItemInserted(mDatas.size() - 1);
    }

    /**
     * 添加一堆数据（数组存放）
     * @param datas
     */
    public void addDatas(T... datas){
        // 验证数据的有效性
        if (datas != null && datas.length > 0){
            // 获取存放适配器数据集合对象的长度
            int startPosition = mDatas.size();
            // 将数组里面的数据添加进适配器集合对象中
            Collections.addAll(mDatas, datas);

            // 通知适配器更新插入的数据
            notifyItemRangeInserted(startPosition, datas.length);
        }
    }

    /**
     * 添加一堆数据（集合存放）
     * @param datas
     */
    public void addDatas(Collection<T> datas){
        // 验证数据的有效性
        if (datas != null && datas.size() > 0){
            // 获取存放适配器数据集合对象的长度
            int startPosition = mDatas.size();

            // 添加数据到适配器集合中
            mDatas.addAll(datas);

            // 通知适配器刷新数据显示
            notifyItemRangeInserted(startPosition, datas.size());

        }
    }


    /**
     * 替换为一个新的集合，其中包括了清空
     * @param datas     一个新的集合对象
     */
    public void replaceData(Collection<T> datas){
        // 验证数据的有效性
        if (datas == null || datas.size() <= 0 ){
            return;
        }
        // 清空适配器集合的数据
        mDatas.clear();
        // 重新添加数据
        mDatas.addAll(datas);

        // 通知适配器刷新数据显示
        notifyDataSetChanged();
    }



    /**
     * 清除适配器所有适配数据的操作方法
     */
    public void clearDatas(){
        if (mDatas != null && mDatas.size() > 0){
            // 清除集合中的所有数据
            mDatas.clear();

            // 通知适配器刷新数据显示
            notifyDataSetChanged();
        }
    }


    /**
     * 数据更新的回调方法
     * @param data
     * @param viewHolder
     */
    @Override
    public void update(T data, ViewHolder<T> viewHolder) {

        // 获取选中的item的位置
        int position = viewHolder.getAdapterPosition();

        if (position >= 0){
            // 移除此位置的数据
            mDatas.remove(position);
            // 像移除的位置添加数据
            mDatas.add(position, data);

            // 通知刷新数据
            notifyItemChanged(position);
        }

    }

    /**
     * 点击Item事件的处理
     * @param view
     */
    @Override
    public void onClick(View view) {
        // 获取ViewHolder的实例对象
        ViewHolder<T> holder  = (ViewHolder<T>) view.getTag(R.id.tag_recycler_holder);

        if (mAdapterListener != null){

            // 得到ViewHolder当前当前对应的适配器中的坐标
            int position = holder.getAdapterPosition();
            // 回调方法
            mAdapterListener.onItemClick(holder, mDatas.get(position));
        }
    }

    /**
     * 长按Item事件的处理
     * @param view
     * @return
     */
    @Override
    public boolean onLongClick(View view) {
        // 获取ViewHolder的实例对象
        ViewHolder<T> holder  = (ViewHolder<T>) view.getTag(R.id.tag_recycler_holder);

        if (mAdapterListener != null){

            // 得到ViewHolder当前当前对应的适配器中的坐标
            int position = holder.getAdapterPosition();
            // 回调方法
            mAdapterListener.onItemLongClick(holder, mDatas.get(position));

            return true;
        }

        return false;
    }


    /**
     * 初始化长点击和长按Item的接口监听对象
     * @param adapterListener
     */
    public void setAdapterListener(AdapterListener<T> adapterListener){
        this.mAdapterListener = adapterListener;
    }


    /**
     * 自定义适配器Item对象点击和长按事件的监听器
     * @param <T>   泛型数据
     */
    public interface AdapterListener<T> {

        /**
         * 当call点击Item的时候触发
         * @param holder
         * @param data
         */
        void onItemClick(BaseRecyclerViewAdapter.ViewHolder<T> holder, T data);

        /**
         * 当call长按Item的时候触发
         * @param holder
         * @param data
         */
        void onItemLongClick(BaseRecyclerViewAdapter.ViewHolder<T> holder, T data);

    }



    /**
     * 自定义的ViewHolder抽象类
     * @param <T>
     */
    public static abstract class ViewHolder<T> extends RecyclerView.ViewHolder {

        protected T mData;

        private AdapterCallback<T> mAdapterCallback;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        /**
         * 用于绑定数据的触发
         * @param data  绑定的数据
         */
        void bindData(T data){
            this.mData = data;

            onBind(data);
        }

        /**
         * 当触发绑定数据的时候的抽象回调方法，必须复写该方法（此方法是显示控件与需要适配的数据进行绑定时执行的方法）
         * @param data
         */
        protected abstract void onBind(T data);


        /**
         * 更新数据
         * @param data
         */
        public void updateData(T data){

            // 调用回调接口进行更新
            if (this.mAdapterCallback != null){
                mAdapterCallback.update(data, this);
            }

        }

        /**
         * 初始化回调接口对象
         * @param callback
         */
        public void setAdapterCallback(AdapterCallback<T> callback){
            this.mAdapterCallback = callback;
        }
    }


    /**
     * 对回调接口AdapterListener做一次实现
     * @param <T>   泛型数据
     */
    public static abstract class AdapterListenerImpl<T> implements AdapterListener<T> {


        @Override
        public void onItemClick(ViewHolder<T> holder, T data) {

        }

        @Override
        public void onItemLongClick(ViewHolder<T> holder, T data) {

        }
    }
}
