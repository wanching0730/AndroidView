package com.example.milka.m0802.ContentResolver.Contact.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListViewCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.milka.m0802.ContentResolver.Contact.Adapter.ContactListAdapter;
import com.example.milka.m0802.ContentResolver.Contact.Util.ContactBean;
import com.example.milka.m0802.ContentResolver.Contact.Util.ContactUtil;
import com.example.milka.m0802.R;

/**
 * Created by Milka on 2017/8/2.
 *
 * 入口活动，模块选择引导
 */

public class ContactMainActivity extends AppCompatActivity {

    private ContactUtil contactUtil;
    private ListView lvContactList;
    private ContactListAdapter adapter;

    private int CURRENT_LISTVIEW_ITEM_POSITION = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_contact);

        initData();
        setPropertyValue();
        setOnClickListener();
    }
    /**
     * 点击事件
     * */
    private void setOnClickListener() {

    }
    /**
     * 参数及属性设置
     * */
    private void setPropertyValue() {
        lvContactList.setAdapter(adapter);
    }
    /**
     * 数据初始赋值
     * */
    private void initData() {
        lvContactList = (ListView) findViewById(R.id.lv_contact_list);
        contactUtil = ContactUtil.getInstance(this);
        adapter = new ContactListAdapter(contactUtil.readContacts(), this);
        //contactUtil.readContacts();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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
                Intent intent = new Intent(this, ContactAddActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
/**
 * 重新激活后更新适配器并刷新UI
 * */
    @Override
    protected void onRestart() {
        super.onRestart();
        adapter.setData(contactUtil.readContacts());
        adapter.notifyDataSetInvalidated();
        lvContactList.setSelection(CURRENT_LISTVIEW_ITEM_POSITION);//回到原来的位置
    }

    @Override
    protected void onPause() {
        super.onPause();
        CURRENT_LISTVIEW_ITEM_POSITION = lvContactList.getFirstVisiblePosition();//得到当前ListView的第一个
    }
}
