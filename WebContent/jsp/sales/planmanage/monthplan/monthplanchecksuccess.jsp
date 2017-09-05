<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="java.util.List"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
	String year = (String)request.getAttribute("year");
    List planList = (List)request.getAttribute("planList");
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>月度任务导入</title>
<script type="text/javascript">
function isSave(){
     if(submitForm('frm')){
 	    MyConfirm("是否确认保存信息?",importSave);
     }
 }
 
function importSave() {
		document.getElementById("savebtn").disabled=true;
		var fsm = document.getElementById("frm");
		fsm.action = "<%=contextPath %>/sales/planmanage/MonthTarget/MonthTargetImport/importExcel.do";
		fsm.submit();
}
</script>
</head>
<body onunload='javascript:destoryPrototype()' onload="loadcalendar();showSub();">
<div class="wbox">
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置>计划管理>月度任务>月度任务导入</div>
<div class="form-panel">
<h2>月度任务导入</h2>
<div class="form-body">
	<table class="table_list" width="85%" align="center" border="0">		
		   <tr>
			    <th><div align="center">经销商代码</div></th>
	            <th><div align="center">经销商名称</div></th>
	            <th><div align="center">车系代码</div></th>
	            <th><div align="center">车系</div></th>
	            <th><div align="center">月度任务</div></th>
	  	   </tr>
	  	<c:forEach items="${planList}" var="planList"
			varStatus="steps">
			<tr class="table_list_row${steps.index%2+1 }">
				<td><div align="center">${planList.DEALER_CODE }</div></td>
				<td><div align="center">${planList.DEALER_SHORTNAME }</div></td>
				<td><div align="center">${planList.GROUP_CODE }</div></td>
				<td><div align="center">${planList.GROUP_NAME }</div></td>
				<td><div align="center">${planList.SUM_AMT }</div></td>
			</tr>
		</c:forEach>
	  	
	</table>
<jsp:include page="${contextPath}/queryPage/orderHidden.html" /></div>
</div>
</div>
<form>
</form>
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<form  name="frm" id="frm">
<table class="table_query" width="85%" align="center" border="0"  id="roll">	
	<tr class="center" >
		<th colspan="6">
			<div class="center" style="text-align: center;">
			    <input type='hidden' name='year' value='<%=year %>' />
			    <input type='hidden' name='month' value='${month }' />
			    <input type='hidden' name='buss_area' value='${buss_area }' />
			     <input type='hidden' name='planType' value='${planType}' />
				<input class="u-button u-submit"   type="button" id="savebtn" name='saveResButton' onclick='isSave();' value='保存' />
				<input class="u-button u-reset"  type='button' name='saveResButton' onclick='history.back();' value='返回' />
			</div>
		</th>	
  	</tr>
</table>
</form>
</body>
</html>
