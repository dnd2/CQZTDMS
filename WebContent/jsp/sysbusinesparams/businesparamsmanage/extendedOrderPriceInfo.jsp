<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.po.TmWarehousePO"%>
<%@ page import="java.util.Map"%>
<%@ taglib prefix="c" uri="/jstl/cout" %>
<%
	String contextPath = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>超期常规订单资源单价维护</title>
<style type="text/css">
		table.log_query{
			border-collapse:collapse;
			width:100%;
			background-color:#F3F4F8;
		}
		table.log_query th, .log_query td{
			background-color:#f8f8f8;
			font-weight:normal;
			padding:5px 0px 3px 0px;
		}
		table.log_query th{
			background-color:#DAE0EE;
			text-align: center;
			color:#08327e;
		}
		table.log_query td{
			padding:5px 0px 3px 0px;
			text-indent:5px;
			background-color:#F0F7F2;
		}
</style>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
</head>
<body onunload='javascript:destoryPrototype();'> 
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：  系统管理 &gt; 系统业务参数维护 &gt; 超期常规订单资源单价维护</div>
	<form id="fm" name="fm" method="post">
		<input type="hidden" name="curPage" id="curPage" value="1" />
		<input type="hidden" id="dlrId" name="dlrId" value="" />
		<table class="table_query" border="0">
			<tr>
				<td width="15%" class="tblopt"><div align="right">年份：</div></td>
				<td width="20%" >
      				<input type="text" id="year" name="year" datatype="0,is_digit,4" value="${val.paymentYear}" disabled="disabled" readonly="readonly"/>
    			</td>
    		</tr> 
			<tr>
    			<td width="15%" class="tblopt"><div align="right">月份：</div></td>
				<td width="20%" >
      				<input type="text" id="month" name="month" datatype="0,is_digit,2" value="${val.paymentMonth }" disabled="disabled" readonly="readonly" />
    			</td>
			</tr>
			<tr>
    			<td width="15%" class="tblopt"><div align="right">价格：</div></td>
				<td width="20%" >
					<input type="text" id="price" name="price" datatype="0,isMoney,10" value="${val.paymentPrice }" title="价格"/>
    			</td> 
    		</tr>
    		<tr>
    			<td width="15%" class="tblopt"><div align="right">备注：</div></td>
				<td width="20%" >
					<textarea id="remark" name="remark" rows="4" cols="20" title="备注"></textarea>
    			</td> 
    		</tr>
			<tr>
    			<td align="center"  colspan="3">
					<input type="button"  class="normal_btn" onclick="validate();" value="提 交" id="queryBtn" />
					<input type="button"  class="normal_btn" onclick="history.back();" value="返回" id="btn" /> 
					<input type="hidden" id='priceId' name="priceId" value="${val.priceId }" />
				</td>
			</tr>
		</table>
		<br/>
		<table class="table_list" border="0">
			<tr class="table_list_th">
				<th width="12%">年份</th>
				<th width="12%">月份</th>
				<th width="12%">更新价格</th>
				<th width="20%">更新时间</th>
				<th width="12%">更新人</th>
				<th width="30%">备注</th>
			</tr>
			<c:forEach items="${logs}" var="log" varStatus="status">
				
				<tr align="center" <c:if test="${status.index % 2 == 0 }"> class='table_list_row2'</c:if> <c:if test="${status.index % 2 != 0 }"> class='table_list_row1'</c:if>>
					<td>${log.PAYMENT_YEAR}</td>
					<td>${log.PAYMENT_MONTH}</td>
					<td>${log.PAYMENT_PRICE}</td>
					<td>${log.UPDATE_DATE}</td>
					<td>${log.UPDATE_NAME}</td>
					<td>${log.REMARK}</td>
				</tr>
			</c:forEach>
		</table>
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	</form>
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</div>
<script type="text/javascript" >

	function validate(){
		if(submitForm('fm')){
			var priceId = document.getElementById("priceId").value;
			var year = document.getElementById("year").value
			var month = document.getElementById("month").value
			var price = document.getElementById("price").value
			makeCall("<%=contextPath%>/sysbusinesparams/businesparamsmanage/ExtendedOrderPriceMaintenance/validate.json", submit, {priceId : priceId, year : year, month : month, price : price});
		}
	}
	
	function submit(json){
		var name = json.errorField
		if(name) {
			MyAlert(document.getElementById(name).title + "不能为空!");
		} else {
			fm.action = "<%=contextPath%>/sysbusinesparams/businesparamsmanage/ExtendedOrderPriceMaintenance/updateExtendedOrderPrice.do";
			fm.submit();
		}
	}

</script>    
</body>
</html>