<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="<%=request.getContextPath()%>/jsp/demo/Fixed.css" type="text/css" rel="stylesheet" />
<script src="<%=request.getContextPath()%>/jsp/demo/Fixed.js"></script>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>
<title>售后报表</title>
<script type="text/javascript">
var Options = {
		cells  : 3
	}
	function expotData(){
	   fm.action="<%=contextPath%>/report/dmsReport/Application/expotData3.do";
       fm.submit();
	}
	function showActivity(){
		OpenHtmlWindow('<%=contextPath%>/jsp_new/activity/activityName.jsp',800,460);
	}
	function myCheck(activity_id,activity_code,activity_name){
		$('activity_code').value=activity_code;
		$('activity_name').value=activity_name;
	}
	function wrapOut1(){
		$('activity_code').value="";
		$('activity_name').value="";
	}
	
	
</script>
</head>
<body >
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;售后报表管理&gt;服务活动完成率报表
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<!-- 查询条件 -->
	<tr>
		
	    <td width="10%" class="table_query_2Col_label_6Letter" nowrap="true">服务活动名称：</td>
		<td width="20%" nowrap="true">
			<input type="text" readonly="readonly" name="activity_name" id="activity_name" class="long_txt"/>
			<input type="hidden" name="activity_code" id="activity_code"/>
			<input type="button" class="mini_btn" value="..." onclick="showActivity();"/>
          	<input type="button" class="normal_btn" value="清除" onclick="wrapOut1();"/>
		</td> 
		<td width="10%"class="table_query_2Col_label_5Letter" nowrap="true">商家代码：</td>
		<td width="20%"  nowrap="true">
				<input class="middle_txt" id="dealer_code"  name="dealerCode" type="text" onclick="showOrgDealer('dealer_code','dealer_id','true','','false','','10771002');" readonly="readonly"/>
				<input type="hidden" name="dealerId" id="dealer_id"/>
 				<input type="button" value="清除" class="normal_btn" onclick="wrapOut();"/>
		</td>
		<td  nowrap="true">开始时间：</td>
		<td  nowrap="true" width="25%">
		  <input class="short_txt" id="startDate" name="startDate" datatype="1,is_date,10" readonly="readonly"
                 maxlength="10" group="startDate,endDate" onclick="calendar();"/>
             	 结束时间
          <input class="short_txt" id="endDate" name="endDate" datatype="1,is_date,10" onclick="calendar();" readonly="readonly"
                 maxlength="10" group="startDate,endDate"/>
		</td>
	</tr>
    <tr>
    	<td align="center" colspan="6">
    		<input type="button" name="queryBtn" id="queryBtn"  value="查询"  class="normal_btn" onClick="checkSum()" >
    		&nbsp;&nbsp;&nbsp;&nbsp;
    		<input type="reset"  name="bntReset" id="bntReset" value="重置" class="normal_btn" />
    		&nbsp;&nbsp;&nbsp;&nbsp;
    		<input type="button" name="btnQuery" id="btnQuery"  value="导出"  class="normal_btn" onClick="expotData();" >
    		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;总完成率：<span id="a4"></span>
    	</td>
    </tr>
    <tr class="table_list_row1">
		<td >参加活动车辆总数量：</td>
		<td ><span id="a1"></span></td>
		<td >已完成总数量：</td>
		<td ><span id="a2"></span></td>
		<td >未完成总数量：</td>
		<td ><span id="a3"></span></td>
	</tr>
</table>
<!-- 查询条件 end -->
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
</body>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
	
	function checkSum(){
		document.getElementById("a1").innerHTML = '';
		document.getElementById("a2").innerHTML = '';
		document.getElementById("a3").innerHTML = '';
		document.getElementById("a4").innerHTML = '';
		makeNomalFormCall("<%=contextPath%>/report/dmsReport/Application/report3Sum.json",function(json){
		document.getElementById("a1").innerHTML = json.valueMap.COUNTALL == null ? '0' : json.valueMap.COUNTALL;//工时费
		document.getElementById("a2").innerHTML = json.valueMap.ACHIVENUM == null ? '0' : json.valueMap.ACHIVENUM;
		document.getElementById("a3").innerHTML = json.valueMap.NOTACHIVENUM == null ? '0' : json.valueMap.NOTACHIVENUM;
		document.getElementById("a4").innerHTML = json.valueMap.PERCENTALL == null ? '0' : json.valueMap.PERCENTALL;
		},'fm');
		__extQuery__(1);
	}
	
	
	
	
	
	
	var url = "<%=contextPath%>/report/dmsReport/Application/report3.json?query=true";
	var title = null;
	var columns = [
		{header: "序号",sortable: false,align:'center',renderer:getIndex},
		{header: "服务商代码", dataIndex: 'DEALER_CODE', align:'center'},
		{header: "服务商简称", dataIndex: 'DEALER_SHORTNAME', align:'center'},
		{header: "大区", dataIndex: 'ORG_NAME', align:'center'},
		{header: "活动代码", dataIndex: 'ACTIVITY_CODE', align:'center'},
		{header: "活动名称", dataIndex: 'ACTIVITY_NAME', align:'center'},
		{header: "服务站分配数量", dataIndex: 'COUNTALLBYDEALER', align:'center'},
		{header: "自店完成数", dataIndex: 'ACHIVENUMSELF', align:'center'},
		{header: "他店帮助完成数", dataIndex: 'HELPACHIVETEMP', align:'center'},
		{header: "帮助他店完成数", dataIndex: 'HELPACHIVE', align:'center'},
		{header: "自店总完成数", dataIndex: 'REPAIRNUM', align:'center'},
		{header: "修理总数量", dataIndex: 'REPAIRNUMALL', align:'center'},
		{header: "完成率", dataIndex: 'PERCENTBYDEALER', align:'center'}
	];
	function wrapOut(){
		document.getElementById("dealer_id").value="";
		document.getElementById("dealer_code").value="";
	}
</script>
<!--页面列表 end -->
</html>