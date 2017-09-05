<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>

<%@page import="com.infodms.dms.common.Constant"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>授权级别维护(授权审核)</title>
</head>
<body onload="__extQuery__(1)">
<div class="wbox">
<form name='fm' id='fm'>
  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔授权管理&gt;授权级别维护</div>
</form>
<div class="form-panel">
		<h2>查询信息</h2>
			<div class="form-body">
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end -->  
  </div>
  </div>
  </div>
<script type="text/javascript" >
var myPage;
	//TYPE 0:技术室审核 1:结算室审核  
	var url = "<%=request.getContextPath()%>/claim/authorization/LevelMain/levelQuery.json?COMMAND=1&TYPE=<%=Constant.AUDIT_TYPE_01%>";
	var title = null;
	
	var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{header: "操作",sortable: false,dataIndex: 'APPROVAL_LEVEL_TIER',renderer:myLink ,align:'center'},
				{header: "授权级别编号",sortable: false,dataIndex: 'APPROVAL_LEVEL_CODE',align:'center'},				
				{header: "授权级别名称",sortable: false,dataIndex: 'APPROVAL_LEVEL_NAME',align:'center'}
		      ];

//设置超链接  begin      
	
	//修改的超链接设置
	function myLink(value,meta,record){
		return String.format("<a  href='#' onclick='updateIt(" + value + ")'>[修改]</a>");
	  <%--   return String.format(
         "<a href=\"<%=contextPath%>/claim/authorization/LevelMain/levelUpdateInit.do?ID="
			+ value + "\">[修改]</a>"); --%>
	}
	
	function updateIt(value) {
		var url = "<%=contextPath%>/claim/authorization/LevelMain/levelUpdateInit.do?ID=" + value;
		OpenHtmlWindow(url, 650, 320, '授权级别修改');
		
		/* fm.action = url ;
		fm.submit() ; */
	}
	
//设置超链接 end
</script>
</body>
</html>