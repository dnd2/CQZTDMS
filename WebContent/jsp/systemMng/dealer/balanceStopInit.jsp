<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>经销商维护</title>
</head>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 系统管理 &gt; 经销商管理 &gt;经销商维护</div>
<form id="fm" name="fm">
<input id="COMPANY_ID" name="COMPANY_ID" type="hidden"/>
<table class="table_query" border="0">
	<tr>
		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">经销商代码：</td>
		<td class="table_query_4Col_input" nowrap="nowrap"><input
			name="DEALER_CODE" maxlength="30" datatype="1,is_noquotation,30" id="DEALER_CODE" type="text" class="middle_txt" /></td>
		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">经销商简称：</td>
		<td class="table_query_4Col_input" nowrap="nowrap"><input
			name="DEALER_NAME" maxlength="30" datatype="1,is_noquotation,75" id="DEALER_NAME" type="text" class="middle_txt" /></td>
	</tr>
	<tr>
		<td class="table_query_3Col_label_6Letter" nowrap="nowrap">状态：</td>
		<td align="left">
			<script>
			genSelBoxExp("STATUS",<%=Constant.IF_TYPE%>,"-1",true,"short_sel",'',"false",'');
			</script>
		</td>
		
			<td class="table_query_3Col_label_6Letter" nowrap="nowrap">产地：</td>
		<td align="left">
			<script>
			genSelBoxExp("YIELDLY",<%=Constant.SERVICEACTIVITY_CAR_YIELDLY%>,"-1",true,"short_sel",'',"false",'');
			</script>
		</td>
		<td colspan="2" align='center'>
			<input name="queryBtn" type="button" class="normal_btn" onclick="__extQuery__(1)" value="查 询" id="queryBtn" />
		</td>
	</tr>
</table>
</form>
<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />	
<script>
var myPage;
var url = "<%=contextPath%>/sysmng/dealer/DealerInfo/queryDealerBS.json";
var title= null;
var columns = [
				{header:"经销商代码",dataIndex:'DEALER_CODE',align:'center'},
				{header:"经销商名称",dataIndex:'DEALER_NAME',align:'center'},
				{header:"产地",dataIndex:'YIELDLY',align:'center',renderer:getItemValue},
				{header:'是否停止结算',dataIndex:'STATUS',align:'center',renderer:getItemValue},
				{header:'变更日期',dataIndex:'UPDATE_DATE',align:'center',renderer:formatDate},
				{id:'action',header:"操作",align:'center',dataIndex:'ID',renderer:myLink}
			  ];
function myLink(value,meta,record){
	var str = '<a href="<%=contextPath%>/sysmng/dealer/DealerInfo/goUpdateBS.do?id='+value+'&status='+record.data.STATUS+'&YILYLE='+record.data.YIELDLY+'&DEALER_ID='+record.data.DEALER_ID+'">修改</a>' ;
	return str;
}
function formatDate(value,meta,record) {
	if(record.data.status =='<%=Constant.CLAIM_APPLY_ORD_TYPE_03%>'){
		return '';
	}else if (value==""||value==null) {
		return "";
	}else {
		return value.substr(0,10);
	}
}
</script>
</body>
</html>
