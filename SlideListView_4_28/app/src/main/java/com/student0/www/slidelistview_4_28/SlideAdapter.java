package com.student0.www.slidelistview_4_28;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by willj on 2017/4/28.
 */

public class SlideAdapter extends BaseAdapter {

    private List<String> list = new ArrayList<>();
    private LayoutInflater inflater;
    private SlideListView listView;

    SlideAdapter(Context context, List<String> data, SlideListView listView){
        list.clear();
        list.addAll(data);
        inflater = LayoutInflater.from(context);
        this.listView = listView;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {

            convertView = inflater.inflate(R.layout.slide_item, parent, false);

            holder = new Holder();
            holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            holder.tv_delete = (TextView) convertView.findViewById(R.id.tv_delete);

            convertView.setTag(holder);
        }else {
            holder = (Holder) convertView.getTag();
        }

        holder.tv_content.setText(getItem(position).toString());
        holder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
                listView.turnToNormal();
                notifyDataSetChanged();
            }
        });
        return convertView;
    }
    class Holder{
        TextView tv_content;
        TextView tv_delete;
    }
}
