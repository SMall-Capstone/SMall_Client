package com.example.small.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.small.Beacon.BeaconList;
import com.example.small.Beacon.NodeInfo;
import com.example.small.Beacon.NodeList;
import com.example.small.R;

import java.util.HashMap;
import java.util.Vector;

public class MyLocationActivity extends AppCompatActivity implements View.OnClickListener {

    boolean isFirst = true;
    double previousX = 0, previousY = 0;
    double resultX, resultY;
    Animation ani;
    ImageView img;
    TimeThread thread;
    private Vector<Integer> route;
    private  HashMap<Integer, NodeInfo> nodeInfos;
    private float prevX, prevY;

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

        RelativeLayout lineContainer = (RelativeLayout)findViewById(R.id.lineContainer);

        DrawingView drawingView = new DrawingView(this);


        Intent intent = getIntent();
        int nodeNum = intent.getIntExtra("nodeNum",-1);
        if(nodeNum != -1){
            Log.i("nodeNum","nodeNum =>"+nodeNum);
            beaconList.calculateDistance();
            resultX = beaconList.getResultX();
            resultY = beaconList.getResultY();
            prevX = (float)resultX;
            prevY=(float)resultY;
            Log.i("nodeNum","x,y =>"+resultX+","+resultY);

            NodeList nodeList = new NodeList();
            int startNode = nodeList.searchNearestNode(resultX,resultY);

            Log.i("nodeNum","startNode =>"+startNode);
            nodeList.init();

            route = nodeList.startNavigator(startNode,nodeNum); //다익스트라

            nodeInfos = nodeList.getNodeInfos();
            for(int i=0;i<route.size();i++){
                Log.i("nodeNum","Navigator -> "+route.get(i) +" : "+nodeInfos.get(route.get(i)).getLocationX()+","+nodeInfos.get(route.get(i)).getLocationY());
            }
            if(isFirst){
                lineContainer.addView(drawingView);
                isFirst=false; //화면이 껐다켜지면 다시 그리는 현상 방지
            }
        }

        img = (ImageView) findViewById(R.id.RedPoint);
        thread = new TimeThread();
        thread.start();



    }

    /*윤재*/
    public class DrawingView extends View{

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

            Log.i("path_yunjae", "축적x = " + canvas.getWidth() + " 축적y = " + canvas.getHeight());


            for(int i=0;i<route.size();i++){
                Log.i("nodeNum","Navigator -> "+route.get(i) +" : "+nodeInfos.get(route.get(i)).getLocationX()+","+nodeInfos.get(route.get(i)).getLocationY());
                paint.setStrokeWidth(13);
                canvas.drawLine((float)(prevX*HomeActivity.accumulationX), (float)(prevY*HomeActivity.accumulationY), (float)(nodeInfos.get(route.get(i)).getLocationX()*HomeActivity.accumulationX), (float)(nodeInfos.get(route.get(i)).getLocationY()*HomeActivity.accumulationY), paint);
                Log.i("path_yunjae", "endX = " + nodeInfos.get(route.get(i)).getLocationX() + "endY = " + nodeInfos.get(route.get(i)).getLocationY());
                prevX = nodeInfos.get(route.get(i)).getLocationX();
                prevY = nodeInfos.get(route.get(i)).getLocationY();
            }


            canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.mapicon), (float)(prevX*HomeActivity.accumulationX)-50, (float)(prevY*HomeActivity.accumulationY)-100, paint);

        }
    }
    //


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
