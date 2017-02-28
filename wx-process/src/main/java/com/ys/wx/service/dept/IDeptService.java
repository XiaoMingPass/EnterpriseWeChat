package com.ys.wx.service.dept;

import com.ys.wx.model.dept.DepartmentForImport;
import com.ys.wx.vo.ReturnVo;

import java.util.List;

/**
 * Title : 部门服务接口，拉取外部数据接口
 * Description :
 * Author : Ceaser wang Jerry xu    date : 2017/1/12
 * Update :                         date :
 * Version : 1.0.0
 * Copyright (c) 二龙湖基地组织  2016 ~ 2017 版权所有
 */
public interface IDeptService {
    ReturnVo<List<DepartmentForImport>> getEHRDeptData() throws Exception;
}
