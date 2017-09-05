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
var myPage;
var url = "<%=contextPath%>/parts/storageManager/partReturnManager/PartDlrReturnApplyQuery/queryPartDlrReturnInfo.json";
var title = null;
var columns = [
    {header: "序号", align: 'center', renderer: getIndex},
    {id: 'action', header: "操作", sortable: false, dataIndex: 'RETURN_ID', renderer: myLink, align: 'center'},
    {header: "退货单号", dataIndex: 'RETURN_CODE', align: 'center'},
    {header: "退货单位", dataIndex: 'DEALER_NAME', style: 'text-align: center;'},
//    {header: "制单单位", dataIndex: 'CREATE_DEALER', align: 'center'},
//    {header: "制单人", dataIndex: 'CREATE_NAME', style: 'text-align: left;'},
    {header: "制单日期", dataIndex: 'CREATE_DATE', align: 'center', renderer: formatDate},
    {header: "退货原因", dataIndex: 'REMARK', style: 'text-align: center;'},
    {header: "提交日期", dataIndex: 'APPLY_DATE', align: 'center', renderer: formatDate},
    {header: "状态", dataIndex: 'STATE', align: 'center', renderer: getItemValue}
];
//设置超链接  begin
//设置超链接
function myLink(value, meta, record) {
    var applyDate = record.data.APPLY_DATE;
    var createOrg = record.data.CREATE_ORG;
    var creator = document.getElementById("creator").value;
    var state = record.data.STATE;
    var outQtys = record.data.OUT_QTYS;
    if (state ==<%=Constant.PART_DLR_RETURN_STATUS_02%>) {
        if (outQtys > 0) {
            return String.format("<a href=\"#\" onclick='view(\"" + value + "\"," + state + ")'>[查看]</a>");
        }
        else {
            return String.format("<a href=\"#\" onclick='view(\"" + value + "\"," + state + ")'>[查看]</a>&nbsp;");
        }
    }
    if (createOrg != creator) {
        return String.format("<a href=\"#\" onclick='view(\"" + value + "\"," + state + ")'>[查看]</a>");
    }
    if (state ==<%=Constant.PART_DLR_RETURN_STATUS_03%>) {//如果已经驳回,那么可以进行修改
        return String.format("<a href=\"#\" onclick='view(\"" + value + "\"," + state + ")'>[查看]</a>"
                + "<a href=\"#\" onclick='updateInfo(\"" + value + "\")'>[修改]</a>"
                + "<a href=\"#\" onclick='fmsubmit(\"" + value + "\")'>[提交]</a>"
                + "<a href=\"#\" onclick='confirmDisable(\"" + value + "\")'>[作废]</a>");
    }
    if (state ==<%=Constant.PART_DLR_RETURN_STATUS_01%>) {
        return String.format("<a href=\"#\" onclick='view(\"" + value + "\"," + state + ")'>[查看]</a>"
                + "<a href=\"#\" onclick='updateInfo(\"" + value + "\")'>[修改]</a>"
                + "<a href=\"#\" onclick='fmsubmit(\"" + value + "\")'>[提交]</a>"
                + "<a href=\"#\" onclick='confirmDisable(\"" + value + "\")'>[作废]</a>");
    }
    if (state ==<%=Constant.PART_DLR_RETURN_STATUS_05%> || state ==<%=Constant.PART_DLR_RETURN_STATUS_04%>) {
        if (outQtys > 0) {
            return String.format("<a href=\"#\" onclick='view(\"" + value + "\"," + state + ")'>[查看]</a>");
        }
        else {
            return String.format("<a href=\"#\" onclick='view(\"" + value + "\"," + state + ")'>[查看]</a>&nbsp;");
        }
    }
    if (state ==<%=Constant.PART_DLR_RETURN_STATUS_06%>) {
        return String.format("<a href=\"#\" onclick='view(\"" + value + "\"," + state + ")'>[查看]</a>"
                + "<a href=\"#\" onclick='printDlrReturn(\"" + value + "\")'>[打印]</a>");
    }

    return String.format("<a href=\"#\" onclick='view(\"" + value + "\"," + state + ")'>[查看]</a>");
}

