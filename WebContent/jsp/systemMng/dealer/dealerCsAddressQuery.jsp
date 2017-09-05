<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>经销商地址查看</title>
<script type="text/javascript">
	String.prototype.trim = function() {
		return this.replace(/(^\s*)|(\s*$)/g, "");
	}
	
	window.onload = function() {
		__extQuery__(1);
	}
</script>
</head>

<body>
<form id="fm" name="fm">
<input type="hidden" value="${param['DEALER_ID']}" id="DEALER_ID" name="DEALER_ID" />
<table class="table_query" border="0">
	<tr>
		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">联系人姓名：</td>
		<td class="table_query_4Col_input" nowrap="nowrap"><input
			name="LINKMAN" maxlength="30" value="" datatype="1,is_noquotation,30" id="LINKMAN" type="text" class="middle_txt" /></td>
		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">联系人性别：</td>
		<td class="table_query_4Col_input" nowrap="nowrap">
			<label>
					<script type="text/javascript">
						genSelBoxExp("GENDER",<%=Constant.GENDER_TYPE%>,"-1",'true',"short_sel",'',"false",'');
					</script>
			</label>
		</td>
	</tr>
	<tr>
		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">联系人手机：</td>
		<td class="table_query_4Col_input" nowrap="nowrap"> 
			<input
				name="MOBILE_PHONE" maxlength="30" value="" datatype="1,is_noquotation,30" id="MOBILE_PHONE" type="text" class="middle_txt" /></td>
		</td>
		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">联系人电话：</td>
		<td class="table_query_4Col_input" nowrap="nowrap">
			<input
				name="TEL" maxlength="30" value="" datatype="1,is_noquotation,30" id="TEL" type="text" class="middle_txt" /></td>
		</td>
	</tr>
	<tr id="tr_state" style="display: none;">
		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">状态：</td>
		<td colspan="3" class="table_query_4Col_input" nowrap="nowrap">
			<label>
					<script type="text/javascript">
						genSelBoxExp("STATE",<%=Constant.STATUS%>,"-1",'true',"short_sel",'',"false",'');
					</script>
			</label>
		</td>
	</tr>	
	
	<tr align="center">
		<td colspan="4" class="table_query_4Col_input" style="text-align: center">
			<input name="queryBtn" type="button" class="normal_btn" onclick="query()" value="查 询" id="queryBtn" /> &nbsp; 
			<input name="button2" type="button" class="normal_btn" onclick="_hide();" value="关闭" />
		</td>
	</tr>
</table>
</form>
<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />	
<script>
var myPage;
var url = "<%=contextPath%>/sysmng/dealer/DealerAddressInfo/queryDealeCsrAddressInfo.json?DEALER_ID=${param['DEALER_ID']}";

var title= null;
var columns = [
				{header: "配件收货地址",width:'30%',   dataIndex: 'ADDR'},
				{header: "状态", width:'10%', dataIndex: 'STATE',renderer:getItemValue},
				{header: "联系人姓名", width:'20%', dataIndex: 'LINKMAN'},
				{header: "性别", width:'20%', dataIndex: 'GENDER',renderer:getItemValue},
				{header: "手机", width:'10%', dataIndex: 'MOBILE_PHONE'},
				{header: "电话", width:'10%', dataIndex: 'TEL'}
			  ];
			  
function query() {
	__extQuery__('${curPage}');
}

function $(id) {
	if(typeof id === "string") {
		return document.getElementById(id);
	}
	
	return id;
}
</script>
</body>
</html>
