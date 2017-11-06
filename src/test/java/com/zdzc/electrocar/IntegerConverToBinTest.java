package com.zdzc.electrocar;

import com.zdzc.electrocar.util.ByteUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IntegerConverToBinTest {

    @Test
    public void testIntConverToBin() {
        //        000C0001
        System.out.println(Integer.valueOf("000C0001",16));
        System.out.println(Integer.toBinaryString(786433));// 11000000000000000001
        System.out.println(Integer.valueOf("11000000000000000001",2).toString());
        System.out.println(ByteUtil.get32BitBinStrFromInt(-786433));
        System.out.println(ByteUtil.get32BitBinStrFromInt(-786433).length());
    }
}
