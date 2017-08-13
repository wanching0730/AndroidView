package com.example.milka.m0802.Camera.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.LruCache;

import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by Milka on 2017/8/11.
 *
 * <p></>工具功能：将利用多线程将批量本地图片转为Base64字符串
 * <p></>设计模式：单例模式
 *
 * 使用说明：
 * 获取实例{@link #getInstance(int, LoadType)}，
 * 并通过实例直接调用工具API{@link #getImageBase64ByPath(String, int, int, LocalImageBase64)}}
 *
 * 依赖的其他工具类文件列表:ImageBase64.java
 *
 * 工具使用例子：
        ImageLoadBase64.getInstance(threadCount,ImageLoadBase64.LoadType.LIFO)
                                .getImageBase64ByPath(
                                        pathList.get(position),
                                        width,
                                        height,
                                        new ImageLoadBase64.LocalImageBase64() {
                                                    public void getString(String s) {
                                                        //s则是所得图片的Base64字符串
                                                        }}
                                                );
 *
 */
public class ImageLoadBase64 {

    private static ImageLoadBase64 mInstance;
    private final int ONE_IN_N_CACHE_SIZE = 5;  //图片缓存大小为当前可用大小的1/ONE_IN_N_CACHE_SIZE;
    private final int DEFAULT_THREAD_COUNT = 3; //默认线程数;
    /**
     * 任务列表，由于任务的添加与提取比较频繁，所以选择LinedList
     * */
    private LinkedList<Runnable> taskQueue = new LinkedList<>();
    private LruCache<String, Bitmap> imageCache;
    private int mThreadCount;
    private Thread loopTread;   //轮询线程
    private Handler mHandler;   //新添加任务消息处理者
    private ExecutorService executorService;    // 线程池
    private Semaphore mHandlerSemaphore;    //mHandler信号量
    private Semaphore threadPoolSemaphore;     //线程池.信号量

    private static final int M_HANDLER_EMPTY_MESSAGE = 0X110;
    private static final int M_UI_HANDLER_OUTPUT_BASE64_MESSAGE = 0X120;
    /**
     * 在主线程中执行界回调
     * */
    private Handler mUIHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == M_UI_HANDLER_OUTPUT_BASE64_MESSAGE){
                Bitmap bm = (Bitmap) msg.obj;
                if (imageBase64 != null && bm != null){
                    imageBase64.getString(ImageBase64.bitmapToBase64(bm));
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
    private ImageLoadBase64(int threadCount, final LoadType loadType){

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
        executorService = Executors.newFixedThreadPool(mThreadCount);    //线程池初始化
        threadPoolSemaphore = new Semaphore(mThreadCount);   //线程池资源
    }
    /**
     * 外部获取单个实例
     *
     * @param threadCount 预分配的线程总数>1
     * @param loadType 加载方式，一般选择后进先出
     * */
    public static ImageLoadBase64 getInstance(int threadCount,@Nullable LoadType loadType){
        if (mInstance == null){
            synchronized (ImageLoadUtil.class){
                if (mInstance == null){
                    mInstance = new ImageLoadBase64(threadCount, loadType);
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
     * */
    private LocalImageBase64 imageBase64;
/**
 * @param path 需要转换的图片的绝对路径
 * @param width 图片解码后的宽度要求
 * @param height 图片解码后的高度要求
 * @param imageBase64 LocalImageBase64对象，通过LocalImageBase64的getString(s)回
 *                    调获取s,s即为目标的base64字符串
 * */
    public void getImageBase64ByPath(final String path, final int width, final int height, LocalImageBase64 imageBase64){
        Bitmap bitmap = searchBitmapFromCache(path);
        this.imageBase64 = imageBase64;
        if(bitmap == null){
            /*如果缓存中不存在需要的Bitmap，则从资源中加载，并将结果返回的同时存入缓存，以备下次访问*/
            addTaskToQueue(new Runnable() {
                @Override
                public void run() {
                    Bitmap bp = readBitmapFromFileDescriptor(path, width, height);
                    saveBitmapToCache(path, bp);
                    refreshUI(bp);
                    threadPoolSemaphore.release();
                }
            });
        }else{
            refreshUI(bitmap);
        }
    }
    /**
     *回调接口
     * */

    public interface LocalImageBase64{
        void getString( String s);
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
     * */
    private void refreshUI(Bitmap bitmap) {

        Message message = Message.obtain();
        message.obj = bitmap;
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
     * 加载方式
     *
     * FIFO :First in first out
     * LIFO ;First in last out
     * */
    public enum LoadType{
        FIFO,LIFO
    }
}

