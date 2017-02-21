package com.ys.wx.service.person;

import java.util.List;

import com.ys.wx.model.person.PersonalForImport;
import com.ys.wx.vo.ReturnVo;

/**
 * 人员数据拉取接口
 * @author wangzequan
 *@Copyright:Copyright (c) 二龙湖基地组织  2015 ~ 2016 版权所有
 */
public interface IPersonService {

    ReturnVo<List<PersonalForImport>> getEHRPersonalData() throws Exception;
    public ReturnVo<List<PersonalForImport>> getEHRPersonalData(int page) throws Exception ;
    public  ReturnVo<List<PersonalForImport>> getEHRPersonalDataByTime(String  time) throws Exception;
}
