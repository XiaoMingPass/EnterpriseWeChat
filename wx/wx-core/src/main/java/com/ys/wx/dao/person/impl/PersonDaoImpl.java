package com.ys.wx.dao.person.impl;

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
import com.ys.wx.dao.person.IPersonDao;
import com.ys.wx.vo.Person;




/**
 * 人员Dao实现
 * @author CeaserWang
 * @date 2017-08-28 16:45:45
 */
@Repository
public class PersonDaoImpl extends MybatisTemplate implements IPersonDao {

	@Override
	public Person getPersonById(String id) throws Exception {
		return (Person)this.selectOne("PersonXML.getPersonById", id);
	}
	
	@Override
	public List<Person> getPersonByIds(String ids) throws Exception {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ids", ids);
		return this.selectList("PersonXML.getPersonByIds", params);
	}
	
	@Override
	public List<Person> getPersonByIdsList(List<String> ids) throws Exception {
		return this.selectList("PersonXML.getPersonByIdsList", ids);
	}

	@Override
	public List<Person> getAll(Person person) throws Exception {
		return this.selectList("PersonXML.getAll", person);
	}

	@Override
	public PagerModel<Person> getPagerModelByQuery(Person person, Query query)
			throws Exception {
		return this.getPagerModelByQuery(person, query, "PersonXML.getPagerModelByQuery");
	}
	
	@Override
	public int getByPageCount(Person person)throws Exception {
		return this.selectOne("PersonXML.getByPageCount", person);
	}

	@Override
	public void insertPerson(Person person) throws Exception {
		if (null != person) {
			person.setId(UUIDGenerator.generate());
			person.setCreateTime(new Date());
			person.setUpdateTime(new Date());
			this.insert("PersonXML.insertPerson", person);
		}
	}
	
	@Override
	public void insertPersonBatch(List<Person> persons) throws Exception {
		if(CollectionUtils.isNotEmpty(persons)){
			for (Person person : persons) {
				if (null != person) {
					person.setId(UUIDGenerator.generate());
					person.setCreateTime(new Date());
				}
			}
			this.insert("PersonXML.insertPersonBatch", persons);
		}
	}
	
	@Override
	public void insertPersonReplaceBatch(List<Person> persons) throws Exception {
		if(CollectionUtils.isNotEmpty(persons)){
			for (Person person : persons) {
				if (null != person) {
					if(CommUtils.isEmpty(person.getId())){
						person.setId(UUIDGenerator.generate());
					}
					person.setCreateTime(new Date());
				}
			}
			this.insert("PersonXML.insertPersonReplaceBatch", persons);
		}
	}
	
	@Override
	public void delPersonById(String id) throws Exception {
		this.delete("PersonXML.delPersonById", id);
	}
	
	@Override
	public void delPersonByIds(String ids) throws Exception {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ids", ids);
		this.delete("PersonXML.delPersonByIds", params);
	}
	
	@Override
	public void delPersonByIdsList(List<String> ids) throws Exception {
		this.delete("PersonXML.delPersonByIdsList", ids);
	}
	
	@Override
	public int updatePerson(Person person) throws Exception {
		if (null != person) {
			person.setUpdateTime(new Date());
			/**清理不需要更新的数据*/
		    this.cleanWhenUpdate(person);
			return this.update("PersonXML.updatePerson", person);
		} else {
			return 0;
		}
	}
	
	@Override
	public int updatePersonByIds(String ids,Person person) throws Exception {
		ids = StringTools.converString(ids);
		if (StringUtils.isNotBlank(ids) && null != person) {
			person.setUpdateTime(new Date());
			Map<String,Object> params = new HashMap<String, Object>();
			params.put("ids", ids);
			/**清理不需要更新的数据*/
		    this.cleanWhenUpdate(person);
			params.put("person", person);
			return this.update("PersonXML.updatePersonByIds", params);
		} else {
			return 0;
		}
		
	}
	
	@Override
	public int updatePersonByIdsList(List<String> ids,Person person) throws Exception {
		if (CollectionUtils.isNotEmpty(ids) && null != person) {
			person.setUpdateTime(new Date());
			Map<String,Object> params = new HashMap<String, Object>();
			params.put("ids", ids);
			/**清理不需要更新的数据*/
		    this.cleanWhenUpdate(person);
			params.put("person", person);
			return this.update("PersonXML.updatePersonByIdsList", params);
		} else {
			return 0;
		}
	}
	
	@Override
	public int updatePersonList(List<Person> persons) throws Exception {
		if(CollectionUtils.isNotEmpty(persons)){
			for (Person person : persons) {
				if (null != person) {
					person.setUpdateTime(new Date());
					/**清理不需要更新的数据*/
					this.cleanWhenUpdate(person);
				}
			}
			return this.update("PersonXML.updatePersonList", persons);
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
	private void cleanWhenUpdate(Person person) {
		person.setCreateTime(null);
		person.setCreator(null);
	}
	
	//------------api------------
}

