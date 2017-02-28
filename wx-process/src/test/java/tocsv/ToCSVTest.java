package tocsv;

import com.ys.wx.function.DataToCSVFile;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Title : 生成CSV表
 * Description :
 * Author : Jerry xu    date : 2017/1/11
 * Update :             date :
 * Version : 1.0.0
 */
public class ToCSVTest {

    private DataToCSVFile datatocsvfile;

    @Before
    public void init() throws Exception {
        @SuppressWarnings("resource")
        ApplicationContext ac = new ClassPathXmlApplicationContext(
                "file:src/main/resources/spring_common.xml",
                "file:src/main/resources/wx_dubbo.xml",
                "file:src/main/resources/wx_dubbo_customer.xml");
        datatocsvfile = (DataToCSVFile) ac.getBean("dataToCSVFile");
    }


    @Test
    public void toCSVTest() {
        datatocsvfile.DataToCSV();
    }
}
