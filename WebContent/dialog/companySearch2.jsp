<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c"%>
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
				<td class="table_query_label" nowrap="nowrap" >公司名称:</td>
				<td class="table_query_input" nowrap="nowrap">
					<input datatype="1,is_noquotation,<%=Constant.Length_Check_Char_100 %>" id="companyName" name="companyName" class="middle_txt" type="text" />
				</td>
			</tr>
			<tr>
				<td class="table_query_label" nowrap="nowrap">
					<input class="normal_btn" id="queryBtn" type="button" value="查 询" onclick="queryCompany();"/>
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
//弹出容器设置
//弹出页面:父页面
var parentContainer = parent || top;
if( parentContainer.frames ['inIframe'] ){
	parentContainer = parentContainer.document.getElementById ('inIframe').contentWindow;
}
//弹出页面document
var parentDocument =parentContainer.document;
//父元素根据 ID获得元素
function parentGetId( id ){
	return parentDocument.getElementById(id);
}

	var url = null;
	//设置表格标题
	var title= null;
	//设置列名属性
	var columns = null;
	
	function queryCompany() {
	   url = "<%=contextPath%>/common/OrgMng/queryComByBrand.json";
	   columns = [
					{header: "选择", dataIndex: 'companyId',width:"50px",renderer:myLink},
					{header: "公司代码",width:"50px", dataIndex: 'COMPANY_CODE'},
					{header: "公司名称", dataIndex: 'COMPANY_NAME'},
					{header: "公司简称", dataIndex: 'COMPANY_SHORTNAME'}
			      ];
	   __extQuery__(1); 
	}
		

	function myLink(value,meta,record){
		return "<input type='radio' name='rd' onclick='setCom(\""+record.data.COMPANY_ID+"\",\""+record.data.COMPANY_SHORTNAME+"\",\""+record.data.AREAS+"\")' />"
	}
	
	function setCom(companyId,companyName,areas){
		parentGetId('COMPANY_NAME').value = companyName;
		parentGetId('COMPANY_ID').value = companyId;
		parentContainer.checkArea(areas);
// 		parentDocument.getElementById('COMPANY_NAME').value = companyName;
// 		parentDocument.getElementById('COMPANY_ID').value = companyId;
// 		parentContainer.checkArea(areas);
 		_hide();
	}
	
	
	function clsTxt(){ //清除经销商文本框
		parentGetId('COMPANY_NAME').value = "";
		parentGetId('COMPANY_ID').value = "";
		_hide();
	}
	
</script>
</html>