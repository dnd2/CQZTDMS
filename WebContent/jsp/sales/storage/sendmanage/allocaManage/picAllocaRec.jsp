<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>

<%@ page import="com.infodms.dms.common.PicConstantURL"%>

<html>
<head>
<%
	String contextPath = request.getContextPath();
	Map<Object, List<Map<String, Object>>> mapc = (Map<Object, List<Map<String, Object>>>) request.getAttribute("rightMap");
	List<Map<String, Object>> list = (List<Map<String, Object>>) request.getAttribute("list");
	Map<String, Object> valueMap = (Map<String, Object>) request.getAttribute("valueMap");
	Map<String, Object> vehicleAllMap = (Map<String, Object>) request.getAttribute("vehicleAllMap");
	
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="<%=request.getContextPath()%>/images/default/storage/images/css.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/style/easyuicss/easyui.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/themes/icon.css"/>

<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=request.getContextPath() %>/js/web/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/web/jquery.easyui.min.js"></script>
<title>配车图形 </title>
</head>
<!--1. 在整个页面创建布局面板-->
 <body class="easyui-layout">
 <form method="post" id="fm" name="fm">
 <!--1.1 egion="north"，指明高度，可以自适应-->
 <div region="north" style="height:50px;">
 <span>产地：</span>
 <select name="yieldly" id="yieldly" class="u-select" onchange="seachPic();">
				<c:if test="${list_yieldly!=null}">
					<c:forEach items="${list_yieldly}" var="list_yieldly">
						<option value="${list_yieldly.AREA_ID}">${list_yieldly.AREA_NAME}</option>
					</c:forEach>
				</c:if>
</select>
<input type="button" name="button1" class="normal_btn" onclick="save();" value="确定配车" /> 
<br/>
 <table>
 <tr><td class="checkTitleEnable"></td><td>可配车</td>
 <td class="checkTitleDisable"></td><td>不可配车</td>
 <td class="checkTitleTrue"></td><td>已选车</td>
 </tr>
 </table>
 </div> 
 <!--1.2 region="west",必须指明宽度-->
 <div region="west" id="westparl" title="信息统计" style="width:220px" collapsible="true">
<table class="admintable" id="leftTable">
 	<tr> 
 		<th class="adminth">区号</th>		
		<th class="adminth">道数</th>
		<th class="adminth">车数</th>
		<th class="adminth">可用库位数</th>
	</tr>
		<c:if test="${!empty leftList}">
			<c:forEach items="${leftList}" var="leftList">
				<tr onmouseover="rowOver(this)" onmouseout="rowOut(this)" onclick="selectRow(${leftList.AREA_ID })" ondblclick="rightReLoad(this,${leftList.AREA_ID })" id="${leftList.AREA_ID }" style="cursor: pointer"> 
				  <td height="35px" class="admincls0">${leftList.AREA_NAME }</td>				  
				  <td  class="admincls0">${leftList.ROADCOUNT }</td>
				  <td  class="admincls0">${leftList.VECOUNT }</td>
				  <td  class="admincls0">${leftList.SITCOUNT }</td>
				</tr>
			</c:forEach>
		</c:if>
</table>
</div>
 <!--1.3region="center",这里的宽度和高度都是由周边决定，不用设置-->
 <div region="center"> 
 
 <!--2. 对<div>标签引用'easyui-layout'类,fit="true"自动适应父窗口,这里我们指定了宽度和高度-->
 <div id="rightList"  class="easyui-layout" style="overflow:auto;" fit="true"> 
 <table  border="0" cellspacing="0" cellpadding="0">
