<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%
    String contextPath = request.getContextPath();
%>
<title>采购入库单明细查询</title>
<script type="text/javascript">

var myPage;

var url = "<%=contextPath%>/parts/salesManager/partInStockQueManager/InStockDetailQueAction/query.json";

var title = null;

// 总入库数量，总入库金额
var qtySum = 0; var amountSum = 0;

var columns = [
    {header: "序号", dataIndex: '', renderer: getIndex, align: 'center'},
//		{header: "采购订单号", dataIndex: 'ORDER_CODE', align: 'center'},
//		{header: "订单类型", dataIndex: 'ORDER_TYPE', align: 'center', renderer: getItemValue},
    {header: "发运单号", dataIndex: 'TRANS_CODE', style: 'text-align: center'},
    {header: "入库单号", dataIndex: 'IN_CODE', style: 'text-align: center'},
    {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align: center'},
    {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align: center'},
    //{header: "配件件号", dataIndex: 'PART_CODE', style: 'text-align:left'},
    {header: "单位", dataIndex: 'UNIT', align: 'center'},
    {header: "入库数量", dataIndex: 'IN_QTY', align: 'center', renderer: getCount},
    {header: "采购单价", dataIndex: 'BUY_PRICE', style: 'text-align:right'},
    {header: "采购金额", dataIndex: 'AMOUNT', style: 'text-align: right', renderer: getCount},
    {header: "入库日期", dataIndex: 'CREATE_DATE', align: 'center'},
    {header: "备注", dataIndex: 'REMARK', align: 'center'}
];

function getCount(value, meta, record){
	var dataIndex = this.dataIndex;
	if(!value){
		value = 0;
		return value;
	}
	if(dataIndex == "IN_QTY"){
		// 入库数量
		qtySum = accAdd(qtySum, parseInt(value));
		$('#qtySum').val(qtySum);
	}else if(dataIndex == "AMOUNT"){
		// 采购金额
		amountSum = accAdd(amountSum, parseFloat(value.replace(/,/g, '')));
		$('#amountSum').val(amountSum);
	}
	return value;
}

// 加法运算，js浮点计算有时会丢失精度
function accAdd(arg1,arg2){
    var r1,r2,m;
    try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}
    try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}
    m=Math.pow(10,Math.max(r1,r2));
    return (parseInt(arg1*m)+parseInt(arg2*m))/m;
}

$(function () {
    __extQuery__(1);
    $('#queryBtn').on('click', function () {
    	qtySum = 0; amountSum = 0;
        __extQuery__(1);
    })
    $( '#exportBtn').on('click', function () {
        fm.action = "<%=contextPath%>/parts/salesManager/partInStockQueManager/InStockDetailQueAction/exportExcel.do";
        fm.submit();
    })
});
</script>

</head>
<style type="text/css">
.mystyle {
	background-color: #F3F4F8;
	border: none;
	color: red;
}
</style>
<body>
	<div class="wbox">
		<form name="fm" id="fm" method="post" enctype="multipart/form-data">
			<div class="navigation">
				<img src="<%=request.getContextPath()%>/img/nav.gif" /> &nbsp;当前位置： 配件采购管理&gt;接收入库明细查询
			</div>
			<div class="form-panel">
				<h2>
					<img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;" />查询条件
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">入库单号：</td>
							<td>
								<input class="middle_txt" type="text" name="inCode" id="inCode" />
							</td>
							<td class="right">发运单号：</td>
							<td>
								<input class="middle_txt" type="text" name="transCode" id="transCode" />
							</td>
							<td class="right">订货单号：</td>
							<td>
								<input class="middle_txt" type="text" name="orderCode" id="orderCode" />
							</td>
						</tr>
						<tr>
							<td class="right">配件编码：</td>
							<td>
								<input class="middle_txt" type="text" name="partOldcode" id="partOldcode" />
							</td>
							<td class="right">配件名称：</td>
							<td>
								<input class="middle_txt" type="text" name="partCname" id="partCname" />
							</td>
							<td class="right">入库日期：</td>
							<td>
								<input id="startDate" style="width: 80px;" class="short_txt" name="startDate" datatype="1,is_date,10" maxlength="10" value="${old}" group="startDate,endDate" />
								<input class="time_ico" value=" " onclick="showcalendar(event, 'startDate', false);" value="?" type="button" />
								至
								<input id="endDate" style="width: 80px;" class="short_txt" name="endDate" datatype="1,is_date,10" value="${now}" maxlength="10" group="startDate,endDate" />
								<input class="time_ico" value=" " onclick="showcalendar(event, 'endDate', false);" value="?" type="button" />
							</td>
						</tr>
						<tr>
							<td class="center" colspan="6">
								<input class="u-button u-submit" type="button" value="查 询" name="BtnQuery" id="queryBtn" />
								<%--<input class="normal_btn" type="button" value="导 出" name="BtnQuery" id="exportBtn"/>--%>
							</td>
						</tr>
						<tr>
							<td class="center" colspan="6">
								总入库数量：
								<input type="text" name="qtySum" id="qtySum" value="0" readonly="readonly" class="mystyle" />
								总入库金额：
								<input type="text" name="amountSum" id="amountSum" value="0" readonly="readonly" class="mystyle" />
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