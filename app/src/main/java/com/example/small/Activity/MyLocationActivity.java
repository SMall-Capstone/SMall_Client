package com.example.small.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.example.small.Beacon.BeaconList;
import com.example.small.R;

public class MyLocationActivity extends AppCompatActivity implements View.OnClickListener {


    double previousX = 0, previousY = 0;
    double resultX, resultY;
    Animation ani;
    ImageView img;
    TimeThread thread;

    BeaconList beaconList = BeaconList.getBeaconListInstance();

    class TimeThread extends Thread {
        boolean isRun = true;

        public void run() {
            while (isRun) {
                beaconList.calculateDistance();

                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            resultX = beaconList.getResultX();
                            resultY = beaconList.getResultY();
                            Log.i("Ball_Animation_update", "\npreviousX = " + (float) previousX + "\n" + "previousY = " + (float) previousY);
                            ani = new TranslateAnimation((float) (previousX * HomeActivity.accumulationX), (float) (resultX * HomeActivity.accumulationX),
                                    (float) (previousY * HomeActivity.accumulationX), (float) (resultY * HomeActivity.accumulationY));

                            ani.setDuration(1500);   //애니매이션 지속 시간
                            ani.setFillAfter(true);  // animation를 setFillAfter를 이용하여 animation후에 그대로 있도록 합니다.
                            img.setVisibility(View.VISIBLE);
                            //img.setAnimation(ani);
                            img.startAnimation(ani);
                            //ani.start();

//                            Log.i("Ball_Animation_update", "Values = " + (float) previousX + "\n" +  (float) (resultX*MainActivity.accumulationX) + "\n" +
//                                    (float)previousY + "\n" + (float)(resultY*MainActivity.accumulationY));

                            previousX = resultX;
                            previousY = resultY;


                        }
                    });

                    beaconList.initNearestPoint();
                    sleep(3000);

                } catch (Exception e) {

                }
            }


        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_my_location);

        img = (ImageView) findViewById(R.id.RedPoint);
        thread = new TimeThread();
        thread.start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (thread != null) {

            thread.isRun = false; //timerThread.interrupt();
            try {
                thread.join();
            } catch (InterruptedException e) {
            }
        }
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
