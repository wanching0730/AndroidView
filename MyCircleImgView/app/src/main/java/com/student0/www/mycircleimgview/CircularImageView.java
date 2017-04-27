package com.student0.www.mycircleimgview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by willj on 2017/4/27.
 */

public class CircularImageView extends android.support.v7.widget.AppCompatImageView {

    private final String TAG = "CircularImageView-->";

    private final int defaultSize = 50;

    public CircularImageView(Context context) {
        super(context);
    }

    public CircularImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircularImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (drawable == null){
            return;
        }
        if (!(drawable instanceof BitmapDrawable)){
            Log.e(TAG, "drawable should be a BitmapDraw");
            return;
        }


        //必要步骤，避免由于初始化之前导致的异常错误
        if (getHeight() == 0 || getWidth() == 0 ){
            return;
        }

        Bitmap mBmp = ((BitmapDrawable) drawable).getBitmap();
        //Make a Mutable Bitmap from Source_bitmap
        /***** below are the diff between  immutable and mutable******/
        // it's about the same difference as on String vs StringBuilder
        // - String is immutable so you can't change its content (well at least not without any hack),
        // while for StringBuilder you can change its content.
        Bitmap bitmap = mBmp.copy(Bitmap.Config.ARGB_8888, true);   //建立可操作图片对象

        canvas.drawBitmap(getCircleBitMap(bitmap, getHeight()), 0, 0, null);
    }

    private Bitmap getCircleBitMap(Bitmap bitmap, int diameter) {
        Bitmap sBmp;

        float padding_1 = Math.min(getPaddingLeft(), getPaddingRight());
        float padding_2 = Math.min(getPaddingTop(), getPaddingBottom());

        float max_padding = Math.min(padding_1, padding_2);
        //判断是否需要改变可操作图片的大小
        if (bitmap.getHeight() != diameter || bitmap.getWidth() != diameter){
            sBmp = bitmap.createScaledBitmap(bitmap, diameter, diameter, false);
        }else{
            sBmp = bitmap;
        }

        Bitmap outBmp = Bitmap.createBitmap(sBmp.getWidth(), sBmp.getHeight(), Bitmap.Config.ARGB_8888);    //目标图对象

        Canvas canvas = new Canvas(outBmp);
        Rect rect_get = new Rect(0, 0, sBmp.getWidth(), sBmp.getHeight());
        Rect rect_set = new Rect(0, 0, outBmp.getWidth(), outBmp.getHeight());

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setFilterBitmap(true);

        canvas.drawCircle(outBmp.getWidth()/2, outBmp.getHeight()/2, diameter/2 - max_padding, paint);    //首次绘制
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));      //提取模式选择
        canvas.drawBitmap(sBmp, rect_get, rect_set, paint); //二次绘制

        return outBmp;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //保证CircularImageView为正方形
        int min = Math.min(getSize(defaultSize, widthMeasureSpec), getSize(defaultSize, heightMeasureSpec));
       setMeasuredDimension(min, min);
    }

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
