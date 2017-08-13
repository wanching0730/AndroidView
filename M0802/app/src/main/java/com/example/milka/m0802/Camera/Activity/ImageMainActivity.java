package com.example.milka.m0802.Camera.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.milka.m0802.Camera.Util.ImageLoadUtil;
import com.example.milka.m0802.Camera.Util.ImagesPathReader;
import com.example.milka.m0802.R;

/**
 * Created by Milka on 2017/8/2.
 */

public class ImageMainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnImageOperation;
    private Button btnImageResource;
    private Button btnCameraDirect;

    //private ImageView ivTest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_camera);
        ImagesPathReader.getInstance(this).getLocalImagesPath();

        initView();
        initData();
        setProperty();
        setListener();
    }
/**
 * 控件初始化
 *
 * */
    private void initView() {
        //ivTest = (ImageView) findViewById(R.id.iv_test);
        btnImageOperation = (Button) findViewById(R.id.btn_image_operate);
        btnImageResource = (Button) findViewById(R.id.btn_image_resource);
        btnCameraDirect = (Button) findViewById(R.id.btn_camera_direct);
    }
/**
 * 非控件变量初始化
 * */
    private void initData() {
        //ImageLoadUtil.getInstance(3, ImageLoadUtil.LoadType.LIFO).loadImage("/storage/emulated/0/DCIM/Camera/IMG20170729183959.jpg", ivTest);
    }
/**
 * 对象属性设置
 * */
    private void setProperty() {

    }
/**
 * 监听设置
 * */
    private void setListener() {
        btnImageOperation.setOnClickListener(this);
        btnImageResource.setOnClickListener(this);
        btnCameraDirect.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_image_operate:
                /*跳转至图片水印、放大、缩小操作页*/
                startActivity(new Intent(this, ImageOperationActivity.class));
                break;
            case R.id.btn_image_resource:
                /*跳转至本地图片资源选择页*/
                startActivity(new Intent(this, ImageResourceActivity.class));
                break;
            case R.id.btn_camera_direct:
                /*跳转至相机页*/
                startActivity(new Intent(this, TakeCameraActivity.class));
                break;
            default:
                break;
        }
    }
}
