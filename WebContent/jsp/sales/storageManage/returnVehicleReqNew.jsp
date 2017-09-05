<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<%@taglib uri="/jstl/cout" prefix="c" %>
<% String contextPath = request.getContextPath(); %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<title>车辆退回基地申请</title>
<script type="text/javascript">
$(document).ready(function(){
	getFund();
});
function chooseVehicle() {
	document.getElementById("showDiv").style.display = '';
	OpenHtmlWindow('<%=contextPath%>/sales/storageManage/ReturnVehicleReq/chooseVehicleInit.do',850,500);
}

function rowDelete(value) {
	 var oTr = document.getElementById("tr" + value) ;
	 oTr.parentNode.removeChild(oTr) ;
	 isAllow() ;
}

function isAllow() {
	var oApplayBtn = document.getElementById("applyRetrun") ;
	var oShowObj = document.getElementById("showTab") ;
	var iRowLen = oShowObj.rows.length ;
	if(iRowLen > 1) {
		oApplayBtn.style.display = "inline" ;
	} else {
		oApplayBtn.style.display = "none" ;
	}
}

function applyReturn() {
	var sRemark = document.getElementById("remarkTxt").value ; 
	 //检验车辆是否失效
	/* if(getCode() == 0){    
		return false;	
	} */
	if(sRemark.length == 0) {
		MyAlert("请输入退车原因!") ;
		return false ;
	}else if(sRemark.length >13){
		MyAlert("请输入字数少于13字!") ;
		return false;
	}
	MyConfirm("确认操作", toSumbit) ;
}

 function getCode(){
		var flag = 1;
	    var code = "";
	    var tb=document.getElementById("showTab");    //获取table对像
	    var rows=tb.rows;
	 	for(var i=1;i<rows.length;i++){    //--循环所有的行
	        var cells=rows[i].cells; 
	       code = code+cells[0].innerText+",";
	     } 
	 	code=code.substring(0,code.length-1);
	 	var url = "<%=contextPath%>/sales/storageManage/ReturnVehicleReq/CheckCode.json?code="+code;
		makeSameCall(url, showInfo, "fm") ;
		function showInfo(json) {
			if( json.code != "" && json.code != null ){
			 MyAlert(json.code+"车型已经失效，请联系长安铃木物料管理员解决此问题");
			 flag = 0;
		     }
         } 
		return flag;
}

function toSumbit() {
	document.getElementById("applyRetrun").style.disable = true;
	var url = "<%=contextPath%>/sales/storageManage/ReturnVehicleReq/returnVehicleApply.do" ;
	var fsm = document.getElementById("fm");
	fsm.action= url ;
	fsm.submit();
}

function getFund(){
	var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/showFund.json";
	makeCall(url,showFund,{dealerId:null});
}
function showFund(json){
		showFundTypeList(json);//资金类型列表显示
}
//资金类型列表显示
function showFundTypeList(json){
	var obj = document.getElementById("fundType");
	obj.options.length = 0;
	for(var i=0;i<json.fundTypeList.length;i++){
		if(json.fundTypeList[i].TYPE_ID==null){
			obj.options[i]=new Option(json.fundTypeList[i].TYPE_NAME, "null"+"|"+json.fundTypeList[i].IS_USE_ORDER_ACCOUNT);
		}else{
			obj.options[i]=new Option(json.fundTypeList[i].TYPE_NAME, json.fundTypeList[i].TYPE_ID+"|"+json.fundTypeList[i].IS_USE_ORDER_ACCOUNT);
		}
		//obj.options[i]=new Option(json.fundTypeList[i].TYPE_NAME, json.fundTypeList[i].TYPE_ID+"|"+json.fundTypeList[i].IS_USE_ORDER_ACCOUNT);
		if(json.fundTypeList[i].TYPE_ID + "" == '${order.fundTypeId}'){
			obj.options[i].selected = true;
		}
	}
	var fundType = document.getElementById("fundType").value;
	var fundTypes = fundType.split("|");
	getAvailableAmount(fundTypes[0]);//获得账户余额
}
//获取可用余额
function getAvailableAmount(arg){
	//var areaObj = document.getElementById("area");
	//var dealerId = areaObj.value.split("|")[1];
	var fundType = document.getElementById("fundType").value;
	var fundTypes = fundType.split("|");
	var fundTypeId = fundTypes[0];
	if(fundTypes[0]==null||"null"==fundTypes[0]||""==fundTypes[0]){
		MyAlert("ERP和DMS账户类型不匹配!!!");
		document.getElementById("applyRetrun").disabled=true;
	}else{
		document.getElementById("applyRetrun").disabled=false;
	}
	var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/UrgentOrderReport/getAvailableAmount.json";
	makeCall(url,showAvailableAmount,{fundTypeId:fundTypeId,dealerId:null});
}	
//显示可用余额
function showAvailableAmount(json){
	var obj = document.getElementById("span4");
	obj.innerHTML = amountFormat(parseFloat(json.returnValue, 10));
	document.getElementById("accountId").value = json.accountId;
}
</script>
</head>
<body> 
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt; 库存管理 &gt; 车辆退回基地申请</div>
	<div class="form-panel">
	<h2>车辆退回基地申请</h2>
	<div class="form-body">
		<form id="fm" name="fm" method="post">
			<input type="button" class="u-button" name="addVehicle" id="addVehicle" value="添加车辆" onclick="chooseVehicle();" />&nbsp;
			<div id="showDiv" style="width:100%;">
				<table class="table_query table_list" style="border:1px solid #B0C4DE"  id="showTab" border="1" cellpadding="1" bordercolor="#B0C4DE" >
					<tr>
						<th nowrap="nowrap">物料代码</th>
						<th nowrap="nowrap">物料名称</th>
						<th nowrap="nowrap">VIN</th>
						<th nowrap="nowrap">单价</th>
						<th nowrap="nowrap">位置说明</th>
						<th nowrap="nowrap">经销商</th>
						<th nowrap="nowrap">库存状态</th>
						<th nowrap="nowrap">入库日期</th>
						<th nowrap="nowrap">操作</th>
					</tr>
				</table>
			</div>
			<br />
			<br />
			<table class="table_query" width="100%">
			<tr>
					<td class="right">退车原因:</td>
					<td  colspan="3">
						<textarea id="remarkTxt" name="remarkTxt" cols="60" rows="3"  placeholder="请输入字数少于13字"></textarea><font color="red">*</font>
					</td>
				</tr>
				<!-- <tr>
					<td class="right">
						资金类型：
					</td>
					<td align="left">
						<span id="span2">
		      				<select name="fundType" onchange="getAvailableAmount(this.options[this.options.selectedIndex].value);">
		      				</select>
	      				</span>
					</td>
					 <td align = "left"><span id="span3">可用余额：</span></td>
	     			 <td align = "left"><span id="span4" class="STYLE2"></span></td>
				</tr> -->
				<tr>
					<td class="right" width="20%"></td>
					<td class="right" width="25%"></td>
					<td  class="right" width="25%">
						<input type="button" class="u-button u-submit"  name="applyRetrun" id="applyRetrun" value="申请退车" onclick="applyReturn();" />
					</td>
					<td class="right" width="35%"></td>
				</tr>
			</table>
		</form>
	</div>
	</div>
</body>
</html>