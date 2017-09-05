<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.bean.TtAsActivityBean"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/fmt" prefix="fmt"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@taglib uri="/jstl/change" prefix="change" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>服务活动管理</title>	
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/jquery-1.7.2.js"></script>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>

<script type="text/javascript">
	$(function(){
		var type=$("#type").val();
		if("add"==type){
			$("#activityCode").val("新增后自动生成");
			$("#subjectName").live("click",function(){
				showsubject();
			});
			$("#afterVehicle").attr("checked",true);
		}
		if("update"==type){
			$("#subjectName").live("click",function(){
				showsubject();
			});
			$("#back").remove();
			ischecked("vehicle");
			$("#showBtn").show();
		}
		if("view"==type){
			ischecked("vehicle");
			$("#report").remove();
			$("#sure").remove();
			$("#back").remove();
			$("#clean").attr("disabled",true);
			$("#beforeVehicle").attr("disabled",true);
			$("#afterVehicle").attr("disabled",true);
			//$("input[type='checkbox']").attr("disabled",true);
			$("input[type='text']").attr("readonly","readonly");
			$("textarea").attr("readonly","readonly");
			$("select").attr("disabled",true);
			$("#infoview2").attr("disabled",false);
			$("#infoview1").attr("disabled",false);
			$("#showBtn1").show();
			$("#viewInfo").show();
		}
		if("intoAdd"==type){
			$("#sure").remove();
			$("#report").remove();
			$("#back").remove();
			$("#clean").attr("disabled",true);
			$("#beforeVehicle").attr("disabled",true);
			$("#afterVehicle").attr("disabled",true);
			$("input[type='text']").attr("readonly","readonly");
			$("textarea").attr("readonly","readonly");
			$("select").attr("disabled",true);
			$("#infoview2").attr("disabled",false);
			$("#infoview1").attr("disabled",false);
			ischecked("vehicle");
			$("#showBtn").show();
			$("#showBtn1").show();
		}
		
	});
	function showsubject(){
		OpenHtmlWindow('<%=contextPath%>/ActivityAction/showSubject.do',800,500);
	}
	function myRadio(templet_id,templet_no,subject_id,subject_no,subject_name){
		var url="<%=contextPath%>/ActivityAction/findDatatemplet.json?id="+templet_id;
		$("#templet_id").val(templet_id);
		$("#templet_no").val(templet_no);
		makeNomalFormCall1(url,findDatatempletBack,"fm");
	}
	function findDatatempletBack(json){
		$('#new_com_add').empty();
		$('#new_acc_add').empty();
		$("#tab_part tr:gt(1)").remove(); 
		$("#tab_labour tr:gt(0)").remove(); 
		var t=json.t;
		$("#templet_no").val(t.TEMPLET_NO);
		$("#subjectId").val(t.SUBJECT_ID);
		$("#subjectName").val(t.SUBJECT_NAME);
		$("#activityType").val(t.ACTIVITY_TYPE);
		$("#t3").val((t.FACT_START_DATE).substr(0,10));
		$("#t4").val((t.FACT_END_DATE).substr(0,10));
		$("#t5").val((t.FACT_START_DATE).substr(0,10));
		$("#t6").val((t.FACT_END_DATE).substr(0,10));
		$("#trouble_desc").val(t.TROUBLE_DESC);
		$("#trouble_reason").val(t.TROUBLE_REASON);
		$("#solution").val(t.CASE_REMARK);
		$("#claimGuide").val(t.APPLY_REMARK);
		var labours=json.labours;
		if(labours!=null&&labours.length>0){
			for(var i=0;i<labours.length;i++){
				var labour_code=labours[i].LABOUR_CODE;
				var cn_des=labours[i].LABOUR_NAME;
				var labour_quotiety=labours[i].LABOUR_QUOTIETY;
				var labour_price=labours[i].LABOUR_PRICE;
				var labour_amount=labours[i].LABOUR_AMOUNT;
				setLabourCode(labour_code,cn_des,labour_quotiety,labour_price,labour_amount);
			}
		}
		var parts=json.parts;
		if(parts!=null&&parts.length>0){
			for(var i=0;i<parts.length;i++){
				var part_id=parts[i].PART_ID;
				var old_part_code=parts[i].OLD_PART_CODE;
				var part_code=parts[i].PART_CODE;
				var part_name=parts[i].PART_NAME;
				var part_cost_price=parts[i].PART_COST_PRICE;
				var part_quantity=parts[i].PART_QUANTITY;
				var part_cost_amount=parts[i].PART_COST_AMOUNT;
				var responsibility_type=parts[i].RESPONSIBILITY_TYPE;
				var part_use_type=parts[i].PART_USE_TYPE;
				var is_return=parts[i].IS_RETURN;
				var producer_code=parts[i].PRODUCER_CODE;
				setMainPartCode(part_id,old_part_code,part_code,part_name,part_cost_price,part_quantity,part_cost_price,part_cost_amount,responsibility_type,part_use_type,is_return,producer_code);
			}
		}
		var acc=json.acc;
		if(acc!=null&&acc.length>0){
			var div=$('#new_acc_add');
			var str="";
			str+='<table id="accessories" border="1" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;">';
			str+='<th colspan="8">';
			str+='	<img class="nav" src="../jsp_new/img/subNav.gif" />辅料项目';
			str+='</th>';
			str+='<tr>';
			str+='	<td nowrap="true" width="20%" >辅料代码</td>';
			str+='	<td nowrap="true" width="20%" >辅料名称</td>';
			str+='	<td nowrap="true" width="20%" >辅料费用</td>';
			str+='</tr>';
			str+='</table>';
			str+='<br>';
			div.append(str);
			for(var i=0;i<acc.length;i++){
				var workhour_code=acc[i].ACC_CODE;
				var workhour_name=acc[i].ACC_NAME;
				var price=acc[i].ACC_PRICE;
				setAcc(workhour_code,workhour_name,price);
			}
		}
		function setAcc(workhour_code,workhour_name,price){
			var tab=$("#accessories");
			var str='<tr><td nowrap="true" width="20%" >';
			str+='<input  name="workHourCode" type="hidden" class="middle_txt" value="'+workhour_code+'"/><span>'+workhour_code+'</span>';
			str+='</td><td nowrap="true" width="20%" >';
			str+='<input  name="workhour_name" type="hidden" class="middle_txt" value="'+workhour_name+'"/><span>'+workhour_name+'</span>';
			str+='</td>';
			str+='<td nowrap="true" width="20%" >';
			str+='<input  name="accessoriesPrice" type="hidden" class="middle_txt" value="'+price+'"/><span>'+price+'</span>';
			str+='</td>';
			str+='</tr>';
			tab.append(str);
		}
		var com=json.com;
		if(com!=null){
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
			str+='<input name="apply_amount" id="apply_amount" type="hidden"  value="'+com.COM_PRICE+'" size="25" /><span>'+com.COM_PRICE+'</span>';
			str+='</td>';
			str+='	<td nowrap="true" width="20%" >';
			str+='<input name="pass_amount" id="pass_amount" type="hidden"    size="25" value="'+com.COM_PRICE+'"/><span>'+com.COM_PRICE+'</span>';
			str+='</td>';
			str+='	<td nowrap="true" width="40%" >';
			str+='<input name="remark"id="remark"  type="hidden"  maxlength="100" size="55" value="'+com.REMARK+'"/><span>'+com.REMARK+'</span>';
			str+='</tr>';
			str+='</table>';
			str+='<br>';
			div.append(str);
		}
		
	}
	function setMainPartCode(part_id,old_part_code,part_code,part_name,part_cost_price,part_quantity,part_cost_price,part_cost_amount,responsibility_type,part_use_type,is_return,producer_code){
		var str='';
		str+='<tr>';
		str+='<td nowrap="true" width="10%" >';
		str+='配件>><input name="part_id" type="hidden" class="middle_txt" value="'+part_id+'"/>';
		str+='</td>';
		str+='<td nowrap="true" width="10%" >';
		str+='<input name="part_code" type="hidden" size="10" value="'+part_code+'"/><span>'+part_code+'</span>';
		str+='</td>';
		str+='<td nowrap="true" width="10%" >';
		str+='<input name="part_name" type="hidden" size="10" value="'+part_name+'"/><span>'+part_name+'</span>';
		str+='</td>';
		str+='<td nowrap="true" width="10%" >';
		str+='<input name="old_part_code" type="hidden" onclick="choose_old_part_code(this);" size="10" value="'+old_part_code+'"/><span>'+old_part_code+'</span>';
		str+='</td>';
		str+='<td nowrap="true" width="10%" >';
		str+='<input name="part_cost_price" type="hidden" size="10" value="'+part_cost_price+'"/><span>'+part_cost_price+'</span>';
		str+='</td>';
		str+='<td nowrap="true" width="10%" >';
		str+='<input name="part_quantity" type="hidden" size="10" value="'+part_quantity+'"/><span>'+part_quantity+'</span>';
		str+='</td>';
		str+='<td nowrap="true" width="10%" >';
		str+='<input name="part_cost_amount" type="hidden"size="10"  value="'+part_cost_amount+'"/><span>'+part_cost_amount+'</span>';
		str+='</td>';
		str+='<td nowrap="true" width="10%" >';
		if("94001001"==responsibility_type){
			str+='<input name="responsibility_type" type="hidden"size="10"  value="'+responsibility_type+'"/><span>主因件</span>';
		}
		if("94001002"==responsibility_type){
			str+='<input name="responsibility_type" type="hidden"size="10"  value="'+responsibility_type+'"/><span>次因件</span>';
		}
		str+='</td>';
		str+='<td nowrap="true" width="10%" >';
		if("1"==part_use_type){
			str+='<input name="part_use_type" type="hidden"size="10"  value="'+part_use_type+'"/><span>更换</span>';
		}
		if("0"==part_use_type){
			str+='<input name="part_use_type" type="hidden"size="10"  value="'+part_use_type+'"/><span>维修</span>';
		}
		str+='</td>';
		str+='<td nowrap="true" width="10%" >';
		str+='<input name="producer_code" type="hidden" size="10" value="'+producer_code+'"/><span>'+producer_code+'</span>';
		str+='</td>';
		str+='<td nowrap="true" width="10%" >';
		if("95361001"==is_return){
			str+='<input name="is_return" type="hidden"size="10"  value="'+is_return+'"/><span>回运</span>';
		}
		if("95361002"==is_return){
			str+='<input name="is_return" type="hidden"size="10"  value="'+is_return+'"/><span>不回运</span>';
		}
		str+='</td>';
		str+='</tr>';
		$("#tab_part").append(str);
	}
	function setLabourCode(labour_code,cn_des,labour_quotiety,labour_price,labour_amount){
		var str='';
		str+='<tr>';
		str+='<td nowrap="true" width="10%" >工时>></td>';
		str+='<td nowrap="true" width="20%" >';
		str+='<input name="labour_code" type="hidden" size="25" value="'+labour_code+'"/><span>'+labour_code+'</span>';
		str+='</td>';
		str+='<td nowrap="true" width="20%" >';
		str+='<input name="labour_name" type="hidden" size="25" value="'+cn_des+'"/><span>'+cn_des+'</span>';
		str+='</td>';
		str+='<td nowrap="true" width="20%" >';
		str+='<input name="labour_quotiety" type="hidden" size="25" value="'+labour_quotiety+'"/><span>'+labour_quotiety+'</span>';
		str+='</td>';
		str+='<td nowrap="true" width="20%" >';
		str+='<input name="labour_price" type="hidden" size="25" value="'+labour_price+'"/><span>'+labour_price+'</span>';
		str+='</td>';
		str+='<td nowrap="true" width="20%" >';
		str+='<input name="labour_amount" type="hidden" size="25" value="'+labour_amount+'"/><span>'+labour_amount+'</span>';
		str+='</td>';
		str+='</tr>';
		$("#tab_labour").append(str);
	}
	function sureInsert(identify){
	var subjectName = $("#subjectName").val();
	   if(""==subjectName.trim()){
	       MyAlert("服务活动主题必须选择！");
	       return;
	    }
	   var  activityName =  $("#activityName").val().trim();
	    if(""==activityName){
	       MyAlert("活动名称必须填写！");
	       return;
	    }
	    var t3 =  $("#t3").val().trim();
	    var t4 =  $("#t4").val().trim();
	    var t5 =  $("#t5").val().trim();
	    var t6 =  $("#t6").val().trim();
	    var i =0;
	    if(""==t3){
	      MyAlert("实际活动日期必须选择！");
	      return;
	    }
	    if(""==t4){
	      MyAlert("实际活动日期必须选择！");
	      return;
	    }
	    if(""==t5){
	      MyAlert("信息录入日期必须选择！");
	      return;
	    }
	    if(""==t6){
	      MyAlert("信息录入日期必须选择！");
	      return;
	    }
	    var  tow_type_activity =  $("#tow_type_activity").val().trim();
	    if(""==tow_type_activity){
	       MyAlert("类型二级必须选择！");
	       return;
	    }
	    var  beforeVehicle =  $("#beforeVehicle").attr("checked");
	    var  afterVehicle =  $("#afterVehicle").attr("checked");
	    if(!beforeVehicle && !afterVehicle){
	        MyAlert("必须选择一个服务活动车辆范围！");
	        return;
	    }
	    
		var url="<%=contextPath%>/ActivityAction/activityAddSure.json?identify="+identify;
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
			MyAlert("提示："+str+"成功！");
			var url='<%=contextPath%>/ActivityAction/activityList.do';
			window.location.href=url;
		}else{
			MyAlert("提示："+str+"失败！");
		}
	}
	function wrapOut(){
		$('#new_com_add').empty();
		$('#new_acc_add').empty();
		$("#tab_part tr:gt(1)").remove(); 
		$("#tab_labour tr:gt(0)").remove(); 
		$("#templet_no").val("");
		$("#subjectId").val("");
		$("#subjectName").val("");
		$("#activityType").val("");
		$("#t3").val("");
		$("#t4").val("");
		$("#t5").val("");
		$("#t6").val("");
		$("#trouble_desc").val("");
		$("#trouble_reason").val("");
		$("#solution").val("");
		$("#claimGuide").val("");
	}
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
</script>
</head>

