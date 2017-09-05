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
<title>采购订单结算</title>
    
<script type="text/javascript">
var myPage;
var url = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderBalanceManager/queryOrderBalanceMng.json";

var title = null;

var columns = [
               {header: "序号", width: '5%', renderer: getIndex},
               {header: "结算单号", dataIndex: 'BALANCE_CODE', style: 'text-align:left'},
               {header: "结算日期", dataIndex: 'CREATE_DATE', align: 'center', renderer: formatDate},
               {header: "采购组织", dataIndex: 'PRODUCE_FAC', align: 'center', renderer: getItemValue},
               {header: "供应商名称", dataIndex: 'VENDER_NAME', style: 'text-align:left'},
               {header: "发票号", dataIndex: 'INVO_NO', align: 'center'},
               {header: "项数", dataIndex: 'XS', align: 'center'},
               {header: "入库数量", dataIndex: 'IN_QTY', align: 'center'},
               {header: "结算数量", dataIndex: 'BAL_QTY', align: 'center'},
               {header: "入库金额（含税）", dataIndex: 'IN_AMOUNT', style: 'text-align:right'},
               {header: "入库金额（不含税）", dataIndex: 'IN_AMOUNT_NOTAX', style: 'text-align:right'},
               {header: "结算金额（含税）", dataIndex: 'BAL_AMOUNT', style: 'text-align:right'},
               {header: "结算金额（不含税）", dataIndex: 'BAL_AMOUNT_NOTAX', style: 'text-align:right'},
               {header: "差异金额（不含税）", dataIndex: 'DIFF_AMOUNT', style: 'text-align:right'},
               {header: "财务确认人", dataIndex: 'NAME', style: 'text-align:right'},
               {header: "财务确认日期", dataIndex: 'CK_PRICE_DATE', style: 'text-align:right'},
               {header: "状态", dataIndex: 'STATE', align: 'center', renderer: getItemValue},
               {header: "提交人", dataIndex: 'SUB_BY', align: 'center'},
               {header: "提交日期", dataIndex: 'SUB_DATE', align: 'center'}
           ];

function query(){
	__extQuery__(1);
}

