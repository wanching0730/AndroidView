package com.example.milka.googlemapdirectdemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(31.174164, 121.418201);
        mMap.addMarker(
                new MarkerOptions()
                        .position(sydney)
                        .title("桂林路附近")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.location))
        );
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        /*地图放大倍数*/
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16));
        /*设置地图类型，默认为2D交错图*/
       // mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        /**定位设置，开始,mMap.setMyLocationEnabled(true);**/
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        /*定位标记，结束*/

        /*（折线距离的节点连接）导航路线添加，节点>= 2*/

        LatLng sijing = new LatLng(31.118421, 121.271089);
        mMap.addPolyline(
                new PolylineOptions()
                        .add(   //路线节点
                                sydney,
                                sijing
                        )
                        .width(5)   //设置路线宽度
                        .color(Color.YELLOW));  //导航路线的颜色
    }
    /*折线节点连接,end*/

    /*实用导肮
    * 首先在google开发者账号里，在console google激活direction api
    *
    * 获取json或者xml数据
    * https://developers.google.com/maps/documentation/geocoding/intro?hl=zh-cn
    * */

}