<form method="post" name="fm" id="fm">
<body onLoad="fix(show);">
<div class="navigation"><img class="nav" src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：售后服务管理&gt;服务活动管理&gt;服务活动管理</div>
<input name="type" id="type" value="${type}" type="hidden" />
<input name="activityId" id="activityId" value="${po.ACTIVITY_ID }" type="hidden" />
<input name="templet_id" id="templet_id" value="${po.TEMPLET_ID }" type="hidden" />
<input id="vehicle" value="${po.VEHICLE_AREA }" type="hidden" />

<input type="hidden" class="middle_txt" value="10000"  name="car_max" id="car_max"  />
<table border="0" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;">
	<tr>
		<th colspan="8">
			<img class="nav" src="<%=contextPath%>/img/subNav.gif" />基本信息
		</th>
	</tr>
	<tr>
		<td width="12.5%"></td>
		<td nowrap="true" width="10%" ><span style="COLOR: red">*</span>服务活动主题：</td>
    	<td nowrap="true" width="15%" >
    		<input type="text" readonly="readonly" name="subjectName" id="subjectName" value="${po.SUBJECT_NAME }"  class="middle_txt"/>
           	<input type="button" id="clean" class="normal_btn" value="清除" onclick="wrapOut();"/>
			<input type="hidden" name="subjectId" id="subjectId" value="${po.SUBJECT_ID }"/>
			<input type="hidden" name="activityType" id="activityType" value="${po.ACTIVITY_TYPE }"/>
    	</td>
		<td nowrap="true" width="10%" ><span style="COLOR: red">*</span>活动编号：</td>
    	<td nowrap="true" width="15%" >
			 <input id="activityCode" name="activityCode" readonly="readonly" value="${po.ACTIVITY_CODE }" type="text" class="middle_txt" >
    	</td>
    	<td nowrap="true" width="10%" ><span style="COLOR: red">*</span>活动名称：</td>
    	<td nowrap="true" width="15%" >
		     <input name="activityName" id="activityName" value="${po.ACTIVITY_NAME }" type="text" class="middle_txt"  maxlength="100">
    	</td>
    	<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%"></td>
		<td nowrap="true" width="10%" ><span style="COLOR: red">*</span>实际活动日期：</td>
    	<td nowrap="true" width="15%" >
    	<input class="short_txt" id="t3" value="<fmt:formatDate value="${po.STARTDATE }" pattern="yyyy-MM-dd"/>"onfocus="calendar();"  name="factstartdate" readonly="readonly" />
    	&nbsp;&nbsp;&nbsp;&nbsp;至&nbsp;&nbsp;&nbsp;&nbsp;
    	<input class="short_txt" id="t4" name="factenddate" value="<fmt:formatDate value="${po.ENDDATE }" pattern="yyyy-MM-dd"/>" readonly="readonly" onfocus="calendar();"/>
    	<input type="hidden" class="middle_txt" value="10000"  name="car_max" id="car_max" datatype="0,is_digit,6" />
    	</td>
    	<td nowrap="true" width="10%" ><span style="COLOR: red">*</span>类型二级：</td>
    	<td nowrap="true" width="15%" >
  		   <script type="text/javascript">
  					genSelBoxExp("tow_type_activity",<%=Constant.TYPE_tow_activity%>,"${po.TOW_TYPE_ACTIVITY}",true,"short_sel","","false",'');
  			</script>
  		</td>
		<td nowrap="true" width="10%" ><span style="COLOR: red">*</span>结算指向：</td>
    	<td nowrap="true" width="15%" >
    	<input id="default" value="1" checked="checked" type="radio" name="default" />
		          按产地结算
		</span>
    	</td>
    	<td width="12.5%"></td>
	</tr>
	<tr>
		<td width="12.5%"></td>
		<td nowrap="true" width="10%" ><span style="COLOR: red">*</span>信息录入日期：</td>
    	<td nowrap="true" width="15%" >
	    	<input class="short_txt" id="t5" value="<fmt:formatDate value="${po.STARTDATE }" pattern="yyyy-MM-dd"/>"  name="startDate" readonly="readonly" onfocus="calendar();"/>
	    	&nbsp;&nbsp;&nbsp;&nbsp;至&nbsp;&nbsp;&nbsp;&nbsp;
	    	<input class="short_txt" id="t6" value="<fmt:formatDate value="${po.ENDDATE }" pattern="yyyy-MM-dd"/>"  name="endDate" readonly="readonly" onfocus="calendar();"/>
    	</td>
    	<td nowrap="true" width="10%" ><span style="COLOR: red">*</span>模板编号：</td>
    	<td nowrap="true" width="15%" > 
    			<input name="templet_no" id="templet_no" value="${po.TEMPLET_NO }" readonly="readonly" type="text" class="middle_txt"  maxlength="100">
  		</td>
		<td nowrap="true" width="10%" ><span style="COLOR: red">*</span>服务活动车辆范围：</td>
    	<td nowrap="true" width="15%" >
	    	<input id="beforeVehicle" value="11321001" class="vehicle" type=checkbox name="vehicle" /> 售前车
	    	 <input id="afterVehicle" value="11321002" class="vehicle"  type="checkbox" name="vehicle" />售后车 
    	</td>
    	<td width="12.5%"></td>
	</tr>
	<tr>
	</tr>
	<tr>
		<td width="12.5%" nowrap="true" colspan="2"><span style="COLOR: red">*</span>活动方案说明：</td>
		<td width="87.5%" nowrap="true" colspan="5">
			<textarea name="solution" id="solution" readonly="readonly" cols="110" rows="4" >${po.SOLUTION }</textarea>
		</td>
	</tr>	 
	<tr>
		<td width="12.5%" nowrap="true" colspan="2"><span style="COLOR: red">*</span>活动申请指导：</td>
		<td width="87.5%" nowrap="true" colspan="5">
			<textarea name="claimGuide" id="claimGuide"readonly="readonly" cols="110" rows="4">${po.CLAIM_GUIDE }</textarea>
		</td>
	</tr>