function queryDif() {
    $("#allQuery")[0].style.display = "none";
    $("#dtlQuery")[0].style.display = "";
    url = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderBalanceManager/queryOrderBalanceInfo.json";
    columns = [
        {header: "序号", width: '5%', renderer: getIndex},
        {header: "结算单号", dataIndex: 'BALANCE_CODE', style: 'text-align:left'},
        {header: "配件类型", dataIndex: 'PART_TYPE', align: 'center', renderer: getItemValue},
        {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
        {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
        {header: "配件件号", dataIndex: 'PART_CODE', style: 'text-align:left'},
        {header: "计划价", dataIndex: 'PLAN_PRICE', style: 'text-align:right'},
        {header: "采购价", dataIndex: 'BUY_PRICE', style: 'text-align:right'},
        {header: "价格差异", dataIndex: 'DIF_PRICE', style: 'text-align:right'}
    ];
    __extQuery__(1);
}
var len = columns.length;

function displayInvoice() {
    if ($("#uploadTable")[0].style.display == "none") {
        $("#uploadTable")[0].style.display = "";
    } else {
        $("#uploadTable")[0].style.display = "none";
    }
}

//格式化日期
function formatDate(value, meta, record) {
    var output = value.substr(0, 10);
    return output;
}

//清空选定供应商
function clearInput() {
    document.getElementById("VENDER_ID").value = '';
    document.getElementById("VENDER_NAME").value = '';
}

//清空选定供应商
function clearInput1() {
    document.getElementById("VENDER_ID2").value = '';
    document.getElementById("VENDER_NAME2").value = '';
}

//导出
function exportOrderBalDifExcel() {
    fm.action = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderBalanceManager/exportOrderBalDifExcel.do";
    fm.target = "_self";
    fm.submit();
}

function myback() {
    window.location.href = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderBalanceManager/purchaseOrderBalanceDetailQueryInit.do";
}

function queryAll() {
    window.location.href = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderBalanceManager/purOrderBalAllQueryInit.do";
}

function queryDtl() {
    window.location.href = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderBalanceManager/purOrderBalDtlQueryInit.do";
}

</script>
</head>
<body onload="__extQuery__(1);"> <!-- onunload='javascript:destoryPrototype()' -->
	<div class="wbox">
		<div class="navigation">
			<img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp; 当前位置：配件管理&gt;采购订单管理&gt; 采购结算查询
		</div>
		<form method="post" name="fm" id="fm" enctype="multipart/form-data">
			<div class="form-panel">
				<h2>
					<img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;" />查询条件
				</h2>
				<div class="form-body">
					<input type="hidden" name="curPage" id="curPage" />
					<input type="hidden" name="partId" id="partId" />
					<table class="table_query" width=100% border="0" class="center" cellpadding="1" cellspacing="1" id="allQuery">
						<tr>
							<td class="right">结算单号：</td>
							<td>
								<input class="middle_txt" type="text" name="BALANCE_CODE" />
							</td>
							<td class="right">结算日期：</td>
							<td width="28%">
								<input name="beginTime" id="beginTime" value="${old}" type="text" class="middle_txt" datatype="1,is_date,10" group="beginTime,endTime" style="width: 80px;">
									<input name='button3' value=" " type='button' class='time_ico' title="点击选择时间" />
									至 <input name="endTime" id="endTime" value="${now}" type="text" class="middle_txt" datatype="1,is_date,10" group="beginTime,endTime" style="width: 80px;">
										<input name='button3' value=" " type='button' class='time_ico' title="点击选择时间" />
							</td>
							<td class="right">供应商：</td>
							<td>
								<input class="middle_txt" type="text" readonly="readonly" id="VENDER_NAME" name="VENDER_NAME" />
								<input class="mark_btn" type="button" value="&hellip;" onclick="showPartVender('VENDER_NAME','VENDER_ID','false')" />
								<INPUT class="u-button" onclick="clearInput();" value=清除 type=button name=clrBtn>
									<input id="VENDER_ID" name="VENDER_ID" type="hidden" value="">
							</td>
						</tr>
						<tr>
							<td class="right">采购员：</td>
							<td>
								<select id="BALANCER_ID" name="BALANCER_ID" class="u-select">
									<option value="">-请选择-</option>
									<c:forEach items="${planerList}" var="planerList">
										<c:choose>
											<c:when test="${curUserId eq planerList.USER_ID}">
												<option selected="selected" value="${planerList.USER_ID }">${planerList.USER_NAME }</option>
											</c:when>
											<c:otherwise>
												<option value="${planerList.USER_ID }">${planerList.USER_NAME }</option>
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</select>
							</td>
							<td class="right">财务确认日期：</td>
							<td width="25%">
								<input name="confirmBeginTime" id="confirmBeginTime" value="" type="text" class="middle_txt" datatype="1,is_date,10" group="confirmBeginTime,confirmEndTime" style="width: 80px;">
									<input name='button3' value=" " type='button' class='time_ico' title="点击选择时间" />
									至 <input name="confirmEndTime" id="confirmEndTime" value="" type="text" class="middle_txt" datatype="1,is_date,10" group="confirmBeginTime,confirmEndTime" style="width: 80px;">
										<input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"/>
							</td>
							<td class="right">发票号：</td>
							<td>
								<input name="INVO_NO1" type="text" class="middle_txt" id="INVO_NO1" />
							</td>
						</tr>
						<tr>
							<td class="right">单据状态：</td>
							<td>
								<script type="text/javascript">
                        			genSelBoxExp("STATE", <%=Constant.PART_PURCHASE_ORDERBALANCE_STATUS%>, "<%=Constant.PART_PURCHASE_ORDERBALANCE_STATUS_01%>", true, "", "", "false", "");
								</script>
							</td>
							<td class="right">采购组织</td>
							<td>
								<script type="text/javascript">
									genSelBoxExp("PURCHASE_WAY",
								<%=Constant.PURCHASE_WAY%>
									, "", true, "", "", "false", "");
								</script>
							</td>
							<td class="right"></td>
							<td></td>
						</tr>
						<tr>
							<td class="center" colspan="6">
								<input name="BtnQuery" id="queryBtn" type="button" class="u-button" onclick="query();" value="查 询" />
								&nbsp;
								<input class="u-button" type="button" value="明细查询" onclick="queryDtl();" />
								&nbsp;
								<input class="u-button" type="button" value="汇总查询" onclick="queryAll();" />
								&nbsp;
								<input name="dtlQueryBtn" id="dtlQueryBtn" class="u-button" type="button" value="结算差异查询" onclick="queryDif();" />
								&nbsp;
								<input class="u-button" type="reset" value="重 置" />
						</tr>
					</table>

					<table class="table_query" width=100% border="0" class="center" cellpadding="1" cellspacing="1" id="dtlQuery" style="display: none">
						<tr>
							<td class="right">结算单号：</td>
							<td>
								<input class="middle_txt" type="text" name="BALANCE_CODE1" />
							</td>
							<td class="right">结算日期：</td>
							<td>
								<input name="balBeginTime" id="balBeginTime" value="${old}" type="text" class="middle_txt" datatype="1,is_date,10" group="balBeginTime,balEndTime" style="width: 80px;">
								<input name='button3' value=" " type='button' class='time_ico' title="点击选择时间" />
								至
								<input name="balEndTime" id="balEndTime" value="${now}" type="text" class="middle_txt" datatype="1,is_date,10" group="balBeginTime,balEndTime" style="width: 80px;">
								<input name='button3' value=" " type='button' class='time_ico' title="点击选择时间" />
							</td>
							<td class="right">配件种类：</td>
							<td>
								<script type="text/javascript">
									genSelBoxExp("PART_TYPE",
								<%=Constant.PART_BASE_PART_TYPES%>
									, "", true, "", "", "false", '');
								</script>
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
							<td class="center" colspan="6">
								<input name="BtnQuery" id="queryBtn2" type="button" class="u-button" onclick="__extQuery__(1);" value="查 询" />
								&nbsp;
								<input class="u-button" type="reset" value="重 置" />
								<%--      &nbsp;<input name="button" type="button" class="long_btn" onclick="setCheckModel();" value="设置发票号"/>--%>
								&nbsp;
								<input name="button" type="button" class="u-button" onclick="exportOrderBalDifExcel();" value="导出" />
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