package com.example.small.Beacon;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import static java.lang.Math.sqrt;

/**
 * Created by 이예지 on 2018-04-10.
 */

public class NodeList {

    private static NodeList nodeList = new NodeList();
    private  HashMap<Integer, NodeInfo> nodeInfos = new HashMap<Integer, NodeInfo>();
    public final static int INF = 9876542;// 선이 없는 곳... 무지 큰수로 설정
    public final static int N = 10;// 정점의 갯수

    // [from_node][to_node] = distance
    int[][] LEN = new int[N][N];

    private NodeList(){

        nodeSetting();
        distanceSetting();
    }

    public static NodeList getNodeList(){
        return nodeList;
    }

    private void nodeSetting() {
        if(nodeInfos == null){
        }else{
            nodeInfos.put(0, new NodeInfo(11, 22));
            nodeInfos.put(1, new NodeInfo(11, 18));
            nodeInfos.put(2, new NodeInfo(11, 14));
            nodeInfos.put(3, new NodeInfo(11, 9));
            nodeInfos.put(4, new NodeInfo(11, 4));
            nodeInfos.put(5, new NodeInfo(9, 4));
            nodeInfos.put(6, new NodeInfo(3, 4));
            nodeInfos.put(7, new NodeInfo(3, 9));
            nodeInfos.put(8, new NodeInfo(3, 14));
            nodeInfos.put(9, new NodeInfo(7, 14));
        }

    }

    private void distanceSetting() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++)
                LEN[i][j] = INF;
        }

        LEN[0][1] = LEN[1][0] = 4;

        LEN[1][2] = LEN[2][1] = 4;

        LEN[2][3] = LEN[3][2] = 5;

        LEN[2][9] = LEN[9][2] = 3;

        LEN[3][4] = LEN[4][3] = 5;
        LEN[4][5] = LEN[5][4] = 4;
        LEN[5][6] = LEN[6][5] = 4;
        LEN[6][7] = LEN[7][6] = 5;
        LEN[7][8] = LEN[8][7] = 5;
        LEN[8][9] = LEN[9][8] = 4;

        /*for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                System.out.print(LEN[i][j] + " ");
            }
            System.out.println();
        }*/
    }

    public int searchNearestNode(double x, double y) {
        int index=0;
        double min=100;

        for(int i=0;i<N;i++) {
            NodeInfo nodeInfo = nodeInfos.get(i);
            double d = pointToPointDistance(x,y,nodeInfo.getLocationX(),nodeInfo.getLocationY());
            if(min>d) {
                min = d;
                index=i;
            }
        }
        return index;
    }

    public double pointToPointDistance(double x1,double y1,double x2,double y2) {
        return Math.round(sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)));
    }

    /*다익스트라 알고리즘*/
    int data[][]; // 전체 지도 데이타
    boolean visit[]; // 방문지 확인
    int dis[]; // 시작점 부터의 거리
    int prev[]; // 도착점 전의 정점 저장
    int s, e; // 시작점과 끝점 저장
    int stack[]; // 시작점부터 끝점까지의 순서 저장
    Vector<Integer> stackV;
    public void init() // 다익스트라(Dijkstra) 알고리즘/단일 점에 따라 최단거리
    {
        data = LEN;
        dis = new int[N];
        visit = new boolean[N];
        prev = new int[N];
        stack = new int[N];
        stackV = new Vector<Integer>();
    }

    public int theLeastDistance() {
        return dis[e - 1];
    }

    public Vector<Integer> startNavigator(int start, int end) {
        System.out.println("==========================================================");
        System.out.println("Dijkstra start");
        System.out.println("startPoint: " + start);
        System.out.println("endPoint: " + end);
        System.out.println("===========================================================");
        s = start;
        e = end;

        int k = 0;
        int min = 0;
        for (int i = 0; i < N; i++) { /* 초기화 */
            dis[i] = INF;
            prev[i] = 0;
            visit[i] = false;
        }
        dis[s - 1] = 0; /* 시작점의 거리는 0 */

        for (int i = 0; i < N; i++) {
            min = INF;
            for (int j = 0; j < N; j++) { /* 정점의 수만큼 반복 */
                if (visit[j] == false && dis[j] < min) { /* 확인하지 않고 거리가 짧은 정점을 찾음 */
                    k = j;
                    min = dis[j];
                }
            }

            visit[k] = true; /* 해당 정점 확인 체크 */

            if (min == INF)
                break; /* 연결된 곳이 없으면 종료 */

            /****
             * I -> J 보다 I -> K -> J의
             * 거리가 더 작으면
             * 갱신
             ****/
            for (int j = 0; j < N; j++) {
                if (dis[k] + data[k][j] < dis[j]) {
                    dis[j] = dis[k] + data[k][j]; /* 최단거리 저장 */
                    prev[j] = k; /* J로 가기 위해서는 K를 거쳐야 함 */
                }
            }
        }
        Vector<Integer> route = inverseFind(); // 콘솔에서 최단 경로 출력
        return route;
    }

    public Vector<Integer> inverseFind() {
        int tmp = 0;
        int top = -1;

        tmp = e - 1;

        int j=0;
        while (true) {

            stack[++top] = tmp + 1;
            Log.i("tmp", "tmp = " + tmp + " stack[" + j +"] : " + stack[j]);
            if (tmp == s - 1) {
                Log.i("tmp", "break");
                break; /* 시작점에 이르렀으면 종료 */
            }

            tmp = prev[tmp];
            j++;
        }

		/* 역추적 결과 출력 */

        stackV.removeAllElements();
        for (int i = top; i > -1; i--) {
            //System.out.printf("%d", stack[i]);
            stackV.add(stack[i]);
            //if (i != 0)
                //System.out.printf(" -> ");
        }
        //System.out.printf("\n");
        return stackV;
    }

    public Vector<Integer> getStack() {
        return stackV;
    }

    public HashMap<Integer, NodeInfo> getNodeInfos() {
        return nodeInfos;
    }

    public void setNodeInfos(HashMap<Integer, NodeInfo> nodeInfos) {
        this.nodeInfos = nodeInfos;
    }
}
