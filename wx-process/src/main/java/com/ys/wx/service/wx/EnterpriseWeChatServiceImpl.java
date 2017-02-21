package com.ys.wx.service.wx;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ys.wx.function.DataToCSVFile;
import com.ys.wx.model.common.SynDataRecord;
import com.ys.wx.model.dept.DepartmentForImport;
import com.ys.wx.model.person.PersonalForImport;
import com.ys.wx.model.wx.response.PartyListResponse;
import com.ys.wx.model.wx.response.PartyListResponse.Department;
import com.ys.wx.model.wx.response.UserDetailsResponse.UserlistBean;
import com.ys.wx.model.wx.response.UserDetailsResponse.UserlistBean.ExtattrBean.AttrsBean;
import com.ys.wx.service.common.ISynDataRecordService;
import com.ys.wx.service.dept.IDeptService;
import com.ys.wx.service.person.IPersonService;
import com.ys.wx.utils.CommUtils;
import com.ys.wx.vo.ReturnVo;

@Service
public class EnterpriseWeChatServiceImpl  implements IEnterpriseWeChatService {

    private static final Logger logger = Logger.getLogger(EnterpriseWeChatServiceImpl.class);
    @Resource
    IWxServiceClient wxServiceClient;
    @Resource
	DataToCSVFile dataToCSVFile;
    @Resource
    IPersonService   personService;
    @Resource
    IDeptService     deptService;
    @Resource
    ISynDataRecordService synDataRecordService;

	/**
	 * 日常数据的增量更新
	 */
	public ReturnVo<String> processDataToWeiXin() {
		ReturnVo<String> rvo = new ReturnVo<String>(0,"同步EHR数据到微信失败");
		try {
			//dataToCSVFile.preCleanFiles();
	 		String token =  wxServiceClient.getToken();
			ReturnVo<List<DepartmentForImport>>  rHRData = deptService.getEHRDeptData();
			//计算新老数据的交集做更新，新数据没有在老数据中找到的做添加
			if(null!=rHRData && CollectionUtils.isNotEmpty(rHRData.getData())){
				List<DepartmentForImport> fireDepartmentForImports = rHRData.getData();
				ArrayList<Department> weixindepts = wxServiceClient.getParty(token, 1);
				Map<Integer,Department> weixindeptsMap = new HashMap<Integer,Department>();
				for (Department department : weixindepts) {
					weixindeptsMap.put(department.id, department);
				}
				List<Department> updatelist = new ArrayList<Department>();
				List<Department> addlist = new ArrayList<Department>();
				for (DepartmentForImport departmentitem : fireDepartmentForImports) {
					Department department = weixindeptsMap.get(Integer.valueOf(departmentitem.getDept_id()));
					//匹配字段发生变化的部门
					if(null!=department){
						boolean flag = false;
						if(!departmentitem.getDept_name().equals(department.name)){
							department.name = departmentitem.getDept_name();
							flag = true;
						}
						if(CommUtils.isNotEmpty(departmentitem.getDept_fid()) && !departmentitem.getDept_fid().equals(department.parentid+"")){
							department.parentid = Integer.valueOf(departmentitem.getDept_fid());
							flag = true;
						}
						if(CommUtils.isNotEmpty(departmentitem.getDept_order()) && !departmentitem.getDept_order().equals(department.order+"")){
							department.order = Integer.valueOf(departmentitem.getDept_order()) ;
							flag = true;
						}
						if(flag){
							updatelist.add(department);
						}
					}else{
							addlist.add(department);
					}
				}
				//调用单个API执行更新
				for (Department department : updatelist) {
					logger.info("update department : >>> "+department);
					wxServiceClient.updateOneParty(token, department);
				}
				//调用单个API执行创建
				for (Department department : addlist) {
					logger.info("add department : >>> "+department);
					wxServiceClient.createParty(token, department);
				}
				String maxEndTime = startRecordOperation();
				ReturnVo<List<PersonalForImport>> pvo = personService.getEHRPersonalDataByTime(maxEndTime);
				if(CollectionUtils.isNotEmpty(pvo.getData())){
					//上传文件(批量)
					File file = dataToCSVFile.PersonDataToCSV(pvo);
        			wxServiceClient.uploadFileSysUser(file);
        			//记录时间戳，为下次同步使用
        			endRecordOperation(maxEndTime,1);
				}
			}
			rvo.setCode(1);
			rvo.setData("同步EHR数据到微信成功");
			rvo.setMsg("同步EHR数据到微信成功");
		} catch (Exception e) {
			logger.error("EnterpriseWeChatServiceImpl-processDataToWeiXin"+e);
			rvo.setMsg("EnterpriseWeChatServiceImpl-processDataToWeiXin"+e);
			e.printStackTrace();
		} 
		return rvo;
	}
	
