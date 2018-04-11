package com.example.small.Beacon;

/**
 * Created by 이예지 on 2018-04-10.
 */

public class NodeInfo {
    private int locationX, locationY;

    public NodeInfo(int x, int y) {
        locationX = x;
        locationY = y;
    }

    public int getLocationX() {
        return locationX;
    }

    public int getLocationY() {
        return locationY;
    }
}