</table>
<table border="1" id="tab_part" cellpadding="1" cellspacing="1" class="table_edit" width="110%" style="text-align: center;">
					<th colspan="12">
					<img class="nav" src="../jsp_new/img/subNav.gif" />作业项目 &nbsp;&nbsp;
					</th>
					<tr>
						<td nowrap="true" width="10%" >维修配件</td>
						<td nowrap="true" width="10%" >新件代码</td>
						<td nowrap="true" width="10%" >新件名称</td>
						<td nowrap="true" width="10%" >旧件代码</td>
						<td nowrap="true" width="10%" >单价</td>
						<td nowrap="true" width="10%" >配件数量</td>
						<td nowrap="true" width="10%" >金额（元）</td>
						<td nowrap="true" width="10%" >责任性质</td>
						<td nowrap="true" width="10%" >维修方式</td>
						<td nowrap="true" width="10%" >供应商代码</td>
						<td nowrap="true" width="10%" >是否回运</td>
					</tr>
					<c:forEach items="${parts }" var="p">
						<tr>
						<td nowrap="true" width="10%" >
						配件>><input name="part_id" type="hidden" class="middle_txt" value="${p.PART_ID }"/>
						</td>
						<td nowrap="true" width="10%" >
						<input name="part_code" readonly="readonly"type="hidden" size="10" value="${p.PART_CODE }"/><span>${p.PART_CODE }</span>
						</td>
						<td nowrap="true" width="10%" >
						<input name="part_name" readonly="readonly" type="hidden"size="10" value="${p.PART_NAME }"/><span>${p.PART_NAME }</span>
						</td>
						<td nowrap="true" width="10%" >
						<input name="old_part_code" readonly="readonly" type="hidden" size="10" value="${p.OLD_PART_CODE }"/><span>${p.OLD_PART_CODE }</span>
						</td>
						<td nowrap="true" width="10%" >
						<input name="part_cost_price" readonly="readonly"type="hidden" size="10" value="${p.PART_COST_PRICE }"/><span>${p.PART_COST_PRICE }</span>
						</td>
						<td nowrap="true" width="10%" >
						<input name="part_quantity" readonly="readonly"type="hidden" size="10" value="${p.PART_QUANTITY }"/><span>${p.PART_QUANTITY }</span>
						</td>
						<td nowrap="true" width="10%" >
						<input name="part_cost_amount" readonly="readonly"type="hidden" size="10"  value="${p.PART_COST_AMOUNT }"/><span>${p.PART_COST_AMOUNT }</span>
						</td>
						<td nowrap="true" width="10%" >
						<input name="responsibility_type" readonly="readonly"type="hidden" size="10"  value="${p.RESPONSIBILITY_TYPE }"/>
						<c:if test="${p.RESPONSIBILITY_TYPE==94001001}">
							<span>主因件</span>
						</c:if>
						<c:if test="${p.RESPONSIBILITY_TYPE==94001002}">
							<span>次因件</span>
						</c:if>
						</td>
						<td nowrap="true" width="10%" >
						<input name="part_use_type" readonly="readonly"type="hidden" size="10"  value="${p.PART_USE_TYPE }"/>
						<c:if test="${p.PART_USE_TYPE==1}">
							<span>更换</span>
						</c:if>
						<c:if test="${p.PART_USE_TYPE==0}">
							<span>维修</span>
						</c:if>
						</td>
						<td nowrap="true" width="10%" >
						<input name="producer_code" type="hidden" readonly="readonly" size="10" value="${p.PRODUCER_CODE }"/><span>${p.PRODUCER_CODE }</span>
						</td>
						<td nowrap="true" width="10%" >
						<input name="is_return" readonly="readonly"type="hidden" size="10"  value="${p.IS_RETURN }"/>
						<c:if test="${p.IS_RETURN==95361001}">
							<span>回运</span>
						</c:if>
						<c:if test="${p.IS_RETURN==95361002}">
							<span>不回运</span>
						</c:if>
						</td>
						</tr>
					</c:forEach>
	    		</table>
    			<table border="1" id="tab_labour" cellpadding="1" cellspacing="1" class="table_edit" width="110%" style="text-align: center;">
					<tr>
					<td nowrap="true" width="10%" >维修工时</td>
					<td nowrap="true" width="20%" >工时代码</td>
					<td nowrap="true" width="20%" >工时名称</td>
					<td nowrap="true" width="20%" >工时定额</td>
					<td nowrap="true" width="20%" >工时单价</td>
					<td nowrap="true" width="20%" >工时金额（元）</td>
					</tr>
	    			 <c:forEach items="${labours }" var="l">
			          <tr>
						<td nowrap="true" width="10%" >工时>></td>
						<td nowrap="true" width="20%" >
						<input name="labour_code" readonly="readonly" size="25" type="hidden" value="${l.LABOUR_CODE }"/><span>${l.LABOUR_CODE }</span>
						</td>
						<td nowrap="true" width="20%" >
						<input name="labour_name" readonly="readonly" size="25" type="hidden" value="${l.LABOUR_NAME }"/><span>${l.LABOUR_NAME }</span>
						</td>
						<td nowrap="true" width="20%" >
						<input name="labour_quotiety" readonly="readonly" size="25" type="hidden" value="${l.LABOUR_QUOTIETY  }"/><span>${l.LABOUR_QUOTIETY }</span>
						</td>
						<td nowrap="true" width="20%" >
						<input name="labour_price" readonly="readonly" size="25" type="hidden" value="${l.LABOUR_PRICE }"/><span>${l.LABOUR_PRICE  }</span>
						</td>
						<td nowrap="true" width="20%" >
						<input name="labour_amount" readonly="readonly" size="25" type="hidden" value="${l.LABOUR_AMOUNT }"/><span>${l.LABOUR_AMOUNT }</span>
						</td>
						</tr>
	    			 </c:forEach>
	    		</table>
