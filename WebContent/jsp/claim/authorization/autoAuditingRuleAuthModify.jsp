<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%
	String contextPath = request.getContextPath();
	List levellist = (List)request.getAttribute("levelList");
	TtAsWrClaimAutoPO autoPO = (TtAsWrClaimAutoPO)request.getAttribute("AUTOPO");
	String authCode = "";
	if(autoPO!=null && autoPO.getAuthCode()!=null)
		authCode = autoPO.getAuthCode();
%>

<%@page import="com.infodms.dms.po.TtAsWrClaimAutoPO"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<title>索赔自动审核规则管理(授权角色维护)</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<script type="text/javascript">
		function frmSubmit(frmName){
			var turl = "<%=contextPath%>/claim/authorization/AutoAuditingRuleMain/updateAutoRuleAuth.json";
			makeNomalFormCall(turl,backDeal,frmName);
		}

		function backDeal(){
			_hide();
			parentContainer.__extQuery__(1);
		}
		function checkAll(obj){
	 var itemlength=fm.elements.length;
		  for(var k=0;k<parseInt(itemlength);k++){		
		        if(fm.elements[k].type=="checkbox"&&fm.elements[k].value<obj.value)
		        {
		         fm.elements[k].checked = obj.checked;       
		        }
		  }
	}
	</script>
</head>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：
售后服务管理&gt;索赔授权管理&gt;索赔自动审核规则管理</div>
<form method="post" id='fm' id='fm'>
<table class="table_edit">
	<tr>
		<td style="color: #252525; width: 80px; text-align: right"
			rowspan="<%=levellist.size()/2+2%>">授权级别：</td>
	</tr>
	<% for(int i=0;i<levellist.size();i++){ 
			  HashMap temp = (HashMap)levellist.get(i);
			  String levelCode = temp.get("APPROVAL_LEVEL_CODE")==null?"":temp.get("APPROVAL_LEVEL_CODE").toString();
			  String levelName = temp.get("APPROVAL_LEVEL_NAME")==null?"":temp.get("APPROVAL_LEVEL_NAME").toString();
		  %>
	<%if(i%2==0){%>
	<tr>
		<%}%>
		<td>
			<input type="checkbox" name="APPROVAL_LEVEL_CODE" 
			value="<%=levelCode%>@<%=levelName%>"
			<%if(authCode.indexOf(levelCode)>=0) {%>checked="true"<%} %>/> 
			<%=temp.get("APPROVAL_LEVEL_NAME")%>
		</td>
		<%if(i%2==2){%>
	</tr>
	<%}%>
	<%} %>
	<%
			for(int j=levellist.size();j<levellist.size()+3;j++){
				if(j%2==0)break;
	   		 %>
	<tr>
		<td style="color: #252525; width: 130px; text-align: right"></td>
	</tr>
	<%}%>
</table>
<table class="table_edit">
 	<tr><td>&nbsp;</td></tr>
	<tr>
		<td align="center"><input name="ok" type="button"
			class="normal_btn" id="commitBtn" value="确定"
			onclick="frmSubmit('fm');" /> <input name="back" type="button"
			class="normal_btn" value="返回" onclick="_hide();" />
		</td>
	</tr>
</table>
<input type="hidden" name="AUTO_ID" value="<%=request.getAttribute("AUTO_ID")%>"/><!-- 自动规则ID -->
</form>
</body>
</html>