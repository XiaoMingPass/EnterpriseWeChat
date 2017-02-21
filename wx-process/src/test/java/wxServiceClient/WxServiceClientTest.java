package wxServiceClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ys.wx.function.DataToCSVFile;
import com.ys.wx.model.dept.DepartmentForImport;
import com.ys.wx.model.person.PersonalForImport;
import com.ys.wx.model.wx.response.PartyListResponse;
import com.ys.wx.model.wx.response.PartyListResponse.Department;
import com.ys.wx.model.wx.response.UserDetailsResponse;
import com.ys.wx.model.wx.response.UserDetailsResponse.UserlistBean;
import com.ys.wx.model.wx.response.UserDetailsResponse.UserlistBean.ExtattrBean.AttrsBean;
import com.ys.wx.service.dept.IDeptService;
import com.ys.wx.service.person.IPersonService;
import com.ys.wx.service.wx.IWxServiceClient;
import com.ys.wx.service.wx.WxServiceClient;
import com.ys.wx.utils.CommUtils;
import com.ys.wx.vo.ReturnVo;

/**
 * 微信API客户端测试类(用于测试环境运行)
 * @Title:
 * @Description:
 * @Author:wangzequan
 * @Since:2017年2月17日 上午10:56:53
 * @Version:1.1.0
 * @Copyright:Copyright (c) 二龙湖基地组织 2015 ~ 2016 版权所有
 */
public class WxServiceClientTest {

	
	IWxServiceClient wxServiceClient;
	
	DataToCSVFile dataToCSVFile;
	
