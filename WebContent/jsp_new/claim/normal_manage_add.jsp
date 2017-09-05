 <!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/jstl/change" prefix="change" %>
<%@ page import="java.util.LinkedList"%>
<%@ page import="java.util.List" %>
<%@ page import=" com.infodms.dms.util.CommonUtils" %>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<head> 
<%  String contextPath = request.getContextPath(); 
	List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("fileList");
	request.setAttribute("fileList",fileList);
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=contextPath%>/js/jslib/jquery-1.7.2.js"></script>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/zyw/jquery-1.3.2.js"></script> 
<script type="text/javascript" src="<%=contextPath%>/js/jslib/zyw/jquery-calendar.js"></script> 
<link rel="stylesheet" type="text/css" href="<%=contextPath%>/js/jslib/zyw/jquery-calendar.css" /> 


<title>一般索赔单管理</title>
<script type="text/javascript">
	//公共的checked
	function ischecked(className){
		var val=$("#"+className).val();
		if(val!=""){
			var str=val.split(",");
			$("."+className).each(function(){
				for(var i=0;i<str.length;i++){
					if(str[i]==$(this).val()){
						$(this).attr("checked",true);
					}
				}
			});
		}
	}
	$(function(){
		var type=$("#type").val();
		var claimType=$("#claim_type").val();
		if("add"==type){
			$("#claim_no").val("新增后自动生成单号");
			$("#ro_no").val("点击请先选择工单信息").css("color","green");
			$("#ysq_no").val("点击选择预授权").css("color","red");
			$("#ro_no").bind("click",function(){
				findRoBaseInfo();			
			});
			if("10661006"!=claimType){
				$("#ysq_no").bind("click",function(){
					if("点击请先选择工单信息"==$("#ro_no").val()){
						MyAlert("提示：请先添加工单号，再进行选择预授权信息！");
						return;
					}
					findYsqBaseInfo(); 			 
				});
			}
			$("#our_car_1").bind("click",function(){
				our_car(1);			
			});
			$("#our_car_2").bind("click",function(){
				our_car(2);			
			});
			$("#our_car_3").bind("click",function(){
				our_car(3);
			});
			$("#QT006_apply").val("0");
			$("#QT001_apply").val("0");
			$("#QT007_apply").val("0");
			$("#QT008_apply").val("0");
			$("#QT009_apply").val("0");
			$("#out_mileage").val("0");
		}
		if("update"==type){
			var campaign_code=$("#campaign_code").val();
			if(null!=campaign_code&&""!=campaign_code){
				$("#trouble_desc").attr("readonly","readonly");
				$("#trouble_reason").attr("readonly","readonly");
			}
			var com=$("input[name='apply_amount']");
			if(com.length>0){
				$("#bntCom").attr("disabled",true);
			}
			$("input[name='old_part_code']").each(function(){
				$(this).live("click",function(){
					choose_old_part_code(this);
				});
			});
			$("input[name='producer_code']").each(function(){
				$(this).bind("click",function(){
					choose_producer_code(this);
				});
			});
			$("#our_car_1").bind("click",function(){
				our_car(1);			
			});
			$("#our_car_2").bind("click",function(){
				our_car(2);			
			});
			$("#our_car_3").bind("click",function(){
				our_car(3);
			});
		}
		if("view"==type){
			$("input[type='button']").attr("disabled",true);
			$("input[type='text']").attr("readonly","readonly");
			$("textarea").attr("readonly","readonly");
			$("input[type='checkbox']").attr("disabled",true);
			$("#report,#sure").hide();
			$("#back").attr("disabled",false);
			$(".normal_btn").each(function(){
				 if($(this).val()=="删 除" || $(this).val()=="添加附件"){
					 $(this).hide();
				} 
			});
			var com=$("input[name='apply_amount']");
			if(com.length>0){
				$("#bntCom").attr("disabled",true);
			}
		}
		if("10661006"==claimType){
			$(".campaign_code").show();
		}
		if("10661013"==claimType){
			var FREE_RO=$("#FREE_RO").val();
			if(""!=FREE_RO){
				$("#freeRo").empty();
				$("#freeRo").append("<input type='hidden' name='free_ro' value='"+FREE_RO+"'/><span>"+FREE_RO+"</span>");
			}
			$(".freeRo").show();
			if("add"!=type){
				$(".free_show").hide();
			}
		}
		if("10661001"==claimType || "10661007"==claimType){
			ischecked("out_car");
		}else{
			$("#is_out").remove();
		}
		if("点击选择预授权"!=$("#ysq_no").val() && ""!=$("#out_mileage").val()){
			$("#bc_mileage").val("1");
		}
	});
	//判断 乘车外出      自备车外出      外出背车 
	//选择乘车外出后输入公里数“交通补助”金额为空，可以手工输入
	//选择自备车外出后输入公里数“交通补助”费用自己带出金额
	//单独选择外出背车后输入公里数“交通补助”金额为0，不允许修改。
	function our_car(type){
		var our_car_1=$("#our_car_1");
		var our_car_2=$("#our_car_2");
		var our_car_3=$("#our_car_3");
		var QT007_apply=$("#QT007_apply");
		var QT001_apply=$("#QT001_apply");
		if($(our_car_1).attr("checked"))
		{
		   document.getElementById("our_car_2").disabled = true;
		}
		if($(our_car_2).attr("checked"))
		{
		   document.getElementById("our_car_1").disabled = true;
		}
		
		if(!$(our_car_1).attr("checked"))
		{
		   document.getElementById("our_car_2").disabled = false;
		}
		if(!$(our_car_2).attr("checked"))
		{
		  document.getElementById("our_car_1").disabled = false;
		}
		
		
		if($(our_car_3).attr("checked")){
		   $("#BCACTU").val("1");
		}else{
		   $("#BCACTU").val("0");
		}
		
	    if($(our_car_1).attr("checked")&&$(our_car_2).attr("checked")&&$(our_car_3).attr("checked")){
			MyAlert("提示：可以同时存在项【乘车外出和外出背车,自备车外出和外出背车】");
			$(our_car_1).removeAttr("checked");
			$(our_car_2).removeAttr("checked");
			$(our_car_3).removeAttr("checked");
			$(QT007_apply).val("0");
			$(QT001_apply).val("0");
			$("#out_mileage").val("0");
			$(QT007_apply).attr("readonly",true);
			$(QT001_apply).attr("readonly",true);
			$("#out_mileage").unbind("blur");
			return;
		}
		//乘车外出和自备车外出
		if($(our_car_1).attr("checked")&&$(our_car_2).attr("checked")){
			MyAlert("提示：可以同时存在项【乘车外出和外出背车,自备车外出和外出背车】");
			$(our_car_1).removeAttr("checked");
			$(our_car_2).removeAttr("checked");
			$(our_car_3).removeAttr("checked");
			$(QT007_apply).val("0");
			$(QT001_apply).val("0");
			$("#out_mileage").val("0");
			$(QT007_apply).attr("readonly",true);
			$(QT001_apply).attr("readonly",true);
			$("#out_mileage").unbind("blur");
			return;
		} 
		if(type==1 && $(our_car_1).attr("checked")){
			$(QT007_apply).removeAttr("readonly");
			$(QT007_apply).focus().css("color","green"); 
		}else if(type==1 && !$(our_car_1).attr("checked")){
			$(QT007_apply).attr("readonly",true);
			$(QT007_apply).val("0").css("color",""); 
		}
		if(type==2 && $(our_car_2).attr("checked")){
			$("#out_mileage").bind("keyup",function(){
				intoMoneyToQT007_apply();				
			});
			$("#out_mileage").bind("blur",function(){
				intoMoneyToQT007_apply();				
			});
			$("#out_mileage").focus().css("color","green"); 
			$(QT007_apply).attr("readonly",true);
		}else if(type==2 && !$(our_car_2).attr("checked")){
			$("#out_mileage").unbind();
			$("#out_mileage").val("0").css("color","");
			$(QT007_apply).val("0");
		}
		if(type==3 && $(our_car_3).attr("checked")){
			$(QT001_apply).removeAttr("readonly");
			$(QT001_apply).val("0");
			$(QT001_apply).focus().css("color","green"); 
		}else if(type==3 && !$(our_car_3).attr("checked")){
			$(QT001_apply).attr("readonly",true);
			$(QT001_apply).val("0").css("color",""); 
		}
		//自备车外出和外出背车
		if($(our_car_2).attr("checked")&&$(our_car_3).attr("checked")){
			
		}
		//乘车外出和外出背车 
		if($(our_car_1).attr("checked")&&$(our_car_3).attr("checked")){
			
		} 
	}
	function sureInsert(identify){
		var temp=0;
		if(""==$("#ro_no").val() || "点击请先选择工单信息"==$("#ro_no").val()){
			MyAlert("提示：请先输入工单号带出信息再提交！");
			temp++;
			return;
		}
		if(""!=$("#ysqvin").val()&&"点击选择预授权"!=$("#ysq_no").val()&& $("#ysqvin").val()!=$("#vin").val()){
			MyAlert("提示：申报预授权的车架号跟工单选择的车架号不一致，请重新选择工单或预授权单号！");
			temp++;
			return;
		}
		if(""==$("#vin").val()){
			MyAlert("提示：请先输入vin带出信息再提交！");
			temp++;
			return;
		}
		var trouble_desc=$("#trouble_desc").val();
		var trouble_reason=$("#trouble_reason").val();
		if(""==$.trim(trouble_reason)){
			MyAlert("提示：请先输入故障现象再提交！");
			temp++;
			return;
		}
		if(""==$.trim(trouble_desc)){
			MyAlert("提示：请先输入原因分析再提交！");
			temp++;
			return;
		}
		var responsibility_type=$("select[name='responsibility_type']");
		if(responsibility_type.length==0 ){
			MyAlert("提示：请至少选择一个主因件再添加！");
			temp++;
			return;
		}
		var checkPz=0;
		var checkPc=0;
		if(responsibility_type.length>0 ){
			 checkPz=0;
			 checkPc=0;
			$("select[name='responsibility_type']").each(function(){
				var val= $(this).val();
				if(val==94001001){
					checkPz++;
				}
				if(val==94001002){
					checkPc++;
				}
			});
		}
		if(checkPz!=1){
			MyAlert("提示：索赔单上有且只有一个主因件！");
			temp++;
			return;
		}
		var producer_code=$("input[name='producer_code']");
		$(producer_code).each(function(){
			if(""==$(this).val()){
				MyAlert("提示：请选择配件的供应商！");
				temp++;
				return;
			}
		});
	    var claimactivityType=$("#claim_type").val();
	    if('10661006'!=claimactivityType){//服务活动不验证留存件走预授权2015-12-22
	    	//留存件转跳预授权判断
			var arr1 = new Array(); 
			var checkids = document.getElementsByName('old_part_code');
		    for(var i=0;i<checkids.length;i++){
			    arr1.push(checkids[i].value);
		    }
	    	var url='<%=contextPath%>/ClaimAction/checkpartCode.json?arr='+arr1;
			sendAjax(url,partCodeback,'fm');
	        function partCodeback(json){
	            if(json.succ=="1"){
	               ysqGoto(4);
	               $("#ysq4").val(4);
	               temp++;
				   return;
	            }
	        }
		}
				
		var claimType=$("#claim_type").val();
		if("10661001"==claimType|| "10661007"==claimType){//改变正常和售前的有这项
			//判断是否有选择，不然填写不给予保存
			var strTemp="";
			var checkids = document.getElementsByName('out_car');
			var tempCheck=0;
		    for(var i=0;i<checkids.length;i++){
			  if(checkids[i].checked){
				  tempCheck++;
			  }
		    }
		    if(tempCheck>0){
				if(""==$.trim($("#out_person").val())){
					strTemp+=" 外出人 ";
					temp++;
				};
				if(""==$.trim($("#out_site").val())){
					strTemp+=" 外出目的地 ";
					temp++;
				};
				if(""==$.trim($("#out_mileage").val())){
					strTemp+=" 单程里程 ";
					temp++;
				};
				if(""==$.trim($("#start_date").val())){
					strTemp+=" 开始时间 ";
					temp++;
				};
				if(""==$.trim($("#end_date").val())){
					strTemp+=" 结束时间 ";
					temp++;
				};
				var startTime = $.trim($("#start_date").val());
				var endTime =$.trim($("#end_date").val());
				if(""!=startTime&&""!=endTime){//判断都不为空的基础上比较
					  var startdate = new Date((startTime).replace(/-/g,"/"));  
				      var enddate = new Date((endTime).replace(/-/g,"/"));    
				      if(enddate < startdate){//结束时间不能小于开始时间 
				    	  strTemp+=" 结束时间不能小于开始时间  ";
						  temp++;
				      } 
				}
				if(strTemp!=""){
					MyAlert("提示：您勾选了外派项，请根据提示填写完整数据"+strTemp);
					return;
				}
			}
		}
		if("10661010"==claimType|| "10661004"==claimType){//特殊服务和配件索赔
			if("点击选择预授权"==$("#ysq_no").val()){
				ysqGoto(5);
				temp++;
				return;
			}
		}
		if("10661013"==claimType){//配件索赔
			var freeRo=$("select[name='free_ro']");
			$(freeRo).each(function(){
				if(""==$(this).val()){
					MyAlert("提示：请选择自费工单号进行关联！");
					temp++;
					return;
				}
			});
		}
		var comApply=$("input[name='apply_amount']");
		ro_type_count();
		if(parseFloat($("#count_span").text())<=0.0 && "10661006"!=claimType){
			MyAlert("提示：索赔单申请金额为0不允许上报！");
			temp++;
			return;
		}
		var ysq1=$("#ysq_no").val();
	    var type= $("#type").val();
	    if(("add"==type && ysq1=="点击选择预授权")||("update"==type && ysq1=="") ){
			if(comApply.length>0 && 10661006!=claimType){
				ysqGoto(1);
				temp++;
				return;
			}
			if(""!=$("#out_mileage").val()){
				if(parseFloat($("#out_mileage").val())>=200.0){
					ysqGoto(6);
					temp++;
					return;
				}
			}
			if(parseFloat($("#count_span").text())>=1000.0){
				ysqGoto(2);
				temp++;
				return;
			}
			var checkids = document.getElementsByName('out_car');
		    for(var i=0;i<checkids.length;i++){
			  if(checkids[i].checked){
				  if(checkids[i].value=="外出背车"){
					  ysqGoto(3);
					  temp++;
					  return;
				  }
			  }
		    }
            var claim_type = $("#claim_type").val();
		    if("10661006"!=claim_type){//服务活动不走预授权
		    //配件维修类型为维修
		    var checkidss = document.getElementsByName('part_use_type');
		    for(var i=0;i<checkidss.length;i++){
			  if(checkidss[i].value==0){
					  ysqGoto(7);
					  temp++;
					  return;
			  }
		    }
		  }
		}else{
			var ysq1=$("#ysq_no").val();
		    var type= $("#type").val();
		    if(("add"==type && ysq1=="点击选择预授权")||("update"==type && ysq1=="") ){
				if($("#bc_mileage").val()=="1"){
					if(parseFloat($("#out_mileage").val())>=200.0){
						ysqGoto(6);
						temp++;
						return;
					}
				}
			    var BCACT= $('#BCACT').val();
			    if(BCACT == '1' ){ 
			       ysqGoto(3);
			        temp++;
					return;
			    }
			    var BCACTU= $('#BCACTU').val();
			    if(BCACTU == '1' ){ 
			    	ysqGoto(1);
			    	temp++;
					return;
			    }
			    //检测金额大于1000走预授权2016-1-5
			     var claim_type = $("#claim_type").val();
			        if(parseFloat($("#count_span").text())>=1000.0 && "10661006"!=claim_type){ 
			        	ysqGoto(2);
			        	temp++;
						return;
			        }

			     if("10661006"!=claim_type){//服务活动不走预授权
			     //配件维修类型为维修
				    var checkidss = document.getElementsByName('part_use_type');
				    for(var i=0;i<checkidss.length;i++){
					  if(checkidss[i].value==0){
							  ysqGoto(7);
							  temp++;
							  return;
					  }
				    }
				 }
			}
		}
	    var max_amount = $("#MAX_AMOUNT").val();
	    var count_amount =parseFloat($("#count_span").text());
	    if(""!=max_amount && max_amount.length>0){
	    	if(parseFloat(max_amount)<parseFloat(count_amount)){
	    		MyAlert("提示:预授权最大预估金额小于索赔单申请金额，不允许上报！");
	    		temp++;
	    		return;
	    	}
	    }
		if(temp==0){
			var url='<%=contextPath%>/ClaimAction/checkClaim.json';
			makeNomalFormCall1(url,function(json){
				var res=json.res;
				if(""!=res){
					MyAlert("提示："+res);
				}else{
				 var ysq = $("#ysq4").val();//限制留存件
				 var ysq1=$("#ysq_no").val().length;
				 if(4!=ysq ||15==ysq1){
					if(0==identify){
						MyConfirm("是否确认保存？",sureInsertCommit,[0]);
					}
					if(1==identify){
						MyConfirm("是否确认上报？",sureInsertCommit,[1]);
					}
				  }
				}
			},"fm");
		}
	}
	function sureInsertCommit(identify){
		$("#sure").attr("disabled",true);
		$("#report").attr("disabled",true);
		var url="<%=contextPath%>/ClaimAction/normalAddSure.json?identify="+identify;
		makeNomalFormCall1(url,sureInsertCommitBack,"fm");
	}
	function sureInsertCommitBack(json){
		var str="";
		if(json.identify=="0"){
			str+="保存";
		}
		if(json.identify=="1"){
			str+="上报";
		}
		if(json.succ=="1"){
			//MyAlert("提示："+str+"成功！");
			var url='<%=contextPath%>/ClaimAction/normalManageList.do';
			window.location.href=url;
		}else{
			MyAlert("提示："+str+"失败！");
			$("#sure").attr("disabled",false);
			$("#report").attr("disabled",false);
		}
	}
	function ro_type_count(){
		var count_part=0.0;
		var part_cost_amount=$("input[name='part_cost_amount']");
		if(part_cost_amount.length>0 ){
			$(part_cost_amount).each(function(){
					count_part+=parseFloat($(this).val());
			});
		}
		
		var count_labour=0.0;
		var labour_amount=$("input[name='labour_amount']");
		if(labour_amount.length>0 ){
			$(labour_amount).each(function(){
					count_labour+=parseFloat($(this).val());
			});
		}
		var pass_amount=$("input[name='pass_amount']");
		if(pass_amount.length>0 ){
			$(pass_amount).each(function(){
					count_labour+=parseFloat($(this).val());
			});
		}
		var accessoriesPrice=$("input[name='accessoriesPrice']");
		if(accessoriesPrice.length>0 ){
			$(accessoriesPrice).each(function(){
					count_labour+=parseFloat($(this).val());
			});
		} 
		var QT006_apply= $("#QT006_apply").val();
		var QT007_apply= $("#QT007_apply").val();
		var QT008_apply= $("#QT008_apply").val();
		var QT009_apply= $("#QT009_apply").val();
		var QT001_apply= $("#QT001_apply").val();
		if(QT006_apply!=undefined && ""!=QT006_apply){
			count_labour+=(parseFloat(QT006_apply)+parseFloat(QT007_apply)+parseFloat(QT008_apply)+parseFloat(QT009_apply)+parseFloat(QT001_apply));
		}
		var count_all=(count_part*1000/1000+count_labour*1000/1000);
		$("#count_span").text(count_all.toFixed(2));
	}
	
	function findRoBaseInfo(){
		var repairtypecode=null;
		var claimType=$("#claim_type").val();
		if("10661013"==claimType){//10661013 配件索赔
			repairtypecode=93331003;
		}else{
			repairtypecode=93331001;
		}
		OpenHtmlWindow('<%=contextPath%>/ClaimAction/findRoBaseInfo.do?repairtypecode='+repairtypecode+'&claimType='+claimType,800,500);
	}
	function findYsqBaseInfo(){
		var com=$("input[name='apply_amount']");
		if(com.length>0){
			$("#com").remove();
		}
		var claimType=$("#claim_type").val();
		var vin=$("#vin").val();
		var ro_no=$("#ro_no").val();
		OpenHtmlWindow('<%=contextPath%>/ClaimAction/findYsqBaseInfo.do?claim_type='+claimType+'&ro_no='+ro_no+"&vin="+vin,800,500);
	}
	//null返回“”
	function getNull(data) {
		if (data==null) {
			return '';
		}else {
			return data;
		}
	}
	//查询工单返回设置的值
	function backBaseInfoData(ro_no,vin,engine_no,in_mileage,color,guarantee_date,warning_level,wrgroup_id,ro_package_id,series_id,model_id,short_name,package_name,cam_code){
		$("#ro_no").val(ro_no);
		$("#vin").val(vin);
		$("#engine_no").val(getNull(engine_no));
		$("#in_mileage").val(getNull(in_mileage));
		$("#color").val(getNull(color));
		$("#guarantee_date").val(guarantee_date.substr(0,10));
		$("#vin").val(vin);
		$("#warning_level").val(getNull(warning_level));
		$("#package_id").val(ro_package_id);
		$("#wrgroup_id").val(wrgroup_id);
		$("#series_id").val(series_id);
		$("#short_name").val(short_name);
		$("#package_name").val(package_name);
		$("#model_id").val(model_id);
		if(""!=cam_code){
			$(".campaign_code").show();
			$("#campaign_code").val(cam_code);
			$("input[name='bntAdd']").attr("disabled",true);
			$("#bntCom").attr("disabled",true);
			makeFormCall('<%=contextPath%>/ActivityAction/findActityByCamCode.json?cam_code='+cam_code,findActityByCamCodeBack,'fm');
		}
		var vin = $("#vin").val();
		if(""!=vin){
            sendAjax("<%=contextPath%>/ClaimAction/querypackagename.json?vin="+vin,function(json){
                if(json.systemcar!="-1"){
                       $("#systemcar").val(json.systemcar.MODEL_NAME);
                   }
                },"fm");
	    }
	}
	function findActityByCamCodeBack(json){
		var t=json.t;
		var activity_type=t.ACTIVITY_TYPE;
		$("#trouble_desc").val(getNull(t.TROUBLE_DESC));
		$("#trouble_reason").val(getNull(t.TROUBLE_REASON));
		$("#trouble_desc").attr("readonly","readonly");
		$("#trouble_reason").attr("readonly","readonly");
		var labours=json.labours;
		var parts=json.parts;
		var acc=json.acc;
		var com=json.com;
		if(null!=acc && acc.length>0){
			str='<table id="acc" border="1" cellpadding="1" cellspacing="1" class="table_edit" width="80%" style="text-align: center;">';
			str+='<th colspan="8">';
			str+='<img class="nav" src="../jsp_new/img/subNav.gif" />辅料项目&nbsp;&nbsp;'; 
			str+='</th>';
			str+='<tr>';
			str+='<td nowrap="true" width="20%" >辅料代码</td>';
			str+='<td nowrap="true" width="20%" >辅料名称</td>';
			str+='<td nowrap="true" width="20%" >辅料费用</td>';
			str+='</tr>';
			for(var i=0;i<acc.length;i++){
				str+='<tr>';
				str+='<td nowrap="true" width="20%" ><input type="hidden" name="workHourCode" class="middle_txt" value="'+acc[i].ACC_CODE+'"/><span>'+acc[i].ACC_CODE+'</span></td>';
				str+='<td nowrap="true" width="20%" ><input type="hidden" name="workhour_name" class="middle_txt" value="'+acc[i].ACC_NAME+'"/><span>'+acc[i].ACC_NAME+'</span></td>';
				str+='<td nowrap="true" width="20%" ><input type="hidden" name="accessoriesPrice" class="middle_txt" value="'+acc[i].ACC_PRICE+'"/><span>'+acc[i].ACC_PRICE+'</span></td>';
				str+='</tr>';
			}
			str+='</table>';
			$('#new_com_add').append(str);
		}
		if(null!=com && com.length>0){
			var str="";
			str+='<table id="com" border="1" cellpadding="1" cellspacing="1" class="table_edit" width="80%" style="text-align: center;">';
			str+='<th colspan="4">';
			str+='	<img class="nav" src="../jsp_new/img/subNav.gif" />补偿费&nbsp;&nbsp;';
			str+='</th>';
			str+='<tr>';
			str+='	<td nowrap="true" width="20%" >补偿费申请金额</td>';
			str+='	<td nowrap="true" width="20%" >审批金额</td>';
			str+='	<td nowrap="true" width="40%" >备注</td>';
			str+='</tr>';
			for(var i=0;i<com.length;i++){
				str+='<tr>';
				str+='	<td nowrap="true" width="20%" >';
				str+='<input name="apply_amount" id="apply_amount" type="hidden"  value="'+com.COM_PRICE+'" size="25" /><span>'+com.COM_PRICE+'</span>';
				str+='</td>';
				str+='	<td nowrap="true" width="20%" >';
				str+='<input name="pass_amount" id="pass_amount" type="hidden"    size="25" value="'+com.COM_PRICE+'"/><span>'+com.COM_PRICE+'</span>';
				str+='</td>';
				str+='	<td nowrap="true" width="40%" >';
				str+='<input name="remark"id="remark"  type="hidden"  maxlength="100" size="55" value="'+com.REMARK+'"/><span>'+com.REMARK+'</span>';
				str+='</tr>';
			}
			str+='</table>';
			str+='<br>';
			$('#new_com_add').append(str);
		}
		if(null!=parts && parts.length>0){
			for(var i=0;i<parts.length;i++){
				var part_id=parts[i].PART_ID;
				var part_code=parts[i].PART_CODE;
				var part_name=parts[i].PART_NAME;
				var part_quantity=parts[i].PART_QUANTITY;
				var part_cost_amount=parts[i].PART_COST_AMOUNT;
				var part_cost_price=parts[i].PART_COST_PRICE;
				var part_use_type=parts[i].PART_USE_TYPE;
				var is_return=parts[i].IS_RETURN;
				var producer_code=parts[i].PRODUCER_CODE;
				var responsibility_type=parts[i].RESPONSIBILITY_TYPE;
				var str='';
				str+='<tr>';
				str+='<td nowrap="true" width="10%" >';
				str+='配件>><input name="real_part_id" type="hidden" class="middle_txt" value="'+part_id+'"/>';
				str+='</td>';
				str+='<td nowrap="true" width="10%" >';
				str+='<input name="part_code" readonly="readonly" size="10" value="'+part_code+'"/>';
				str+='</td>';
				str+='<td nowrap="true" width="25%" >';
				str+='<input name="part_name" style="width:200px;" readonly="readonly" size="10" value="'+part_name+'"/>';
				str+='</td>';
				str+='<td nowrap="true" width="10%" >';
				str+='<input name="old_part_code" readonly="readonly"  size="10" value="'+part_code+'"/>';
				str+='</td>';
				str+='<td nowrap="true" width="5%" >';
				if(part_use_type==1){
					if(10561005==activity_type){
						str+='<input name="part_cost_price"  readonly="readonly" size="10" value="0.0"/>';
					}else{
						str+='<input name="part_cost_price"  readonly="readonly" size="10" value="'+part_cost_price+'"/>';
					}
				}else{
					str+='<input name="part_cost_price"  readonly="readonly" size="10" value="0.0"/>';
				}
				str+='</td>';
				str+='<td nowrap="true" width="5%" >';
				str+='<input name="part_quantity"  readonly="readonly" size="10" value="'+part_quantity+'"/>';
				str+='</td>';
				str+='<td nowrap="true" width="10%" >';
				if(part_use_type==1){
					if(10561005==activity_type){
						str+='<input name="part_cost_amount" readonly="readonly"size="10"  value="0.0"/>';
					}else{
						str+='<input name="part_cost_amount" readonly="readonly"size="10"  value="'+part_cost_amount+'"/>';
					}
				}else{
					str+='<input name="part_cost_amount" readonly="readonly"size="10"  value="0.0"/>';
				}
				str+='</td>';
				str+='<td nowrap="true" width="10%" ><select class="min_sel" name="responsibility_type">';
				if(responsibility_type==94001001){
					str+='<option value="94001001">主因件</option>';
				}else{
					str+='<option value="94001002">次因件</option>';
				}
				str+='</select></td>';
				str+='<td nowrap="true" width="10%" ><select class="min_sel" name="part_use_type">';
				if(part_use_type==1){
					str+='<option value="1">更换</option>';
				}else{
					str+='<option value="0">维修</option>';
				}
				str+='</select></td>';
				str+='<td nowrap="true" width="10%" >';
				str+='<input name="producer_code" value="'+producer_code+'" readonly="readonly" size="10" value=""/>';
				str+='</td>';
				str+='<td nowrap="true" width="10%" ><select class="min_sel" name="is_return">';
				if(1==part_use_type){
					if("95361002"==is_return){
						str+='<option value="95361002">不回运</option>';
					}
					if("95361001"==is_return){
						str+='<option value="95361001">回运</option>';
					}
				}
				if(0==part_use_type){
					str+='<option value="95361002">不回运</option>';
				}
				str+='</select></td>';
				str+='<td nowrap="true" width="10%" >';
				str+='服务活动';
				str+='</td>';
				str+='</tr>';
				$("#tab_part").append(str);
			}
		}
		if(null!=labours && labours.length>0){
			for(var i=0;i<labours.length;i++){
				var labour_code=labours[i].LABOUR_CODE;
				var cn_des=labours[i].LABOUR_NAME;
				var labour_fix=labours[i].LABOUR_QUOTIETY;
				var parameter_value=labours[i].LABOUR_PRICE;
				var labour_amount=labours[i].LABOUR_AMOUNT;
				var str='';
				str+='<tr>';
				str+='<td nowrap="true" width="10%" >工时>></td>';
				str+='<td nowrap="true" width="20%" >';
				str+='<input name="labour_code" readonly="readonly" size="25" value="'+labour_code+'"/>';
				str+='</td>';
				str+='<td nowrap="true" width="20%" >';
				str+='<input name="labour_name" readonly="readonly" size="25" value="'+cn_des+'"/>';
				str+='</td>';
				str+='<td nowrap="true" width="20%" >';
				str+='<input name="labour_quotiety" readonly="readonly" size="25" value="'+labour_fix+'"/>';
				str+='</td>';
				str+='<td nowrap="true" width="20%" >';
				str+='<input name="labour_price" readonly="readonly" size="25" value="'+parameter_value+'"/>';
				str+='</td>';
				str+='<td nowrap="true" width="20%" >';
				str+='<input name="labour_amount" readonly="readonly" size="25" value="'+labour_amount+'"/>';
				str+='</td>';
				str+='<td nowrap="true" width="10%" >';
				str+='服务活动';
				str+='</td>';
				str+='</tr>';
				$("#tab_labour").append(str);
			}
		}
			
	}
	function addPart(){
		var ro_no=$("#ro_no").val();
		if(""==ro_no || "点击请先选择工单信息"==ro_no){
			MyAlert("提示：请先点击工单号带出信息再添加配件和工时！");
			return;
		}
		var part_id="";
		$("input[name='real_part_id']").each(function(){
			part_id+=$(this).val()+",";
		});
		var claim_type=$("#claim_type").val();
		OpenHtmlWindow('<%=contextPath%>/ClaimAction/addPartNormal.do?ro_no='+ro_no+'&part_id='+part_id+'&claim_type='+claim_type,800,500);
	}
	function addLabour(){
		var ro_no=$("#ro_no").val();
		if(""==ro_no){
			MyAlert("提示：请先点击工单号带出信息再添加配件和工时！");
			return;
		}
		var part_id="";
		$("input[name='real_part_id']").each(function(){
			part_id+=$(this).val()+",";
		});
		var labour_codes="";
		$("input[name='labour_code']").each(function(){
			labour_codes+=$(this).val()+",";
		});
		var package_id= $("#package_id").val();
		var wrgroup_id= $("#wrgroup_id").val();
		OpenHtmlWindow('<%=contextPath%>/ClaimAction/addLabourNormal.do?package_id='+package_id+'&wrgroup_id='+wrgroup_id+'&labour_codes='+labour_codes+'&part_id_1='+part_id,800,500);
	}
	var object;
	function choose_old_part_code(obj){
		object=obj;
		var series_id=$("#series_id").val();
		var model_id=$("#model_id").val();
		OpenHtmlWindow('<%=contextPath%>/ClaimAction/queryOldPartCode.do?series_id='+series_id+'&model_id='+model_id,800,500);
	}
	function setOldPartCode(part_id,part_code,part_name,claim_price_param){
		var tr=$(object).parent().parent();
		tr.children("td:eq(3)").find('input').val(part_code);
	}
	function choose_producer_code(obj){
		object=obj;
		var tr= $(obj).parent().parent();
		var partcode=tr.children("td:eq(1)").find('input').val();
		OpenHtmlWindow('<%=contextPath%>/ClaimAction/supplierCodeByPartCode.do?partcode='+partcode,800,500);
	}
	function setMainSupplierCode(maker_code,maker_shotname){
		var tr=$(object).parent().parent();
		tr.children("td:eq(9)").find('input:eq(0)').val(maker_code);
		tr.children("td:eq(9)").find('input:eq(1)').val(maker_shotname);
	}
	function setMainPartCode(part_id,part_code,part_name,part_quantity,part_cost_price,part_cost_amount,part_use_type,is_return){
		var str='';
		str+='<tr>';
		str+='<td nowrap="true" width="10%" >';
		str+='配件>><input name="real_part_id" type="hidden" class="middle_txt" value="'+part_id+'"/>';
		str+='</td>';
		str+='<td nowrap="true" width="10%" >';
		str+='<input name="part_code" readonly="readonly" size="10" value="'+part_code+'"/>';
		str+='</td>';
		str+='<td nowrap="true" width="25%" >';
		str+='<input name="part_name" style="width:200px;" readonly="readonly" size="10" value="'+part_name+'"/>';
		str+='</td>';
		str+='<td nowrap="true" width="10%" >';
		str+='<input name="old_part_code" readonly="readonly" onclick="choose_old_part_code(this);" size="10" value="'+part_code+'"/>';
		str+='</td>';
		str+='<td nowrap="true" width="5%" >';
		str+='<input name="part_cost_price" style="width:50px;" readonly="readonly" size="10" value="'+part_cost_price+'"/>';
		str+='</td>';
		str+='<td nowrap="true" width="5%" >';
		str+='<input name="part_quantity"   style="width:30px;"  readonly="readonly" size="10" value="'+part_quantity+'"/>';
		str+='</td>';
		str+='<td nowrap="true" width="10%" >';
		str+='<input name="part_cost_amount" readonly="readonly"size="10"  value="'+part_cost_amount+'"/>';
		str+='</td>';
		str+='<td nowrap="true" width="10%" ><select class="min_sel" name="responsibility_type">';
		str+='<option value="94001001">主因件</option>';
		str+='<option value="94001002">次因件</option>';
		str+='</select></td>';
		str+='<td nowrap="true" width="10%" ><select class="min_sel" name="part_use_type">';
		if(95431002==part_use_type){
			str+='<option value="1">更换</option>';
		}
		if(95431001==part_use_type){
			str+='<option value="0">维修</option>';
		}
		str+='</select></td>';
		str+='<td nowrap="true" width="10%" >';
		str+='<input name="producer_code" onclick="choose_producer_code(this);" readonly="readonly" size="10" value="" />';
		str+='<input name="producer_name" type="hidden" readonly="readonly" size="20" value=""/>';
		str+='</td>';
		str+='<td nowrap="true" width="10%" ><select class="min_sel" name="is_return">';
		if(95431002==part_use_type){
			if("95361002"==is_return){
				str+='<option value="95361002">不回运</option>';
			}
			if("95361001"==is_return){
				str+='<option value="95361001">回运</option>';
				str+='<option value="95361002">不回运</option>';
			}
		}
		if(95431001==part_use_type){
			str+='<option value="95361002">不回运</option>';
		}
		str+='</select></td>';
		str+='<td nowrap="true" width="10%" >';
		str+='<input type="button" value="删除" style="color: red;" onclick="deleteTr(this);" class="normal_btn" />';
		str+='</td>';
		str+='</tr>';
		$("#tab_part").append(str);
		var ro_no=$("#ro_no").val();
		var url='<%=contextPath%>/ClaimAction/findAccData.json?part_code='+part_code+'&ro_no='+ro_no;
		sendAjax(url,showacc,'fm');
	}
	function  showacc(json){
		var acc=json.acc;
		if(null!=acc && acc.length>0){
			if($("#acc").length>0){
				$("#acc").remove();
			}
			str='<table id="acc" border="1" cellpadding="1" cellspacing="1" class="table_edit" width="80%" style="text-align: center;">';
			str+='<th colspan="8">';
			str+='<img class="nav" src="../jsp_new/img/subNav.gif" />辅料项目&nbsp;&nbsp;'; 
			str+='</th>';
			str+='<tr>';
			str+='<td nowrap="true" width="20%" >辅料代码</td>';
			str+='<td nowrap="true" width="20%" >辅料名称</td>';
			str+='<td nowrap="true" width="20%" >辅料费用</td>';
			str+='</tr>';
			for(var i=0;i<acc.length;i++){
				str+='<tr>';
				str+='<td nowrap="true" width="20%" ><input type="hidden" name="workHourCode" class="middle_txt" value="'+acc[i].WORKHOUR_CODE+'"/><span>'+acc[i].WORKHOUR_CODE+'</span></td>';
				str+='<td nowrap="true" width="20%" ><input type="hidden" name="workhour_name" class="middle_txt" value="'+acc[i].WORKHOUR_NAME+'"/><span>'+acc[i].WORKHOUR_NAME+'</span></td>';
				str+='<td nowrap="true" width="20%" ><input type="hidden" name="accessoriesPrice" class="middle_txt" value="'+acc[i].PRICE+'"/><span>'+acc[i].PRICE+'</span></td>';
				str+='</tr>';
			}
			str+='</table>';
			$('#new_com_add').append(str);
		}
	}
	function setLabourCode(labour_code,cn_des,labour_quotiety,labour_fix,parameter_value,val){
		var str='';
		str+='<tr>';
		str+='<td nowrap="true" width="10%" >工时>></td>';
		str+='<td nowrap="true" width="20%" >';
		str+='<input name="labour_code" readonly="readonly" size="25" value="'+labour_code+'"/>';
		str+='</td>';
		str+='<td nowrap="true" width="20%" >';
		str+='<input name="labour_name" readonly="readonly" size="25" value="'+cn_des+'"/>';
		str+='</td>';
		str+='<td nowrap="true" width="20%" >';
		str+='<input name="labour_quotiety" readonly="readonly" size="25" value="'+labour_fix+'"/>';
		str+='</td>';
		str+='<td nowrap="true" width="20%" >';
		str+='<input name="labour_price" readonly="readonly" size="25" value="'+parameter_value+'"/>';
		str+='</td>';
		str+='<td nowrap="true" width="20%" >';
		str+='<input name="labour_amount" readonly="readonly" size="25" value="'+(labour_fix*parameter_value).toFixed(1)+'"/>';
		str+='</td>';
		str+='<td nowrap="true" width="10%" >';
		str+='<input type="button" value="删除" style="color: red;" onclick="deleteTr(this);" class="normal_btn" />';
		str+='</td>';
		str+='</tr>';
		$("#tab_labour").append(str);
	}
	function deleteTr(obj){
		$(obj).parent().parent().remove(); 
		if($("#acc").length>0){//移除辅料信息
			$("#acc").remove();
		}
		//根据页面上的旧件代码，工单号重新查询辅料信息
		var  object = document.getElementsByName('part_code');
        var ro_no=$("#ro_no").val();
           for(var i=0 ;i<object.length;i++){
           var part_code=  object[i].value;
   		   var url='<%=contextPath%>/ClaimAction/findAccData.json?part_code='+part_code+'&ro_no='+ro_no;
   		   sendAjax(url,showacc,'fm');
           }
	}
	function deleteTrPart(obj,partId){
		var claim_no=$("#claim_no").val();
		var ro_no=$("#ro_no").val();
		sendAjax('<%=contextPath%>/ClaimAction/deleteTrPart.json?partId='+partId+'&ro_no='+ro_no+'&claim_no='+claim_no,
			function(json){
				if(json.succ=="1"){
					MyAlert("提示：成功！索赔单配件删除，工单配件还原可用状态！");
					$(obj).parent().parent().remove(); 
				}else{
					MyAlert("提示：删除失败！");
				}
			},'fm');
	}
	function com_add(){
	   document.getElementById('BCACT').value = '1';
		$("#bntCom").attr("disabled",true);
		var div=$('#new_com_add');
		var str="";
		str+='<table id="com" border="1" cellpadding="1" cellspacing="1" class="table_edit" width="80%" style="text-align: center;">';
		str+='<th colspan="4">';
		str+='	<img class="nav" src="../jsp_new/img/subNav.gif" />补偿费&nbsp;&nbsp;<input type="button" class="normal_btn" value="删除" onclick="delComTable();"';
		str+='</th>';
		str+='<tr>';
		str+='	<td nowrap="true" width="20%" >补偿费申请金额</td>';
		str+='	<td nowrap="true" width="20%" >审批金额</td>';
		str+='	<td nowrap="true" width="40%" >备注</td>';
		str+='</tr>';
		str+='<tr>';
		str+='	<td nowrap="true" width="20%" >';
		str+='<input name="apply_amount" id="apply_amount"  size="25" onblur="comCheckPrice(this);" />';
		str+='</td>';
		str+='	<td nowrap="true" width="20%" >';
		str+='<input name="pass_amount" id="pass_amount" readonly="readonly"   size="25" value=""/>';
		str+='</td>';
		str+='	<td nowrap="true" width="40%" >';
		str+='<input name="remark"  maxlength="100" size="55" value=""/>';
		str+='</tr>';
		str+='</table>';
		str+='<br>';
		div.append(str);
		ysqGoto(1);
	}
	function delComTable(){
	   document.getElementById('BCACT').value = '0';
		$("#com").remove();
		$("#bntCom").attr("disabled",false);
	}
	function intoMoneyToQT007_apply(){
		var val=$("#out_mileage").val();
		var our_car_2_our_car_2 = $("#our_car_2").attr("checked");
		if(val!=""&& /^[0-9]+([.]{1}[0-9]{1,2})?$/.test(val)&&our_car_2_our_car_2){
			$("#QT007_apply").val(val*3.0.toFixed(1));
		}else if(0==val){
			MyAlert("自备车超过200KM需预授权");
		}else if(our_car_2_our_car_2){
			MyAlert("提示：请输入正确的数据！");
			$("#out_mileage").val("0");
		}
	}
	function ysqGoto(type){
		var ysq1=$("#ysq_no").val();
	    var types= $("#type").val();
	    if(("add"==types && ysq1=="点击选择预授权")||("update"==types && ysq1=="") ){
			var str="";
			if(type==1){//补偿费
				str+="【添加补偿费】";
			}
			if(type==2){//数据金额大于1000
				str+="【填写的数据金额大于了1000.0元】";
			}
			if(type==3){//选择了背车费
				str+="【选择了背车费】";
			}
			if(type==4){//件的关注部位
				str+="【该件在维护的关注部位】";
			}
			if(type==5){//件的关注部位
				str+="【该索赔类型必须要预授权】";
			}
			if(type==6){//自备车超过200KM需预授权
				str+="【自备车超过200KM需预授权】";
			}
			if(type==7){//件的类型选择了维修需预授权
				str+="【该件维修类型是维修需预授权】";
			}
			MyConfirm("提示：您触发达到了预授权标准:"+str+"需跳至预授权，不然无法保存和上报！，是否跳转到预授权页面？",ysqGotoJspNew,"");
		}
	}
	function ysqGotoJspNew(){
		var checkPz=0;
		var checkPc=0;
		var our_car_falg = $("#our_car_3").attr("checked");//背车费标识
		var realPartId=$("input[name='real_part_id']");
		var real_part_id="";
		var responsibility_type=$("select[name='responsibility_type']");
		if(responsibility_type.length>0 ){
			 checkPz=0;
			 checkPc=0;
			$("select[name='responsibility_type']").each(function(i){
				var val= $(this).val();
				if(val==94001001){
				   var part_code_main=$(this).parent().parent().children().eq(3).children().val();
				   var part_name_main=$(this).parent().parent().children().eq(2).children().val();
				   var produce_code_main=$(this).parent().parent().children().eq(9).children().eq(0).val();
				   var produce_name_main=$(this).parent().parent().children().eq(9).children().eq(1).val();
					$("#part_code_main").val(part_code_main);
					$("#part_name_main").val(part_name_main);
					$("#produce_code_main").val(produce_code_main);
					$("#produce_name_main").val(produce_name_main);
					real_part_id=$(realPartId[i]).val();
					checkPz++;
				}
				if(val==94001002){
					checkPc++;
				}
			});
		}
		if(checkPz!=1){
			MyAlert("提示：索赔单上有且只有一个主因件！");
			$("#com").remove();
			$("#bntCom").attr("disabled",false);
			return;
		}else{
			var url="<%=contextPath%>/YsqAction/goToYsqApply.do?real_part_id="+real_part_id+"&our_car_falg="+our_car_falg;
			$("#fm").attr("action",url);
			$("#fm").submit();
		}
	}
	function checkApply(obj){
		if(!(/^[0-9]+([.]{1}[0-9]{1,2})?$/.test(obj.value))){
			MyAlert("提示：请输入正整数或者正小数保留两位小数！");
			$(obj).val("0");
		}
		if("点击选择预授权"==$("#ysq_no").val()){
			if($("#QT001_apply").val()>0){
				ysqGoto(3);
			}
		}
	}
	function comCheckPrice(obj){
		if(!(/^[0-9]+([.]{1}[0-9]{1,2})?$/.test(obj.value))){
			MyAlert("提示：请输入正整数或者正小数保留两位小数！");
			$(obj).val("");
			return;
		}
		$("#pass_amount").val(obj.value);
	}
	function backBCqData(OUT_AUDIT_AMOUNT,MAX_AMOUNT){
	  if($("#is_out").length>0){
	 	 $("#QT001_apply").val(OUT_AUDIT_AMOUNT);
	 	 $("#MAX_AMOUNT").val(MAX_AMOUNT);
	 	 $("#our_car_3").attr("checked","checked");
	 	 $("#our_car_4").attr("checked","checked");
	 	  document.getElementById('our_car_3').style.display = 'none';
		  document.getElementById('our_car_4').style.display = '';
	  }
	}
	
	function backYsqData(data,ysqPart,vrLevel,max_estimate){
		
		var fo_no=data.YSQ_NO;
		var vin=data.VIN;//预授权VIN
		$("#ysqvin").val(vin);
		var trouble_desc=data.TROUBLE_DESC;
		var trouble_reason=data.TROUBLE_REASON; //原因分析
		var apply_amount=data.COM_APPLY;
		var audit_amount=data.COM_PASS;
		var apply_remark=data.COM_REMARK;
		var out_mileage=data.BC_MILEAGE;
		var is_return=data.IS_RETURN;//预授权上件是否回运
//		var is_returned=data.IS_RETURNED;
		var vr_level=vrLevel;
		$("#MAX_AMOUNT").val(max_estimate);//最大预估金额
		var maker_code=data.PRODUCER_CODE;//供应商代码
		var claimType=$("#claim_type").val();
		$("#our_car_3").removeAttr("checked");
		$("#our_car_4").removeAttr("checked");
		$("#our_car_3").css("display","none");//背车费状态
		$("#our_car_4").css("display","");
		$("#our_car_1").css("disabled","disabled");//有预授权的情况，不允许再选
		$("#our_car_2").css("disabled","disabled");
		if(""!=maker_code){
			$("#tab_part tr").eq(2).remove();
			var price=ysqPart.CLAIM_PRICE_PARAM;
			var url='<%=contextPath%>/YsqAction/judgePartUseType.json?ysq_no='+fo_no;
			makeNomalFormCall1(url,function(json){
				var judge=-1;
				var t=json.data;
				var partquantity=t.partQuantity;
				if(t.partUseType==95431002){//更换
					judge=2;
	    		}else{
	    			judge=1;//维修
	    		}
				var str='';
				str+='<tr>';
				str+='<td nowrap="true" width="10%" >';
				str+='配件>><input name="real_part_id" type="hidden" class="middle_txt" value="'+ysqPart.PART_ID+'"/>';
				str+='</td>';
				str+='<td nowrap="true" width="10%" >';
				str+='<input name="part_code" readonly="readonly" size="10" value="'+ysqPart.PART_CODE+'"/>';
				str+='</td>';
				str+='<td nowrap="true" width="10%" >';
				str+='<input name="part_name" readonly="readonly" style="width: 200px;" size="10" value="'+ysqPart.PART_NAME+'"/>';
				str+='</td>';
				str+='<td nowrap="true" width="10%" >';
				str+='<input name="old_part_code" readonly="readonly" onclick="choose_old_part_code(this);" size="10" value="'+ysqPart.PART_CODE+'"/>';
				str+='</td>';
				str+='<td nowrap="true" width="10%" >';
				str+='<input name="part_cost_price" readonly="readonly" size="10" value="'+price+'"/>';
				str+='</td>';
				if("10661004"==claimType){//10661004 急件索赔
					price=0;
				}
				str+='<td nowrap="true" width="10%" >';
				if("10661004"==claimType){//10661004 急件索赔
					str+='<input name="part_quantity" readonly="readonly" size="10" value="0"/>';
				}else{
					if(judge==2){
						str+='<input name="part_quantity" readonly="readonly" size="10" value="'+partquantity+'"/>';
					}else{
						str+='<input name="part_quantity" readonly="readonly" size="10" value="0"/>';
					}
				}
				str+='</td>';
				str+='<td nowrap="true" width="10%" >';
				if(judge==2){
					str+='<input name="part_cost_amount" readonly="readonly"size="10"  value="'+parseFloat(price*partquantity).toFixed(2)+'"/>';
				}else{
					str+='<input name="part_cost_amount" readonly="readonly"size="10"  value="0.0"/>';
				}
				str+='</td>';
				str+='<td nowrap="true" width="10%" ><select class="min_sel" name="responsibility_type">';
				str+='<option value="94001001">主因件</option>';
				str+='</select></td>';
				str+='<td nowrap="true" width="10%" ><select class="min_sel" name="part_use_type">';
				if(judge==2){
					str+='<option value="1">更换</option>';
				}else{
					str+='<option value="0">维修</option>';
				}
				str+='</select></td>';
				str+='<td nowrap="true" width="10%" >';
				str+='<input name="producer_code"  readonly="readonly" size="10" value="'+maker_code+'"/>';
				str+='</td>';
				str+='<td nowrap="true" width="10%" ><select class="min_sel" name="is_return">';
				
				if(is_return==95361001){
				   str+='<option value="95361001">回运</option>';
				}else if(is_return==95361002){
				   str+='<option value="95361002">不回运</option>';
				}
				//	if(95361002==is_returned){
				//		str+='<option value="95361002">不回运</option>';
				//	}
				//	if(95361002!=is_returned){
				//		str+='<option value="95361001">回运</option>';
				//		str+='<option value="95361002">不回运</option>';
				//	}
				str+='</select></td>';
				str+='<td nowrap="true" width="10%" >';
				str+='<input type="button" value="删除" disabled="disabled" onclick="deleteTr(this);" class="normal_btn" />';
				str+='</td>';
				str+='</tr>';
				$("#tab_part").append(str);
				$("#ysq_no").val(fo_no);
				$("#vin").val(vin);
				$("#trouble_desc").val(getNull(trouble_desc));
				$("#trouble_reason").val(getNull(trouble_reason));
				$("#out_mileage").val(getNull(out_mileage));
				$("#out_mileage").attr("tempVal",getNull(out_mileage));
				
				if(apply_amount!=null && ""!=apply_amount){
					if($("#com").length>0){
						$("#com").remove();
					}
					$("#bntCom").attr("disabled",true);
					var div=$('#new_com_add');
					var str="";
					str+='<table id="com" border="1" cellpadding="1" cellspacing="1" class="table_edit" width="80%" style="text-align: center;">';
					str+='<th colspan="4">';
					str+='	<img class="nav" src="../jsp_new/img/subNav.gif" />补偿费&nbsp;&nbsp;';
					str+='</th>';
					str+='<tr>';
					str+='	<td nowrap="true" width="20%" >补偿费申请金额</td>';
					str+='	<td nowrap="true" width="20%" >审批金额</td>';
					str+='	<td nowrap="true" width="40%" >备注</td>';
					str+='</tr>';
					str+='<tr>';
					str+='	<td nowrap="true" width="20%" >';
					str+='<input name="apply_amount" id="apply_amount" type="hidden" readonly="readonly"  size="25"  value="'+apply_amount+'"/><span>'+apply_amount+'</span>';
					str+='</td>';
					str+='	<td nowrap="true" width="20%" >';
					str+='<input name="pass_amount" id="pass_amount" type="hidden" readonly="readonly"   size="25" value="'+audit_amount+'"/><span>'+audit_amount+'</span>';
					str+='</td>';
					str+='	<td nowrap="true" width="40%" >';
					str+='<input name="remark"id="remark" readonly="readonly" type="hidden"  maxlength="100" size="55" value="'+apply_remark+'"/><span>'+apply_remark+'</span>';
					str+='</tr>';
					str+='</table>';
					str+='<br>';
					div.append(str);
				}
					$("#warning_level").val(getNull(vr_level));
					var part_code=ysqPart.PART_CODE;
					var ro_no=$("#ro_no").val();
					var url='<%=contextPath%>/ClaimAction/findAccData.json?part_code='+part_code+'&ro_no='+ro_no;
					sendAjax(url,function(json){
						var div=$('#new_com_add');
						var acc=json.acc;
						if(null!=acc && acc.length>0){
							if($("#acc").length>0){
								$("#acc").remove();
							}
							str='<table id="acc" border="1" cellpadding="1" cellspacing="1" class="table_edit" width="80%" style="text-align: center;">';
							str+='<th colspan="8">';
							str+='<img class="nav" src="../jsp_new/img/subNav.gif" />辅料项目&nbsp;&nbsp;'; 
							str+='</th>';
							str+='<tr>';
							str+='<td nowrap="true" width="20%" >辅料代码</td>';
							str+='<td nowrap="true" width="20%" >辅料名称</td>';
							str+='<td nowrap="true" width="20%" >辅料费用</td>';
							str+='</tr>';
							for(var i=0;i<acc.length;i++){
								str+='<tr>';
								str+='<td nowrap="true" width="20%" ><input type="hidden" name="workHourCode" class="middle_txt" value="'+acc[i].WORKHOUR_CODE+'"/><span>'+acc[i].WORKHOUR_CODE+'</span></td>';
								str+='<td nowrap="true" width="20%" ><input type="hidden" name="workhour_name" class="middle_txt" value="'+acc[i].WORKHOUR_NAME+'"/><span>'+acc[i].WORKHOUR_NAME+'</span></td>';
								str+='<td nowrap="true" width="20%" ><input type="hidden" name="accessoriesPrice" class="middle_txt" value="'+acc[i].PRICE+'"/><span>'+acc[i].PRICE+'</span></td>';
								str+='</tr>';
							}
							str+='</table>';
							div.append(str);
						}
					},'fm');
					sendAjax('<%=contextPath%>/ClaimAction/showDataInfoByVin.json?vin='+vin,backByVin,'fm');
			},'fm');
		}
	}
	function backByVin(json){
			var t=json.info;
			if(null==t){
				$("#in_mileage").val("");
				$("#color").val("");
				$("#engine_no").val("");
				$("#guarantee_date").val("");
				$("#model_id").val("");
				$("#package_id").val("");
				$("#wrgroup_id").val("");
				$("#series_id").val("");
			}else{
				$("#in_mileage").val(t.MILEAGE);
				$("#color").val(getNull(t.COLOR));
				$("#engine_no").val(getNull(t.ENGINE_NO));
				$("#guarantee_date").val((t.INVOICE_DATE).substr(0,10));//购车日期改为发票日期2015-10-28
				$("#model_id").val(t.MODEL_ID);
				$("#package_id").val(t.PACKAGE_ID);
				$("#wrgroup_id").val(t.WRGROUP_ID);
				$("#series_id").val(t.SERIES_ID);
			}
	}
	function btnFree(){
		var ro_no=$("#ro_no").val();
		if("点击请先选择工单信息"==ro_no){
			MyAlert("提示：请选择工单号！");
		}
		var part_id=$("input[name='real_part_id']");
		var responsibility_type=$("select[name='responsibility_type']");
		if(responsibility_type.length==0 ){
			MyAlert("提示：请至少选择一个主因件再添加！");
			return;
		}
		var partId="";
		var checkPz=0;
		if(responsibility_type.length>0 ){
			 checkPz=0;
			$("select[name='responsibility_type']").each(function(i){
				var val= $(this).val();
				if(val==94001001){
					partId=part_id[i].value;
					checkPz++;
				}
			});
		}
		if(checkPz!=1){
			MyAlert("提示：索赔单上有且只有一个主因件！");
			return;
		}
		sendAjax('<%=contextPath%>/ClaimAction/findFreeRoByPartId.json?ro_no='+ro_no+'&partId='+partId,backFreeRo,'fm');
	}
	function backFreeRo(json){
		$("#freeRo").empty();
		$("#freeRo").append(json.res);
		$("select[name='responsibility_type']").bind("change",function(){
			$("#freeRo").empty();
		});
	}
