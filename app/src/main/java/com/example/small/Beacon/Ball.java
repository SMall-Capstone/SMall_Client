package com.example.small.Beacon;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.util.Random;

public class Ball {
    final   int RAD = 20;     // 볼의 반지름
    double x, y, dx, dy;               // 볼의 중심 좌표
    int width, height;    // 볼의 넓이와 높이
    int color;


    private static Ball ball = new Ball(0,0);//싱글톤 패턴 적용
    public static Ball getBallInstance(){
        return ball;
    }


    private Ball(int x, int y) {
        this.x = x;
        this.y = y;

        Random Rnd = new Random();
       /* do {
            dx = Rnd.nextInt(11)-5;     // -5 ~ 5 중 난수로 x방향 속도 설정
            dy = Rnd.nextInt(11)-5;     // -5 ~ 5 중 난수로 y방향 속도 설정
        } while(dx==0 || dy==0);        //  0은 제외*/

        width=height=RAD*2;           // 원의 반지름 (RAD)의 2배가 Ball의 폭과 높이

        color = Color.rgb(255,0,0);
    }

    public void draw(Canvas canvas) {

        Paint paint = new Paint();

        for (int r = RAD, alpha = 1; r > 4; r--, alpha += 5)
        { // 바깥쪽은 흐릿하게 안쪽은 진하게 그려지는 원
            paint.setColor(Color.argb(alpha, Color.red(color),
                    Color.green(color), Color.blue(color)));
            canvas.drawCircle((float)x + RAD, (float)y + RAD, r, paint);
        }
        Log.i("yunjae", "Ball draw x = " + x + " y = " + y);
    }
    /*void move(int width, int height) {
      *//*  x += dx;       // x 좌표값을 dx 만큼 증가
        y += dy;       // y 좌표값을 dy 만큼 증가
        *//*

        if (x<0 || x > width- this.width) {   // 화면 좌우 경계에 닿은 경우
            dx *= -1;                       // 좌우 방향 반전
        }
        if (y<0  || y > height- this.height) {    // 화면 상하 경계에 닿은 경우
            dy *= -1;                           // 상하 방향 반전

        }
    }*/

    public void setLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }
}