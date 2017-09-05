<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.bean.TtAsActivityBean" %>
<%@page import="java.util.List" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>服务活动设定-车龄定义列表</title>
<%
	String contextPath = request.getContextPath();
%>
<%
	List<TtAsActivityBean> list = (List<TtAsActivityBean>) request.getAttribute("VehicleAgeList");
%>
<script language="JavaScript" type="text/javascript">
	//日历控件
	function doInit(){
		loadcalendar();
		showControl();
		defaultInit();
	}
	function showControl(){
		var beforeVehicle='<%=request.getAttribute("beforeVehicle") %>';
		if(<%=Constant.SERVICEACTIVITY_VEHICLE_AREA_01%>==beforeVehicle)
			$('saleDate').style.display = 'none' ;
	}
	// 如果有值,则初始化
	function defaultInit(){
		var b1 = '<%=request.getParameter("b1")%>';
		var b2 = '<%=request.getParameter("b2")%>';
		var e1 = '<%=request.getParameter("e1")%>';
		var e2 = '<%=request.getParameter("e2")%>';
		if('${type}' == '1')
		{
		   if(b1&&b1!='null')
		   { 
		   		$('beginDate').value = b1 ;
		   }else
		   {
		      $('beginDate').disabled = 'disabled';
		      $('beginDate').callFunction = '';
		   }
		    	
			if(b2&&b2!='null')
			{
				$('beginDate2').value = b2 ;
			}
			else
			{
				$('beginDate2').disabled = 'disabled';
				 $('beginDate2').callFunction = '';
			}	
			if(e1&&e1!='null')
			{
				$('endDate').value = e1 ;
			}else
			{
				$('endDate').disabled = 'disabled';
				$('endDate').callFunction = '';
			}	
			if(e2&&e2!='null')
			{
				$('endDate2').value = e2 ;
			}else
			{
			   $('endDate2').disabled = 'disabled';
			   $('endDate2').callFunction = '';
			}	
		}else
		{
		  if(b1&&b1!='null')
			$('beginDate').value = b1 ;
			if(b2&&b2!='null')
				$('beginDate2').value = b2 ;
			if(e1&&e1!='null')
				$('endDate').value = e1 ;
			if(e2&&e2!='null')
				$('endDate2').value = e2 ;
		}
		
	}
	function sures(){
	    var b1 = '<%=request.getParameter("b1")%>';
		var b2 = '<%=request.getParameter("b2")%>';
		var e1 = '<%=request.getParameter("e1")%>';
		var e2 = '<%=request.getParameter("e2")%>';
		if(!submitForm('FRM')) {
			return false;
		}	
		if(document.getElementById('beginDate').value.length==0 && document.getElementById('endDate').value.length==0 &&document.getElementById('beginDate2').value.length==0 && document.getElementById('endDate2').value.length==0){
		_hide();
		return false;
		}
		if('${type}' == '1')
		{
		    if(b1 != '')
		    {
		    	 if( $('beginDate').value > b1 ||  $('endDate').value < e1)
			    {
			       MyAlert('提报后的修改的生产日期必须比提报时的广!');
			       return false;
			    } 
		    }
		   
		    if(b2 != '')
		    {
		       if($('beginDate2').value > b2 ||  $('endDate2').value < e2)
			    {
			    	MyAlert('提报后的修改的销售日期必须比提报时的广!');
			    	return false;
			    }
		    
		    }
		       document.FRM.action = "<%=contextPath%>/claim/serviceActivity/ServiceActivityManageVehicleAge/serviceActivityVhclAgeDefine.do";
		 	   document.FRM.submit();
		}else
		{
		  document.FRM.action = "<%=contextPath%>/claim/serviceActivity/ServiceActivityManageVehicleAge/serviceActivityVhclAgeDefine.do";
		  document.FRM.submit();
		}
		
		
		
	}
</script>
</head>

<body>
<div class="navigation">
	<img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;服务活动管理&gt;服务活动管理&gt;车龄定义
</div>
<form name="FRM" id="FRM">
<input type="hidden" name="activityId" id="activityId" value="<%=request.getAttribute("activityId") %>" />

<table class="table_edit">
	<tr>
		<td width="10%" align="right" height="30">生产日期：</td>
		<td width="20%">
			<input type="text" class="short_txt" name="beginDate" id="beginDate"  datatype="1,is_date,10" group="beginDate,endDate" hasbtn="true" callFunction="showcalendar(event, 'beginDate', false);"/>
            至
            <input type="text" class="short_txt" name="endDate" id="endDate"  datatype="1,is_date,10" group="beginDate,endDate" hasbtn="true" callFunction="showcalendar(event, 'endDate', false);"/>
		</td>
	</tr>
	<tr id="saleDate">
		<td width="10%" align="right" height="30">销售日期：</td>
		<td width="20%">
			<input type="text" class="short_txt" name="beginDate2" id="beginDate2"  datatype="1,is_date,10" group="beginDate2,endDate2" hasbtn="true" callFunction="showcalendar(event, 'beginDate2', false);"/>
            至
            <input type="text" class="short_txt" name="endDate2" id="endDate2"  datatype="1,is_date,10" group="beginDate2,endDate2" hasbtn="true" callFunction="showcalendar(event, 'endDate2', false);"/>	
		</td>
	</tr>
</table>
<br />

<table class="table_edit">
	<tr>
		<td align="center">
			<input type="button" value="确定" class="normal_btn" onclick="sures();"/>
			&nbsp;&nbsp;
			<input type="button" value="关闭" class="normal_btn" onclick="_hide();"/>
		</td>
	</tr>
</table>
</form>
</body>
</html>