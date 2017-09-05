<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
    String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>采购退货查询</title>
<script type="text/javascript">
$(function(){
	__extQuery__(1);
});
var myPage;
var url = "<%=contextPath%>/parts/storageManager/partReturnManager/PartReturnQuery/queryPartReturnInfo.json";
var title = null;
var columns = [
    {header: "序号", align: 'center', renderer: getIndex},
    {header: "操作", id: 'action',  sortable: false, dataIndex: 'RETURN_ID', renderer: myLink, align: 'center'},
    {header: "退货单号", dataIndex: 'RETURN_CODE', align: 'center'},
    {header: "退货单位", dataIndex: 'DEALER_NAME', align: 'center'},
    {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align: center'},
    {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align: center'},
    {header: "件号", dataIndex: 'PART_CODE', style: 'text-align: center'},
    {header: "配件类型", dataIndex: 'PART_TYPE', style: 'text-align: center'},
    {header: "单位", dataIndex: 'UNIT', align: 'center'},
    {header: "制单人", dataIndex: 'NAME', align: 'center'},
    {header: "出库库房", dataIndex: 'STOCK_OUT_NAME', align: 'center'},
    {header: "退货数量", dataIndex: 'RETURN_QTY', align: 'center'},
//     {header: "计划价", dataIndex: 'SALE_PRICE3', align: 'center'},
//     {header: "计划金额", dataIndex: 'RETURN_AMOUNT', align: 'center'},
    {header: "退货日期", dataIndex: 'CREATE_DATE', align: 'center', renderer: formatDate}
];

//格式化日期
function formatDate(value, meta, record) {
    var output = value.substr(0, 10);
    return output;
}

function exportPartReturnExcel() {
    fm.action = "<%=contextPath%>/parts/storageManager/partReturnManager/PartReturnQuery/exportPartReturnExcel.do";
    fm.target = "_self";
    fm.submit();
}

//设置超链接
function myLink(value, meta, record) {
    var state = record.data.STATE;
    return String.format("<a href=\"#\" onclick='view(\"" + value + "\"," + state + ")'>[查看]</a>");
}

//查看申请详细页面
function view(value, state) {
    window.location.href = '<%=contextPath%>/parts/storageManager/partReturnManager/PartOemReturnApplyManager/queryOemReturnApplyDetailInit.do?returnId=' + value + '&state=' + state + '&pageFlag=2';
}
   
   //打印 显示
function PrintView(value,type){
      window.open('<%=contextPath%>/parts/storageManager/partReturnManager/PartReturnQuery/partProcurementReturnPrint.do?value=' + value +'&type=' + type);
}
</script>
</head>
<body>
	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 配件管理&gt;配件仓库管理 >配件退货管理&gt;采购退货查询
		</div>
		<form name="fm" id="fm" method="post">
			<input type="hidden" name="flag" value="2" />
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
							<td class="right">件号：</td>
							<td>
								<input class="middle_txt" type="text" name="PART_CODE" id="PART_CODE" />
							</td>
							<td class="right">配件编码：</td>
							<td>
								<input class="middle_txt" type="text" name="PART_OLDCODE" id="PART_OLDCODE" />
							</td>
						</tr>
						<tr>
							<td class="right">配件名称：</td>
							<td>
								<input class="middle_txt" type="text" name="PART_CNAME" id="PART_CNAME" />
							</td>
							<td class="right">退货日期：</td>
							<td colspan="3">
								<input name="startDate" id="t1" value="${old }" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2"  style="width:80px;"/>
								<input name='button3' value=" " type='button' class='time_ico' title="点击选择时间" onclick="showcalendar(event, 't1', false);" />
								至
								<input name="endDate" id="t2" value="${now }" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2"  style="width:80px;"/>
								<input name='button3' value=" " type='button' class='time_ico' title="点击选择时间" onclick="showcalendar(event, 't2', false);" />
							</td>
						</tr>
						<tr>
							<td colspan="6" class="center">
								<input name="BtnQuery" id="queryBtn" class="u-button" type="button" value="查 询" onclick="__extQuery__(1);" />
								<input name="expButton" id="expButton" class="u-button" type="button" value="导出" onclick="exportPartReturnExcel();" />
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
