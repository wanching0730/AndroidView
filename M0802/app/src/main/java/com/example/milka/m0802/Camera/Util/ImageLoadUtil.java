package com.example.milka.m0802.Camera.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by Milka on 2017/8/3.
 *
 * 图像加载工具(单例模式)，暂只支持本地图像资源
 * 采用单例模式，所以有且只有第一次getInstance(threadCount, LoadType)的参数是真实的
 * 
 * 使用说明：
 * 1、批量加载显示本地图片
 * ImageLoadUtil.getInstance(myThreadCount, ImageLoadUtil.LoadType.LIFO)
 *              .loadImage( myImagePath, myImageView);
 * 
 * myThreadCount : >=1, 加载资源安排最多的子线程数，不宜过大（建议3-5),默认为3 
 * LoadType :图片加载方式
 * myImagePath: 图片资源的绝对路径
 * myImageView: 需要渲染的ImageView对象
 * 
 */

public class ImageLoadUtil {


    private static ImageLoadUtil mInstance;

    private final int ONE_IN_N_CACHE_SIZE = 5;  //图片缓存大小为当前可用大小的1/ONE_IN_N_CACHE_SIZE;
    private final int DEFAULT_THREAD_COUNT = 3; //默认线程数;
    /**
     * 任务列表，由于任务的添加与提取比较频繁，所以选择LinedList
     * */
    private LinkedList<Runnable> taskQueue = new LinkedList<>();
    private LruCache<String, Bitmap> imageCache;

    private Thread loopTread;   //轮询线程
    private Handler mHandler;   //新添加任务消息处理者
    private ExecutorService executorService;    // 线程池

    private Semaphore mHandlerSemaphore;    //mHandler信号量
    private Semaphore threadPoolSemaphore;     //线程池.信号量

    private static final int M_HANDLER_EMPTY_MESSAGE = 0X110;
    private static final int M_UI_HANDLER_OUTPUT_BASE64_MESSAGE = 0X120;

    /**
     * 在主线程中执行界面UI更新操作
     * */
    private Handler mUIHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == M_UI_HANDLER_OUTPUT_BASE64_MESSAGE){
                Holder holder = (Holder) msg.obj;
                Bitmap bm = holder.bitmap;
                ImageView imageView = holder.imageView;
                String path = holder.path;
                if(imageView.getTag().toString().equals(path)){
                    imageView.setImageBitmap(bm);
                }
            }
            }

    };
    /**
     * 内部实现构造函数
     *
     * @param threadCount 预分配的线程总数>1，默认为1
     * @param loadType 加载方式，一般选择后进先出，默认为后进先出
     * */
    private int mThreadCount;
    
    private ImageLoadUtil(int threadCount, final LoadType loadType){
        mThreadCount = (threadCount < 1 ? DEFAULT_THREAD_COUNT: threadCount);
        /*设置缓存为当前可用内存的*/
        int maxMemory = (int) (Runtime.getRuntime().totalMemory()/1024);
        int cacheSize = maxMemory/ONE_IN_N_CACHE_SIZE;
        imageCache = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes()*value.getHeight()/1024;
            }
        };
        mHandlerSemaphore = new Semaphore(1);
        /*轮询线程初始化*/
        try {
            mHandlerSemaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        loopTread = new Thread(){
            @Override
            public void run() {
                Looper.prepare();
                mHandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        if (executorService == null){
                            try {
                                threadPoolSemaphore.acquire();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        executorService.execute(getTaskForQueue(loadType));
                    }
                };
                mHandlerSemaphore.release();
                Looper.loop();
            }
        };
        loopTread.start();
        executorService = Executors.newFixedThreadPool(threadCount);    //线程池初始化
        threadPoolSemaphore = new Semaphore(threadCount);   //线程池资源
    }
    /**
     * 外部获取单个实例
     *
     * @param threadCount 预分配的线程总数>1
     * @param loadType 加载方式，一般选择后进先出
     * */
    public static ImageLoadUtil getInstance(int threadCount,@Nullable LoadType loadType){
        if (mInstance == null){
            synchronized (ImageLoadUtil.class){
                if (mInstance == null){
                    mInstance = new ImageLoadUtil(threadCount, loadType);
                }
            }
        }
        return mInstance;
    }
