<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
    String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>查询</title>
</head>
<script type="text/javascript">
$(function(){
	__extQuery__(1);
});
var myPage;
var url = "<%=contextPath%>/parts/storageManager/partDlrStockMvManager/partDlrStockMvAction/queryWhDealer.json";
var title = null;
var columns = [ {
	header : "选择",
	align : 'center',
	dataIndex : 'ORG_ID',
	renderer : getRadio
}, {
	header : "经销商编码",
	dataIndex : 'ORG_CODE',
	align : 'center'
}, {
	header : "经销商名称",
	dataIndex : 'ORG_NAME',
	align : 'center'
} ];

function getRadio(value, meta, record) {
	var whId = record.data.WH_ID;
	var orgId = record.data.ORG_ID;
	var orgCode = record.data.ORG_CODE;
	var orgName = record.data.ORG_NAME;
	var html = '<input type="radio" onclick="selData(\'' + whId + '\',\'' + orgId + '\',\'' + orgCode + '\',\'' + orgName + '\')" />';
	return String.format(html);
}
function selData(whId, id, code, name) {
	parentDocument.getElementById("FROM_WH_ID").value = whId;
	parentDocument.getElementById("FROM_ORG_ID").value = id;
	parentDocument.getElementById("FROM_ORG_CODE").value = code;
	parentDocument.getElementById("FROM_ORG_NAME").value = name;
	_hide();
}
</script>
<body onload="__extQuery__(1);">
	<div class="wbox">
		<form name="fm" id="fm" method="post" enctype="multipart/form-data">
			<div class="form-panel">
				<h2>
					<img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;" />查询条件
				</h2>
				<div class="form-body">
					<table border="0" class="table_query">
						<tr>
							<td class="right">经销商名称：</td>
							<td>
								<input class="middle_txt" type="text" id="ORG_NAME" name="ORG_NAME" />
							</td>

							<td class="right">
								<div id="code_name">经销商编码：</div>
							</td>
							<td>
								<div id="code_text">
									<input class="middle_txt" type="text" id="ORG_CODE" name="ORG_CODE" />
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="4" class="center">
								<input class="u-button" type="button" value="查 询" onclick="__extQuery__(1);" />
								<input class="u-button" type="button" value="关 闭" onclick="_hide();" />
							</td>
						</tr>
					</table>
				</div>
			</div>
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		</form>
	</div>
</body>
</html>
