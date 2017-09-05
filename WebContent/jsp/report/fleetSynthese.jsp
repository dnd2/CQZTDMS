<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>集团客户综合查询</title>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/funccommon/activityfunc.js"></script>
<script type="text/javascript">
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
function doInit(){
	_setDate_("startTime", "endTime", "1", "0") ;
	loadcalendar();
	genLocSel('txt1','','','','',''); // 加载省份城市和县
	if(document.getElementById("orgCode").value ==""&&document.getElementById("dealerCode").value ==""){
	document.getElementById("button4").disabled=true;
	document.getElementById("button4clearbutton").disabled=true;
		document.getElementById("orgbuclearbutton").disabled=false;
		document.getElementById("orgbu").disabled=false;
	}else if(document.getElementById("orgCode").value !=""){
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

function submitFRM(){
	document.FRM.action='<%=request.getContextPath()%>/report/FleetSyntheseReport/getFleetSyntheseReport.do';
	document.FRM.target = "_blank";
	document.FRM.submit();
}

function txtClr(valueId) {
	document.getElementById(valueId).value = '' ;
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

//下载
function download(){
	var cur_date = new Date();
	var cur_time = cur_date.format('yyyy-MM-dd');
	var endTime = document.getElementById("endTime").value;
	var startTime = document.getElementById("startTime").value;
	if(startTime > endTime){
		MyAlert('截止日期不能超过当前日期！');
	}else if(startTime == '' && endTime == ''){
		document.getElementById("endTime").value = cur_time;
		document.FRM.action='<%=request.getContextPath()%>/report/FleetSyntheseReport/FleetSyntheseReportdownload.json';
		document.FRM.target="_self";
		document.FRM.submit();
	}else{
		document.FRM.action='<%=request.getContextPath()%>/report/FleetSyntheseReport/FleetSyntheseReportdownload.json';
		document.FRM.target="_self";
		document.FRM.submit();
	}
}
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车销售报表 > 整车销售报表 > 集团客户综合查询</div>
<form method="POST" name="FRM" id="FRM">
<input type="hidden" name="file" value="commonXml/FleetSales.xml"></input>
<table class="table_edit">
	 <tr>
	    <td  align="right" nowrap >报备确认日期：</td>
	    <td class="table_list_th">
	   		<input type="hidden" name="aaa" size="15" value="" />
       		<input name="startTime" id="startTime"  readonly="readonly" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 'startTime', false);">
       		&nbsp;至&nbsp;
       		<input name="endTime" id="endTime"   readonly="readonly" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 'endTime', false);">
        </td>
        <td></td>
    </tr>
    <tr>
		<td align="right"><div align="right">省份：</div></td>
		<td class="table_list_th">
			<input type="hidden" name="aaa" size="15" value="" />
			<select class="min_sel" id="txt1" name="region"></select>
		</td>
		<td></td>
	</tr>
	<tr>
	    <td  align="right" nowrap >集团客户名称：</td>
	    <td class="table_list_th">
		    <input type="hidden" name="aaa" size="15" value="" />
			<input type="text" class="middle_txt" name="fleetName" size="15" id="fleetName" value="" />
		</td>
		<td></td>
    </tr>
    <tr>
		<td align="right"><input type="radio" id="odtype" name="odtype" value="0" onclick="checkType()" checked="checked"></input>经销商：</td>
		<td class="table_list_th"><input type="hidden" name="dealerId" size="15" id="dealerId" value="" />
			<input type="text" class="middle_txt" readonly="readonly" name="dealerCode" size="15" id="dealerCode" value="" />
			<input name="button4" type="button"  class="mini_btn" onclick="showOrgDealer('dealerCode','dealerId','true','','true');" value="..." />
			<input type="button" name="button4clearbutton" id="button4clearbutton" class="cssbutton" value="清除" onClick="txtClr('dealerCode');"/>
		</td>
		<td></td>
	</tr>
	
	<tr>
		<td align="right">
			<input type="radio" id="odtype" name="odtype" value="0" onclick="checkOrgType()" checked="checked"></input>
			选择区域：
		</td>
		<td class="table_list_th">
			<input type="hidden"  name="orgId" size="15" value=""  id="orgId" class="middle_txt" datatype="1,is_noquotation,75" />
			<input type="text"  readonly="readonly"  name="orgCode" size="15" value=""  id="orgCode" class="middle_txt" datatype="1,is_noquotation,75" />
			<input name="orgbu"  id="orgbu" type="button" class="mark_btn" onclick="showOrg('orgCode','orgId','false')" value="..." />
			<input type="button" name="orgbuclearbutton" id="orgbuclearbutton" class="cssbutton" value="清除" onClick="toClearOrgs();"/>
		</td>
		<td colspan="4" align="center" nowrap="nowrap">
			<input name="button2" type=button class="cssbutton" onClick="submitFRM();" value="查询">
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input name="downloadBtn" type=button class="cssbutton" onClick="download();" value="下载">	
		</td>
	</tr> 
</table>

</form>
</body>
</html>