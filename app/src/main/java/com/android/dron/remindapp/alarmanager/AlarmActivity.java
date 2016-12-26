package com.android.dron.remindapp.alarmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.android.dron.remindapp.R;

public class AlarmActivity extends AppCompatActivity {

    private String msg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        msg = intent.getStringExtra("msg");
        setContentView(R.layout.alarm);
    }
}
