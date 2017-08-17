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

import com.example.milka.googlemapjsonreader.utils.DirectInfo;
import com.google.gson.Gson;

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
/**
 * 执行异步任务
 *传入完整的url，实例化AsyncTas，并调用execute(url)，传递参数执行异步任务
 * @param url 请求网络数据的url
 **/
    private void reqJSONByUrl(String url){
        new GetJsonByUrl().execute(url);
    }
/**
 * 使用AsyncTask从网络异步加载导航的JSON数据类
 * 在请求结束时，回调onPostExecute(String s),并将返回的JSON
 * 字符串作为回调方法onPostExecute()的参数传递供使用。
 *
 * 内部的提示交互及编程风格在使用时需要改善
 * */
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
                parseJSON(s);
            }else{
                Toast.makeText(MainActivity.this, "网络请求返回NULL", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * JSON解析,使用Gson解析
     * compile 'com.google.code.gson:gson:2.7'
     * */
    private void parseJSON(String jsonString){
        if (jsonString == null){
            return;
        }
        Gson gson = new Gson();
        DirectInfo directInfo = gson.fromJson(jsonString, DirectInfo.class);
        Log.i(TAG, directInfo.routes.get(0).legs.get(0).end_address);
        String s = gson.toJson(directInfo);
        tvJson.setText(s);
    }
}
