package com.example.milka.googlemapjsonreader;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Milka on 2017/8/17.
 */

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    private Button btnReq;
    private TextView tvJson;
    private ProgressDialog progressDialog;
//https://maps.googleapis.com/maps/api/directions/json?origin=&destination=&key=AIzaSyA_8uc5Vd4R33QMu7IDubThTLSCrltes7g&language=zh-CN
    private static final String DIRECTION_URL_API = "https://maps.googleapis.com/maps/api/directions/json?";
    /*此处的KEY 需要在https://developers.google.com/maps/documentation/directions/get-api-key?hl=zh-cn#header中获得*/
    private static final String GOOGLE_API_KEY = "AIzaSyA_8uc5Vd4R33QMu7IDubThTLSCrltes7g";
    private static final String ORIGIN_POSITION = "31.174164,121.418201";
    private static final String DESTINATION_POSITION = "31.118421,121.271089";
    private static final String LANGUAGE_TYPE = "zh-CN";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        setListener();
        progressDialog = new ProgressDialog(MainActivity.this);
    }

    private void setListener() {
        btnReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = DIRECTION_URL_API
                        + "origin="
                        + ORIGIN_POSITION
                        + "&destination="
                        + DESTINATION_POSITION
                        + "&key="
                        + GOOGLE_API_KEY
                        + "&language="
                        + LANGUAGE_TYPE;
                Log.i(TAG, url);
                reqJSONByUrl(url);
            }
        });
    }

    private void initView() {
        btnReq = (Button) findViewById(R.id.btn_req);
        tvJson = (TextView) findViewById(R.id.tv_json);
    }

    private void reqJSONByUrl(String url){
        new GetJsonByUrl().execute(url);
    }



    private class GetJsonByUrl extends AsyncTask<String, Integer, String>{


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
                Log.i(TAG, s);
                tvJson.setText(s);
            }else{
                Toast.makeText(MainActivity.this, "网络请求返回NULL", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
