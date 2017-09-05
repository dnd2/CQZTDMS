<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>经销商订单审核</title>
<script type="text/javascript">
	var areaId = document.getElementsByName('org_id');
	var areaName = document.getElementsByName('org_name');
	var provinceId = document.getElementsByName('region_code');
	var provinceName = document.getElementsByName('region_name');
	var provinceArea = document.getElementsByName('org_code');
	
	function showArea(){
		for(var i = 0 ;i<areaId.length;i++){
			document.write("<option value="+areaId[i].value+" title="+areaName[i].value+">"+areaName[i].value+"</option>");
		}
	}

	function showProvince(value){
		var str = '<select class="short_sel">' ;
		for(var i = 0 ;i<provinceId.length;i++){
			if(provinceArea[i].value==value)
				str += '<option value='+provinceId[i].value+' title='+provinceName[i].value+'>'+provinceName[i].value+'</option>';
		}
		$('provinceSel').innerHTML = str+'</select>' ;
	}
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车销售 > 结算中心管理 > 经销商订单管理 > 经销商订单审核</div>
<form method="POST" name="fm" id="fm">
	<c:forEach items="${area}" var="a">
		<input type="hidden" name="org_id" value="${a.orgId}"/>
		<input type="hidden" name="org_name" value="${a.orgName}"/>
	</c:forEach>
	<c:forEach items="${province}" var="b">
		<input type="hidden" name="region_code" value="${b.regionCode}"/>
		<input type="hidden" name="region_name" value="${b.regionName}"/>
		<input type="hidden" name="org_code" value="${b.orgId}"/>
	</c:forEach>
	
	<table class="table_edit">
		<tr>
			<td width="10%" align="right">大区：</td>
			<td width="20%">
				<select class="short_sel" id="areaSel" onchange="showProvince(value);">
					<option>-请选择-</option>
					<script type="text/javascript">
						showArea();
					</script>
				</select>
			</td>
			<td width="10%" align="right">省份：</td>
			<td width="20%">
				<span id="provinceSel">
					<select class="short_sel">
					</select>
				</span>
			</td>
		</tr>
	</table>
</form>
</body>
</html>
