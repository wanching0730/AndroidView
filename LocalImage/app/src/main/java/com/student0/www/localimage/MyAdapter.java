package com.student0.www.localimage;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by willj on 2017/5/9.
 */

public class MyAdapter extends RecyclerView.Adapter {

    private final static String TAG = "MyAdapter ---";
    private List<String> list = new ArrayList<>();
    private Context context;

    public MyAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //((MyHolder)holder).imageView
        Load.getInstance().loadImage(((MyHolder)holder).imageView, list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private List<String> init(){
        List<String> paths = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_UNKNOWN)){
            Log.e(TAG, "no external storage");
            return null;
        }

        Cursor cursor = cr.query(Uri.parse("content://media/internal/images"), null, null, null, null);

        while (cursor.moveToNext()){
            String path = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Images.Media.DATA));
            paths.add(path);
        }
        return paths;
    }
}
