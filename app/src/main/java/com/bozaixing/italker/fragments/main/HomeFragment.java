package com.bozaixing.italker.fragments.main;

import android.util.Log;
import android.view.View;

import com.bozaixing.italker.R;
import com.bozaixing.italker.common.app.BaseFragment;
import com.bozaixing.italker.common.widgets.GalleryView;

/*
 * Descr:   
 * Author:  bozaixing.
 * Date:    2017-06-07.
 * Email:   654152983@qq.com.
 */
public class HomeFragment extends BaseFragment {

    /**
     * UI
     */
//    private GalleryView mGalleryView;


    /**
     * 加载布局界面的资源ID
     * @return
     */
    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_home;
    }


    @Override
    protected void initWidgets(View view) {

//        mGalleryView = (GalleryView) view.findViewById(R.id.home_gallery_view);

    }

    @Override
    protected void initDatas() {

//        mGalleryView.setup(getLoaderManager(), new GalleryView.OnSelectedStateChangedListener() {
//            @Override
//            public void onSelectedImageCountChanged(int count) {
//
//
//                Log.i(TAG, "AAAAAA---AAAA"+ count);
//            }
//        });

    }
}
