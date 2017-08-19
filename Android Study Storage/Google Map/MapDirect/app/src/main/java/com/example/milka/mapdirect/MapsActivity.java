package com.example.milka.mapdirect;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.milka.mapdirect.utils.DrawDirection;
import com.example.milka.mapdirect.utils.ParseJson;
import com.example.milka.mapdirect.utils.ReqParams;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationServices;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

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

    private FusedLocationProviderClient fusedLocationProviderClient;

    private LatLng originLocation;
    private LatLng targetLocation;

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
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        showCurrentLocation(fusedLocationProviderClient);
    }
/**
 * 此函数需要额外的时间请求
 * 所以一般不能直接放在onCreate中或者onMapReady中执行，会导致第一次无法直接定位到CurrentLocation
 * 此函数只是实现定位目前的经纬度，当地图加载完成后无可正常使用
 * */
    private void showCurrentLocation(FusedLocationProviderClient fusedLocationProviderClient) {
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
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    Location location = task.getResult();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 10));
                } else {
                    if (task.isSuccessful()) {
                        Log.i(TAG, "task success");
                    }
                    Log.i(TAG, "getLastLocation:exception", task.getException());
                }
            }
        });
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
        mMap.setBuildingsEnabled(true);
        mMap.setTrafficEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_direct:
                if (originLocation != null && targetLocation != null) {
                    mMap.clear();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 18));
                    directPain(originLocation, targetLocation);
                }
                break;
            default:
                break;
        }
    }

    private void directPain(LatLng originLocation, LatLng targetLocation) {
        String originPosition = "" + originLocation.latitude + "," + originLocation.longitude;
        String destinationPosition = "" + targetLocation.latitude + "," + targetLocation.longitude;
        String host = DIRECTION_API_URL;
        String params = new ReqParams(originPosition, destinationPosition, GOOGLE_API_KEY)
                .setLanguage(LANGUAGE_TYPE)
                .setMode(MODE)
                .getParamsString();
        Log.i(TAG, host + params);
        new DrawDirection(this, mMap).execute(host + params);
    }
}
