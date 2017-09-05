<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
	Map<String, Object> vehicleInfo = (Map<String, Object>)request.getAttribute("vehicleInfo");
%>
<%@taglib uri="/jstl/cout" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>运单结算查询</title>
<script type="text/javascript">
function formatMoney(dataStr){
	var str = formatCurrency(dataStr);
	document.write(str);
}
</script>
</head>
<body onunload='javascript:destoryPrototype();' onload="ChangeDateToString() ;changeFleet();">
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />
		&nbsp;当前位置：  整车物流管理&gt; 发运管理 &gt;运单结算查询</div>
	<form id="fm" name="fm" method="post"  enctype="multipart/form-data" >
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="balId" name="balId" value="${balId }"/>
		<table class="table_list" border="1">
			<tr align="left"><th colspan="5">&nbsp;车系汇总信息</th></tr>
			<tr>
				<th>序号</th>
				<th>车系代码</th>
				<th>车系名称</th>
				<th>数量</th>	
				<th>结算金额</th>
			</tr>	
				<c:forEach items="${series_list}" var="po">
					<tr align="center">
						<td>${po.ROW_NUM }</td>
						<td>${po.GROUP_CODE }</td>
						<td>${po.GROUP_NAME }</td>
						<td>${po.VEHICLE_NUM }</td>
						<td><script>
						formatMoney(${po.BAL_AMOUNT });
						</script></td>
					</tr>
				</c:forEach>
			<tr>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<Td>&nbsp;</Td>
				<td><strong>合计:</strong></td>
				<td>${sum_amount }</td>
			</tr>
		</table>
		<br />
		<br />
		<table class="table_list" border="1">
			<tr align="left"><th colspan="11">&nbsp;车辆明细信息</th></tr>
				    <Tr>
				    	<th>序号</th>
						<th>组板号</th>
						<th>发运时间</th>
						<th>车系</th>
					<!-- 	<th>物料名称</th>	 -->
						<th>VIN</th>
						<th>发动机号</th>
						<th>车厂出库日期</th>
						<th>验收入库日期</th>
						<th>运送里程</th>
						<th>里程单价</th>
						<th>结算金额</th>
				    </Tr>	
				    <c:forEach items="${detail_list}" var="po">
						<tr align="center">
							<td>${po.ROW_NUM }</td>
							<td>${po.BO_NO }</td>
							<Td>${po.BILL_CRT_DATE }</Td>
							<td>${po.GROUP_NAME }</td>
						<!-- 	<td>${po.MATERIAL_NAME }</td>  -->
							<td>${po.VIN }</td>
							<td>${po.ENGINE_NO }</td>
							<td>${po.OUT_DATE }</td>
							<td>${po.STORAGE_DATE }</td>
							<td>${po.SEND_DISTANCE }</td>
							<td><script>
								formatMoney(${po.SINGLE_PRICE });
							</script></td>
							<TD><script>
								formatMoney(${po.BAL_AMOUNT });
							</script></TD>
						</tr>
					</c:forEach>
					<tr>
						<td colspan="9">&nbsp;</td>
						<td><strong>合计:</strong></td>
						<td>${sum_amount }</td>
					</tr>
		</table>
		<table class="table_query" width="90%" border="0" align="center">
			<tr>
				<td align="center" >
<%-- 					<c:if test="${command == '1' }"> --%>
<!-- 						<input id="fanhui" name="button" type="button" class="normal_btn" onclick="toCancleBal()" value="取消结算" /> -->
<%-- 					</c:if> --%>
<%-- 					<c:if test="${command == '0' }"> --%>
<!-- 						<input id="fanhui" name="button" type="button" class="normal_btn" onclick="toExcel()" value="导出" /> -->
<%-- 					</c:if> --%>
					<input id="fanhui" name="button" type="button" class="normal_btn" onclick="window.history.back()" value="返回" />
				</td>
			</tr>
		</table>
	</form>
</div>
<script type="text/javascript">
		function toCancleBal(){
			var balId = document.getElementById("balId").value;
			MyConfirm("确定取消结算?",function(){
				sendAjax("<%=contextPath%>/sales/storage/sendmanage/SendBillSettlementQuery/cancleBalance.json?balId="+balId,showResult,'fm');
			});	
		}
		function showResult(json){
			if(json.result == "1"){
				parent.MyAlert("取消成功!");
				window.location.href = "<%=contextPath%>/sales/storage/sendmanage/SendBillSettlementQuery/sendBillSettlementQueryInit.do";
			}
		}
	
		function toExcel(){
			var balId = document.getElementById("balId").value;
			window.location.href = "<%=contextPath%>/sales/storage/sendmanage/SendBillSettlementQuery/toExcel.do?balId="+balId;
		}
</script>
</body>
</html>