package dept;

import com.ys.wx.model.dept.DepartmentForImport;
import com.ys.wx.service.dept.IDeptService;
import com.ys.wx.service.wx.IWxServiceClient;
import com.ys.wx.service.wx.WxServiceClient;
import com.ys.wx.vo.ReturnVo;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.util.List;

/**
 * Title : 生成CSV表
 * Description :
 * Author : Jerry xu    date : 2017/1/11
 * Update :             date :
 * Version : 1.0.0
 */
public class DeptTest {

    private IDeptService     deptService;
    private IWxServiceClient wxServiceClient;

    @Before
    public void init() throws Exception {
        @SuppressWarnings("resource")
        ApplicationContext ac = new ClassPathXmlApplicationContext(
                "file:src/main/resources/spring_common.xml",
                "file:src/main/resources/wx_dubbo.xml",
                "file:src/main/resources/wx_dubbo_customer.xml");
        deptService = (IDeptService) ac.getBean("deptServiceImpl");
        wxServiceClient = (IWxServiceClient) ac.getBean("wxServiceClient");
    }

    @Test
    public void testGetEHRDeptData() {
        try {
            ReturnVo<List<DepartmentForImport>> rvo = deptService.getEHRDeptData();
            System.out.println(rvo.getData());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdateDeptAndPersonDataToTestEnvironment() {
        File deptDataFile = new File("F://Out//party5437401530154456255.csv");
        File personDataFile = new File("F://Out//user6587454125567159188.csv");

        wxServiceClient.uploadFile(deptDataFile);
        if (WxServiceClient.isIsSuccessParty()) {
            System.out.println("上传部门数据到测试环境成功。。。。");
            wxServiceClient.uploadFile(personDataFile);
            System.out.println("上传人员数据到测试环境成功。。。。");
        }
        System.out.println();
    }

}
