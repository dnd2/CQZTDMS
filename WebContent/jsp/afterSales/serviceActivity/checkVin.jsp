<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<%
	String contextPath = request.getContextPath();
    String activityId = request.getParameter("activityId");
    String status = request.getParameter("status");
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=7">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/AfterSales/serviceActivity/checkVin.js"></script>
<title>VIN选择查看</title>
<script type="text/javascript">
//选择车型开始
var myPage;

var url ="<%=contextPath%>/afterSales/serviceActivity/ServiceActivityAction/getActivityVin.json";
			
var title = null;
var columns = [
            {header : "序号",align : 'center',renderer : getIndex}, 
			{header: "操作", dataIndex: 'ID', align:'center',renderer:myLink},
			{header: "VIN", dataIndex: 'VIN', align:'center'}
	      ];
function myLink(value, meta, record) {
	var status = document.getElementById("status").value;
	if(status == 1){
		return String.format("<a href='#' onclick='deleteData("+value+")' id=''>[删除]</a>");
	}else{
		return String.format("");
	}
	
}

/**
 * 删除
 * @param id
 */
function deleteData(value){
	makeNomalFormCall("<%=contextPath%>/afterSales/serviceActivity/ServiceActivityAction/deleteVinData.json?id="+value,deleteDataResult,"fm");
}
function deleteDataResult(obj){
	 if(obj.msg=="01"){
		 	MyAlert("删除成功");
		 	__extQuery__(1);
		   //MyAlert("删除成功！",__extQuery__(1));
	   }else{
		   MyAlert("删除失败");
	   }
}


function addVin(){
	var status = document.getElementById("status").value;
	var activityId = document.getElementById("activityId").value;
	window.location.href = "<%=contextPath%>/afterSales/serviceActivity/ServiceActivityAction/addVin.do?activityId="+activityId;
}
$(document).ready(function(){__extQuery__(1);});
</script>
</head>
<body >
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 查看VIN</div>
<form method="post" name ="fm" id="fm" >
<div class="form-panel">
				<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
				<div class="form-body">
	<input id="activityId" name="activityId" type="hidden" value="<%=activityId%>"/>	
	<input id="status" name="status" type="hidden" value="<%=status%>"/>	
	<table class="table_query">
    <tr >
      <td style="text-align: center">
      	vin：<input name="vin" type="text" id="vin"  class="middle_txt"/>
      </td>
  	</tr>
  	<br>
  	<tr >
  	    <td colspan="4" style="text-align: center">
  	       <%if(status.equals("1")){ %>
  	    	 <input type="button" name="queryBtn" id="queryBtn"  value="新增"  class="u-button u-submit" onClick="addVin();" >
  	      <% }
  	       %>
 
      	<input type="button" name="queryBtn" id="queryBtn"  value="查询"  class="u-button u-query" onClick="__extQuery__(1);" >
        <input type="button" value="关 闭" class="u-button u-cancel" onclick="_hide();" />
      </td>
  	</tr>
</table>
	</div>
</div>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
</div>
</body>
</html>