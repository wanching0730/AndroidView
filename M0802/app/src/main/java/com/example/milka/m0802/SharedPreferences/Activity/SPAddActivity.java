package com.example.milka.m0802.SharedPreferences.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.milka.m0802.R;
import com.example.milka.m0802.SharedPreferences.Util.SPBean;
import com.example.milka.m0802.SharedPreferences.Util.SPConstant;
import com.example.milka.m0802.SharedPreferences.Util.SharedPreferencesSimpleUtil;

/**
 * Created by Milka on 2017/8/3.
 */

public class SPAddActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtPersonName;
    private EditText edtPersonPhoneNum;
    private Button btnAddSubmit;
    private SharedPreferencesSimpleUtil util;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp_add_element);

        initData();
        setPropertyValue();
        setOnClickListener();
    }
    /**
     * 变量初始赋值
     * */
    private void initData(){
        util = new SharedPreferencesSimpleUtil(this);

        edtPersonName = (EditText) findViewById(R.id.edt_add_name);
        edtPersonPhoneNum = (EditText) findViewById(R.id.edt_add_phone);
        btnAddSubmit = (Button) findViewById(R.id.btn_add_submit);
    }
    /**
     * 动态属性设置
     * */
    private void setPropertyValue(){

    }
    /**
     * 监听设置
     * */
    private void setOnClickListener(){
        btnAddSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add_submit:
                /*save the data*/
                String key = edtPersonName.getText().toString();
                String value = edtPersonPhoneNum.getText().toString();
                if (value.isEmpty() || key.isEmpty()){
                    Toast.makeText(this, SPConstant.WARN_ILLEGAL_INPUT, Toast.LENGTH_SHORT).show();
                    return;
                }
                util.add(new SPBean(key, value));
                String msg = SPConstant.SUCCESS_SAVE;
                Toast.makeText(this,msg, Toast.LENGTH_LONG).show();
                onBackPressed();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_buttom, R.anim.slide_out_head);
    }
}
