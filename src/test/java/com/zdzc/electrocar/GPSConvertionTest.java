package com.zdzc.electrocar;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zdzc.electrocar.util.ByteUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class GPSConvertionTest {

    @Test
    public void testGPSConvertion() {
        double lng = 120.306685;//经度 120.065805
        double lat = 29.912306;//纬度 30.313685
        double[] gps = new double[2];
        String locations = String.valueOf(lng)+","+String.valueOf(lat);
        String key = "FnobWp9rTBylcNitkgoWWEmEnqcKQlGL";//高德地图API KEY
        //高德地图API URL
        String httpUrl = "http://api.map.baidu.com/geoconv/v1/?coords="+locations
                +"&from=1&to=5&ak="+key;
        BufferedReader br = null;
        String result = "";
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            br = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
            String line;
            while ((line = br.readLine()) != null) {
                result += line;
            }
            br.close();
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        //将返回的字符串数据转为JSON对象
        JSONObject jsonObject = JSONObject.parseObject(result);
        JSONArray location = jsonObject.getJSONArray("result");
        JSONObject loca = location.getJSONObject(0);
        String x = loca.getString("x");
        String y = loca.getString("y");
        System.out.println(x);
        System.out.println(y);
//        String loca = location.getString(0);
//        String[] strArr = StringUtils.split(loca,",");
//        gps[0] = Double.valueOf(strArr[0]);
//        gps[1] = Double.valueOf(strArr[1]);
//        System.out.println(gps[0]);
//        System.out.println(gps[1]);
    }
}
