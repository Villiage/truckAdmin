package com.fxlc.truckadmin.bean;

import java.util.List;

/**
 * Created by cyd on 2017/9/20.
 */

public class SourceList {


    private List<SourceBean> sourcesList;

    public List<SourceBean> getSourcesList() {
        return sourcesList;
    }

    public void setSourcesList(List<SourceBean> sourcesList) {
        this.sourcesList = sourcesList;
    }

    public static class SourceBean {
        /**
         * sourceId : 6a40e1c968ab4b098110d30bb3ec97a6
         * sourceName : 1aaaaaa
         */

        private String sourceId;
        private String sourceName;

        public String getSourceId() {
            return sourceId;
        }

        public void setSourceId(String sourceId) {
            this.sourceId = sourceId;
        }

        public String getSourceName() {
            return sourceName;
        }

        public void setSourceName(String sourceName) {
            this.sourceName = sourceName;
        }
    }
}
