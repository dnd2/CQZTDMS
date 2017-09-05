<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="java.util.*"%>
<%String contextPath = request.getContextPath();%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>   
<%List<Map<String, Object>> claimSit =(List<Map<String, Object>>)request.getAttribute("claimSit"); %>
<%List<Map<String, Object>> detail =(List<Map<String, Object>>)request.getAttribute("detail"); %>
<%List<Map<String, Object>> detail1 =(List<Map<String, Object>>)request.getAttribute("detail1"); %>
<%List<Map<String, Object>> detail2 =(List<Map<String, Object>>)request.getAttribute("detail2"); %>
<%List<Map<String, Object>> detail3 =(List<Map<String, Object>>)request.getAttribute("detail3"); %>
<%List<Map<String, Object>> detail4 =(List<Map<String, Object>>)request.getAttribute("detail4"); %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script type="text/javascript">
function doInit(){
   	loadcalendar();  //初始化时间控件
}

</script>
<TITLE>经销商抽查审核</TITLE>
</HEAD>
<BODY>
<div class="navigation"><img src="../../../img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔结算管理&gt;经销商抽查审核&gt;审核</div>
<form method="post" name ="fm" id="fm">
	<input type="hidden" id="claimId" name="claimId" value="${ID }"/>
	<input type="hidden" id="checkId" name="checkId" value="${checkId }"/>
    <table><tr><td align="left">委托书</td></tr></table>
	<table class="table_list">
		<tr class="table_list_row2">
			<td>选择</td>
			<td>委托书名称</td>
		</tr>
		<%
			for(int i=0;i<detail.size();i++){
				Boolean flag=false;
				for (int j=0;j<claimSit.size();j++){
					if(detail.get(i).get("CODE_ID").toString().equals(claimSit.get(j).get("CHECK_SITUATION").toString())){
						flag = true ;
					}
					
				}
				if(flag){
				%>
					<tr class="table_list_row${status.index%2+1}">
						<td><input type="checkbox" disabled="disabled" id="recesel" name="recesel" value="<%=detail.get(i).get("CODE_ID") %>" checked/></td>
						<td><%=detail.get(i).get("CODE_DESC")%></td>
					</tr>
				<%}else{%>
					<tr class="table_list_row${status.index%2+1}">
						<td><input type="checkbox" disabled="disabled" id="recesel" name="recesel" value="<%=detail.get(i).get("CODE_ID") %>" 
						/></td>
						<td><%=detail.get(i).get("CODE_DESC")%></td>
					</tr>
				<%}
			}
		%>
	</table>
	<table><tr><td align="left">配件领料单</td></tr></table>
	<table class="table_list">
		<tr class="table_list_row2">
			<td>选择</td>
			<td>配件领料单名称</td>
		</tr>
		<%
			for(int i=0;i<detail1.size();i++){
				Boolean flag=false;
				for (int j=0;j<claimSit.size();j++){
					if(detail1.get(i).get("CODE_ID").toString().equals(claimSit.get(j).get("CHECK_SITUATION").toString())){
						flag = true ;
					}
				}
				if(flag){
				%>
					<tr class="table_list_row${status.index%2+1}">
						<td><input type="checkbox" disabled="disabled" id="recesel" name="recesel" value="<%=detail1.get(i).get("CODE_ID") %>" checked/></td>
						<td><%=detail1.get(i).get("CODE_DESC")%></td>
					</tr>
				<%}else{%>
					<tr class="table_list_row${status.index%2+1}">
						<td><input type="checkbox" disabled="disabled" id="recesel" name="recesel" value="<%=detail1.get(i).get("CODE_ID") %>" 
						/></td>
						<td><%=detail1.get(i).get("CODE_DESC")%></td>
					</tr>
				<%}
			}
		%>
	</table>
	<table><tr><td align="left">三包单据</td></tr></table>
	<table class="table_list">
		<tr class="table_list_row2">
			<td>选择</td>
			<td>三包单据名称</td>
		</tr>
			<%
			for(int i=0;i<detail2.size();i++){
				Boolean flag=false;
				for (int j=0;j<claimSit.size();j++){
					if(detail2.get(i).get("CODE_ID").toString().equals(claimSit.get(j).get("CHECK_SITUATION").toString())){
						flag = true ;
					}
				}
				if(flag){
				%>
					<tr class="table_list_row${status.index%2+1}">
						<td><input type="checkbox" disabled="disabled" id="recesel" name="recesel" value="<%=detail2.get(i).get("CODE_ID") %>" checked/></td>
						<td><%=detail2.get(i).get("CODE_DESC")%></td>
					</tr>
				<%}else{%>
					<tr class="table_list_row${status.index%2+1}">
						<td><input type="checkbox" disabled="disabled" id="recesel" name="recesel" value="<%=detail2.get(i).get("CODE_ID") %>" 
						/></td>
						<td><%=detail2.get(i).get("CODE_DESC")%></td>
					</tr>
				<%}
			}
		%>	

	</table>
	<table><tr><td align="left">单据合并检查</td></tr></table>
	<table class="table_list">
		<tr class="table_list_row2">
			<td>选择</td>
			<td>单据合并检查名称</td>
		<%
			
			for(int i=0;i<detail3.size();i++){
				Boolean flag=false;
				for (int j=0;j<claimSit.size();j++){
					if(detail3.get(i).get("CODE_ID").toString().equals(claimSit.get(j).get("CHECK_SITUATION").toString())){
						flag = true ;
					}	
				}
				if(flag){
				%>
					<tr class="table_list_row${status.index%2+1}">
						<td><input type="checkbox" disabled="disabled" id="recesel" name="recesel" value="<%=detail3.get(i).get("CODE_ID") %>" 
						checked/></td>
						<td><%=detail3.get(i).get("CODE_DESC")%></td>
					</tr>
				<%}else{%>
					<tr class="table_list_row${status.index%2+1}">
						<td><input type="checkbox" disabled="disabled" id="recesel" name="recesel" value="<%=detail3.get(i).get("CODE_ID") %>" 
						/></td>
						<td><%=detail3.get(i).get("CODE_DESC")%></td>
					</tr>
				<%}
			}
		%>
	</table>
	<table><tr><td align="left">其他</td></tr></table>
	<table class="table_list">
		<tr class="table_list_row2">
			<td>选择</td>
			<td>其他栏目名称</td>
		<%
			
			for(int i=0;i<detail4.size();i++){
				Boolean flag=false;
				for (int j=0;j<claimSit.size();j++){
					if(detail4.get(i).get("CODE_ID").toString().equals(claimSit.get(j).get("CHECK_SITUATION").toString())){
						flag = true ;
					}	
				}
				if(flag){
				%>
					<tr class="table_list_row${status.index%2+1}">
						<td><input type="checkbox" disabled="disabled" id="recesel4" name="recesel4" value="<%=detail4.get(i).get("CODE_ID") %>" 
						checked/></td>
						<td><%=detail4.get(i).get("CODE_DESC")%></td>
					</tr>
				<%}else{%>
					<tr class="table_list_row${status.index%2+1}">
						<td><input type="checkbox" disabled="disabled" id="recesel4" name="recesel4" value="<%=detail4.get(i).get("CODE_ID") %>" 
						/></td>
						<td><%=detail4.get(i).get("CODE_DESC")%></td>
					</tr>
				<%}
			}
		%>
	</table>
	<table><tr><td align="left">备注</td></tr></table>
	<table>
		<tr>
			<td><textarea name="remark" disabled="disabled" id="remark" rows="5" cols="100">${rem }</textarea></td>
		</tr>
	</table>
	<br />
	<table class="table_edit">
		<tr>
			<td colspan="4" align="center">
				<input type="button" onclick="_hide();" class="normal_btn" style="width=8%" value="关闭"/>
			</td>
		</tr>
  </table>
</form>
<script type="text/javascript">
function goBack(value){
	location = '<%=contextPath%>/claim/application/DealerNewKp/queryDetail.do?id='+value;
}
</script>
</BODY>
</html>