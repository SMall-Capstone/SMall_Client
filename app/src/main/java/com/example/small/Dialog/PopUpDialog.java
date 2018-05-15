package com.example.small.Dialog;

import android.app.Fragment;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.small.R;

public class PopUpDialog extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_dialog);

        final TextView popUpClose = (TextView)findViewById(R.id.textView_popUpClose);
        popUpClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    //다이얼로그 영역 외의 부분 클릭시 다이얼로그 꺼짐 방지
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Rect dialogBounds = new Rect();
        getWindow().getDecorView().getHitRect(dialogBounds);
        if(!dialogBounds.contains((int)ev.getX(),(int)ev.getY()))
            return false;
        return super.dispatchTouchEvent(ev);
    }
}
