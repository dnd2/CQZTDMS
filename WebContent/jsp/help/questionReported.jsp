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
<title>问题提报</title>
<script type="text/javascript">
function addFRM(){
	document.fm.action='<%=request.getContextPath()%>/help/questionSolve/QuestionReported/addQuestionPage.do';
	document.fm.submit();
}

function chkTextValue(valPara) {
	var iLen = valPara.trim().length ;
	//MyAlert("输入的字数在4-30字之间");
	if(iLen <= 3) {
		document.getElementById("queryBtn").disabled = true ;
		
	} else {
		document.getElementById("queryBtn").disabled = false ;
	}
}

function trim() {
	var regExp = /^\s*(.*?)\s*$/ ;

	return this.replace(regExp, "RegExp.$1") ;
}
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：帮助答疑 > 帮助答疑 > 问题提报</div>
<form id="fm" name="fm">
<table class="table_edit">
<tr>
	<td nowrap="nowrap" align="right">
		<label>问题分类：</label>
		</td>
		<td align="left">
		<script type="text/javascript">
		genSelBox("questionClass",<%=Constant.QUETION_TYPE%>,"","true");
		</script>
	</td>
</tr>

<tr>
		<td nowrap="nowrap" align="right">
		<label>关键字搜索：</label>
		</td>
		<td align="left">
		<input type="text" id="questionDesc" name="questionDesc" value="" class="normal_txt" size="40" onkeyup="chkTextValue(this.value) ;" maxlength="30"/>&nbsp;
		<input type="button" name="button1" id="queryBtn" onclick="__extQuery__(1) ;javascript:document.getElementById('button2').style.display='inline' ;" onchange="" class="cssbutton" value="查询"/>&nbsp;
		<input type="button" name="button2" id="btton2" onclick="addFRM();" class="cssbutton"   value="新增"/>&nbsp;<br/>
		<font color="red">输入的字数必须在4-30字之间,否则查询按钮无效。如果想输入多个条件，请用空格隔开!</font>
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
	var url = "<%=contextPath%>/help/questionSolve/QuestionReported/getDetailMessageOfQuestion.json";
	var title = null;
	var columns = [
	                {header: "序号",dataIndex: 'ROWNUM',align:'center'},
	                {header: "问题ID",dataIndex: 'QUESTION_ID',align:'center'},
					{header: "问题分类",dataIndex: 'QUESTION_TYPE',align:'center',renderer:getItemValue},
					//{header: "问题模块",dataIndex: 'QUESTION_MODULE',align:'center'},
					{header: "问题描述",dataIndex: 'QUESTION_DESCRIBE',align:'center',renderer:getQuesDes},
					{header: "回答内容",dataIndex: 'ANSWER',align:'center'},
					{header: "操作",dataIndex: '明细',align:'center',renderer:myLink}
		      ];
   // MyAlert(columns);
function getQuesDes(value,metaDate,record){
	var sQuesDes = document.getElementById("questionDesc").value ;
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
	var	url="<%=contextPath%>/help/questionSolve/QuestionReported/getDetailMessages.do?QID="+qid;
	window.open(url,"inIframe");
		}
</script>
</body>
</html>