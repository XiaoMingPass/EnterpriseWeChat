package com.ys.wx.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Title : 返回VO
 * Description :
 * Author : Jerry xu    date : 2017/15/46
 * Update :             date :
 * Version : 1.0.0
 * Copyright (c) 二龙湖基地组织  2016 ~ 2017 版权所有
 */
public class ReturnVo<T> implements Serializable {

    private static final long serialVersionUID = -5580228202640516960L;
    // 响应编码
    private Integer code;
    // 响应消息
    private String  msg;
    // 返回的vo
    private T       data;
    // 返回的list
    private List<T> datas;

    public ReturnVo() {
        super();
    }

    public ReturnVo(Integer code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
    }

    public ReturnVo(Integer code, String msg, T data, List<T> datas) {
        super();
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.datas = datas;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }
}