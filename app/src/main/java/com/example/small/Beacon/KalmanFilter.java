package com.example.small.Beacon;
/*

import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

*/

import android.widget.TextView;

/**
 * Created by user on 2018-02-19.
 *//*


public class KalmanFilter {

//
//    private double Q = 0.00001;
//    private double R = 0.001;
//    private double X = 0, P = 1, K;
//    private double sum=0;
//
//    //첫번째값을 입력받아 초기화 한다. 예전값들을 계산해서 현재값에 적용해야 하므로 반드시 하나이상의 값이 필요하므로~
//    KalmanFilter(double initValue) {
//        X = initValue;
//    }
//
//    //예전값들을 공식으로 계산한다
//    private void measurementUpdate(){
//        K = (P + Q) / (P + Q + R);
//        P = R * (P + Q) / (R + P + Q);
//    }
//    //현재값을 받아 계산된 공식을 적용하고 반환한다
//    public double update(double measurement, String beaconId, TextView textView, TextView textView_distance){
//        measurementUpdate();
//        X = X + (measurement - X) * K;
//        textView_distance.setText(" " + Math.pow(10, (-66 - X) / (10 * 2))); // 위쪽 거리 텍스트뷰
//        textView.append(beaconId + " 평균 = " + X + "\n"); //아래쪽 텍스트뷰
//        return X;
//    }

    private double Q = 0.1; //작을수록 비콘간 rssi 세기 차이 정확함
    private double R = 0.001; // 작을수록 측정값이 튀는 정도가 약해짐
    private double X = 0, P = 1, K;
    private double sum=0;

    //첫번째값을 입력받아 초기화 한다. 예전값들을 계산해서 현재값에 적용해야 하므로 반드시 하나이상의 값이 필요하므로~
    KalmanFilter(double initValue) {
        X = initValue;
    }

    //예전값들을 공식으로 계산한다
    private void measurementUpdate(){
        K = (P + Q) / (P + Q + R);
        P = R * (P + Q) / (R + P + Q);
    }
    //현재값을 받아 계산된 공식을 적용하고 반환한다
    public double update(double measurement, String beaconId, TextView textView, TextView textView_distance){
        measurementUpdate();
      */
/*  if(((X + 10) < measurement) || ((X - 10) > measurement)){
            return X;
        }
        else {
            X = X + (measurement - X) * K;
            textView_distance.setText(" " + Math.pow(10, (-66 - X) / (10 * 2))); // 위쪽 거리 텍스트뷰
            textView.append(beaconId + " 평균 = " + X + "\n"); //아래쪽 텍스트뷰
            return X;
        }*//*


        X = X + (measurement - X) * K;

        textView_distance.setText(" " + Math.pow(10, (-66 - X) / (10 * 2))); // 위쪽 거리 텍스트뷰
        textView.append(beaconId + " 평균 = " + X + "\n"); //아래쪽 텍스트뷰
        return X;

    }
}

*/

    //Seongwon 수정
//============================================================================================================
public class KalmanFilter implements RssiFilter{
    private double processNoise;//Process noise
    private double measurementNoise;//Measurement noise
    private double estimatedRSSI;//calculated rssi
    private double errorCovarianceRSSI;//calculated covariance
    private boolean isInitialized = false;//initialization flag

    public KalmanFilter(){
        this.processNoise = 0.125;
        this.measurementNoise = 0.8;
    /*  this.processNoise = 0.3;
        this.measurementNoise = 0.5;*/
    }
    public KalmanFilter(double processNoise, double measurementNoise){
        this.processNoise = processNoise;
        this.measurementNoise = measurementNoise;
    }

    TextView View;

    public TextView getTextView(TextView View){
        return View;
    }

    public TextView setTextView(TextView View){
        return this.View = View;
    }

    @Override
    public double applyFilter(double rssi){
        double priorRSSI;
        double kalmanGain;
        double priorErrorCovarianceRSSI;
        if (!isInitialized){
            priorRSSI = rssi;
            priorErrorCovarianceRSSI = 1;
            isInitialized = true;
        }else {
            priorRSSI = estimatedRSSI;
            priorErrorCovarianceRSSI = errorCovarianceRSSI + processNoise;
        }

        kalmanGain = priorErrorCovarianceRSSI / (priorErrorCovarianceRSSI + measurementNoise);
        estimatedRSSI = priorRSSI + (kalmanGain * (rssi - priorRSSI));
        errorCovarianceRSSI = (1 - kalmanGain) * (priorErrorCovarianceRSSI);


        return estimatedRSSI;
    }

}
//============================================================================================================
