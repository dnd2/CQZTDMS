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
<title>配件移库出库</title>
<script type="text/javascript">
$(function(){
	__extQuery__(1);
});
var myPage;
var url = "<%=contextPath%>/parts/storageManager/partDlrStockMvManager/partDlrStockMvAction/partMVSearch.json";
var title = null;

var columns = [
    {header: "序号", dataIndex: 'RETAIL_ID', renderer: getIndex, align: 'center'},
    {id: 'action', header: "操作", sortable: false, dataIndex: 'ID', renderer: myLink, align: 'center'},
    {header: "调拨单号", dataIndex: 'CHG_CODE', align: 'center'},
    {header: "调拨经销商", dataIndex: 'TOORG_NAME', align: 'center'},
    {header: "移出仓库", dataIndex: 'WH_NAME', align: 'center'},
    {header: "移入仓库", dataIndex: 'TOWH_NAME', align: 'center'},
    {header: "制单人", dataIndex: 'NAME', align: 'center'},
    {header: "制单日期", dataIndex: 'CREATE_DATE', align: 'center'},
    {header: "备注", dataIndex: 'REMARK', align: 'center'},
    {header: "状态", dataIndex: 'STATE', align: 'center', renderer: getItemValue}
];

//设置超链接
function myLink(value, meta, record) {
    var changeId = record.data.CHG_ID;
    var state = record.data.STATE;
    var str = "";
    if ('<%=Constant.PART_MV_STATUS_03 %>' == state) {
        str = "<a href=\"#\" onclick='viewOrder(\"" + changeId + "\")'>[查看]</a>&nbsp;<a href=\"#\" onclick='outStockOrder(\"" + changeId + "\")'>[出库]</a>";
    }
    else {
        str = "<a href=\"#\" onclick='viewOrder(\"" + changeId + "\")'>[查看]</a>&nbsp;";
    }

    return String.format(str);
}

//查看
function viewOrder(parms) {
    var optionType = "view";
    OpenHtmlWindow("<%=contextPath%>/parts/storageManager/partDlrStockMvManager/partDlrStockMvAction/viewMVDtlInint.do?chgId=" + parms + "&optionType=" + optionType, 900, 500);
}

//出库
function outStockOrder(value) {
    var optionType = "outStock";
    fm.action = "<%=contextPath%>/parts/storageManager/partDlrStockMvManager/partDlrStockMvAction/viewMVDtlInint.do?CHG_ID=" + value + "&optionType=" + optionType;
       fm.target = "_self"
       fm.submit();
   }

   // 获取经销商
function getDealer() {
   	OpenHtmlWindow(g_webAppName+'/jsp/parts/storageManager/partDlrStockMvManager/selectWhDealer.jsp',730,390);
}

   
</script>

</head>
<body>
	<div class="wbox">
		<form method="post" name="fm" id="fm">
			<div class="navigation">
				<img src="<%=request.getContextPath()%>/img/nav.gif" /> &nbsp;当前位置： 配件管理 &gt; &gt;配件仓储管理 &gt; 配件调拨出库
				<input type="hidden" name="fromOrgId" id="fromOrgId" value="${parentOrgId }" />
				<input type="hidden" name="orgCode" id="orgCode" value="${parentOrgCode }" />
				<input type="hidden" name="orgName" id="orgName" value="${companyName }" />
				<input type="hidden" name="orderState" id="orderState" value="<%=Constant.PART_MV_STATUS_03%>" />
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
								<input id="checkSDate" class="short_txt" name="checkSDate" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" />
								<input class="time_ico" onclick="showcalendar(event, 'checkSDate', false);" value=" " type="button" />
								至
								<input id="checkEDate" class="short_txt" name="checkEDate" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" />
								<input class="time_ico" onclick="showcalendar(event, 'checkEDate', false);" value=" " type="button" />
							</td>
							<td class="right">调拨经销商：</td>
							<td>
								<input type="hidden" id="FROM_WH_ID" name="fromWhId">
								<input type="hidden" id="FROM_ORG_ID" name="fromOrgId">
								<input type="hidden" id="FROM_ORG_CODE" name="fromOrgCode">
								<input type="text" class="middle_txt" id="FROM_ORG_NAME" name="fromOrgName" readonly>
								<input name='dlbtn2' id='dlbtn2' class='mini_btn' type='button' value='...' onclick="getDealer();" />
							</td>
						</tr>
						<tr>
							<td class="center" colspan="6">
								<input class="u-button" type="button" value="查 询" name="BtnQuery" id="queryBtn" onclick="__extQuery__(1)" />
								<input class="u-button" type="button" value="清 空" name="BtnQuery" id="queryBtn" onclick="reset()" />
							</td>
						</tr>
					</table>
				</div>
			</div>

			<!-- 查询条件 end -->
			<!--分页 begin -->
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
			<!--分页 end -->
		</form>
	</div>
</body>
</html>