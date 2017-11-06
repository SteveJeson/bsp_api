package com.zdzc.electrocar;

import com.zdzc.electrocar.util.ByteUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GPSConvertionTest {

    @Test
    public void testGPSConvertion() {
//        double lng = 120.065801;//经度 120.065805
//        double lat = 30.313682;//纬度 30.313685
//        double[] gps = GPSConvertion.transLatLng(lng,lat);
//        System.out.println("坐标："+gps[0]+","+gps[1]);
        //高德在线测试：120.070616590712,30.3113929579
        //120.07061673405347,30.311392935445674
        //高德在线测试：120.070620659723,30.311395941841
        //120.0706207374132,30.311395941592757

    }
}
