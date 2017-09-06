<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
    String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件采购退货审核</title>
<script type="text/javascript" >
$(function(){
	__extQuery__(1);
});
var myPage;

var url = "<%=contextPath%>/parts/storageManager/partReturnManager/PartOemReturnChkManager/queryPartOemReturnApplyInfo.json";

var title = null;

var columns = [
			{header: "序号", align:'center',renderer:getIndex},
			{id:'action',header: "操作",sortable: false,dataIndex: 'RETURN_ID',renderer:myLink ,align:'center'},
			{header: "退货单号", dataIndex: 'RETURN_CODE', align:'center'},
               {header: "退货类型", dataIndex: 'RETURN_TO', align: 'center'},
			{header: "制单人", dataIndex: 'CREATE_NAME', align:'center'},
			{header: "制单日期", dataIndex: 'CREATE_DATE', align:'center',renderer:formatDate},
			{header: "退货原因", dataIndex: 'REMARK', align:'center'},
			{header: "提交日期", dataIndex: 'APPLY_DATE', align:'center',renderer:formatDate}
	      ];



//设置超链接
function myLink(value,meta,record){
		return String.format("<a href=\"#\" onclick='checkApply(\""+value+"\")'>[审核]</a>");
}

//审核
function checkApply(value){
	window.location.href = '<%=contextPath%>/parts/storageManager/partReturnManager/PartOemReturnChkManager/queryApplyDetailInit.do?returnId='+value;
}

//格式化日期
function formatDate(value,meta,record){
	var output = value.substr(0,10);
	return output;
}
</script>
</head>
<body>
	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 配件管理&gt;配件仓库管理 &gt;配件退货管理&gt;采购退货审核
		</div>
		<form name="fm" id="fm" method="post">
			<div class="form-panel">
				<h2>
					<img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;" />查询条件
				</h2>
				<div class="form-body">
					<table class="table_query" width=100% border="0" align="center" cellpadding="1" cellspacing="1">
						<tr>
							<td class="right">退货单号：</td>
							<td>
								<input class="middle_txt" type="text" name="RETURN_CODE" id="RETURN_CODE" />
							</td>
							<td class="right">制单日期：</td>
							<td>
								<div align="left">
									<input name="startDate" id="t1" value="${old }" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2"  style="width:80px;"/>
									<input name='button3' type='button' class='time_ico' value=" " title="点击选择时间" />
									&nbsp;至&nbsp;
									<input name="endDate" id="t2" value="${now }" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2"  style="width:80px;"/>
									<input name='button3' type='button' class='time_ico' value=" " title="点击选择时间" />
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="4" class="center">
								<input name="BtnQuery" id="queryBtn" class="u-button" type="button" value="查 询" onclick="__extQuery__(1);" />
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