	/**
	 * 得到开始时间戳
	 * @return
	 * @throws Exception
	 * @Description:
	 * @author wangzequan 2017 上午11:40:28
	 */
	private String startRecordOperation() throws Exception {
		String maxEndTime = "1970-01-01 00:00:00";
		SynDataRecord qsynDataRecord = new SynDataRecord();
		qsynDataRecord.setType(1);
		int existcount = synDataRecordService.getByPageCount(qsynDataRecord);
		if (existcount != 0) {
			SynDataRecord synDataRecord = new SynDataRecord();
			synDataRecord.setType(1);
			maxEndTime = synDataRecordService.getSynDataRecordMaxEndTime(synDataRecord);
		}
		return maxEndTime;
	}


	
	/**获取结束时间戳
	 * type : 1:person;
	 * @param maxEndTime
	 * @throws Exception
	 * @Description:
	 * @author wangzequan 2017 下午12:48:07
	 */
		
	private void endRecordOperation(String maxEndTime,int type) throws Exception {
		SynDataRecord synDataRecord = new SynDataRecord();
		synDataRecord.setType(type);
		synDataRecord.setStartTime(CommUtils.parseStrToLongDate(maxEndTime));
		synDataRecord.setEndTime(new Date());
		synDataRecord.setOperator("ceaser");
		synDataRecord.setOperateTime(new Date());
		synDataRecordService.insertSynDataRecord(synDataRecord);
	}

