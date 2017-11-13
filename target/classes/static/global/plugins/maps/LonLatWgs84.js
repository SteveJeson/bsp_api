/**
 * 坐标系切换
 * */
var x_PI = 3.14159265358979324 * 3000.0 / 180.0;
var PI = 3.1415926535897932384626;
var a = 6378245.0;
var ee = 0.00669342162296594323;
var LonLatWgs = (function() {
	return {
		bd09togcj02:function(bd_lon, bd_lat) {
		    var x_pi = 3.14159265358979324 * 3000.0 / 180.0;
		    var x = bd_lon - 0.0065;
		    var y = bd_lat - 0.006;
		    var z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
		    var theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
		    var gg_lng = z * Math.cos(theta);
		    var gg_lat = z * Math.sin(theta);
		    return [gg_lng, gg_lat]
		},
		gcj02towgs84:function(lng, lat) {
		    if (LonLatWgs.ischina(lng, lat)) {
		        return [lng, lat]
		    }
		    else {
		        var dlat = LonLatWgs.transformlat(lng - 105.0, lat - 35.0);
		        var dlng = LonLatWgs.transformlng(lng - 105.0, lat - 35.0);
		        var radlat = lat / 180.0 * PI;
		        var magic = Math.sin(radlat);
		        magic = 1 - ee * magic * magic;
		        var sqrtmagic = Math.sqrt(magic);
		        dlat = (dlat * 180.0) / ((a * (1 - ee)) / (magic * sqrtmagic) * PI);
		        dlng = (dlng * 180.0) / (a / sqrtmagic * Math.cos(radlat) * PI);
		        mglat = lat + dlat;
		        mglng = lng + dlng;
		        return [lng * 2 - mglng, lat * 2 - mglat]
		    }
		},transformlat:function(lng, lat) {
		    var ret = -100.0 + 2.0 * lng + 3.0 * lat + 0.2 * lat * lat + 0.1 * lng * lat + 0.2 * Math.sqrt(Math.abs(lng));
		    ret += (20.0 * Math.sin(6.0 * lng * PI) + 20.0 * Math.sin(2.0 * lng * PI)) * 2.0 / 3.0;
		    ret += (20.0 * Math.sin(lat * PI) + 40.0 * Math.sin(lat / 3.0 * PI)) * 2.0 / 3.0;
		    ret += (160.0 * Math.sin(lat / 12.0 * PI) + 320 * Math.sin(lat * PI / 30.0)) * 2.0 / 3.0;
		    return ret
		},transformlng:function(lng, lat) {
		    var ret = 300.0 + lng + 2.0 * lat + 0.1 * lng * lng + 0.1 * lng * lat + 0.1 * Math.sqrt(Math.abs(lng));
		    ret += (20.0 * Math.sin(6.0 * lng * PI) + 20.0 * Math.sin(2.0 * lng * PI)) * 2.0 / 3.0;
		    ret += (20.0 * Math.sin(lng * PI) + 40.0 * Math.sin(lng / 3.0 * PI)) * 2.0 / 3.0;
		    ret += (150.0 * Math.sin(lng / 12.0 * PI) + 300.0 * Math.sin(lng / 30.0 * PI)) * 2.0 / 3.0;
		    return ret
		},ischina:function(lng, lat) {
		    return (lng < 72.004 || lng > 137.8347) || ((lat < 0.8293 || lat > 55.8271) || false);
		},getLonlat:function(lng, lat){
			var hx=LonLatWgs.bd09togcj02(lng,lat);
			var wgs=LonLatWgs.gcj02towgs84(hx[0],hx[1]);
			return wgs;
		}
		
	}
})();