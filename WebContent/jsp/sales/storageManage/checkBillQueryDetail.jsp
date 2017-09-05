<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="com.infodms.dms.po.TtInspectionDetailPO"%>

<%
	String contextPath = request.getContextPath();
	Map<String,Object> checkDetail = (Map<String,Object>)request.getAttribute("checkDetail");
	List<TtInspectionDetailPO> detailList = (List<TtInspectionDetailPO>)request.getAttribute("detailList");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>车辆验收单查询明细(</title>
</head>
<body onunload='javascript:destoryPrototype();'>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp; 车辆验收单明细查询</div>
<table class="table_list">
	<tr>
		<th class="right">送车交接单号：</th>
		<th align="left">${checkDetail.SENDCAR_ORDER_NUMBER}</th>
		<th class="right">订单类型：</th>
		<th align="left">${checkDetail.DELIVRY_TYPE }</th>
	</tr>
	<tr>
		<th class="right">出货地点：</th>
		<th align="left">${checkDetail.COMPANY_SHORTNAME }</th>
		<th class="right">出货仓库：</th>
		<th align="left">${checkDetail.WAREHOUSE_NAME }</th>
	</tr>
	<tr>
		<th class="right">收货单位：</th>
		<th align="left">${checkDetail.DEALER_SHORTNAME }</th>
		<th class="right">收货单位联系人：</th>
		<th align="left">${checkDetail.LINK_MAN }</th>
	</tr>
	<tr>
		<th class="right">收货单位地址：</th>
		<th align="left">${checkDetail.ADDRESS }</th>
		<th class="right">收货单位联系电话：</th>
		<th align="left">${checkDetail.PHONE }</th>
	</tr>
	<tr>
		<th class="right">运输公司：</th>
		<th align="left">${checkDetail.SHIP_METHOD_CODE }</th>
		<th class="right">运输公司联系人：</th>
		<th align="left">${checkDetail.MOTORMAN }</th>
	</tr>
	<tr>
		<th class="right">运输公司联系电话：</th>
		<th align="left">${checkDetail.MOTORMAN_PHONE }</th>
		<th class="right">运输方式：</th>
		<th align="left">${checkDetail.DELIVRY_TYPE }</th>
	</tr>
	<tr>
		<th class="right">总数量：</th>
		<th align="left">${checkDetail.SENDCAR_AMOUNT }</th>
		<th class="right">发出日期：</th>
		<th align="left">${checkDetail.FLATCAR_ASSIGN_DATE }</th>
	</tr>
	<tr>
		<th class="right">预计到达日期：</th>
		<th align="left">${checkDetail.ARRIVE_DATE }</th>
		<th class="right">批售单号：</th>
		<th align="left">${checkDetail.ORDER_NO }</th>
		
	</tr>
	<tr>
		<th class="right">发运申请单号：</th>
		<th align="left">${checkDetail.DLVRY_REQ_NO }</th>
		<th class="right">VIN：</th>
		<th align="left">${checkDetail.VIN }</th>
		
	</tr>
	<tr>
		<th class="right">物料名称：</th>
		<th align="left">${checkDetail.MATERIAL_NAME }</th>
		<th class="right">生产日期：</th>
		<th align="left">${checkDetail.PRODUCT_DATE }</th>
		
	</tr>
	<tr>
		<th class="right">出厂日期：</th>
		<th align="left">${checkDetail.FACTORY_DATE }</th>
		<th class="right">接收状态：</th>
		<th align="left">${checkDetail.IS_RECEIVE }</th>
	</tr>
</table>

<table class="table_query" width="85%" align="center" border="0">
	<tr align="center">
		<td><input type="button" class="normal_btn" value="返回"
			onclick="history.back();" /></td>
	</tr>
</table>

</div>
</body>
</html>