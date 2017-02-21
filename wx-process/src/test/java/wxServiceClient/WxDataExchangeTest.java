package wxServiceClient;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ys.wx.api.IWxJobApi;
import com.ys.wx.function.DataToCSVFile;
import com.ys.wx.model.common.SynDataRecord;
import com.ys.wx.model.dept.DepartmentForImport;
import com.ys.wx.model.person.PersonalForImport;
import com.ys.wx.model.wx.response.PartyListResponse;
import com.ys.wx.model.wx.response.PartyListResponse.Department;
import com.ys.wx.model.wx.response.UserDetailsResponse;
import com.ys.wx.model.wx.response.UserDetailsResponse.UserlistBean;
import com.ys.wx.model.wx.response.UserDetailsResponse.UserlistBean.ExtattrBean.AttrsBean;
import com.ys.wx.service.common.ISynDataRecordService;
import com.ys.wx.service.dept.IDeptService;
import com.ys.wx.service.person.IPersonService;
import com.ys.wx.service.wx.IWxServiceClient;
import com.ys.wx.service.wx.WxServiceClient;
import com.ys.wx.utils.CommUtils;
import com.ys.wx.vo.ReturnVo;

/**
 * 数据切换 逻辑测试类(用于正式环境运行)
 * 
 * @Title:
 * @Description:
 * @Author:wangzequan
 * @Since:2017年2月17日 上午9:50:21
 * @Version:1.1.0
 * @Copyright:Copyright (c) 二龙湖基地组织 2015 ~ 2016 版权所有
 */
public class WxDataExchangeTest {

	IWxServiceClient wxServiceClient;

	DataToCSVFile dataToCSVFile;

	private IPersonService personService;
	private IDeptService deptService;

	ISynDataRecordService synDataRecordService;

	IWxJobApi wxJobApi;

	@Before
	public void init() throws Exception {
		@SuppressWarnings("resource")
		ApplicationContext ac = new ClassPathXmlApplicationContext(
				new String[] { "file:src/main/resources/spring_common.xml", "file:src/main/resources/wx_dubbo.xml",
						"file:src/main/resources/wx_dubbo_customer.xml" });

		wxServiceClient = (IWxServiceClient) ac.getBean("wxServiceClient");
		dataToCSVFile = (DataToCSVFile) ac.getBean("dataToCSVFile");
		personService = (IPersonService) ac.getBean("personServiceImpl");
		deptService = (IDeptService) ac.getBean("deptServiceImpl");
		synDataRecordService = (ISynDataRecordService) ac.getBean("synDataRecordServiceImpl");
		wxJobApi = (IWxJobApi) ac.getBean("wxJobApiImpl");
	}

	/**
	 * 删除测试环境的组织部门数据
	 * 
	 * @Description:
	 * @author wangzequan 2017 上午11:50:24
	 */
	private synchronized void getParty(String token, int id, final int excludeId) {
		ArrayList<Department> departments = wxServiceClient.getParty(token, id);
		if (CollectionUtils.isNotEmpty(departments)) {
			for (int i = 0; i < departments.size(); i++) {
				if (id == departments.get(i).id) {
					continue;
				} else {
					if (departments.get(i).id != excludeId) {
						if (!wxServiceClient.deleteParty(token, departments.get(i).id + "")) {
							getParty(token, departments.get(i).id, excludeId);
						}
					}
				}
			}
		}
	}

