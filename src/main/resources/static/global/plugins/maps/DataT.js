/**
 * 扩展date函数
 */
Date.prototype.format = function(format) {
    var o = {
        "M+": this.getMonth() + 1,
        "d+": this.getDate(),
        "h+": this.getHours(),
        "m+": this.getMinutes(),
        "s+": this.getSeconds(),
        "q+": Math.floor((this.getMonth() + 3) / 3),
        "S": this.getMilliseconds()
    }
    if (/(y+)/.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
        }
    }
    return format;
};

function getFormatDate(date, pattern) {
    if (date == undefined || date == "" || date == null) {
       return "";
    }
    date=new Date(date);
    if (pattern == undefined) {
        pattern = "yyyy-MM-dd hh:mm:ss";
    }
    return date.format(pattern);
};

/**
 *  方位角 转 象限角
 * */
function jiaodu(num){
	var xlj="--";
	num = num -0;
	if(num == 0)
		xlj="正北";
	    else if(num == 90)
	    xlj="正东";
		 else if(num == 180)
	   xlj="正南";
		 else if(num == 270)
	  xlj="正西";
	   else if(num == 360)
		xlj="正北";
	  else if( 90 >num && num > 0)
		xlj="东北";
	else if(180 >num && num > 90)
	  xlj="东南";
	else if(270>num && num>180)
	xlj="西南";
	else if(360>num && num>270)
		xlj="西北"
	return xlj;
};
