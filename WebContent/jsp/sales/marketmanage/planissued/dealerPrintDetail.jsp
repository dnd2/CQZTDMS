<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.*"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<style media="print" type="text/css">
	.cssbutton   {   DISPLAY:   none   }          
</style>
<title>活动执行方案打印</title>
<% String contextPath = request.getContextPath();  %>
<script type="text/javascript">
<!--
function doInit() {
	setCost() ;
	getTotalPrice(${actCost},'activitPrice') ;
	getTotalPrice(${actCostNoTax},'activitPriceNoTax') ;
	getTotalPrice(${medCost},'mediaPrice') ;
	getTotalPrice(${medCostNoTax},'mediaPriceNoTax') ;
	getTotalCost('activitPrice','mediaPrice','totalCost') ;
	getTotalCost('activitPriceNoTax','mediaPriceNoTax','totalCostNoTax') ;
}

function getTotalPrice(price, sId) {
	document.getElementById(sId).innerHTML = serPrice(price) ;
}

function getTotalCost() {
	var fTotlaCost = 0 ;
	// var iLen = arguments.length ;
	
	for(var i=0; i<2; i++) {
		if(document.getElementById(arguments[i]).innerHTML != 'undfined') {
			fTotlaCost += parseFloat(document.getElementById(arguments[i]).innerHTML) ;
		}
	}
	
	document.getElementById(arguments[2]).innerHTML = serPrice(fTotlaCost) ;
}

function setCost() {
	document.getElementById("cost").innerHTML = serPrice(${cost }) ;
}

function serPrice(fPrice) {
	var aPrice = fPrice.toString().split("\.") ;
	var iLen = aPrice.length ;

	if(iLen == 1) {
		fPrice = aPrice[0] + ".00" ;
	} else if (iLen == 2) {
		var iLen_A = aPrice[1].length ;

		if(iLen_A == 1) {
			fPrice = aPrice[0] + "." + aPrice[1] + "0" ;
		} else if(iLen_A > 2) {
			fPrice = aPrice[0] + "." + aPrice[1].substr(0, 2) ;
		}
	}
	
	return fPrice ;
}
//-->
</script>
</head>
<body>
<br />
<div align="center">
<table border="0" cellspacing="0" cellpadding="0" width="98%">
<tr>
<td>
	<img src="<%=request.getContextPath()%>/img/chana/logov.jpg" align="left"/>
	<strong><font size="5">长安汽车市场推广计划申请表</font></strong>
</td>
</tr>
</table>
<table border="0" cellspacing="0" cellpadding="0" width="98%">
<tr>
<td align="right" valign="bottom">
	<strong>计划编号：</strong>${map2.CAMPAIGN_NO }
</td>
</tr>
</table>
<table border="1" cellspacing="0" cellpadding="0" style="border-bottom:0.5px" width="98%">
<tr align="left">
	<td height="20" width="9%" align="center"><strong>申请单位</strong></td>
	<td width="8%"><strong>A/B网：</strong></td>
	<td width="12%">${map2.AREA_NAME}</td>
	<td width="10%"><strong>运营大区：</strong></td>
	<td width="11%">${map2.ORG_NAME}</td>
	<td width="11%"><strong>服务中心/<br />授权专营店：</strong></td>
	<td width="16%">${map2.DEALER_NAME}　</td>
	<td width="10%"><strong>申请日期：</strong></td>
	<td width="11%">${map2.SUBMITS_DATE}　</td>
</tr>
<tr align="left">
	<td height="20" align="center"><strong>费用类别</strong></td>
	<td colspan="2"><script>document.write(getItemValue(${type }));</script></td>
	<td height="20"><strong>计划名称：</strong></td>
	<td colspan="10">${map2.CAMPAIGN_NAME}</td>
</tr>
</table>
<table border="1" cellspacing="0" cellpadding="0" style="border-top:0.5px;border-bottom:0.5px" width="98%">
<tr align="left">
	<td colspan="6" height="20"><strong>1、月度目标</strong></td>
