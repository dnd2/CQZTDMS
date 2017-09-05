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
<title>配件领用审核</title>
<script type="text/javascript" >
$(function(){
	__extQuery__(1);
});
var myPage;

var url = "<%=contextPath%>/parts/salesManager/partResaleReceiveManager/partResRecChkAction/partResRecSearch.json";
			
var title = null;

var columns = [
			{header: "序号", dataIndex: 'RETAIL_ID', renderer:getIndex,align:'center'},
			{id:'action',header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink ,align:'center'},
			{header: "领用单号", dataIndex: 'RETAIL_CODE', align:'center'},
			{header: "类型", dataIndex: 'CHG_TYPE', align:'center',renderer:getItemValue},
			{header: "制单单位", dataIndex: 'SORG_CNAME', align:'center'},
			{header: "仓库", dataIndex: 'WH_CNAME', align:'center'},
			{header: "制单人", dataIndex: 'NAME', align:'center'},
			{header: "制单日期", dataIndex: 'CREATE_DATE', align:'center'},
			{header: "制单金额(元)", dataIndex: 'AMOUNTS', style:'text-align: right;'},
			{header: "状态", dataIndex: 'STATE', align:'center',renderer:getItemValue}
	      ];

//设置超链接
function myLink(value,meta,record)
{
	var changeId = record.data.RETAIL_ID;
	var state = record.data.STATE;
	var str = "";
	var saveState = <%=Constant.PART_RESALE_RECEIVE_ORDER_TYPE_01 %>;
	var cmmtState = <%=Constant.PART_RESALE_RECEIVE_ORDER_TYPE_02 %>;
	if(cmmtState == state)
	{
		str = "<a href=\"#\" onclick='viewDetail(\""+changeId+"\")'>[查看]</a>&nbsp;<a href=\"#\" onclick='moifyPg(\""+changeId+"\")'>[审核]</a>&nbsp;<a href=\"#\" onclick='openPtPage(\""+changeId+"\")'>[打印订单]</a>";
	}
	else
	{
		str = "<a href=\"#\" onclick='viewDetail(\""+changeId+"\")'>[查看]</a>";
	}
	return String.format(str);
}

	//查看
// 	function viewDetail(parms){
// 		btnDisable();
// 		var optionType = "view";
<%-- 		var actionURL = "<%=contextPath%>/parts/salesManager/partResaleReceiveManager/partResRecChkAction/partRecInit.do"; --%>
// 		document.getElementById("actionURL").value = actionURL;
<%-- 		document.fm.action="<%=contextPath%>/parts/salesManager/partResaleReceiveManager/partResRecChkAction/viewOrderDeatilInint.do?changeId=" + parms +"&optionType=" + optionType; --%>
// 		document.fm.target="_self";
// 		document.fm.submit();
// 	}
	
function viewDetail(parms) {
    btnDisable();
    var optionType = "view";
    var actionURL = "<%=contextPath%>/parts/salesManager/partResaleReceiveManager/partResRecAction/partRecInit.do";
  	var orderType = document.getElementById("orderType").value;
  	OpenHtmlWindow("<%=contextPath%>/parts/salesManager/partResaleReceiveManager/partResRecAction/viewOrderDeatilInint.do?changeId=" + parms + "&optionType=" + optionType + "&actionURL=" + actionURL + "&orderType=" + orderType, 800, 500);
}

//修改
function moifyPg(parms){
	btnDisable();
	var optionType = "modify";
	var actionURL = "<%=contextPath%>/parts/salesManager/partResaleReceiveManager/partResRecChkAction/partRecInit.do";
	document.getElementById("actionURL").value = actionURL;
	document.fm.action="<%=contextPath%>/parts/salesManager/partResaleReceiveManager/partResRecChkAction/viewOrderDeatilInint.do?changeId=" + parms +"&optionType=" + optionType;
	document.fm.target="_self";
	document.fm.submit();
}


//打印页面
function openPtPage(parms) {
	var optionType = "print";
	var pageType = "rec";
	var orderType = document.getElementById("orderType").value;
	var parentOrgId = document.getElementById("parentOrgId").value;
	window.open("<%=contextPath%>/parts/salesManager/partResaleReceiveManager/partResRecAction/viewOrderDeatilInint.do?changeId=" + parms + "&optionType=" + optionType + "&pageType=" + pageType + "&orderType=" + orderType + "&parentOrgId=" + parentOrgId, "", "toolbar,menubar,scrollbars,resizable,status,location,directories,copyhistory,height=600,width=500");
	//document.fm.action="<%=contextPath%>/parts/salesManager/partResaleReceiveManager/partResRecAction/viewOrderDeatilInint.do?changeId=" + parms +"&optionType=" + optionType + "&pageType=" + pageType;
	//document.fm.target="_blank";
	//document.fm.submit();
}

//下载
function exportPartStockExcel(){
	document.fm.action="<%=contextPath%>/parts/salesManager/partResaleReceiveManager/partResRecChkAction/exportSaleOrdersExcel.do";
	document.fm.target="_self";
	document.fm.submit();
}

 </script>

</head>
<body>
	<div class="wbox">
		<form method="post" name="fm" id="fm">
			<div class="navigation">
				<img src="<%=request.getContextPath()%>/img/nav.gif" /> &nbsp;当前位置：配件管理 &gt; 内部领用管理 &gt; 内部领用审核
				<input type="hidden" name="parentOrgId" id="parentOrgId" value="${parentOrgId }" />
				<input type="hidden" name="parentOrgCode" id="parentOrgCode" value="${parentOrgCode }" />
				<input type="hidden" name="companyName" id="companyName" value="${companyName }" />
				<input type="hidden" name="actionURL" id="actionURL" value="" />
				<input type="hidden" name="orderType" id="orderType" value="<%=Constant.PART_SALE_STOCK_REMOVAL_TYPE_02%>" />
			</div>
			<div class="form-panel">
				<h2>
					<img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;" />查询条件
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">领用单号：</td>
							<td>
								<input class="middle_txt" type="text" name="changeCode" id="changeCode" />
							</td>
							<td class="right">状态：</td>
							<td>
								<script type="text/javascript">
		        					genSelBoxExp("orderState",<%=Constant.PART_RESALE_RECEIVE_ORDER_TYPE%>,<%=Constant.PART_RESALE_RECEIVE_ORDER_TYPE_02%>, true, "", "onchange='__extQuery__(1)'", "false", "");
								</script>
							</td>
						</tr>
						<tr>
							<td class="right">制单日期：</td>
							<td>
								<input id="checkSDate" style="width: 80px;" class="short_txt" name="checkSDate" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" />
								<input class="time_ico" value=" " type="button" />
								至
								<input id="checkEDate" style="width: 80px;" class="short_txt" name="checkEDate" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" />
								<input class="time_ico" value=" " type="button" />
							</td>
							<td class="right">仓库：</td>
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
						</tr>
						<tr>
							<td class="center" colspan="4">
								<input class="u-button" type="button" value="查 询" name="BtnQuery" id="queryBtn" onclick="__extQuery__(1)" />
								<input class="u-button" type="reset" value="重 置" \>
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