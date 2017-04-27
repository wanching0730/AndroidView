package com.student0.www.qqlistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by willj on 2017/4/21.
 */

public class MyAdapter extends BaseAdapter {

    private   List<Integer> data = new ArrayList<>();


    private LayoutInflater mInflater;
    private SildView ls;

    public MyAdapter(Context context, SildView listView) {
        for(int i = 0; i < 20; i ++){
            data.add(i);
        }
        mInflater = LayoutInflater.from(context);
        this.ls = listView;
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
        ViewHolder v = null;
        if (convertView == null){
            convertView = mInflater.inflate(R.layout.item, parent, false);
            v = new ViewHolder();
            v.textView = (TextView) convertView.findViewById(R.id.textView);
            v.tv_delete = (TextView) convertView.findViewById(R.id.tv_delete);
            convertView.setTag(v);
        }else{
            v = (ViewHolder) convertView.getTag();
        }
        v.textView.setText(data.get(position).toString());
        v.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.tv_delete){
                    data.remove(position);
                    notifyDataSetChanged();
                    listTurnNormal();
                }
            }
        });
        return convertView;
    }

    private void listTurnNormal() {
        this.ls.turnNormal();
    }

    public void removeData(int position){
        this.data.remove(position);
    }

    public List<Integer> getData(){
        return this.data;
    }

}
class ViewHolder{
    TextView textView;
    TextView tv_delete;
}
