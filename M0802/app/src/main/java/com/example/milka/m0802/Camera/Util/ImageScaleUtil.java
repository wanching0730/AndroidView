package com.example.milka.m0802.Camera.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.widget.ImageView;

/**
 * Created by Milka on 2017/8/3.
 *
 * bitmap图像放缩工具
 */

public class ImageScaleUtil {
    /**
     * @param sourceBitmap 需要压缩的ImageView
     * @param reqWidth 需求的Bitmap高度
     * @param reqHeight 需求的Bitmap高度
     * */
    public static Bitmap getScaleBitmap(Bitmap sourceBitmap, int reqWidth, int reqHeight){
        int sourceWidth = sourceBitmap.getWidth();
        int sourceHeight = sourceBitmap.getHeight();
        /*压缩比*/
        float scaleWidth = ((float)reqWidth)/sourceWidth;
        float scaleHeight = ((float)reqHeight)/sourceHeight;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap newBitMap = Bitmap.createBitmap(sourceBitmap, 0, 0, sourceWidth, sourceHeight,
                matrix, true);

      return newBitMap;
    };
}
