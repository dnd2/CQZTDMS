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
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
	List<FsFileuploadPO> attachLs = (LinkedList<FsFileuploadPO>)request.getAttribute("lists");
	String dealer = request.getAttribute("dealer") == null ? "" : request.getAttribute("dealer").toString();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>首页新闻</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<script>
 KE.show({
    id : 'contents',
    resizeMode : 0,
    afterCreate : function(id) {
	KE.toolbar.disable(id, []);
	KE.readonly(id);
	KE.g[id].newTextarea.disabled = true;
  }
 });
 //var homePageNewsAttachUrl = "<%=request.getContextPath()%>/claim/basicData/HomePageNews/HomePageNewsAttachQuery.do?fjid=";
 var homePageNewsAttachUrl = "<%=request.getContextPath()%>/util/FileDownLoad/FileDownLoadQuery.do?fjid=";
</script>
</head>
<body>
<div class="wbox">
<div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：;首页新闻</div>
<form name='fm' id='fm' method="post">
 <div class="form-panel">
			<div class="form-body">
<input type="hidden" name="newsId" id="newsId" value="${list.NEWS_ID }" />
<table  class="table_query">
 <tr >
 	<td style="text-align:right" nowrap  class="table_add_2Col_label_6Letter">单据编码： 	</td>
 	<td>${list.NEWS_CODE}
 	<input type="hidden" id="comman" value="${comman}"/>
 	</td>
 	<td style="text-align:right" nowrap  class="table_add_2Col_label_6Letter">发表单位：</td>
 	<td >${list.ORG_NAME}
 	</td>

 	<td style="text-align:right" nowrap  class="table_add_2Col_label_6Letter">发表人：</td>
 	<td >${list.VOICE_PERSON}
 	</td>
 </tr>
 <tr>
 	<td style="text-align:right"  class="table_add_2Col_label_6Letter">发表日期：</td>
 	<td id="newsDate">${list.NEWS_DATE }</td>
 	<td style="text-align:right" class="table_add_2Col_label_6Letter">失效日期：</td>
 	<td id="newsDate">${list.EXPIRY_DATE }</td>
 	<c:if test="${11781001 == list.DUTY_TYPE}">
 	<td style="text-align:right" class="table_add_2Col_label_6Letter">消息类型：</td>
 	<td ><c:choose>
		  <c:when test="${'11991003'eq list.MSG_TYPE}">经销商【公用】信息</c:when>		
		  <c:when test="${'10771002'eq list.MSG_TYPE }">经销商【售后】信息</c:when>		
		  <c:when test="${'10771001,10771003,10771004' eq list.MSG_TYPE}">经销【销售】信息</c:when>		
		  <c:otherwise>无</c:otherwise>		
		</c:choose>
     </td>
    </c:if>
 </tr> 
 <tr>
 	<td style="text-align:right" class="table_add_2Col_label_6Letter">标题：</td>
 	<td>${list.NEWS_TITLE}</td>
 	<td style="text-align:right" class="table_add_2Col_label_6Letter">新闻类别：</td>
 	<td align="left" nowrap="true">
 	<script type="">
 		document.write(getItemValue(${list.NEWS_TYPE}));
 	</script>
	</td>
	<td></td><td></td>
 </tr>
</table>
<br />
<table class="table_query">
 <tr>
 	<td style="text-align:right"></td>
 	<td colspan="6">
 		<textarea id="contents"  disabled="true" name="contents" rows="10" cols="100"  style="width:98%">${list.CONTENTS }</textarea>
	</td>
 </tr>
</table>
<table class="table_list" border="0" id="file">
			<input type="hidden" name="fjids"/>
		<tr>
			<td width="100%" colspan="2">
				<table class='table_list' id='fileUploadTab'>
					<tr>
						<th style="width:100%">附件名称</th>
					</tr>
				</table>
			</td>
		</tr>
		<%if(attachLs != null) { for(int i=0;i<attachLs.size();i++) { %>
		  	<script type="text/javascript">
		  		viewUploadRow('<%=CommonUtils.checkNull(attachLs.get(i).getFjid()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFilename()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFileurl())%>');
		  	</script>
		<%}} %>
</table>
</div>
</div>
<br />
</form>
</div>
<script>
if($('fileUploadTab').select('[class="normal_btn"]')[0]!=undefined){
	$('fileUploadTab').select('[class="normal_btn"]')[0].disabled=true;
}

//清空经销商框
function clearInput(){
	var target = document.getElementById('dealerCode');
	target.value = '';
}

function reBack() {
	if($('comman').value==2){
	   _hide();
	}
	else
	{
	history.back();
	}
}

function save() {
	fm.action = "<%=contextPath%>/claim/basicData/HomePageNews/saveNews.do"
	fm.submit();
}

 // 已阅读新闻
 function read() {
	 var newsId = document.getElementById("newsId").value;
	 var newsback = document.getElementById("newsback").value;
	 if(newsback == '') {MyAlert('必须填写回复意见!'); return;}
	 var url = "<%=contextPath%>/claim/basicData/HomePageNews/saveNewsRead.json";
	 makeCall(url, function(json){
			if (json.Exception) {
				MyDivAlert('阅读出错!');
			} else {
				MyAlert('阅读成功!'); 
				_hide();
				parentContainer.__extQuery__(1);
			}
		}, {newsId : newsId, newsback: newsback});
 }
 
 var value = $('newsDate').innerHTML;
 $('newsDate').update(value.substr(0,10));
</script>
</body>
</html>