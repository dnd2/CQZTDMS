<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%
    String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件出库详情-明细</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript">
$(function(){
	__extQuery__(1);
});
var myPage;
var showType = "outDetail";
var url = "<%=contextPath%>/parts/storageManager/partStoInveManager/stockQueryAction/showInOrOutDetSearch.json?showType=" + showType;
var title = null;
var columns = [
    {header: "序号", dataIndex: 'RECORD_ID', renderer: getIndex},
    {header: "销售单号", dataIndex: 'SO_CODE', style: 'text-align:left;'},
    {header: "出库单号", dataIndex: 'OUT_CODE', style: 'text-align:left;'},
    {header: "出库类型", dataIndex: 'OUT_TYPE', style: 'text-align:left;'},
    {header: "订货单位", dataIndex: 'DEALER_NAME', style: 'text-align:left;'},
    {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left;'},
    {header: "配件名称", dataIndex: 'PART_NAME', style: 'text-align:left;'},
    //{header: "件号", dataIndex: 'PART_CODE', style: 'text-align:left;'},
    {header: "出库数量", dataIndex: 'PART_NUM'},
    {header: "货位", dataIndex: 'LOC_NAME', style: 'text-align:left;'},
    {header: "批次", dataIndex: 'BATCH_NO', style: 'text-align:left;'},
    /* {header: "单价(元)", dataIndex: 'SALE_PRICE', style: 'text-align:right;'},
    {header: "金额(元)", dataIndex: 'SALE_AMOUNT', style: 'text-align:right;'}, 20170830 屏蔽*/
    {header: "出库日期", dataIndex: 'CREATE_DATE'}
];

function searchInfo() {
    var sd = document.getElementById("checkSDate").value;
    var ed = document.getElementById("checkEDate").value;
    /*if ("" == sd && "" == ed) {
        MyAlert("请先设置查询的出库日期!");
        return false;
    }*/
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
    sendAjax(url, countResult, 'fm');
}

function countResult(json) {
    if (null != json) {
        document.getElementById("detailCount").value = json.detailCount + "";
        document.getElementById("outCount").value = json.outCount + "";
    }
}

//add zhumingwei 2013-09-17
//下载
function exportPartStockExcel() {
    document.fm.action = "<%=contextPath%>/parts/storageManager/partStoInveManager/stockQueryAction/exportPartSoCodeDetailExcel.do";
    document.fm.target = "_self";
    document.fm.submit();
}
</script>
</head>
<body>
	<div class="navigation">
		<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：配件管理 &gt; 基础信息管理 &gt; 配件基础信息查询 &gt; 配件库存查询 &gt; 配件出库详情
	</div>
	<form name='fm' id='fm'>
		<input type="hidden" name="partId" id="partId" value="${partId }" />
		<input type="hidden" name="whId" id="whId" value="${whId }" />
		<input type="hidden" name="locId" id="locId" value="${locId }" />
		<input type="hidden" name="sumFlag" id="sumFlag" value="${sumFlag}" />
		<div class="form-panel">
			<h2>
				<img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;" />查询条件
			</h2>
			<div class="form-body">
				<table class="table_query">
					<tr>
						<td width="10%" class="right">出库日期：</td>
						<td width="26%">
							<input id="checkSDate" class="middle_txt" name="checkSDate" value="${sDate}" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" style="width: 80px;" />
							<input class="time_ico" onclick="showcalendar(event, 'checkSDate', false);" value=" " type="button" />
							至
							<input id="checkEDate" class="middle_txt" name="checkEDate" value="${eDate}" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" style="width: 80px;" />
							<input class="time_ico" onclick="showcalendar(event, 'checkEDate', false);" value=" " type="button" />
						</td>
						<td width="10%" class="right">销售单号：</td>
						<td width="20%">
							<input class="middle_txt" id="soCode" name="soCode" type="text" value="" />
						</td>
						<td width="10%" class="right">出库单号：</td>
						<td width="20%">
							<input class="middle_txt" id="outCode" name="outCode" type="text" value="" />
						</td>
					</tr>
					<tr>
						<td width="10%" class="right">订货单位：</td>
						<td width="20%">
							<input class="middle_txt" type="text" name="dealerName" id="dealerName" />
						</td>

						<td width="10%" class="right">明细总数：</td>
						<td width="25%">
							<input class="middle_txt" id="detailCount" name="detailCount" type="text" value="0" style="background-color: #99D775;" readonly="readonly" />
						</td>
						<td width="10%" class="right">出库总数：</td>
						<td width="20%">
							<input class="middle_txt" id="outCount" name="outCount" type="text" value="0" style="background-color: #99D775;" readonly="readonly" />
						</td>
						<td width="10%" class="right"></td>
						<td width="20%"></td>
					</tr>
					<tr>
						<td colspan="6" class="center">
							<input class="u-button" type="button" value="查 询" name="BtnQuery" id="queryBtn" onclick="searchInfo()" />
							<input class="u-button" type="reset" value="重 置">
							<input class="u-button" type="button" value="导 出" onclick="exportPartStockExcel()" />
							<input class="u-button" type="button" name="button1" value="关 闭" onclick="_hide();" />
						</td>
					</tr>
				</table>
			</div>
		</div>
		<!--分页 begin -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		<!--分页 end -->
	</form>
</body>
</html>