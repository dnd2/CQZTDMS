<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<%
    String contextPath = request.getContextPath();
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>物流商维护查询</title>

<script type="text/javascript">
$(function(){
	__extQuery__(1);
});
var myPage;
//查询路径           
var url = "<%=contextPath%>/parts/baseManager/logisticsManage/LogisticsManage/logisticsQuery.json";
var title = null;
var columns = [
			{header: "序号",align:'center',renderer:getIndex},
			{header: "物流商编码",dataIndex: 'LOGI_CODE',align:'center'},
			{header: "物流商名称",dataIndex: 'LOGI_FULL_NAME',align:'center'},
			{header: "联系人",dataIndex: 'CON_PER',align:'center'},
			{header: "联系人电话",dataIndex: 'CON_TEL',align:'center'},
			{header: "公司地址",dataIndex: 'ADDRESS',align:'center'},
			//{header: "产地",dataIndex: 'YIELDLY',align:'center'},
			{header: "状态",dataIndex: 'STATUS',align:'center',renderer:getItemValue},
			{header: "操作",sortable: false, dataIndex: 'LOGI_ID', align:'center',renderer:myLink}
	      ];


//设置超链接
function myLink(value,meta,record){
 		return String.format("<a href=\"#\" onclick='sel(\""+value+"\")'>[修改]</a>");
}

//详细页面
function sel(value){
 window.location.href='<%=contextPath%>/parts/baseManager/logisticsManage/LogisticsManage/editLogisticsInit.do?Id='+value;  
}
//跳转新增页面
function addReservoir(){
	fm.action = "<%=contextPath%>/parts/baseManager/logisticsManage/LogisticsManage/addLogisticsInit.do";
	fm.submit();
}
</script>
</head>
<body>
	<div class="navigation">
		<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：配件管理 &gt;基础信息管理 &gt; 配件基础信息维护 &gt; 物流商维护
	</div>
	<form name="fm" method="post" id="fm">
		<div class="form-panel">
			<h2>
				<img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;" />查询条件
			</h2>
			<div class="form-body">
				<table class="table_query" id="subtab">
					<tr class="csstr" class="center">
						<td class="right">物流商代码：</td>
						<td class="left">
							<input type="text" id="LOGI_CODE" name="LOGI_CODE" class="middle_txt" size="15" />
						</td>
						<td class="right">物流商全称：</td>
						<td class="left">
							<input type="text" id="LOGI_FULL_NAME" name="LOGI_FULL_NAME" class="middle_txt" size="15" />
						</td>
					</tr>
					<tr class="csstr" class="center">
						<td class="right">联系人：</td>
						<td class="left">
							<input type="text" id="CON_PER" name="CON_PER" class="middle_txt" size="15" />
						</td>
						<td class="right">状态：</td>
						<td class="left">
							<label> <script type="text/javascript">
							genSelBoxExp("STATUS",<%=Constant.STATUS%>,"",true,"","","false",'');
						</script>
							</label>
						</td>
					</tr>
					<tr class="csstr" class="center" style="display: none">
						<td class="right">产地：</td>
						<td class="left">
							<select name="YIELDLY" id="YIELDLY" class="short_sel">
								<option value="">--请选择--</option>
								<c:if test="${list!=null}">
									<c:forEach items="${list}" var="list">
										<option value="${list.AREA_ID}">${list.AREA_NAME}</option>
									</c:forEach>
								</c:if>
							</select>
						</td>
					</tr>
					<tr>
						<td colspan="6" class="center">
							<input type="button" id="queryBtn" class="u-button" value="查询" onclick="__extQuery__(1);" />
							<input type="reset" id="resetButton" class="u-button" value="重置" />
							<input type="button" id="queryBtn" class="u-button" value="新增" onclick="addReservoir();" />
						</td>
					</tr>
				</table>
			</div>
		</div>
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	</form>
	<!--页面列表 begin -->
</body>
</html>
