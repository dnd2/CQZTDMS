<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
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
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript">
	//获取选择框的值
	function getCode(value){
		var str = getItemValue(value);
		document.write(str);
	}
	//获取序号
	function getIdx(id){
		document.write(document.getElementById(id).rows.length-2);
	}
	//获取序号
	function getIdx1(){
		document.write(document.getElementById("file1").rows.length-2);
	}
	//返回
	function goBack(){
		var orderType=document.getElementById("orderType").value;
		if(orderType==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_02%>){
			window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrder/partDlrOrderInit.do';
		}else if(orderType==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_01%>){
			window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrder/partEmergencyDlrOrderInit.do';
		}else if(orderType==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_03%>){
			window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrder/partPlanDlrOrderInit.do';
		}else if(orderType==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04%>){
			window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrder/partDirectDlrOrderInit.do';
		}else if(orderType==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_07%>){
			window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrder/partMarketDlrOrderInit.do';
		}else if(orderType==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_08%>){
			window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrder/partDiscountDlrOrderInit.do';
		}
	}
</script>
</head>

<body >
<div class="wbox">
<input type="hidden" name = "orderType" id="orderType" value="${mainMap.ORDER_TYPE}" />
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：
		配件管理 > 配件采购管理  &gt;配件订单提报>采购订单查看</div>
  <table class="table_add" border="0"
    style="line-height:20px; #859aff 1px solid; BORDER-TOP:#859aff 1px solid; BORDER-LEFT:#859aff 1px solid; BORDER-BOTTOM:#859aff 1px solid;border-color: #859aff;"
    cellSpacing=1 cellPadding=1 width="100%">
    <tr>
    <th colspan="6" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 采购订单信息</th>
  </tr>
    <tr>
      <td class="table_add_3Col_label_6Letter" nowrap>订单号：</td>
      <td >${mainMap.ORDER_CODE}</td>
      <td  class="table_add_3Col_label_6Letter">订货单位：</td>
      <td >${mainMap.DEALER_NAME}</td>
      <td  class="table_add_3Col_label_6Letter"  >制单人：</td>
      <td >${mainMap.NAME}</td>
    </tr>
    <tr>
      <td class="table_add_3Col_label_6Letter" >制单日期：</td>
      <td >${mainMap.CREATE_DATE}</td>
      <td class="table_add_3Col_label_6Letter">销售单位：</td>
      <td>${mainMap.SELLER_NAME}</td>
      <td class="table_add_3Col_label_6Letter"  >接收单位：</td>
      <td >${mainMap.RCV_ORG}</td>
    </tr>
    <tr>
      <td class="table_add_3Col_label_6Letter" >接收地址：</td>
      <td >${mainMap.ADDR}</td>
      <td class="table_add_3Col_label_6Letter">接收人：</td>
      <td>${mainMap.RECEIVER}</td>
      <td class="table_add_3Col_label_6Letter" >接收人电话：</td>
      <td >${mainMap.TEL}</td>
    </tr>
    <tr>
          <td class="table_add_3Col_label_6Letter" >接收人手机：</td>
      <td >${mainMap.MOBILE_PHONE}</td>
      <td class="table_add_3Col_label_6Letter" >邮政编码：</td>
      <td >${mainMap.POST_CODE}</td>
      <td class="table_add_3Col_label_6Letter">到站名称：</td>
      <td>${mainMap.STATION}</td>
      <td class="table_add_3Col_label_6Letter"  >发运方式：</td>
      <td >
      	${mainMap.TRANS_TYPE}
	  </td>
    </tr>
    <tr>
          <td class="table_add_3Col_label_6Letter"  >发运方式：</td>
      <td >
      	${mainMap.TRANS_TYPE}
	  </td>
      <td class="table_add_3Col_label_6Letter" >付款方式：</td>
      <td >
		<script type="text/javascript">
				getCode(${mainMap.PAY_TYPE});;
	  	</script>
	  </td>
      <td class="table_add_3Col_label_6Letter"  >订单类型：</td>
      <td >
      	<script type="text/javascript">
				getCode(${mainMap.ORDER_TYPE});;
	  	</script>
      </td>

    </tr>
  <tr>
        <td class="table_add_3Col_label_6Letter"  >总金额：</td>
      <td >${mainMap.ORDER_AMOUNT}<font color="blue"> 元</font></td>
    <td class="table_add_3Col_label_6Letter" >备注：</td>
    <td colspan="5">${mainMap.REMARK}</td>
  </tr>
