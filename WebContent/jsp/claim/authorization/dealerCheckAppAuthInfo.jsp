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
			<td><input type="checkBox" id="checkBoxAll" name="checkBoxAll" onclick="selectAll(this,'recesel')" />选择</td>
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
						<td><input type="checkbox" id="recesel" name="recesel" value="<%=detail.get(i).get("CODE_ID") %>" checked/></td>
						<td><%=detail.get(i).get("CODE_DESC")%></td>
					</tr>
				<%}else{%>
					<tr class="table_list_row${status.index%2+1}">
						<td><input type="checkbox" id="recesel" name="recesel" value="<%=detail.get(i).get("CODE_ID") %>" 
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
			<td><input type="checkBox" id="checkBoxAll1" name="checkBoxAll1" onclick="selectAll(this,'recesel1')" />选择</td>
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
						<td><input type="checkbox" id="recesel1" name="recesel1" value="<%=detail1.get(i).get("CODE_ID") %>" checked/></td>
						<td><%=detail1.get(i).get("CODE_DESC")%></td>
					</tr>
				<%}else{%>
					<tr class="table_list_row${status.index%2+1}">
						<td><input type="checkbox" id="recesel1" name="recesel1" value="<%=detail1.get(i).get("CODE_ID") %>" 
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
			<td><input type="checkBox" id="checkBoxAll2" name="checkBoxAll2" onclick="selectAll(this,'recesel2')" />选择</td>
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
						<td><input type="checkbox" id="recesel2" name="recesel2" value="<%=detail2.get(i).get("CODE_ID") %>" checked/></td>
						<td><%=detail2.get(i).get("CODE_DESC")%></td>
					</tr>
				<%}else{%>
					<tr class="table_list_row${status.index%2+1}">
						<td><input type="checkbox" id="recesel2" name="recesel2" value="<%=detail2.get(i).get("CODE_ID") %>" 
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
			<td><input type="checkBox" id="checkBoxAll3" name="checkBoxAll3" onclick="selectAll(this,'recesel3')" />选择</td>
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
						<td><input type="checkbox" id="recesel3" name="recesel3" value="<%=detail3.get(i).get("CODE_ID") %>" 
						checked/></td>
						<td><%=detail3.get(i).get("CODE_DESC")%></td>
					</tr>
				<%}else{%>
					<tr class="table_list_row${status.index%2+1}">
						<td><input type="checkbox" id="recesel3" name="recesel3" value="<%=detail3.get(i).get("CODE_ID") %>" 
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
			<td><input type="checkBox" id="checkBoxAll4" name="checkBoxAll4" onclick="selectAll(this,'recesel4')" />选择</td>
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
						<td><input type="checkbox" id="recesel4" name="recesel4" value="<%=detail4.get(i).get("CODE_ID") %>" 
						checked/></td>
						<td><%=detail4.get(i).get("CODE_DESC")%></td>
					</tr>
				<%}else{%>
					<tr class="table_list_row${status.index%2+1}">
						<td><input type="checkbox" id="recesel4" name="recesel4" value="<%=detail4.get(i).get("CODE_ID") %>" 
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
			<td><textarea name="remark" id="remark" rows="5" cols="100">${rem }</textarea></td>
		</tr>
	</table>
	<br />
	<table class="table_edit">
		<tr>
			<td colspan="4" align="center">
				<input type="button" value="提交" class="normal_btn" onclick="save();"/>
				<input type="button" value="返回" class="normal_btn" onclick="goBack(${checkId });"/>
			</td>
		</tr>
  </table>
</form>
<script type="text/javascript">
function goBack(value){
	location = '<%=contextPath%>/claim/application/DealerNewKp/dealerCheckAppAuthInit.do?id='+value ;
}
function save(){
	//var allChecks = document.getElementsByName("recesel");
	//var allFlag = false;
	//for(var i = 0;i<allChecks.length;i++){
		//if(allChecks[i].checked){
			//allFlag = true;
		//}
	//}
	var allFlag = true;//这一行暂时写死了，想到办法可以修改。
	if(allFlag){
		MyConfirm("确认提交?",changeSubmit);
	}else{
		MyAlert("请选择后再点击操作按钮!");
	}
}

function changeSubmit() {
	var url="<%=request.getContextPath()%>/claim/application/DealerNewKp/save.json";
	makeNomalFormCall(url,showResult,'fm');
}
function showResult(json){
	var flag=json.flag;
	var checkId=json.checkId
	if(flag=='01'){
		MyAlert('提交成功');
		goBack(checkId);
	}else{
		MyAlert('操作失败,请联系管理员');
	}
}
</script>
</BODY>
</html>