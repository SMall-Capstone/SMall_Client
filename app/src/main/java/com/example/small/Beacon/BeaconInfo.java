package com.example.small.Beacon;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by 이예지 on 2018-03-07.
 */

public class BeaconInfo implements Comparable<BeaconInfo>{
    private String name,minor;
    private int filteredRSSIvalue;
    private boolean isFirst;
    private double location_x,location_y;
    private double distance;
    private int nearestPoint;
    private ArrayList<Integer> filteredRssiQueue = new ArrayList<Integer>();
    private ArrayList<Integer> noFilterRssiQueue = new ArrayList<Integer>();
    //비콘을 이용한 이벤트를 위한 변수
    private boolean isStampBeacon;
    private boolean isPopUpBeacon;
    private int stampCount,popUpCount;

    public BeaconInfo(){

    }

    public BeaconInfo(String name, String minor) {
        this.name = name;
        this.minor = minor;
        this.isFirst = true;
        this.filteredRSSIvalue = -1000;
        this.distance = -1;
        this.nearestPoint=0;
        this.isStampBeacon = false;
        this.isPopUpBeacon = false;
        this.stampCount=0;
        this.popUpCount=0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMinor() {
        return minor;
    }

    public void setMinor(String minor) {
        this.minor = minor;
    }

    public int getFilteredRSSIvalue() {
        return filteredRSSIvalue;
    }

    public void setFilteredRSSIvalue(int filteredRSSIvalue) {
        this.filteredRSSIvalue = filteredRSSIvalue;
    }

    public boolean getIsFirst() {
        return isFirst;
    }

    public void setIsFirst(boolean isFirst) {
        this.isFirst = isFirst;
    }

    public double getLocation_x() {
        return location_x;
    }

    public void setLocation_x(double location_x) {
        this.location_x = location_x;
    }

    public double getLocation_y() {
        return location_y;
    }

    public void setLocation_y(double location_y) {
        this.location_y = location_y;
    }

    public void setLocation(double x,double y){
        this.setLocation_x(x);
        this.setLocation_y(y);
        Log.i("Location",this.getMinor()+" location setting");
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public boolean isStampBeacon() {
        return isStampBeacon;
    }

    public void setStampBeacon(boolean stampBeacon) {
        isStampBeacon = stampBeacon;
    }

    public int getStampCount() {
        return stampCount;
    }

    public void setStampCount(int stampCount) {
        this.stampCount = stampCount;
    }

    public int getPopUpCount() {
        return popUpCount;
    }

    public void setPopUpCount(int popUpCount) {
        this.popUpCount = popUpCount;
    }

    @Override
    public int compareTo(BeaconInfo b) {
        if(this.filteredRSSIvalue > b.getFilteredRSSIvalue())
            return 1;
        else if(this.filteredRSSIvalue < b.getFilteredRSSIvalue())
            return -1;
        return 0;
    }

    public void addFilteredRssiQueue(int rssi){
        this.filteredRssiQueue.add(rssi);
    }

    public void removeInFilteredRssiQueue(){
        this.filteredRssiQueue.remove(0);
    }

    public ArrayList<Integer> getFilteredRssiQueue() {
        return filteredRssiQueue;
    }

    public int getAvgRssi(ArrayList<Integer> rssiQueue){
        int sum = 0;
        for(int i=0;i<rssiQueue.size();i++){
            sum+=rssiQueue.get(i);
        }
        return (int)(sum/rssiQueue.size());
    }

    public void addNoFilterRssiQueue(int rssi){
        this.noFilterRssiQueue.add(rssi);
    }

    public void removeInNoFilterRssiQueue(){
        this.noFilterRssiQueue.remove(0);
    }

    public ArrayList<Integer> getNoFilterRssiQueue() {
        return noFilterRssiQueue;
    }

    public void addNearestPoint(int point){
        nearestPoint += point;
    }

    public int getNearestPoint() {
        return nearestPoint;
    }

    public void setNearestPoint(int nearestPoint) {
        this.nearestPoint = nearestPoint;
    }

    public boolean isPopUpBeacon() {
        return isPopUpBeacon;
    }

    public void setPopUpBeacon(boolean popUpBeacon) {
        isPopUpBeacon = popUpBeacon;
    }
}
