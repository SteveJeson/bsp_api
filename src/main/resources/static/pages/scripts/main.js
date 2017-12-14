var Main = (function() {
	var orgName;
	return {
		map : null,
		baiduMap : null,
		//平台框架中  各种 dataTable插件实例 都放这里，以便于清除浮动
		dataTableExamples:{},
		//破坏掉 dataTable 表格 对象
		destroyDataTable:function(){
			if(Object.getOwnPropertyNames(Main.dataTableExamples).length>0){
				for(key in Main.dataTableExamples){
					Main.dataTableExamples[key].destroy();
				}
				Main.dataTableExamples={};
			}
			if($("table.table-header-fixed").size() > 0){
				$("table.table-header-fixed").each(function(){
					$(this).remove();
				});
			}
		},
		
		
		//获取后台地区名称
		areaName:function(){
			$.ajax({
				url:'manage/login/areaName',
				dataType:'json',
				type:'GET',
				success:function(data){
					if(null != data.data && data.success){
						orgName=data.data.orgName;
						Main.getWeather(orgName);
					}
				}
			})
		},
		
		getWeather:function (city) {
            $.ajax({
                url:"http://api.map.baidu.com/telematics/v3/weather",
                type:"GET",
                data:{
                    location:city,
                    output:'json',
                    ak:'6tYzTvGZSOpYB5Oc2YGGOKt8'
                },

                dataType:"jsonp",
                jsonp:"callback",
				jsonpCallback:"success_jsonpCallback",
                success:function(data){
					if(typeof(data.results) != 'undefined') {
                        //百度那边的 数据已经回来，我现在要解析这个数据.
                        var weatherData = data.results[0].weather_data;
                        var tem = weatherData[0].date.slice(14, weatherData[0].date.length - 1);
                        // var htmlstr = '。' + city + '天气' + tem + '，' + weatherData[0].weather;
                        var htmlstr = '|&nbsp;' + weatherData[0].weather + '&nbsp;' + tem ;
                        $('.weather-container').append(htmlstr)
                    }
                }
            })
        },

		getParam : function (name){
            new RegExp("(^|&)"+name+"=([^&]*)").exec(window.location.search.substr(1));
            return RegExp.$2
        },
		
		//左边菜单栏样式效果
		sidebarMenuStyle:function(url){
			// var end=url.indexOf("/", url.indexOf("/")+1)+1;
			// var key=url.slice(0,end);

			// var fromposition = url.indexOf('fromposition');
			// var fromtrack = url.indexOf('fromtrack');
			// var comefrom;
            //
			// if(fromposition>0){
			// 	comefrom = 0
			// }
            //
			// if(fromtrack> 0){
			// 	comefrom = 1
			// }
			// /*todo:各级菜单默认跳转到第一个,该方法待优化，目前是写死的*/
			// if(key == 'manage/agriMachInfo/'){
			// 	key = 'manage/cooperative/';
			// }else if(key == 'manage/videoCenter/'){
			// 	key = 'manage/videoCenter/init';
			// }else if(key == 'manage/servicesinternetInfo/'){
             //    key = 'manage/cooperative/';
			// }else if(key == 'manage/home/'){
             //    var firstlink = $('.indexmenu').find('li').first().find('a').attr('href');
			// 	var endoff = firstlink.indexOf("/", firstlink.indexOf("/")+1)+1;
			// 	key = firstlink.slice(2,endoff);
			// }else if(key == 'manage/carLocationPage/' && comefrom == 0){
			// 	key = 'manage/carLocation/';
			// }else if(key == 'manage/carLocationPage/' && comefrom == 1){
			// 	key = 'manage/cartrack/';
			// }else if(key == 'manage/cartrack/'){
             //    key = 'manage/carLocation/';
			// }
            if("undefined" != typeof map){
            	map = null;
			}
            // //添加对应的菜单选中样式
			// if(key == 'manage/videoCenter/'){
			// 	$("a[href^='#!"+key+"']").parent().addClass('activated')
			// }
            //
			// if(key == 'manage/home/' || key == 'manage/cooperationstatistics/' || key == 'manage/servicesinternet/'){
			//     var homeurl = $('.indexnav').attr('href');
			//     var newend = homeurl.indexOf("/", homeurl.indexOf("/")+1)+1;
			//     var newkey = homeurl.slice(2,newend);
			//     key = newkey;
            // }
            //
            // if(key == 'manage/login/'){
             //    $('.left-bar_menu').find('.toplink').removeClass('activated');
			// 	$('.left-bar_submenu').hide();
			// 	return false;
			// }

            $('.left-bar_submenu').find("a[href^='init']").parents('.toplink').addClass('activated')
            if(!$('.left-side-bar').hasClass('thin-bar')){
                $('.left-bar_submenu').find("a[href^='init']").parents('.toplink').find('ul').show(500);
                $('.left-bar_submenu').find("a[href^='init']").parent('li').addClass('active');
			}
        },
		
		//异步加载页面 功能页面（功能模块）
		getMainContent:function(url){
			Main.getMainContent(url,null);
		},
		//异步加载页面 功能页面（功能模块）
		getMainContent:function(url,param){
			if (url != null && $.trim(url)!='' && $.trim(url)!='#' &&  $.trim(url) != 'javascript:;') {
				Main.sidebarMenuStyle(url);
				$.get(url, param,function(data) {
					// Main.destroyDataTable();
					$('#main-content').html($.parseHTML( data, true ));
                    // map = new AMap.Map('map-content',{
                    //     center: [120.111691,30.219249],
                    //     zoom:6
                    // })
                    // AMapUI.loadUI(['control/BasicControl'], function(BasicControl) {
                    //
                    //     //添加一个缩放控件
                    //     map.addControl(new BasicControl.Zoom({
                    //         position: 'lt'
                    //     }));
                    //
                    //     //缩放控件，显示Zoom值
                    //     map.addControl(new BasicControl.Zoom({
                    //         position: 'lb',
                    //         showZoomNum: true
                    //     }));
                    //
                    //     //图层切换控件
                    //     map.addControl(new BasicControl.LayerSwitcher({
                    //         position: 'rt'
                    //     }));
                    // });
					// Main.setMainContentMinHeight();
				});
			}
		},


		//设置  $('#main-content')  最小高度
		setMainContentMinHeight:function(){
			if($(window).height()>$(".all-wrapper").outerHeight(true)){
				var height=$(window).height()-$(".all-wrapper").outerHeight(true)+$('#main-content').outerHeight();
                $('#main-content').css("min-height",height);
			}
		},
		
		//通过url异步调用打开前往功能页面  
		goUrlAjaxPage:function(url){
			Main.goUrlAjaxPage(url,null);
		},
		//通过url异步调用打开前往功能页面  
		goUrlAjaxPage:function(url,param){
			if (url != null && $.trim(url)!='' && $.trim(url)!='#' &&  $.trim(url) != 'javascript:;') {
				if(url.indexOf("#!")!=0){
					url="#!"+url;
				}

				if(param!=null){
					if(url.indexOf("?")!=-1){
						window.location.hash=url+"&"+$.param(param);
					}else{
						window.location.hash=url+"?"+$.param(param);
					}
				}else{
					window.location.hash=url;
				}
			}
		},
		
		// 处理全屏幕模式切换
		handleFullScreenMode:function() {
			function toggleFullScreen() {
				if (!document.fullscreenElement &&
				!document.mozFullScreenElement && !document.webkitFullscreenElement && !document.msRequestFullscreenElement) {// current working methods
					if (document.documentElement.requestFullscreen) {
						document.documentElement.requestFullscreen();
					} else if (document.documentElement.mozRequestFullScreen) {
						document.documentElement.mozRequestFullScreen();
					} else if (document.documentElement.webkitRequestFullscreen) {
						document.documentElement.webkitRequestFullscreen(Element.ALLOW_KEYBOARD_INPUT);
					} else if(document.documentElement.msRequestFullscreen()){
						document.documentElement.msRequestFullscreen();
					}
				} else {
					if (document.cancelFullScreen) {
						document.cancelFullScreen();
					} else if (document.mozCancelFullScreen) {
						document.mozCancelFullScreen();
					} else if (document.webkitCancelFullScreen) {
						document.webkitCancelFullScreen();
					} else if (document.msCancelFullScreen){
						document.msCanelFullScreen();
					}
				}
			}

			$('#trigger_fullscreen').click(function() {
				toggleFullScreen();
			});
		},

				
		init: function() {
			this.handleFullScreenMode();
            // var url = $(".locatemenu").find('li').first().find('a').attr('href').replace("#!","");
			// Main.getMainContent(url);
            if($(window).height()>$(".all-wrapper").outerHeight(true)){
                // var height=$(window).height()-$(".all-wrapper").outerHeight(true)+$('#map-content').outerHeight(true);
                var height = $(window).height() - $(".headtop").height() - $(".copyright").height() - $(".new-bread").height() - $(".list-search").height() - 24
                $('#map-content').css("min-height",height);
            }
            Main.map = new AMap.Map('map-content',{
                center: [120.111691,30.219249],
                zoom:6
            })
            $(".form_datetime").datetimepicker({
                minView: "hour",
                language:  'zh-CN',
                autoclose: true,
                isRTL: App.isRTL(),
                format: "yyyy-mm-dd hh:ii",
                pickerPosition: (App.isRTL() ? "bottom-right" : "bottom-left")
            }).on('changeDate', function(e){

                var stime = $("#startTime").val();
                var startTime=new Date(Date.parse(stime.replace(/-/g,"/"))).getTime();

                var etime = $("#endTime").val();
                var endTime=new Date(Date.parse(etime.replace(/-/g,"/"))).getTime();
                if(!isNaN(startTime) && !isNaN(endTime)){
                    if(startTime>endTime){
                        toastr.info("开始时间不能大于结束时间!");
                        return false;
                    }
                }
            });
            $(".form_datetime").datetimepicker({
                minView: "hour",
                startView: "day",
                language:  'zh-CN',
                autoclose: true,
                isRTL: App.isRTL(),
                format: "yyyy-mm-dd hh:ii",
                pickerPosition: (App.isRTL() ? "bottom-right" : "bottom-left")
            });
            AMapUI.loadUI(['control/BasicControl'], function(BasicControl) {

                //添加一个缩放控件
                Main.map.addControl(new BasicControl.Zoom({
                    position: 'lt'
                }));

                //缩放控件，显示Zoom值
                Main.map.addControl(new BasicControl.Zoom({
                    position: 'lb',
                    showZoomNum: true
                }));

                //图层切换控件
                Main.map.addControl(new BasicControl.LayerSwitcher({
                    position: 'rt'
                }));
            });
            Welcome.hideAndShow(true, false, false);
			window.location.hash = $(".locatemenu").find('li').first().find('a').attr('href');
			// 左侧菜单
			$('.ham').click(function () {
				$('.left-side-bar').toggleClass('thin-bar');
				$('.left-bar_menu').toggleClass('thin-bar_menu');
				$('.normal-content').toggleClass('wide-content');
				$('.form-operator').toggleClass('left46');
				$('.left-bar_submenu').hide()
            })

			$('.toplink').click(function () {
				if(!$(this).hasClass('activated')){
					$(this).addClass('activated').siblings().removeClass('activated')
				}

				if(!($(this).parent().hasClass('thin-bar_menu'))){
                    $(this).find('ul').show(500);
                    $(this).siblings().find('ul').hide(500)
				}
            })
			
			$('.toplevel').click(function () {
				$(this).next().find('li').first().addClass('active').siblings().removeClass('active')
            })

			$('.left-bar_submenu li').click(function () {
				$(this).addClass('active').siblings().removeClass('active')
            })


			$('.toplink').hover(function () {
				if($(this).parent().hasClass('thin-bar_menu')){
                    $(this).find('.left-bar_submenu').stop(true).show(300)
                }
            },function () {
				if($(this).parent().hasClass('thin-bar_menu')){
                    $(this).find('.left-bar_submenu').stop(true).hide(300)
				}
            })



            $(window).scroll(function(){
                var scrollTop = $(this).scrollTop();
                var scrollHeight = $(document).height();
                var windowHeight = $(this).height();
                var tobottom = scrollHeight - (scrollTop + windowHeight);
                if( tobottom <= 24 ){
                    $('.form-operator').css('bottom','30px')
                }else{
                    $('.form-operator').css('bottom','0')
				}
            });
		}
	};
})();

