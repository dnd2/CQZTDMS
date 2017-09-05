<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="java.util.*" %>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>经销商与销售商维护</title>
</head>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统管理 &gt; 经销商管理 &gt;<c:if test="${is_Add == true}">新增经销商与售后服务关系</c:if><c:if test="${is_Mod == true}">修改经销商与售后服务关系</c:if></div>
 <form method="post" name = "fm"  id="fm">
 <input name="dealerCode" type="hidden" id="dealerCode"/>
<input name="dealerName" type="hidden" id="dealerName"/>
 	
 	<input id="relationId" name="relationId" type="hidden" value="${dMap.RELATION_ID }"/>
 	
    <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
    	  <TR>
			<TH colSpan=4 align=left><IMG class=nav src="<%=contextPath%>/img/subNav.gif"> 经销商与售后服务绑定关系信息</TH>
    	  <tr>
			<td class="table_query_3Col_label_6Letter" nowrap="nowrap">销售经销商：</td>
			<td class="table_query_4Col_input" nowrap="nowrap">
				<input name="xs_dealerCode" type="hidden" id="xs_dealerCode" class="middle_txt" value="${xs_dealerCode}"  />
	      		<input name="xs_dealerName" type="text" id="xs_dealerName" class="middle_txt" value="${xs_dealerName}" />
	      		<input name="xs_dealerId" type="hidden" id="xs_dealerId" class="middle_txt" value="${dMap.XS_DEALER_ID }" />
	            <input name="dlbtn" type="button" class="mini_btn" onclick="showOrgDealer('xs_dealerCode','xs_dealerId','false', '', 'true','','10771001','xs_dealerName');" value="..." />
	            <input type="button" class="normal_btn" onclick="txtClr('xs_dealerId');txtClr('xs_dealerCode');txtClr('xs_dealerName');" value="清 空" id="clrBtn" /> 
			</td>
			<td class="table_query_3Col_label_6Letter" nowrap="nowrap">售后经销商：</td>
			<td class="table_query_4Col_input" nowrap="nowrap">
				<input id="sh_dealerCode"  name="sh_dealerCode" type="hidden" value="${sh_dealerCode}" />
	            <input name="sh_dealerName" type="text" id="sh_dealerName" class="middle_txt" value="${sh_dealerName}" />
	            <input name="sh_dealerId" type="hidden" id="sh_dealerId" value="${dMap.SH_DEALER_ID }" />
	            <input name="button1" type="button" class="mini_btn" style="cursor: pointer;" onclick="showOrgDealer('sh_dealerCode','sh_dealerId','false','','true','','10771002','sh_dealerName');" value="..." />        
	            <input name="button2" type="button" class="normal_btn" onClick="txtClr('sh_dealerId');txtClr('sh_dealerCode');txtClr('sh_dealerName');" value="清除"/>
			</td>
		 </tr>
	     <tr align="center">
			<td colspan="4" class="table_query_4Col_input" style="text-align: center">
				<input type="button" value="保存" name="saveBtn" class="normal_btn" onclick="saveBindingRelationInfo()"/>	&nbsp;&nbsp;
				<input type="button" value="返回" name="cancelBtn"  class="normal_btn" onclick="history.back();" />
			</td>
		 </tr>
	</table>
</form>
<script type="text/javascript" >
function saveBindingRelationInfo()
{  
	if(document.getElementById("xs_dealerId").value == '') {
		MyAlert('请选择销售经销商!'); 
		return;
	}
	if(document.getElementById("sh_dealerId").value == '') {
		MyAlert('请选择售后经销商!'); 
		return;
	}
	makeNomalFormCall('<%=contextPath%>/sysmng/dealer/DealerInfo/checkBindingRelation.json',showResultCodeCheck,'fm');
}
function showResultCodeCheck(obj){
	var msg=obj.msg;
	if(msg=='true'){
		if(confirm("信息可处理,是否继续?"))
		{
			var url = "<%=contextPath%>/sysmng/dealer/DealerInfo/saveBindingRelationInfo.json";
			makeNomalFormCall(url,function(json){
				if(json.Exception) {
					MyAlert(json.Exception);
				} else {
					MyAlert(json.message);
					window.location.href = "<%=contextPath%>/sysmng/dealer/DealerInfo/queryBindingRelation.do";
				}
			},'fm','');
		}		
	}else if(msg == "false"){
		MyAlert('销售经销商或者售后经销商已经绑定!');
	}
}
</script>
</body>
</html>
