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
                                loop: true, //循环播放
                                speed: 1000 //巡航速度，单位千米/小时
                            });

                            navg1.start();
                        });
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