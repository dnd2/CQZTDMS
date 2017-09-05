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
<title>注册人员审核</title>
<script type="text/javascript">
	function doInit(){
		loadcalendar();   //初始化时间控件
	}
	//验证身份证的是否重复
	function checkIdNoRepeat(){
		var id_no=$('idNO').value;
			var url = "<%=contextPath%>/sales/usermng/UserManage/userIdNoCheck.json?idNO="+id_no;
			makeFormCall(url, checkRepeat, "fm") ;
	}
	function checkRepeat(json){
		if(json.count>0){
			MyAlert("您输入的身份证人员在职,请驳回");
			return;
		}else{
			checkBankCardRepeat();
		}
	}
	//检查银行卡号是否重复
	function checkBankCardRepeat(){
		var url = "<%=contextPath%>/sales/usermng/UserManage/userBankCardCheck.json";
		makeFormCall(url, checkCardRepeat, "fm") ;
	}
	
	function checkCardRepeat(json){
		if(json.count>0){
				MyAlert("您输入的银行卡号已经存在,请驳回!");
				return false;
		}else{
			checkPersonCount() ;
		}
	}
	//验证经理级别的人员不超过1人
	function checkPersonCount(){
		var url = "<%=contextPath%>/sales/usermng/UserManage/userPisitionCountCheck.json";
		makeFormCall(url, PersonCountRes, "fm") ;
	}
	function PersonCountRes(json){
		if(json.count>=1){
				MyAlert("该职位最多只能注册一人,请驳回!");
				return false;
		}else{
			userAudits() ;
		}
	}


	//审核操作
	function userAudits(){
		MyConfirm("确认审核?", submitSure) ;
	}
	function submitSure() {
		document.getElementById("userAudit").disabled = true ;
		var url = "<%=contextPath%>/sales/usermng/UserManage/userAudit.json";
		makeFormCall(url, SubmitTip, "fm") ;
	}
	//点击提交后执行的方法
	function SubmitTip(json) {
		var subFlag = json.subFlag ;
		if(subFlag == 'success') {
			MyAlert("审核成功!") ;
			$('fm').action= "<%=contextPath%>/sales/usermng/UserManage/userAuditInit.do";
			$('fm').submit();
		} else {
			document.getElementById("userAudit").disabled = false ;
			MyAlert("审核失败!") ;
		}
	}
	function userRejects(){
		MyConfirm("确认驳回?", rejectSure) ;
	}
	function rejectSure(){
		document.getElementById("status").value=99991004;
		document.getElementById("userReject").disabled = true ;
		var url = "<%=contextPath%>/sales/usermng/UserManage/userAudit.json";
		makeFormCall(url, rejectTip, "fm") ;
	}
	function rejectTip(json){
		var subFlag = json.subFlag ;
		if(subFlag == 'success') {
			MyAlert("驳回成功!") ;
			$('fm').action= "<%=contextPath%>/sales/usermng/UserManage/userAuditInit.do";
			$('fm').submit();
		} else {
			document.getElementById("userReject").disabled = false ;
			MyAlert("驳回失败!") ;
		}
	}
</script>

</head>

<body>
<div class="wbox">
<div class="navigation"><img src="/dms/img/nav.gif" />&nbsp;当前位置： 经销商实销管理 &gt; 人员管理 &gt; 注册人员审核</div>
<form id="fm" name="fm" method="post">
<input type="hidden" name="curPage" id="curPage" value="1" />
<input type="hidden" name="registId" id="registId" style="width:153px" value="${tvprp.registId}"  />
<input type="hidden" name="idNO" id="idNO" style="width:153px" value="${tvprp.idNo}"  />
<input type="hidden" name="bankCardNo" id="bankCardNo" style="width:153px" value="${tvprp.bankcardNo}" />
<input type="hidden" name="position" id="position" style="width:153px" value="${tvprp.position}" />
<input type="hidden" name="dealerId" id="dealerId" style="width:153px" value="${tvprp.dealerId}" />
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
			<td align="left">
				<label >${tvprp.name}</label></td>
				<td align="right">身份证号：</td>
			<td align="left">
			<label >${tvprp.idNo}</label>
				</td>
		</tr>
		<tr>
			
		</tr>
		<tr>
          <td align="right">电子邮件：</td>
          <td align="left">
          <label >${tvprp.email}</label>
		   </td>
		   <td align="right">联系电话：</td>
          <td align="left">
          <label >${tvprp.mobile}</label></td>
	  </tr>
	  <tr>
          <td align="right">职位：</td>
           <td align="left"><c:if test="${tvprp.position==99961001}">销售顾问</c:if><c:if test="${tvprp.position==99961002}">销售经理</c:if><c:if test="${tvprp.position==99961003}">市场经理</c:if><c:if test="${tvprp.position==99961004}">总经理</c:if></td>
			<td align="right">性别：</td>
			 <td align="left"><c:if test="${tvprp.gender==10031001}">男</c:if><c:if test="${tvprp.gender==10031002}">女</c:if></td>
	  	</tr>
		<tr>
			<td align="right">学历：</td>
			<td align="left"><script>document.write(getItemValue('${tvprp.degree}'));</script></td>
			<td align="right">入职日期：</td>
			<td align="left">${entryDate}</td>
		</tr>
		<tr>
          <td align="right">是否投资人：</td>
           <td align="left"><script>document.write(getItemValue('${tvprp.isInvestor}'));</script></td>
            <td align="right">所属银行：</td>
          <td align="left"><script>document.write(getItemValue('${tvprp.bank}'));</script></td>
	  </tr>
		<tr>
          <td align="right">银行卡号：</td>
           <td align="left"><label>${tvprp.bankcardNo}</label></td>
           <td align="right">备注：</td>
			<td align="left">
				<label>${tvprp.remark}</label>
			</td>
	  </tr>
		<tr>
          <td align="right">审核意见：</td>
		  <td align="left" colspan="3">
		 		 <textarea rows="3" cols="30" name="audit"></textarea>
	       </td>
	  	</tr>
	  	<tr>
		<td align="center" colspan="4">
			<input type="button" class="normal_btn" onclick="checkIdNoRepeat();" value="审核通过" id="userAudit" />
			<input type="button" class="normal_btn" onclick="userRejects();" value="驳回" id="userReject" />
			<input type="button" class="normal_btn" onclick="javascript:history.back() ;" value="返 回" id="retBtn" />
		</td>
	</tr>
	</table>
</form>
</div>
</body>
</html>