    /**
     * 第一次切换环境使用（不对外部服务使用）
     */
	public void processChangeEnvironment() {
		//新数据和老数据匹配到的额
    	BufferedWriter matchedfw = null;
    	//新数据和老数据未匹配到的
    	BufferedWriter unmatchedfw = null;
    	//BufferedWriter ghostfw = null;
    	//BufferedWriter upFailfw = null;
    	try {
    		matchedfw = new BufferedWriter(new FileWriter(new File("f://Out//workspace//matched.txt")) ) ;
    		unmatchedfw = new BufferedWriter (new FileWriter(new File("f://Out//workspace//unmatched.txt")));
    		//ghostfw = new BufferedWriter (new FileWriter(new File("f://Out//workspace//ghostuser.txt")));
    		//upFailfw = new BufferedWriter (new FileWriter(new File("f://Out//workspace//updateFaileduser.txt")));
 		   //1.create  temp dept 创建临时部门
 		   Department department = new PartyListResponse().new Department();
 		   department.id=99999;
 		   department.name="临时部门";
 		   department.parentid = 1;
 		   department.order=0;
 		   
 		  DepartmentForImport tempdept = new DepartmentForImport();
 		  tempdept.setDept_fid(department.parentid+"");
 		  tempdept.setDept_name(department.name);
 		  tempdept.setDept_id(department.id+"");
 		  tempdept.setDept_order(department.order+"");
 		  
 		  String token =  wxServiceClient.getToken();
 		   //首先是创建 如果失败则执行更新操作
 		   if(wxServiceClient.createParty(token, department) || wxServiceClient.updateOneParty(token, department)){
 			   Thread.sleep(5000);
 			   //1.1.把所有人员放到临时部门下边
 			   List<UserlistBean> userlist = wxServiceClient.getUserDetails("",1);
 			   if(CollectionUtils.isNotEmpty(userlist)){
 				   ReturnVo<List<PersonalForImport>> pVo = new  ReturnVo<List<PersonalForImport>>(0,"失败");
 				   List<PersonalForImport> personalForImports = new ArrayList<PersonalForImport>();
 				   PersonalForImport personalforimport = null;
 				   for (UserlistBean userlistbean : userlist) {
 					   personalforimport = new PersonalForImport();
 					   personalforimport.setPsn_name(userlistbean.name);//姓名
 					   personalforimport.setPsn_id(userlistbean.userid); //帐号	
 					   personalforimport.setPsn_mobile(userlistbean.mobile); //手机号
 					   personalforimport.setPsn_email(userlistbean.email);  //邮箱
 					   personalforimport.setPsn_deptcode( department.id+"");//所在部门 【将所有人员放到临时部门下边】
 					   personalforimport.setPsn_postname(userlistbean.position); //职位
 					   personalforimport.setWeixinid(userlistbean.weixinid); // 微信号	 	
 					   if(null!=userlistbean.extattr && null!=userlistbean.extattr.attrs && userlistbean.extattr.attrs.size()>0){
 						   List<AttrsBean>  attrsBean =  userlistbean.extattr.attrs;
 						   for (AttrsBean attrsBeanItem : attrsBean) {
 							   if("所在办公地".equals(attrsBeanItem.name)){
 								   personalforimport.setLocaloffice(attrsBeanItem.value);
 							   }else if("全称".equals(attrsBeanItem.name)){
 								   personalforimport.setFullName(attrsBeanItem.value);
 							   }else if("简称".equals(attrsBeanItem.name)){
 								   personalforimport.setShortName(attrsBeanItem.value);
 							   }else if("排序号".equals(attrsBeanItem.name)){
 								   if(CommUtils.isNotEmpty(attrsBeanItem.value)){
 									   personalforimport.setOrderNo(Integer.valueOf(attrsBeanItem.value) );
 								   }
 							   }
 							   else if("是否可见".equals(attrsBeanItem.name)){
 								   personalforimport.setIsvisible(attrsBeanItem.value);
 							   }
 							   else if("固定电话".equals(attrsBeanItem.name)){
 								   personalforimport.setPhone(attrsBeanItem.value);
 							   }
 							   else if("座机短号".equals(attrsBeanItem.name)){
 								   personalforimport.setPhoneShort(attrsBeanItem.value);
 							   }
 							   else if("工号".equals(attrsBeanItem.name)){
 								   personalforimport.setPsn_code(attrsBeanItem.value);
 							   }
 							   else if("手机短号".equals(attrsBeanItem.name)){
 								   personalforimport.setPhoneShort(attrsBeanItem.value);
 							   }
 						   }
 					   }
 					   personalForImports.add(personalforimport);
 				   }
 				   //执行修改
 				   pVo.setData(personalForImports);
 				   File file =  dataToCSVFile.PersonDataToCSV(pVo);
 				   wxServiceClient.uploadFile(file);
 				   Thread.sleep(90000);
 			   }
 			   //2 删除老的组织(临时组织除外)
 			    deleteTestEnvironmentOrganizationData(department.id);
 			    //3上传新的组织
 				ReturnVo<List<DepartmentForImport>>  rHRData = deptService.getEHRDeptData();
 				if(null!=rHRData && CollectionUtils.isNotEmpty(rHRData.getData())){
 					//为了防止删除临时部门，加入临时部门
 					rHRData.getData().add(tempdept);
 					//生成部门表
 			        File deptCSVFile = dataToCSVFile.DeptDataToCSV(rHRData);
 			        wxServiceClient.uploadFile(deptCSVFile);
 			        //4 融合人员数据并上传
 			        if (WxServiceClient.isIsSuccessParty()) {
 			        	Thread.sleep(90000);
 		        		//List<UserlistBean> wxuserlist = wxServiceClient.getUserDetails("",1);//拉取正式环境人员数据
 		        		ReturnVo<List<PersonalForImport>> ehrPersons = personService.getEHRPersonalData();
 		        		if(null!=ehrPersons){
 		        			Map<String,UserlistBean> updateCollectMap = new HashMap<String,UserlistBean>();
 		        			List<PersonalForImport>  ehrPersonList = 	ehrPersons.getData();
 		        			for (UserlistBean personalForImport : userlist) {
 		        				updateCollectMap.put(personalForImport.userid.trim()+"-"+personalForImport.name.trim(), personalForImport);
 							}
 		        			for (PersonalForImport personalForImport : ehrPersonList) {
 		        				UserlistBean userlistbean = updateCollectMap.get(personalForImport.getPsn_id().trim()+"-"+personalForImport.getPsn_name().trim());
 		        				if(null!=userlistbean){
 		        					//case1:匹配到的数据以微信服务端(老数据)的数据为准，但是部门除外,部门以外部流入的数据为准
 		        					copyOldPersonToNewPersonExcludeDeptId(matchedfw, personalForImport, userlistbean);
 		        					//case2:数据内容以新数据为准，部门以老数据的组织为准
 		        					//personalForImport.setPsn_deptcode(null!=userlistbean.department &&  userlistbean.department.size()>0? userlistbean.department.get(0).toString():"");
 		        					//case3:所有数据一律以新数据为准，这种情况不需要做任何处理。本次循环逻辑可以直接去掉
 		        				}else{
 		        					unmatchedfw.write(personalForImport+"\n");
 		        				}
 							}
 		        			//执行批量更新人员
 		        			ReturnVo<List<PersonalForImport>> pVo = new ReturnVo<List<PersonalForImport>>();
 	        				pVo.setData(ehrPersonList);
 	        				File file = dataToCSVFile.PersonDataToCSV(pVo);
 	        				wxServiceClient.uploadFileSysUser(file);
 	        				//记录结束时间戳，为下次同步使用
 	        				endRecordOperation("1970-01-01 00:00:00",1);//person
 	        				
 	        				/*int total = ehrPersonList.size();
 		        			int pagecount = total/1000;
 		        			int pageStart = 0;
 		        			int pageEnd = 1001;
 		        			for(int  i=1;i<=pagecount+1;i++){
 		        				System.out.println("start:"+pageStart+" | pageEnd:"+pageEnd);
 		        				ReturnVo<List<PersonalForImport>> pVo = new ReturnVo<List<PersonalForImport>>();
 		        				pVo.setData(ehrPersonList.subList(pageStart, pageEnd));
 		        				pageStart = (i*1000)+1;
 		        				pageEnd = ((pageStart+1000)> ehrPersonList.size()?ehrPersonList.size():pageStart+1000);
 		        				//Thread.sleep(60001);
 		        			    //File file = dataToCSVFile.PersonDataToCSV(pVo);
 		        				//wxServiceClient.uploadFile(file);
 		        			}*/
 		        		}
 			        }
 				}
 		}
 	} catch (Exception e) {
 		e.printStackTrace();
 	}finally{
		try {
			matchedfw.flush();
			matchedfw.close();
			unmatchedfw.flush();
			unmatchedfw.close();
			//upFailfw.flush();
			//upFailfw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
	}
/**
 * 把老的数据的信息拷贝到新数据之上，但是部门保持以新数据为准
 * @param matchedfw
 * @param personalForImport
 * @param userlistbean
 * @throws IOException
 */
	private void copyOldPersonToNewPersonExcludeDeptId(BufferedWriter matchedfw, PersonalForImport personalForImport,
			UserlistBean userlistbean) throws IOException {
		personalForImport.setPsn_mobile(CommUtils.defaultIfEmpty(userlistbean.mobile, "").trim()); //手机号
		personalForImport.setPsn_email(CommUtils.defaultIfEmpty(userlistbean.email,"").trim());  //邮箱
		//personalForImport.setPsn_deptcode(null!=userlistbean.department &&  userlistbean.department.size()>0? userlistbean.department.get(0).toString():"");//所在部门
		personalForImport.setPsn_postname(CommUtils.defaultIfEmpty(userlistbean.position,"").trim()); //职位
		personalForImport.setWeixinid(CommUtils.defaultIfEmpty(userlistbean.weixinid,"").trim()); // 微信号	 	
		 if(null!=userlistbean.extattr && null!=userlistbean.extattr.attrs && userlistbean.extattr.attrs.size()>0){
			 List<AttrsBean>  attrsBean =  userlistbean.extattr.attrs;
			 for (AttrsBean attrsBeanItem : attrsBean) {
				 if("所在办公地".equals(attrsBeanItem.name)){
					 personalForImport.setLocaloffice(attrsBeanItem.value);
				 }else if("全称".equals(attrsBeanItem.name)){
					 personalForImport.setFullName(attrsBeanItem.value);
				 }else if("简称".equals(attrsBeanItem.name)){
					 personalForImport.setShortName(attrsBeanItem.value);
				 }else if("排序号".equals(attrsBeanItem.name)){
					 if(CommUtils.isNotEmpty(attrsBeanItem.value)){
						 personalForImport.setOrderNo(Integer.valueOf(attrsBeanItem.value) );
					 }
				 }
				 else if("是否可见".equals(attrsBeanItem.name)){
					 personalForImport.setIsvisible(attrsBeanItem.value);
				 }
				 else if("固定电话".equals(attrsBeanItem.name)){
					 personalForImport.setPhone(attrsBeanItem.value);
				 }
				 else if("座机短号".equals(attrsBeanItem.name)){
					 personalForImport.setPhoneShort(attrsBeanItem.value);
				 }
				 else if("工号".equals(attrsBeanItem.name)){
					 personalForImport.setPsn_code(attrsBeanItem.value);
				 }
				 else if("手机短号".equals(attrsBeanItem.name)){
					 personalForImport.setPhoneShort(attrsBeanItem.value);
				 }
			}
		 }
		matchedfw.write(personalForImport+"\n");
	}

    /**
     * excludeId排除部门 不做删除操作
     * 删除组织部门数据
     * @Description:
     * @author wangzequan 2017 上午11:50:24
     */
    private synchronized  void  getParty(String token,int id,final int excludeId){
    	ArrayList<Department>  departments =  wxServiceClient.getParty(token,id);
    	if(CollectionUtils.isNotEmpty(departments)){
	    	for (int i=0;i<departments.size();i++) {
	    		if(id==departments.get(i).id){
	    				continue;
	    		}else{
	    			if(departments.get(i).id!= excludeId){
	    				if(!wxServiceClient.deleteParty(token, departments.get(i).id+"") ){
	    					getParty(token,departments.get(i).id,excludeId);
	    				}
	    			}
	    		}
			}
    	}
    }
    
    /**
     * 删除组织数据
     * @Description:
     * @author wangzequan 2017 下午4:25:39
     */
    public void deleteTestEnvironmentOrganizationData(final int excludeId){
    	//String  token =  wxServiceClient.getToken();
    		for (int i = 0; i < 60; i++) {
    			Thread  t =  new Thread(new Runnable() {
    				public void run() {
    					getParty("",1,excludeId);
    				}
    			});
    			t.start();
    			try {
					t.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
    }
}
