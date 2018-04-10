package com.example.small.ViewPager;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.small.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CouponFragment extends Fragment {
    private WebView mWebView;
    private TextView errorVeiw;

    public CouponFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coupon, container, false);

        errorVeiw = (TextView)view.findViewById(R.id.web_error_view_coupon);
        mWebView = (WebView)view.findViewById(R.id.web_coupon_page);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            //네트워크연결에러
            @Override
            public void onReceivedError(WebView view, int errorCode,String description, String failingUrl) {
                switch(errorCode) {
                    case ERROR_AUTHENTICATION:
                        break;               // 서버에서 사용자 인증 실패
                    case ERROR_BAD_URL:
                        break;                           // 잘못된 URL
                    case ERROR_CONNECT:
                        break;                          // 서버로 연결 실패
                    case ERROR_FAILED_SSL_HANDSHAKE:
                        break;    // SSL handshake 수행 실패
                    case ERROR_FILE:
                        break;                                  // 일반 파일 오류
                    case ERROR_FILE_NOT_FOUND:
                        break;               // 파일을 찾을 수 없습니다
                    case ERROR_HOST_LOOKUP:
                        break;           // 서버 또는 프록시 호스트 이름 조회 실패
                    case ERROR_IO:
                        break;                              // 서버에서 읽거나 서버로 쓰기 실패
                    case ERROR_PROXY_AUTHENTICATION:
                        break;   // 프록시에서 사용자 인증 실패
                    case ERROR_REDIRECT_LOOP:
                        break;               // 너무 많은 리디렉션
                    case ERROR_TIMEOUT:
                        break;                          // 연결 시간 초과
                    case ERROR_TOO_MANY_REQUESTS:
                        break;     // 페이지 로드중 너무 많은 요청 발생
                    case ERROR_UNKNOWN:
                        break;                        // 일반 오류
                    case ERROR_UNSUPPORTED_AUTH_SCHEME:
                        break; // 지원되지 않는 인증
                    case ERROR_UNSUPPORTED_SCHEME:
                        break;          // URI가 지원되지 않는 방식
                }
                super.onReceivedError(view, errorCode, description, failingUrl);

                mWebView.setVisibility(View.GONE);
                errorVeiw.setVisibility(View.VISIBLE);
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            //alert 처리
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                new AlertDialog.Builder(view.getContext())
                        .setTitle("알림")
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok,
                                new AlertDialog.OnClickListener(){
                                    public void onClick(DialogInterface dialog, int which) {
                                        result.confirm();
                                    }
                                })
                        .setCancelable(false)
                        .create()
                        .show();
                return true;
            }
            //confirm 처리

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                new AlertDialog.Builder(view.getContext()).setTitle("알림").setMessage(message).setPositiveButton("Yes",
                        new AlertDialog.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm();
                            }
                        }).setNegativeButton("No", new AlertDialog.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {
                        result.cancel();
                    }
                }).setCancelable(false).create().show();
                return true;
            }
        });

        mWebView.loadUrl("http://223.194.131.184:8080/couponPage");
        return view;
    }

}
