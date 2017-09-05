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
<title>配件销售退货申请查询</title>
<script type="text/javascript">
$(function(){
	__extQuery__(1);
});  

var myPage;

var url = "<%=contextPath%>/parts/storageManager/partReturnManager/PartDlrReturnApplyQuery/queryPartDlrReturnBackInfo.json";

var title = null;

var columns = [
    {header: "序号", align: 'center', renderer: getIndex},
    {id: 'action', header: "操作", sortable: false, dataIndex: 'RETURN_ID', renderer: myLink, align: 'center'},
    {header: "退货单号", dataIndex: 'RETURN_CODE', align: 'center'},
    {header: "退货单位", dataIndex: 'DEALER_NAME', style: 'text-align: left;'},
    {header: "制单单位", dataIndex: 'CREATE_DEALER', align: 'center'},
    {header: "制单人", dataIndex: 'CREATE_NAME', style: 'text-align: left;'},
    {header: "制单日期", dataIndex: 'CREATE_DATE', align: 'center', renderer: formatDate},
    {header: "退货原因", dataIndex: 'REMARK', style: 'text-align: left;'},
    {header: "状态", dataIndex: 'STATE', align: 'center', renderer: getItemValue},
    {header: "提交日期", dataIndex: 'APPLY_DATE', align: 'center', renderer: formatDate}
];

//设置超链接  begin
//设置超链接
function myLink(value, meta, record) {
    var state = record.data.STATE;
    if (state ==<%=Constant.PART_DLR_RETURN_STATUS_04%>) {
        return String.format("<a href=\"#\" onclick='view(\"" + value + "\"," + state + ")'>[查看]</a>&nbsp;<a href=\"#\" onclick='checkApply(\"" + value + "\"," + state + ")'>[回运]</a>");
    }
}

//查看申请详细页面
function view(value, state) {
    OpenHtmlWindow('<%=contextPath%>/parts/storageManager/partReturnManager/PartDlrReturnApplyQuery/queryReturnApplyDetailInit.do?returnId=' + value + '&state=' + state+"&flag=1",900,400);
}

// 回运
function checkApply(value) {
    window.location.href = '<%=contextPath%>/parts/storageManager/partReturnManager/PartDlrReturnChkManager/returnBackDetailInit.do?returnId=' + value + '&chkLevel=all';
}

//格式化日期
function formatDate(value, meta, record) {
    var output = value.substr(0, 10);
    return output;
}

function addInit() {
    window.location.href = '<%=contextPath%>/parts/storageManager/partReturnManager/PartDlrReturnApplyQuery/addInit.do';
}

function getResult(jsonObj) {
    if (jsonObj != null) {
        var success = jsonObj.success;
        MyAlert(success);
        __extQuery__(jsonObj.curPage);
    }
}

</script>

</head>
<body>
	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" /> &nbsp;当前位置： 配件管理&gt;配件仓库管理 >配件退货管理&gt;销售退货回运
		</div>
		<form name="fm" id="fm" method="post">
			<input type="hidden" id="creator" name="creator" value="${createOrg }" />
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

							<td class="right">退货单位：</td>
							<td>
								<input class="middle_txt" type="text" name="DEALER_NAME" id="DEALER_NAME" />
							</td>

							<td class="right">制单日期：</td>
							<td>
								<input name="startDate" id="t1" value="${old }" type="text" style="width: 80px;" class="short_txt" datatype="1,is_date,10" group="t1,t2">
								<input name='button3' value=" " type='button' class='time_ico' title="点击选择时间" onclick="showcalendar(event, 't1', false);" />
								&nbsp;至&nbsp;
								<input name="endDate" id="t2" value="${now }" type="text" style="width: 80px;" class="short_txt" datatype="1,is_date,10" group="t1,t2">
								<input name='button3' value=" " type='button' class='time_ico' title="点击选择时间" onclick="showcalendar(event, 't2', false);" />
							</td>

						</tr>
						<tr>
							<td colspan="6" class="center">
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
