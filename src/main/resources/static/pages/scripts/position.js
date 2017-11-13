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