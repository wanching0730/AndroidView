package com.example.milka.m0802.ContentResolver.Contact.Util;

import android.support.annotation.Nullable;

/**
 * Created by Milka on 2017/8/2.
 */

public class ContactBean {

    private String personName;
    private String phoneNum;
    /**
     * @param personName 联系人姓名
     * @param phoneNum 联系人电话号码
    */
    public ContactBean(@Nullable String personName, @Nullable String phoneNum) {
        this.personName = personName;
        this.phoneNum = phoneNum;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
}
