package com.ys.wx.model.wx.response;

import java.io.Serializable;

/**
 * Title : 获取Token返回Response
 * Description :
 * Author : Jerry xu    date : 2017/1/3
 * Update :             date :
 * Version : 1.0.0
 */
public class TokenResponse implements Serializable {
    public String errcode;//错误返回编码
    public String errmsg;//错误返回消息
    public String access_token;//正确返回token；--获取到的凭证。长度为64至512个字节
    public String expires_in;//正确返回凭证；--凭证的有效时间（秒）
}