    private IPersonService   personService;
    private IDeptService     deptService;
    
    
    @Before
    public void init() throws Exception {
        @SuppressWarnings("resource")
        ApplicationContext ac = new ClassPathXmlApplicationContext(
                new String[]{"file:src/main/resources/spring_common.xml",
                        "file:src/main/resources/wx_dubbo.xml",
                        "file:src/main/resources/wx_dubbo_customer.xml"});
        
        wxServiceClient = (IWxServiceClient)ac.getBean("wxServiceClient");
        dataToCSVFile = (DataToCSVFile)ac.getBean("dataToCSVFile");
        personService = (IPersonService)ac.getBean("personServiceImpl");
        deptService = (IDeptService)ac.getBean("deptServiceImpl");
    }

    
   @Test
   public void  testGetUsersFile(){
        try {
     		//拉取正式环境人员数据
     		List<UserlistBean> wxuserlist = wxServiceClient.getUserDetails("",1);
     		ReturnVo<List<PersonalForImport>> ehrPersons = personService.getEHRPersonalData(1);
     		if(null!=ehrPersons){
     			Map<String,UserlistBean> updateCollectMap = new HashMap<String,UserlistBean>();
     			List<PersonalForImport>  ehrPersonList = 	ehrPersons.getData();
     			setPersons(ehrPersonList);//放在亚厦装饰
     			for (UserlistBean personalForImport : wxuserlist) {
     				updateCollectMap.put(personalForImport.userid.trim()+"-"+personalForImport.name.trim(), personalForImport);
				}
     			int  count = 0;
     			for (PersonalForImport personalForImport : ehrPersonList) {
     				if(personalForImport.getPsn_id().trim().equals("130403198008210317")){
     					System.out.println(personalForImport);
     				}
     				UserlistBean userlistbean = updateCollectMap.get(personalForImport.getPsn_id().trim()+"-"+personalForImport.getPsn_name().trim());
     				if(null!=userlistbean){
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
     				}else{
     				}
     				count++;
     				if(count==199){
     					break;
     				}
					}
     			
     			ReturnVo<List<PersonalForImport>> pVo = new ReturnVo<List<PersonalForImport>>();
 				pVo.setData(ehrPersonList.subList(0, ehrPersonList.size()>200?150:ehrPersonList.size()));
 				File file = dataToCSVFile.PersonDataToCSV(pVo);
 				wxServiceClient.uploadFileSysUser(file);
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
	        } catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 从正式环境拉下组织和人员数据分别保存到文件
     * @Description:
     * @author wangzequan 2017 下午1:20:31
     */
    
    @Test
    public void testGetRealPartyAndPersonToFile(){
    	//拉取正式环境的组织
    	ArrayList<Department> departments = 	wxServiceClient.getParty("",1);
    	if(CollectionUtils.isNotEmpty(departments)){
    		ReturnVo<List<DepartmentForImport>> dVo  = new ReturnVo<List<DepartmentForImport>>(0,"失败");
    		List<DepartmentForImport>  departmentForImports = new ArrayList<DepartmentForImport>();
    		DepartmentForImport tempDepartmentForImport = null;
    		for (Department department : departments) {
    			tempDepartmentForImport =  new DepartmentForImport();
    			tempDepartmentForImport.setDept_id(department.id+"");
    			tempDepartmentForImport.setDept_name(department.name);
    			tempDepartmentForImport.setDept_fid(department.parentid+"");
    			tempDepartmentForImport.setDept_order(department.order+"");
    			departmentForImports.add(tempDepartmentForImport);
			}
    		dVo.setData(departmentForImports);
    		File file = dataToCSVFile.DeptDataToCSV(dVo);
    		List<UserlistBean> userlist = wxServiceClient.getUserDetails("",1);
    		saveWeiXinServerPersonList(userlist); 
     	}
    }


	
	/**
	 * @throws NumberFormatException
	 * @Description:
	 * @author wangzequan 2017 下午4:38:03
	 */
	private void saveWeiXinServerPersonList(List<UserlistBean> userlist ) throws NumberFormatException {
		//拉取正式环境人员数据
		
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
				 personalforimport.setPsn_deptcode( null!=userlistbean.department &&  userlistbean.department.size()>0? userlistbean.department.get(0).toString():"" );//所在部门
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
			 pVo.setData(personalForImports);
			 dataToCSVFile.PersonDataToCSV(pVo);
		}
	}
    
	
    /**
     * 上传正式环境数据到的 测试环境
     *
     * @Description:
     * @author wangzequan 2017 上午9:55:00
     */
    
	@Test
    public void testUpdateDeptAndPersonDataToTestEnvironment(){
    	//File deptDataFile   = new File("F://Out//party5437401530154456255.csv");
      	File personDataFile  = new File("F://Out//user6587454125567159188.csv");
//    	
    	//wxServiceClient.uploadFile(deptDataFile);
    	//if (WxServiceClient.isIsSuccessParty() || 1==1){
			System.out.println("上传部门数据到测试环境成功。。。。");
			wxServiceClient.uploadFile(personDataFile);
			System.out.println("上传人员数据到测试环境成功。。。。");
    	//}
		}
    
	
    /**
     * 删除测试环境的组织部门数据
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
     * 删除测试环境的组织数据
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
    
    @Test
    public void deleteAlldept(){
    	deleteTestEnvironmentOrganizationData(-1);
    }
    
    @Test
    public void deleteUser(){
    	try {
    		/*File f = new File("F://Out//import_file.json");
			FileReader fr = new FileReader(f);
			BufferedReader bfr = new BufferedReader(fr);
			String s = "";
			while((s = bfr.readLine())!=null){
				wxServiceClient.deleteUser("", s.split(",")[0]);
				wxServiceClient.deleteUser("", s.split(",")[1]);
			}*/
    		//330682000000000000
    		wxServiceClient.deleteUser("", "incoder");
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    }
    
