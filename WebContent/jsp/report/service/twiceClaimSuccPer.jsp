<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@page import="java.util.*" %>
<%@ page import="com.infodms.dms.common.Constant" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>二次索赔成功率报表</title>
<%
	String contextPath = request.getContextPath();
%>
</head>
<body>
<div id="loader" style='position: absolute; z-index: 200; background: #FFCC00; padding: 1px; top: 4px; display: none; display: none;'></div>
<div class="navigation">
<img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：报表管理&gt;售后服务报表&gt;二次索赔成功率报表</div>
<form method="post" name="fm" id="fm">
<table class="table_query">
	<tr>
		<td width="15%" align="right">供应商代码：</td>
		<td width="30%" align="left">
			<input type="text" name="supplyCode" class="middle_txt" id="supplyCode" value=""/>
		</td>
		<td width="15%" align="right">供应商名称：</td>
		<td width="30%" align="left">
			<input type="text" name="supplyName" class="middle_txt" value="" id=""supplyName""/>
		</td>
	</tr>
	<tr>
		<td width="15%" align="right">车型代码：</td>
		<td align="left" nowrap="true">
			<input type="text" name="modelCode" value="" class="middle_txt" id="modelCode"/>
		</td>	
		<td width="15%" align="right">结算基地：</td>
		<td align="left" nowrap="true">
			<script type="text/javascript">
						genSelBoxExp("YIELDLY",<%=Constant.PART_IS_CHANGHE%>,"",true,"short_sel",'',"false",'');
			</script>
		</td>	
	</tr>
	<tr>
		<td width="15%" align="right">开票时间：</td>
		<td align="left" nowrap="true" colspan="3">
			<input name="bgDate" type="text" class="short_time_txt" id="bgDate" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'bgDate', false);" />  	
	        &nbsp;至&nbsp; <input name="egDate" type="text" class="short_time_txt" id="egDate" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'egDate', false);" /> 
		</td>	
	</tr>
	<tr>
		<td align="center" colspan="4">
			<input class="normal_btn" type="button" id="queryBtn" name="button1" value="查询" onclick="__extQuery__(1);" />&nbsp;
		    <input class="normal_btn" type="button" id="queryBtn" name="button1" value="下载" onclick="exportExcel();" />
		</td>
	</tr>
</table>
<jsp:include page="${contextPath}/queryPage/orderHidden.html" /> <jsp:include
	page="${contextPath}/queryPage/pageDiv.html" /></form>
<script type="text/javascript">
	var myPage;

	var url = "<%=contextPath%>/report/service/OldPartReport/twiceClaimSuccQuery.json?type=0";
				
	var title = null;

	var columns = [
				{header: "序号", renderer:getIndex, align:'center'},
				{header: "供应商代码", dataIndex: 'PRODUCER_CODE', align:'center'},
				{header: "供应商名称", dataIndex: 'MAKER_NAME', align:'center'},
				{header: "入库数量", dataIndex: 'IN_NUM', align:'center'},
				{header: "出库数量", dataIndex: 'OUT_NUM', align:'center'},
				{header: "含税索赔金额", dataIndex: 'OUT_AMOUNT', align:'center'},
				{header: "二次索赔成功率", dataIndex: 'SUC_PERCENT', align:'center'}
		      ];
		            
	function exportExcel(){
	if($('supplyCode').value==""&&$('supplyName').value==""&&$('bgDate').value==""&&$('egDate').value==""
	&&$('modelCode').value==""){
		if(confirm("你未选择任何条件,可能会导致数据量过大而失败.继续下载?")){
		exportExcelDo();}
	}else{
	exportExcelDo();
	}
	}
	function exportExcelDo(){
		fm.action = "<%=contextPath%>/report/service/OldPartReport/twiceClaimSuccQuery.do";
		fm.submit();
}
	loadcalendar();
	 function   showMonthFirstDay()     
  {     
	  var   Nowdate=new   Date();     
	  var   MonthFirstDay=new   Date(Nowdate.getYear(),Nowdate.getMonth(),1);     
	  return DateUtil.Format("yyyy-MM-dd",MonthFirstDay);  
  }     
  function   showMonthLastDay()     
  {     
	  var   Nowdate=new   Date();     
	  var   MonthNextFirstDay=new   Date(Nowdate.getYear(),Nowdate.getMonth()+1,1);     
	  var   MonthLastDay=new   Date(MonthNextFirstDay-86400000);     
	  return DateUtil.Format("yyyy-MM-dd",MonthLastDay);   
  }    
</script>
</body>
</html>