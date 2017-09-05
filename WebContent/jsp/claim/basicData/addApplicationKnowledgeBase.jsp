<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="com.infodms.dms.po.TmDealerPO"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>应用知识库</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<script >
<!--
		//初始化时间控件
	    function doInit(){
	  	   loadcalendar();
	    }  

function save(){
	if(!$('modelPartSel')) {
		
	} else {
		if($('modelPartSel').value==""){
			MyAlert('请选择维修部位!');
			return;
		}
	}
	if(!submitForm('fm')) {
		return false
	}
	fm.action="<%=contextPath%>/claim/basicData/HomePageNews/saveApplicationKnowledgeBase.do"
	fm.submit();
}
//-->
</script>
</head>
<body >
<div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;个人信息管理&gt;新增应用知识库</div>
<form name='fm' id='fm' method="post">
<table class="table_add" border="0">
 <tr>
 	<input name="newsId" value="${newsId}" type="hidden" />
 	<input name="flag" value="${flag}" type="hidden" />
<%--  	<td align="right" nowrap="nowrap"  >单据编码：
 		<input name="newsCode" value="${newsCode}" type="hidden" />
 	</td>
 	<td>${newsCode}</td> --%>
 	<td align="right" nowrap="nowrap"  >发表单位：</td>
 	<td align="left">${list.ORG_NAME}
 		<input type="hidden"  name=orgId value="${list.ORG_ID}"/>
 		<input type="hidden"  name="orgName" value="${list.ORG_NAME}"/>
 	</td>
 	 <td align="right" nowrap="nowrap"  >发表人：</td>
 	<td align="left">${list.VOICE_PERSON}
 		<input type="hidden"  name="name" value="${list.VOICE_PERSON}"/>
 	</td>
 	 	<td >&nbsp;</td>
 	<td>&nbsp;</td>
 </tr>
 <tr>
 	<td nowrap="nowrap" align="right">发表日期：</td>
 	<td>${date}</td>
 	<td >&nbsp;</td>
 	<td>&nbsp;</td>
 </tr>
 <tr>
 	<td align="right">标题：</td>
 	<td><input name="title" type="text" class="middle_txt" id="title" maxlength="300" datatype="0,is_null,300"/></td>
 	<c:choose>
			<c:when test="${!empty listModelGroup}">
				<td align="right">车型组：</td>
				<td align="left" nowrap="true">
					<select id="modelGroupSel" name="modelGroupSel">
						<option value="" selected="selected">所有</option>
						<c:forEach items="${listModelGroup}" var="model">
							<option value="${model.wrgroupId }">${model.wrgroupName }</option>
						</c:forEach>
					</select>
				</td>
				<td align="right">维修部位：</td>
				<td align="left" nowrap="nowrap">
					<script type="text/javascript">
							genSelBoxExp("modelPartSel",<%=Constant.REPAIR_PART%>,"",true,"short_sel","","false",'');
					</script>
				</td>
			</c:when>
			<c:otherwise>
				<td >&nbsp;</td>
				<td >&nbsp;</td>
				<td >&nbsp;</td>
				<td >&nbsp;</td>
			</c:otherwise>
			</c:choose>
 </tr>
 <tr>
 	<td align="right">内容:</td>
 	<td colspan="7">
 		<textarea name="contents" cols="100" rows="10" ></textarea>
	</td>
 </tr>
</table>
<br />
<table class="table_info" border="0" id="file" >
	<tr>
 		<td width="100%" colspan="3">
 		  	<jsp:include page="${contextPath}/uploadDiv.jsp" /> 
 		<input type="button" class="normal_btn"  onclick="showUpload2('<%=contextPath%>')" value ='添加附件'/></td>
 </tr>
 <tr>
 	<td height="23"></td>
	<td height="23"></td>
 	<td colspan="2" align="center">
 		<input class="normal_btn" type="button" name="ok" id="commitBtn" value="确定" onclick="save();"/> 
 		<input class="normal_btn" name="back" type="button" onclick="reBack();" value="返回"/>
 	</td>
 </tr>
</table>
<br />
</form>
<script>

function reBack(){
	location="<%=contextPath%>/claim/basicData/HomePageNews/ApplicationKnowledgeBase.do";
}
$('deId').style.display="none";
$('deId1').style.display="none";
$('deId2').style.display="none";
$('deId3').style.display="none";

function checkDealer(obj){
	
	if(obj.value==<%=Constant.VIEW_NEWS_type_1%>){
		$('deId').style.display="block";
		$('deId1').style.display="block";
		$('deId2').style.display="block";
		$('deId3').style.display="block";
	}
	else{
		$('deId').style.display="none";
		$('deId1').style.display="none";
		$('deId2').style.display="none";
		$('deId3').style.display="none";
	}
}
function clearInput(){
	var target = document.getElementById('dealerCode');
	target.value = '';
}


</script>
</body>
</html>