<tr>
<td>
<table border="0" cellspacing="0" cellpadding="0">
<!-- 右边图形表头Start -->
<tr>
<%if (list != null && list.size()>0) {
		for (int boHead = 0; boHead < list.size(); boHead++) {
			Map<String, Object> boHeadMap=list.get(boHead);
%>
	<td class="btz"><%=boHead+1 %>	
	<%
	if(Integer.parseInt(boHeadMap.get("IN_STATUS").toString())==Constant.AUTO_IN_STATUS_02){
	%>
	入*锁
	<%	
	}
	if(Integer.parseInt(boHeadMap.get("OUT_STATUS").toString())==Constant.AUTO_OUT_STATUS_02){
	%>
	出*锁
	<%	
	}
	%>
	<input type="hidden" id="in_status" name="in_status" value="<%=boHeadMap.get("IN_STATUS") %>" />
	<input type="hidden" id="out_status" name="out_status" value="<%=boHeadMap.get("OUT_STATUS") %>" />
	<input type="hidden" id="road_value_hr" name="road_value_hr" value="<%=boHeadMap.get("ROAD_ID") %>" />
	</td>
<%
	}
}
if(list.size()<25){
	for(int i=0;i<25-list.size();i++){
		%>
		  <td class="roadnull"><%=list.size()+i+1 %></td>
		<%
	}
}
%>
</tr>
<!-- 右边图形表头End -->
<!-- 右边图形主体内容显示Start -->
<tr> 
<%
	if (list != null  && list.size()>0) {
		for (int boBody = 0; boBody < list.size(); boBody++) {
			Map<String, Object> boMap = (Map<String, Object>) list.get(boBody);
%>
<td class="btzj"><table width="100%" border="1" cellpadding="0" cellspacing="0" style="top: 0px">
<%
			if (mapc!=null && mapc.size()>0) {
				List<Map<String, Object>> sitList = (List<Map<String, Object>>) mapc.get(boMap.get("ROAD_ID"));
				if (sitList != null) {
					for (int ve = 0; ve < sitList.size(); ve++) {
						Map<String, Object> sitMap = (Map<String, Object>) sitList.get(ve);
						if(sitMap!=null && sitMap.size()>0){
%> 						
				          <tr>
				            <td  height="45px" class="sithave">
				            <%
				            if(!sitMap.get("VEHICLE_ID").toString().equals("-1") && sitMap.get("VEHICLE_ID")!=null){//判断该库位是否有车
				            	if(sitMap.get("MATERIAL_ID") == null) {continue;}
				            	if(sitMap.get("MATERIAL_ID").equals(vehicleAllMap.get("MATERIAL_ID")) //同物料
				            	   && Integer.parseInt(boMap.get("IN_STATUS").toString())==Constant.AUTO_IN_STATUS_01//入库解锁状态
				            	   && Integer.parseInt(boMap.get("OUT_STATUS").toString())==Constant.AUTO_OUT_STATUS_01//出库解锁状态
				            		&& Integer.parseInt(sitMap.get("CHECK_VEHICLE").toString())!=1){//配车状态为非配车的
				            %>
						            	<div onclick="chooseVehicle(this)" class="checkEnable" style="background-image:url(<%=sitMap.get("RE_URL")==null?contextPath+PicConstantURL.NOTPIC:contextPath+sitMap.get("RE_URL")  %>);background-repeat:no-repeat;background-position: center,center;">
										 <input type="hidden" id="vId_<%=sitMap.get("VEHICLE_ID") %>" value="<%=sitMap.get("VEHICLE_ID") %>" /><!-- 车辆ID -->
										 <input type="hidden" id="vehicle_id" name="vehicle_id" value="<%=sitMap.get("VEHICLE_ID") %>" /><!-- 车辆ID -->
										  <input type="hidden" id="main_vin" name="main_vin" value="<%=sitMap.get("VIN") %>" /><!-- VIN -->
										  <input type="hidden" id="main_engine_no" name="main_engine_no" value="<%=sitMap.get("ENGINE_NO") %>" /><!-- 发动机号 -->
										  <input type="hidden" id="main_org_storage_date" name="main_org_storage_date" value="<%=sitMap.get("ORG_STORAGE_DATE") %>" /><!-- 入库日期 -->
										  <input type="hidden" id="main_area_name" name="main_area_name" value="<%=sitMap.get("AREA_NAME") %>" /><!-- 库区名称 -->
										  <input type="hidden" id="main_road_name" name="main_road_name" value="<%=sitMap.get("ROAD_NAME") %>" /><!-- 库道名称 -->
										  <input type="hidden" id="main_sit_name" name="main_sit_name" value="<%=sitMap.get("SIT_NAME") %>" /><!-- 库位名称 -->	
											<input type="hidden" id="main_check_vehicle" name="main_check_vehicle" value="0" /><!-- 是否已选-->	  									 
						            	 </div>
						      <%					            		
				            	}else{
				             %>
				            	<div class="checkDisable" style="background-image:url(<%=contextPath+sitMap.get("RE_URL")==null?contextPath+PicConstantURL.NOTPIC:contextPath+sitMap.get("RE_URL") %>);background-repeat: no-repeat;background-position: center,center;">
								 <input type="hidden" id="vehicle_id" name="vehicle_id" value="<%=sitMap.get("VEHICLE_ID") %>" /><!-- 车辆ID -->
								   <input type="hidden" id="main_vin" name="main_vin" value="<%=sitMap.get("VIN") %>" /><!-- VIN -->
									<input type="hidden" id="main_engine_no" name="main_engine_no" value="<%=sitMap.get("ENGINE_NO") %>" /><!-- 发动机号 -->
								    <input type="hidden" id="main_org_storage_date" name="main_org_storage_date" value="<%=sitMap.get("ORG_STORAGE_DATE") %>" /><!-- 入库日期 -->
								    <input type="hidden" id="main_area_name" name="main_area_name" value="<%=sitMap.get("AREA_NAME") %>" /><!-- 库区名称 -->
									<input type="hidden" id="main_road_name" name="main_road_name" value="<%=sitMap.get("ROAD_NAME") %>" /><!-- 库道名称 -->
									<input type="hidden" id="main_sit_name" name="main_sit_name" value="<%=sitMap.get("SIT_NAME") %>" /><!-- 库位名称 -->										 
				            	 </div>
				            <%
				            	}
				            }else{
				            %>
				            <div class="sitnull">
								<input type="hidden" id="vehicle_id" name="vehicle_id" value="" /><!-- 车辆ID -->
								<input type="hidden" id="main_vin" name="main_vin" value="" /><!-- VIN -->
								<input type="hidden" id="main_engine_no" name="main_engine_no" value="" /><!-- 发动机号 -->
								<input type="hidden" id="main_org_storage_date" name="main_org_storage_date" value="" /><!-- 入库日期 -->
								<input type="hidden" id="main_area_name" name="main_area_name" value="<%=sitMap.get("AREA_NAME") %>" /><!-- 库区名称 -->
								<input type="hidden" id="main_road_name" name="main_road_name" value="<%=sitMap.get("ROAD_NAME") %>" /><!-- 库道名称 -->
								<input type="hidden" id="main_sit_name" name="main_sit_name" value="<%=sitMap.get("SIT_NAME") %>" /><!-- 库位名称 -->										 
								
							</div>
				            <%
				            }
				            %>
				           </td>
				          </tr>   
						<%
						}
 					}
					int maxRoad=Integer.parseInt(valueMap.get("maxRoad").toString());
					if(maxRoad<12){
						for(int i=0;i<12-sitList.size();i++){
							%>
							 <tr>
				           		 <td class="bodynull">
									<div>&nbsp;</div>
								</td>
				          	</tr>  
							<%
						}
					}else{
						for(int i=0;i<maxRoad-sitList.size();i++){
							%>
							 <tr>
				           		 <td class="bodynull">
									<div>&nbsp;</div>
								</td>
				          	</tr>  
							<%
						}
					}
 				}
 			}
%>
 </table></td>
<%
 		}
		if(list.size()<25){
			for(int i=0;i<25-list.size();i++){
				%>
				<td class="bodynull">
					<div>&nbsp;</div>
				</td>
				<%
			}
		}
		
 	}else{//无库道时候
 		%>
 		<td class="btzj">
				<table width="100%" border="0" cellpadding="0" cellspacing="0">
 		<%
 		for(int i=0;i<12;i++){
			%>
			
					 <tr>
		           		 <td class="bodynull"><div>&nbsp;</div></td>
		          	</tr>
	         
			<%
		}
 		%>
 		 	</table>  
          	</td>
 		<%
 		
 	}

 %>
 </tr>
 <!-- 右边图形主体内容显示end -->
 <!-- 右边图形下表头Start -->
