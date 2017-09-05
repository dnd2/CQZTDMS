<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<title>装箱单管理 > 查看 > 明细查看</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript">
//获取选择框的值
function getCode(value){
	var str = getItemValue(value);
	document.write(str);
}
//获取序号
function getIdx(id){
	document.write(document.getElementById(id).rows.length-1);
}
//返回
function goBack(id){
	window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPickOrder/partPickOrderDetail.do?pickOrderId="+id;
}
</script>
</head>
<body>
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置:配件管理 > 配件销售管理 > 装箱单管理  > 查看 > 明细查看</div>
  		<div class="form-panel">
            <h2><img class="panel-icon nav" src="<%=contextPath%>/img/subNav.gif"/>销售单信息</h2>
            <div class="form-body">
			  <table class="table_query"  cellSpacing=1 cellPadding=1 width="100%">
			  <tr>
			      <td  class="right">销售单号:</td>
			      <td >${mainMap.SO_CODE}</td>
			      <td  class="right">销售日期:</td>
			      <td >${mainMap.CREATE_DATE_FM}</td>
			      <td  class="right">销售制单人:</td>
			      <td >${mainMap.CREATE_BY_NAME}</td>
			    </tr>
			    <tr>
			      <td class="right">销售单位:</td>
			      <td >${mainMap.SELLER_NAME}</td>
			      <td class="right">订货单位:</td>
			      <td >${mainMap.DEALER_NAME}</td>
			      <td class="right">订货制单人:</td>
			      <td >${mainMap.BUYER_NAME}</td>
			    </tr>
			    <tr>
			      <td class="right">出库仓库:</td>
				  <td >${mainMap.WH_NAME}</td>
			      <td class="right">接收单位:</td>
			      <td >${mainMap.CONSIGNEES}</td>
			      <td class="right">接收地址:</td>
			      <td >${mainMap.ADDR}</td>
			    </tr>
			    <tr>
			      <td class="right"> 接收人:</td>
			      <td  >${mainMap.RECEIVER}</td>
			      <td class="right"><span> 接收人电话:</span></td>
			      <td  >${mainMap.TEL}</td>
			      <td class="right">邮政编码:</td>
			      <td >${mainMap.POST_CODE}</td>
			    </tr>
			    <tr>
			      <td class="right">到站名称:</td>
			      <td  >${mainMap.STATION}</td>
			      <td class="right">发运方式:</td>
			      <td >${mainMap.TRANS_TYPE1}
			      <td class="right">付款方式:</td>
			      <td >
					<script type="text/javascript">getCode('${mainMap.PAY_TYPE}');</script>
				  </td>
			      </td>
			    </tr>
			    <tr>
			      <td class="right">订单类型:</td>
			      <td >
			      	<script type="text/javascript">
							getCode('${mainMap.ORDER_TYPE}');
					</script>
			      </td>
			      <td class="right">总金额:</td>
			      <td >${mainMap.CONVERS_AMOUNT}</td>
			      <td class="right">运费支付方式:</td>
			      <td >
			      	<script type="text/javascript">
							getCode(${mainMap.TRANSPAY_TYPE});
					</script>
				  </td>
			    </tr>
			  <tr>
			    <td class="right">备注:</td>
			    <td colspan="5">${mainMap.REMARK}</td>
			  </tr>
			</table>
		</div>
	</div>	
  <table id="file" class="table_list">
     <caption><img class="panel-icon nav" src="<%=contextPath%>/img/subNav.gif"/>配件信息</caption>
    <tr>
      <th>序号</th>
      <th>配件编码</th>
      <th>配件名称</th>
      <th>件号</th>
      <th>最小包装量 </th>
      <th>单位 </th>
      <th>采购数量</th>
      <th>销售数量</th>
      <th>销售单价</th>
      <th>销售金额</th>
      <th>当前库存</th>
      <th>备注</th>
    </tr>
     <c:forEach items="${detailList}" var="data" >
     	<tr class="table_list_row1">
   			  <td class="center">&nbsp;
   			  	<script type="text/javascript">getIdx("file");</script>
   			  </td>
		      <td align="left"><c:out value="${data.PART_OLDCODE}" /></td>
		      <td align="left"><c:out value="${data.PART_CNAME}" /></td>
             <td align="left"><c:out value="${data.PART_CODE}" /></td>
		      <td>&nbsp;<c:out value="${data.MIN_PACKAGE}" /></td>
		      <td>&nbsp;<c:out value="${data.UNIT}" /></td>
		      <td>&nbsp;<c:out value="${data.BUY_QTY}" /></td>
		      <td>&nbsp;<c:out value="${data.SALES_QTY}" /></td>
		      <td>&nbsp;<c:out value="${data.CONVERS_PRICE}" /></td>
		      <td>&nbsp;<c:out value="${data.CONVERS_AMOUNT}" /></td>
		      <td>&nbsp;<c:out value="${data.STOCK_QTY}" /></td>
		      <td>&nbsp;<c:out value="${data.REMARK}" /></td>
		 </tr>
	</c:forEach>
  </table>
<table border="0" class="table_query">
  <tr>
  <td class="center"><input class="u-button" type="button" value="返 回" onclick="goBack('${mainMap.PICK_ORDER_ID}')"/></td>
  </tr>
  </table>
</div>
</body>
</html>

