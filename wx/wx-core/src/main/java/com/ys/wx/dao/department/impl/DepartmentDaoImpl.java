package com.ys.wx.dao.department.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.ys.tools.common.StringTools;
import com.ys.tools.common.UUIDGenerator;
import com.ys.tools.pager.PagerModel;
import com.ys.tools.pager.Query;
import com.ys.tools.template.MybatisTemplate;
import com.ys.ucenter.utils.CommUtils;
import com.ys.wx.dao.department.IDepartmentDao;
import com.ys.wx.vo.Department;




/**
 * 部门Dao实现
 * @author CeaserWang
 * @date 2017-08-28 16:45:03
 */
@Repository
public class DepartmentDaoImpl extends MybatisTemplate implements IDepartmentDao {

	@Override
	public Department getDepartmentById(String id) throws Exception {
		return (Department)this.selectOne("DepartmentXML.getDepartmentById", id);
	}
	
	@Override
	public List<Department> getDepartmentByIds(String ids) throws Exception {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ids", ids);
		return this.selectList("DepartmentXML.getDepartmentByIds", params);
	}
	
	@Override
	public List<Department> getDepartmentByIdsList(List<String> ids) throws Exception {
		return this.selectList("DepartmentXML.getDepartmentByIdsList", ids);
	}

	@Override
	public List<Department> getAll(Department department) throws Exception {
		return this.selectList("DepartmentXML.getAll", department);
	}

	@Override
	public PagerModel<Department> getPagerModelByQuery(Department department, Query query)
			throws Exception {
		return this.getPagerModelByQuery(department, query, "DepartmentXML.getPagerModelByQuery");
	}
	
	@Override
	public int getByPageCount(Department department)throws Exception {
		return this.selectOne("DepartmentXML.getByPageCount", department);
	}

	@Override
	public void insertDepartment(Department department) throws Exception {
		if (null != department) {
			department.setId(UUIDGenerator.generate());
			department.setCreateTime(new Date());
			department.setUpdateTime(new Date());
			this.insert("DepartmentXML.insertDepartment", department);
		}
	}
	
	@Override
	public void insertDeptReplaceBatch(List<Department> departments) throws Exception {
		if(CollectionUtils.isNotEmpty(departments)){
			for (Department department : departments) {
				if (null != department) {
					if(CommUtils.isEmpty(department.getId())){
						department.setId(UUIDGenerator.generate());
					}
					department.setCreateTime(new Date());
				}
			}
			this.insert("DepartmentXML.insertDeptReplaceBatch", departments);
		}
	}
	
	@Override
	public void insertDepartmentBatch(List<Department> departments) throws Exception {
		if(CollectionUtils.isNotEmpty(departments)){
			for (Department department : departments) {
				if (null != department) {
					department.setId(UUIDGenerator.generate());
					department.setCreateTime(new Date());
				}
			}
			this.insert("DepartmentXML.insertDepartmentBatch", departments);
		}
	}
	
	@Override
	public void delDepartmentById(String id) throws Exception {
		this.delete("DepartmentXML.delDepartmentById", id);
	}
	
	@Override
	public void delDepartmentByIds(String ids) throws Exception {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ids", ids);
		this.delete("DepartmentXML.delDepartmentByIds", params);
	}
	
	@Override
	public void delDepartmentByIdsList(List<String> ids) throws Exception {
		this.delete("DepartmentXML.delDepartmentByIdsList", ids);
	}
	
	@Override
	public int updateDepartment(Department department) throws Exception {
		if (null != department) {
			department.setUpdateTime(new Date());
			/**清理不需要更新的数据*/
		    this.cleanWhenUpdate(department);
			return this.update("DepartmentXML.updateDepartment", department);
		} else {
			return 0;
		}
	}
	
	@Override
	public int updateDepartmentByIds(String ids,Department department) throws Exception {
		ids = StringTools.converString(ids);
		if (StringUtils.isNotBlank(ids) && null != department) {
			department.setUpdateTime(new Date());
			Map<String,Object> params = new HashMap<String, Object>();
			params.put("ids", ids);
			/**清理不需要更新的数据*/
		    this.cleanWhenUpdate(department);
			params.put("department", department);
			return this.update("DepartmentXML.updateDepartmentByIds", params);
		} else {
			return 0;
		}
		
	}
	
	@Override
	public int updateDepartmentByIdsList(List<String> ids,Department department) throws Exception {
		if (CollectionUtils.isNotEmpty(ids) && null != department) {
			department.setUpdateTime(new Date());
			Map<String,Object> params = new HashMap<String, Object>();
			params.put("ids", ids);
			/**清理不需要更新的数据*/
		    this.cleanWhenUpdate(department);
			params.put("department", department);
			return this.update("DepartmentXML.updateDepartmentByIdsList", params);
		} else {
			return 0;
		}
	}
	
	@Override
	public int updateDepartmentList(List<Department> departments) throws Exception {
		if(CollectionUtils.isNotEmpty(departments)){
			for (Department department : departments) {
				if (null != department) {
					department.setUpdateTime(new Date());
					/**清理不需要更新的数据*/
					this.cleanWhenUpdate(department);
				}
			}
			return this.update("DepartmentXML.updateDepartmentList", departments);
		} else {
			return 0;
		}
	}
	
	/**
	 * 清理不需要更新的数据
	 * @param orderRebok
	 * @Description:
	 * @author wentaoxiang 2016年6月1日 下午5:19:16
	 */
	private void cleanWhenUpdate(Department department) {
		department.setCreateTime(null);
		department.setCreator(null);
	}
	
	//------------api------------
}

