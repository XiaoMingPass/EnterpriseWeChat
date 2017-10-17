package com.ys.wx.service.person.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ys.tools.common.StringTools;
import com.ys.tools.pager.PagerModel;
import com.ys.tools.pager.Query;
import com.ys.ucenter.model.user.Personnel;
import com.ys.wx.constant.WxConstant;
import com.ys.wx.dao.department.IDepartmentDao;
import com.ys.wx.dao.person.IPersonDao;
import com.ys.wx.service.common.ISequenceService;
import com.ys.wx.service.person.IPersonService;
import com.ys.wx.vo.Department;
import com.ys.wx.vo.Person;




/**
 * 人员Service实现
 * @author CeaserWang
 * @date 2017-08-28 16:45:45
 */
@Service
public class PersonServiceImpl implements IPersonService {

	private static Logger logger = LoggerFactory.getLogger(PersonServiceImpl.class);
	
	@Resource
	private ISequenceService sequenceService;
	
	@Resource
	private IPersonDao personDao;

	@Resource
	private IDepartmentDao departmentDao;
	
	@Override
	public Person getPersonById(String id) throws Exception {
		return StringUtils.isNotBlank(id) ? this.personDao.getPersonById(id.trim()) : null;
	}

	@Override
	public List<Person> getPersonByIds(String ids) throws Exception {
		ids = StringTools.converString(ids);
		return StringUtils.isNotBlank(ids) ? this.personDao.getPersonByIds(ids) : null;
	}
	
	@Override
	public List<Person> getPersonByIdsList(List<String> ids) throws Exception {
		return CollectionUtils.isNotEmpty(ids) ? this.personDao.getPersonByIdsList(ids) : null;
	}

	@Override
	public List<Person> getAll(Person person) throws Exception {
		return null != person ? this.personDao.getAll(person) : null;
	}

	@Override
	public PagerModel<Person> getPagerModelByQuery(Person person, Query query)
			throws Exception {
		return (null != person && null != query) ? this.personDao.getPagerModelByQuery(person, query) : null;
	}
	
	@Override
	public int getByPageCount(Person person)throws Exception {
		return (null != person) ? this.personDao.getByPageCount(person) : 0;
	}

	@Override
	public void insertPerson(Person person) throws Exception {
		this.personDao.insertPerson(person);
	}
	
	@Override
	public void insertPersonReplaceBatch(List<Person> persons) throws Exception {
		this.personDao.insertPersonReplaceBatch(persons);
	}
	
	@Override
	public void insertPersonBatch(List<Person> persons) throws Exception {
		this.personDao.insertPersonBatch(persons);
	}
	
	@Override
	public void delPersonById(String id) throws Exception {
		if (StringUtils.isNotBlank(id)) {
			this.personDao.delPersonById(id.trim());
		}
	}
	
	@Override
	public void delPersonByIds(String ids) throws Exception {
		ids = StringTools.converString(ids);
		if(StringUtils.isNotBlank(ids)){
			this.personDao.delPersonByIds(ids);
		}
	}
	
	@Override
	public void delPersonByIdsList(List<String> ids) throws Exception {
		if(CollectionUtils.isNotEmpty(ids)){
			this.personDao.delPersonByIdsList(ids);
		}
	}

	@Override
	public int updatePerson(Person person) throws Exception {
		return this.personDao.updatePerson(person);
	}
	
	@Override
	public int updatePersonByIds(String ids,Person person) throws Exception {
		return this.personDao.updatePersonByIds(ids, person);
	}
	
	@Override
	public int updatePersonByIdsList(List<String> ids,Person person) throws Exception {
		return this.personDao.updatePersonByIdsList(ids, person);
	}
	
	@Override
	public int updatePersonList(List<Person> persons) throws Exception {
		return this.personDao.updatePersonList(persons);
	}

	/**
	 * 姓名、身份证、手机、邮箱、部门、公司、工号是否发生变化
	 * @param source
	 * @param person
	 * @return
	 */
	private boolean isModified(Personnel source,Person person){
		boolean rflag = true;
		if(!source.getName().equals(person.getName()) 
				|| (null!=source.getIdcard() && null!=person.getIdcard() && !source.getIdcard().equals(person.getIdcard()))
				|| (null!=source.getSelfMobile() && null!=person.getMobile() && !source.getSelfMobile().equals(person.getMobile()))
				|| (null!=source.getSelfEmail() && null!=person.getEmail() && !source.getSelfEmail().equals(person.getEmail()))
				|| (null!=source.getDeptId() && null!=person.getDeptId() && !source.getDeptId().equals(person.getDeptId()))
				|| (null!=source.getCompanyId() && null!=person.getCompanyId() && !source.getCompanyId().equals(person.getCompanyId()))
				|| (null!=source.getNo() && null!=person.getNo() && !source.getNo().equals(person.getNo()))
				|| (null!=source.getDelFlag() && null!=person.getDelFlag() && !source.getDelFlag().equals(person.getDelFlag()))
				){
			rflag = true;
		}
		return rflag;
	}
	
