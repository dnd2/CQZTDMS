<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="com.infodms.dms.po.TmDealerPO"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="com.infodms.dms.common.Constant"%>
<%@page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@page import="java.util.LinkedList"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%
	String contextPath = request.getContextPath();
	List<FsFileuploadPO> attachLs = (LinkedList<FsFileuploadPO>)request.getAttribute("lists");
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>应用知识库</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
<div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;个人信息管理&gt;查看应用知识库</div>
<form name='fm' id='fm' method="post">
<table  class="table_edit">
 <tr >
 	<td align="right" nowrap  class="table_add_2Col_label_6Letter">单据编码： 	</td>
 	<td>${list.CODE}
 	<input type="hidden" id="comman" value="${comman}"/>
 	</td>
 	<td align="right" nowrap  class="table_add_2Col_label_6Letter">发表单位：</td>
 	<td >${list.ORG_NAME}
 	</td>

 	<td align="right" nowrap  class="table_add_2Col_label_6Letter">发表人：</td>
 	<td >${list.VOICE_PERSON}
 	</td>
 </tr>
 <tr>
 	<td align="right"  class="table_add_2Col_label_6Letter">发表日期：</td>
 	<td id="newsDate">${list.PUBLISHED_DATE }</td>
 	<c:choose>
	<c:when test="${!empty listModelName}">
		<c:choose>
	<c:when test="${listModelName!='0'}">
	 	<td align="right">车型组：</td>
	 	<td>
	 		${listModelName }
		</td>
		<td align="right">维修部位：</td>
	 	<td>
			<script type="text/javascript">writeItemValue(${list.MODEL_PART });</script>
		</td>
	</c:when>
	<c:otherwise>
		<td align="right">车型组：</td>
	 	<td>
	 		所有
		</td>
		<td align="right">维修部位：</td>
	 	<td>
			<script type="text/javascript">writeItemValue(${list.MODEL_PART });</script>
		</td>
	</c:otherwise>
	</c:choose>
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
 	<td align="right" class="table_add_2Col_label_6Letter">标题：</td>
 	<td>${list.TITLE}</td>
 </tr>
</table>
<br />
<table class="table_edit">
 <tr>
 	<td align="right">内容:</td>
 	<td>
 		<textarea   disabled="true" name="contents" rows="10" cols="100">${list.CONTENTS }</textarea>
	</td>
 </tr>
</table>
<table class="table_info" border="0" id="file">
			<input type="hidden" name="fjids"/>
		<tr>
			<td width="100%" colspan="2">
				<jsp:include page="${contextPath}/uploadDiv.jsp" />
			</td>
		</tr>
		<%for(int i=0;i<attachLs.size();i++) { %>
    			<script type="text/javascript">
    			 var homePageNewsAttachUrl = "<%=request.getContextPath()%>/util/FileDownLoad/FileDownLoadQuery.do?fjid=";
     			addUploadRowByDbView('<%=CommonUtils.checkNull(attachLs.get(i).getFilename()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFjid()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFileurl())%>',null,homePageNewsAttachUrl);
   			 
    			//addUploadRowByDb('<%=CommonUtils.checkNull(attachLs.get(i).getFilename()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFjid()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFileurl())%>');
    			</script>
    	<%} %>
    	<tr>
    	<td align="center">
    		<input class="normal_btn" name="back" type="button" onclick="reBack();" value="关闭"/>
		</td>
    	</tr>
</table>
<br />
</form>
<script>
if($('fileUploadTab').select('[class="normal_btn"]')[0]!=undefined){
	$('fileUploadTab').select('[class="normal_btn"]')[0].disabled=true;
	for(i=0;i<=$('fileUploadTab').select('[class="normal_btn"]').size();i++){
		$('fileUploadTab').select('[class="normal_btn"]')[i].disabled=true;
		}
}

//清空经销商框
function clearInput(){
	var target = document.getElementById('dealerCode');
	target.value = '';
}

function reBack(){
		if($('comman').value==2){
			_hide();
		}
		else{
			history.back();
			//location="<%=contextPath%>/claim/basicData/HomePageNews/ApplicationKnowledgeBase.do";
		}
	}
 var value = $('newsDate').innerHTML
 $('newsDate').update(value.substr(0,10));
</script>
</body>
</html>