<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
	
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>潜客更多联系人</title>
<script type="text/javascript">
 </script>
</head>
<body onunload='javascript:destoryPrototype();'>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;潜客更多联系人</div>
 <br>
 <table width="85%" align="center" class="table_query">
  <tr class="tabletitle">
    <td>潜客更多联系人</td>
  </tr>
</table>
<table class="table_list" id="storageId">
	<tr class=tabletitle>
		<th>联系人名称</th>
		<th>性别</th>
		<th>电话 </th>
		<th>手机 </th>
		<th>传真 </th>
		<th>E-MAIL </th>
		<th>备注 </th>
	</tr>
	<c:forEach items="${morelinkmans}" var="linkman" varStatus="vstatus">
		<tr class="<c:if test='${vstatus.index%2==0}'>table_list_row1</c:if><c:if test='${vstatus.index%2!=0}'>table_list_row2</c:if>">
			<td>
				${linkman.CONTACTOR_NAME }
			</td>
			<td>
			   <script>document.write(getItemValue(${linkman.GENDER }));</script>
			</td>
			<td>
			${linkman.PHONE }
			</td>
			<td>
			${linkman.MOBILE }
			</td>
			<td>
			${linkman.FAX }
			</td>
			<td>
			${linkman.E_MAIL }
			</td>
			<td>
			${linkman.REMARK }
			</td>
		</tr>
	</c:forEach>
</table>
<table  class="table_query">
    <tr align="center">      
      <td>
      	<input class="normal_btn" type="button" name="button" value="关闭"  onclick="_hide();" />
      </td>
  	</tr>	
   </table>  
</div>
</body>
</html>