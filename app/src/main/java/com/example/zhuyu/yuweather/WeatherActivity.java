package com.example.zhuyu.yuweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.zhuyu.yuweather.gson.DailyForecast;
import com.example.zhuyu.yuweather.gson.Weather;
import com.example.zhuyu.yuweather.util.FragmentUtil;
import com.example.zhuyu.yuweather.util.HttpUtil;
import com.example.zhuyu.yuweather.util.ParseJsonUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {
    private TextView
            cityNameText, //城市名
            upDateText, //更新时间
            degreeText, //当前温度
            weatherInfoText,    //当前天气概况
            aqiText,        //AQI指数
            pm25Text,       //PM2.5指数
            comfortText,    //舒适度
            carwashText,    //洗车指数
            sportText;      //运动建议
    private Button homeBtn;
    public  DrawerLayout drawerLayout;//设为公有是为了在fragment中访问
    private FrameLayout homeFragmentLayout;
    private LinearLayout forecastLayout;
    public  SwipeRefreshLayout swipeRefresh;//设为公有是为了在fragment中访问
    private ScrollView weatherLayout;
    private static final String TAG = "WeatherActivity";
    private ImageView backgroundImage;
    private Weather weather = null;
    public String weatherId=null;//设为公有是为了在fragment中访问
    private ActionBar actionBar;
    private AreaFragment homefragment=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //实现背景图片和状态栏一体
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        //初始化控件
        findViewById();



        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);//设置下拉刷新颜色
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryWeather(weatherId);
                loadBingImage();
            }
        });
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
                if (homefragment==null){
                    homefragment=new AreaFragment();
                    FragmentUtil.replaceFragment(homefragment,WeatherActivity.this,R.id.homefragmentLayout);
                }
            }
        });
        //获取缓存数据
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherStr = preferences.getString("weatherStr", null);
        String bingImageUrl = preferences.getString("bingImageUrl", null);
        weatherLayout.setVisibility(View.INVISIBLE);
        //如果在本地缓存中找到数据，不进行访问服务器，直接显示，需手动刷新
        if (weatherStr != null) {
            Weather weather = ParseJsonUtil.parseJsontoWeather(weatherStr);
            weatherId=weather.basic.weatherId;
            showWeather(weather);
        }
        else {  //若本地没有数据，则访问服务器查询
            Intent intent = getIntent();
            weatherId = intent.getStringExtra("weatherId");
            Log.d(TAG, "weatherId" + weatherId);
            queryWeather(weatherId);
        }

        if (bingImageUrl != null) {
            Glide.with(this)
                    .load(bingImageUrl)
                    .into(backgroundImage);
        } else {
            loadBingImage();
        }
    }
    private void findViewById(){
        cityNameText = (TextView) findViewById(R.id.citynameText);
        upDateText = (TextView) findViewById(R.id.updatetimeText);
        degreeText = (TextView) findViewById(R.id.degreeText);
        weatherInfoText = (TextView) findViewById(R.id.weatherInfoText);
        aqiText = (TextView) findViewById(R.id.aqiText);
        pm25Text = (TextView) findViewById(R.id.pm25Text);
        comfortText = (TextView) findViewById(R.id.comfortText);
        carwashText = (TextView) findViewById(R.id.carwashText);
        sportText = (TextView) findViewById(R.id.sportText);
        homeBtn= (Button) findViewById(R.id.homeBtn);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipRefresh);
        drawerLayout= (DrawerLayout) findViewById(R.id.drawerLayout);
        homeFragmentLayout= (FrameLayout) findViewById(R.id.homefragmentLayout);
        forecastLayout = (LinearLayout) findViewById(R.id.forecastLayout);
        weatherLayout = (ScrollView) findViewById(R.id.weatherLayout);
        backgroundImage = (ImageView) findViewById(R.id.backgroundImage);
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("中国");//设置toolbar标题
        setSupportActionBar(toolbar);
        actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
    }

    public void queryWeather(String weatherId) {
        String url = "https://free-api.heweather.com/v5//weather?key=af1ffd49fe4d491d87fe961dab37ce9b&city=" + weatherId;
        // String url="http://guolin.tech/api/weather?key=af1ffd49fe4d491d87fe961dab37ce9b&cityid="+weatherId;
        HttpUtil.sendOkhttpRequest(url, parseWeatherCallBack);//发送请求
    }

    private void loadBingImage() {
        String url = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkhttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingImageUrl = response.body().string();//返回的是微软必应每日一图的图片链接
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bingImageUrl", bingImageUrl);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this)
                                .load(bingImageUrl)
                                .into(backgroundImage);
                    }
                });

            }
        });
    }

    //访问服务器后的回调方法
    Callback parseWeatherCallBack = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            final String result = response.body().string();//获取响应结果
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("result", result);
                    weather = ParseJsonUtil.parseJsontoWeather(result);
                    if (weather != null & weather.status.equals("ok")) {
                        Log.d("status", weather.status);
                        Log.d("", weather.daily_forecast.get(1).wind.dir);
                        //保存数据到本地缓存
                        SharedPreferences.Editor editor =
                                PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                        editor.putString("weatherStr", result);
                        editor.apply();
                        showWeather(weather);
                    }
                }
            });
        }
    };

    private void showWeather(Weather weather) {
        String cityName = weather.basic.cityname;
        String upDate = "更新时间：" + weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.tmp + "°";
        String weatherInfo = weather.now.cond.txt;
        String comfort = "舒适度：" + weather.suggestion.comf.txt;
        String carwash = "洗车指数：" + weather.suggestion.cw.txt;
        String sport = "运动建议：" + weather.suggestion.sport.txt;
        cityNameText.setText(cityName);
        upDateText.setText(upDate);//更新时间
        degreeText.setText(degree); //当前温度
        weatherInfoText.setText(weatherInfo);   //当前天气概况
        comfortText.setText(comfort);    //舒适度
        carwashText.setText(carwash);    //洗车指数
        sportText.setText(sport);     //运动建议
        //部分地区没有aqi数据
        if (weather.aqi != null) {
            String aqi = weather.aqi.city.aqi;
            String pm25 = weather.aqi.city.pm25;
            aqiText.setText(aqi);        //AQI指数
            pm25Text.setText(pm25);       //PM2.5指数
        }
        forecastLayout.removeAllViews();
        for (DailyForecast forecast : weather.daily_forecast) {
            View view = LayoutInflater.from(WeatherActivity.this)
                    .inflate(R.layout.item_forecast, forecastLayout, false);
            TextView itemDataText = (TextView) view.findViewById(R.id.itemDataText);
            TextView itemInfoText = (TextView) view.findViewById(R.id.itemInfoText);
            TextView itemMinText = (TextView) view.findViewById(R.id.itemMinText);
            TextView itemMaxText = (TextView) view.findViewById(R.id.itemMaxText);
            itemDataText.setText(forecast.date);
            itemInfoText.setText(forecast.cond.txt_d);
            itemMinText.setText(forecast.tmp.min + "℃");
            itemMaxText.setText(forecast.tmp.max + "℃");
            forecastLayout.addView(view);
        }
        weatherLayout.setVisibility(View.VISIBLE);
        swipeRefresh.setRefreshing(false);
    }
}
