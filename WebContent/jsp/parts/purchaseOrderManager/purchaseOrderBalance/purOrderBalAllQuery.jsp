<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>采购订单结算汇总查询</title>
<script type="text/javascript">
$(function(){
	__extQuery__(1);
});

var myPage;
var url = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderBalanceManager/queryOrderBalanceAll.json";

var title = null;
var calculateConfig = {subTotalColumns:" |PRODUCE_STATE"};
var columns = [
    {header: "序号", width: '5%', renderer: getIndex},
    {header: "发票号", dataIndex: 'INVO_NO', align: 'center'},
    {header: "配件类型", dataIndex: 'PRODUCE_STATE', align: 'center', renderer: getItemValue},
  /*  {header: "计划价", dataIndex: 'BUY_PRICE', style: 'text-align:right'},
    {header: "采购价", dataIndex: 'BUY_PRICE1', style: 'text-align:right'},*/
    {header: "计划金额", dataIndex: 'IN_AMOUNT', style: 'text-align:right', renderer: formatCurrency},
    {header: "采购金额", dataIndex: 'BAL_AMOUNT', style: 'text-align:right', renderer: formatCurrency}
   /* {header: "财务确认日期", dataIndex: 'CK_PRICE_DATE', align: 'center', renderer: formatDate}*/
];

var len = columns.length;

//格式化日期
function formatDate(value, meta, record) {
    var output = value.substr(0, 10);
    return output;
}

function formatCurrency(num,metadata,record) {
	num = num.toString().replace(/\$|\,/g,'');
	if(isNaN(num))
		num = "0";
	sign = (num == (num = Math.abs(num)));
	num = Math.floor(num*100+0.50000000001);
	cents = num%100;
	num = Math.floor(num/100).toString();
	if(cents<10)
		cents = "0" + cents;
	for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)
		num = num.substring(0,num.length-(4*i+3))+','+
	num.substring(num.length-(4*i+3));
	return (((sign)?'':'-') + '' + num + '.' + cents);
}

//导出
function exportOrderBalAllExcel() {
    fm.action = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderBalanceManager/exportOrderBalAllExcel.do";
    fm.target = "_self";
    fm.submit();
}

function myback() {
    window.location.href = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderBalanceManager/purchaseOrderBalanceDetailQueryInit.do";
}
</script>
</head>
<body>
	<div class="wbox">
		<div class="navigation">
			<img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp; 当前位置：配件管理&gt;采购订单管理&gt; 采购结算查询
		</div>
		<form method="post" name="fm" id="fm" enctype="multipart/form-data">
			<input type="hidden" name="curPage" id="curPage" />
			<input type="hidden" name="partId" id="partId" />
			<div class="form-panel">
				<h2>
					<img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;" />查询条件
				</h2>
				<div class="form-body">
					<table class="table_query" width=100% border="0" align="center" cellpadding="1" cellspacing="1" id="allQuery">
						<tr>
							<td class="right">发票号：</td>
							<td>
								<input name="invo_num" type="text" class="middle_txt" id="invo_num" />
							</td>
							<td class="right">财务确认日期：</td>
							<td>
								<input name="beginTime" id="beginTime" value="" type="text" class="short_txt" datatype="1,is_date,10" group="beginTime,endTime" style="width: 80px;">
								<input name='button3' value=" " type='button' class='time_ico' title="点击选择时间" onclick="showcalendar(event, 'beginTime', false);" />
								至
								<input name="endTime" id="endTime" value="" type="text" class="short_txt" datatype="1,is_date,10" group="beginTime,endTime" style="width: 80px;">
								<input name='button3' value=" " type='button' class='time_ico' title="点击选择时间" />
							</td>
						</tr>
						<tr>
							<td colspan="6" class="center">
								<input name="BtnQuery" id="queryBtn" class="u-button" type="button" value="查询" onclick="__extQuery__(1);" />
								&nbsp;
								<input class="u-button" type="reset" value="重 置" />
								&nbsp;
								<input class="u-button" type="button" value="导出" onclick="exportOrderBalAllExcel();" />
								&nbsp;
								<input name="button" type="button" class="u-button" onclick="myback();" value="返回" />
							</td>
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