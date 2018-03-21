package com.example.small.Activity;

import android.Manifest;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.small.Adapter.TabPagerAdapter;
import com.example.small.Beacon.Ball;
import com.example.small.Beacon.BeaconInfo;
import com.example.small.Beacon.BeaconList;
import com.example.small.Beacon.KalmanFilter;
import com.example.small.Beacon.Map;
import com.example.small.Dialog.StampDialog;
import com.example.small.R;
import com.example.small.Server.HttpClient;
import com.example.small.ViewPager.CouponFragment;
import com.example.small.ViewPager.EventFragment;
import com.example.small.ViewPager.FloorInfoFragment;
import com.example.small.ViewPager.ShoppingNewsFragment;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static java.lang.Math.pow;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BeaconConsumer {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabPagerAdapter pagerAdapter;

    public Ball ball = Ball.getBallInstance();
    private BeaconManager beaconManager;
    private BluetoothManager bluetoothManager; //블루투스 매니저는 기본적으로 있어야하기때문에 여기서는 생략합니다.
    private BluetoothAdapter bluetoothAdapter; //블루투스 어댑터에서 탐색, 연결을 담당하니 여기서는 어댑터가 주된 클래스입니다.
    private KalmanFilter mKalmanAccRSSI;
    public BeaconList beaconList;
    private double previousX=-1,previousY=-1;
    private double accumulationX = 65.29, accumulationY = 66; //축적 계산한 x,y값에 곱해야 할 값

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        //actionBar.setTitle("Smart Mall");


        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar);

       // getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("쇼핑뉴스"));
        tabLayout.addTab(tabLayout.newTab().setText("이벤트"));
        tabLayout.addTab(tabLayout.newTab().setText("쿠폰"));
        tabLayout.addTab(tabLayout.newTab().setText("층별안내"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.selectedTextColor));
        tabLayout.setTabTextColors(R.color.mainTextColor,getResources().getColor(R.color.selectedTextColor));

        // Initializing ViewPager
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        // Creating TabPagerAdapter adapter
        pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        // Set TabSelectedListener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ImageButton testButton = (ImageButton)findViewById(R.id.testButton);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),StampActivity.class);
                startActivity(intent);
            }
        });

        ImageButton mapButton = (ImageButton)findViewById(R.id.mapButton);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MyLocationActivity.class);
                startActivity(intent);
            }
        });

        beaconServiceStart();//비콘관련 코드 몰아놓음
    }

    private void beaconServiceStart(){
        //비콘 목록 불러오기(Singleton)
        beaconList = BeaconList.getBeaconListInstance();

        //SeongWon 위치권한 물어보기 //haneul 저장권한
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            }, 1);
        }
        //위치권한 물어보기 End

        //칼만필터 초기화
        //End 칼만필터 초기화
        mKalmanAccRSSI = new KalmanFilter();

        // 실제로 비콘을 탐지하기 위한 비콘매니저 객체를 초기화
        beaconManager = BeaconManager.getInstanceForApplication(this);

        // 여기가 중요한데, 기기에 따라서 setBeaconLayout 안의 내용을 바꿔줘야 하는듯 싶다.
        // 필자의 경우에는 아래처럼 하니 잘 동작했음.
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));

        // 비콘 탐지를 시작한다. 실제로는 서비스를 시작하는것.
        beaconManager.bind(this);

        bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            //블루투스를 지원하지 않거나 켜져있지 않으면 장치를끈다.
            Toast.makeText(this, "블루투스를 켜주세요", Toast.LENGTH_SHORT).show();
            finish();
        }

        bluetoothAdapter.startLeScan(mLeScanCallback);//탐색시작
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            // 비콘이 감지되면 해당 함수가 호출된다. Collection<Beacon> beacons에는 감지된 비콘의 리스트가,
            // region에는 비콘들에 대응하는 Region 객체가 들어온다.
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    /*beaconList.clear();
                    for (Beacon beacon : beacons) {
                        beaconList.add(beacon);
                    }*/
                }
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        CouponFragment couponFragment = new CouponFragment();
        EventFragment eventFragment = new EventFragment();
        FloorInfoFragment floorInfoFragment = new FloorInfoFragment();
        ShoppingNewsFragment shoppingNewsFragment = new ShoppingNewsFragment();

        if (id == R.id.menu_myCoupon) {
            Intent intent=new Intent(HomeActivity.this, MyCouponActivity.class);
            intent.putExtra("activity","MyCupon activity");
            startActivity(intent);

        } else if (id == R.id.menu_bookmark) {
            Intent intent=new Intent(HomeActivity.this, BookMarkActivity.class);
            intent.putExtra("activity","BookMark activity");
            startActivity(intent);

        } else if (id == R.id.menu_navi) {
            Intent intent=new Intent(HomeActivity.this, NavigatorActivity.class);
            intent.putExtra("activity","Navi activity");
            startActivity(intent);

        }
        //////////////프레그먼트//////////////////////////////////
        else if (id == R.id.menu_news) {
            viewPager.setCurrentItem(0);

        } else if (id == R.id.menu_event) {
            viewPager.setCurrentItem(1);

        } else if (id == R.id.menu_coupon) {
            viewPager.setCurrentItem(2);

        }  else if (id == R.id.menu_infoFloor) {
            viewPager.setCurrentItem(3);
        }
        /////////////////////////////////////////////////////////

        else if (id == R.id.menu_myInfo) {
            Intent intent=new Intent(HomeActivity.this, MyInfoActivity.class);
            intent.putExtra("activity","MyInfo activity");
            startActivity(intent);

        } else if (id == R.id.menu_setting) {
            Intent intent=new Intent(HomeActivity.this, SettingActivity.class);
            intent.putExtra("activity","Setting activity");
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void replaceFragment(android.support.v4.app.Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.viewPager, fragment, "fragmentTag");
        fragmentTransaction.commit();
    }

    static ArrayList<BeaconInfo> beaconInfos;
    public static int stamp=0;
    public BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            Log.i("yunjae", "callback");

            if (device.getName() != null && device.getName().contains("MiniBeacon")) {
                BeaconInfo beaconInfo = beaconList.findBeacon(device.getName());
                if(beaconInfo==null){
                    Log.i("BeaconName","beaconInfo를 얻지 못함-"+device.getName());
                    //Toast.makeText(getApplicationContext(),"beacon null / "+device.getName(),Toast.LENGTH_LONG).show();
                }
                else{
                    if (beaconInfo.getIsFirst() == true) {
                        //첫번째 측정되는 RSSI값 버리기
                        beaconInfo.setIsFirst(false);
                    } else {
                        int filteredRSSI = (int) mKalmanAccRSSI.applyFilter(rssi);//새로 필터링 된 값

                        //새로 필터링 된 값으로 RSSI값 설정
                        beaconInfo.setFilteredRSSIvalue(filteredRSSI);

                        //거리계산해서 setting
                        double d = (double) pow(10, (beaconList.getTxPower() - filteredRSSI) / (10 * 2));
                        double distance = Double.parseDouble(String.format("%.2f",d));
                        beaconInfo.setDistance(distance);

                        //비콘을 rssi값 기준으로 정렬
                        beaconInfos = beaconList.findNearestBeacons();

                        if(beaconInfos.size() >= 3) {
                            //가장 가까운 3개의 비콘정보로 거리를 계산
                            calculateDistance(beaconInfos.get(0), beaconInfos.get(1), beaconInfos.get(3));
                        }


                        ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
                        List<ActivityManager.RunningTaskInfo> info;
                        info = activityManager.getRunningTasks(7);
                        for (Iterator iterator = info.iterator(); iterator.hasNext();)  {
                            ActivityManager.RunningTaskInfo runningTaskInfo = (ActivityManager.RunningTaskInfo) iterator.next();
                            if(runningTaskInfo.topActivity.getClassName().equals("com.example.small.Activity.StampActivity")) {
                                if (beaconInfos.get(0).isStampBeacon()) {
                                    if (beaconInfos.get(0).getCount() == 3) {
                                        Log.i("StampEvent",beaconInfos.get(0).getMinor()+"스탬프 이벤트 발생 count="+beaconInfos.get(0).getCount());
                                        //스탬프 비콘에 가장 가깝게 다가간 측정횟수가 3번일 때 스탬프 다이얼로그 발생
                                        //stampDialog(getApplicationContext());
                                        Intent intent = new Intent(getApplicationContext(),StampDialog.class);
                                        intent.putExtra("stamp",stamp);
                                        startActivity(intent);

                                        beaconInfos.get(0).setCount(beaconInfos.get(0).getCount() + 1);
                                    } else {
                                        //쿠폰 비콘에 가장 가깝게 다가간 측정횟수 +1
                                        beaconInfos.get(0).setCount(beaconInfos.get(0).getCount() + 1);
                                    }
                                }
                            }
                        }



                    }
                }

            }

        }//onLeScan끝

        double resultX,resultY;

        public void calculateDistance(BeaconInfo b1,BeaconInfo b2, BeaconInfo b3){
            double X1 = b1.getLocation_x();
            double Y1 = b1.getLocation_y();
            double X2 = b2.getLocation_x();
            double Y2 = b2.getLocation_y();
            double D1 = b1.getDistance();
            double D2 = b2.getDistance();


            Log.i("Check//",b1.getName()+"/"+b2.getName()+"/"+b3.getName());
            Log.i("Check//",X1+"/"+Y1+"/"+X2+"/"+Y2+"/"+D1+"/"+D2);

            double T = Math.log( Math.pow((X2 - X1),2) + Math.pow((Y2 - Y1),2));
            double TrianglePlusX = X1 + D1 * Math.cos( Math.atan( (Y2 - Y1) / (X2 - X1) ) +
                    Math.acos( (Math.pow(D1,2) - Math.pow(D2,2) + Math.pow(T,2) ) / (2 * D1 * T) ) );
            double TriangleMinusX = X1 + D1 * Math.cos( Math.atan( (Y2 - Y1) / (X2 - X1) ) -
                    Math.acos( (Math.pow(D1,2) - Math.pow(D2,2) + Math.pow(T,2) ) / (2 * D1 * T) ) );
            double TrianglePlusY = Y1 + D1 * Math.sin( Math.atan( (Y2 - Y1) / (X2 - X1) ) +
                    Math.acos( (Math.pow(D1,2) -  Math.pow(D2, 2) + Math.pow(T,2) ) / (2 * D1 * T) ) );
            double TriangleMinusY = Y1 + D1 * Math.sin( Math.atan( (Y2 - Y1) / (X2 - X1) ) -
                    Math.acos( (Math.pow(D1,2) -  Math.pow(D2, 2) + Math.pow(T,2) ) / (2 * D1 * T) ) );



            double d1 = pointTopointDistance(TrianglePlusX,TrianglePlusY,b3.getLocation_x(),b3.getLocation_y());
            double d2 = pointTopointDistance(TriangleMinusX,TriangleMinusY,b3.getLocation_x(),b3.getLocation_y());
            if(d1 < d2){
                resultX = TrianglePlusX;
                resultY = TrianglePlusY;
            }
            else {
                resultX = TriangleMinusX;
                resultY = TriangleMinusY;
            }

            Map m = Map.getMapInstance();
            if(resultX < 0 || resultX > m.getMaxWidth()) {
                if(resultX<0)
                    resultX = 0;
                if(resultX>m.getMaxWidth())
                    resultX = m.getMaxWidth();
            }
            if(resultY < 0 || resultX > m.getMaxHeight()) {
                if(resultY<0)
                    resultY = 0;
                if(resultY>m.getMaxHeight())
                    resultY = m.getMaxHeight();
            }

            //좌표가 NaN으로 나올 경우 이전값으로 대체하여 사용
            if(Double.isNaN(resultX) || Double.isNaN(resultY)){
                resultX = previousX;
                resultY = previousY;
            }

            if(previousX==-1 && previousY==-1) {
                //이전에 저장된 값이 없는 경우=>처음 측정된 값
                previousX=resultX;
                previousY=resultY;

                Log.i("현재 위치","현재 위치 : ("+Double.parseDouble(String.format("%.2f",resultX))+","+Double.parseDouble(String.format("%.2f",resultY))+")\n");
                ball.setLocation(resultX*accumulationX, resultY*accumulationY);
                Log.i("yunjae", "x = " + resultX + " y = " + resultY);
            }
            else {
                //이전의 값과 차이가 설정 값 이상 나지 않는 경우에만 좌표출력
                if ( ! (previousX-resultX<-5 || previousX-resultX>5) ){
                    Log.i("현재 위치","현재 위치 : ("+Double.parseDouble(String.format("%.2f",resultX))+","+Double.parseDouble(String.format("%.2f",resultY))+")\n");
                    //ball.setLocation((float)resultX*(float)62.29, (float)resultY*(float)77.14);
                    ball.setLocation(resultX*accumulationX, resultY*accumulationY);
                    Log.i("yunjae", "x = " + resultX + " y = " + resultY);
                }
            }

        }

        public double pointTopointDistance(double x1,double y1,double x2,double y2){
            return Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
        }

    };
}
