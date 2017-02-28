package com.ys.wx.utils.pager;

import java.io.Serializable;
import java.util.Map;

/**
 * Title : 查询参数封装
 * Description :
 * Author : Bruce.liu    date : 2015年1月24日
 * Update :              date :
 * Version : 1.0.0
 * Copyright (c) 二龙湖基地组织  2016 ~ 2017 版权所有
 */
public class Query implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 817880730448759944L;
    /**
     * 页码 easyUI
     */
    private int pageNumber;
    /**
     * 每页显示记录数 easyUI
     */
    private int pageSize = 15;
    /**
     * easyUI的排序
     */
    private String               orderby;
    /**
     * 排序 key是返回值的属性名 value是desc或者asc
     * 我们添加数据的时候最好用linkMap
     */
    private Map<String, ORDERBY> sqlOrderBy;
    /**
     * 跳转 miniUI
     */
    private int                  pageIndex;
    /**
     * miniUI
     */
    private String               page;
    /**
     * 行数 适应miniUI
     */
    private int                  rows;

    public Query() {
    }

    public Query(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageIndex() {
        if (this.pageIndex < 0) {
            this.pageIndex = 1;
        } else {
            this.pageIndex++;
        }
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

//    public String getOrderby() {
//        return orderby;
//    }
//
//    public void setOrderby(String orderby) {
//        this.orderby = orderby;
//    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public String getOrderby() {
        return orderby;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    public Map<String, ORDERBY> getSqlOrderBy() {
        return sqlOrderBy;
    }

    public void setSqlOrderBy(Map<String, ORDERBY> sqlOrderBy) {
        this.sqlOrderBy = sqlOrderBy;
    }

}
