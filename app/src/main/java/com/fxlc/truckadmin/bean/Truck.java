package com.fxlc.truckadmin.bean;

import java.io.Serializable;

/**
 * Created by cyd on 2017/6/23.
 */

public class Truck implements Serializable {
    private int status;
    private int cartype;


    public int getCartype() {
        return cartype;
    }

    public void setCartype(int cartype) {
        this.cartype = cartype;
    }

    private String brand;
    private String style;
    private String soup;
    private String drive;
    private String carNo;
    private String bossMobile;
    private String followMobile;

    private String handcarNo;


    private String type;
    private String length;
    private String height;
    private String mortgageMoney;

    private String imgs;
    private String handImgs;


    public String getDrive() {
        return drive;
    }

    public void setDrive(String drive) {
        this.drive = drive;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
    public String getSoup() {
        return soup;
    }

    public void setSoup(String soup) {
        this.soup = soup;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }



    public String getHandcarNo() {
        return handcarNo;
    }

    public void setHandcarNo(String handcarNo) {
        this.handcarNo = handcarNo;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMortgageMoney() {
        return mortgageMoney;
    }

    public void setMortgageMoney(String mortgageMoney) {
        this.mortgageMoney = mortgageMoney;
    }

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }

    public String getHandImgs() {
        return handImgs;
    }

    public void setHandImgs(String handImgs) {
        this.handImgs = handImgs;
    }
}
