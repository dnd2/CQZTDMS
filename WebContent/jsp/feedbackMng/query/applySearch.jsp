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
				<td class="table_query_label" nowrap="nowrap">申请单位ID：</td>
				<td class="table_query_input"   nowrap="nowrap">
					<input datatype="1,is_noquotation,<%=Constant.Length_Check_Char_100 %>" id="userId" name="userId" class="short_txt" type="text" />
				</td>
				<td class="table_query_label" nowrap="nowrap" >申请单位名称：</td>
				<td class="table_query_input" nowrap="nowrap">
					<input datatype="1,is_noquotation,<%=Constant.Length_Check_Char_100 %>" id="name" name="name" class="middle_txt" type="text" />
				</td>
				<td class="table_query_label" nowrap="nowrap" >申请单位电话：</td>
				<td class="table_query_input" nowrap="nowrap">
					<input datatype="1,is_noquotation,<%=Constant.Length_Check_Char_100 %>" id="phone" name="phone" class="middle_txt" type="text" />
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
	var url = "<%=contextPath%>/feedbackmng/apply/CommonQueryApply/queryApplyMantain.json";
	//设置表格标题
	var title= null;
	//设置列名属性
	var columns = [
					{header: "选择", dataIndex: 'USER_ID',width:"50px",renderer:myLink},
					{header: "申请单位ID",width:"50px", dataIndex: 'USER_ID'},
					{header: "申请单位名称",width:"50px", dataIndex: 'NAME'},
					{header: "申请单位电话", dataIndex: 'PHONE'}
			      ];
	
	function myLink(value,meta,record){
		return "<input type='radio' name='rd' onclick='setCom(\""+record.data.USER_ID+"\",\""+record.data.NAME+"\",\""+record.data.PHONE+"\")' />"
	}
	
	function setCom(userId,name,phone){
		parentDocument.getElementById('name').value = name;
		parentDocument.getElementById('userId').value = userId;
		parentDocument.getElementById('phone').value = phone;
		_hide();
	}
	
	
	function clsTxt(){ //清除经销商文本框
		parentDocument.getElementById('name').value = "";
		parentDocument.getElementById('userId').value = "";
		parentDocument.getElementById('phone').value = "";
		_hide();
	}
</script>
</html>