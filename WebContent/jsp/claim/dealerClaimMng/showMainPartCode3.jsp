<!DOCTYPE html PUBLIC "-//W3C//Dtd Xhtml 1.0 transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<% String contextPath = request.getContextPath(); %>
<body onload="__extQuery__(1);">
<div class="wbox">
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" width="11" height="11" />&nbsp;当前位置：服务活动配件赠送明细 </div>
</div>
  <form  name="fm" id="fm" method="post">
  <input name="yiedlyType" id="yiedlyType" value="${yiedlyType }" type="hidden"/>
  <input name="id" id="id" value="${id }" type="hidden"/>
   <!--查询条件begin-->
    <table class="table_query" border="0" >
        <tr>
        <td colspan="4" align="center">
        <input name="button" id="queryBtn" type="button" onclick="_hide()" class="normal_btn"  value="关闭" />
        </td>
      </tr>
    </table>
    <!--查询条件end-->
    <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
<script language="JavaScript" >
	var myPage;
	
	var url = "<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/queryPartCode3.json";
				
	var title = null;

	var columns = [
				{header: "活动代码", dataIndex: 'activityCode', align:'center'},
				{header: "活动名称", dataIndex: 'activityName', align:'center'},
				{header: "配件代码", dataIndex: 'partCode', align:'center'},
				{header: "配件名称", dataIndex: 'partName', align:'center'},
				{header: "配件价格", dataIndex: 'price', align:'center'}
		      ];
		      
</script>
</body>
</html>