</script>
</head>
<body>
<div class="navigation">
<img src="../jsp_new/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;经销商索赔单管理&gt;一般索赔单管理
</div>
<form name="fm" id="fm" method="post">
<input class="middle_txt" id="BCACT" value="0" name="BCACT" type="hidden"  />
<input class="middle_txt" id="BCACTU" value="0" name="BCACTU" type="hidden"  />
<!-- 最大预估金额 -->
<input class="middle_txt" id="MAX_AMOUNT" value="${dataYsq.MAX_ESTIMATE }" name="MAX_AMOUNT" type="hidden"  />
<input class="middle_txt" id="bc_mileage" value="0" name="bc_mileage" type="hidden"  />
<!-- 主因件代码，主因件名称，主因件供应商 -->
<input class="middle_txt" id="part_code_main" value="" name="part_code_main" type="hidden"  />
<input class="middle_txt" id="part_name_main" value="" name="part_name_main" type="hidden"  />
<input class="middle_txt" id="produce_code_main" value="" name="produce_code_main" type="hidden"  />
<input class="middle_txt" id="produce_name_main" value="" name="produce_name_main" type="hidden"  />

<input class="middle_txt" id="type" value="${type }" name="type" type="hidden"  />
<input class="middle_txt" id="id" value="${t.ID }" name="id" type="hidden"  />
<input class="middle_txt" id="claim_type" value="${claim_type }" name="claim_type" type="hidden"  />
<input class="middle_txt" id="model_id" value="${t.MODEL_ID }" name="model_id" type="hidden"  />
<input class="middle_txt" id="series_id" value="${t.SERIES_ID }" name="series_id" type="hidden"  />
<input class="middle_txt" id="package_id" value="${t.PACKAGE_ID }" name="package_id" type="hidden"  />
<input class="middle_txt" id="wrgroup_id" value="${t.WRGROUP_ID }" name="wrgroup_id" type="hidden"  />
<input class="middle_txt" id="submit_times" value="${t.SUBMIT_TIMES }" name="submit_times" type="hidden"  />
<input type="hidden" id="out_car"  value="${r.OUT_CAR }"/>
<input type="hidden" id="FREE_RO"  value="${t.FREE_RO }"/>
<input type="hidden" id="claim_type"  value="${claim_type }"/>
<input type="hidden" id="ysq4" name="ysq4" value=""/>
<input type="hidden" id="ysqvin" name="ysqvin" value="" />
<table border="0" id="tab_base" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;" >
	<th colspan="8">
		<img class="nav" src="../jsp_new/img/subNav.gif"/>基本信息
	</th>
	<tr>
		<td width="12.5%"></td>
		<td nowrap="true" width="10%" >索赔单号：</td>
    	<td nowrap="true" width="15%" >
    		<input class="middle_txt" id="claim_no" value="${t.CLAIM_NO }" readonly="readonly" name="claim_no" type="text" maxlength="30" />
    	</td>
		<td nowrap="true" width="10%" >VIN：</td>
    	<td nowrap="true" width="15%" >
    		<input class="middle_txt" id="vin" value="${t.VIN }" readonly="readonly" name="vin" type="text" maxlength="30" />
    	</td>
    	<td nowrap="true" width="10%" >发动机号：</td>
    	<td nowrap="true" width="15%" >
    		<input class="middle_txt" id="engine_no" value="${ t.ENGINE_NO }" readonly="readonly" name="engine_no" type="text" maxlength="30" />
    	</td>
    	<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%"></td>
		<td nowrap="true" width="10%" >工单号：</td>
    	<td  width="15%" >
    		<input class="middle_txt" id="ro_no"  value="${t.RO_NO }" readonly="readonly" name="ro_no" type="text" maxlength="30" />
    	</td>
		<td nowrap="true" width="10%" >行驶里程：</td>
    	<td nowrap="true" width="15%" >
    		<input class="middle_txt" id="in_mileage" value="${t.IN_MILEAGE }" readonly="readonly" name="in_mileage" type="text" maxlength="30" />
    	</td>
    	<td nowrap="true" width="10%" >颜色：</td>
    	<td nowrap="true" width="15%" >
    		<input class="middle_txt" id="color" value="${t.APP_COLOR  }" readonly="readonly" name="color" type="text" maxlength="30" />
    	</td>
    	<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%"></td>
		<td nowrap="true" width="10%" >预授权号：</td>
    	<td  width="15%" >
    		<input class="middle_txt" id="ysq_no"   value="${t.YSQ_NO  }" readonly="readonly" name="ysq_no" type="text" maxlength="30" />
    	</td>
		<td nowrap="true" width="10%" >购车日期：</td>
    	<td nowrap="true" width="15%" >
    		<input class="middle_txt" id="guarantee_date" value="<fmt:formatDate value="${t.GUARANTEE_DATE}" pattern='yyyy-MM-dd'/>" readonly="readonly" name="guarantee_date" type="text" maxlength="30" />
    	</td>
		<td nowrap="true" width="10%" >三包预警：</td>
    	<td nowrap="true" width="15%" >
    		<input class="middle_txt" id="warning_level" value="${t.WARNING_LEVEL  }" readonly="readonly" name="warning_level" type="text" maxlength="30" />
    	</td>
    	<td width="12.5%"></td>
	</tr>
	<tr>
	  <td width="12.5%"></td>
		<td nowrap="true" width="10%" >服务站简称：</td>
		<td nowrap="true" width="15%"  class="campaign_code" >
    		<input class="middle_txt" id="short_name" value="${t.SHORTNAME}" readonly="readonly" name="short_name" type="text" maxlength="30" />
    	</td>
		<td nowrap="true" width="10%" >配置：</td>
		<td nowrap="true" width="15%"  class="campaign_code" >
    		<input class="middle_txt" id="package_name" value="${t.PACKAGE_NAME }" readonly="readonly" name="package_name" type="text" maxlength="30" />
    	</td>
		<td nowrap="true" width="10%" >车型：</td>
		<td nowrap="true" width="15%"  class="campaign_code" >
    		<input class="middle_txt" id="systemcar" value="${dataCarsystem.MODEL_NAME}" readonly="readonly" name="systemcar" type="text" maxlength="30" />
    	</td>
	  <td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%"></td>
		<td nowrap="true" width="10%" >索赔类型：</td>
    	<td  width="15%" >
    		<change:tcCode value="${claim_type }"></change:tcCode>
    	</td>
		<td nowrap="true" width="10%" style="display: none;" class="campaign_code">活动代码：</td>
    	<td nowrap="true" width="15%" style="display: none;" class="campaign_code" >
    		<input class="middle_txt" id="campaign_code" value="${t.CAMPAIGN_CODE  }" readonly="readonly" name="campaign_code" type="text" maxlength="30" />
    	</td>
		<td nowrap="true" width="10%" style="display: none;" class="freeRo">自费工单:<a class="free_show" href="#" onclick="btnFree();"><span class="free_show"  style="color: red;">[查]</span></a></td>
    	<td nowrap="true" width="15%" style="display: none;" class="freeRo" id="freeRo">
    		<select class='short_sel' name='free_ro'><option value=''>-请选择-</option></select>
    	</td>
    	<td width="12.5%"></td>
	</tr>
