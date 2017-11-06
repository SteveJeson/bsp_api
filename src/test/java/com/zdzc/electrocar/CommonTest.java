package com.zdzc.electrocar;

import com.zdzc.electrocar.util.Command;
import org.junit.Test;

import java.math.BigDecimal;

public class CommonTest {

    @Test
    public void test() {
        /*System.out.println(Command.C0);
        System.out.println(Command.C0.getCommand());*/
        System.out.println(Command.valueOf("C0").getCommand());

        String a = "100";
        String b = "100%";
        //去掉%
        String tempA = a.replace("%","");
        System.out.println("after sub:"+tempA+";length:"+tempA.length());
        String tempB = b.replace("%","");
        System.out.println(tempB);
        //精确表示
        BigDecimal dataA = new BigDecimal(tempA);
        System.out.println(dataA);
        BigDecimal dataB = new BigDecimal(tempB);
        System.out.println(dataB);
        System.out.println(dataA.compareTo(dataB));//大于为1，相同为0，小于为-1
    }
}
