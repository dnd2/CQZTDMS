<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
	String detail_id = (String)request.getAttribute("detail_id");
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>经销商订单发运指令下达查询</title>
<script type="text/javascript">
function doInit(){
	__extQuery__(1);
}
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车销售 > 结算中心管理 > 经销商订单管理 > 经销商订单发运指令下达查询</div>
<form method="POST" name="fm" id="fm">
<table width="85%" align="center" class="table_query">
  <tr class="tabletitle">
    <td>车辆信息列表<input type="hidden" name="detail_id" value="<%=detail_id %>"></td>
  </tr>
</table>
<!--分页 begin --> 
<jsp:include page="${contextPath}/queryPage/orderHidden.html" /> 
<jsp:include page="${contextPath}/queryPage/pageDiv.html" /> 
<!--分页 end -->
</form>
<script type="text/javascript">
	var myPage;
	var url = "<%=contextPath%>/sales/balancecentermanage/dealerordermanage/DealerOrderDispatchQuery/dispatchListQuery.json?COMMAND=1";
	var title = null;
	var columns = [
				{header: "VIN", dataIndex: 'VIN', align:'center'},
				{header: "物料编码", dataIndex: 'MATERIAL_CODE', align:'center'},
				{header: "物料名称", dataIndex: 'MATERIAL_NAME', align:'center'},
				{header: "当前节点", dataIndex: 'NODE_CODE', align:'center',renderer:getItemValue},
				{header: "节点更新时间", dataIndex: 'NODE_DATE', align:'center'}
		      ];
</script>
</body>
</html>
