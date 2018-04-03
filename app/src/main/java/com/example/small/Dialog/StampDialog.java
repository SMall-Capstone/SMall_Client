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
                switch (userInfo.getStamp()) {
                    case 0:
                        StampActivity.stamp1.setImageResource(R.drawable.graystamp);
                        StampActivity.stamp2.setImageResource(R.drawable.graystamp);
                        StampActivity.stamp3.setImageResource(R.drawable.graystamp);
                        StampActivity.stampTextView.setText("0개");
                        break;
                    case 1:
                        StampActivity.stamp1.setImageResource(R.drawable.purplestamp);
                        StampActivity.stamp2.setImageResource(R.drawable.graystamp);
                        StampActivity.stamp3.setImageResource(R.drawable.graystamp);
                        StampActivity.stampTextView.setText("1개");
                        break;
                    case 2:
                        StampActivity.stamp1.setImageResource(R.drawable.purplestamp);
                        StampActivity.stamp2.setImageResource(R.drawable.purplestamp);
                        StampActivity.stamp3.setImageResource(R.drawable.graystamp);
                        StampActivity.stampTextView.setText("2개");
                        break;
                    case 3:
                        StampActivity.stamp1.setImageResource(R.drawable.purplestamp);
                        StampActivity.stamp2.setImageResource(R.drawable.purplestamp);
                        StampActivity.stamp3.setImageResource(R.drawable.purplestamp);
                        //여기서 stamp초기화 하고 giftbox클릭 가능하게 만들기 + giftDialog띄우기
                        userInfo.setStamp(0);
                        StampActivity.giftBox.setEnabled(true);
                        StampActivity.stampGiftTextView.setText("선물상자를 클릭해보세요!");
                        StampActivity.stampTextView.setText("3개");

                        break;
                    default:break;
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
