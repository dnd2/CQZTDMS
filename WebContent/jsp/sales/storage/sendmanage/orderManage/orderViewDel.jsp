<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>


<%@ page import="java.util.*"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt" %> 
<%
	String contextPath = request.getContextPath();
%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<title>批售订单明细</title>
</head>
<body onload="countOrderAccountInit();">
	<div class=navigation><img src="<%=request.getContextPath()%>/img/nav.gif">&nbsp;当前位置： 储运管理&gt;发运管理&gt;批售订单查询&gt;明细查看</div>
	<form method="POST" name="fm" id="fm">
     	<input type="hidden" name="yieldId" id="yieldId" value="${orderMap.AREA_ID }" /> 
     	<input type="hidden" name="orderId" id="orderId" value="${orderMap.ORDER_ID }" />
     	<input type="hidden" name="dealerId" id="dealerId" value="${orderMap.DEALER_ID }"/>
     	<input type="hidden" name="finType" id="finType" value="${orderMap.FUND_TYPE_ID }"/>
     	<input type="hidden" name="process_type" id="process_type"  value="${process_tpye }"/>
     <div class="form-panel">
		<h2>信息查看</h2>
		<div class="form-body">
	  <TABLE class=table_query id="moneyTable">
	  	<tr>
	  		<td class="right" width="10%">经销商名称：</td>
			<td width="13%"><span id="orderNo_span"><c:out value="${orderMap.DEALER_NAME}" default="0"></c:out></span></td>
			<td class="right" width="10%">资金类型：</td>
			<td width="13%"><span id="orderNo_span"><c:out value="${orderMap.ACC_TYPE_TXT}" default="0"></c:out></span></td>
			<td class="right" width="10%">发运仓库：</td>
			<td width="13%"><span id="orderType_span">${orderMap.WAREHOUSE_NAME}</span></td>	
		</tr>
		<tr>
			<td class="right">收车联系人：</td>
			<td><span id="dealer_name_span">${orderMap.LINK_MAN}</span></td>
			<td class="right">收车联系电话：</td>
			<td><span id="delivery_span">${orderMap.TEL}</span></td>
			<td class="right">发运方式：</td>
			<td><span id="expectLastDate_span">${orderMap.SEND_WAY_TXT}</span></td>
		<tr>
			<td class="right">收车地址：</td>
			<td colspan="3"><span id="dealer_code_span">${orderMap.ADDRESS}</span></td>
			<td class="right">最晚到货日期：</td>
			<td><span id="expectDate_span"><c:out value="${orderMap.PLAN_DELIVER_DATE}"></c:out></span></td>
		</tr>
	    <tr>
	      <td class="right">备注：</td>
	      <td>
				<span id='yfin_total_span'>${orderMap.REQ_REMARK}</span> 
		  </td>
	      <td class="right"></td>
      	  <td>
      		</td>
	      <td colspan="2">&nbsp;</td>
	    </tr>
	  </TABLE>
	  </div>
	 </div>
   		<TABLE class=table_list style="border-bottom:1px solid #DAE0EE" >  
		    <tr class=cssTable >
		      <th nowrap="nowrap">车系</th>
		      <th nowrap="nowrap">车型</th>
		      <th nowrap="nowrap">配置</th>
		      <th nowrap="nowrap">颜色</th>
		      <th nowrap="nowrap">物料编码</th>
			  <th nowrap="nowrap">本次提报量</th>
		      <th nowrap="nowrap" id="span8">启票价</th>
		     <!-- <th nowrap="nowrap" id="span9">折扣金额</th> --> 
		      <th nowrap="nowrap" id="totalTdTitle">启票金额</th>
		    </tr>
    		<tbody id="tbody1">
    			<c:forEach items="${materialList }" var="list" varStatus="status">
    				<tr class="table_list_row2">
    					<td>
    						${list.SERIES_NAME}
    					</td>
    					<td>${list.MODEL_NAME }</td>
    					<td>${list.PACKAGE_NAME }</td>
    					<td>${list.COLOR_NAME }</td>
    					<td>${list.MATERIAL_CODE}</td>
    					<td>${list.REQ_AMOUNT}</td>
    					<td>
    						${list.DISCOUNT_S_PRICE}
    					</td>
    					<!-- 
    					<td>
    						${list.DISCOUNT_PRICE}
    					</td>
    					 -->
    					<td>
    						${list.TOTAL_PRICE}
    					</td>
    				</tr>
    			</c:forEach>
    		</tbody>
  		</table>	
</form>
</body>
</html>
