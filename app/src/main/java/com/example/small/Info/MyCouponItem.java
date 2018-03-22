package com.example.small.Info;

import android.widget.ImageView;

/**
 * Created by HANEUL on 2018-03-21.
 */

public class MyCouponItem {



    private int coupon_image;
    private String coupon_name;

    public MyCouponItem(int coupon_image, String coupon_name) {
        this.coupon_image=coupon_image;
        this.coupon_name=coupon_name;
    }

    public int getCoupon_image() {
        return coupon_image;
    }

    public void setCoupon_image(int coupon_image) {
        this.coupon_image = coupon_image;
    }

    public String getCoupon_name() {
        return coupon_name;
    }

    public void setCoupon_name(String coupon_name) {
        this.coupon_name = coupon_name;
    }
}
