<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
	String addr = request.getParameter("addr");
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>demo</title>
<link href="<%=contextPath%>/style/content.css" rel="stylesheet" type="text/css"/>
<link href="<%=contextPath%>/style/calendar.css" type="text/css" rel="stylesheet"/>
<script type="text/javascript">



function saveTransIdAndPrint(){
	 $('scanDiv').style.display = "none";
	 $('printDiv').style.display = "block";
	 window.print();
}
function saveTransId(){
	if($('transId').value==""){
		MyAlert('请扫描单号!');
		return;
	}
	if($('transOrg').value==""){
		MyAlert('请选择承运单位!');
		return;
	}
	var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkg/saveTrans.json?";	
	sendAjax(url,getResult,'fm');
}
function getResult(jsonObj) {
  	if (jsonObj != null) {
       var success = jsonObj.success;
       var error = jsonObj.error;
       var exceptions = jsonObj.Exception;
       if (success) {
           
           
       } else if (error) {
           MyAlert(error);
       } else if (exceptions) {
           MyAlert(exceptions.message);
       }
       saveTransIdAndPrint();
   }
} 
document.body.onkeydown=function(event){
	var eve = document.all?window.event:event;
	if(eve.keyCode==13){
		var elm=eve.srcElement || eve.target;
		if(elm.id=="transId"){
			$('printBtn').focus();
		}
		return;
	}
}
</script>
</head>

<body style="background-repeat: no-repeat;background-attachment: scroll;margin: auto;scrolling:auto" >
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
<input id="pickOrderId" name="pickOrderId" value="${pickOrderId}" type="hidden" />
	<div id="scanDiv" style="display:block;height:250px;width:550px;position:absolute;top:60px;left:225px;border-style:outset;background-color:#87CEFA"  >
		<table cellSpacing=10 cellPadding=20 >
			<tr>
				<th >
					<font color='red' size='3'><c:if test="${dataMap.transTypeId==1}">普通物流单</c:if><c:if test="${dataMap.transTypeId!=1}">快递物流单</c:if></font>
				</th>
			</tr>
			<tr >
				<td align="right">
					库房：
				</td>
				<td align="left">
					<input id ="wh" name="wh"  type="text" class="text_style" value="${dataMap.whName}" readonly/>
				</td>
				<td align="right">
					寄件人：
				</td>
				<td align="left">
					<input id ="sender" name="sender" type="text" class="text_style" value="${dataMap.userName}" readonly/>
				</td>
			</tr>
			<tr>
				<td align="right">
					服务站：
				</td>
				<td colspan="3" align="left">
					<input id ="dealerName" name="dealerName"  style="width:325px" type="text" value="${dataMap.dealerName}" readonly/>
				</td>
			</tr>
			<tr>
				<td align="right">
					原地点：
				</td>
				<td align="left">
					
					<script type="text/javascript">
						genSelBoxExp("produceFac",<%=Constant.YIELDLY%>,"",false,""," style='width:125px' disabled","false",'');
			  		</script>
		           
			
				</td>
				<td align="right">
					发运方式：
				</td>
				<td align="left">
					<select name="transType" id="transType" disabled style="width:127px">
		                <option value="">-请选择-</option>
		                <c:if test="${transList!=null}">
		                    <c:forEach items="${transList}" var="list">
		                        <option value="${list.fixValue}">${list.fixName}</option>
		                    </c:forEach>
		                </c:if>
		            </select>
				</td>
			</tr>
			<tr>
				<td align="right">
					目的地：
				</td>
				<td colspan="3" align="left" >
					<input id ="addr" name="addr" type="text" readonly style="width:325px" value="${dataMap.addr}" />
				</td>
			</tr>
			<tr>
				<td align="right">
					承运单号：
				</td>
				<td align="left">
					<input id ="transId" name="transId" type="text" class="text_style" value="" />
				</td>
				<td align="right">
					承运单位：
				</td>
				<td align="left">
					<select name="transOrg" id="transOrg" value="${dataMap.transTypeId}" onchange="chgTransOrg()" style="width:127px">
		                <option value="">-请选择-</option>
		                <c:if test="${transOrgList!=null}">
		                    <c:forEach items="${transOrgList}" var="list">
		                        <option value="${list.fixValue}">${list.fixName}</option>
		                    </c:forEach>
		                </c:if>
		            </select>
				</td>
			</tr>
			
		</table>
		
		<div style="position:absolute;top:200px;left:350px">
			<input style="width:50px" id="printBtn" type="button" value="打 印" onclick="saveTransId();"/>
		</div>
		<div style="position:absolute;top:200px;left:410px">
			<input style="width:50px" id="printBtn2" type="button" value="关 闭" onclick="window.close()"/>
		</div>
	</div>
	<div id ="printDiv" style="display:none">
		<div style="position:absolute;top:68pt;left:110pt"><font size="2" ><%--${dataMap.userName}--%>售后配件处</font></div>
		<div style="position:absolute;top:90pt;left:110pt"><font size="2" ><%--${dataMap.sellerName}--%>北汽幻速汽车销售有限公司销售公司售后配件处</font></div>
		<div style="position:absolute;top:100pt;left:110pt"><font size="2" ><%--${dataMap.addr}--%>江西景德镇108信箱</font></div>
        <div style="position:absolute;top:125pt;left:270pt"><font size="2" ><%--${dataMap.addr}--%>3&nbsp;3&nbsp;3&nbsp;0&nbsp;0&nbsp;2&nbsp;</font></div>
        <div style="position:absolute;top:68pt;left:240pt"><font size="2" ><%--${dataMap.userName}--%>0798-8462131</font></div>

		<div style="position:absolute;top:155pt;left:110pt"><font size="2" >${dataMap.receiver}</font></div>
        <div style="position:absolute;top:155pt;left:260pt"><font size="2" >${dataMap.phone}</font></div>
        <div style="position:absolute;top:175pt;left:110pt"><font size="2" >${dataMap.dealerName}</font></div>
        <div style="position:absolute;top:160pt;left:540pt"><font size="2" >${dataMap.year}年${dataMap.month}月${dataMap.day}日</font></div>
		<div style="position:absolute;top:190pt;left:110pt;white-space:normal; width:270px;"><font size="2" >${dataMap.addr}</font></div>
		<div style="position:absolute;top:215pt;left:110pt;white-space:normal; width:270px;"><font size="1" >${dataMap.remark}</font></div>
        <div style="position:absolute;top:255pt;left:500pt;white-space:normal; width:100px;"><font size="1" >${dataMap.soCodes}</font></div>
		<div style="position:absolute;top:310pt;left:310pt"><font size="2" >${dataMap.totalAmount}</font></div>

	</div>
</form>
</body>
<script type="text/javascript">
$('transType').value=${dataMap.transTypeId}; 
$('transId').focus();
function chgTransOrg(){
	$('transId').focus();
}
</script>
</html>
