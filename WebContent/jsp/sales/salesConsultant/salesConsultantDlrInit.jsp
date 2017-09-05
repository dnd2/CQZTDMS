<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>销售顾问申请</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
<!--
function addSubmit() {
	var url = "<%=contextPath%>/sales/salesConsultant/SalesConsultant/submitAddInit.do";
	$('fm').action= url ;
	$('fm').submit();
}

function cancelIt(value) {
	MyConfirm("确定取消?", SureCancel, [value]) ;
}

function SureCancel(value) {
	var url = "<%=contextPath%>/sales/salesConsultant/SalesConsultant/submitCancel.json";
	
	makeCall(url, cancelSet, {headId: value}) ;
}

function cancelSet(json) {
	var subFlag = json.subFlag ;
	
	if(subFlag == "success") {
		MyAlert("取消成功!") ;
		
		__extQuery__(1);
	} else {
		MyAlert("取消失败!") ;
	}
}

function updateIt(value) {
	var url = "<%=contextPath%>/sales/salesConsultant/SalesConsultant/submitUpdateInit.do?headId=" + value;
	$('fm').action= url ;
	$('fm').submit();
}
//-->
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 经销商实销管理 &gt; 销售顾问管理 &gt; 销售顾问申请</div>
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
			<td align="right">销售顾问姓名：</td>
			<td align="left">
				<input type="text" class="middle_txt" name="salesConName" id="salesConName" value="" />
			</td>
		</tr>
		<tr>
			<td align="center" colspan="2">
				<input type="hidden" name="areaIds" id="areaIds" value="${areaIds }" />
				<input name="addBtn" id="addBtn" type="button" class="cssbutton" onClick="addSubmit() ;" value="新增">&nbsp;
				<input name="qryBtn" id="queryBtn" type="button" class="cssbutton" onClick="__extQuery__(1);" value="查询">
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
<script type="text/javascript" >
<!--
	var myPage;
	//查询路径           
	var url = "<%=contextPath%>/sales/salesConsultant/SalesConsultant/submitQuery.json";
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
		var status = record.data.STATUS ;
		if(<%= Constant.SALES_CONSULTANT_STATUS_PASS%> == status) {
			return String.format("<a href='#' onclick='cancelIt(" + value + ")'>[取消]</a>");
		} else {
			return String.format("<a href='#' onclick='updateIt(" + value + ")'>[修改]</a>");
		}
	}
//-->
</script>
<!--页面列表 end -->
</body>
</html>