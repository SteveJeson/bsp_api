/** toastr 初始化  **/
toastr.options = {
	"closeButton": true,
	"debug": false,
	"newestOnTop": false,
	"progressBar": false,
	"positionClass": "toast-top-center",
	"preventDuplicates": false,
	"onclick": null,
	"showDuration": "300",
	"hideDuration": "1000",
	"timeOut": "3000",
	"extendedTimeOut": "3000",
	"showEasing": "swing",
	"hideEasing": "linear",
	"showMethod": "fadeIn",
	"hideMethod": "fadeOut"
}

/**全局配置   jquery ajax  **/
$.ajaxSetup({
	cache: false,
	complete:function(XMLHttpRequest,textStatus){
		if(XMLHttpRequest.getResponseHeader("sessionStatus")!=null){
			var sessionStatus=XMLHttpRequest.getResponseHeader("sessionStatus"); 
			if(sessionStatus=="timeout"){
				toastr.error("登录超时,请重新登录！");
			//如果超时就处理 ，指定要跳转的页面  
				window.location ="manage/page/login"+window.location.hash;
			}
		}else if(XMLHttpRequest.getResponseHeader("authorizeStatus")!=null){
			var authorizeStatus=XMLHttpRequest.getResponseHeader("authorizeStatus"); 
			if(XMLHttpRequest.status==401 && authorizeStatus=="unauthorized"){
				//toastr.error("功能访问未授权！");
				Main.goUrlAjaxPage("#!manage/error/frame/401");
			}			
		}else{
			if(XMLHttpRequest.status!=200 && XMLHttpRequest.status!=0){
				//toastr.error("连接服务器出错；错误代码："+XMLHttpRequest.status);
				if(XMLHttpRequest.status==404){
					Main.goUrlAjaxPage("#!manage/error/frame/404");
				}else if(XMLHttpRequest.status==500){
					Main.goUrlAjaxPage("#!manage/error/frame/500");
				}else {
					Main.goUrlAjaxPage("#!manage/error/frame/other",{status:XMLHttpRequest.status,statusText:XMLHttpRequest.statusText});
				}
			}
		}
	}
});

/***jQuery.validator **/
jQuery.extend(jQuery.validator.messages, {
  required: "必填字段",
  remote: "请修正该字段",
  email: "请输入正确格式的电子邮件",
  url: "请输入合法的网址",
  date: "请输入合法的日期",
  dateISO: "请输入合法的日期 (ISO).",
  number: "请输入合法的数字",
  digits: "只能输入整数",
  creditcard: "请输入合法的信用卡号",
  equalTo: "请再次输入相同的值",
  accept: "请输入拥有合法后缀名的字符串",
  maxlength: jQuery.validator.format("请输入一个 长度最多是 {0} 的字符串"),
  minlength: jQuery.validator.format("请输入一个 长度最少是 {0} 的字符串"),
  rangelength: jQuery.validator.format("请输入 一个长度介于 {0} 和 {1} 之间的字符串"),
  range: jQuery.validator.format("请输入一个介于 {0} 和 {1} 之间的值"),
  max: jQuery.validator.format("请输入一个最大为{0} 的值"),
  min: jQuery.validator.format("请输入一个最小为{0} 的值")
});

//邮编验证
jQuery.validator.addMethod("isZipCode", function(value, element) {  
    var tel = /^[0-9]{6}$/;
    return this.optional(element) || (tel.test(value));
}, "请输入正确的邮政编码！");

//手机号码验证
jQuery.validator.addMethod("isMobile", function(value, element) {
	var length = value.length;
	var mobile = /^(13[0-9]{9})|(18[0-9]{9})|(14[0-9]{9})|(17[0-9]{9})|(15[0-9]{9})$/;
	return this.optional(element) || (length == 11 && mobile.test(value));
}, "请输入正确的手机号码！");


