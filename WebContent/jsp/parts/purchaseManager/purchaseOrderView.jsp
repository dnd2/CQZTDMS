<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>采购订单-明细查看</title>
</head>
<body onunload='javascript:destoryPrototype()' onload="__extQuery__(1);loadcalendar();">
<div class="wbox">
	<div class="navigation"> <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 配件管理&gt;采购计划管理&gt;采购订单&gt;查看</div>
<form name="fm" id="fm" method="post" >
<input type="hidden" name="orderId" value="${mainInfo.ORDER_ID}"/>
<input type="hidden" id="backflag" name="backflag" value="${backflag}"/>
	<table class="table_query">
    <tr>
      <td bgcolor="#F3F4F8"   align="right">订单单号:</td>
      <td bgcolor="#FFFFFF" align="left" width="24%">&nbsp;<c:out value="${mainInfo.ORDER_CODE}" />
      <input type="hidden" name="orderCode" value="${mainInfo.ORDER_CODE}"/>
      </td>
<!--       <td   align="right" bgcolor="#F3F4F8">采购员:</td> -->
<%--       <td align="left" bgcolor="#FFFFFF" width="24%">&nbsp;<c:out value="${mainInfo.BUYER}" /><br /> --%>
<%--       <input type="hidden" name="BUYER_ID" value="${mainInfo.BUYER_ID}"/> --%>
<%--       <input type="hidden" name="BUYER" value="${mainInfo.BUYER}"/> --%>
<!--       </td> -->
      <td   align="right" bgcolor="#F3F4F8">制单日期:</td>
      <td align="left" bgcolor="#FFFFFF" width="24%">&nbsp;<c:out value="${mainInfo.CREATE_DATE}" /></td>
    </tr>
<!--     <tr> -->
<!--       <td   align="right" bgcolor="#F3F4F8">计划类型:</td> -->
<!--       <td align="left" bgcolor="#FFFFFF">&nbsp; -->
<!--       	<script type="text/javascript"> -->
<%--        			genSelBoxExp("PLAN_TYPE",<%=Constant.PART_PURCHASE_PLAN_TYPE%>,${mainInfo.PLAN_TYPE},true,"short_sel","disabled='disabled'","false",''); --%>
<!-- 		</script> -->
<%-- 		<input type="hidden" name="PLAN_TYPE1" value="${mainInfo.PLAN_TYPE}"/> --%>
<!--       </td> -->
<!--       <td   align="right" bgcolor="#F3F4F8">库房:</td> -->
<%--       <td align="left" bgcolor="#FFFFFF" width="21%">&nbsp;<c:out value="${mainInfo.WH_NAME}" /> --%>
<%--       <input type="hidden" name="WH_ID" value="${mainInfo.WH_ID}"/> --%>
<%--       <input type="hidden" name="WH_NAME" value="${mainInfo.WH_NAME}"/> --%>
<!--       <br /> -->
<!--       </td> -->
<!--     </tr> -->
    <tr>
      <td bgcolor="#F3F4F8"   align="right">总数量:</td>
      <td bgcolor="#FFFFFF" align="left">&nbsp;<input readonly class="phone_txt" type="text" style="border:0px;background-color:#F3F4F8;" value="${mainInfo.SUM_QTY}"  name="SUM_QTY" id="SUM_QTY" /></td>
      <td bgcolor="#F3F4F8"   align="right">总金额:</td>
      <td bgcolor="#FFFFFF" align="left">&nbsp;<input readonly class="phone_txt" type="text" style="border:0px;background-color:#F3F4F8;" value="${mainInfo.AMOUNT}"  name="AMOUNT" id="AMOUNT" /></td>
      <td bgcolor="#F3F4F8"   align="right"></td>
      <td bgcolor="#FFFFFF" align="left"></td>
    </tr>
<!--     <tr> -->
<!--       <td   align="right" bgcolor="#F3F4F8">备注:</td> -->
<%--       <td align="left" bgcolor="#FFFFFF" colspan="5">&nbsp;<c:out value="${mainInfo.REMARK}" /></td> --%>
<!--     </tr> -->
</table>
    <table class="table_query">
<th colspan="4" width="100%"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" />配件信息</th>
    <tr>
       <td align="right">  配件编码：</td>
       <td align="left" >
           <input class="middle_txt" id="PART_OLDCODE"
                  datatype="1,is_noquotation,30" name="PART_OLDCODE"
                  type="text"/>
       </td>
       <td  align="right">  配件名称：</td>
       <td align="left">
           <input class="middle_txt" id="PART_CNAME"
                  datatype="1,is_noquotation,30" name="PART_CNAME"
                  type="text"/>
       </td>
    </tr>
    <tr>
		<td align="center" colspan="4">
			<input class="normal_btn" type="button" name="BtnQuery"
				id="queryBtn" value="查 询" onclick="__extQuery__(1)" />
			</td>
	</tr>
    </table>
  <jsp:include page="${contextPath}/queryPage/orderHidden.html" />
  <jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<table border="0" class="table_query">
  <tr align="center">
  <td>
  <input class="normal_btn" type="button" value="返 回" onclick="javascript:goback();"/>&nbsp;</td>
  </tr>
  </table>
</form>
<script type="text/javascript" ><!--
var myPage;

var url = "<%=contextPath%>/parts/purchaseManager/PurchaseOrderManager/queryPurchaseOrderDetail1.json";

var title = null;

var columns = [
				{header: "序号", align:'center',renderer:getIndex},
				{header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
				{header: "配件名称", dataIndex: 'PART_CNAME',style: 'text-align:left'},
                {header: "件号", dataIndex: 'PART_CODE', style: 'text-align:left'},
				{header: "单位", dataIndex: 'UNIT', align:'center'},
				{header: "供应商名称", dataIndex: 'VENDER_NAME', align:'center'},
// 				{header: "制造商名称", dataIndex: 'MAKER_NAME', align:'center'},
// 				{header: "库管员", dataIndex: 'WHMAN_NAME', align:'center'},
				{header: "计划数量", dataIndex: 'PLAN_QTY', align:'center'},
				{header: "采购数量", dataIndex: 'BUY_QTY', align:'center'},
				{header: "金额", dataIndex: 'BUY_AMOUNT', align:'center'},
				{header: "已生成数量", dataIndex: 'CHECK_QTY', align:'center'},
// 				{header: "备注", dataIndex: 'REMARK', align:'center'}
		      ];

//返回查询页面
function goback(){
	window.location.href = '<%=contextPath%>/parts/purchaseManager/PurchaseOrderManager/purchaseOrderQueryInit.do?flag=true';
}
-->
</script>
</div>
</body>
</html>
