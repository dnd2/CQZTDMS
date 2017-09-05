<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
	<link href="<%=request.getContextPath()%>/style/dtree_default.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/web/dtree1.js"></script>
</head>
<body onunload='javascript:destoryPrototype();'  onload="__extQuery__(1);">
	 <div >
	 <form id='fm' name='fm'>
		<input type="hidden" id="orgId" name="orgId" value="" />
		<input type="hidden" id="ORG_ID" name="ORG_ID" value="${ORG_ID}" />
		<input type="hidden" id="ORDER_IDS" name="ORDER_IDS" value="${ORDER_IDS }" />
		<br>
		<table class="table_query" >
		<tr>
				<td class="table_query_label" width="180" align="right" nowrap="nowrap">订单号：</td>
				<td class="table_query_input" align="left"   nowrap="nowrap">
					<input datatype="1,is_noquotation,<%=Constant.Length_Check_Char_100 %>" id="ORDER_NO" name="ORDER_NO" style="width:150px;" class="short_txt" type="text" />
				</td>
			</tr>
		</table>
		<table class="table_query" >
			<tr style="width: 90%"> 
				<td class="table_query_label" nowrap="nowrap" align="right">
					<input class="normal_btn" id="queryBtn" type="button" value="查 询" onclick="__extQuery__(1);"/>
					<input class="normal_btn" onclick="add();" type="reset" value="添加" />
					<input class="normal_btn" type="button" onclick="_hide();" value="关 闭" />
				</td>
			</tr>
		</table>
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		</form>
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		</div>
</body>
<script>
	var url = "<%=contextPath%>/wholeSaleSupport/wholeSaleSupporApply/WholeSaleSupporApply/queryWholeSaleOrder.json";
	//设置表格标题
	var title= null;
	//设置列名属性
	var columns = [
					{header: "<input type='checkbox' id='selectAll' name='selectAll' onclick='checkselectAllBox(this)'  />", dataIndex: 'orderId',width:"50px",renderer:myLink},
					{header: "品牌名称",width:"50px", dataIndex: 'brandName'},
					{header: "车型", dataIndex: 'modelName'},
					{header: "订单号", dataIndex: 'orderNo'},
					{header: "数量", dataIndex: 'orderAmount'},
					{header: "单价", dataIndex: 'normalPrice'}
			      ];
	
	function myLink(value,meta,record){                                                                       
		return "<input type='checkbox' name='selectOne' onclick='checkselectAllBox(this)' brandName=\""+record.data.brandName+"\" modelName=\""+record.data.modelName+"\"  normalPrice=\""+record.data.normalPrice+"\"  modelId=\""+record.data.productId+"\" orderId=\""+record.data.orderId+"\" orderAmount=\""+record.data.orderAmount+"\" orderNo=\""+record.data.orderNo+"\"  />"
	}
	var objarr = document.getElementsByName('selectOne');
	function checkselectAllBox(obj) {
		if(obj.id == "selectAll" && obj.checked == true) {
			for(var i=0; i<objarr.length; i++) {
				objarr[i].checked = true;
			}
		} else if(obj.id == "selectAll" && obj.checked == false) {
			for(var i=0; i<objarr.length; i++) {
				objarr[i].checked = false;
			}
		} else {
			if(obj.checked == false) {
				$('selectAll').checked = false;
			} else {
				var st = true;
				for(var i=0; i<objarr.length; i++) {
					if(objarr[i].checked == false) {
						st = false;
						break;
					}
				}
				st ? $('selectAll').checked = true : "";
			}
		}
	}
	
	function add() {
			var brandNameTemp = new Array();
			var modelNameTemp = new Array();
			var modelIdTemp = new Array();
			var orderIdTemp = new Array();
			var orderNoTemp = new Array();
			var orderAmountTemp = new Array();
			var normalPriceTemp = new Array();
			var j=0;
			for(var i=0; i<objarr.length; i++)
			{
				if(objarr[i].checked == true) 
				{
					brandNameTemp.push(objarr[i].brandName);
					modelNameTemp.push(objarr[i].modelName);
					modelIdTemp.push(objarr[i].modelId);
					orderIdTemp.push(objarr[i].orderId);
					orderNoTemp.push(objarr[i].orderNo);
					orderAmountTemp.push(objarr[i].orderAmount);
					normalPriceTemp.push(objarr[i].normalPrice);
					j++;
				}
			}
			parentContainer.returnValue(brandNameTemp,modelNameTemp,modelIdTemp,orderIdTemp,orderNoTemp,orderAmountTemp,normalPriceTemp);
		_hide();
	}
	function setDlr(dlrId,dlrName){
		parentDocument.getElementById('dealerName').value = dlrName;
		parentDocument.getElementById('dealerId').value = dlrId;
		_hide();
	}
	
	function setOrg(orgId){
		$('orgId').value = orgId;
		__extQuery__(1);
	}
	
	function clsTxt(){ //清除经销商文本框
		parentDocument.getElementById('dealerName').value = "";
		parentDocument.getElementById('dealerId').value = "";
		_hide();
	}
</script>
</html>