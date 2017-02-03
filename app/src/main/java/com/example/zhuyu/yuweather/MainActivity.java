package com.example.zhuyu.yuweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.zhuyu.yuweather.util.FragmentUtil;


public class MainActivity extends AppCompatActivity {


    private ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("中国");//设置toolbar标题
        setSupportActionBar(toolbar);
         actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(this);

        //先判断缓存中是否有数据，如果哟说明不是第一次启动，直接启动weatherActivity
        if (preferences.getString("weatherStr",null)!=null){
            Intent intent=new Intent(MainActivity.this,WeatherActivity.class);
            startActivity(intent);
            this.finish();
        }

        Fragment fragment=new AreaFragment();
        FragmentUtil.replaceFragment(fragment,this,R.id.areaFramelayout);
}

}
