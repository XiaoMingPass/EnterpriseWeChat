package com.ys.wx.service.department.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ys.tools.common.StringTools;
import com.ys.tools.pager.PagerModel;
import com.ys.tools.pager.Query;
import com.ys.ucenter.constant.UcenterConstant;
import com.ys.ucenter.enms.DepartmentStatusEnum;
import com.ys.ucenter.utils.CommUtils;
import com.ys.wx.constant.WxConstant;
import com.ys.wx.dao.department.IDepartmentDao;
import com.ys.wx.service.common.ISequenceService;
import com.ys.wx.service.company.ICompanyService;
import com.ys.wx.service.department.IDepartmentService;
import com.ys.wx.vo.Company;
import com.ys.wx.vo.Department;




/**
 * 部门Service实现
 * @author CeaserWang
 * @date 2017-08-28 16:45:03
 */
@Service
public class DepartmentServiceImpl implements IDepartmentService {
	private static Logger logger = LoggerFactory.getLogger(DepartmentServiceImpl.class);
	
	@Resource
	private ISequenceService sequenceService;
	
	@Resource
	private IDepartmentDao departmentDao;

	@Resource
	private ICompanyService companyService;	
	
	@Override
	public Department getDepartmentById(String id) throws Exception {
		return StringUtils.isNotBlank(id) ? this.departmentDao.getDepartmentById(id.trim()) : null;
	}

	@Override
	public List<Department> getDepartmentByIds(String ids) throws Exception {
		ids = StringTools.converString(ids);
		return StringUtils.isNotBlank(ids) ? this.departmentDao.getDepartmentByIds(ids) : null;
	}
	
	@Override
	public List<Department> getDepartmentByIdsList(List<String> ids) throws Exception {
		return CollectionUtils.isNotEmpty(ids) ? this.departmentDao.getDepartmentByIdsList(ids) : null;
	}

	@Override
	public List<Department> getAll(Department department) throws Exception {
		return null != department ? this.departmentDao.getAll(department) : null;
	}

	@Override
	public PagerModel<Department> getPagerModelByQuery(Department department, Query query)
			throws Exception {
		return (null != department && null != query) ? this.departmentDao.getPagerModelByQuery(department, query) : null;
	}
	
	@Override
	public int getByPageCount(Department department)throws Exception {
		return (null != department) ? this.departmentDao.getByPageCount(department) : 0;
	}

	@Override
	public void insertDepartment(Department department) throws Exception {
		this.departmentDao.insertDepartment(department);
	}
	
	@Override
	public void insertDepartmentBatch(List<Department> departments) throws Exception {
		this.departmentDao.insertDepartmentBatch(departments);
	}
	
	@Override
	public void insertDeptReplaceBatch(List<Department> departments) throws Exception{
		this.departmentDao.insertDeptReplaceBatch(departments);
	}
	
	@Override
	public void delDepartmentById(String id) throws Exception {
		if (StringUtils.isNotBlank(id)) {
			this.departmentDao.delDepartmentById(id.trim());
		}
	}
	
	@Override
	public void delDepartmentByIds(String ids) throws Exception {
		ids = StringTools.converString(ids);
		if(StringUtils.isNotBlank(ids)){
			this.departmentDao.delDepartmentByIds(ids);
		}
	}
	
	@Override
	public void delDepartmentByIdsList(List<String> ids) throws Exception {
		if(CollectionUtils.isNotEmpty(ids)){
			this.departmentDao.delDepartmentByIdsList(ids);
		}
	}

	@Override
	public int updateDepartment(Department department) throws Exception {
		return this.departmentDao.updateDepartment(department);
	}
	
	@Override
	public int updateDepartmentByIds(String ids,Department department) throws Exception {
		return this.departmentDao.updateDepartmentByIds(ids, department);
	}
	
	@Override
	public int updateDepartmentByIdsList(List<String> ids,Department department) throws Exception {
		return this.departmentDao.updateDepartmentByIdsList(ids, department);
	}
	
	@Override
	public int updateDepartmentList(List<Department> departments) throws Exception {
		return this.departmentDao.updateDepartmentList(departments);
	}

	
	private boolean isModified(com.ys.ucenter.model.user.Department source,Department dept){
		boolean rflag = true;
		if( (null!=source.getPreDeptId() && null!=dept.getPid() &&  !source.getPreDeptId().equals(dept.getPid())) || 
				(null!=source.getName() &&  null!=dept.getName() &&  !source.getName().equals(dept.getName())) ||
				(null!=source.getCompanyId() &&  null!=dept.getCompanyId() &&  !source.getCompanyId().equals(dept.getCompanyId())) ||
				((null!=source.getDelFlag() && null!=dept.getDelFlag()) && source.getDelFlag().intValue()!=dept.getDelFlag().intValue())){
			rflag = true;
		}
		return rflag;
	}
	
