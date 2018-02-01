package com.example.small.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.small.R;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Intent intent = getIntent(); //이 액티비티를 부른 인텐트를 받는다.
        String activity = intent.getStringExtra("activity");
        TextView textView = findViewById(R.id.activity_setting);
        textView.setText(activity);
    }
}
