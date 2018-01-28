package com.example.small.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;

import com.example.small.LoginFailDialog;
import com.example.small.R;
import com.example.small.Server.HttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    String userid,password;
    String serverURL = "http://116.43.86.11:8080/Android_login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    public void onButtonLogin(View v) {
        userid = ((EditText) findViewById(R.id.etId)).getText().toString();
        password = ((EditText) findViewById(R.id.etPassword)).getText().toString();

        Map<String,String> params = new HashMap<String,String>();
        params.put("userid",userid);
        params.put("password",password);

        loginDB IDB = new loginDB();
        IDB.execute(params);
    }

    public void onButtonSignUp(View v){
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
    }


    LoginFailDialog loginFailDialog;

    class loginDB extends AsyncTask<Map<String, String>, Integer, String> {

        String data=null;
        String receiveMsg="";
        //id, pw, 나이, 성별, 보유쿠폰, 즐겨찾기정보
        String name,gender,myCoupon,myBookmark;
        int birth;

        @Override
        protected String doInBackground(Map<String, String>...maps) {

            /*String param = "userid=" + userid + "&password=" + password +"";
            Log.i("yunjae", param);*/

            HttpClient.Builder http = new HttpClient.Builder("POST",serverURL);
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

        }

    }
    public View.OnClickListener leftListener = new View.OnClickListener() {
        public void onClick(View v) {
            Log.i("yunjae", "왼쪽클릭");
            loginFailDialog.dismiss();
        }
    };
}
