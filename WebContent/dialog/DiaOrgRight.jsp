<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
</head>
<body onunload='javascript:destoryPrototype()' >
	<form id='fm' name='fm'>
		<input type="hidden" id="orgId" name="orgId" value="" />
		<table class="table_query" >
			<tr>
				<td class="table_query_label" nowrap="nowrap">经销商代码：</td>
				<td class="table_query_input"   nowrap="nowrap">
					<input id="dlrCode" name="dlrCode" class="short_txt" type="text" />
				</td>
				<td class="table_query_label" nowrap="nowrap" >经销商名称：</td>
				<td class="table_query_input" nowrap="nowrap">
					<input id="dlrName" name="dlrName" class="middle_txt" type="text" />
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
</body>
<script>
	var url = "<%=contextPath%>/common/OrgMng/queryDlr.json";
	//设置表格标题
	var title= null;
	//设置列名属性
	var columns = [
					{header: "选择", dataIndex: 'dlrId',renderer:myLink},
					{header: "经销商代码", dataIndex: 'dlrCode'},
					{header: "经销商名称", dataIndex: 'dlrName'}
			      ];
	
	function myLink(value,meta,record){
		return "<input type='radio' name='rd' onclick='setDlr(\""+record.data.dlrId+"\",\""+record.data.dlrShortName+"\")' />"
	}
	
	function setDlr(dlrId,dlrName){
		parent.parentDocument.getElementById('dlrName').value = dlrName;
		parent.parentDocument.getElementById('dlrId').value = dlrId;
		_hide();
	}
	
	function clsTxt(){ //清除经销商文本框
		parent.parentDocument.getElementById('dlrName').value = "";
		parent.parentDocument.getElementById('dlrId').value = "";
		_hide();
	}
</script>
</html>
