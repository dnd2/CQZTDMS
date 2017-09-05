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
<title> 退车入库 </title>

</head>
<body onunload='javascript:destoryPrototype();'>
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理>发运管理> 退车入库
	</div>
    <form method="post" name ="fm" id="fm">
    <table  class="table_edit">
     <tr class="csstr" align="center">
      <td class="right" width="10%">产地：</td>  
		<td class="table_query_2Col_input">
			<input type="hidden" id="VEHICLE_ID" name="VEHICLE_ID" class="middle_txt" value="<c:out value="${vehicleMap.VEHICLE_ID}"/>" />
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
			<span>${vehicleMap.ORG_STORAGE_DATE}</span>	
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
	</table>
	<table class="table_edit">
			<tr>
				<td align="center" colspan="4">
					<input type="button" name="button1" class="normal_btn" id="saveButton" onclick="save()" value="确定入库" /> 
					<input type="button" name="button1" class="normal_btn" id="goBack" onclick="back();" value="返回" /> 
				</td>
			</tr>
		</table>
	<br/>
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
	makeNomalFormCall("<%=contextPath%>/sales/storage/sendmanage/RebackStock/saveRebackStock.json",saveVechileBack,'fm','queryBtn'); 
}
function saveVechileBack(json){
	if(json.returnValue == 1)
	{
		//打印库位码
		MyUnCloseConfirm("操作成功,是否需要打印标签?如选择取消，将无法打印该标签!",isPrintSitCode,[json.vehicleId],canelFunc);
	}else if(json.returnValue == 2)
	{
		disabledButton(["saveButton","goBack"],false);
		MyAlert("操作失败！库位错误！");
	}	
	else if(json.returnValue == 3)
	{
		disabledButton(["saveButton","goBack"],false);
		MyAlert("操作失败！该库位已有车辆！");
	}
	else
	{
		disabledButton(["saveButton","goBack"],false);
		MyAlert("操作失败！请联系系统管理员！");
	}	
}
//取消操作执行
function canelFunc(){
	fm.action = "<%=contextPath%>/sales/storage/sendmanage/RebackStock/rebackStockInit.do";
	fm.submit();
}
//打印库位码(确定)
function isPrintSitCode(val){
	var tarUrl = "<%=contextPath%>/sales/storage/storagemanage/VehicleSiteAdjust/SitCodePrint.do?vehicle_id="+val;
	window.open(tarUrl,'','left=0,top=0,width='+screen.availWidth +'- 10,height='+screen.availHeight+'-50,toolbar=no,resizable=yes,menubar=no,scrollbars=no,location=no');
	fm.action = "<%=contextPath%>/sales/storage/sendmanage/RebackStock/rebackStockInit.do";
	fm.submit();
}
//初始化    
function doInit(){	
	//日期控件初始化
}
function back(){
	fm.action = "<%=contextPath%>/sales/storage/sendmanage/RebackStock/rebackStockInit.do";
	fm.submit();
}
</script>
</body>
</html>
