<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt" %>
<%@page import="java.util.Map"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>开票通知单查询</title>
	</head>
<body >
<form method="post" name="fm" id="fm">
<input type="hidden" name="id" value="<c:out value="${map.ID}"/>"/>
	<table width="100%">
	    <tr>
		    <td>
				<div class="navigation">
			    	<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：售后服务管理&gt;索赔结算管理&gt;开票通知单查询
			    </div>
		    </td>
	    </tr>
	</table>
	<table class="table_edit">
		<tr>
			<td align="right" nowrap="nowrap">单据编号：</td>
			<td align="left" nowrap="nowrap">
			   <c:out value="${map.BALANCE_NO}"/>
			</td>
			<td align="right" nowrap="nowrap">制单人姓名：</td>
			<td align="left" nowrap="nowrap">
				<c:out value="${map.NAME}"/>
			</td>
			<td align="right" nowrap="nowrap">制单日期：</td>
			<td align="left" nowrap="nowrap">
				<c:out value="${map.UPDATE_DATE}"/>
			</td>
		</tr>
		<tr>
			<!-- <td align="right" nowrap="nowrap">制单单位：</td>
			<td align="left" nowrap="nowrap">
			 <c:out value="${map.INVOICE_MAKER}"/>
			</td>
			 -->
			<td align="right" nowrap="nowrap">产地：</td>
			<td>
				<script type="text/javascript">
					document.write(getItemValue('<c:out value="${map.YIELDLY}"/>'));
				</script>
			</td>
			<td align="right" nowrap="nowrap">维修站编码：</td>
			<td align="left" nowrap="nowrap">
				<c:out value="${map.DEALER_CODE}"/>
			</td>
			<td align="right" nowrap="nowrap">维修站名称：</td>
			<td align="left" nowrap="nowrap">
				<c:out value="${map.DEALER_NAME1}"/>
			</td>
		</tr>
		<tr>
			<td align="right" nowrap="nowrap">总计(元)：</td>
			<td align="left" nowrap="nowrap">
			   <c:out value="${map.BALANCEAMOUNT}"/>
			</td>
			<td align="right" nowrap="nowrap">财务扣款(元)：</td>
			<td align="left" nowrap="nowrap">
				<c:out value="${map.FINANCIALDEDUCT}"/>
			</td>
			<td align="right" nowrap="nowrap">开票金额(元)：</td>
			<td align="left" nowrap="nowrap">
				${map.NOTEAMOUNT}
			</td>
		</tr>
		<tr>
			<td align="right" nowrap="nowrap">转行政扣款(元)：</td>
			<td align="left" nowrap="nowrap">
				<script>
				  if(parseFloat(${map.NOTEAMOUNT})>0)
					  document.write(0);
				  else
					  document.write(Math.abs(${map.NOTEAMOUNT}));
				</script>
			</td>
			<td align="right" nowrap="nowrap">结算起日期：</td>
			<td align="left" nowrap="nowrap">
			   <fmt:formatDate value='${map.START_DATE}' pattern='yyyy-MM-dd'/>
			</td>
			<td align="right" nowrap="nowrap">结算止日期：</td>
			<td align="left" nowrap="nowrap">
				<fmt:formatDate value='${map.END_DATE}' pattern='yyyy-MM-dd'/>
			</td>
		</tr>
		<tr>
			<td align="right" nowrap="nowrap">财务备注：</td>
			<td align="left" nowrap="nowrap" colspan="3">
				<c:out value="${map.FUNANCIAL_REMARK}"/>
			</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
	</table>
	<br/>
	<table class="table_list">
	<tr class="table_list_th">
			    <th  >行号</th>
			    <th  >车系</th>
			    <th  >售前工时费</th>
			    <th  >售前材料费</th>
			    <th  >售后工时费</th>
			    <th  >售后材料费</th>
			    <th  >保养数</th>
			    <th  >保养费</th>
			    <th  >保养工时费</th>
			    <th  >保养材料费</th>
			    <th  >服务活动次数</th>
			    <th  >服务活动费用</th>
			    <th  >售前索赔单数</th>
			    <th  >售后索赔单数</th>
			    <th  >费用合计</th>
		</tr>

	
	<c:set var="pageSize"  value="20" />
  <c:forEach var="dList" items="${detail}" varStatus="status">
	<tr class="table_list_row${status.index%2+1}">
	    		<td >${status.index+1 }</td>
			    <td  >${dList.SERIES_NAME}</td>
			    <td  >${dList.BEFORE_LABOUR_AMOUNT}</td>
			    <td  width="30px%">${dList.BEFORE_PART_AMOUNT} </td>
			    <td  >${dList.AFTER_LABOUR_AMOUNT}</td>
			    <td  >${dList.AFTER_PART_AMOUNT}</td>
			    <td  >${dList.FREE_CLAIM_COUNT}</td>
			    <td  >${dList.FREE_CLAIM_AMOUNT}</td>
			    <td  >${dList.FREE_LABOUR_AMOUNT}</td>
			    <td  >${dList.FREE_PART_AMOUNT}</td>
			    <td  >${dList.SERVICE_CLAIM_COUNT}</td>
			    <td  >${dList.SERVICE_FIXED_AMOUNT}</td>
			    <td  >${dList.BEFORE_CLAIM_COUNT}</td>
			    <td  >${dList.AFTER_CLAIM_COUNT}</td>
			    <td  >${dList.TOTAL_AMOUNT }</td>
	 </tr>
  </c:forEach>
	
	</table>
	<br/>
	<table class="table_list">
	<tr class="table_list_th">
			    <th  >行号</th>
			    <th  >审核人</th>
			    <th  >审核时间</th>
			    <th  >状态</th>
			    <th  >备注</th>
		</tr>

	
		<c:forEach var="dList" items="${detail1}" varStatus="status">
		<tr class="table_list_row${status.index%2+1}">
	    		<td >${status.index+1 }</td>
			    <td  >${dList.STOP_BY}</td>
			    <td  >${dList.STOP_DATE}</td>
			    <td  >${dList.STATUS}</td>
			    <td  width="30px%">${dList.REMARK} </td>
	 	</tr>
  		</c:forEach>
	
	</table>
	<table class="table_edit">
		<tr>
			<td colspan="6" align="center">
				<%
					Map map = (Map)request.getAttribute("map");
					String flag = String.valueOf(request.getAttribute("infoFlag"));
					if(String.valueOf(map.get("STATUS")).equals(Constant.ACC_STATUS_04)&&flag.equals("1")){
				%>
				<input type="hidden" class="normal_btn" name="checkBtn" onclick="checkBalance()" value="确认"/>
				<%
					}
				%>
				<input type="button" class="normal_btn" name="backBtn" onclick="javascript:history.go(-1)" value="返回"/>
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript">
	function checkBalance()
	{
		MyConfirm("是否确认？", checkBalanceFor)
	}
	
	function checkBalanceFor()
	{
		fm.action = "<%=contextPath%>/claim/authorization/BalanceMain/checkBalanceFor.do";
		fm.submit();
	}
</script>
</body>
</html>