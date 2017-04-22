package com.student0.www.slidview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by willj on 2017/4/22.
 */

public class SlidAdapter extends BaseAdapter {

    private List<String> data = new ArrayList<>();
    private LayoutInflater inflater;

    public SlidAdapter(Context context, List<String> data){
        inflater = LayoutInflater.from(context);
        this.data = data;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item, parent, false);
            vh = new ViewHolder();
            vh.textView = (TextView) convertView.findViewById(R.id.textview);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }
        vh.textView.setText(data.get(position));
        return convertView;
    }
}

class ViewHolder{
    TextView textView;
}
