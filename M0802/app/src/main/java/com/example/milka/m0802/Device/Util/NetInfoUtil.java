package com.example.milka.m0802.Device.Util;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import static com.example.milka.m0802.Device.Util.NetInfoUtil.SearchType.NetSpeed;


/**
 * Created by Milka on 2017/8/8
 *
 * 获取网路状态及网速信息.
 *
 * 使用说明:
 * 1、工具实例化: NetInfoUtil util = new NetInfoUtil(context, textView, SearchType.type);
 * 2、启动测试并更新UI:  util.startShowInfo();
 *
 *权限：
 * <uses-permission android:name="android.permission.INTERNET"></uses-permission>
 <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"></uses-permission>
 */

public class NetInfoUtil {

    private static final int NEW_SPEED_COMING = 110;
    private static final int NEW_NET_STATUS_COMING = 111;
    private static final int NEW_BLUE_TOOTH_STATUS_COMING = 112;
    private static final int NEW_GPS_STATUS_COMING = 113;

    private long lastTotalRxBytes = 0;
    private long lastTimeStamp = 0;
    private Context context;
    private TextView textViewInfo;
    private SearchType type;
    /**
     * 在UI线程中更新
     * */
    private Handler uiHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (textViewInfo != null){
                switch (msg.what){
                    case NEW_SPEED_COMING:
                    case NEW_BLUE_TOOTH_STATUS_COMING:
                    case NEW_GPS_STATUS_COMING:
                    case NEW_NET_STATUS_COMING:
                        textViewInfo.setText(msg.obj.toString());
                        break;
                    default:
                        break;
                }
            }
        }
    };
    /**
     *构造函数
     * @param context
     * @param textView 需要显示信息的TextView
     * */
    public NetInfoUtil(Context context, TextView textView, SearchType type) {
        this.context = context.getApplicationContext();
        this.textViewInfo = textView;
        this.type = type;
    }
    /**
     * 定义定时测速任务
     * */
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            if (type != NetSpeed){
                showStatus();
            }else{
                showNetSpeed();
            }

        }
    };
    /**
     * 调用此API开始获取状态
     * */
    public void startShowInfo(){
        lastTotalRxBytes = getTotalRxBytes();
        lastTimeStamp = System.currentTimeMillis();
        new Timer().schedule(task, 1000, 1000);
    }

    /**
     * 判断网络是否可用
     * */
    public  boolean isNetWorkConnected(){
        if (context != null){
            ConnectivityManager manager = (ConnectivityManager)context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = manager.getActiveNetworkInfo();
            if (info != null && info.isConnected()){
                return info.isAvailable();
            }
        }
        return false;
    }
    /**
     * 判断蓝牙是否打开
     *
     * */
    public boolean isBlueToothAvailable(){
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();;
        if (adapter != null && adapter.isEnabled()){
            return true;
        }
        return false;
    }

    private void showStatus(){
        Message m = uiHandler.obtainMessage();
        switch (type){
            case BlueToothStatus:
                m.what = NEW_BLUE_TOOTH_STATUS_COMING;
                m.obj = isBlueToothAvailable();
                break;
            case GPSStatus:
                m.what = NEW_GPS_STATUS_COMING;
                m.obj = isGPSAvailable();
                break;
            case NetStatus:
                m.what = NEW_NET_STATUS_COMING;
                m.obj = isNetWorkConnected();
                break;
            default:
                break;
        }
        uiHandler.sendMessage(m);
    }

    /**
     * 判断GPS是否打开
     * */
    public boolean isGPSAvailable(){
        LocationManager manager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return true;
        }
        return false;
    }


    /**
     * 获取当前的RxBytes
     * */
    private long getTotalRxBytes() {
        return TrafficStats.getUidRxBytes(context.getApplicationInfo().uid) == TrafficStats.UNSUPPORTED ? 0 :(TrafficStats.getTotalRxBytes()/1024);//转为KB
    }
    /**
     * 测速及通过handler给主线程发送测速结果
     * */
    private void showNetSpeed(){
        long nowTotalRxBytes = getTotalRxBytes();
        long nowTimeStamp = System.currentTimeMillis();
        long speed = (nowTotalRxBytes - lastTotalRxBytes) * 1000 / (nowTimeStamp - lastTimeStamp);

        lastTimeStamp = nowTimeStamp;
        lastTotalRxBytes = nowTotalRxBytes;
        /*给主线程发送Message*/
        Message message = uiHandler.obtainMessage();
        message.what = NEW_SPEED_COMING;
        message.obj = speed;
        uiHandler.sendMessage(message);
    }

   public enum SearchType{
        NetStatus,
        NetSpeed,
        BlueToothStatus,
        GPSStatus
    }
}