</table>
<div style="text-align: center; font-weight: bolder;">
	<table border="0" id="tab_2" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;">
		<tr>
				<td nowrap="true" width="5%" ></td>
				<td nowrap="true" width="30%" style="font-weight: bold;"></td>
				<td nowrap="true" width="5%" ></td>
				<td nowrap="true" width="30%" style="font-weight: bold;"></td>
				<td nowrap="true" width="30%" style="font-weight: bold; display: none;" >处理结果</td>
		</tr>
		<tr>
				<td nowrap="true" width="5%" >故障现象</td>
				<td nowrap="true" width="30%" >
					<textarea style="font-weight: bold;" name="trouble_reason" id="trouble_reason" rows="4" cols="35">${t.TROUBLE_REASON }</textarea>
				</td>
				<td nowrap="true" width="5%" >原因分析及处理结果</td>
				<td nowrap="true" width="30%" >
					<textarea style="font-weight: bold;" name="trouble_desc" id="trouble_desc" rows="4" cols="35">${t.TROUBLE_DESC }</textarea>
				</td>
				<td nowrap="true" width="30%" style="display: none;">
					<textarea style="font-weight: bold;" name="repair_method" id="repair_method" rows="4" cols="30">${t.REPAIR_METHOD }</textarea>
				</td>
		</tr>
	</table>
