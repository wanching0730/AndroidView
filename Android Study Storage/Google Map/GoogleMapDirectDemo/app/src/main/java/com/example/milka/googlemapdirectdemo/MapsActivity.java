package com.example.milka.googlemapdirectdemo;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.milka.googlemapdirectdemo.utils.ParseJson;
import com.example.milka.googlemapdirectdemo.utils.ReqParams;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private final String TAG = "MapsActivity";

    private GoogleMap mMap;
    private ProgressDialog progressDialog;
    private List<LatLng> routes;

    private static final String DIRECTION_API_URL = "https://maps.googleapis.com/maps/api/directions/json?";
    /*此处的KEY 需要在https://developers.google.com/maps/documentation/directions/get-api-key?hl=zh-cn#header中获得*/
    private static final String GOOGLE_API_KEY = "AIzaSyA_8uc5Vd4R33QMu7IDubThTLSCrltes7g";
    private static final String ORIGIN_POSITION = "31.174164,121.418201";
    private static final String DESTINATION_POSITION = "31.118421,121.271089";
    private static final String LANGUAGE_TYPE = "zh-CN";
    private static final String MODE = "DRIVING";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        progressDialog = new ProgressDialog(this);
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

//        LatLng sijing = new LatLng(31.118421, 121.271089);
//        mMap.addPolyline(
//                new PolylineOptions()
//                        .add(   //路线节点
//                                sydney,
//                                sijing
//                        )
//                        .width(5)   //设置路线宽度
//                        .color(Color.YELLOW));  //导航路线的颜色

        String url = DIRECTION_API_URL
                + new ReqParams(ORIGIN_POSITION, DESTINATION_POSITION, GOOGLE_API_KEY)
                .setLanguage(LANGUAGE_TYPE)
                .setMode(MODE)
                .getParamsString();
        Log.i(TAG, url);
        reqJSONByUrlAndPoint(url);
    }

    private void pointMap(List<LatLng> routes) {
        PolylineOptions polylineOptions = new PolylineOptions();
        int routesSize = routes.size();
        Log.i(TAG , ""+ routesSize);
        polylineOptions.addAll(routes);
        polylineOptions.width(10).color(Color.BLACK).geodesic(true);
        if (!polylineOptions.isGeodesic()){
            polylineOptions.geodesic(true);
        }
        mMap.addPolyline(polylineOptions);
    }

    /*折线节点连接,end*/

    /*实用导肮
    * 首先在google开发者账号里，在console google激活direction api
    *
    * 获取json或者xml数据
    * https://developers.google.com/maps/documentation/geocoding/intro?hl=zh-cn
    * */

    /**
     * 执行异步任务
     *传入完整的url，实例化AsyncTas，并调用execute(url)，传递参数执行异步任务
     * @param url 请求网络数据的url
     **/
    private void reqJSONByUrlAndPoint(String url){
        Log.i(TAG, url);
        new GetJsonByUrl().execute(url);
    }
    /**
     * 使用AsyncTask从网络异步加载导航的JSON数据类
     * 在请求结束时，回调onPostExecute(String s),并将返回的JSON
     * 字符串作为回调方法onPostExecute()的参数传递供使用。
     *
     * 内部的提示交互及编程风格在使用时需要改善
     * */
    private class GetJsonByUrl extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            String link = params[0];
            try {
                URL url = new URL(link);
                InputStream is = url.openConnection().getInputStream();
                StringBuffer buffer = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                reader.close();
                is.close();
                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDialog.setMessage("loading" + values[0] + "%");
        }
        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            if (s != null){
                routes = new ParseJson(s).getRouteAt(0);
                pointMap(routes);
            }else{
                Toast.makeText(MapsActivity.this, "网络请求返回NULL", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