	@Override
	public void process(List<Personnel> personnelList) {
		logger.info("PersonServiceImpl-process start :>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		try {
			Person qp = new Person();
			qp.setDelFlag(null);
			List<Person>  allPersons =  getAll(qp);
			Map<String,Person> allPersonsMaps = new  HashMap<String, Person>();
			if(CollectionUtils.isNotEmpty(allPersons)){
				for (Person p : allPersons) {
					allPersonsMaps.put(p.getNo().trim(), p);
				}
			}
			
			Department department = new Department();
			department.setDelFlag(null);
			List<Department> departments = departmentDao.getAll(department);
			Map<String,String> deptIdToCodeMaps = new  HashMap<String, String>();
			if(CollectionUtils.isNotEmpty(departments)){
				for (Department department2 : departments) {
					deptIdToCodeMaps.put(department2.getId(), department2.getCode());
				}
			}
			
			if(CollectionUtils.isNotEmpty(personnelList)){
				logger.info("接受队列人员数量:"+personnelList.size());
				List<Person> savePersons = new ArrayList<Person>();
				Person tempPerson = null;
				for (Personnel tPerson : personnelList) {
					if(!(tPerson.getNo().startsWith("0") || tPerson.getNo().startsWith("9"))/*
							||  (tPerson.getStatus().intValue()!=1 && tPerson.getPoststatus().intValue()!=1)
							|| 1!=tPerson.getDelFlag().intValue()*/){
						logger.info("人员："+tPerson.getName()+" , no : "+tPerson.getNo()+"被过滤掉（0和9开头的被过滤。）");
						continue;
					}
					//判断修改
					if(allPersonsMaps.containsKey(tPerson.getNo().trim())){
						Person lPerson =  allPersonsMaps.get(tPerson.getNo());
						if(isModified(tPerson,lPerson)){
							logger.info("当前人员发生修改:"+tPerson);
							
							lPerson.setId(lPerson.getId());
							lPerson.setName(tPerson.getName());
							lPerson.setIdcard(tPerson.getIdcard());
							lPerson.setSex(tPerson.getSex()+"");
							lPerson.setWeixin("");
							lPerson.setMobile(tPerson.getSelfMobile());
							lPerson.setEmail(tPerson.getCompanyEmail());
							lPerson.setCompanyId(tPerson.getCompanyId());
							lPerson.setDeptId(tPerson.getDeptId());
							lPerson.setDeptCode(deptIdToCodeMaps.get(tPerson.getDeptId()));
							lPerson.setPosition(tPerson.getPosition());
							lPerson.setOfficeAddr("");
							lPerson.setFullName(tPerson.getAlphabetic());
							lPerson.setShortName("");
							lPerson.setIsVisable("");
							lPerson.setPhone(tPerson.getPhone());
							lPerson.setShortPhone("");
							lPerson.setNo(tPerson.getNo());
							lPerson.setShortMobile(tPerson.getShortPhone());
							lPerson.setSyn(WxConstant.MODYFIED);
							Integer delFlag = 1;
							if( ((null!=tPerson.getStatus() && tPerson.getStatus().intValue()==0) 
									&& (null!=tPerson.getPoststatus() && tPerson.getPoststatus().intValue()==0))
									||(null!=tPerson.getDelFlag() && tPerson.getDelFlag().intValue()==0) ){
								delFlag = 0;
							}
							lPerson.setDelFlag(delFlag);
							lPerson.setUpdateTime(new Date());
							savePersons.add(lPerson);
						}else{
							logger.info("当前人员未发生修改:"+tPerson);
							//没有修改，跳出
							continue;
						}
					}else{
						logger.info("当前人员发生新增:"+tPerson);
						tempPerson = new Person();
						tempPerson.setId(tPerson.getId());
						tempPerson.setName(tPerson.getName());
						tempPerson.setIdcard(tPerson.getIdcard());
						tempPerson.setSex(tPerson.getSex()+"");
						tempPerson.setWeixin("");
						tempPerson.setMobile(tPerson.getSelfMobile());
						tempPerson.setEmail(tPerson.getCompanyEmail());
						tempPerson.setCompanyId(tPerson.getCompanyId());
						tempPerson.setDeptId(tPerson.getDeptId());
						tempPerson.setDeptCode(deptIdToCodeMaps.get(tPerson.getDeptId()));
						tempPerson.setPosition(tPerson.getPosition());
						tempPerson.setOfficeAddr("");
						tempPerson.setFullName(tPerson.getAlphabetic());
						tempPerson.setShortName("");
						tempPerson.setIsVisable("");
						tempPerson.setPhone(tPerson.getPhone());
						tempPerson.setShortPhone("");
						tempPerson.setNo(tPerson.getNo());
						tempPerson.setShortMobile(tPerson.getShortPhone());
						tempPerson.setSyn(WxConstant.MODYFIED);
						Integer delFlag = 1;
						if( ((null!=tPerson.getStatus() && tPerson.getStatus().intValue()==0) 
								&& (null!=tPerson.getPoststatus() && tPerson.getPoststatus().intValue()==0))
								||(null!=tPerson.getDelFlag() && tPerson.getDelFlag().intValue()==0) ){
							delFlag = 0;
						}
						tempPerson.setDelFlag(delFlag);
						tempPerson.setCreateTime(new Date());
						savePersons.add(tempPerson);
					}
				}
				insertPersonReplaceBatch(savePersons);
			}
		} catch (Exception e) {
			logger.error("PersonServiceImpl-process"+e);
			e.printStackTrace();
		}
		logger.info("PersonServiceImpl-process end :>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
	}
	
	
	//------------api------------
	
}

