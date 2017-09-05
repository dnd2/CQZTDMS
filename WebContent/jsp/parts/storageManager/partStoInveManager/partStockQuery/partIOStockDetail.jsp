<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%
    String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件出入库详情</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript">
$(function(){
	searchInfo();
});
var myPage;
var showType = "rcDetail";
var url = "<%=contextPath%>/parts/storageManager/partStoInveManager/stockQueryAction/showInOrOutDetSearch.json?showType=" + showType;
var title = null;
var columns = [
    {header: "序号", dataIndex: '', renderer: getIndex},
    {header: "日期", dataIndex: 'CREATE_DATE'},
    {header: "类型", dataIndex: 'IN_TYPE', style: 'text-align:left;'},
    {header: "单号", dataIndex: 'CODE', style: 'text-align:left;'},
    {header: "服务站编码", dataIndex: 'DEALER_CODE', style: 'text-align:left;'},
    {header: "服务站名称", dataIndex: 'DEALER_NAME', style: 'text-align:left;'},
    {header: "入库数量", dataIndex: 'IN_QTY'},
    {header: "出库数量", dataIndex: 'OUT_QTY'},
    {header: "结存数量", dataIndex: 'ITEM_QTY'}
];

function searchInfo() {
    var sd = document.getElementById("checkSDate").value;
    var ed = document.getElementById("checkEDate").value;
    countInfoQuery();
    __extQuery__(1);
}

//导出
function exportPartPreeDetailExcel() {
    document.fm.action = "<%=contextPath%>/parts/storageManager/partStoInveManager/stockQueryAction/exportPDDetExcel.do";
    document.fm.target = "_self";
    document.fm.submit();
}

function countInfoQuery() {
    var url = "<%=contextPath%>/parts/storageManager/partStoInveManager/stockQueryAction/countInOrOutQuery.json?showType=" + showType;
    makeNomalFormCall(url, countResult, 'fm');
}

function countResult(json) {
    if (null != json) {
        document.getElementById("OUT_QTY").value = json.OUT_QTY + "";
        document.getElementById("IN_QTY").value = json.IN_QTY + "";
    }
}

//下载
function exportPartStockExcel() {
    document.fm.action = "<%=contextPath%>/parts/storageManager/partStoInveManager/stockQueryAction/exportPartINDetailExcel.do";
    document.fm.target = "_self";
    document.fm.submit();
}
</script>
</head>
<body>
	<div class="navigation">
		<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 配件管理 &gt;基础信息管理 &gt; 配件基础信息查询 &gt; 配件库存查询 &gt; 配件出入库详情
	</div>
	<form name='fm' id='fm'>
		<input type="hidden" name="partId" id="partId" value="${partId }" />
		<input type="hidden" name="whId" id="whId" value="${whId }" />
		<input type="hidden" name="locId" id="locId" value="${locId }" />
		<input type="hidden" name="rcFlag" id="rcFlag" value="${rcFlag }" />
		<div class="form-panel">
			<h2>
				<img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;" />查询条件
			</h2>
			<div class="form-body">
				<table class="table_query">
					<tr>
						<td class="center">日期：</td>
						<td>
							<input id="checkSDate" class="middle_txt" name="checkSDate" value="${sDate}" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" style="width: 80px;" />
							<input class="time_ico" onclick="showcalendar(event, 'checkSDate', false);" value=" " type="button" />
							至
							<input id="checkEDate" class="middle_txt" name="checkEDate" value="${eDate}" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" style="width: 80px;" />
							<input class="time_ico" onclick="showcalendar(event, 'checkEDate', false);" value=" " type="button" />
						</td>
						<td class="left">业务单号：</td>
						<td>
							<input id="orderCode" class="middle_txt" name="orderCode" value="" />
						</td>
					</tr>
					<tr>
						<td class="center">入库总数：</td>
						<td>
							<input id="IN_QTY" name="IN_QTY" type="text" value="0" readonly="readonly" style="border: none; background-color: #F3F4F8" />
						</td>
						<td class="left">出库总数：</td>
						<td>
							<input id="OUT_QTY" name="OUT_QTY" type="text" value="0" readonly="readonly" style="border: none; background-color: #F3F4F8" />
					</tr>
					<tr>
						<td colspan="4" class="center">
							<input class="u-button" type="button" value="查 询" name="BtnQuery" id="queryBtn" onclick="searchInfo()" />
							<input class="u-button" type="reset" value="重 置">
							<input class="u-button" type="button" value="导 出" onclick="exportPartStockExcel()" />
							<input class="u-button" type="button" name="button1" value="关 闭" onclick="_hide();" />
						</td>
					</tr>
				</table>
			</div>
		</div>
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	</form>
</body>
</html>