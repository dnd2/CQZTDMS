<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件详细信息</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
	function doInit(){
   		__extQuery__(1);
	}
</script>
</head>

<body>
<div class="navigation">
  <img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 配件管理&gt;基本信息查询&gt;配件基本信息查询</div>
<form method="post" name = "fm">
<input type="hidden" name="partId" id="partId" value="<c:out value="${dealerInfo.PART_ID}"/>"/>
  <table width="101%" border="1" class="table_edit" cellpadding="0" cellspacing="0">
    <tr>
      <th colspan="6"><img src="<%=request.getContextPath()%>/img/subNav.gif" alt="" class="nav" /> 配件信息</th>
    </tr>
    <tr bgcolor="F3F4F8">
      <td class="table_query_3Col_label_6Letter">配件代码：</td>
      <td class="table_query_3Col_input">
		  <c:out value="${dealerInfo.PART_CODE}"/>
	  </td>
      <td class="table_query_3Col_label_6Letter">配件名称： </td>
      <td class="table_query_3Col_input">
	  	  <c:out value="${dealerInfo.PART_NAME}"/>
	  </td>
      <td class="table_query_3Col_label_6Letter">配件类型：</td>
      <td class="table_query_3Col_input">
      	  <c:out value="${dealerInfo.PART_TYPE}"/>
      </td>
    </tr>
    <tr >
      <td class="table_query_3Col_label_6Letter">单位：</td>
      <td class="table_query_3Col_input">
	  	  <c:out value="${dealerInfo.UNIT}"/>
	  </td>
      <td class="table_query_3Col_label_6Letter">最小包装数：</td>
      <td class="table_query_3Col_input">
	  	  <c:out value="${dealerInfo.MINI_PACK}"/>
	  </td>
      <td class="table_query_3Col_label_6Letter">停用： </td>
      <td class="table_query_3Col_input">
      		<c:out value="${dealerInfo.STOP_FLAG}"/>
      </td>
    </tr>
    <tr >
      <td class="table_query_3Col_label_6Letter">替代件：</td>
      <td class="table_query_3Col_input">
      		<c:out value="${dealerInfo.REPLACE_PART_ID}"/>
      </td>
      <td class="table_query_3Col_label_6Letter">替代关系：</td>
      <td class="table_query_3Col_input">
      		<c:out value="${dealerInfo.CHANGE_CODE}"/>
      </td>
      <td class="table_query_3Col_label_6Letter">单车用量：</td>
      <td class="table_query_3Col_input">
      		<c:out value="${dealerInfo.CAR_AMOUNT}"/>
      </td>
    </tr>
    <tr >
      <td class="table_query_3Col_label_6Letter">销售价格：</td>
      <td class="table_query_3Col_input">
	  		<c:out value="${dealerInfo.SALE_PRICE}"/>
	  </td>
      <td class="table_query_3Col_label_6Letter">销售指导价格：</td>
      <td class="table_query_3Col_input">
	  		<c:out value="${dealerInfo.CUSTOMER_PRICE}"/>
	  </td>
      <td class="table_query_3Col_label_6Letter">索赔价格：</td>
      <td class="table_query_3Col_input">
	  		<c:out value="${dealerInfo.CLAIM_PRICE}"/>
	  </td>
    </tr>
    <tr >
      <td class="table_query_3Col_label_6Letter">备注：</td>
      <td colspan="5" class="table_query_3Col_input">
      		<c:out value="${dealerInfo.REMARK}"/>
      </td>
    </tr>
  </table>
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
	<br>
	 <table class="table_edit">
       <tr >
         <td align="center" >
         	<input type="button" name="BtnNo" value="关闭" class="normal_btn" onClick="_hide();">
         </td>
       </tr>
     </table>
    <br>
</form>
<script type="text/javascript">
	var myPage;

	var url = "<%=contextPath%>/partsmanage/infoSearch/PartInfoSearch/getDealerInfo.json";
				
	var title = null;

	var columns = [
				{header: "供应商代码", dataIndex: 'SUPPLIER_CODE', align:'center'},
				{header: "供应商名称", dataIndex: 'SUPPLIER_NAME', align:'center'},
				{header: "简称", dataIndex: 'SHORT_NAME', align:'center'},
				{header: "联系人", dataIndex: 'LINK_MAN', align:'center'},
				{header: "联系人电话", dataIndex: 'PHONE_NUMBER', align:'center'}
		      ];
		      
</script>
<!--页面列表 end -->
</body>
</html>