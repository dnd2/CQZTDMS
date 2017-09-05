<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@page import="java.util.LinkedList"%>
<%@ page import="java.util.List" %>
<%@ page import=" com.infodms.dms.util.CommonUtils" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ taglib prefix="fmt" uri="/jstl/fmt" %>
<%@taglib uri="/jstl/fmt" prefix="fmt" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title></title>
<%String contextPath = request.getContextPath();%>
<script type="text/javascript">
	function doInit(){
   		loadcalendar();//初始化时间控件
	}
</script>
</head>
<body onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
         当前位置：售后服务管理&gt;索赔结算管理&gt;结算室收票查询
</div>
<form method="post" name="fm" id="fm">
<input type="hidden" value="${balance_yieldly}" id="balance_yieldly" name="balance_yieldly"/>
<input type="hidden"  id="dealerid" name="dealerid"/>
<input type="hidden"  id="dealerName1" name="dealerName1"/>
<input type="hidden"  id="aplcount1" name="aplcount1"/>
<input type="hidden"  id="number1" name="number1" value="${number}" />
<input type="hidden"  id="jude" name="jude"/>
<table align="center"  class="table_query" border='1'>
	<tr>
		<td align="center" nowrap="true">总运费</td>
        <td lign="center" class="table_query_4Col_input" nowrap="nowrap">
			${ticketsPO.sumCarriage}
		</td>
		<td align="center" nowrap="true">昌河运费</td>
        <td lign="center" class="table_query_4Col_input" nowrap="nowrap">
			${ticketsPO.carriage}
		</td>
		<td align="center" nowrap="true">东安运费</td>
        <td lign="center" class="table_query_4Col_input" nowrap="nowrap">
			${ticketsPO.daCarriage}
		</td>
		<td align="center" nowrap="true">服务站简称</td>
		<td align="center"  nowrap="true">
			${ticketsPO.dealername}
		</td>
	</tr>
	<tr>
		<td align="center" nowrap="true">信函总类</td>
        <td class="table_query_4Col_input" nowrap="nowrap">
		<script type='text/javascript'>
      	 var activityType=getItemValue('${ticketsPO.letter}');
      	 document.write(activityType) ;
      </script>			
		</td>
		<td align="center" nowrap="true">发出日期</td>
        <td align="center" class="table_query_4Col_input" nowrap="nowrap">
			<fmt:formatDate value="${ticketsPO.startdate}" pattern="yyyy-MM-dd"/>
		</td>
		<td align="center" nowrap="true">收到日期</td>
		<td align="center" align="left" nowrap="true">
		<fmt:formatDate value="${ticketsPO.enddate}" pattern="yyyy-MM-dd"/>
		</td>
		<td align="center" nowrap="true">信函情况</td>
        <td class="table_query_4Col_input" nowrap="nowrap">
        <script type='text/javascript'>
      	 var activityType=getItemValue('${ticketsPO.lettersf}');
      	 document.write(activityType) ;
      </script>	
		</td>
	</tr>
	<tr>
		<td align="center" colspan="2" nowrap="true">单据批号</td>
		<td align="center"  colspan="2" nowrap="true">收单数</td>
		<td align="center"  colspan="2" nowrap="true">索赔类型</td>
		<td align="center" colspan="2" nowrap="true">编号</td>
	</tr>
	<tr>
		<td align="center" colspan="2" nowrap="true">${ticketsPO.goodsnum}</td>
		<td align="center"  colspan="2" nowrap="true">${ticketsPO.aplcount}</td>
		<td align="center" colspan="2" nowrap="true">${ticketsPO.claimType}</td>
		<td align="center" colspan="2" nowrap="true">${ticketsPO.numberAp}</td>
	</tr>
	<tr>
		<td colspan="8" align="center">
			 <INPUT class=normal_btn onclick="javascript:history.go(-1)" value=返回 type=button name=bt_back>
		</td>
	</tr>
	<tr>
	<td>
	</td>
	</tr>
</table>
	<!--分页  -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	
<script type="text/javascript">
</script>
</form>
</body>
</html>