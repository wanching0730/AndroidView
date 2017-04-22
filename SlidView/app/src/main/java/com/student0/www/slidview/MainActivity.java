package com.student0.www.slidview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SlidView slidView;
    private SlidAdapter adapter;

    private List<String> data = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        for (int i = 0; i < 20; i ++){
            data.add(String.valueOf(i));
        }
        slidView = (SlidView) findViewById(R.id.slidView);
        adapter = new SlidAdapter(getBaseContext(), data);
        slidView.setAdapter(adapter);
    }
}
