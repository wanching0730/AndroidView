package com.example.milka.mapdirect;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnTurnToDirection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        setListener();
    }


    private void initView() {
        btnTurnToDirection = (Button) findViewById(R.id.btn_turn_to_direction);
    }

    private void setListener() {
        btnTurnToDirection.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_turn_to_direction:
                startActivity(new Intent(this, MapsActivity.class));
                break;
            default:
                break;
        }
    }
}
