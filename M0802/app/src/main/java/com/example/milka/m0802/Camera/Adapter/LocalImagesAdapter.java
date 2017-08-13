package com.example.milka.m0802.Camera.Adapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.milka.m0802.Camera.Activity.ImageResourceActivity;
import com.example.milka.m0802.Camera.Util.ImageLoadBase64;
import com.example.milka.m0802.Camera.Util.ImageLoadUtil;
import com.example.milka.m0802.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Milka on 2017/8/7.
 *
 * RecycleView适配器
 */

public class LocalImagesAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    private LayoutInflater inflater;
    private List<String> pathList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    /**
     * 设置回调，传递图像信息
     * */
    public interface OnItemClickListener{
        void onItemClick(View view, String path);
    }
    /**
     * 构造函数
     * @param context 适配器对应控件所在的上下文
     * @param pathList 图片绝对路径的List集
     * */
    public LocalImagesAdapter(Context context, List<String> pathList) {
        inflater = LayoutInflater.from(context);
        this.pathList.clear();
        this.pathList.addAll(pathList);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(inflater.inflate(R.layout.item_image_local_images, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((Holder)holder).imageView.setOnClickListener(this);
        ImageLoadUtil.getInstance(3, ImageLoadUtil.LoadType.LIFO).loadImage(pathList.get(position),  ((Holder)holder).imageView);
    }

    @Override
    public int getItemCount() {
        return pathList.size();
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener != null){
            onItemClickListener.onItemClick(v, (String) v.getTag());
        }
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    class Holder extends RecyclerView.ViewHolder{

        ImageView imageView;

        public Holder(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.iv_item_local);
        }
    }
}
