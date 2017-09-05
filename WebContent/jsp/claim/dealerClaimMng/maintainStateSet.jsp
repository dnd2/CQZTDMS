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

<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：售后服务管理 &gt;维修登记 &gt;维修工单登记 &gt;保养状态判定</div>
<form method="post" name ="fm" id="fm">
	<table class="table_edit">
		<tr>
			<td width="15%" align="right">VIN：</td>
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
			<td align="right">当前日期：</td>
			<td>${now}</td>
			<td align="right" >购车日期：</td>
			<td>${map.SALES_DATE}
			</td>
			<td align="right" >历史保养次数：</td>
			<td>${map.FREE_TIMES}</td>
		</tr>
		<tr>
			<td align="right" >工单里程：</td>
			<td>${mile}</td>
			<td align="right" >购车天数：</td>
			<td>${days}</td>
			<td align="right" >当前工单保养次数：</td>
			<td>${map.FREE_TIMES+1}</td>
		</tr>
		<tr>
			<td align="right" >当前保养里程范围(公里)：</td>
			<td>${map.START_MILEAGE}-${map.END_MILEAGE}</td>
			<td align="right" >当前保养时间范围(天)：</td>
			<td>${map.MIN_DAYS}-${map.MAX_DAYS}</td>
			<td align="right" >是否需要预授权申请：</td>
			<td>
			<script type="text/javascript">writeItemValue('${flag}');</script>
			</td>
		</tr>
		<tr>
			<td align="right" >是否已申请预授权：</td>
			<td>
			<script type="text/javascript">writeItemValue('${preClaimYN}');</script>
			</td>
			<td align="right" >预授权审核状态：</td>
			<td>${preClaimStatus}</td>
		</tr>
	</table>
	<br />
	<table width="100%">
		<tr>
			<td>
				<font color=red>
					*校验以录入工单里程为准！如果工单里程发生变化，请再次校验以作参考！
				</font>
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