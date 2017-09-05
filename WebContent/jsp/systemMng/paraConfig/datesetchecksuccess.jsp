<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="java.util.List"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
	String year = (String)request.getAttribute("year");
    List planList = (List)request.getAttribute("planList");
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>>工作日历导入</title>
</head>
<body onunload='javascript:destoryPrototype()' onload="loadcalendar();showSub();">
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置>工作日历导入</div>
<table class="table_list" width="85%" align="center" border="0">		
  	
</table>
<jsp:include page="${contextPath}/queryPage/orderHidden.html" /></div>
<form>
</form>
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<form  name="frm" id="frm">
<table class="table_query" width="85%" align="center" border="0"  id="roll">	
	<tr align="center" >
		<td>
			<div align="left">
			    <input type='hidden' name='year' value='${year }' />
			     文件无错误,点“<font color="#FF0000">保存</font>”按钮,完成导入操作
			     <p></p>
				<input class="cssbutton" type='button' name='saveResButton' onclick='importSave();' value='保存' />
				<input class="cssbutton" type='button' name='backBtn' onclick='history.back();' value='返回' />
			</div>
		</td>	
  	</tr>
</table>
</form>
<script type="text/javascript">
function importSave() {
	if(submitForm('frm')){
			frm.action = "<%=contextPath %>/sysmng/paraConfig/DateSetImport/importExcel.do";
			frm.submit();
		}
}
</script>
</body>
</html>
