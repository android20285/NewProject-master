package com.cn.tools;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * Created by Administrator on 2016/10/11.
 */

public class MySharedPreference {

    public static final String CONFIG_FILE = "foodingredients_file";

    private SharedPreferences mySharedPreference = null;

    public MySharedPreference(Context context) {
        mySharedPreference = context.getSharedPreferences(CONFIG_FILE, 0);
    }


    /**
     * 专门用于不同进程之间的操作
     *
     * @param c
     * @return
     */
    public static MySharedPreference get(Context c) {
        return new MySharedPreference(c);
    }

    public void clear() {
        mySharedPreference.edit().clear().commit();
    }

    public void remove(String key) {
        mySharedPreference.edit().remove(key).commit();
    }

    public void removeAll() {
        Set<String> keys = mySharedPreference.getAll().keySet();
        for (String key : keys) {
            mySharedPreference.edit().remove(key).commit();
        }
    }


    /**
     * 用于存放用户信息及长连接配置文件
     * /data/data/com.cn.good.foodingredients/Shared_Preference/foodingredients_file
     *
     * @return
     */
    public String getSPFilePathNoSuffix() {
        StringBuilder sb = new StringBuilder()
                .append("/data/data/")
                .append("com.cn.good.foodingredients")
                .append("/Shared_Preference/")
                .append(CONFIG_FILE);
        return sb.toString();
    }

    public boolean put(String key, boolean value) {
        return mySharedPreference.edit().putBoolean(key, value).commit();
    }

    public boolean put(String key, int value) {
        return mySharedPreference.edit().putInt(key, value).commit();
    }

    public boolean put(String key, long value) {
        return mySharedPreference.edit().putLong(key, value).commit();
    }

    public boolean put(String key, float value) {
        return mySharedPreference.edit().putFloat(key, value).commit();
    }

    public boolean put(String key, String value) {
        return mySharedPreference.edit().putString(key, value).commit();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return mySharedPreference.getBoolean(key, defaultValue);
    }

    public int getInt(String key, int defaultValue) {
        return mySharedPreference.getInt(key, defaultValue);
    }

    public long getLong(String key, long defaultValue) {
        return mySharedPreference.getLong(key, defaultValue);
    }

    public float getFloat(String key, float defaultValue) {
        return mySharedPreference.getFloat(key, defaultValue);
    }

    public String getString(String key, String defaultValue) {
        return mySharedPreference.getString(key, defaultValue);
    }
}
