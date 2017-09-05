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
<title>入库单查询</title>
<script type="text/javascript" >
$(function(){
	__extQuery__(1);
});
var myPage;

var url = "<%=contextPath%>/parts/salesManager/partInStockQueManager/inStockQueAction/inStockQueSearch.json";
			
var title = null;

var columns = [
			{header: "序号", dataIndex: 'IN_ID', renderer:getIndex,align:'center'},
               {id:'action',header: "操作",sortable: false,dataIndex: 'IN_ID',renderer:myLink ,align:'center'},
			{header: "入库单号", dataIndex: 'IN_CODE', align:'center'},
			{header: "订货单号", dataIndex: 'ORDER_CODE', align:'center'},
			{header: "订购单位", dataIndex: 'DEALER_NAME', style: 'text-align: left;'},
			{header: "销售单位", dataIndex: 'SELLER_NAME', style: 'text-align: left;'},
			{header: "制单人", dataIndex: 'NAME', style: 'text-align: left;'},
			{header: "入库日期", dataIndex: 'SALE_DATE', align:'center'},
			{header: "订单类型", dataIndex: 'ORDER_TYPE', align:'center',renderer:getItemValue},
// 			{header: "状态", dataIndex: 'STATE', align:'center',renderer:getItemValue},
			{header: "备注", dataIndex: 'REMARK2', align:'center'}
	      ];

//设置超链接
function myLink(value,meta,record)
{
	var inId = record.data.IN_ID;
	var str = "<a href=\"#\" onclick='viewDetail(\""+inId+"\")'>[查看]</a>";
	return String.format(str);
}

//查看
function viewDetail(parms){
	document.getElementById("inId").value = parms;
	btnDisable();
	document.fm.action="<%=contextPath%>/parts/salesManager/partInStockQueManager/inStockQueAction/viewDeatilInit.do";
	document.fm.target="_self";
	document.fm.submit();
}

//下载
function exportPartStockExcel(){
	document.fm.action="<%=contextPath%>/parts/salesManager/partInStockQueManager/inStockQueAction/exportInStockExcel.do";
	document.fm.target="_self";
	document.fm.submit();
}
</script>
</head>
<body>
	<div class="wbox">
		<form name="fm" id="fm" method="post" enctype="multipart/form-data">
			<div class="navigation">
				<img src="<%=request.getContextPath()%>/img/nav.gif" /> &nbsp;当前位置：配件管理&gt 配件采购管理&gt;采购入库单查询
				<input type="hidden" name="parentOrgId" id="parentOrgId" value="${parentOrgId }" />
				<input type="hidden" name="parentOrgCode" id="parentOrgCode" value="${parentOrgCode }" />
				<input type="hidden" name="companyName" id="companyName" value="${companyName }" />
				<input type="hidden" name="userRole" id="userRole" value="${userRole }" />
				<input type="hidden" name="inId" id="inId" value="" />
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
							<td class="right">订购单位：</td>
							<td>
								<input class="middle_txt" type="text" name="dealerName" id="dealerName" />
							</td>
							<td class="right">销售单位：</td>
							<td>
								<input class="middle_txt" type="text" name="sellerName" id="sellerName" />
							</td>
						</tr>
						<tr>
							<td class="right">订货单号：</td>
							<td>
								<input class="middle_txt" type="text" name="orderCode" id="orderCode" />
							</td>
							<td class="right">入库日期：</td>
							<td>
								<input id="checkSDate" class="short_txt" name="checkSDate" datatype="1,is_date,10" maxlength="10" value="${old}" group="checkSDate,checkEDate" />
								<input class="time_ico" onclick="showcalendar(event, 'checkSDate', false);" value=" " type="button" />
								至
								<input id="checkEDate" class="short_txt" name="checkEDate" datatype="1,is_date,10" value="${now}" maxlength="10" group="checkSDate,checkEDate" />
								<input class="time_ico" onclick="showcalendar(event, 'checkEDate', false);" value=" " type="button" />
							</td>
							<td class="right">订单类型：</td>
							<td>
								<script type="text/javascript">
									genSelBoxExp("orderType", <%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE%>, "", true, "", "", "false", "");
								</script>
							</td>
						</tr>
						<tr>
							<td class="center" colspan="6">
								<input class="u-button" type="button" value="查 询" name="BtnQuery" id="queryBtn" onclick="__extQuery__(1)" />
								<input class="u-button" type="button" value="导 出" onclick="exportPartStockExcel()" />
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