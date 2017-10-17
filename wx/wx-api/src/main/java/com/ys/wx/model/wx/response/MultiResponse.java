package com.ys.wx.model.wx.response;

import java.io.Serializable;

/**
 * Title : 上传文件返回Response
 * Description :
 * Author : Jerry xu    date : 2017/1/9
 * Update :             date :
 * Version : 1.0.0
 */
public class MultiResponse implements Serializable {
    public String type;//媒体文件类型，分别有图片（image）、语音（voice）、视频（video）,普通文件(file)
    public String media_id;//媒体文件上传后获取的唯一标识。最大长度为256字节
    public String created_at;//媒体文件上传时间戳
}
