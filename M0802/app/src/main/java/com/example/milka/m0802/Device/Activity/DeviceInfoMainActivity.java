package com.example.milka.m0802.Device.Activity;

import android.content.Intent;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.milka.m0802.Camera.Adapter.LocalImagesAdapter;
import com.example.milka.m0802.Device.Util.FileSizeUtil;
import com.example.milka.m0802.Device.Util.NetInfoUtil;
import com.example.milka.m0802.R;

/**
 * Created by Milka on 2017/8/7.
 *
 * 设备信息首页
 */

public class DeviceInfoMainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "DeviceInfoMainActivity";

    private TextView tvDeviceVersion;
    private TextView tvProduction;
    private TextView tvExternalStorageSize;
    private TextView tvNetStatus;
    private TextView tvNetSpeed;
    private TextView tvBlueToothStatus;
    private TextView tvGPSStatus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_device_information);
        initView();
        initData();
        setProperty();
        setListener();
    }

    private void initView() {
        tvDeviceVersion = (TextView) findViewById(R.id.tv_version_info);
        tvProduction = (TextView) findViewById(R.id.tv_production);
        tvExternalStorageSize = (TextView) findViewById(R.id.tv_external_storage_size);
        tvNetStatus = (TextView) findViewById(R.id.tv_net_status);
        tvNetSpeed = (TextView) findViewById(R.id.tv_net_speed);
        tvBlueToothStatus = (TextView) findViewById(R.id.tv_blue_tooth_status);
        tvGPSStatus = (TextView) findViewById(R.id.tv_gps_status);
    }

    private void initData() {
    }

    private void setProperty() {
/*蓝牙不可用则跳转*/
        if (!new NetInfoUtil(this, tvBlueToothStatus, NetInfoUtil.SearchType.BlueToothStatus).isBlueToothAvailable()){
            startActivity(new Intent(Settings.ACTION_SETTINGS));
        }

        String model = Build.MODEL; //型号
        String manufacture =Build.MANUFACTURER; //厂商
        //String insertMemorySize = String.valueOf(FileSizeUtil.getTotalInsertMemorySize());
        String externalMemorySize = String.valueOf(FileSizeUtil.getTotalExternalMemorySize());

        tvDeviceVersion.setText(model);
        tvProduction.setText(manufacture);
        tvExternalStorageSize.setText(externalMemorySize);
        new NetInfoUtil(this, tvNetStatus, NetInfoUtil.SearchType.NetStatus).startShowInfo();
        new NetInfoUtil(this, tvBlueToothStatus, NetInfoUtil.SearchType.BlueToothStatus).startShowInfo();
        new NetInfoUtil(this, tvNetStatus, NetInfoUtil.SearchType.NetStatus).startShowInfo();
        new NetInfoUtil(this, tvNetSpeed, NetInfoUtil.SearchType.NetSpeed).startShowInfo();
        new NetInfoUtil(this, tvGPSStatus, NetInfoUtil.SearchType.GPSStatus).startShowInfo();
        //tvNetStatus.setText();
    }

    private void setListener() {
        tvBlueToothStatus.setOnClickListener(this);
        tvGPSStatus.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_blue_tooth_status:
                /**跳转至设置*/
                if (!new NetInfoUtil(this, tvBlueToothStatus, NetInfoUtil.SearchType.BlueToothStatus).isBlueToothAvailable()) {
                    startActivity(new Intent(Settings.ACTION_SETTINGS));
                }
                break;
            case R.id.tv_gps_status:
                /**跳转至设置*/
                if (!new NetInfoUtil(this, tvGPSStatus, NetInfoUtil.SearchType.GPSStatus).isGPSAvailable()){
                    startActivity(new Intent(Settings.ACTION_SETTINGS));
                }
                break;
            default:
                break;
        }
    }
}
