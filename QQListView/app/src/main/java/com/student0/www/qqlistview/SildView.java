package com.student0.www.qqlistview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * Created by willj on 2017/4/21.
 */

public class SildView extends ListView {

    private final String TAG = "Action -->";

    private final int INFO_POSITION = 0;    //消息显示布局在item中的位置
    private final int DELETE_POSITION = 1;  //删除键布局在item中的位置

    private final int USEFUL_Y = 20;    //滑动时对Y值的标准判断
    private final double STANDARD_SCALE = (1.0/4);     //左滑有效时的最底宽度与删除键的比例

    private int mScreenWidth;   //屏幕宽度

    private int mDownX; //手指首次按下的X坐标（相对于容器，此变量在SildView的onTouchEvent中赋值，所以相对的容器为SlidView）
    private int mDownY; //手指首次按下的Y坐标

    private boolean isDeleteShow;   //“删除”键是否已经显示，最多显示一个删除键
    private ViewGroup mPointChild;  //手指按下地位的item组件
    private boolean isAllowItemClick;
    private int mDeleteWidth; //删除键的宽度
    private LinearLayout.LayoutParams mItemLayoutParams; //手指按下时的item中INFO部分的结构参数


    public SildView(Context context) {
        super(context, null);
    }

    public SildView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SildView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    //初始化
    private void init(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        //A structure describing general information about a display, such as its size, density, and font scaling.
        DisplayMetrics ds = new DisplayMetrics();

        wm.getDefaultDisplay().getMetrics(ds);  //Init 'ds'
        mScreenWidth = ds.widthPixels;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                performActionDown(ev);
                System.out.println(TAG + TouchEventUtil.getTouchAction(ev.getAction()));
                break;
            case MotionEvent.ACTION_MOVE:
                System.out.println(TAG + TouchEventUtil.getTouchAction(ev.getAction()));
                performActionMove(ev);
                break;
            case MotionEvent.ACTION_UP:
                System.out.println(TAG + TouchEventUtil.getTouchAction(ev.getAction()));
                performActionUp(ev);
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void performActionUp(MotionEvent ev) {

        if(isDeleteShow && -mItemLayoutParams.leftMargin <=(mDeleteWidth * (1.0 - STANDARD_SCALE))){    //右滑取消删除显示
            turnNormal();
        } else if (-mItemLayoutParams.leftMargin >= (mDeleteWidth * STANDARD_SCALE)){ //若左滑距离超过STANDARD_SCALE倍则显示删除键显示
            mItemLayoutParams.leftMargin = -mDeleteWidth;
            isDeleteShow = true;
            mPointChild.getChildAt(INFO_POSITION).setLayoutParams(mItemLayoutParams);
        }else {
            turnNormal();
        }
    }

    private boolean performActionMove(MotionEvent ev) {
        int nowX = (int)ev.getX();
        int nowY = (int)ev.getY();

        int diffX = nowX - mDownX;
        int diffY = nowY - mDownY;

        //对手势进行判断与处理
        if (Math.abs(diffX) > Math.abs(diffY) && Math.abs(diffY) < USEFUL_Y){
            if (!isDeleteShow && nowX < mDownX){    //  删除按钮未显示时向左滑
                //若滑动距离大于删除组件的宽度时，重置diffX
                // 根据diffX设置Margin达到移动效果,且不得超过删除组件的最大宽度
                if(-diffX > mDeleteWidth){
                    diffX = -mDeleteWidth;
                }
                //根据diffX设置Margin达到移动效果
                mItemLayoutParams.leftMargin = diffX;
                //调用参数达到试图上的效果
                mPointChild.getChildAt(INFO_POSITION).setLayoutParams(mItemLayoutParams);
                isAllowItemClick = false;
            }else if (isDeleteShow && nowX > mDownX){   //删除按钮显示时向右滑
                if (diffX > mDeleteWidth){
                    diffX = mDeleteWidth;   //确保不越界
                }
                mItemLayoutParams.leftMargin = diffX - mDeleteWidth;
                mPointChild.getChildAt(INFO_POSITION).setLayoutParams(mItemLayoutParams);
                isAllowItemClick = false;
            }
            return true;
        }
        return super.onTouchEvent(ev);
    }

    private void performActionDown(MotionEvent ev) {
        mDownX = (int)ev.getX();
        mDownY = (int) ev.getY();
        //当删除键已经显示，再次点击，判断mPointChild当前待处理item是否与点击的item相同
        //不同则取消“待处理”状态的显示，
        if (isDeleteShow){
            //获取点击处的item对象
            ViewGroup tempViewGroup = (ViewGroup) getChildAt(pointToPosition(mDownX, mDownY) - getFirstVisiblePosition());
            if (!mPointChild.equals(tempViewGroup)){
                turnNormal();
            }
        }

        //获取当前item
//        int i = pointToPosition(mDownX, mDownY);
        mPointChild = (ViewGroup) getChildAt(pointToPosition(mDownX, mDownY) - getFirstVisiblePosition());

        mDeleteWidth = mPointChild.getChildAt(DELETE_POSITION).getLayoutParams().width;
        //设置Action_DOWN时的item视图(只显示INFO部分)
        mItemLayoutParams = (LinearLayout.LayoutParams)mPointChild.getChildAt(INFO_POSITION).getLayoutParams();
        mItemLayoutParams.width = mScreenWidth;
        mPointChild.getChildAt(INFO_POSITION).setLayoutParams(mItemLayoutParams);
        super.onTouchEvent(ev);
    }

    public void turnNormal() {
        mItemLayoutParams.leftMargin = 0;
        mPointChild.getChildAt(INFO_POSITION).setLayoutParams(mItemLayoutParams);
        isDeleteShow = false;
    }
/**
 * 移动过程中忽略其他的处理，比如忽略OnClick时间
 * */
    public boolean isAllowItemClick(){
        return isAllowItemClick;
    }

    public boolean isDeleteShow(){
        return isDeleteShow;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {  //将事物拦截，阻止上级的响应
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                isAllowItemClick = true;
                break;
            case MotionEvent.ACTION_MOVE:
                int nowX = (int)ev.getX();
                int nowY = (int)ev.getY();
                int diffY = nowY - mDownY;
                int diffX = nowX - mDownX;
                if (Math.abs(diffX) > dp2px(4) || Math.abs(diffY) > dp2px(4)) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    public float dp2px(int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getContext().getResources().getDisplayMetrics());
    }

    public void setDeleteShow(boolean v){
        this.isDeleteShow = v;
    }
}
