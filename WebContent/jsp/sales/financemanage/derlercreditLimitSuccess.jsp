<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="java.util.List"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
	String year = (String)request.getAttribute("year");
    List dateList = (List)request.getAttribute("dateList");
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<script type="text/javascript">
 function isSave(){          
      	   MyConfirm("是否确认保存信息?",importSave);         
       }
 
function importSave() {
		document.getElementById("savebtn").disabled=true;
		var fm = document.getElementById("fm");
		fm.	action = "<%=contextPath %>/sales/financemanage/DerlerCreditLimit/importExcel.do";
		fm.submit();
		//makeCall(url,saveBack,'');
}

function saveBack(){
	
}

</script>


<title>返利任务导入</title>
</head>
<body onunload='javascript:destoryPrototype()' onload="loadcalendar();showSub();">
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置>财务管理>返利管理>返利任务导入</div>
			
<table class="table_list" width="85%" align="center" border="0">		
	   <tr>
		    <th><div align="center">经销商代码</div></th>
            <th><div align="center">经销商简称</div></th>
            <th><div align="center">返利金额</div></th>
            <th><div align="center">备注</div></th>
  	   </tr>
  	<c:forEach items="${dateList}" var="dateList"
		varStatus="steps">
		<tr class="table_list_row${steps.index%2+1 }">
			<td><div align="center">${dateList.CONTACT_DEPT_CODE }</div></td>
			<td><div align="center">${dateList.CONTACT_DEPT_SHORTNAME }</div></td>
			<td><div align="center">${dateList.PAY_SUM }</div></td>
			<td><div align="center">${dateList.REMARK }</div></td>
		</tr>
	</c:forEach>
 	
</table>


<form  name="frm" id="frm">
<table class="table_query" width="85%" align="center" border="0"  id="roll">	
	<tr align="center" >
		<th colspan="6">
			<div align="center">
				<input class="u-button u-query" type="button" id="savebtn" name='saveResButton' onclick='isSave();' value='保存' />
				<input class="u-button u-cancel" type='button' name='saveResButton' onclick='history.back();' value='返回' />
			</div>
		</th>	
  	</tr>
</table>
</form>
<form name="fm" id="fm"  method="post"  enctype="multipart/form-data"></form>

</body>
</html>
