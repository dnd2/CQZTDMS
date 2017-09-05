<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> 
<%@ page import="com.infodms.dms.common.Constant"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>密码维护</title>
<script type="text/javascript">
	function confirmAdd() {
		var newPass = document.getElementById("newPassword");
		var checkNewPass = document.getElementById("checkNewPassword");
		var isAdmin = document.getElementById("isAdmin");
		if(document.getElementById("passId").value == ""){
			if(newPass.value == ""){
				MyAlert("请输入新密码");			
			}else if(checkNewPass.value == ""){
				MyAlert("请重复输入新密码");
			}else if(newPass.value != checkNewPass.value){
				MyAlert("两次密码输入不一致，请重新输入");
			}else if(!isNumAndStr(newPass)){
				if(isAdmin.value=='true'){
					MyAlert("密码至少有八个字符，必须同时包含字母和数字");
				}else{
					MyAlert("密码至少有六个字符，必须同时包含字母和数字");
				}
			}else {
				fm.action="<%=contextPath%>/sysusermng/sysuserinfo/SysPasswordManager/modefyPasswordInfo.do";
				fm.submit(); 
			}	
		}else {
			if(document.getElementById("nowPassword").value == ""){
				MyAlert("请输入原密码");
			}else if(newPass.value == ""){
				MyAlert("请输入新密码");			
			}else if(checkNewPass.value == ""){
				MyAlert("请重复输入新密码");
			}else if(newPass.value != checkNewPass.value){
				MyAlert("两次密码输入不一致，请重新输入");
			}else if(!isNumAndStr(newPass)){
				if(isAdmin.value=='true'){
					MyAlert("密码至少有八个字符，必须同时包含字母和数字");
				}else{
					MyAlert("密码至少有六个字符，必须同时包含字母和数字");
				}
			}else {
				fm.action="<%=contextPath%>/sysusermng/sysuserinfo/SysPasswordManager/modefyPasswordInfo.do";
				fm.submit(); 
			}	
		}
	}
	
	function isNumAndStr(elem){
	     var str = elem.value; 
	     var regexpUperStr=/[A-Z]+/;
	     var reexpLowerStr=/[a-z]+/;
	     var regexpNum=/\d+/;
	     var uperStrFlag = regexpUperStr.test(str);
	     var lowerStrFlag = reexpLowerStr.test(str);
	     var numFlag = regexpNum.test(str);
	     var isAdmin = document.getElementById("isAdmin");
	     if(((uperStrFlag&&lowerStrFlag)||(lowerStrFlag&&numFlag)||(uperStrFlag&&numFlag))){
		     if(isAdmin.value=='true'){
			     if(str.length>=8){
				     return true;
				 }
			 }else{
				 if(str.length>=6){
					 return true;
					 }
		     }
		 }
	     return false;
	   }

</script>
</head>
<body>
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 个人信息管理  &gt; 个人信息管理   &gt; 密码修改</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input id="userId" name="userId" type="hidden" value="${user.userId}"/>
		<input id="passId" name="passId" type="hidden" value="${user.password}"/>
		<input id="isAdmin" name="isAdmin" type="hidden" value="${isAdmin}"/>	
		<div class="form-panel">
			<h2>密码修改</h2>
			<div class="form-body">
				<table class="table_query table-center" border="0">
					<c:if test="${checkMessage != null && checkMessage != ''}">
						<tr>
							<td colspan="2" style="text-align: center;">&nbsp;<font color="red">*&nbsp;注意：【${checkMessage}】</font>&nbsp;</td>
						</tr>
					</c:if>
					<tr>
						<td>&nbsp;用户名：&nbsp;</td>
						<td>&nbsp;${user.acnt}&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;原密码：&nbsp;</td>
						<td>
							<input class="middle_txt" type="password" id="nowPassword" name="nowPassword"/>
							<font color="red">&nbsp;*&nbsp;(第一次设定密码此项可不填写)</font>
						</td>
					</tr>
					<tr>
						<td>&nbsp;新密码：&nbsp;</td>
						<td>
							<input class="middle_txt" type="password" id=newPassword name="newPassword"/>
						</td>
					</tr>
					<tr>		  		
						<td>&nbsp;再输一遍：&nbsp;</td>
						<td>
							<input class="middle_txt" type="password" id=checkNewPassword name="checkNewPassword"/>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<input class="u-button u-submit" type="button" value="确定"  onclick="confirmAdd();"/>
						</td>
					</tr>
				</table>
			</div>
		</div>
	</form>
</div>	
</body>
</html>