//IP地址验证
jQuery.validator.addMethod("isIp", function (value, element) {
	var ipv4_regex = /^(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[0-9]{1,2})(\.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[0-9]{1,2})){3}$/;
	var ipv6_regex = /^\s*((([0-9A-Fa-f]{1,4}:){7}(([0-9A-Fa-f]{1,4})|:))|(([0-9A-Fa-f]{1,4}:){6}(:|((25[0-5]|2[0-4]\d|[01]?\d{1,2})(\.(25[0-5]|2[0-4]\d|[01]?\d{1,2})){3})|(:[0-9A-Fa-f]{1,4})))|(([0-9A-Fa-f]{1,4}:){5}((:((25[0-5]|2[0-4]\d|[01]?\d{1,2})(\.(25[0-5]|2[0-4]\d|[01]?\d{1,2})){3})?)|((:[0-9A-Fa-f]{1,4}){1,2})))|(([0-9A-Fa-f]{1,4}:){4}(:[0-9A-Fa-f]{1,4}){0,1}((:((25[0-5]|2[0-4]\d|[01]?\d{1,2})(\.(25[0-5]|2[0-4]\d|[01]?\d{1,2})){3})?)|((:[0-9A-Fa-f]{1,4}){1,2})))|(([0-9A-Fa-f]{1,4}:){3}(:[0-9A-Fa-f]{1,4}){0,2}((:((25[0-5]|2[0-4]\d|[01]?\d{1,2})(\.(25[0-5]|2[0-4]\d|[01]?\d{1,2})){3})?)|((:[0-9A-Fa-f]{1,4}){1,2})))|(([0-9A-Fa-f]{1,4}:){2}(:[0-9A-Fa-f]{1,4}){0,3}((:((25[0-5]|2[0-4]\d|[01]?\d{1,2})(\.(25[0-5]|2[0-4]\d|[01]?\d{1,2})){3})?)|((:[0-9A-Fa-f]{1,4}){1,2})))|(([0-9A-Fa-f]{1,4}:)(:[0-9A-Fa-f]{1,4}){0,4}((:((25[0-5]|2[0-4]\d|[01]?\d{1,2})(\.(25[0-5]|2[0-4]\d|[01]?\d{1,2})){3})?)|((:[0-9A-Fa-f]{1,4}){1,2})))|(:(:[0-9A-Fa-f]{1,4}){0,5}((:((25[0-5]|2[0-4]\d|[01]?\d{1,2})(\.(25[0-5]|2[0-4]\d|[01]?\d{1,2})){3})?)|((:[0-9A-Fa-f]{1,4}){1,2})))|(((25[0-5]|2[0-4]\d|[01]?\d{1,2})(\.(25[0-5]|2[0-4]\d|[01]?\d{1,2})){3})))(%.+)?\s*$/;
	return ipv4_regex.test(value)|| ipv6_regex.test(value);
}, "请输入有效的IP地址！");


//密码输入验证
jQuery.validator.addMethod("isPwd", function (value, element) {
  var pattern = /^[A-Za-z0-9_-]*$/g;
  if(value !=''){if(!pattern.test(value)){return false;}};	
  return true;	 
} ,  "密码只能由数字,字母和下划线组成！"); 

//小数点数字验证
jQuery.validator.addMethod("isNumber", function (value, element) {
var pattern = /^(-)?\d{1,10}(\.\d{1,4})?$/;
if(value !=''){if(!pattern.test(value)){return false;}};	
return true;	 
} ,  "请输入正确的数据！ (小数点前最大10位，小数点后最大4位)"); 

//小数点数字验证
jQuery.validator.addMethod("isBigNumber", function (value, element) {
var pattern = /^(-)?\d{1,9}(\.\d{1,5})?$/;
if(value !=''){if(!pattern.test(value)){return false;}};	
return true;	 
} , "请输入正确的数据！ (小数点前最大9位，小数点后最大5位)");

//小数点数字验证 点前缀不大于多少位
jQuery.validator.addMethod("numberPrefixLength", function ( value, element, param ){
    var length;
    if(value.indexOf(".")!=-1){
        length=value.split(".")[0].length;
    }else{
        length=value.length
    }
    return this.optional( element ) || length <= param;

} , "请输入正确的数据， 小数点前最大{0}位！");

//小数点数字验证 点后缀不大于多少位
jQuery.validator.addMethod("numberSuffixLength", function ( value, element, param ){
    var length=0;
    if(value.indexOf(".")!=-1){
        length=value.split(".")[1].length;
    }
    return this.optional( element ) || length <= param;

} , "请输入正确的数据， 小数点后最大{0}位！");

//大于某值
jQuery.validator.addMethod("greaterThan", function (value, element, param) {
	var target = $(param);
	return value > target.val();
}, "请输入大于比较值的数据！");

//16进制验证
jQuery.validator.addMethod("isHexadecimal", function(value, element) {
    var hexadecimal = /^([0-9|a-f|A-F]*)$/;
    return this.optional(element) || hexadecimal.test(value);
}, "16字符进制验证！");

