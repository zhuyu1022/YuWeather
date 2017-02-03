package com.example.zhuyu.yuweather.gson;

/**
 * Created by ZHUYU on 2017/2/1 0001.
 */

public class Suggestion {

    public Comf comf;//舒适度指数
    public Cw cw;  //洗车指数
    public Drsg drsg; //穿衣指数
    public  Flu flu;//感冒指数
    public Sport sport;//运动指数
    public Trav trav;//旅游指数
    public Uv uv;//紫外线指数


    public class Comf {
        public String brf; //较舒适
        public String txt; //白天天气晴好，您在这种天气条件下，会感觉早晚凉爽、舒适，午后偏热。
    }

    public class Cw {
        public String brf;//较不宜
        public String txt;//较不宜洗车，未来一天无雨，风力较大，如果执意擦洗汽车，要做好蒙上污垢的心理准备。
    }

    public class Drsg {
        public String brf;//热
        public String txt;//天气热，建议着短裙、短裤、短薄外套、T恤等夏季服装。
    }

    public class Flu {
        public String brf;//较易发
        public String txt;//虽然温度适宜但风力较大，仍较易发生感冒，体质较弱的朋友请注意适当防护。
    }

    public class Sport {
        public String brf;//较适宜
        public String txt;//天气较好，但风力较大，推荐您进行室内运动，若在户外运动请注意防风。
    }

    public class Trav {
        public String brf;//适宜
        public String txt;//天气较好，风稍大，但温度适宜，是个好天气哦。适宜旅游，您可以尽情地享受大自然的无限风光。
    }

    public class Uv {
        public String brf;//强
        public String txt;//紫外线辐射强，建议涂擦SPF20左右、PA++的防晒护肤品。避免在10点至14点暴露于日光下。
    }
}
