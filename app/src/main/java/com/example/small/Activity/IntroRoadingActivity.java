package com.example.small.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.small.R;

public class IntroRoadingActivity extends AppCompatActivity {

    private Animation animation;
    private Animation.AnimationListener animationListener;
    private Activity thisActivity = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_roading);

        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg){

                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finish();
//                overridePendingTransition(R.anim.alpha);

            }
        };
        handler.sendEmptyMessageDelayed(0,2000);


/*
        animation = AnimationUtils.loadAnimation(this, R.anim.alpha);

        animation.setAnimationListener(
                new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Intent intent = new Intent(thisActivity , HomeActivity.class);
                        startActivity(intent);
                        thisActivity.finish();
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
        });

        thisActivity.animation*/

    }
}
