package com.example.milka.m0802.Camera.Activity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.milka.m0802.Camera.Util.ImageScaleUtil;
import com.example.milka.m0802.Camera.Util.ImageWaterMarkUtil;
import com.example.milka.m0802.R;

/**
 * Created by Milka on 2017/8/7.
 *
 * 图片变换活动
 * 变换包括： 放大、缩小、水印
 */

public class ImageOperationActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivOperateImage;
    private EditText edtWaterMarkText;
    private Button btnWaterMark;
    private Button btnGrow;
    private Button btnShrink;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_operate);

        initView();
        initData();
        setProperty();
        setListener();
    }

    /**
     * 控件初始化
     * */
    private void initView() {
        ivOperateImage = (ImageView) findViewById(R.id.iv_operate_image);
        edtWaterMarkText = (EditText) findViewById(R.id.edt_image_water_mark_txt);
        btnWaterMark = (Button) findViewById(R.id.btn_image_water_mark);
        btnShrink = (Button) findViewById(R.id.btn_image_shrink);
        btnGrow = (Button) findViewById(R.id.btn_image_grow);
    }
/**
 * 非控件变量初始化
 * */
    private void initData() {

    }
/**
 * 对象变量设置
 * */
    private void setProperty() {

    }
/**
 * 监听设置
 * */
    private void setListener() {
        btnWaterMark.setOnClickListener(this);
        btnGrow.setOnClickListener(this);
        btnShrink.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_image_water_mark:
                /*水印*/
                String mark = edtWaterMarkText.getText().toString() + "";
                ivOperateImage.setImageBitmap(ImageWaterMarkUtil.createWatermark(getBitmapFromImageView(ivOperateImage), mark));
                break;
            case R.id.btn_image_shrink:
                /*缩小*/
                ivOperateImage.setImageBitmap(ImageScaleUtil
                        .getScaleBitmap(getBitmapFromImageView(ivOperateImage),
                                (int)(getBitmapFromImageView(ivOperateImage).getWidth()* 0.8),
                                (int)(getBitmapFromImageView(ivOperateImage).getHeight() * 0.8)));
                break;
            case R.id.btn_image_grow:
                /*放大*/
                ivOperateImage.setImageBitmap(ImageScaleUtil
                        .getScaleBitmap(getBitmapFromImageView(ivOperateImage),
                                (int)(getBitmapFromImageView(ivOperateImage).getWidth()* 1.2),
                                (int)(getBitmapFromImageView(ivOperateImage).getHeight() * 1.2)));
                break;
            default:
                break;
        }
    }
/**
 * 从ImageView获取Bitmap对象
 * */
    private Bitmap getBitmapFromImageView(ImageView imageView){
        Bitmap bitmap=((BitmapDrawable)imageView.getDrawable()).getBitmap();
        return bitmap;
    }
}
