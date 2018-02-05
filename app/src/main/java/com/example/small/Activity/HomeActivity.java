package com.example.small.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
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
import android.widget.ImageButton;

import com.example.small.Adapter.TabPagerAdapter;
import com.example.small.R;
import com.example.small.ViewPager.CouponFragment;
import com.example.small.ViewPager.EventFragment;
import com.example.small.ViewPager.FloorInfoFragment;
import com.example.small.ViewPager.ShoppingNewsFragment;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabPagerAdapter pagerAdapter;

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
//////////////프레그먼트////////////////////////////////
        else if (id == R.id.menu_event) {

         //   replaceFragment(eventFragment);

            viewPager.setCurrentItem(1);


        } else if (id == R.id.menu_coupon) {
            viewPager.setCurrentItem(2);

        } else if (id == R.id.menu_news) {
            viewPager.setCurrentItem(0);

        } else if (id == R.id.menu_infoFloor) {
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
}
