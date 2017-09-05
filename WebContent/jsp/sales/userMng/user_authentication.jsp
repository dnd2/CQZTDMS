<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
	
	String isSpecialCar = String.valueOf(request.getAttribute("isSpecialCar"));
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<title>顾问认证</title>
<script type="text/javascript">
	function doInit(){
		loadcalendar();   //初始化时间控件
	}
//审核操作
function userAuthens(){
	var authenTime=document.getElementById("authenDate").value;
	if(authenTime==null||authenTime==''){
		MyAlert("请选择认证时间!");
		return ;
	}
	MyConfirm("确认认证?", submitSure) ;
}
function submitSure() {
	document.getElementById("userAuthen").disabled = true ;
	var url = "<%=contextPath%>/sales/usermng/UserManage/userAuthen.json";
	makeFormCall(url, SubmitTip, "fm") ;
}
//点击提交后执行的方法
function SubmitTip(json) {
	var subFlag = json.subFlag ;
	
	if(subFlag == 'success') {
		MyAlert("认证成功!") ;
		$('fm').action= "<%=contextPath%>/sales/usermng/UserManage/userAuthenInit.do";
		$('fm').submit();
	} else {
		document.getElementById("userAuthen").disabled = false ;
		MyAlert("认证失败!") ;
	}
}
	
</script>

</head>

<body>
<div class="wbox">
<div class="navigation"><img src="/dms/img/nav.gif" />&nbsp;当前位置： 经销商实销管理 &gt; 人员管理 &gt; 顾问认证</div>
<form id="fm" name="fm" method="post">
<input type="hidden" name="curPage" id="curPage" value="1" />
<input type="hidden" name="personId" id="personId" style="width:153px" value="${tvpp.personId}" datatype="0,is_textarea,30" />
<div style="display:none">
	<script type="text/javascript">
	                genSelBoxExp("status",9999,"99991003",false,"mini_sel","","false",'');
	</script> 
</div>
	<table class="table_query" border="0">
		<tr>
			<td align="right">经销商代码：</td>
			<td align="left">
			${dealer.dealerCode}
			</td>
			<td align="right">经销商名称：</td>
			<td align="left">
			${dealer.dealerShortname} 
			</td>
		</tr>
		<tr>
			<td align="right">人员姓名：</td>
			<td  align="left">${tvpp.name}</td>
			<td align="right">身份证号：</td>
			<td align="left">${tvpp.idNo}</td>
		</tr>
		<tr>
          <td align="right">电子邮件：</td>
          <td align="left">${tvpp.email}</td>
           <td align="right">联系电话：</td>
          <td align="left">${tvpp.mobile}</td>
	  </tr>
	  <tr>
          <td align="right">职位：</td>
           <td align="left"><script>document.write(getItemValue('${tvpp.position}'));</script></td>
           <td align="right">性别：</td>
			 <td align="left"><script>document.write(getItemValue('${tvpp.gender}'));</script></td>
		</tr>
		<tr>
			<td align="right">学历：</td>
			<td align="left">
				<script>document.write(getItemValue('${tvpp.degree}'));</script>
			</td>
			<td align="right">入职日期：</td>
			<td align="left">${entryDate}</td>
		</tr>
			
		<tr>
          <td align="right">是否投资人：</td>
           <td align="left"><c:if test="${tvpp.isInvestor==99951001}">是</c:if><c:if test="${tvpp.isInvestor==99951002}">否</c:if></td>
            <td align="right">所属银行：</td>
          <td align="left"><label>工商银行</label></td>
	  </tr>
		<tr>
          <td align="right">银行卡号：</td>
          <td align="left">${tvpp.bankCardno}</td>
          <td align="right">现在等级：</td>
			<td align="left">	  <script>document.write(getItemValue('${tvpp.authenticationLevel}'));</script></td>
	  </tr>
		<tr>
			<td align="right">认证后等级：</td>
			<td align="left"><c:if test="${tvpp.authenticationLevel==null||tvpp.authenticationLevel==0}">三星级销售顾问</c:if><c:if test="${tvpp.authenticationLevel==29911001}">四星级销售顾问</c:if><c:if test="${tvpp.authenticationLevel==29911002||tvpp.authenticationLevel==29911003}">五星级销售顾问</c:if></td>
			<td align="right">认证日期：</td>
			<td align="left">
				<input class="short_txt" readonly="readonly" type="text" id="authenDate" name="authenDate" datatype="0,is_date,10" />
				<input class="time_ico" type="button" onclick="showcalendar(event, 'authenDate', false);" value="&nbsp;" />			</td>
		</tr>
<!--		<tr>-->
<!--			<td align="right">备注：</td>-->
<!--			<td align="left" colspan="3">-->
<!--				${tvpp.remark}-->
<!--			</td>-->
<!--			-->
<!--		</tr>-->
		<tr>
		<td align="right">认证说明：</td>
			<td align="left" colspan="3">
				<textarea rows="3" cols="25" name="auth_remark" id="auth_remark"></textarea>
			</td>
		</tr>
		<tr>
			<td align="center" colspan="4">
				<input type="button" class="normal_btn" onclick="userAuthens();" value="认证" id="userAuthen" />
				<input type="button" class="normal_btn" onclick="javascript:history.back() ;" value="返 回" id="retBtn" />
			</td>
		</tr>
	</table>
</form>
</div>
</body>
</html>
