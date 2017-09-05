<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>

<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<%
	String contextPath = request.getContextPath();
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>发运组板查看</title>
</head>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：储运管理>发运管理>发运组板查看</div>
<form name="fm" method="post" id="fm">
<div class="form-panel">
	<h2>发运组板查看</h2>
	<div class="form-body">
	<table class="table_query" style="margin-top: 1px; margin-bottom: 1px;" id="subtab1">
		<tr>
			<td noWrap class="right">发运方式:</td>
			<td align="left" width="15%">${sendBoardMap.SHIP_NAME }</td>
			<td noWrap class="right">承运商:</td>
			<td align="left" width="15%">${sendBoardMap.LOGI_NAME }</td>
			<td noWrap class="right">发运结算地:</td>
			<td align="left" width="15%">${sendBoardMap.BAL_ADDR}</td>
		</tr>
		<tr>
			<td noWrap class="right">组板号:</td>
			<td align="left" width="15%">${sendBoardMap.BO_NO }</td>
			<td noWrap class="right">组板日期:</td>
			<td align="left" width="15%">${sendBoardMap.BO_DATE }</td>
			<td noWrap class="right">驾驶员姓名:</td>
			<td align="left" width="15%">${sendBoardMap.DRIVER_NAME}</td>
		</tr>
		<tr>
			<td noWrap class="right">驾驶员电话:</td>
			<td align="left" width="15%">${sendBoardMap.DRIVER_TEL }</td>
			<td noWrap class="right">承运车牌号:</td>
			<td align="left" width="15%">${sendBoardMap.CAR_NO }</td>
			<td noWrap class="right">领票车队:</td>
			<td align="left" width="15%">${sendBoardMap.CAR_TEAM }</td>
		</tr>
	</table>
	</div>
</div>
<div id=detailDiv>
<table class=table_list width="100%">
	<tbody>
		<tr class=cssTable>
			<th noWrap align=middle>订单号</th>
			<th noWrap align=middle>发运仓库</th>
			<th noWrap align=middle>经销商或收货仓库</th>
			<th noWrap align=middle>车系</th>
			<th noWrap align=middle>车型</th>
			<th noWrap align=middle>配置</th>
			<th noWrap align=middle>颜色</th>
			<th noWrap align=middle>物料代码</th>
			<th noWrap align=middle>已组板量</th>
			<th noWrap align=middle>本次组板量</th>
			<th noWrap align=middle>发运数量</th>
		</tr>
		<tr class=table_list_row1>
			<c:if test="${list!=null}">
				<c:forEach items="${list}" var="list">
					<tr class=table_list_row1>
						<td>${list.ORD_NO}</td>
						<!-- 批售单号 -->
						<td>${list.WH_NAME}</td>
						<!-- 发运仓库 -->
						<td>${list.DEALER_NAME}</td>
						<!-- 经销商Name -->
						<td>${list.SERIES_NAME}</td>
						<!-- 车系名称 -->
						<td>${list.MODEL_NAME}</td>
						<!-- 车型名称 -->
						<td>${list.PACKAGE_NAME}</td>
						<!-- 配置名称 -->
						<td>${list.COLOR_NAME}</td>
						<!-- 颜色 -->
						<td>${list.MATERIAL_CODE}</td>
						<!-- 物料CODE-->
						<td>${list.DLV_BD_TOTAL}</td>
						<!-- 已组板数量 -->
						<td>${list.BD_TOTAL}</td>
						<!-- 本次组板数量-->
						<td>${list.FY_TOTAL}</td>
						<!-- 发运数量  -->
					</tr>
				</c:forEach>
			</c:if>
		</tr>
	</tbody>
</table>
</div>

</form>
<!--页面列表 begin -->
<script type="text/javascript">
	function doInit() {
		var arrayObj = new Array();
		var arrayObj1 = new Array();
		var arrayObj2 = new Array();
		var amountSum = 0;//获取已组板
		var boardSum = 0;//获取已组板总数
		var thisBoardSum = 0;//当前组板总数
		arrayObj = document.getElementsByName("HIDDEN_CHECK_AMOUNT");//开票数量
		arrayObj1 = document.getElementsByName("HIDDEN_BOARD_NUM");//已组板总数
		arrayObj2 = document.getElementsByName("HIDDEN_THIS_BOARD_NUM");//当前组板总数
		for ( var i = 0; i < arrayObj.length; i++) {
			if (arrayObj[i].value != "") {
				amountSum += arrayObj[i].value * 1;
			}
		}
		for ( var i = 0; i < arrayObj1.length; i++) {
			if (arrayObj1[i].value != "") {
				boardSum += arrayObj1[i].value * 1;
			}
		}
		for ( var i = 0; i < arrayObj2.length; i++) {
			if (arrayObj2[i].value != "") {
				thisBoardSum += arrayObj2[i].value * 1;
			}
		}
		document.getElementById("SHOW_CHECK_AMOUNT").innerHTML = amountSum;
		document.getElementById("SHOW_BOARD_NUM").innerHTML = boardSum;
		document.getElementById("SHOW_THIS_BOARD_NUM").innerHTML = thisBoardSum;
	}
</script>
</body>
</html>
