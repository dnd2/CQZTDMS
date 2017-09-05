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
		var startDate=$("startDate").value;
		var endDate=$("endDate").value;
		var serviceDate=$("serviceDate").value;
		var serviceMile=$("serviceMile").value;
		
		var re = /^\+?[1-9][0-9]*$/;
	
		if(carModel!=null&&carModel!=""){
			modelChecked=true;
		}else{
			MyAlert("请选择车型");
			return false;
		}
		if(serviceDate==""){
			MyAlert("三包期不能为空");
			return false;
		}else if(serviceMile==""){
			MyAlert("三包里程不能为空");
			return false;
		}
		if(!re.test(serviceDate)){
			MyAlert("三包期必须为正整数");
			return false;
		}
		if(!re.test(serviceMile)){
			MyAlert("三包里程必须为正整数");
			return false;
		}
		
		if(startDate==""||endDate==""){
			MyAlert("请选择开始时间");
			return false;
		}else{
			 fm.action="<%=contextPath%>/report/dmsReport/Application/expotDataCPV.do";
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
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;售后报表管理&gt;CPV报表
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<!-- 查询条件 -->
	<tr>
		<td width="10%"  class="table_query_2Col_label_5Letter" nowrap="true">车型：</td>
		<td width="10%">
		<input type="text" id="cars" onclick="showCarModel()" class="middle_txt"  name="cars" readonly="readonly">
		<input type="hidden" id="carModel"  name="carModel">
		
<!-- 				<input type="checkbox" name="model" id="model2" value="B2" checked="checked"/>幻速S2 -->
<!-- 				<input type="checkbox" name="model" id="model3" value="B3" checked="checked"/>幻速S3 -->
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
				<td width="10%"   align="center">三包期：</td>
				<td width="15%"><input name="sDate" class="middle_txt" id="serviceDate"><font color="red">*</font> </td>
				<td width="15%"  align="right">三包里程：</td>
				<td width="15%"><input name="sMile" class="middle_txt" id="serviceMile"><font color="red">*</font> </td>
		<td width="15%"></td>
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
	//选择车型
	var modelChecked=false;
	
// 	function showRegion1(inputCode ,inputId,isMulti )
// 	{   
// //	    toClearDealers();
// 		if(!inputCode){ inputCode = null;}
// 		if(!inputId){ inputId = null;}
// 		if(!isMulti){ isMulti = null;}
// 		OpenHtmlWindow(g_webAppName+'/dialog/showRegion.jsp?INPUTCODE='+inputCode+"&INPUTID="+inputId+"&ISMULTI="+isMulti,730,390);
// 	}
	
	
	
	
	
	function checkSum(){
		var startDate=$("startDate").value;
		var endDate=$("endDate").value;
		var serviceDate=$("serviceDate").value;
		var serviceMile=$("serviceMile").value;
		var carModel=$("carModel").value;
		
		var re = /^\+?[1-9][0-9]*$/;
	
		if(carModel!=null&&carModel!=""){
			modelChecked=true;
		}else{
			MyAlert("请选择车型");
			return false;
		}
		if(serviceDate==""){
			MyAlert("三包期不能为空");
			return false;
		}else if(serviceMile==""){
			MyAlert("三包里程不能为空");
			return false;
		}
		if(!re.test(serviceDate)){
			MyAlert("三包期必须为正整数");
			return false;
		}
		if(!re.test(serviceMile)){
			MyAlert("三包里程必须为正整数");
			return false;
		}
		
		if(startDate==""||endDate==""){
			MyAlert("请选择开始时间");
			return false;
		}else{
			__extQuery__(1);
		}
		
	}
	
	
	
	
	var url = "<%=contextPath%>/report/dmsReport/Application/queryCPV.json";
	var title = null;
	var columns = [
		{header: "序号",sortable: false,align:'center',renderer:getIndex},
		{header: "年份", dataIndex: 'VDATEYEAR', align:'center'},
		{header: "月份", dataIndex: 'VDATEMONTH', align:'center'},
		{header: "车型", dataIndex: 'GROUP_NAME', align:'center'},
		{header: "在报台数", dataIndex: 'VCOUNT', align:'center'},
		{header: "索赔费用", dataIndex: 'RMOUNT', align:'center'},
		{header: "CPV", dataIndex: 'CPV', align:'center'}
	];
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