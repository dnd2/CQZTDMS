<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
</head>
<body onunload='javascript:destoryPrototype()' onload="__extQuery__(1)">
<div class="wbox">
	<div class="navigation">
		<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统管理 &gt; 定时任务 &gt; 定时任务管理
	</div>
<form id="fm" >
	<input id="queryBtn" type="hidden" onclick="__extQuery__(1);"/>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
</form>

<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</div>
</body>
</html>
<script type="text/javascript" >
	var url = "<%=contextPath%>/sysmng/task/Task/taskSearch.json";

	//设置表格标题
	var title= "定时任务查看";
	
	var columns = [
	               	{header: "序号", renderer:getIndex},
					{header: "任务名称", dataIndex: 'actionName'},
					{header: "优先级", dataIndex: 'taskPriority'},
					{header: "控制方式", dataIndex: 'taskManual'},
					{header: "事务持续时间",dataIndex: 'taskDuration'},
					{header: "状态",dataIndex: 'taskStatus',renderer:getTaskStatus},
					{id:'action',header: "操作", sortable: true,dataIndex: 'taskId',renderer:myLink}
				  ];		   
	function getTaskStatus(val) {
		return val==1 ? "有效" : "无效";
	}
	//设置超链接
   function myLink(value,meta,record){
	   var str = "";
       str += "<a href=\"<%=contextPath%>/sysmng/task/Task/taskPlanSearch.do?taskId="+value+"\">[明细]</a>"
        +"<a href=\"<%=contextPath%>/sysmng/task/Task/taskUpdate.do?taskId="+value+"\">[修改]</a>"
        +"<a href=\"<%=contextPath%>/sysmng/task/Task/taskStatusUpdate.do?taskId="+value+"&taskStatus="+record.data.taskStatus+"\">"+(record.data.taskStatus==1?"[停用]":"[启用]")+"</a>"  
        return String.format(str);
    }
</script>