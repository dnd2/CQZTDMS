<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="java.util.List"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
	String year = (String)request.getAttribute("year");
	String month = (String)request.getAttribute("month");
    List tmpList = (List)request.getAttribute("tmpList");
    List weekList = (List)request.getAttribute("weekList");
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>待分配资源导入</title>
</head>
<body onunload='javascript:destoryPrototype()' onload="loadcalendar();showSub();">
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置>计划管理>配额分配>区域配额计算>待分配资源导入</div>
<table class="table_list" width="85%" align="center" border="0">		
	   <tr>
		    <th><div align="center">业务范围</div></th>
            <th><div align="center">配置代码</div></th>
            <th><div align="center">配置名称</div></th>
            <%
            for(int i=0;i<weekList.size();i++){
            	Map map=(Map)weekList.get(i);
            %>
            <th><div align="center"><%=(String)map.get("WEEK")+"周" %></div></th>
            <%
            }
            %>
            <th><div align="center">合计</div></th>
  	   </tr>
  	    <%
            for(int i=0;i<tmpList.size();i++){
            	long sum=0;
            	Map map=(Map)tmpList.get(i);
            %>
		<tr class="table_list_row<%=i%2+1 %>">
			<td><div align="center"><%=(String)map.get("AREA_NAME") %></div></td>
			<td><div align="center"><%=(String)map.get("GROUP_CODE") %></div></td>
			<td><div align="center"><%=(String)map.get("GROUP_NAME") %></div></td>
			<%
            for(int j=0;j<weekList.size();j++){
            	String amt=map.get("W"+j).toString();
            	sum+=new Long(amt).longValue();
            %>
			<td><div align="center"><%=amt %></div></td>
			<%
            }
            %>
			<td><div align="center"><%=sum %></div></td>
		</tr>
  	    <%
          }
         %>
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
			    <input type='hidden' name='year' value='${year }' />
			    <input type='hidden' name='month' value='${month }' />
			    <input type='hidden' name='buss_area' value='${buss_area }' />
				<input class="cssbutton" type="button" name='saveResButton' onclick='importSave();' value='确定' />
				<input class="cssbutton" type='button' name='saveResButton' onclick='history.back();' value='返回' />
			</div>
		</th>	
  	</tr>
</table>
</form>
<script type="text/javascript">
function importSave() {
	if(submitForm('frm')){
			frm.action = "<%=contextPath %>/sales/planmanage/ProductPlan/ResourcesPlanImport/importExcel.do";
			frm.submit();
		}
}
</script>
</body>
</html>
