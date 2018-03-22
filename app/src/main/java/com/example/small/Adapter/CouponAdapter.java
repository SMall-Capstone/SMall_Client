package com.example.small.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.small.Info.MyCouponItem;
import com.example.small.R;

import java.util.ArrayList;

/**
 * Created by HANEUL on 2018-03-21.
 */

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.ViewHolder> {


    private Context context;
    private ArrayList<MyCouponItem> mcoupons; // Allows to remember the last item shown on screen
    private int lastPosition = -1;

    public CouponAdapter(ArrayList<MyCouponItem> coupons, Context mContext) {
        mcoupons = coupons;
        context = mContext;
    } // 필수로 Generate 되어야 하는 메소드 1 : 새로운 뷰 생성

    @Override
    public CouponAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) { // 새로운 뷰를 만든다
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mycoupon_item_cardview, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    } // 필수로 Generate 되어야 하는 메소드 2 : ListView의 getView 부분을 담당하는 메소드

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.imageView.setImageResource(mcoupons.get(position).getCoupon_image());
        holder.textView.setText(mcoupons.get(position).getCoupon_name());
    } // // 필수로 Generate 되어야 하는 메소드 3

    @Override
    public int getItemCount() {
        return mcoupons.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;

        public ViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.coupon_image_cardview);
            textView = (TextView) view.findViewById(R.id.coupon_name_cardview);
        }
    }

}
