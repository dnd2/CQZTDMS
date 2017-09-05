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
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">
 <!--
 function queryIt() {
	 __extQuery__(1) ;
 }
 
 function addThis() {
	 fm.action = "<%=contextPath%>/groups/DivideGroupsAction/groupsAddInit.do" ;
	 fm.submit() ;
 }
 
 function updateThis(valueId) {
	 var url = "<%=contextPath%>/groups/DivideGroupsAction/groupsUpdateInit.do?headId=" + valueId ;
	 window.open(url, "inIframe") ;
 }
 //-->
 </script>
<title>分组操作</title>
</head>
<body> 
<div>
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt; 分组管理 &gt; 分组操作</div>
		<form id="fm" name="fm" method="post">
			<table class="table_query">
				<tr>
					<td align="right"><label for="groupsName">组名称：</label></td>
					<td><input type="text" class="middle_txt" name="groupsName" id="groupsName" value="" /></td>
					<td align="right"><label for="groupsType">组类型：</label></td>
					<td>
						<script type="text/javascript">
							genSelBoxExp("groupsType",<%=Constant.DIVIDE_GROUP_TYPE%>,"",true,"short_sel","","false",'') ;
						</script>
					</td>
					<td></td>
				</tr>
				<tr>
					<td align="right"><label for="groupsStatus">组状态：</label></td>
					<td>
						<script type="text/javascript">
							genSelBoxExp("groupsStatus",<%=Constant.STATUS%>,"",true,"short_sel","","false",'') ;
						</script>
					</td>
					<td align="right"></td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td colspan="5" align="center">
						<input type="button" class="normal_btn" name="queryBtn" id="queryBtn" value="查 询" onclick="queryIt() ;" />&nbsp;
						<input type="button" class="normal_btn" name="addIt" id="addIt" value="新 增" onclick="addThis() ;" />
					</td>
				</tr>
			</table>
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	</form>
</div>
<script type="text/javascript">
	var myPage;
	var url = "<%=contextPath%>/groups/DivideGroupsAction/groupsQuery.json?COMMAND=1";
	var title = null;
	
	var columns = [
				{header: "组名称", dataIndex: 'GROUP_NAME', align:'center'},
				{header: "组类型", dataIndex: 'GROUP_TYPE', align:'center',renderer:getItemValue},
				{header: "组范围", dataIndex: 'GROUP_AREA', align:'center',renderer:getItemValue},
				{header: "组状态", dataIndex: 'GROUP_STATUS', align:'center',renderer:getItemValue},
				{id:'action',header: "操作", walign:'center',idth:70,sortable: false,dataIndex: 'GROUP_ID',renderer:myLink}
		      ];

	function myLink(value,metaDate,record){
		return String.format("<a href='#' onclick='updateThis(" + value + ")'>[修改]</a>") ;
    }
</script>    
</body>
</html>