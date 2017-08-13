package com.example.milka.m0802.ContentResolver.Contact.Activity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.milka.m0802.ContentResolver.Contact.Util.ContactBean;
import com.example.milka.m0802.ContentResolver.Contact.Util.ContactConstant;
import com.example.milka.m0802.ContentResolver.Contact.Util.ContactUtil;
import com.example.milka.m0802.R;

/**
 * Created by Milka on 2017/8/2.
 *
 * 新填联系人Activity
 */

public class ContactAddActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtPersonName;
    private EditText edtPersonPhoneNum;
    private Button btnAddSubmit;
    private ContactUtil contactUtil;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_add);
        contactUtil = ContactUtil.getInstance(this);

        initData();
        setPropertyValue();
        setOnClickListener();
    }
    /**
     * 变量初始赋值
     * */
    private void initData(){
        edtPersonName = (EditText) findViewById(R.id.edt_new_person_name);
        edtPersonPhoneNum = (EditText) findViewById(R.id.edt_new_phone);
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
                String personName = edtPersonName.getText().toString();
                String personPhone = edtPersonPhoneNum.getText().toString();
                if (personName.isEmpty() || personPhone.isEmpty()){
                    Toast.makeText(this, ContactConstant.WARN_ILLEGAL_INPUT, Toast.LENGTH_SHORT).show();
                    return;
                }
                contactUtil.addContact(new ContactBean(personName, personPhone));
                String msg = ContactConstant.SUCCESS_SAVE;
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
        /*退出动画*/
        overridePendingTransition(R.anim.slide_in_buttom, R.anim.slide_out_head);
    }
}
