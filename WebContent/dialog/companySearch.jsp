<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<script>
		var url = "<%=contextPath%>/common/OrgMng/queryCom.json";
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
			return "<input type='radio' name='rd' onclick='setCom(\""+record.data.companyId+"\",\""+record.data.companyShortname+"\")' />"
		}
		
		function setCom(companyId,companyName){
			__parent().$('#COMPANY_NAME').val(companyName);
			__parent().$('#COMPANY_ID').val(companyId);
			_hide();
		}
		
		$(function($){
			__extQuery__(1);
		});
	</script>
</head>
<body>
	 <div>
	 	<form id='fm' name='fm'>
			<table class="table_query" >
				<tr>
					<td>公司代码：</td>
					<td>
						<input datatype="1,is_noquotation,<%=Constant.Length_Check_Char_100 %>" id="companyCode" name="companyCode" class="short_txt" type="text" />
					</td>
					<td >公司名称：</td>
					<td>
						<input datatype="1,is_noquotation,<%=Constant.Length_Check_Char_100 %>" id="companyName" name="companyName" class="middle_txt" type="text" />
					</td>
				</tr>
				<tr>
					<td colspan="4" style="text-align: center;" >
						<input class="normal_btn" id="queryBtn" type="button" value="查 询" onclick="__extQuery__(1);"/>
						<input class="normal_btn" type="reset" value="重 置" />
						<input class="normal_btn" type="button" onclick="_hide();" value="关 闭" />
					</td>
				</tr>
			</table>
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		</form>
	</div>
</body>
</html>