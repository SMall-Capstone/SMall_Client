package com.example.small.Info;

import android.support.annotation.NonNull;

/**
 * Created by 이예지 on 2018-02-12.
 */

public class BeaconInfo implements Comparable<BeaconInfo>{

    private String major,minor,location;
    private Double distance;

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getMinor() {
        return minor;
    }

    public void setMinor(String minor) {
        this.minor = minor;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    @Override
    public int compareTo(@NonNull BeaconInfo beaconInfo) {
        if(distance>beaconInfo.getDistance()){
            return 1;
        }
        else if(distance<beaconInfo.getDistance()){
            return -1;
        }
        else
            return 0;
    }
}
