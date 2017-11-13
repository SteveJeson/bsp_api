function SelCity(obj, e) {
    //1.生成省市选择的浮层div
    var ths = obj;
    var level = $("#level").val();
    var type = $("#orgType").val();
    var dal = $("#head").val();

    if($(ths).hasClass('agri-group')){
        Iput.isAgri.isargi = 'group'
    }else if($(ths).hasClass('agri-move')){
        Iput.isAgri.isargi = 'move'
    }else if($(ths).hasClass('organization')){
    	Iput.isAgri.isargi = 'organization'
    }else if($(ths).hasClass('verifyOrgAndGetAddr')){
        Iput.isAgri.isargi = 'verifyOrgAndGetAddr'
    }else if($(ths).hasClass('account')){
        Iput.isAgri.isargi = 'account'
    }else if($(ths).hasClass('ismap')){
        Iput.isMap.ismap = 'yes'
    }

    //新增分组，选择分组的时候验证新的分组名称是否在该分组下已存在
    function revalidate(){
    	if($("#orgName").val() != undefined && $("#orgName").val() != ""){
			$("#orgName").removeData("previousValue");
			$("#orgForm").valid();
		}
    }

    Iput.show({id: ths, event: e, content: dal, width: "470"});
    $("#cColse").click(function () {
        Iput.colse();
        revalidate();
    });

    $('.dropdown-menu').click(function () {
        Iput.colse();
    });

    $('#header_menu').click(function () {
        Iput.colse();
    });

    $('.text-uppercase').click(function () {
        Iput.colse();
    });

    //a.读取城市的信息,生成标签添加到城市的界面
    var tb_province = [];
    var orgId = "";
    var leng = 5;
    var orgName = "";
    $.ajax({
        url: "manage/generic/restrictOrgJson",
        type: 'POST',
        data:{
            orgId:orgId,
            orgType:type
        },
        dataType: 'json',
        success: function (response) {
            if(response.items != null){
            	 $.each(response.items, function (i, item) {
            		//显示当前账户所在顶级组织(省份)
            		if(item.level == level){
                        orgName = limitLength(item.orgName,orgName,leng);
                        tb_province.push('<a data-level="0" class="orgCell" data-id="' +  item.orgId  + '" data-name="' +item.orgName + '" title="' +item.orgName + '">' +orgName + '</a>');

            			$("#_citys0").append(tb_province.join(""));
            		}
            		
            		//a.获取对应省份下的城市信息
            		//点击顶级组织获得二级组织列表(省份-城市)
                    $("#_citys0 a").click(function () {
                    	
                    	//b.添加城市信息
                        var f;
                        var g = '';
                        var orgId = $(this).data('id');
                        $("#orgId").val(orgId);
                        $.ajax({
                            url: "manage/generic/restrictOrgJson",
                            type: 'POST',
                            data:{
                                orgId:orgId,
                                orgType:type
                            },
                            dataType: 'json',
                            success: function (response) {
                                if( response.items != null && response.items.length > 0 ){
                                	//显示二级组织列表(城市)
                                	 $("#orgId").val(orgId);
                                	 $.each(response.items, function (i, item) {
                                         orgName = limitLength(item.orgName,orgName,leng);
                                		 g +='<a data-level="0" class="orgCell" data-id="' +  item.orgId  + '" data-name="' +item.orgName + '"  title="' +item.orgName + '">' +orgName + '</a>';
                                	 });
                                	 $("#_citysheng li").removeClass("citySel");
                                	 $("#_citysheng li:eq(1)").addClass("citySel");
                                	 
                                	 $("#_citys1 a").remove();
                                     $("#_citys1").append(g);

                                     //显示二级组织的界面(城市)
                                     $("._citys1").hide();
                                     $("._citys1:eq(1)").show();
                                     $("#_citys0 a,#_citys1 a,#_citys2 a").removeClass("AreaS");
                                     $(this).addClass("AreaS");
                                     
                                     //点击二级组织获得三级组织列表(城市-区县)
                                     $("#_citys1 a").click(function () {
                                    	 
                                         //给该区县添加选中的类名
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
                                         ths.value = bc + '-' + $(this).data("name");
                                         ths.title = bc + '-' + $(this).data("name");

                                         //c.添加区县信息
                                         var ar = '';
                                         var orgId =$(this).data('id');
                                         $.ajax({
                                             url: "manage/generic/restrictOrgJson",
                                             type: 'POST',
                                             data:{
                                                 orgId:orgId,
                                                 orgType:type
                                             },
                                             dataType: 'json',
                                             success: function (response) {
                                                 if(response.items != null && response.items.length > 0){
                                                	 $("#orgId").val(orgId);
                                                 	 $.each(response.items, function (i, item) {
                                                         orgName = limitLength(item.orgName,orgName,leng);
                                                 		 ar +='<a data-level="1" class="orgCell" data-id="' +  item.orgId  + '" data-name="' +item.orgName + '" title="' +item.orgName + '" >' +orgName + '</a>';
                                                 	 });
                                                 	 $("#_citysheng li").removeClass("citySel");
                                                 	 $("#_citysheng li:eq(2)").addClass("citySel");
                                                 	 
                                                 	$("#_citys2 a").remove();
                                                    $("#_citys2").append(ar);
                                                    $("._citys1").hide();
                                                    $("._citys1:eq(2)").show();
                                                   
                                                    //三级组织的点击事件(区县-街道)
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
                                                        ths.value = bc + '-' + bp + '-' + $(this).data("name");
                                                        ths.title = bc + '-' + bp + '-' + $(this).data("name");

                                                        
                                                        //d.添加街道信息
                                                        var street = '';
                                                        var orgId =$(this).data('id');
                                                        $.ajax({
                                                            url: "manage/generic/restrictOrgJson",
                                                            type: 'POST',
                                                            data:{
                                                                orgId:orgId,
                                                                orgType:type
                                                            },
                                                            dataType: 'json',
                                                            success: function (response) {
                                                                if(response.items != null && response.items.length > 0){
                                                                	 $.each(response.items, function (i, item) {
                                                                         orgName = limitLength(item.orgName,orgName,leng);
                                                                		 street +='<a data-level="2" class="orgCell" data-id="' +  item.orgId  + '" data-name="' +item.orgName + '" title="' +item.orgName + '" >' +orgName + '</a>';
                                                                	 });
                                                                	 $("#_citysheng li").removeClass("citySel");
                                                                	 $("#_citysheng li:eq(3)").addClass("citySel");
                                                                	 
                                                                	$("#_citys3 a").remove();
                                                                	$("#_citys3").append(street);
                                                                	$("._citys1").hide();
                                                                	$("._citys1:eq(3)").show();
                                                                  
                                                                    //四级组织的点击事件(街道-合作社)
                                                                    $("#_citys3 a").click(function () {
                                                                    	
                                                                       //为该街道添加样式
                                                                       $("#_citys3 a").removeClass("AreaS");
                                                                       $(this).addClass("AreaS");
                                                                       
                                                                       var orgId = $(this).data('id');
                                                                       $("#orgId").val(orgId);
                                                                       
                                                                       //把街道名添加到结果中去
                                                                       var lev = $(this).data("name");
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
                                                                       ths.value = bc + '-' + bp + '-' + ba + '-' + $(this).data("name");
                                                                       ths.title = bc + '-' + bp + '-' + ba + '-' + $(this).data("name");

                                                                       //e.添加合作社信息
                                                                       var coop = '';
                                                                       var orgId =$(this).data('id');
                                                                       $.ajax({
                                                                           url: "manage/generic/restrictOrgJson",
                                                                           type: 'POST',
                                                                           data:{
                                                                               orgId:orgId,
                                                                               orgType:type
                                                                           },
                                                                           dataType: 'json',
                                                                       
                                                                           success: function (response) {
                                                                               if(response.items != null && response.items.length > 0){
                                                                               	 $.each(response.items, function (i, item) {
                                                                                     orgName = limitLength(item.orgName,orgName,leng);
                                                                               		 coop +='<a data-level="2" class="orgCell" data-id="' +  item.orgId  + '" data-name="' +item.orgName + '" title="' +item.orgName + '">' +orgName + '</a>';
                                                                               	 });
                                                                               	 $("#_citysheng li").removeClass("citySel");
                                                                               	 $("#_citysheng li:eq(4)").addClass("citySel");
                                                                               	 
                                                                               	 $("#_citys4 a").remove();
                                                                               	 $("#_citys4").append(coop);
                                                                               	 $("._citys1").hide();
                                                                               	 $("._citys1:eq(4)").show();
                                                                               	 
                                                                               	$("#_citys4 a").click(function (){
                                                                               		
//                                                                               		if(level == 2){
//                                                                                   		//关闭弹框
//                                                                                         Iput.colse();
//                                                                                    };
                                                                               	  //为该合作社添加样式
                                                                                  $("#_citys4 a").removeClass("AreaS");
                                                                                  $(this).addClass("AreaS");
                                                                                  
                                                                                  var orgId = $(this).data('id');
                                                                                  $("#orgId").val(orgId);
                                                                                  //把合作社名添加到结果中去
                                                                                  var lev = $(this).data("name");
                                                                                  if (document.getElementById("coop") == null) {
                                                                                	  var hcitys = $('<input>', {
	                                                                                      type: 'hidden',
	                                                                                      name: "coop",
	                                                                                      "data-id": $(this).data("id"),
	                                                                                      id: "coop",
	                                                                                      val: lev
                                                                                	  });
                                                                                	  $(ths).after(hcitys);
                                                                                   }
                                                                                   else {
                                                                                	   $("#coop").val(lev);
                                                                                	   $("#coop").attr("data-id", $(this).data("id"));
                                                                                   }
                                                                                  var bc = $("#hcity").val();
                                                                                  var bp = $("#hproper").val();
                                                                                  var ba = $("#harea").val();
                                                                                  var bs = $("#street").val();
                                                                                  ths.value = bc + '-' + bp + '-' + ba + '-' + bs + '-' + $(this).data("name");
                                                                                  ths.title = bc + '-' + bp + '-' + ba + '-' + bs + '-' + $(this).data("name");

                                                                                  
                                                                                //e.添加农场主信息
                                                                                  var farmer = '';
                                                                                  var orgId =$(this).data('id');
                                                                                  $.ajax({
                                                                                      url: "manage/generic/restrictOrgJson",
                                                                                      type: 'POST',
                                                                                      data:{
                                                                                          orgId:orgId,
                                                                                          orgType:type
                                                                                      },
                                                                                      dataType: 'json',
                                                                                  
                                                                                      success: function (response) {
                                                                                          if(response.items != null && response.items.length > 0){
                                                                                          	 $.each(response.items, function (i, item) {
                                                                                                 orgName = limitLength(item.orgName,orgName,leng);
                                                                                          		 farmer +='<a data-level="2" javascript="mouseover();" class="orgCell" data-id="' +  item.orgId  + '" data-name="' +item.orgName + '" title="' +item.orgName + '" >' +orgName + '</a>';
                                                                                          	 });
                                                                                          	 $("#_citysheng li").removeClass("citySel");
                                                                                          	 $("#_citysheng li:eq(5)").addClass("citySel");
                                                                                          	 
                                                                                          	 $("#_citys5 a").remove();
                                                                                          	 $("#_citys5").append(farmer);
                                                                                          	 $("._citys1").hide();
                                                                                          	 $("._citys1:eq(5)").show();
                                                                                          	 
                                                                                          	$("#_citys5 a").click(function (){
                                                                                          		
                                                                                          	//为该农场主添加样式
                                                                                             $("#_citys5 a").removeClass("AreaS");
                                                                                             $(this).addClass("AreaS");
                                                                                             
                                                                                             var orgId = $(this).data('id');
                                                                                             $("#orgId").val(orgId);
                                                                                             //把农场主名添加到结果中去
                                                                                             var lev = $(this).data("name");
                                                                                             if (document.getElementById("farmer") == null) {
                                                                                            	 var hcitys = $('<input>', {
                                                                                            		 type: 'hidden',
                                                                                            		 name: "farmer",
                                                                                            		 "data-id": $(this).data("id"),
                                                                                            		 id: "farmer",
                                                                                            		 val: lev
                                                                                            	 });
                                                                                            	 $(ths).after(hcitys);
                                                                                              }
                                                                                              else {
                                                                                            	 $("#farmer").val(lev);
                                                                                            	 $("#farmer").attr("data-id", $(this).data("id"));
                                                                                              }
                                                                                             var bc = $("#hcity").val();
                                                                                             var bp = $("#hproper").val();
                                                                                             var ba = $("#harea").val();
                                                                                             var bs = $("#street").val();
                                                                                             var bf = $("#coop").val();
                                                                                             ths.value = bc + '-' + bp + '-' + ba + '-' + bs + '-' + bf + '-' + $(this).data("name");
                                                                                             ths.title = bc + '-' + bp + '-' + ba + '-' + bs + '-' + bf + '-' + $(this).data("name");

                                                                                             //关闭弹框
                                                                                             Iput.colse();
                                                                                             revalidate();
			                                                                      
                                                                                          	});
                                                                                          }else{
                                                                                         	 //关闭弹框
                                                                                           	 Iput.colse();
                                                                                           	 revalidate();
                                                                                          }
                                                                                        }
                                                                                    });      
                                                                               	});
                                                                             }else{
                                                                            	 //关闭弹框
                                                                              	 Iput.colse();
                                                                              	 revalidate();
                                                                             }
                                                                           }
                                                                       });       
                                                                   });
                                                                }else{
                                                                	//关闭弹框
                                                                 	Iput.colse();
                                                                 	revalidate();
                                                                }
                                                            }
                                                        });
                                                    });
                                                 }else{
                                                	 //关闭弹框
                                                 	 Iput.colse(); 
                                                 	 revalidate();
                                                 }
                                             }
                                         });
                                     });
                                }else{
                                	//关闭弹框
                                	Iput.colse();
                                	revalidate();
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
                        ths.title = $(this).data("name");
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
                            ths.title = bc + "-" + $(this).data("name");

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
                                    		 ar +='<a data-level="1" javascript="mouseover();" class="orgCell" data-id="' +  item.orgId  + '" data-name="' +item.orgName + '">' +item.orgName + '</a>';
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
                                ths.title = bc + "-" + bp + "-" + $(this).data("name");

                                //关闭弹框
                                Iput.colse();
                                revalidate();
                            });
                        });
                    });
                });
            }
        }
    });

    $("#cClear").click(function () {
        $("#city").val("");
        $("#orgId").val("");
    });

    $("#_citysheng li").click(function () {
        $("#_citysheng li").removeClass("citySel");
        $(this).addClass("citySel");
        var s = $("#_citysheng li").index(this);
        $("._citys1").hide();
        $("._citys1:eq(" + s + ")").show();
    });

    $("#cClear").mouseover(function () {
        $("#cClear").css("color","#fffdfe");
        $("#cClear").css("background-color","#888");
    });
    $("#cClear").mouseleave(function () {
        $("#cClear").css("color","#888");
        $("#cClear").css("background-color","#fffdfe");
    });
    $("#cColse").mouseover(function () {
        $("#cColse").css("color","	#fffdfe");
        $("#cColse").css("background-color","#56b4f8");
    });
    $("#cColse").mouseleave(function () {
        $("#cColse").css("color","#56b4f8");
        $("#cColse").css("background-color","#fffdfe");
    });
    
}

function limitLength(obj,temp,leng) {
    if(obj.length > leng){
        temp = obj.substring(0,leng)+"...";
    }else{
        temp = obj;
    }
    return temp;
}

function isEmptyObject(obj) {
	for (var key in obj) {
		return false;
	}
	return true;
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

