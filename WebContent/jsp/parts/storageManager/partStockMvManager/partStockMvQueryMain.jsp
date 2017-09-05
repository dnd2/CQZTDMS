<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%
    String contextPath = request.getContextPath();
%>
<title>配件调拨查询</title>
<script language="javascript" type="text/javascript">
$(function(){
    __extQuery__(1);
});

var myPage;
var url = "<%=contextPath%>/parts/storageManager/partStockMvManager/partStockMvAction/partMVSearch.json";
var title = null;

var columns = [
    {header: "序号", dataIndex: 'RETAIL_ID', renderer: getIndex, align: 'center'},
    {id: 'action', header: "操作", sortable: false, dataIndex: 'ID', renderer: myLink, align: 'center'},
    {header: "移库单号", dataIndex: 'CHG_CODE', align: 'center'},
    {header: "移库单位", dataIndex: 'ORG_NAME', align: 'center'},
    {header: "备注", dataIndex: 'REMARK', align: 'center'},
    {header: "移出仓库", dataIndex: 'WH_NAME', align: 'center'},
    {header: "移入仓库", dataIndex: 'TOWH_NAME', align: 'center'},
    {header: "制单人", dataIndex: 'NAME', align: 'center'},
    {header: "制单日期", dataIndex: 'CREATE_DATE', align: 'center'},
    {header: "审核意见", dataIndex: 'CHECK_REMARK', align: 'center'},
    {header: "状态", dataIndex: 'STATE', align: 'center', renderer: getItemValue}
];

