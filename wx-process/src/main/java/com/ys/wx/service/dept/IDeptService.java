package com.ys.wx.service.dept;

import java.util.List;

import com.ys.wx.model.dept.DepartmentForImport;
import com.ys.wx.vo.ReturnVo;

/**
 * 部门服务接口，拉取外部数据
 * @author wangzequan
 *@Copyright:Copyright (c) 二龙湖基地组织  2015 ~ 2016 版权所有
 */
public interface IDeptService {
    ReturnVo<List<DepartmentForImport>> getEHRDeptData() throws Exception;
}
