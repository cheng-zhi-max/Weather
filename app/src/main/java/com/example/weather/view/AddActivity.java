package com.example.weather.view;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.weather.API;
import com.example.weather.Const;
import com.example.weather.GetJsonDataUtil;
import com.example.weather.MainActivity;
import com.example.weather.R;
import com.example.weather.bean.CityBean;
import com.example.weather.bean.WeatherBean;
import com.example.weather.databinding.ActivityAddBinding;
import com.example.weather.databinding.ActivityMainBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class AddActivity extends AppCompatActivity {
    private ActivityAddBinding binding;
    private API req = Const.getReq();
    private WeatherBean weatherBean;
    public String city = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.button.setOnClickListener(v -> {
            city = binding.textView5.getText().toString();
            Intent intent = new Intent();
            intent.setAction("com.example.weather.view.CITY");
            intent.putExtra("city",city);
            //发射广播
            sendBroadcast(intent);
            this.finish();
        });
    }


}