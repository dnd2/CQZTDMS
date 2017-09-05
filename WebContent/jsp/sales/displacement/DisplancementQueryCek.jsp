<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>二手车置换审核记录查询</title>
<script type="text/javascript">
<!--
	function txtClr(valueId) {
		document.getElementById(valueId).value = '' ;
	}
  //->
</script>
</head>
<body onunload='javascript:destoryPrototype();'> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  整车销售 &gt; 客户信息管理 &gt; 二手车置换查询</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<table class="table_query" border="0">
			<tr>
		<td align="right" width="20%">二手车置换类型：</td>
		<td align="left" width="30%">
		<script type="text/javascript">
					genSelBoxExp("displacement_type",<%=Constant.DisplancementCarrequ_replace%>,"",false,"short_sel",'',"false",'');
			</script>
		</td>
				<td class="table_query_3Col_input" >
					<input type="hidden" name="vehicle_id" id="vehicle_id" />
					<input type="button" class="normal_btn" onclick="__extQuery__(1);" value=" 查  询  " id="queryBtn" /> 
				</td>
			</tr>
		</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	</form>
	
</div>
<script type="text/javascript">

	var myPage;
	
	var url = "<%=contextPath%>/sales/displacement/DisplacementCar/DisplacementQueryCekCar.json?COMMAND=1";
	
	var title = null;
	
	var columns = [
				{header: "旧车底盘号 ", dataIndex: 'OLD_VIN', align:'center'},
				{header: "新车底盘号", dataIndex: 'NEW_VIN', align:'center'},
				{header: "实销日期", dataIndex: 'NEW_SALES_DATE', align:'center'},
				{header: "二手车置换状态", dataIndex: 'DISPLACEMENT_TYPE', align:'center',renderer:getItemValue},
				{header: "审核状态", dataIndex: 'OPERATE_STATUS', align:'center',renderer:getItemValue}
		      ];
</script>    
</body>
</html>