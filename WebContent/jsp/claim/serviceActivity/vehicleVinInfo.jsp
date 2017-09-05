<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>服务活动-车辆信息</title>
<%
	String contextPath = request.getContextPath();
%>
</head>
<body onload="__extQuery__(1);">
<form id="fm" name="fm" id="fm">
<input type="hidden" id="vin" name="vin" value="<%=request.getAttribute("vin")%>"/>
	<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;服务活动管理&gt;服务活动管理
	</div>
 <!--分页开始 -->
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
 <!--分页 结束 --> 
</form>
<!-- 页面列表 开始 -->
		<script type="text/javascript" >
			var myPage;
		
			var url = "<%=contextPath%>/claim/serviceActivity/ServiceActivityManageVehicleStatus/serviceActivityManageVehicleVin.json";
						
			var title = null;
		
			var columns = [
						{header: "序号", dataIndex: '1', align:'center'},
						{header: "工单编号",dataIndex: 'RO_NO' ,align:'center'},
						{header: "进厂日期",dataIndex: 'START_TIME' ,align:'center'},
						{header: "出厂日期",dataIndex: 'END_TIME_SUPPOSED' ,align:'center'},
						{header: "进厂里程",dataIndex: 'START_MILEAGE' ,align:'center'},
						{header: "是否换表",dataIndex: 'IS_RED_CHANGE_MILEAGE' ,align:'center'},
						{header: "项目类别",dataIndex: 'ITEMTYPE' ,align:'center'},
						{header: "项目代码",dataIndex: 'ADD_ITEM_CODE' ,align:'center'},
						{header: "项目名称",dataIndex: 'ADD_ITEM_NAME' ,align:'center'},
						{header: "故障描述",dataIndex: 'ERRORDESC' ,align:'center'},
						{header: "数量/单位",dataIndex: 'NUMUNIT' ,align:'center'},
						{header: "收费区分",dataIndex: 'CHARGE_MODE' ,align:'center'}
				      ];
		</script>
<!-- 页面列表结束 -->
<table class="table_query">
	<tr>
		<td align="center"> 
			<input class="normal_btn" type="button" name="button" value="关闭"  onclick="_hide();"/>
		</td>
	</tr>
</table>
</body>
</html>