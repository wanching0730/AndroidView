package com.example.milka.m0802.SharedPreferences.Util;
import android.content.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Milka on 2017/8/1.
 *
 * 数据增删改查（简易版SharedPreferencesUtil）
 */

public class SharedPreferencesSimpleUtil {

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private Context context;
    
    public SharedPreferencesSimpleUtil(Context context) {
        this.context = context.getApplicationContext();
        sp = this.context.getSharedPreferences(SPConstant.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor= sp.edit();
    }

    /**
     * 数据对象添加
     * */
    public  void add(SPBean spBean){
        if (spBean == null) return;
        //editor.remove(SPConstant.SHARED_PREFERENCES_DATA_INDEX_NAME);
        editor.putString(spBean.getKey(), spBean.getValue());
        editor.commit();
    }

    /**
     * 数据对象删除
     * */
    public void delete(String key){
        editor.remove(key);
        editor.commit();
    }
    /**
     * 数据对象编辑
     * */
    public  void edit(SPBean oldBean, SPBean newspBean){
        editor.remove(oldBean.getKey());
        editor.putString(newspBean.getKey(), newspBean.getValue());
        editor.commit();
    }
    /**
     * 所有数据对象查询
     * */
    public List<SPBean> listData(){
        List<SPBean> listData = new ArrayList<>();
        Map<String, ?> allEntries = sp.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Log.d("values", entry.getKey() + " key: " + entry.getValue().toString());
            listData.add(new SPBean(entry.getKey(), entry.getValue().toString()));
        }
        return listData;
    }
}
