/**
 * 地图配置 
 * 后期移植过来
 */
var MapConF={
		Extent:{
			min:new OpenLayers.Bounds(117.42472743164,28.491450839844,122.23399256836,30.276729160156), //9级
			max:new OpenLayers.Bounds(120.15430291663,30.250588351097,120.16369601261,30.254075222817)  //18级
		},lev:{
			min:10, //地图最小等级
			centent:12 , //定位等级
			init:15     //初始化等级
		},center:{
			c:new OpenLayers.LonLat(119.82936,29.38409)
		},Util:{
			img:{
				markersImg:{
					on:"assets/pages/img/blue.png",  //蓝色图标
					uon:"assets/pages/img/red.png",  //红图标
					start:"assets/pages/img/go.png", //开始
					car_red:"assets/global/img/car.png", //车辆
					marker_red:"assets/pages/img/red.png",	//定位
					load:'<img   alt="" src="assets/global/plugins/maps/img/load.gif">'
				}
			},msg:{
				query:{
					hzs:"合作社位置信息获取失败!",
					fwwd:"服务网点位置信息获取失败!",
					machinery:"农机位置信息获取失败！"
				}
			},html:{
				openlayerNum:'<div style="position: relative;margin:-30px auto auto 0px; font-size:13px;color:white;font-weight:bold; text-align:center;width:100%">{id}</div>',
				openlayerCar:'<div style="position: relative;margin:0px auto auto -80px; font-size:13px;color:red;font-weight:bold; text-align:center;width:200px">{id}</div>'
			},state:{
				on:"在线",
				uon:"离线"
			},code:{
				online_minutes:10 //10分钟内 算在线
			}
		}
		
};

