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
<script type="text/javascript" src="<%=request.getContextPath() %>/jsp/sales/storage/storagemanage/storageUtil/picUtil.js"></script>

<title> 图形调整 </title>

</head>
<body>
    <form method="post" name ="fm" id="fm">
    <table  class="table_edit">
  <tr class="csstr" align="center">
	  <td class="right">库区：</td>  
		    <td class="table_query_1Col_input">
		    <input type="hidden" name="oldSitId" id="oldSitId" value="<%=map.get("SIT_ID")%>"/> 
		    <input type="hidden" name="vehicleId" id="vehicleId" value="<%=map.get("VEHICLE_ID")%>"/> 
	  		<select name="AREA_NAME" id="AREA_NAME" class="u-select" onchange="roadCheckBox('other');">
	  		<option value="">-请选择-</option>
	  			<c:if test="${list_Area!=null}">
					<c:forEach items="${list_Area}" var="list">
						<option value="${list.AREA_ID }">${list.AREA_NAME }</option>
					</c:forEach>
				</c:if>
	  		</select>
     	 </td>   
		  <td class="right">库道：</td>
		   	   <td class="table_query_1Col_input">
	  		<select name="ROAD_NAME" id="ROAD_NAME" class="u-select" onchange="sitCheckBox('other');" >
	  		<option value="">-请选择-</option>
	  		</select>
			 </td> 
			  <td class="right">库位：</td>
		   	   <td class="table_query_1Col_input">
	  		<select name="SIT_NAME" id="SIT_NAME" class="u-select">
	  		<option value="">-请选择-</option>
	  		</select>
	  		</td>
 </tr>
	</table>
	<table class="table_edit">
			<tr>
				<td align="center" colspan="4">
					<input type="button" name="button1" class="normal_btn" onclick="saveInfo()" value="调整" /> 
					<input type="button" name="button1" class="normal_btn" onclick="parent._hide()" value="关闭" /> 
				</td>
			</tr>
		</table>
	</form>
<script type="text/javascript" >
var divDialogFlag = true;
function saveNext()
{ 	
	var sitId= document.getElementById("SIT_NAME").value;	
	var oldSitId=document.getElementById("oldSitId").value;	
	var vehicleId= document.getElementById("vehicleId").value;
	parent.document.getElementById('inIframe').contentWindow.adjVehicle(vehicleId,sitId,oldSitId);
	parent._hide();
	
}
function checkData(){
	var area = document.getElementById("AREA_NAME").value;
	var road = document.getElementById("ROAD_NAME").value;
	var sit = document.getElementById("SIT_NAME").value;
	if(area==""){
		MyAlertR("请选择库区",800,250);
		return false;
	}
	if(road==""){
		MyAlertR("请选择库道",800,250);
		return false;
	}
	if(sit==""){
		MyAlertR("请选库位",800,250);
		return false;
	}
	return true;
}
function saveInfo()
{ 	
	if(checkData()==true){		
		MyConfirmR("确认保存该信息！",800,250,saveNext);
	}

}
//初始化    
function doInit(){	
	cobo();
}
function cobo(){
	var area = document.getElementById("AREA_NAME");
	var areaId='<%=map.get("AREA_ID")%>';
	var roadId= '<%=map.get("ROAD_ID")%>';
	var sitId='<%=map.get("SIT_ID")%>';
	var areaName='<%=map.get("AREA_NAME")%>';
	var roadName= '<%=map.get("ROAD_NAME")%>';
	var sitName='<%=map.get("SIT_NAME")%>';
	area.value=areaId;
	doInitCombo("","",roadId,roadName,sitId,sitName);
}
</script>
</body>
</html>
