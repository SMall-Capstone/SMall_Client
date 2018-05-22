package com.example.small.Beacon;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import static java.lang.Math.sqrt;

/**
 * Created by 이예지 on 2018-03-07.
 */

public class BeaconList {
    HashMap<String,BeaconInfo> beaconInfoHashMap = new HashMap<String,BeaconInfo>();
    private static BeaconList beaconList = new BeaconList();//싱글톤 패턴 적용
    ArrayList<String> beaconId = new ArrayList<String>();
    ArrayList<BeaconInfo> beaconInfos = new ArrayList<BeaconInfo>();
    ArrayList<BeaconInfo> beaconInfosSortByPoint = new ArrayList<BeaconInfo>();
    private int txPower=-62;
    private double previousX=-1,previousY=-1;
    private double resultX,resultY;

    private BeaconList() {
        beaconInfoHashMap.put("MiniBeacon_00165",new BeaconInfo("MiniBeacon_00165","165"));
        beaconInfoHashMap.get("MiniBeacon_00165").setLocation(3,3);

        beaconInfoHashMap.put("MiniBeacon_00175",new BeaconInfo("MiniBeacon_00175","175"));
        beaconInfoHashMap.get("MiniBeacon_00175").setLocation(8,1);

        beaconInfoHashMap.put("MiniBeacon_00177",new BeaconInfo("MiniBeacon_00177","177"));
        beaconInfoHashMap.get("MiniBeacon_00177").setLocation(0,8);

        beaconInfoHashMap.put("MiniBeacon_00783",new BeaconInfo("MiniBeacon_00783","783"));
        beaconInfoHashMap.get("MiniBeacon_00783").setLocation(4,9);

        beaconInfoHashMap.put("MiniBeacon_01031",new BeaconInfo("MiniBeacon_01031","1031"));
        beaconInfoHashMap.get("MiniBeacon_01031").setLocation(10,8);

        beaconInfoHashMap.put("MiniBeacon_01352",new BeaconInfo("MiniBeacon_01352","1352"));
        beaconInfoHashMap.get("MiniBeacon_01352").setLocation(7,15);
        //beaconInfoHashMap.get("MiniBeacon_01352").setPopUpBeacon(true);

        beaconInfoHashMap.put("MiniBeacon12802",new BeaconInfo("MiniBeacon12802","12802"));
        beaconInfoHashMap.get("MiniBeacon12802").setLocation(12,13);

        beaconInfoHashMap.put("MiniBeacon12928",new BeaconInfo("MiniBeacon12928","12928"));
        beaconInfoHashMap.get("MiniBeacon12928").setLocation(9,19);

        beaconInfoHashMap.put("MiniBeacon13298",new BeaconInfo("MiniBeacon13298","13298"));
        beaconInfoHashMap.get("MiniBeacon13298").setLocation(11,23);
        beaconInfoHashMap.get("MiniBeacon13298").setStampBeacon(true);

        beaconInfoHashMap.put("MiniBeacon_14863",new BeaconInfo("MiniBeacon_14863","14863"));
        beaconInfoHashMap.get("MiniBeacon_14863").setLocation(13,3);
        beaconInfoHashMap.get("MiniBeacon_14863").setStampBeacon(true);

        beaconInfoHashMap.put("MiniBeacon_14990",new BeaconInfo("MiniBeacon_14990","14990"));
        beaconInfoHashMap.get("MiniBeacon_14990").setLocation(1,14);
        beaconInfoHashMap.get("MiniBeacon_14990").setStampBeacon(true);
        beaconInfoHashMap.get("MiniBeacon_14990").setPopUpBeacon(true);

        beaconInfoHashMap.put("MiniBeacon_14997",new BeaconInfo("MiniBeacon_14997","14997"));
        beaconInfoHashMap.get("MiniBeacon_14997").setLocation(14,18);

        beaconId.add("MiniBeacon_00165");
        beaconId.add("MiniBeacon_00175");
        beaconId.add("MiniBeacon_00177");
        beaconId.add("MiniBeacon_00783");
        beaconId.add("MiniBeacon_01031");
        beaconId.add("MiniBeacon_01352");
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

    //비콘 rssi, point기준으로 정렬
    public ArrayList<BeaconInfo> findNearestBeaconsByRssi(){
        beaconInfos.clear();
        for(int i=0;i<beaconInfoHashMap.size();i++){
            beaconInfos.add(beaconInfoHashMap.get(beaconId.get(i)));
        }

        if(beaconInfos.size()==beaconInfoHashMap.size()){
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

        }
        else {
            Log.i("Sort","beaconInfos setting fail");
        }

        return beaconInfos;
    }

    public void addPointByRssiSorting(ArrayList<BeaconInfo> removeOutlierBeaconInfos){

        Collections.sort(removeOutlierBeaconInfos, new Comparator<BeaconInfo>() {
            @Override
            public int compare(BeaconInfo beaconInfo1, BeaconInfo beaconInfo2) {
                if (beaconInfo1.getFilteredRSSIvalue() < beaconInfo2.getFilteredRSSIvalue()) {
                    return 1;
                } else if (beaconInfo1.getFilteredRSSIvalue() > beaconInfo2.getFilteredRSSIvalue()) {
                    return -1;
                } else
                    return 0;
            }
        });

        /*for(int i=0;i<removeOutlierBeaconInfos.size();i++){
            Log.i("addPoint",removeOutlierBeaconInfos.get(i).getName());
        }
        Log.i("addPoint","==========================================");*/

        for (int i = 0; i < 3; i++) {
            Log.i("addPoint",removeOutlierBeaconInfos.get(i).getName()+" -> "+i+"번째//");
            for (int k = 0; k < beaconInfos.size(); k++) {
                //포인트 부여
                if((beaconInfos.get(k).getName()).equals(removeOutlierBeaconInfos.get(i).getName())){
                    if(i==0) {
                        beaconInfos.get(k).addNearestPoint(3);
                        Log.i("addPoint",beaconInfos.get(k).getName()+" -> 5점");
                    }
                    else if(i==1) {
                        beaconInfos.get(k).addNearestPoint(2);
                        Log.i("addPoint",beaconInfos.get(k).getName()+" -> 3정");
                    }
                    else if(i==2) {
                        beaconInfos.get(k).addNearestPoint(1);
                        Log.i("addPoint",beaconInfos.get(k).getName()+" -> 1점");
                    }

                }
            }
        }
    }

    private void removeOutlier(ArrayList<BeaconInfo> beaconInfos, String s) {
        for(int i=1;i<beaconInfos.size();i++){ //i=0은 안해도됨
            if(beaconInfos.get(i).getMinor().contains(s)) {
                beaconInfos.remove(i);
                i--;
            }
        }
    }
    public void removeOutlierAll(ArrayList<BeaconInfo> beaconInfosSortByPoint){
        //이상치 비콘 삭제
        /*if(beaconInfosSortByPoint.get(0).getMinor().contains("13298")||beaconInfosSortByPoint.get(0).getMinor().contains("14997")||beaconInfosSortByPoint.get(0).getMinor().contains("12928")){
            removeOutlier(beaconInfosSortByPoint,"165");
            removeOutlier(beaconInfosSortByPoint,"175");
            removeOutlier(beaconInfosSortByPoint,"14863");
            if(beaconInfosSortByPoint.get(0).getMinor().contains("13298")){
                removeOutlier(beaconInfosSortByPoint,"1031");
                removeOutlier(beaconInfosSortByPoint,"177");

            }
        }
        if(beaconInfosSortByPoint.get(0).getMinor().contains("165")||beaconInfosSortByPoint.get(0).getMinor().contains("175")||beaconInfosSortByPoint.get(0).getMinor().contains("14863")){
            removeOutlier(beaconInfosSortByPoint,"13298");
            removeOutlier(beaconInfosSortByPoint,"14997");
            removeOutlier(beaconInfosSortByPoint,"12928");
            removeOutlier(beaconInfosSortByPoint,"14990");
            if (beaconInfosSortByPoint.get(0).getMinor().contains("14863")) {
                removeOutlier(beaconInfosSortByPoint,"783");
            }
        }
        if(beaconInfosSortByPoint.get(0).getMinor().contains("14990") || beaconInfosSortByPoint.get(0).getMinor().contains("177")||beaconInfosSortByPoint.get(0).getMinor().contains("1352")) {
            removeOutlier(beaconInfosSortByPoint, "14863");
            if (beaconInfosSortByPoint.get(0).getMinor().contains("14990")) {
                removeOutlier(beaconInfosSortByPoint, "12802");
                removeOutlier(beaconInfosSortByPoint, "175");
            }
        }
        if(beaconInfosSortByPoint.get(0).getMinor().contains("1031")) {
            removeOutlier(beaconInfosSortByPoint, "783");
        }

        //haneul 예지확인받기ㅠㅠㅠㅠㅠㅠㅠ
        if(beaconInfosSortByPoint.get(0).getMinor().contains("1352") || beaconInfosSortByPoint.get(0).getMinor().contains("12802")||beaconInfosSortByPoint.get(0).getMinor().contains("12928")) {
            removeOutlier(beaconInfosSortByPoint, "175");
            removeOutlier(beaconInfosSortByPoint, "165");
            removeOutlier(beaconInfosSortByPoint, "14863");

            if (beaconInfosSortByPoint.get(0).getMinor().contains("12928")) {
                removeOutlier(beaconInfosSortByPoint, "1031");
                removeOutlier(beaconInfosSortByPoint, "783");
            }
            if(beaconInfosSortByPoint.get(0).getMinor().contains("12802")) {
                removeOutlier(beaconInfosSortByPoint, "14990");
            }
        }

        if(beaconInfosSortByPoint.get(0).getMinor().contains("165") || beaconInfosSortByPoint.get(0).getMinor().contains("175")||beaconInfosSortByPoint.get(0).getMinor().contains("783")) {
            removeOutlier(beaconInfosSortByPoint, "14997");
            removeOutlier(beaconInfosSortByPoint, "1352");
            removeOutlier(beaconInfosSortByPoint, "12928");
            removeOutlier(beaconInfosSortByPoint, "12802");

            if (beaconInfosSortByPoint.get(0).getMinor().contains("175")) {
                removeOutlier(beaconInfosSortByPoint, "14990");

            }
            if (beaconInfosSortByPoint.get(0).getMinor().contains("165")) {
                removeOutlier(beaconInfosSortByPoint, "1352");

            }
        }*/
    }

    public ArrayList<BeaconInfo> findNearestBeaconsByPoint(){
        beaconInfosSortByPoint.clear();
        beaconInfosSortByPoint.addAll(beaconInfos);

        if(beaconInfosSortByPoint.size()==beaconInfoHashMap.size()){
            //Collections.sort(beaconInfos);

            Collections.sort(beaconInfosSortByPoint, new Comparator<BeaconInfo>() {
                @Override
                public int compare(BeaconInfo beaconInfo1, BeaconInfo beaconInfo2) {
                    if(beaconInfo1.getNearestPoint() < beaconInfo2.getNearestPoint()){
                        return 1;
                    }
                    else if(beaconInfo1.getNearestPoint() > beaconInfo2.getNearestPoint()){
                        return -1;
                    }
                    else
                        return 0;
                }
            });

        }
        else {
            Log.i("Sort","beaconInfos setting fail");
        }

        removeOutlierAll(beaconInfosSortByPoint);
        return beaconInfosSortByPoint;
    }

    public void initNearestPoint() {
        for (int i = 0; i < beaconInfosSortByPoint.size(); i++) {
            beaconInfosSortByPoint.get(i).setNearestPoint(0);
        }
    }

    public void calculateDistance(){

        BeaconInfo b1, b2,  b3;
        ArrayList<BeaconInfo> beaconInfosSortByPoint = findNearestBeaconsByPoint();

        b1 = beaconInfosSortByPoint.get(0);
        b2 = beaconInfosSortByPoint.get(1);
        b3 = beaconInfosSortByPoint.get(2);

        Log.i("NearestPoint", "============================================================================");
        Log.i("NearestPoint", "calculateDistance");
        Log.i("NearestPoint", b1.getName() + " => " + b1.getNearestPoint() + " / " + b1.getDistance());
        Log.i("NearestPoint", b2.getName() + " => " + b2.getNearestPoint() + " / "+ b2.getDistance());
        Log.i("NearestPoint", b3.getName() + " => " + b3.getNearestPoint() + " / "+ b3.getDistance());

        /*for (int i=0;i<beaconInfosSortByPoint.size();i++){
            Log.i("NearestPoint", beaconInfosSortByPoint.get(i).getName() + " => " + beaconInfosSortByPoint.get(i).getNearestPoint() + " / " + beaconInfosSortByPoint.get(i).getDistance());
        }*/

        double[] resultXY1,resultXY2,resultXY3;

        resultXY1 = middleOfNodePoint(b1,b2);
        resultXY2 = middleOfNodePoint(b2,b3);
        resultXY3 = middleOfNodePoint(b3,b1);

        if((resultXY1[0]==-100) || (resultXY2[0]==-100) || (resultXY3[0]==-100)){
            //원의 교점을 구하지 못해서 NaN이 나온경우
            Log.i("NearestPoint","좌표 NaN");
            resultX = previousX;
            resultY = previousY;
        }
        else{
            resultX = (resultXY1[0]+resultXY2[0]+resultXY3[0])/3;
            resultY = (resultXY1[1]+resultXY2[1]+resultXY3[1])/3;
        }

        Map m = Map.getMapInstance();
        if(resultX < 0 || resultX > m.getMaxWidth()) {
            if(resultX<0)
                resultX = 0;
            if(resultX>m.getMaxWidth())
                resultX = m.getMaxWidth()-1;
        }
        if(resultY < 0 || resultX > m.getMaxHeight()) {
            if(resultY<0)
                resultY = 0;
            if(resultY>m.getMaxHeight())
                resultY = m.getMaxHeight()-1;
        }

        //좌표가 NaN으로 나올 경우 이전값으로 대체하여 사용
        if(Double.isNaN(resultX) || Double.isNaN(resultY)){
            resultX = previousX;
            resultY = previousY;
        }

        if(previousX==-1 && previousY==-1) {
            //이전에 저장된 값이 없는 경우=>처음 측정된 값
            previousX=resultX;
            previousY=resultY;
            Log.i("yunjae", "x = " + resultX + " y = " + resultY);
        }
        else {
            //이전의 값과 차이가 설정 값 이상 나지 않는 경우에만 좌표출력
            if ( ! (previousX-resultX<-10 || previousX-resultX>10) ){
                previousX=resultX;
                previousY=resultY;
                Log.i("yunjae", "x = " + resultX + " y = " + resultY);
            }
        }

        Log.i("NearestPoint", "rsult = "+resultX+", "+resultY);


    }

    public double[] middleOfNodePoint(BeaconInfo b1, BeaconInfo b2){
        /*
         * 원1 // 중심 : (a, b) 반지름 : r
        * 원2 // 중심 : (c, d) 반지름 : s
        */
        double a = b1.getLocation_x(), b = b1.getLocation_y(), r = b1.getDistance();
        double c = b2.getLocation_x(), d = b2.getLocation_y(), s = b2.getDistance();

/*

        //거리값 조절
        if(r>12){
            r *= 0.8;
        }
        else if(4.5<r && r<5.5){
            r *= 1.3;
        }
        else if(4.5>r){
            r *= 1.5;
        }

        if(s>12){
            s *= 0.8;
        }
        else if(4.5<s && s<5.5){
            s *= 1.3;
        }
        else if(4.5>s){
            s *= 1.5;
        }
*/

        if(12 > s){
            r *= 0.8;
            s *= 0.8;
        }

        double e = c - a;
        double f = d - b;
        double p = sqrt(e * e + f * f);
        double k = (p * p + r * r - s * s) / (2 * p);
        double x1 = a + (e * k) / p + (f / p) * sqrt(r * r - k * k);
        double y1 = b + (f * k) / p - (e / p) * sqrt(r * r - k * k);
        double x2 = a + (e * k) / p - (f / p) * sqrt(r * r - k * k);
        double y2 = b + (f * k) / p + (e / p) * sqrt(r * r - k * k);

        double[] resultXY = new double[2];
        if(Double.isNaN(x1) || Double.isNaN(y1) || Double.isNaN(x2) || Double.isNaN(y2)){
            Log.i("NearestPoint","좌표 NaN"+b1.getName()+" / "+b2.getName()+" / "+r+","+s);
            resultXY[0] = -100;
            resultXY[1] = -100;
            return resultXY;
        }
        else{
            resultXY[0] = (x1+x2)/2.0;
            resultXY[1] = (y1+y2)/2.0;
            return resultXY;
        }

    }

    public void initStampBeacon(){
        beaconInfoHashMap.get("MiniBeacon_14863").setStampCount(0);
        beaconInfoHashMap.get("MiniBeacon_14990").setStampCount(0);
        beaconInfoHashMap.get("MiniBeacon_14997").setStampCount(0);
    }

    public void initPopUpBeacon(){
        beaconInfoHashMap.get("MiniBeacon_14990").setPopUpCount(0);
    }
    public int getTxPower() {
        return txPower;
    }

    public void setTxPower(int txPower) {
        this.txPower = txPower;
    }

    public double getPreviousX() {
        return previousX;
    }

    public void setPreviousX(double previousX) {
        this.previousX = previousX;
    }

    public double getPreviousY() {
        return previousY;
    }

    public void setPreviousY(double previousY) {
        this.previousY = previousY;
    }

    public double getResultX() {
        return resultX;
    }

    public void setResultX(double resultX) {
        this.resultX = resultX;
    }

    public double getResultY() {
        return resultY;
    }

    public void setResultY(double resultY) {
        this.resultY = resultY;
    }
}