<tr>
<%if (list != null  && list.size()>0) {
		for (int boEnd = 0; boEnd < list.size(); boEnd++) {
			Map<String, Object> boEndMap=list.get(boEnd);
%>
	<td class="btz"><%=boEnd+1 %>
		<%
	if(Integer.parseInt(boEndMap.get("IN_STATUS").toString())==Constant.AUTO_IN_STATUS_02){
	%>
	入*锁
	<%	
	}
	if(Integer.parseInt(boEndMap.get("OUT_STATUS").toString())==Constant.AUTO_OUT_STATUS_02){
	%>
	出*锁
	<%	
	}
	%>
	<input type="hidden" id="in_status" name="in_status" value="<%=boEndMap.get("IN_STATUS") %>" />
	<input type="hidden" id="out_status" name="out_status" value="<%=boEndMap.get("OUT_STATUS") %>" />	
	<input type="hidden" id="road_value_hr" name="road_value_hr" value="<%=boEndMap.get("ROAD_ID") %>" />
	</td>
<%
	}
}
if(list.size()<25){
	for(int i=0;i<25-list.size();i++){
		%>
		  <td class="roadnull"><%=list.size()+i+1 %></td>
		<%
	}
}
%>
</tr>
<!-- 右边图形下表头End -->
 </table>
