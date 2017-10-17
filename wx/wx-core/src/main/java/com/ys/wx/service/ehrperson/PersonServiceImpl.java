package com.ys.wx.service.ehrperson;

import com.mhome.tools.vo.ReturnVo;
import com.ys.ucenter.api.IOrgApi;
import com.ys.ucenter.model.user.Department;
import com.ys.ucenter.model.user.Personnel;
import com.ys.wx.constant.WxConstant;
import com.ys.wx.model.person.PersonalForImport;
import com.ys.wx.model.person.PersonalWraper;
import com.ys.wx.service.data.ehr.IQueryInfoWXService;
import com.ys.wx.service.data.ehr.IQueryInfoWXServicePortType;
import com.ys.wx.utils.CommUtils;
import com.ys.wx.utils.XStreamUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 获取人员信息
 */
@Service(value="ehrPersonService")
public class PersonServiceImpl implements IPersonService {

    private static final Logger logger = Logger.getLogger(PersonServiceImpl.class);
    @Resource
    IOrgApi   orgApi;
    @Resource
    private IQueryInfoWXService iQueryInfowxService;
    
    /**
     * 获取人员信息
     * @return 人员信息对象
     * @throws Exception
     */
	@Deprecated
    @Override
    public ReturnVo<List<PersonalForImport>> getEHRPersonalData() throws Exception {
        logger.info(">>>>>>>>>>>>>开始获取人员信息数据>>>>>>>>>>>>>\n");
        ReturnVo<List<PersonalForImport>> rVo = new ReturnVo<List<PersonalForImport>>(0, "失败");
        //第一次请求，获取到count数量
        PersonalWraper personalwraper = getEHRPersonalDataByPage(0);
        int count = Integer.parseInt(personalwraper.getCount());
        int pages;
        List<PersonalForImport> list = new ArrayList<PersonalForImport>();
        //把第一次请求的结果存起来
        list.addAll(personalwraper.getPersonals());
        //根据数据进行计算出分页数
        if (count < 500) {
            pages = 0;
        } else {
            //计算数据分页，并循环取出数据
            pages = count % 500 == 0 ? count / 500 - 1 : count / 500;
            for (int i = 1; i <= pages; i++) {
                PersonalWraper personal = getEHRPersonalDataByPage(i);
                list.addAll(personal.getPersonals());
            }
        }
        rVo.setData(list);
        rVo.setCode(1);
        rVo.setMsg("成功");
        logger.info("总计获取:" + list.size() + "条数据；\n实际分页(从0开始计算)：" + pages + "\n>>>>>>>>>>>>>完成获取人员信息数据>>>>>>>>>>>>>\n");
        return rVo;
    }
    /**
     * 获取人员信息
     * @return 人员信息对象
     * @throws Exception
     */
	@Deprecated
    public ReturnVo<List<PersonalForImport>> getMDMPersonalData() throws Exception {
    	logger.info(">>>>>>>>>>>>>开始获取人员信息数据>>>>>>>>>>>>>\n");
    	ReturnVo<List<PersonalForImport>> rVo = new ReturnVo<List<PersonalForImport>>(0, "失败");
    	com.ys.tools.vo.ReturnVo<Personnel> personnelsvo = orgApi.getAllPersonnel(new Personnel());
    	List<PersonalForImport> personalForImportList = new ArrayList<PersonalForImport>();
    	if(null!=personnelsvo && personnelsvo.getCode().intValue()==WxConstant.SUCCESS){
    		Map<String,String> dIdToTempNoMap = new HashMap<String, String>();
    		com.ys.tools.vo.ReturnVo<Department>  departmentVo= orgApi.getAllDepartment(new Department());
    		if(null!=departmentVo && departmentVo.getCode().intValue()==WxConstant.SUCCESS){
    			List<Department> departmentList= departmentVo.getDatas();
    			for (Department department : departmentList) {
    				dIdToTempNoMap.put(department.getId(), department.getTempNo());
				}
    		}
    		
    		List<Personnel> personals =  personnelsvo.getDatas();
    		PersonalForImport itemPersonalForImport = null;
    		for (Personnel personnel : personals) {
    			itemPersonalForImport = new PersonalForImport();
    			itemPersonalForImport.setPsn_name(personnel.getName());
    			itemPersonalForImport.setPsn_mobile(personnel.getSelfMobile());
    			itemPersonalForImport.setPsn_email(personnel.getSelfEmail());
//    			itemPersonalForImport.setPsn_id("设置身份证号码");
    			itemPersonalForImport.setPsn_deptcode(dIdToTempNoMap.get(personnel.getDeptId()));
    			itemPersonalForImport.setPhoneShort(personnel.getShortPhone());
    			itemPersonalForImport.setPsn_postname(personnel.getPosition());
    			itemPersonalForImport.setPsn_code(personnel.getNo());
			}
    	}
    	rVo.setData(personalForImportList);
    	rVo.setCode(1);
    	rVo.setMsg("成功");
    	return rVo;
    }

