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
	
	var A;
	var N;
	var model
	function expotData(){
		
		var startDate=$("startDate").value;
		var endDate=$("endDate").value;
		var carModel=document.getElementById("carModel").value;
		N=$("N").value;
		A=$("A").value;
		if(carModel!=null &&carModel!=""){
			modelChecked=true;
		}else{
			MyAlert("请选择车型");
			return false;
		}
		if(N==""){
			MyAlert("生产车辆取值范围不能为空");
			return false;
		}else if(A==""){
			MyAlert("索赔车辆取值范围：不能为空");
			return false;
		}
		if(!re.test(N)){
			MyAlert("生产车辆取值范围必须为正整数");
			return false;
		}
		if(!re.test(A)){
			MyAlert("索赔车辆取值范围必须为正整数");
			return false;
		}
		if(startDate==""||endDate==""){
			MyAlert("请选择时间");
		}else{
			 fm.action="<%=contextPath%>/report/dmsReport/Application/expotData3CS.do";
		       fm.submit();
		}
	  
	}
	
	function showCarModel(){
		OpenHtmlWindow('<%=contextPath%>/ApplicationAction/showCarType.do',800,500);
	}
</script>
</head>
<body >
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;售后报表管理&gt;3CS管理报表
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<!-- 查询条件 -->
	<input type="hidden" name="year" id="year"/> 
	<input type="hidden" name="month" id="month"/> 
	
	<tr>
		<td width="10%"  class="table_query_2Col_label_5Letter" nowrap="true">车型：</td>
		<td width="10%">
				<input type="text" id="cars" class="middle_txt" onclick="showCarModel()" name="cars" readonly="readonly">
		<input type="hidden" id="carModel"  name="carModel">
		</td>
		<td width="10%"class="table_query_2Col_label_5Letter" nowrap="true">算法：</td>
		<td width="15%">
		<select name="math">
			<option value="3CS" selected="selected">3CS</option>
			<option value="IPTV" >IPTV</option>
		</select>
		
		</td>
		<td align="right" nowrap="true">开始时间：</td>
		<td align="left" nowrap="true" width="25%">
		  <input class="short_txt" id="startDate" name="startDate" datatype="1,is_date,10" readonly="readonly"
                 maxlength="10" group="startDate,endDate" onclick="calendar();"/><font color="red">*</font>
             	 结束时间
          <input class="short_txt" id="endDate" name="endDate" datatype="1,is_date,10" onclick="calendar();" readonly="readonly"
                 maxlength="10" group="startDate,endDate"/><font color="red">*</font>
		</td>
		<td width="10%"></td>
	</tr>
	<tr>
		<td width="15%"  class="table_query_2Col_label_8Letter" nowrap="true" >生产车辆取值范围：</td>
		<td width="15%"><input name="N" class="middle_txt" id="N"><font color="red">*</font> </td>
		
		<td width="15%"  class="table_query_2Col_label_8Letter" nowrap="true">索赔车辆取值范围：</td>
		<td width="20%"><input name="A" class="middle_txt" id="A"> <font color="red">*</font> </td>
		<td width="12%"></td>
	</tr>
    <tr>
    	<td align="center" colspan="10">
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

<form action="" name="fm1" id="fm1">
	<input type="hidden" name="N" id="N1"/> 
</form>
</body>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
	var modelChecked=false;
	var re = /^\+?[1-9][0-9]*$/;
	function checkSum(){
		var startDate=$("startDate").value;
		var endDate=$("endDate").value;
		var carModel=document.getElementById("carModel").value;
		N=$("N").value;
		A=$("A").value;
		if(carModel!=null &&carModel!=""){
			modelChecked=true;
		}else{
			MyAlert("请选择车型");
			return false;
		}
		if(N==""){
			MyAlert("生产车辆取值范围不能为空");
			return false;
		}else if(A==""){
			MyAlert("索赔车辆取值范围：不能为空");
			return false;
		}
		if(!re.test(N)){
			MyAlert("生产车辆取值范围必须为正整数");
			return false;
		}
		if(!re.test(A)){
			MyAlert("索赔车辆取值范围必须为正整数");
			return false;
		}
		if(startDate==""||endDate==""){
			MyAlert("请选择时间");
		}else{
			__extQuery__(1);
		}
		
	}
	
	
	
	
	var url = "<%=contextPath%>/report/dmsReport/Application/queryReport17.json";
	var title = null;
	var columns = [
		{header: "序号",sortable: false,align:'center',renderer:getIndex},
		{header: "年份", dataIndex: 'VDATEYEAR', align:'center'},
		{header: "月份", dataIndex: 'VDATEMONTH', align:'center'},
		{header: "生产车辆", dataIndex: 'PCOUNT', align:'center',renderer:productCar},
		{header: "销售车辆", dataIndex: 'SCOUNT', align:'center',renderer:saleCar},
		{header: "索赔数量", dataIndex: 'RCOUNT', align:'center',renderer:claimCar},
		{header: "3CS值", dataIndex: 'PT', align:'center'}
	];
	
	function productCar(value,meta,record){
		return String.format("<a href='#' onclick='productCar1("+record.data.VDATEYEAR+","+record.data.VDATEMONTH+" )'>"+value+"</a>");
	}
	function saleCar(value,meta,record){
		return String.format("<a href='#' onclick='saleCar1("+record.data.VDATEYEAR+","+record.data.VDATEMONTH+" )'>"+value+"</a>");
	}
	
	function claimCar(value,meta,record){
		return String.format("<a href='#' onclick='claimCar1("+record.data.VDATEYEAR+","+record.data.VDATEMONTH+" )'>"+value+"</a>");
	}
	
	function productCar1(year,month){
		$("year").value=year;
		$("month").value=month;
		$("N1").value=N;
		$("fm").action="<%=contextPath%>/report/dmsReport/Application/reportproductCar.do";
		$("fm").submit();
	}
	function saleCar1(year,month){
		$("year").value=year;
		$("month").value=month;
		$("fm").action="<%=contextPath%>/report/dmsReport/Application/reportClaimCar.do";
		$("fm").submit();
	}
	function claimCar1(year,month){
		sendAjax(url,function(json){
			if(json.result){
				
			}
		},'fm1');
		
		
		$("year").value=year;
		$("month").value=month;
		$("N").value=N;
		$("fm").action="<%=contextPath%>/report/dmsReport/Application/reportClaimCar.do";
		$("fm").submit();
	}
	
	
	
	function getYear(value,meta,record){
		return value.substring(0,4);
	}
	function getMonth(value,meta,record){
		return value.substring(5,7);
	}
	function myLink(value,meta,record){
		var width=900;
		var height=500;
		var screenW = window.screen.width-30;	
		var screenH = document.viewport.getHeight();
		if(screenW!=null && screenW!='undefined')
			width = screenW;
		if(screenH!=null && screenH!='undefined')
			height = screenH;
		var roNo = record.data.RO_NO;
		var ID = record.data.ID;
		var claimNo = record.data.CLAIM_NO;
		return String.format("<a href='#' onclick='OpenHtmlWindow(\"<%=contextPath%>/claim/application/ClaimBillStatusTrack/claimBillDetailForward.do?roNo="+roNo+"&ID="
				+ ID + "\","+width+","+height+")' >"+claimNo+"</a>");
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