<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔工时明细</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</head>
<body>
  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;索赔基础数据&gt;下发索赔工时</div>
<form id="fm" name="fm">
  <table class="table_list" style="border-bottom:1px solid #DAE0EE" >
      <th>工时代码</th>
      <th>中文说明</th>
      <th>英文说明</th>
      <th>工时系数</th>
      <th>索赔工时</th>
      <th>附加工时明细</th>
       <c:forEach var="addlist" items="${ADDLIST}">
       <tr class="table_list_row1">
          <td> 
			<c:out value="${addlist.LABOUR_CODE}"></c:out>
          </td>
          <td>
          <c:out value="${addlist.CN_DES}"></c:out>
          </td>
          <td>
          <c:out value="${addlist.EN_DES}"></c:out>
          </td>          
          <td>
          <c:out value="${addlist.LABOUR_QUOTIETY}"></c:out>
          </td>
          <td>
          <c:out value="${addlist.LABOUR_HOUR}"></c:out>
          </td>
          <td>
          	<a href="#" onclick="addlaborDetail('${addlist.ID}');">附加工时</a>
          </td>          
        </tr>
    </c:forEach>
</table>
<br/>
<table class="table_edit">
      <tr> 
      <td colspan="2" align="center">
		<input type="button" onclick="_hide();" class="normal_btn"  value="关闭"/>
      </td>
	  </tr>
</table>
</form>
<script type="text/javascript">
//车型组对应的附加工时明细：
function addlaborDetail(val){
	OpenHtmlWindow('<%=contextPath%>/claim/basicData/ClaimDowLoadMain/addLaborDetail.do?WRGROUP_ID='+val,900,500);
} 
</script>
</body>
</html>
