package com.example.small.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.small.Info.UserInfo;
import com.example.small.R;
import com.example.small.Server.HttpClient;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    String userid,password,name,gender,birth;
    boolean isCheckId=false;
    String serverURL_duplicate_check = "http://"+HttpClient.ipAdress+":8080/Android_login_duplicate_check";
    String serverURL_register = "http://"+HttpClient.ipAdress+":8080/Android_register";

    private CheckBox check_acc;
    private CheckBox check_bag_shoes;
    private CheckBox check_beauty;
    private CheckBox check_casual;
    private CheckBox check_child;
    private CheckBox check_furniture;
    private CheckBox check_health;
    private CheckBox check_home_appliances;
    private CheckBox check_sport;
    private CheckBox check_suit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        TextView checkIdBtn = (TextView)findViewById(R.id.checkIdBtn);
        checkIdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //서버에 아이디 중복 확인
                checkID();

            }
        });

        TextView submitBtn = (TextView)findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isCheckId == false){
                    Toast.makeText(getApplicationContext(),"아이디 중복을 확인해주세요.",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(checkPW() && checkGender() && checkBirth()){
                        name = ((EditText)findViewById(R.id.editText_name)).getText().toString();

                        Map<String,String> params = new HashMap<String,String>();
                        params.put("userid",userid);
                        params.put("password",password);
                        params.put("name",name);
                        params.put("gender",gender);
                        params.put("birth",birth);

                        registDB RDB = new registDB();
                        RDB.execute(params);
                    }
                }

            }
        });


        /////////////////////관심사 체크박스/////////////////////////

        check_acc = (CheckBox)findViewById(R.id.check_acc);
        check_bag_shoes = (CheckBox)findViewById(R.id.check_bag_shoes);
        check_beauty = (CheckBox)findViewById(R.id.check_beauty);
        check_casual = (CheckBox)findViewById(R.id.check_casual);
        check_child = (CheckBox)findViewById(R.id.check_child);
        check_furniture = (CheckBox)findViewById(R.id.check_furniture);
        check_health = (CheckBox)findViewById(R.id.check_health);
        check_home_appliances = (CheckBox)findViewById(R.id.check_home_appliances);
        check_sport = (CheckBox)findViewById(R.id.check_sport);
        check_suit = (CheckBox)findViewById(R.id.check_suit);

    }

    public void checkboxClick(View v){              // CheckBox 에 onClick 메소드 설정, CheckBox에 설정한 id 값으로 제어
        if(check_acc.isChecked()){
            Toast.makeText(getApplicationContext(), check_acc.getText().toString(), Toast.LENGTH_SHORT).show();
        } else if(check_bag_shoes.isChecked()) {
            Toast.makeText(getApplicationContext(), check_bag_shoes.getText().toString(), Toast.LENGTH_SHORT).show();
        } else if(check_beauty.isChecked()) {
            Toast.makeText(getApplicationContext(),check_beauty.getText().toString(),Toast.LENGTH_SHORT).show();
        }else if(check_casual.isChecked()) {
            Toast.makeText(getApplicationContext(), check_casual.getText().toString(), Toast.LENGTH_SHORT).show();
        }else if(check_child.isChecked()) {
            Toast.makeText(getApplicationContext(), check_child.getText().toString(), Toast.LENGTH_SHORT).show();
        }else if(check_furniture.isChecked()) {
            Toast.makeText(getApplicationContext(), check_furniture.getText().toString(), Toast.LENGTH_SHORT).show();
        }else if(check_health.isChecked()) {
            Toast.makeText(getApplicationContext(), check_health.getText().toString(), Toast.LENGTH_SHORT).show();
        }else if(check_home_appliances.isChecked()) {
            Toast.makeText(getApplicationContext(), check_home_appliances.getText().toString(), Toast.LENGTH_SHORT).show();
        }else if(check_sport.isChecked()) {
            Toast.makeText(getApplicationContext(), check_sport.getText().toString(), Toast.LENGTH_SHORT).show();
        }else if(check_suit.isChecked()) {
            Toast.makeText(getApplicationContext(), check_suit.getText().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void checkID(){
        //서버에 아이디 보내서 중복 확인
        userid = ((EditText)findViewById(R.id.editText_id)).getText().toString();

        Map<String,String> params = new HashMap<String,String>();
        params.put("userid",userid);

        idCheckDB IDB = new idCheckDB();
        IDB.execute(params);

    }

    private boolean checkPW(){

        Pattern p = Pattern.compile("(^.*(?=.{6,100})(?=.*[0-9])(?=.*[a-zA-Z]).*$)");

        password = ((EditText)findViewById(R.id.editText_pw)).getText().toString();
        Matcher m = p.matcher(password);
        if (m.find() && !password.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*") && password.length()>7){
            return true;
        }else{
            Toast.makeText(getApplicationContext(),"비밀번호는 영문,숫자를 조합하여 8자 이상으로 작성해주세요.",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean checkGender(){
        if(((RadioButton)findViewById(R.id.radioButton_man)).isChecked())
            gender="M";
        else
            gender="W";
        return true;
    }

    private boolean checkBirth(){
        birth = ((EditText) findViewById(R.id.editText_birth)).getText().toString();
        if(birth.length()==8) {
            return true;
        }
        else
        {
            Toast.makeText(getApplicationContext(),"생년월일을 잘못 입력하셨습니다.",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    class idCheckDB extends AsyncTask<Map<String, String>, Integer, String> {

        @Override
        protected String doInBackground(Map<String, String>...maps) {

            HttpClient.Builder http = new HttpClient.Builder("POST",serverURL_duplicate_check);
            http.addAllParameters(maps[0]);

            HttpClient post = http.create();
            post.request();

            int statusCode = post.getHttpStatusCode();

            Log.i("yunjae", "응답코드"+statusCode);

            String body = post.getBody();

            Log.i("yunjae", "body : "+body);

            return body;

        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            Log.i("yunjae", aVoid);

            if(aVoid.contains("NOK")){
                Log.i("yunjae", "아이디 중복");
                ((EditText)findViewById(R.id.editText_id)).setText("");
                Toast.makeText(getApplicationContext(),"이미 존재하는 아이디 입니다.",Toast.LENGTH_SHORT).show();
                isCheckId=false;
            }
            else{
                //OK
                Toast.makeText(getApplicationContext(),"사용가능한 아이디 입니다.",Toast.LENGTH_SHORT).show();
                ((EditText)findViewById(R.id.editText_id)).setClickable(false);
                isCheckId=true;
            }

        }
    }

    class registDB extends AsyncTask<Map<String, String>, Integer, String> {

        @Override
        protected String doInBackground(Map<String, String>...maps) {

            HttpClient.Builder http = new HttpClient.Builder("POST",serverURL_register);
            http.addAllParameters(maps[0]);

            HttpClient post = http.create();
            post.request();

            int statusCode = post.getHttpStatusCode();

            Log.i("yunjae", "응답코드"+statusCode);

            String body = post.getBody();

            Log.i("yunjae", "body : "+body);

            return body;

        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            Log.i("yunjae", aVoid);

            if(aVoid.contains("OK")){
                Toast.makeText(getApplicationContext(),"회원가입을 환영합니다. 로그인 후 이용해 주세요.",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
            }
            else{
                //NOK
                Log.i("yunjae", "회원가입 오류");
                Toast.makeText(getApplicationContext(),"회원가입 오류",Toast.LENGTH_SHORT).show();
            }

        }
    }

}