//科学计数法验证
jQuery.validator.addMethod("isScientificCount", function(value, element) {
    var scientificCount = /^[-+]?(\d+(\.\d*)?|\.\d+)([eE][-+]?\d+)?[dD]?$/;
    return this.optional(element) || scientificCount.test(value);
}, "科学计数法！");

//中文匹配
jQuery.validator.addMethod("isChineseVerify", function(value, element) {
    var chineseVerify = /^[^\u4e00-\u9fa5]{0,}$/;
    return this.optional(element) || chineseVerify.test(value);
}, "中文输入验证！");

//经度验证
jQuery.validator.addMethod("isLng", function(value, element) {
    var lngTest = /^-?(?:(?:360(?:\.0{1,5})?)|(?:(?:(?:1[0-7]\d)|(?:[1-9]?\d))(?:\.\d{1,6})?))$/;
    return this.optional(element) ||  lngTest.test(value);
}, "请输入正确的经度");

//纬度验证
jQuery.validator.addMethod("isLat", function(value, element) {
    var latTest = /^-?(?:90(?:\.0{1,5})?|(?:[1-8]?\d(?:\.\d{1,6})?))$/;
    return this.optional(element) ||  latTest.test(value);
}, "请输入正确的纬度");

//身份证验证
jQuery.validator.addMethod("isIdCard", function (value, element) {
    var pattern = /^(\d{15}$|^\d{18}$|^\d{17}(\d|X|x))$/;
    if(value!=''){if(!pattern.test(value)){return false;}};
    return true;
}, "请输入有效的身份证号码！");

jQuery.validator.addMethod("isIdNum", function (value, element) {
    var num = $("#buymachineType option:selected").val();
    if(num == 10){
        var pattern = /^(\d{15}$|^\d{18}$|^\d{17}(\d|X|x))$/;
        if(value!=''){
            if(!pattern.test(value)){
                return false;
            }
        };
        return true;
    }else if(num == 20){
        if(value!=''){
            if(value.length > 20){
                return false;
            }
        };
        return true;
    }else {
        if(value.length > 20){
            return false;
        }
        return true;
    }
}, "请根据已选择的“购机对象种类”，输入有效的身份证号码(长度不能超过18)或者组织机构代码(长度不能超过20)！");
var reg = /^([1-9]\d{0,5}|0)(\.\d{1,4})?$/;

jQuery.validator.addMethod("saleSum", function (value, element) {
    var reg = /^([1-9]\d{0,7}|0)(\.\d{1,2})?$/;
    if(value!=''){
        if(!reg.test(value)){
            return false;
        }
    };
    return true;
}, "请输入数字，小数点前8位,小数点后2位！");

//数字、字母验证
jQuery.validator.addMethod("isNumericLetters", function (value, element) {
    var pattern = /^[A-Za-z0-9]+$/g;
    if(value !=''){if(!pattern.test(value)){return false;}};
    return true;
} ,  "只能由数字和字母组成！");


//正浮点数
jQuery.validator.addMethod("isPositiveFloat", function (value, element) {
    var pattern = /^\d+(\.\d+)?$/g;
    if(value !=''){if(!pattern.test(value)){return false;}};
    return true;
} ,  "请输入有效的正浮点数！");

//正整数
jQuery.validator.addMethod("isPositiveInteger", function (value, element) {
    var pattern = /^\d+$/g;
    if(value !=''){if(!pattern.test(value)){return false;}};
    return true;
} ,  "请输入有效的正整数！");

//账号校验
jQuery.validator.addMethod("validateAccount", function (value, element) {
  var pattern = /^[A-Za-z0-9_]*$/g;
  if(value !=''){if(!pattern.test(value)){return false;}};	
  return true;	 
} ,  "账号只能由数字,字母和下划线组成！"); 


/***jQuery.validator **/

/**
 * 过滤特殊字符方法
 * @param s 要过滤的字符串
 * @returns {string} 过滤后的字符串
 */
function dropSpecialWords(s){
    var pattern = new RegExp("[`~!@#$^&*()\"=|{}':;',\\[\\].<>+-/?~！%@#￥……&*（）——|{}《》【】‘；：”“'。，、？]");
    var rs = "";
    for (var i = 0; i < s.length; i++) {
        rs = rs + s.substr(i, 1).replace(pattern, '');
    }
    return rs;
}

/**bootbox 模态窗口 中文化 **/
bootbox.setDefaults("locale","zh_CN");