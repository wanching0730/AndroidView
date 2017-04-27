package com.student0.www.qqlistview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by willj on 2017/4/20.
 */

public class FatherView extends LinearLayout {

    private final String TAG = "FatherView";

    public FatherView(Context context) {
        super(context);
    }

    public FatherView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FatherView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        System.out.println(TAG + "--->dispatchTouchEvent" + TouchEventUtil.getTouchAction(ev.getAction()));
        return super.dispatchTouchEvent(ev);
        //return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        System.out.println(TAG + "--->onInterceptTouchEvent" + TouchEventUtil.getTouchAction(ev.getAction()));
        return super.onInterceptTouchEvent(ev);
        //return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        System.out.println(TAG + "--->onTouchEvent" + TouchEventUtil.getTouchAction(event.getAction()));
        //return super.onTouchEvent(event);
        return true;
    }
}
