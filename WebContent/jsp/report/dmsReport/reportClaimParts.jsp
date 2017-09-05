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
<title>售后报表</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>

<script type="text/javascript">
var Options = {
		cells  : 3
	}
	function expotData(){
	//新分单时间
	var str ='2015-05-25 19:30:00';
	var st = str.replace(/-/g,"/");
	var date = (new Date(st)).getTime();  
	   
	var start=document.getElementById("startDate").value;
	var end=document.getElementById("endDate").value;
	
	var startdate=(new Date(start.replace(/-/g,"/"))).getTime();
	var enddate=(new Date(end.replace(/-/g,"/"))).getTime();
	if(""==start){
		MyAlert("提示:必须选择开始结束时间!");
        return;
	}else if(""==end){
		MyAlert("提示:必须选择开始结束时间!");
        return;
	}else if (startdate>=date &&enddate<date) {
		MyAlert("提示:开始结束时间必须同时满足新分单上线时间之前或者之后!请重新选择开始时间或者结束时间!");
        return;
	}else if (startdate<date &&enddate>=date) {
		MyAlert("提示:开始结束时间必须同时满足新分单上线时间之前或者之后!请重新选择开始时间或者结束时间!");
        return;
	}else{
		fm.action="<%=contextPath%>/report/dmsReport/Application/exportClaimParts.do";
		fm.submit();
	}
	  
	}
</script>
</head>
<body >
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;售后报表管理&gt;查询索赔单零件查询<span style="color: red;">(开始结束时间必须是同时满足2015-05-25之前或者之后,否则不能查询)</span>
</div>
<form name="fm" id="fm"  method="post">
<input type="hidden" name="year" value="${year}">
<input type="hidden" name="month" value="${month}">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<!-- 查询条件 -->
	<tr>
		<td width="10%"  class="table_query_2Col_label_5Letter" nowrap="true" >零部件代码：</td>
		<td width="15%"><input name="partCode" class="middle_txt" id="partCode"> </td>
		
		<td width="10%"  class="table_query_2Col_label_5Letter" nowrap="true">零部件名称：</td>
		<td width="15%"><input name="partName" class="middle_txt" id="partName"> </td>
	</tr>
	<tr>
	<td width="10%"class="table_query_2Col_label_5Letter" nowrap="true">商家代码：</td>
		<td width="15%"  nowrap="true">
				<input class="middle_txt" id="dealer_code"  name="dealerCode" type="text" onclick="showOrgDealer('dealer_code','dealer_id','true','','false','','10771002');" readonly="readonly"/>
				<input type="hidden" name="dealerId" id="dealer_id"/>
 				<input type="button" value="清除" class="normal_btn" onclick="wrapOut();"/>
		</td>
		<td width="10%"  class="table_query_2Col_label_5Letter"  nowrap="true" >开始时间：</td>
		<td width="15%"><input name="startDate" onclick="calendar();" value="${start}" readonly="readonly" class="middle_txt" id="startDate"> </td>
		
		<td width="10%"  class="table_query_2Col_label_5Letter" nowrap="true">结束时间：</td>
		<td width="10%"><input name="endDate"  onclick="calendar();" value="${end}" readonly="readonly" class="middle_txt" id="endDate"> </td>
		<td width="35%"></td>
	</tr>
	
	<tr>
		<td align="right">车型：</td>
        <td align="left">
   		     <select name="group_id" id="group_id">
   		     <option value="">-请选择-</option>
   		    <c:forEach items="${listpo}" var="list">
   		       <option value="${list.GROUP_CODE }">
   		          ${list.GROUP_NAME }
   		        </option>
   		    </c:forEach>
   		</select>
   		</td>
		
		<td align="right">索赔类型：</td>
               <td align="left">
		    	 <script type="text/javascript">
		            genSelBoxExp("claim_type",<%=Constant.CLA_TYPE%>,"",true,"short_sel","","false",'');
		        </script>
		    	 
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
</body>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
	var modelChecked=false;
	var re = /^\+?[1-9][0-9]*$/;
	function checkSum(){
		//新分单时间
		var str ='2015-05-25 23:13:15';
		var st = str.replace(/-/g,"/");
		var date = (new Date(st)).getTime();  
		   
		var start=document.getElementById("startDate").value;
		var end=document.getElementById("endDate").value;
		var startdate=(new Date(start.replace(/-/g,"/"))).getTime();
		var enddate=(new Date(end.replace(/-/g,"/"))).getTime();
		if(""==start){
			MyAlert("提示:必须选择开始结束时间!");
	        return;
		}else if(""==end){
			MyAlert("提示:必须选择开始结束时间!");
	        return;
		}else if (startdate>=date &&enddate<date) {
			MyAlert("提示:开始结束时间必须同时满足新分单上线时间之前或者之后!请重新选择开始时间或者结束时间!");
	        return;
		}else if (startdate<date &&enddate>=date) {
			MyAlert("提示:开始结束时间必须同时满足新分单上线时间之前或者之后!请重新选择开始时间或者结束时间!");
	        return;
		}else{
			__extQuery__(1);
		}
		
	}
	var url = "<%=contextPath%>/report/dmsReport/Application/queryClaimParts.json";
	var title = null;
	var columns = [
	       		{header: "序号",sortable: false,align:'center',renderer:getIndex},
	       		{header: "商家代码", dataIndex: 'DEALER_CODE', align:'center'},
	       		{header: "商家简称", dataIndex: 'DEALER_SHORTNAME', align:'center'},
	       		{header: "车型", dataIndex: 'MODEL_NAME', align:'center'},
	       		{header: "供应商代码", dataIndex: 'PRODUCER_CODE', align:'center'},
	       		{header: "供应商名称", dataIndex: 'PRODUCER_NAME', align:'center'},
	       		{header: "索赔单号", dataIndex: 'CLAIM_NO', align:'center'},
	       		{header: "零件代码", dataIndex: 'PART_CODE', align:'center'},
	       		{header: "零件名称", dataIndex: 'PART_NAME', align:'center'},
	       		{header: "零件数量", dataIndex: 'BALANCE_QUANTITY', align:'center'},
	       		{header: "车架号", dataIndex: 'VIN', align:'center'},
	       		{header: "索赔类型", dataIndex: 'CLAIM_TYPE', align:'center'},
	       		{header: "材料费", dataIndex: 'PART_AMOUNT', align:'center'},
	       		{header: "工时费", dataIndex: 'LABOUR_AMOUNT', align:'center'},
	       		{header: "外出费", dataIndex: 'OUT_AMOUNT', align:'center'},
	       		{header: "辅料费", dataIndex: 'ACC_AMOUNT', align:'center'},
	       		{header: "其他", dataIndex: 'OTHER_AMOUNT', align:'center'},
	       		{header: "合计", dataIndex: 'AMOUNT', align:'center'}
	       	];
	function getYear(value,meta,record){
		return value.substring(0,4);
	}
	function getMonth(value,meta,record){
		return value.substring(5,7);
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