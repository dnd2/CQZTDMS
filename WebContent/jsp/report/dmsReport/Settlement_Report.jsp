<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>售后报表</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>

<script type="text/javascript">
   function doInit(){
	  loadcalendar();  //初始化时间控件
    }
	function expotData(){
		fm.action="<%=contextPath%>/report/dmsReport/Application/SettlementExport.do";
		fm.submit();
	}
</script>
</head>
<body >
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;售后报表管理&gt;结算汇总报表
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	
	<tr>
		<td width="10%" align="left" class="table_query_2Col_label_4Letter"  >大区：</td>
		<td width="15%" align="left"><input name="root_org_name" class="middle_txt" id="root_org_name"> </td>
		<td width="5%">  </td>
		<td width="15%" align="right">索赔单号：</td>
		<td width="15%">
		  <input type="text" name="claim_no" id="claim_no" class="middle_txt"/>
		</td>
		<td width="5%">  </td>
		<td width="10%" align="right">工单号: </td>
		<td width="15%" > 
		    <input type="text" name="ro_no" id="ro_no" class="middle_txt"/>
		</td>
		<td width="10%">  </td>
	</tr>
	<tr>
		<td width="10%" align="left" class="table_query_2Col_label_4Letter"  >车型：</td>
		<td width="15%" align="left">
		<input name="model_name" class="middle_txt" id="model_name">
		 </td>
		<td width="5%">  </td>
		<td width="15%" align="right">VIN：</td>
		<td width="15%">
		  <input type="text" name="vin" id="vin" class="middle_txt"/>
		</td>
		<td width="5%">  </td>
		<td width="10%" align="right">配件代码: </td>
		<td width="15%" > 
		    <input type="text" name="part_code" id="part_code" class="middle_txt"/>
		</td>
		<td width="10%">  </td>
	</tr>
	<tr>
	   <td width="15%" align="right">审核时间：</td>
	   <td colspan="8">
	       <input name="start_date" type="text" class="short_time_txt" id="start_date" onclick="calendar();" readonly="readonly"/> 
             &nbsp;至&nbsp; <input name="end_date" type="text" class="short_time_txt" id="end_date" onclick="calendar();" readonly="readonly"/> 
	   </td>
	</tr>
    <tr>
    	<td align="center" colspan="8">
    		<input type="button" name="queryBtn" id="queryBtn"  value="查询"  class="normal_btn" onClick="__extQuery__(1);" >
    		<input type="button" name="btnQuery" id="btnQuery"  value="导出"  class="normal_btn" onClick="expotData();" >
    	</td>
    </tr>
    
</table>
<!-- 查询条件 end -->
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>


<form action="" id="fm1">
	<input type="hidden" name="year" id="year1"/>
	<input type="hidden" name="month" id="month1"/>

</form>

</body>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
	var modelChecked=false;
	var re = /^\+?[1-9][0-9]*$/;
	var year=0;
	
	function checkSum(){
		year=document.getElementById("year").value;
		var re = /^\+?[1-9][0-9]*$/;
		if(year!=""){
			if(!re.test(year)){
				MyAlert("年份为正整数");
				return false;
			}
		}
		__extQuery__(1);
	}
	var url = "<%=contextPath%>/report/dmsReport/Application/SettlementReport.json?type=query";
	var title = null;
	var columns = [
        {header: "序号",sortable: false,align:'center',renderer:getIndex},
		{header: "大区",dataIndex: 'ROOT_ORG_NAME',align:'center'},
		{header: "服务站简称",dataIndex: 'DEALER_SHORTNAME',align:'center'},
		{header: "工单号", dataIndex: 'RO_NO', align:'center'},
		{header: "索赔单号", dataIndex: 'CLAIM_NO', align:'center'},
		{header: "索赔类型", dataIndex: 'CLAIM_TYPE', align:'center'},
		{header: "审核日期", dataIndex: 'REPORT_DATE', align:'center'},
		{header: "车型", dataIndex: 'MODEL_NAME', align:'center'},
		{header: "VIN", dataIndex: 'VIN', align:'center'},
		{header: "客户名", dataIndex: 'CTM_NAME', align:'center'},
		{header: "客户电话", dataIndex: 'MAIN_PHONE', align:'center'},
		{header: "配件代码", dataIndex: 'PART_CODE', align:'center'},
		{header: "配件名称", dataIndex: 'PART_NAME', align:'center'},
		{header: "材料费", dataIndex: 'BALANCE_PART_AMOUNT', align:'center'},
		{header: "工时费", dataIndex: 'BALANCE_LABOUR_AMOUNT', align:'center'},
		{header: "其他费用 ", dataIndex: 'OTHER_AMOUNT', align:'center'},
		{header: "合计", dataIndex: 'COUNT_ALL', align:'center'}
	];

	function getDealer1(value,meta,record){
		return String.format("<a href='#' onclick='getDealer(\""+record.data.YM+"\")'>"+value+"</a>");
	}
	function getRige1(value,meta,record){
		return String.format("<a href='#' onclick='getRige(\""+record.data.YM+"\")'>"+value+"</a>");
	}
	function getIn1(value,meta,record){
		return String.format("<a href='#' onclick='getIn(\""+record.data.YM+"\")'>"+value+"</a>");
	}
	function getRefuse1(value,meta,record){
		return String.format("<a href='#' onclick='getRefuse(\""+record.data.YM+"\")'>"+value+"</a>");
	}
	
	
	function getDealer(month){
		if(year==0){
			year=new Date().getFullYear();
		}
		month=month.replace("月","");
		$("month1").value=month;
		$("year1").value=year;
		$("fm1").action="<%=contextPath%>/report/dmsReport/Application/reportDealer.do";
		$("fm1").submit();
	}
	function getRige(month){
		if(year==0){
			year=new Date().getFullYear();
		}
		month=month.replace("月","");
		$("month1").value=month;
		$("year1").value=year;
		$("fm1").action="<%=contextPath%>/report/dmsReport/Application/reportRige.do";
		$("fm1").submit();
	}
	function getIn(month){
		if(year==0){
			year=new Date().getFullYear();
		}
		month=month.replace("月","");
		$("month1").value=month;
		$("year1").value=year;
		$("fm1").action="<%=contextPath%>/report/dmsReport/Application/reportIn.do";
		$("fm1").submit();
	}
	function getRefuse(month){
		if(year==0){
			year=new Date().getFullYear();
		}
		month=month.replace("月","");
		$("month1").value=month;
		$("year1").value=year;
		$("fm1").action="<%=contextPath%>/report/dmsReport/Application/reportRefuse.do";
		$("fm1").submit();
	}
	
	
	function wrapOut(){
		document.getElementById("dealer_id").value="";
		document.getElementById("dealer_code").value="";
	}
	function formatDate1(value,meta,record) {
		if (value==""||value==null) {
			return "";
		}else {
			return String.format(value.substr(0,10));
		}
	}
</script>
<!--页面列表 end -->
</html>