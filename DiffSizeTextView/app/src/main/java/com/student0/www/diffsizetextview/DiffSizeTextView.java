package com.student0.www.diffsizetextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Dimension;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by willj on 2017/4/29.
 */

public class DiffSizeTextView extends View {

    private final String TAG = "DiffSizeTextView ---- ";

    private final String CLASS_MATCH_ERROR = "class don't match";

    private int charCount;  //前半部分String的length()
    private float specialSize;  //前半部String的textSize
    private int gravity;
    private float normalSize;   //后半部分String的textSize
    private String text;    //内容
    private float gap;  //前后内容的间隔

    private final int DEFAULT_TEXT_SIZE = 20;

    public DiffSizeTextView(Context context) {
        super(context);
    }


    public DiffSizeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DiffSizeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs) {

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.DiffSizeTextView);
        charCount = array.getInteger(R.styleable.DiffSizeTextView_char_count, 0);
        specialSize = array.getDimensionPixelSize(R.styleable.DiffSizeTextView_special_size, DEFAULT_TEXT_SIZE);
        gravity = array.getInteger(R.styleable.DiffSizeTextView_gravity, SelfGravity.CENTER_VERTICAL);
        normalSize = array.getDimensionPixelSize(R.styleable.DiffSizeTextView_normal_size, DEFAULT_TEXT_SIZE);
        text = array.getString(R.styleable.DiffSizeTextView_text);
        gap  = array.getDimensionPixelSize(R.styleable.DiffSizeTextView_gap, DEFAULT_TEXT_SIZE);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        paint.setTextSize(specialSize);


        Paint.FontMetrics fontMetrics = paint.getFontMetrics();

        Rect rect_special = new Rect();
        Rect rect_normal = new Rect();
        if (text == null) return;
        paint.getTextBounds(text, 0, charCount, rect_special);    //测量特殊字符的Rect占位

        float baseline_special = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2
                            - fontMetrics.top;
        if (charCount != 0 ){
            canvas.drawText(
                    text.substring(0, charCount),
                    getPaddingLeft(), baseline_special ,
                    paint);    //画特殊字符
        }
        paint.setTextSize(normalSize);
        paint.getTextBounds(text, charCount, text.length(), rect_normal);
        fontMetrics = paint.getFontMetrics();
        float baseline_normal = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2
                - fontMetrics.top;
        canvas.drawText(
                text.substring(charCount, text.length()),
                getPaddingLeft() + rect_special.width() + gap,
                baseline_normal , paint);
    }


}
class SelfGravity{
    public static final int TOP = 1;
    public static final int CENTER = 2;
    public static final int BOTTOM = 3;
    public static final int CENTER_HORIZONTAL = 4;
    public static final int CENTER_VERTICAL = 5;
        }