//设置超链接
function myLink(value, meta, record) {
    var changeId = record.data.CHG_ID;
    var state = record.data.STATE;
    var str = "";
<%--     if ('<%=Constant.PART_MV_STATUS_01 %>' == state) { --%>
//         str = "<a href=\"#\" onclick='viewOrder(\"" + changeId + "\")'>[查看]</a>&nbsp;<a href=\"#\" onclick='modOrder(\"" + changeId + "\")'>[修改]</a>&nbsp;<a href=\"#\" onclick='commitOrder(\"" + changeId + "\")'>[提交]</a>&nbsp;<a href=\"#\" onclick='delOrder(\"" + changeId + "\")'>[作废]</a>&nbsp;<a href=\"#\" onclick='printOrder(\"" + changeId + "\")'>[打印]</a>"
//     }
//     else {
        str = "<a href=\"#\" onclick='viewOrder(\"" + changeId + "\")'>[查看]</a>&nbsp;<a href=\"#\" onclick='printOrder(\"" + changeId + "\")'>[打印]</a>";
//     }

    return String.format(str);
}

//提交
function commitOrder(value) {
    MyConfirm("确定提交?", commitOrderAction, [value]);
}

function commitOrderAction(value) {
    btnDisable();
    var url = "<%=contextPath%>/parts/storageManager/partStockMvManager/partStockMvAction/submitMVOrder.json?chgId=" + value + "&curPage=" + myPage.page;
    sendAjax(url, getResult, 'fm');
}

//删除
function delOrder(value) {
    MyConfirm("确定删除?", delOrderAction, [value]);
}

function delOrderAction(value) {
    btnDisable();
    var url = "<%=contextPath%>/parts/storageManager/partStockMvManager/partStockMvAction/deleteMVorder.json?chgId=" + value + "&curPage=" + myPage.page;
    sendAjax(url, getResult, 'fm');
}
function getResult(json) {
	btnEnable();
    if (null != json) {
        if (json.error != null) {
            MyAlert(json.error);
            __extQuery__(json.curPage);
        } else if (json.success == "success") {
            MyAlert("操作成功!");
            __extQuery__(json.curPage);
        } else {
            MyAlert("操作失败，请联系管理员!");
        }
    }
}

//查看
function viewOrder(parms) {
    var optionType = "view";
    OpenHtmlWindow("<%=contextPath%>/parts/storageManager/partStockMvManager/partStockMvAction/viewMVDtlInint.do?chgId=" + parms + "&optionType=" + optionType, 1000, 500);
}

//修改
function modOrder(value) {
    var optionType = "modify";
    fm.action = "<%=contextPath%>/parts/storageManager/partStockMvManager/partStockMvAction/viewMVDtlInint.do?CHG_ID=" + value + "&optionType=" + optionType;
    fm.target = "_self"
    fm.submit();
}
//打印页面
function printOrder(parms) {
    var optionType = "print";
    document.fm.action = "<%=contextPath%>/parts/storageManager/partStockMvManager/partStockMvAction/viewMVDtlInint.do?CHG_ID=" + parms + "&optionType=" + optionType;
    document.fm.target = "_blank";
    document.fm.submit();
}

//新增移库
function addMv() {
    document.fm.action = "<%=contextPath%>/parts/storageManager/partStockMvManager/partStockMvAction/partMvAddInit.do";
    document.fm.target = "_self";
    document.fm.submit();
    btnDisable();
}

//下载
function exportPartStockExcel() {
    document.fm.action = "<%=contextPath%>/parts/salesManager/partResaleReceiveManager/partResRecAction/exportSaleOrdersExcel.do";
    document.fm.target = "_self";
    document.fm.submit();
}

</script>
</head>
<body>
	<div class="wbox">
		<form method="post" name="fm" id="fm">
			<div class="navigation">
				<img src="<%=request.getContextPath()%>/img/nav.gif" /> &nbsp;当前位置： 配件管理 &gt; &gt;配件仓储管理 &gt; 配件调拨查询
				<input type="hidden" name="orgId" id="orgId" value="${parentOrgId }" />
				<input type="hidden" name="orgCode" id="orgCode" value="${parentOrgCode }" />
				<input type="hidden" name="orgName" id="orgName" value="${companyName }" />
				<input type="hidden" name="actionURL" id="actionURL" value="" />
			</div>
			<div class="form-panel">
				<h2>
					<img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;" />查询条件
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">移库单号：</td>
							<td>
								<input class="middle_txt" type="text" name="changeCode" id="changeCode" />
							</td>
							<td class="right">制单日期：</td>
							<td>
								<input id="createSDate" datatype="1,is_date,10" class="short_txt" style="width: 80px;" type="text" />
								<input class="time_ico" onclick="showcalendar(event, 'createSDate', false);" value=" " type="button" />
								至
								<input id="createEDate" datatype="1,is_date,10" class="short_txt" style="width: 80px;" type="text" />
								<input class="time_ico" onclick="showcalendar(event, 'createEDate', false);" value=" " type="button" />
							</td>
							<td class="right">状态：</td>
							<td>
								<script type="text/javascript">
									genSelBoxExp("orderState", <%=Constant.PART_MV_STATUS%> , "", true, "u-select", "", "false", "");
								</script>
							</td>
						</tr>
						<tr>
							<td class="right">移出仓库：</td>
							<td>
								<select name="whId" id="whId" class="u-select">
									<option value="">-请选择-</option>
									<c:if test="${WHList!=null}">
										<c:forEach items="${WHList}" var="list">
											<option value="${list.WH_ID }">${list.WH_CNAME }</option>
										</c:forEach>
									</c:if>
								</select>
							</td>
							<td class="right">移入仓库：</td>
							<td colspan="3">
								<select name="toWhId" id="toWhId" class="u-select">
									<option value="">-请选择-</option>
									<c:if test="${WHList2!=null}">
										<c:forEach items="${WHList2}" var="list2">
											<option value="${list2.WH_ID }">${list2.WH_CNAME }</option>
										</c:forEach>
									</c:if>
								</select>
							</td>
						</tr>
						<tr>
							<td class="center" colspan="6">
								<input class="u-button" type="button" value="查 询" name="queryBtn" id="queryBtn" onclick="__extQuery__(1)" />
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
	</div>
</body>
</html>