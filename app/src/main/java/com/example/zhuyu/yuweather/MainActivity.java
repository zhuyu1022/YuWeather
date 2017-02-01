package com.example.zhuyu.yuweather;

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
       // actionBar.setDisplayHomeAsUpEnabled(true);
    Fragment fragment=new AreaFragment();
    FragmentUtil.replaceFragment(fragment,this);
}

}
