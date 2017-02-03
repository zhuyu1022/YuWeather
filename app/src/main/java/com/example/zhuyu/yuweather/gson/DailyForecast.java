package com.example.zhuyu.yuweather.gson;

/**
 * Created by ZHUYU on 2017/2/1 0001.
 */

public class DailyForecast {
    public Astro astro;
    public Cond cond;
    public String date;     //日期 "2016-08-30"
    public String hum;      // 相对湿度 45
    public String pcpn;    // 降水量  0.0
    public String pop;     // 降水概率 8
    public String pres;   //  气压   1005
    public Tmp tmp;  //温度
    public String vis;  //能见度
    public Now.Wind wind;  //风力情况

    public class Astro {

        public String mr;    // 月升时间
        public String sr;    //日出时间
        public String ss;    //日落时间
        public String ms;   // 月落时间
    }

    public class Cond {
        public String code_d;//100",
        public String code_n;//100",
        public String txt_d;// "晴",
        public String txt_n;//晴"
    }

    public class Tmp {
        public String max;
        public String min;
    }

}
