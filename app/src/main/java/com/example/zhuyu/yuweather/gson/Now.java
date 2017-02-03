package com.example.zhuyu.yuweather.gson;

/**
 * Created by ZHUYU on 2017/2/1 0001.
 */

public class Now {

    public Cond cond;
    public class Cond {
        public String code;//100  天气状况代码
        public String txt;//晴
    }

    public String fl;    //体感温度
    public String hum;   //相对湿度
    public String pcpn;  //降水量
    public String pres;  //气压
    public String tmp;  //温度
    public String vis;   //能见度

    public Wind wind;
    public  class Wind {
        public String deg;    //330   风向（360度）
        public String dir;    // 西北风  风向
        public String sc;    // 6-7  风力等级
        public String spd;    //34  风速
    }

}
