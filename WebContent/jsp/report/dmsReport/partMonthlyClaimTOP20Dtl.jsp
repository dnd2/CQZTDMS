<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript">
	
	//获取序号
	function getIdx(){
		document.write(document.getElementById("file1").rows.length-2);
	}
	//获取序号
	function getIdx1(){
		document.write(document.getElementById("file2").rows.length-2);
	}
	//返回
	function goBack(){
		window.location.href = '<%=contextPath%>/report/dmsReport/ClaimQueryReport/partMonthlyClaimTOP20Query.do?';
	}
	//关闭
	function goClose(){
		_hide();
	}

	//明细下载
	function exportDetl()
	{
		document.fm.action="<%=contextPath%>/report/dmsReport/ClaimQueryReport/expotDataPartMonthlyClaimTOP20Dtl.do?";
		document.fm.target="_self";
		document.fm.submit();
	}
</script>
</head>

<body>
	<form name="fm" id="fm" method="post" enctype="multipart/form-data">
		<input type="hidden" name="partCode" id="partCode" value="${partCode}" />
		<input type="hidden" name="beginTime" id="beginTime" value="${beginTime }" />
		<input type="hidden" name="endTime" id="endTime" value="${endTime}" />
		<div class="wbox">
			<div class="navigation">
				<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;售后报表管理&gt;月索赔零件数量TOP20查询&gt;月索赔零件数量TOP20明细查看
			</div>
			<table border="0" width="100%">
				<tr align="center" width="100%">
					<td><input class="normal_btn" type="button" value="明细下载"
						onclick="exportDetl()" /> <input class="normal_btn" type="button"
						value="关 闭" onclick="goClose()" /></td>
				</tr>
			</table>
			
			<table id="file1" class="table_list"
				style="border-bottom: 1px solid #DAE0EE">
				<tr>
					<th colspan="13" align="left"><img class="nav"
						src="<%=contextPath%>/img/subNav.gif" />明细查看</th>
				</tr>
				<tr bgcolor="#FFFFCC">
					<td>序号</td>
					<td>经销商代码</td>
					<td>经销商名称</td>
					<td>索赔单号</td>
					<td>配件编码</td>
					<td>配件名称</td>
					<td>索赔数量</td>
					<td>索赔金额</td>
					<td>供应商代码</td>					
					<td>供应商名称</td>
				</tr>
				
				<c:forEach items="${dtlList}" var="data">
					<tr class="table_list_row1">
						<td align="center">
							<script type="text/javascript">
	       					getIdx();
							</script>
						</td>
						<td align="center"><c:out value="${data.DEALER_CODE}" /></td>
						<td align="left"><c:out value="${data.DEALER_SHORTNAME}" /></td>
						<td align="center"><c:out value="${data.CLAIM_NO}" /></td>
						<td align="center"><c:out value="${data.DOWN_PART_CODE}" /></td>
						<td align="left"><c:out value="${data.DOWN_PART_NAME}" /></td>
						<td align="center"><c:out value="${data.BALANCE_QUANTITY}" /></td>
						<td align="center"><c:out value="${data.BALANCE_AMOUNT}" /></td>
						<td align="center"><c:out value="${data.DOWN_PRODUCT_CODE}" /></td>
						<td align="left"><c:out value="${data.DOWN_PRODUCT_NAME}" /></td>
					</tr>
				</c:forEach>
			</table>
			
		</div>
	</form>
</body>
</html>

