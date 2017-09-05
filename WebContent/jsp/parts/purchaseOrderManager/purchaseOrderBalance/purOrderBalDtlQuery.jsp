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
<title>采购订单结算明细查询</title>
<script type="text/javascript">
$(function(){
	__extQuery__(1);
});
var myPage;
var url = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderBalanceManager/queryOrderBalanceInfo.json";

var title = null;

columns = [
    {header: "序号", width: '5%', renderer: getIndex},
    {header: "验收单号", dataIndex: 'CHECK_CODE', style: 'text-align:left'},
    {header: "入库单号", dataIndex: 'IN_CODE', style: 'text-align:left'},
    {header: "结算单号", dataIndex: 'BALANCE_CODE', style: 'text-align:left'},
    {header: "发票号", dataIndex: 'INVO_NO', align: 'center'},
    {header: "采购员", dataIndex: 'BUYER', align: 'center'},
    {header: "配件类型", dataIndex: 'PART_TYPE', align: 'center', renderer: getItemValue},
    {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
    {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
    {header: "配件件号", dataIndex: 'PART_CODE', style: 'text-align:left'},
    {header: "供应商名称", dataIndex: 'VENDER_NAME', style: 'text-align:left'},
    {header: "制造商名称", dataIndex: 'MAKER_NAME', style: 'text-align:left'},
    {header: "入库库房", dataIndex: 'WH_NAME', align: 'center'},
    {header: "入库人员", dataIndex: 'IN_NAME', align: 'center'},
    {header: "入库数量", dataIndex: 'IN_QTY', align: 'center'},
    {header: "退货数量", dataIndex: 'RETURN_QTY', align: 'center'},
    {header: "结算数量", dataIndex: 'BAL_QTY', align: 'center'},
    {header: "结算人员", dataIndex: 'NAME', align: 'center'},
    {header: "计划价", dataIndex: 'PLAN_PRICE', style: 'text-align:right'},
    {header: "计划金额", dataIndex: 'PLAN_AMOUNT', style: 'text-align:right'},
    {header: "采购价", dataIndex: 'BUY_PRICE', style: 'text-align:right'},
    {header: "采购金额", dataIndex: 'BAL_AMOUNT', style: 'text-align:right'},
    {header: "状态", dataIndex: 'STATE', align: 'center', renderer: getItemValue}
];

var len = columns.length;

//格式化日期
function formatDate(value, meta, record) {
    var output = value.substr(0, 10);
    return output;
}

//导出
function exportOrderBalExcel() {
	fm.action = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderBalanceManager/exportOrderBalExcel.do";
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
			<img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp; 当前位置：配件管理&gt;采购财务管理&gt; 采购结算查询
		</div>
		<form method="post" name="fm" id="fm" enctype="multipart/form-data">
			<input type="hidden" name="backFlag" value="1" />
			<div class="form-panel">
				<h2>
					<img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;" />查询条件
				</h2>
				<div class="form-body">
					<table class="table_query" width=100% border="0" class="center" cellpadding="1" cellspacing="1" id="dtlQuery">
						<tr>
							<td class="right">验收单号：</td>
							<td>
								<input class="middle_txt" type="text" name="CHECK_CODE" />
							</td>
							<td class="right">验收日期：</td>
							<td>
								<input name="chkBeginTime" id="t3" value="" type="text" class="middle_txt" datatype="1,is_date,10" group="t3,t4" style="width: 80px;">
								<input name='button3' value=" " type='button' class='time_ico' title="点击选择时间" />
								&nbsp;至&nbsp;
								<input name="chkEndTime" id="t4" value="" type="text" class="middle_txt" datatype="1,is_date,10" group="t3,t4" style="width: 80px;">
								<input name='button3' value=" " type='button' class='time_ico' title="点击选择时间" />
							</td>
							<td class="right">库房：</td>
							<td>
								<select id="WH_ID2" name="WH_ID2" class="u-select">
									<option value="">-请选择-</option>
									<c:forEach items="${wareHouses}" var="wareHouse">
										<option value="${wareHouse.whId }">${wareHouse.whName }</option>
									</c:forEach>
								</select>
							</td>
						</tr>

						<tr>
							<td class="right">入库单号：</td>
							<td>
								<input class="middle_txt" type="text" name="IN_CODE" />
							</td>

							<td class="right">入库日期：</td>
							<td>
								<input name="inBeginTime" id="t5" value="" type="text" class="middle_txt" datatype="1,is_date,10" group="t5,t6" style="width: 80px;">
								<input name='button3' value=" " type='button' class='time_ico' title="点击选择时间" />
								&nbsp;至&nbsp;
								<input name="inEndTime" id="t6" value="" type="text" class="middle_txt" datatype="1,is_date,10" group="t5,t6" style="width: 80px;">
								<input name='button3' value=" " type='button' class='time_ico' title="点击选择时间" />
							</td>
							<td class="right">配件种类：</td>
							<td>
								<script type="text/javascript">
				                    genSelBoxExp("PART_TYPE", <%=Constant.PART_BASE_PART_TYPES%>, "", true, "", "", "false", '');
				                </script>
							</td>
						</tr>

						<tr>
							<td class="right">结算单号：</td>
							<td>
								<input class="middle_txt" type="text" name="BALANCE_CODE1" />
							</td>
							<td class="right">结算日期：</td>
							<td>
								<input name="balBeginTime" id="balBeginTime" value="" type="text" class="middle_txt" datatype="1,is_date,10" group="balBeginTime,balEndTime" style="width: 80px;">
								<input name='button3' value=" " type='button' class='time_ico' title="点击选择时间" />
								至
								<input name="balEndTime" id="balEndTime" value="" type="text" class="middle_txt" datatype="1,is_date,10" group="balBeginTime,balEndTime" style="width: 80px;">
								<input name='button3' value=" " type='button' class='time_ico' title="点击选择时间" />
							</td>
							<td class="right">供应商：</td>
							<td>
								<input class="middle_txt" type="text" readonly="readonly" id="VENDER_NAME2" name="VENDER_NAME" />
								<input class="mark_btn" type="button" value="&hellip;" onclick="showPartVender('VENDER_NAME2','VENDER_ID2','false')" />
								<INPUT class=short_btn onclick="clearInput1();" value=清除 type=button name=clrBtn>
								<input id="VENDER_ID2" name="VENDER_ID2" type="hidden" value="">
							</td>
						</tr>

						<tr>
							<td class="right">配件编码：</td>
							<td>
								<input name="PART_OLDCODE" type="text" class="middle_txt" id="PART_OLDCODE" />
							</td>
							<td class="right">配件名称：</td>
							<td>
								<input name="PART_CNAME" type="text" class="middle_txt" id="PART_CNAME" />
							</td>
							<td class="right">配件件号：</td>
							<td>
								<input name="PART_CODE" type="text" class="middle_txt" id="PART_CODE" />
							</td>
						</tr>
						<tr>
							<td class="right">单据状态：</td>
							<td>
								<script type="text/javascript">
                    				genSelBoxExp("STATE2", <%=Constant.PART_PURCHASE_ORDERBALANCE_STATUS%>, "<%=Constant.PART_PURCHASE_ORDERBALANCE_STATUS_01%>", true, "", "", "false", '');
								</script>
							</td>
							<td class="right">发票号：</td>
							<td>
								<input name="INVO_NO" type="text" class="middle_txt" id="INVO_NO" />
							</td>
							<td class="right">财务确认日期：</td>
							<td>
								<input name="confirmBeginTime" id="confirmBeginTime" value="" type="text" class="middle_txt" datatype="1,is_date,10" group="confirmBeginTime,confirmEndTime" style="width: 80px;">
								<input name='button3' value=" " type='button' class='time_ico' title="点击选择时间" />
								至
								<input name="confirmEndTime" id="confirmEndTime" value="" type="text" class="middle_txt" datatype="1,is_date,10" group="confirmBeginTime,confirmEndTime" style="width: 80px;">
								<input name='button3' value=" " type='button' class='time_ico' title="点击选择时间" />
							</td>
						</tr>
						<tr>
							<td class="center" colspan="6">
								<input name="BtnQuery" id="queryBtn2" type="button" class="u-button" onclick="__extQuery__(1);" value="查 询" />
								<%--      &nbsp;<input name="button" type="button" class="long_btn" onclick="setCheckModel();" value="设置发票号"/>--%>
								&nbsp;
								<input class="u-button" type="reset" value="重 置" />
								&nbsp;
								<input name="button" type="button" class="u-button" onclick="exportOrderBalExcel();" value="导出" />
								&nbsp;
								<input name="button" type="button" class="u-button" onclick="myback();" value="返回" />
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