package com.student0.www.qqlistview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by willj on 2017/4/20.
 */

public class ChildView extends LinearLayout {

    private final String TAG = "ChildView";

    public ChildView(Context context) {
        super(context);
    }

    public ChildView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChildView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        return false;
    }
}
