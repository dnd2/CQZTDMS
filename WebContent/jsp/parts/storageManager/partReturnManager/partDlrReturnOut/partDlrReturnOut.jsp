<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
    String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件销售退货出库</title>
<script type="text/javascript" >
$(function(){
	__extQuery__(1);
});
var myPage;

var url = "<%=contextPath%>/parts/storageManager/partReturnManager/PartDlrReturnOutManager/queryPartDlrReturnChkInfo.json";
			
var title = null;

var columns = [
			{header: "序号", align:'center',renderer:getIndex},
			{header: "件号", dataIndex: 'PART_CODE', align:'center'},
			{header: "配件编码", dataIndex: 'PART_OLDCODE', align:'center'},
			{header: "配件名称", dataIndex: 'PART_CNAME', align:'center'},
			{header: "采购数量", dataIndex: 'BUY_QTY', align:'center'},
			{header: "申请退货数量", dataIndex: 'APPLY_QTY', align:'center'},
			{header: "审核数量", dataIndex: 'CHECK_QTY', align:'center'},
			{header: "备注", dataIndex: 'REMARK', align:'center'}
	      ];

//出库
function outPartDlrReturn(){
   	if(confirm("确定出库?")){
   		btnDisable();
		var url = '<%=contextPath%>/parts/storageManager/partReturnManager/PartDlrReturnOutManager/outPartDlrReturn.json';
		sendAjax(url,getResult,'fm');
	}   
}

function getResult(jsonObj) {
  	btnEnable();
	if(jsonObj!=null){
		var success = jsonObj.success;
		var error = jsonObj.error;
		var exceptions = jsonObj.Exception;
		if(success){
			MyAlert(success);
			window.location.href = '<%=contextPath%>/parts/storageManager/partReturnManager/PartDlrReturnOutManager/queryPartReturnApplyInit.do';
		}else if(error){
			MyAlert(error);
		    window.location.href = '<%=contextPath%>/parts/storageManager/partReturnManager/PartDlrReturnOutManager/queryPartReturnApplyInit.do';
		}else if(exceptions){
			MyAlert(exceptions.message);
		}
	}
}
   
//返回查询页面
function goback(){
	window.location.href = '<%=contextPath%>/parts/storageManager/partReturnManager/PartDlrReturnOutManager/queryPartReturnApplyInit.do';
}
</script>
</head>
<body>
	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 配件管理&gt;配件仓库管理 >配件退货管理&gt;销售退货出库&gt;出库
		</div>
		<form name="fm" id="fm" method="post">
			<input type="hidden" name="returnId" value="${po['RETURN_ID']}" />
			<input type="hidden" name="soCode" value="${po['SO_CODE']}" />
			<table class="table_query">
				<tr>
					<th colspan="4">
						<img src="<%=contextPath%>/img/nav.gif" /> 信息
					</th>
				</tr>
				<tr>
					<td class="right">退货单号：</td>
					<td>${po["RETURN_CODE"]}</td>
					<td class="right">制单单位：</td>
					<td a>${po["ORG_NAME"]}</td>
				</tr>
				<tr>
					<td cclass="right">退货单位：</td>
					<td>${po["DEALER_NAME"]}</td>
					<td cclass="right">销售单号：</td>
					<td>${po["SO_CODE"]}</td>
				</tr>
				<tr>
					<td cclass="right">销售单位：</td>
					<td>${po["SELLER_NAME"]}</td>
				</tr>
				<tr>
					<td cclass="right">退货原因：</td>
					<td>${po["REMARK"]}</td>
				</tr>
			</table>
			<table id="file" class="table_list" style="border-bottom: 1px solid #DAE0EE">
				<tr>
					<th colspan="7" align="left">
						<img class="nav" src="<%=contextPath%>/img/nav.gif" />配件信息
					</th>
				</tr>
			</table>
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
			<table border="0" class="table_query">
				<tr align="center">
					<td>
						<input class="normal_btn" type="button" name="saveBtn" id="saveBtn" value="出库" onclick="outPartDlrReturn();" />
						&nbsp;
						<input class="normal_btn" type="button" value="返 回" onclick="javascript:goback();" />
						&nbsp;
					</td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>
