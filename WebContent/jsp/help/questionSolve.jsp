<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@page import="java.util.List"%>
<%@page import="java.util.Map"%><html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>问题解答</title>
<script type="text/javascript">
function rebackFRM(){
	document.FRM.action='<%=request.getContextPath()%>/help/questionSolve/QuestionSolve/questionSolveInit.do';
	document.FRM.submit();
}
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：帮助答疑 > 帮助答疑 > 问题解答</div>
<form id="fm" name="fm">
<table class="table_edit">
<tr>
	<td nowrap="nowrap" align="right">
		<label>问题分类：</label>
		</td>
		<td align="left">
		<script type="text/javascript">
		genSelBox("questionClass_solve",<%=Constant.QUETION_TYPE%>,"","true");
		</script>&nbsp;
	</td>
</tr>
<tr>
	<td nowrap="nowrap" align="right">
		<label>问题描述：</label>
		</td>
		<td align="left">
		<input type="text" id="questionDescr" name="questionDescr" value="" maxlength="40" size="50"/>&nbsp;
		<input type="button" name="queryBtn" id="queryBtn" onclick="__extQuery__(1) ;"  class="cssbutton" value="查询"/><br/>
		<font color="red">输入的字数必须在4-30字之间。如果想输入多个条件，请用空格隔开! </font>
		</td>
</tr>
</table>
<!-- 查询条件 end -->   
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end --> 
</form>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
	//查询路径 
	var url = "<%=contextPath%>/help/questionSolve/QuestionSolve/getDealerQuestion.json";
	var title = null;
	//MyAlert(1);
	var columns = [
	                {header: "序号",dataIndex: 'ROWNUM',align:'center'},
	                {header: "问题ID",dataIndex: 'QUESTION_ID',align:'center'},
	                {header: "问题状态",dataIndex: 'QUESTION_STATUS',align:'center',renderer:getItemValue},
					{header: "问题分类",dataIndex: 'QUESTION_TYPE',align:'center',renderer:getItemValue},
					//{header: "问题模块",dataIndex: 'QUESTION_MODULE',align:'center'},
					{header: "问题描述",dataIndex: 'QUESTION_DESCRIBE',align:'center',renderer:getQuesDes},
					{header: "操作",dataIndex: '明细',align:'center',renderer:myLink}
		      ];


	function getQuesDes(value,metaDate,record){
		var sQuesDes = document.getElementById("questionDescr").value ;
		var aQuesDes = sQuesDes.split(/\s/) ;

		var iLen = aQuesDes.length ;

		for(var i=0; i<iLen; i++) {
			var rQuesDes = new RegExp(aQuesDes[i] + "","gi") ;
			
			value = value.replace(rQuesDes, "<font color='red'>" + aQuesDes[i] + "</font>") ;
		}

		return String.format(value) ;
	}

	
	function myLink(value,meta,record){
			var qId = record.data.QUESTION_ID;
			//MyAlert(qId);
			return String.format(
					"<a href=\"#\" onclick='answer(\""+qId+"\")'>[问题明细]</a>"
			)
}
	function answer(qId){
		var url = "<%=contextPath%>/help/questionSolve/QuestionSolve/toDtail.do?qIds="+qId; 
		window.open(url, "inIframe") ;
		// MyAlert(url);
		}
</script>
</body>
</html>