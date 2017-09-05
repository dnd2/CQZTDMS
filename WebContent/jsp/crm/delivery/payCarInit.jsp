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
<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/delivery/payCar.js"></script>
<title>交车查询界面</title>
</head>
<body onload="loadcalendar();doInit();" > 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt;  潜客管理  &gt;交车管理 &gt; 交车查询</div>
	<form id="fm" name="fm" method="post">
	<input  type="hidden" id="curPaths" value="<%=contextPath%>"/>
	<input type="hidden" name="curPage" id="curPage" value="1" />
	<input type="hidden" id="dlrId" name="dlrId" value="" />
	<table class="table_query" border="0">
		<tr>
	      	<td align="right" nowrap >客户名称：</td>
	      	<td align="left" nowrap >
	      	<input type="text" name="ctmName" id="ctmName"/>
	      	</td>
	     <td  align="right"  >手机：</td>
	     <td ><input type="text" name="telephone" id="telephone"/></td>
<!--	      <td >订单日期：</td>-->
<!--	     <td > <input name="orderDate" type="text" id="orderDate"  class="short_txt" readonly    />-->
<!--          <input class="time_ico" type="button" onClick="showcalendar(event, 'orderDate', false);" /></td>-->
	     
	      <td align="right"  >交车日期：</td>
	     <td > 
	                    起 <input name="preDlvDate" type="text" id="preDlvDate"  class="short_txt" readonly    />
            <input class="time_ico" type="button" onClick="showcalendar(event, 'preDlvDate', false);" />止:
          	<input name="preDlvDateEnd" type="text" id="preDlvDateEnd"  class="short_txt" readonly    />
          	<input class="time_ico" type="button" onClick="showcalendar(event, 'preDlvDateEnd', false);" /> 
         </td>
		</tr>
		<tr>
			<td align="right"  >状态：</td>
	         <td>
	         	<select name="ifDelv" style="width: 130px">
	         		<option value="">--请选择--</option>
	         		<option value="60571001">已交车</option>
	         		<option value="60571002">已上报</option>
	         		<option value="60571003">待退车</option>
	         		<option value="60571004">已退车</option>
	         	</select>
	         </td>
	        <td align="right" nowrap >VIN：</td>
	      	<td align="left" nowrap >
	      	<input type="text" name="vin" id="vin"/>
	      </td>
			<c:if test="${adviserLogon=='no' }">
						<td align="right"  >顾问：</td>
					<td>
						<select id="adviserId" name="adviserId" style="width: 130px">
				      			<option value="">--请选择--</option>
				      		<c:forEach var="item" items="${adviserList }" varStatus="status">
				      			<option id="${item.USER_ID }" value="${item.USER_ID }">${item.NAME }</option>
				      		</c:forEach>
				      	</select>
					</td>
				</c:if>
		    	<c:if test="${managerLogon=='yes' }">
						<td align="right" >分组：</td>
						<td width="25%">
							<select id="groupId" name="groupId" style="width: 130px" >
				      			<option value="">--请选择--</option>
				      		<c:forEach var="item" items="${groupList }" varStatus="status">
				      			<option id="${item.GROUP_ID }" value="${item.GROUP_ID }">${item.GROUP_NAME }</option>
				      		</c:forEach>
				      	</select>
						</td>
				</c:if>
		
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