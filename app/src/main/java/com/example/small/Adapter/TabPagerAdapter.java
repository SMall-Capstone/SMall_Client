package com.example.small.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.small.ViewPager.CouponFragment;
import com.example.small.ViewPager.FloorInfoFragment;
import com.example.small.ViewPager.RecommendFragment;
import com.example.small.ViewPager.ShoppingNewsFragment;

/**
 * Created by 이예지 on 2018-01-23.
 */

public class TabPagerAdapter extends FragmentStatePagerAdapter {

    private int tabCount;
    private ShoppingNewsFragment shoppingNewsFragment = new ShoppingNewsFragment();
    private RecommendFragment recommendFragment = new RecommendFragment();
    private CouponFragment couponFragment = new CouponFragment();
    private FloorInfoFragment floorInfoFragment = new FloorInfoFragment();

    public TabPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {

        // Returning the current tabs
        switch (position) {
            case 0:
                return recommendFragment;
            case 1:
                return shoppingNewsFragment;
            case 2:
                return couponFragment;
            case 3:
                return floorInfoFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
