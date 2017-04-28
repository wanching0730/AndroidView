package com.student0.www.slidelistview_4_28;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SlideAdapter slideAdapter;
    private SlideListView slideListView;

    private List<String> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        slideAdapter = new SlideAdapter(this, data, slideListView);
        slideListView.setAdapter(slideAdapter);
    }

    private void init() {
        for (int i = 0; i < 20 ; i++){
            data.add(String.valueOf(i));
        }
        slideListView = (SlideListView) findViewById(R.id.slideView);
    }
}
