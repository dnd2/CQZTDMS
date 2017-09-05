<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<title>配件出库单查询-查看</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript">
	//获取选择框的值
	function getCode(value){
		var str = getItemValue(value);
		document.write(str);
	}
	//获取序号
	function getIdx(){
		document.write(document.getElementById("file").rows.length-2);
	}
	//获取序号
	function getIdx1(){
		document.write(document.getElementById("file1").rows.length-2);
	}
	//返回
	function goBack(){
		window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartOutstock/partOutstockInit.do';
	}
</script>
</head>
<body>
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置:配件管理  &gt; 配件销售管理  &gt;配件出库单查询  &gt;查看</div>
  <div class="form-panel">
     <h2><img class="panel-icon nav" src="<%=contextPath%>/img/subNav.gif"/>销售单信息</h2>
     <div class="form-body">
	  <table class="table_query">
	    <tr>
	      <td width="13%" class="right">销售单号:</td>
	      <td width="20%" >${mainMap.SO_CODE}</td>
	      <td width="13%" class="right">销售日期:</td>
	      <td width="20%">${mainMap.SALE_DATE1}</td>
	      <td width="13%" class="right">销售制单人:</td>
	      <td width="20%">${mainMap.CREATE_BY_NAME}</td>
	    </tr>
	    <tr>
	      <td  class="right">销售单位:</td>
	      <td>${mainMap.SELLER_NAME}</td>
	      <td  class="right">订货单位:</td>
	      <td>${mainMap.DEALER_NAME}</td>
	      <td  class="right">订货制单人:</td>
	      <td >${mainMap.orderCreateBy}</td>
	    </tr>
	    <tr>
	      <td  class="right">订货日期:</td>
	      <td>${mainMap.orderCreateDate}</td>
	      <td  class="right">出库仓库:</td>
	      <td >${mainMap.WH_NAME}</td>
	      <td  class="right">接收单位:</td>
	      <td>${mainMap.CONSIGNEES}</td>
	    </tr>
	    <tr>
	      <td  class="right">接收地址:</td>
	      <td>${mainMap.ADDR}</td>
	      <td  class="right">接收人:</td>
	      <td>${mainMap.RECEIVER}</td>
	      <td  class="right"><span>接收人电话:</span></td>
	      <td>${mainMap.TEL}</td>
	    </tr>
	    <tr>
	      <td  class="right">邮政编码:</td>
	      <td>${mainMap.POST_CODE}</td>
	      <td class="right">到站名称:</td>
	      <td>${mainMap.STATION1}</td>
	      <td class="right">发运方式:</td>
	      <td>${mainMap.TRANS_TYPE1} </td>
	    </tr>
	    <tr>
	      <td  class="right">付款方式:</td>
	      <td>
		  	<script type="text/javascript">getCode(${mainMap.PAY_TYPE});</script>
		  </td>
	      <td  class="right">订单类型:</td>
	      <td>
	      	<script type="text/javascript">getCode(${mainMap.ORDER_TYPE});</script>
	      </td>
	      <td  class="right">销售总金额:</td>
	      <td>${mainMap.AMOUNT}</td>
	    </tr>
	    <tr>
	      <td class="right">运费支付方式:</td>
	      <td>
	      	<script type="text/javascript">getCode(${mainMap.TRANSPAY_TYPE1});</script>
	      </td>
	      <td  class="right">配件金额:</td>
	        <td><script type="text/javascript">document.write((${mainMap.AMOUNT}-${mainMap.FREIGHT}).toFixed(2));</script> 
	        </td>
	        <td  class="right">运费金额:</td>
	        <td>${mainMap.FREIGHT}</td>
	    </tr>
	  <tr>
	    <td class="right">备注:</td>
	    <td colspan="5">${mainMap.REMARK}</td>
	  </tr>
	</table>
	</div>
  </div>	
  <table id="file" class="table_list" >
    <tr>
      <th colspan="14" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />配件信息</th>
    </tr>
    <tr >
      <td class="right">序号</td>
      <td class="right">配件编码</td>
      <td class="right">配件名称</td>
      <td class="right">件号</td>
      <td class="right">最小包装量</td>
      <td class="right">单位</td>
      <td class="right">采购数量</td>
      <td class="right">销售数量</td>
      <td class="right">出库货位</td>
      <td class="right">出库批次</td>
      <td class="right">出库数量</td>
      <td class="right">销售单价</td>
      <td class="right">销售金额</td>
     <%-- <td class="right">当前库存</td>--%>
      <td class="right">备注</td>
    </tr>
    <c:forEach items="${detailList}" var="data" >
     	<tr class="table_list_row1">
   			  <td class="right">&nbsp;
   			  	<script type="text/javascript">getIdx();</script>
   			  </td>
		      <td class="right"><c:out value="${data.PART_OLDCODE}" /></td>
		      <td class="right"><c:out value="${data.PART_CNAME}" /></td>
              <td class="right"><c:out value="${data.PART_CODE}" /></td>
		      <td>&nbsp;<c:out value="${data.MIN_PACKAGE}" /></td>
		      <td>&nbsp;<c:out value="${data.UNIT}" /></td>
		      <td>&nbsp;<c:out value="${data.BUY_QTY1}" /></td>
              <td>&nbsp;<c:out value="${data.SALES_QTY}" /></td>
		      <td> <c:out value="${data.LOC_NAME}" /></td>
		      <td> <c:out value="${data.BATCH_NO}" /></td>
		      <td>&nbsp;<c:out value="${data.OUTSTOCK_QTY}" /></td>
		      <td>&nbsp;<c:out value="${data.SALE_PRICE}" /></td>
		      <td>&nbsp;<c:out value="${data.SALE_AMOUNT}" /></td>
		     <%-- <td>&nbsp;<c:out value="${data.STOCK_QTY}" /></td>--%>
		      <td>&nbsp;<c:out value="${data.REMARK}" /></td>
		 </tr>
	</c:forEach>
  </table>
  <table id="file1" class="table_list" >
    <tr>
      <th colspan="6" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />单据操作信息        </th>
    </tr>
    <tr>
      <td class="right" width="3%">序号</td>
      <td class="right" width="10%">操作人</td>
      <td class="right" width="13%">操作时间</td>
      <td class="right" width="14%">环节</td>
      <td class="right" width="8%">状态</td>
    </tr>
    <c:forEach items="${historyList}" var="data" >
     	<tr class="table_list_row1">
   			  <td class="right">&nbsp;
   			  	<script type="text/javascript">getIdx1();</script>
   			  </td>
   			  <td class="right"><c:out value="${data.OPT_NAME}" /></td>
		      <td class="right"><c:out value="${data.OPT_DATE}" /></td>
		      <td class="right"><c:out value="${data.WHAT}" /></td>
		      <td>&nbsp;
				<script type="text/javascript">getCode(${data.STATUS});</script>
			  </td>
		</tr>
	</c:forEach>
  </table>
	<table border="0" class="table_query">
  		<tr>
     	<c:choose>
          <c:when test="${flag == 1}">
              <td  class="center"><input class="u-button" type="button" value="关 闭" onclick="_hide();"/></td>
          </c:when>
          <c:otherwise>
              <td  class=""center""><input class="u-button" type="button" value="返 回1" onclick="history.back();"/></td>
          </c:otherwise>
	    </c:choose>
  	</tr>
  </table>
</div>
</body>
</html>