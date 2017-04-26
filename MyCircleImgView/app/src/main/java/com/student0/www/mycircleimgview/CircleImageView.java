package com.student0.www.mycircleimgview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by willj on 2017/4/25.
 */
public class CircleImageView extends AppCompatImageView {

    private final String TAG = "CircleImageView-->";

    public CircleImageView(Context context) {
        super(context, null);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        //空值判断，必要步骤，避免由于没有设置src导致的异常错误
        if (drawable == null) {
            return;
        }

        //必要步骤，避免由于初始化之前导致的异常错误
        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }
        if (!(drawable instanceof BitmapDrawable)){
            Log.e(TAG, "drawable should be a BitmapDraw");
            return;
        }
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();

        //Make a Mutable Bitmap from Source_bitmap
        /***** below are the diff between  immutable and mutable******/
        // it's about the same difference as on String vs StringBuilder
        // - String is immutable so you can't change its content (well at least not without any hack),
        // while for StringBuilder you can change its content.
        Bitmap b = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        Bitmap roundBitmap = getCroppedBitmap(b, getWidth());
        canvas.drawBitmap(roundBitmap, 0, 0, null);
    }

    private Bitmap getCroppedBitmap(Bitmap bitmap, int radius){

        Bitmap sbmp;    //待画图，原始图为正方形？图像本身：改变成正方后的图片

        if (bitmap.getWidth() != radius || bitmap.getHeight() != radius){
            float minMeasure = Math.min(bitmap.getHeight(), bitmap.getWidth());
            float scale = minMeasure/radius;
            sbmp = Bitmap.createScaledBitmap(bitmap, radius, radius, false);
        }else{
            sbmp = bitmap;
        }
        //目标画布
        Bitmap outbmp = Bitmap.createBitmap(sbmp.getWidth(), sbmp.getHeight(),
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(outbmp);
        final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());

        Paint paint = new Paint();

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawARGB(0, 0, 0, 0);

        canvas.drawCircle(sbmp.getWidth() / 2 ,
                sbmp.getHeight() / 2 , radius/2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        // 大于src则把src的裁截区放大，
        //小于src则把src的裁截区缩小。
        canvas.drawBitmap(sbmp, rect, rect, paint);
        return outbmp;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int min = Math.min(width, height);
        setMeasuredDimension(min, min);
    }
}

