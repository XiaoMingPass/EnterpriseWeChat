package com.ys.wx.service.dept;

import com.mhome.tools.vo.ReturnVo;
import com.ys.ucenter.api.IOrgApi;
import com.ys.ucenter.constant.UcenterConstant;
import com.ys.ucenter.model.vo.OrgTreeApiVo;
import com.ys.wx.model.dept.DepartmentForImport;
import com.ys.wx.model.dept.DepartmentWraper;
import com.ys.wx.service.data.ehr.IQueryInfoWXService;
import com.ys.wx.service.data.ehr.IQueryInfoWXServicePortType;
import com.ys.wx.utils.XStreamUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取部门信息
 */
@Service
public class DeptServiceImpl implements IDeptService {

    private static final Logger logger = Logger.getLogger(DeptServiceImpl.class);
    @Resource
    IOrgApi   orgApi;
    @Resource
    private IQueryInfoWXService iQueryInfowxService;

    public ReturnVo<List<DepartmentForImport>> getEHRDeptData() throws Exception {
        logger.info(">>>>>>>>>>>>开始获取部门信息数据>>>>>>>>>>>>>>\n");
        ReturnVo<List<DepartmentForImport>> rVo = new ReturnVo<List<DepartmentForImport>>(0, "失败");
        IQueryInfoWXServicePortType type = iQueryInfowxService.getIQueryInfoWXServiceSOAP11PortHttp();
//        String dateStr = "1970-01 00:00:00";
        String wxResponse = type.getDeptWXAll();
        DepartmentWraper departmentwraper = XStreamUtil.toBean(wxResponse, DepartmentWraper.class);
        rVo.setData(departmentwraper.getDepts());
        rVo.setCode(1);
        rVo.setMsg("成功");
        logger.info(">>>>>>>>>>>>完成获取部门信息数据>>>>>>>>>>>>>>\n");
        return rVo;
    }
    
    public ReturnVo<List<DepartmentForImport>> getMDMDeptData() throws Exception {
    	logger.info(">>>>>>>>>>>>开始获取部门信息数据>>>>>>>>>>>>>>\n");
    	ReturnVo<List<DepartmentForImport>> rVo = new ReturnVo<List<DepartmentForImport>>(0, "失败");
    	List<DepartmentForImport> departmentForImports = new ArrayList<DepartmentForImport>();
    	com.ys.tools.vo.ReturnVo<OrgTreeApiVo> orgtreevo = orgApi.getOriginalOrgTree();
    	if(null!=orgtreevo && orgtreevo.getCode().intValue()==UcenterConstant.SUCCESS && CollectionUtils.isNotEmpty(orgtreevo.getDatas()) ){
    		List<OrgTreeApiVo> OrgTreeApiVoList = orgtreevo.getDatas();
    		DepartmentForImport departmentForImport = null;
    		for (OrgTreeApiVo orgTreeApiVo : OrgTreeApiVoList) {
    			departmentForImport = new DepartmentForImport();
    			departmentForImport.setDept_id(orgTreeApiVo.getId());
    			departmentForImport.setDept_fid(orgTreeApiVo.getpId());
    			departmentForImport.setDept_name(orgTreeApiVo.getText());
    			departmentForImports.add(departmentForImport);
			}
    	}
    	
    	rVo.setData(departmentForImports);
    	rVo.setCode(1);
    	rVo.setMsg("成功");
    	logger.info(">>>>>>>>>>>>完成获取部门信息数据>>>>>>>>>>>>>>\n");
    	return rVo;
    }

    
    
}
