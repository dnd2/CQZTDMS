<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>物料价格维护 </title>
</head>
<body>
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：系统管理 > 产品维护 > 物料价格维护 </div>
<table class="table_query" width="85%" align="center" border="0">		
	   <tr>
		    <th><div align="center">行号</div></th>
            <th><div align="center">错误信息</div></th>
  	   </tr>
  	<c:forEach items="${errorList}" var="errorList" >
		<tr class="table_list_row1">
			<td><div align="center">${errorList.rowNum }</div></td>
			<td><div align="center">${errorList.errorDesc }</div></td>
		</tr>
	</c:forEach>
  	
</table>
<jsp:include page="${contextPath}/queryPage/orderHidden.html" /></div>
<form>
</form>
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<form  name="frm" id="frm">
<table class="table_query" width="85%" align="center" border="0"  id="roll">	
	<tr align="center" >
		<th colspan="6">
			<div align="left">
				<input class="cssbutton" type='button' name='saveResButton' onclick='importSave();' value='确定' />
			</div>
		</th>	
  	</tr>
</table>
</form>
<script type="text/javascript">
function importSave() {
	if(submitForm('frm')){
	    var url='<%=contextPath %>/sysproduct/productmanage/MaterialPriceManage/materialPriceManageQueryPre.do';
		frm.action = url;
		frm.submit();
	}
}
</script>
</body>
</html>
