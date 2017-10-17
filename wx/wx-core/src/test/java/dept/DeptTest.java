package dept;

import com.mhome.tools.vo.ReturnVo;
import com.ys.wx.function.EHRDataToCSVFile;
import com.ys.wx.function.WeChatDataToCSVFile;
import com.ys.wx.model.dept.DepartmentForImport;
import com.ys.wx.model.wx.response.PartyListResponse;
import com.ys.wx.service.dept.IDeptService;
import com.ys.wx.service.wx.IEnterpriseWeChatService;
import com.ys.wx.service.wx.IWxServiceClient;
import com.ys.wx.utils.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DeptTest {

    IDeptService             deptService;
    IWxServiceClient         wxServiceClient;
    IEnterpriseWeChatService weChatService;
    EHRDataToCSVFile         dataToCSVFile;
    WeChatDataToCSVFile      weChatDataToCSVFile;

    private String access_token = "";
    private long tokenTime; //记录获取token的时间

    @Before
    public void init() throws Exception {
        @SuppressWarnings("resource")
        ApplicationContext ac = new ClassPathXmlApplicationContext(
                "file:src/main/resources/spring_common.xml",
                "file:src/main/resources/wx_dubbo.xml",
                "file:src/main/resources/wx_dubbo_customer.xml");
        deptService = (IDeptService) ac.getBean("deptServiceImpl");
        wxServiceClient = (IWxServiceClient) ac.getBean("wxServiceClient");
        weChatService = (IEnterpriseWeChatService) ac.getBean("enterpriseWeChatServiceImpl");
        dataToCSVFile = (EHRDataToCSVFile) ac.getBean("EHRDataToCSVFile");
        weChatDataToCSVFile = (WeChatDataToCSVFile) ac.getBean("weChatDataToCSVFile");
    }

    /**
     * ehr数据生成组织表
     */
    @Test
    public void testGetEHRDeptData() {
        try {
            ReturnVo<List<DepartmentForImport>> rvo = deptService.getEHRDeptData();
            File deptCSVFile = dataToCSVFile.DeptDataToCSV(rvo);
            System.out.println("成功生成ehr数据表" + deptCSVFile.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * weChat微信数据生成组织表
     */
    @Test
    public void testGetWeiXinDeptData() {
        if (!wxServiceClient.verifyTokenValid(tokenTime, access_token)) {
            access_token = wxServiceClient.getToken();
        }
        ArrayList<PartyListResponse.Department> rvo = wxServiceClient.getParty(access_token, 1);
        File deptCSVFile = weChatDataToCSVFile.WeChatDeptDataToCSV(rvo);
        System.out.println("成功生成微信数据表" + deptCSVFile.getName());
    }

    /**
     * 只同步party表
     */
    @Test
    public void testUploadDeptDataToTestEnvironment() {
        File file = FileUtils.getFiles(FileUtils.storageFilePath(), "party");
        wxServiceClient.uploadFile(file);
    }

    /**
     * 删除组织部门，excludeId=0,表示从最顶层删除所有的部门
     */
    @Test
    public void testDeleteDept() {
        weChatService.deleteTestEnvironmentOrganizationData("",0);
    }


}
