<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>

<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<%
String contextPath=request.getContextPath();
String orderId = request.getParameter("orderId") == null ? "" : request.getParameter("orderId").toString();
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title> 车辆成本预算管理 </title>

</head>

<body>
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：车辆明细
	</div>
<form name="fm" method="post" id="fm">
<input type="hidden" value="<%=orderId %>" id="orderId" name="orderId"/>
<!-- 查询条件 begin -->
<table class="table_query" id="subtab" >

</table>
<!-- 查询条件 end -->
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
<table class="table_query">
	<tr>
		<td align="center"><input type="button" onclick="_hide()" value="关  闭" class="normal_btn"/></td>
	</tr>
</table>
</form>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
	//查询路径           
	var url = "<%=contextPath%>/sales/storage/storagemanage/VehicleCostbudget/vehicleConnectedQuery.json";
	var title = null;
	var columns = [
				{header: "序号",align:'center',renderer:getIndex},
				{header: "VIN",dataIndex: 'VIN',align:'center'},
				{header: "车系",dataIndex: 'SERIES_NAME',align:'center'},
				{header: "配置",dataIndex: 'PACKAGE_NAME',align:'center'},
				{header: "颜色",dataIndex: 'MATERIAL_NAME',align:'center'},
				{header: "物料代码",dataIndex: 'MATERIAL_CODE',align:'center'},
				{header: "发动机号",dataIndex: 'ENGINE_NO',align:'center'},
				{header: "生产日期",dataIndex: 'PRODUCT_DATE',align:'center'},
				{header: "入库日期",dataIndex: 'ORG_STORAGE_DATE',align:'center'}
		      ];
	//初始化    
	function doInit(){
		__extQuery__(1);
	}
</script>
</body>
</html>
