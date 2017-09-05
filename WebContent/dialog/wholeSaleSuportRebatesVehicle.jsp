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
		<input type="hidden" id="VEHICLE_IDS" name="VEHICLE_IDS" value="${VEHICLE_IDS }" />
		<input type="hidden" id="promotionId" name="promotionId" value="${promotionId }" />
		<input type="hidden" id="selectNum" name="selectNum" value="${selectNum }" />
		<br>
		<table class="table_query" >
		<tr>
				<td class="table_query_label" width="180" align="right" nowrap="nowrap">VIN：</td>
				<td class="table_query_input" align="left"   nowrap="nowrap">
					<input datatype="1,is_noquotation,<%=Constant.Length_Check_Char_100 %>" id="vin" name="vin" style="width:150px;" class="short_txt" type="text" />
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
	var url = "<%=contextPath%>/financial/wholeSaleSuportRebates/WholeSaleSuportRebates/queryWholeSaleSuportRebatesVehicle.json";
	//设置表格标题
	var title= null;
	//设置列名属性
	var columns = [
					{header: "<input type='checkbox' id='selectAll' name='selectAll' onclick='checkselectAllBox(this)'  />", dataIndex: 'vehicleId',width:"50px",renderer:myLink},
					{header: "车型", dataIndex: 'modelName'},
					{header: "订单号", dataIndex: 'orderNo'},
					{header: "VIN", dataIndex: 'vin'},
					{header: "单价", dataIndex: 'normalPrice'}
			      ];
	function myLink(value,meta,record){                                                                       
		return "<input type='checkbox' name='selectOne' onclick='checkselectAllBox(this)'  preferentialPoint=\""+record.data.preferentialPoint+"\"  flag=\""+record.data.flag+"\"   preferentialAmount=\""+record.data.preferentialAmount+"\"   statusinvdate=\""+record.data.statusinvdate+"\"   vehicleId=\""+record.data.vehicleId+"\"   invoiceNo=\""+record.data.invoiceNo+"\"  channel=\""+record.data.channel+"\"    vin=\""+record.data.vin+"\"    modelName=\""+record.data.modelName+"\"  normalPrice=\""+record.data.normalPrice+"\"  modelId=\""+record.data.productId+"\" customeCode=\""+record.data.customeCode+"\"  flagUsed=\""+record.data.flagUsed+"\"  orderId=\""+record.data.orderId+"\"orderNo=\""+record.data.orderNo+"\"  />"
	}
	var objarr = document.getElementsByName('selectOne');
	function checkselectAllBox(obj) {
		if(obj.id == "selectAll" && obj.checked == true) {
			for(var i=0; i<objarr.length; i++) {
				objarr[i].checked = true;
				if(objarr[i].flag==1){
				if(confirm("您选择的订单车辆已经申报过批售或变动促销返利，是否继续选择？")){
				objarr[i].checked = true;
				 }else{ 
				 $('selectAll').checked = false;
				 objarr[i].checked = false;
				 }
				}
				if(objarr[i].flagUsed!=0){
				if(confirm("您选择的订单车辆已经申报过批售或变动促销返利，是否继续选择？")){
				objarr[i].checked = true;
				 }else{ 
				 $('selectAll').checked = false;
				 objarr[i].checked = false;
				 }
				}
			}
		} else if(obj.id == "selectAll" && obj.checked == false) {
			for(var i=0; i<objarr.length; i++) {
				objarr[i].checked = false;
			}
		} else {
			if(obj.checked == false) {
				$('selectAll').checked = false;
			} else {
					if(window.event==null){
					        element=parent.window.event.srcElement;  
					      } else {
					        element=window.event.srcElement;   
					      }
					      while(element.tagName!="INPUT"){
					       element=element.parentElement;  
					    	} 
					   if(element.flag==1&&element.checked){
							if(confirm("您选择的订单车辆已经申报过批售或变动促销返利，是否继续选择？")){
							 element.checked = true;
							 }else{ 
							element.checked = false;
							 }
					}
					   if(element.flagUsed!=0&&element.checked){
							if(confirm("您选择的订单车辆已经申报过批售或变动促销返利，是否继续选择？")){
							 element.checked = true;
							 }else{ 
							element.checked = false;
							 }
					}
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
			var vehicleIdTemp = new Array();
			var modelNameTemp = new Array();
			var modelIdTemp = new Array();
			var invoiceNoTemp = new Array();
			var statusinvdateTemp = new Array();
			var orderIdTemp = new Array();
			var orderNoTemp = new Array();
			var channelTemp = new Array();
			var vinTemp = new Array();
			var normalPriceTemp = new Array();
			var preferentialPointTemp = new Array();
			var preferentialAmountTemp = new Array(); 
			var flagTemp = new Array(); 
			var customeCodeTemp = new Array(); 
			var j=0;
			for(var i=0; i<objarr.length; i++)
			{
				if(objarr[i].checked == true) 
				{
					vehicleIdTemp.push(objarr[i].vehicleId);
					modelNameTemp.push(objarr[i].modelName);
					modelIdTemp.push(objarr[i].modelId);
					invoiceNoTemp.push(objarr[i].invoiceNo);
					statusinvdateTemp.push(objarr[i].statusinvdate);
					orderIdTemp.push(objarr[i].orderId);
					orderNoTemp.push(objarr[i].orderNo);
					var channelVar=getItemValue(objarr[i].channel);
					channelTemp.push(channelVar);
					vinTemp.push(objarr[i].vin);
					normalPriceTemp.push(objarr[i].normalPrice);
					preferentialPointTemp.push(objarr[i].preferentialPoint);
					preferentialAmountTemp.push(objarr[i].preferentialAmount);
					flagTemp.push(objarr[i].flag);
					customeCodeTemp.push(objarr[i].customeCode);
				}
			}
			if(parseInt(document.getElementById("selectNum").value,10)<vehicleIdTemp.length){
				MyAlert("你选择的条数已经超出可上报数！");
				return;
			}
			parentContainer.returnValue(vehicleIdTemp,modelNameTemp,modelIdTemp,invoiceNoTemp,statusinvdateTemp,orderIdTemp,orderNoTemp,orderIdTemp,channelTemp,vinTemp,normalPriceTemp,preferentialPointTemp,preferentialAmountTemp,flagTemp,customeCodeTemp);
		_hide();
	}
</script>
</html>