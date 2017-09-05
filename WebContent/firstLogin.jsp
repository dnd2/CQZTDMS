<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%
String contextPath = request.getContextPath();
%>
<title>君马新能源DCS系统</title>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
</head>
<body onload="loadcalendar();" >
<form id="fm" name="fm" method="post">
	<input type="hidden" name="curPage" id="curPage" value="1" />
	<input id="userId" name="userId" type="hidden" value="${user.userId}"/>
	<input id="passId" name="passId" type="hidden" value="${user.password}"/>
	<input id="isAdmin" name="isAdmin" type="hidden" value="${isAdmin}"/>	
		<table class="table_query" border="0">
			<c:if test="${checkMessage != null && checkMessage != ''}">
				<tr>
			  		<td colspan="2" style="text-align: center;">&nbsp;<font color="red">*&nbsp;注意：【${checkMessage}】</font>&nbsp;</td>
				</tr>
			</c:if>
			<tr>
		 		<td style="width: 50%;text-align: right;">&nbsp;用户名：&nbsp;</td>
		  		<td style="width: 50%;text-align: left;">&nbsp;${user.acnt}&nbsp;</td>
			</tr>
			<tr>
		 		<td style="width: 50%;text-align: right;">&nbsp;原密码：&nbsp;</td>
		  		<td style="width: 50%;text-align: left;">
		  			<input class="middle_txt" type="password" id="nowPassword" name="nowPassword"/>
		  		</td>
			</tr>
			<tr>
				<td style="width: 50%;text-align: right;">&nbsp;新密码：&nbsp;</td>
		  		<td style="width: 50%;text-align: left;">
		  			<input class="middle_txt" type="password" id=newPassword name="newPassword"/>
		  		</td>
		  	</tr>
			<tr>		  		
		  		<td style="width: 50%;text-align: right;">&nbsp;再输一遍：&nbsp;</td>
				<td style="width: 50%;text-align: left;">
					<input class="middle_txt" type="password" id=checkNewPassword name="checkNewPassword"/>
				</td>
			</tr>
			<tr>
				<td colspan="2" style="text-align: center;">
					<input class="normal_btn" type="button" value="确定"  onclick="confirmAdd();"/>
				</td>
			</tr>
		</table>
	</form>
</body>
<script language="javascript"><!--
	
	function confirmAdd() {
		var newPass = document.getElementById("newPassword");
		var checkNewPass = document.getElementById("checkNewPassword");
		var isAdmin = document.getElementById("isAdmin");
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
			}else{
				makeNomalFormCall('<%=contextPath%>/common/UserManager/modefyPassword.json',showReportRes,'fm');
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

	function showReportRes(json){
		if(json.returnValue == '1'){
			MyAlert("密码修改成功!");
			fm.action='<%=contextPath%>/common/UserManager/login.do';
			fm.submit();
		}else if(json.returnValue == '0'){
			MyAlert("原始密码错误!");
		}else{
			MyAlert("密码修改失败!");
		}
	}

</script>
</html>