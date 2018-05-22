package com.example.small.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.small.Beacon.BeaconList;
import com.example.small.Beacon.NodeInfo;
import com.example.small.Beacon.NodeList;
import com.example.small.R;

import java.util.HashMap;
import java.util.Vector;

public class MyLocationActivity extends AppCompatActivity implements View.OnClickListener, SensorEventListener {

    double previousX = 0, previousY = 0;
    double resultX, resultY;
    int heading = 0;
    int imgX, imgY;
    int compassX, compassY;
    int previousHeading = 0;
    Animation ani, ani2;
    AnimationSet animationSet;
    ImageView img;
    TimeThread thread;
    private Vector<Integer> route;
    private HashMap<Integer, NodeInfo> nodeInfos;
    private float prevX, prevY;
    private double ActivityX, ActivityY;
    private double accX = 1080/14.0;
    private double accY = 1710/23.0;

    /** Called when the activity is first created. */
    private SensorManager sensorM;
    private Sensor mAccelerometer;
    private Sensor mMagneticField;


    private  LinearLayout stampmap_location;

    BeaconList beaconList = BeaconList.getBeaconListInstance();

    protected void onResume() {
        super.onResume();
        sensorM.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorM.registerListener(this, mMagneticField, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        sensorM.unregisterListener(this);
    }

    //센서의 정확도가 바뀌었을때(호출될일 없음, 향후 업데이트를 위해서)
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    //등록한 방향 센서 값이 바뀌었을 때 호출되는 콜백 메소드
    //@Override
    public void onSensorChanged(SensorEvent event) {
        //event 객체에 센서값에 대한 정보가 들어있다.
        switch(event.sensor.getType()){
            case Sensor.TYPE_ORIENTATION : //방향센서 값이 바뀌었을때
                heading = (int)event.values[0];
                previousHeading = heading;
                Log.i("heading", "heading = " + heading);
                break;
        }

    }

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

                            float px = (float)((previousX * accX)+1.4);
                            float py = (float)((previousY * accX)+0.8);

                            float x = (float)((resultX * accX)+1.4);
                            float y = (float) ((resultY * accY)+0.8);

                            Log.i("Ball_Animation_update", "\npreviousX = " + (float) previousX + "\n" + "previousY = " + (float) previousY);
                            ani = new TranslateAnimation(px, x, py, y);

                            ani.setDuration(1500);   //애니매이션 지속 시간
                            ani.setFillAfter(true);  // animation를 setFillAfter를 이용하여 animation후에 그대로 있도록 합니다.


                            ani2 = new RotateAnimation(previousHeading, heading-8, x, y);
                            ani2.setDuration(1500);
                            ani2.setFillAfter(true);

                            img.setVisibility(View.VISIBLE);


                            animationSet = new AnimationSet(true);
                            animationSet.setInterpolator(new AccelerateInterpolator());
                            animationSet.addAnimation(ani);
                            animationSet.addAnimation(ani2);
                            img.setAnimation(animationSet);


                            previousX = resultX;
                            previousY = resultY;

                            Log.i("path_yunjae", "accX = " + accX + " accY = " + accY);


                        }
                    });

                    beaconList.initNearestPoint();
                    sleep(1500);

                } catch (Exception e) {

                }
            }


        }

    }

    NodeList nodeList = new NodeList();
    int startNode,nodeNum;
    private boolean isFirst = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_my_location);



        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        sensorM = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        //센서값이 바뀔때마다 수정되야 하므로 리스너를 등록한다.
        //센서 리스너 객체(센서이벤트리스너 임플리먼츠), 센서타입, 센서 민감도를 매니져에 등록한다.
        sensorM.registerListener(this,//Activity가 직접 리스너를 구현
                sensorM.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
        mAccelerometer = sensorM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagneticField = sensorM.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        RelativeLayout lineContainer = (RelativeLayout) findViewById(R.id.lineContainer);

        stampmap_location =   (LinearLayout) findViewById(R.id.location_activity_layout);

        isFirst = true;
        DrawingView drawingView = new DrawingView(this);

        Log.i("path_yunjae", "Activity X = " + this.getWindowManager().getDefaultDisplay().getWidth() + "Activity Y = " + this.getWindowManager().getDefaultDisplay().getHeight());
        ActivityX = this.getWindowManager().getDefaultDisplay().getWidth()/14.0;
        ActivityY = this.getWindowManager().getDefaultDisplay().getHeight()/23.0;

        Intent intent = getIntent();
        nodeNum = intent.getIntExtra("nodeNum", -1);
        if (nodeNum != -1) {
            Log.i("nodeNum", "nodeNum =>" + nodeNum);
            beaconList.calculateDistance();
            resultX = beaconList.getResultX();
            resultY = beaconList.getResultY();
            prevX = (float) resultX;
            prevY = (float) resultY;
            Log.i("nodeNum", "x,y =>" + resultX + "," + resultY);

            nodeList = new NodeList();
            startNode = nodeList.searchNearestNode(resultX, resultY);

            Log.i("nodeNum", "startNode =>" + startNode);
            nodeList.init();

            route = nodeList.startNavigator(startNode, nodeNum); //다익스트라

            nodeInfos = nodeList.getNodeInfos();
            for (int i = 0; i < route.size(); i++) {
                Log.i("nodeNum", "Navigator -> " + route.get(i) + " : " + nodeInfos.get(route.get(i)).getLocationX() + "," + nodeInfos.get(route.get(i)).getLocationY());
            }

            lineContainer.addView(drawingView);

        }

        img = (ImageView) findViewById(R.id.RedPoint);

        imgX = img.getWidth();
        imgY = img.getHeight();
        compassX = imgX/2;
        compassY = imgY/2;

        thread = new TimeThread();
        thread.start();

    }

    /*윤재*/
    public class DrawingView extends View {

        private double X, Y;

        public DrawingView(Context context) {
            super(context);
        }

        public DrawingView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        public DrawingView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            Paint paint = new Paint();
            paint.setColor(getResources().getColor(R.color.pathColor));
            paint.setAntiAlias(true);

            X = canvas.getWidth()/14.0;
            Y = canvas.getHeight()/23.0;

            Log.i("path_yunjae", "축적x = " + canvas.getWidth() + " 축적y = " + canvas.getHeight());


            for (int i = 0; i < route.size(); i++) {
                Log.i("nodeNum", "Navigator -> " + route.get(i) + " : " + nodeInfos.get(route.get(i)).getLocationX() + "," + nodeInfos.get(route.get(i)).getLocationY());
                paint.setStrokeWidth(13);
                canvas.drawLine((float) (prevX * X), (float) (prevY * Y), (float) (nodeInfos.get(route.get(i)).getLocationX() * X), (float) (nodeInfos.get(route.get(i)).getLocationY() * Y), paint);
                Log.i("path_yunjae", "endX = " + nodeInfos.get(route.get(i)).getLocationX() + "endY = " + nodeInfos.get(route.get(i)).getLocationY());
                prevX = nodeInfos.get(route.get(i)).getLocationX();
                prevY = nodeInfos.get(route.get(i)).getLocationY();
            }

            Bitmap redpoint = BitmapFactory.decodeResource(getResources(), R.drawable.redpoint);
            redpoint = Bitmap.createScaledBitmap(redpoint, 100, 100, false);
            canvas.drawBitmap(redpoint, (float) (prevX * X) - 50, (float) (prevY * Y) - 100, paint);

            prevX = (float) resultX;
            prevY = (float) resultY;
            }


    }
    //
/////////////////////////////////스탬프 지도로 바꾸기/////////////////////////////////////////
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_stamp, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private boolean background = true;
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.stamp_show_btn) {

            if(background == false) {
                background = true;
            } else {
                background = false;
            }

           if (!background) {
               stampmap_location.setBackgroundResource(R.drawable.stampmap);
               Log.i("hhhhhhh",background+"");
           }
           else {
               stampmap_location.setBackgroundResource(R.drawable.exitmap);
               Log.i("hhhhhhh",background+"");
           }
        }

        return super.onOptionsItemSelected(item);

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
