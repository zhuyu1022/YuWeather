package com.example.zhuyu.yuweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ZHUYU on 2017/2/1 0001.
 */

public class Basic {
    @SerializedName("city")
    public String cityname;

    public String cnty;

    @SerializedName("id")
    public String weatherId;

    public String lat;//维度

    public String lon;//经度

    public String prov;//所属省份

    public Update update;


    public class Update {

        @SerializedName("loc")
        public String updateTime;//更新时间

        public String utc;
    }
}
