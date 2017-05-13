package com.student0.www.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by willj on 2017/5/11.
 */

public class MyService extends Service {


    class DownloadBinder extends Binder{

        public void startDownload(){
            Log.i(TAG, "startDownload()");
        }
        public int getProgress(){
            Log.i(TAG, "getProgress()");
            return 0;
        }
    }

    private final static String TAG = "MyService";

    private DownloadBinder mBinder = new DownloadBinder();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("TAG", "onBind()");

        return mBinder;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate()");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand()");

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy()");
        super.onDestroy();
    }
}
