<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="java.util.List"%>

<%@taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>常规订单提报明细</title>
<script type="text/javascript">
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;常规订单提报明细</div>
<form method="POST" name="fm" id="fm">
  <table class="table_list" style="border-bottom:1px solid #DAE0EE" >
  <tr align="center" class="tabletitle">
    <th>订单号</th>
    <th>车系代码</th>
    <th>车系名称</th>
    <th>物料代码</th>
    <th>物料名称</th>
    <th>颜色</th>
    <th>订单数量</th>
  </tr>
  <c:forEach items="${detailList}" var="detailList">
  <tr class="table_list_row1">
   	<td align="center" nowrap="nowrap">${detailList.ORDER_NO}</td>
   	<td align="center" nowrap="nowrap">${detailList.GROUP_CODE}</td>
   	<td align="center" nowrap="nowrap">${detailList.GROUP_NAME}</td>
   	<td align="center" nowrap="nowrap">${detailList.MATERIAL_CODE}</td>
   	<td align="center" nowrap="nowrap">${detailList.MATERIAL_NAME}</td>
   	<td align="center" nowrap="nowrap">${detailList.COLOR_NAME}</td>
   	<td align="center" nowrap="nowrap">${detailList.ORDER_AMOUNT}</td>
  </tr>
  </c:forEach>
</table>

  <TABLE class=table_query>
    <TR class=cssTable style="cursor:hand">
      <td width="100%" align=left>
      	<input class="cssbutton" name="baocun" type="button"  value="提交" onClick="submitCommit();">
      </td>
    </tr>
 </table>
</form>
<script type="text/javascript">
	function submitCommit(){
		MyDivConfirm("是否提交?",submitCommitAction);
	}
	function submitCommitAction(){
		//mod by LQ 
		if (parent.$('inIframe')) {
			parent.$('inIframe').contentWindow.orderSubmit();
		} else {
			parent.orderSubmit();
		}
		parent._hide();
	}
	
</script>
</body>
</html>
