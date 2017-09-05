<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>集客量</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
<!--
function doInit(){
	loadcalendar();  //初始化时间控件
	// genLocSel('region','','','','','');
}
function queryInit() {
	if(submitForm('fm')){
		var startDate = document.getElementById("startDate").value ;
		var endDate = document.getElementById("endDate").value ;
		var dealerId = document.getElementById("dealerId").value ;
		var region = document.getElementById("regionCode").value ;
		// var orgId = document.getElementById("orgId").value ;
		var url = "<%=contextPath%>/sales/customerRegisterSlot/customerRegisterSlot/oemTotalQuery.do?startDate=" + startDate + "&endDate=" + endDate + "&dealerId=" + dealerId +"&region=" + region + "" ;
		// MyAlert(url) ;
		window.open(url, "集客量统计查询") ;
	}
}

function downLoadDtlInit() {
	$('fm').action= "<%=contextPath%>/sales/customerRegisterSlot/customerRegisterSlot/dealerDownLoad.json"
	$('fm').submit();
}

function downLoadInit() {
	if(submitForm('fm')){
		$('fm').action= "<%=contextPath%>/sales/customerRegisterSlot/customerRegisterSlot/oemTotalDownLoad.json"
		$('fm').submit();
	}
}

function downLoadDrlInit() {
	if(submitForm('fm')){
		$('fm').action= "<%=contextPath%>/sales/customerRegisterSlot/customerRegisterSlot/oemTotalDrlDownLoad.json"
		$('fm').submit();
	}
}

function clrTxt(value) {
	document.getElementById(value).value = "" ;
}

function CallshowRegDealer(){
	  var regionCode =  $('regionCode').value ;
    showRegDealer('dealerCode','dealerId','true','',regionCode);
	}
	
function toClearDealers(){
	 $('dealerCode').value = '';
	}
//-->
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 经销商实销管理 &gt; 集客量录入 &gt;集客量</div>
<form method="post" name="fm" id="fm">
<!-- 查询条件 begin -->
	<table class="table_query" >
		<tr>
			<td align="right">日期：</td>
			<td>
				<div align="left">
	            	<input name="startDate" id="startDate" value="" type="text" class="short_txt" datatype="0,is_date,10" group="startDate,endDate" hasbtn="true" callFunction="showcalendar(event, 'startDate', false);" />
	            	&nbsp;至&nbsp;
	            	<input name="endDate" id="endDate" value="" type="text" class="short_txt" datatype="0,is_date,10" hasbtn="true" callFunction="showcalendar(event, 'endDate', false);" />
            	</div>	
			</td>
			<td align="right">选择省份：</td>
      		<td>
				<input type="text"  readonly="readonly"  name="regionCode" size="15" value=""  id="regionCode" class="middle_txt" />
				<input name="regbu"  id="regbu" type="button" class="mark_btn" onclick="showRegion('regionCode','regionId','true')" value="..." />
				<input type="hidden"  name="regionId" size="15" value=""  id="regionId" />
				<input type="button"  class="cssbutton" value="清除" onClick="clrTxt('regionCode');clrTxt('regionId');clrTxt('dealerId');clrTxt('dealerCode');"/>
			<td width="13%"></td>
		</tr>
		<tr>
		<td align="right">经销商公司：</td>
			<td><input type="hidden" name="dealerId" size="15" id="dealerId" value="" />
				<input type="text" class="middle_txt" readonly="readonly" name="dealerCode" size="15" id="dealerCode" value="" />
				<input name="button4" type="button"  class="mini_btn" onclick="showCompanyByOther('dealerCode', 'dealerId', 'true', '${orgId }', '<%=Constant.MSG_TYPE_1 %>')"; value="..." />
				<input type="button" class="normal_btn" name="divGroup" id="divGroup" value="公司分组" onclick="showDivideGroup('dealerId', '', 'dealerCode', '<%=Constant.DIVIDE_GROUP_TYPE_DLR_COMPANY %>', '<%=Constant.DIVIDE_CLASS_SALE %>') ;" />
				<input type="button" name="button4clearbutton" id="button4clearbutton" class="cssbutton" value="清除" onClick="clrTxt('dealerCode');clrTxt('dealerId');"/>
			</td>
     	<td align="right"></td>
		<td align="left"></td>
		<td width="13%"></td>
		</tr>
		<tr>
			<td></td>
			<td></td>
			<td></td>
			<td align="right">
				<input name="queryBtn" type=button class="cssbutton" onClick="queryInit();" value="查询">&nbsp;
				<input name="dtlDown" type=button class="cssbutton" onClick="downLoadDtlInit();" value="明细下载">&nbsp;
				<input name="button2" type=button class="cssbutton" onClick="downLoadInit();" value="下载">&nbsp;
				<input name="dlrTotDown" type=button class="cssbutton" style="width:100px" onClick="downLoadDrlInit();" value="经销商合计下载">
			</td>
			<td></td>
		</tr>
	</table>
</form>
</body>
</html>