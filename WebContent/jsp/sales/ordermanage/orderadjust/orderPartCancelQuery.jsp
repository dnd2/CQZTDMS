<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>订单取消</title>
<script type="text/javascript">
function doInit(){
	__extQuery__(1);
}
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车销售 > 销售订单管理 > 订单调整 > 订单取消</div>
<form method="POST" name="fm" id="fm">
	<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    <!--分页 end -->
    
    <table class=table_query width="95%" align="center" border=0 id="table1">
	  <tr class="tabletitle">
        <td width="11%" align="right">输入取消原因：</td>
	    <td width="18%" align="left"><textarea rows="3" name="chngReason"></textarea></td>
	    <td width="71%" align="left">
	    	<input type="hidden" name="orderId" value="${orderId}">
	    	<input type="hidden" name="oldVer" value="${ver}">
	    	<input type="hidden" name="dealerId">
	    	<input type="hidden" name="index">
		    <input type="button" name="bt_search" value="确定" onclick="confirmCancel();" />
	        <input type="button" name="bt_search2" value="返回" onclick="history.back();" />
        </td>
	  </tr>
	</table>
</form>
<script type="text/javascript">
	document.getElementById("table1").style.display = "none";
	var HIDDEN_ARRAY_IDS=['table1'];
	
	var myPage;
	//查询路径
	var url = "<%=request.getContextPath()%>/sales/ordermanage/orderadjust/OrderCancel/orderPartCancelQuery.json?command=1";
				
	var title = null;

	var columns = [
				{header: "订货方", dataIndex: 'ORDER_ORG_NAME', align:'center'},
				{header: "付款方", dataIndex: 'BILLING_ORG_NAME', align:'center'},
				{header: "订单号码", dataIndex: 'ORDER_NO', align:'center' ,renderer:myDetail},
				{header: "订单周度", dataIndex: 'ORDER_DATE', align:'center'},
				{header: "订单类型", dataIndex: 'ORDER_TYPE', align:'center' ,renderer:getItemValue},
				{header: "物料名称", dataIndex: 'MATERIAL_NAME', align:'center'},
				//{header: "定做批次号", dataIndex: 'SPECIAL_BATCH_NO', align:'center'},
				{header: "提报数量", dataIndex: 'ORDER_AMOUNT', align:'center'},
				{header: "审核数量", dataIndex: 'CHECK_AMOUNT', align:'center'},
				{header: "申请发运数量", dataIndex: 'CALL_AMOUNT', align:'center'},
				//{header: "发运数量", dataIndex: 'DELIVERY_AMOUNT', align:'center'},
				//{header: "发运数量", dataIndex: 'MATCH_AMOUNT', align:'center'},
				{header: "取消数量", dataIndex: 'DETAIL_ID', align:'center' ,renderer:myInput}
		      ];	
		      
    var i = 0;	         
	
	//设置链接
	function myDetail(value,meta,record){
	    return String.format("<a href='#' onclick='orderDetailInfo(\""+ value +"\")'>"+value+"</a>");
	}
	//订单明细链接
	function orderDetailInfo(value){ 
		OpenHtmlWindow('<%=request.getContextPath()%>/sales/ordermanage/orderdetail/OrderDetailInfoQuery/orderDetailInfoQuery.do?&orderNo='+value,700,500);
	}
	
	function myInput(value,meta,record){
		i++;
		document.getElementById("index").value = i;
		document.getElementById("dealerId").value = record.data.DEALER_ID;
		return String.format("<input type='text' size='2' datatype='0,is_digit,6' id='chngAmt" + i + "' name='chngAmt" + i + "' value='0'/><input type='hidden' name='detailId" + i + "' value='"+value+"'/><input type='hidden' name='checkAmount" + i + "' value='"+record.data.CHECK_AMOUNT+"'/><input type='hidden' name='callAmount" + i + "' value='"+record.data.CALL_AMOUNT+"'/><input type='hidden' name='deliveryAmount" + i + "' value='"+record.data.DELIVERY_AMOUNT+"'/><input type='hidden' name='matchAmount" + i + "' value='"+record.data.MATCH_AMOUNT+"'/><input type='hidden' name='ver" + i + "' value='"+record.data.VER+"'/>");
	}
	
	function confirmCancel(){
		if(submitForm('fm')){
			var size = parseInt(document.getElementById("index").value);
			for(var j=1; j<=size; j++){
	   			var chngAmt = document.getElementById("chngAmt"+j);
	   			var checkAmount = document.getElementById("checkAmount"+j);
	   			var callAmount = document.getElementById("callAmount"+j);
	   			//var deliveryAmount = document.getElementById("deliveryAmount"+j);
	   			//var matchAmount = document.getElementById("matchAmount"+j);
	   			if(parseInt(chngAmt.value, 10) > parseInt(checkAmount.value, 10) - parseInt(callAmount.value, 10)){
	   				MyAlert("取消数量不能大于审核数量减去申请发运数量！");
	   				chngAmt.focus();
	   				return false;
	   			}
	   		}
			MyConfirm("是否确认取消订单?",partCancel);
		}
	}
	
	function partCancel(){
		makeNomalFormCall('<%=request.getContextPath()%>/sales/ordermanage/orderadjust/OrderCancel/orderPartCancel.json',showResult,'fm');
	}
	
	function showResult(json){
		if(json.returnValue == '1'){
			window.location.href = '<%=request.getContextPath()%>/sales/ordermanage/orderadjust/OrderCancel/orderCancelQueryPre.do';
		}else if(json.returnValue == '2'){
			MyAlert("数据已被修改，操作失败！");
		}else{
			MyAlert("取消失败！请联系系统管理员！");
		}
	}
</script>
</body>
</html>
