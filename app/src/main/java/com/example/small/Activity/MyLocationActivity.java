package com.example.small.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
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

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MyLocationActivity extends AppCompatActivity implements View.OnClickListener {

    double previousX = 0, previousY = 0;
    double resultX, resultY;
    Animation ani;
    ImageView img;
    TimeThread thread;
    private Vector<Integer> route;
    private HashMap<Integer, NodeInfo> nodeInfos;
    private float prevX, prevY;
    private double ActivityX, ActivityY;


    private  LinearLayout stampmap_location;

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
                            ani = new TranslateAnimation((float) (previousX * ActivityX), (float) (resultX * ActivityX),
                                    (float) (previousY * ActivityY), (float) (resultY * ActivityY));

                            ani.setDuration(3000);   //애니매이션 지속 시간
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

        RelativeLayout lineContainer = (RelativeLayout) findViewById(R.id.lineContainer);

        stampmap_location =   (LinearLayout) findViewById(R.id.location_activity_layout);

        isFirst = true;
        DrawingView drawingView = new DrawingView(this);

        Log.i("path_yunjae", "Activity X = " + this.getWindowManager().getDefaultDisplay().getWidth() + "Activity Y = " + this.getWindowManager().getDefaultDisplay().getHeight());
        ActivityX = this.getWindowManager().getDefaultDisplay().getWidth()/14.0;
        ActivityY = this.getWindowManager().getDefaultDisplay().getHeight()/24.0;

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
        thread = new TimeThread();
        thread.start();

        Toast.makeText(getApplicationContext(),"onCreate",Toast.LENGTH_SHORT).show();
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
            Y = canvas.getHeight()/24.0;

            Log.i("path_yunjae", "축적x = " + canvas.getWidth() + " 축적y = " + canvas.getHeight());


            for (int i = 0; i < route.size(); i++) {
                Log.i("nodeNum", "Navigator -> " + route.get(i) + " : " + nodeInfos.get(route.get(i)).getLocationX() + "," + nodeInfos.get(route.get(i)).getLocationY());
                paint.setStrokeWidth(13);
                canvas.drawLine((float) (prevX * X), (float) (prevY * Y), (float) (nodeInfos.get(route.get(i)).getLocationX() * X), (float) (nodeInfos.get(route.get(i)).getLocationY() * Y), paint);
                Log.i("path_yunjae", "endX = " + nodeInfos.get(route.get(i)).getLocationX() + "endY = " + nodeInfos.get(route.get(i)).getLocationY());
                prevX = nodeInfos.get(route.get(i)).getLocationX();
                prevY = nodeInfos.get(route.get(i)).getLocationY();
            }

            canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.mapicon), (float) (prevX * X) - 50, (float) (prevY * Y) - 100, paint);

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
