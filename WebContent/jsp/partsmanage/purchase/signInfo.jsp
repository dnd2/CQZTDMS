<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- created by lishuai103@yahoo.com.cn 201006025 配件签收 -->
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件采购订单维护</title>
</head>

<script language="JavaScript">

	//初始化方法
	function doInit()
	{
		__extQuery__(1);
	}

</script>
<body>
<img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 配件管理 &gt; 配件采购 &gt;配件订单维护
<form id="fm" name="fm" method="post">
<input type="hidden" id="orderId" name="orderId" value="<c:out value="${orderId}"/>" />
<input type="hidden" id="doNo" name="doNo" value="<c:out value="${sign.DO_NO}"/>" />
<input type="hidden" id="orderNo" name="orderNo" value="<c:out value="${orderNo}"/>" />
    <table class="table_edit"  >
      <tr>
        <td class="table_query_2Col_label_6Letter">货运单号：</td>
        <td class="table_query_2Col_input">
          	<c:out value="${sign.DO_NO}"/>
        </td>
        <td class="table_query_2Col_label_6Letter">采购单号：</td>
        <td class="table_query_2Col_input">
          	<c:out value="${orderNo}"/>
        </td>
      </tr>
      <tr>
        <td class="table_query_2Col_label_6Letter">签收时间：</td>
        <td class="table_query_2Col_input">
          	<c:out value="${sign.SIGN_DATE}"/>
        </td>
        <td class="table_query_2Col_label_6Letter">签收人：</td>
        <td class="table_query_2Col_input">
          	<c:out value="${sign.SIGN_USER_ID}"/>
        </td>
      </tr>
    </table>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end -->
<br>
	<table class="table_edit">
		<tr>
		  <td align="center">		    
			<input type="button" name="BtnNo" value="返回" class="normal_btn" onclick="javascript:history.go(-1)">
          </td>
        </tr>
	</table>
</form>
<script type="text/javascript">
	var myPage;
//查询路径
	var url = "<%=contextPath%>/partsmanage/purchase/PurchaseOrderSearch/queryDonoSignInfo.json";
	
	var title = null;

	var columns = [
				{header: "配件号", dataIndex: 'PART_CODE', align:'center'},
				{header: "配件名称", dataIndex: 'PART_NAME', align:'center'},
				{header: "单位", dataIndex: 'UNIT', align:'center'},
				{header: "订货数量", dataIndex: 'ORDER_COUNT', align:'center'},
				{header: "货运输量", dataIndex: 'COUNT', align:'center'},
				{header: "签收数量", dataIndex: 'SIGN_QUANTITY', align:'center'}
		      ];
</script>
</body>
</html>