package com.fxlc.truckadmin.bean;

import java.io.Serializable;

/**
 * Created by cyd on 2017/6/14.
 */

public class User implements Serializable {
    String id;
    String name;
    String mobile;
    String token;
    int pstatus;
    float money;
    int credit;
    int sumOrder;
    String sumWeight;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getPstatus() {
        return pstatus;
    }

    public void setPstatus(int pstatus) {
        this.pstatus = pstatus;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public int getSumOrder() {
        return sumOrder;
    }

    public void setSumOrder(int sumOrder) {
        this.sumOrder = sumOrder;
    }

    public String getSumWeight() {
        return sumWeight;
    }

    public void setSumWeight(String sumWeight) {
        this.sumWeight = sumWeight;
    }
}
