package com.example.small.Info;

import java.io.Serializable;

/**
 * Created by 이예지 on 2018-01-29.
 */

public class UserInfo implements Serializable{
    String userid;
    String password,name,gender;
    int birth;
    int stamp;
    String favorite;

    private static UserInfo userInfo = new UserInfo();

    private UserInfo(){

    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getBirth() {
        return birth;
    }

    public void setBirth(int birth) {
        this.birth = birth;
    }

    public static UserInfo getUserInfo(){
        return userInfo;
    }

    public int getStamp() {
        return stamp;
    }

    public void setStamp(int stamp) {
        this.stamp = stamp;
    }

    public void addStamp(){
        this.stamp++;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    public void logout(){
        userInfo = new UserInfo();
    }
}
