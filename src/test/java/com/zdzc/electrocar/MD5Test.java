package com.zdzc.electrocar;

import com.zdzc.electrocar.util.MD5Util;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MD5Test {

    @Test
    public void testMD5() {
        String method = "GET";
        String uri = "/bsp/api/GPSInfo/916122700110";
        String pwd = "123456";
        String authStr = method+uri+pwd;
        String hexStr = DigestUtils.md5Hex(authStr);
        System.out.println("加密后："+hexStr);
    }
}
