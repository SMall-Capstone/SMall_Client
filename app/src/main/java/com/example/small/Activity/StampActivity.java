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
import com.example.small.R;

import java.util.ArrayList;

public class StampActivity extends AppCompatActivity {
    public static ImageButton giftBox;

    public static TextView stampTextView,stampGiftTextView;

    public static ImageView stamp1,stamp2,stamp3;
    static int stamp=0;

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

        BeaconList beaconList = BeaconList.getBeaconListInstance();
        final ArrayList<BeaconInfo> beaconInfos = beaconList.findNearestBeacons();

        giftBox.setEnabled(false);
        giftBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),"초콜릿3개 선물☆",Toast.LENGTH_SHORT).show();
                giftDialog();
            }
        });


    }

    private void giftDialog() {
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setMessage("☆초콜릿3개 선물 당첨☆ \n 부스에서 선물을 받아가세요!").setCancelable(
                false).setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //stampTextView.setText("0개");
                        stampGiftTextView.setText("초콜릿 3개 당첨!");
                        giftBox.setImageResource(R.drawable.opengiftbox);
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

    @Override
    protected void onStop() { //액티비티를 끈경우 모든 변수를 초기화
        super.onStop();
        Log.i("StampActivityOnStop","Stop & Init");
        HomeActivity.stamp=0;
        BeaconList beaconList = BeaconList.getBeaconListInstance();
        beaconList.initEventBeacon();
    }
}


