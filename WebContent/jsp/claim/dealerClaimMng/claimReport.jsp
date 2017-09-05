<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>       
<head>
<script type="text/javascript">
function doInit(){
   	loadcalendar();  //初始化时间控件
}
</script>
<TITLE>售后服务管理</TITLE>

</HEAD>
<BODY>

<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：售后服务管理 &gt;维修登记 &gt;维修工单登记 &gt;配件三包判定</div>
<form method="post" name ="fm" id="fm">
	<table class="table_edit">
		<th colspan="6">
			<img src="../../../img/nav.gif" />&nbsp;车辆基本信息
		</th>
		<tr>
			<td width="15%" align="right" >VIN：</td>
			<td width="15%">${map.VIN}</td>
			<td width="15%" align="right">发动机号：</td>
			<td width="15%">${map.ENGINE_NO}</td>
			<td width="15%" align="right">牌照号：</td>
			<td width="15%">${map.LICENSE_NO}</td>		
		</tr>
		<tr>
			<td align="right" >产地：</td>
			<td>
				<script type="text/javascript">
					writeItemValue('${map.YIELDLY}');
				</script>
			</td>
			<td align="right">车型：</td>
			<td>${map.MODEL_NAME}</td>
			<td align="right">三包策略：</td>
			<td>${map.GAME_NAME}</td>
		</tr>
		<tr>
			<td align="right" >工单开始日期：</td>
			<td>${now}</td>
			<td align="right" >购车日期：</td>
			<td>${map.SALES_DATE}
			</td>
			<td align="right" >行驶天数：</td>
			<td>${days}</td>
		</tr>
		<tr>
			<td align="right" >工单里程：</td>
			<td>${mile}</td>
			<td align="right" >历史保养次数：</td>
			<td>${map.FREE_TIMES}</td>
			<td align="right" >保养状态：</td>
			<td>${flag}</td>
		</tr>
		<tr>
			<td align="right" >车型组名称：</td>
			<td>${map.WRGROUP_NAME}</td>
			<td align="right" >车辆状态：</td>
			<td>
				<script>
					writeItemValue(${map.LIFE_CYCLE});
				</script>
			</td>
			<td align="right" >是否长安汽车：</td>
			<td>${CHANA}</td>
		</tr>
	</table>
	<br />
	<table class="table_edit" width="100%">
		<th>
			<img src="../../../img/nav.gif" />&nbsp;维修配件三包期判定
		</th>
	</table>
	<table width="95%">
		<tr align="center">
			<td>是否三包</td>
			<td>新件代码</td>
			<td>新件名称</td>
			<td>大类名称</td>
			<td>三包月份</td>
			<td>三包里程</td> 
		</tr>
		<c:forEach var="part" items="${nlist}">
			<c:if test="${part.isWarranty==yes}">
				<tr bgcolor="#cceedd" align="center"> 
					<td><strong>√</strong></td>
					<td>${part.partCode}</td>
					<td>${part.partName}</td>
					<td>${part.typeName}</td>
					<td>${part.claimMonth}</td>
					<td>${part.claimMelieage}</td> 
				</tr>
			</c:if>	
			<c:if test="${part.isWarranty!=yes}">
				<tr bgcolor="#e8e8e8" align="center">
					<td></td>
					<td>${part.partCode}</td>
					<td>${part.partName}</td>
					<td>${part.typeName}</td>
					<td>${part.claimMonth}</td>
					<td>${part.claimMelieage}</td> 
				</tr>
			</c:if>
		</c:forEach>
	</table>
	<table width="100%">
		<tr>
			<td> 
			</td>
		</tr>
		<tr>
			<td colspan="4" align="center">
				<input type="button" value="关闭" class="normal_btn" onclick="window.close();"/>
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript">
function goBack(){
	location = '<%=contextPath%>/claim/laborlist/LaborListAction/firstUrlInit.do' ;
}
</script>
</BODY>
</html>