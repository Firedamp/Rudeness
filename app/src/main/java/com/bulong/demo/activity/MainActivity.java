package com.bulong.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bulong.demo.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void gotoNormal(View view){
        startActivity(new Intent(this, NormalActivity.class));
    }

    public void gotoRude(View view){
        startActivity(new Intent(this, RudeActivity.class));
    }


}
