package com.example.small.Activity;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import com.example.small.R;
import com.example.small.Server.HttpClient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NavigatorActivity extends AppCompatActivity implements BeaconConsumer {

    private BeaconManager beaconManager;
    // 감지된 비콘들을 임시로 담을 리스트
    private List<Beacon> beaconList = new ArrayList<>();
    TextView textView;

    Beacon nearestBeacon;
    String serverURL = "http://"+HttpClient.ipAdress+":8080/Android_beacon_location";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigator);

        // 실제로 비콘을 탐지하기 위한 비콘매니저 객체를 초기화
        beaconManager = BeaconManager.getInstanceForApplication(this);
        textView = (TextView)findViewById(R.id.Textview);

        // 여기가 중요한데, 기기에 따라서 setBeaconLayout 안의 내용을 바꿔줘야 하는듯 싶다.
        // 필자의 경우에는 아래처럼 하니 잘 동작했음.
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));

        // 비콘 탐지를 시작한다. 실제로는 서비스를 시작하는것.
        beaconManager.bind(this);


/*
        double distance=100000;

        for(Beacon beacon : beaconList){
            String strBeacon = "Major ID : " + beacon.getId2() +" Minor ID : " + beacon.getId3();
            textView.append(strBeacon + " Distance : " + Double.parseDouble(String.format("%.3f", beacon.getDistance())) + "m\n");
            Log.i("yunjae", "비콘 정보 : " + strBeacon);
            if(distance>beacon.getDistance()) {
                nearestBeacon = beacon;
                distance=beacon.getDistance();
            }
        }
*/

        /*Map<String,String> params = new HashMap<String,String>();
        params.put("id",nearestBeacon.getId1()+"");
        params.put("major",nearestBeacon.getId1()+"");
        params.put("minor",nearestBeacon.getId1()+"");

        beconDB BDB = new beconDB();
        BDB.execute(params);*/

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            // 비콘이 감지되면 해당 함수가 호출된다. Collection<Beacon> beacons에는 감지된 비콘의 리스트가,
            // region에는 비콘들에 대응하는 Region 객체가 들어온다.
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    beaconList.clear();
                    for (Beacon beacon : beacons) {
                        beaconList.add(beacon);
                    }
                }
            }

        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {   }
    }

    // 버튼이 클릭되면 textView 에 비콘들의 정보를 뿌린다.
    public void OnButtonClicked(View view){
        // 아래에 있는 handleMessage를 부르는 함수. 맨 처음에는 0초간격이지만 한번 호출되고 나면
        // 1초마다 불러온다.
        handler.sendEmptyMessage(0);
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            textView.setText("");
            // 비콘의 아이디와 거리를 측정하여 textView에 넣는다.
            double distance=100000;
            for(Beacon beacon : beaconList){
                String strBeacon = "Major ID : " + beacon.getId2() +" Minor ID : " + beacon.getId3();
                textView.append(strBeacon + " Distance : " + Double.parseDouble(String.format("%.3f", beacon.getDistance())) + "m\n");
                //Log.i("yunjae", "비콘 정보 : " + strBeacon);
                if(distance>beacon.getDistance()) {
                    nearestBeacon = beacon;
                    distance=beacon.getDistance();
                }
            }

            Map<String,String> params = new HashMap<String,String>();
            params.put("id",nearestBeacon.getId1()+"");
            params.put("major",nearestBeacon.getId2()+"");
            params.put("minor",nearestBeacon.getId3()+"");
            Log.i("yunjae", "id1 : " + nearestBeacon.getId1());

            beconDB BDB = new beconDB();
            BDB.execute(params);

            // 자기 자신을 3초마다 호출
            handler.sendEmptyMessageDelayed(0, 3000);
        }
    };


    class beconDB extends AsyncTask<Map<String, String>, Integer, String> {

        @Override
        protected String doInBackground(Map<String, String>...maps) {

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

            Toast.makeText(getApplicationContext(),aVoid,Toast.LENGTH_LONG).show();

        }
    }
}