    /**
     * 分页获取人员信息
     *
     * @param pageIndex
     * @return
     * @throws Exception
     */
	@Deprecated
    private PersonalWraper getEHRPersonalDataByPage(Integer pageIndex) throws Exception {
        IQueryInfoWXServicePortType type = iQueryInfowxService.getIQueryInfoWXServiceSOAP11PortHttp();
        String dateStr = "1970-01 00:00:00";
//        String s = type.getPsndocsByTime(dateStr, pageIndex);
        //getPsndocsByTimeAndStatusWX(时间字符串，默认值为0，第几页的数据)[该方法，默认每次打印500条数据，且页数是从0开始计数]
        String s = type.getPsndocsByTimeAndStatusWX(dateStr, 0, pageIndex);
        return XStreamUtil.toBean(s, PersonalWraper.class);
    }


    /**
     * @param page
     * @return
     * @throws Exception
     */
	@Deprecated
    public ReturnVo<List<PersonalForImport>> getEHRPersonalData(int page) throws Exception {
        logger.info(">>>>>>>>>>>>>开始获取人员信息数据>>>>>>>>>>>>>\n");
        ReturnVo<List<PersonalForImport>> rVo = new ReturnVo<List<PersonalForImport>>(0, "失败");
        List<PersonalForImport> list = new ArrayList<PersonalForImport>();
        PersonalWraper personal = getEHRPersonalDataByPage(page);
        list.addAll(personal.getPersonals());
        rVo.setData(list);
        rVo.setCode(1);
        rVo.setMsg("成功");
        return rVo;
    }


    /**
     * @param time
     * @return
     * @throws Exception
     */
	@Deprecated
    public ReturnVo<List<PersonalForImport>> getEHRPersonalDataByTime(String time) throws Exception {
        ReturnVo<List<PersonalForImport>> rVo = new ReturnVo<List<PersonalForImport>>(0, "失败");
        IQueryInfoWXServicePortType type = iQueryInfowxService.getIQueryInfoWXServiceSOAP11PortHttp();
        //String dateStr = "1970-01 00:00:00";
//        String s = type.getPsndocsByTime(dateStr, pageIndex);
        //getPsndocsByTimeAndStatusWX(时间字符串，默认值为0，第几页的数据)[该方法，默认每次打印500条数据，且页数是从0开始计数]
        int pageIndex = 0;
        String s = type.getPsndocsByTimeAndStatusWX(time, 0, pageIndex);
        PersonalWraper personalwraper = XStreamUtil.toBean(s, PersonalWraper.class);
        if (null != personalwraper && null != personalwraper.getPersonals() && personalwraper.getPersonals().size() == 500) {
            String s1 = type.getPsndocsByTimeAndStatusWX(time, 0, pageIndex);
            if (CommUtils.isNotEmpty(s1)) {
                PersonalWraper personal = XStreamUtil.toBean(s1, PersonalWraper.class);
                if (null != personal && CollectionUtils.isNotEmpty(personal.getPersonals())) {
                    personalwraper.getPersonals().addAll(personal.getPersonals());
                }
            }
        }
        rVo.setData(personalwraper != null ? personalwraper.getPersonals() : null);
        return rVo;
    }

}
