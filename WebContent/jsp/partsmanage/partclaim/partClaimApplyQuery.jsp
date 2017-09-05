<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件索赔申请</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
</script>
</head>
<body>
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 配件管理&gt;配件索赔&gt;配件索赔申请
<form name="fm" id="fm">
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
		<td class="table_query_2Col_label_5Letter">签收单号：</td>
      	<td><input name="signNo" type="text" id="signNo" class="middle_txt" datatype="1,is_digit,4"/></td>
      	<td class="table_query_2Col_label_6Letter">采购订单号：</td>
      	<td><input name="orderNo" type="text" id="PART_CODE"  class="middle_txt"/></td>
    </tr>
    <tr>
      <td class="table_query_2Col_label_5Letter">签收日期：</td>
      <td><input name="beginDate" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);">
          &nbsp;至&nbsp;	
          <input name="endDate" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);">			
      </td>
      <td class="table_query_2Col_label_6Letter">货运单号：</td>
      <td><input name="doNo" type="text" id="PART_NAME"  class="middle_txt"/></td>
    </tr>
    <tr>
    	<td></td><td></td><td></td><td></td>
      	<td class="table_query_2Col_label_4Letter">
      		<input type="button" name="BtnQuery" id="queryBtn"  value="查询"  class="normal_btn" onClick="__extQuery__(1)" >
      	</td>
    </tr>
</table>
<!-- 查询条件 end -->
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end -->
</form>
<!--页面列表 begin -->
<script type="text/javascript" >
	var myPage;
	var url = "<%=contextPath%>/partsmanage/partclaim/PartClaimApply/partClaimApplyQuery.json";
				
	var title = null;

	var columns = [
				{header: "签收单号", dataIndex: 'SIGN_NO', align:'center', showsize:30},
				{header: "货运订单号", dataIndex: 'DO_NO', align:'center'},
				{header: "采购订单号", dataIndex: 'ORDER_NO', align:'center', renderer:myLink},
				{header: "运输方式", dataIndex: 'TRANS_TYPE', align:'center',renderer:getItemValue},
				{header: "签收日期", dataIndex: 'SIGN_DATE', align:'center'},
				{header: "签收明细项", dataIndex: 'SIGN_COUNT', align:'center'},
				{id:'action', header: "操作", sortable: false, dataIndex: 'ORDER_NO', renderer:oper, align:'center'}
		      ];
		      
//设置超链接  begin      
	
	//点击采购订单号
	function myLink(value,meta,record) {
  		return String.format("<a href=\"#\" onclick='sel(\""+record.data.ORDER_ID+"\")'>[" + value + "]</a>");
	}
	
	//采购订单详细页面
	function sel(value) {
		OpenHtmlWindow('<%=contextPath%>/partsmanage/purchase/PurchaseOrderSearch/partOrderInfo.do?orderId=' + value,800,500);
	}
	//点击索赔申请
	function oper(value,meta,record) {
		return String.format("<a href=\"#\" onclick='claim(\""+value+"\", \""+record.data.DO_NO+"\", "+record.data.CLAIM_ID+")'>[索赔申请]</a>");
	}
	function claim(orderNo, doNo, claimId) {
		fm.method = 'post';
		claimId = (null == claimId) ? '' : claimId;
		fm.action = '<%=contextPath%>/partsmanage/partclaim/PartClaimApply/partOrderDetail.do?orderNo=' + orderNo + '&doNo=' + doNo + '&claimId=' + claimId;
		fm.submit();
	}
	


	
//设置超链接 end
	
</script>
<!--页面列表 end -->
</body>
</html>