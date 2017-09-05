<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<title>人员转换</title>
<%
	String contextPath = request.getContextPath();
	
	String isSpecialCar = String.valueOf(request.getAttribute("isSpecialCar"));
%>
<script type="text/javascript">
 function addDealerName(){
 		var url = "<%=contextPath%>/sales/usermng/UserManage/userDealerName.json";
		makeFormCall(url, dealerNameReturn, "fm") ;
 }
 function  dealerNameReturn(json){
 	$("dealerName").innerText=json.dealerName;
 }
 
 function switchDealer(){
 	var url = "<%=contextPath%>/sales/usermng/UserManage/userSwitchDealer.json";
 	if(submitForm("fm")){
 		makeFormCall(url, switchDealerReturn, "fm") ;
 	}
	
 }
 function switchDealerReturn(json){
	 var count=json.count;
	 	if(count==1){
	 		MyAlert("转换成功！！");
	 		document.location.href="<%=contextPath%>/sales/usermng/UserManage/personSwitchPre.do";
	 	}else{
	 		MyAlert("转换失败！！");
	 	}
 }
</script>
</head>

<body >
<div class="wbox">
<div class="navigation"><img src="/dms/img/nav.gif" />&nbsp;当前位置： 经销商实销管理 &gt; 人员管理 &gt; 机构人员转换</div>
<form id="fm" name="fm" method="post">
	<input id="personId" name="personId" type="hidden" value="${personId}"/>
	<table class="table_query" border="0">
		<tr>
		<td align="left" colspan="2"><font size="4" color="red">原来的经销商信息</font></td>
		<td align="left" colspan="2"><font size="4" color="red">调入的经销商信息</font></td>
		</tr>
		<tr>
		<td align="center" colspan="2">&nbsp;</td>
		<td align="center" colspan="2">&nbsp;</td>
		</tr>
		<tr>
		<td align="left">原来的经销商代码：</td>
		<td align="left">${oldCode}&nbsp;</td>
		<td align="left">调入的经销商代码：</td>
		<td> 
		 <input name="dealerCode" type="text" id="dealerCode" class="middle_txt"  size="20" datatype="0,is_textarea,30" readonly />
		<input type="button" id="button1" value="..." class="mini_btn" onclick="showOrgDealerAll('dealerCode','dealerId','false','','true','true','<%=Constant.DEALER_TYPE_DVS%>,<%=Constant.DEALER_TYPE_JSZX%>,<%=Constant.DEALER_TYPE_QYZDL%>')"/>
		</td>
		</tr>
		<tr>
		<td align="left">原来的经销商名称：</td>
		<td align="left">${oldName}&nbsp;</td>
		<td align="left" style="display:none;">调入的经销商名称;</td>
		<td align="left"><span id="dealerName"></span>&nbsp;&nbsp;</td>
		</tr>
		<tr>
		  <td align="center" colspan="4">
		  <input type="button" class="normal_btn" onclick="switchDealer();" value="转 换" id="retBtn" />
		  </td>
		</tr>
	</table>
</form>
</div>
</body>
</html>
