<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
	String contextPath = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>经销商地址查询</title>
</head>
<body>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：经销商地址查询</div>
<form id="fm" name="fm" method="post">
<input type="hidden" name="curPage" id="curPage" value="1" /> <input type="hidden" id="dlrId" name="dlrId" value="" />
<table class="table_query" border="0">
	<tr>
		<td width="20%" class="tblopt">
		<div class="right">选择经销商：</div>
		</td>
		<td>
			<input name="dealerCode" type="text" id="dealerCode" class="SearchInput" value="" size="20" readonly="readonly" />
            <input name="dlbtn" type="button" class="mini_btn" onclick="showOrgDealer('dealerCode','','true');" value="..." />
            <input type="button" class="normal_btn" onclick="txtClr('dealerCode');" value="清 空" id="clrBtn" />
		</td>
		<td></td>
	</tr>
	<tr>
		<td width="20%" class="tblopt">
		<div class="right">业务范围：</div>
		</td>
		<td>
			<select name="areaId">
				<c:forEach items="${areaBusList}" var="po">
					<option value="${po.AREA_ID}">${po.AREA_NAME}</option>
				</c:forEach>
            </select>
		</td>
	</tr>
	<tr>
		<td width="20%" class="tblopt">
		<div class="right">地址名称：</div>
		</td>
		<td width="39%"><input type="text" id="address" name="address" /></td>
		<td class="table_query_3Col_input">
			<input type="button" class="normal_btn" onclick="__extQuery__(1);" value="查 询" id="queryBtn" />
		</td>
	</tr>
</table>
<jsp:include page="${contextPath}/queryPage/orderHidden.html" /> 
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
</div>
<script type="text/javascript">

	var myPage;
	var url = "<%=contextPath%>/sales/storageManage/DealerAddressQuery/queryAddress.json?COMMAND=1";
	var title = null;
	var columns = [
		{header: "经销商代码", dataIndex: 'DEALER_CODE', align:'center'},
		{header: "经销商名称", dataIndex: 'DEALER_SHORTNAME', align:'center'},
		{header: "地址代码", dataIndex: 'ADD_CODE', align:'center'},
		{header: "地址名称", dataIndex: 'ADDRESS', align:'center'},
		{header: "联系人", dataIndex: 'LINK_MAN', align:'center'},
		{header: "电话", dataIndex: 'TEL', align:'center'},
		{header: "省份", dataIndex: 'PROVINCE_NAME', align:'center'},
		{header: "地级市", dataIndex: 'CITY_NAME', align:'center'},
		{header: "县", dataIndex: 'AREA_NAME', align:'center'},
		{header: "状态", dataIndex: 'STATUS', align:'center',renderer:getItemValue},
		{header: "备注", dataIndex: 'REMARK', align:'center'}
		];
	
	function txtClr(valueId) {
		document.getElementById(valueId).value = '' ;
	}
</script>
</body>
</html>