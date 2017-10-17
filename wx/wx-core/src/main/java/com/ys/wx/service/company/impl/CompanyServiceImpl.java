package com.ys.wx.service.company.impl;

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
import com.ys.ucenter.constant.UcenterConstant;
import com.ys.ucenter.utils.CommUtils;
import com.ys.wx.constant.WxConstant;
import com.ys.wx.dao.company.ICompanyDao;
import com.ys.wx.service.common.ISequenceService;
import com.ys.wx.service.company.ICompanyService;
import com.ys.wx.vo.Company;




/**
 * 公司Service实现
 * @author CeaserWang
 * @date 2017-08-28 16:43:59
 */
@Service
public class CompanyServiceImpl implements ICompanyService {

	private static Logger logger = LoggerFactory.getLogger(CompanyServiceImpl.class);
	
	@Resource
	private ISequenceService sequenceService;
	
	@Resource
	private ICompanyDao companyDao;

	@Override
	public Company getCompanyById(String id) throws Exception {
		return StringUtils.isNotBlank(id) ? this.companyDao.getCompanyById(id.trim()) : null;
	}

	@Override
	public List<Company> getCompanyByIds(String ids) throws Exception {
		ids = StringTools.converString(ids);
		return StringUtils.isNotBlank(ids) ? this.companyDao.getCompanyByIds(ids) : null;
	}
	
	@Override
	public List<Company> getCompanyByIdsList(List<String> ids) throws Exception {
		return CollectionUtils.isNotEmpty(ids) ? this.companyDao.getCompanyByIdsList(ids) : null;
	}

	@Override
	public List<Company> getAll(Company company) throws Exception {
		return null != company ? this.companyDao.getAll(company) : null;
	}

	@Override
	public PagerModel<Company> getPagerModelByQuery(Company company, Query query)
			throws Exception {
		return (null != company && null != query) ? this.companyDao.getPagerModelByQuery(company, query) : null;
	}
	
	@Override
	public int getByPageCount(Company company)throws Exception {
		return (null != company) ? this.companyDao.getByPageCount(company) : 0;
	}

	@Override
	public void insertCompany(Company company) throws Exception {
		this.companyDao.insertCompany(company);
	}
	
	@Override
	public void insertCompanyBatch(List<Company> companys) throws Exception {
		this.companyDao.insertCompanyBatch(companys);
	}
	
	@Override
	public void insertCompanyReplaceBatch(List<Company> companys) throws Exception{
		this.companyDao.insertCompanyReplaceBatch(companys);
	}
	
	@Override
	public void delCompanyById(String id) throws Exception {
		if (StringUtils.isNotBlank(id)) {
			this.companyDao.delCompanyById(id.trim());
		}
	}
	
	@Override
	public void delCompanyByIds(String ids) throws Exception {
		ids = StringTools.converString(ids);
		if(StringUtils.isNotBlank(ids)){
			this.companyDao.delCompanyByIds(ids);
		}
	}
	
	@Override
	public void delCompanyByIdsList(List<String> ids) throws Exception {
		if(CollectionUtils.isNotEmpty(ids)){
			this.companyDao.delCompanyByIdsList(ids);
		}
	}

	@Override
	public int updateCompany(Company company) throws Exception {
		return this.companyDao.updateCompany(company);
	}
	
	@Override
	public int updateCompanyByIds(String ids,Company company) throws Exception {
		return this.companyDao.updateCompanyByIds(ids, company);
	}
	
	@Override
	public int updateCompanyByIdsList(List<String> ids,Company company) throws Exception {
		return this.companyDao.updateCompanyByIdsList(ids, company);
	}
	
	@Override
	public int updateCompanyList(List<Company> companys) throws Exception {
		return this.companyDao.updateCompanyList(companys);
	}

	
	private boolean isModified(com.ys.ucenter.model.company.Company source,Company company){
		boolean rflag = false;
		if(!source.getPid().equals(company.getPid()) || !source.getName().equals(company.getName()) ||
				((null!=source.getDelFlag() && null!=company.getDelFlag()) && source.getDelFlag().intValue()!=company.getDelFlag().intValue())){
			rflag = true;
		}
		return rflag;
	}
	
	@Override
	public void process(List<com.ys.ucenter.model.company.Company> companyList) {
		logger.info("CompanyServiceImpl-process start :>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		try {
			Company qc = new Company();
			qc.setDelFlag(null);
			List<Company>  allComapnys =  getAll(qc);
			Map<String,Company> allComapnysMaps = new  HashMap<String, Company>();
			if(CollectionUtils.isNotEmpty(allComapnys)){
				for (Company company : allComapnys) {
					allComapnysMaps.put(company.getId(), company);
				}
			}
			
			if(CollectionUtils.isNotEmpty(companyList)){
				logger.info("获得队列公司数量:"+companyList.size());
				Map<String,String> globalIdToCode = new  HashMap<String, String>();
				List<Company> saveCompanys = new ArrayList<Company>();
				Company tempCompany = null;
				for (com.ys.ucenter.model.company.Company company : companyList) {
					
					if(UcenterConstant.NO_DELETE_FLAG!=company.getDelFlag().intValue() 
							||  !UcenterConstant.EHR_IS_COM.equals(company.getOrgtype())
							||  1!=company.getStatus()){
						continue;
					}
					
					tempCompany = new Company();
					//判断修改
					if(allComapnysMaps.containsKey(company.getId())){
						Company lcompany =  allComapnysMaps.get(company.getId());
						if(isModified(company,lcompany)){
							logger.info("当前处理发生修改:"+company);
							tempCompany.setId(company.getId());
							tempCompany.setPid(company.getPid());
							tempCompany.setName(company.getName());
							tempCompany.setDelFlag(company.getDelFlag());
							tempCompany.setCode(lcompany.getCode());
							tempCompany.setPcode(lcompany.getPcode());
							tempCompany.setUpdateTime(new Date());
							tempCompany.setSyn(WxConstant.MODYFIED);
							tempCompany.setOrderNo("");
						}else{
							logger.info("当前处理未发生修改:"+company);
							globalIdToCode.put(lcompany.getId(), lcompany.getCode());
							//没有修改，跳出
							continue;
						}
					}else{
						logger.info("当前处理发生新增:"+company);
						tempCompany.setId(company.getId());
						tempCompany.setPid(CommUtils.defaultIfEmpty(company.getPid(), "") );
						tempCompany.setCode(sequenceService.next(WxConstant.GLOBAL));
						//tempCompany.setPcode(pcode);
						tempCompany.setName(company.getName());
						tempCompany.setCreateTime(new Date());
						tempCompany.setUpdateTime(null);
						tempCompany.setOrderNo("");
						tempCompany.setDelFlag(company.getDelFlag());
						tempCompany.setSyn(WxConstant.MODYFIED);
					}
					globalIdToCode.put(tempCompany.getId(), tempCompany.getCode());
					saveCompanys.add(tempCompany);
				}
				//矫正pcode
				for (Company company : saveCompanys) {
					String currentCodeMap = globalIdToCode.get(company.getPid());
					if(CommUtils.isEmpty(currentCodeMap)){
						Company com = allComapnysMaps.get(company.getPid());
						if(null!=com){
							currentCodeMap = com.getCode();
						}
					}
					company.setPcode(currentCodeMap);
				}
				
				insertCompanyReplaceBatch(saveCompanys);
			}
		} catch (Exception e) {
			logger.error("CompanyServiceImpl-process"+e);
			e.printStackTrace();
		}
		logger.info("CompanyServiceImpl-process end :>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
	}
	
	//------------api------------
	
}

