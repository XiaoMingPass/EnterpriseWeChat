package person;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ys.wx.model.person.PersonalForImport;
import com.ys.wx.service.person.IPersonService;
import com.ys.wx.vo.ReturnVo;

/**
 * wsimport -keep -p org
 * http://10.10.20.1/uapws/service/nc.itf.ys.idm.IQueryOrgService?wsdl wsimport
 * -keep -p dept
 * http://10.10.20.1/uapws/service/nc.itf.ys.idm.IQueryDeptService?wsdl wsimport
 * -keep -p person
 * http://10.10.20.1/uapws/service/nc.itf.ys.idm.IQueryPsndocService?wsdl
 * wsimport -keep -p job
 * http://10.10.20.1/uapws/service/nc.itf.ys.idm.IQueryJobService?wsdl wsimport
 * -keep -p port
 * http://10.10.20.1/uapws/service/nc.itf.ys.idm.IQueryPostService?wsdl
 *
 * @Title:
 * @Description:
 * @Author:wangzequan
 * @Since:2016年11月25日 下午4:13:04
 * @Version:1.1.0
 * @Copyright:Copyright (c) 二龙湖基地组织  2015 ~ 2016 版权所有
 */
public class PersonTest {


    IPersonService ipersonservice;

    @Before
    public void init() throws Exception {
        @SuppressWarnings("resource")
        ApplicationContext ac = new ClassPathXmlApplicationContext(
                new String[]{"file:src/main/resources/spring_common.xml",
                        "file:src/main/resources/wx_dubbo.xml",
                        "file:src/main/resources/wx_dubbo_customer.xml"});
        ipersonservice = (IPersonService) ac.getBean("personServiceImpl");
    }

    @Test
    public void testGetPsndocsByTime() {
        try {
            ReturnVo<List<PersonalForImport>> rvo = ipersonservice.getEHRPersonalData();
            System.out.println(rvo.getData());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