	@Override
	public void process(List<com.ys.ucenter.model.user.Department> departmentList) {
		logger.info("DepartmentServiceImpl-process start :>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		try {
			Department qd = new Department();
			qd.setDelFlag(null);
			List<Department>  allDepts =  getAll(qd);
			Map<String,Department> allDeptsMaps = new  HashMap<String, Department>();
			if(CollectionUtils.isNotEmpty(allDepts)){
				for (Department dept : allDepts) {
					allDeptsMaps.put(dept.getId(), dept);
				}
			}
			
			if(CollectionUtils.isNotEmpty(departmentList)){
				logger.info("接受队列部门数量:"+departmentList.size());
				Map<String,String> globalIdToCode = new  HashMap<String, String>();
				List<Department> saveSepts = new ArrayList<Department>();
				Department tempDepartment = null;
				for (int i=0;i<departmentList.size();i++) {
					
					com.ys.ucenter.model.user.Department department =departmentList.get(i); 
					if(departmentList.size()>1 && ( UcenterConstant.NO!=department.getIscancle().intValue() 
							|| UcenterConstant.NO_DELETE_FLAG != department.getDelFlag().intValue()
							|| department.getStatus().intValue()!=DepartmentStatusEnum.Y.getCode().intValue())){
						departmentList.remove(i);
						i--;
						continue;
					}
					tempDepartment = new Department();
					//判断修改
					if(allDeptsMaps.containsKey(department.getId())){
						Department ldept =  allDeptsMaps.get(department.getId());
						if(isModified(department,ldept)){
							logger.info("当前处理被修改:"+department);
							tempDepartment.setId(department.getId());
							tempDepartment.setPid(department.getPreDeptId());
							tempDepartment.setCompanyId(department.getCompanyId());
							tempDepartment.setName(department.getName());
							
							tempDepartment.setDelFlag(department.getDelFlag());
							tempDepartment.setCode(ldept.getCode());
							tempDepartment.setPcode(ldept.getPcode());
							tempDepartment.setUpdateTime(new Date());
							tempDepartment.setSyn(WxConstant.MODYFIED);
							tempDepartment.setOrderNo("");
						}else{
							logger.info("当前处理未发生修改:"+department);
							globalIdToCode.put(ldept.getId(), ldept.getCode());
							//没有修改，跳出
							continue;
						}
					}else{
						logger.info("当前处理发生新增:"+department);
						tempDepartment.setId(department.getId());
						tempDepartment.setPid(department.getPreDeptId());
						tempDepartment.setCompanyId(department.getCompanyId());
						tempDepartment.setCode(sequenceService.next(WxConstant.GLOBAL));
						//tempCompany.setPcode(pcode);
						tempDepartment.setName(department.getName());
						tempDepartment.setCreateTime(new Date());
						tempDepartment.setUpdateTime(null);
						tempDepartment.setOrderNo("");
						tempDepartment.setDelFlag(department.getDelFlag());
						tempDepartment.setSyn(WxConstant.MODYFIED);
					}
					globalIdToCode.put(tempDepartment.getId(), tempDepartment.getCode());
					saveSepts.add(tempDepartment);
				}
				//矫正pcode
				for (Department tdept : saveSepts) {
					String currentCodeMap = globalIdToCode.get(tdept.getPid());
					if(CommUtils.isEmpty(currentCodeMap)){
						Department dept = allDeptsMaps.get(tdept.getPid());
						if(null!=dept){
							currentCodeMap = dept.getCode();
						}
					}
					tdept.setPcode(currentCodeMap);
				}
				
				insertDeptReplaceBatch(saveSepts);
			}
		} catch (Exception e) {
			logger.error("DepartmentServiceImpl-process"+e);
			e.printStackTrace();
		}
		logger.info("DepartmentServiceImpl-process end :>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		
	}
	
	
	public List<Department> genDeptTreeMixCompany()throws Exception{
		List<Department> treeOfdepartments = new ArrayList<Department>();
		Company company = new Company();
		company.setDelFlag(null);
		List<Company> companies = companyService.getAll(company);
		if(CollectionUtils.isNotEmpty(companies)){
			
			Map<String,Company> companyIdToId = new HashMap<String, Company>();
			
			for (Company company2 : companies) {
				companyIdToId.put(company2.getId(), company2);
			}
			
			Department department = new Department();
			department.setDelFlag(null);
			treeOfdepartments = getAll(department);
			Map<String,String> comTypeDeptMap = new HashMap<String, String>();
			if(CollectionUtils.isNotEmpty(treeOfdepartments)){
				for (int i=0;i<treeOfdepartments.size();i++) {
					Department department2 = treeOfdepartments.get(i);
					if(CommUtils.isEmpty(department2.getPid())){
						Company mapcom = companyIdToId.get(department2.getCompanyId());
						if(null!=mapcom ){
							department2.setPid(mapcom.getId());	
							department2.setPcode(mapcom.getCode());
							comTypeDeptMap.put(mapcom.getId(),mapcom.getId());
							logger.info("match : "+department2);
						}else{
							treeOfdepartments.remove(i);
							i--;
						}
					}
				}
				for (int i=0;i<treeOfdepartments.size();i++) {
					if(CommUtils.isEmpty(treeOfdepartments.get(i).getPcode())){
						treeOfdepartments.remove(i);
						i--;
					}
				}
			}
			
			Department tempDepartment = null;
			for (Company company2 : companies) {
			//	if(!comTypeDeptMap.containsKey(company2.getId())){
					tempDepartment = new Department();
					tempDepartment.setId(company2.getId());
					tempDepartment.setPid(company2.getPid());
					tempDepartment.setCode(company2.getCode());
					tempDepartment.setPcode(company2.getPcode());
					tempDepartment.setName(company2.getName());
					logger.info("add : "+tempDepartment);
					treeOfdepartments.add(tempDepartment);
			//	}
			}
		}
		
		return treeOfdepartments;
		
	}
	
	
	private List<com.ys.wx.vo.Department>  getFilterData(List<com.ys.wx.vo.Department> rHRData) {
        try {
            Set<String> ids = new HashSet<String>();
            Set<String> names = new HashSet<String>();
            for (com.ys.wx.vo.Department department : rHRData) {
        		if(CommUtils.isEmpty(department.getCode()) || CommUtils.isEmpty(department.getName())){
        			logger.info("空的数据-name:"+department.getName()+"-code:"+department.getCode()+"-pcode:"+department.getPcode());
        		}
				if(ids.contains(department.getCode())){
					logger.info("重复编码:"+department.getCode());
				}else{
					ids.add(department.getCode());
				}
				if(names.contains(department.getPcode()+department.getName())){
					logger.info("同一部门【"+department.getPcode()+"】下存在相同名字部门，重复名称:"+department.getName());
				}else{
					names.add(department.getPcode()+department.getName());
				}
			}
            List<com.ys.wx.vo.Department> pCodeIsnull =getPcodeIsNull(rHRData);
            return  treeChilds(pCodeIsnull,rHRData,"|-");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private  List<com.ys.wx.vo.Department> treeChilds(List<com.ys.wx.vo.Department> ch,List<com.ys.wx.vo.Department> rHRData,String s){
    	List<com.ys.wx.vo.Department> departments = new ArrayList<Department>();
    	for (com.ys.wx.vo.Department department : ch) {
    		if(CommUtils.isEmpty(department.getCode()) || CommUtils.isEmpty(department.getName())){
    			logger.info("【"+s+"】"+"name:"+department.getName()+"-code:"+department.getCode()+"-pcode:"+department.getPcode());
    		}
    		logger.info(s+"name:"+department.getName()+"-code:"+department.getCode()+"-pcode:"+department.getPcode());
    		departments.add(department);
    		List<com.ys.wx.vo.Department> cchilds = getchilds(department.getCode(),rHRData);
    		if(CollectionUtils.isNotEmpty(cchilds)){
    			treeChilds(cchilds,rHRData,s+"|-");
    		}
		}
    	return departments;
    }
    
    private List<com.ys.wx.vo.Department> getchilds(String pcode,List<com.ys.wx.vo.Department> rHRData){
    	List<com.ys.wx.vo.Department> rl = new ArrayList<com.ys.wx.vo.Department>();
    	for (com.ys.wx.vo.Department department : rHRData) {
			if(CommUtils.isNotEmpty(department.getPcode()) && department.getPcode().equals(pcode)){
				rl.add(department);
			}
		}
    	return rl;
    }
    
    private  List<com.ys.wx.vo.Department> getPcodeIsNull(List<com.ys.wx.vo.Department> rHRData){
    	List<com.ys.wx.vo.Department> rl = new ArrayList<com.ys.wx.vo.Department>();
    	for (com.ys.wx.vo.Department department : rHRData) {
			if(CommUtils.isNotEmpty(department.getPcode()) && department.getPcode().equals("1")){
				rl.add(department);
			}
		}
    	return rl;
    }
	
	//------------api------------
	
}

