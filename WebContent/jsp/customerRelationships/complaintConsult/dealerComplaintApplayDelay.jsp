<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>经销商延期申请</title>
<script type="text/javascript">

	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body>
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户关系管理 &gt; 投诉咨询管理 &gt;经销商延期申请</div>
	<form method="post" name = "fm" id="fm">
		<table  border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
			<th colspan="4"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />延期申请</th>
			
			<tr>
				<td align="right" nowrap="true" style="width: 10%">延期至：</td>
				<td align="left" nowrap="true">
				    <input  class="middle_txt" type="text" name="delayDate" id="delayDate" value=""  datatype="1,is_date,10"/>
				    <input class="time_ico" value=" " onclick="showcalendar(event, 'delayDate', false);"   type="button"/>   
				   
				    <input type="hidden" id="cpid" name="cpid" value="${cpid}"> 
				    <input type="hidden" id="endDate" name="endDate" value="${endDate}">  
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="true" style="width: 10%">延期原因：</td>
				<td align="left" nowrap="true">
					<textarea rows="5"  style="width: 95%" id="reason" name="reason"></textarea>
				</td>
			</tr>
			
			<tr>
				<td colspan="8" align="center">
					<input class="normal_btn" type="button" value="保存" name="saveButton" id="saveButton" onclick="save()" />
					&nbsp;
					<input class="normal_btn" type="button" value="返回"  onclick="history.back();" />
        		</td>
			</tr>
			
		</table>
		
	</form>
	<script type="text/javascript">
		function save(){
			//验证
			if(check()){
				document.getElementById("saveButton").disabled = true;
				makeNomalFormCall('<%=contextPath%>/customerRelationships/complaintConsult/DealerComplaintDeal/applayDelaySubmit.json',saveBack,'fm','');
			}
		}
		
		function saveBack(json){
			if(json.isSuccess != null && json.isSuccess=='true'){
				MyAlertForFun("保存成功",sendPage);
			}else {
				MyAlert("保存失败,请联系管理员!");
			}
			document.getElementById("saveButton").disabled = false;
		}
		
		function check(){
			document.getElementById("saveButton").disabled = true;
			var msg ="";
			if(""==document.getElementById('delayDate').value){
				msg+="延期时间不能为空!</br>"
			}else{
				var dateS = document.getElementById('delayDate').value;
				var dateE = document.getElementById('endDate').value;
				date1 = dateS.split("-");
	            date2 = dateE.split("-");
	            // 创建 Date对象
	            var tDate1 = new Date(date1[0], date1[1], date1[2]);
	            var tDate2 = new Date(date2[0], date2[1], date2[2]);
	            if (tDate1 <= tDate2)
	            {
	                msg+="延期时间不能小于等于关闭时间，请重新选择!应关闭时间为:"+dateE+"</br>";
	            } 
			}
			if(""==document.getElementById('reason').value ){
				msg+="延期原因不能为空!</br>"
			}else if(!WidthCheck(document.getElementById('reason').value,1000)){
				msg+="延期原因太长!</br>"
			}
			
			if(msg!=""){
				MyAlert(msg);
				document.getElementById("saveButton").disabled = false;
				return false;
			}else{
				return true;
			}
			
		}
		
		//   判断长度是否合格 
		// 
		// 引数 s   传入的字符串 
		//   n   限制的长度n以下 
		function WidthCheck(s, n){   
			var w = 0;   
			for (var i=0; i<s.length; i++) {   
			   var c = s.charCodeAt(i);   
			   //单字节加1   
			   if ((c >= 0x0001 && c <= 0x007e) || (0xff60<=c && c<=0xff9f)) {   
			    w++;   
			   }   
			   else {   
			    w+=2;   
			   }   
			}   
			if (w > n) {   
			   return false;   
			}   
			return true;   
		}
		
				//页面跳转：
		function sendPage(){
			window.location.href = "<%=contextPath%>/customerRelationships/complaintConsult/DealerComplaintDeal/dealerComplaintDealInit2.do";
		}

	</script>
</body>
</html>