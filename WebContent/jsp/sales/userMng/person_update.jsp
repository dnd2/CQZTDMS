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
<title>人员修改</title>
<script type="text/javascript">
	function doInit(){
		loadcalendar();   //初始化时间控件
	}
//修改操作
function userUpdates(){
	var textStr = document.getElementById("remark").value ;
	if(submitForm("fm")){
		//if(textStr.length == 0) {
		//	MyAlert("备注不能为空！") ;
			
		//	return false ;
		//}
		//验证银行卡号
		if(!checkBankCardNO()){
			return false;
		}
		//验证电子邮件
		if(!checkEmail()){
			return false;
		}
		//验证电话
		if(!checkMobile()){
			return false;
		}
	}
	checkIdNoRepeat();
	
}

//验证银行卡号
function checkBankCardNO(){
	var cardNO=document.getElementById("bankCardNo").value;
	var str="0123456789";
	for(var i=0;i<cardNO.length;i++){
		if(str.indexOf(cardNO.substr(i,1))<0){
			MyAlert("银行卡号为纯数字，您输入有误!");
			return false;
		}
	}
	return true;
}
//验证电子邮件
function checkEmail(){
	var email=document.getElementById("email").value;
	var regex=/^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9_]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
	
	if(email==null||""==email){
		return true;
	}
	if(!regex.test(email)){
		MyAlert("您输入电子邮件有误!");
		return false;
	}
	return true;
}
//验证电话
function checkMobile(){
	var mobile=document.getElementById("mobile").value;
	var str="0123456789-";
	for(var i=0;i<mobile.length;i++){
		if(str.indexOf(mobile.substr(i,1))<0){
			MyAlert("电话只能为数字和-组成!");
			return false;
		}
	}
	return true;
}
//验证身份证的是否重复
	function checkIdNoRepeat(){
		var url = "<%=contextPath%>/sales/usermng/UserManage/userIdNoCheck.json?flag=1";
		makeFormCall(url, checkRepeat, "fm") ;
	}
	function checkRepeat(json){
			if(json.count>0){
				MyAlert("您输入的身份证人员在职,请重新输入!");
				return false;
			}
			checkBankCardRepeat();
	}
	//检查银行卡号是否重复
	function checkBankCardRepeat(){
		var url = "<%=contextPath%>/sales/usermng/UserManage/userBankCardCheck.json?flag=1";
		makeFormCall(url, checkCardRepeat, "fm") ;
	}

function checkCardRepeat(json){
	if(json.count>0){
			MyAlert("您输入的银行卡号已经存在,请重新输入!");
			return false;
	}else{
		checkPersonCount() ;
	}
}
//验证经理级别的人员不超过2人
function checkPersonCount(){
	var url = "<%=contextPath%>/sales/usermng/UserManage/userPisitionCountCheck.json";
	makeFormCall(url, PersonCountRes, "fm") ;
}

function PersonCountRes(json){
	if(json.count>=1){
			MyAlert("该职位最多只能注册1人,请重新选择!");
			return false;
	}else{
		MyConfirm("确认修改?", submitSure) ;
	}
}
function submitSure() {
	document.getElementById("userUpdate").disabled = true ;
	var url = "<%=contextPath%>/sales/usermng/UserManage/personUpdate.json";
	makeFormCall(url, SubmitTip, "fm") ;
}
//点击提交后执行的方法
function SubmitTip(json) {
	var subFlag = json.subFlag ;
	
	if(subFlag == 'success') {
		MyAlert("修改成功!") ;
		//$('fm').action= "<%=contextPath%>/sales/usermng/UserManage/personUpdateInit.do";
		//$('fm').submit();
		history.back();
	} else {
		document.getElementById("userAudit").disabled = false ;
		MyAlert("修改失败!") ;
	}
}
</script>

</head>