//查看申请详细页面
function view(value, state) {
    OpenHtmlWindow('<%=contextPath%>/parts/storageManager/partReturnManager/PartDlrReturnApplyQuery/queryReturnApplyDetailInit.do?returnId=' + value + '&state=' + state+"&flag=1",900,400);
}

//修改
function updateInfo(value) {
    window.location.href = '<%=contextPath%>/parts/storageManager/partReturnManager/PartDlrReturnApplyQuery/updateRInfoInit.do?returnId=' + value;
}

//打印
function printDlrReturn(value) {
    window.open("<%=contextPath%>/parts/storageManager/partReturnManager/PartDlrReturnApplyQuery/opPrintHtml.do?returnId=" + value, "", "toolbar,menubar,scrollbars,resizable,status,location,directories,copyhistory,height=600,width=500");
}

//提交申请
function fmsubmit(value) {
	MyConfirm("确定提交?", function(){
        var url = '<%=contextPath%>/parts/storageManager/partReturnManager/PartDlrReturnApplyQuery/submitReturnApply.json?returnId=' + value + '&curPage=' + myPage.page;
        makeNomalFormCall(url, getResult, 'fm');
	});
}

//撤回提交的申请
function confirmReback(value, state) {
    MyConfirm("确定撤回提交的申请?", function(){
    	 var url = '<%=contextPath%>/parts/storageManager/partReturnManager/PartDlrReturnApplyQuery/rebackReturnApply.json?returnId=' + value + '&orderState=' + state + '&curPage=' + myPage.page;
         makeNomalFormCall(url, getResult, 'fm');
	});
}

//作废退货申请
function confirmDisable(value) {
    MyConfirm("确定作废退货申请?", function(){
            var url = '<%=contextPath%>/parts/storageManager/partReturnManager/PartDlrReturnApplyQuery/disableReturnApply.json?returnId=' + value + '&curPage=' + myPage.page;
            makeNomalFormCall(url, getResult, 'fm');
	});
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

//下载
function exportReturnApplyExcel() {
    fm.action = "<%=contextPath%>/parts/storageManager/partReturnManager/PartDlrReturnApplyQuery/exportReturnApplyExcel.do";
    fm.target = "_self";
    fm.submit();
}
//设置超链接 end

$(function(){
	__extQuery__(1);
});
</script>
</head>
<div class="wbox">
	<body>
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 配件管理&gt;配件仓库管理 >配件退货管理&gt;销售退货申请
		</div>
		<form name="fm" id="fm" method="post">
			<input type="hidden" id="creator" name="creator" value="${createOrg }" />
			<input type="hidden" name="state" value="<%=Constant.PART_DLR_RETURN_STATUS_01%>" />
			<div class="form-panel">
				<h2>
					<img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;" />查询条件
				</h2>
				<div class="form-body">
					<table class="table_query">
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
								<input name="startDate" id="t1" value="${old }" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" style="width:80px;">
								<input name='button3' value=" " type='button' class='time_ico' title="点击选择时间" onclick="showcalendar(event, 't1', false);" />
								&nbsp;至&nbsp;
								<input name="endDate" id="t2" value="${now }" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" style="width:80px;">
								<input name='button3' value=" " type='button' class='time_ico' title="点击选择时间" onclick="showcalendar(event, 't2', false);" />
							</td>

						</tr>
						<tr>
							<td colspan="6" class="center">
								<input name="BtnQuery" id="queryBtn" class="u-button" type="button" value="查 询" onclick="__extQuery__(1);" />
								<input class="u-button" type="button" value="新 增" onclick="addInit();" />
								<input class="u-button" type="button" value="导 出" onclick="exportReturnApplyExcel();" />
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
