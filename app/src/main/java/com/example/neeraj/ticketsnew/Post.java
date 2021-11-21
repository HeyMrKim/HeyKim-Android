package com.example.neeraj.ticketsnew;

import com.google.gson.annotations.SerializedName;

public class Post {

    @SerializedName("userid")
    private String userid;
    @SerializedName("password")
    private String password;

    @SerializedName("accessToken")
    private String accessToken;
    @SerializedName("resultCode")
    private String resultCode;



    public void setAccessToken(String accessToken){
        this.userid = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setResultCode(String resultCode){
        this.userid = resultCode;
    }
    public String getResultCode() {
        return resultCode;
    }






    public void setUserid(String userid){
        this.userid = userid;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getUserid() {
        return userid;
    }
    public String getPassword () {
        return password;
    }



}