</div>
<div style="text-align: center;">
    		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color: red;">费用合计: </span>
    		<span style="color: red; font-weight: bold;" id="count_span">${t.BALANCE_AMOUNT }</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    		<input type="button"  name="bntCount"  id="bntCount"  value="合计" onclick="ro_type_count();" class="normal_btn" />
    		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;提示：[点击合计可以计算当前页面的总费用]
    		&nbsp;&nbsp;<span style="color: red;">添加补偿请点>></span>&nbsp;&nbsp;<input type="button"  name="bntCom"  id="bntCom"  value="补偿添加" onclick="com_add();" class="normal_btn" />
    		</div> 
    		<br>
<!-- 添加附件 开始  -->
        <table id="add_file"  width="75%" class="table_info" border="0" id="file">
	    		<tr>
	        		<th>
						<input type="hidden" id="fjids" name="fjids"/>
						<img class="nav" src="<%=contextPath%>/img/subNav.gif" />附件信息
						<font color="red">
							<span id="span1"></span>
						</font>
			     		<input type="button" class="normal_btn" align="right" id="addfile" onclick="showUpload('<%=contextPath%>')" value ='添加附件'/>
					</th>
				</tr>
				<tr>
    				<td width="75%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
  				</tr>
  				<%if(fileList!=null){
	  				for(int i=0;i<fileList.size();i++) { %>
					  <script type="text/javascript">
				    		addUploadRowByDb('<%=CommonUtils.checkNull(fileList.get(i).getFilename()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFileurl())%>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>','<%=request.getContextPath()%>/util/FileDownLoad/FileDownLoadQuery.do?fjid=' );
				    	</script>
				<%}}%>
			</table> 
  		<!-- 添加附件 结束 -->
  		<br>
    		<div id="new_add">
	    		<table border="1" id="tab_part" cellpadding="1" cellspacing="1" class="table_edit" width="120%" style="text-align: center;">
					<th colspan="12">
					<img class="nav" src="../jsp_new/img/subNav.gif" />作业项目 &nbsp;&nbsp;
					</th>
					<tr>
						<td nowrap="true" width="10%" >维修配件</td>
						<td nowrap="true" width="10%" >新件代码</td>
						<td nowrap="true" width="25%" >新件名称</td>
						<td nowrap="true" width="10%" >旧件代码</td>
						<td nowrap="true" width="8%" >单价</td>
						<td nowrap="true" width="8%" >配件数量</td>
						<td nowrap="true" width="8%" >金额（元）</td>
						<td nowrap="true" width="10%" >责任性质</td>
						<td nowrap="true" width="10%" >维修方式</td>
						<td nowrap="true" width="10%" >供应商代码</td>
						<td nowrap="true" width="10%" >是否回运</td>
						<td nowrap="true" width="10%" >
							<c:if test="${t.CAMPAIGN_CODE!=null}">
								<input type="button"  name="bntAdd" disabled="disabled" value="添加" onclick="addPart();" class="normal_btn" />
							</c:if>
							<c:if test="${t.CAMPAIGN_CODE==null}">
								<input type="button"  name="bntAdd"  value="添加" onclick="addPart();" class="normal_btn" />
							</c:if>
						</td>
					</tr>
					<c:forEach items="${parts }" var="p" varStatus="status">
						<tr>
						<td nowrap="true" width="10%" >
						${status.index+1}<input name="part_id" type="hidden" class="middle_txt" value="${p.PART_ID }"/>
						<input name="real_part_id" type="hidden" class="middle_txt" value="${p.REAL_PART_ID }"/>
						</td>
						<td nowrap="true" width="10%" >
						<input name="part_code" type="hidden" readonly="readonly" size="10" value="${p.PART_CODE }"/><span>${p.PART_CODE }</span>
						</td>
						<td nowrap="true" width="10%" >
						<input name="part_name" type="hidden" style="width:100px;" readonly="readonly" size="10" value="${p.PART_NAME }"/><span>${p.PART_NAME }</span>
						</td>
						<td nowrap="true" width="10%" >
						<input name="old_part_code"  type="hidden" readonly="readonly"  size="10" value="${p.DOWN_PART_CODE }"/><span>${p.DOWN_PART_CODE }</span>
						</td>
						<td nowrap="true" width="10%" >
						<input name="part_cost_price" type="hidden" readonly="readonly" size="10" value="${p.APPLY_PRICE }"/><span>${p.APPLY_PRICE }</span>
						</td>
						<td nowrap="true" width="10%" >
						<input name="part_quantity" type="hidden" readonly="readonly" size="10" value="${p.APPLY_QUANTITY }"/><span>${p.APPLY_QUANTITY }</span>
						</td>
						<td nowrap="true" width="10%" >
						<input name="part_cost_amount" type="hidden" readonly="readonly"size="10"  value="${p.APPLY_AMOUNT }"/><span>${p.APPLY_AMOUNT }</span>
						</td>
						<td nowrap="true" width="10%" >
							<select class="min_sel" name="responsibility_type">
							<c:if test="${t.CAMPAIGN_CODE!=null}">
								<c:if test="${p.RESPONSIBILITY_TYPE==94001001}">
									<option value="94001001" selected="selected">主因件</option>
								</c:if>
								<c:if test="${p.RESPONSIBILITY_TYPE==94001002}">
									<option value="94001002" selected="selected">次因件</option>
								</c:if>
							</c:if>
							<c:if test="${t.CAMPAIGN_CODE==null}">
								<c:if test="${p.RESPONSIBILITY_TYPE==94001001}">
									<option value="94001001" selected="selected">主因件</option>
									<option value="94001002">次因件</option>
								</c:if>
								<c:if test="${p.RESPONSIBILITY_TYPE==94001002}">
									<option value="94001001">主因件</option>
									<option value="94001002" selected="selected">次因件</option>
								</c:if>
							</c:if>
						</select>
						</td>
						<td nowrap="true" width="10%" ><select class="min_sel" name="part_use_type">
						<c:if test="${p.PART_USE_TYPE==1}">
								<option value="1" selected="selected">更换</option>
						</c:if>
						<c:if test="${p.PART_USE_TYPE==0}">
								<option value="0" selected="selected">维修</option>
						</c:if>
						</select>
						</td>
						<td nowrap="true" width="10%" >
						<input name="producer_code"  readonly="readonly" size="10" value="${p.PRODUCER_CODE }"/>
						</td>
						<td nowrap="true" width="10%" >
						<select class="min_sel" name="is_return">
						<c:if test="${p.PART_USE_TYPE==0}">
								<option value="95361002">不回运</option>
						</c:if>
						<c:if test="${p.PART_USE_TYPE==1}">
								<c:if test="${t.CAMPAIGN_CODE==null}">
									<c:if test="${p.IS_RETURN==95361001}">
										<option value="95361001" >回运</option>
										<option value="95361002">不回运</option>
										</c:if>
										<c:if test="${p.IS_RETURN==95361002}">
										<option value="95361002">不回运</option>
										<option value="95361001" >回运</option>
									</c:if>
								</c:if>
								<c:if test="${t.CAMPAIGN_CODE!=null}">
									<c:if test="${p.IS_RETURN==95361001}">
										<option value="95361001" >回运</option>
										</c:if>
										<c:if test="${p.IS_RETURN==95361002}">
										<option value="95361002">不回运</option>
									</c:if>
								</c:if>
						</c:if>
						</select>
						</td>
						<td nowrap="true" width="10%" >
						<c:if test="${t.CAMPAIGN_CODE==null}">
							<input type="button" value="删除" style="color: red;" onclick="deleteTrPart(this,${p.REAL_PART_ID });" class="normal_btn" />
						</c:if>
						<c:if test="${t.CAMPAIGN_CODE!=null}">
							服务活动	
						</c:if>
						</td>
						</tr>
					</c:forEach>
	    		</table>
    			<table border="1" id="tab_labour" cellpadding="1" cellspacing="1" class="table_edit" width="120%" style="text-align: center;">
					<tr>
					<td nowrap="true" width="10%" >维修工时</td>
					<td nowrap="true" width="20%" >工时代码</td>
					<td nowrap="true" width="20%" >工时名称</td>
					<td nowrap="true" width="20%" >工时定额</td>
					<td nowrap="true" width="20%" >工时单价</td>
					<td nowrap="true" width="20%" >工时金额（元）</td>
					<td nowrap="true" width="10%" >
					<c:if test="${t.CAMPAIGN_CODE==null}">
							<input type="button"  name="bntAdd" value="添加" onclick="addLabour();" class="normal_btn" />
					</c:if>
					<c:if test="${t.CAMPAIGN_CODE!=null}">
						<input type="button" disabled="disabled"  name="bntAdd" value="添加" onclick="addLabour();" class="normal_btn" />
					</c:if>
					</td>
					</tr>
	    			 <c:forEach items="${labours }" var="l" varStatus="status">
			          <tr>
						<td nowrap="true" width="10%" >${status.index+1}</td>
						<td nowrap="true" width="20%" >
						<input name="labour_code"type="hidden" readonly="readonly" size="25" value="${l.LABOUR_CODE }"/><span>${l.LABOUR_CODE }</span>
						</td>
						<td nowrap="true" width="20%" >
						<input name="labour_name" type="hidden"readonly="readonly" size="25" value="${l.LABOUR_NAME }"/><span>${l.LABOUR_NAME  }</span>
						</td>
						<td nowrap="true" width="20%" >
						<input name="labour_quotiety" type="hidden"readonly="readonly" size="25" value="${l.APPLY_QUANTITY }"/><span>${l.APPLY_QUANTITY }</span>
						</td>
						<td nowrap="true" width="20%" >
						<input name="labour_price" type="hidden"readonly="readonly" size="25" value="${l.APPLY_PRICE }"/><span>${l.APPLY_PRICE }</span>
						</td>
						<td nowrap="true" width="20%" >
						<input name="labour_amount" type="hidden"readonly="readonly" size="25" value="${l.APPLY_AMOUNT }"/><span>${l.APPLY_AMOUNT }</span>
						</td>
						<td nowrap="true" width="10%" >
						<c:if test="${t.CAMPAIGN_CODE==null}">
							<input type="button" value="删除" style="color: red;" onclick="deleteTr(this);" class="normal_btn" />
						</c:if>
						<c:if test="${t.CAMPAIGN_CODE!=null}">
							服务活动	
						</c:if>
						</td>
						</tr>
	    			 </c:forEach>
	    		</table>
