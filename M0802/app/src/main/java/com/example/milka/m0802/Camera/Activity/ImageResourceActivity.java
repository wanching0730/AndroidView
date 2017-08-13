package com.example.milka.m0802.Camera.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import com.example.milka.m0802.Camera.Adapter.LocalImagesAdapter;
import com.example.milka.m0802.Camera.Util.ImagesPathReader;
import com.example.milka.m0802.R;

/**
 * Created by Milka on 2017/8/7.
 *
 * 加载、选择本地资源主页
 */

public class ImageResourceActivity extends AppCompatActivity {

    private final String TAG = "ImageResourceActivity";
    private RecyclerView recyclerViewLocalImages;
    private LocalImagesAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_resource);

        initView();
        initData();
        setProperty();
        setListener();
    }

    private void initView() {
        recyclerViewLocalImages = (RecyclerView) findViewById(R.id.recycleView_local_images);
    }

    private void initData() {
        adapter = new LocalImagesAdapter(this, ImagesPathReader.getInstance(this).getLocalImagesPath());
    }

    private void setProperty() {
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        recyclerViewLocalImages.setLayoutManager(manager);
        recyclerViewLocalImages.setAdapter(adapter);


    }

    private void setListener() {
        adapter.setOnItemClickListener(new LocalImagesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, String path) {
                Log.i(TAG, path);
                Toast.makeText(ImageResourceActivity.this, path, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
