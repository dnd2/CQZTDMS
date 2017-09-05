<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/jstl/change" prefix="change" %>
<%@ taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<title>售后经销商绑定查询</title>
<script type="text/javascript">
	var myPage;
	var url = "<%=contextPath%>/sysmng/dealer/ServiceDealerBind/queryServiceDealerInfo.json";
	var title= null;
	var columns = [{header: "大区",width:'10%',   dataIndex: 'ROOT_ORG_NAME'},
					{header: "省份",width:'10%',   dataIndex: 'REGION_NAME'},
					{header: "经销商代码",width:'10%',   dataIndex: 'DEALER_CODE'},
					{header: "经销商名称", width:'20%', dataIndex: 'DEALER_NAME'},
					{header: "经销商等级", width:'10%', dataIndex: 'DEALER_LEVEL',renderer:getItemValue},
					{header: "组织名称", width:'20%', dataIndex: 'ORG_NAME'},
					{header: "所属公司", width:'20%', dataIndex: 'COMPANY_NAME'},
					{header: "经销商状态", width:'10%', dataIndex: 'SERVICE_STATUS',renderer:getItemValue},
					{header: "授权类型", width:'10%', dataIndex: 'AUTHORIZATION_TYPE'},
					{header: "授权时间 ", width:'10%', dataIndex: 'AUTHORIZATION_DATE'},
					{header: "绑定经销商", width:'10%', dataIndex: 'BINDED_DEALER_NAME'},
					{id:'action',header: "绑定详情", walign:'center',idth:70,sortable: false,dataIndex: 'DEALER_ID',renderer:myLink}
				  ];

	//查询
	function doQuery() {
	    //执行查询
	    __extQuery__(1);
	}
	function myLink(dealer_id,metaDate,record){
		var link = "";
		if (!record.data.BINDED_DEALER_NAME) {
			link = "<a href=\"<%=contextPath%>/sysmng/dealer/ServiceDealerBind/queryBindServiceDealerInit.do?DEALER_ID="+dealer_id+"\">[绑定]</a>"; 
		}
	    return String.format(link);
	} 
</script>
</head>
<body onload="doQuery()">
<form method="post" id="fm" name="fm">
	<table class="table_query" border="0">
		<tr>
			<td class="table_query_3Col_label_6Letter" nowrap="nowrap">经销商代码：</td>
			<td class="table_query_4Col_input" nowrap="nowrap">
				<input name="DEALER_CODE" maxlength="30" datatype="1,is_noquotation,30" id="DEALER_CODE" type="text" class="middle_txt" />
			</td>
			<td class="table_query_3Col_label_6Letter" nowrap="nowrap">经销商名称：</td>
			<td class="table_query_4Col_input" nowrap="nowrap">
				<input name="DEALER_NAME" maxlength="30" datatype="1,is_noquotation,75" id="DEALER_NAME" type="text" class="middle_txt" />
			</td>
		</tr>
		<tr align="center">
			<td colspan="4" class="table_query_4Col_input" style="text-align: center">
				<input name="queryBtn" type="button" class="normal_btn" onclick="doQuery()" value="查 询" id="queryBtn" />
			</td>
		</tr>
	</table>
</form>
<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</body>
</html>

