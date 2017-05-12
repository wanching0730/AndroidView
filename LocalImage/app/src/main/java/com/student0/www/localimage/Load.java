package com.student0.www.localimage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
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
    private ExecutorService threadPool;

    private Thread loopThread;
    private LinkedList<Runnable> taskQuene = new LinkedList<>();
    private Handler mHandler;
    private Handler mUIHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Holder holder = (Holder) msg.obj;
            Bitmap bm = holder.bitmap;
            ImageView imageView = holder.imageView;
            String path = holder.path;
            if(imageView.getTag().toString().equals(path)){
                imageView.setImageBitmap(bm);
            }
        }
    };
    private Semaphore threadPoolTaskSemaphore;
    private Semaphore mHandlerSemaphore;

    private Load(int threadCount, final Type type){
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
                        threadPool.execute(getTask(type));
                    }
                };
                mHandlerSemaphore.release();
                Looper.loop();
            }
        };
        loopThread.start();
        threadPool = Executors.newFixedThreadPool(threadCount);
        threadPoolTaskSemaphore = new Semaphore(threadCount);
        mHandlerSemaphore = new Semaphore(0);
    }

    public static Load getInstance(){
        if (mInstance == null){
            synchronized (Load.class){
                if (mInstance == null){
                    mInstance = new Load(THREAD_COUNT, Type.FILO);
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

    private synchronized Runnable getTask(Type type){
        if (type == Type.FIFO){
            return taskQuene.removeLast();
        }else if (type == Type.FILO){
            return taskQuene.removeFirst();
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

//            if (height > reqHeight || width > reqWidth) {
//
//                final int heightRatio = Math.round((float) height / (float) reqHeight);
//                final int widthRatio = Math.round((float) width / (float) reqWidth);
//
//                inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;
//            }

            if (height > reqHeight || width > reqWidth) {
                if (width > height) {
                    inSampleSize = Math.round(height / reqHeight);
                } else {
                    inSampleSize = Math.round(width / reqWidth);
                }
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
        imageView.setImageBitmap(null); //防止闪屏
        imageView.setTag(path);
        if (mUIHandler == null){

        }
//        final int width = imageView.getWidth();
//        final int height = imageView.getHeight();
        final ImageSize imageSize = getImageViewSize(imageView);
        Bitmap bitmap = getBitmapFormCache(path);
        if (bitmap == null){
            addTask(new Runnable() {
                @Override
                public void run() {
                    Bitmap bp = readBitmapFromFileDescriptor(path, imageSize.width, imageSize.height);
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
            Holder holder = new Holder();
            holder.bitmap = bitmap;
            holder.imageView = imageView;
            holder.path = path;

            Message message = Message.obtain();
            message.obj = holder;
            mUIHandler.sendMessage(message);
        }

    }
    /**
     * 根据ImageView获得适当的压缩的宽和高对图片进行压缩，防止内存溢出
     * */
    private ImageSize getImageViewSize(ImageView imageView) {
        ImageSize imageSize = new ImageSize();

        //获取屏幕的宽度
        /**
         * 判断并获取实际的
         * 判断并获取布局中的
         * 判断并获取最大的
         * 上述都没设置则压缩为最大屏幕的
         * */
        DisplayMetrics displayMetrics = imageView.getContext().getResources().getDisplayMetrics();

        ViewGroup.LayoutParams lp = imageView.getLayoutParams();

        //压缩宽度
        int width = imageView.getWidth();
        if (width <= 0){
            width = lp.width; //获得imageView再layout中声明的宽度
        }
        if (width <= 0){
            width = imageView.getMaxWidth();//检查最大值
        }
        if(width <= 0){
            width = displayMetrics.widthPixels;
        }
        //压缩高度
        int height = imageView.getHeight();
        if (height <= 0){
            height = lp.height; //获得imageView再layout中声明的宽度
        }
        if (height <= 0){
            height = imageView.getMaxHeight();//检查最大值
        }
        if(height <= 0){
            height = displayMetrics.heightPixels;
        }


        imageSize.height = height;
        imageSize.width = width;
        return imageSize;
    }

    private class ImageSize{
        int width;
        int height;
    }
   class Holder{
       Bitmap bitmap;
       String path;
       ImageView imageView;
    }

}
