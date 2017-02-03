package com.example.zhuyu.yuweather.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.zhuyu.yuweather.AreaFragment;
import com.example.zhuyu.yuweather.R;

import java.util.ArrayList;

/**
 * Created by ZHUYU on 2017/1/30 0030.
 */

public class FragmentUtil {
    public static void replaceFragment(Fragment fragment, final AppCompatActivity activity,int layoutId){

        final FragmentManager fragmentManager=activity.getSupportFragmentManager();//获取FragmentManager
        FragmentTransaction transaction=fragmentManager.beginTransaction();//开启事务
        transaction.replace(layoutId,fragment);//将fragment实例添加到指定容器中
       // transaction.addToBackStack(null);  //设置fragment返回栈
        transaction.commit();       //提交事务
    }
}
