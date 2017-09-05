<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- created by lishuai103@yahoo.com.cn 20100603 配件采购订单明细 -->
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
	String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件采购订单明细</title>
</head>
<body>
<div class="navigation">
  <img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 配件管理 &gt; 经销商配件采购 &gt; 配件采购订单明细</div>
 <table class="table_edit">
 	<tr >
   		<th colspan="19" align="left"><img src="<%=request.getContextPath()%>/img/subNav.gif" alt="" class="nav" /> 基本信息</th>
    </tr>
    <tr >
     <td class="table_query_3Col_label_6Letter">订单编号：</td>
     <td class="table_query_3Col_input">
     	<c:out value="${orderInfo.ORDER_NO}"/>
     </td>
     <td class="table_query_3Col_label_6Letter">经销商代码：</td>
     <td class="table_query_3Col_input">
	 	<c:out value="${orderInfo.DEALER_CODE}"/>
	 </td>
     <td class="table_query_3Col_label_6Letter">经销商名称：</td>
     <td class="table_query_3Col_input">
     	<c:out value="${orderInfo.DEALER_NAME}"/>
     </td>
   </tr>
   <tr >
     <td class="table_query_3Col_label_6Letter">运输方式：</td>
     <td align="left" >
     	<script type="text/javascript">
     		writeItemValue('<c:out value="${orderInfo.TRANS_TYPE}"/>');
     	</script>
     </td>
     <td class="table_query_3Col_label_6Letter">订货金额：</td>
     <td class="table_query_3Col_input">
     	<c:out value="${orderInfo.ORDER_PRICE}"/>
     </td>
     <td class="table_query_3Col_label_6Letter">订单状态：</td>
     <td class="table_query_3Col_input">
     	<script type="text/javascript">
     		writeItemValue('<c:out value="${orderInfo.ORDER_STATUS}"/>');
     	</script>
     </td>
   </tr>
   <tr >
     <td class="table_query_3Col_label_6Letter">销售单号：</td>
     <td class="table_query_3Col_input">
	    <c:out value="${orderInfo.SO_NO}"/>
	 </td>
     <td class="table_query_3Col_label_6Letter"><span >销售金额：</span></td>
     <td class="table_query_3Col_input">
     	<c:out value="${orderInfo.SALES_SUM}"/>
     </td>
     <td align="right">&nbsp;</td>
     <td align="left">&nbsp;</td>
   </tr>
   <tr >
     <td class="table_query_3Col_label_6Letter"><span >备注：</span></td>
     <td colspan="5" align="left" >
     	<c:out value="${orderInfo.REMARK}"/>
     </td>
    </tr>
 </table>

  	  <table class="table_list" style="border-bottom:1px solid #DAE0EE">
  	  <tr>
          <th colspan="19" align="left"><img src="<%=request.getContextPath()%>/img/subNav.gif" alt="" class="nav" /> 配件信息</th>
       </tr>   
       <tr class="table_list_th">
          <th>配件号</th>
          <th>配件名称</th>
          <th>单位</th>
          <th>订货数量</th>
          <th>销售数量</th>
          <th>货运数量</th>
          <th>签收数量</th>
          <th>销售价</th>
          <th>折扣</th>
        </tr>   
         <c:forEach items="${partInfo}" var="pf">
       		<tr class="table_list_row1">
            	<td class=""><c:out value="${pf.PART_CODE}"/></td>
            	<td class=""><c:out value="${pf.PART_NAME}"/></td>
            	<td class=""><c:out value="${pf.UNIT}"/></td>
           	 	<td class=""><c:out value="${pf.ORDER_COUNT}"/></td>
           	 	<td class=""><c:out value="${pf.ONFITTING_COUNT}"/></td>
            	<td class=""><c:out value="${pf.CARRYING_COUNT}"/></td>
            	<td class=""><c:out value="${pf.RECEIVED_COUNT}"/></td>
           	 	<td class=""><c:out value="${pf.SALE_PRICE}"/></td>
           	 	<td class=""><c:out value="${pf.DISCOUNT_RATE}"/></td>
        	</tr>
       	</c:forEach> 
    </table>
<br>
<table class="table_list" style="border-bottom:1px solid #DAE0EE">
	<tr>
  		<th colspan="19" align="left"><img src="<%=request.getContextPath()%>/img/subNav.gif" alt="" class="nav" /> 订单流转记录</th>
 	</tr>   
 	<tr class="table_list_th">
 		<th>序号</th>
        <th>操作时间</th>
        <th>状态</th>
        <th>操作部门</th>
 	</tr>
 	 <c:forEach items="${orderLog}" var="og">
       		<tr class="table_list_row1">
            	<td class=""><c:out value="${og.NUM}"/></td>
            	<td class=""><c:out value="${og.OPERTATE_DATE}"/></td>
            	<td class="">
            		<script type="text/javascript">
     					writeItemValue('<c:out value="${og.NODE_STATUS}"/>');
     				</script>
            	</td>
           	 	<td class=""><c:out value="${og.ORG_NAME}"/></td>
        	</tr>
       	</c:forEach> 
</table>
<br>
      <table class="table_edit">
        <tr>
          <td width="37%"  align="center" >
          	  <input type="button" name="BtnNo222" value="关闭" class="normal_btn" onclick="_hide()"></td>
        </tr>
      </table>
</body>
</html>