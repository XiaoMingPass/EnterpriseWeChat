package com.ys.wx.service.dept;

import com.mhome.tools.vo.ReturnVo;
import com.ys.wx.model.dept.DepartmentForImport;

import java.util.List;

public interface IDeptService {
	/**
	 * use other ehr interface get organization data that the author is wangjialiang
	 * @return
	 * @throws Exception
	 * author:CeaserWang
	 */
	@Deprecated
    ReturnVo<List<DepartmentForImport>> getEHRDeptData() throws Exception;
    
    /**
     * supported by user center(MDM) for weixin project,{@link #getEHRDeptData} is Deprecated
     * @return
     * @throws Exception
     * author:CeaserWang
     */
    public ReturnVo<List<DepartmentForImport>> getMDMDeptData() throws Exception ;
}
