<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>

<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<%
String contextPath=request.getContextPath();
Map<String, Object> map =(Map<String, Object>)request.getAttribute("vehicleMap"); 
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=request.getContextPath() %>/jsp/sales/storage/storagemanage/storageUtil/storageUtil.js"></script>
<title> 车辆位置调整 </title>

</head>
<body onunload='javascript:destoryPrototype();'>
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理>仓库管理> 车辆位置调整
	</div>
    <form method="post" name ="fm" id="fm">
    <table  class="table_edit">
     <tr class="csstr" align="center">
      <td class="right" width="10%">产地：</td>  
		<td class="table_query_2Col_input">
			<input type="hidden" id="VEHICLE_ID" name="VEHICLE_ID" class="middle_txt" value="<c:out value="${vehicleMap.VEHICLE_ID}"/>" />
			<input type="hidden" id="SIT_ID" name="SIT_ID" class="middle_txt" value="<c:out value="${vehicleMap.SIT_ID}"/>" />
			<input type="hidden" id="PER_CODE" name="PER_CODE" class="middle_txt" value="<c:out value="${vehicleMap.PER_CODE}"/>" />
			<input type="hidden" id="PRINT_VIN" name="PRINT_VIN" class="middle_txt" value="<c:out value="${vehicleMap.VIN}"/>" />
	  		<span>${vehicleMap.YIELDLY}</span>
     	 </td>  
     	  <td class="right" width="10%">生产单号：</td>  
     	  <td class="table_query_2Col_input">
     	  	<span>${vehicleMap.PLAN_DETAIL_ID}</span>  
     	  </td>
     </tr>
      <tr class="csstr" align="center">
      <td class="right">车型：</td>  
		<td class="table_query_2Col_input">
			<span>${vehicleMap.MODEL_CODE}</span>
     	 </td>  
     	  <td class="right">车型名称：</td>  
     	  <td class="table_query_2Col_input">
	     	 <span>${vehicleMap.MODEL_NAME}</span>
     	  </td>
     </tr>
      <tr class="csstr" align="center">
      <td class="right">配置代码：</td>  
		<td class="table_query_2Col_input">
			<span>${vehicleMap.PACKAGE_CODE}</span>
     	 </td>  
     	  <td class="right">配置名称：</td>  
     	  <td class="table_query_2Col_input">
	     	<span>${vehicleMap.PACKAGE_NAME}</span>
     	  </td>
     </tr>
       <tr class="csstr" align="center">
      <td class="right">物料代码：</td>  
		<td class="table_query_2Col_input">
			<span>${vehicleMap.MATERIAL_CODE}</span>	  		
     	 </td>  
     	  <td class="right">物料名称：</td>  
     	  <td class="table_query_2Col_input">
     		 <span>${vehicleMap.MATERIAL_NAME}</span>	
     	  </td>
     </tr>
        <tr class="csstr" align="center">
      <td class="right">VIN：</td>  
		<td class="table_query_2Col_input">
			 <span>${vehicleMap.VIN}</span>	
     	 </td>  
     	  <td class="right">发动机号：</td>  
     	  <td class="table_query_2Col_input">
     	   <span>${vehicleMap.ENGINE_NO}</span>	
     	  </td>
     </tr>
        <tr class="csstr" align="center">
      <td class="right">生产日期：</td>  
		<td class="table_query_2Col_input">	
		 	<span>${vehicleMap.PRODUCT_DATE}</span>		 	
     	 </td>  
     	  <td class="right">下线日期：</td>  
     	  <td class="table_query_2Col_input">
     	  	<span>${vehicleMap.OFFLINE_DATE}</span>	
     	  </td>
     </tr>
        <tr class="csstr" align="center">
      <td class="right">入库日期：</td>  
		<td class="table_query_2Col_input">
			<span>${vehicleMap.STORAGE_DATE}</span>
     	 </td>  
     	   <td class="right">接车员：</td>
     	   <td class="table_query_2Col_input">
     	   <span>${vehicleMap.PER_NAME}</span>	
			 </td> 
     </tr>
    <tr class="csstr" align="center">
	  <td class="right">库区：</td>  
		    <td class="table_query_2Col_input">
	  		<select name="AREA_NAME" id="AREA_NAME" class="u-select" onchange="roadCheckBox('other');">
	  			<option value="">-请选择-</option>
	  			<c:if test="${list_An!=null}">
					<c:forEach items="${list_An}" var="list">
						<option value="${list.AREA_ID }">${list.AREA_NAME }</option>
					</c:forEach>
				</c:if>
	  		</select>
     	 </td>   
		  <td class="right">库道：</td>
		   	   <td class="table_query_2Col_input">
	  		<select name="ROAD_NAME" id="ROAD_NAME" class="u-select" onchange="sitCheckBox('other');" >
	  			<option value="">-请选择-</option>
	  			
	  		</select>
			 </td> 
 </tr>
 <tr class="csstr" align="center">
 <td class="right">库位：</td>
		   	   <td class="table_query_2Col_input">
	  		<select name="SIT_NAME" id="SIT_NAME" class="u-select">
	  			<option value="">-请选择-</option>
	  			
	  		</select>
			 </td> 
			 <td class="right">库位码：</td>  
     	  <td class="table_query_2Col_input">
	     	  <span>${vehicleMap.SIT_CODE}</span>
     	  </td>
 </tr>
	 <tr class="csstr" align="center">
	 	<td class="right">调整备注：</td>
		<td align="left" colspan="3">
			<textarea id="ADJUST_REMARK" name="ADJUST_REMARK" datatype="1,is_null,400" style="width: 90%;"></textarea><font color="red">限200字</font>
		</td>
	 </tr>
	</table>
	<table class="table_edit">
			<tr>
				<td align="center" colspan="4">
					<input type="button" name="button1" class="normal_btn" id="saveButton" onclick="save()" value="确定调整" /> 
					<!--<input type="button" name="button1" class="normal_btn" onclick="printSitCode();" value="打印库位码" /> -->
					<input type="button" name="button1" id="goBack" class="normal_btn" onclick="window.close();" value="返回" /> 
					<!-- <input type="button"  id="pictureRec" name="pictureRec" class="normal_btn"   onclick="picRec();" value="图形接车" /> -->
				</td>
			</tr>
		</table>
	<br/>
	<table class="tab_view">
		<tr>
			<th width="10%">调整人</th>
			<th width="20%">调整日期</th>
			<th width="70%">调整备注</th>
		</tr>
		
		<c:if test="${adjustList != null }">		
			<c:forEach items="${adjustList }" var="al">
				<tr>
					<td align="center" class="tab_viewtd"><c:out value="${al.NAME }"/></td>
					<td align="center" class="tab_viewtd"><c:out value="${al.ADJUST_DATE }"/></td>
					<td class="tab_viewtd"><c:out value="${al.ADJUST_REMARK }"/></td>
				</tr>	
			</c:forEach>		
		</c:if>
		
	</table>
	</form>
