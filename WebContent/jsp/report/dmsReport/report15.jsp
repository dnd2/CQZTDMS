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
	function expotData(){
		var N=$("year").value;
		var re = /^\+?[1-9][0-9]*$/;
		if(N!=""){
			if(!re.test(N)){
				MyAlert("年份为正整数");
				return false;
			}
		}
		
		fm.action="<%=contextPath%>/report/dmsReport/Application/expotData15.do";
		fm.submit();
	  
	}
</script>
</head>
<body >
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;售后报表管理&gt;旧件库存报表
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	
	<tr>
		<td width="10%" align="left" class="table_query_2Col_label_4Letter"  >年份：</td>
		<td width="15%" align="left"><input name="year" class="middle_txt" id="year"> </td>
		<td width="15%"></td>
		<td width="60%">
		  供应商名称：<input type="text" name="producer_name" id="producer_name" class="middle_txt"/>
		</td>
	</tr>
    <tr>
    	<td align="center" colspan="8">
    		<input type="button" name="queryBtn" id="queryBtn"  value="查询"  class="normal_btn" onClick="checkSum(1)" >
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
	
	
	var url = "<%=contextPath%>/report/dmsReport/Application/queryReport15.json";
	var title = null;
	var columns = [
		{header: "序号",sortable: false,align:'center',renderer:getIndex},
		{header: "月份", dataIndex: 'YM', align:'center'},
		{header: "收件家数", dataIndex: 'DEALER_NUM', align:'center',renderer:getDealer1},
		{header: "收货件数", dataIndex: 'SIGE_NUM', align:'center',renderer:getRige1},
		{header: "正常入库件数", dataIndex: 'IN_NUM', align:'center',renderer:getIn1},
		{header: "拒赔件数", dataIndex: 'REFUSE_NUM', align:'center',renderer:getRefuse1},
		{header: "备注", dataIndex: 'REMARK', align:'center'}
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