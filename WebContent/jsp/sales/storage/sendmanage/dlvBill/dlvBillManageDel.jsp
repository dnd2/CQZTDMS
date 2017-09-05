<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>

<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<%
String contextPath=request.getContextPath();
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>交接单查看</title>
</head>
<body>
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理>发运管理>交接单查看</div>
<form name="fm" method="post" id="fm">
<table class="table_list" style="margin-top: 1px; margin-bottom: 1px;" id="subtab1">
<tr  class="table_list_row2">
	<td noWrap align=right>交接单号:</td>
	<td  align=center width="15%">${map.BILL_NO }</td>
	<td noWrap align=right>订单号:</td>
	<td align=center width="15%">${map.ORDER_NO }</td>
	<td noWrap align=right>发运仓库:</td>
	<td  align=center width="15%">${map.WH_NAME}</td>
	<td noWrap align=right>经销商或收货仓库:</td>
	<td  align=center width="15%">${map.DEALER_NAME }</td>
</tr>
<tr  class="table_list_row2">
	<td noWrap align=right>发运方式:</td>
	<td  align=center width="15%">${map.SHIP_NAME}</td>
	<td noWrap align=right>承运商:</td>
	<td align=center width="15%">${map.LOGI_NAME}</td>
	<td noWrap align=right>发运结算地:</td>
	<td  align=center width="15%">${map.BAL_ADDR}</td>
	<td noWrap align=right>组板号:</td>
	<td  align=center width="15%">${map.BO_NO}</td>
</tr>
<tr  class="table_list_row2">
	<td noWrap align=right>组板日期:</td>
	<td align=center width="15%">${map.BO_DATE }</td>
	<td noWrap align=right>司机姓名:</td>
	<td align=center width="15%">${map.DRIVER_NAME }</td>
	<td noWrap align=right>司机手机:</td>
	<td align=center width="15%">${map.DRIVER_TEL}</td>
	<td noWrap align=right>所属车队:</td>
	<td  align=center width="15%">${map.CAR_TEAM}</td>
</tr>
<tr  class="table_list_row2">
	<td noWrap align=right>车牌号:</td>
	<td align=center width="15%">${map.CAR_NO}</td>
	<td noWrap align=right>计划装车日期:</td>
	<td  align=center width="15%">${map.PLAN_LOAD_DATE}</td>
	<td noWrap align=right>最晚发运日期:</td>
	<td align=center width="15%">${map.DLV_FY_DATE}</td>
	<td noWrap align=right>最晚到货日期:</td>
	<td align=center width="15%">${map.DLV_JJ_DATE}</td>
</tr>
</table>
<div style="
	overflow-x:scroll;
	overflow-y:scroll;
	border:solid 1px #C2C2C2;
	scrollbar-3dlight-color:#595959;
	scrollbar-arrow-color:#CCCCCC;
	scrollbar-base-color:#CFCFCF;
	scrollbar-darkshadow-color:#FFFFFF;
	scrollbar-face-color:#F3F4F8;
	scrollbar-highlight-color:#FFFFFF;
	scrollbar-shadow-color:#595959;" id=detailDiv>
<table class=table_list width="100%">
<tbody>
<tr class=cssTable>
<th noWrap align=middle>车系</th>
<th noWrap align=middle>车型</th>
<th noWrap align=middle>配置</th>
<th noWrap align=middle>颜色</th>
<th noWrap align=middle>整车物料码</th>
<th noWrap align=middle>车架号</th>
</tr>
<tr class=table_list_row1>
<c:if test="${list!=null}">
<c:forEach items="${list}" var="list">
<tr class=table_list_row1>
<td>${list.SERIES_NAME}</td><!-- 车系名称 -->
<td>${list.MODEL_NAME}</td><!-- 车型名称 -->
<td>${list.PACKAGE_NAME}</td><!-- 配置名称 -->
<td>${list.COLOR_NAME}</td><!-- 颜色 -->
<td>${list.MATERIAL_CODE}</td><!-- 物料CODE-->
<td>${list.VIN}</td><!--车架号 -->
</tr>
</c:forEach>
</c:if>
</tr>
</tbody></table>
</div>

</form>
<!--页面列表 begin -->
<script type="text/javascript" >
	function doInit(){
		var arrayObj = new Array(); 
		var arrayObj1 = new Array(); 
		var arrayObj2 = new Array(); 
		var amountSum=0;//获取已组板
		var boardSum=0;//获取已组板总数
		var thisBoardSum=0;//当前组板总数
		arrayObj=document.getElementsByName("HIDDEN_CHECK_AMOUNT");//开票数量
		arrayObj1=document.getElementsByName("HIDDEN_BOARD_NUM");//已组板总数
		arrayObj2=document.getElementsByName("HIDDEN_THIS_BOARD_NUM");//当前组板总数
		for(var i=0;i<arrayObj.length;i++){
			if(arrayObj[i].value!=""){
				amountSum+=arrayObj[i].value*1;
			}
		}
		for(var i=0;i<arrayObj1.length;i++){
			if(arrayObj1[i].value!=""){
				boardSum+=arrayObj1[i].value*1;
			}
		}
		for(var i=0;i<arrayObj2.length;i++){
			if(arrayObj2[i].value!=""){
				thisBoardSum+=arrayObj2[i].value*1;
			}
		}
		document.getElementById("SHOW_CHECK_AMOUNT").innerHTML=amountSum;
		document.getElementById("SHOW_BOARD_NUM").innerHTML=boardSum;
		document.getElementById("SHOW_THIS_BOARD_NUM").innerHTML=thisBoardSum;
	}
</script>
</body>
</html>