</td>
</tr>
</table>
<input type="hidden" id="hr_road_value"/><!-- 入库操作 -->
<input type="hidden" id="inStatus"/>
<input type="hidden" id="outStatus"/>
<input type="hidden" id="pr_vehicle_id"/>
<input type="hidden" id="thisNum_a"/><!-- 已配车数 -->
<input type="hidden" id="thisNum_Count"/><!-- 当前组板数 -->
<input type="hidden" id="thisVehicle"/><!-- 本页面已选车辆 -->
<input type="hidden" id="hisVehicle"/><!-- 历史已选车辆 -->


	<!--头部菜单  -->
	<div id="head_mm" class="easyui-menu" style="width:120px;">
		<div id="inlock" onclick="inLock()">入库锁定</div>
		<div id="inrelock" onclick="inReLock()">入库解锁</div>
		<div id="outlock" onclick="outLock()">出库锁定</div>
		<div id="outrelock" onclick="outReLock()">出库解锁</div>
	</div>   
	<!--主菜单右键  -->
  	<div id="mm" class="easyui-menu" style="width:120px;">
		<div id="vehiclemsg" onclick="vehicleMsg()">查看信息</div>
	</div> 
 </div>   
 </div>
</form>
<script type="text/javascript" >
//设置页面的大小
function getWindowSize(){
	var temp= document.getElementById("#westparl").is(":hidden");//是否隐藏 
	var rightList=document.getElementById("rightList");
	var width=document.body.clientWidth-250;
	var height=document.body.clientHeight-70;
	if(temp==true){
		width=document.body.clientWidth-50;
	}
	
	document.getElementById('#rightList').css("width",width);
	document.getElementById('#rightList').css("height",height);
}
$(window).resize(function(){
	getWindowSize();
});
function doInit(){
	getWindowSize();
	document.getElementById("#yieldly").val(${valueMap.pyieldly});
	selectRow(${valueMap.pareaId});
	getParentValue();
	getCheck();
	headmenu();
	bodymenu();
	othermenu();			
}
//获取父页面的一些参数值
function getParentValue(){
	var boDeId=${valueMap.boDeId};
	var thisNum_a="thisNum_"+boDeId;
	var aNum_="aNum_"+boDeId;
	var vehicle_Ids= "VEHICLE_IDS_"+boDeId;
	var vehicleIds=window.opener.document.getElementsByName(vehicle_Ids); 
	var thisNum_Count=window.opener.document.getElementById(thisNum_a).innerHTML; 
	var aNum_Count=window.opener.document.getElementById(aNum_).innerHTML; 
	var vehicleStr="";
	if(vehicleIds.length>0){
		for(var i=0;i<vehicleIds.length;i++){
			vehicleStr+=vehicleIds[i].value+",";
		}
	}
	document.getElementById("#thisNum_a").val(aNum_Count);//已配车数
	document.getElementById("#thisNum_Count").val(thisNum_Count);//当前组板数
	document.getElementById("#hisVehicle").val(vehicleStr);//历史已选车辆
}
//判断是否已选
function getCheck(){
	var vehicleAllIds=window.opener.document.getElementsByName("VEHICLE_IDS");//获取当前组板号下的所有车辆ID(父页面)
	var vehicle_id=document.getElementsByName("vehicle_id");//获取当前组板号下的所有车辆ID(当前页面)
	if(vehicleAllIds!=null && vehicleAllIds.length>0 && vehicle_id!=null && vehicle_id.length>0){
		for(var i=0;i<vehicleAllIds.length;i++){//父页面已有的
			for(var j=0;j<vehicle_id.length;j++){//当前页面所有车辆
				if(vehicleAllIds[i].value==vehicle_id[j].value){//已选中 设置样式位选中状态（即不可用状态）
					var veId="#vId_"+vehicleAllIds[i].value;
					$(veId).parent().removeClass("checkEnable");
					$(veId).parent().addClass("checkTrue");
					break;
				}
			}
		}
	}
	
}
// 头部menu
function headmenu(){
		// 头部右键操作
	   document.getElementById(".btz").bind('contextmenu',function(e){
		e.preventDefault();
		//每次触发=右键的时候获取该库道的入库出库状态
		document.getElementById("#inStatus").val(e.target.children.in_status.defaultValue);//入库状态
		document.getElementById("#outStatus").val(e.target.children.out_status.defaultValue);//出库状态
		document.getElementById("#hr_road_value").val(e.target.children.road_value_hr.defaultValue);//库道值
		
		// 入库
		if(e.target.children.in_status.defaultValue==<%=Constant.AUTO_IN_STATUS_02%>){// 入库锁定
			document.getElementById('#head_mm').menu('disableItem', document.getElementById('#inlock'));
			document.getElementById('#head_mm').menu('enableItem', document.getElementById('#inrelock'));					
		}else{
			
			document.getElementById('#head_mm').menu('enableItem', document.getElementById('#inlock'));
			document.getElementById('#head_mm').menu('disableItem', document.getElementById('#inrelock'));
		}
		// 出库
		if(e.target.children.out_status.defaultValue==<%=Constant.AUTO_OUT_STATUS_02%>){// 出库锁定
			document.getElementById('#head_mm').menu('disableItem', document.getElementById('#outlock'));
			document.getElementById('#head_mm').menu('enableItem', document.getElementById('#outrelock'));	
		}else{
			document.getElementById('#head_mm').menu('enableItem', document.getElementById('#outlock'));
			document.getElementById('#head_mm').menu('disableItem', document.getElementById('#outrelock'));	
		}
		document.getElementById('#head_mm').menu('show', {
			left: e.pageX,
			top: e.pageY
		});
	});
}
	 // 主要按钮
