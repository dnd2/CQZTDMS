<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>

<%
	String contextPath = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>定时任务时间设置</title>
</head>
<body onunload='javascript:destoryPrototype();'> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  系统管理 &gt; 系统业务参数维护 &gt; 定时任务时间设置</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<table class="table_query" border="0">
			<tr>
				<td width="20%" class="tblopt"><div align="right">定时任务名称：</div></td>
				<td width="20%" >
      				<input type="text" id="taskName" name="taskName" />
    			</td>
    			<td width="20%" >
    			<input type="button" class="normal_btn" onclick="__extQuery__(1);" value="查 询" id="queryBtn" /> 
    			</td>
			</tr>
		</table>
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	</form>
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</div>
<script type="text/javascript" >
	var myPage;
	var url = "<%=contextPath%>/sysbusinesparams/businesparamsmanage/TaskTimeSet/taskTimeQuery.json?COMMAND=1";
	var title = null;
	var columns = [
				{header: "定时任务名称", dataIndex: 'ACTION_DESC', align:'center'},
				{header: "间隔时间", dataIndex: 'TASK_INTERVAL', align:'center'},
				{header: "单位", dataIndex: 'PLAN_REPEAT_TYPE_NAME', align:'center'},
				{id:'action',header: "操作", walign:'center',idth:70,sortable: false,dataIndex: 'PLAN_ID',renderer:myLink}
		      ];

	function myLink(plan_id,metaDate,record){
        return String.format("<a href=\"#\" onclick=toUpdate('"+plan_id+"');>[设置]</a>");
    }
    function toUpdate(plan_id){
    	OpenHtmlWindow("<%=contextPath%>/sysbusinesparams/businesparamsmanage/TaskTimeSet/taskTimeSetInit.do?plan_id="+plan_id,400,200);
    }
    function getQuery(){
    	__extQuery__(1);
    }   
 </script>    
</body>
</html>