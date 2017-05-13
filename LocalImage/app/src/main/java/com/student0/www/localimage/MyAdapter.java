package com.student0.www.localimage;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;

import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by willj on 2017/5/9.
 */

public class MyAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    private final static String TAG = "MyAdapter ---";
    private List<String> list = new ArrayList<>();
    private LayoutInflater inflater;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public MyAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
       list.addAll(initPaths());
        Log.i(TAG, "ex");
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener != null){
            onItemClickListener.onItemClick(v, (String) v.getTag());
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view, String path);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(inflater.inflate(R.layout.item_recycle_view, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MyHolder)holder).imageView.setOnClickListener(this);
        Load.getInstance().loadImage(((MyHolder)holder).imageView, list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private List<String> initPaths(){
        List<String> paths = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_UNKNOWN)){
            Log.e(TAG, "no external storage");
            return null;
        }
        Uri mImgUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = cr.query(mImgUri, null,
                MediaStore.Images.Media.MIME_TYPE + " = ? or "
                        + MediaStore.Images.Media.MIME_TYPE + " = ? or "
                        + MediaStore.Images.Media.MIME_TYPE + " = ? ",
                new String[]{"image/jpeg", "image/png", "image/jpg"},
                MediaStore.Images.Media.DATE_MODIFIED );

        while (cursor.moveToNext()){
            String path = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Images.Media.DATA));
            paths.add(path);
            Log.d(TAG, path);
        }
        return paths;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
