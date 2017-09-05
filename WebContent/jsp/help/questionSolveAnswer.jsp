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
<title>问题回答</title>
<script type="text/javascript">
function answerFRM(){
	document.FRM.action='<%=request.getContextPath()%>/help/questionSolve/QuestionSolve/answerQuestion.do?qIds=${QId }';
	document.FRM.submit();
}


function cancelFRM(){
	document.FRM.action='<%=request.getContextPath()%>/help/questionSolve/QuestionSolve/cancelQuestion.do';
	document.FRM.submit();
}

</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：帮助答疑 > 帮助答疑 > 问题解答</div>
<form method="POST" name="FRM" id="FRM">
<table class="table_edit">
<tr>
	<td align="right">
	<label>问题ID：</label>
	</td>
	<td align="left">
	<input type="hidden" id="qid" name="qid" value="${QId }"/>
	<label id="questionId">${id }</label>
	</td>
</tr>
<tr>
	<td align="right">
	<label>问题描述：</label>
	</td>
	<td align="left">
	<label id="questionDescr">${desc }</label>
	</td>
</tr>


<tr>
	<td id="question_classfy" nowrap="nowrap" align="right">
		<label>问题分类：</label>
		</td>
		<td align="left">
			<label id="classFy">${classFy }</label>
		</td>
</tr>

<tr>
	<td id="question_classfy" nowrap="nowrap" align="right">
		<label>是否是常规问题：</label>
		</td>
		<td align="left">
			<c:if test="${answer == null || answer == ''}">
				<select name="common" id="common">
				  <option value ="10041001" selected="selected">是</option>
				  <option value ="10041002">否</option>
				</select>
			</c:if>
			<c:if test="${answer != null && answer != ''}">
				<label id="classFy">${common }</label>
			</c:if>
		</td>
</tr>
<tr>
	<td id="question_classfy" nowrap="nowrap" align="right">
		<label>下发区域：</label>
		</td>
		<td align="left">
			<c:if test="${answer == null || answer == ''}">
				<select name="dealerType" id="dealerType">
				  <option value ="0" selected="selected">所有</option>
				  <option value ="10771001">经销商整车销售</option>
				  <option value ="10771002">经销商售后</option>
				</select>
			</c:if>
			<c:if test="${answer != null && answer != ''}">
				<label id="classFy">${dealerType }</label>
			</c:if>
		</td>
</tr>


<tr>
	<td id="status" align="right" nowrap="nowrap" valign="top">
		<label>回答内容：</label>
		</td>
		<td align="left" nowrap="nowrap">
		<textarea rows="10" cols="50" id="answerContent" name="answerContent">${answer }</textarea>
	</td>
</tr>
<c:if test="${lists != null}">
<tr>
	<td id="status" align="right" nowrap="nowrap">
		<label>附件：</label>
		</td>
		<td align="left" nowrap="nowrap">
		<label id="fj">
		    <c:forEach items="${lists}" var="item">  
		        <a href="${item.fileurl}">${item.filename} &nbsp;&nbsp;</a>
    		</c:forEach>  
		</label>
	</td>
</tr>
</c:if>
</table>

<!-- 添加附件start -->
	<table class="table_info" border="0" id="file">
	    <c:if test="${answer == null || answer == ''}">
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
  		</c:if>
  		<tr>
	<td align="right">&nbsp;</td>
	<td align="left">
		<c:if test="${answer == null || answer == ''}">
			<input type="button" name="answerBtn" id="queryBtn" onclick="answerFRM();" class="cssbutton" value="回答"/> &nbsp;
		</c:if>
		<!--<input type="button" name="cancelBtn" id="cancelBtn" onclick="cancelFRM();" class="cssbutton" value="取消"/> &nbsp;
		--><input type="button" name="rebackBtn" id="rebackBtn" onclick="javascript:window.close();"  class="cssbutton" value="关闭"/>
	</td>
</tr>
	</table>
	<!-- 添加附件end -->
</form>
</body>
</html>