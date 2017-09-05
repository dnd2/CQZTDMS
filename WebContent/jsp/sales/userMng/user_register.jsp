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
<title>人员注册</title>
<script type="text/javascript">
function doInit(){
		document.getElementById("bank").readonly=true;
		loadcalendar();   //初始化时间控件
}
function addSubmit(){
	checkIdNoRepeat();
}
function submitSure() {
	document.getElementById("addSub").disabled = true ;
	var url = "<%=contextPath%>/sales/usermng/UserManage/userAdd.json";
	makeFormCall(url, SubmitTip, "fm") ;
}
//点击提交后执行的方法
function SubmitTip(json) {
	var subFlag = json.subFlag ;
	
	if(subFlag == 'success') {
		MyAlert("保存成功!") ;
		
		$('fm').action= "<%=contextPath%>/sales/usermng/UserManage/userRegisterInit.do";
		$('fm').submit();
	} else {
		document.getElementById("addSub").disabled = false ;
		MyAlert("保存失败!") ;
	}
}

//添加验证身份证号码 
function isIdentityNumber() {
	var identityNumber = document.getElementById("idNO").value
	var sexOnPage = document.getElementById('gender').value;
	identityNumber = identityNumber.toUpperCase();  
    //身份证号码为15位或者18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X。   
    if(!(/(^\d{15}$)|(^\d{17}([0-9]|X)$)/.test(identityNumber))) { 
    	MyAlert('输入的身份证号长度不对，或者号码不符合规定！15位号码应全为数字，18位号码末位可以为数字或X。'); 
        return false; 
    };
    var len = identityNumber.length;
    var year;
    var month;
    var day;
    var sex;
    if(len == 15) {
    	year = identityNumber.substr(6,2);
    	year += '19';
        month = identityNumber.substr(8,2);     
        day = identityNumber.substr(10,2); 
        sex = identityNumber.substr(14);
    } else if(len == 18) {
    	year = identityNumber.substr(6,4);     
        month = identityNumber.substr(10,2);     
        day = identityNumber.substr(12,2); 
        sex = identityNumber.substr(16,1);
    }
   	 //验证月份
	   if(month.substr(0,1)!=0){
	    	if(parseInt(month)>12){
	    		MyAlert("输入的身份证号月份不匹配!");
	    		return false;
	    	}
	 	}
	 	if(day.substr(0,1)!=0){
	 		if(parseInt(day)>31){
	    		MyAlert("输入的身份证号天数不匹配!");
	    		return false;
	    	}
	 	}
    if(sex % 2 == 0) {
    	if(sexOnPage == "10031001") {
    		MyAlert("输入的身份证号的性别和用户选择的性别不匹配!");
    		return false;
    	} 
    } else {
    	if(sexOnPage == "10031002") {
    		MyAlert("输入的身份证号的性别和用户选择的性别不匹配!");
    		return false;
    	} 
    }
    return true;
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
	var regex=/^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
	regex=/^[a-zA-Z0-9_-]+@\w+\.\w+$/
	if(email==null||""==email){
		return true;
	}
	if(!regex.test(email)){
		MyAlert("您输入电子邮件有误!");
		return false;
	}
	return true;
}
	//验证身份证的是否重复
	function checkIdNoRepeat(){
		var url = "<%=contextPath%>/sales/usermng/UserManage/userIdNoCheck.json";
		makeFormCall(url, checkRepeat, "fm") ;
	}
	
	function checkRepeat(json){
		if(submitForm("fm")) {
			//验证银行卡号
			if(!checkBankCardNO()){
				return false;
			}
			//验证电子邮件
			if(!checkEmail()){
				return false;
			}
			//验证身份证号码
			if(!isIdentityNumber()) {
				return false;
			};
			if(json.count>0){
				MyAlert("您输入的身份证人员在职,请重新输入!");
				return false;
			}
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
		submitSure() ;
	}
}

</script>

<script type="text/javascript" src="<%=contextPath%>/js/sales/userMng/user_register.js" language="javascript" ></script>
</head>

<body>
<div class="wbox">
<div class="navigation"><img src="/dms/img/nav.gif" />&nbsp;当前位置： 经销商实销管理 &gt; 人员管理 &gt; 人员注册</div>
<form id="fm" name="fm" method="post">
<input  type="hidden" name="path" id="path"/>
<input type="hidden" name="dealerId" id="dealerId" value="${dealerId}" />
<input type="hidden" name="curPage" id="curPage" value="1" />
<div style="display:none">
	<script type="text/javascript">
	                genSelBoxExp("status",9999,"99991001",false,"mini_sel","","false",'');
	</script> 
</div>
 
	<table class="table_query" border="0">
		<tr>
			<td align="right">人员姓名：</td>
			<td align="left">
				<input type="text" class="middle_txt" name="name" id="name" value="" datatype="0,is_textarea,30" />	
			</td>
			<td align="right">身份证号：</td>
			<td align="left">
				<input type="text" class="middle_txt" name="idNO" id="idNO" value="" datatype="0,is_textarea,18" />	
			</td>
		</tr>
		<tr>
			
		</tr>
		<tr>
          <td align="right">电子邮件：</td>
		  <td align="left"><input type="text" class="middle_txt" name="email" id="email" value=""  /> </td>
	  	 <td align="right">联系手机：</td>
		  <td align="left"><input type="text" class="middle_txt" name="mobile" id="mobile" datatype="0,is_mobile,11" />  
		   </td>
	  </tr>
	  <tr>
          <td align="right">职位：</td>
		  <td align="left">
		 		 <script type="text/javascript">
	                genSelBoxExp("position",9996,"",false,"mini_sel","","false",'');
	            </script> 
	       </td>
	       <td align="right">性别：</td>
			<td align="left">
				<script type="text/javascript">
	                genSelBoxExp("gender",1003,"",false,"mini_sel","","false",'');
	            </script>
	         </td>
	  	</tr>
		<tr>
		<td align="right">学历：</td>
			<td align="left">
				<script type="text/javascript">
	                genSelBoxExp("degree",2994,"",false,"mini_sel","","false",'');
	            </script>
	         </td>
			<td align="right">入职日期：</td>
			<td align="left">
				<input class="short_txt" readonly="readonly" type="text" id="entryDate" name="entryDate" datatype="0,is_date_now,10"/>
				<input class="time_ico" type="button" onclick="showcalendar(event, 'entryDate', false);" value="&nbsp;" />			</td>
		</tr>
		<tr>
          <td align="right">是否投资人：</td>
		  <td align="left">
		  		<script type="text/javascript">
	                genSelBoxExp("isInvestor",9995,"99951002",false,"mini_sel","","false",'');
	            </script>   
	      </td>
	      <td align="right">所属银行：</td>
		  <td align="left" >
		  	<select name="bank" id="bank">
		  		<option value="99971002">工商银行</option>
		  	</select>     
	              </td>
	  </tr>
		<tr>
          <td align="right">银行卡号：</td>
		  <td align="left">
		  <input type="text" name="bankCardNO" id="bankCardNO" style="width: 220px;" datatype="0,is_textarea,20"/>
          </td>
	  </tr>
		<tr>
			<td align="right">备注：</td>
			<td align="left" colspan="3">
				<textarea id="remark" name="remark" rows="3" cols="30"></textarea>
			</td>
		</tr>
		<tr>
		<td align="center" colspan="4">
			<input type="button" class="normal_btn" onclick="addSubmit();" value="保  存" id="addSub" />
			<input type="button" class="normal_btn" onclick="javascript:history.back() ;" value="返 回" id="retBtn" />
		</td>
	</tr>
	</table>
</form>
</div>
</body>
</html>
