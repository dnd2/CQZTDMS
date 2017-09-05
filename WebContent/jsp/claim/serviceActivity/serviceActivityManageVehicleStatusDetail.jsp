<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@page import="com.infodms.dms.bean.TtAsActivityVehicleBean"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<% TtAsActivityVehicleBean VehicleBean=(TtAsActivityVehicleBean)request.getAttribute("VehicleBean"); %>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>服务车辆信息及状态查询</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
function doInit()
	{
	   loadcalendar();
	}
</script>
</head>

<body>
	<div class="navigation">
			<img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;服务活动管理&gt;服务活动管理
	</div>
<table width="95%" border="0" class="table_edit">
	  <tr>
		  <th colspan="4" align="left">
				  <img class="nav" src="<%=contextPath %>/img/subNav.gif" /> 车辆信息
		  </th>
	  </tr>
      <tr>
	      <td class="table_query_2Col_label_2Letter" >VIN：</td>
	      <td class="table_query_2Col_label_4Letter" ><c:out value="${VehicleBean.vin}"></c:out></td> 
		  <td class="table_query_2Col_label_8Letter" >活动责任经销商代码：</td>
		  <td class="table_query_2Col_label_4Letter" ><c:out value="${VehicleBean.dealerCode}"></c:out></td> 
	  </tr>
	  <tr>
		   <td  class="table_query_2Col_label_5Letter" >车辆牌照：</td>
		   <td  align="left" ><c:out value="${VehicleBean.lincenseTag}"></c:out></td> 
		   <td></td>
		   <td></td>
    </tr>
  </table>
<br/>
<table width="95%" border="0"  class="table_edit">
	 <th colspan="4" align="left">
			 <img class="nav" src="<%=contextPath %>/img/subNav.gif" /> 车主信息
	 </th>
    <tr>
      <td width="18%"  align="right" >车主名称：</td>
      <td width="28%"  align="left" ><c:out value="${VehicleBean.linkman}"></c:out></td> 
	  <td width="18%"  align="right" >车主所在省：</td>
	  <td width="36%"  align="left" ><c:out value="${VehicleBean.province}"></c:out></td> 
	</tr>
	<tr>
	  <td  align="right" >车主所在地区：</td>
	  <td  align="left" ><c:out value="${VehicleBean.area}"></c:out></td>
	  <td  align="right" >车主所在县市：</td>
	  <td  align="left" ><c:out value="${VehicleBean.town}"></c:out></td> 
     </tr>
    <tr>
      <td  align="right" >车主邮编：</td>
      <td  align="left" ><c:out value="${VehicleBean.postalCode}"></c:out></td> 
	  <td  align="right" >车主地址：</td>
	  <td  align="left" ><c:out value="${VehicleBean.customerAddress}"></c:out> </td> 
    </tr>
	<tr>
	  <td  align="right" >车主EMAIL：</td>
	  <td  align="left" ><c:out value="${VehicleBean.email}"></c:out></td> 
	  <td align="right"></td>
	  <td align="left"></td>
    </tr>
  </table>
<br/>
<table width="95%" border="0" class="table_edit">
	<th colspan="4" align="left">
		<img class="nav" src="<%=contextPath %>/img/subNav.gif" /> 联系人信息
	</th>
    <tr>
      <td width="18%" align="right" >联系人的姓名：</td>
    <td width="28%" align="left" ><c:out value="${VehicleBean.linkman}"></c:out></td> 
	  <td width="18%"  align="right" >联系人的手机：</td>
	  <td width="36%" align="left" ><c:out value="${VehicleBean.linkmanMobile}"></c:out></td> 
	 </tr>
	  <tr>
	    <td width="18%" align="right" >联系人办公电话：</td>
	   <td width="28%" align="left" ><c:out value="${VehicleBean.linkmanOfficePhone}"></c:out></td> 
	   <td  align="right" >联系人家庭电话：</td>
	   <td width="36%" align="left" ><c:out value="${VehicleBean.linkmanFamilyPhone}"></c:out></td> 
    </tr>
  </table>
<br/>
	<form name="forName" >
		<table  width="85%" align=center>
		<tr align ="right">
			   <td align="center">
			   <input  type=button value='关闭' name="closeName" class="normal_btn" onclick="_hide();">
			   </td>
		</tr>
		</table>
	</form>
</body>
</html>