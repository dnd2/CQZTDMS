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
<title>物流商与经销商关系维护 </title>
</head>

<body>
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置 > 经销商管理 > 经销商公司更名审核 > 查看修改明细
	</div>
	<form name="fm" method="post" id="fm">
		<input type="hidden" id ="COMPANY_ID" name = "COMPANY_ID" value="${COMPANY_ID}"></input>
		<input type="hidden" id ="ver" name = "ver" value="${ver}"></input>
		<!-- 查询条件 begin -->
		<table class="table_query" id="subtab" >
		</table>
		<!-- 查询条件 end -->	
		<!--分页 begin -->
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		<!--分页 end -->
	</form>				
	<table border="0" width="100%">
		<tr align="center" width="100%">
			<td>
				<input class="u-button u-cancel" type="button" value="关闭 " onclick="goClose()" />
			</td>
		</tr>
	</table>
</div>	
	
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
	//查询路径           
	var url = "<%=contextPath%>/sysmng/orgmng/DlrInfoMng/detailQuery.json";
	var title = null;
	var columns = [
				{header: "修改字段名",dataIndex: 'KEY'},
				{header: "修改前",dataIndex: 'OLD_VALUE'},
				{header: "修改后",dataIndex: 'VALUE'}
		      ];

	//初始化    
	function doInit(){
		__extQuery__(1);
	}
	function goClose(){
		_hide();
	}
	function cancel()
	{
	 window.location.href='<%=contextPath%>/sales/storage/storagebase/LogisticsManage/logiDealerRelationInit.do';  
	}
</script>
</body>
</html>
