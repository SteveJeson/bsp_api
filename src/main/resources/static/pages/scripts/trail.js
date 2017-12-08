var Trail = (function () {
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
                        map = new AMap.Map('map-content',{
                            center: [120.198487,30.785582],
                            zoom:13
                        })

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
                                Trail.genPoints(point, map, info, 'P', '#ff7f0e')
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
                                Trail.genPoints(point, map, info, iconText, shapeColor)
                            }
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
    Trail.init(map);
    $("#trailSearch").click(function () {
        var isGaode = true;
        var deviceCode = $("#deviceCode").val();
        var startTime = $("#startTime").val() + ":00";
        var endTime = $("#endTime").val() + ":59";
        var startYear = startTime.split(" ")[0].split("-")[0];
        var endYear = endTime.split(" ")[0].split("-")[0];
        var startMonth = startTime.split(" ")[0].split("-")[1];
        var endMonth = endTime.split(" ")[0].split("-")[1];
        var startDay = startTime.split(" ")[0].split("-")[2];
        var endDay = endTime.split(" ")[0].split("-")[2];
        if (startYear == endYear && startMonth == endMonth && (endDay - startDay == 1)){
            var endHour = endTime.split(" ")[1].split(":")[0];
            var endMinute = endTime.split(" ")[1].split(":")[1];
            if (endHour == 0 && endMinute == 0){
                endTime = endYear + "-" + endMonth + "-" + startDay + " " + "23:59:59";
                console.log("endTime: " + endTime)
            }
        }
        Trail.getTrail("getTrail",{deviceCode: deviceCode,startTime: startTime,endTime: endTime,isGaode: isGaode},map);
    })
})