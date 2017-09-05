<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
	String companyType = Constant.DUTY_TYPE_COMPANY.toString();
	String deptType = Constant.DUTY_TYPE_DEPT.toString();
	String largeRegionType = Constant.DUTY_TYPE_LARGEREGION.toString();
	String smallRegionType = Constant.DUTY_TYPE_SMALLREGION.toString();
	String dealerType = Constant.DUTY_TYPE_DEALER.toString();
	request.setAttribute("companyType", companyType);
	request.setAttribute("deptType", deptType);
	request.setAttribute("largeRegionType", largeRegionType);
	request.setAttribute("dealerType", dealerType);
	int year = (Integer)request.getAttribute("year");
	int month = (Integer)request.getAttribute("month");
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script language="javaScript">
	function submitFRM(){
		var sysTime = new Date().format('yyyy-MM-dd');
		var endTime = document.getElementById("endTime").value;
		var startTime = document.getElementById("startTime").value;
		if(sysTime < endTime){
			MyAlert('截止日期不能超过当前日期！');
		} else if( endTime == '' || startTime == ''){
			MyAlert('请先填写起止日期！');
		} else if(startTime > endTime) {
			MyAlert('起始日期不能超过结束日期！');
		} else {
			document.FRM.action='<%=request.getContextPath()%>/report/reportOne/SalesReport/getTimeoutStatisticsSummaryReport.do';
			document.FRM.target="_blank";
			document.FRM.submit();
		}
	}

	function download(){
		//取系统时间
		var sysTime = new Date();
		var sysYear = sysTime.getFullYear(); 
		var sysMonth = sysTime.getMonth()+1;
		//取选择时间
		var startYear = parseInt(document.getElementById("startYear").value);
		var startMonth = parseInt(document.getElementById("startMonth").value);
		var endYear = parseInt(document.getElementById("endYear").value);
		var endMonth = parseInt(document.getElementById("endMonth").value);
		if(startYear > endYear) {
			MyAlert('起始年份不能超过结束年份！');
			return false;
		} else if(startYear > sysYear || endYear > sysYear) {
			MyAlert('起止年份不能超过当前年份！');
			return false;
		} else if(startYear == endYear && endMonth < startMonth) {
			MyAlert('起始月份不能超过结束月份！');
			return false;
		} else if(startYear == sysYear && startMonth > sysMonth || startYear == sysYear && endMonth > sysMonth){
			MyAlert('起止月份不能超过当前月份！');
			return false;
		} else if( startYear == '' || startMonth == '' || endYear == '' || endMonth == ''){
			MyAlert('请先填写起止日期！');
			return false;
		}  else {
			document.FRM.action='<%=request.getContextPath()%>/report/reportOne/SalesReport/getTimeoutStatisticsSummaryDownload.do';
			document.FRM.target="_self";
			document.FRM.submit();
		}
	}

	function toClear(){
			document.getElementById("campaignModel").value="";
			document.getElementById("modelId").value="";
	}
	
	function toClearDealers(){
		document.getElementById("dealerCode").value="";
		document.getElementById("dealerId").value=""
	}
	
	function toClearOrgs(){
		document.getElementById("orgCode").value="";
		document.getElementById("orgId").value="";
	}
	
	function checkType(){
		document.getElementById("orgbuclearbutton").disabled=true;
		document.getElementById("orgbu").disabled=true;
		document.getElementById("button4").disabled=false;
		document.getElementById("button4clearbutton").disabled=false;
		document.getElementById("orgCode").value="";
		document.getElementById("orgId").value="";
	}
	
	function checkOrgType(){
		document.getElementById("orgbuclearbutton").disabled=false;
		document.getElementById("orgbu").disabled=false;
		document.getElementById("button4").disabled=true;
		document.getElementById("button4clearbutton").disabled=true;
		document.getElementById("dealerCode").value="";
		document.getElementById("dealerId").value="";
	}
	
	Date.prototype.format = function(format)
	{
		 var o = {
		 "M+" : this.getMonth()+1, //month
		 "d+" : this.getDate(),    //day
		 "h+" : this.getHours(),   //hour
		 "m+" : this.getMinutes(), //minute
		 "s+" : this.getSeconds(), //second
		 "q+" : Math.floor((this.getMonth()+3)/3),  //quarter
		 "S" : this.getMilliseconds() //millisecond
		 }
		 if(/(y+)/.test(format)) format=format.replace(RegExp.$1,
		 (this.getFullYear()+"").substr(4 - RegExp.$1.length));
		 for(var k in o)if(new RegExp("("+ k +")").test(format))
		 format = format.replace(RegExp.$1,
		 RegExp.$1.length==1 ? o[k] :
		 ("00"+ o[k]).substr((""+ o[k]).length));
		 return format;
	}
