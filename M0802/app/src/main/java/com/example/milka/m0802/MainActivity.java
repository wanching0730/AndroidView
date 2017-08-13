package com.example.milka.m0802;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.milka.m0802.Camera.Activity.ImageMainActivity;
import com.example.milka.m0802.ContentResolver.Contact.Activity.ContactMainActivity;
import com.example.milka.m0802.Device.Activity.DeviceInfoMainActivity;
import com.example.milka.m0802.Html.Activity.HtmlMainActivity;
import com.example.milka.m0802.SharedPreferences.Activity.SPMainActivity;

/**
 * Created by Milka on 2017/8/2.
 *
 * Describe:
 * 入口Activity，功能模块选择
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "MainActivity";

    private Button btnImageDirect;
    private Button btnContactDirect;
    private Button btnSharedPreferencesDirect;
    private Button btnHtmlDirect;
    private Button btnDeviceInfoDirect;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        setPropertyValue();
        setOnClickListener();
    }
/**
 * 设置监听
 * */
    private void setOnClickListener() {
        btnImageDirect.setOnClickListener(this);
        btnContactDirect.setOnClickListener(this);
        btnSharedPreferencesDirect.setOnClickListener(this);
        btnHtmlDirect.setOnClickListener(this);
        btnDeviceInfoDirect.setOnClickListener(this);
    }

    private void setPropertyValue() {
    }

    /**
 * 变量初始赋值
 * */
    private void initData() {
        btnContactDirect = (Button) findViewById(R.id.btn_contact_direct);
        btnImageDirect = (Button) findViewById(R.id.btn_image_direct);
        btnSharedPreferencesDirect = (Button) findViewById(R.id.btn_sp_direct);
        btnHtmlDirect = (Button) findViewById(R.id.btn_html_direct);
        btnDeviceInfoDirect = (Button)findViewById(R.id.btn_device_info_direct);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.btn_contact_direct:
                /*系统通讯录*/
                intent = new Intent(this, ContactMainActivity.class);
                break;
            case R.id.btn_image_direct:
                /*照相及照片*/
                intent = new Intent(this, ImageMainActivity.class);
                break;
            case R.id.btn_sp_direct:
                /*SharedPreferences*/
                intent = new Intent(this, SPMainActivity.class);
                break;
            case R.id.btn_html_direct:
                /*Html5*/
                startActivity(new Intent(this, HtmlMainActivity.class));
                break;
            case R.id.btn_device_info_direct:
                /*Device information */
                startActivity(new Intent(this, DeviceInfoMainActivity.class));
                break;
            default:
                break;
        }
        if (intent == null) return;
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
