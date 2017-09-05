<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<%@ page import="com.infodms.dms.po.TtReturnVehicleHeadPO"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<% String contextPath = request.getContextPath(); %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<title>退车明细审核</title>
<%
	TtReturnVehicleHeadPO trvh = (TtReturnVehicleHeadPO)request.getAttribute("trvh") ;
%>
<script type="text/javascript">
function doInit() {
	getFund();
}

function chkInfo() {
	var bFlag = false ;
	var aReqId = document.getElementsByName("reqIds") ;
	var iLen = aReqId.length ;
	for(var i=0; i<iLen; i++) {
		if(aReqId[i].checked) {
			bFlag = true ;
			break ;
		}
	}
	return bFlag ;
}

function toPass() {
	var reason = document.getElementById("reason").value ;
	//if(!chkInfo()) {
	//	MyAlert("请选择退车信息!") ;
		
	//	return false ;
	//}
	/* if(!reason.length) {
		MyAlert("请填写审核描述!") ;
		return false ;
	} */
	MyConfirm("确认通过?", passSub) ;
}

function passSub() {
	document.getElementById("passIt").disabled = true ;
	document.getElementById("ReturnIt").disabled = true ;
	var url = "<%=contextPath%>/sales/storageManage/ReturnVehicleReq/returnDtlPass.json" ;
	sendAjax(url, showResult, "fm") ;
}

function toReturn() {
	var reason = document.getElementById("reason").value ;
	//if(!chkInfo()) {
	//	MyAlert("请选择退车信息!") ;
	//	return false ;
	//}
	 if(!reason.length) {
		MyAlert("请填写审核描述!") ;
		return false ;
	} 
	MyConfirm("确认驳回?", returnSub) ;
}

function returnSub() {
	document.getElementById("passIt").disabled = true ;
	document.getElementById("ReturnIt").disabled = true ;
	var url = "<%=contextPath%>/sales/storageManage/ReturnVehicleReq/returnDtlRet.json" ;
	sendAjax(url, showResult, "fm") ;
}

function showResult(json) {
	var flag = json.flag ;
	
	if(flag == "success") {
		MyAlert("操作成功") ;
		
		history.back() ;
	}else {
		MyAlert("操作失败") ;
		document.getElementById("passIt").disabled = false ;
		document.getElementById("ReturnIt").disabled = false ;
	}
}
function getFund(){
	var dealerId=document.getElementById("dealerId").value;
	var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/showFund.json";
	makeCall(url,showFund,{dealerId:dealerId});
}
function showFund(json){
		showFundTypeList(json);//资金类型列表显示
}
//资金类型列表显示
function showFundTypeList(json){
	
	var obj = document.getElementById("fundType");
	obj.options.length = 0;
	for(var i=0;i<json.fundTypeList.length;i++){
		obj.options[i]=new Option(json.fundTypeList[i].TYPE_NAME, json.fundTypeList[i].TYPE_ID+"|"+json.fundTypeList[i].IS_USE_ORDER_ACCOUNT);
		if(json.fundTypeList[i].TYPE_ID + "" == '${trvh.accountTypeId}'){
			obj.options[i].selected = true;
		}
	}
	var fundType = document.getElementById("fundType").value;
	var fundTypes = fundType.split("|");
	getAvailableAmount(fundType);//获得账户余额
}
//获取可用余额
function getAvailableAmount(arg){
	//var areaObj = document.getElementById("area");
	//var dealerId = areaObj.value.split("|")[1];
	var fundType=arg;
	var dealerId=document.getElementById("dealerId").value;
	var fundTypes = fundType.split("|");
	var fundTypeId = fundTypes[0];
	var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/getAvailableAmount.json";
	makeCall(url,showAvailableAmount,{fundTypeId:fundTypeId,dealerId:dealerId});
}	
//显示可用余额
function showAvailableAmount(json){
	var obj = document.getElementById("span4");
	obj.innerHTML = amountFormat(parseFloat(json.returnValue, 10));
}
</script>
</head>
<body > 
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt; 库存管理 &gt; 退车明细审核(储运)</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" value="${dealerId}" id="dealerId"/>
		<input type="hidden" value="3"  id="reqType" name="reqType"/>
		<input type="hidden" value="${trvh.accountTypeId}" id="fundTypeId"/>
		<div class="form-panel">
		<h2>退车明细审核(储运)</h2>
		<div class="form-body">
			<table class="table_list">
				<tr>
					<th>退车号</th>
					<th>申请数量</th>
					<th>退车原因</th>
				</tr>
				<tr>
					<td><%=trvh.getReturnVehicleNo() %><input type="hidden" name="headId" id="headId" value="<%=trvh.getHeadId() %>"></td>
					<td><%=trvh.getApplyAmount() %></td>
					<td><%=trvh.getReason() %></td>
				</tr>
			</table>
			<br />
			<br />
			<table class="table_list">
				<tr>
					<th>VIN</th>
					<th>车型名称</th>
					<th>配置</th>
					<th>物料代码</th>
					<th>物料名称</th>
					<th>单价</th>
				</tr>
				<c:forEach var="dtl" items="${dltList }">
					<tr>
						<td>${dtl.VIN }<input type='hidden' name='reqIds' value='${dtl.REQ_ID }'/></td>
						<td>${dtl.MODEL_NAME }</td>
						<td>${dtl.PACKAGE_NAME }</td>
						<td>${dtl.MATERIAL_CODE }</td>
						<td>${dtl.MATERIAL_NAME }</td>
						<td>${dtl.SINGLE_PRICE }</td>
					</tr>
				</c:forEach>
			</table>
			<br />
			<table class="table_query">
				<tr>
		            <td class="right" width="10%">审核描述：</td>
		            <td colspan="3" width="90%">
		              <textarea name="reason" id="reason" datatype="0,is_null,100" rows='3' cols='80' ></textarea>
		            </td> 
	            </tr>
	            <tr>
					<td class="right" width="10%">
						仓库：
					</td>
					<td class="left"  colspan="3" width="90%">
						<select id="warHouseId" name="warHouseId" class="u-select">
							<c:forEach items="${houseList }" var="item">
								<option value="${item.WAREHOUSE_ID }">${item.WAREHOUSE_NAME }</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<td class="center" colspan="4">
						<input type="button"  class="u-button u-submit"  name="passIt" id="passIt" value="确 定" onclick="toPass();" />&nbsp;
					   <input type="button"  class="u-button u-reset"  name="ReturnIt" id="ReturnIt" value="驳 回" onclick="toReturn() ;"/> 
					</td>
				</tr>
			</table>
		</div>
		</div>
	</form>
</div>
</body>
</html>