function bodymenu(){
	// 内容右键操作
	document.getElementById(".sithave > div").bind('contextmenu',function(e){
		var pr_vehicle_id=e.target.children.vehicle_id.defaultValue;//车辆ID
		document.getElementById("#pr_vehicle_id").val(pr_vehicle_id);//车辆ID		
		e.preventDefault();
		if(pr_vehicle_id!=""){// 查看信息i
			document.getElementById('#mm').menu('enableItem', document.getElementById('#vehiclemsg'));
		}else{
			document.getElementById('#mm').menu('disableItem', document.getElementById('#vehiclemsg'));	
		}
		document.getElementById('#mm').menu('show', {
			left: e.pageX,
			top: e.pageY
		});
	});
}
		// 其他按钮
function othermenu(){
	// 禁用无库位的右键操作
	document.getElementById(".bodynull").bind('contextmenu',function(e){
		e.preventDefault();
	});
	// 禁用无库道的右键操作
	document.getElementById(".roadnull").bind('contextmenu',function(e){
		e.preventDefault();
	});
}
// 查看信息
function vehicleMsg(){
	var vid=document.getElementById("#pr_vehicle_id").val();
	if(vid!="" && vid!=-1 && vid!=null){
		OpenHtmlWindow("<%=contextPath%>/sales/storage/storagemanage/VehiclePicture/vehicleSeach.do?vehicle_id="+vid,800,280);
	}
	
}
//入库锁定
function inLock(target){	
	var inStatus=document.getElementById('#inStatus').val();	
	var road_value=document.getElementById("#hr_road_value").val();
	var changeStatus=<%=Constant.AUTO_IN_STATUS_02%>;
	var yieldly=document.getElementById('#yieldly').val();	
	var areaId=${valueMap.pareaId};	
	var MATERIAL_ID=${valueMap.materialId};
	var boDeId=${valueMap.boDeId};
	
	if(inStatus!=<%=Constant.AUTO_IN_STATUS_02%>){//jquery bug需过滤下 虽然样式改变了，但是点击该MENU还是能执行操作
		var inLockUrl = "<%=contextPath%>/sales/storage/sendmanage/AllocaManage/inLockStatus.json";
		makeCall(inLockUrl,statusBack,{road_value:road_value,changeStatus:changeStatus,yieldly:yieldly,areaId:areaId,MATERIAL_ID:MATERIAL_ID,boDeId:boDeId});
	}
}
// 出库锁定
function outLock(target){
	var outStatus=document.getElementById('#outStatus').val();	
	var road_value=document.getElementById("#hr_road_value").val();
	var changeStatus=<%=Constant.AUTO_OUT_STATUS_02%>;
	var yieldly=document.getElementById('#yieldly').val();	
	var areaId=${valueMap.pareaId};	
	var MATERIAL_ID=${valueMap.materialId};
	var boDeId=${valueMap.boDeId};
	if(outStatus!=<%=Constant.AUTO_OUT_STATUS_02%>){//jquery bug需过滤下 虽然样式改变了，但是点击该MENU还是能执行操作
		var inLockUrl = "<%=contextPath%>/sales/storage/sendmanage/AllocaManage/outLockStatus.json";
		makeCall(inLockUrl,statusBack,{road_value:road_value,changeStatus:changeStatus,yieldly:yieldly,areaId:areaId,MATERIAL_ID:MATERIAL_ID,boDeId:boDeId});
	}
}
// 入库解锁
function inReLock(target){
	var inStatus=document.getElementById('#inStatus').val();	
	var road_value=document.getElementById("#hr_road_value").val();
	var changeStatus=<%=Constant.AUTO_IN_STATUS_01%>;
	var yieldly=document.getElementById('#yieldly').val();	
	var MATERIAL_ID=${valueMap.materialId};
	var boDeId=${valueMap.boDeId};
	var areaId=${valueMap.pareaId};	
	if(inStatus!=<%=Constant.AUTO_IN_STATUS_01%>){//jquery bug需过滤下 虽然样式改变了，但是点击该MENU还是能执行操作
		var inLockUrl = "<%=contextPath%>/sales/storage/sendmanage/AllocaManage/inLockStatus.json";
		makeCall(inLockUrl,statusBack,{road_value:road_value,changeStatus:changeStatus,yieldly:yieldly,areaId:areaId,MATERIAL_ID:MATERIAL_ID,boDeId:boDeId});
	}
}
// 出库解锁
function outReLock(target){
	var outStatus=document.getElementById('#outStatus').val();	
	var road_value=document.getElementById("#hr_road_value").val();
	var changeStatus=<%=Constant.AUTO_OUT_STATUS_01%>;
	var MATERIAL_ID=${valueMap.materialId};
	var boDeId=${valueMap.boDeId};
	var areaId=${valueMap.pareaId};	
	var yieldly=document.getElementById('#yieldly').val();	
	if(outStatus!=<%=Constant.AUTO_OUT_STATUS_01%>){//jquery bug需过滤下 虽然样式改变了，但是点击该MENU还是能执行操作
		var outLockUrl = "<%=contextPath%>/sales/storage/sendmanage/AllocaManage/outLockStatus.json";
		makeCall(outLockUrl,statusBack,{road_value:road_value,changeStatus:changeStatus,yieldly:yieldly,areaId:areaId,MATERIAL_ID:MATERIAL_ID,boDeId:boDeId});
	}
}
function statusBack(json){
	window.location.href = "<%=request.getContextPath()%>/sales/storage/sendmanage/AllocaManage/picRecInit.do?areaId="+json.valueMap.pareaId+"&yieldly="+json.valueMap.pyieldly+"&MATERIAL_ID="+json.valueMap.materialId+"&boDeId="+json.valueMap.boDeId;
}
function rightReLoad(tar,areaId){
	var yieldly=document.getElementById("#yieldly").val();
	var MATERIAL_ID=${valueMap.materialId};
	var boDeId=${valueMap.boDeId};
	window.location.href = "<%=request.getContextPath()%>/sales/storage/sendmanage/AllocaManage/picRecInit.do?areaId=" +areaId+"&yieldly="+yieldly+"&MATERIAL_ID="+MATERIAL_ID+"&boDeId="+boDeId;			
}
function seachPic(){
	var yieldly=document.getElementById("#yieldly").val();
	var MATERIAL_ID=${valueMap.materialId};
	var boDeId=${valueMap.boDeId};
	window.location.href = "<%=request.getContextPath()%>/sales/storage/sendmanage/AllocaManage/picRecInit.do?&yieldly="+yieldly+"&MATERIAL_ID="+MATERIAL_ID+"&boDeId="+boDeId;
}
var arrayAllObj = new Array();　
var arrayOnlyObj;
var arrAllObj = new Array();　
var arrOnlyObj;　
var parentTagert;//记录历史的对象（即上次选取的对象）
function chooseVehicle(tagert){
	var changeType=window.opener.changeType; //获取选择类型（配车管理为多选:more,配车调整位单选:only）
	var thisNum_a=document.getElementById("#thisNum_a").val();//已配车数
	var thisNum_Count=document.getElementById("#thisNum_Count").val();//当前组板数量
	var thisVehicle=document.getElementById("#thisVehicle").val();//<!-- 本页面已选车辆 -->	
	var pr_vehicle_id= tagert.children.vehicle_id.defaultValue;//车辆ID
	var pr_vin= tagert.children.main_vin.defaultValue;//VIN
	var pr_engine_no= tagert.children.main_engine_no.defaultValue;//发动机号
	var pr_org_storage_date= tagert.children.main_org_storage_date.defaultValue;//入库日期
	var pr_area_name= tagert.children.main_area_name.defaultValue;//库区名称
	var pr_road_name= tagert.children.main_road_name.defaultValue;//库道名称
	var pr_sit_name= tagert.children.main_sit_name.defaultValue;//库位名称 
	var boDeId=${valueMap.boDeId};	//组板明细ID
	var check_vehicle=tagert.children.main_check_vehicle.defaultValue;//是否已选 1已选，0未选
	if(changeType=="more"){
		if(parseInt(thisNum_a)+parseInt(arrayAllObj.length+1)<=parseInt(thisNum_Count)){//组板数<=已配车数+当前选择车数
			if(check_vehicle!=null && check_vehicle!=1 && tagert.className!="checkTrue"){
				arrayOnlyObj = new Array();　
				arrayOnlyObj.push(pr_vehicle_id);
				arrayOnlyObj.push(pr_vin);
				arrayOnlyObj.push(pr_engine_no);
				arrayOnlyObj.push(pr_org_storage_date);
				arrayOnlyObj.push(pr_area_name);
				arrayOnlyObj.push(pr_road_name);
				arrayOnlyObj.push(pr_sit_name);
				arrayAllObj.push(arrayOnlyObj); 
				tagert.className = "checkTrue";
			}else{
				MyAlert("该车辆已选择!");
			}
		}else{
			MyAlert("所选车辆已满!");
		}
	}else if(changeType=="only"){
		if(check_vehicle!=null && check_vehicle!=1 && tagert.className!="checkTrue"){
			if(arrAllObj!=null && arrAllObj.length>0){//首先清掉已有的
				arrAllObj= new Array();　//只取一个值（直接NEW了个参数）
				parentTagert.className = "checkEnable";//记录上次的操作位可用
			}			
			arrOnlyObj = new Array();　
			arrOnlyObj.push(pr_vehicle_id);
			arrOnlyObj.push(pr_vin);
			arrOnlyObj.push(pr_engine_no);
			arrOnlyObj.push(pr_org_storage_date);
			arrOnlyObj.push(pr_area_name);
			arrOnlyObj.push(pr_road_name);
			arrOnlyObj.push(pr_sit_name);
			arrAllObj.push(arrOnlyObj); 
			tagert.className = "checkTrue";
			parentTagert=tagert;
		}
	}else{
		 MyAlert("连接错误!请重新打开该页面！");
		 window.close();
	}
	
}
function save(){
	var changeType=window.opener.changeType; //获取选择类型（配车管理为多选:more,配车调整位单选:only）
	var boDeId=${valueMap.boDeId};	//组板明细ID
	if(changeType=="more"){	
		if(arrayAllObj.length>0){
			MyAlert("选车成功");
			window.opener.addRowVehicle(arrayAllObj,boDeId);
			window.close();
		}else{
			MyAlert("请选择车辆信息");
		}
	}else if(changeType=="only"){
		if(arrAllObj.length>0){
			var delId=window.opener.delID;//具体哪辆配车ID
			if(delId!="undefined"){
				window.opener.changeRowVehicle(arrAllObj,boDeId,delId);
				window.close();
			}else{
				MyAlert("连接错误!请重新打开该页面！");
				window.close();
			}
		}else{
			MyAlert("请选择调整车辆信息");
		}
	}else{
		 MyAlert("连接错误!请重新打开该页面！");
		 window.close();
	}
}
function selectRow(target) { 
	var sTable = document.getElementById("leftTable") 
	for(var i=1;i<sTable.rows.length;i++) // 遍历除第一行外的所有行
	{ 
		if (sTable.rows[i].id != target) // 判断是否当前选定行
		{ 
			sTable.rows[i].bgColor = "#ffffff"; // 设置背景色
			sTable.rows[i].onmouseover = resumeRowOver; // 增加onmouseover
			sTable.rows[i].onmouseout = resumeRowOut;// 增加onmouseout																// 事件
		} 
		else 
		{ 
			sTable.rows[i].bgColor = "#d3d3d3"; 
			sTable.rows[i].onmouseover = ""; // 去除鼠标事件
			sTable.rows[i].onmouseout = ""; // 去除鼠标事件
		} 
	} 
} 
// 移过时tr的背景色
function rowOver(target) { 
	target.bgColor='#e4e4e4'; 
	target.setStyle=""
} 
// 移出时tr的背景色
function rowOut(target) { 
	target.bgColor='#ffffff'; 
} 
// 恢复tr的的onmouseover事件配套调用函数
function resumeRowOver() { 
	rowOver(this); 
} 
// 恢复tr的的onmouseout事件配套调用函数
function resumeRowOut() { 
	rowOut(this); 
}
</script>
 </body>
</html>