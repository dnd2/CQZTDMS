<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
	String inputId = request.getParameter("inputId") ;
	String inputCode = request.getParameter("inputCode") ;
	String inputName = request.getParameter("inputName") ;
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">
 <!--
 //http://10.33.5.245:8080/CHANADMS/groups/DivideGroupsAction/groupsDialogInit.do?groupType=80801001&groupArea=80811001
 function queryDtl() {
	 var oGroupId = document.getElementById("groupsId") ;
	 var oBtn = document.getElementById("subIt") ;
	 
	 if(oGroupId.value) {
		 oBtn.style.display = "inline" ;
	 	__extQuery__(1) ; 
	 } else {
		 oBtn.style.display = "none" ;
	 }
 }
 
 function setValues() {
	 var aInfo = document.getElementsByName("theIds") ;
	 var sIds = "" ;
	 var sCodes = "" ;
	 var sNames = "" ;
	 
	 var iLen = aInfo.length ;
	 
	 if(iLen > 0) {
		 for(var i=0; i<iLen; i++) {
			 if(aInfo[i].checked) {
				 var aInfoDtl = aInfo[i].value.split(",") ;
				 
				 if(sIds.length == 0) {
					 sIds = aInfoDtl[0] ;
					 sCodes = aInfoDtl[2] ;
					 sNames = aInfoDtl[1] ;
				 } else {
					 sIds += "," + aInfoDtl[0] ;
					 sCodes += "," + aInfoDtl[2] ;
					 sNames += "," + aInfoDtl[1] ;
				 }
			 }
		 }
	 } else {
		 MyAlert("请选择信息!") ;
		 
		 return false ;
	 }
	 
	 if("${inputId}") {
		 var sId = "${inputId}" ;
		 var oId = top.inIframe.document.getElementById(sId) ;
		 
		 if(oId) {
			 oId.value = sIds ;
		 }
	 }
	 
	if("${inputCode}") {
		 var sCode = "${inputCode}" ;
		 var oCode = top.inIframe.document.getElementById(sCode) ;
		 
		 if(oCode) {
			 oCode.value = sCodes ;
		 }
	 }
	 
	if("${inputName}") {
		 var sName = "${inputName}" ;
		 var oName = top.inIframe.document.getElementById(sName) ;
		 
		 if(oName) {
			 oName.value = sNames ;
		 }
	}
	
	_hide() ;
 }
 //-->
 </script>
<title>分组弹出框</title>
</head>
<body> 
<div>
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  分组弹出框</div>
		<form id="fm" name="fm" method="post">
			<table class="table_query">
				<tr>
					<td align="right"><label for="groupsId">组名称：</label></td>
					<td>
						<select id="groupsId" name="groupsId" onchange="queryDtl() ;">
							<option value="">-请选择-</option>
							<c:forEach var="po" items="${groupList }" >
								<option value="${po.GROUP_ID }">${po.GROUP_NAME }</option>
							</c:forEach>
						</select>
					</td>
					<td align="right"></td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<td colspan="5" align="left">
						<input type="button" class="normal_btn" style="display:none" name="subIt" id="subIt" value="确 认" onclick="setValues() ;" />&nbsp;
						<input type="button" class="normal_btn" name="closeIt" id="closeIt" value="关 闭" onclick="_hide() ;" />
					</td>
				</tr>
			</table>
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		</form>
</div>
<script type="text/javascript">
	var myPage;
	var url = "<%=contextPath%>/groups/DivideGroupsAction/queryDtl.json" ;
	var title = null;
	var columns = [
					{id:'check',header: "选择<input type='checkbox' name='checkAll' onclick='selectAll(this,\"theIds\");' checked='checked' />", width:'6%',sortable: false,dataIndex: 'THE_ID',renderer:myCheckBox}, //
					{header: "名称", dataIndex: 'THE_NAME', align:'center'},
					{header: "编码", dataIndex: 'THE_CODE', align:'center'}
			      ];
	
	function myCheckBox(value,metaDate,record){
		var theName = record.data.THE_NAME ;
		var theCode = record.data.THE_CODE ;
		
		var theValue = value + "," + theName + "," + theCode ;
		
		return String.format("<input type=\"checkbox\" name='theIds' value='" + theValue + "' checked='checked' />");
	}
</script>    
</body>
</html>