<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>问题设置（车厂）</title>
<script type="text/javascript">
function rebackFRM(){
	document.FRM.action='<%=request.getContextPath()%>/help/questionSolve/Setting/setQuestionInit.do';
	document.FRM.submit();
}
function setQuestionFRM(){
	var questionStatus__sq = document.getElementById("questionStatus__sq").value;
	if(questionStatus__sq == ''){
		MyAlert("是否有效必须选择！");
		return;
	}
	var questionClass__sq = document.getElementById("questionClass__sq").value;
	if(questionClass__sq == ''){
		MyAlert("问题分类必须选择！");
		return;
	}
	var isCommon__sq = document.getElementById("isCommon__sq").value;
	if(isCommon__sq == ''){
		MyAlert("是否是常规问题必须选择！");
		return;
	}
	var answerContent_sq = document.getElementById("answerContent_sq").value;
	if(answerContent_sq == ''){
		MyAlert("回答内容必须选择！");
		return;
	}
	document.FRM.action='<%=request.getContextPath()%>/help/questionSolve/Setting/setQuestion.do';
	document.FRM.submit();
}
function checkIsCommon(value){
	if(document.getElementById("isCommon__sq").value==<%=Constant.IF_TYPE%>){
		document.getElementById("status").style.display="block";
		document.getElementById("label_content").style.display="block";
		document.getElementById("status_content").style.display="block";
		document.getElementById("answerContent_sq").style.display="block";
	}
	else{
		document.getElementById("status").style.display="none";
		document.getElementById("label_content").style.display="none";
		document.getElementById("status_content").style.display="none";
		document.getElementById("answerContent_sq").style.display="none";
	}
}
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：帮助答疑 > 帮助答疑 > 问题设置（车厂）</div>
<form method="POST" name="FRM" id="FRM">
<table class="table_edit">
<tr>
	<td align="right">
	<label>问题ID：</label>
	</td>
	<td align="left">
	<input type="hidden" id="qid" name="qid" value="${id }"/>
	<label id="questionId_sq">${id }</label>
	</td>
</tr>
<tr>
	<td id="question_classfy" nowrap="nowrap" align="right">
		<label>问题分类：</label>
		</td>
		<td align="left">
			<label id="classFy_sq">${classFy}</label>
	</td>
</tr>
<tr>
	<td align="right">
	<label>问题描述：</label>
	</td>
	<td align="left">
	<label id="questionDescr_sq">${desc }</label>
	</td>
</tr>

<tr>
	<td nowrap="nowrap" align="right">
		<label>是否有效：</label>
		</td>
		<td align="left">
		<script type="text/javascript">
		genSelBox("questionStatus__sq",<%=Constant.QUETION_STATUS%>,"","true");
		</script>&nbsp;<br/>
		<font color="red">有效=已回答、无效=取消</font>
	</td>
</tr>
<tr>
	<td nowrap="nowrap" align="right">
		<label>问题分类：</label>
		</td>
		<td align="left">
		<script type="text/javascript">
		genSelBox("questionClass__sq",<%=Constant.QUETION_TYPE%>,"","true");
		</script>&nbsp;
	</td>
</tr>
<tr>
	<td nowrap="nowrap" align="right">
		<label>是否是常规问题：</label>
		</td>
		<td align="left">
		<script type="text/javascript">
		genSelBox("isCommon__sq",<%=Constant.IF_TYPE%>,"","true");
		//checkIsCommon(this);
		</script>&nbsp;
	</td>
</tr>
<tr>
	<td nowrap="nowrap" align="right">
		<label>下发区域：</label>
		</td>
		<td align="left">
		<select name="dealerType" id="dealerType">
		  <option value ="0" selected="selected">所有</option>
		  <option value ="10771001">经销商整车销售</option>
		  <option value ="10771002">经销商售后</option>
		</select>
	</td>
</tr>

<tr>
	<td id="status"   align="right" nowrap="nowrap" valign="top">
		<label id="label_content">回答内容：</label>
		</td>
		<td align="left" id="status_content" nowrap="nowrap">
		<textarea rows="10" cols="50" id="answerContent_sq" name="answerContent_sq"></textarea>
	</td>
</tr>
</table>

<!-- 添加附件start -->
	<table class="table_info" border="0" id="file">
	    <tr>
	        <th><img class="nav" src="<%=contextPath%>/img/subNav.gif" />&nbsp;附件列表<input type="hidden" id="fjid" name="fjid"/>
				<span>
					<input type="button" class="cssbutton"  onclick="showUpload('<%=contextPath%>')" value ='添加附件'/>
				</span>
			</th>
		</tr>
		<tr>
			<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
  		</tr>
  		<tr>
	<td align="right">&nbsp;</td>
	<td align="left">
		<input type="button" name="updateBtn" id="updateBtn" class="cssbutton" onclick="setQuestionFRM();" value="修改"/>&nbsp;
		<input type="button" name="rebackBtn" id="rebackBtn" class="cssbutton" onclick="rebackFRM();" value="返回"/>
		<!--<input type="button" name="updateBtn" id="updateBtn" onclick="setQuestionFRM();" class="cssbutton" value="修改"/> 
		<input type="button" name="rebackBtn" id="rebackBtn"  onclick="rebackFRM();" class="cssbutton" value="返回"/>
	--></td>
</tr>
	</table>
	<!-- 添加附件end -->
</form>
</body>
</html>