package com.fxlc.truckadmin.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cyd on 2017/8/21.
 */

public class BankCards implements Serializable{
    private List<BankCard> bankCardList;

    public List<BankCard> getBankcard() {
        return bankCardList;
    }

    public void setBankcard(List<BankCard> bankcard) {
        this.bankCardList = bankcard;
    }

    public static class BankCard implements Serializable{


        /**
         * id : 010da20937fa4bb1b9e6a3ebc6890388
         * bankType : 农业银行
         * bankNo : 648726942669
         */

        private String cardId;
        private String bankType;
        private String bankNo;
        private String name;

        public String getId() {
            return cardId;
        }

        public void setId(String id) {
            this.cardId = id;
        }

        public String getBankType() {
            return bankType;
        }

        public void setBankType(String bankType) {
            this.bankType = bankType;
        }

        public String getBankNo() {
            return bankNo;
        }

        public void setBankNo(String bankNo) {
            this.bankNo = bankNo;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
