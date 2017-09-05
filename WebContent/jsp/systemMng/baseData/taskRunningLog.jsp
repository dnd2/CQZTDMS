<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.infodms.dms.common.Constant"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>定时任务</title>
</head>
<body onunload='javascript:destoryPrototype()' onload="__extQuery__(1);loadcalendar();">
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置
	系统管理 &gt;定时任务&gt; 任务运行日志
</div>
<form method="post" id="fm">
<table class="table_query" border="0">
	<tr>
		<td class="table_query_3Col_label_6Letter   nowrap="nowrap">任务名称</td>
		<td class="table_query_3Col_input"   nowrap="nowrap">
			<input class="middle_txt"  type="text" name="taskName" id="taskName"  />		</td>
		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">执行类型</td>
		<td class="table_query_3Col_input" nowrap="nowrap"><select class="short_sel" name="runType" id="runType">
          <option value="">请选择</option>
          <option value="MANUAL">手工</option>
          <option value="SYSTEM">自动</option>
                </select></td>
		<td class="table_query_3Col_label_5Letter" nowrap="nowrap">执行结果</td>
		<td class="table_query_3Col_input" nowrap="nowrap">
			<select class="short_sel" name="runResult" id="runResult">
			  <option value="">请选择</option>
			  <option value="1">成功</option>
			  <option value="0">失败</option>
			  <option value="-1">执行被忽略</option>
			  <option value="2">未执行</option>
			</select>		</td>
	</tr>
	<tr>
		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">计划开始时间：</td>
		<td class="table_query_3Col_input" nowrap="nowrap" >
			<input id="startPlanTime" name="startPlanTime" style="width: 130px;" class="short_txt" type="text"  />
			<input  class="time_ico" value=" " type="button" onClick="showcalendar(event, 'startPlanTime', false);"/>
		</td>
		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">实际开始时间：</td>
		<td class="table_query_3Col_input" style="width:200px;" nowrap="nowrap" >
			<input id="startTime" name="startTime" datatype="1,is_date,<%=Constant.Length_Check_Char_10 %>"  group="startTime,endTime"  class="short_txt" type="text"  />
			<input  class="time_ico" value=" " type="button" onClick="showcalendar(event, 'startTime', false);"/>至
			<input id="endTime" name="endTime"  datatype="1,is_date,<%=Constant.Length_Check_Char_10 %>"  group="startTime,endTime"  class="short_txt" type="text"  />
			<input  class="time_ico" value=" " type="button" onClick="showcalendar(event, 'endTime', false);"/>
		</td>
		<td class="table_query_3Col_label_5Letter" nowrap="nowrap">是否可跳过：</td>
		<td class="table_query_3Col_input" nowrap="nowrap"><select class="short_sel" name="ingoreFlag" id="ingoreFlag">
          <option value="">请选择</option>
          <option value="0">是</option>
          <option value="1">否</option>
        </select></td>
	</tr>
	<tr class="table_query_last">
	  <td colspan="6">
                   <input class="normal_btn" id="queryBtn" type="button" value="查询" onclick="__extQuery__(1);"/>  
				   <input type="button" class="normal_btn" onclick="requery()" value="重置"/> &nbsp;      
	 </td>		
	</tr>
</table>
<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
</form>
<br><br>
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</div>
</body>
</html>
<script type="text/javascript" >
	var url = "<%=contextPath%>/sysmng/task/TaskSearch/taskSearch.json?COMMAND=1";
	//设置表格标题
	var title = "任务日志信息";
	
	var columns = [
					{header: "序号", renderer:getIndex},
					{header: "任务名称",dataIndex: 'taskName',orderCol:"ACTION_NAME"},
					{header: "计划编号",dataIndex: 'planId',orderCol:"PLAN_ID"},
					{header: "计划开始时间",dataIndex: 'planStatrTime',orderCol:"PLAN_START_TIME"},
					{header: "实际开始时间",dataIndex: 'realStartTime',orderCol:"REAL_START_TIME"},
					{header: "实际结束时间",dataIndex: 'realEndTime',orderCol:"REAL_END_TIME"},
					{header: "跳过",dataIndex: 'ignoreFlag',orderCol:"IGNORE_FLAG"},
					{header: "结果",dataIndex: 'executeResult',orderCol:"EXECUTE_RESULT"},
					{header: "操作",dataIndex: 'logId',renderer:myLink}
				  ];
	//设置超链�?
   function myLink(value){
	   var str = "";
       str += "<a href=\"<%=contextPath%>/sysmng/task/TaskSearch/taskDetailSearch.do?logId="+value+"\">[明细]</a>"
        return String.format(str);
    }
   function requery() {
		$('taskName').value="";
		$('runType').value="";
		$('runResult').value="";
		$('ingoreFlag').value="";
		$('startTime').value="";
		$('startPlanTime').value="";
		$('endTime').value="";
	}
</script>