</tr>
<tr align="center">
	<td height="20" width="30%"><strong>实销数</strong></td>
	<td width="12%">${map3.DELIVERY_CNT_TGT }</td>
	<td width="7%"><strong>辆</strong></td>
	<td width="30%"><strong>来电（店）数</strong></td>
	<td width="12%">${map3.CALLS_HOUSES_CNT_TGT }</td>
	<td width="7%"><strong>组</strong></td>
</tr>
<tr align="center">
	<td height="20"><strong>启票数</strong></td>
	<td>${map3.ORDER_CNT_TGT }</td>
	<td><strong>辆</strong></td>
	<td><strong>信息留存数</strong></td>
	<td>${map3.RESERVE_CNT_TGT }</td>
	<td><strong>组</strong></td>
</tr>
</table>
<table border="1" cellspacing="0" cellpadding="0" style="border-top:0.5px;border-bottom:0.5px" width="98%">
<tr align="left">
	<td colspan="6" height="20"><strong>2、资源计划</strong></td>
</tr>
<tr align="center">
<td height="20" colspan="2"><strong>可用费用</strong></td>
<td colspan="2"><strong>含税申请费用</strong></td>
<td colspan="2"><strong>不含税申请费用</strong></td>
</tr>
<tr align="center">
<td height="20" width="25%" align="right"><span id="cost">0</span></td>
<td width="7%"><strong>元</strong></td>
<td width="27%" align="right"><span id="totalCost">0</span></td>
<td width="7%"><strong>元</strong></td>
<td width="25%" align="right"><span id="totalCostNoTax">0</span></td>
<td width="7%"><strong>元</strong></td>
</tr>
</table>
<table border="1" cellspacing="0" cellpadding="0" style="border-top:0.5px;border-bottom:0.5px" width="98%">
<tr align="left">
	<td colspan="9" height="20"><strong>3、推广活动</strong></td>
</tr>
<tr align="center">
<td height="20" width="9%"><strong>省份</strong></td>
<td width="9%"><strong>活动<br />类型</strong></td>
<td width="12%"><strong>起止时间</strong></td>
<td width="15%"><strong>活动主题</strong></td>
<td width="14%"><strong>活动明细</strong></td>
<td width="11%"><strong>规格/单位</strong></td>
<td width="8%"><strong>项目<br />数量</strong></td>
<td width="10%"><strong>项目单价<br />（元）</strong></td>
<td width="10%"><strong>金额<br />（元）</strong></td>
</tr>
<c:forEach items="${list1}" var="list1">
<tr align="center">
<td height="20">
<script type="text/javascript">
	writeRegionName('<c:out value="${list1.REGION}"/>') ;
</script>　
</td>
<td>
<script type="text/javascript">
	writeItemValue('${list1.ACTIVITY_TYPE}');
</script>
</td>
<td>${list1.START_DATE}至${list1.END_DATE}　</td>
<td>${list1.ACTIVITY_CONTENT}　</td>
<td>${list1.ITEM_NAME}　</td>
<td>${list1.ITEM_REMARK}　</td>
<td>${list1.ITEM_COUNT}</td>
<td>
<script type="text/javascript">
document.write(serPrice(${list1.ITEM_PRICE})) ;
</script>
</td>
<td>
<script type="text/javascript">
document.write(serPrice(${list1.PLAN_COST})) ;
</script>
<input name="planCost1" type="hidden" value="${list1.PLAN_COST}"/>
</td>
</tr>
</c:forEach>
</table>
<table border="1" cellspacing="0" cellpadding="0" style="border-top:0.5px;border-bottom:0.5px" width="98%">
<tr align="center">
<td height="20" width="34%" align="right"><strong>费用小计[含税]（元）：</strong></td>
<td width="15%" align="left"><span id="activitPrice">0</span></td>
<td height="20" width="34%" align="right"><strong>费用小计（元）：</strong></td>
<td width="15%" align="left"><span id="activitPriceNoTax">0</span></td>
</tr>
</table>
<table border="1" cellspacing="0" cellpadding="0" style="border-top:0.5px;border-bottom:0.5px" width="98%">
<tr align="left">
	<td colspan="10" height="20"><strong>4、媒体投放</strong></td>
