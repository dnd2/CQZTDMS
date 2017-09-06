<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
    String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${path}/common/jsp_head_new.jsp" />
<title>采购关查看</title>
<script type="text/javascript">
$(function(){
	__extQuery__(1);
});
var myPage;
var url = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartSalesRelation/salerChildQuery.json";			
var title = null;
var columns = [
	{header: "序号", align: 'center', renderer: getIndex, width: '7%'},
	{header: "服务商代码", dataIndex: 'CHILDORG_CODE', style: 'text-align: center;'},
	{header: "服务商", dataIndex: 'CHILDORG_NAME', style: 'text-align: center;'},
	{header: "是否有效", dataIndex:'STATE', align:'center', renderer: getItemValue}
];	
</script>
</head>
<body>
	<div class="wbox">
		<div class="navigation">
			<img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：配件管理 &gt; 基础信息管理 &gt; 配件基础信息维护 &gt; 配件采购关系维护 &gt; 查看下级单位
		</div>
		<form name="fm" id="fm" method="post">
			<input type="hidden" name="curPage" id="curPage" value="${curPage}" />
			<div class="form-panel">
				<h2>
					<img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;" />查询条件
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">销售单位代码:</td>
							<td>${fatherCode}
								<input type="hidden" name="FATHER_CODE" value="${fatherCode}" />
							</td>
							<td class="right">销售单位名称:</td>
							<td>${fatherName}
								<input type="hidden" name="FATHER_NAME" value="${fatherName}" />
								<input type="hidden" name="FATHER_ID" value="${fatherId}" />
							</td>
						</tr>
						<tr>
							<td class="right">服务商代码:</td>
							<td>
								<input type="text" class="middle_txt" name="DEALER_CODE" value="" />
							</td>
							<td class="right">服务商名称:</td>
							<td>
								<input type="text" style="width: 250px;" class="middle_txt" name="DEALER_NAME" value="" />
							</td>
						</tr>
						<tr>
							<td colspan="4" class="center">
								<input type="button" name="queryBtn" id="queryBtn" value="查 询" onclick="__extQuery__(1);" class="u-button" />
								<input type="button" name="saveBtn" id="saveBtn" value="返 回" onclick="javascript: _hide();" class="u-button" />
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
	</div>

</body>
</html>
