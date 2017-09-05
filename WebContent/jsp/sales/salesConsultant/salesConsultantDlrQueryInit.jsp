<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>销售顾问查询</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
<!--
function dtlQuery(value) {
	var url = "<%=contextPath%>/sales/salesConsultant/SalesConsultant/dtlQuery.do?headId=" + value ;
	$('fm').action= url ;
	$('fm').submit();
}

function downloadIt() {
	var url = "<%=contextPath%>/sales/salesConsultant/SalesConsultant/dlrDownload.json" ;
	$('fm').action= url ;
	$('fm').submit();
}
//-->
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 经销商实销管理 &gt; 销售顾问管理 &gt; 销售顾问查询</div>
<form method="post" name="fm" id="fm">
<!-- 查询条件 begin -->
	<table class="table_query" >
		<tr>
			<td align="right">业务范围：</td>
			<td align="left">
				<select name="areaId" id="areaId" class="short_sel">
					<option value="">-请选择-</option>
					<c:forEach items="${areaList}" var="list">
						<option value="${list.AREA_ID}"><c:out value="${list.AREA_NAME}"/></option>
					</c:forEach>
				</select><input type="hidden" name="area" id="area"/>
			</td>
		</tr>
		<tr>
			<td align="right">状态：</td>
			<td align="left">
				<script type="text/javascript">
	                genSelBoxExp("status",<%=Constant.SALES_CONSULTANT_STATUS%>,"",true,"mini_sel","","false",'');
	            </script>
			</td>
		</tr>
		<tr>
			<td align="right">销售顾问姓名：</td>
			<td align="left">
				<input type="text" class="middle_txt" name="salesConName" id="salesConName" value="" />
			</td>
		</tr>
		<tr>
			<td align="center" colspan="2">
				<input type="hidden" name="areaIds" id="areaIds" value="${areaIds }" />
				<input name="qryBtn" id="queryBtn" type="button" class="cssbutton" onClick="__extQuery__(1);" value="查询">&nbsp;
				<input name="downloadBtn" id="downloadBtn" type="button" class="cssbutton" onClick="downloadIt() ;" value="下载">
			</td>
		</tr>
	</table>
<!-- 查询条件 end -->
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end --> 
</form>
<!--页面列表 begin -->
<script type="text/javascript" ><!--
	var myPage;
	//查询路径           
	var url = "<%=contextPath%>/sales/salesConsultant/SalesConsultant/dlrQuery.json";
	var title = null;
	var columns = [
				{header: "姓名",dataIndex: 'NAME',align:'center'},
				{header: "性别",dataIndex: 'SEX',align:'center',renderer:getItemValue},
				{header: "年龄",dataIndex: 'AGE',align:'center'},
				{header: "学历",dataIndex: 'ACADEMIC_RECORDS',align:'center',renderer:getItemValue},
				{header: "从事汽车行业年份", dataIndex: 'TRADEYEAR', align:'center'},
				{header: "从事长安汽车行业年份", dataIndex: 'CHANATRADEYEAR', align:'center'},
				{header: "联系电话", dataIndex: 'TEL', align:'center'},
				{header: "申请原因", dataIndex: 'REASON', align:'center'},
				{header: "状态", dataIndex: 'STATUS', align:'center',renderer:getItemValue},
				{header: "操作",sortable: false, dataIndex: 'ID', align:'center',renderer:myLink}
		      ];


	//超链接设置
	function myLink(value,meta,record){
		return String.format("<a href='#' onclick='dtlQuery(" + value + ")'>[明细]</a>");
	}
//-->
</script>
<!--页面列表 end -->
</body>
</html>