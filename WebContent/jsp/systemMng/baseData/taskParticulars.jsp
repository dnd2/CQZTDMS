<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.po.TcTaskInfoPO" %>
<%@ page import="com.infodms.dms.po.TcTaskPlanPO" %>
<%
	String contextPath = request.getContextPath();
	TcTaskInfoPO showBean = (TcTaskInfoPO)request.getAttribute("TaskInfo_BEAN");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>定时任务</title>
</head>

<body onunload='javascript:destoryPrototype()' onload="__extQuery__(1)">
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：
	系统管理 &gt; 定时任务 &gt; 定时任务管理 </div>
<table class="table_info" border="0">
	    <th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;任务信息</th>
	<tr>
		<td nowrap="nowrap" class="table_info_3col_label_4Letter">任务名称：</td>
        <td nowrap="nowrap" class="table_info_3col_input" ><%=showBean.getActionName()!=null? showBean.getActionName():""%></td>
		<td nowrap="nowrap" class="table_info_3col_label_4Letter" >优先级：</td>
		<td nowrap="nowrap" class="table_info_3col_input"><%=showBean.getTaskPriority()!=null? showBean.getTaskPriority().toString():""%></td>
		<td nowrap="nowrap" class="table_info_3col_label_4Letter" >任务状态：</td>
		<td nowrap="nowrap" class="table_info_3col_input"><%=showBean.getTaskStatus().intValue()!=0? "有效":"无效"%></td>
	</tr>
</table>
<br/><br/>
<form method="post" id="fm">
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<table  class="table_edit_button">
	<tr >
		<td  nowrap="nowrap" >
			<input id="saveBtn" class="normal_btn" type="button" value="新增"  onclick="window.location.href='<%=request.getContextPath()%>/sysmng/task/Task/taskPlanAdd.do?taskId=<%=showBean.getTaskId() %>'"/>
			<input class="normal_btn" type="button" value="返回" onclick="window.location.href='<%=request.getContextPath()%>/sysmng/task/Task/taskSearchInit.do'"/><input id="queryBtn" type="hidden" onclick="__extQuery__(1);"/>		
		</td>
	</tr>
</table>
</form>
</div>
</body>
</html>
<script type="text/javascript" >
	var url = "<%=contextPath%>/sysmng/task/Task/taskPlanSearch.json?taskId=<%=showBean.getTaskId()!=null? showBean.getTaskId():""%>";
	//设置表格标题
	var title = "计划信息";
	
	var columns = [
					{header: "序号", renderer:getIndex},
					{header: "计划编号", dataIndex: 'planId'},
					{header: "优先级",dataIndex: 'planPriority'},
					{header: "类型",dataIndex: 'planRepeatType'},
					{header: "开始时间",dataIndex: 'planStart'},
					{header: "结束时间",dataIndex: 'planEnd'},
					{header: "是否可跳过", dataIndex: 'planIgnoreFlag'},
					{header: "执行间隔",dataIndex: 'taskInterval'},
					{header: "启动超时时间",dataIndex: 'planStartTimeout'},
					{header: "模式",dataIndex: 'planPattern'},
					{header: "状态",dataIndex: 'planStatus'},
					{id:'action',header: "操作",dataIndex: 'planId',renderer:myLink}
				  ];
			      
	//设置超链接
   function myLink(value){
	   var str = "";
       str += "<a href=\"<%=contextPath%>/sysmng/task/Task/taskPlanUpdate.do?planId="+value+"\">[修改]</a>"
        return String.format(str);
    }
   
</script>
