package com.ys.wx.dao.company.impl;

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
import com.ys.wx.dao.company.ICompanyDao;
import com.ys.wx.vo.Company;




/**
 * 公司Dao实现
 * @author CeaserWang
 * @date 2017-08-28 16:43:59
 */
@Repository
public class CompanyDaoImpl extends MybatisTemplate implements ICompanyDao {

	@Override
	public Company getCompanyById(String id) throws Exception {
		return (Company)this.selectOne("CompanyXML.getCompanyById", id);
	}
	
	@Override
	public List<Company> getCompanyByIds(String ids) throws Exception {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ids", ids);
		return this.selectList("CompanyXML.getCompanyByIds", params);
	}
	
	@Override
	public List<Company> getCompanyByIdsList(List<String> ids) throws Exception {
		return this.selectList("CompanyXML.getCompanyByIdsList", ids);
	}

	@Override
	public List<Company> getAll(Company company) throws Exception {
		return this.selectList("CompanyXML.getAll", company);
	}

	@Override
	public PagerModel<Company> getPagerModelByQuery(Company company, Query query)
			throws Exception {
		return this.getPagerModelByQuery(company, query, "CompanyXML.getPagerModelByQuery");
	}
	
	@Override
	public int getByPageCount(Company company)throws Exception {
		return this.selectOne("CompanyXML.getByPageCount", company);
	}

	@Override
	public void insertCompany(Company company) throws Exception {
		if (null != company) {
			company.setId(UUIDGenerator.generate());
			company.setCreateTime(new Date());
			company.setUpdateTime(new Date());
			this.insert("CompanyXML.insertCompany", company);
		}
	}
	
	@Override
	public void insertCompanyBatch(List<Company> companys) throws Exception {
		if(CollectionUtils.isNotEmpty(companys)){
			for (Company company : companys) {
				if (null != company) {
					company.setId(UUIDGenerator.generate());
					company.setCreateTime(new Date());
				}
			}
			this.insert("CompanyXML.insertCompanyBatch", companys);
		}
	}
	
	@Override
	public void insertCompanyReplaceBatch(List<Company> companys) throws Exception {
		if(CollectionUtils.isNotEmpty(companys)){
			for (Company company : companys) {
				if (null != company) {
					if(CommUtils.isEmail(company.getId())){
						company.setId(UUIDGenerator.generate());
					}
				}
			}
			this.insert("CompanyXML.insertCompanyReplaceBatch", companys);
		}
	}
	
	@Override
	public void delCompanyById(String id) throws Exception {
		this.delete("CompanyXML.delCompanyById", id);
	}
	
	@Override
	public void delCompanyByIds(String ids) throws Exception {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ids", ids);
		this.delete("CompanyXML.delCompanyByIds", params);
	}
	
	@Override
	public void delCompanyByIdsList(List<String> ids) throws Exception {
		this.delete("CompanyXML.delCompanyByIdsList", ids);
	}
	
	@Override
	public int updateCompany(Company company) throws Exception {
		if (null != company) {
			company.setUpdateTime(new Date());
			/**清理不需要更新的数据*/
		    this.cleanWhenUpdate(company);
			return this.update("CompanyXML.updateCompany", company);
		} else {
			return 0;
		}
	}
	
	@Override
	public int updateCompanyByIds(String ids,Company company) throws Exception {
		ids = StringTools.converString(ids);
		if (StringUtils.isNotBlank(ids) && null != company) {
			company.setUpdateTime(new Date());
			Map<String,Object> params = new HashMap<String, Object>();
			params.put("ids", ids);
			/**清理不需要更新的数据*/
		    this.cleanWhenUpdate(company);
			params.put("company", company);
			return this.update("CompanyXML.updateCompanyByIds", params);
		} else {
			return 0;
		}
		
	}
	
	@Override
	public int updateCompanyByIdsList(List<String> ids,Company company) throws Exception {
		if (CollectionUtils.isNotEmpty(ids) && null != company) {
			company.setUpdateTime(new Date());
			Map<String,Object> params = new HashMap<String, Object>();
			params.put("ids", ids);
			/**清理不需要更新的数据*/
		    this.cleanWhenUpdate(company);
			params.put("company", company);
			return this.update("CompanyXML.updateCompanyByIdsList", params);
		} else {
			return 0;
		}
	}
	
	@Override
	public int updateCompanyList(List<Company> companys) throws Exception {
		if(CollectionUtils.isNotEmpty(companys)){
			for (Company company : companys) {
				if (null != company) {
					company.setUpdateTime(new Date());
					/**清理不需要更新的数据*/
					this.cleanWhenUpdate(company);
				}
			}
			return this.update("CompanyXML.updateCompanyList", companys);
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
	private void cleanWhenUpdate(Company company) {
		company.setCreateTime(null);
		company.setCreator(null);
	}
	
	
	
	
	//------------api------------
}

