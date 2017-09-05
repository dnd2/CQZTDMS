<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@ taglib prefix="fmt" uri="/jstl/fmt" %>
<%
    String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript">
//获取选择框的值
function getCode(value) {
    var str = getItemValue(value);
    document.write(str);
}
//获取序号
function getIdx() {
    document.write(document.getElementById("file").rows.length - 1);
}
//返回
function goBack() {
    window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrInstock/partDlrInstockInit.do';
}
</script>
</head>
<body>
	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置: 配件管理 > 配件销售管理 &gt;配件销售单 >配件发运单查看
		</div>
		<div class="form-panel">
			<h2>
				<img src="<%=request.getContextPath()%>/img/subNav.gif" /> 发运单信息
			</h2>
			<div class="form-body">
				<table class="table_query">
					<tr>
						<td class="right">发运单:</td>
						<td>
							<%--             ${mainMap.TRPLAN_CODE} --%>
							${mainMap.TRANS_CODE}
						</td>
						<td class="right">发运日期:</td>
						<td><fmt:formatDate value="${mainMap.CREATE_DATE}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
						<td class="right">发出仓库:</td>
						<td>${mainMap.WH_NAME}</td>
					</tr>
					<tr>
						<td class="right">订货单位:</td>
						<td>${mainMap.DEALER_NAME}</td>
						<td class="right">收货单位:</td>
						<td>${mainMap.CONSIGNEES}</td>
						<td class="right">订单类型:</td>
						<td>
							<script type="text/javascript">getCode(${mainMap.ORDER_TYPE});</script>
						</td>
					</tr>
					<tr>
						<td class="right">接收地址:</td>
						<td>${mainMap.ADDR}</td>
						<td class="right"> 接收人:</td>
						<td>${mainMap.RECEIVER}</td>
						<td class="right">
							<span> 接收人电话:</span>
						</td>
						<td>${mainMap.TEL}</td>
					</tr>
					<tr>
						<td class="right">总金额:</td>
						<td>${mainMap.AMOUNT}</td>
					</tr>
					<tr>
						<td class="right">备注:</td>
						<td colspan="5">${mainMap.REMARK}</td>
					</tr>
				</table>
			</div>
		</div>
		<table id="file" class="table_list">
			<caption><img class="panel-icon nav" src="<%=contextPath%>/img/subNav.gif"/>配件信息</caption>
			<tr>
				<th>序号</th>
				<th>配件编码</th>
				<th>配件名称</th>
				<!-- <th>件号</th> -->
				<th>最小包装量</th>
				<th>单位</th>
				<th>订货数量</th>
				<th>发运数量</th>
				<th>备注</th>
			</tr>
			<c:forEach items="${detailList}" var="data" varStatus="status">
				<tr class="table_list_row1">
					<td align="center">&nbsp; ${status.index+1}</td>
					<td style="text-align: left">
						<c:out value="${data.PART_OLDCODE}" />
					</td>
					<td style="text-align: center">
						<c:out value="${data.PART_CNAME}" />
					</td>
					<!-- <td align="center"><c:out value="${data.PART_CODE}" /></td> -->
					<td align="center">
						&nbsp;
						<%--<c:out value="${data.MIN_PACKAGE}"/>--%>
						1
					</td>
					<td align="center">
						&nbsp;
						<c:out value="${data.UNIT}" />
					</td>
					<td align="center">
						&nbsp;
						<c:out value="${data.TRANS_QTY}" />
					</td>
					<td align="center">
						&nbsp;
						<c:out value="${data.TRANS_QTY}" />
					</td>
					<td align="center">
						&nbsp;
						<c:out value="${data.REMARK}" />
					</td>
				</tr>
			</c:forEach>
		</table>
		<table class="table_query">
			<tr>
				<c:if test="${trplanCode != '' }">
					<td class="center">
						<input class="u-button" type="button" value="关 闭" onclick="window.close();" />
					</td>
				</c:if>
				<c:if test="${trplanCode == '' }">
					<td class="center">
						<input class="u-button" type="button" value="关 闭" onclick="_hide();" />
					</td>
				</c:if>
			</tr>
		</table>
	</div>
</body>
</html>

