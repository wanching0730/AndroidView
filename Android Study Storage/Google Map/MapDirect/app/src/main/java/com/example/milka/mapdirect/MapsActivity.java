package com.example.milka.mapdirect;

import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.milka.mapdirect.utils.ParseJson;
import com.example.milka.mapdirect.utils.ReqParams;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {
    private final String TAG = "MapsActivity";

    private GoogleMap mMap;
    private PlaceAutocompleteFragment originAutocompleteFragment;
    private PlaceAutocompleteFragment targetAutocompleteFragment;
    private SupportMapFragment mapFragment;
    private Button btnDirect;

    private LatLng originLocation;
    private LatLng targetLocation;

    private ProgressDialog progressDialog;

    private List<LatLng> routes;

    private static final String DIRECTION_API_URL = "https://maps.googleapis.com/maps/api/directions/json?";
    /*此处的KEY 需要在https://developers.google.com/maps/documentation/directions/get-api-key?hl=zh-cn#header中获得*/
    private static final String GOOGLE_API_KEY = "AIzaSyApp9CynXJd6pFntmF1BGVmcCadY1Is6J8";
    private static final String LANGUAGE_TYPE = "zh-CN";
    private static final String MODE = "DRIVING";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        initView();
        setListener();
        mapFragment.getMapAsync(this);
        progressDialog = new ProgressDialog(this);
    }

    private void setListener() {
        originAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO:Get info about the selected place.
                //Log.i(TAG, "Place: " + place.getName());
                //Log.i(TAG,"起始纬度：" + place.getLatLng().latitude + "起始经度：" + place.getLatLng().longitude + "");
                originLocation = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
            }

            @Override
            public void onError(Status status) {
                // TODO:Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
        targetAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO:Get info about the selected place.
                // Log.i(TAG, "Place: " + place.getName());
                // Log.i(TAG,"结束纬度：" + place.getLatLng().latitude + "结束经度：" + place.getLatLng().longitude + "");
                targetLocation = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
            }

            @Override
            public void onError(Status status) {
// TODO:Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
        btnDirect.setOnClickListener(this);
    }

    private void initView() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        originAutocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.origin_place_autocomplete_fragment);
        targetAutocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.target_place_autocomplete_fragment);
        btnDirect = (Button) findViewById(R.id.btn_direct);
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
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(31.174164, 121.418201);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_direct:
                if (originLocation != null && targetLocation != null){
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 16));
                    directPain(originLocation,targetLocation);
                }
                break;
            default:
                break;
        }
    }

    private void directPain(LatLng originLocation, LatLng targetLocation){
        String originPosition = "" + originLocation.latitude +","+ originLocation.longitude;
        String destinationPosition = "" + targetLocation.latitude + "," + targetLocation.longitude;
        String url = DIRECTION_API_URL
                + new ReqParams(originPosition, destinationPosition, GOOGLE_API_KEY)
                .setLanguage(LANGUAGE_TYPE)
                .setMode(MODE)
                .getParamsString();
        Log.i(TAG, url);
        reqJSONByUrlAndPoint(url);
    }

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

}
