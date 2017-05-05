package com.cn.good.foodingredients.bean;


import com.alibaba.fastjson.annotation.JSONField;
import com.cn.entity.Entity;

/**
 * Created by will wu on 2016/8/23.
 */


public class UserLoginInfo extends Entity {
    @JSONField(name = "package")
    private String packageX;
    private String uid;
    private String ss;

    public String getPackageX() {
        return packageX;
    }

    public void setPackageX(String packageX) {
        this.packageX = packageX;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSs() {
        return ss;
    }

    public void setSs(String ss) {
        this.ss = ss;
    }
}
