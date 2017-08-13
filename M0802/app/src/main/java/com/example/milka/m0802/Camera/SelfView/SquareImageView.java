package com.example.milka.m0802.Camera.SelfView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Milka on 2017/8/7.
 */

public class SquareImageView extends android.support.v7.widget.AppCompatImageView {

    private final int defaultSize = 50;

    public SquareImageView(Context context) {
        super(context, null);
    }

    public SquareImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //保证ImageView为正方形
        int min = Math.min(getSize(defaultSize, widthMeasureSpec), getSize(defaultSize, heightMeasureSpec));
        setMeasuredDimension(min, min);
    }
    /**
     * 控件大小定义
     * */
    private int getSize(int defaultSize, int measureSpec){
        int resultSize = defaultSize;

        int size = MeasureSpec.getSize(measureSpec);
        int mode = MeasureSpec.getMode(measureSpec);

        switch (mode){
            case MeasureSpec.AT_MOST:
                resultSize = size;
                break;
            case MeasureSpec.EXACTLY:
                resultSize = size;
                break;
            case MeasureSpec.UNSPECIFIED:
                resultSize = defaultSize;
                break;
            default:
                break;
        }
        return resultSize;
    }
}
