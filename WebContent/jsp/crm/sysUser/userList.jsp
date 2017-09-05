<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.util.CommonUtils"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">

</script>

<title>实销信息上报</title>
</head>
<body onunload='javascript:destoryPrototype();'> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  潜客管理 &gt; 团队管理 &gt; 经销商用户列表</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<input  type="hidden" name="userId" value="${userId}"/>
		<input type="hidden" name="poseRank" id="poseRank" value="${poseRank}"/>
		<table class="table_query" border="0">
			<tr>
				<td width="20%" class="tblopt"><div align="right">用户名称：</div></td>
				<td width="39%" >
      				<input type="text" id="poseName" name="userName" class="middle_txt" size="20"   />
    			</td>
    			<td width="20%" class="tblopt"><div align="right">用户账号：</div></td>
				<td width="39%" >
      				<input type="text" id="poseCode" name="userCode" class="middle_txt" size="20"   />
    			</td>
				<td class="table_query_3Col_input" >
					&nbsp;&nbsp;<input type="button" class="normal_btn" onclick="__extQuery__(1);" value="查 询" id="queryBtn" /> 
				</td>
			</tr>
		</table>
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
	
</div>
<script type="text/javascript">

	var myPage;
	var url = "<%=contextPath%>/crm/sysUser/DealerSysUser/getUserList.json?COMMAND=1";
	var title = null;
	
	var columns = [
				{id:'action',header: "操作", walign:'center',idth:70,sortable: false,dataIndex: 'USER_ID',renderer:myLink},
				{header: "用户账户", dataIndex: 'ACNT', align:'center'},
				{header: "用户名称", dataIndex: 'NAME', align:'center'},
				{header: "用户状态", dataIndex: 'USER_STATUS', align:'center',renderer:getItemValue},
				{header: "职位级别", dataIndex: 'POSE_RANK', align:'center',renderer:getItemValue}
		      ];

	function myLink(value,meta,rec){
		var data = rec.data;
		return "<input type='radio'  name='pose_id' value='"+value+"' onclick='submit_(\""+data.USER_ID+"\",\""+data.NAME+"\");' />";
    }

	function submit_(user_id,user_name){
		if (parent.$('inIframe')) {
			_hide();
			parent.$('inIframe').contentWindow.showSalesManInfo(user_id,user_name);
		}else {
			parent._hide();
			parent.showSalesManInfo(user_id,user_name);
		}

	}

</script>    
</body>
</html>