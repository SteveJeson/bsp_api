var Welcome = (function () {

    return {
        marker : null,
        pathSimplifier : null,
        searchTime : [],
        baiduMap : null,
        positionClick : function(map){
            Welcome.hideAndShow(true, false, false);
            map.setZoomAndCenter(6,[120.111691,30.219249]);
            map.clearMap( );
            //清楚巡航器并隐藏轨迹
            if (Welcome.pathSimplifier != null){
                Welcome.pathSimplifier.clearPathNavigators();
                Welcome.pathSimplifier.hide();
            }
        },
        trailClick : function (map) {
            Welcome.hideAndShow(false, true, false);
            map.clearMap();
            map.setZoomAndCenter(6,[120.111691,30.219249]);
        },
        baiduMapClick : function () {
            Welcome.hideAndShow(false, false, true);
            if (Welcome.baiduMap == null) {
                var height = $(window).height() - $(".headtop").height() - $(".copyright").height() - $(".new-bread").height() - $(".list-search").height() - 24
                $('#map-content-baidu').css("min-height", height);
                // //初始化地图,选取第一个点为起始点
                Welcome.baiduMap = new BMap.Map("map-content-baidu");
                Welcome.baiduMap.centerAndZoom(new BMap.Point(120.111691, 30.219249), 15);
                Welcome.baiduMap.enableScrollWheelZoom();
                Welcome.baiduMap.addControl(new BMap.NavigationControl());
                Welcome.baiduMap.addControl(new BMap.ScaleControl());
                Welcome.baiduMap.addControl(new BMap.OverviewMapControl({isOpen: true}));
            }

        },
        hideAndShow : function (isPosition, isTrail, isBaiduTrail) {
            $("#deviceCode").val("");
            if (isPosition){
                $("#tabName").text("实时位置");
                $("#beginTime").hide();
                $("#finishTime").hide();
                $("#positionSearch").show();
                $("#trailSearch").hide();
            }
            if (isTrail){
                $("#trailSearch").show();
            }
            if (isBaiduTrail){
                $("#trailSearch").hide();
                $("#baiduTrailSearch").show();
                $("#map-content").hide();
                $("#map-content-baidu").show();
            }
            if (isBaiduTrail || isTrail){
                $("#tabName").text("历史轨迹")
                $("#beginTime").show();
                $("#finishTime").show();
                $("#positionSearch").hide();
                var time = new Date();
                $("#startTime").val(time.getFullYear() + "-" + (time.getMonth()+1) + "-" + time.getDate() + " 00:00")
                $("#endTime").val(time.getFullYear() + "-" + (time.getMonth()+1) + "-" + time.getDate() + " " + time.getHours() + ":" + time.getMinutes())
            }
            if (isPosition || isTrail){
                $("#map-content-baidu").hide();
                $("#map-content").show();
                $("#baiduTrailSearch").hide();
            }
        },
        getPosition : function (url,data,map) {
            $.ajax({
                url : url,
                data : data,
                type : 'POST',
                error : function(xhr, ajaxOptions, thrownError){

                },
                success : function(response){
                    if (response.success){
                        map.setZoomAndCenter(13,[response.data.lng,response.data.lat]);
                        AMapUI.loadUI(['control/BasicControl'], function(BasicControl) {
                            //添加一个缩放控件
                            map.addControl(new BasicControl.Zoom({
                                position: 'lt'
                            }));

                            //缩放控件，显示Zoom值
                            map.addControl(new BasicControl.Zoom({
                                position: 'lb',
                                showZoomNum: true
                            }));

                            //图层切换控件
                            map.addControl(new BasicControl.LayerSwitcher({
                                position: 'rt'
                            }));
                        });
                        Welcome.marker = new AMap.Marker({
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
                                    AMap.event.addListener(Welcome.marker, 'click', function() {
                                        infoWindow.open(map, Welcome.marker.getPosition());
                                    });
                                }
                            })
                        })
                    }else {
                        toastr.warning(response.message);
                    }
                }
            })
        },
        genPoints : function (point, map, info, iconText, shapeColor) {
            AMapUI.loadUI(['overlay/SvgMarker'], function(SvgMarker) {
                if (!SvgMarker.supportSvg) {
                    //当前环境并不支持SVG，此时SvgMarker会回退到父类，即SimpleMarker
                    alert('当前环境不支持SVG');
                }
                //创建shape
                var shape = new SvgMarker.Shape['WaterDrop']({
                    height: 24,
                    strokeWidth: 1,
                    strokeColor: '#ccc',
                    fillColor: shapeColor
                });
                var labelCenter = shape.getCenter();

                var lnglat = [];
                lnglat.push(point.longitude)
                lnglat.push(point.latitude)
                var marker = new SvgMarker(
                    shape, {
                        map: map,
                        position: lnglat,
                        containerClassNames: 'shape-WaterDrop',
                        iconLabel: {
                            innerHTML: String.fromCharCode(iconText.charCodeAt(0)),
                            style: {
                                top: labelCenter[1] - 9 + 'px'
                            }
                        },
                        showPositionPoint: true
                    });

                var infoWindow = new AMap.InfoWindow({content: info.join("<br>")});
                marker.content = info.join("<br>");
                marker.on('click', markerClick);
                function markerClick(e) {
                    infoWindow.setContent(e.target.content);
                    infoWindow.open(map, e.target.getPosition());
                }
            })
        },
        getTrail : function (url,data,map) {
            $.ajax({
                url : url,
                data : data,
                type : 'POST',
                error : function(xhr, ajaxOptions, thrownError){

                },
                success : function(response){
                    if (response.success){
                        // map = new AMap.Map('map-content',{
                        //     center: [120.198487,30.785582],
                        //     zoom:13
                        // })
                        map.setZoomAndCenter(6,[120.111691,30.219249]);

                        AMapUI.load(['ui/misc/PathSimplifier', 'lib/$'], function(PathSimplifier, $) {
                            if (!PathSimplifier.supportCanvas) {
                                alert('当前环境不支持 Canvas！');
                                return;
                            }
                            var pathSimplifierIns = new PathSimplifier({
                                zIndex: 100,
                                //autoSetFitView:false,
                                map: map, //所属的地图实例

                                getPath: function(pathData, pathIndex) {

                                    return pathData.path;
                                },
                                getHoverTitle: function(pathData, pathIndex, pointIndex) {

                                    if (pointIndex >= 0) {
                                        //point
                                        return pathData.name + '，点：' + pointIndex + '/' + pathData.path.length;
                                    }

                                    return pathData.name + '，点数量' + pathData.path.length;
                                },
                                renderOptions: {

                                    renderAllPointsIfNumberBelow: 100 //绘制路线节点，如不需要可设置为-1
                                }
                            });
                            Welcome.pathSimplifier = pathSimplifierIns;

                            window.pathSimplifierIns = pathSimplifierIns;

                            //设置数据
                            var trails = new Array();
                            for(var i = 0; i < response.data.trails.length; i++){
                                trails[i] = response.data.trails[i];
                            }
                            pathSimplifierIns.setData([{
                                name: "轨迹",
                                path: trails
                            }]);
                            //对第一条线路（即索引 0）创建一个巡航器
                            var navg1 = pathSimplifierIns.createPathNavigator(0, {
                                loop: true, //循环播放
                                speed: 5000 //巡航速度，单位千米/小时
                            });
                            navg1.start();
                        });
                        markers = [];
                        AMapUI.loadUI(['overlay/SvgMarker'], function(SvgMarker) {
                            if (!SvgMarker.supportSvg) {
                                //当前环境并不支持SVG，此时SvgMarker会回退到父类，即SimpleMarker
                                alert('当前环境不支持SVG');
                            }
                            var parkPoints = response.data.parkerPoints;
                            for (var c = 0; c < parkPoints.length; c++) {
                                var point = parkPoints[c];
                                var info = [];
                                info.push("<div> 停留：" + point.parkTime + "</div>");
                                info.push("<div> 开始：" + point.beginTime + "</div>");
                                info.push("<div> 结束：" + point.endTime + "</div>");
                                info.push("<div>地址：" + point.position + "</div>");
                                Welcome.genPoints(point, Main.map, info, 'P', '#ff7f0e')
                            }
                            var startEndPoints = response.data.startEndPoints;
                            for (var i = 0; i < startEndPoints.length; i++){
                                var point = startEndPoints[i];
                                var info = [];
                                var iconText;
                                var shapeColor;
                                var title;
                                if (i == 0){
                                    iconText = 'S';
                                    shapeColor = '#2ca02c';
                                    title = '起点'
                                } else {
                                    iconText = 'D';
                                    shapeColor = '#d62728';
                                    title = '终点';
                                }
                                info.push("<div>" + title +  "</div>")
                                info.push("<div>时间：" + point.beginTime + "</div>");
                                info.push("<div>地址：" + point.position + "</div>");
                                Welcome.genPoints(point, Main.map, info, iconText, shapeColor)
                            }
                        })
                    }else {
                        toastr.warning(response.message);
                    }
                }
            })
        },
        getBaiduTrail : function (url,data) {
            $.ajax({
                url : url,
                data : data,
                type : 'POST',
                error : function(xhr, ajaxOptions, thrownError){
                },
                success : function(response){
                    if (response.success){
                        var points = [];
                        var car;
                        var label; //信息标签
                        var centerPoint;
                        for (var i = 0; i < response.data.trails.length;i++){
                            points.push(new BMap.Point(response.data.trails[i][0],response.data.trails[i][1]))
                        }
                        //初始化地图,选取第一个点为起始点
                        Welcome.baiduMap.centerAndZoom(new BMap.Point(response.data.trails[0][0],response.data.trails[0][1]), 15);
                        Welcome.baiduMap.addOverlay(new BMap.Polyline(points, {strokeColor: "black", strokeWeight: 5, strokeOpacity: 1}));
                    }else {
                        toastr.warning(response.message);
                    }
                }
            })
        },
        generateTime : function (startTime, endTime) {
            //处理时间格式
            var startYear = startTime.split(" ")[0].split("-")[0];
            var endYear = endTime.split(" ")[0].split("-")[0];
            var startMonth = startTime.split(" ")[0].split("-")[1];
            var endMonth = endTime.split(" ")[0].split("-")[1];
            var startDay = startTime.split(" ")[0].split("-")[2];
            var endDay = endTime.split(" ")[0].split("-")[2];
            var endTimeInt = endYear + endMonth + endDay + endTime.split(" ")[1].split(":")[0] + endTime.split(" ")[1].split(":")[1]+"00";
            if (startYear == endYear && startMonth == endMonth && (endDay - startDay == 1)){//确保一天的查询时间范围从 00:00:00 到 23:59:59
                var endHour = endTime.split(" ")[1].split(":")[0];
                var endMinute = endTime.split(" ")[1].split(":")[1];
                if (endHour == 0 && endMinute == 0){
                    // endTime = endYear + "-" + endMonth + "-" + startDay + " " + "23:59:59";
                    endTimeInt = endYear + endMonth + endDay + "235959";
                }
            }
            var startTimeInt = startYear+startMonth+startDay+startTime.split(" ")[1].split(":")[0]+startTime.split(" ")[1].split(":")[1]+"00";
            startTimeInt = startTimeInt.substr(2,startTimeInt.length-2)
            endTimeInt = endTimeInt.substr(2,endTimeInt.length-2)
            Welcome.searchTime = [];
            Welcome.searchTime.push(startTimeInt)
            Welcome.searchTime.push(endTimeInt)
        }
    }
})();

