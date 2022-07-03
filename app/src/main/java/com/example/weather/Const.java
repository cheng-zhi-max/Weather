package com.example.weather;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Const {
    private static String IP = "https://v0.yiketianqi.com";

    public static String getIP() {
        return IP;
    }

    public static void setIP(String IP) {
        Const.IP = IP;
    }

    public static API getReq() {
        return new Retrofit.Builder().baseUrl(IP + "/").addConverterFactory(GsonConverterFactory.create()).build().create(API.class);
    }
}