</div>
 <div id="new_acc_add"  style="text-align: center; width: 100%;">
 			<c:if test="${acc!=null}">
 			<table id="accessories" border="1" cellpadding="1" cellspacing="1" class="table_edit" width="100%" style="text-align: center;">
					<th colspan="8">
						<img class="nav" src="../jsp_new/img/subNav.gif" />辅料费&nbsp;&nbsp;
					</th>
					<tr>
						<td nowrap="true" width="20%" >辅料代码</td>
						<td nowrap="true" width="20%" >辅料费用</td>
						<td nowrap="true" width="20%" >辅料名称</td>
					</tr>
    			 <c:forEach items="${acc }" var="a">
    				<tr><td nowrap="true" width="20%" >
						<input readonly="readonly" name="workHourCode" type="hidden" class="middle_txt" value="${a.ACC_CODE  }"/><span>${a.ACC_CODE  }</span>
						</td>
						<td nowrap="true" width="20%" >
						<input readonly="readonly" name="accessoriesPrice" type="hidden" class="middle_txt" value="${a.ACC_PRICE  }"/><span>${a.ACC_PRICE  }</span>
						</td>
						<td nowrap="true" width="20%" >
						<input readonly="readonly" name="workhour_name" type="hidden" size="35" value="${a.ACC_NAME  }"/><span>${a.ACC_NAME  }</span>
						</td>
						</tr>
    			 </c:forEach>
    			 </table>
    		</c:if>
