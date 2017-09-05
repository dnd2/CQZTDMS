<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<%
String contextPath=request.getContextPath();
List weekList=(List)request.getAttribute("weekList");
List list=(List)request.getAttribute("list");
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>生产计划确认</title>

<script language="JavaScript">
	function subChecked(){
	    var str="";
		MyConfirm("是否执行确认操作?",confirmSubmit);
	}
	function confirmSubmit(){
		$('fm').action=  "<%=contextPath %>/sales/planmanage/ProductPlan/ProductPlanConfirm/productTargetConfirmSubmint.do";
		$('fm').submit();
	}
</script>
</head>

<body>
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />当前位置>计划管理>生产计划> 生产计划确认</div>
<table class=table_query align="center" width="95%" >
  <tr>
    <td colspan=6 align = left > ${year }年${month }月 生产计划确认</td>
  </tr>
</table>
<p>&nbsp;</p>
<form name="fm" method="post" >
<%
if(null!=weekList&&null!=list&&weekList.size()>0&&list.size()>0){
%>
<table class="table_list">
  <tr>
		    <th><div align="center">配置代码</div></th>
            <th><div align="center">配置名称</div></th>
            <%
                long sum=0;
            	for(int i=0;i<weekList.size();i++){
            		Map map=(Map)weekList.get(i);
            %>
            	<th><div align="center"><%=map.get("WEEK") %>周</div></th>
            <%} %>
            <th><div align="center">合计</div></th>
  	   </tr>
  	    <%
           for(int j=0;j<list.size();j++){
        	    sum=0;
                Map map=(Map)list.get(j);
        %>
		<tr class="table_list_row<%=j%2+1 %>">
			<td><div align="center"><%=map.get("GROUP_CODE") %></div></td>
			<td><div align="center"><%=map.get("GROUP_CODE") %></div></td>
			<%
            	for(int i=0;i<weekList.size();i++){
            		String amt=map.get("W"+i).toString();
            		sum+=new Long(amt).longValue();
            %>
			<td><div align="center"><%=amt %></div></td>
			<%} %>
			<td><div align="center"><%=sum %></div></td>
		</tr>
	<%} %>
</table>
<br>
<table class=table_query>
	<tr>
	   <td colspan="6" nowrap="nowrap">
       	  新版本号:<input type="text" name="plan_ver" size="17" value="${planVer}" class="middle_txt" readonly="true" />      
       	  版本描述:<input type="text" name="plan_desc" size="17" value="" class="middle_txt" /> 
       	  <input type="button" class="cssbutton"  name="vdcConfirm" value="确认" onclick="subChecked();" />
       	  <input type="hidden" name="year" value="${year }" />   
       	  <input type="hidden" name="month" value="${month }" />
       	  <input type="hidden" name="areaId" value="${areaId }" />   
       </td>
	</tr>
</table>
<%}else{ %>
<table class=table_query>
	<tr>
	   <td align="center">
	   		<font color="red">暂无数据</font>
       </td>
	</tr>
</table>
<%} %>
</form>
</body>
</html>
