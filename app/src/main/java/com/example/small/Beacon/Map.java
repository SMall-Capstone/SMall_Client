package com.example.small.Beacon;

/**
 * Created by 이예지 on 2018-03-13.
 */

public class Map {
    private double maxWidth,maxHeight;
    public static Map map = new Map();

    private Map(){
        this.maxWidth = 14;
        this.maxHeight = 28;
    }


    public double getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(double maxWidth) {
        this.maxWidth = maxWidth;
    }

    public double getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(double maxHeight) {
        this.maxHeight = maxHeight;
    }

    public static Map getMapInstance(){
        return map;
    }

    public void setSize(double maxWidth,double maxHeight){
        setMaxWidth(maxWidth);
        setMaxHeight(maxHeight);
    }
}
