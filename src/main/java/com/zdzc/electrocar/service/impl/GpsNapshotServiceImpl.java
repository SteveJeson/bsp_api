package com.zdzc.electrocar.service.impl;

import com.zdzc.electrocar.common.CommonBusiness;
import com.zdzc.electrocar.dto.GPSNapshotDto;
import com.zdzc.electrocar.entity.GPSNapshotEntity;
import com.zdzc.electrocar.mapper.GPSNapshotEntityMapper;
import com.zdzc.electrocar.service.GpsNapshotService;
import com.zdzc.electrocar.util.GPSConvertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liuw on 2017/8/29.
 */
@Service
public class GpsNapshotServiceImpl implements GpsNapshotService {

    private static Logger log = LoggerFactory.getLogger(GpsNapshotServiceImpl.class);

    @Autowired
    private GPSNapshotEntityMapper mapper;

    /**
     * 获取某个设备最新坐标信息
     * @param deviceId  终端设备号
     * @return GPSNapshotEntity
     */
    @Override
    public GPSNapshotEntity getLatestGPSInfoListByDeviceId(String deviceId) {
        List<GPSNapshotEntity> entities = this.mapper.selectLatestGPSListByDeviceId(deviceId);
        if (entities.size() > 0) {
            return entities.get(0);
        }
        return null;
    }

    /**
     * 将普通实体类属性信息复制给特定dto类
     * @param entity
     * @return GPSNapshotDto
     */
    @Override
    public GPSNapshotDto copyGPSEntityToDto(GPSNapshotEntity entity) {
        if (entity != null) {
            GPSNapshotDto dto = new GPSNapshotDto();
            dto.setDeviceId(entity.getDeviceId());//终端设备号
            double lng = entity.getLongitude();
            double lat = entity.getLatitude();
            dto.setLng(lng);//经度
            dto.setLat(lat);//纬度
            //转换成高德经纬度
            double[] gps = CommonBusiness.getGaodeGPS(lng, lat);
            dto.setOlng(lng!=0?gps[0]:lng);
            dto.setOlat(lat!=0?gps[1]:lat);

            dto.setTime(entity.getTime());//采集时间
            dto.setSpeed(entity.getSpeed());//速度
            dto.setOnline("1");//在线状态，默认为1
            dto.setMile(entity.getMile());
            //TODO 设防状态先默认设为1 1:设防 0:撤防
            dto.setFortifyStatus((byte)1);
            dto.setAccStatus(CommonBusiness.getAccStatus(entity.getAlarmStatus()));
            return dto;
        }
        return null;
    }


}
