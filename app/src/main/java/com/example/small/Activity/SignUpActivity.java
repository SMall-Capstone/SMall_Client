package com.example.small.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.small.R;

public class SignUpActivity extends AppCompatActivity {

    String userid,password,name,gender;
    int birth;
    boolean isCheckId=false;
    String serverURL = "http://223.194.156.73/AndroidProject_SharedDiary/login.jsp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        TextView checkIdBtn = (TextView)findViewById(R.id.checkIdBtn);
        checkIdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //서버에 아이디 중복 확인
                isCheckId=true; //중복되지 않은 아이디인 경우 true

            }
        });

        TextView submitBtn = (TextView)findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isCheckId == false){
                    Toast.makeText(getApplicationContext(),"아이디 중복을 확인해주세요.",Toast.LENGTH_SHORT);
                }

                if(checkPW() && checkGender() &&checkBirth()){

                }
            }
        });
    }

    private boolean checkID(){

        return true;
    }

    private boolean checkPW(){
        return true;
    }

    private boolean checkGender(){
        return true;
    }

    private boolean checkBirth(){
        return true;
    }
}
