package com.example.milka.m0802.ContentResolver.Contact.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.milka.m0802.ContentResolver.Contact.Util.ContactBean;
import com.example.milka.m0802.ContentResolver.Contact.Util.ContactConstant;
import com.example.milka.m0802.ContentResolver.Contact.Util.ContactUtil;
import com.example.milka.m0802.R;

/**
 * Created by Milka on 2017/8/3.
 */

public class ContactOperationActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "OperationDetailActivity";

    /**
     * 外部通过setOperation启用本OperationDetailActivity
     *
     * @param fromContext 起始Context
     * @param targetContactBean 传入的ContactBean对象
     * */
    public static void setOperation(Context fromContext, @Nullable ContactBean targetContactBean){

        if (targetContactBean.getPersonName() == null || targetContactBean.getPhoneNum() == null) return;
        /*参数传入*/
        Intent intent = new Intent(fromContext, ContactOperationActivity.class);
        intent.putExtra(ContactConstant.LIST_PERSON_NAME_KEY, targetContactBean.getPersonName());
        intent.putExtra(ContactConstant.LIST_PERSON_PHONE_NUM_KEY, targetContactBean.getPhoneNum());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        fromContext.startActivity(intent);
    }
    private ContactBean oldContactBean;
    private EditText edtTargetName;
    private EditText edtTargetPhone;

    private Button btnDelete;
    private Button btnEditWithSave;

    private Boolean isEditStatus = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_operation);
        initData();
        setPropertyValue();
        setOnClickListener();
    }
    /**
     * 事件监听
     * */
    private void setOnClickListener() {
        btnDelete.setOnClickListener(this);
        btnEditWithSave.setOnClickListener(this);
    }
    /**
     * 属性动态配置
     * */
    private void setPropertyValue() {
        /*设置输入不可编辑*/
//        edtTargetName.setFocusable(false);
//        edtTargetPhone.setFocusable(false);
        edtTargetName.setEnabled(false);
        edtTargetPhone.setEnabled(false);

        edtTargetName.setText(oldContactBean.getPersonName().toString());
        edtTargetPhone.setText(oldContactBean.getPhoneNum().toString());

    }
    /**
     * 变量初始化赋值
     * */
    private void initData(){
        /*参数解析与获取*/
        Intent receiveIntent = getIntent();
        String personName = receiveIntent.getStringExtra(ContactConstant.LIST_PERSON_NAME_KEY);
        String personPhone = receiveIntent.getStringExtra(ContactConstant.LIST_PERSON_PHONE_NUM_KEY);
        oldContactBean = new ContactBean(personName, personPhone);

        edtTargetName = (EditText) findViewById(R.id.edt_target_name);
        edtTargetPhone = (EditText)findViewById(R.id.edt_target_phone);

        btnDelete = (Button) findViewById(R.id.btn_delete);
        btnEditWithSave = (Button) findViewById(R.id.btn_edit);

        Log.d(TAG, oldContactBean.getPersonName());

    }

    @Override
    public void onBackPressed() {
        if (isEditStatus){
            this.isEditStatus = !this.isEditStatus;
            edtTargetName.setEnabled(isEditStatus? true:false);
            edtTargetPhone.setEnabled(isEditStatus? true:false);
            edtTargetName.setText(oldContactBean.getPersonName());
            edtTargetPhone.setText(oldContactBean.getPhoneNum());
            btnDelete.setVisibility(isEditStatus? View.INVISIBLE:View.VISIBLE);

            btnEditWithSave.setText(
                    isEditStatus?
                            getString(R.string.btn_txt_save)
                            :getString(R.string.btn_txt_edit)
            );
            return;
        }
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_delete:
                /*delete the select date*/
                String personName = edtTargetName.getText().toString();
                String personPhone = edtTargetPhone.getText().toString();
                if (personName != null){
                    ContactUtil contactUtil = ContactUtil.getInstance(this);
                    //contactUtil.deleteContact(personName);
                    contactUtil.deleteContactBean(new ContactBean(personName, personPhone));
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }else{
                    Toast.makeText(this, ContactConstant.ERROR_PERSON_NAME_NULL, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_edit:
                /*edit the select data*/
                this.isEditStatus = !this.isEditStatus;
                edtTargetName.setEnabled(isEditStatus? true:false);
                edtTargetPhone.setEnabled(isEditStatus? true:false);

                btnDelete.setVisibility(isEditStatus? View.INVISIBLE:View.VISIBLE);

                btnEditWithSave.setText(
                        isEditStatus?
                                getString(R.string.btn_txt_save)
                                :getString(R.string.btn_txt_edit)
                );
                /*修改操作*/
                if (!isEditStatus){
                    ContactBean newBean = new ContactBean(
                            edtTargetName
                                    .getText().toString(),
                            edtTargetPhone
                                    .getText().toString()
                    );
                    ContactUtil contactUtil = ContactUtil.getInstance(this);
                    contactUtil.updateContact(oldContactBean, newBean);
                    //contactUtil.editContact(oldContactBean, newBean);
                }
                break;
            default:
                break;
        }
    }
}
