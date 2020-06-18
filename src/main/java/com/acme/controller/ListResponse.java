package com.acme.controller;

import java.util.List;

public class ListResponse<T> {
    private Meta meta;

    private List<T> data;

    protected ListResponse() {}

    public ListResponse(List<T> data) {
        this.meta = new Meta(data.size());
        this.data = data;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public static class Meta {
        private Integer totalCount;

        protected Meta() {}

        public Meta(Integer totalCount) {
            this.totalCount = totalCount;
        }

        public Integer getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(Integer totalCount) {
            this.totalCount = totalCount;
        }
    }
}
