package com.ys.wx.service.person;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ys.wx.model.person.PersonalForImport;
import com.ys.wx.model.person.PersonalWraper;
import com.ys.wx.service.deptandperson.IQueryInfoWXService;
import com.ys.wx.service.deptandperson.IQueryInfoWXServicePortType;
import com.ys.wx.utils.CommUtils;
import com.ys.wx.utils.XStreamUtil;
import com.ys.wx.vo.ReturnVo;

/**
 * 人员数据拉取实 现
 * @author wangzequan
 *@Copyright:Copyright (c) 二龙湖基地组织  2015 ~ 2016 版权所有
 */

@Service
public class PersonServiceImpl implements IPersonService {

    private static final Logger logger = Logger.getLogger(PersonServiceImpl.class);
    @Resource
    private IQueryInfoWXService iQueryInfowxService;

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

    private PersonalWraper getEHRPersonalDataByPage(Integer pageIndex) throws Exception {
        IQueryInfoWXServicePortType type = iQueryInfowxService.getIQueryInfoWXServiceSOAP11PortHttp();
        String dateStr = "1970-01 00:00:00";
//        String s = type.getPsndocsByTime(dateStr, pageIndex);
        //getPsndocsByTimeAndStatusWX(时间字符串，默认值为0，第几页的数据)[该方法，默认每次打印500条数据，且页数是从0开始计数]
        String s = type.getPsndocsByTimeAndStatusWX(dateStr, 0, pageIndex);
        return XStreamUtil.toBean(s, PersonalWraper.class);
    }

    
    
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
    
    
    
    public  ReturnVo<List<PersonalForImport>> getEHRPersonalDataByTime(String  time) throws Exception {
    	 ReturnVo<List<PersonalForImport>> rVo = new ReturnVo<List<PersonalForImport>>(0, "失败");
        IQueryInfoWXServicePortType type = iQueryInfowxService.getIQueryInfoWXServiceSOAP11PortHttp();
        //String dateStr = "1970-01 00:00:00";
//        String s = type.getPsndocsByTime(dateStr, pageIndex);
        //getPsndocsByTimeAndStatusWX(时间字符串，默认值为0，第几页的数据)[该方法，默认每次打印500条数据，且页数是从0开始计数]
        int pageindex= 0;
        String s = type.getPsndocsByTimeAndStatusWX(time, 0, pageindex);
        PersonalWraper personalwraper =  XStreamUtil.toBean(s, PersonalWraper.class);
        if(null!=personalwraper && null!=personalwraper.getPersonals() && personalwraper.getPersonals().size()==500){
        	String s1   = type.getPsndocsByTimeAndStatusWX(time, 0, pageindex++);
        	if(CommUtils.isNotEmpty(s1) ){
        		PersonalWraper personalwraper1 =  XStreamUtil.toBean(s1, PersonalWraper.class);
        		if(null!=personalwraper1 && CollectionUtils.isNotEmpty(personalwraper1.getPersonals()) ){
        			personalwraper.getPersonals().addAll(personalwraper1.getPersonals());
        		}
        	}
        }
        rVo.setData(personalwraper.getPersonals());
        return rVo;
    }
    
}
