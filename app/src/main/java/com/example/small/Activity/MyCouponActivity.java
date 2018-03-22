package com.example.small.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.example.small.Adapter.CouponAdapter;
import com.example.small.Info.MyCouponItem;
import com.example.small.R;

import java.util.ArrayList;

public class MyCouponActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.Adapter Adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<MyCouponItem> items;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_coupon);

        recyclerView = (RecyclerView) findViewById(R.id.myCoupon_recycler_view);

        // Item 리스트에 아이템 객체 넣기
        items = new ArrayList<MyCouponItem>();

        items.add(new MyCouponItem(R.drawable.coupon1, "쿠폰1"));
        items.add(new MyCouponItem(R.drawable.coupon3, "쿠폰2"));
        items.add(new MyCouponItem(R.drawable.coupon1, "쿠폰3"));
        items.add(new MyCouponItem(R.drawable.coupon3, "쿠폰4"));
        items.add(new MyCouponItem(R.drawable.coupon3, "쿠폰5"));

        // StaggeredGrid 레이아웃을 사용한다
       layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
       // layoutManager = new LinearLayoutManager(this);
        //layoutManager = new GridLayoutManager(this,2);

        // 지정된 레이아웃매니저를 RecyclerView에 Set 해주어야한다.
      //  recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        Adapter = new CouponAdapter(items, getApplicationContext());
        recyclerView.setAdapter(Adapter);

    }
}