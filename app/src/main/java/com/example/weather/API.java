package com.example.weather;

import com.example.weather.bean.WeatherBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface API {
    @GET("易客云天气自己申请账号，有免费天气api次数")
    Call<WeatherBean> weather();

    @GET("易客云天气自己申请账号，有免费天气api次数")
    Call<WeatherBean> weatherCity(@Query("city") String city);
}
