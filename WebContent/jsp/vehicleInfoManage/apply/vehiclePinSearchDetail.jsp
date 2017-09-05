<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>车辆信息变更申请</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
	function clrDlr(){
	$('dealerCode').value="";
	$('dealerId').value="";
	}
</script>
</head>
<body>
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置:售后服务管理&gt;车辆信息管理&gt;经销商PIN码查询记录
</div>
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center"  class="table_query">
	<tr>
		<td  align="right">经销商名称：</td>
      	<td><input name="dealerName" type="text" id="dealerName" maxlength="20" class="middle_txt"/></td>
		<td >经销商：</td>
             <td nowrap="nowrap" >
           			<input name="dealerCode" id="dealerCode" type="text" class="middle_txt" readonly="readonly" />
            		<input class="mini_btn" type="button" id="dealerBtn" value="&hellip;" onclick="showOrgDealer('dealerCode','dealerId','true','','true','','10771002');"/> 
           			<input type="hidden" name="dealerId" id="dealerId" value=""/>    
           			<input class="normal_btn" type="button" value="清空" onclick="clrDlr();"/>
  			</td>
    </tr>
    <tr>
      <td align="right">经销商查询时间：</td>
      <td align="left" nowrap="true" colspan="3">
			<input name="PIN_CREATE_DATE" type="text" class="short_time_txt" id="PIN_CREATE_DATE" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'PIN_CREATE_DATE', false);" />  	
             &nbsp;至&nbsp; <input name="PIN_END_DATE" type="text" class="short_time_txt" id="PIN_END_DATE" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'PIN_END_DATE', false);" /> 
		</td>	
		          	
    </tr>
    <tr>
    	<td align="center" colspan="6">
    		<input type="button" name="BtnQuery" id="queryBtn"  value="查询"  class="normal_btn" onClick="__extQuery__(1)" >
    	</td>
    </tr>
</table>
<!-- 查询条件 end -->
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
	var url = "<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/dealerPinSearch2.json";
	var title = null;
	var columns = [
				{header: "经销商代码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "经销商名称", dataIndex: 'DEALER_NAME', align:'center'},
				{header: "VIN", dataIndex: 'VIN', align:'center'},
				{header: "查询时间", dataIndex: 'SEARCH_DATE', align:'center'},
				{header: "查询结果", dataIndex: 'PIN_NO', align:'center'}
		      ];
		    	
</script>
<!--页面列表 end -->
</body>
</html>