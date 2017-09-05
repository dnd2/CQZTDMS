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
<script type="text/javascript" src="<%=request.getContextPath()%>/js/crm/customer/customer.js"></script>
<script type="text/javascript">
	var myPage;
	var url = "<%=contextPath%>/crm/customer/CustomerManage/customerQueryList.json?COMMAND=1";
	var title = null;
	var columns = null;
	columns=[
				{header: "客户编码", dataIndex:'CUSTOMER_CODE',  align:'center'},
				{header: "客户名称", dataIndex: 'CUSTOMER_NAME', align:'center'},
				{header: "客户电话", dataIndex: 'TELEPHONE', align:'center'},
				{header: "客户地址", dataIndex: 'STATUS', align:'center',renderer:getItemValue},
				{header: "意向车型", dataIndex: 'INTENT_VEHICLE', align:'center'}
		      ];
</script>
<title>客户管理</title>
</head>
<body onunload='javascript:destoryPrototype();' > 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt;  潜客管理  &gt;客户管理 &gt; 客户新增查询</div>
		<form id="fm" name="fm" method="post">
		<input  type="hidden" id="curPaths" value="<%=contextPath%>"/>
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<table class="table_query" border="0">
			<tr>
	     	 	<td align="left" nowrap >客户名称：</td>
		     	<td align="left" nowrap >
		      	<input type="text" name="customerName" />
		      	</td>
		    	<td >电话：</td>
		     	<td ><input type="text" name="phone" /></td>
		     	<td >意向车型：</td>
		     	<td ><input type="text" name="vechile" /></td>
			</tr>
	    	<tr>
		    	<td colspan="3"> &nbsp;</td>
				<td  colspan="3" align="left" >
						<input type="button" class="normal_btn" onclick="__extQuery__(1);" value=" 查  询  " id="queryBtn" />
						&nbsp;&nbsp;&nbsp;<input type="button" class="normal_btn" onclick="addPre();" value=" 新  增 " id="addBtn" />
				</td>
		</tr>
		</table>
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />	
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	</form>
	
	
</div>   
</body>
</html>