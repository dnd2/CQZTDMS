<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
    String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<script type="text/javascript">
$(function(){
	__extQuery__(1);
});

var objArr = [];
//初始化查询TABLE
var myPage;
var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrInstockExceptionLog/partDlrInstockExceptionLogQuery.json";
var title = null;

var columns = [
    {header: "序号", align: 'center', renderer: getIndex},
    {id: 'action', header: "操作", sortable: false, dataIndex: 'IN_ID', renderer: myLink, align: 'center'},
    {header: "发运单号", dataIndex: 'TRPLAN_CODE', align: 'center'},
    {header: "服务商编码", dataIndex: 'DEALER_CODE', align: 'center'},
    {header: "服务商", dataIndex: 'DEALER_NAME', style:"text-align: center"},
    {header: "破损异常件个数", dataIndex: 'EXCEPTION_NUM', align: 'center'},
    {header: "日期", dataIndex: 'CREATE_DATE', align: 'center'},
    {header: "验收人", dataIndex: 'CREATE_BY_NAME', align: 'center'},
    {header: "状态", dataIndex: 'STATE', align: 'center', renderer: getItemValue}
];
function myLink(value, meta, record) {
    if (record.data.STATE ==<%=Constant.INSTOCK_EXCEPTION_REPLY_03%> || record.data.STATE ==<%=Constant.INSTOCK_EXCEPTION_REPLY_02%>) {
        return String.format("<a href=\"#\" onclick='detail(\"" + value + "\")'>[查看]</a>");
    }
    return String.format("<a href=\"#\" onclick='detail(\"" + value + "\",\"" + record.data.STATE + "\")'>[回复]</a>" + "<a href=\"#\" onclick='closeE(\"" + value + "\")'>[关闭]</a>");
}

function detail(value, state) {
    window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrInstockExceptionLog/detail.do?inId=" + value + "&state=" + state;
}
function closeE(value) {
	MyConfirm('确定关闭该入库异常！', function() {
	    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrInstockExceptionLog/closeException.json?inId=" + value;
	    sendAjax(url, getResult, 'fm');
	});
}
function getResult(jsonObj) {
    if (jsonObj != null) {
        var success = jsonObj.success;
        var error = jsonObj.error;
        var exceptions = jsonObj.Exception;
        if (success) {
            MyAlert(success, function(){
            	__extQuery__(1);
	            // window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrInstockExceptionLog/partDlrInstockExceptionLogInit.do';
            });
        } else if (error) {
            MyAlert(error);
        } else if (exceptions) {
            MyAlert(exceptions.message);
        }
    }
}
</script>
</head>
<body onload="__extQuery__(1)" enctype="multipart/form-data">
	<form name="fm" id="fm" method="post">
		<div class="wbox">
			<div class="navigation">
				<img src="<%=contextPath%>/img/nav.gif" alt="" />>&nbsp;当前位置： 配件管理 &gt; 配件销售管理 &gt; 验收异常处理
			</div>
			<div class="form-panel">
				<h2>
					<img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;" />查询条件
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">日期：</td>
							<td>
								<input name="EstartDate" type="text" style="width: 80px;" class="middle_txt" id="EstartDate" value="${old}" />
								<input name="button2" type="button" class="time_ico" value=" " />
								至
								<input name="EendDate" type="text" style="width: 80px;" class="middle_txt" id="EendDate" value="${now}" />
								<input name="button2" type="button" class="time_ico" value=" " />
							</td>
							<td class="right">状态：</td>
							<td>
								<script type="text/javascript">
									genSelBoxExp("state",<%=Constant.INSTOCK_EXCEPTION_REPLY%>, "", true, "", "", "false", '');
								</script>
							</td>
						</tr>
						<tr>
							<td class="center" colspan="4">
								<input name="BtnQuery" id="queryBtn" class="u-button u-query" type="button" value="查 询" onclick="__extQuery__(1);" />
							</td>
						</tr>
					</table>
				</div>
			</div>
		</div>
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	</form>
</body>
</html>
