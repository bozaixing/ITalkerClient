package com.bozaixing.italker.common.widgets;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/*
 * Descr:   自定义正方形显示控件
 * Author:  bozaixing.
 * Date:    2017-06-09.
 * Email:   654152983@qq.com.
 */
public class SquareLayout extends FrameLayout {


    public SquareLayout(@NonNull Context context) {
        super(context);
    }

    public SquareLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * 重写测量方法
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 给父类传递的测量值都为宽度，
        // 那么就是基于宽度显示的正方形
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