function calendarTime(){
	 //获取当前系统日期
	 var today=new Date();
	 var y=today.getFullYear();
	 var mo=today.getMonth();
	 var da=today.getDate();
	 var h=today.getHours();
	 var m=today.getMinutes();
	 var s=today.getSeconds();
	 var weekString="日一二三四五六";
	 var TheDate=new Date();
	 var CalendarData=new Array(20);
	 var madd=new Array(12);
	 var numString="一二三四五六七八九十";
	 var monString="正二三四五六七八九十冬腊";
	 var cYear;
	 var cMonth;
	 var cDay;
	 var cHour;
	 var cDateString;
	 var DateString;
	 var Browser=navigator.appName;
	 function init1(){
		 CalendarData[0]=0x41A95;
		 CalendarData[1]=0xD4A;
		 CalendarData[2]=0xDA5;
		 CalendarData[3]=0x20B55;
		 CalendarData[4]=0x56A;
		 CalendarData[5]=0x7155B;
		 CalendarData[6]=0x25D;
		 CalendarData[7]=0x92D;
		 CalendarData[8]=0x5192B;
		 CalendarData[9]=0xA95;
		 CalendarData[10]=0xB4A;
		 CalendarData[11]=0x416AA;
		 CalendarData[12]=0xAD5;
		 CalendarData[13]=0x90AB5;
		 CalendarData[14]=0x4BA;
		 CalendarData[15]=0xA5B;
		 CalendarData[16]=0x60A57;
		 CalendarData[17]=0x52B;
		 CalendarData[18]=0xA93;
		 CalendarData[19]=0x40E95;
		 madd[0]=0;
		 madd[1]=31;
		 madd[2]=59;
		 madd[3]=90;
		 madd[4]=120;
		 madd[5]=151;
		 madd[6]=181;
		 madd[7]=212;
		 madd[8]=243;
		 madd[9]=273;
		 madd[10]=304;
		 madd[11]=334;
	 }
	 function GetBit(m,n){
		return (m>>n)&1;
	 }
	 function e2c(){
		 var total,m,n,k;
		 var isEnd=false;
		 var tmp=TheDate.getYear();
		 if (tmp<1900){
			 tmp+=1900;
			 total=(tmp-2001)*365+Math.floor((tmp-2001)/4)+madd[TheDate.getMonth()]+TheDate.getDate()-23;
		 }
		 if (TheDate.getYear()%4==0&&TheDate.getMonth()>1){
			 total++;
		 }
		 for(m=0;;m++){
			 k=(CalendarData[m]<0xfff)?11:12;
			 for(n=k;n>=0;n--){
				 if(total<=29+GetBit(CalendarData[m],n)){
					 isEnd=true;
					 break;
				 }
				 total=total-29-GetBit(CalendarData[m],n);
			 }
			 if(isEnd)break;
		 }
		 cYear=2001 + m;
		 cMonth=k-n+1;
		 cDay=total;
		 if(k==12){
			 if(cMonth==Math.floor(CalendarData[m]/0x10000)+1){
				 cMonth=1-cMonth;
			 }

			 if(cMonth>Math.floor(CalendarData[m]/0x10000)+1){
				 cMonth--;
			 }
		 }
		 cHour=Math.floor((TheDate.getHours()+3)/2);
	 }
	 function GetcDateString(){
		 var tmp="";
		 if(cMonth<1){
			 tmp+="闰";
			 tmp+=monString.charAt(-cMonth-1);
		 }else{
             tmp+=monString.charAt(cMonth-1);
         }
		 tmp+="月";
		 tmp+=(cDay<11)?"初":((cDay<20)?"十":((cDay<21)?"二":((cDay<30)?"廿":"三")));
		 tmp+=numString.charAt((cDay-1)%10);
		 tmp+=" ";
		 cDateString=tmp;
	 }
	 init1();
	 e2c();
	 GetcDateString();
	 //调用checkTime（）函数，小于十的数字前加0
	 m=checkTime(m);
	 s=checkTime(s);
	 //s设置层txt的内容
	 //document.getElementById('txt').innerHTML=y+"年"+(mo+1)+"月"+da+"日 "+h+":"+m+":"+s+" 星期"+weekString.charAt(today.getDay())+" 农历"+cDateString;
	 document.getElementById('txt').innerHTML=document.getElementById('txt').innerHTML+" 欢迎您！<span class='fa fa-calendar calendar-icon'></span>"+y+"-"+(mo+1)+"-"+da+"&nbsp;"+"("+"&nbsp;"+cDateString+")";
	 //过500毫秒再调用一次
	 //t=setTimeout('calendarTime()',500);
	 //小于10，加0
	 function checkTime(i){
		 if(i<10){
			i="0"+i;
		 }
		 return i;
	 }
}


$(document).ready(function() {
    if ("onhashchange" in window) {
        window.onhashchange=function(){
            Main.getMainContent(window.location.hash.replace("#!",""));
        }
    }
    Main.init();

});




