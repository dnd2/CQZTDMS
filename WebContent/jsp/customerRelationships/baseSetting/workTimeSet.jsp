<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>班次类型设置</title>
<script type="text/javascript">

	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body onload="__extQuery__(1)">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户关系管理 &gt; 基础设定 &gt;班次类型设置</div>
	<form method="post" name = "fm" id="fm">
		<table  border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
			<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />班次类型设置</th>
			
			<tr>
				<td colspan="4" align="center">
					<input class="normal_btn" type="button" value="查询" name="recommit" id="queryBtn" onclick="__extQuery__(1);" />
					&nbsp;
          			<!-- <input name="addBtn" type="button" class="normal_btn" onclick="addWorkTime();" value="新增" /> -->
        		</td>
			</tr>
		</table>
		
	 <!-- 查询条件 end -->
	 <!--分页 begin -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	 <!--分页 end -->
	</form>
<script type="text/javascript">
	//工作时间新增
	function addWorkTime(){
		window.location.href='<%=contextPath%>/customerRelationships/baseSetting/workTimeSet/addWorkTimeSet.do';
	}
	
	var myPage;
    //查询路径
	var url = "<%=contextPath%>/customerRelationships/baseSetting/workTimeSet/queryWorkTimeSet.json";
				
	var title = null;

	var columns = [
				{header: "班次类型",dataIndex: 'WT_TYPE',align:'center'},
				{header: "上班时间", dataIndex: 'STA_TIME', align:'center'},
				{header: "下班时间", dataIndex: 'END_TIME', align:'center'},
				{id:'action',header: "操作",sortable: false,dataIndex: 'ID',renderer:myLink}
		      ];
	
	function myLink(value,meta,record){
		return String.format("<input name='detailBtn' type='button' class='normal_btn' onclick='viewDetail(\""+ value +"\")' value='修改'/>");
	}
	function viewDetail(value){		
		window.location.href='<%=contextPath%>/customerRelationships/baseSetting/workTimeSet/addWorkTimeSet.do?id='+value ;
	}
</script>
</body>
</html>