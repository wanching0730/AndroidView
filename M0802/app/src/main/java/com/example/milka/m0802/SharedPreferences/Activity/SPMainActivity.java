package com.example.milka.m0802.SharedPreferences.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.milka.m0802.R;
import com.example.milka.m0802.SharedPreferences.Adapter.SPAdapter;
import com.example.milka.m0802.SharedPreferences.Util.SPBean;
import com.example.milka.m0802.SharedPreferences.Util.SharedPreferencesSimpleUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Milka on 2017/8/2
 *
 * SharedPreference入口.
 */

public class SPMainActivity extends AppCompatActivity {

    private SharedPreferencesSimpleUtil simpleUtil;
    private ListView lvSharedPreference;
    private SPAdapter spAdapter;
    //private List<SPBean> beanList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_shared_preferences);

        initData();
        setAdapter();
        setClickListener();
    }
    /**
     * 数据初始化
     **/
    private void initData(){
        simpleUtil = new SharedPreferencesSimpleUtil(this);
        lvSharedPreference = (ListView)findViewById(R.id.lv_sp_list);
        spAdapter = new SPAdapter(simpleUtil.listData(), this);
    }

    private void setAdapter(){
        lvSharedPreference.setAdapter(spAdapter);
    }
    /**
     *设置监听
     **/
    private void setClickListener(){
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contact_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_contact_add:
                Intent intent = new Intent(this, SPAddActivity.class);
                this.startActivity(intent);
                overridePendingTransition(R.anim.slide_in_head, R.anim.slide_out_buttom);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        spAdapter.setListData(simpleUtil.listData());
        spAdapter.notifyDataSetInvalidated();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
