<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>

<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<%
String contextPath=request.getContextPath();
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title> 车辆信息查看 </title>

</head>
<body>
    <table  class="table_info">
     <tr class="csstr" align="center">
      <td class="right">产地：</td>  
		<td class="table_query_2Col_input">
	  		<span>${vehicleMap.YIELDLY}</span>
     	 </td>  
     	  <td class="right">生产单号：</td>  
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
			<span>${vehicleMap.AREA_NAME}</span>
     	 </td>  
     	   <td class="right">库道：</td>
     	   <td class="table_query_2Col_input">
     	   <span>${vehicleMap.ROAD_NAME}</span>	
			 </td> 
			 
 </tr>
 <tr class="csstr" align="center">
 <td class="right">库位：</td>  
		<td class="table_query_2Col_input">
			<span>${vehicleMap.SIT_NAME}</span>
     	 </td>  
			 <td class="right">库位码：</td>  
     	  <td class="table_query_2Col_input">
	     	  <span>${vehicleMap.SIT_CODE}</span>
     	  </td>
 </tr>	
 <tr>
			<td align="center" colspan="4">
					<input type="button" name="button1" class="normal_btn" onclick="parent._hide()" value="返回" /> 
				</td>
			</tr>
	</table>
</body>
</html>
