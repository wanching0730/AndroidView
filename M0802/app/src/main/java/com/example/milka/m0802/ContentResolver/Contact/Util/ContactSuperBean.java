package com.example.milka.m0802.ContentResolver.Contact.Util;

import java.util.List;

/**
 * Created by Milka on 2017/8/4.
 *
 * 一个联系人多个号码
 */

public class ContactSuperBean {

    private String name;
    private List<String> phoneList;

    public ContactSuperBean(String name, List<String> phoneList) {
        this.name = name;
        this.phoneList = phoneList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPhoneList() {
        return phoneList;
    }

    public void setPhoneList(List<String> phoneList) {
        this.phoneList = phoneList;
    }
}
