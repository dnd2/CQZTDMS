<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
	String sqlName = request.getParameter("sqlName");
	String paraJson = request.getParameter("paraJson");
	String callFunc = request.getParameter("callFunc");
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>查询</title>
</head>

<body onload ="__extQuery__(1)">
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
<input type='hidden' name='sqlName' id='sqlName' value='<%=sqlName%>'/>
<input type='hidden' name='paraJson' id='paraJson' value='<%=paraJson%>'/>
    <table class="table_query" >
	    <tr id="groupId">
	        <td width="20%">&nbsp;</td>
	        <td width="20%">查询条件：</td>
	        <td width="40%"><input type="text" id="para" name="para" jset="para"></td>	        
	        <td width="20%"><input name="queryBtn" id="queryBtn" type="button" class="normal_btn" onclick="__extQuery__(1);" value="查 询"/></td>	        	        
        </tr>
    </table>
<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
</body>
<script type="text/javascript">
	var myPage;
	var url = "<%=contextPath%>/common/PopSelectManager/getResult.json";
	var title = null;
	var columns = [
				{header: "选择",align:'center',dataIndex:'ID',renderer:getRadio},
				{header: "描述",dataIndex:'DESCR',align:'center'}
				];
	
	function getRadio(value, meta, record){
		var formatString = "<input type='radio' value="+value+" onClick='call("+JSON.stringify(record.data)+")'/>";
		return String.format(formatString);
	}

    /*选择之后触发函数*/
	function call(data){
		parentContainer.<%=callFunc%>(data);
		_hide();
	}
</script>
</html>
