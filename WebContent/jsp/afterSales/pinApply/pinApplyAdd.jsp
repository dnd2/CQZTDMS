<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<%@ page import="com.infodms.dms.common.Constant"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">
var globalContextPath = "<%=contextPath%>" ;
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>pin申请查询新增</title>
</head>
<body onload="loadcalendar();">
<div class="wbox">
	<div class="navigation"><font style="font-size: 13px;font-family:serif;"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理 &gt; 车辆信息管理管理&gt; PIN码查看申请新增页面</font></div>
	<form id="fm" name="fm" method="post">
	<div class="form-panel">
		<h2>基本信息</h2>
			<div class="form-body">
	<table class="table_query">
		<tr >			
				<td style="text-align:right">维修站编码：
				</td>
				<td>
					${dealerCode }
				</td>
				<td style="text-align:right">维修站名称：
				</td>
				<td>
					${dealerName }
				</td>
				<td style="text-align:right">制单日期：
				</td>
				<td>
					${date }
				</td>
			 </tr>	 
			  <tr >		
			  <td style="text-align:right">制单人：
				</td>
				<td>
					${userName }
				</td>	
				<td style="text-align:right">VIN号：</td>
  				<td>
  					<input class="middle_txt"  type="text" name="vin" id="vin" maxlength="18"/> 					
					<span style="PADDING-LEFT: 2px; WIDTH: 7px; HEIGHT: 18px; COLOR: red; FONT-SIZE: 9pt">*</span>
  				</td> 				
				<td style="text-align:right"></td>
				<td>
				</td>				
			 </tr>	 
			 <tr >
			  	<td style="text-align:right">
			  		备注：
				</td>
				<td colspan="5">
			  		<textarea rows="3" cols="80" onblur="bz();" id="reamk1" name="reamk1"></textarea>
			  		<input class="middle_txt"  type="hidden" name="reamk" id="reamk"/>
			  		<span style="PADDING-LEFT: 2px; WIDTH: 7px; HEIGHT: 18px; COLOR: red; FONT-SIZE: 9pt">*</span>
				</td>
			 </tr>
			 <tr >
				 <td colspan="6" style="text-align:center">
					<input class="normal_btn" type="button" value="确定" onclick="specilAdd();"/>&nbsp;
					<input class="normal_btn" type="button" value="返回" onclick="_hide();"/>&nbsp;		
			   	 </td> 
			</tr>
	</table>
	</div>
	</div>
</form>
<script type="text/javascript">
/* function Back(){
	window.location.href=globalContextPath+"/afterSales/pinApply/PinApplyAction/pinList.do";
} */
function bz(){
	var reamk1=document.getElementById("reamk1").value;
		document.getElementById("reamk").value=reamk1;
	if(document.getElementById("reamk").value.length>200){	
		reamk1=reamk1.substr(0,200);
		document.getElementById("reamk").value=reamk1;
		document.getElementById("reamk1").value=reamk1;
		MyAlert("备注字数超出限制长度！");
		return;
	}
}
function specilAdd(){
	if(document.getElementById("vin").value=="")
	 {
		MyAlert("VIN号不能为空!");
	  return ;
	 }
	if(document.getElementById("reamk").value=="")
	 {
		MyAlert("备注不能为空!");
	  return ;
	 }
	MyConfirm("是否保存?",specilAddInfo);	
}
function specilAddInfo(){
		var url=globalContextPath+"/afterSales/pinApply/PinApplyAction/pinListAdd.json?flag=t";
		makeNomalFormCall(url,callBackspecilAddInfo,'fm');  
}
function callBackspecilAddInfo(json){
	var msg=json.msg;
	if(msg=="00"){
		MyAlert("保存成功！");
		__parent().__extQuery__(1) ;
		_hide() ;
	}else{
		MyAlert("保存失败！");
	}	
}

</script>
</div>
</body>
</html>