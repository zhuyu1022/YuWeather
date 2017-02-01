package com.example.zhuyu.yuweather;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhuyu.yuweather.db.City;
import com.example.zhuyu.yuweather.db.County;
import com.example.zhuyu.yuweather.db.Province;
import com.example.zhuyu.yuweather.util.HttpUtil;
import com.example.zhuyu.yuweather.util.ParseJsonUtil;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class AreaFragment extends Fragment {


    private static final String areaBaseUrl = "http://guolin.tech/api/china";
    private static final int   INIT_LEVEL=0;
    private static final int PRIVINCE_LEVEL = 1;
    private static final int CITY_LEVEL = 2;
    private static final int COUNTY_LEVEL = 3;
    private int areaLevel = PRIVINCE_LEVEL;

    private int provinceCode;
    private String provinceName = null;
    private int cityCode;
    private String cityName = null;
    public String weatherId;
    private RecyclerView recyclerView;
    private List<String> itemList = new ArrayList<>();
    private List<Province> provinceList = new ArrayList<>();
    private List<City> cityList = new ArrayList<>();
    private List<County> countyList = new ArrayList<>();
    private AreaAdapter adapter;
    private AppCompatActivity activity;
    private ActionBar actionBar;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_area, container, false);
        activity = (AppCompatActivity) getActivity();
        actionBar = activity.getSupportActionBar();
        setHasOptionsMenu(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        adapter=new AreaAdapter(itemList,activity);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));



        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        queryProvinces();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (areaLevel == COUNTY_LEVEL) {
                    queryCitys();
                    areaLevel=CITY_LEVEL;
                } else if (areaLevel == CITY_LEVEL) {
                    queryProvinces();
                    areaLevel=PRIVINCE_LEVEL;
                    actionBar.setDisplayHomeAsUpEnabled(false);
                    actionBar.setTitle("中国");
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    Callback MyCallBack = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, "加载失败，请检查网络连接！", Toast.LENGTH_SHORT).show();
                    closeProgressDialog();
                }
            });
        }
        @Override
        public void onResponse(Call call, final Response response) throws IOException {

            switch (areaLevel) {
                case PRIVINCE_LEVEL:
                    ParseJsonUtil.parseJsontoCity(response.body().string(),provinceCode);//解析响应结果斌保存到数据库
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            queryCitys();
                        }
                    });
                    break;
                case CITY_LEVEL:
                    ParseJsonUtil.parseJsontoCounty(response.body().string(), cityCode);//解析响应结果斌保存到数据库
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            queryCountys();
                        }
                    });
                    break;
                case COUNTY_LEVEL:
                    break;
                default:
            }
        }
    };

    /**
     * 根据返回的response数据解析并显示到列表上
     */
    private void queryProvinces() {
        provinceList.clear();
        provinceList = DataSupport.findAll(Province.class);
        Log.d("provinceList.size", provinceList.size() + "");
        //判断数据库中是否存在数据
        if (provinceList.size() > 0) {
            itemList.clear();
            for (int i = 0; i < provinceList.size(); i++) {
                itemList.add(provinceList.get(i).getProvinceName());
            }
            areaLevel=PRIVINCE_LEVEL;
            adapter.notifyDataSetChanged();
            closeProgressDialog();
        } else {
            //若不存在数据，则访问服务器获取
            HttpUtil.sendOkhttpRequest(areaBaseUrl, MyCallBack);
            showProgressDialog();
        }
    }

    private void queryCitys() {
        Log.d("queryCitys start", "arealevel:"+areaLevel+"");
        cityList.clear();
        cityList = DataSupport.where("provinceId=?", String.valueOf(provinceCode)).find(City.class);
        //判断数据库中是否存在数据
        if (cityList.size() > 0) {
            itemList.clear();
            for (int i = 0; i < cityList.size(); i++) {
                itemList.add(cityList.get(i).getCityName());
            }
            areaLevel=CITY_LEVEL;
            Log.d("queryCitys change", "arealevel:"+areaLevel+"");
            adapter.notifyDataSetChanged();
            actionBar.setDisplayHomeAsUpEnabled(true);//显示toolbar上的返回按钮
            actionBar.setTitle(provinceName);
            closeProgressDialog();
        } else {
            //若不存在数据，则访问服务器获取
            HttpUtil.sendOkhttpRequest(areaBaseUrl + "/" + provinceCode, MyCallBack);
            showProgressDialog();
        }
    }
    private void queryCountys() {
        countyList.clear();
        countyList = DataSupport.where("cityId=?", String.valueOf(cityCode)).find(County.class);
        //判断数据库中是否存在数据
        if (countyList.size() > 0) {
            itemList.clear();
            for (int i = 0; i < countyList.size(); i++) {
                itemList.add(countyList.get(i).getCountyName());
            }
            areaLevel=COUNTY_LEVEL;
            adapter.notifyDataSetChanged();
            actionBar.setTitle(cityName);
            closeProgressDialog();
        } else {
            showProgressDialog();
            //若不存在数据，则访问服务器获取
            HttpUtil.sendOkhttpRequest(areaBaseUrl + "/" + provinceCode + "/" + cityCode, MyCallBack);
        }
    }

    class AreaAdapter extends RecyclerView.Adapter<AreaAdapter.ViewHolder> {
        List<String> mItemList;
        private AppCompatActivity activity;

        public AreaAdapter(List<String> mItemList, AppCompatActivity activity) {
            this.mItemList = itemList;
            this.activity = activity;
        }
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private TextView areaText;

            public ViewHolder(View itemView) {
                super(itemView);
                areaText = (TextView) itemView.findViewById(R.id.areaText);
                areaText.setOnClickListener(this);

            }
            @Override
            public void onClick(View v) {//子项点击事件
                Log.d("onitemClick", "arealevel:"+areaLevel+"");
                int position = getAdapterPosition();//获取子项所在位置
                if (areaLevel == PRIVINCE_LEVEL) {
                    provinceCode = provinceList.get(position).getProvinceCode();
                    provinceName = provinceList.get(position).getProvinceName();
                    queryCitys();
                } else if (areaLevel == CITY_LEVEL) {
                    cityCode = cityList.get(position).getCityCode();
                    cityName = cityList.get(position).getCityName();
                    queryCountys();
                }
            }
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_area, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            String str = mItemList.get(position);
            holder.areaText.setText(str);
        }

        @Override
        public int getItemCount() {
            return mItemList.size();
        }

    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("加载中");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    private void closeProgressDialog() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

}
