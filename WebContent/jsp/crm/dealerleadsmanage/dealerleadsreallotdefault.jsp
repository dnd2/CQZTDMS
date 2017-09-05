<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
    int yes = Constant.IF_TYPE_YES;
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">
	function doInit(){
		loadcalendar();   //初始化时间控件
	}
	function txtClr(valueId) {
		document.getElementById(valueId).value = '' ;
	}
</script>

<title>销售线索分派</title>
</head>
<body onunload='javascript:destoryPrototype();'> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置>潜客管理>经销商线索管理>线索分派</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="leadsAllotId" name="leadsAllotId" value="${leadsAllotId}"/>
		<input type="hidden" id="leadsGroup" name="leadsGroup" value="${leadsGroup}"/>
		<table class="table_query" width="95%" align="center">
		<c:if test="${returnValue==2}">
		<tr>
			<td  colspan="6">
			</td>
		</tr>
		</c:if>
		    <tr>
			      <td align="right" width="17%"></td>
			      <td> 
			      <font color="red">${errorMsg }</font>
			      </td>
			</tr>
		    <tr>
		      	<input name="ret" type="button" class="normal_btn" onClick="javascript:history.go(-2);" value="返回" /></td>
		    </tr>
  	</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	</form>
	
</div>
</body>
</html>