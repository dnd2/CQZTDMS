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
<title>修改应用知识库</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
<div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;个人信息管理&gt;修改应用知识库</div>
<form name='fm' id='fm' method="post">
<input type=hidden name="modelPart" value="${list.MODEL_PART}"/>
<input type=hidden name='listModelGroup' value="${listModelGroup}"/>
<input type=hidden name='listModelId' value='${listModelId }'/>
<table  class="table_edit" border="0">
 <tr >
 	<td nowrap="nowrap" align="right">单据编码：
 		<input name="newsId" value="${list.ID}" type="hidden" />
 	</td>
 	<td>${list.CODE}</td>
 	<td nowrap="nowrap" align="right">发表单位：</td>
 	<td>${list.ORG_NAME}</td>
 	<td>&nbsp;</td>
 	<td>&nbsp;</td>
 </tr>
 <tr>
 	<td nowrap="nowrap" align="right">发表人：</td>
 	<td>${list.VOICE_PERSON}</td>
 	 <td align="right">发表日期：</td>
 	 <td id="newsDa">${list.PUBLISHED_DATE}</td>
 	 <td>&nbsp;</td>
 	 <td>&nbsp;</td>
 </tr>
 <tr>
 	<td align="right">标题：</td>
 	<td><input name="title" type="text" value="${list.TITLE}" class="middle_txt" size="50"/></td>
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
				<td align="left" nowrap="true">
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
 	<td colspan="5">
 		<textarea name="contents" style="width: 95%" rows="10" >${list.CONTENTS }</textarea>
	</td>
 </tr>
</table>
<br />
<table class="table_info" border="0" id="file">
			<input type="hidden" name="fjids"/>
		<tr colspan="8">
			<th>
				<img class="nav" src="../../../img/subNav.gif" />&nbsp;附件列表：
					&nbsp;&nbsp;&nbsp;&nbsp;
				<span align="left"><input type="button" class="normal_btn"  onclick="showUpload2('<%=contextPath%>')" value ='添加附件'/></span>
			</th>
		</tr>
		<tr>
			<td width="100%" colspan="2">
				<jsp:include page="${contextPath}/uploadDiv.jsp" />
			</td>
		</tr>
		<%for(int i=0;i<attachLs.size();i++) { %>
    			<script type="text/javascript">
    			addUploadRowByDb('<%=CommonUtils.checkNull(attachLs.get(i).getFilename()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFjid()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFileurl())%>');
    			</script>
    	<%} %>
    	<tr>
    	<td align="center" >
    		<input class="normal_btn" type="button" name="ok" id="commitBtn" value="确定" onclick="save();"/> 
 			<input class="normal_btn" name="back" type="button" onclick="history.back();" value="返回"/>    	</td>
    	</tr>
</table>
<br />
</form>
<script>
init();
function init(){
	var msg = $('listModelGroup');
	if(msg.value!=""){
		var modelId = $('listModelId').value;
		var modelGroupSel = $('modelGroupSel');
		for(var i = 0; i < modelGroupSel.options.length; i++){
			if(modelGroupSel.options[i].value==modelId){
				modelGroupSel.options[i].selected="selected";
				break;
			}
			if(modelId=='0'){
				modelGroupSel.options[0].selected="selected";
				break;
			}
		}
		var modelPartSel = $('modelPartSel');
		for(var i = 0; i < modelPartSel.options.length; i++){
			if(modelPartSel.options[i].value==$('modelPart').value){
				modelPartSel.options[i].selected="selected";
				break;
			}
		}
	}
}

function reBack(){
	location="<%=contextPath%>/claim/basicData/HomePageNews/ApplicationKnowledgeBase.do";
}
function save(){
	if($('listModelGroup').value!=""&&('modelPartSel').value==""){
		MyAlert('请选择维修部位!');
		return;
	}
	makeNomalFormCall("<%=contextPath%>/claim/basicData/HomePageNews/updateApplicationKnowledgeBase.json",saveBack,'fm','');
}

function saveBack(json){
	if(json.success == "success")
	{
		parent.MyAlert("修改成功！");
	}
	else
	{
		parent.MyAlert("修改失败！请联系系统管理员！");
	}
	history.back();
}
</script>
</body>
</html>