</div>
<br>
<div id="new_com_add"  style="text-align: center; width: 100%;">
<c:if test="${com!=null}">
	<table id="com" border="1" cellpadding="1" cellspacing="1" class="table_edit" width="80%" style="text-align: center;">
		<th colspan="4">
			<img class="nav" src="../jsp_new/img/subNav.gif" />补偿费&nbsp;&nbsp;
			<c:if test="${t.YSQ_NO==null}">
					<c:if test="${t.CAMPAIGN_CODE==null}">
							<input type="button" class="normal_btn" value="删除" onclick="delComTable();"/>
					</c:if>
					<c:if test="${t.CAMPAIGN_CODE!=null}">
					</c:if>
			</c:if>
		</th>
		<tr>
			<td nowrap="true" width="20%" >补偿费申请金额</td>
			<td nowrap="true" width="20%" >审批金额</td>
			<td nowrap="true" width="40%" >备注</td>
		</tr>
		<c:if test="${t.YSQ_NO!=null}">
			<tr>
				<td nowrap="true" width="20%" >
				<input name="apply_amount" type="hidden" id="apply_amount"  size="25"  value="${com.APPLY_PRICE }"/>
				<span>${com.APPLY_PRICE }</span>
				</td>
					<td nowrap="true" width="20%" >
				<input name="pass_amount"type="hidden" id="pass_amount" readonly="readonly"  size="25" value="${com.PASS_PRICE }"/>
				<span>${com.PASS_PRICE }</span>
				</td>
					<td nowrap="true" width="40%" >
				<input name="remark" type="hidden" id="remark" maxlength="100"  size="55" value="${com.REASON }"/>
				<span>${com.REASON }</span>
			</tr>
		</c:if>
		<c:if test="${t.YSQ_NO==null}">
		<c:if test="${t.CAMPAIGN_CODE!=null}">
				<tr>
				<td nowrap="true" width="20%" >
				<input name="apply_amount" type="hidden" id="apply_amount"  size="25"  value="${com.APPLY_PRICE }"/>
				<span>${com.APPLY_PRICE }</span>
				</td>
					<td nowrap="true" width="20%" >
				<input name="pass_amount"type="hidden" id="pass_amount" readonly="readonly"  size="25" value="${com.PASS_PRICE }"/>
				<span>${com.PASS_PRICE }</span>
				</td>
					<td nowrap="true" width="40%" >
				<input name="remark" type="hidden" id="remark" maxlength="100"  size="55" value="${com.REASON }"/>
				<span>${com.REASON }</span>
			</tr>			
		</c:if>
		<c:if test="${t.CAMPAIGN_CODE==null}">
			<tr>
				<td nowrap="true" width="20%" >
				<input name="apply_amount" id="apply_amount"  size="25" onblur="comCheckPrice(this);" value="${com.APPLY_PRICE }"/>
				</td>
					<td nowrap="true" width="20%" >
				<input name="pass_amount" id="pass_amount" readonly="readonly"  size="25" value="${com.PASS_PRICE }"/>
				</td>
					<td nowrap="true" width="40%" >
				<input name="remark" id="remark" maxlength="100"  size="55" value="${com.REASON }"/>
			</tr>
		</c:if>
		</c:if>
	</table>
	<br>
	</c:if> 
	
	<c:if test="${acc!=null}">
			<table id="acc" border="1" cellpadding="1" cellspacing="1" class="table_edit" width="80%" style="text-align: center;">
				<th colspan="8">
					<img class="nav" src="../jsp_new/img/subNav.gif" />辅料项目&nbsp;&nbsp;
				</th>
				<tr>
					<td nowrap="true" width="20%" >辅料代码</td>
					<td nowrap="true" width="20%" >辅料名称</td>
					<td nowrap="true" width="20%" >辅料费用</td>
					<td nowrap="true" width="20%" >关联配件</td>
				</tr>
   			 <c:forEach items="${acc }" var="a">
   				<tr><td nowrap="true" width="20%" >
					<input  type="hidden" name="workHourCode" class="middle_txt" value="${a.WORKHOUR_CODE }"/><span>${a.WORKHOUR_CODE }</span>
					</td><td nowrap="true" width="20%" >
					<input type="hidden" name="workhour_name" class="middle_txt" value="${a.WORKHOUR_NAME }"/><span>${a.WORKHOUR_NAME }</span>
					</td>
					<td nowrap="true" width="20%" >
					<input type="hidden" name="accessoriesPrice" class="middle_txt" value="${a.PRICE }"/><span>${a.PRICE }</span>
					</td>
					<td nowrap="true" width="20%" >
					<input type="hidden" name="accessoriesOutMainPart" class="middle_txt" value="${a.MAIN_PART_CODE }"/><span>${a.MAIN_PART_CODE }</span>
					</td>
					</tr>
   			 </c:forEach>
   			 </table>
    		</c:if>
