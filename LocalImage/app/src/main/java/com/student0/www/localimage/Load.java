package com.student0.www.localimage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.ImageView;

import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by willj on 2017/5/9.
 */

public class Load {

    private final static int THREAD_COUNT = 4;
    private final static Type TYPE = Type.FILO;

    private static Load mInstance;
    private static Type type;
    private ExecutorService threadPool;

    private Thread loopThread;
    private LinkedList<Runnable> taskQuene = new LinkedList<>();
    private Handler mHandler;
    private Handler mUIHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Holder holder = (Holder) msg.obj;
            if (holder.imageView.getTag().equals(holder.path)){
                holder.imageView.setImageBitmap(holder.bitmap);
            }
        }
    };
    private Semaphore threadPoolTaskSemaphore;
    private Semaphore mHandlerSemaphore;

    private Load(int threadCount, Type type){
        loopThread = new Thread(){
            @Override
            public void run() {
                Looper.prepare();
                mHandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        try {
                            threadPoolTaskSemaphore.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        threadPool.execute(getTask());
                    }
                };
                Looper.loop();
            }
        };
        loopThread.start();
        this.type = type;
        threadPool = Executors.newFixedThreadPool(threadCount);
        threadPoolTaskSemaphore = new Semaphore(threadCount);
        mHandlerSemaphore = new Semaphore(0);
    }

    public static Load getInstance(){
        if (mInstance == null){
            synchronized (Load.class){
                if (mInstance == null){
                    mInstance = new Load(THREAD_COUNT, type);
                }
            }
        }
        return mInstance;
    }

    private synchronized void addTask(Runnable r){
        taskQuene.add(r);
        if (mHandler == null){
            try {
                mHandlerSemaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        mHandler.sendEmptyMessage(0x010);
    }

    private synchronized Runnable getTask(){
        if (type == Type.FIFO){
            return taskQuene.removeLast();
        }else if (type == Type.FILO){
            return taskQuene.getFirst();
        }else {
            return null;
        }
    }

    private Bitmap getBitmapFormCache(String path) {
        return MyCache.getInstacne().getCache().get(path);
    }

    private void saveBitmapToCache(String path, Bitmap bitmap){
        MyCache.getInstacne().getCache().put(path, bitmap);
    }

    private Bitmap readBitmapFromFileDescriptor(String filePath, int reqWidth, int reqHeight) {
        try {
            FileInputStream fis = new FileInputStream(filePath);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;

            BitmapFactory.decodeFileDescriptor(fis.getFD(), null, options);
            final int width = options.outWidth;
            final int height = options.outHeight;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {

                final int heightRatio = Math.round((float) height / (float) reqHeight);
                final int widthRatio = Math.round((float) width / (float) reqWidth);

                inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;
            }

            options.inJustDecodeBounds = false;
            options.inSampleSize = inSampleSize;

            return BitmapFactory.decodeFileDescriptor(fis.getFD(), null, options);
        } catch (Exception ex) {
        }
        return null;
    }



    enum Type{
        FIFO, FILO
    }

    public void loadImage(final ImageView imageView, final String path){

        final int width = imageView.getWidth();
        int height = imageView.getHeight();

        imageView.setTag(path);
        Bitmap bitmap = getBitmapFormCache(path);
        if (bitmap == null){
            addTask(new Runnable() {
                @Override
                public void run() {
                    Bitmap bp = readBitmapFromFileDescriptor(path, width, width);
                    saveBitmapToCache(path, bp);
                    Holder holder = new Holder();
                    holder.bitmap = bp;
                    holder.imageView = imageView;
                    holder.path = path;

                    Message message = Message.obtain();
                    message.obj = holder;
                    mUIHandler.sendMessage(message);
                    threadPoolTaskSemaphore.release();
                }
            });
        }else {
            imageView.setImageBitmap(bitmap);
        }

    }

   class Holder{
       Bitmap bitmap;
       String path;
       ImageView imageView;
    }

}
