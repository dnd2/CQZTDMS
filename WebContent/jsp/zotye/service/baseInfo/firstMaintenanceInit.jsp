<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>首保信息维护</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
<!--
function addSubmit() {
	var url = "<%=contextPath%>/zotye/service/baseInfo/FirstMaintenanceAction/firstMaintenaceAddInit.do";
	OpenHtmlWindow(url, 650, 380, '首保信息新增');
	
	/* fm.action = url ;
	fm.submit() ; */
}

function updateIt(value) {
	var url = "<%=contextPath%>/zotye/service/baseInfo/FirstMaintenanceAction/firstMaintenaceUpdateInit.do?id=" + value;
	OpenHtmlWindow(url, 650, 380, '首保信息修改');
	
	/* fm.action = url ;
	fm.submit() ; */
}
//-->
</script>
</head>
<body onload="__extQuery__(1);">
<div class="wbox">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理 &gt; 基础数据管理 &gt; 首保信息维护</div>
<form method="post" name="fm" id="fm">
<!-- 查询条件 begin -->
	<div class="form-panel">
		<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
				<table class="table_query" >
					<tr>
						<td style="text-align: center" colspan="2">
							<input type="hidden" name="areaIds" id="areaIds" value="${areaIds }" />
							<input name="queryBtn" id="queryBtn" type="button" class="normal_btn" onClick="__extQuery__(1);" value="查询">
							<input name="addBtn" id="addBtn" type="button" class="normal_btn" onClick="addSubmit() ;" value="新增">&nbsp;
						</td>
					</tr>
				</table>
			</div>
	</div>
<!-- 查询条件 end -->
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end --> 
</form>
<!--页面列表 begin -->
</div>
<script type="text/javascript" >
<!--
	var myPage;
	//查询路径           
	var url = "<%=contextPath%>/zotye/service/baseInfo/FirstMaintenanceAction/firstMaintenaceQuery.json";
	var title = null;
	var columns = [
				{header: "序号",align:'center', renderer:getIndex, width:'7%'},
				{header: "操作",sortable: false, dataIndex: 'ID', align:'center',renderer:myLink},
				{header: "首保里程",dataIndex: 'END_MILEAGE',align:'center'},
				{header: "首保时间/天",dataIndex: 'MAX_DAYS',align:'center'},
				{header: "创建日期",dataIndex: 'CREATE_DATE',align:'center'},
				{header: "创建人",dataIndex: 'NAME',align:'center'}
		      ];
	
	function myLink(value,meta,record){
		return String.format("<a href='#' onclick='updateIt(" + value + ")'>[修改]</a>");
	}
//-->
</script>
<!--页面列表 end -->
</body>
</html>