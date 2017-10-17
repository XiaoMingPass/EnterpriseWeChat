package com.ys.wx.dao.person;

import java.util.List;

import com.ys.tools.pager.PagerModel;
import com.ys.tools.pager.Query;
import com.ys.wx.vo.Person;



/**
 * 人员Dao接口
 * @author CeaserWang
 * @date 2017-08-28 16:45:45
 */
public interface IPersonDao {

	/**
	 * 通过id得到人员Person
	 * @param id
	 * @return 
	 * @throws Exception
	 * @Description:
	 */
	public Person getPersonById(String id) throws Exception;

	/**
	 * 通过ids批量得到人员Person
	 * @param ids 如："'1','2','3','4'..."
	 * @return 
	 * @throws Exception
	 * @Description:
	 */
	public List<Person> getPersonByIds(String ids) throws Exception;
	
	/**
	 * 通过ids批量得到人员Person
	 * @param ids 
	 * @return 
	 * @throws Exception
	 * @Description:
	 */
	public List<Person> getPersonByIdsList(List<String> ids) throws Exception;

	/**
	 * 得到所有人员Person
	 * @param person
	 * @return 
	 * @throws Exception
	 * @Description:
	 */
	public List<Person> getAll(Person person) throws Exception;

	/**
	 * 分页查询人员Person
	 * @param person
	 * @param query
	 * @return
	 * @throws Exception
	 * @Description:
	 */
	public PagerModel<Person> getPagerModelByQuery(Person person, Query query) throws Exception;
	
	/**
	 * 查询记录数
	 * @param person
	 * @return
	 * @throws Exception
	 * @Description:
	 */
	public int getByPageCount(Person person)throws Exception ;

	/**
	 * 添加人员Person
	 * @param person
	 * @throws Exception
	 * @Description:
	 */
	public void insertPerson(Person person) throws Exception;
	
	/**
	 * 有则改之，无则加勉
	 * @param persons
	 * @throws Exception
	 */
	public void insertPersonReplaceBatch(List<Person> persons) throws Exception;
	
	/**
	 * 批量添加人员Person
	 * @param persons
	 * @throws Exception
	 * @Description:
	 */
	public void insertPersonBatch(List<Person> persons) throws Exception;

	/**
	 * 通过id删除人员Person
	 * @param id
	 * @throws Exception
	 * @Description:
	 */
	public void delPersonById(String id) throws Exception;
	
	/**
	 * 通过id批量删除人员Person
	 * @param ids 如："'1','2','3','4'..."
	 * @throws Exception
	 * @Description:
	 */
	public void delPersonByIds(String ids) throws Exception;
	
	/**
	 * 通过id批量删除人员Person
	 * @param ids 
	 * @throws Exception
	 * @Description:
	 */
	public void delPersonByIdsList(List<String> ids) throws Exception;

	/**
	 * 通过id修改人员Person
	 * @param person
	 * @throws Exception
	 * @Description:
	 */
	public int updatePerson(Person person) throws Exception;
	
	/**
	 * 通过ids批量修改人员Person
	 * @param ids 如："'1','2','3','4'..."
	 * @param person
	 * @throws Exception
	 * @Description:
	 */
	public int updatePersonByIds(String ids,Person person) throws Exception;
	
	/**
	 * 通过ids批量修改人员Person
	 * @param ids 
	 * @param person
	 * @throws Exception
	 * @Description:
	 */
	public int updatePersonByIdsList(List<String> ids,Person person) throws Exception;
	
	/**
	 * 通过id批量修改人员Person
	 * @param persons
	 * @throws Exception
	 * @Description:
	 */
	public int updatePersonList(List<Person> persons) throws Exception;
	
	//------------api------------
}
