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
	<div style=" margin:2px;float:left;border-right:solid 1px;">
			<script type="text/javascript">
				var rs = ${orgs}.orgs;
				d = new dTree('d', "<%=contextPath%>");
				d.add(0,-1,'经销公司选择');
				for(var i=0;i<rs.length;i++){
					d.add(rs[i].id,0,rs[i].name,"javascript:setDlr(" + rs[i].id + ",'" + rs[i].name + "');");
				}
				document.write(d);
				d.closeAll();
				d.o(1);
		
			</script>
	 </div>
	 <div style="float:right;">
	 <form id='fm' name='fm'>
		<input type="hidden" id="orgId" name="orgId" value="" />
		<table class="table_query" >
			<tr>
				<td class="table_query_label" nowrap="nowrap">代理商代码：</td>
				<td class="table_query_input"   nowrap="nowrap">
					<input datatype="1,is_noquotation,<%=Constant.Length_Check_Char_100 %>" id="dlrCode" name="dlrCode" class="short_txt" type="text" />
				</td>
				<td class="table_query_label" nowrap="nowrap" >代理商：</td>
				<td class="table_query_input" nowrap="nowrap">
					<input datatype="1,is_noquotation,<%=Constant.Length_Check_Char_100 %>" id="dlrName" name="dlrName" class="middle_txt" type="text" />
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
	var url = "<%=contextPath%>/common/OrgMng/queryDlr.json";
	//设置表格标题
	var title= null;
	//设置列名属性
	var columns = [
					{header: "选择", dataIndex: 'dlrId',width:"50px",renderer:myLink},
					{header: "代理商代码",width:"50px", dataIndex: 'dlrCode'},
					{header: "代理商", dataIndex: 'dlrName'},
					{header: "代理商简称", dataIndex: 'dlrShortName'}
			      ];
	
	function myLink(value,meta,record){
		return "<input type='radio' name='rd' onclick='setDlr(\""+record.data.dlrId+"\",\""+record.data.codeName+"\")' />"
	}
	
	function setDlr(dlrId,dlrName){
		parentDocument.getElementById('dlrName').value = dlrName;
		parentDocument.getElementById('dlrId').value = dlrId;
		_hide();
	}
	
	function clsTxt(){ //清除经销商文本框
		parentDocument.getElementById('dlrName').value = "";
		parentDocument.getElementById('dlrId').value = "";
		_hide();
	}
</script>
</html>