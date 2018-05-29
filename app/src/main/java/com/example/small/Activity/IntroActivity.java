package com.example.small.Activity;

import android.app.Fragment;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.example.small.R;

public class IntroActivity extends Fragment{

        private Animation animation;
        private Animation.AnimationListener animationListener;
        private Fragment fragment = this;
        public IntroActivity(){

        }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.activity_intro, container, false);
//        initAnimation(this);

        animation = AnimationUtils.loadAnimation(getActivity().getApplicationContext() , R.anim.alpha);
//        animation.setAnimationListener(animationListener);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                /*getUUID();*/
                getActivity().getFragmentManager().beginTransaction().remove(fragment).commit();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        view.startAnimation(animation);

        return view;
    }



/*
    public void getUUID(){

            Intent  intent = new Intent(getActivity().getApplicationContext(), HomeActivity.class);
            startActivity(intent);
            getActivity().finish();

    }
*/

    /*public void initAnimation(final Fragment fragment) {
        Animation.AnimationListener animationListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                *//*getUUID();*//*
//                getActivity().getFragmentManager().beginTransaction().remove(animatio).commit();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };

    }*/
}

