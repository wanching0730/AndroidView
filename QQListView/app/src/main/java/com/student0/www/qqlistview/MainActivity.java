package com.student0.www.qqlistview;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    private SildView listView;
    private MyAdapter adapter;

    private final int INFO_POSITION = 0;
    private final int DELETE_POSITION = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init(getBaseContext());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listView.isAllowItemClick() && !listView.isDeleteShow()){
                    Toast.makeText(MainActivity.this, String.valueOf(adapter.getData().get(position)),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void init(Context context) {
        listView = (SildView) findViewById(R.id.listView);
        adapter = new MyAdapter(context, listView);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        System.out.println(TAG + "--->dispatchTouchEvent"  + TouchEventUtil.getTouchAction(ev.getAction()));
        return super.dispatchTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        System.out.println(TAG + "--->onInterceptTouchEvent" + TouchEventUtil.getTouchAction(event.getAction()));
        return super.onTouchEvent(event);
    }
}
