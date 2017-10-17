package wxServiceClient;

import com.mhome.tools.vo.ReturnVo;
import com.ys.ucenter.api.IOrgApi;
import com.ys.wx.api.IWxJobApi;
import com.ys.wx.function.EHRDataToCSVFile;
import com.ys.wx.model.common.SynDataRecord;
import com.ys.wx.model.dept.DepartmentForImport;
import com.ys.wx.model.person.PersonalForImport;
import com.ys.wx.model.wx.response.PartyListResponse;
import com.ys.wx.model.wx.response.PartyListResponse.Department;
import com.ys.wx.model.wx.response.UserDetailsResponse.UserlistBean;
import com.ys.wx.model.wx.response.UserDetailsResponse.UserlistBean.ExtattrBean.AttrsBean;
import com.ys.wx.service.common.ISynDataRecordService;
import com.ys.wx.service.dept.IDeptService;
import com.ys.wx.service.ehrperson.IPersonService;
import com.ys.wx.service.wx.IEnterpriseWeChatService;
import com.ys.wx.service.wx.IWxServiceClient;
import com.ys.wx.service.wx.WxServiceClient;
import com.ys.wx.utils.CommUtils;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class UcenterCDPExchangeProgress {

    ISynDataRecordService    synDataRecordService;
    IWxServiceClient         wxServiceClient;
    IEnterpriseWeChatService weChatService;
    IWxJobApi                wxJobApi;
    EHRDataToCSVFile         dataToCSVFile;
    IPersonService           personService;
    IDeptService             deptService;
    IOrgApi                  orgApi;

    @Before
    public void init() throws Exception {
        @SuppressWarnings("resource")
        ApplicationContext ac = new ClassPathXmlApplicationContext(
                "file:src/main/resources/spring_common.xml", "file:src/main/resources/wx_dubbo.xml",
                "file:src/main/resources/wx_dubbo_customer.xml");

        wxServiceClient = (IWxServiceClient) ac.getBean("wxServiceClient");
        weChatService = (IEnterpriseWeChatService) ac.getBean("enterpriseWeChatServiceImpl");
        dataToCSVFile = (EHRDataToCSVFile) ac.getBean("EHRDataToCSVFile");
        personService = (IPersonService) ac.getBean("personServiceImpl");
        deptService = (IDeptService) ac.getBean("deptServiceImpl");
        synDataRecordService = (ISynDataRecordService) ac.getBean("synDataRecordServiceImpl");
        wxJobApi = (IWxJobApi) ac.getBean("wxJobApiImpl");
        orgApi = (IOrgApi) ac.getBean("orgApi");
    }


    /**
     * 第一次数据切换测试
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

            DepartmentForImport tempdept = new DepartmentForImport();
            tempdept.setDept_fid(department.parentid + "");
            tempdept.setDept_name(department.name);
            tempdept.setDept_id(department.id + "");
            tempdept.setDept_order(department.order + "");

            String token = wxServiceClient.getToken();
            if (wxServiceClient.createParty(token, department) || wxServiceClient.updateOneParty(token, department)) {
                Thread.sleep(5000);
                // 1.1.把所有人员放到临时部门下边
                List<UserlistBean> userlist = wxServiceClient.getUserDetails("", 1);
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
                ReturnVo<List<DepartmentForImport>> rHRData = deptService.getMDMDeptData();//poll dept data  from MDM >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                if (null != rHRData && CollectionUtils.isNotEmpty(rHRData.getData())) {
                    // 为了防止删除临时部门，加入临时部门
                    rHRData.getData().add(tempdept);
                    // 生成部门表
                    File deptCSVFile = dataToCSVFile.DeptDataToCSV(rHRData);//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                    wxServiceClient.uploadFile(deptCSVFile);
                    // 4 融合人员数据并上传
                    if (WxServiceClient.isIsSuccessParty()) {
                        Thread.sleep(100000);
                        // 拉取正式环境人员数据
                        // List<UserlistBean> wxuserlist =
                        // wxServiceClient.getUserDetails("",1);
                        ReturnVo<List<PersonalForImport>> ehrPersons = personService.getEHRPersonalData();//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                        if (null != ehrPersons) {
                            Map<String, UserlistBean> updateCollectMap = new HashMap<String, UserlistBean>();
                            List<PersonalForImport> ehrPersonList = ehrPersons.getData();
                            // setPersons(ehrPersonList);//放在亚厦装饰
                            for (UserlistBean personalForImport : userlist) {
                                updateCollectMap.put(personalForImport.userid.trim() + "-" + personalForImport.name.trim(), personalForImport);
                            }
                            // int count = 0;
                            for (PersonalForImport personalForImport : ehrPersonList) {
                                UserlistBean userlistbean = updateCollectMap.get(personalForImport.getPsn_id().trim() + "-" + personalForImport.getPsn_name().trim());
                                if (null != userlistbean) {
                                    personalForImport.setPsn_mobile(CommUtils.defaultIfEmpty(userlistbean.mobile, "").trim()); // 手机号
                                    personalForImport.setPsn_email(CommUtils.defaultIfEmpty(userlistbean.email, "").trim()); // 邮箱
                                    // personalForImport.setPsn_deptcode(null!=userlistbean.department
                                    // && userlistbean.department.size()>0?
                                    // userlistbean.department.get(0).toString():"");//所在部门
                                    personalForImport.setPsn_postname(CommUtils.defaultIfEmpty(userlistbean.position, "").trim()); // 职位
                                    personalForImport.setWeixinid(CommUtils.defaultIfEmpty(userlistbean.weixinid, "").trim()); // 微信号
                                    if (null != userlistbean.extattr && null != userlistbean.extattr.attrs && userlistbean.extattr.attrs.size() > 0) {
                                        List<AttrsBean> attrsBean = userlistbean.extattr.attrs;
                                        for (AttrsBean attrsBeanItem : attrsBean) {
                                            if ("所在办公地".equals(attrsBeanItem.name)) {
                                                personalForImport.setLocaloffice(attrsBeanItem.value);
                                            } else if ("全称".equals(attrsBeanItem.name)) {
                                                personalForImport.setFullName(attrsBeanItem.value);
                                            } else if ("简称".equals(attrsBeanItem.name)) {
                                                personalForImport.setShortName(attrsBeanItem.value);
                                            } else if ("排序号".equals(attrsBeanItem.name)) {
                                                if (CommUtils.isNotEmpty(attrsBeanItem.value)) {
                                                    personalForImport.setOrderNo(Integer.valueOf(attrsBeanItem.value));
                                                }
                                            } else if ("是否可见".equals(attrsBeanItem.name)) {
                                                personalForImport.setIsvisible(attrsBeanItem.value);
                                            } else if ("固定电话".equals(attrsBeanItem.name)) {
                                                personalForImport.setPhone(attrsBeanItem.value);
                                            } else if ("座机短号".equals(attrsBeanItem.name)) {
                                                personalForImport.setPhoneShort(attrsBeanItem.value);
                                            } else if ("工号".equals(attrsBeanItem.name)) {
                                                personalForImport.setPsn_code(attrsBeanItem.value);
                                            } else if ("手机短号".equals(attrsBeanItem.name)) {
                                                personalForImport.setPhoneShort(attrsBeanItem.value);
                                            }
                                        }
                                    }
                                    matchedfw.write(personalForImport + "\n");
                                } else {
                                    unmatchedfw.write(personalForImport + "\n");
                                }
                            }

                            ReturnVo<List<PersonalForImport>> pVo = new ReturnVo<List<PersonalForImport>>();
                            pVo.setData(ehrPersonList.subList(0,ehrPersonList.size()>=200?200: ehrPersonList.size()));
                            File file = dataToCSVFile.PersonDataToCSV(pVo);
                            wxServiceClient.uploadFileSysUser(file);

                            endRecordOperation("1970-01-01 00:00:00", 1);// person

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
    public void testMDMData() {
        try {
            ReturnVo<List<DepartmentForImport>> rHRData = deptService.getMDMDeptData();//poll dept data  from MDM
            System.out.println(rHRData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWeinxinData() {
        try {
            List<UserlistBean> userlist = wxServiceClient.getUserDetails("", 1);
            System.out.println(userlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