</div>
 <div style="text-align: center;"  id="is_out">
			<table  border="0" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;">
					<th colspan="8">
						<img class="nav" src="../jsp_new/img/subNav.gif" />外出维修&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="checkbox" name="out_car" class="out_car" id="our_car_1" value="乘车外出"/>乘车外出&nbsp;&nbsp;
						<input type="checkbox"  name="out_car" class="out_car" id="our_car_2" value="自备车外出"/>自备车外出&nbsp;&nbsp;
						
					   <input type="checkbox"  style="display: none;" checked="checked" disabled="disabled"  name="out_carpp" class="out_car" id="our_car_4" value="外出背车"/> <input type="checkbox"   name="out_car" class="out_car" id="our_car_3" value="外出背车"/>外出背车&nbsp;&nbsp;
					  
					</th>
					<tr>
						<td nowrap="true" width="10%" >&nbsp;&nbsp;开始时间:&nbsp;</td>
						<td nowrap="true" width="10%" >
							<input type="text" id="start_date" name="start_date"  onfocus="$(this).calendar()" value="<fmt:formatDate value="${r.START_TIME }" pattern='yyyy-MM-dd HH:mm:ss'/>" readonly="readonly" class="middle_txt"/>
						</td>
						<td nowrap="true" width="10%" >&nbsp;&nbsp;外出人员:&nbsp;&nbsp;</td>
						<td nowrap="true" width="10%" >
							<input  name="out_person" id="out_person" class="middle_txt" maxlength="50" value="${r.OUT_PERSON }"/>
						</td>
						<td nowrap="true" width="10%" >&nbsp;&nbsp;单程里程:&nbsp;&nbsp;</td>
						<td nowrap="true" width="10%" >
							<input  name="out_mileage" tempVal="${r.OUT_MILEAGE }" id="out_mileage" onblur="intoMoneyToQT007_apply();" maxlength="8"  class="middle_txt" value="${r.OUT_MILEAGE }"/>
						</td>
						<td nowrap="true" width="20%" ></td>
						<td nowrap="true" width="20%" ></td>
					</tr>
					<tr>
						<td nowrap="true" width="10%" >&nbsp;&nbsp;结束时间:&nbsp;</td>
						<td nowrap="true" width="10%" >
							<input type="text" id="end_date" name="end_date"  onfocus="$(this).calendar()" value="<fmt:formatDate value="${r.END_TIME }" pattern='yyyy-MM-dd HH:mm:ss'/>" readonly="readonly" class="middle_txt"/>
						</td>
						<td nowrap="true" width="10%" >&nbsp;&nbsp;外出目的地:&nbsp;&nbsp;</td>
						<td nowrap="true" width="10%" >
							<input  name="out_site" id="out_site" class="middle_txt" maxlength="50"  value="${r.OUT_SITE }"/>
						</td>
						<td nowrap="true" width="10%" ></td>
						<td nowrap="true" width="10%" >
						</td>
						<td nowrap="true" width="20%" ></td>
						<td nowrap="true" width="20%" ></td>
					</tr>
			</table>
			<br>
			<table id="accessories"  border="1" cellpadding="1" cellspacing="1" class="table_edit" width="80%" style="text-align: center;">
					<tr >
						<th nowrap="true" width="10%" style="text-align: center;">项目名称</th>
						<th nowrap="true" width="10%" style="text-align: center;">过路过桥费</th>
						<th nowrap="true" width="10%" style="text-align: center;">交通补助费</th>
						<th nowrap="true" width="10%" style="text-align: center;">住宿费</th>
						<th nowrap="true" width="10%" style="text-align: center;">餐补费</th>
						<th nowrap="true" width="10%" style="text-align: center;">背车费</th>
					</tr>
					<tr>
						<td nowrap="true" width="10%" >
							申请金额（元）
						</td>
						<td nowrap="true" width="10%" >
							<input id="QT006_apply" name="QT006_apply" onblur="checkApply(this);" class="middle_txt" value="${o.QT006_APPLY }"/>
						</td>
						<td nowrap="true" width="10%" >
							<input  id="QT007_apply" readonly="readonly" onblur="checkApply(this);" name="QT007_apply" class="middle_txt" value="${o.QT007_APPLY }"/>
						</td>
						<td nowrap="true" width="10%" >
							<input  id="QT008_apply" name="QT008_apply" onblur="checkApply(this);"  class="middle_txt" value="${o.QT008_APPLY }"/>
						</td>
						<td nowrap="true" width="10%" >
							<input  id="QT009_apply" name="QT009_apply" onblur="checkApply(this);" class="middle_txt" value="${o.QT009_APPLY }"/>
						</td>
						<td nowrap="true" width="10%" >
							<input  id="QT001_apply" name="QT001_apply" onblur="checkApply(this);"  readonly="readonly" class="middle_txt" value="${o.QT001_APPLY }"/>
						</td>
					</tr>
			</table>
	</div>    
	<br>
	<br>
	<br>
	<br>
<table width=100% border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td height="12" align=left width="33%">&nbsp;</td>
            	<td height="12" align=center width="33%" nowrap="true">
               	&nbsp;&nbsp;
               	<input type="button" class="normal_btn" id="sure" onclick="sureInsert(0);"  style="width=8%" value="保存" />
               	&nbsp;&nbsp;
               	<input type="button" class="normal_btn" id="report" onclick="sureInsert(1);"  style="width=8%" value="上报" />
               	&nbsp;&nbsp;
				<input type="button" id="back" onClick="history.back();" class="normal_btn"  style="width=8%" value="返回"/>
   			</td>
           	<td height="12" align=center width="33%">
     			</td>
		</tr>
</table>
</form>
</body>
</html>