package com.bozaixing.italker.common.widgets.recycler;

/*
 * Descr:   RecyclerView适配器的回调接口对象
 * Author:  bozaixing.
 * Date:    2017-05-19.
 * Email:   654152983@qq.com.
 */
public interface AdapterCallback<T> {

    void update(T data, BaseRecyclerViewAdapter.ViewHolder<T> viewHolder);

}
