<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
	Long userId = (Long)request.getAttribute("logonUser");
%>

<%@page import="com.infodms.dms.common.Constant"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔工时查询</title>
<script type="text/javascript">
	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body>
<form name='fm' id='fm'>
</form>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end -->
<script type="text/javascript" >
var myPage;
	var url = "<%=request.getContextPath()%>/claim/basicData/HomePageNews/HomePageNewsQuary.json?COMMAND=1";
	var title = null;
	
	var columns = [
				{header: "序号",sortable: false,align:'center',renderer:getIndex},
				{header: "单据编码",sortable: false,dataIndex: 'NEWS_CODE',align:'center'},				
				//{header: "发表人",sortable: false,dataIndex: 'CN_DES',align:'center'},
				{header: "发表日期",sortable: false,dataIndex: 'NEWS_DATE',align:'center',renderer:formatDate},
				{header: "标题",sortable: false,dataIndex: 'NEWS_TITLE',align:'center'},
				{header: "新闻类别",sortable: false,dataIndex: 'NEWS_TYPE',align:'center',renderer:getItemValue},
				{header: "单据状态",sortable: false,dataIndex: 'STATUS',align:'center',renderer:getItemValue},
				{header: "操作",sortable: false,dataIndex: 'NEWS_ID',renderer:myLink ,align:'center'}
		      ];

//设置超链接  begin      
	
	//修改的超链接设置
	__extQuery__(1);
</script>
</body>
</html>