</div>
<div id="new_com_add"  style="text-align: center; width: 100%;">
<c:if test="${com!=null}">
	<table id="com" border="1" cellpadding="1" cellspacing="1" class="table_edit" width="80%" style="text-align: center;">
		<th colspan="4">
			<img class="nav" src="../jsp_new/img/subNav.gif" />补偿费&nbsp;&nbsp;
		</th>
		<tr>
			<td nowrap="true" width="20%" >补偿费申请金额</td>
			<td nowrap="true" width="20%" >审批金额</td>
			<td nowrap="true" width="20%" >备注</td>
		</tr>
		<tr>
			<td nowrap="true" width="20%" >
			<input name="apply_amount" id="apply_amount" type="hidden" size="25" onblur="comCheckPrice(this);" value="${com.COM_PRICE  }"/><span>${com.COM_PRICE  }</span>
			</td>
				<td nowrap="true" width="20%" >
			<input name="pass_amount" id="pass_amount" type="hidden" readonly="readonly"  size="25" value="${com.COM_PRICE }"/><span>${com.COM_PRICE  }</span>
			</td>
				<td nowrap="true" width="20%" >
			<input name="remark" id="remark" type="hidden" maxlength="100"  size="35" value="${com.REMARK }"/><span>${com.REMARK  }</span>
			</td>
		</tr>
	</table>
	<br>
	</c:if>
