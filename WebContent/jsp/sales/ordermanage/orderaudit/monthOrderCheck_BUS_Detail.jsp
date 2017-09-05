<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>月度常规订单审核(事业部)</title>
<script type="text/javascript">
function doInit(){
	
}
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车销售 > 销售订单管理 > 订单审核 > 月度常规订单审核</div>
<form method="POST" name="fm" id="fm">
  <table class="table_list" align=center width="95%">
	<tr class="table_list_th">
		<th>经销商代码</th>
		<th>经销商名称</th>
		<th>订单号</th>
		<th>订单月度</th>
	</tr>
	<tr class="table_list_row1">
		<td>${orderInfo.DEALER_CODE }</td>
		<td>${orderInfo.DEALER_SHORTNAME }</td>
		<td>${orderInfo.DEALER_CODE }</td>
		<td>${orderInfo.ORDERMONTH }<input type="hidden" id="orderId" name="orderId" value="${orderInfo.ORDER_ID }" /></td>
	</tr>
 </table>
 <br>
 <table class="table_list" align=center width="95%">
   <tr class="table_list_th">
		<th>车系</th>
		<th>物料编号</th>
		<th>物料名称</th>
		<th>提报数量</th>
		<!--<th>审核数量</th>	-->
	</tr>
	<c:if test="${dList!=null}">
		<c:forEach items="${dList}" var="list">
			<tr align="center" class="table_list_row1">
			<td>${list.GROUP_NAME}</td>
			<td>${list.MATERIAL_CODE}</td>
			<td>${list.MATERIAL_NAME}</td>
			<td>
				${list.ORDER_AMOUNT}
				<input type="hidden" id="orderAmount${list.ORDER_AMOUNT}" name="orderAmount" value="${list.ORDER_AMOUNT}" />
			</td>
			<!--
			<td>
				<input type="text" id="checkAmount${list.DETAIL_ID}" name="checkAmount" class="SearchInput" datatype="0,is_digit,6"  size="2" maxlength="6" value="${list.ORDER_AMOUNT}" />
			</td>
			-->
			</tr>
		</c:forEach>
	</c:if>
  </table>
  <br>
  <table class="table_query">
    <tr class="cssTable" >
      <th colspan="2" align="left"> 审核信息</th>
    </tr>
    <tr class="cssTable" >
      <td width="15%" align="right">审核描述：</td>
      <td  align="left"><label>
        <textarea name="checkDesc" cols="50" rows="3"></textarea>
      </label></td>
    </tr>
    <tr class="cssTable" >
      <td align="left">&nbsp;</td>
      <td align="left">
      		<input class='normal_btn'  name="add" type="button" onclick="checkSubmit('1');" value ='通过' />
			<input class='normal_btn'  name="add22" type="button" onclick="checkSubmit('2');" value ='驳回' />
    </tr>
  </table>
  
</form>
<script type="text/javascript">

	function checkSubmit(value){
		var checkDesc = document.getElementsByName("checkDesc");
		if(checkDesc[0].value.length>350){
			MyDivAlert("审核描述内容过多，请重新填写");
			return;
		}
		MyDivConfirm("是否提交?",checkSubmitAction,[value]);
	}
	function checkSubmitAction(value){
		makeNomalFormCall('<%=request.getContextPath()%>/sales/ordermanage/orderaudit/MonthOrderCheck/monthOrderCheck_BUS_submit.json?checkStatus='+value,showDateInfo,'fm');
	}
	function showDateInfo(json){
		if(json.returnValue == '1'){
			parent._hide();
			parent.$('inIframe').contentWindow.reloadAction();
		}else{
			MyDivAlert("操作失败，请重新操作或联系管理员");
		}
	}
</script>
</body>
</html>
