package com.zdzc.dataClear;

import com.zdzc.Application;
import com.zdzc.dataClear.service.DataClearService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
        System.out.println(Long.MAX_VALUE);
    }
}