</div>
<table class="table_edit"  id="REMARKS_ID" > 
        	 <th colspan="8"  ><img src="../jsp_new/img/subNav.gif" alt="" class="nav" />
					申请内容
				</th>
          	<tr>
					<td nowrap="true" width="10%">
						<span style="COLOR: red">*</span>故障描述：
					</td>
					<td class="tbwhite" colspan="3">
						<textarea name='trouble_desc' maxlength="100" readonly="readonly"	id='trouble_desc' rows='3' cols='32'>${t.TROUBLE_DESC }</textarea>
					</td>
					<td nowrap="true" width="10%">
						<span style="COLOR: red">*</span>故障原因：
					</td>
					<td colspan="3" class="tbwhite">
						<textarea name='trouble_reason' id='trouble_reason' readonly="readonly" maxlength="100"  rows='3' cols='32'>${t.TROUBLE_REASON}</textarea>
					</td>
				</tr>
          </table>
</div>
<br/>
<table width=100% border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td height="12" align=left width="33%">&nbsp;</td>
            	<td height="12" align=center width="33%" nowrap="true">
               	&nbsp;&nbsp;
               	<input type="button" class="normal_btn" id="sure" onclick="sureInsert(0);"  style="width=8%" value="保存" />
               	&nbsp;&nbsp;
               	<input type="button" class="normal_btn" id="report" onclick="sureInsert(1);"  style="width=8%" value="提交" />
               	&nbsp;&nbsp;
				<input type="button" id="back" onClick="history.back();" class="normal_btn"  style="width=8%" value="返回"/>
   			</td>
           	<td height="12" align=center width="33%">
     			</td>
		</tr>
