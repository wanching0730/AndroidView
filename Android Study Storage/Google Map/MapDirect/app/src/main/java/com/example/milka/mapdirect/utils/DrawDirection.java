package com.example.milka.mapdirect.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.milka.mapdirect.MapsActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by Milka on 2017/8/19.
 *
 * 使用AsyncTask从网络异步加载导航的JSON数据类
 * 在请求结束时，回调onPostExecute(String s),并将返回的JSON
 * 字符串作为回调方法onPostExecute()的参数传递供使用。
 *
 * 内部的提示交互及编程风格在使用时需要改善
 * */

 public class DrawDirection extends AsyncTask<String, Integer, String> {
    private final String TAG = "drawDirection";
    private ProgressDialog progressDialog;
    private List<LatLng> routes;
    private GoogleMap mMap;
    private Context context;
    public DrawDirection(Context context, GoogleMap mMap){
        this.progressDialog = new ProgressDialog(context);
        this.mMap = mMap;
        this.context = context;
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
            Toast.makeText(context, "网络请求返回NULL", Toast.LENGTH_SHORT).show();
        }
    }
}
