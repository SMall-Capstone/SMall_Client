package com.example.small.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.small.Info.UserInfo;
import com.example.small.R;
import com.example.small.Server.HttpClient;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    String userid,password,name,gender,birth;
    boolean isCheckId=false;
    String serverURL_duplicate_check = "http://"+HttpClient.ipAdress+":8080/Android_login_duplicate_check";
    String serverURL_register = "http://"+HttpClient.ipAdress+":8080/Android_register";

    private Spinner spinner_fashion;
    private Spinner spinner_beauty;
    private Spinner spinner_general;
    private Spinner spinner_sports;
    private Spinner spinner_health;

    private int fashion_point = 0;
    private int beauty_point = 0;
    private int general_point = 0;
    private int sports_point = 0;
    private int health_point = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        TextView checkIdBtn = (TextView) findViewById(R.id.checkIdBtn);
        checkIdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //서버에 아이디 중복 확인
                checkID();

            }
        });

        TextView submitBtn = (TextView) findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCheckId == false) {
                    Toast.makeText(getApplicationContext(), "아이디 중복을 확인해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    if (checkPW() && checkGender() && checkBirth()) {

                        fashion_point = spinnerPoint(spinner_fashion);
                        beauty_point = spinnerPoint(spinner_beauty);
                        general_point = spinnerPoint(spinner_general);
                        sports_point = spinnerPoint(spinner_sports);
                        health_point = spinnerPoint(spinner_health);

                        Log.i("haneul","fashion point : " + fashion_point +" / "+ spinner_fashion.getSelectedItem() + "\n" +
                                "beauty point : " + beauty_point +" / "+ spinner_beauty.getSelectedItem() + "\n" +
                                "general point : " + general_point +" / "+ spinner_general.getSelectedItem() + "\n" +
                                "sports point : " + sports_point +" / "+ spinner_sports.getSelectedItem() + "\n" +
                                "health point : " + health_point +" / "+ spinner_health.getSelectedItem() + "\n");


                        name = ((EditText) findViewById(R.id.editText_name)).getText().toString();

                        Map<String, String> params = new HashMap<String, String>();
                        params.put("userid", userid);
                        params.put("password", password);
                        params.put("name", name);
                        params.put("gender", gender);
                        params.put("birth", birth);

                        /*registDB RDB = new registDB();
                        RDB.execute(params);*/
                    }
                }

            }
        });

        /////////////////////관심사 spinner/////////////////////////



        spinner_fashion = (Spinner) findViewById(R.id.spinner_fashion);
        spinner_beauty = (Spinner) findViewById(R.id.spinner_beauty);
        spinner_general = (Spinner) findViewById(R.id.spinner_general);
        spinner_sports = (Spinner) findViewById(R.id.spinner_sports);
        spinner_health = (Spinner) findViewById(R.id.spinner_health);

        //input array data
        final ArrayList<String> list = new ArrayList<>();
        list.add("관심없음");
        list.add("그저그럼");
        list.add("관심있음");

        ArrayAdapter spinnerAdapter;
        spinnerAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, list);
        spinner_fashion.setAdapter(spinnerAdapter);
        spinner_beauty.setAdapter(spinnerAdapter);
        spinner_general.setAdapter(spinnerAdapter);
        spinner_sports.setAdapter(spinnerAdapter);
        spinner_health.setAdapter(spinnerAdapter);

     /*   spinner_fashion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(SignUpActivity.this,"fashion : "+ fashion , Toast.LENGTH_SHORT);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
    }

    private int spinnerPoint (Spinner spinner) {
        int point = 0;

        if (spinner.getSelectedItem().toString().equals("관심있음")) {
            point = 5;
        } else if (spinner.getSelectedItem().toString().equals("그저그럼")){
            point = 2;
        }

        return point;
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
