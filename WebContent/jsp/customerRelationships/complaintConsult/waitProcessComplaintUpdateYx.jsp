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
<title>待投诉处理查询</title>
<script type="text/javascript">

	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body onload="changeBizTypeEvent('${complaintAcceptMap.BIZTYPE}','${complaintAcceptMap.BIZCONT}',false)">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 客户关系管理 &gt; 投诉咨询管理 &gt;待投诉处理查询</div>
	<form method="post" name = "fm" id="fm">
		<table width="100%" class="tab_edit">
			<th colspan="4" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />投诉单信息</th>
			<tr>
				<td width="20%" align="right" nowrap="true">客户名称：</td>
				<td width="30%" align="left" nowrap="true">
					<input id="ctmname" name="ctmname" type="text" value="${complaintAcceptMap.CTMNAME}">					
					<font color="red">*</font>
				</td>
				<td width="20%" align="right" nowrap="true">联系电话：</td>
				<td width="30%" align="left">
					<input id="phone" name="phone" type="text" value="${complaintAcceptMap.PHONE}" maxlength="40">
					<font color="red">*</font>
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="true">VIN：</td>
				<td align="left" nowrap="true">
					${complaintAcceptMap.VIN}
				</td>
				<td align="right" nowrap="true">行驶里程：</td>
				<td align="left">
					${complaintAcceptMap.MILEAGE}	
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="true">里程范围：</td>
				<td align="left" nowrap="true">
					${complaintAcceptMap.MILEAGERANGE}
				</td>
				<td align="right" nowrap="true">车辆用途：</td>
				<td align="left">
					${complaintAcceptMap.VINUSER}
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="true">故障部件：</td>
				<td align="left" nowrap="true">
					${complaintAcceptMap.FAULTPART}
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="true">省份：</td>
				<td align="left" nowrap="true">
					${complaintAcceptMap.PRO}
				</td>
				<td align="right" nowrap="true">城市：</td>
				<td align="left">
					${complaintAcceptMap.CITY}
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="true">车种：</td>
				<td align="left" nowrap="true">
					${complaintAcceptMap.SGROUP}
				</td>
				<td align="right" nowrap="true">购车日期：</td>
				<td align="left">
					${complaintAcceptMap.BUYDATE}
				</td>
			</tr>
			
			<tr>
				<td align="right" nowrap="true">生产日期：</td>
				<td align="left" nowrap="true">
					${complaintAcceptMap.PRDATE}
				</td>
				<td align="right" nowrap="true">车型：</td>
				<td align="left">
					${complaintAcceptMap.MGROUP}
				</td>
			</tr>


			<tr>
				<td align="right" nowrap="true">业务类型：</td>
				<td align="left" nowrap="true">		
					${complaintAcceptMap.BIZTYPE}
				</td>
				<td align="right" nowrap="true">内容类型：</td>
				<td align="left" nowrap="true">
					${complaintAcceptMap.BIZCONT}
				</td>
			</tr>
			
			<tr id="complaintTrOne">
				<td align="right" nowrap="true">抱怨级别：</td>
				<td align="left" nowrap="true">
					${complaintAcceptMap.CPLEVEL}
				</td>
				<td align="right" nowrap="true">规定处理期限：</td>
				<td align="left" nowrap="true">
					${complaintAcceptMap.CPLIMIT}
				</td>
			</tr>
			<tr id="complaintTrTwo">
				<td align="right" nowrap="true">抱怨对象：</td>
				<td align="left" nowrap="true" colspan="3">
					${complaintAcceptMap.COBJ}
				</td>
			</tr>
			<tr>
				<td align="right" nowrap="true">抱怨咨询内容：</td>
				<td align="left" nowrap="true" colspan="3">
					<textarea id="complaintContent" name="complaintContent" rows="5" style="width: 95%">${complaintAcceptMap.CPCONT}</textarea>
					<font color="red">*</font>
					<input type="hidden" id="cpid" name="cpid" value="${complaintAcceptMap.CPID}">
					<input type="hidden" id="pageId" name="pageId" value="${pageId}">
				</td>
			</tr>
		</table>
		<br/>
		<div style="width: 100%; text-align: center;">
			<c:choose>
				<c:when test="${openPage==1}">
					<input class="normal_btn" type="button" value="保存" name="saveButton" id="saveButton" onclick="save()" />
						&nbsp;
					<input name="button" type="button" class="long_btn" onclick="window.close();" ;" value="关闭" /> 
				</c:when>
				<c:otherwise>
					<input class="normal_btn" type="button" value="保存" name="saveButton" id="saveButton" onclick="save()" />
					&nbsp;
					<input name="button" type="button" class="long_btn" onclick="history.back();" ;" value="返回" /> 
				</c:otherwise>
			</c:choose>
		</div>
	</form>
<script type="text/javascript">
		function check(){
			var msg ="";
			if(""==document.getElementById('ctmname').value){
				msg+="客户名称不能为空!</br>"
			}
			if(""==document.getElementById('phone').value){
				msg+="联系电话不能为空!</br>"
			}
			if(""==document.getElementById('complaintContent').value){
				msg+="抱怨咨询内容不能为空!</br>"
			}else if(!WidthCheck(document.getElementById('complaintContent').value, 1000)){
				msg+="抱怨咨询内容太长!</br>"
			}
	
			if(msg!=""){
				MyAlert(msg);
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
	
		function save(){
			//验证
			if(check()){
				document.getElementById("saveButton").disabled = true;
				makeNomalFormCall('<%=contextPath%>/customerRelationships/complaintConsult/WaitComplaintSearch/waitComplaintUpdateSubmit.json',saveBack,'fm','');
			}
		}
		
		function saveBack(json){
			if(json.success != null && json.success=='true' && json.pageId=='complaintSearch' ){
				MyAlertForFun("保存成功!",sendPage1);
			}else if(json.success != null && json.success=='true' && json.pageId=='waitComplaintSearch'){
				MyAlertForFun("保存成功!",sendPage);
			}else{
				MyAlert("保存失败,请联系管理员!");
			}
			document.getElementById("saveButton").disabled = false;
		}
		
		
		//页面跳转：
		function sendPage(){
			window.location.href = "<%=contextPath%>/customerRelationships/complaintConsult/WaitComplaintSearch/waitComplaintSearchInit.do?flagInit=1";
		}	
		//页面跳转：
		function sendPage1(){
			//window.location.href = "<%=contextPath%>/customerRelationships/complaintConsult/ComplaintSearch/complaintSearchInit.do";
			opener.location="javascript:refreshData()";
			window.close();
		}	

	</script>
</body>
</html>