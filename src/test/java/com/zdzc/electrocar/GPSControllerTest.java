package com.zdzc.electrocar;

import com.zdzc.electrocar.service.GpsSnapshotService;
import com.zdzc.electrocar.util.JSONResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class GPSControllerTest {
    @Autowired
    private GpsSnapshotService snapshotService;

    @Test
    public void getSnapshot(){
        JSONResult jsonResult = snapshotService.selectByDeviceCode("917090500013");
        System.out.println(jsonResult.getStatusCode());
        System.out.println(jsonResult.getMessage());
    }
}
