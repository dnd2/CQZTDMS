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
<title>个人信息维护</title>
<script type="text/javascript">
	function confirmAdd() {
		if(!submitForm('fm')){
			return;	
		}
		if(document.getElementById("name").value==""){
			MyAlert("请填写姓名");
	 	}else if(document.getElementById("phone").value==""){
			MyAlert("请填写电话");
	 	}else if(document.getElementById("handPhone").value==""){
			MyAlert("请填写手机");
	 	}else{
			fm.action="<%=contextPath%>/sysusermng/sysuserinfo/SysUserInfoManager/modifyUserInfo.do";
			fm.submit(); 	
	 	}
	}
</script>
</head>
<body onload="__extQuery__(1);">
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 个人信息管理  &gt; 个人信息管理   &gt; 个人信息维护 </div>
	<form id="fm" name="fm" method="post">
	<input type="hidden" name="curPage" id="curPage" value="1" />
	<input id="userId" name="userId" type="hidden" value="${user.userId}" datatype="0,is_null,20"/><div class="form-panel">
<div class="form-panel">
<h2>基本信息</h2>
			<div class="form-body">
		<table class="table_query" border="0">
<!--			<tr>-->
<!--		 		<td style="width: 10%;text-align: right;">&nbsp;登陆账号：&nbsp;</td>-->
<!--		  		<td>${user.acnt}</td>-->
<!--			</tr>-->
			<tr>
		 		<td style="text-align: right;">登陆账号：</td>
		  		<td>${user.acnt}</td>
		  		<td style="text-align: right;">最后登录时间：</td>
		  		<td><fmt:formatDate value="${user.lastsigninTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
			<td style="text-align: right;">姓名：</td>
		  		<td>
		  			<input class="middle_txt" type="text" id="name" name="name" value="${user.name}" />
		  			<font color="red">&nbsp;*</font>
		  		</td>
		  		</tr>
			<tr>
		  		<td style="text-align: right;">性别：</td>
				<td>
					<script type="text/javascript"> genSelBox("gender",<%=Constant.GENDER_TYPE%>,${user.gender},false,"","");</script>
				</td>
				<td style="text-align: right;">出生年月日：</td>
		  		<td >
		  			<input name="birthday" type="text" class="middle_txt" style="width:80px" id="birthday" value="<fmt:formatDate value="${user.birthday}" pattern="yyyy-MM-dd"/>" />
           			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'birthday', false);" value="" />
		  		</td>
		  		<td style="text-align: right;">邮政编码：</td>
		  		<td>
		  			<input class="middle_txt" datatype="0,is_digit,6" maxlength="6" type="text" id="zipCode" name="zipCode" value="${user.zipCode}" />
		  		</td>
			</tr>
			<tr>
				<td style="text-align: right;">电话：</td>
				<td >
					<input class="middle_txt" type="text" id="phone" name="phone"  datatype="1,is_phone,15"
					 value="${user.phone}" />
					<font id="dhh" color="bule">&nbsp;*</font>
				</td>
				<td style="text-align: right;">手机：</td>
				<td>
					<input class="middle_txt" type="text" id="handPhone"  datatype="1,is_phone,15"
					name="handPhone" value="${user.handPhone}"/>
					<font id="dhh" color="bule">&nbsp;*</font>
				</td>
				<td style="text-align: right;">E-mail：</td>
				<td>
					<input class="middle_txt" type="text" id="email" name="email" datatype="1,is_email,50"
					 value="${user.email}" />
				</td>
			</tr>
			<tr>
				<td style="text-align: right;">地址：</td>
				<td>
					<input class="middle_txt" type="text" id="addr" name="addr" value="${user.addr}" />
				</td>
				<td colspan="4"></td>
			</tr>
			<tr>
				<td colspan="6" style="text-align: center;">
					<input id="queryBtn" class="normal_btn" type="hidden" onclick="__extQuery__(1)" />	
					<input class="normal_btn" type="button" value="修改"  onclick="confirmAdd();"/>
				</td>
			</tr>
		</table>
		</div>
		</div>
		<div class="form-panel">
<h2>职位信息</h2>
			<div class="form-body">
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />		
		</div>
		</div>
	</form>
	<script type="text/javascript" >
		var myPage;
		var url = "<%=contextPath%>/sysusermng/sysuserinfo/SysUserInfoManager/queryUserInfo.json?command=1";
		var title= null;
	
		var columns = [
						{header: "序号",width:'10%',  renderer:getIndex},
						{header: "所属组织", width:'45%', dataIndex: 'orgDesc'},
						{header: "职位名称", width:'45%', dataIndex: 'poseName'}
					  ];   
		
	</script>
	</div>
</body>
</html>