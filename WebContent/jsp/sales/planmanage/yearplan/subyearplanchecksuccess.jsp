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
<title>年度目标导入确认</title>
</head>
<body onunload='javascript:destoryPrototype()' onload="loadcalendar();showSub();">
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置>计划管理>年度目标>年度目标导入</div>
<table class="table_query" width="85%" align="center" border="0">		
	   <tr>
		    <th><div align="center">经销商名称</div></th>
            <th><div align="center">车系</div></th>
            <th><div align="center">合计</div></th>
            <th><div align="center">1月</div></th>
            <th><div align="center">2月</div></th>
            <th><div align="center">3月</div></th>
            <th><div align="center">4月</div></th>
            <th><div align="center">5月</div></th>
            <th><div align="center">6月</div></th>
            <th><div align="center">7月</div></th>
            <th><div align="center">8月</div></th>
            <th><div align="center">9月</div></th>
            <th><div align="center">10月</div></th>
            <th><div align="center">11月</div></th>
            <th><div align="center">12月</div></th>
  	   </tr>
  	<c:forEach items="${planList}" var="planList"
		varStatus="steps">
		<tr class="table_list_row1">
			<td><div align="center">${planList.DEALER_NAME }</div></td>
			<td><div align="center">${planList.GROUP_NAME }</div></td>
			<td><div align="center">${planList.SUM_AMT }</div></td>
			<td><div align="center">${planList.JAN_AMT }</div></td>
			<td><div align="center">${planList.FEB_AMT }</div></td>
			<td><div align="center">${planList.MAR_AMT }</div></td>
			<td><div align="center">${planList.APR_AMT }</div></td>
			<td><div align="center">${planList.MAY_AMOUNT }</div></td>
			<td><div align="center">${planList.JUN_AMT }</div></td>
			<td><div align="center">${planList.JUL_AMT }</div></td>
			<td><div align="center">${planList.AUG_AMT }</div></td>
			<td><div align="center">${planList.SEP_AMT }</div></td>
			<td><div align="center">${planList.OCT_AMT }</div></td>
			<td><div align="center">${planList.NOV_AMT }</div></td>
			<td><div align="center">${planList.DEC_AMT }</div></td>
		</tr>
	</c:forEach>
  	
</table>
<jsp:include page="${contextPath}/queryPage/orderHidden.html" /></div>
<form>
</form>
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<form  name="frm" id="frm">
<table class="table_query" width="85%" align="center" border="0"  id="roll">	
	<tr align="center" >
		<th colspan="6">
			<div align="left">
			    <input type='hidden' name='year' value='<%=year %>' />
			    <input type='hidden' name='buss_area' value='${buss_area}' />
			    <input type='hidden' name='planType' value='${planType}' />
				<input class=cssbutton type='button' name='saveResButton' onclick='importSave();' value='保存' />
				<input class=cssbutton type='button' name='saveResButton' onclick='history.back();' value='返回' />
			</div>
		</th>	
  	</tr>
</table>
</form>
<script type="text/javascript">
function importSave() {
	if(submitForm('frm')){
			frm.action = "<%=contextPath %>/sales/planmanage/YearTarget/SubYearTargetImport/importExcel.do";
			frm.submit();
		}
}
</script>
</body>
</html>
