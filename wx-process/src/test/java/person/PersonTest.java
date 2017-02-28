package person;

import com.ys.wx.model.person.PersonalForImport;
import com.ys.wx.service.person.IPersonService;
import com.ys.wx.vo.ReturnVo;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * Title : 微信API客户端测试类(用于测试环境运行)
 * Description :
 * Author : Ceaser wang Jerry xu    date : 2017/1/12
 * Update :                         date :
 * Version : 1.0.0
 * Copyright (c) 二龙湖基地组织  2016 ~ 2017 版权所有
 */
public class PersonTest {


    private IPersonService ipersonservice;

    @Before
    public void init() throws Exception {
        @SuppressWarnings("resource")
        ApplicationContext ac = new ClassPathXmlApplicationContext(
                "file:src/main/resources/spring_common.xml",
                "file:src/main/resources/wx_dubbo.xml",
                "file:src/main/resources/wx_dubbo_customer.xml");
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
