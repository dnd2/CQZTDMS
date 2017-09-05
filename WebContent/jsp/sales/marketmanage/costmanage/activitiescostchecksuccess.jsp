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
<title>市场活动费用导入</title>
</head>
<body onunload='javascript:destoryPrototype()' onload="loadcalendar();showSub();">
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置： 市场活动管理 &gt; 活动费用管理&gt;活动费用导入</div>
<table class="table_list" width="85%" align="center" border="0">		
	   <tr>
		    <th><div align="center">组织代码</div></th>
            <th><div align="center">组织名称</div></th>
            <th><div align="center">费用类型编码</div></th>
            <th><div align="center">费用类型</div></th>
            <th><div align="center">资金数量</div></th>
  	   </tr>
  	<c:forEach items="${planList}" var="planList"
		varStatus="steps">
		<tr class="table_list_row${steps.index%2+1 }">
			<td><div align="center">${planList.ORG_CODE }</div></td>
			<td><div align="center">${planList.ORG_NAME }</div></td>
			<td><div align="center">${planList.COST_TYPE}</div></td>
			<td><div align="center">${planList.COST_DESC}</div></td>
			<td><div align="center">${planList.ORG_AMOUNT }</div></td>
		</tr>
	</c:forEach>
  	
</table>
<jsp:include page="${contextPath}/queryPage/orderHidden.html" /></div>
<form>
</form>
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<form  name="frm" id="frm">
<input type="hidden" name="areaId" id="areaId" value="${areaId}"></input>
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
 function isSave(){
          if(submitForm('frm')){
      	    MyConfirm("是否确认保存信息?",importSave);
          }
       }
function importSave() {
		document.getElementById("savebtn").disabled=true;
		frm.action = "<%=contextPath %>/sales/marketmanage/costmanage/ActivitiesOrgCostImport/importExcel.do";
		frm.submit();
}
</script>
</body>
</html>
