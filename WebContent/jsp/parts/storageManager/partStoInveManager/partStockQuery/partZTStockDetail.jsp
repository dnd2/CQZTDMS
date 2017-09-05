<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>
<%
    String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>盘点封存详情</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" >
$(function(){
	__extQuery__(1);
});
var myPage;
var url = "<%=contextPath%>/parts/storageManager/partStoInveManager/stockQueryAction/showPartZTDetSearch.json";
var title = null;
var columns = [
				{header: "序号", dataIndex: '', renderer:getIndex},
				{header: "配件编码", dataIndex: 'PART_OLDCODE'},
				{header: "配件名称", dataIndex: 'PART_CNAME'},
				{header: "件号", dataIndex: 'PART_CODE'},
				{header: "业务单号", dataIndex: 'ORDER_CODE'},
				{header: "计划员", dataIndex: 'BUYER'},
				{header: "日期", dataIndex: 'CREATE_DATE'},
				{header: "数量", dataIndex: 'OR_QTY'},
				{header: "在途类型", dataIndex: 'BTYPE'}
	      	  ];

function searchInfo(){
    __extQuery__(1);
}
</script>
</head>
<body>
	<div class="navigation">
		<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 基础信息管理 &gt; 配件基础信息查询 &gt; 配件库存查询 &gt; 在途详情
	</div>
	<form name='fm' id='fm'>
		<input type="hidden" name="partId" id="partId" value="${partId }" />
		<input type="hidden" name="whId" id="whId" value="${whId }" />
		<input type="hidden" name="viewPage" id="viewPage" value="${viewPage }" />

		<div class="form-panel">
			<h2>
				<img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;" />查询条件
			</h2>
			<div class="form-body">

				<table class="table_query">
					<tr>
						<td width="10%" class=="right">单号：</td>
						<td width="20%">
							<input class="long_txt" id="orderCode" name="orderCode" type="text" value="" />
						</td>
						<td width="10%" class=="right">在途类型：</td>
						<td width="20%">
							<select name="ztType" id="ztType">
								<option value="">请选择</option>
								<option value="有效订单">有效订单</option>
								<option value="入库中">入库中</option>
							</select>
						</td>
					</tr>
					<tr>
						<td colspan="6" class=="center">
							<input class="u-button" type="button" value="查 询" name="BtnQuery" id="queryBtn" onclick="searchInfo()" />
							<input class="u-button" type="reset" value="重 置">
							<input class="u-button" type="button" name="button1" value="关 闭" onclick="_hide();" />
						</td>
					</tr>
				</table>
			</div>
		</div>
		<!--分页 begin -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		<!--分页 end -->
	</form>
</body>
</html>