</table>
<br>
<table id="showBtn" style="display: none" width=100% border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td colspan="12" align="center">
			<input type="button" name="type_add" id="" class="normal_btn"  onclick="openModel();" value="车型" /> 
			<input type="button" id="milage_add" name="milage_add" class="normal_btn"   onclick="openMilage();" value="里程限制" /> 
			<input type="button" id="age_add" name="age_add" class="normal_btn"   onclick="openVehicleAge();" value="车龄" /> 
			<input type="button" id="veh_add" name="veh_add" class="normal_btn"  onclick="openCharactor();" value="车辆性质" />
			<input type="button" id="act_add" name="act_add" class="long_btn"  onclick="openProduceBase();" value="生产基地" />
			<input type="button" id="vin_insert" name="vin_insert" class="long_btn" onclick="openVIN();" value="活动VIN导入" />
			<input type="button" id="vin_load" name="vin_load" class="long_btn"   onclick="downloadTemplate();" value="下载VIN模版" />
			<input type="button" id="back1" onClick="history.back();" class="normal_btn"  style="width=8%" value="返回"/>
		</td>
	</tr>
</table>
<br>
<!-- 控制显示信息  -->
<table class="table_edit" id="showBtn1" style="display: none" width=100% border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td width="13%">
			<input type="checkbox" value="true" id="ckbAll" name="ckbAll" onclick="showClaimDetail('all',this);" /> 全部显示</td>
		<td width="13%">
			<input type="checkbox" name="bt_back" onclick="showClaimDetail('authTable',this);" /> 车型列表
		</td>
		<td width="15%">
			<input type="checkbox" name="bt_back" onclick="showClaimDetail('ageTable',this);" /> 车龄定义列表
		</td>
		<td width="15%">
			<input type="checkbox" name="bt_back" onclick="showClaimDetail('charactorTable',this);" /> 车辆性质 
		</td>
		<td width="15%" nowrap>
			<input type="checkbox" name="bt_back" onclick="showClaimDetail('milageTable',this);" /> 里程限制
		</td>
		<td width="15%" nowrap>
			<input type="checkbox" name="bt_back" onclick="showClaimDetail('charactoryieldly',this);" /> 产地
		</td>
		
	</tr>
</table>
<br />
<table id="authTable" width="95%" border="0" class="table_list" style="border-bottom: 1px solid #DAE0EE; display: none">
  <tr>
    <th colspan="4" align="left"> <img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 车型列表 </th>
  </tr>
  <tr>
    <th><b>车型大类</b></th>
    <th><b>车系名称</b></th>
    <th><b>车型名称</b></th>
    <th><b>车型代码</b></th>
  </tr>
  <c:forEach var="ActivityVhclMaterialGroupList" items="${ActivityVhclMaterialGroupList}">
    <tr class="table_list_row1">
      <td>${ActivityVhclMaterialGroupList.groupCode}</td>
      <td><c:out value="${ActivityVhclMaterialGroupList.groupName}"></c:out></td>
      <td><c:out value="${ActivityVhclMaterialGroupList.parentGroupName}"></c:out></td>
      <td>${ActivityVhclMaterialGroupList.parentGroupCode}</td>
    </tr>
  </c:forEach>
</table>
<br />
<table id="ageTable" width="95%" border="0" class="table_list" style="border-bottom: 1px solid #DAE0EE; display: none">
  <tr>
    <th colspan="6" align="left"> <img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 车龄定义列表 </th>
  </tr>
  <tr>
    <th>日期类型</th>
    <th><b>起始日期</b></th>
    <th><b>截止日期</b></th>
  </tr>
  <c:forEach var="ActivitygetActivityAgeList"
		items="${ActivitygetActivityAgeList}">
    <tr class="table_list_row1">
      <td><script type="text/javascript">
					document.write(getItemValue(${ActivitygetActivityAgeList.dateType}));
				</script></td>
      <td><c:out value="${ActivitygetActivityAgeList.saleDateStart}"></c:out></td>
      <td><c:out value="${ActivitygetActivityAgeList.saleDateEnd}"></c:out></td>
    </tr>
  </c:forEach>
</table>
<br />
<table id="charactorTable" width=95% border=0 class="table_list" style="border-bottom: 1px solid #DAE0EE; display: none">
  <tr>
    <th colspan="3" align="left"> <img class="nav" src="<%=contextPath%>/img/subNav.gif"> 车辆性质 </th>
  </tr>
  <c:forEach var="ActivityCharactorList" items="${ActivityCharactorList}">
    <tr class="table_list_row1">
      <td><c:out value="${ActivityCharactorList.codeDesc}"></c:out></td>
    </tr>
  </c:forEach>
</table>
<table id="charactoryieldly" width=95% border=0 class="table_list" style="border-bottom: 1px solid #DAE0EE; display: none">
  <tr>
    <th colspan="3" align="left"> <img class="nav" src="<%=contextPath%>/img/subNav.gif"> 产地 </th>
  </tr>
  <c:forEach var="ttAsActivityYieldlyPO" items="${ttAsActivityYieldlyPO}">
    <tr class="table_list_row1">
      <td> ${ttAsActivityYieldlyPO.areaName} </td>
    </tr>
  </c:forEach>
</table>
<table id="milageTable" width="95%" border="0" class="table_list" style="border-bottom: 1px solid #DAE0EE; display: none">
  <tr>
    <th colspan="3" align="left" nowrap> <img class="nav" src="<%=contextPath%>/img/subNav.gif" />里程限制 </th>
  </tr>
  <tr>
    <th><b>起始里程</b></th>
    <th><b>结束里程</b></th>
  </tr>
  <c:forEach var="milageList" items="${ActivityMileageList}">
    <tr class="table_list_row1">
      <td><c:out value="${milageList.MILAGE_START}"></c:out></td>
      <td><c:out value="${milageList.MILAGE_END}"></c:out></td>
    </tr>
  </c:forEach>
