package com.example.small.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.small.R;

public class MyCouponActivity extends AppCompatActivity {

    WebView webView;
    String URL_naver = "https://m.naver.com"; //<-원하는 URL

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_coupon);

        Intent intent = getIntent(); //이 액티비티를 부른 인텐트를 받는다.
        String activity = intent.getStringExtra("activity");
        /*TextView textView = findViewById(R.id.activity_mycoupon);
        textView.setText(activity);*/

        webView = (WebView)findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient()); // 이구문이 빠지면 web view 가아닌 설치된 다른 웹브라우저에서 새창으로 열린다.
        webView.loadUrl(URL_naver);
    }
}
