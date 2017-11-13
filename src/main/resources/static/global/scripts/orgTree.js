function SelCity(obj, e) {
    //1.生成省市选择的浮层div
    var ths = obj;
    var level = $("#level").val();
    var dal = $("#head").val();
    alert('级别'+level);
    alert('表头信息'+dal);
    
    
//    var dal = '<div class="_citys"><span title="完成" style="width: 26px;line-height: 15px;" id="cColse" >完成</span><ul id="_citysheng" class="_citys0">';
//    
//    if(level == 1){
//    	dal+='<li class="citySel">省份</li><li>城市</li><li>区县</li><li>街道</li><li>合作社</li><li>农场主</li></ul><div id="_citys0" class="_citys1"></div>'+
//    		    '<div style="display:none" id="_citys1" class="_citys1"></div><div style="display:none" id="_citys2" class="_citys1">'+
//    			'</div><div style="display:none" id="_citys3" class="_citys1"></div><div style="display:none" id="_citys4" class="_citys1">'+
//    		    '</div><div style="display:none" id="_citys5" class="_citys1"></div></div>';
//    }else if(level == 2){
//    	dal+='<li class="citySel">城市</li><li>区县</li><li>街道</li><li>合作社</li><li>农场主</li></ul><div id="_citys0" class="_citys1"></div>'+
//    		    '<div style="display:none" id="_citys1" class="_citys1"></div><div style="display:none" id="_citys2" class="_citys1">'+
//    			'</div><div style="display:none" id="_citys3" class="_citys1"></div><div style="display:none" id="_citys4" class="_citys1">'+
//    		    '</div></div>';
//    	
//    }else if(level == 3){
//    	dal+='<li class="citySel">区县</li><li>街道</li><li>合作社</li><li>农场主</li></ul><div id="_citys0" class="_citys1"></div>'+
//    		    '<div style="display:none" id="_citys1" class="_citys1"></div><div style="display:none" id="_citys2" class="_citys1">'+
//    			'</div><div style="display:none" id="_citys3" class="_citys1"></div></div>';
//    }else if(level == 4){
//    	dal+='<li class="citySel">街道</li><li>合作社</li><li>农场主</li></ul><div id="_citys0" class="_citys1">'+
//    	'</div><div style="display:none" id="_citys1" class="_citys1">'+
//		'</div><div style="display:none" id="_citys2" class="_citys1"></div></div>';
//    }else if(level == 5){
//    	dal+='<li class="citySel">合作社</li><li>农场主</li></ul><div id="_citys0" class="_citys1"></div>'+
//    	'<div style="display:none" id="_citys1" class="_citys1"></div></div>';
//    }else{
//    	dal+='<li class="citySel">农场主</li></ul><div id="_citys0" class="_citys1"></div></div>';
//    };
    
    Iput.show({id: ths, event: e, content: dal, width: "470"});
    $("#cColse").click(function () {
        Iput.colse();
    });
    
    //2.读取城市的信息,生成标签添加到城市的界面
    var tb_province = [];
    var b = province;
    var orgId = "";
    $.ajax({
        url: "manage/generic/restrictOrgJson",
        type: 'POST',
        data:{
            orgId:orgId
        },
        dataType: 'json',
        success: function (response) {
            if(response.items != null){
            	 $.each(response.items, function (i, item) {
            		//显示当前账户所在顶级组织
            		if(item.level == level){
            			tb_province.push('<a data-level="0" class="orgCell" data-id="' +  item.orgId  + '" data-name="' +item.orgName + '">' +item.orgName + '</a>');
            			$("#orgId").val(item.orgId);
            			$("#_citys0").append(tb_province.join(""));
            		}
            		//点击顶级组织获得二级组织列表
                    $("#_citys0 a").click(function () {
                    	//a.获取对应市份下的区县信息
                    	if(level == 5){
                    		//关闭弹框
                            Iput.colse();
                    	};
                        var f;
                        var g = '';
                        var orgId = $(this).data('id');
                        $("#orgId").val(orgId);
                        alert("组织id" + orgId);
                        $.ajax({
                            url: "manage/generic/restrictOrgJson",
                            type: 'POST',
                            data:{
                                orgId:orgId
                            },
                            dataType: 'json',
                            success: function (response) {
                                if(response.items!=null){
                                	//显示二级组织列表
                                	$("#orgId").val(orgId);
                                	 $.each(response.items, function (i, item) {
                                		 g +='<a data-level="0" class="orgCell" data-id="' +  item.orgId  + '" data-name="' +item.orgName + '">' +item.orgName + '</a>';
                                	 });
                                	 $("#_citysheng li").removeClass("citySel");
                                	 $("#_citysheng li:eq(1)").addClass("citySel");
                                	 
                                	 $("#_citys1 a").remove();
                                     $("#_citys1").append(g);

                                     //显示城市的界面
                                     $("._citys1").hide();
                                     $("._citys1:eq(1)").show();
                                     $("#_citys0 a,#_citys1 a,#_citys2 a").removeClass("AreaS");
                                     $(this).addClass("AreaS");
                                     
                                     $("#_citys1 a").click(function () {
                                    	 
                                         //给该城市添加选中的类名
                                         $("#_citys1 a,#_citys2 a").removeClass("AreaS");
                                         $(this).addClass("AreaS");

                                         var orgId = $(this).data('id');
                                         $("#orgId").val(orgId);
                                         
                                         //把该城市名添加到输出结果中去
                                         var lev = $(this).data("name");
                                         if (document.getElementById("hproper") == null) {
                                             var hcitys = $('<input>', {
                                                 type: 'hidden',
                                                 name: "hproper",
                                                 "data-id": $(this).data("id"),
                                                 id: "hproper",
                                                 val: lev
                                             });
                                             $(ths).after(hcitys);
                                         }
                                         else {
                                             $("#hproper").attr("data-id", $(this).data("id"));
                                             $("#hproper").val(lev);
                                         }
                                         var bc = $("#hcity").val();
                                         ths.value = bc + "-" + $(this).data("name");
                                         alert('level' + level + 'value' + ths.value);
                                         
                                         if(level == 4){
                                      		//关闭弹框
                                            Iput.colse();
                                      	 };
                                         
                                         //b.添加区县信息
                                         var ar = '';
                                         var orgId =$(this).data('id');
                                         $.ajax({
                                             url: "manage/generic/restrictOrgJson",
                                             type: 'POST',
                                             data:{
                                                 orgId:orgId
                                             },
                                             dataType: 'json',
                                             success: function (response) {
                                                 if(response.items!=null){
                                                	 $("#orgId").val(orgId);
                                                 	 $.each(response.items, function (i, item) {
                                                 		 ar +='<a data-level="1" class="orgCell" data-id="' +  item.orgId  + '" data-name="' +item.orgName + '">' +item.orgName + '</a>';
                                                 	 });
                                                 	 $("#_citysheng li").removeClass("citySel");
                                                 	 $("#_citysheng li:eq(2)").addClass("citySel");
                                                 	 
                                                 	$("#_citys2 a").remove();
                                                    $("#_citys2").append(ar);
                                                    $("._citys1").hide();
                                                    $("._citys1:eq(2)").show();
                                                   
                                                    //区县的点击事件
                                                    $("#_citys2 a").click(function () {
                                                        //为该区县添加样式
                                                        $("#_citys2 a").removeClass("AreaS");
                                                        $(this).addClass("AreaS");
                                                        
                                                        var orgId = $(this).data('id');
                                                        $("#orgId").val(orgId);
                                                        
                                                        //把区县名添加到结果中去
                                                        var lev = $(this).data("name");
                                                       
                                                        if (document.getElementById("harea") == null) {
                                                            var hcitys = $('<input>', {
                                                                type: 'hidden',
                                                                name: "harea",
                                                                "data-id": $(this).data("id"),
                                                                id: "harea",
                                                                val: lev
                                                            });
                                                            $(ths).after(hcitys);
                                                        }
                                                        else {
                                                            $("#harea").val(lev);
                                                            $("#harea").attr("data-id", $(this).data("id"));
                                                        }
                                                        var bc = $("#hcity").val();
                                                        var bp = $("#hproper").val();
                                                        ths.value = bc + "-" + bp + "-" + $(this).data("name");
                                                        
                                                        if(level == 3){
                                                      		//关闭弹框
                                                            Iput.colse();
                                                      	};
                                                        
                                                        var street = '';
                                                        var orgId =$(this).data('id');
                                                        $.ajax({
                                                            url: "manage/generic/restrictOrgJson",
                                                            type: 'POST',
                                                            data:{
                                                                orgId:orgId
                                                            },
                                                            dataType: 'json',
                                                            success: function (response) {
                                                                if(response.items!=null){
                                                                	 $.each(response.items, function (i, item) {
                                                                		 street +='<a data-level="2" class="orgCell" data-id="' +  item.orgId  + '" data-name="' +item.orgName + '">' +item.orgName + '</a>';
                                                                	 });
                                                                	 $("#_citysheng li").removeClass("citySel");
                                                                	 $("#_citysheng li:eq(3)").addClass("citySel");
                                                                	 
                                                                	$("#_citys3 a").remove();
                                                                	$("#_citys3").append(street);
                                                                	$("._citys1").hide();
                                                                	$("._citys1:eq(3)").show();
                                                                  
                                                                    //区县的点击事件
                                                                    $("#_citys3 a").click(function () {
                                                                    	
                                                                       //为该区县添加样式
                                                                       $("#_citys3 a").removeClass("AreaS");
                                                                       $(this).addClass("AreaS");
                                                                       
                                                                       var orgId = $(this).data('id');
                                                                       $("#orgId").val(orgId);
                                                                       
                                                                       //把区县名添加到结果中去
                                                                       var lev = $(this).data("name");
//                                                                       if (document.getElementById("harea") == null) {
                                                                       if (document.getElementById("street") == null) {
                                                                           var hcitys = $('<input>', {
                                                                               type: 'hidden',
                                                                               name: "street",
                                                                               "data-id": $(this).data("id"),
                                                                               id: "street",
                                                                               val: lev
                                                                           });
                                                                           $(ths).after(hcitys);
                                                                       }
                                                                       else {
                                                                           $("#street").val(lev);
                                                                           $("#street").attr("data-id", $(this).data("id"));
                                                                       }
                                                                       
                                                                       var bc = $("#hcity").val();
                                                                       var bp = $("#hproper").val();
                                                                       var ba = $("#harea").val();
                                                                       ths.value = bc + "-" + bp + "-" + ba + "-" + $(this).data("name");
                                                                       alert('value :' + ths.value);
                                                                       
                                                                       if(level == 2){
                                                                     		//关闭弹框
                                                                           Iput.colse();
                                                                       };
                                                                       
                                                                       var coop = '';
                                                                       var orgId =$(this).data('id');
                                                                       alert('coop - orgId' + orgId);
                                                                       $.ajax({
                                                                           url: "manage/generic/restrictOrgJson",
                                                                           type: 'POST',
                                                                           data:{
                                                                               orgId:orgId
                                                                           },
                                                                           dataType: 'json',
                                                                       
                                                                           success: function (response) {
                                                                               if(response.items!=null){
                                                                               	 $.each(response.items, function (i, item) {
                                                                               		coop +='<a data-level="2" class="orgCell" data-id="' +  item.orgId  + '" data-name="' +item.orgName + '">' +item.orgName + '</a>';
                                                                               	 });
                                                                               	 $("#_citysheng li").removeClass("citySel");
                                                                               	 $("#_citysheng li:eq(4)").addClass("citySel");
                                                                               	 
                                                                               	 $("#_citys4 a").remove();
                                                                               	 $("#_citys4").append(coop);
                                                                               	 $("._citys1").hide();
                                                                               	 $("._citys1:eq(4)").show();
                                                                               	 
                                                                               	$("#_citys4 a").click(function (){
                                                                               		
                                                                               	//为该区县添加样式
                                                                                  $("#_citys4 a").removeClass("AreaS");
                                                                                  $(this).addClass("AreaS");
                                                                                  
                                                                                  var orgId = $(this).data('id');
                                                                                  $("#orgId").val(orgId);
                                                                                  //把区县名添加到结果中去
                                                                                  var lev = $(this).data("name");
                                                                                  if (document.getElementById("cooperative") == null) {
                                                                                	  var hcitys = $('<input>', {
	                                                                                      type: 'hidden',
	                                                                                      name: "cooperative",
	                                                                                      "data-id": $(this).data("id"),
	                                                                                      id: "cooperative",
	                                                                                      val: lev
                                                                                	  });
                                                                                	  $(ths).after(hcitys);
                                                                                   }
                                                                                   else {
                                                                                	   $("#cooperative").val(lev);
                                                                                	   $("#cooperative").attr("data-id", $(this).data("id"));
                                                                                   }
                                                                                  var bc = $("#hcity").val();
                                                                                  var bp = $("#hproper").val();
                                                                                  var ba = $("#harea").val();
                                                                                  var bo = $("#street").val();
                                                                                  alert('bo' + bo);
                                                                                  ths.value = bc + '-' + bp + '-' + ba + '-' + bo + '-' + $(this).data("name");
                                                                                  alert('_citys4' + ths.value );
                                                                               	});
                                                                               	
                                                                                //合作社的点击事件
//                                                                                $("#_citys4 a").click(function (){
//                                                                                	
//                                                                                	
//                                                                                   //为该区县添加样式
//                                                                                   $("#_citys4 a").removeClass("AreaS");
//                                                                                   $(this).addClass("AreaS");
//                                                                                   
//                                                                                   var orgId = $(this).data('id');
//                                                                                   $("#orgId").val(orgId);
//                                                                                   
//                                                                                   //把区县名添加到结果中去
//                                                                                   var lev = $(this).data("name");
//                                                                                   if (document.getElementById("cooperative") == null) {
//                                                                                       var hcitys = $('<input>', {
//                                                                                           type: 'hidden',
//                                                                                           name: "cooperative",
//                                                                                           "data-id": $(this).data("id"),
//                                                                                           id: "cooperative",
//                                                                                           val: lev
//                                                                                       });
//                                                                                       $(ths).after(hcitys);
//                                                                                   }
//                                                                                   else {
//                                                                                       $("#cooperative").val(lev);
//                                                                                       $("#cooperative").attr("data-id", $(this).data("id"));
//                                                                                   }
//                                                                                   
//                                                                                   var bc = $("#hcity").val();
//                                                                                   var bp = $("#hproper").val();
//                                                                                   var ba = $("#harea").val();
//                                                                                   var bo = $("#cooperative").val();
//                                                                                   ths.value = bc + "-" + bp + "-" + ba + "-" + bo + "-" $(this).data("name");
//                                                                       
//			                                                                       //关闭弹框
//			                                                                       Iput.colse();
//                                                                                });
//                                                                             }else{
//                                                                            	 alert('response' + response.items);
                                                                             }
                                                                           }
                                                                       });       
                                                                   });
                                                                }
                                                            }
                                                        });
                                                    });
                                                 }
                                             }
                                         });
                                     });
                                }
                            }
                        });
                        
                        //生成城市的界面
                        $("#_citys1 a").remove();
                        $("#_citys1").append(g);

                        //显示城市的界面
                        $("._citys1").hide();
                        $("._citys1:eq(1)").show();
                        $("#_citys0 a,#_citys1 a,#_citys2 a").removeClass("AreaS");
                        $(this).addClass("AreaS");

                        //获取点击省的名字,添加到输出结果中
                        var lev = $(this).data("name");
                        ths.value = $(this).data("name");
                        if (document.getElementById("hcity") == null) {
                            var hcitys = $('<input>', {
                                type: 'hidden',
                                name: "hcity",
                                "data-id": $(this).data("id"),
                                id: "hcity",
                                val: lev
                            });
                            $(ths).after(hcitys);
                        }
                        else {
                            $("#hcity").val(lev);
                            $("#hcity").attr("data-id", $(this).data("id"));
                        }

                        //城市的点击事件
                        $("#_citys1 a").click(function () {
                            //给该城市添加选中的类名
                            $("#_citys1 a,#_citys2 a").removeClass("AreaS");
                            $(this).addClass("AreaS");

                            //把该城市名添加到输出结果中去
                            var lev = $(this).data("name");
                            if (document.getElementById("hproper") == null) {
                                var hcitys = $('<input>', {
                                    type: 'hidden',
                                    name: "hproper",
                                    "data-id": $(this).data("id"),
                                    id: "hproper",
                                    val: lev
                                });
                                $(ths).after(hcitys);
                            }
                            else {
                                $("#hproper").attr("data-id", $(this).data("id"));
                                $("#hproper").val(lev);
                            }
                            var bc = $("#hcity").val();
                            ths.value = bc + "-" + $(this).data("name");

                            //b.添加区县信息
                            var ar = "";
                            var orgId =$(this).data('id');
                            $.ajax({
                                url: "manage/generic/restrictOrgJson",
                                type: 'POST',
                                data:{
                                    orgId:orgId
                                },
                                dataType: 'json',
                                success: function (response) {
                                    if(response.items!=null){
                                    	$("#orgId").val(orgId);
                                    	 $.each(response.items, function (i, item) {
                                    		 ar +='<a data-level="1" class="orgCell" data-id="' +  item.orgId  + '" data-name="' +item.orgName + '">' +item.orgName + '</a>';
                                    	 });
                                    	 $("#_citysheng li").removeClass("citySel");
                                    	 $("#_citysheng li:eq(2)").addClass("citySel");
                                    }
                                }
                            });
                            $("#_citys2 a").remove();
                            $("#_citys2").append(ar);
                            $("._citys1").hide();
                            $("._citys1:eq(2)").show();

                            //区县的点击事件
                            $("#_citys2 a").click(function () {
                                //为该区县添加样式
                                $("#_citys2 a").removeClass("AreaS");
                                $(this).addClass("AreaS");

                                //把区县名添加到结果中去
                                var lev = $(this).data("name");
                                if (document.getElementById("harea") == null) {
                                    var hcitys = $('<input>', {
                                        type: 'hidden',
                                        name: "harea",
                                        "data-id": $(this).data("id"),
                                        id: "harea",
                                        val: lev
                                    });
                                    $(ths).after(hcitys);
                                }
                                else {
                                    $("#harea").val(lev);
                                    $("#harea").attr("data-id", $(this).data("id"));
                                }
                                var bc = $("#hcity").val();
                                var bp = $("#hproper").val();
                                ths.value = bc + "-" + bp + "-" + $(this).data("name");
                                
                                //关闭弹框
                                Iput.colse();
                            });
                        });
                    });
                });
            }
        }
    });
    
    $("#_citysheng li").click(function () {
        $("#_citysheng li").removeClass("citySel");
        $(this).addClass("citySel");
        var s = $("#_citysheng li").index(this);
        $("._citys1").hide();
        $("._citys1:eq(" + s + ")").show();
    });
    
}

