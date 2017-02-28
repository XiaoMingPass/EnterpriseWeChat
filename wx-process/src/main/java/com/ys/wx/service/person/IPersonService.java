package com.ys.wx.service.person;

import com.ys.wx.model.person.PersonalForImport;
import com.ys.wx.vo.ReturnVo;

import java.util.List;

/**
 * Title : 人员数据拉取接口
 * Description :
 * Author : Ceaser wang Jerry xu    date : 2017/1/12
 * Update :                         date :
 * Version : 1.0.0
 * Copyright (c) 二龙湖基地组织  2016 ~ 2017 版权所有
 */
public interface IPersonService {

    ReturnVo<List<PersonalForImport>> getEHRPersonalData() throws Exception;

    ReturnVo<List<PersonalForImport>> getEHRPersonalData(int page) throws Exception;

    ReturnVo<List<PersonalForImport>> getEHRPersonalDataByTime(String time) throws Exception;
}
