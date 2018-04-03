package com.example.small.Dialog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.small.Activity.HomeActivity;
import com.example.small.Activity.StampActivity;
import com.example.small.Info.UserInfo;
import com.example.small.R;

public class StampDialog extends AppCompatActivity {

    private String title, content;
    private TextView titleView, contentTextView;
    private Button yesBtn, noBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stamp_dialog);

        titleView = (TextView) findViewById(R.id.stampDialogTitle_textView);
        titleView.setText("Stamp Get!");
        contentTextView = (TextView) findViewById(R.id.stampDialogContentTextView);
        contentTextView.setText("스템프를 획득하셨습니다.\n 적립하시겠습니까?");

        yesBtn = (Button) findViewById(R.id.yesBtn);
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserInfo userInfo = UserInfo.getUserInfo();
                userInfo.addStamp();
                Log.i("StampCount","Dialog ->"+userInfo.getStamp());

                if(userInfo.getStamp()==3){
                    userInfo.setStamp(0);
                    //Notification띄우기
                }
                finish();
            }
        });
        noBtn = (Button) findViewById(R.id.noBtn);
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}
