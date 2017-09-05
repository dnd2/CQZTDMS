<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>客户信息查看</title>
<script type="text/javascript">

	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body>
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户关系管理 &gt; 客户管理 &gt;客户信息查看</div>
		<table width="100%" class="tab_edit">
			<th colspan="6" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />基本信息</th>
			
			<tr>
				<td width="13%" align="right">客户名称：</td>
				<td width="20%" align="left">${clientInforManageMap.CTMNAME}</td>
				<td width="13%" align="right">客户类型：</td>
				<td width="20%" align="left">${clientInforManageMap.CTMTYPE}</td>
				<td width="13%" align="right">用途：</td>
				<td width="21%" align="left">${clientInforManageMap.SALESADDRESS}</td>
			</tr>
			
			<tr>
				<td align="right">身份证号：</td>
				<td align="left" colspan="5">${clientInforManageMap.CARDNUM}</td>
			</tr>
			
			<tr>
				<td align="right">省份：</td>
				<td align="left">${clientInforManageMap.PROVINCE}</td>
				<td align="right">县市：</td>
				<td align="left">${clientInforManageMap.CITY}</td>
				<td align="right">邮编：</td>
				<td align="left">${clientInforManageMap.POSTCODE}</td>
			</tr>
			
			<tr>
				<td align="right">客户地址：</td>
				<td align="left" colspan="5">${clientInforManageMap.ADDRESS}</td>
			</tr>
			
			<tr>
				<td align="right">联系人：</td>
				<td align="left">${clientInforManageMap.CTMNAME}</td>
				<td align="right">客户级别：</td>
				<td align="left">${clientInforManageMap.GUESTSTARS}</td>
				<td align="right"></td>
				<td align="left"></td>
			</tr>
			
			<tr>
				<td align="right">单位电话：</td>
				<td align="left">${clientInforManageMap.COMPHONE}</td>
				<td align="right">住宅电话：</td>
				<td align="left">${clientInforManageMap.OTHERPHONE}</td>
				<td align="right">手机：</td>
				<td align="left">${clientInforManageMap.PHONE}</td>
			</tr>
			
			<th colspan="6" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />车辆信息</th>
			
			<tr>
				<td align="right">车种：</td>
				<td align="left">${clientInforManageMap.SERIESNAME}</td>
				<td align="right">车型：</td>
				<td align="left">${clientInforManageMap.MODELNAME}</td>
				<td align="right">颜色：</td>
				<td align="left">${clientInforManageMap.COLOR}</td>
			</tr>
			
			<tr>
				<td align="right">产地：</td>
				<td align="left">${clientInforManageMap.YIELDLY}</td>
				<td align="right">内部型号：</td>
				<td align="left">${clientInforManageMap.PACKAGENAME}</td>
				<td align="right">省份：</td>
				<td align="left">${clientInforManageMap.AREA}</td>
			</tr>
			
			<tr>
				<td align="right">发动机号：</td>
				<td align="left">${clientInforManageMap.ENGINENO}</td>
				<td align="right">底盘号：</td>
				<td align="left">${clientInforManageMap.VIN}</td>
				<td align="right">价格：</td>
				<td align="left">${clientInforManageMap.PRICE}</td>
			</tr>
			
			<tr>
				<td align="right">生产日期：</td>
				<td align="left">${clientInforManageMap.PDATE}</td>
				<td align="right">购买日期：</td>
				<td align="left">${clientInforManageMap.PUDATE}</td>
				<td align="right">装备代码：</td>
				<td align="left">${clientInforManageMap.MATERIALCODE}</td>
			</tr>
			
			<tr>
				<td align="right">经销商名称：</td>
				<td align="left">${clientInforManageMap.DEALERNAME}</td>
				<td align="right">经销商等级：</td>
				<td align="left" colspan="3">${clientInforManageMap.DLEVEL}</td>
			</tr>
		</table>
		<br/>
		<div style="width: 100%; text-align: center;">
			<input name="addBtn" type="button" class="normal_btn"  value="关闭" onclick="window.close();" />
		</div>
</body>
</html>