</div>
<script type="text/javascript" >
function checkData(){
	var area = document.getElementById("AREA_NAME").value;
	var road = document.getElementById("ROAD_NAME").value;
	var sit = document.getElementById("SIT_NAME").value;
	if(area==""){
		MyAlert("请选择库区");
		return false;
	}
	if(road==""){
		MyAlert("请选择库道");
		return false;
	}
	if(sit==""){
		MyAlert("请选库位");
		return false;
	}
	return true;
}
function save()
{ 	
	if(checkData()==true){
		MyConfirm("确认保存该信息！",saveReceiving);
	}

}
function saveReceiving()
{ 
	disabledButton(["saveButton","goBack"],true);
	makeNomalFormCall("<%=contextPath%>/sales/storage/storagemanage/VehicleSiteAdjust/saveVehicleSiteAdjust.json",saveVechileBack,'fm','queryBtn'); 
}
function saveVechileBack(json){
	if(json.returnValue == 1)
	{
		parent.MyAlert("操作成功！");
		//打印库位码
		MyUnCloseConfirm("操作成功,是否需要打印标签?",isPrintSitCode,[json.vehicleId],canelFunc);
	}else if(json.returnValue==2){
		disabledButton(["saveButton","goBack"],false);
		MyAlert("操作失败！请联系系统管理员！");
	}else if(json.returnValue==3){
		disabledButton(["saveButton","goBack"],false);
		MyAlert("该位置已占用，请重新选择！");
	}else if(json.returnValue==4){
		disabledButton(["saveButton","goBack"],false);
		MyAlert("数据已经被修改,请注意避免同时操作！");
	}else if(json.returnValue==5){
		disabledButton(["saveButton","goBack"],false);
		MyAlert("该车辆已经配车，请调整后再移库！");
	}else if(json.returnValue==6){
		disabledButton(["saveButton","goBack"],false);
		MyAlert("车辆不存在，请避免同时操作！");
	}else
	{
		disabledButton(["saveButton","goBack"],false);
		MyAlert("操作失败！请联系系统管理员！");
	}	
}
//取消操作执行
function canelFunc(){
	window.close();
}
//打印库位码(确定)
function isPrintSitCode(val){
	var area = document.getElementById("AREA_NAME").value;
	var road = document.getElementById("ROAD_NAME").value;
	var sit = document.getElementById("SIT_NAME").value;
	var perCode = document.getElementById("PER_CODE").value;
	var vin = document.getElementById("PRINT_VIN").value;
	var sitCode=area+"-"+road+"-"+sit+"*"+((perCode==null || perCode=='null')?'':perCode);
	window.opener.printSitCode(vin,sitCode);//调用父页面的打印
}
function storageInit(){
	var area = document.getElementById("AREA_NAME");
	var road = document.getElementById("ROAD_NAME");
	var sit = document.getElementById("SIT_NAME");
	area.value = <%=map.get("AREA_ID")%>;
	road.options.length = 0;
	road.options[0]=new Option(<%=map.get("ROAD_NAME")%>, <%=map.get("ROAD_ID")%>+"");
	sit.options.length = 0;
	sit.options[0]=new Option(<%=map.get("SIT_NAME")%>, <%=map.get("SIT_ID")%>+"");	
}
//初始化    
function doInit(){	
	storageInit();
	//日期控件初始化
}
//图形接车
function picRec(){
	//把库区带入后台处理
	window.open("<%=request.getContextPath()%>/sales/storage/storagemanage/VehicleSiteAdjust/picRecInit.do?areaId="+<%=map.get("AREA_ID")%>+"&yieldly="+${vehicleMap.YIELDLY_ID}+"&sitId="+ <%=map.get("SIT_ID")%>);
}
//图形页面返回的值
function setValues(areaId,roadId,sitId) {
	var area = document.getElementById("AREA_NAME");
	var road = document.getElementById("ROAD_NAME");
	var sit = document.getElementById("SIT_NAME");
	area.value = areaId;
	road.value = roadId;
	sit.value = sitId;
}
function back(){
	fm.action="<%=contextPath%>/sales/storage/storagemanage/VehicleSiteAdjust/vehicleSiteAdjustInit.do";
	fm.submit();
}
</script>
</body>
</html>
