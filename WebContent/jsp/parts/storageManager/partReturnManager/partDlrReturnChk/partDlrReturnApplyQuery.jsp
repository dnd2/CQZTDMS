<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
    String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件销售退货申请审核</title>
<script type="text/javascript" >
$(function(){
	__extQuery__(1);
});
var myPage;

var url = "<%=contextPath%>/parts/storageManager/partReturnManager/PartDlrReturnChkManager/queryPartDlrReturnApplyInfo.json";
			
var title = null;

var columns = [
			{header: "序号", align:'center',renderer:getIndex},
			{id:'action',header: "操作",sortable: false,dataIndex: 'RETURN_ID',renderer:myLink ,align:'center'},
			{header: "退货单号", dataIndex: 'RETURN_CODE', align:'center'},
			{header: "退货单位", dataIndex: 'DEALER_NAME', style:'text-align: left;'},
			{header: "制单人", dataIndex: 'CREATE_NAME', style:'text-align: left;'},
			{header: "提交日期", dataIndex: 'APPLY_DATE', align:'center',renderer:formatDate},
			{header: "状态", dataIndex: 'STATE', align: 'center', renderer: getItemValue},
			{header: "退货原因", dataIndex: 'REMARK', style:'text-align: left;'}
	      ];
	if('${chkLevel}' == '2'){
		// 二级审核	
		columns[8] = {header: "一级审核人", dataIndex: 'VL_ONE_BY_NAME', align: 'center'};
		columns[9] = {header: "一级审核时间", dataIndex: 'VL_ONE_DATE', align: 'center'};
	}
	if('${chkLevel}' == '3'){
		// 三级审核
		columns[8] = {header: "一级审核人", dataIndex: 'VL_ONE_BY_NAME', align: 'center'};
		columns[9] = {header: "一级审核时间", dataIndex: 'VL_ONE_DATE', align: 'center'};
		columns[10] = {header: "二级审核人", dataIndex: 'VL_TWO_BY_NAME', align: 'center'};
		columns[11] = {header: "二级审核时间", dataIndex: 'VL_TWO_DATE', align: 'center'};
	}


//设置超链接
function myLink(value,meta,record){
		return String.format("<a href=\"#\" onclick='checkApply(\""+value+"\")'>[审核]</a>");
}

//审核
function checkApply(value)
{
	window.location.href = '<%=contextPath%>/parts/storageManager/partReturnManager/PartDlrReturnChkManager/queryApplyDetailInit.do?returnId='+value+'&chkLevel='+'${chkLevel}';
}

//格式化日期
function formatDate(value,meta,record){
	var output = value.substr(0,10);
	return output;
}
</script>

</head>
<body>
	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 配件管理&gt;配件仓库管理 >配件退货管理&gt;销售退货审核
		</div>
		<form name="fm" id="fm" method="post">
			<input type="hidden" id="chkLevel" name="chkLevel" value="${chkLevel}">
			<div class="form-panel">
				<h2>
					<img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;" />查询条件
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">退货单号：</td>
							<td>
								<input class="middle_txt" type="text" name="RETURN_CODE" id="RETURN_CODE" />
							</td>
							<td class="right">退货单位：</td>
							<td>
								<input class="middle_txt" type="text" name="DEALER_NAME" id="DEALER_NAME" />
							</td>
							<td align="right">销售人员：</td>
							<td>
								<select name="salerId" id="salerId" class="u-select">
									<option value="">--请选择--</option>
									<c:if test="${salerFlag}">
										<c:forEach items="${salerList}" var="saler">
											<c:choose>
												<c:when test="${curUserId eq saler.USER_ID}">
													<option selected="selected" value="${saler.USER_ID}">${saler.NAME}</option>
												</c:when>
												<c:otherwise>
													<option value="${saler.USER_ID}">${saler.NAME}</option>
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</c:if>
								</select>
							</td>
						</tr>
						<tr>
							<td colspan="6" class="center">
								<input name="BtnQuery" id="queryBtn" class="u-button" type="button" value="查 询" onclick="__extQuery__(1);" />
							</td>
						</tr>
					</table>
				</div>
			</div>


			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		</form>
	</div>
</body>
</html>
