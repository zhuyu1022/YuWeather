package com.example.zhuyu.yuweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ZHUYU on 2017/2/1 0001.
 */

public class Aqi {
    public AqiCity city;

    public class AqiCity {
        public String aqi; //: "60",
        public String co;//"0",
        public String no2; // "14",
        public String o3;//"95",
        public String pm10;// "67",
        public String pm25;//"15",
        public String qlty; //"良",  //共六个级别，分别：优，良，轻度污染，中度污染，重度污染，严重污染
        public String so2;//"10"
    }
}
