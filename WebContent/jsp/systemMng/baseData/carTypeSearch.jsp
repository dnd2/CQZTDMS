<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>销售意向</title>
</head>
<body onunload='javascript:destoryPrototype()' onload="__extQuery__(1);loadcalendar();">
<div class="wbox">
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />当前位置：
	系统管理 &gt; 基础数据维护 &gt; 车型配置信息查询
</div>
<form id="fm"   onkeydown="if(event.keyCode==13){return   false};">
<table class="table_query" border="0">
	<tr>
		<td nowrap="nowrap" align="center">车型代码：
			<input class="middle_txt" name="modelCode" type="text"/></td>
		<td align="left" width="350">
			<input class="normal_btn" type="button" value="查 询" id="queryBtn" onclick="__extQuery__(1);"/>
		</td>	
	</tr>
</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
</form>
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</div>
</body>
</html>
<script type="text/javascript" >
		var url = "<%=contextPath%>/sysmng/sysData/CarTypeInfoQuery/queryCar.json?COMMAND=1";
	var title= null;
	//设置列名属性
	var columns = [
					{header: "序号", width:'35', renderer:getIndex}, //设置序号的方式
					{header: "车型代码",orderCol:"MODEL_CODE",dataIndex: 'modelCode'},
					{header: "标准配置",  dataIndex: 'standardPkg'}
				  ];


</script>
