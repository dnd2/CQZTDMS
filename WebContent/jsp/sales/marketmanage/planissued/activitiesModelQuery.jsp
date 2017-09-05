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
				<td class="table_query_label" align="right">车型代码：</td>
				<td class="table_query_input" align="left">
					<input datatype="1,is_noquotation,<%=Constant.Length_Check_Char_100 %>" id="prodCode" name="prodCode" class="middle_txt" type="text" />
				</td>
				<td class="table_query_label" align="right">车型名称：</td>
				<td class="table_query_input" align="left">
					<input datatype="1,is_noquotation,<%=Constant.Length_Check_Char_100 %>" id="prodName" name="prodName" class="middle_txt" type="text" />
				</td>
				<td width="30%">
					<input name="button2" type=button class="cssbutton" onclick="__extQuery__(1);" value="查询"/>
					<input class="cssbutton" type="reset" value="重置" />
					<input class="cssbutton" type="button" onclick="_hide();" value="关闭" />
				</td>
			</tr>
		</table>
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	</form>
 </div>
<script language="javascript">
	var url = "<%=contextPath%>/sales/marketmanage/planissued/ActivitiesPlanMake/queryModel.json";
	//设置表格标题
	var title= null;
	//设置列名属性
	var columns = [
					{header: "选择", dataIndex: 'PROD_ID',width:"50px",renderer:myLink},
					{header: "车型代码",width:"50px", dataIndex: 'PROD_CODE'},
					{header: "车型名称", dataIndex: 'PROD_NAME'}
			      ];
	
	function myLink(value,meta,record){
		return "<input type='radio' name='rd' onclick='setModle(\""+record.data.PROD_ID+"\",\""+record.data.PROD_NAME+"\")' />"
	}
	function setModle(prodCode,prodName){
		parent.$('inIframe').contentWindow.$('campaignModel').value = prodName;
		parent.$('inIframe').contentWindow.$('modelId').value = prodCode;
		_hide();
	}
	function doInit(){
		__extQuery__(1);
	}
</script>
</body>
</html>