package com.example.small.Dialog;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.example.small.Activity.HomeActivity;
import com.example.small.Activity.StampActivity;
import com.example.small.Info.UserInfo;
import com.example.small.R;
import com.example.small.Server.HttpClient;

import java.util.HashMap;
import java.util.Map;

public class StampDialog extends AppCompatActivity {

    private String title, content;
    private TextView titleView, contentTextView;
    private Button yesBtn, noBtn;
    private String TAG = "StampDialog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stamp_dialog);

        titleView = (TextView) findViewById(R.id.stampDialogTitle_textView);
        titleView.setText("Stamp Get!");
        contentTextView = (TextView) findViewById(R.id.stampDialogContentTextView);
        contentTextView.setText("스탬프를 획득하셨습니다.\n 스탬프 모으기 메뉴에서 확인해주세요.");

        yesBtn = (Button) findViewById(R.id.yesBtn);
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserInfo userInfo = UserInfo.getUserInfo();
                userInfo.addStamp();
                Log.i("StampCount","Dialog ->"+userInfo.getStamp());

                if(userInfo.getStamp()==3){
                    //Notification띄우기
                    onSimpleNotification(view);
                }

                java.util.Map<String, String> params = new HashMap<String, String>();
                params.put("userid", userInfo.getUserid());
                params.put("stamp", String.valueOf(userInfo.getStamp()));

                stampDB SDB = new stampDB();
                SDB.execute(params);

                finish();
            }
        });


    }

    public final static int MY_NOTIFICATION_ID = 1;
    public void onSimpleNotification(View v) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.gifticon);
        mBuilder.setContentTitle("선물이 도착했어요!");
        mBuilder.setContentText("스탬프 모으기탭에서 선물을 확인하세요.");

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // MY_NOTIFICATION_ID allows you to update the notification later on.
        mNotificationManager.notify(MY_NOTIFICATION_ID, mBuilder.build());
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

    class stampDB extends AsyncTask<Map<String, String>, Integer, String> {
        String serverURL = "http://"+ HttpClient.ipAdress+":8080/Android_saveStamp";
        @Override
        protected String doInBackground(java.util.Map<String, String>...maps) {

            Log.i("StampDB", "서버와 통신");
            HttpClient.Builder http = new HttpClient.Builder("POST",serverURL);
            http.addAllParameters(maps[0]);

            HttpClient post = http.create();
            post.request();

            int statusCode = post.getHttpStatusCode();

            Log.i(TAG, "응답코드stampDB"+statusCode);

            String body = post.getBody();

            Log.i(TAG, "body : "+body);

            return body;

        }
    }
}
