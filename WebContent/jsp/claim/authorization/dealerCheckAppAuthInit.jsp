<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="java.util.*"%>
<%String contextPath = request.getContextPath();%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>       
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
<input type="hidden" name="sum_id" id="sum_id" value="${list.DEALER_CODE }"/>
    <table  class="table_query">
		<tr>
			<td width="10%" align="right" height="26">抽单编号：</td>
			<td width="20%" align="left">${list.CHECK_NO }</td>
			<td width="10%" align="right">抽单日期：</td>
			<td width="20%" align="left"><fmt:formatDate value="${list.CHECK_DATE }" pattern="yyyy-MM-dd"/></td>
			<td width="10%" align="right">抽查数量：</td>
			<td width="20%" align="left">${list.CHECK_COUNT }<input type="hidden" id="check_id" name="chenck_id" value="${list.ID }"/> </td>
		</tr>
		<tr>
			<td width="10%" align="right">开票单号：</td>
			<td width="20%" align="left">${list.BALANCE_NO }</td>
			<td width="10%" align="right">结算时间段：</td>
			<td width="20%" align="left"><fmt:formatDate value="${list.START_DATE }" pattern="yyyy-MM-dd"/>至<fmt:formatDate value="${list.END_DATE }" pattern="yyyy-MM-dd"/></td>
			<td width="10%" align="right">基地：</td>
			<td width="20%" align="left">
				<script type="text/javascript">
					document.write(getItemValue('${list.YIELDLY }'));
				</script>
			</td>
		</tr>
		<tr>
			
		</tr>
		<tr>
			<td width="10%" align="right" height="26">经销商代码：</td>
			<td width="20%" align="left">${list.DEALER_CODE }</td>
			<td width="10%" align="right">经销商名称：</td>
			<td width="20%" align="left">${list.DEALER_NAME }</td>
			<td width="20%" align="left"></td>
			<td width="20%" align="left"></td>
		</tr>
		<tr>
			<td width="10%" align="right" height="26">分销中心：</td>
			<td width="20%" align="left">${list.ROOT_ORG_NAME }</td>
			<td width="10%" align="right">省份：</td>
			<td width="20%" align="left">${list.REGION_NAME }</td>
			<td width="20%" align="left"></td>
			<td width="20%" align="left"></td>
		</tr>
	</table>
	<br />
	<table class="table_list">
		<tr class="table_list_row2">
			<td>序号</td>
			<td>VIN</td>
			<td>车型</td>
			<td>索赔单类型</td>
			<td>索赔单号</td>
			<td>金额</td>
			<td>操作</td>
		</tr>
		<c:forEach var="detail" items="${detail}" varStatus="status">
			<tr class="table_list_row${status.index%2+1}">
				<td>${status.index+1}</td>
				<td><c:out value="${detail.VIN}"></c:out></td>
				<td><c:out value="${detail.GROUP_CODE}"></c:out></td>
				<td><script type="text/javascript">document.write(getItemValue('${detail.CLAIM_TYPE }'));</script></td>
				<td><c:out value="${detail.CLAIM_NO}"></c:out></td>
				<td><fmt:formatNumber value="${detail.BALANCE_AMOUNT}" pattern="##"/></td>
				<c:if test="${detail.STATUS==99901002}">
					<td><a href="#" onclick="getAuth(${detail.CLAIM_ID});">重新审核</a></td>
				</c:if>
				<c:if test="${detail.STATUS==99901001}">
					<td><a href="#" onclick="getAuth(${detail.CLAIM_ID});">审核</a></td>
				</c:if>
			</tr>
		</c:forEach>
	</table>
	<br />
	<table class="table_edit">
		<tr>
			<td colspan="4" align="center">
				<input type="button" value="返回" class="normal_btn" onclick="goBack();"/>
				<input type="button" value="完成" class="normal_btn" onclick="getSign();"/>
			</td>
		</tr>
  </table>
</form>
<script type="text/javascript">
function getAuth(value){
	var checkId=$('check_id').value;
	location.href='<%=contextPath%>/claim/application/DealerNewKp/dealerCheckAppAuthInfo.do?id='+value+'&checkId='+checkId;
}

function goBack(){
	location = '<%=contextPath%>/claim/application/DealerNewKp/checkApplicationAuthInit.do' ;
}

function getSign(){
	var id=$('check_id').value;
	if(confirm('确认操作?')){
		var url = '<%=contextPath%>/claim/application/DealerNewKp/dealerCheckAppAuth.json?id='+id;
		makeNomalFormCall(url,getCallback,'fm');
	}
}
function getCallback(json){
	if(json.flag){
		MyAlert('操作已成功!');
		goBack();
    }
}
</script>
</BODY>
</html>