	/**
	 * 删除测试环境的组织数据
	 * 
	 * @Description:
	 * @author wangzequan 2017 下午4:25:39
	 */
	public void deleteTestEnvironmentOrganizationData(final int excludeId) {
		// String token = wxServiceClient.getToken();

		for (int i = 0; i < 60; i++) {
			Thread t = new Thread(new Runnable() {
				public void run() {
					getParty("", 1, excludeId);
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

	private void setPersons(List<PersonalForImport> ehrPersonList) {
		// 第一个人（存在）
		/*
		 * PersonalForImport personalforimport = new PersonalForImport();
		 * personalforimport.setPsn_id("ceaser");
		 * personalforimport.setPsn_name("王泽全");
		 * personalforimport.setPsn_mobile("13758256466");
		 * personalforimport.setPsn_deptcode(200001+"");
		 * personalforimport.setWeixinid("wzq657");
		 * personalforimport.setPsn_email("1156721874@qq.com");
		 * ehrPersonList.add(0,personalforimport);
		 * 
		 * //第二个人不存在 PersonalForImport personalforimport1 = new
		 * PersonalForImport(); personalforimport1.setPsn_id("ceaser1");
		 * personalforimport1.setPsn_name("王泽全1");
		 * personalforimport1.setPsn_mobile("13758256455");
		 * personalforimport1.setPsn_deptcode(2+"");
		 * personalforimport1.setWeixinid("wzq6571");
		 * personalforimport1.setPsn_email("1156721875@qq.com");
		 * ehrPersonList.add(1,personalforimport1);
		 */

		PersonalForImport personalforimport1 = new PersonalForImport();
		personalforimport1.setPsn_id("ceaser");
		personalforimport1.setPsn_name("ceaser");
		personalforimport1.setPsn_mobile("13758256455");
		personalforimport1.setPsn_deptcode(1 + "");
		personalforimport1.setWeixinid("wzq6577");
		personalforimport1.setPsn_email("1156721875@qq.com");
		personalforimport1.setPsn_postname("0");
		personalforimport1.setLocaloffice("0");
		personalforimport1.setFullName("0");
		personalforimport1.setShortName("0");
		personalforimport1.setOrderNo(0);
		personalforimport1.setIsvisible("0");
		personalforimport1.setPsn_officephone("0");
		personalforimport1.setPhoneShort("0");
		personalforimport1.setMobileShortNo("0");
		personalforimport1.setPsn_code("0");
		ehrPersonList.add(0, personalforimport1);
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
				deleteTestEnvironmentOrganizationData(department.id);
				// 3上传新的组织
				ReturnVo<List<DepartmentForImport>> rHRData = deptService.getEHRDeptData();
				if (null != rHRData && CollectionUtils.isNotEmpty(rHRData.getData())) {
					// 为了防止删除临时部门，加入临时部门
					rHRData.getData().add(tempdept);
					// 生成部门表
					File deptCSVFile = dataToCSVFile.DeptDataToCSV(rHRData);
					wxServiceClient.uploadFile(deptCSVFile);
					// 4 融合人员数据并上传
					if (WxServiceClient.isIsSuccessParty()) {
						Thread.sleep(100000);
						// 拉取正式环境人员数据
						// List<UserlistBean> wxuserlist =
						// wxServiceClient.getUserDetails("",1);
						ReturnVo<List<PersonalForImport>> ehrPersons = personService.getEHRPersonalData();
						if (null != ehrPersons) {
							Map<String, UserlistBean> updateCollectMap = new HashMap<String, UserlistBean>();
							List<PersonalForImport> ehrPersonList = ehrPersons.getData();
							// setPersons(ehrPersonList);//放在亚厦装饰
							for (UserlistBean personalForImport : userlist) {
								updateCollectMap.put(
										personalForImport.userid.trim() + "-" + personalForImport.name.trim(),
										personalForImport);
							}
							// int count = 0;
							for (PersonalForImport personalForImport : ehrPersonList) {
								UserlistBean userlistbean = updateCollectMap.get(personalForImport.getPsn_id().trim()
										+ "-" + personalForImport.getPsn_name().trim());
								if (null != userlistbean) {
									personalForImport
											.setPsn_mobile(CommUtils.defaultIfEmpty(userlistbean.mobile, "").trim()); // 手机号
									personalForImport
											.setPsn_email(CommUtils.defaultIfEmpty(userlistbean.email, "").trim()); // 邮箱
									// personalForImport.setPsn_deptcode(null!=userlistbean.department
									// && userlistbean.department.size()>0?
									// userlistbean.department.get(0).toString():"");//所在部门
									personalForImport.setPsn_postname(
											CommUtils.defaultIfEmpty(userlistbean.position, "").trim()); // 职位
									personalForImport
											.setWeixinid(CommUtils.defaultIfEmpty(userlistbean.weixinid, "").trim()); // 微信号
									if (null != userlistbean.extattr && null != userlistbean.extattr.attrs
											&& userlistbean.extattr.attrs.size() > 0) {
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
							pVo.setData(ehrPersonList/*
														 * .subList(0,
														 * ehrPersonList.size()>
														 * =200?200:
														 * ehrPersonList.size())
														 */);
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
	 * 每天定时任务拉取数据同步到微信
	 * 
	 * @Description:
	 * @author wangzequan 2017 上午9:51:14
	 */

	@Test
	public void commonProcess() {
		try {
			String token = wxServiceClient.getToken();
			ReturnVo<List<DepartmentForImport>> rHRData = deptService.getEHRDeptData();
			// 计算新老数据的交集做更新，新数据没有在老数据中找到的做添加
			if (null != rHRData && CollectionUtils.isNotEmpty(rHRData.getData())) {
				List<DepartmentForImport> fireDepartmentForImports = rHRData.getData();
				ArrayList<Department> weixindepts = wxServiceClient.getParty(token, 1);
				Map<Integer, Department> weixindeptsMap = new HashMap<Integer, Department>();
				for (Department department : weixindepts) {
					weixindeptsMap.put(department.id, department);
				}
				List<Department> updatelist = new ArrayList<Department>();
				List<Department> addlist = new ArrayList<Department>();
				for (DepartmentForImport departmentitem : fireDepartmentForImports) {
					Department department = weixindeptsMap.get(Integer.valueOf(departmentitem.getDept_id()));
					// 新旧数据得到匹配,如果姓名、父级部门‘排序编号不一样 就会触发部门的修改
					if (null != department) {
						boolean flag = false;
						if (!departmentitem.getDept_name().equals(department.name)) {
							department.name = departmentitem.getDept_name();
							flag = true;
						}
						if (CommUtils.isNotEmpty(departmentitem.getDept_fid())
								&& !departmentitem.getDept_fid().equals(department.parentid + "")) {
							department.parentid = Integer.valueOf(departmentitem.getDept_fid());
							flag = true;
						}
						if (CommUtils.isNotEmpty(departmentitem.getDept_order())
								&& !departmentitem.getDept_order().equals(department.order + "")) {
							department.order = Integer.valueOf(departmentitem.getDept_order());
							flag = true;
						}
						if (flag) {
							updatelist.add(department);
						}
					} else {
						// 没有匹配到的作为 添加操作
						addlist.add(department);
					}
				}
				// 分别调用添加和修改部门的单个API接口同步数据
				for (Department department : updatelist) {
					System.out.println("update department : >>> " + department);
					wxServiceClient.updateOneParty(token, department);
				}
				for (Department department : addlist) {
					System.out.println("add department : >>> " + department);
					wxServiceClient.createParty(token, department);
				}
				// 增量获取EHR数据，然后以增量更新的方式同步到微信公众号
				String maxEndTime = startRecordOperation();
				ReturnVo<List<PersonalForImport>> pvo = personService.getEHRPersonalDataByTime(maxEndTime);
				if (CollectionUtils.isNotEmpty(pvo.getData())) {
					File file = dataToCSVFile.PersonDataToCSV(pvo);
					wxServiceClient.uploadFileSysUser(file);
					endRecordOperation(maxEndTime, 1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param updateList
	 * @param addList
	 * @param updateCollectMap
	 * @param count
	 * @param personalForImport
	 * @return
	 * @throws NumberFormatException
	 * @Description:
	 * @author wangzequan 2017 下午9:05:07
	 */

	private void processMatchPerson(List<UserlistBean> updateList, List<UserlistBean> addList,
			Map<String, UserlistBean> updateCollectMap, PersonalForImport personalForImport)
			throws NumberFormatException {
		UserlistBean userlistbean = updateCollectMap
				.get(personalForImport.getPsn_id().trim() + "-" + personalForImport.getPsn_name().trim());
		if (null != userlistbean) {
			if (null == userlistbean.department) {
				userlistbean.department = new ArrayList<Integer>();
			}
			userlistbean.department.add(Integer.valueOf(personalForImport.getPsn_deptcode().trim()));
			userlistbean.email = personalForImport.getPsn_email();
			userlistbean.mobile = personalForImport.getPsn_mobile();
			userlistbean.weixinid = personalForImport.getWeixinid();
			if (null != userlistbean.extattr && CollectionUtils.isNotEmpty(userlistbean.extattr.attrs)) {
				List<AttrsBean> attrsbeans = userlistbean.extattr.attrs;
				// 所在办公地 全称"; 简称"; 排序号"; 是否可见"; 固定电话"; 座机短号"; 工号"; 手机短号";
				for (AttrsBean attrsBean : attrsbeans) {
					if ("所在办公地".equals(attrsBean.name)) {
						attrsBean.name = personalForImport.getLocaloffice();
					} else if ("全称".equals(attrsBean.name)) {
						attrsBean.name = personalForImport.getFullName();
					} else if ("简称".equals(attrsBean.name)) {
						attrsBean.name = personalForImport.getShortName();
					} else if ("排序号".equals(attrsBean.name)) {
						attrsBean.name = personalForImport.getOrderNo() + "";
					} else if ("是否可见".equals(attrsBean.name)) {
						attrsBean.name = personalForImport.getIsvisible();
					} else if ("固定电话".equals(attrsBean.name)) {
						attrsBean.name = personalForImport.getPhone();
					} else if ("座机短号".equals(attrsBean.name)) {
						attrsBean.name = personalForImport.getPhoneShort();
					} else if ("工号".equals(attrsBean.name)) {
						attrsBean.name = personalForImport.getPsn_code();
					} else if ("手机短号".equals(attrsBean.name)) {
						attrsBean.name = personalForImport.getMobileShortNo();
					}
				}
			}
			updateList.add(userlistbean);
		} else {
			UserlistBean newuserlistbean = new UserDetailsResponse().new UserlistBean();
			newuserlistbean.userid = personalForImport.getPsn_id().trim();
			newuserlistbean.name = personalForImport.getPsn_name();
			newuserlistbean.department = new ArrayList<Integer>();
			newuserlistbean.department
					.add(Integer.parseInt(CommUtils.defaultIfEmpty(personalForImport.getPsn_deptcode(), "").trim()));
			newuserlistbean.mobile = CommUtils.defaultIfEmpty(personalForImport.getPsn_mobile(), "").trim();
			newuserlistbean.email = CommUtils.defaultIfEmpty(personalForImport.getPsn_email(), "").trim();
			AttrsBean attrsbean = new UserDetailsResponse().new UserlistBean().new ExtattrBean().new AttrsBean();
			for (int i = 0; i < 9; i++) {
				switch (i) {
				case 0:
					attrsbean.name = "所在办公地";
					attrsbean.value = personalForImport.getLocaloffice();
					continue;
				case 1:
					attrsbean.name = "全称";
					attrsbean.value = personalForImport.getFullName();
					continue;
				case 2:
					attrsbean.name = "简称";
					attrsbean.value = personalForImport.getShortName();
					continue;
				case 3:
					attrsbean.name = "排序号";
					attrsbean.value = personalForImport.getOrderNo() + "";
					continue;
				case 4:
					attrsbean.name = "是否可见";
					attrsbean.value = personalForImport.getIsvisible();
					continue;
				case 5:
					attrsbean.name = "固定电话";
					attrsbean.value = personalForImport.getPhone();
					continue;
				case 6:
					attrsbean.name = "座机短号";
					attrsbean.value = personalForImport.getPhoneShort();
					continue;
				case 7:
					attrsbean.name = "工号";
					attrsbean.value = personalForImport.getPsn_code();
					continue;
				case 8:
					attrsbean.name = "手机短号";
					attrsbean.value = personalForImport.getMobileShortNo();
					continue;
				}
			}
			newuserlistbean.extattr = new UserDetailsResponse().new UserlistBean().new ExtattrBean();
			newuserlistbean.extattr.attrs = new ArrayList<UserDetailsResponse.UserlistBean.ExtattrBean.AttrsBean>();
			newuserlistbean.extattr.attrs.add(attrsbean);
			addList.add(newuserlistbean);
		}
	}

	/**
	 * @return
	 * @throws Exception
	 * @Description:
	 * @author wangzequan 2017 下午12:48:35
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
	public void testDao() {
		try {
			List<SynDataRecord> synDataRecords = synDataRecordService.getAll(new SynDataRecord());
			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test() {
		try {
			String email = "11*56\"7?'21•8[,?'‘’`·，。\\/;；：-+()*]74,@qq.com";
			System.out.println(email.replaceAll("[,\\[\\]?'‘’`·，。\\\\/;；：•\\+()\\-\\*\"]", ""));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testIWxJobApi() {
		wxJobApi.processDataToWeiXin();
	}

/**
 * 将微信服务端的人员信息 除了组织架构、人员id、姓名，其他的字段按照外部数据进行更新
 * @Description:
 * @author wangzequan 2017 上午11:09:11
 */
	
	@Test
	public void doFlushPersonInfo() {
		try {
			ReturnVo<List<PersonalForImport>> ehrPersons = personService.getEHRPersonalData();
			if (null != ehrPersons) {
				Map<String, UserlistBean> updateCollectMap = new HashMap<String, UserlistBean>();
				List<PersonalForImport> ehrPersonList = ehrPersons.getData();
				List<PersonalForImport> ehrAddPersonList = new ArrayList<PersonalForImport>();
				setPersons(ehrPersonList);//放在亚厦装饰
				List<UserlistBean> userlist = wxServiceClient.getUserDetails("", 1);
				for (UserlistBean personalForImport : userlist) {
					updateCollectMap.put(personalForImport.userid.trim() + "-" + personalForImport.name.trim(),personalForImport);
				}
				// int count = 0;
				for (PersonalForImport personalForImport : ehrPersonList) {
					UserlistBean userlistbean = updateCollectMap.get(personalForImport.getPsn_id().trim() + "-" + personalForImport.getPsn_name().trim());
					if (null != userlistbean) {
						personalForImport.setPsn_deptcode(userlistbean.department.get(0)+"");
						ehrAddPersonList.add(personalForImport);
					}
				}
				
				ReturnVo<List<PersonalForImport>> pVo = new ReturnVo<List<PersonalForImport>>();
				pVo.setData(ehrAddPersonList/*.subList(0, ehrPersonList.size()>=200?200:ehrPersonList.size())*/);
				File file = dataToCSVFile.PersonDataToCSV(pVo);
				wxServiceClient.uploadFileSysUser(file);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