<body>
<div class="wbox">
<div class="navigation"><img src="/dms/img/nav.gif" />&nbsp;当前位置： 经销商实销管理 &gt; 人员管理 &gt; 机构人员信息修改</div>
<form id="fm" name="fm" method="post">
<input type="hidden" name="curPage" id="curPage" value="1" />
<input type="hidden" name="dealerId" id="dealerId" value="${tvprp.dealerId}" />
<input type="hidden" name="idNO" id="id_no" value="${tvprp.idNo}" />
<input type="hidden" name="personId" id="personId" style="width:153px" value="${tvprp.personId}" datatype="0,is_textarea,30" />
<div style="display:none">
	<script type="text/javascript">
	                genSelBoxExp("status",9999,"99991003",false,"mini_sel","","false",'');
	</script> 
</div>
	<table class="table_query" border="0">
		<tr>
			<td align="right">人员姓名：</td>
			<td align="left">
				<input type="text" class="middle_txt" name="name" id="name" value="${tvprp.name}"  datatype="0,is_textarea,30" />			</td>
		</tr>
		<tr>
			<td align="right">身份证号：</td>
			<td align="left">
			<label>${tvprp.idNo}</label>
<!--				<input type="text" class="middle_txt" name="idNO" id="idNO" value="${tvprp.idNo}"  datatype="0,is_textarea,18" />			</td>-->
		</tr>
		<tr>
          <td align="right">电子邮件：</td>
		  <td align="left"><input type="text" class="middle_txt" name="email" id="email" value="${tvprp.email}"   />          </td>
	  </tr>
		<tr>
          <td align="right">联系电话：</td>
		  <td align="left"><input type="text" class="middle_txt" name="mobile" id="mobile" value="${tvprp.mobile}"  datatype="0,is_textarea,18" />          </td>
	  </tr>
	  <tr>
          <td align="right">职位：</td>
		  <td align="left">
		 		 <script type="text/javascript">
	                genSelBoxExp("position",9996,"${tvprp.position}",false,"mini_sel","","false",'');
	            </script> 
	       </td>
	  	</tr>
		<tr>
			<td align="right">性别：</td>
			<td align="left">
				<script type="text/javascript">
	                genSelBoxExp("gender",1003,"${tvprp.gender}",false,"mini_sel","","false",'');
	            </script>
	         </td>
		</tr>
		<tr>
			<td align="right">学历：</td>
			<td align="left">
				<script type="text/javascript">
	                genSelBoxExp("degree",2994,"${tvprp.degree}",false,"mini_sel","","false",'');
	            </script>
	         </td>
		</tr>
		<tr>
			<td align="right">入职日期：</td>
			<td align="left">
				<input class="short_txt" readonly="readonly" type="text" id="entryDate" name="entryDate" datatype="0,is_date,10" value="${entryDate}"/>
				<input class="time_ico" type="button" onclick="showcalendar(event, 'entryDate', false);" value="&nbsp;" />			</td>
		</tr>
		<tr>
          <td align="right">是否投资人：</td>
		  <td align="left">
		  		<script type="text/javascript">
	                genSelBoxExp("isInvestor",9995,"${tvprp.isInvestor}",false,"mini_sel","","false",'');
	            </script>   
	      </td>
	  </tr>
		<tr>
          <td align="right">所属银行：</td>
		  <td align="left">
		 	 <select name="bank" id="bank">
			  		<option value="99971002">工商银行</option>
			  	</select>
<!--		  <script type="text/javascript">-->
<!--	                genSelBoxExp("bank",9997,"",false,"mini_sel","","false",'');-->
<!--	            </script> -->
	                     </td>
	  </tr>
		<tr>
          <td align="right">银行卡号：</td>
		  <td align="left">
		  <input type="text"  maxlength="20" name="bankCardNO" id="bankCardNO" style="width: 224px;" datatype="0,is_textarea,20" value="${tvprp.bankCardno}"/>
          </td>
	  </tr>
		<tr>
			<td align="right">备注：</td>
			<td align="left">
				<textarea id="remark" name="remark" rows="3" cols="30"  >${tvprp.remark}</textarea>&nbsp;
			</td>
		</tr>
	</table>
	<table border="0" align="center">
	<tr>
		<td align="center" colspan="2">
			<input type="button" class="normal_btn" onclick="userUpdates();" value="修改" id="userUpdate" />
			<input type="button" class="normal_btn" onclick="javascript:history.back() ;" value="返 回" id="retBtn" />
		</td>
	</tr>
</table>
</form>
</div>
</body>
</html>
