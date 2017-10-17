package person;

import com.mhome.tools.vo.ReturnVo;
import com.ys.wx.function.EHRDataToCSVFile;
import com.ys.wx.function.WeChatDataToCSVFile;
import com.ys.wx.model.person.PersonalForImport;
import com.ys.wx.model.wx.response.UserDetailsResponse;
import com.ys.wx.service.ehrperson.IPersonService;
import com.ys.wx.service.wx.IWxServiceClient;
import com.ys.wx.utils.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.util.List;

/**
 * @Title:
 * @Description:
 * @Author:wangzequan
 * @Since:2016年11月25日 下午4:13:04
 * @Version:1.1.0
 * @Copyright:Copyright (c) 浙江蘑菇加电子商务有限公司 2015 ~ 2016 版权所有
 */
public class PersonTest {


    IPersonService      ipersonservice;
    IWxServiceClient    wxServiceClient;
    EHRDataToCSVFile    dataToCSVFile;
    WeChatDataToCSVFile weChatDataToCSVFile;

    private String access_token = "";
    private long tokenTime; //记录获取token的时间

    @Before
    public void init() throws Exception {
        @SuppressWarnings("resource")
        ApplicationContext ac = new ClassPathXmlApplicationContext(
                "file:src/main/resources/spring_common.xml",
                "file:src/main/resources/wx_dubbo.xml",
                "file:src/main/resources/wx_dubbo_customer.xml");
        ipersonservice = (IPersonService) ac.getBean("ehrPersonService");
        wxServiceClient = (IWxServiceClient) ac.getBean("wxServiceClient");
        dataToCSVFile = (EHRDataToCSVFile) ac.getBean("EHRDataToCSVFile");
        weChatDataToCSVFile = (WeChatDataToCSVFile) ac.getBean("weChatDataToCSVFile");
    }

    /**
     * ehr数据生成人员表
     */
    @Test
    public void testGetPsndocsByTime() {
        try {
            ReturnVo<List<PersonalForImport>> rvo = ipersonservice.getEHRPersonalData();
            File personCSVFile = dataToCSVFile.PersonDataToCSV(rvo);
            System.out.println("成功生成" + personCSVFile.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * weChat微信数据生成人员表
     */
    @Test
    public void testGetWeiXinPersonalData() {
        if (!wxServiceClient.verifyTokenValid(tokenTime, access_token)) {
            access_token = wxServiceClient.getToken();
        }
        //department_id:表示获取当前所在部门的人员，根据实际情况改写该值，当前环境1表示整个顶层部门
        List<UserDetailsResponse.UserlistBean> rvo = wxServiceClient.getUserDetails(access_token, 1);
       // File deptCSVFile = weChatDataToCSVFile.WeChatPersonDataToCSV(rvo);
      //  System.out.println("成功生成微信数据表" + deptCSVFile.getName());
    }

    /**
     * 只同步user表
     */
    @Test
    public void testUploadDeptDataToTestEnvironment() {
        File file = FileUtils.getFiles(FileUtils.storageFilePath(), "user");
        wxServiceClient.uploadFile(file);
    }

    /**
     * 删除人员
     */
    @Test
    public void testDeletePersonal() {

    }

}
