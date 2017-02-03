package com.example.zhuyu.yuweather.util;

import android.util.Log;

import com.example.zhuyu.yuweather.db.City;
import com.example.zhuyu.yuweather.db.County;
import com.example.zhuyu.yuweather.db.Province;
import com.example.zhuyu.yuweather.gson.Weather;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by ZHUYU on 2017/1/29 0029.
 */

public class ParseJsonUtil {
    public static  void parseJsontoProvince(String JsonData){
            try {
                JSONArray jsonArray=new JSONArray(JsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    int provinceCode=jsonObject.getInt("id");
                    String provinceName=jsonObject.getString("name");
                    Province p=new Province();
                    p.setProvinceCode(provinceCode);
                    p.setProvinceName(provinceName);
                    p.save();//保存到数据库
                    Log.d("Province", provinceName);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
    }
    public static  void parseJsontoCity(String JsonData,int provinceId){

        try {
            JSONArray jsonArray=new JSONArray(JsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                int cityCode=jsonObject.getInt("id");
                String cityName=jsonObject.getString("name");
                City city=new City();
                city.setCityCode(cityCode);
                city.setCityName(cityName);
                city.setProvinceId(provinceId);
                Log.d("city", cityName);
                city.save();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static  void parseJsontoCounty(String JsonData,int cityId){
        try {
            JSONArray jsonArray=new JSONArray(JsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                String  weatherId=jsonObject.getString("weather_id");
                String countyName=jsonObject.getString("name");
                Log.d("weatherid", weatherId);
                County county=new County();
                county.setWeatherId(weatherId);
                county.setCountyName(countyName);
                county.setCityId(cityId);
                county.save();
                Log.d("county", countyName);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static Weather parseJsontoWeather(String jsonData){
        try {
            JSONObject jsonObject=new JSONObject(jsonData);

            JSONArray jsonArray=jsonObject.getJSONArray("HeWeather5");
            String content=jsonArray.getJSONObject(0).toString();
            Gson gson=new Gson();
            Weather weather=new Weather();
            weather=gson.fromJson(content,Weather.class);
            return weather;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
