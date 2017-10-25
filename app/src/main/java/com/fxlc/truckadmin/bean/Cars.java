package com.fxlc.truckadmin.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cyd on 2017/9/20.
 */

public class Cars implements Serializable{



    private String truckerId;
    private List<CarlistBean> carlist;

    public String getTruckerId() {
        return truckerId;
    }

    public void setTruckerId(String truckerId) {
        this.truckerId = truckerId;
    }

    public List<CarlistBean> getCarlist() {
        return carlist;
    }

    public void setCarlist(List<CarlistBean> carlist) {
        this.carlist = carlist;
    }

    public static class CarlistBean implements Serializable{
        /**
         * carId : 38f77540dec0486ba33f34a1f397efe6
         * carNo : 晋1222
         */

        private String carId;
        private String carNo;

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
    }
}
