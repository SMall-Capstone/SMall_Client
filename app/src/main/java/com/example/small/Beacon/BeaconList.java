package com.example.small.Beacon;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by 이예지 on 2018-03-07.
 */

public class BeaconList {
    HashMap<String,BeaconInfo> beaconInfoHashMap = new HashMap<String,BeaconInfo>();
    private static BeaconList beaconList = new BeaconList();//싱글톤 패턴 적용
    ArrayList<String> beaconId = new ArrayList<String>();
    private int txPower = -66;

    private BeaconList() {
        beaconInfoHashMap.put("MiniBeacon12802",new BeaconInfo("MiniBeacon12802","40001","12802"));
        beaconInfoHashMap.get("MiniBeacon12802").setLocation(7,2);
        beaconInfoHashMap.get("MiniBeacon12802").setStampBeacon(true);

        beaconInfoHashMap.put("MiniBeacon12928",new BeaconInfo("MiniBeacon12928","40001","12928"));
        beaconInfoHashMap.get("MiniBeacon12928").setLocation(4,7);
        beaconInfoHashMap.get("MiniBeacon12928").setStampBeacon(true);

        beaconInfoHashMap.put("MiniBeacon13298",new BeaconInfo("MiniBeacon13298","40001","13298"));
        beaconInfoHashMap.get("MiniBeacon13298").setLocation(9,7);
        beaconInfoHashMap.get("MiniBeacon13298").setStampBeacon(true);

        beaconInfoHashMap.put("MiniBeacon_14863",new BeaconInfo("MiniBeacon_14863","40001","14863"));
        beaconInfoHashMap.get("MiniBeacon_14863").setLocation(6,12);

        beaconInfoHashMap.put("MiniBeacon_14990",new BeaconInfo("MiniBeacon_14990","40001","14990"));
        beaconInfoHashMap.get("MiniBeacon_14990").setLocation(11,12);

        beaconInfoHashMap.put("MiniBeacon_14997",new BeaconInfo("MiniBeacon_14997","40001","14997"));
        beaconInfoHashMap.get("MiniBeacon_14997").setLocation(8,17);


        beaconId.add("MiniBeacon12802");
        beaconId.add("MiniBeacon12928");
        beaconId.add("MiniBeacon13298");
        beaconId.add("MiniBeacon_14863");
        beaconId.add("MiniBeacon_14990");
        beaconId.add("MiniBeacon_14997");

    }

    public BeaconInfo findBeacon(String name){
        return beaconInfoHashMap.get(name);
    }

    public static BeaconList getBeaconListInstance(){
        return beaconList;
    }

    public ArrayList<BeaconInfo> findNearestBeacons(){
        ArrayList<BeaconInfo> beaconInfos = new ArrayList<BeaconInfo>();

        for(int i=0;i<beaconInfoHashMap.size();i++){
            beaconInfos.add(beaconInfoHashMap.get(beaconId.get(i)));
        }

        if(beaconInfos.size()==6/*==beaconInfoArrayList.size()*/){

            //Collections.sort(beaconInfos);
            Collections.sort(beaconInfos, new Comparator<BeaconInfo>() {
                @Override
                public int compare(BeaconInfo beaconInfo1, BeaconInfo beaconInfo2) {
                    if(beaconInfo1.getFilteredRSSIvalue() < beaconInfo2.getFilteredRSSIvalue()){
                        return 1;
                    }
                    else if(beaconInfo1.getFilteredRSSIvalue() > beaconInfo2.getFilteredRSSIvalue()){
                        return -1;
                    }
                    else
                        return 0;
                }
            });

            return beaconInfos;
        }
        else {
            Log.i("Sort","beaconInfos setting fail");
            return beaconInfos;
        }

    }

    public void initEventBeacon(){
        beaconInfoHashMap.get("MiniBeacon12802").setCount(0);
        beaconInfoHashMap.get("MiniBeacon12928").setCount(0);
        beaconInfoHashMap.get("MiniBeacon13298").setCount(0);
    }

    public int getTxPower() {
        return txPower;
    }

    public void setTxPower(int txPower) {
        this.txPower = txPower;
    }
}