</table>
<table width="95%" border="0" style="display: none" align="center" cellpadding="4" id="viewInfo" cellSpacing="1">
  <tr align="center">
    <td><input type="button" name="vehicleInfo" value="车辆信息" class="normal_btn" onclick="openCharactorView();" id="infoview1"/>
      <input type="button" name="bt_back" class="normal_btn" id="infoview2" onclick="javascript:history.go(-1)" value="返回"/></td>
  </tr>
</table>
</form>
<script type="text/javascript">
var activityId=$("#activityId").val();
var activityCode=$("#activityCode").val();

//服务活动-车辆信息
function openCharactorView(){
	OpenHtmlWindow('<%=contextPath%>/claim/serviceActivity/ServiceActivityManage/getActivityVehicleListInfoInit.do?activityId='+activityId,800,500);
}

//服务活动-车型列表开始
function openModel(){
	OpenHtmlWindow('<%=contextPath%>/claim/serviceActivity/ServiceActivityManageModel/serviceActivityManageModelQuery.do?activityId='+activityId,800,500);
}
//服务互动 里程限制
function openMilage(){
	OpenHtmlWindow('<%=contextPath%>/claim/serviceActivity/ServiceActivityManage/getActivityMilage.do?activeId='+activityId,400,200);
}
//服务活动-车龄定义列表开始
function openVehicleAge(){
    var beforeVehicle ="";
    if(document.fm.beforeVehicle.checked==true){
    	beforeVehicle =document.fm.beforeVehicle.value;
    }
	OpenHtmlWindow('<%=contextPath%>/claim/serviceActivity/ServiceActivityManageVehicleAge/serviceActivityManageVehicleAgeQuery.do?activityId='+activityId+'&beforeVehicle='+beforeVehicle,500,300);
}
//服务活动-车辆性质列表开始
function openCharactor(){
    var beforeVehicle =document.fm.beforeVehicle;
    if(beforeVehicle.checked==true){
			MyAlert("选择售前车不能维护车辆性质！");
			return false;
		}	
	OpenHtmlWindow('<%=contextPath%>/claim/serviceActivity/ServiceActivityManageCharactor/serviceActivityManageCharactorQuery.do?activityId='+activityId,800,500);
}
//服务活动-生产基地列表开始
function openProduceBase(){
	OpenHtmlWindow('<%=contextPath%>/claim/serviceActivity/ServiceActivityManageProduceBase/serviceActivityManageProduceBaseQuery.do?activityId='+activityId,800,500);
}
//服务活动-VIN清单导入开始
function openVIN(){
	OpenHtmlWindow('<%=contextPath%>/claim/serviceActivity/ServiceActivityVinImport/serviceActivityVinImportInit.do?activityId='+activityId+"&flag=modify",800,500);
}
//服务活动-VIN清单导入开始
function openVIN1(){
	window.location='<%=contextPath%>/ActivityAction/serviceActivityVinImportInit.do?activityId='+activityId+'&flag=modify'+'&activitycode='+activityCode;
	//OpenHtmlWindow('<%=contextPath%>/ActivityAction/serviceActivityVinImportInit.do?activityId='+activityId+"&flag=modify",800,500);
}
function downloadTemplate(){
	fm.action = '<%=contextPath%>/claim/basicData/ClaimBasicLabourPrice/downloadVin.do';
    fm.submit();
 }
//控制显示（工时、配件、其他项目和授权信息）
function showClaimDetail(tableId,checkBoxObj){

	if('all'==tableId){
		var authT = document.getElementById('authTable');
		var ageT = document.getElementById('ageTable');
		var charactorT = document.getElementById('charactorTable');
		var milage = document.getElementById('milageTable');
		var yieldly =  document.getElementById('charactoryieldly'); 
		if(checkBoxObj.checked){
			authT.style.display='';
			ageT.style.display='';
			milage.style.display='';
			charactorT.style.display='';
			yieldly.style.display = '';
		}else{
			authT.style.display='none';
			ageT.style.display='none';
			charactorT.style.display='none';
			milage.style.display='none';
			yieldly.style.display = 'none';
		}
	}else {
		var temp = document.getElementById(tableId);
		if(checkBoxObj.checked){
			temp.style.display = '';
		}else{
			temp.style.display = 'none';
		}
	}
	changeCheckBox(tableId,checkBoxObj);
}
//控制CheckBox（工时、配件、其他项目和授权信息）状态
function changeCheckBox(tableId,checkBoxObj){
	if('all'==tableId){
		var cbArray = document.getElementsByName('bt_back');
		var status = checkBoxObj.checked;
		for(var i=0;i<cbArray.length;i++){
			cbArray[i].checked = status;
		}
	}else{
		var allCB = document.getElementById('ckbAll');
		if(checkBoxObj.checked)
			allCB.checked = false;
		else
			allCB.checked = false;
	}
}
</script>
</body>
</html>