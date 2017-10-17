package com.ys.wx.listener;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ys.ucenter.enms.SynEhrEnum;
import com.ys.ucenter.model.company.Company;
import com.ys.ucenter.model.user.Department;
import com.ys.ucenter.model.user.Personnel;
import com.ys.ucenter.vo.mq.SynEhrVo;
import com.ys.wx.service.company.ICompanyService;
import com.ys.wx.service.department.IDepartmentService;
import com.ys.wx.service.person.IPersonService;

/**
 * 微信端队列监听器
 * @author wangzequan
 *
 */

@Component
public class WeChatListener {
	private static Logger logger = LoggerFactory.getLogger(WeChatListener.class);
	
	@Resource
	private ICompanyService companyService;
	@Resource
	private IDepartmentService departmentService;
	@Resource
	private IPersonService personService;
	
   public void receiveMessage(String jsonvo) throws InterruptedException {
		try {
			logger.info("WeChatListener-receiveMessage start : >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			if(null!=jsonvo){
				logger.info("WeChatListener-receiveMessage-jsonvo:"+jsonvo);
				SynEhrVo<JSONObject>  synEhrVo = (SynEhrVo<JSONObject>)JSON.parseObject(jsonvo, SynEhrVo.class);
				List<JSONObject> list = synEhrVo.getDatas();
				if(CollectionUtils.isNotEmpty(list)){
					//公司类型消息
					if(SynEhrEnum.COMPANY.getSn().equals(synEhrVo.getSynEhr().getSn())){
						List<Company>  companyList = convertDataToOther(list,Company.class);
						companyService.process(companyList);
						//部门类型消息
					}else if(SynEhrEnum.DEPARTMENT.getSn().equals(synEhrVo.getSynEhr().getSn())){
						List<Department>  departmentList = convertDataToOther(list,Department.class);
						departmentService.process(departmentList);
						//人员类型消息
					}else if(SynEhrEnum.PERSONAL.getSn().equals(synEhrVo.getSynEhr().getSn())){
						List<Personnel>  personnelList = convertDataToOther(list,Personnel.class);
						personService.process(personnelList);
					}
				}
			
			}
		} catch (Exception e) {
			logger.error("WeChatListener-receive:"+e);
			e.printStackTrace();
		}
		logger.info("WeChatListener-receiveMessage end : >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
   }
   
   
	/**
	 * @param list
	 * @throws Exception
	 * @Description:
	 * @author wangzequan 2017 下午7:03:41
	 * @param <T>
	 */
		
	private <T> List<T>  convertDataToOther(List<JSONObject> list,Class<T> iclass) throws Exception {
		List<T> resultList = new  ArrayList<T>();
		for (JSONObject jsONObject : list) {
			T bean = JSON.parseObject(jsONObject.toString(), iclass) ;
			resultList.add(bean);
		}
		return resultList;
	}
   
}
