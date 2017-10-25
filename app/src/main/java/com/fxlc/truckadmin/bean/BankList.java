package com.fxlc.truckadmin.bean;

import java.util.List;

/**
 * Created by cyd on 2017/8/18.
 */

public class BankList {

    private List<BankBean> bankList;

    public List<BankBean> getBankList() {
        return bankList;
    }

    public void setBankList(List<BankBean> bankList) {
        this.bankList = bankList;
    }

    public static class BankBean {
        /**
         * id : 201b0b22a2a24fa4a61c24d8260dd39a
         * name : 农业银行
         */

        private String id;
        private String name;

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
    }
}
