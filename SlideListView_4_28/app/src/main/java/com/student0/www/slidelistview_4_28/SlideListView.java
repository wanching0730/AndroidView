package com.student0.www.slidelistview_4_28;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * Created by willj on 2017/4/28.
 */

public class SlideListView extends ListView {

    private final int CONTENT_POSITION = 0;
    private final int DELETE_POSITION = 1;
    
    private int mScreenWidth;   //屏幕宽度
    private int deleteWidth;
    private ViewGroup targetItemViewGroup;  //被标记item
    private boolean isSelect;   //是否有选中，标记

    private int downX;  //DOWN时的X
    private int downY;
    private LinearLayout.LayoutParams childOneParams;   //padding操作的child数据

    public SlideListView(Context context) {
        super(context, null);
        init(context);
    }



    public SlideListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SlideListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public SlideListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        mScreenWidth = displayMetrics.widthPixels;
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                return performActionAsDown(ev);
            case MotionEvent.ACTION_MOVE:
                performActionAsMove(ev);
                break;
            case MotionEvent.ACTION_UP:
                performActionAsUp(ev);
                break;
        }

        return super.onTouchEvent(ev);
    }
/**
 * 1、获取屏幕宽度，绘制当前的子item，使其保持DOWN前的状态
 * 2、判断是否有item已经被选中标记（有Delete控件）;
 *          如果已经有被标记item，判断被标记item是否为当前点击处对应的item？
 *              是：则此时不做处理，
 *              否：则还原被标记item，拦截事件
 *          如果没有，则将当前DOWN处的作为标记item
 * */
    private boolean performActionAsDown(MotionEvent ev) {
        
        downX = (int)ev.getX();
        downY = (int)ev.getY();
        
        ViewGroup tmpItem = (ViewGroup) this.getChildAt(pointToPosition(downX, downY) - getFirstVisiblePosition());
        if (isSelect){
            if (tmpItem != targetItemViewGroup){
                turnToNormal();
                return false;
            }
        }else{
            targetItemViewGroup = tmpItem;
            childOneParams = (LinearLayout.LayoutParams) targetItemViewGroup.getChildAt(CONTENT_POSITION).getLayoutParams();
            childOneParams.width = mScreenWidth;
            targetItemViewGroup.getChildAt(CONTENT_POSITION).setLayoutParams(childOneParams);
            deleteWidth = targetItemViewGroup.getChildAt(DELETE_POSITION).getLayoutParams().width;
        }
        return true;
    }
/**
 * 重置选中item视图，标记判断置位false,选中item置为null
 * */
    public void turnToNormal() {
        if (targetItemViewGroup != null){
            childOneParams.leftMargin = 0;
            targetItemViewGroup.getChildAt(CONTENT_POSITION).setLayoutParams(childOneParams);
            isSelect = false;
        }
    }

    /**通过performActionAsDown后，只有被标记item才有MOVE响应
 * 判断是否有item已经被选中标记（有Delete控件）;
 *          是：判断手势-->向左不处理，向右则修改Child的padding,不得超过padding界线
 *          否：判断手势-->向右不处理，向左则修改Child的padding,不得超过padding界线
 * */
    private void performActionAsMove(MotionEvent ev) {
        if (targetItemViewGroup == null) return;
        int moveX = (int) ev.getX();
        int moveY = (int)ev.getY();
        
        int diffX = moveX - downX;
        int diffY = moveY - downY;
        
        if (isSelect){
            if (diffX > 0  && Math.abs(diffX) > Math.abs(diffY)){   //右滑
                if (diffX > deleteWidth){
                    diffX = deleteWidth;
                }
                childOneParams.leftMargin = diffX - deleteWidth;
            }
        }else {
            if (diffX < 0 && Math.abs(diffX) > Math.abs(diffY)){
                if (Math.abs(diffX) > deleteWidth){
                    diffX = -deleteWidth;
                }
                childOneParams.leftMargin = diffX;
            }
        }
        targetItemViewGroup.getChildAt(CONTENT_POSITION).setLayoutParams(childOneParams);
    }
/**
 * 判断是否有选中标记位？
 *      有：标记item的Delete控件全显示,设置标志位
 *      无：则标记item至为normal，设置标志位
 * */
    private void performActionAsUp(MotionEvent ev) {
        int upX = (int) ev.getX();
        int upY = (int)ev.getY();

        int diffX = upX - downX;
        int diffY = upY - downY;
        if (! isSelect && diffX < 0 && Math.abs(diffX) > Math.abs(diffY)){
            isSelect = true;
            childOneParams.leftMargin = - deleteWidth;
        }else if (isSelect && diffX > 0 && Math.abs(diffX) > Math.abs(diffY)){
            childOneParams.leftMargin = 0;
            isSelect = false;
        }
        targetItemViewGroup.getChildAt(CONTENT_POSITION).setLayoutParams(childOneParams);
    }
}
