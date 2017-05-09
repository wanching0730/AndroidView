package com.student0.www.localimage;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by willj on 2017/5/9.
 */

public class MyCache {

    private LruCache<String, Bitmap> cache;
    private static MyCache mInstance;
    private final int PER_FOR = 4;

    public static MyCache getInstacne(){
        if (mInstance == null){
            synchronized (MyCache.class){
                if (mInstance == null){
                    mInstance = new MyCache();
                }
            }
        }
        return mInstance;
    }

    public MyCache() {

        int max = (int) Runtime.getRuntime().maxMemory();
        int size = max/PER_FOR;

        cache = new LruCache<String, Bitmap>(size){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return super.sizeOf(key, value);
            }
        };
    }

    public LruCache<String, Bitmap> getCache(){
        return cache;
    }
}
