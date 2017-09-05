<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="java.util.List"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>销售线索导入 </title>
</head>
<body onunload='javascript:destoryPrototype()' onload="loadcalendar();showSub();">
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置>潜客管理>总部线索管理>线索导入</div>
<table class="table_list" width="85%" align="center" border="0">		
	   <tr>
		    <th><div align="center">客户姓名</div></th>
            <th><div align="center">联系电话</div></th>
            <th><div align="center">客户所在省</div></th>
            <th><div align="center">客户所在市</div></th>
            <th><div align="center">客户所在区</div></th>
            <th><div align="center">线索来源</div></th>
            <th><div align="center">收集时间</div></th>
            <th><div align="center">经销商代码</div></th>
            <th><div align="center">意向车系</div></th>
  	   </tr>
  	<c:forEach items="${leadsList}" var="leadsList"
		varStatus="steps">
		<tr class="table_list_row${steps.index%2+1 }">
			<td><div align="center">${leadsList.CUSTOMER_NAME }</div></td>
			<td><div align="center">${leadsList.TELEPHONE }</div></td>
			<td><div align="center">${leadsList.PROVINCE }</div></td>
			<td><div align="center">${leadsList.CITY }</div></td>
			<td><div align="center">${leadsList.AREA }</div></td>
			<td><div align="center">${leadsList.LEADS_ORIGIN }</div></td>
			<td><div align="center">${leadsList.COLLECT_DATE }</div></td>
			<td><div align="center">${leadsList.DEALER_ID }</div></td>
			<td><div align="center">${leadsList.INTENT_CAR }</div></td>
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
				<input class="cssbutton" type="button" id="savebtn" name='saveResButton' onclick='isSave();' value='保存' />
				<input class="cssbutton" type='button' name='saveResButton' onclick='history.back();' value='返回' />
			</div>
		</th>	
  	</tr>
</table>
</form>
<script type="text/javascript">
//禁止用F5键 
function document.onkeydown() 
{ 
if ( event.keyCode==116) 
{ 
event.keyCode = 0; 
event.cancelBubble = true; 
return false; 
} 
} 

//禁止右键弹出菜单 
function document.oncontextmenu(){event.returnValue=false;}//屏蔽鼠标右键 


 function isSave(){
          if(submitForm('frm')){
      	    MyConfirm("是否确认保存信息?",importSave);
          }
       }
function importSave() {
		document.getElementById("savebtn").disabled=true;
		frm.action = "<%=contextPath %>/crm/oemleadsmanage/OemLeadsManage/importExcel.do";
		frm.submit();
}
</script>
</body>
</html>