</tr>
<tr align="center">
<td height="20" width="9%"><strong>省份</strong></td>
<td width="8%"><strong>车型<br />品牌</strong></td>
<td width="10%"><strong>宣传<br />主题</strong></td>
<td width="12%"><strong>广告<br />日期</strong></td>
<td width="9%"><strong>媒体<br />名称</strong></td>
<td width="11%"><strong>规格/版式<br />单日频次</strong></td>
<td width="11%"><strong>刊发位置<br/>播出时间</strong></td>
<td width="8%"><strong>总<br/>次数</strong></td>
<td width="10%"><strong>结算单价<br /><font size="1">元/次</font></strong></td>
<td width="10%"><strong>金额<br />（元）</strong></td>
</tr>
<c:forEach items="${list2}" var="list2">
<tr>
<td height="20">
<script type="text/javascript">
	writeRegionName('<c:out value="${list2.REGION}"/>') ;
</script>
</td>
<td>
<label>
<script type="text/javascript">
	writeItemValue("${list2.MEDIA_MODEL}") ;
</script>
</label>
</td>
<td>
	${list2.ADV_SUBJECT}
</td>
<td>
	${list2.ADV_DATE}至${list2.END_DATE}
</td>
<td>
	${list2.MEDIA_NAME}
</td>
<td>
	${list2.MEDIA_SIZE}
</td>
<td>
	${list2.MEDIA_PUBLISH}
</td>
<td>
	${list2.TOTAL_COUNT}
</td>
<td>
<script type="text/javascript">
document.write(serPrice(${list2.ITEM_PRICE})) ;
</script>
</td>
<td>
<script type="text/javascript">
document.write(serPrice(${list2.PLAN_COST})) ;
</script>
<input type="hidden" name="planCost2" value="${list2.PLAN_COST}" />
</td>
</tr>
</c:forEach>
</table>
<table border="1" cellspacing="0" cellpadding="0" style="border-top:0.5px;border-bottom:0.5px" width="98%">
<tr align="center">
<td height="20" width="34%" align="right"><strong>费用小计[含税]（元）：</strong></td>
<td width="15%" align="left"><span id="mediaPrice">0</span></td>
<td height="20" width="34%" align="right"><strong>费用小计（元）：</strong></td>
<td width="15%" align="left"><span id="mediaPriceNoTax">0</span></td>
</tr>
</table>
<table border="1" cellspacing="0" cellpadding="0" style="border-top:0.5px" width="98%">
<tr align="left">
	<td colspan="5" height="20"><strong>5、审批</strong></td>
</tr>
<tr align="center">
<td height="20" colspan="2"><strong>部门</strong></td>
<td><strong>审批人员</strong></td>
<td><strong>审批意见</strong></td>
<td><strong>审批时间</strong></td>
</tr>
<tr>
<td align="left" height="20" width="12%"><strong>服务中心</strong></td>
<td width="16%" align="left">　</td>
<td width="9%">${map2.NAME}　</td>
<td width="50%" align="left">${map2.REMARK}　</td>
<td width="11%">${map2.SUBMITS_DATE}　</td>
</tr>
<tr>
<td align="left" height="40" rowspan="2"><strong>运营大区</strong></td>
<td align="left">区域经理</td>
<td>${mapC0.NAME}　</td>
<td align="left">${mapC0.CHECK_DESC}　</td>
<td>${mapC0.CHECK_DATE}　</td>
</tr>
<!--  
<tr>
<td align="left">市场主管/专员</td>
<td>${mapC1.NAME}　</td>
<td align="left">${mapC1.CHECK_DESC}　</td>
<td>${mapC1.CHECK_DATE}　</td>
</tr>
-->
<tr>
<td align="left">大区经理/负责人</td>
<td>${mapC1.NAME}　</td>
<td align="left">${mapC1.CHECK_DESC}　</td>
<td>${mapC1.CHECK_DATE}　</td>
</tr>
<!--
<tr>
<td height="40" align="left" rowspan="2"><strong>事业部领导</strong></td>
<td height="20" align="left">分管副总意见</td>
<td colspan="3">　</td>
</tr>
<tr>
<td height="20" align="left">事业部总经理意见</td>
<td colspan="3">　</td>
</tr>
-->
</table>
</div>
<br />
<div align="center">
<input type="button" class="cssbutton" onclick="window.print() ;" value="打印">
</div>
</body>
</html>