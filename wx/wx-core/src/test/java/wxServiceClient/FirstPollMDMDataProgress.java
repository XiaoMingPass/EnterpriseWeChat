package wxServiceClient;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.mhome.tools.vo.ReturnVo;
import com.ys.wx.api.IWxJobApi;
import com.ys.wx.constant.WxConstant;
import com.ys.wx.function.EHRDataToCSVFile;
import com.ys.wx.model.common.SynDataRecord;
import com.ys.wx.model.person.PersonalForImport;
import com.ys.wx.model.wx.response.PartyListResponse;
import com.ys.wx.model.wx.response.PartyListResponse.Department;
import com.ys.wx.model.wx.response.UserDetailsResponse.UserlistBean;
import com.ys.wx.model.wx.response.UserDetailsResponse.UserlistBean.ExtattrBean.AttrsBean;
import com.ys.wx.service.common.ISequenceService;
import com.ys.wx.service.common.ISynDataRecordService;
import com.ys.wx.service.company.ICompanyService;
import com.ys.wx.service.department.IDepartmentService;
import com.ys.wx.service.wx.IEnterpriseWeChatService;
import com.ys.wx.service.wx.IWxServiceClient;
import com.ys.wx.service.wx.WxServiceClient;
import com.ys.wx.utils.CommUtils;
import com.ys.wx.vo.Company;
import com.ys.wx.vo.Person;

public class FirstPollMDMDataProgress {

	private static Logger logger = LoggerFactory.getLogger(FirstPollMDMDataProgress.class);
	
    ISynDataRecordService    synDataRecordService;
    IWxServiceClient         wxServiceClient;
    IEnterpriseWeChatService weChatService;
    IWxJobApi                wxJobApi;
    EHRDataToCSVFile         dataToCSVFile;

    IDepartmentService departmentService;
    com.ys.wx.service.person.IPersonService personService;
	private ISequenceService sequenceService;
	
	ICompanyService companyService;
    
    @Before
    public void init() throws Exception {
        @SuppressWarnings("resource")
        ApplicationContext ac = new ClassPathXmlApplicationContext(
                "file:src/main/resources/spring_common.xml", "file:src/main/resources/wx_dubbo.xml",
                "file:src/main/resources/wx_dubbo_customer.xml");
        
        wxServiceClient = (IWxServiceClient) ac.getBean("wxServiceClient");
        weChatService = (IEnterpriseWeChatService) ac.getBean("enterpriseWeChatServiceImpl");
        dataToCSVFile = (EHRDataToCSVFile) ac.getBean("EHRDataToCSVFile");
        synDataRecordService = (ISynDataRecordService) ac.getBean("synDataRecordServiceImpl");
        wxJobApi = (IWxJobApi) ac.getBean("wxJobApiImpl");
        
        departmentService = (IDepartmentService) ac.getBean("departmentServiceImpl");
        personService = (com.ys.wx.service.person.IPersonService) ac.getBean("personServiceImpl");
        sequenceService = (ISequenceService) ac.getBean("sequenceServiceImpl");
        companyService = (ICompanyService) ac.getBean("companyServiceImpl");
    }

    
    @Test
    public void schduleTask(){
    	weChatService.processLocalToWeiXin();
    }
    

