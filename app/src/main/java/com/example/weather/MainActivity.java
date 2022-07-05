package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.weather.bean.CityBean;
import com.example.weather.bean.WeatherBean;
import com.example.weather.databinding.ActivityMainBinding;
import com.example.weather.view.AddActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private API req = Const.getReq();
    private WeatherBean weatherBean;
    private List<CityBean> cityBeanList;
    private String city;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();


        setWeather();
        refresh();
        broadcast();

        binding.image.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AddActivity.class));
        }); //跳转查询


    }


    @SuppressLint("SetTextI18n")
    private void setWeather() {
        new Thread(() -> {
            try {
                weatherBean = req.weather().execute().body();
                this.runOnUiThread(() -> {
                    String Json = GetJsonDataUtil.getJson(getApplicationContext(), "city.json");
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<CityBean>>() {
                    }.getType();
                    cityBeanList = gson.fromJson(Json, listType);
                    for (int i = 0; i < cityBeanList.size(); i++) {
                        if (weatherBean.getCityid().equals(cityBeanList.get(i).getId())) {
                            binding.textView.setText(cityBeanList.get(i).getProvinceZh() + weatherBean.getCity());
                        }
                    }

                    binding.textView2.setText(weatherBean.getTem() + "℃");
                    binding.textView3.setText(weatherBean.getWea());
                    binding.textView4.setText(weatherBean.getAir_tips());
                    if (weatherBean.getWea().contains("晴")) {
                        binding.weatherImg.setBackgroundResource(R.drawable.clear);
                    } else if (weatherBean.getWea().contains("多云")) {
                        binding.weatherImg.setBackgroundResource(R.drawable.cloudy);
                    } else if (weatherBean.getWea().contains("雨")) {
                        binding.weatherImg.setBackgroundResource(R.drawable.rain);
                    } else if (weatherBean.getWea().contains("雾")) {
                        binding.weatherImg.setBackgroundResource(R.drawable.wu);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @SuppressLint("SetTextI18n")
    private void setWeatherCity(String city) {
        new Thread(() -> {
            try {
                weatherBean = req.weatherCity(city).execute().body();

                this.runOnUiThread(() -> {
                    String Json = GetJsonDataUtil.getJson(getApplicationContext(), "city.json");
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<CityBean>>() {
                    }.getType();
                    cityBeanList = gson.fromJson(Json, listType);
                    for (int i = 0; i < cityBeanList.size(); i++) {
                        if (weatherBean.getCityid().equals(cityBeanList.get(i).getId())) {
                            binding.textView.setText(cityBeanList.get(i).getProvinceZh() + weatherBean.getCity());
                        }
                    }
                    binding.textView2.setText(weatherBean.getTem() + "℃");
                    binding.textView3.setText(weatherBean.getWea());
                    binding.textView4.setText(weatherBean.getAir_tips());
                    if (weatherBean.getWea().contains("晴")) {
                        binding.weatherImg.setBackgroundResource(R.drawable.clear);
                    } else if (weatherBean.getWea().contains("多云")) {
                        binding.weatherImg.setBackgroundResource(R.drawable.cloudy);
                    } else if (weatherBean.getWea().contains("雨")) {
                        binding.weatherImg.setBackgroundResource(R.drawable.rain);
                    } else if (weatherBean.getWea().contains("雾")) {
                        binding.weatherImg.setBackgroundResource(R.drawable.wu);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void refresh() {
        binding.swip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setWeather();
                Toast.makeText(getApplicationContext(), "刷新成功", Toast.LENGTH_SHORT).show();
                binding.swip.setRefreshing(false);
            }
        });
    }

    private void refreshCity(String city) {
        binding.swip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setWeatherCity(city);
                Toast.makeText(getApplicationContext(), "刷新成功", Toast.LENGTH_SHORT).show();
                binding.swip.setRefreshing(false);
            }
        });
    }

    private void broadcast(){
        MessageReceiver mr = new MessageReceiver();          // 广播注册类
        IntentFilter filter = new IntentFilter();           // 过滤器
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);      // 设置优先级
        filter.addAction("com.example.weather.view.CITY");
        getApplicationContext().registerReceiver(mr, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.example.weather.view.CITY")) {
                setWeatherCity(intent.getStringExtra("city"));
                refreshCity(intent.getStringExtra("city"));

            }
        }
    }


}