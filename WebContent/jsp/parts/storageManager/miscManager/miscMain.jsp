<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
    String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>杂项入库</title>
<script language="javascript">
$(function() {
	__extQuery__(1);
})
var myPage;
var url = "<%=contextPath%>/parts/storageManager/miscManager/MiscManager/getMainList.json";
var title = null;
var columns = [
    {header: "序号", align: 'center', renderer: getIndex},
    {header: "操作", align: 'center', sortable: false, dataIndex: 'MISC_ORDER_ID', renderer: myLink},
    {header: "入库单号", dataIndex: 'MISC_ORDER_CODE', align: 'center'},
    {header: "入库仓库", dataIndex: 'WH_NAME', align: 'center'},
    {header: "备注", dataIndex: 'REMARK', style: 'text-align: center'},
    {header: "制单人", dataIndex: 'NAME', align: 'center'},
    {header: "入库日期", dataIndex: 'CREATE_DATE', align: 'center'}
];

//设置超链接  begin	
function myLink(value, meta, record) {
    var type = record.data.B_TYPE;
    var formatString = "<a href=\"#\" title='查看明细' onclick='view(\"" + value + "\")' >[查看]</a>";
    formatString += "<a href=\"#\" onclick='PrintView(\"" + value + "\"," + type + ")' >[打印标准杂收单]</a>";
    return String.format(formatString);
}
//查看
function view(orderId) {
    var buttonFalg = "disabled";
    OpenHtmlWindow('<%=contextPath%>/parts/storageManager/miscManager/MiscManager/MiscViewInit.do?orderId=' + orderId + '&buttonFalg=' + buttonFalg, 800, 400);
}
//新增方法
function miscAdd() {
    window.location.href = "<%=contextPath%>/parts/storageManager/miscManager/MiscManager/MiscAddInit.do";
}


function miscLink(value, meta, record) {
    var type = record.data.B_TYPE;
    return String.format("<a href=\"#\" onclick='PrintView(\"" + value + "\"," + type + ")' >[打印标准杂收单]</a>");
}

function PrintView(value, type) {
    window.open('<%=contextPath%>/parts/storageManager/miscManager/MiscManager/miscPrint.do?value=' + value + '&type=' + type);
}

function exportDtl() {
    fm.action = "<%=contextPath%>/parts/storageManager/miscManager/MiscManager/exportDtl.do";
    fm.target = "_self";
    fm.submit();
}
</script>

</head>
<body>
	<div class="wbox">
		<div class="navigation">
			<img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：配件管理&gt;配件仓库管理&gt; 杂项入库
		</div>
		<form method="post" name="fm" id="fm">
			<div class="form-panel">
				<h2>
					<img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;" />查询条件
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr id="groupId">
							<td class="right">入库单号：</td>
							<td>
								<input class="middle_txt" type="text" id="orderCode" name="orderCode" jset="para">
							</td>
							<td class="right">入库时间：</td>
							<td>
								<input id="checkSDate" style="width: 80px;" class="short_txt" name="checkSDate" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" jset="para" />
								<input class="time_ico" value=" " type="button" />
								&nbsp;至&nbsp;
								<input id="checkEDate" style="width: 80px;" class="short_txt" name="checkEDate" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" jset="para" />
								<input class="time_ico" value=" " type="button" />
							</td>
						</tr>
						<tr>
							<td class='center' colspan=4>
								<input name="queryBtn" id="queryBtn" type="button" class="u-button" onclick="__extQuery__(1);" value="查 询" />
								<input name="addBtn" id="addBtn" type="button" class="u-button" onclick="miscAdd();" value="新 增" />
								<input name="expButton" id="expButton" class="u-button" type="button" value="明细导出" onclick="exportDtl();" />
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