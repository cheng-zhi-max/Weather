package com.example.weather;

import com.example.weather.bean.WeatherBean;

import retrofit2.Call;
import retrofit2.http.GET;

public interface API {
    @GET("api?appid=91395745&appsecret=40OfE1a7&version=v61")
    Call<WeatherBean> weather();
}
