package com.example.small.Activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.small.Beacon.BeaconInfo;
import com.example.small.Beacon.BeaconList;
import com.example.small.Info.UserInfo;
import com.example.small.R;

import java.util.ArrayList;

public class StampActivity extends AppCompatActivity {
    public static ImageButton giftBox;

    public static TextView stampTextView,stampGiftTextView;

    public static ImageView stamp1,stamp2,stamp3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stamp);

        stamp1 = (ImageView) findViewById(R.id.stampImageView1);
        stamp2 = (ImageView) findViewById(R.id.stampImageView2);
        stamp3 = (ImageView) findViewById(R.id.stampImageView3);

        giftBox = (ImageButton) findViewById(R.id.giftboxImageButton);
        stampTextView = (TextView) findViewById(R.id.stampTextView);

        stampGiftTextView = (TextView)findViewById(R.id.stampGiftTextView);

        giftBox.setEnabled(false);
        giftBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),"초콜릿3개 선물☆",Toast.LENGTH_SHORT).show();
                giftDialog();
            }
        });

        UserInfo userInfo = UserInfo.getUserInfo();
        if(userInfo.getName() != null){
            Log.i("StampCount","StampActivity ->"+userInfo.getStamp());
            switch (userInfo.getStamp()) {
                case 0:
                    StampActivity.stamp1.setImageResource(R.drawable.stamp_x);
                    StampActivity.stamp2.setImageResource(R.drawable.stamp_x);
                    StampActivity.stamp3.setImageResource(R.drawable.stamp_x);
                    StampActivity.stampTextView.setText("0개");
                    break;
                case 1:
                    StampActivity.stamp1.setImageResource(R.drawable.stamp_o);
                    StampActivity.stamp2.setImageResource(R.drawable.stamp_x);
                    StampActivity.stamp3.setImageResource(R.drawable.stamp_x);
                    StampActivity.stampTextView.setText("1개");
                    break;
                case 2:
                    StampActivity.stamp1.setImageResource(R.drawable.stamp_o);
                    StampActivity.stamp2.setImageResource(R.drawable.stamp_o);
                    StampActivity.stamp3.setImageResource(R.drawable.stamp_x);
                    StampActivity.stampTextView.setText("2개");
                    break;
                case 3:
                    StampActivity.stamp1.setImageResource(R.drawable.stamp_o);
                    StampActivity.stamp2.setImageResource(R.drawable.stamp_o);
                    StampActivity.stamp3.setImageResource(R.drawable.stamp_o);
                    //여기서 stamp초기화 하고 giftbox클릭 가능하게 만들기 + giftDialog띄우기
                    userInfo.setStamp(0);
                    StampActivity.giftBox.setEnabled(true);
                    StampActivity.stampGiftTextView.setText("선물상자를 클릭해보세요!");
                    StampActivity.stampTextView.setText("3개");

                    break;
                default:break;
            }
        }
        else{
            StampActivity.stampGiftTextView.setText("로그인 후 이용 가능한 서비스입니다.");
        }


    }

    private void giftDialog() {
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setMessage("☆초콜릿3개 선물 당첨☆ \n 부스에서 선물을 받아가세요!").setCancelable(
                false).setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        BeaconList beaconList = BeaconList.getBeaconListInstance();
                        beaconList.initStampBeacon();
                        //stampTextView.setText("0개");
                        stampGiftTextView.setText("초콜릿 3개 당첨!");
                        giftBox.setImageResource(R.drawable.get_gift);
                        giftBox.setEnabled(false);
                        dialog.dismiss();
                    }
                }).setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Action for 'NO' Button
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alt_bld.create();
        // Title for AlertDialog
        alert.setTitle("Gift Get");
        // Icon for AlertDialog
        alert.setIcon(R.drawable.gifticon);
        alert.show();
    }

}


