package com.example.small.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.small.R;

/**
 * Created by 이예지 on 2017-08-14.
 */

public class LoginFailDialog extends Dialog {

    private String title;
    private View.OnClickListener mLeftClickListener;
    private TextView titleView;
    private Button checkbtn;

    public LoginFailDialog(@NonNull Context context, String title, View.OnClickListener singleListener) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.title = title;
        this.mLeftClickListener = singleListener;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        //WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        //myDialog.getWindow().getAttributes();
        //lpWindow.copyFrom(myDialog.getWindow().getAttributes());
        lpWindow.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lpWindow.height = 300;
        getWindow().setAttributes(lpWindow);
        setContentView(R.layout.dialog);

        titleView = (TextView)findViewById(R.id.textView);
        checkbtn = (Button)findViewById(R.id.button);

        titleView.setText(title);

        if(mLeftClickListener != null) {
            checkbtn.setOnClickListener(mLeftClickListener);
        }

        Log.i("yunjae", title);
    }
}