    /**
     * 第一次数据切换测试  -- 单个API调用方式
     *
     * @Description:
     * @author wangzequan 2017 上午9:50:53
     */
    @Test
    public void useTempDept() {
        BufferedWriter matchedfw = null;
        BufferedWriter unmatchedfw = null;
        // BufferedWriter ghostfw = null;
        BufferedWriter upFailfw = null;
        try {
            matchedfw = new BufferedWriter(new FileWriter(new File("f://Out//workspace//matched.txt")));
            unmatchedfw = new BufferedWriter(new FileWriter(new File("f://Out//workspace//unmatched.txt")));
            // ghostfw = new BufferedWriter (new FileWriter(new
            // File("f://Out//workspace//ghostuser.txt")));
            upFailfw = new BufferedWriter(new FileWriter(new File("f://Out//workspace//updateFaileduser.txt")));
            // 1.create temp dept
            Department department = new PartyListResponse().new Department();
            department.id = 99999;
            department.name = "临时部门";
            department.parentid = 1;
            department.order = 0;

            com.ys.wx.vo.Department tempdept = new com.ys.wx.vo.Department();
            tempdept.setPcode(department.parentid + "");
            tempdept.setName(department.name);
            tempdept.setCode(department.id + "");
            tempdept.setOrderNo(department.order + "");

            String token = wxServiceClient.getToken();
            if (wxServiceClient.createParty(token, department) || wxServiceClient.updateOneParty(token, department)) {
                Thread.sleep(5000);
                // 1.1.把所有人员放到临时部门下边
                List<UserlistBean> userlist = wxServiceClient.getUserDetails(token, 1);
                if (CollectionUtils.isNotEmpty(userlist)) {
                    ReturnVo<List<PersonalForImport>> pVo = new ReturnVo<List<PersonalForImport>>(0, "失败");
                    List<PersonalForImport> personalForImports = new ArrayList<PersonalForImport>();
                    PersonalForImport personalforimport = null;
                    for (UserlistBean userlistbean : userlist) {
                        personalforimport = new PersonalForImport();
                        personalforimport.setPsn_name(userlistbean.name);// 姓名
                        personalforimport.setPsn_id(userlistbean.userid); // 帐号
                        personalforimport.setPsn_mobile(userlistbean.mobile); // 手机号
                        personalforimport.setPsn_email(userlistbean.email); // 邮箱
                        personalforimport.setPsn_deptcode(department.id + "");// 所在部门
                        personalforimport.setPsn_postname(userlistbean.position); // 职位
                        personalforimport.setWeixinid(userlistbean.weixinid); // 微信号
                        if (null != userlistbean.extattr && null != userlistbean.extattr.attrs
                                && userlistbean.extattr.attrs.size() > 0) {
                            List<AttrsBean> attrsBean = userlistbean.extattr.attrs;
                            for (AttrsBean attrsBeanItem : attrsBean) {
                                if ("所在办公地".equals(attrsBeanItem.name)) {
                                    personalforimport.setLocaloffice(attrsBeanItem.value);
                                } else if ("全称".equals(attrsBeanItem.name)) {
                                    personalforimport.setFullName(attrsBeanItem.value);
                                } else if ("简称".equals(attrsBeanItem.name)) {
                                    personalforimport.setShortName(attrsBeanItem.value);
                                } else if ("排序号".equals(attrsBeanItem.name)) {
                                    if (CommUtils.isNotEmpty(attrsBeanItem.value)) {
                                        personalforimport.setOrderNo(Integer.valueOf(attrsBeanItem.value));
                                    }
                                } else if ("是否可见".equals(attrsBeanItem.name)) {
                                    personalforimport.setIsvisible(attrsBeanItem.value);
                                } else if ("固定电话".equals(attrsBeanItem.name)) {
                                    personalforimport.setPhone(attrsBeanItem.value);
                                } else if ("座机短号".equals(attrsBeanItem.name)) {
                                    personalforimport.setPhoneShort(attrsBeanItem.value);
                                } else if ("工号".equals(attrsBeanItem.name)) {
                                    personalforimport.setPsn_code(attrsBeanItem.value);
                                } else if ("手机短号".equals(attrsBeanItem.name)) {
                                    personalforimport.setPhoneShort(attrsBeanItem.value);
                                }else if("职务".equals(attrsBeanItem.name)){
                                	personalforimport.setPsn_postname(attrsBeanItem.value);
                                }else if("性别".equals(attrsBeanItem.name)){
                                	if(CommUtils.isNotEmpty(attrsBeanItem.value)){
                                		personalforimport.setSex(attrsBeanItem.value);
                                	}
                               }
                            }
                        }
                        if(CollectionUtils.isEmpty(userlistbean.department)){
                        	userlistbean.department = new ArrayList<Integer>();
                        }
                        userlistbean.department.clear();
                        userlistbean.department.add(department.id);
                        wxServiceClient.updateUser(token, userlistbean);
                        personalForImports.add(personalforimport);
                    }
                    /*pVo.setData(personalForImports);
                    File file = dataToCSVFile.PersonDataToCSV(pVo);
                    wxServiceClient.uploadFile(file);*/
                    Thread.sleep(100000);
                }
                // 2 删除老的组织(临时组织除外)
                weChatService.deleteTestEnvironmentOrganizationData(token,department.id);
                // 3上传新的组织
                List<com.ys.wx.vo.Department> rHRData = departmentService.genDeptTreeMixCompany();//poll dept data  from MDM >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                if (null != rHRData && CollectionUtils.isNotEmpty(rHRData)) {
                    // 为了防止删除临时部门，加入临时部门
                    rHRData.add(tempdept);
                    // 生成部门表
                    File deptCSVFile = dataToCSVFile.DeptDataToCSVForNew(rHRData);//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                    wxServiceClient.uploadFile(deptCSVFile);
                    // 4 融合人员数据并上传
                    if (WxServiceClient.isIsSuccessParty()) {
                        Thread.sleep(100000);
                        // 拉取正式环境人员数据
                        // List<UserlistBean> wxuserlist =
                        // wxServiceClient.getUserDetails("",1);
                        Person person = new Person();
                        person.setDelFlag(null);
                        List<Person> ehrPersons = personService.getAll(person);//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                        if (null != ehrPersons) {
                            Map<String, UserlistBean> updateCollectMap = new HashMap<String, UserlistBean>();
                            List<Person> ehrPersonList = ehrPersons;
                            // setPersons(ehrPersonList);//放在亚厦装饰
                            for (UserlistBean personalForImport : userlist) {
                                updateCollectMap.put(personalForImport.userid.trim()/* + "-" + personalForImport.name.trim()*/, personalForImport);
                            }
                             int count = 0;
                            for (Person itemperson : ehrPersonList) {
                            	if(count>=200){
                            		logger.info("总数量超过200，结束。");
                            		break;
                            	}
                                UserlistBean userlistbean = null;
                                if(CommUtils.isNotEmpty(itemperson.getIdcard())){
                                	userlistbean = updateCollectMap.get(itemperson.getIdcard().trim()/* + "-" + personalForImport.getPsn_name().trim()*/);
                                }
                                if (null != userlistbean) {
                                    itemperson.setMobile(CommUtils.defaultIfEmpty(userlistbean.mobile, "").trim()); // 手机号
                                    itemperson.setEmail(CommUtils.defaultIfEmpty(userlistbean.email, "").trim()); // 邮箱
                                    // personalForImport.setPsn_deptcode(null!=userlistbean.department
                                    // && userlistbean.department.size()>0?
                                    // userlistbean.department.get(0).toString():"");//所在部门
                                    itemperson.setPosition(CommUtils.defaultIfEmpty(userlistbean.position, "").trim()); // 职位
                                    itemperson.setWeixin(CommUtils.defaultIfEmpty(userlistbean.weixinid, "").trim()); // 微信号
                                    if (null != userlistbean.extattr && null != userlistbean.extattr.attrs && userlistbean.extattr.attrs.size() > 0) {
                                        List<AttrsBean> attrsBean = userlistbean.extattr.attrs;
                                        for (AttrsBean attrsBeanItem : attrsBean) {
                                            if ("所在办公地".equals(attrsBeanItem.name)) {
                                                itemperson.setOfficeAddr(attrsBeanItem.value);
                                            } else if ("全称".equals(attrsBeanItem.name)) {
                                                itemperson.setFullName(attrsBeanItem.value);
                                            } else if ("简称".equals(attrsBeanItem.name)) {
                                                itemperson.setShortName(attrsBeanItem.value);
                                            } else if ("排序号".equals(attrsBeanItem.name)) {
                                                if (CommUtils.isNotEmpty(attrsBeanItem.value)) {
                                                    itemperson.setOrderNo(Integer.valueOf(attrsBeanItem.value));
                                                }
                                            } else if ("是否可见".equals(attrsBeanItem.name)) {
                                                itemperson.setIsVisable(attrsBeanItem.value);
                                            } else if ("固定电话".equals(attrsBeanItem.name)) {
                                                itemperson.setPhone(attrsBeanItem.value);
                                            } else if ("座机短号".equals(attrsBeanItem.name)) {
                                                itemperson.setShortPhone(attrsBeanItem.value);
                                            } else if ("工号".equals(attrsBeanItem.name)) {
                                                itemperson.setNo(attrsBeanItem.value);
                                            } else if ("手机短号".equals(attrsBeanItem.name)) {
                                                itemperson.setShortMobile(attrsBeanItem.value);
                                            }else if("职务".equals(attrsBeanItem.name)){
                                            	itemperson.setPosition(attrsBeanItem.value);
                                            }else if("性别".equals(attrsBeanItem.name)){
                                            	if(CommUtils.isNotEmpty(attrsBeanItem.value)){
                                            		itemperson.setSex(attrsBeanItem.value);
                                            	}
                                           }
                                        }
                                    }
                                    if(CommUtils.isNotEmpty(itemperson.getDeptCode())){
                                    	userlistbean.department = new ArrayList<Integer>();
                                    	userlistbean.department.add(Integer.valueOf(itemperson.getDeptCode()));
                                    }
                                    wxServiceClient.updateUser(token, userlistbean);
                                    matchedfw.write(itemperson + "\n");
                                    logger.info("更新一个人员.");
                                } else {
                                    unmatchedfw.write(itemperson + "\n");
                                    wxServiceClient.addUser(token, itemperson);
                                    logger.info("添加人员("+count+"):"+itemperson);
                                }
                                count++;
                            }
                            //File file = dataToCSVFile.PersonDataToCSVForNew(ehrPersonList.subList(0, 200));
                            //wxServiceClient.uploadFileSysUser(file);

                            //数据状态恢复
                            Company company = new Company();
                            company.setSyn(WxConstant.MODYFIED);
                            List<Company> companies =  companyService.getAll(company);
                            if(CollectionUtils.isNotEmpty(companies)){
                            	for (Company company2 : companies) {
                            		company2.setSyn(WxConstant.NOMODYFIED);
								}
                            	companyService.updateCompanyList(companies);
                            }
                            
                            
                            com.ys.wx.vo.Department qdepartment = new com.ys.wx.vo.Department();
                            qdepartment.setSyn(WxConstant.MODYFIED);
                            List<com.ys.wx.vo.Department> departments = departmentService.getAll(qdepartment);
                            
                            if(CollectionUtils.isNotEmpty(departments)){
                            	for (com.ys.wx.vo.Department department2 : departments) {
                            		department2.setSyn(WxConstant.NOMODYFIED);
								}
                            	departmentService.updateDepartmentList(departments);
                            }
                            
                            Person 	qperson = new Person();
                            qperson.setSyn(WxConstant.MODYFIED);
                            List<Person>  persons =  personService.getAll(qperson);
                            if(CollectionUtils.isNotEmpty(persons)){
                            	for (Person person2 : persons) {
                            		person2.setSyn(WxConstant.NOMODYFIED);
								}
                            	personService.updatePersonList(persons);
                            }
                            //endRecordOperation("1970-01-01 00:00:00", 1);// person

							/*
                             * int total = ehrPersonList.size(); int pagecount =
							 * total/1000; int pageStart = 0; int pageEnd =
							 * 1001; for(int i=1;i<=pagecount+1;i++){
							 * System.out.println("start:"+pageStart+
							 * " | pageEnd:"+pageEnd);
							 * ReturnVo<List<PersonalForImport>> pVo = new
							 * ReturnVo<List<PersonalForImport>>();
							 * pVo.setData(ehrPersonList.subList(pageStart,
							 * pageEnd)); pageStart = (i*1000)+1; pageEnd =
							 * ((pageStart+1000)>
							 * ehrPersonList.size()?ehrPersonList.size():
							 * pageStart+1000); //Thread.sleep(60001); //File
							 * file = dataToCSVFile.PersonDataToCSV(pVo);
							 * //wxServiceClient.uploadFile(file); }
							 */
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                matchedfw.flush();
                matchedfw.close();
                unmatchedfw.flush();
                unmatchedfw.close();
                upFailfw.flush();
                upFailfw.close();
                logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>end");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    

    /**
     * 第一次数据切换测试 --- 使用文件批量上传
     *
     * @Description:
     * @author wangzequan 2017 上午9:50:53
     */
    @Test
    public void useTempDeptUseFile() {
        BufferedWriter matchedfw = null;
        BufferedWriter unmatchedfw = null;
        // BufferedWriter ghostfw = null;
        BufferedWriter upFailfw = null;
        try {
            matchedfw = new BufferedWriter(new FileWriter(new File("f://Out//workspace//matched.txt")));
            unmatchedfw = new BufferedWriter(new FileWriter(new File("f://Out//workspace//unmatched.txt")));
            // ghostfw = new BufferedWriter (new FileWriter(new
            // File("f://Out//workspace//ghostuser.txt")));
            upFailfw = new BufferedWriter(new FileWriter(new File("f://Out//workspace//updateFaileduser.txt")));
            // 1.create temp dept
            Department department = new PartyListResponse().new Department();
            department.id = 99999;
            department.name = "临时部门";
            department.parentid = 1;
            department.order = 0;

            com.ys.wx.vo.Department tempdept = new com.ys.wx.vo.Department();
            tempdept.setPcode(department.parentid + "");
            tempdept.setName(department.name);
            tempdept.setCode(department.id + "");
            tempdept.setOrderNo(department.order + "");

            String token = wxServiceClient.getToken();
            if (wxServiceClient.createParty(token, department) || wxServiceClient.updateOneParty(token, department)) {
                Thread.sleep(5000);
                // 1.1.把所有人员放到临时部门下边
                List<UserlistBean> userlist = wxServiceClient.getUserDetails(token, 1);
                if (CollectionUtils.isNotEmpty(userlist)) {
                    ReturnVo<List<PersonalForImport>> pVo = new ReturnVo<List<PersonalForImport>>(0, "失败");
                    List<PersonalForImport> personalForImports = new ArrayList<PersonalForImport>();
                    PersonalForImport personalforimport = null;
                    for (UserlistBean userlistbean : userlist) {
                        personalforimport = new PersonalForImport();
                        personalforimport.setPsn_name(userlistbean.name);// 姓名
                        personalforimport.setPsn_id(userlistbean.userid); // 帐号
                        personalforimport.setPsn_mobile(userlistbean.mobile); // 手机号
                        personalforimport.setPsn_email(userlistbean.email); // 邮箱
                        personalforimport.setPsn_deptcode(department.id + "");// 所在部门
                        personalforimport.setPsn_postname(userlistbean.position); // 职位
                        personalforimport.setWeixinid(userlistbean.weixinid); // 微信号
                        if (null != userlistbean.extattr && null != userlistbean.extattr.attrs
                                && userlistbean.extattr.attrs.size() > 0) {
                            List<AttrsBean> attrsBean = userlistbean.extattr.attrs;
                            for (AttrsBean attrsBeanItem : attrsBean) {
                                if ("所在办公地".equals(attrsBeanItem.name)) {
                                    personalforimport.setLocaloffice(attrsBeanItem.value);
                                } else if ("全称".equals(attrsBeanItem.name)) {
                                    personalforimport.setFullName(attrsBeanItem.value);
                                } else if ("简称".equals(attrsBeanItem.name)) {
                                    personalforimport.setShortName(attrsBeanItem.value);
                                } else if ("排序号".equals(attrsBeanItem.name)) {
                                    if (CommUtils.isNotEmpty(attrsBeanItem.value)) {
                                        personalforimport.setOrderNo(Integer.valueOf(attrsBeanItem.value));
                                    }
                                } else if ("是否可见".equals(attrsBeanItem.name)) {
                                    personalforimport.setIsvisible(attrsBeanItem.value);
                                } else if ("固定电话".equals(attrsBeanItem.name)) {
                                    personalforimport.setPhone(attrsBeanItem.value);
                                } else if ("座机短号".equals(attrsBeanItem.name)) {
                                    personalforimport.setPhoneShort(attrsBeanItem.value);
                                } else if ("工号".equals(attrsBeanItem.name)) {
                                    personalforimport.setPsn_code(attrsBeanItem.value);
                                } else if ("手机短号".equals(attrsBeanItem.name)) {
                                    personalforimport.setPhoneShort(attrsBeanItem.value);
                                }else if("职务".equals(attrsBeanItem.name)){
                                	personalforimport.setPsn_postname(attrsBeanItem.value);
                                }else if("性别".equals(attrsBeanItem.name)){
                                	if(CommUtils.isNotEmpty(attrsBeanItem.value)){
                                		personalforimport.setSex(attrsBeanItem.value);
                                	}
                               }
                            }
                        }
                        personalForImports.add(personalforimport);
                    }
                    pVo.setData(personalForImports);
                    File file = dataToCSVFile.PersonDataToCSV(pVo);
                    wxServiceClient.uploadFile(file);
                    Thread.sleep(100000);
                }
                // 2 删除老的组织(临时组织除外)
                weChatService.deleteTestEnvironmentOrganizationData(token,department.id);
                // 3上传新的组织
                List<com.ys.wx.vo.Department> rHRData = departmentService.genDeptTreeMixCompany();//poll dept data  from MDM >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                if (null != rHRData && CollectionUtils.isNotEmpty(rHRData)) {
                    // 为了防止删除临时部门，加入临时部门
                    rHRData.add(tempdept);
                    // 生成部门表
                    File deptCSVFile = dataToCSVFile.DeptDataToCSVForNew(rHRData);//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                    wxServiceClient.uploadFile(deptCSVFile);
                    // 4 融合人员数据并上传
                    if (WxServiceClient.isIsSuccessParty()) {
                        Thread.sleep(100000);
                        // 拉取正式环境人员数据
                        // List<UserlistBean> wxuserlist =
                        // wxServiceClient.getUserDetails("",1);
                        Person person = new Person();
                        person.setDelFlag(null);
                        List<Person> ehrPersons = personService.getAll(person);//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                        if (null != ehrPersons) {
                            Map<String, UserlistBean> updateCollectMap = new HashMap<String, UserlistBean>();
                            List<Person> ehrPersonList = ehrPersons;
                            // setPersons(ehrPersonList);//放在亚厦装饰
                            for (UserlistBean personalForImport : userlist) {
                                updateCollectMap.put(personalForImport.userid.trim()/* + "-" + personalForImport.name.trim()*/, personalForImport);
                            }
                             int count = 0;
                             List<Person> weixinPersonList  = new ArrayList<Person>();
                            for (Person itemperson : ehrPersonList) {
                            	if(null!=itemperson.getDelFlag() && itemperson.getDelFlag().intValue()==WxConstant.DEL){
                            		continue;
                            	}
                            	if(count>=200){
                            		 logger.info("总数量超过200，结束。");
                            		break;
                            	}
                                UserlistBean userlistbean = null;
                                if(CommUtils.isNotEmpty(itemperson.getIdcard())){
                                	userlistbean = updateCollectMap.get(itemperson.getIdcard().trim()/* + "-" + personalForImport.getPsn_name().trim()*/);
                                }
                                if (null != userlistbean) {
                                    itemperson.setMobile(CommUtils.defaultIfEmpty(userlistbean.mobile, "").trim()); // 手机号
                                    itemperson.setEmail(CommUtils.defaultIfEmpty(userlistbean.email, "").trim()); // 邮箱
                                    // personalForImport.setPsn_deptcode(null!=userlistbean.department
                                    // && userlistbean.department.size()>0?
                                    // userlistbean.department.get(0).toString():"");//所在部门
                                    itemperson.setPosition(CommUtils.defaultIfEmpty(userlistbean.position, "").trim()); // 职位
                                    itemperson.setWeixin(CommUtils.defaultIfEmpty(userlistbean.weixinid, "").trim()); // 微信号
                                    if (null != userlistbean.extattr && null != userlistbean.extattr.attrs && userlistbean.extattr.attrs.size() > 0) {
                                        List<AttrsBean> attrsBean = userlistbean.extattr.attrs;
                                        for (AttrsBean attrsBeanItem : attrsBean) {
                                            if ("所在办公地".equals(attrsBeanItem.name)) {
                                                itemperson.setOfficeAddr(attrsBeanItem.value);
                                            } else if ("全称".equals(attrsBeanItem.name)) {
                                                itemperson.setFullName(attrsBeanItem.value);
                                            } else if ("简称".equals(attrsBeanItem.name)) {
                                                itemperson.setShortName(attrsBeanItem.value);
                                            } else if ("排序号".equals(attrsBeanItem.name)) {
                                                if (CommUtils.isNotEmpty(attrsBeanItem.value)) {
                                                    itemperson.setOrderNo(Integer.valueOf(attrsBeanItem.value));
                                                }
                                            } else if ("是否可见".equals(attrsBeanItem.name)) {
                                                itemperson.setIsVisable(attrsBeanItem.value);
                                            } else if ("固定电话".equals(attrsBeanItem.name)) {
                                                itemperson.setPhone(attrsBeanItem.value);
                                            } else if ("座机短号".equals(attrsBeanItem.name)) {
                                                itemperson.setShortPhone(attrsBeanItem.value);
                                            } else if ("工号".equals(attrsBeanItem.name)) {
                                                itemperson.setNo(attrsBeanItem.value);
                                            } else if ("手机短号".equals(attrsBeanItem.name)) {
                                                itemperson.setShortMobile(attrsBeanItem.value);
                                            }else if("职务".equals(attrsBeanItem.name)){
                                            	itemperson.setPosition(attrsBeanItem.value);
                                            }else if("性别".equals(attrsBeanItem.name)){
                                            	if(CommUtils.isNotEmpty(attrsBeanItem.value)){
                                            		itemperson.setSex(attrsBeanItem.value);
                                            	}
                                           }
                                        }
                                    }
                                    matchedfw.write(itemperson + "\n");
                                    logger.info("更新一个人员.");
                                } else {
                                    unmatchedfw.write(itemperson + "\n");
                                    logger.info("添加人员("+count+"):"+itemperson);
                                }
                                weixinPersonList.add(itemperson);
                                count++;
                            }
                            File file = dataToCSVFile.PersonDataToCSVForNew(weixinPersonList);
                            wxServiceClient.uploadFileSysUser(file);

                            //数据状态恢复
                            Company company = new Company();
                            company.setSyn(WxConstant.MODYFIED);
                            List<Company> companies =  companyService.getAll(company);
                            if(CollectionUtils.isNotEmpty(companies)){
                            	for (Company company2 : companies) {
                            		company2.setSyn(WxConstant.NOMODYFIED);
								}
                            	companyService.updateCompanyList(companies);
                            }
                            
                            
                            com.ys.wx.vo.Department qdepartment = new com.ys.wx.vo.Department();
                            qdepartment.setSyn(WxConstant.MODYFIED);
                            List<com.ys.wx.vo.Department> departments = departmentService.getAll(qdepartment);
                            
                            if(CollectionUtils.isNotEmpty(departments)){
                            	for (com.ys.wx.vo.Department department2 : departments) {
                            		department2.setSyn(WxConstant.NOMODYFIED);
								}
                            	departmentService.updateDepartmentList(departments);
                            }
                            
                            Person 	qperson = new Person();
                            qperson.setSyn(WxConstant.MODYFIED);
                            List<Person>  persons =  personService.getAll(qperson);
                            if(CollectionUtils.isNotEmpty(persons)){
                            	for (Person person2 : persons) {
                            		person2.setSyn(WxConstant.NOMODYFIED);
								}
                            	personService.updatePersonList(persons);
                            }
                            //endRecordOperation("1970-01-01 00:00:00", 1);// person

							/*
                             * int total = ehrPersonList.size(); int pagecount =
							 * total/1000; int pageStart = 0; int pageEnd =
							 * 1001; for(int i=1;i<=pagecount+1;i++){
							 * System.out.println("start:"+pageStart+
							 * " | pageEnd:"+pageEnd);
							 * ReturnVo<List<PersonalForImport>> pVo = new
							 * ReturnVo<List<PersonalForImport>>();
							 * pVo.setData(ehrPersonList.subList(pageStart,
							 * pageEnd)); pageStart = (i*1000)+1; pageEnd =
							 * ((pageStart+1000)>
							 * ehrPersonList.size()?ehrPersonList.size():
							 * pageStart+1000); //Thread.sleep(60001); //File
							 * file = dataToCSVFile.PersonDataToCSV(pVo);
							 * //wxServiceClient.uploadFile(file); }
							 */
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                matchedfw.flush();
                matchedfw.close();
                unmatchedfw.flush();
                unmatchedfw.close();
                upFailfw.flush();
                upFailfw.close();
                logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>end");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    
    /**
     * type : 1:person;
     *
     * @param maxEndTime
     * @throws Exception
     * @Description:
     * @author wangzequan 2017 下午12:48:07
     */
    private void endRecordOperation(String maxEndTime, int type) throws Exception {
        SynDataRecord synDataRecord = new SynDataRecord();
        synDataRecord.setType(type);
        synDataRecord.setStartTime(CommUtils.parseStrToLongDate(maxEndTime));
        synDataRecord.setEndTime(new Date());
        synDataRecord.setOperator("ceaser");
        synDataRecord.setOperateTime(new Date());
        synDataRecordService.insertSynDataRecord(synDataRecord);
    }
    
    @Test
    public void testSequence() throws Exception{
    	for(int i=0;i<20;i++){
    		System.out.println(">>>>>>>>>>>>>>> : "+sequenceService.next(WxConstant.GLOBAL));
    	}
    }

    public volatile int inc = 1;
    
    @Test
    public void testMDMData() {
        try {
            List<com.ys.wx.vo.Department> rHRData = departmentService.genDeptTreeMixCompany();
            System.err.println(">>>>>>>>>>>>>>>>>>>>>> : "+rHRData.size());
            Set<String> ids = new HashSet<String>();
            Set<String> names = new HashSet<String>();
            for (com.ys.wx.vo.Department department : rHRData) {
        		if(CommUtils.isEmpty(department.getCode()) || CommUtils.isEmpty(department.getName())){
        			logger.info("空的数据-name:"+department.getName()+"-code:"+department.getCode()+"-pcode:"+department.getPcode());
        		}
				if(ids.contains(department.getCode())){
					logger.info("重复编码:"+department.getCode());
				}else{
					ids.add(department.getCode());
				}
				if(names.contains(department.getPcode()+department.getName())){
					logger.info("同一部门【"+department.getPcode()+"】下存在相同名字部门，重复名称:"+department.getName());
				}else{
					names.add(department.getPcode()+department.getName());
				}
			}
            List<com.ys.wx.vo.Department> pCodeIsnull =getPcodeIsNull(rHRData);
            treeChilds(pCodeIsnull,rHRData,"|-");
            System.out.println(rHRData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private  void treeChilds(List<com.ys.wx.vo.Department> ch,List<com.ys.wx.vo.Department> rHRData,String s){
    	for (com.ys.wx.vo.Department department : ch) {
    		if(CommUtils.isEmpty(department.getCode()) || CommUtils.isEmpty(department.getName())){
    			logger.info("【"+s+"】"+"name:"+department.getName()+"-code:"+department.getCode()+"-pcode:"+department.getPcode());
    		}
    		logger.info(s+"name:"+department.getName()+"-code:"+department.getCode()+"-pcode:"+department.getPcode()+" : count : "+inc++);
    		List<com.ys.wx.vo.Department> cchilds = getchilds(department.getCode(),rHRData);
    		if(CollectionUtils.isNotEmpty(cchilds)){
    			treeChilds(cchilds,rHRData,s+"|-");
    		}
		}
    }
    
    private List<com.ys.wx.vo.Department> getchilds(String pcode,List<com.ys.wx.vo.Department> rHRData){
    	List<com.ys.wx.vo.Department> rl = new ArrayList<com.ys.wx.vo.Department>();
    	for (com.ys.wx.vo.Department department : rHRData) {
			if(CommUtils.isNotEmpty(department.getPcode()) && department.getPcode().equals(pcode)){
				rl.add(department);
			}
		}
    	return rl;
    }
    
    private  List<com.ys.wx.vo.Department> getPcodeIsNull(List<com.ys.wx.vo.Department> rHRData){
    	List<com.ys.wx.vo.Department> rl = new ArrayList<com.ys.wx.vo.Department>();
    	for (com.ys.wx.vo.Department department : rHRData) {
			if(CommUtils.isNotEmpty(department.getPcode()) && department.getPcode().equals("1")){
				rl.add(department);
			}
		}
    	return rl;
    }
    
    @Test
    public void testWeinxinData() {
        try {
        	String token = wxServiceClient.getToken();
        	ArrayList<Department> weixindepts = wxServiceClient.getParty(token, 1);
        	
        	
        	 ReturnVo<List<PersonalForImport>> pVo = new ReturnVo<List<PersonalForImport>>(0, "失败");
        	 /*PersonalForImport personalForImport = new PersonalForImport();
        	 personalForImport.setPsn_name("ACER");
        	 personalForImport.setPsn_deptcode("99999");
        	 personalForImport.setPsn_id("340122199105206555");
        	 personalForImport.setPsn_mobile("18756086555");
        	 personalForImport.setPsn_code("00005555");
        	 personalForImport.setPsn_email("463760555@qq.com");
        	 List<PersonalForImport> personalForImports = new ArrayList<PersonalForImport>();
        	 personalForImports.add(personalForImport);
        	 pVo.setData(personalForImports);
        	 File file = dataToCSVFile.PersonDataToCSV(pVo);
             wxServiceClient.uploadFileSysUser(file);*/
        	
            // wxServiceClient.updateUser(access_token, userlistbean)
             
            // 3上传新的组织
        	/* List<com.ys.wx.vo.Department> rHRData = new ArrayList<com.ys.wx.vo.Department>();
            
            com.ys.wx.vo.Department d = new com.ys.wx.vo.Department();
            d.setCode("2");
            d.setPcode("1");
            d.setName("亚厦控股");
            rHRData.add(d);
            
            if (null != rHRData && CollectionUtils.isNotEmpty(rHRData)) {
                File deptCSVFile = dataToCSVFile.DeptDataToCSVForNew(rHRData);//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                wxServiceClient.uploadFile(deptCSVFile);
            }  */  
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