    // author:ceaser Wang
    @Test
    public void fireData(){
    	//部门数据，通过部门名称匹配，ehr数据更新正式环境的数据。包括部门id，父级id 最后生成csv文件。
    	BufferedWriter matchedfw = null;
    	BufferedWriter unmatchedfw = null;
    	BufferedWriter ghostfw = null;
    	BufferedWriter upFailfw = null;
    	try {
    		matchedfw = new BufferedWriter(new FileWriter(new File("f://Out//workspace//matched.txt")) ) ;
    		unmatchedfw = new BufferedWriter (new FileWriter(new File("f://Out//workspace//unmatched.txt")));
    		ghostfw = new BufferedWriter (new FileWriter(new File("f://Out//workspace//ghostuser.txt")));
    		upFailfw = new BufferedWriter (new FileWriter(new File("f://Out//workspace//updateFaileduser.txt")));
			//ReturnVo<List<DepartmentForImport>>  rHRData = deptService.getEHRDeptData();
			//if(null!=rHRData && CollectionUtils.isNotEmpty(rHRData.getData())){
				  //生成部门表
	            //File deptCSVFile = dataToCSVFile.DeptDataToCSV(rHRData);
	            //wxServiceClient.uploadFile(deptCSVFile);
	            //if (WxServiceClient.isIsSuccessParty() && 1==0) {//部门表同步成功
	            	//Thread.sleep(1000);
	        		//拉取正式环境人员数据
	        		List<UserlistBean> userlist = wxServiceClient.getUserDetails("",1);
	        		ReturnVo<List<PersonalForImport>> ehrPersons = personService.getEHRPersonalData();
	        		if(null!=ehrPersons){
	        			Map<String,UserlistBean> updateCollectMap = new HashMap<String,UserlistBean>();
	        			List<PersonalForImport>  ehrPersonList = 	ehrPersons.getData();
	        			for (UserlistBean personalForImport : userlist) {
	        				updateCollectMap.put(personalForImport.userid.trim()+"-"+personalForImport.name.trim(), personalForImport);
						}
	        			//ehrPersonList.clear();
	        			List<UserlistBean> updateList = new ArrayList<UserlistBean>();
	        			List<UserlistBean> addList = new ArrayList<UserlistBean>();
	        			//ReturnVo<List<PersonalForImport>> pVo =  new ReturnVo<List<PersonalForImport>>();
	        			
	        			for (PersonalForImport personalForImport : ehrPersonList) {
	        				UserlistBean userlistbean = updateCollectMap.get(personalForImport.getPsn_id().trim()+"-"+personalForImport.getPsn_name().trim());
	        				if(null!=userlistbean){
	        					if(null==userlistbean.department){
	        						userlistbean.department = new ArrayList<Integer>();
	        					}
	        					userlistbean.department.add(Integer.valueOf(personalForImport.getPsn_deptcode().trim()) );
	        					updateList.add(userlistbean);
	        					matchedfw.write(personalForImport+"\n");
	        				}else{
	        					UserlistBean newuserlistbean = new UserDetailsResponse().new UserlistBean();
	        					newuserlistbean.userid = personalForImport.getPsn_id().trim();
	        					newuserlistbean.name = personalForImport.getPsn_name();
	        					newuserlistbean.department  = new ArrayList<Integer>();
	        					newuserlistbean.department.add(Integer.parseInt(CommUtils.defaultIfEmpty(personalForImport.getPsn_deptcode(), "") .trim()));
	        					newuserlistbean.mobile = CommUtils.defaultIfEmpty(personalForImport.getPsn_mobile(),"").trim();
	        					newuserlistbean.email = CommUtils.defaultIfEmpty(personalForImport.getPsn_email(),"").trim();
	        					AttrsBean attrsbean = new UserDetailsResponse().new UserlistBean().new ExtattrBean().new AttrsBean();
	        					for(int i = 0;i<9;i++){
	        						switch(i){
		        						case 0:attrsbean.name = "所在办公地"; attrsbean.value = personalForImport.getLocaloffice(); continue;
		        						case 1:attrsbean.name = "全称";attrsbean.value = personalForImport.getFullName(); continue;
		        						case 2:attrsbean.name = "简称"; attrsbean.value = personalForImport.getShortName();continue;
		        						case 3:attrsbean.name = "排序号";attrsbean.value = personalForImport.getOrderNo()+""; continue;
		        						case 4:attrsbean.name = "是否可见";attrsbean.value = personalForImport.getIsvisible(); continue;
		        						case 5:attrsbean.name = "固定电话";attrsbean.value = personalForImport.getPhone(); continue;
		        						case 6:attrsbean.name = "座机短号";attrsbean.value = personalForImport.getPhoneShort(); continue;
		        						case 7:attrsbean.name = "工号"; attrsbean.value = personalForImport.getPsn_code(); continue;
		        						case 8:attrsbean.name = "手机短号"; attrsbean.value = personalForImport.getMobileShortNo(); continue;
	        						}
	        					}
	        					newuserlistbean.extattr = new UserDetailsResponse().new UserlistBean().new ExtattrBean(); 
	        					newuserlistbean.extattr.attrs = new ArrayList<UserDetailsResponse.UserlistBean.ExtattrBean.AttrsBean>();
	        					newuserlistbean.extattr.attrs.add(attrsbean);
	        					addList.add(newuserlistbean);
	        					ghostfw.write(personalForImport+"\n");
	        					unmatchedfw.write(personalForImport+"\n");
	        				}
						}
	        			int count = 0;
	        			for (UserlistBean userlistBean : updateList) {
	        				//wxServiceClient.deleteUser("", userlistBean.userid);
	        				if(!wxServiceClient.updateUser("", userlistBean)){
	        					upFailfw.write(userlistBean+"\n");
	        				}
	        				if(count==1000){
	        					Thread.sleep(60009);
	        				}
	        				count++;
						}
	        			for (UserlistBean userlistBean : addList) {
	        				//wxServiceClient.deleteUser("", userlistBean.userid);
	        				if(!wxServiceClient.addUser("", userlistBean)){
	        					upFailfw.write(userlistBean+"\n");
	        				}
	        				if(count==1000){
	        					Thread.sleep(60009);
	        				}
	        				count++;
	        			}
	        			
	        			//dataToCSVFile.PersonDataToCSV(pVo)
	        			//wxServiceClient.uploadFile(file);
	        			
	        		}
	           // }
			//}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				matchedfw.flush();
				matchedfw.close();
				unmatchedfw.flush();
				unmatchedfw.close();
				upFailfw.flush();
				upFailfw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    	//人员数据。通过名字和手机号以及邮箱匹配人员数据，
    }
    
    
 // author:ceaser Wang
    @Test
    public void fireDataFile(){
    	//部门数据，通过部门名称匹配，ehr数据更新正式环境的数据。包括部门id，父级id 最后生成csv文件。
    	BufferedWriter matchedfw = null;
    	BufferedWriter unmatchedfw = null;
    	BufferedWriter ghostfw = null;
    	BufferedWriter upFailfw = null;
    	try {
    		matchedfw = new BufferedWriter(new FileWriter(new File("f://Out//workspace//matched.txt")) ) ;
    		unmatchedfw = new BufferedWriter (new FileWriter(new File("f://Out//workspace//unmatched.txt")));
    		//ghostfw = new BufferedWriter (new FileWriter(new File("f://Out//workspace//ghostuser.txt")));
    		upFailfw = new BufferedWriter (new FileWriter(new File("f://Out//workspace//updateFaileduser.txt")));
			ReturnVo<List<DepartmentForImport>>  rHRData = deptService.getEHRDeptData();
			if(null!=rHRData && CollectionUtils.isNotEmpty(rHRData.getData())){
				  //生成部门表
	            File deptCSVFile = dataToCSVFile.DeptDataToCSV(rHRData);
	            wxServiceClient.uploadFile(deptCSVFile);
	            if (WxServiceClient.isIsSuccessParty() ) {//部门表同步成功
	            	Thread.sleep(1000);
	        		//拉取正式环境人员数据
	        		List<UserlistBean> userlist = wxServiceClient.getUserDetails("",1);
	        		ReturnVo<List<PersonalForImport>> ehrPersons = personService.getEHRPersonalData();
	        		if(null!=ehrPersons){
	        			Map<String,UserlistBean> updateCollectMap = new HashMap<String,UserlistBean>();
	        			List<PersonalForImport>  ehrPersonList = 	ehrPersons.getData();
	        			setPersons(ehrPersonList);
	        			for (UserlistBean personalForImport : userlist) {
	        				updateCollectMap.put(personalForImport.userid.trim()+"-"+personalForImport.name.trim(), personalForImport);
						}
	        			List<PersonalForImport> addList = new ArrayList<PersonalForImport>();
	        			//ReturnVo<List<PersonalForImport>> pVo =  new ReturnVo<List<PersonalForImport>>();
	        			
	        			int  count = 0;
	        			for (PersonalForImport personalForImport : ehrPersonList) {
	        				UserlistBean userlistbean = updateCollectMap.get(personalForImport.getPsn_id().trim()+"-"+personalForImport.getPsn_name().trim());
	        				if(null!=userlistbean){
	        					/*if(CollectionUtils.isNotEmpty(userlistbean.department)){
	        						personalForImport.setPsn_deptcode(userlistbean.department.get(0)+"");
	        					}*/
	        					//personalForImport.setPsn_name(userlistbean.name);//姓名
	        					//personalForImport.setPsn_id(userlistbean.userid); //帐号	
	        					personalForImport.setPsn_mobile(CommUtils.defaultIfEmpty(userlistbean.mobile, "").trim()); //手机号
	        					personalForImport.setPsn_email(CommUtils.defaultIfEmpty(userlistbean.email,"").trim());  //邮箱
	        					personalForImport.setPsn_deptcode( null!=userlistbean.department &&  userlistbean.department.size()>0? userlistbean.department.get(0).toString():"" );//所在部门
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
	        				}else{
	        					//ghostfw.write(personalForImport+"\n");
	        					unmatchedfw.write(personalForImport+"\n");
	        				}
	        				addList.add(personalForImport);
	        				count++;
	        				if(count==199){
	        					break;
	        				}
						}
	        			
	        			ReturnVo<List<PersonalForImport>> pVo = new ReturnVo<List<PersonalForImport>>();
        				pVo.setData(addList);
        				File file = dataToCSVFile.PersonDataToCSV(pVo);
        				wxServiceClient.uploadFile(file);
        				
        				
        				
	        			/*int total = addList.size();
	        			int pagecount = total/1000;
	        			int pageStart = 0;
	        			int pageEnd = 1001;
	        			for(int  i=1;i<=pagecount+1;i++){
	        				System.out.println("start:"+pageStart+" | pageEnd:"+pageEnd);
	        				ReturnVo<List<PersonalForImport>> pVo = new ReturnVo<List<PersonalForImport>>();
	        				pVo.setData(addList.subList(pageStart, pageEnd));
	        				pageStart = (i*1000)+1;
	        				pageEnd = ((pageStart+1000)> addList.size()?addList.size():pageStart+1000);
	        				//Thread.sleep(60001);
	        			    //File file = dataToCSVFile.PersonDataToCSV(pVo);
	        				//wxServiceClient.uploadFile(file);
	        			}*/
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
				upFailfw.flush();
				upFailfw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    	//人员数据。通过名字和手机号以及邮箱匹配人员数据，
    }
    
    @Test
    public void finaloperation(){
    	BufferedWriter matchedfw = null;
    	BufferedWriter unmatchedfw = null;
    	BufferedWriter ghostfw = null;
    	BufferedWriter upFailfw = null;
    	try {
    		matchedfw = new BufferedWriter(new FileWriter(new File("f://Out//workspace//matched.txt")) ) ;
    		unmatchedfw = new BufferedWriter (new FileWriter(new File("f://Out//workspace//unmatched.txt")));
    		//ghostfw = new BufferedWriter (new FileWriter(new File("f://Out//workspace//ghostuser.txt")));
    		upFailfw = new BufferedWriter (new FileWriter(new File("f://Out//workspace//updateFaileduser.txt")));
			//1、得到人员；老数据列表
			List<UserlistBean> wxuserlist = wxServiceClient.getUserDetails("",1);
			//1.1保存人员列表
			saveWeiXinServerPersonList(wxuserlist);
			//2.删除人员和组织架构
				//2.1删除人员
				deleteAllUser();
				//2.2删除组织架构
				deleteTestEnvironmentOrganizationData(-1);
			//3.得到EHR的组织架构，上传。
				ReturnVo<List<DepartmentForImport>>  rHRData = deptService.getEHRDeptData();
				if(null!=rHRData && CollectionUtils.isNotEmpty(rHRData.getData())){
					//生成部门表
			        File deptCSVFile = dataToCSVFile.DeptDataToCSV(rHRData);
			        wxServiceClient.uploadFile(deptCSVFile);
			        if (WxServiceClient.isIsSuccessParty() ) {//部门表同步成功
			        	//部门表同步成功
		            	Thread.sleep(5000);
		        		//拉取正式环境人员数据
		        		//List<UserlistBean> wxuserlist = wxServiceClient.getUserDetails("",1);
		        		ReturnVo<List<PersonalForImport>> ehrPersons = personService.getEHRPersonalData(1);
		        		if(null!=ehrPersons){
		        			Map<String,UserlistBean> updateCollectMap = new HashMap<String,UserlistBean>();
		        			List<PersonalForImport>  ehrPersonList = 	ehrPersons.getData();
		        			setPersons(ehrPersonList);
		        			for (UserlistBean personalForImport : wxuserlist) {
		        				updateCollectMap.put(personalForImport.userid.trim()+"-"+personalForImport.name.trim(), personalForImport);
							}
		        			//List<PersonalForImport> addList = new ArrayList<PersonalForImport>();
		        			//ReturnVo<List<PersonalForImport>> pVo =  new ReturnVo<List<PersonalForImport>>();
		        			
		        			int  count = 0;
		        			for (PersonalForImport personalForImport : ehrPersonList) {
		        				if("650106197910102310".equals(personalForImport.getPsn_id())){
		        					System.out.println();
		        				}
		        				UserlistBean userlistbean = updateCollectMap.get(personalForImport.getPsn_id().trim()+"-"+personalForImport.getPsn_name().trim());
		        				if(null!=userlistbean){
		        					/*if(CollectionUtils.isNotEmpty(userlistbean.department)){
		        						personalForImport.setPsn_deptcode(userlistbean.department.get(0)+"");
		        					}*/
		        					//personalForImport.setPsn_name(userlistbean.name);//姓名
		        					//personalForImport.setPsn_id(userlistbean.userid); //帐号	
		        					personalForImport.setPsn_mobile(CommUtils.defaultIfEmpty(userlistbean.mobile, "").trim()); //手机号
		        					personalForImport.setPsn_email(CommUtils.defaultIfEmpty(userlistbean.email,"").trim());  //邮箱
		        					personalForImport.setPsn_deptcode( null!=userlistbean.department &&  userlistbean.department.size()>0? userlistbean.department.get(0).toString():"" );//所在部门
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
		        				}else{
		        					//ghostfw.write(personalForImport+"\n");
		        					unmatchedfw.write(personalForImport+"\n");
		        				}
		        				//addList.add(personalForImport);
		        				count++;
		        				if(count==199){
		        					//break;
		        				}
							}
		        			
		        			ReturnVo<List<PersonalForImport>> pVo = new ReturnVo<List<PersonalForImport>>();
	        				pVo.setData(ehrPersonList);
	        				File file = dataToCSVFile.PersonDataToCSV(pVo);
	        				wxServiceClient.uploadFile(file);
	        				
	        				
	        				
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
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				matchedfw.flush();
				matchedfw.close();
				unmatchedfw.flush();
				unmatchedfw.close();
				upFailfw.flush();
				upFailfw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    		
    }
    
   private void setPersons(List<PersonalForImport> ehrPersonList) {
	   //第一个人（存在）
       /*PersonalForImport personalforimport =  new PersonalForImport();
       personalforimport.setPsn_id("ceaser");
       personalforimport.setPsn_name("王泽全");
       personalforimport.setPsn_mobile("13758256466");
       personalforimport.setPsn_deptcode(200001+"");
       personalforimport.setWeixinid("wzq657");
       personalforimport.setPsn_email("1156721874@qq.com");
       ehrPersonList.add(0,personalforimport);
       
       //第二个人不存在
       PersonalForImport personalforimport1 =  new PersonalForImport();
       personalforimport1.setPsn_id("ceaser1");
       personalforimport1.setPsn_name("王泽全1");
       personalforimport1.setPsn_mobile("13758256455");
       personalforimport1.setPsn_deptcode(2+"");
       personalforimport1.setWeixinid("wzq6571");
       personalforimport1.setPsn_email("1156721875@qq.com");
       ehrPersonList.add(1,personalforimport1);*/
	   
       /*PersonalForImport personalforimport1 =  new PersonalForImport();
       personalforimport1.setPsn_id("incoder");
       personalforimport1.setPsn_name("Jerry");
       personalforimport1.setPsn_mobile("13468653073");
       personalforimport1.setPsn_deptcode(200001+"");
       personalforimport1.setWeixinid("flyskysea");
       personalforimport1.setPsn_email("balbo@foxmail.com");
       ehrPersonList.add(0,personalforimport1);*/
	   
       PersonalForImport personalforimport1 =  new PersonalForImport();
       personalforimport1.setPsn_id("130403198008210317");
       personalforimport1.setPsn_name("石江辉");
       personalforimport1.setPsn_mobile("15378785029");
       personalforimport1.setPsn_deptcode(300001+"");
       personalforimport1.setWeixinid("flyskysea");
       personalforimport1.setPsn_email("balbo@foxmail.com");
       ehrPersonList.add(0,personalforimport1);
	
   }


   @Test
   public void addOneUser(){
		try {
			ReturnVo<List<PersonalForImport>> ehrPersons = personService.getEHRPersonalData();
			if(null!=ehrPersons && CollectionUtils.isNotEmpty(ehrPersons.getData())){
				List<PersonalForImport>  du =  ehrPersons.getData();
				for (PersonalForImport personalforimport : du) {
					UserlistBean userlistbean = new UserDetailsResponse().new UserlistBean();
					userlistbean.userid = personalforimport.getPsn_id();
					userlistbean.name = personalforimport.getPsn_name();
					userlistbean.mobile = personalforimport.getPsn_mobile();
					System.out.println(personalforimport.getPsn_email());
					//userlistbean.email = personalforimport.getPsn_email();
					List<Integer> dp = new ArrayList<Integer>();
					dp.add(Integer.valueOf(personalforimport.getPsn_deptcode()) );
					userlistbean.department = dp;
					wxServiceClient.addUser("", userlistbean);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
   }
    
   
   
   @Test
   public void addOneUserByInput(){
		try {
			
				   ReturnVo<List<DepartmentForImport>>  deptvo = new ReturnVo<List<DepartmentForImport>>();
				   List<DepartmentForImport> deptlist = new ArrayList<DepartmentForImport>();
				   DepartmentForImport di = new DepartmentForImport();
				   di.setDept_id("99999");
				   di.setDept_name("临时部门A");
				   di.setDept_fid("1");
				   di.setDept_order(0+"");
				   deptlist.add(di);
				   
				/*   DepartmentForImport di1 = new DepartmentForImport();
				   di1.setDept_id("3");
				   di1.setDept_name("二");
				   di1.setDept_fid("1");
				   deptlist.add(di1);
				   
				   DepartmentForImport di2 = new DepartmentForImport();
				   di2.setDept_id("4");
				   di2.setDept_name("三");
				   di2.setDept_fid("1");
				   deptlist.add(di2);
				   
				 */  
				   deptvo.setData(deptlist);
				   //生成部门表
		           File deptCSVFile = dataToCSVFile.DeptDataToCSV(deptvo);
		           wxServiceClient.uploadFile(deptCSVFile);
				   
				   
				   
			//程静怡	3.30682E+17			15267507757	464863102@qq.com	112	职员				0		0571-89880949/900949		2079	
		           /*  ReturnVo<List<PersonalForImport>> pVo = new ReturnVo<List<PersonalForImport>>();
			       List<PersonalForImport> list = new ArrayList<PersonalForImport>();
			       //第一个人（存在）
			         PersonalForImport personalforimport =  new PersonalForImport();
			       personalforimport.setPsn_id("330682198310070666");
			       personalforimport.setPsn_name("甄颖4");
			       personalforimport.setPsn_mobile("13735323666");
			       personalforimport.setPsn_deptcode(200001+"");
			     //  personalforimport.setWeixinid("wzq657");
			       personalforimport.setPsn_email("404808966@qq.com");
			       list.add(personalforimport);*/
			       
			       /*
			       //第二个人不存在
			       PersonalForImport personalforimport1 =  new PersonalForImport();
			       personalforimport1.setPsn_id("ceaser1");
			       personalforimport1.setPsn_name("王泽全1");
			       personalforimport1.setPsn_mobile("13758256455");
			       personalforimport1.setPsn_deptcode(4+"");
			       personalforimport1.setWeixinid("wzq6571");
			       personalforimport1.setPsn_email("1156721875@qq.com");
			       list.add(personalforimport1);*/
			       
			      /* PersonalForImport personalforimport1 =  new PersonalForImport();
			       personalforimport1.setPsn_id("incoder");
			       personalforimport1.setPsn_name("Jerry");
			       personalforimport1.setPsn_mobile("13468653073");
			       personalforimport1.setPsn_deptcode(200001+"");
			       personalforimport1.setWeixinid("flyskysea");
			       personalforimport1.setPsn_email("balbo@foxmail.com");
			       list.add(personalforimport1);
			       
			       pVo.setData(list);
			       File file = dataToCSVFile.PersonDataToCSV(pVo);
   				   wxServiceClient.uploadFile(file);*/
		} catch (Exception e) {
			e.printStackTrace();
		}
   }
   
   @Test
   public void translateXlsxToCSVFile(){
	   try {
		File f= new File("f://YASHA.csv");
		   FileReader fr = new FileReader(f);
		   BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(f),"gbk"));
		   String s ="";
		   while( (s = bfr.readLine())!=null){
			   String [] strArr = s.split(",");
			   System.out.println(Arrays.toString(strArr));
		   }
		   
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
   }
   
   
   @Test
   public void deleteAllUser(){
	   List<UserlistBean> userlist = wxServiceClient.getUserDetails("",1);
	   for (UserlistBean userlistBean : userlist) {
		   wxServiceClient.deleteUser("", userlistBean.userid);
	}
   }
   
   @Test
   public void useTempDept(){
	   try {
		   //1.create  temp dept
		   Department department = new PartyListResponse().new Department();
		   department.id=99999;
		   department.name="临时部门" ;
		   department.parentid = 1;
		   department.order=0;
		   if(wxServiceClient.createParty("", department)){
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
					   personalforimport.setPsn_deptcode( department.id+"");//所在部门
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
				   pVo.setData(personalForImports);
				   File file =  dataToCSVFile.PersonDataToCSV(pVo);
				   wxServiceClient.uploadFile(file);
			   }
			   //2 删除老的组织(临时组织除外)
			    deleteTestEnvironmentOrganizationData(department.id);
			    //3上传新的组织
				ReturnVo<List<DepartmentForImport>>  rHRData = deptService.getEHRDeptData();
				if(null!=rHRData && CollectionUtils.isNotEmpty(rHRData.getData())){
					//生成部门表
			        File deptCSVFile = dataToCSVFile.DeptDataToCSV(rHRData);
			        wxServiceClient.uploadFile(deptCSVFile);
			        //4 融合人员数据并上传
			        if (WxServiceClient.isIsSuccessParty()) {
			        	//部门表同步成功
		            	Thread.sleep(5000);
		        		//拉取正式环境人员数据
		        		//List<UserlistBean> wxuserlist = wxServiceClient.getUserDetails("",1);
		        		ReturnVo<List<PersonalForImport>> ehrPersons = personService.getEHRPersonalData(1);
		        		if(null!=ehrPersons){
		        			Map<String,UserlistBean> updateCollectMap = new HashMap<String,UserlistBean>();
		        			List<PersonalForImport>  ehrPersonList = 	ehrPersons.getData();
		        			setPersons(ehrPersonList);
		        			for (UserlistBean personalForImport : userlist) {
		        				updateCollectMap.put(personalForImport.userid.trim()+"-"+personalForImport.name.trim(), personalForImport);
							}
		        			//List<PersonalForImport> addList = new ArrayList<PersonalForImport>();
		        			//ReturnVo<List<PersonalForImport>> pVo =  new ReturnVo<List<PersonalForImport>>();
		        			
		        			int  count = 0;
		        			for (PersonalForImport personalForImport : ehrPersonList) {
		        				UserlistBean userlistbean = updateCollectMap.get(personalForImport.getPsn_id().trim()+"-"+personalForImport.getPsn_name().trim());
		        				if(null!=userlistbean){
		        					/*if(CollectionUtils.isNotEmpty(userlistbean.department)){
		        						personalForImport.setPsn_deptcode(userlistbean.department.get(0)+"");
		        					}*/
		        					//personalForImport.setPsn_name(userlistbean.name);//姓名
		        					//personalForImport.setPsn_id(userlistbean.userid); //帐号	
		        					personalForImport.setPsn_mobile(CommUtils.defaultIfEmpty(userlistbean.mobile, "").trim()); //手机号
		        					personalForImport.setPsn_email(CommUtils.defaultIfEmpty(userlistbean.email,"").trim());  //邮箱
		        					personalForImport.setPsn_deptcode( null!=userlistbean.department &&  userlistbean.department.size()>0? userlistbean.department.get(0).toString():"" );//所在部门
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
		        				}
		        				count++;
		        				if(count==199){
		        					break;
		        				}
							}
		        			
		        			ReturnVo<List<PersonalForImport>> pVo = new ReturnVo<List<PersonalForImport>>();
	        				pVo.setData(ehrPersonList.subList(0, ehrPersonList.size()>200?150:ehrPersonList.size()));
	        				File file = dataToCSVFile.PersonDataToCSV(pVo);
	        				wxServiceClient.uploadFile(file);
	        				
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
	}
   }
   
   @Test
   public void testUpdateTempDept(){
	   Department department = new PartyListResponse().new Department();
	   department.id=99999;
	   department.name="临时部门" ;
	   department.parentid = 1;
	   department.order=0;
	   String token =  wxServiceClient.getToken();
	   wxServiceClient.updateOneParty(token, department);
}
   @Test
   public void testUpdateOtherDept(){
	   //[id=151, name=营销中心-综合管理部, parentid=128, order=5] 江苏区域公司-营销分中心-技术支持部
	   // [id=555, name=江苏区域公司-营销分中心-技术支持部333, parentid=129, order=1]
	   Department department = new PartyListResponse().new Department();
	   department.id=151;
	   department.name="营销中心-综合管理部7777" ;
	   department.parentid = 128;
	   department.order=5;
	   String token =  wxServiceClient.getToken();
	   wxServiceClient.updateOneParty(token, department);
   }
   
   
   @Test
   public void testGetParty(){
	   try {
		//ReturnVo<List<DepartmentForImport>> vo =  deptService.getEHRDeptData();
		   String token =  wxServiceClient.getToken();
		System.out.println();
	} catch (Exception e) {
		e.printStackTrace();
	}
   }
   
   
}
