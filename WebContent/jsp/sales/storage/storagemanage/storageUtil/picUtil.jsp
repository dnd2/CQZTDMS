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
	
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="<%=request.getContextPath()%>/images/default/storage/images/css.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/style/easyuicss/easyui.css"/>

<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=request.getContextPath() %>/js/web/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/web/jquery.easyui.min.js"></script>
<title>接车图形 </title>
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
 <table  border="0" cellspacing="0"  cellpadding="0" >
<tr>
<td>
<table  border="0" cellspacing="0" cellpadding="0">
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
<td class="btzj"><table  border="1" cellpadding="0" cellspacing="0">
<%
			if (mapc!=null && mapc.size()>0) {
				List<Map<String, Object>> sitList = (List<Map<String, Object>>) mapc.get(boMap.get("ROAD_ID"));
				if (sitList != null) {
					for (int ve = 0; ve < sitList.size(); ve++) {
						Map<String, Object> sitMap = (Map<String, Object>) sitList.get(ve);
						if(sitMap!=null && sitMap.size()>0){
%> 						
				          <tr><td class="sithave">
				            <%
				            if(!sitMap.get("VEHICLE_ID").toString().equals("-1") && sitMap.get("VEHICLE_ID")!=null){
				            %>
				            	<div style="background-image:url(<%=sitMap.get("RE_URL")==null?contextPath+PicConstantURL.NOTPIC:contextPath+sitMap.get("RE_URL")  %>);background-repeat: no-repeat;background-position: center,center;">
				            	 <input type="hidden" id="area_value" name="area_value" value="<%=sitMap.get("AREA_ID") %>" /><!-- 库区值 -->
				            	 <input type="hidden" id="road_value" name="road_value" value="<%=sitMap.get("ROAD_ID") %>" /><!-- 库道值 -->
				            	 <input type="hidden" id="sit_value" name="sit_value" value="<%=sitMap.get("SIT_ID") %>" /><!-- 库位值 -->
				         		 <input type="hidden" id="show_sit_value" name="show_sit_value" value="<%=sitMap.get("SIT_ID") %>" /><!-- 库位值(显示用) -->
				            	 <input type="hidden" id="main_in_status" name="main_in_status" value="<%=sitMap.get("IN_STATUS") %>" />
								 <input type="hidden" id="main_out_status" name="main_out_status" value="<%=sitMap.get("OUT_STATUS") %>" />
								 <input type="hidden" id="vehicle_id" name="vehicle_id" value="<%=sitMap.get("VEHICLE_ID") %>" /><!-- 车辆ID -->
								 <input type="hidden" id="life_cycle" name="life_cycle" value="<%=sitMap.get("LIFE_CYCLE") %>" /><!-- 生命周期 -->
				            	 </div>
				            <%
				            }else{
				            %>
				            <div>
				            	<input type="hidden" id="area_value" name="area_value" value="<%=sitMap.get("AREA_ID") %>" /><!-- 库区值 -->
				            	<input type="hidden" id="road_value" name="road_value" value="<%=sitMap.get("ROAD_ID") %>" /><!-- 库道值 -->
				            	<input type="hidden" id="sit_value" name="sit_value" value="<%=sitMap.get("SIT_ID") %>" /><!-- 库位值 -->
				            	<input type="hidden" id="show_sit_value" name="show_sit_value" value="" /><!-- 库位值(显示用) -->
				            	<input type="hidden" id="main_in_status" name="main_in_status" value="<%=sitMap.get("IN_STATUS") %>" />
								<input type="hidden" id="main_out_status" name="main_out_status" value="<%=sitMap.get("OUT_STATUS") %>" />
								<input type="hidden" id="vehicle_id" name="vehicle_id" value="<%=sitMap.get("VEHICLE_ID") %>" /><!-- 车辆ID -->
								<input type="hidden" id="life_cycle" name="life_cycle" value="" /><!-- 生命周期 -->
							</div>
				            <%
				            }
				            %>
				           </td></tr>   
						<%
						}
 					}
					int maxRoad=Integer.parseInt(valueMap.get("maxRoad").toString());
					if(maxRoad<12){
						for(int i=0;i<12-sitList.size();i++){
							%>
							 <tr><td class="bodynull"><div></div></td></tr>  
							<%
						}
					}else{
						for(int i=0;i<maxRoad-sitList.size();i++){
							%>
							 <tr><td class="bodynull"><div></div></td></tr>  
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
				<td class="bodynull"><div></div></td>
				<%
			}
		}
 	}else{//无库道时候
 		%>
 		<td class="btzj">
			<table border="0" cellpadding="0" cellspacing="0">
 		<%
 		for(int i=0;i<12;i++){
			%>
				<tr><td class="bodynull"><div></div></td></tr>   
			<%
		}
 		%>
 		 </table></td>
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
 </table></td></tr></table>
<input type="hidden" id="hr_road_value"/><!-- 入库操作 -->
<input type="hidden" id="inStatus"/>
<input type="hidden" id="outStatus"/>
<input type="hidden" id="pr_area_value"/>
<input type="hidden" id="pr_road_value"/>
<input type="hidden" id="pr_sit_value"/>
<input type="hidden" id="pr_sit_value_old"/>
<input type="hidden" id="pr_vehicle_id"/>
<input type="hidden" id="pr_life_cycle"/>
	<!--头部菜单  -->
	<div id="head_mm" class="easyui-menu" style="width:120px;">
		<div id="inlock" onclick="inLock()">入库锁定</div>
		<div id="inrelock" onclick="inReLock()">入库解锁</div>
		<div id="outlock" onclick="outLock()">出库锁定</div>
		<div id="outrelock" onclick="outReLock()">出库解锁</div>
	</div>   
	<!--主菜单右键  -->
  	<div id="mm" class="easyui-menu" style="width:120px;">
		<div id="adjustVehicle" onclick="adjustVehicle()">调整位置</div>
		<div id="but_retreat" onclick="retreatVehicleInit()">回退生产线</div>
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
	headmenu();
	bodymenu();
	othermenu();			
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
		var in_status=e.target.children.main_in_status.defaultValue;
		var out_status=e.target.children.main_out_status.defaultValue;
		var pr_area_value=e.target.children.area_value.defaultValue;
		var pr_road_value=e.target.children.road_value.defaultValue;
		var pr_sit_value=e.target.children.sit_value.defaultValue;
		var sit_value=e.target.children.show_sit_value.defaultValue;
		var pr_vehicle_id=e.target.children.vehicle_id.defaultValue;
		var pr_life_cycle=e.target.children.life_cycle.defaultValue;
		document.getElementById("#pr_area_value").val(pr_area_value);//库区
		document.getElementById("#pr_road_value").val(pr_road_value);//库道
		document.getElementById("#pr_sit_value").val(pr_sit_value);//库位
		document.getElementById("#pr_sit_value_old").val(sit_value);//库位
		document.getElementById("#inStatus").val(in_status);//入库状态
		document.getElementById("#outStatus").val(out_status);//出库状态
		document.getElementById("#pr_vehicle_id").val(pr_vehicle_id);//车辆ID
		document.getElementById("#pr_life_cycle").val(pr_life_cycle);//生命周期
		
		e.preventDefault();
		if(in_status!=<%=Constant.AUTO_IN_STATUS_02%> && out_status!=<%=Constant.AUTO_OUT_STATUS_02%> && sit_value!=""){// 位置调整
			document.getElementById('#mm').menu('enableItem', document.getElementById('#adjustVehicle'));
		}else{
			document.getElementById('#mm').menu('disableItem', document.getElementById('#adjustVehicle'));	
		}
		if(sit_value!=""){// 查看信息i
			document.getElementById('#mm').menu('enableItem', document.getElementById('#vehiclemsg'));
		}else{
			document.getElementById('#mm').menu('disableItem', document.getElementById('#vehiclemsg'));	
		}
		if(pr_vehicle_id!="" && pr_vehicle_id!=-1 && pr_life_cycle==<%=Constant.VEHICLE_LIFE_02%>){// 退回生产线
			document.getElementById('#mm').menu('enableItem', document.getElementById('#but_retreat'));
		}else{
			document.getElementById('#mm').menu('disableItem', document.getElementById('#but_retreat'));	
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
//调整位置
function adjustVehicle(){
	var inStatus=document.getElementById('#inStatus').val();	
	var outStatus=document.getElementById('#outStatus').val();	
	var sit_value_old=document.getElementById("#pr_sit_value_old").val();
	var vid=document.getElementById("#pr_vehicle_id").val();
	if(inStatus!=<%=Constant.AUTO_IN_STATUS_02%> && outStatus!=<%=Constant.AUTO_OUT_STATUS_02%> && sit_value_old!=""){//jquery bug需过滤下 虽然样式改变了，但是点击该MENU还是能执行操作
		var sit_value=document.getElementById("#pr_sit_value").val();
		OpenHtmlWindow("<%=contextPath%>/sales/storage/storagemanage/VehiclePicture/AdjustInit.do?sitId="+sit_value,800,250);
	}
}
//退回生产线
function retreatVehicleInit(){
	var vid=document.getElementById("#pr_vehicle_id").val();//车辆ID
	var vin=document.getElementById("#pr_vin").val();//VIN号
	var life_cycle=document.getElementById("#pr_life_cycle").val();//生命周期
	if(vid!="" && vid!=-1 && vid!=null && vin!="" && life_cycle==<%=Constant.VEHICLE_LIFE_02%>){
		OpenHtmlWindow("<%=contextPath%>/sales/storage/storagemanage/VehiclePicture/retreatVehiclePIC.do?vehicle_id="+vid,800,350);
	}
}
function retreatVehicle(remark,groupIds){//车辆退回
	var reUrl = "<%=contextPath%>/sales/storage/storagemanage/VehicleRetreat/retreatVehicle.json";
	makeCall(reUrl,retreatVechileBack,{REMARK:remark,groupIds:groupIds});
}
function retreatVechileBack(json){
	window.location.href = "<%=request.getContextPath()%>/sales/storage/storagemanage/VehiclePicture/picRecInit.do";
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
	if(inStatus!=<%=Constant.AUTO_IN_STATUS_02%>){//jquery bug需过滤下 虽然样式改变了，但是点击该MENU还是能执行操作
		var inLockUrl = "<%=contextPath%>/sales/storage/storagemanage/VehiclePicture/inLockStatus.json";
		makeCall(inLockUrl,statusBack,{road_value:road_value,changeStatus:changeStatus,yieldly:yieldly,areaId:areaId});
	}
}
// 出库锁定
function outLock(target){
	var outStatus=document.getElementById('#outStatus').val();	
	var road_value=document.getElementById("#hr_road_value").val();
	var changeStatus=<%=Constant.AUTO_OUT_STATUS_02%>;
	var yieldly=document.getElementById('#yieldly').val();	
	var areaId=${valueMap.pareaId};	
	if(outStatus!=<%=Constant.AUTO_OUT_STATUS_02%>){//jquery bug需过滤下 虽然样式改变了，但是点击该MENU还是能执行操作
		var inLockUrl = "<%=contextPath%>/sales/storage/storagemanage/VehiclePicture/outLockStatus.json";
		makeCall(inLockUrl,statusBack,{road_value:road_value,changeStatus:changeStatus,yieldly:yieldly,areaId:areaId});
	}
}
// 入库解锁
function inReLock(target){
	var inStatus=document.getElementById('#inStatus').val();	
	var road_value=document.getElementById("#hr_road_value").val();
	var changeStatus=<%=Constant.AUTO_IN_STATUS_01%>;
	var yieldly=document.getElementById('#yieldly').val();	
	var areaId=${valueMap.pareaId};	
	if(inStatus!=<%=Constant.AUTO_IN_STATUS_01%>){//jquery bug需过滤下 虽然样式改变了，但是点击该MENU还是能执行操作
		var inLockUrl = "<%=contextPath%>/sales/storage/storagemanage/VehiclePicture/inLockStatus.json";
		makeCall(inLockUrl,statusBack,{road_value:road_value,changeStatus:changeStatus,yieldly:yieldly,areaId:areaId});
	}
}
// 出库解锁
function outReLock(target){
	var outStatus=document.getElementById('#outStatus').val();	
	var road_value=document.getElementById("#hr_road_value").val();
	var changeStatus=<%=Constant.AUTO_OUT_STATUS_01%>;
	var yieldly=document.getElementById('#yieldly').val();	
	var areaId=${valueMap.pareaId};	
	if(outStatus!=<%=Constant.AUTO_OUT_STATUS_01%>){//jquery bug需过滤下 虽然样式改变了，但是点击该MENU还是能执行操作
		var inLockUrl = "<%=contextPath%>/sales/storage/storagemanage/VehiclePicture/outLockStatus.json";
		makeCall(inLockUrl,statusBack,{road_value:road_value,changeStatus:changeStatus,yieldly:yieldly,areaId:areaId});
	}
}
function statusBack(json){
	window.location.href = "<%=request.getContextPath()%>/sales/storage/storagemanage/VehiclePicture/picRecInit.do?areaId="+json.valueMap.pareaId+"&yieldly="+json.valueMap.pyieldly;
}
function rightReLoad(tar,areaId){
	var yieldly=document.getElementById("#yieldly").val();
	window.location.href = "<%=request.getContextPath()%>/sales/storage/storagemanage/VehiclePicture/picRecInit.do?areaId=" +areaId+"&yieldly="+yieldly;			
}
function seachPic(){
	var yieldly=document.getElementById("#yieldly").val();
	window.location.href = "<%=request.getContextPath()%>/sales/storage/storagemanage/VehiclePicture/picRecInit.do?&yieldly="+yieldly;
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
//位置调整
function adjVehicle(VEHICLE_ID,SIT_NAME,SIT_ID){
	var adjkUrl = "<%=contextPath%>/sales/storage/storagemanage/VehicleSiteAdjust/saveVehicleSiteAdjust.json";
	makeCall(adjkUrl,saveVechileBack,{VEHICLE_ID:VEHICLE_ID,SIT_NAME:SIT_NAME,SIT_ID:SIT_ID});
}
function saveVechileBack(json){
	window.location.href = "<%=request.getContextPath()%>/sales/storage/storagemanage/VehiclePicture/picRecInit.do";
}
</script>
 </body>
</html>