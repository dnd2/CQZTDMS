<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%
    String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>资金占用详情</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script language="javascript" type="text/javascript">
$(function(){
	__extQuery__(1);
});
var myPage;
var url = "<%=contextPath%>/parts/financeManager/dealerAccQueryManager/partDealerAccQueryAction/partDealerKyDetail2.json?query=1";
var title = null;
var columns = [
    {header: "序号", dataIndex: 'RECORD_ID', renderer: getIndex},
//    {header: "服务商名称", dataIndex: 'DEALER_NAME'},
    {header: "日期", dataIndex: 'CREATE_DATE'},
    {header: "类型", dataIndex: 'CHANGE_TYPE'},
    {header: "摘要", dataIndex: 'ABSTRACT'},
    {header: "销售单", dataIndex: 'ORDER_CODE'},
    {header: "入账金额", dataIndex: 'R_AMOUNT', style: 'text-align:right; padding-right:2%;'},
    {header: "出账金额", dataIndex: 'C_AMOUNT', style: 'text-align:right; padding-right:2%;'},
    {header: "余额", dataIndex: 'BALANCE', style: 'text-align:right; padding-right:2%;'},
    {header: "开票金额", dataIndex: 'INVO_AMOUNT', style: 'text-align:right; padding-right:2%;'}
];
//导出
function exportPartPreeDetailExcel() {
    document.fm.action = "<%=contextPath%>/parts/financeManager/dealerAccQueryManager/partDealerAccQueryAction/exportDLRKYDtl.do";
    document.fm.target = "_self";
    document.fm.submit();
}
</script>
</head>
<body>
	<div class="navigation">
		<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 配件财务管理 &gt; 服务商账户查询 &gt; 服务商往来明细账查询
	</div>
	<form name='fm' id='fm'>
		<input type="hidden" name="parentOrgId" id="parentOrgId" value="${parentOrgId }" />
		<input type="hidden" name="ACCOUNT_ID" id="ACCOUNT_ID" value="${ACCOUNT_ID }" />
		<div class="form-panel">
			<h2>
					<img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;" />查询条件
			</h2>
			<div class="form-body">
				<table class="table_query">
					<tr>
						<td class="right">服务商名称：</td>
						<td>
							<input class="middle_txt" id="dealerNameSelect" name="dealerNameSelect" type="text" value="${dealerName}" disabled="disabled" readonly="readonly" />
							<input type="hidden" name="dealerId" id="dealerId" value="${dealerId }" />
						</td>
						<td class="right">日期：</td>
						<td>
							<input id="checkSDate" style="width: 80px;" class="short_txt" name="checkSDate" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" />
							<input class="time_ico" onclick="showcalendar(event, 'checkSDate', false);" value=" " type="button" />
							至
							<input id="checkEDate" style="width: 80px;" class="short_txt" name="checkEDate" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" />
							<input class="time_ico" onclick="showcalendar(event, 'checkEDate', false);" value=" " type="button" />
						</td>
						<td class="right">摘要：</td>
						<td>
							<input class="middle_txt" type="text" name="remark" id="remark" value="" />
					</tr>
					<tr>
						<td colspan="6" class="center">
							<input class="u-button" type="button" value="查 询" name="BtnQuery" id="queryBtn" onclick="__extQuery__(1)" />
							<input class="u-button" type="reset" value="重 置" />
							<input class="u-button" type="button" value="导 出" onclick="exportPartPreeDetailExcel()" />
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