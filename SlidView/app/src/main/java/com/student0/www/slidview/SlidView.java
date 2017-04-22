package com.student0.www.slidview;

import android.app.Service;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * Created by willj on 2017/4/22.
 */

public class SlidView extends ListView {

    private int mScreenWidth;
    private Context mContext;
    private int mDeleteWidth;
    private ViewGroup mPointView;
    private boolean isDeleteShow;

    private LinearLayout.LayoutParams params;

    private int mDownX;
    private int mDownY;

    private final int INFO_POSITION = 0;
    private final int DELETE_POSITION = 1;

    private final double SCALE_WHEN_SHOW = 1.0/4.0;
    private final double SCALE_WHEN_HEIDEN = 1.0/5.0;
    private LinearLayout.LayoutParams mItemLayoutParams;

    public SlidView(Context context) {
        super(context, null);
    }

    public SlidView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        mScreenWidth = displayMetrics.widthPixels;
    }

    public SlidView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                performAsActionDown(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                performAsActionMove(ev);
                break;
            case MotionEvent.ACTION_UP:
                performAsActionUp(ev);
                break;
        }

        return super.onTouchEvent(ev);
    }

    private void performAsActionDown(MotionEvent ev) {
        mDownX = (int) ev.getX();
        mDownY = (int) ev.getY();
        if(isDeleteShow){
            LinearLayout tempView = (LinearLayout)getChildAt(pointToPosition(mDownX, mDownY) - getFirstVisiblePosition());
            if (!tempView.equals(mPointView)){       //判断当前点击的item不是已选中的item，是则取消选中
                turnNormal();
            }
        }
        mPointView = (ViewGroup)getChildAt(pointToPosition(mDownX, mDownY) - getFirstVisiblePosition());
        params = (LinearLayout.LayoutParams) mPointView.getChildAt(INFO_POSITION).getLayoutParams();
        mDeleteWidth = mPointView.getChildAt(DELETE_POSITION).getLayoutParams().width;
        params.width = mScreenWidth;
        mPointView.getChildAt(INFO_POSITION).setLayoutParams(params);
    }

    private void turnNormal() {
        params.leftMargin = 0;
        mPointView.getChildAt(INFO_POSITION).setLayoutParams(params);
    }

    private void performAsActionMove(MotionEvent ev) {
        int nowX = (int) ev.getX();
        int nowY = (int) ev.getY();

        int diffX = nowX - mDownX;
        int diffY = nowY - mDownY;
        if (diffX < 0 ) {   //Delete没显示时，左滑显示Delete
            if (Math.abs(diffX) > mDeleteWidth) {
                diffX = - mDeleteWidth;
            }
            if (isDeleteShow){
                diffX = -mDeleteWidth;
            }
            isDeleteShow = true;
            params = (LinearLayout.LayoutParams) mPointView.getChildAt(INFO_POSITION).getLayoutParams();
            params.leftMargin = diffX;
            mPointView.getChildAt(INFO_POSITION).setLayoutParams(params);
        }else if (diffX > 0 && isDeleteShow){
            if (Math.abs(diffX) > mDeleteWidth){
               diffX = mDeleteWidth;
            }
           //LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mPointView.getChildAt(INFO_POSITION).getLayoutParams();
           params.leftMargin = diffX - mDeleteWidth ;
           mPointView.getChildAt(INFO_POSITION).setLayoutParams(params);
        }
    }

    private void performAsActionUp(MotionEvent ev) {
        int nowX = (int) ev.getX();
        int nowY = (int) ev.getY();

        int diffX = nowX - mDownX;
        int diffY = nowY - mDownY;
        if (diffX < 0 && isDeleteShow){
            params.leftMargin = -mDeleteWidth;
        }else if (diffX > 0 && isDeleteShow){
            params.leftMargin = 0;
            isDeleteShow = false;
        }
        mPointView.getChildAt(INFO_POSITION).setLayoutParams(params);
    }
}
