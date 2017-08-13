package com.example.milka.m0802.ContentResolver.Contact.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.LayoutInflaterFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.milka.m0802.ContentResolver.Contact.Activity.ContactOperationActivity;
import com.example.milka.m0802.ContentResolver.Contact.Util.ContactBean;
import com.example.milka.m0802.R;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Milka on 2017/8/2.
 *
 * 通讯录适配器
 */

public class ContactListAdapter extends BaseAdapter {

    private List<ContactBean> data = new ArrayList<>();
    private LayoutInflater inflater;
    private Context context;
/**
 * 适配器构造函数
 *
 * @param data 通讯录数据源
 * @param context 组件的上下文
 * */
    public ContactListAdapter(List<ContactBean> data, Context context) {
        this.data.clear();
        this.data.addAll(data);
        this.context = context.getApplicationContext();
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.contact_item_contact_list, parent, false);
            holder = new Holder();
            holder.tvPersonName = (TextView) convertView.findViewById(R.id.tv_person_name);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }
        holder.tvPersonName.setText(((ContactBean)getItem(position)).getPersonName());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactOperationActivity.setOperation(context, data.get(position));
            }
        });
        return convertView;
    }

    public void setData(List<ContactBean> contactBeen) {
        this.data.clear();
        this.data.addAll(contactBeen);
    }
}
/**
 * ListView优化
 * */
class Holder{
    TextView tvPersonName;
    TextView tvPhoneNumber;
}