package com.yhp.DataDistribute;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.yhp.distributedata.InterFaceManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        InterFaceManager.getInstance().registerInterface();
    }
}