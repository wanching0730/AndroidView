package com.student0.www.localimage;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by willj on 2017/5/9.
 */

public class MyHolder extends RecyclerView.ViewHolder {
    ImageView imageView;

    public MyHolder(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.item);
    }

}
