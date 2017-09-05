<%@ page import="com.infodms.dms.po.TcTaskInfoPO" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
	TcTaskInfoPO showBean = (TcTaskInfoPO)request.getAttribute("SHOW_BEAN");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>定时任务</title>
</head>

<body>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：
	系统管理 &gt; 定时任务 &gt; 定时任务管理 </div>
<form id="fm" method="post">
<table class="table_info" border="0">
	    <th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;任务信息</th>
	<tr>
		<td nowrap="nowrap" class="table_info_2col_label_4Letter">任务名称：</td>
      	<td nowrap="nowrap" class="table_info_2col_input" >
        <%=showBean.getActionName()!=null? showBean.getActionName():""%>
		</td>
	</tr>
	<tr>
		<td nowrap="nowrap" class="table_info_2col_label_4Letter" >优先级：</td>
    <td nowrap="nowrap" class="table_info_2col_input">
	   <input id="pri" datatype="0,is_digit,1,1" name="TASK_PRI" type="text" class="middle_txt" value="<%=showBean.getTaskPriority()!=null? showBean.getTaskPriority().toString():""%>" />(0-9)<font color="red">   *</font></td>
	<td nowrap="nowrap" class="table_info_2col_label_4Letter">控制方式：</td>
	  <td nowrap="nowrap" class="table_info_2col_input" ><span class="table_query_3Col_input">
	    <select class="short_sel" name="TASK_MAN" id="TASK_MAN">
		  <option value="MANUAL" <%if( showBean.getTaskManual().equals("MANUAL")){%>selected<%} %>>MANUAL</option>
		  <option value="SYSTEM" <%if( showBean.getTaskManual().equals("SYSTEM")){%>selected<%} %>>SYSTEM</option>
        </select>
	  </span><font color="red">    *</font></td>
	</tr>
	<tr>
	  <td nowrap="nowrap" class="table_info_2col_label_4Letter" >持续时间：</td>
	  <td nowrap="nowrap" class="table_info_2col_input"><input maxlength="8" id="dur" datatype="0,is_digit,5,1" name="TASK_DUR" type="text" class="middle_txt" value="<%=showBean.getTaskDuration()!=null? showBean.getTaskDuration().toString():""%>" />(秒)<font color="red">   *</font></td>
	  <td nowrap="nowrap" class="table_info_2col_label_4Letter" >任务描述：</td>
	  <td nowrap="nowrap" class="table_info_2col_input"><textarea id="TASK_DESC" datatype="1,is_textarea,100" name="TASK_DESC" cols="30" rows="3" ><%=showBean.getActionDesc()!=null? showBean.getActionDesc().toString():""%></textarea></td>
  	</tr>
</table>
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;'></div>
<br/>
<table  class="table_edit_button">
	<tr >
		<td  nowrap="nowrap" >
			<input type="hidden" name="taskId" id="taskId" value="<%=showBean.getTaskId()%>"/>
			<input id="saveBtn" class="normal_btn" type="button" value="保 存"  onclick="saveCutomer();"/>
			<input class="normal_btn" type="button" value="取 消" onclick="goBack();"/>		
		</td>
	</tr>
</table>
</form>
<script type="text/javascript" >
	function saveCutomer(){
		if(submitForm('fm')){
				disableBtn($("saveBtn"));
				makeFormCall('<%=request.getContextPath()%>/sysmng/task/Task/taskSave.json',showResult,'fm');
		}else{
			return;
		}
	}
	function showResult(json){
		if(json.ACTION_RESULT == '1'){
			window.location.href = '<%=request.getContextPath()%>/sysmng/task/Task/taskSearch.do?';
		}
	}
	function goBack(){
		javascript:history.go(-1);
	}
</script>
</div>
</body>
</html>