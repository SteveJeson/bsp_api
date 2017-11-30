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
        getTrail:function (url,data,map) {
            $.ajax({
                url : url,
                data : data,
                type : 'POST',
                error : function(xhr, ajaxOptions, thrownError){

                },
                success : function(response){
                    if (response.success){
                        // console.log(response);
                        map = new AMap.Map('map-content',{
                            center: [120.198487,30.785582],
                            zoom:13
                        })

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
                            for(var i = 0; i < response.data.length; i++){
                                trails[i] = response.data[i];
                            }

                            pathSimplifierIns.setData([{
                                name: "test",
                                path: trails
                            }]);

                            //对第一条线路（即索引 0）创建一个巡航器
                            var navg1 = pathSimplifierIns.createPathNavigator(0, {
                                loop: false, //循环播放
                                speed: 1000 //巡航速度，单位千米/小时
                            });

                            navg1.start();
                        });
                        markers = [];
                        AMapUI.loadUI(['overlay/SvgMarker'], function(SvgMarker) {
                            if (!SvgMarker.supportSvg) {
                                //当前环境并不支持SVG，此时SvgMarker会回退到父类，即SimpleMarker
                                alert('当前环境不支持SVG');
                            }
                            var points = [[120.306264105903,29.903348253039],[120.310802408855,29.914267306858]]
                            var colors = [
                                "#d62728", "#ff9896", "#9467bd", "#c5b0d5", "#8c564b", "#c49c94"
                            ];
                            //SvgMarker.Shape下的Shape
                            var shapeKeys = [
                                'WaterDrop'
                            ];
                            for (var c = 0; c < points.length; c++) {

                                //var x = startX + (c - colNum / 2) * 70;
                                // var y = startY + 50 + (0 - rowNum / 2) * 80;
                                //alert('x: ' + x + ' ,y: ' + y)
                                //创建shape
                                var shape = new SvgMarker.Shape[shapeKeys[0]]({
                                    height: 24 * (1 + c * 0.3),
                                    strokeWidth: 1,
                                    strokeColor: '#ccc',
                                    fillColor: colors[0]
                                });

                                var labelCenter = shape.getCenter();

                                //var position = map.pixelToLngLat(new AMap.Pixel(x, y));
                                //alert(position)
                                var info = [];
                                info.push("<div> 停留："+points[c]+"</div>");
                                info.push("<div> 开始：</div>div>");
                                info.push("<div> 结束：</div>");
                                info.push("<div>地址 : 北京市望京阜通东大街方恒国际中心A座16层</div>");
                                var infoWindow = new AMap.InfoWindow({content: info.join("<br>")});

                                var marker = new SvgMarker(
                                    shape, {
                                        map: map,
                                        position: points[c],
                                        containerClassNames: 'shape-' + shapeKeys[0],
                                        iconLabel: {
                                            innerHTML: String.fromCharCode('P'.charCodeAt(0)),
                                            style: {
                                                top: labelCenter[1] - 9 + 'px'
                                            }
                                        },
                                        showPositionPoint: true
                                    });

                                marker.content = info.join("<br>");
                                marker.on('click', markerClick);
                                marker.emit('click', {target: marker});
                                function markerClick(e) {
                                    infoWindow.setContent(e.target.content);
                                    infoWindow.open(map, e.target.getPosition());
                                }
                                map.setFitView();

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
        var deviceCode = $("#deviceCode").val();
        var startTime = $("#startTime").val() + ":00";
        var endTime = $("#endTime").val() + ":59";
        Trail.getTrail("getTrail",{deviceCode: deviceCode,startTime: startTime,endTime: endTime},map);
    })
})