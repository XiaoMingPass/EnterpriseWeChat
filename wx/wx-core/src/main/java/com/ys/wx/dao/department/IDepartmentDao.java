package com.ys.wx.dao.department;

import java.util.List;

import com.ys.tools.pager.PagerModel;
import com.ys.tools.pager.Query;
import com.ys.wx.vo.Department;



/**
 * 部门Dao接口
 * @author CeaserWang
 * @date 2017-08-28 16:45:03
 */
public interface IDepartmentDao {

	/**
	 * 通过id得到部门Department
	 * @param id
	 * @return 
	 * @throws Exception
	 * @Description:
	 */
	public Department getDepartmentById(String id) throws Exception;

	/**
	 * 通过ids批量得到部门Department
	 * @param ids 如："'1','2','3','4'..."
	 * @return 
	 * @throws Exception
	 * @Description:
	 */
	public List<Department> getDepartmentByIds(String ids) throws Exception;
	
	/**
	 * 通过ids批量得到部门Department
	 * @param ids 
	 * @return 
	 * @throws Exception
	 * @Description:
	 */
	public List<Department> getDepartmentByIdsList(List<String> ids) throws Exception;

	/**
	 * 得到所有部门Department
	 * @param department
	 * @return 
	 * @throws Exception
	 * @Description:
	 */
	public List<Department> getAll(Department department) throws Exception;

	/**
	 * 分页查询部门Department
	 * @param department
	 * @param query
	 * @return
	 * @throws Exception
	 * @Description:
	 */
	public PagerModel<Department> getPagerModelByQuery(Department department, Query query) throws Exception;
	
	/**
	 * 查询记录数
	 * @param department
	 * @return
	 * @throws Exception
	 * @Description:
	 */
	public int getByPageCount(Department department)throws Exception ;

	/**
	 * 有则改之无则加勉
	 * @param departments
	 * @throws Exception
	 */
	public void insertDeptReplaceBatch(List<Department> departments) throws Exception;
	
	/**
	 * 添加部门Department
	 * @param department
	 * @throws Exception
	 * @Description:
	 */
	public void insertDepartment(Department department) throws Exception;
	
	
	
	/**
	 * 批量添加部门Department
	 * @param departments
	 * @throws Exception
	 * @Description:
	 */
	public void insertDepartmentBatch(List<Department> departments) throws Exception;

	/**
	 * 通过id删除部门Department
	 * @param id
	 * @throws Exception
	 * @Description:
	 */
	public void delDepartmentById(String id) throws Exception;
	
	/**
	 * 通过id批量删除部门Department
	 * @param ids 如："'1','2','3','4'..."
	 * @throws Exception
	 * @Description:
	 */
	public void delDepartmentByIds(String ids) throws Exception;
	
	/**
	 * 通过id批量删除部门Department
	 * @param ids 
	 * @throws Exception
	 * @Description:
	 */
	public void delDepartmentByIdsList(List<String> ids) throws Exception;

	/**
	 * 通过id修改部门Department
	 * @param department
	 * @throws Exception
	 * @Description:
	 */
	public int updateDepartment(Department department) throws Exception;
	
	/**
	 * 通过ids批量修改部门Department
	 * @param ids 如："'1','2','3','4'..."
	 * @param department
	 * @throws Exception
	 * @Description:
	 */
	public int updateDepartmentByIds(String ids,Department department) throws Exception;
	
	/**
	 * 通过ids批量修改部门Department
	 * @param ids 
	 * @param department
	 * @throws Exception
	 * @Description:
	 */
	public int updateDepartmentByIdsList(List<String> ids,Department department) throws Exception;
	
	/**
	 * 通过id批量修改部门Department
	 * @param departments
	 * @throws Exception
	 * @Description:
	 */
	public int updateDepartmentList(List<Department> departments) throws Exception;
	
	//------------api------------
}
