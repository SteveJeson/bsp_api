package com.zdzc.electrocar.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zdzc.electrocar.util.ByteUtil;
import com.zdzc.electrocar.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Map;

/**
 * Created by liuw on 2017/8/31.
 * 通用业务处理
 */
public class CommonBusiness {
    private static Logger log = LoggerFactory.getLogger(CommonBusiness.class);

    private static String gaodeWebServerKey = "261b9ce89ab991fc23a481974ad6b881";
    /**
     * 通过报警信息获取ACC状态 1：开启 0：关闭
     * @param alarmStatus
     * @return
     */
    public static int getAccStatus(int alarmStatus) {
        String binStr = ByteUtil.get32BitBinStrFromInt(alarmStatus);
        int accStatus = Integer.valueOf(binStr.substring(0,1));
        return accStatus;
    }

    /**
     * 通过报警信息获取ACC状态 1：开启 0：关闭
     * 此方法为备用方法
     * 当参数alarmStatus为二进制字符串时可调用
     */
    public static int getAccStatus(String alarmStatus) {
        int accStatus = Integer.valueOf(alarmStatus.substring(0,1));
        return accStatus;
    }

    /**
     * 获取高德地图GPS坐标
     * @param lng
     * @param lat
     * @return
     */
    public static double[] getGaodeGPS(double lng,double lat) {
        double[] gps = new double[2];
        String locations = String.valueOf(lng)+","+String.valueOf(lat);
        String key = "261b9ce89ab991fc23a481974ad6b881";//高德地图API KEY
        //高德地图API URL
        String httpUrl = "http://restapi.amap.com/v3/assistant/coordinate/convert?locations="+locations
                +"&coordsys=gps&output=json&key="+key;
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
            log.error("=========MalformedURLException 高德地图API URL解析错误！==========");
        } catch (IOException e) {
            log.error("=========IOException 高德地图API URL IO异常！");
        }
        //将返回的字符串数据转为JSON对象
        JSONObject jsonObject = JSONObject.parseObject(result);
        String loc = jsonObject.getString("locations");
        String[] strArr = StringUtils.split(loc,",");
        gps[0] = Double.valueOf(strArr[0]);
        gps[1] = Double.valueOf(strArr[1]);
        return gps;
    }

    /**
     * 获取百度地图GPS坐标
     * @param lng
     * @param lat
     * @return
     */
    public static double[] getBaiduGPS(double lng,double lat) {
        double[] gps = new double[2];
        String locations = String.valueOf(lng)+","+String.valueOf(lat);
        String key = "FnobWp9rTBylcNitkgoWWEmEnqcKQlGL";//百度地图API KEY
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
            log.error("=========MalformedURLException 百度地图API URL解析错误！==========");
        } catch (IOException e) {
            log.error("=========IOException 百度地图API URL IO异常！");
        }
        //将返回的字符串数据转为JSON对象
        JSONObject jsonObject = JSONObject.parseObject(result);
        JSONArray location = jsonObject.getJSONArray("result");
        JSONObject loca = location.getJSONObject(0);
        String x = loca.getString("x");
        String y = loca.getString("y");
        gps[0] = Double.valueOf(x);
        gps[1] = Double.valueOf(y);
        return gps;
    }


    public static String getGaodeLocation(Double lng, Double lat){
        String key = gaodeWebServerKey;
        String httpUrl = "http://restapi.amap.com/v3/geocode/regeo?key=" + key + "&location=" + lng + "," + lat + "";
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
            JSONObject jsonObject = JSONObject.parseObject(result);
            Map<String, Object> regeocode = (Map<String,Object>)jsonObject.get("regeocode");
            return regeocode.get("formatted_address").toString();
        } catch (MalformedURLException e) {
            log.error("=========MalformedURLException 高德地图API URL解析错误！==========");
        } catch (IOException e) {
            log.error("=========IOException 高德地图API URL IO异常！");
        }
        return null;
    }

    /**
     * 根据顺序号获取轨迹数据库名
     * @param seqNo
     * @return
     */
    public static String getTrailDbName(Integer seqNo){
        String seqNoStr = seqNo + "";
        try {
            if (seqNoStr.length() >= 6){
                int dbNo = Integer.valueOf(seqNoStr.substring(0, seqNoStr.length() - 6));
                return Const.DateBase.TRAIL_DB_PREFIX + dbNo;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据顺序号和查询日期确定轨迹表名
     * @param seqNo    顺序号
     * @param date     查询日期
     * @return
     */
    public static String getTrailTableName(Integer seqNo, String date){
        String seqNoStr = seqNo + "";
        String month = date.substring(2,4);
        if (month.startsWith("0")){
            month = month.substring(1);
        }
        String day = date.substring(4,6);
        if (day.startsWith("0")){
            day = day.substring(1);
        }
        try {
            int length = seqNoStr.length();
            if (length >= 6){
                int tabNo = Integer.valueOf(seqNoStr.substring(length - 6, length));
                return Const.DateBase.TRAIL_TABLE_PREFIX + getMonth(Integer.valueOf(month)) + day + "_" + getTrailTableNo(tabNo);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取顺序号获取对应的报警库名
     * @param seqNo     顺序号
     * @return
     */
    public static String getAlarmDbName(Integer seqNo){
        String seqNoStr = seqNo + "";
        try {
            if (seqNoStr.length() >= 7){
                int dbNo = Integer.valueOf(seqNoStr.substring(0, seqNoStr.length() - 7));
                return Const.DateBase.ALARM_DB_PREFIX + dbNo;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据顺序号和查询日期确定报警表名
     * @param seqNo        顺序号
     * @param date         查询日期
     * @return
     */
    public static String getAlarmTableName(Integer seqNo, String date){
        String seqNoStr = seqNo + "";
        try {
            int length = seqNoStr.length();
            String month = date.substring(2,4);
            if (month.startsWith("0")){
                month = month.substring(1);
            }
            if (length >= 7){
                int tabNo = Integer.valueOf(seqNoStr.substring(length - 7, length));
                return Const.DateBase.ALARM_TABLE_PREFIX + getMonth(Integer.valueOf(month)) + "_" + getAlarmTableNo(tabNo);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 根据月份确定 a, b, c, d
     * a : 1, 5, 9
     * b : 2, 6, 10
     * c : 3, 7, 11
     * d : 4, 8, 12
     * @param month
     * @return
     */
    public static String getMonth(int month){
        String[] indexs = Const.Public.MONTH_INDEX;
        if (month > 0 && month <= 12) {
            return indexs[(month - 1) % 4];
        }
        return indexs[0];
    }

    /**
     * 计算轨迹表的表序号
     * @param num    库中的第几条记录
     * @return
     */
    public static int getTrailTableNo(int num){
        if (num > 0){
            return (num - 1) / 10000 + 1;
        }
        return 1;
    }

    /**
     * 计算报警表的表序号
     * @param num    库中的第几条记录
     * @return
     */
    public static int getAlarmTableNo(int num){
        if (num > 0){
            return (num - 1) / 20000 + 1;
        }
        return 1;
    }

}
