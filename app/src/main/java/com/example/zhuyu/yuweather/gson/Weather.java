package com.example.zhuyu.yuweather.gson;

import java.util.List;

/**
 * Created by ZHUYU on 2017/2/1 0001.
 */

public class Weather {
    public String status;
    public Aqi aqi;
    public Basic basic;
    public List<DailyForecast> daily_forecast;
    public List<HourlyForecast> hourly_forecast;
    public Now now;
    public Suggestion suggestion;
}
