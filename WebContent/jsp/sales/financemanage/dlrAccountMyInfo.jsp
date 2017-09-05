<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>账户余额明细</title>
<script type="text/javascript">
	function doInit(){
		//__extQuery__(1);
	}
</script>
</head>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 财务管理 &gt; 账户余额查询 &gt;账户余额明细</div>
 <form method="post" name = "fm" >
    <table width=100% border="0" align="center" class="table_list">
    <c:if test="${oemFlag==1}">
    	<tr>
			<td  colspan="6" align="left"><font color="red">注意事项:</font></td>
		</tr>
		<tr>
		<td  colspan="6" align="left"><font color="red">1、入账联系人：现金-喻起萍 67595227 ，承兑汇票-郭萍 67591948 兵财额度-刘昕 67591948；</font></td>
		</tr>
		<tr>
		<td  colspan="6" align="left"><font color="red">2、如以上人员反映已录入，但dms在<font size="3" ><strong>2小时</strong></font>后无显示，请联系杨书友：67591947，确认财务的资金系统是否已入账。</font></td>
		</tr>
		<tr>
		<td  colspan="6" align="left"><font color="red">3、到账时间：现款-厂家财务收到款后当天录入系统，如<font size="3" ><strong>2-3天后</strong></font>未入账再进行咨询，请不要频繁打电话催促，承兑汇票、兵财额度-收到汇票和银行通知额度后录入系统。  
</font></td>
		</tr>
		<tr>
		<td  colspan="6" align="left"><font color="red">4、长安信贷专户资金类型，专项用于轿车长安信贷还款和保证金扣款。此账户的资金不能用于经销商新启票。</font></td>
		</tr>
    </c:if>
    <tr>
		<td  colspan="6" align="left"><font color="red"><strong>兵财融资可用金额计算：兵财融资账户金额 - （轿车所有基地兵财融资冻结金额 + 微车所有基地兵财融资冻结金额）</strong></font></td>
	</tr>
	<tr>
		<td  colspan="6" align="left"><font color="red"> 1、现金、承兑汇票及长安信贷：扣订单的启票单位款项;</font></td>
	</tr>
	<tr>
		<td  colspan="6" align="left"><font color="red">2、三方信贷：扣订单的启票单位和采购单位款项，启票单位款项=启票单位自有款项+采购单位款项; </font></td>
	</tr>
	<tr>
		<td  colspan="6" align="left"><font color="red">3、兵财存货融资：扣订单的采购单位款项; </font></td>
	</tr>
	<tr>
		<td  colspan="6" align="left"><font color="red">4、所有资金类型订单的价格折扣均扣启票单位款项.</font></td>
	</tr>
 	<c:forEach var="list" items="${getList}">
 		<tr>
	    	<th colspan="7" align="left">
	    		<img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 账户余额明细
	    	</th>
	    </tr>
	    <tr class="table_list_row2">
	    	<th align="center"><font color="red"><strong>经销商代码：</strong></font></th>
	    	<th><strong>${list.DEALER_CODE}</strong></th>
	    	<th align="center"><font color="red"><strong>经销商名称：</strong></font></th>
	    	<th colspan="4"><strong>${list.DEALER_NAME}</strong></th>
	    </tr>
	    <tr class="table_list_row2">
	    	<td align="center">资金类型：</td>
	    	<td colspan="2">账面余额</td>
	    	<td colspan="2">预扣款</td>
	    	<td colspan="2">可用金额</td>
	    </tr>
	    	<c:forEach var="list1" items="${getMyList}">
	   			<c:if test="${list1.DEALER_CODE==list.DEALER_CODE}">
					<tr class="table_list_row1">
						<td align="center">${list1.TYPE_NAME}</td>
						<td colspan="2"><script>document.write(amountFormat(${list1.BALANCE_AMOUNT}));</script></td>
						<td colspan="2"><script>document.write(amountFormat(${list1.FREEZE_AMOUNT}));</script></td>
						<td colspan="2"><script>document.write(amountFormat(${list1.AVAILABLE_AMOUNT}));</script></td>
					</tr>
				</c:if>
			</c:forEach>
		<tr class="table_list_row2">
			<td align="center">合计：</td>
			<td colspan="2"><script>document.write(amountFormat(${list.BALANCE_AMOUNT}));</script></td>
			<td colspan="2"><script>document.write(amountFormat(${list.FREEZE_AMOUNT}));</script></td>
			<td colspan="2"><script>document.write(amountFormat(${list.AVAILABLE_AMOUNT}));</script></td>
		</tr> 
		<tr>
	    	<th colspan="7" align="left">
	    		<img class="nav" src="<%=contextPath%>/img/subNav.gif" /> 预扣款明细
	    	</th>
	    </tr>
		 <tr align="center" class="table_list_row2">
			<td align="center"><strong>开票单位代码</strong></td>
			<td align="center"><strong>开票单位名称</strong></td>
			<td align="center"><strong>销售订单号</strong></td>
			<td align="center"><strong>订单状态</strong></td>
			<td align="center"><strong>订单提报日期</strong></td>
			<td align="center"><strong>资金类型</strong></td>
			<td align="center"><strong>冻结金额</strong></td>
			<td align="center"><strong>扣款类型</strong></td>
		</tr>
		<c:forEach var="freezeList" items="${freezeList}">
	   		<c:if test="${freezeList.DEALER_CODE==list.DEALER_CODE}">
				<tr class="table_list_row2">
					<td align="center">${freezeList.DEALER_CODE}</td>
					<td>${freezeList.DEALER_SHORTNAME}</td>
					<td>${freezeList.ORDER_NO}</td>
					<td><script>document.write(getItemValue(${freezeList.ORDER_STATUS }));</script></td>
					<td>${freezeList.RAISE_DATE}</td>
					<td>${freezeList.TYPE_NAME}</td>
					<td><script>document.write(amountFormat(${freezeList.FREEZE_AMOUNT}));</script></td>
					<td>${freezeList.FREEZE_TYPE}</td>
				</tr>
			</c:if>
		</c:forEach>
		<tr><td colspan="7">&nbsp;</td></tr>
	</c:forEach>
	</table>
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!-- 查询条件 end -->
</form>

<!--页面列表 begin -->
<script type="text/javascript" >
	
</script>
<!--页面列表 end -->
</body>
</html>