</table>
<table id="file1" class="table_list" style="border-bottom:1px solid #DAE0EE">
    <tr>
      <th colspan="12" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />配件信息        </th>
    </tr>
    <tr bgcolor="#FFFFCC" >
      <td>序号</td>
      <td>配件编码</td>
      <td>配件名称</td>
      <!-- <td>件号</td>-->
      <td>最小包装量 </td>
      <td>订货数量</td>
      <td>单位 </td>
      <td>订货单价<font color="blue">(元)</font></td>
      <td>订货金额<font color="blue">(元)</font></td>
      <td>上级是否有库存</td>
      <td>当前库存</td>
      <td>备注</td>
    </tr>
    <c:forEach items="${detailList}" var="data" >
     	<tr class="table_list_row1">
   			  <td align="center">&nbsp;
   			  	<script type="text/javascript">
       				getIdx("file1");
				</script>
   			  </td>
   			  <td align="center"><c:out value="${data.PART_OLDCODE}" /></td>
		      <td align="center"><c:out value="${data.PART_CNAME}" /></td>
		      <td align="center"><c:out value="${data.PART_CODE}" /></td>
		      <td>&nbsp;<c:out value="${data.MIN_PACKAGE}" /></td>
		      <td>&nbsp;<c:out value="${data.BUY_QTY}" /></td>
		      <td>&nbsp;<c:out value="${data.UNIT}" /></td>
		      <td>&nbsp;<c:out value="${data.BUY_PRICE}" /></td>
		      <td>&nbsp;<c:out value="${data.BUY_AMOUNT}" /></td>
		      <td>&nbsp;
		      	<script type="text/javascript">
					getCode(${data.IS_HAVA});
	  			</script>
		      </td>
		      <td>&nbsp;<c:out value="${data.STOCK_QTY}" /></td>
		      <td>&nbsp;<c:out value="${data.REMARK}" /></td>
		 </tr>
	</c:forEach>
  </table>
<table id="file2" class="table_list" style="border-bottom:1px solid #DAE0EE">
    <tr>
      <th colspan="6" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif" />单据操作信息        </th>
    </tr>
    <tr bgcolor="#FFFFCC">
      <td align="center" width="3%">序号</td>
      <td align="center" width="10%">操作人</td>
      <td align="center" width="13%">操作时间</td>
      <td align="center" width="14%">环节</td>
      <td align="center" width="8%">状态</td>
     
    </tr>
    <c:forEach items="${historyList}" var="data" >
     	<tr class="table_list_row1">
   			  <td align="center">&nbsp;
   			  	<script type="text/javascript">
       				getIdx("file2");
				</script>
   			  </td>
   			  <td align="center"><c:out value="${data.OPT_NAME}" /></td>
		      <td align="center"><c:out value="${data.OPT_DATE}" /></td>
		      <td align="center"><c:out value="${data.WHAT}" /></td>
		      <td>&nbsp;
				<script type="text/javascript">
					getCode(${data.STATUS});
	  			</script>
			  </td>
		</tr>
	</c:forEach>
  </table>
<table border="0" class="table_query">
  <tr>
  <td  class="center"><input class="normal_btn" type="button" value="返 回" onclick="goBack()"/></td>
  </tr>
</table>
</div>
</body>
</html>

