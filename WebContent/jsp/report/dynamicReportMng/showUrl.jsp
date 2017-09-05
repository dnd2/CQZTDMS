<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@page import="java.util.List"%>
<%@page import="com.infodms.dms.po.TtAsReportShowparamPO" %>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
	List<TtAsReportShowparamPO> lists = (List<TtAsReportShowparamPO>)request.getAttribute("list2");
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>动态数据管理</title>
<script type="text/javascript">
function doInit(){
	loadcalendar();
}
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：数据管理 > 售后数据管理 > 数据管理</div>
<form method="POST" name="fm" id="fm">
<input type="hidden" name="reportId" value="${reportId}" />
<input type="hidden" name="userId" value="${userId}" />
<input type="hidden" name="dealerId" value="${dealerId}" />
<a href="#" onclick="showM('remark')">数据说明</a>
<table class="table_query" id="remark" style="display:none">
	<tr>
		<td>数据说明：${report.remark }</td>
	</tr>
	<tr>
		<td>使用说明：${report.remark2 }</td>
	</tr>
</table>
<br />
<a href="#" onclick="showM('input')">输入参数信息</a>
<table class="table_edit" id="input" style="display:block">
	<c:forEach var="input" items="${list3}" varStatus="st">
		<tr class="table_list_row${st.index%2+1}">
			<td>${input.paramName}</td>
			<td>
				<c:if test="${input.paramType==13881001}">
					<input type="text" name="${input.paramCode}" class="middle_txt" />
				</c:if>
				<c:if test="${input.paramType==13881002}">
					<input type="text" name="${input.paramCode}" id="${input.paramCode}" class="short_txt" datatype="1,is_date,10"  hasbtn="true" callFunction="showcalendar(event, '${input.paramCode}', false);"/>
				</c:if>
				<c:if test="${input.paramType==13881003}">
					<script>
						genSelBoxExp("${input.paramCode}",'${input.defaultValue}',"",true,"short_sel","","false",'');
					</script>
				</c:if>
				<c:if test="${input.paramType==13881004}">
					<textarea name="${input.paramCode}" rows="2" cols="40"></textarea>
					<br/>
					<label style="color:red">各值之间用英文逗号分隔！</label>
				</c:if>
			</td>
		</tr>
	</c:forEach>
</table>
<br />
<table class="table_edit">
	<tr>
		<td align="center" width="90%">
			<input type="button" id="queryBtn" value="前台运行" class="normal_btn" onclick="doQuery()" />&nbsp;
			<input type="button" id="queryBtn" value="导出Excel" class="normal_btn" onclick="toExcel()" />&nbsp;
			<input type="button" value="返回" class="normal_btn" onclick="goBack()" />
		</td>
	</tr>
</table>
<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
<script type="text/javascript">
	var url = '<%=contextPath%>/report/reportmng/DynamicReportMng/doQuery.json' ;

	var title = null ;

	var columns = [
	               {header:'序号',renderer:getIndex},
	               <%
	               for (int i=0;i<lists.size()-1;i++){
	              	%>	               
	              	               {header:'<%=lists.get(i).getShowName()%>',width:'10%',align:'center',dataIndex:'<%=lists.get(i).getOtherName()%>'},

	              <%}%>
	              <%
	              	if(lists.size()>0){
	              %>
	              {header:'<%=lists.get(lists.size()-1).getShowName()%>',width:'10%',align:'center',dataIndex:'<%=lists.get(lists.size()-1).getOtherName()%>'}
	              <%}%>
		           	] ;

	function doQuery(){
		__extQuery__(1);
	}
	function toExcel(){
		fm.action = '<%=contextPath%>/report/reportmng/DynamicReportMng/toExcel.do' ;
		fm.submit();
	}
	function showM(id){
		if($(id).style.display=='block')
			$(id).style.display = 'none' ;
		else
			$(id).style.display = 'block' ;
	}
	function goBack(){
		history.go(-1);
	}
</script>
</body>
</html>