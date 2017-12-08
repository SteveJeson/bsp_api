var Baidu = (function () {
    return {
        init:function (map) {
            if($(window).height()>$(".all-wrapper").outerHeight(true)){
                var height=$(window).height()-$(".all-wrapper").outerHeight(true)+$('#map-content').outerHeight();
                $('#map-content').css("min-height",height);
            }
            //初始化地图,选取第一个点为起始点
            map = new BMap.Map("map-content");
            map.centerAndZoom(new BMap.Point(120.111691,30.219249), 15);
            map.enableScrollWheelZoom();
            map.addControl(new BMap.NavigationControl());
            map.addControl(new BMap.ScaleControl());
            map.addControl(new BMap.OverviewMapControl({isOpen: true}));


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
        getTrail:function (url,data,map) {
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
                        map = new BMap.Map("map-content");
                        map.centerAndZoom(new BMap.Point(response.data.trails[0][0],response.data.trails[0][1]), 15);
                        map.enableScrollWheelZoom();
                        map.addControl(new BMap.NavigationControl());
                        map.addControl(new BMap.ScaleControl());
                        map.addControl(new BMap.OverviewMapControl({isOpen: true}));

                        //通过DrivingRoute获取一条路线的point
                        // var driving = new BMap.DrivingRoute(map);
                        // driving.search(points[0], points[points.length-1]);
                        map.addOverlay(new BMap.Polyline(points, {strokeColor: "black", strokeWeight: 5, strokeOpacity: 1}));
                        // driving.setSearchCompleteCallback(function() {
                        //     //得到路线上的所有point
                        //     // points = driving.getResults().getPlan(0).getRoute(0).getPath();
                        //     //画面移动到起点和终点的中间
                        //     // centerPoint = new BMap.Point((points[0].lng + points[points.length - 1].lng) / 2, (points[0].lat + points
                        //     //         [points.length - 1].lat) / 2);
                        //     map.panTo(centerPoint);
                        //     //连接所有点
                        //
                        //
                        //     //显示小车子
                        //     label = new BMap.Label("", {offset: new BMap.Size(-20, -20)});
                        //     car = new BMap.Marker(points[0]);
                        //     car.setLabel(label);
                        //     map.addOverlay(car);
                        // });
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
    Baidu.init(map);
    $("#trailSearch").click(function () {
        var isGaode = false;
        var deviceCode = $("#deviceCode").val();
        var startTime = $("#startTime").val();
        var endTime = $("#endTime").val()
        var startYear = startTime.split(" ")[0].split("-")[0];
        var endYear = endTime.split(" ")[0].split("-")[0];
        var startMonth = startTime.split(" ")[0].split("-")[1];
        var endMonth = endTime.split(" ")[0].split("-")[1];
        var startDay = startTime.split(" ")[0].split("-")[2];
        var endDay = endTime.split(" ")[0].split("-")[2];
        var endTimeInt = endYear + endMonth + endDay + endTime.split(" ")[1].split(":")[0] + endTime.split(" ")[1].split(":")[1]+"00";
        if (startYear == endYear && startMonth == endMonth && (endDay - startDay == 1)){
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
        Baidu.getTrail("getTrail",{deviceCode: deviceCode,startTime: startTimeInt,endTime: endTimeInt,isGaode: isGaode},map);
    })
})