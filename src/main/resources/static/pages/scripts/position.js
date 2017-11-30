var Position = (function () {
    return {
        init:function (map) {
            if($(window).height()>$(".all-wrapper").outerHeight(true)){
                var height=$(window).height()-$(".all-wrapper").outerHeight(true)+$('#map-content').outerHeight();
                $('#map-content').css("min-height",height);
            }
            map = new AMap.Map('map-content',{
                center: [120.111691,30.219249],
                zoom:6
            })

        },
        getPosition:function (url,data,map) {
            $.ajax({
                url : url,
                data : data,
                type : 'POST',
                error : function(xhr, ajaxOptions, thrownError){

                },
                success : function(response){
                    if (response.success){
                        map = new AMap.Map('map-content',{
                            center: [response.data.lng,response.data.lat],
                            zoom:13
                        })
                        var marker = new AMap.Marker({
                            position : [response.data.lng,response.data.lat],
                            map : map
                        })
                        //构建自定义信息窗体
                        function createInfoWindow(title, content) {
                            var info = document.createElement("div");
                            info.className = "info";

                            //可以通过下面的方式修改自定义窗体的宽高
                            info.style.width = "250px";
                            // 定义顶部标题
                            var top = document.createElement("div");
                            var titleD = document.createElement("div");
                            var closeX = document.createElement("img");
                            top.className = "info-top";
                            titleD.innerHTML = title;
                            closeX.src = "http://webapi.amap.com/images/close2.gif";
                            closeX.onclick = closeInfoWindow;

                            top.appendChild(titleD);
                            top.appendChild(closeX);
                            info.appendChild(top);

                            // 定义中部内容
                            var middle = document.createElement("div");
                            middle.className = "info-middle";
                            middle.style.backgroundColor = 'white';
                            middle.innerHTML = content;
                            info.appendChild(middle);

                            // 定义底部内容
                            var bottom = document.createElement("div");
                            bottom.className = "info-bottom";
                            bottom.style.position = 'relative';
                            bottom.style.top = '0px';
                            bottom.style.margin = '0 auto';
                            var sharp = document.createElement("img");
                            sharp.src = "http://webapi.amap.com/images/sharp.png";
                            bottom.appendChild(sharp);
                            info.appendChild(bottom);
                            return info;
                        }
                        //关闭信息窗体
                        function closeInfoWindow() {
                            map.clearInfoWindow();
                        }
                        //组装信息窗体内容
                        var title = '设备号：' + response.data.deviceId;
                        content = [];
                        content.push("经度：" + response.data.lng);
                        content.push("纬度：" + response.data.lat);
                        content.push("时间：" + response.data.time);
                        //根据经纬度逆推地理位置并监听标记点的点击事件
                        AMap.service('AMap.Geocoder',function () {
                            geocoder = new AMap.Geocoder({
                                city: "010"//城市，默认：“全国”
                            });
                            var lnglat = [response.data.lng,response.data.lat];
                            geocoder.getAddress(lnglat,function (status,result) {
                                if (status === 'complete' && result.info === 'OK') {
                                    content.push("地址：" + result.regeocode.formattedAddress);
                                    var infoWindow = new AMap.InfoWindow({
                                        isCustom: true,  //使用自定义窗体
                                        content: createInfoWindow(title, content.join("<br/>")),
                                        offset: new AMap.Pixel(16, -45)
                                    })
                                    AMap.event.addListener(marker, 'click', function() {
                                        infoWindow.open(map, marker.getPosition());
                                    });
                                }
                            })
                        })
                    }else {
                        toastr.warning(response.message);
                    }
                }
            })
        }
    }
})();

$(function(){
    var map;
    Position.init(map);
    $("#positionSearch").click(function () {
        var deviceCode = $("#deviceCode").val();
        Position.getPosition("getPosition",{deviceCode: deviceCode},map);
    })
})