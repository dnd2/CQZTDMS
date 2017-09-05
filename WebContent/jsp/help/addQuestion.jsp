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
<title>常规问题新增</title>
<script type="text/javascript">

function addQuestionFRM(){
	document.FRM.action='<%=request.getContextPath()%>/help/questionSolve/Setting/addQuestion.do';
	document.FRM.submit();

}

function rebackFRM(){
	document.FRM.action='<%=request.getContextPath()%>/help/questionSolve/Setting/setInit.do';
	document.FRM.submit();

}
function chkSel(){
	var classFy = document.getElementById("qTypt_common").value;
	
	if(classFy.length == 0){
		return false ;
	}

	return true ;
}

function chkText1(){
	var classFy = document.getElementById("qDesc_common").value.trim() ;
	
	if(classFy.length < 4 || classFy.length > 300){
		return false ;
	}

	return true ;
}

function chkText2(){
	var classFy = document.getElementById("qAnswer_common").value.trim() ;
	
	if(classFy.length < 4 || classFy.length > 300){
		return false ;
	}

	return true ;
}

function addIt(value){
	if(!chkSel()){
		MyAlert("请选择问题分类！") ;

		return false ;
	}

	if(!chkText1()){
		MyAlert("问题描述输入汉字个数必须在4-300个之间！") ;

		return false ;
	}


	if(!chkText2()){
		MyAlert("问题描述输入汉字个数必须在4-300个之间！") ;

		return false ;
	}
	
	document.getElementById("status").value = value ;
	
	addFRM() ;
	chkFile() ;
	
}

function trim() {
	var regExp = /^\s*(.*?)\s*$/ ;

	return this.replace(regExp, "RegExp.$1") ;
}


function chkFile() {
	var oTab = document.getElementById('fileUploadTab') ;
	var iLen = oTab.rows.length ;
	
	if (iLen <= 1) {
		return false ;
	} else {
		return true ; 
	}
}
</script>
</head>
<body">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：帮助答疑 > 帮助答疑 >  常规问题新增</div>
<form method="POST" name="FRM" id="FRM">
<table class="table_edit">
<tr>
	<td id="question_classfy" nowrap="nowrap" align="right">
		<label>问题分类:</label>
		<input type="hidden" id="status" name="status" value=""/>
		</td>
		<td align="left">
			<script type="text/javascript">
			genSelBox("qTypt_common",<%=Constant.QUETION_TYPE%>,"","true");
		</script>&nbsp;
		<font color="red">*</font>
	</td>
</tr>

<tr>
		<td align="right" valign="top" >
		<label>问题描述:</label>
		</td>
		<td align="left" >
		<textarea rows="5" cols="50" id="qDesc_common" name="qDesc_common"></textarea>
		<font color="red">*</font>
	</td>
</tr>


<tr>
		<td align="right" valign="top" >
		<label>回答内容:</label>
		</td>
		<td align="left" >
		<textarea rows="5" cols="50" id="qAnswer_common" name="qAnswer_common"></textarea>
		<font color="red">*</font>
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
	<td align="center" colspan="2">
		<input type="button" name="submitBtn" id="submitBtn" onclick="addQuestionFRM();" onchange="" class="cssbutton" value="提交"/>&nbsp;
		<input type="button" name="rebackBtn" id="rebackBtn" onclick="rebackFRM();"  class="cssbutton" value="返回"/>
	</td>
</tr>
	</table>
	<!-- 添加附件end -->
</form>
</body>
</html>