/**
 * 根据加载类型，获取任务队列中的任务
 * */
    private Runnable getTaskForQueue(LoadType loadType) {
        if (taskQueue.size() > 0){
            return loadType == LoadType.LIFO ?
                    taskQueue.removeFirst():
                    taskQueue.removeLast();
        }else {
            return null;
        }

    }

    /**
     *添加任务至任务队列
     *并通知
     * */
    private synchronized void addTaskToQueue(Runnable newTack){
        taskQueue.add(newTack);
        if (mHandler == null){
            try {
                mHandlerSemaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        mHandler.sendEmptyMessage(M_HANDLER_EMPTY_MESSAGE);
    }
/**
 * 调用此函数可通过图片资源的绝对路径path，为ImageView加载Bitmap图片资源
 * 简易步骤:
 *          1、搜索缓存，若缓存中存在对应得Path->Bitmap则直接更新UI
 *          2、若缓存中无，则从外存中获取
 * @param path 原始图片资源的绝对路径
 * @param imageView 需要渲染Bitmap对象的ImageView
 * */
    public void loadImage(final String path, final ImageView imageView){
        imageView.setImageBitmap(null);     //防止闪屏
        imageView.setTag(path);
        final ImageSize imageSize = getImageViewSize(imageView);
        Bitmap bitmap = searchBitmapFromCache(path);
        if(bitmap == null){
            /*如果缓存中不存在需要的Bitmap，则从资源中加载，并将结果返回的同时存入缓存，以备下次访问*/
            addTaskToQueue(new Runnable() {
                @Override
                public void run() {
                    Bitmap bp = readBitmapFromFileDescriptor(path, imageSize.width, imageSize.height);
                    saveBitmapToCache(path, bp);
                    refreshUI(bp, imageView, path);
                    threadPoolSemaphore.release();
                }
            });
        }else{
            refreshUI(bitmap, imageView, path);
        }
    }

/**
 * 将压缩后的Bitmap对象，与Bitmap原始数据的绝对路径加入缓存中
 *
 * @param path 原始图片资源的绝对路径
 * @param bitmap 压缩后可以直接使用的Bitmap对象
 * */
    private void saveBitmapToCache(String path, Bitmap bitmap) {
        imageCache.put(path, bitmap);
    }
/**
 * 向UI Handler发送UI更新消息
 * @param bitmap 根据指定大小压缩后的bitmap对对象
 * @param imageView 需要渲染的ImageView对象
 * @param path 参数一的Bitmap对象的原始资源的绝对路径
 * */
    private void refreshUI(Bitmap bitmap, ImageView imageView, String path) {
        Holder holder = new Holder();
        holder.bitmap = bitmap;
        holder.imageView = imageView;
        holder.path = path;

        Message message = Message.obtain();
        message.obj = holder;
        message.what = M_UI_HANDLER_OUTPUT_BASE64_MESSAGE;
        mUIHandler.sendMessage(message);
    }
/**
 *通过制定所需图片的大小与图片的据对路径来返回Bitmap对象
 * 若查找失败则返回null
 *
 * @param filePath 图片资源的绝对路径
 * @param reqWidth 需要的图片宽度
 * @param reqHeight 需要的图片高度
 * @return Bitmap或者null
 * */
    private Bitmap readBitmapFromFileDescriptor(String filePath, int reqWidth, int reqHeight) {
        try {
            FileInputStream fis = new FileInputStream(filePath);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;      //直接获取图片尺寸，不将图片加载进内存

            BitmapFactory.decodeFileDescriptor(fis.getFD(), null, options);
            final int width = options.outWidth;
            final int height = options.outHeight;
            int inSampleSize = 1;
            inSampleSize = Math.max(height/reqHeight, width/reqWidth);

            options.inPreferredConfig =
                    Bitmap.Config.RGB_565;  //通过RGB_565的颜色选择，来减少加载时的内存的使用
            options.inJustDecodeBounds =
                    false;                  //设置inJustDecodeBounds使文件载入内存并返回Bitmap对象
            options.inSampleSize = inSampleSize;

            return BitmapFactory.decodeFileDescriptor(fis.getFD(), null, options);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    /**
     * 通过据对路径从缓存中读取Bitmap
     * */
    private Bitmap searchBitmapFromCache(String path) {
        return imageCache.get(path);
    }

    /**
     * 根据ImageView获得适当的压缩的宽和高对图片进行压缩，防止尺寸过大导致内存溢出
     * */
    private ImageSize getImageViewSize(ImageView imageView) {

        ImageSize imageSize = new ImageSize();
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
    /**
     * 特殊的数据结构用以存储个体所需的信息
     * */
    class Holder{
        Bitmap bitmap;  //渲染ImageView所用的Bitmap对象
        String path;    //ImageView渲染图片的绝对路径
        ImageView imageView;    //需要加载图片的ImageView
    }

    /**
     * 加载方式
     *
     * FIFO :First in first out
     * LIFO ;First in last out
     * */
    public enum LoadType{
        FIFO,LIFO
    }
}

