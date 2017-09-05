<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
    List list=(List)request.getAttribute("list");
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>生产计划确认</title>
</head>
<body>
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置>计划管理>生产计划> 生产计划确认 </div>
<%
if(null!=list&&list.size()>0){
%>
<table class="table_list" width="85%" align="center" border="0">		
	   <tr>
		    <th><div align="center">业务范围</div></th>
            <th><div align="center">年月</div></th>
            <th><div align="center">生产计划总量</div></th>
            <!--
            <th><div align="center">版本号</div></th>
            -->
            <th><div align="center">操作</div></th>
  	   </tr>
  	<c:forEach items="${list}" var="list" varStatus="steps">
		<tr class="table_list_row${steps.index%2+1 }">
			<td><div align="center">${list.AREA_NAME }</div></td>
			<td><div align="center">${list.PLAN_YEAR }年${list.PLAN_MONTH }月</div></td>
			<td><div align="center">${list.PLAN_AMOUNT }</div></td> 
			<!--<td><div align="center">${list.MAXVER }</div></td>-->
			<td>
			    <div align="center">
			       <input class="cssbutton" type='button' name='saveResButton' onclick='importSave(${list.PLAN_YEAR },${list.PLAN_MONTH },${list.AREA_ID });' value='确认' />
			    </div>
			</td>
		</tr>
	</c:forEach>
</table>
<%}else{ %>
<table class=table_list>
	<tr>
	   <td align="center">
	   		<font color="red">暂无数据</font>
       </td>
	</tr>
</table>
<%} %>
<jsp:include page="${contextPath}/queryPage/orderHidden.html" /></div>
<form name="frm" id="frm" method="post">
<input type="hidden" name="year" id="year" value="" />
<input type="hidden" name="month" id="month" value="" />
<input type="hidden" name="areaId" id="areaId" value="" />
</form>
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<script type="text/javascript">
function importSave(year,month,areaId) {
	if(submitForm('frm')){
		    var url='<%=contextPath %>/sales/planmanage/ProductPlan/ProductPlanConfirm/productPlanConfirmSearch.do';
		    document.getElementById("year").value=year;
		    document.getElementById("month").value=month;
		    document.getElementById("areaId").value=areaId;
			frm.action = url;
			frm.submit();
		}
}
</script>
</body>
</html>
