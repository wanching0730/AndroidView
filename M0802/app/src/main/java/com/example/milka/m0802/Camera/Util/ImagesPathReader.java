package com.example.milka.m0802.Camera.Util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Milka on 2017/8/5
 *
 * 本地图片路径读取者.
 * 获取所有本地图片的绝对路径
 * 设计模式：单例模式
 *
 * 使用说明：
 * List<String> pathList = ImagesPathReader.getInstance(myContext).getLocalImagesPath();
 *
 */

public class ImagesPathReader {

    private static final String TAG = "ImagesPathReader";

    /*筛选文件的后缀名*/
    private final static String SELECT_IMAGE_TYPE_JPEG = "image/jpeg";
    private final static String SELECT_IMAGE_TYPE_PNG = "image/png";
    private final static String SELECT_IMAGE_TYPE_JPG = "image/jpg";

    private final static String ERROR_NO_OUT_SIDE_STORAGE = "内存卡不存在";

    private static List<String> localImagesPath;

    private static ImagesPathReader mInstance;
    private static Context context;
/**
 * 构造函数
 * */
    private ImagesPathReader(Context context){
        this.context = context.getApplicationContext();
        localImagesPath = initImagesPath();
    }
/**
 * 实例获取
 * */
    public static ImagesPathReader getInstance(Context context){
        if (mInstance == null){
            synchronized (ImagesPathReader.class){
                if (mInstance == null){
                    mInstance = new ImagesPathReader(context);
                }
            }
        }
        return mInstance;
    }
    /**
     * 获取路径核心函数
     * @return 类型为List<String>
     * */
    private static List<String> initImagesPath(){
        List<String> list = new ArrayList<>();
        /*检测外部存储设备是否存在*/
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Log.e(TAG, ERROR_NO_OUT_SIDE_STORAGE);
            return list;
        }
        /*通过ContentProvider，及筛选条件获取目标类型文件路径*/
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver cr = context.getApplicationContext().getContentResolver();
        Cursor cursor = cr.query(uri, null,
                MediaStore.Images.Media.MIME_TYPE + " = ? or "
                        + MediaStore.Images.Media.MIME_TYPE + " = ? or "
                        + MediaStore.Images.Media.MIME_TYPE + " = ? ",
                new String[]{SELECT_IMAGE_TYPE_JPEG, SELECT_IMAGE_TYPE_PNG, SELECT_IMAGE_TYPE_JPG},
                MediaStore.Images.Media.DATE_MODIFIED );
        while (cursor.moveToNext()){
            String path = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Images.Media.DATA));
            list.add(path);
            Log.i(TAG, path);
        }
        cursor.close();
        return list;
    }
    /**
     * 获取路径的唯一接口
     * */
    public List<String> getLocalImagesPath(){
        return localImagesPath;
    }
}
