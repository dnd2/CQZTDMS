<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<link href="<%=request.getContextPath()%>/style/dtree_default.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/web/dtree1.js"></script>
</head>
	
<body onunload='javascript:destoryPrototype()' >
	 <div style="float:right;">
	 <form id='fm' name='fm'>
			<table class="table_query" >
			<tr>
				<td class="table_query_label" nowrap="nowrap">公司代码：</td>
				<td class="table_query_input"   nowrap="nowrap">
					<input datatype="1,is_noquotation,<%=Constant.Length_Check_Char_100 %>" id="companyCode" name="companyCode" class="short_txt" type="text" />
				</td>
				<td class="table_query_label" nowrap="nowrap" >公司名称：</td>
				<td class="table_query_input" nowrap="nowrap">
					<input datatype="1,is_noquotation,<%=Constant.Length_Check_Char_100 %>" id="companyName" name="companyName" class="middle_txt" type="text" />
				</td>
			</tr>
			<tr>
				<td class="table_query_label" nowrap="nowrap">
					<input class="normal_btn" id="queryBtn" type="button" value="查 询" onclick="__extQuery__(1);"/>
				</td>
				<td class="table_query_label" nowrap="nowrap">
					<input class="normal_btn" type="reset" value="重 置" />
				</td>
				<td class="table_query_label" nowrap="nowrap">
					<input class="normal_btn" type="button" onclick="_hide();" value="关 闭" />
				</td>
				<td class="table_query_label" nowrap="nowrap">
					<input class="normal_btn" onclick="clsTxt();" type="reset" value="清 除" />
				</td>
			</tr>
		</table>
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		</form>
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		</div>
</body>
<script>
	var url = "<%=contextPath%>/common/OrgMng/queryCom111.json";
	//设置表格标题
	var title= null;
	//设置列名属性
	var columns = [
					{header: "选择", dataIndex: 'companyId',width:"50px",renderer:myLink},
					{header: "公司代码",width:"50px", dataIndex: 'companyCode'},
					{header: "公司名称", dataIndex: 'companyName'},
					{header: "公司简称", dataIndex: 'companyShortname'}
			      ];
	
	function myLink(value,meta,record){
		return "<input type='radio' name='rd' onclick='setCom(\""+record.data.companyId+"\",\""+record.data.companyShortname+"\",\""+record.data.companyName+"\",\""+record.data.companyCode+"\")' />"
	}
	
	function setCom(companyId,companyName,comName,comCode){
		parentDocument.getElementById('COMPANY_NAME').value = companyName;
		parentDocument.getElementById('COMPANY_ID').value = companyId;
		//parentDocument.getElementById('DEALER_NAME').value = comName;
		//parentDocument.getElementById('SHORT_NAME').value = companyName;
		//parentDocument.getElementById('DEALER_CODE').value = comCode;
		_hide();
	}
	
	
	function clsTxt(){ //清除经销商文本框
		parentDocument.getElementById('COMPANY_NAME').value = "";
		parentDocument.getElementById('COMPANY_ID').value = "";
		_hide();
	}
</script>
</html>