</script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>常规订单超时统计汇总表</title>
<script type="text/javascript">
function doInit(){
	loadcalendar();
	if(document.getElementById("orgCode").value ==""&&document.getElementById("dealerCode").value ==""){
		document.getElementById("button4").disabled=false;
		document.getElementById("button4clearbutton").disabled=false;
		document.getElementById("orgbuclearbutton").disabled=true;
		document.getElementById("orgbu").disabled=true;
	} else if(document.getElementById("orgCode").value !=""){
		document.getElementById("button4").disabled=true;
		document.getElementById("button4clearbutton").disabled=true;
		document.getElementById("orgbuclearbutton").disabled=false;
		document.getElementById("orgbu").disabled=false;
	}else{
		document.getElementById("button4").disabled=false;
		document.getElementById("button4clearbutton").disabled=false;
		document.getElementById("orgbuclearbutton").disabled=true;
		document.getElementById("orgbu").disabled=true;
	}
}
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车销售管理 > 整车销售管理 > 常规订单超时统计汇总表</div>
<form method="POST" name="FRM" id="FRM">
<input type="hidden" name="file" value="commonXml/Strage2Statu.xml"></input>
<table class="table_edit">
	<tr>
 		  <td align="right" nowrap ></td>
	      <td align="right">起止时间：</td>
	      <td>
	        <select name="startYear" id="startYear">
	        	<option value="${year-1}">${year-1}</option>
	        	<option value="${year}" selected="selected">${year}</option>
	        	<option value="${year+1}">${year+1}</option>
	        </select> 年
	        <select name="startMonth" id="startMonth">
	        	<%
	        		for(int i=1;i<13;i++){
	        			if(i == month) {
     			%>
     					<option value="<%=i %>" selected="selected"><%=i %></option>
     			<%
	        				
	        			} else {
	        	%>
	        			
	        			<option value="<%=i %>"><%=i %></option>
	        	<%
	        			}
	        		}
	        	%>
	        </select> 月
	        &nbsp;至&nbsp;
	        <select name="endYear" id="endYear">
	        	<option value="${year-1}">${year-1}</option>
	        	<option value="${year}" selected="selected">${year}</option>
	        	<option value="${year+1}">${year+1}</option>
	        </select> 年
	        <select name="endMonth" id="endMonth">
	        	<%
	        		for(int i=1;i<13;i++){
	        			
	        			if(i == month) {
     			%>
     					<option value="<%=i %>" selected="selected"><%=i %></option>
     			<%
	        				
	        			} else {
	        	%>
	        			
	        			<option value="<%=i %>"><%=i %></option>
	        	<%
	        			}
	        		}
	        	%>
	        </select> 月
	        <font color="red">*</font>
	      </td>
	      <td>
			<c:if test="${dutyType eq dealerType}">
				<!-- <input name="submitBtn" type=button class="cssbutton" onClick="submitFRM();" value="查询"> -->
				<input name="downloadBtn" type=button class="cssbutton" onClick="download();" value="下载">
			</c:if>
		</td>
	 </tr>    
	<%-- <tr>
		<td  align="right" nowrap ></td>
		<td  align="right" nowrap >起止日期：</td>
		<td class="table_list_th">
			<input type="hidden" name="aaa" size="15" id="aaa" value="" />
		    <input name="startTime" id="startTime"  readonly="readonly" value="${startTime}" type="text" class="short_txt" datatype="1,is_date,10" group="startTime,endTime" hasbtn="true" callFunction="showcalendar(event, 'startTime', false);">
		    &nbsp;至&nbsp;
		    <input name="endTime" id="endTime"   readonly="readonly" value="${endTime}" type="text" class="short_txt" datatype="1,is_date,10" group="startTime,endTime" hasbtn="true" callFunction="showcalendar(event, 'endTime', false);">
		    <font color="red">*</font>
	    </td>
		<td>
			<c:if test="${dutyType eq dealerType}">
				<!-- <input name="submitBtn" type=button class="cssbutton" onClick="submitFRM();" value="查询"> -->
				<input name="downloadBtn" type=button class="cssbutton" onClick="download();" value="下载">
			</c:if>
		</td>
	</tr> --%>
	<c:if test="${dutyType eq companyType || dutyType eq deptType}">
	<tr>
		<td  align="right"></td>
		<td align="right"><input type="radio" id="odtype" name="odtype" value="0" onclick="checkOrgType()" checked="checked"></input>选择区域：</td>
		<td class="table_list_th">
		<input type="hidden"  name="orgId" size="15" value=""  id="orgId" class="middle_txt" datatype="1,is_noquotation,75" />
		<input type="text"  readonly="readonly"  name="orgCode" size="15" value=""  id="orgCode" class="middle_txt" datatype="1,is_noquotation,75" />
		<input name="orgbu"  id="orgbu" type="button" class="mark_btn" onclick="showOrg('orgCode','orgId','false')" value="..." />
		<input type="button" name="orgbuclearbutton" id="orgbuclearbutton" class="cssbutton" value="清除" onClick="toClearOrgs();"/>
		</td>
		<td align="left" nowrap="nowrap">
			&nbsp;
		</td>
	</tr>
	</c:if>
	<c:if test="${dutyType != dealerType}">
	<tr>
		<td  align="right"></td>
		<td align="right"><input type="radio" id="odtype" name="odtype" value="0" onclick="checkType()" checked="checked"></input>选择经销商：</td>
		<td class="table_list_th"><input type="hidden" name="dealerId" size="15" id="dealerId" value="" />
			<input type="text" class="middle_txt" readonly="readonly" name="dealerCode" size="15" id="dealerCode" value="" />
			<input name="button4" type="button"  class="mini_btn" onclick="showOrgDealer('dealerCode','dealerId','true');" value="..." />
			<input type="button" name="button4clearbutton" id="button4clearbutton" class="cssbutton" value="清除" onClick="toClearDealers();"/>
		</td>
		<td>
			<!-- <input name="submitBtn" type=button class="cssbutton" onClick="submitFRM();" value="查询"> -->
			<input name="downloadBtn" type=button class="cssbutton" onClick="download();" value="下载">
		</td>
	</tr>
	</c:if>
	<tr>
	</tr>
</table>
</form>
</body>
</html>