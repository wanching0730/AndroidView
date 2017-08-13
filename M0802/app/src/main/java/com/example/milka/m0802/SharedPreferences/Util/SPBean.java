package com.example.milka.m0802.SharedPreferences.Util;

/**
 * Created by Milka on 2017/8/3.
 *
 * SharedPreference键值对
 */

public class SPBean {
    private String key;
    private String value;

    public SPBean(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
