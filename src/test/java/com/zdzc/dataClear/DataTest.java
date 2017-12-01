package com.zdzc.dataClear;

import com.zdzc.dataClear.entity.Schema;
import com.zdzc.dataClear.service.DataClearService;
import com.zdzc.electrocar.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  * @Description: 类的描述
 *  * @author chengwengao
 *  * @date 2017/12/1 0001 14:43
 *  
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class DataTest {

    @Autowired
    private DataClearService dataClearService;

    @Test
    public void test3(){
        Map<String, String> param = new HashMap<String, String>();
        param.put("dbName", "gps_1");
        param.put("tabName", "t_gps_a2\\_%");
        List<Schema> schemas = dataClearService.selectTabByStrLike(param);
        System.out.println(schemas.size());
    }
}
