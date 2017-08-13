package com.example.milka.m0802.SharedPreferences.Activity;

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

import com.example.milka.m0802.ContentResolver.Contact.Activity.ContactOperationActivity;
import com.example.milka.m0802.ContentResolver.Contact.Util.ContactBean;
import com.example.milka.m0802.ContentResolver.Contact.Util.ContactConstant;
import com.example.milka.m0802.ContentResolver.Contact.Util.ContactUtil;
import com.example.milka.m0802.R;
import com.example.milka.m0802.SharedPreferences.Util.SPBean;
import com.example.milka.m0802.SharedPreferences.Util.SharedPreferencesSimpleUtil;

/**
 * Created by Milka on 2017/8/3.
 */

public class SPOperationActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "SPOperationActivity";

    /**
     * 外部通过setOperation启用本Activity
     *
     * @param fromContext 起始Context
     * @param targetSPBean 传入的Bean对象
     * */
    public static void setOperation(Context fromContext, @Nullable SPBean targetSPBean){
        /*参数传入*/
        Intent intent = new Intent(fromContext, SPOperationActivity.class);
        intent.putExtra(ContactConstant.LIST_PERSON_NAME_KEY, targetSPBean.getKey());
        intent.putExtra(ContactConstant.LIST_PERSON_PHONE_NUM_KEY, targetSPBean.getValue());
        fromContext.startActivity(intent);
    }
    private SPBean oldSPBean;
    private EditText edtTargetKey;
    private EditText edtTargetValue;

    private Button btnDelete;
    private Button btnEditWithSave;

    private Boolean isEditStatus = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp_operation);
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
//        edtTargetKey.setFocusable(false);
//        edtTargetValue.setFocusable(false);
        edtTargetKey.setEnabled(false);
        edtTargetValue.setEnabled(false);

        edtTargetKey.setText(oldSPBean.getKey().toString());
        edtTargetValue.setText(oldSPBean.getValue().toString());

    }
    /**
     * 变量初始化赋值
     * */
    private void initData(){
        /*参数解析与获取*/
        Intent receiveIntent = getIntent();
        String personName = receiveIntent.getStringExtra(ContactConstant.LIST_PERSON_NAME_KEY);
        String personPhone = receiveIntent.getStringExtra(ContactConstant.LIST_PERSON_PHONE_NUM_KEY);
        oldSPBean = new SPBean(personName, personPhone);

        edtTargetKey = (EditText) findViewById(R.id.edt_target_name);
        edtTargetValue = (EditText)findViewById(R.id.edt_target_phone);

        btnDelete = (Button) findViewById(R.id.btn_delete);
        btnEditWithSave = (Button) findViewById(R.id.btn_edit);

        Log.d(TAG, oldSPBean.getValue());

    }

    @Override
    public void onBackPressed() {
        if (isEditStatus){
            this.isEditStatus = !this.isEditStatus;
            edtTargetKey.setEnabled(isEditStatus? true:false);
            edtTargetValue.setEnabled(isEditStatus? true:false);
            edtTargetKey.setText(oldSPBean.getKey());
            edtTargetValue.setText(oldSPBean.getValue());
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
                String key = edtTargetKey.getText().toString();
                if (key != null){
                    SharedPreferencesSimpleUtil util = new SharedPreferencesSimpleUtil(this);
                    util.delete(key);
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }else{
                    Toast.makeText(this, ContactConstant.ERROR_PERSON_NAME_NULL, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_edit:
                /*edit the select data*/
                this.isEditStatus = !this.isEditStatus;
                edtTargetKey.setEnabled(isEditStatus? true:false);
                edtTargetValue.setEnabled(isEditStatus? true:false);

                btnDelete.setVisibility(isEditStatus? View.INVISIBLE:View.VISIBLE);

                btnEditWithSave.setText(
                        isEditStatus?
                                getString(R.string.btn_txt_save)
                                :getString(R.string.btn_txt_edit)
                );
                /*修改操作*/
                if (!isEditStatus){
                    SPBean newBean = new SPBean(
                            edtTargetKey
                                    .getText().toString(),
                            edtTargetValue
                                    .getText().toString()
                    );
                    SharedPreferencesSimpleUtil util = new SharedPreferencesSimpleUtil(this);
                    util.edit(oldSPBean, newBean);
                }
                break;
            default:
                break;
        }
    }
}
