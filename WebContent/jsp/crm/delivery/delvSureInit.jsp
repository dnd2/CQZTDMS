<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>

<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/delivery/delvSure.js"></script>

<title>待交车界面</title>
</head>
<body onload="loadcalendar();doInit();" > 
<div class="wbox" id="show">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt;  潜客管理  &gt;交车管理 &gt; 交车确认列表</div>
	<form id="fm" name="fm" method="post">
	<input  type="hidden" id="curPaths" value="<%=contextPath%>"/>
	<input type="hidden" name="curPage" id="curPage" value="1" />
	<input type="hidden" id="dlrId" name="dlrId" value="" />
	<input type="hidden" id="userType" name="userType" value="${userType }" />
	<table class="table_query" border="0">
		<tr>
      	<td align="left" nowrap >客户名称：</td>
      	<td align="left" nowrap >
      		<input type="text" name="ctmName" id="ctmName"/>
      	</td>
	     <td >手机：</td>
	     <td ><input type="text" name="telephone" id="telephone"/></td>
	      <td >订单日期：</td>
	     <td > 
	     	起:<input name="orderDate" type="text" id="orderDate"  class="short_txt" readonly    />
            <input class="time_ico" type="button" onClick="showcalendar(event, 'orderDate', false);" />止:
          	<input name="orderDateEnd" type="text" id="orderDateEnd"  class="short_txt" readonly    />
          	<input class="time_ico" type="button" onClick="showcalendar(event, 'orderDateEnd', false);" /> 
          </td>
	      <td >预交车日期：</td>
	     <td > 
	                    起:<input name="preDlvDate" type="text" id="preDlvDate"  class="short_txt" readonly    />
            <input class="time_ico" type="button" onClick="showcalendar(event, 'preDlvDate', false);" />止:
          	<input name="preDlvDateEnd" type="text" id="preDlvDateEnd"  class="short_txt" readonly    />
          	<input class="time_ico" type="button" onClick="showcalendar(event, 'preDlvDateEnd', false);" /> 
          </td>
		</tr>
		<tr>
		 <td align="right" nowrap >VIN：</td>
	      	<td align="left" nowrap >
	      	<input type="text" name="vin" id="vin"/>
	      </td>
		</tr>
	    <tr>
	    	<td> &nbsp;</td>
			<td  colspan="6" align="center" >
					<input type="button" class="normal_btn" onclick="__extQuery__(1);" value=" 查  询  " id="queryBtn" />
			</td>
	        <td align="right" >
	             页面大小：<input name="pageSize" id="pageSize" type="text" class="mini_txt" value="10" datatype="0,isDigit,3"/>
	        </td>
		</tr>
	</table>
<!--分页部分  -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />	
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
</div>   
</body>
</html>