package com.example.small.Beacon;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.small.R;

public class AnimatedView extends SurfaceView implements SurfaceHolder.Callback{
    Context context;
    //ArrayList<Ball> balls= new ArrayList<Ball>();
    Ball ball = Ball.getBallInstance();
    TimerThread timerThread;
    SurfaceHolder surfaceHolder;
    Bitmap bitmap;
    Paint mPaint;

    public AnimatedView(Context context) {
        super(context);
        this.context= context;
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mapimage);
        mPaint = new Paint();
    }

    public AnimatedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context= context;
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        /*timerThread= new TimerThread();
        timerThread.start();*/ //onSizeChanged->surfaceCreated
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        timerThread= new TimerThread();
        timerThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stop();
    }

    public void stop(){
        if(timerThread != null){

            timerThread.isRun= false; //timerThread.interrupt();
            try {
                timerThread.join();
            }catch(InterruptedException e){
            }
        }
    }

    /*public void updateAnimation() {
        for (int i=0; i<balls.size(); i++) {
            Ball B = balls.get(i);
            B.move(getWidth(),getHeight());
        }
    }*/

    /*public void updateAnimation() {
            ball.move(getWidth(),getHeight());
    }*/

    /*protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i("yunjae", "onDraw");
        updateAnimation();
        for (int i=0; i<balls.size(); i++) {
            Ball B = balls.get(i);
            B.draw(canvas);
        }

    }*/

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i("yunjae", "onDraw");
        //updateAnimation();
        ball.draw(canvas);
        invalidate();
    }

    /*@Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            //balls.add(new Ball((int)event.getX(), (int)event.getY()));
            balls.add(new Ball(50, 50));
            invalidate();
            return true;
        }
        return super.onTouchEvent(event);
    }*/

    Handler handler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            invalidate();
        }
    };

    /*class TimerThread extends Thread {
        public boolean  isRun= true;
        int ticker = 0;
        public void run(){
            while(isRun){
                Log.i("jmlee", "thread is running");
                //handler.sendEmptyMessage(0);
                // part1 : paint
                if(ticker++ % 10 == 0) {
                    synchronized (surfaceHolder) {
                        Canvas canvas = surfaceHolder.lockCanvas();
                        canvas.drawColor(Color.WHITE);
                        for (int i = 0; i < balls.size(); i++) {
                            Ball B = balls.get(i);
                            B.draw(canvas);
                        }
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
                // part2 : update
                updateAnimation();

                try{
                    sleep(10);
                }catch(InterruptedException e){
                    break;
                }
            }
        }
    }*/
    class TimerThread extends Thread {
        public boolean  isRun= true;
        int ticker = 0;
        public void run(){
            while(isRun){
                Log.i("jmlee", "thread is running");
                //handler.sendEmptyMessage(0);
                // part1 : paint
                if(ticker++ % 10 == 0) {
                    synchronized (surfaceHolder) {
                        Canvas canvas = surfaceHolder.lockCanvas();
                        canvas.drawColor(Color.WHITE);

                        int width = canvas.getWidth();
                        int height = canvas.getHeight();

                        Bitmap resize_bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
                        canvas.drawBitmap(resize_bitmap, 0, 0, null);

                        Log.i("canvas", "x = " + canvas.getWidth() + " y = " + canvas.getHeight());
                        ball.draw(canvas);
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
                // part2 : update
                //updateAnimation();

                try{
                    sleep(10);
                }catch(InterruptedException e){
                    break;
                }
            }
        }
    }
}

