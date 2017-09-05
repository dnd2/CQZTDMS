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
<title>已保存问题修改（经销商）</title>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：帮助答疑 > 帮助答疑 > 已保存问题修改（经销商）</div>
<form id="fm" name="fm">
<table class="table_edit">
<tr>
	<td nowrap="nowrap" align="right">
		<label>问题分类：</label>
		</td>
		<td align="left">
		<script type="text/javascript">
		genSelBox("questionClass_rs",<%=Constant.QUETION_TYPE%>,"","true");
		</script>
	</td>
</tr>

<tr>
		<td nowrap="nowrap" align="right">
		<label>关键字搜索：</label>
		</td>
		<td align="left">
		<input type="text" id="questionDesc_rs" name="questionDesc_common" value="" class="normal_txt" size="40" maxlength="30"/>&nbsp;
		<input type="button" name="queryBtn"  id="queryBtn" onclick="__extQuery__(1); " onchange="" class="cssbutton" value="查询"/>&nbsp;
		<br/>
		<font color="red">输入的字数必须在4-30字之间。如果想输入多个条件，请用空格隔开!</font>
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
	var url = "<%=contextPath%>/help/questionSolve/QuestionReported/getReportedSearchData.json";
	var title = null;
	var columns = [
	                {header: "序号",dataIndex: 'ROWNUM',align:'center'},
	                {header: "问题ID",dataIndex: 'QUESTION_ID',align:'center'},
					{header: "问题分类",dataIndex: 'QUESTION_TYPE',align:'center',renderer:getItemValue},
					{header: "问题状态",dataIndex: 'QUESTION_STATUS',align:'center',renderer:getItemValue},
					//{header: "问题模块",dataIndex: 'QUESTION_MODULE',align:'center'},
					{header: "问题描述",dataIndex: 'QUESTION_DESCRIBE',align:'center',renderer:getQuesDes},
					{header: "回答内容",dataIndex: 'ANSWER',align:'center'},
					{header: "操作",dataIndex: '明细',align:'center',renderer:myLink}
		      ];
    //MyAlert(columns);
function getQuesDes(value,metaDate,record){
	var sQuesDes = document.getElementById("questionDesc_rs").value ;
	var aQuesDes = sQuesDes.split(/\s/) ;

	var iLen = aQuesDes.length ;

	for(var i=0; i<iLen; i++) {
		var rQuesDes = new RegExp(aQuesDes[i] + "","gi") ;
		
		value = value.replace(rQuesDes, "<font color='red'>" + aQuesDes[i] + "</font>") ;
	}

	return String.format(value) ;
}
function myLink(value,meta,record){
		var qid = record.data.QUESTION_ID;
		return String.format(
				"<a href=\"#\" onclick='viewDetail(\""+qid+"\")'>[明细]</a>"
		)
}

	function viewDetail(qid){
	var	url="<%=contextPath%>/help/questionSolve/QuestionReported/ReportedSearch.do?QID="+qid;
	window.open(url,"inIframe");
		}
</script>
</body>
</html>