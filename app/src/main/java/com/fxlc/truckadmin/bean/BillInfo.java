package com.fxlc.truckadmin.bean;

import java.io.Serializable;

/**
 * Created by cyd on 2017/9/21.
 */

public class BillInfo implements Serializable{



    private String carId;
    private String carNo;
    private String bossMobile;
    private String followMobile;
    private String handcarNo;
    private String orderId;
    private String orderNo;
    private String from;
    private String to;
    private String sourceName;
    private int isInsurance;
    private String insurance;

    private String loadWeight;
    private String loadBill;
    private String unloadWeight;
    private String unloadBill;
    private String orderStatus;

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getBossMobile() {
        return bossMobile;
    }

    public void setBossMobile(String bossMobile) {
        this.bossMobile = bossMobile;
    }

    public String getFollowMobile() {
        return followMobile;
    }

    public void setFollowMobile(String followMobile) {
        this.followMobile = followMobile;
    }

    public String getHandcarNo() {
        return handcarNo;
    }

    public void setHandcarNo(String handcarNo) {
        this.handcarNo = handcarNo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public int getIsInsurance() {
        return isInsurance;
    }

    public void setIsInsurance(int isInsurance) {
        this.isInsurance = isInsurance;
    }

    public String getLoadWeight() {
        return loadWeight;
    }

    public void setLoadWeight(String loadWeight) {
        this.loadWeight = loadWeight;
    }

    public String getLoadBill() {
        return loadBill;
    }

    public void setLoadBill(String loadBill) {
        this.loadBill = loadBill;
    }

    public String getUnloadWeight() {
        return unloadWeight;
    }

    public void setUnloadWeight(String unloadWeight) {
        this.unloadWeight = unloadWeight;
    }

    public String getUnloadBill() {
        return unloadBill;
    }

    public void setUnloadBill(String unloadBill) {
        this.unloadBill = unloadBill;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
