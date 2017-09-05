<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<%
String contextPath=request.getContextPath();
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>定时任务时间设置 </title>
</head>
<body>
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />当前位置：&nbsp;系统管理 &gt; 系统业务参数维护 &gt; 定时任务时间设置
	</div>
<form name="fm" method="post" id="fm">
<!-- 查询条件 begin -->
<table class="table_list" style="margin-top: 1px; margin-bottom: 1px;">
		 <tr class="table_list_row2">
		   <td align="right">定时任务名称：</td> 
	  	   <td align="left" colspan="3">${valueMap.ACTION_DESC }</td> 
	    </tr>
	    <tr class="table_list_row2">
		   <td align="right">时间间隔：</td> 
	  	   <td align="left">
	  	   <input type="text" id="TASK_INTERVAL" name="TASK_INTERVAL" datatype="0,isDigit,3" class="middle_txt" value="${valueMap.TASK_INTERVAL}"/>
	  	   </td> 
	    </tr>
	    <tr class="table_list_row2">
	  	   <td align="right">单位：</td> 
	  	   <td align="left">
	  	   <select id="PLAN_REPEAT_TYPE" name="PLAN_REPEAT_TYPE">
	  	   		<option value="MINUTE">分</option>
	  	    	<option value="HOUR">小时</option>
	  	     	<option value="DAY">天</option>
	  	   </select>
	  	   </td> 
	    </tr>
	    <tr class="table_list_row2">
	  	   <td align="center" colspan="2">
	  	   <input type="hidden" id="PLAN_ID" name="PLAN_ID" class="middle_txt" value="${valueMap.PLAN_ID}"/>
	  	   <input type="button" class="normal_btn" onclick="saveTaskTime()" value=" 保存  " /> 
	  	     <input type="button" class="normal_btn" onclick="_hide();" value=" 返回  " /> 
	  	   </td> 
	    </tr>
</table>
</form>
<!--页面列表 begin -->
<script type="text/javascript" >
function saveTaskTime(){
	//MyConfirm("确认设置！",saveTT);
	saveTT();
}
function saveTT()
{ 
	makeNomalFormCall("<%=contextPath%>/sysbusinesparams/businesparamsmanage/TaskTimeSet/saveTaskTime.json",SaveBack,'fm','queryBtn'); 
}

function SaveBack(json)
{
	if(json.returnValue == 1)
	{
		_hide();
		parent.MyAlert("操作成功！");
		parentContainer.getQuery();
	}else
	{
		_hide();
		parent.MyAlert("操作失败！请联系系统管理员！");
	}
}
function doInit(){
	document.getElementById("PLAN_REPEAT_TYPE").value='${valueMap.PLAN_REPEAT_TYPE}';	
}
</script>
</body>
</html>
