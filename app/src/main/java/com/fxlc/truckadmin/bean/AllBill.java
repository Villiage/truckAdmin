package com.fxlc.truckadmin.bean;

import java.util.List;

/**
 * Created by cyd on 2017/9/22.
 */

public class AllBill {


    private List<BillBean> finishList;
    private List<BillBean> unfinishList;

    public List<BillBean> getFinishList() {
        return finishList;
    }

    public void setFinishList(List<BillBean> finishList) {
        this.finishList = finishList;
    }

    public List<BillBean> getUnfinishList() {
        return unfinishList;
    }

    public void setUnfinishList(List<BillBean> unfinishList) {
        this.unfinishList = unfinishList;
    }

    public static class BillBean {

        private String orderId;
        private String carNo;
        private String loadDate;
        private String unloadDate;
        private String loadWeight;
        private String unloadWeight;
        private String endSum;
        private String orderStatus;
        private String sourceId;
        private String carId;

        public String getSourceId() {
            return sourceId;
        }

        public void setSourceId(String sourceId) {
            this.sourceId = sourceId;
        }

        public String getCarId() {
            return carId;
        }

        public void setCarId(String carId) {
            this.carId = carId;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getCarNo() {
            return carNo;
        }

        public void setCarNo(String carNo) {
            this.carNo = carNo;
        }

        public String getLoadDate() {
            return loadDate;
        }

        public void setLoadDate(String loadDate) {
            this.loadDate = loadDate;
        }

        public String getUnloadDate() {
            return unloadDate;
        }

        public void setUnloadDate(String unloadDate) {
            this.unloadDate = unloadDate;
        }

        public String getLoadWeight() {
            return loadWeight;
        }

        public void setLoadWeight(String loadWeight) {
            this.loadWeight = loadWeight;
        }

        public String getUnloadWeight() {
            return unloadWeight;
        }

        public void setUnloadWeight(String unloadWeight) {
            this.unloadWeight = unloadWeight;
        }

        public String getEndSum() {
            return endSum;
        }

        public void setEndSum(String endSum) {
            this.endSum = endSum;
        }

        public String getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
        }
    }
}
