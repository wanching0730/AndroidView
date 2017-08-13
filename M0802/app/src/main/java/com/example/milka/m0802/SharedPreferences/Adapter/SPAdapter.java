package com.example.milka.m0802.SharedPreferences.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.milka.m0802.R;
import com.example.milka.m0802.SharedPreferences.Activity.SPOperationActivity;
import com.example.milka.m0802.SharedPreferences.Util.SPBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Milka on 2017/8/3.
 *
 * SharedPreference 展示列表适配器
 */

public class SPAdapter extends BaseAdapter {

    private List<SPBean> data = new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;

    public SPAdapter(List<SPBean> data, Context context) {
        this.data.clear();
        this.data.addAll(data);
        this.context = context;
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
            convertView = inflater.inflate(R.layout.sp_item_list, parent, false);

            holder = new Holder();
            holder.tvKey = (TextView) convertView.findViewById(R.id.tv_sp_key);
            holder.tvValue = (TextView)convertView.findViewById(R.id.tv_sp_value);
            convertView.setTag(holder);
        }else {
            holder = (Holder) convertView.getTag();
        }
        holder.tvKey.setText(((SPBean)getItem(position)).getKey());
        holder.tvValue.setText(((SPBean)getItem(position)).getValue());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*item被点击，跳转到具体删查改页*/
                SPOperationActivity.setOperation(context, (SPBean) getItem(position));
                Activity activity = (Activity)context;
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        return convertView;
    }

    public void setListData(List<SPBean> beanList) {
        data.clear();
        data.addAll(beanList);
    }
}

class Holder{
    TextView tvKey;
    TextView tvValue;
}