function getCity(obj) {
    var c = obj.data('id');
    var e = province;
    var f;
    var g = '';
    var orgId = obj.data('id');
    $.ajax({
        url: "manage/generic/restrictOrgJson",
        type: 'POST',
        data:{
            orgId:orgId
        },
        dataType: 'json',
        success: function (response) {
            if(response.items!=null){
            	$("#orgId").val(orgId);
            	 $.each(response.items, function (i, item) {
            		 g +='<a data-level="0" data-id="' +  item.orgId  + '" data-name="' +item.orgName + '">' +item.orgName + '</a>';
            		
            	 });
            	 $("#_citysheng li").removeClass("citySel");
            	 $("#_citysheng li:eq(1)").addClass("citySel");
       		    return g;
            }
        }
    });
}

function getArea(obj) {
    var c = obj.data('id');
    var e = area;
    var f = [];
    var g = '';
    $.ajax({
        url: "manage/generic/restrictOrgJson",
        type: 'POST',
        data:{
            orgId:orgId
        },
        dataType: 'json',
        success: function (response) {
            if(response.items!=null){
            	$("#orgId").val(orgId);
            	 $.each(response.items, function (i, item) {
            		 g +='<a data-level="1" data-id="' +  item.orgId  + '" data-name="' +item.orgName + '">' +item.orgName + '</a>';
            	 });
            	 $("#_citysheng li").removeClass("citySel");
            	   $("#_citysheng li:eq(2)").addClass("citySel");
            }
        }
    });
    
    for (var i = 0, plen = e.length; i < plen; i++) {
        if (e[i]['pid'] == parseInt(c)) {
            f.push(e[i]);
        }
    }
    for (var j = 0, clen = f.length; j < clen; j++) {
        g += '<a data-level="1" data-id="' + f[j]['id'] + '" data-name="' + f[j]['name'] + '" title="' + f[j]['name'] + '">' + f[j]['name'] + '</a>'
    }

    $("#_citysheng li").removeClass("citySel");
    $("#_citysheng li:eq(2)").addClass("citySel");
    return g;
}

