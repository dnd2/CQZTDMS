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
<title>经销商库龄分析报表（分区域）</title>
<script type="text/javascript">
function toClearOrgs(){
	document.getElementById("orgCode").value="";
	document.getElementById("orgId").value="";
}

function doInit(){
	loadcalendar();
}

function submitFRM(){
	document.FRM.action='<%=request.getContextPath()%>/server2';
	document.FRM.submit();
}

function txtClr(valueId) {
	document.getElementById(valueId).value = '' ;
	document.getElementById("dealerId").value="";
}
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车销售报表 > 整车销售报表 > 集团客户合同表</div>
<form method="POST" name="FRM" id="FRM">
<input type="hidden" name="file" value="commonXml/FLEETCONTRACT.xml"></input>
<table class="table_edit">
<tr>
	    <td  align="right" nowrap ></td>
	    <td  align="right" nowrap >审核日期：</td>
	    <td class="table_list_th">
	    <input type="hidden" name="aaa" size="15" id="aaa" value="" />
       		<input name="checkDate1" id="t1"  readonly="readonly" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);">
       		&nbsp;至&nbsp;
       		<input name="checkDate2" id="t2"   readonly="readonly" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);">
        </td>
	   <td align="left" nowrap="nowrap">
			<input name="button2" type=button class="cssbutton" onClick="submitFRM();" value="查询">	
		</td>
    </tr>
</table>
</form>
</body>
</html>