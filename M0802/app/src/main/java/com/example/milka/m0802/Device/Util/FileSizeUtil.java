package com.example.milka.m0802.Device.Util;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * Created by Milka on 2017/8/8.
 *
 * 文件大小测量工具
 *
 * 权限：
 * <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"></uses-permission>
 */

public class FileSizeUtil {

    public final static int ERROR = -1;
    /**
     * @return 外存存在?true:false
     * */
    public static boolean externalStorageAvailable(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
    /**
     * 获取内存总容量
     * */
    public static long getTotalInsertMemorySize(){
        File path = Environment.getDataDirectory();
        StatFs statFs = new StatFs(path.getPath());
        long block = statFs.getBlockSizeLong();
        long blockCount = statFs.getBlockCountLong();
        return block * blockCount;
    }

    /**
     * 获取SDCARD总存储空间
     * */
    public static long getTotalExternalMemorySize(){
        if (externalStorageAvailable()){
            File path = Environment.getExternalStorageDirectory();
            StatFs statFs = new StatFs(path.getPath());
            long block = statFs.getBlockSizeLong();
            long blockCount = statFs.getBlockCountLong();
            return block * blockCount;
        }else{
            return ERROR;
        }
    }

}