$(function () {
    $("#trail").click(function () {
        Welcome.trailClick(Main.map);
    })
    $("#position").click(function () {
        Welcome.positionClick(Main.map);
    })
    $("#baiduMap").click(function () {
        Welcome.baiduMapClick()
    })
    $("#positionSearch").click(function () {
        var deviceCode = $("#deviceCode").val();
        Welcome.getPosition("getPosition",{deviceCode: deviceCode},Main.map)
    })
    $("#trailSearch").click(function () {
        var isGaode = true;
        var deviceCode = $("#deviceCode").val();
        var startTime = $("#startTime").val();
        var endTime = $("#endTime").val()
        Welcome.generateTime(startTime,endTime);
        Welcome.getTrail("getTrail",{deviceCode: deviceCode,startTime: Welcome.searchTime[0],endTime: Welcome.searchTime[1],isGaode: isGaode},Main.map);
    })
    $("#baiduTrailSearch").click(function () {
        var isGaode = false;
        var deviceCode = $("#deviceCode").val();
        var startTime = $("#startTime").val();
        var endTime = $("#endTime").val()
        Welcome.generateTime(startTime,endTime);
        Welcome.getBaiduTrail("getTrail",{deviceCode: deviceCode,startTime: Welcome.searchTime[0],endTime: Welcome.searchTime[1],isGaode: isGaode})
    })
})
