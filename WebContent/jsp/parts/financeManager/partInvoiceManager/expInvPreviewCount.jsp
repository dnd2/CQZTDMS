<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%
    String contextPath = request.getContextPath();
%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>资金占用详情</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script language="javascript" type="text/javascript">
	function doInit() {
		loadcalendar(); //初始化时间控件
		__extQuery__(1);
	}
</script>
<script type="text/javascript" >
$(function(){
	__extQuery__(1);
});

var myPage;
var url = "<%=contextPath%>/parts/financeManager/partInvoiceManager/partInvoiceAction/expInvCountDetail.json";
var title = null;
var columns = [
				{header: "序号", dataIndex: 'DEALER_ID', renderer:getIndex},
				{header: "订货单位", dataIndex: 'DEALER_NAME',style:'text-align:left; padding-left:8%;'},
				{header: "订单总数", dataIndex: 'ORDER_COUNT',style:'text-align:right; padding-right:4.3%;'},
				{header: "销售总金额(元)", dataIndex: 'AMOUNT',style:'text-align:right; padding-right:7%;'},
				{header: "明细总行数", dataIndex: 'LINE_COUNT',style:'text-align:right; padding-right:5%;'}
				
	      	  ];

</script> 
</head>
<body>
	<div class="wbox">
	<div class="navigation">
		<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 配件财务管理 &gt; 财务金税发票 &gt; 导出金税文本查询
	</div>
	<form name='fm' id='fm'>
		<input type="hidden" name="parentOrgId" id="parentOrgId" value="${parentOrgId }" />
		<input type="hidden" name="parentOrgCode" id="parentOrgCode" value="${parentOrgCode }" />
		<div class="form-panel">
			<h2>
				<img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;" />查询条件
			</h2>
			<div class="form-body">
				<table class="table_query">
					<tr>
						<td class="rigth">订货单位：</td>
						<td>
							<input class="middle_txt" id="dealerName" name="dealerName" type="text" value="" />
						</td>
						<td class="rigth">导出状态：</td>
						<td>
							<script type="text/javascript">
								genSelBoxExp("invoOutState", <%=Constant.PART_INVO_OUT_STATE%>, <%=Constant.PART_INVO_OUT_STATE_01%>,true, "", "onchange=__extQuery__(1)", "false", "");
							</script>
						</td>
						<td class="rigth">订单类型：</td>
						<td>
							<script type="text/javascript">
								genSelBoxExp("orderType", <%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE%>, "", true, "", "onchange=__extQuery__(1)", "false", "");
							</script>
						</td>
					</tr>
					<tr>
						<td class="rigth">出库单号：</td>
						<td>
							<input class="middle_txt" id="outCode" name="outCode" type="text" />
						</td>
						<td class="rigth">出库日期：</td>
						<td>
							<input id="checkSDate" style="width: 80px;" class="short_txt" name="checkSDate" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" />
							<input class="time_ico" onclick="showcalendar(event, 'checkSDate', false);" value=" " type="button" />
							至
							<input id="checkEDate" style="width: 80px;" class="short_txt" name="checkEDate" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" />
							<input class="time_ico" onclick="showcalendar(event, 'checkEDate', false);" value=" " type="button" />
						</td>
						<td class="rigth">出库仓库：</td>
						<td>
							<select name="whId" id="whId" class="u-select" onchange="__extQuery__(1)">
								<option value="">-请选择-</option>
								<c:if test="${WHList!=null}">
									<c:forEach items="${WHList}" var="list">
										<option value="${list.WH_ID }">${list.WH_CNAME }</option>
									</c:forEach>
								</c:if>
							</select>
						</td>
					</tr>
					<tr>
						<td colspan="6" class="center">
							<input class="u-button" type="button" value="查 询" name="BtnQuery" id="queryBtn" onclick="__extQuery__(1)" />
							<input class="u-button" type="reset" value="重 置"/>
							<input class="u-button" type="button" name="button1" value="关 闭" onclick="_hide();" />
						</td>
					</tr>
				</table>
			</div>
		</div>
		<!--分页 begin -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	</form>
	</div>
</body>
</html>