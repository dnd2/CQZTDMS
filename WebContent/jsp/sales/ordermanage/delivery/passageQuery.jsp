<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>在途车辆信息查询</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
<!--
function doInit() {
	var defaultRadio = document.getElementById("deliveryQuery") ;
	checkRadio(defaultRadio) ;
	// checkQuery(defaultRadio) ;
}

function checkRadio(obj) {
	if(obj.id == "deliveryQuery") {
		document.getElementById("dealerCode").disabled = false ;
		document.getElementById("button3").disabled = false ;
		document.getElementById("deliveryNo").disabled = false ;
		document.getElementById("moveNo").disabled = true ;
		document.getElementById("orgId").disabled = false ;
		document.getElementById("areaId").disabled = false ;
		document.getElementById("txt1").disabled = false ;
	} else if(obj.id == "moveQuery") {
		document.getElementById("dealerCode").disabled = true ;
		document.getElementById("button3").disabled = true ;
		document.getElementById("deliveryNo").disabled = true ;
		document.getElementById("moveNo").disabled = false ;
		document.getElementById("orgId").disabled = true ;
		document.getElementById("areaId").disabled = true ;
		document.getElementById("txt1").disabled = true ;
	}
}

function checkQuery() {
	var oRadioA = document.getElementById("deliveryQuery") ;
	var oRadioB = document.getElementById("moveQuery") ;
	
	if(oRadioA.checked) {
		deliveryQuery() ;
	} else if(oRadioB.checked) {
		moveQuery() ;
	}
}
//-->
</script>
</head>
<body onload="genLocSel('txt1','','','','','');">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 销售订单管理 &gt; 订单发运 &gt;在途车辆信息查询</div>
<form method="post" name="fm" id="fm">
<input name="sendCarsNo" type="hidden" id="sendCarsNo"  class="middle_txt" value=""/>
<!-- 查询条件 begin -->
	<table class="table_query" >
		<tr>
			<td align="right">选择查询：</td>
			<td>
				<input type="radio" name="queryType" id="deliveryQuery" onclick="checkRadio(this);" checked="checked" />&nbsp;<label for="deliveryQuery">启票在途查询</label>
				<input type="radio" name="queryType" id="moveQuery" onclick="checkRadio(this);" />&nbsp;<label for="moveQuery">移库在途查询</label>
			</td>
			<td align="right"></TD>
			<td align="left"></td>
		</tr>
		<tr>
			<td align="right">选择开票方：</td>
			<td>
				<input type="text" readonly="readonly" name="dealerCode" class="long_txt" id="dealerCode" value="" />
				<input name="button3" id="button3" type="button"  class="mini_btn" onclick="showOrgDealer('dealerCode','','true');" value="..." />
				<input type="button" class="normal_btn" onclick="remarkCode()" value="清除">
			</td>
			<td align="right">送车交接单号：</TD>
			<td align="left">
				<input type="text" name="sendcarNo" class="middle_txt" id="sendcarNo" value="" />
			</td>
		</tr>
		<tr>
			<td align="right">ERP订单号：</TD>
			<td>
				<input type="text" name="erpNo" class="middle_txt" id="erpNo" value="" />
			</td>
			<td  align="right">发运单号：</td>
			<td align="left">
				<input type="text" class="middle_txt" name="deliveryNo" id="deliveryNo" />
			</td>
		</tr>
		<tr>
			<td  align="right">事业部：</td>
			<td align="left">
				<select id="orgId" name="orgId" class="short_sel">
				<option value="">--请选择--</option>
					<c:forEach items="${orgList}" var="orgList">
						<option value="${orgList.ORG_ID }">${orgList.ORG_NAME }</option>
					</c:forEach>
				</select>
			</td>
			<td  align="right">移库单号：</td>
			<td align="left">
				<input type="text" class="middle_txt" name="moveNo" id="moveNo" />
			</td>
		</tr>
		<tr>
			<td align="right">业务范围：</td>
			<td>
				<select name="areaId" id="areaId" class="short_sel">
					<option value="">-请选择-</option>
					<c:forEach items="${areaBusList}" var="list">
						<option value="${list.AREA_ID}"><c:out value="${list.AREA_NAME}"/></option>
					</c:forEach>
				</select>
				<td align="right">省份：</td>
			<td align="left">
					<select class="short_sel" id="txt1" name="downtown"></select>
			</td>
			<td align="right"></td>
			<td align="left"></td>
		</tr>
		<tr>
			<td align="right">&nbsp;</td>
			<td align="right">&nbsp;</td>
			<td align="right">&nbsp;</td>
			<td align="left">
				<input name="button2" id="queryBtn" type="button" class="cssbutton" onClick="checkQuery();" value="查询">
			</td>
		</tr>
	</table>
<!-- 查询条件 end -->
<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<!--分页 end --> 
</form>
<!--页面列表 begin -->
<script type="text/javascript" >

	var myPage;
	//查询路径           
	var url = "";
	
	var title = null;
	
	var columns = null ;

	function deliveryQuery() {
		url = "<%=contextPath%>/sales/ordermanage/delivery/OrderCommandQuery/passageQuery.json";
		
		columns = [
						{header: "序号",align:'center',renderer:getIndex},
						{header: "送车交接单",dataIndex: 'SENDCAR_ORDER_NUMBER',align:'center',renderer:myHref},
						{header: "销售订单号",dataIndex: 'ORDER_NUMBER',align:'center'},
						{header: "发运类型",dataIndex: 'DELIVERY_TYPE',align:'center',renderer:getItemValue},
						{header: "发运单号", dataIndex: 'DELIVERY_NO', align:'center'},
						{header: "发运时间", dataIndex: 'FLATCAR_ASSIGN_DATE', align:'center'},
						{header: "财务审核时间", dataIndex: 'FINANCE_CHK_DATE', align:'center'},
						{header: "承运单位", dataIndex: 'SHIP_METHOD_CODE', align:'center'},
						{header: "发车数量", dataIndex: 'DE_AMOUNT', align:'center'},
						{header: "实际在途", dataIndex: 'DEING_AMOUNT', align:'center'},
						{header: "平板车号", dataIndex: 'FLATCAR_ID', align:'center'},
						{header: "发车仓库", dataIndex: 'WAREHOUSE_NAME', align:'center'},
						{header: "一级采购单位", dataIndex: 'BILLING_DLR_NAME', align:'center'},
						{header: "二级采购单位", dataIndex: 'ORDER_DLR_NAME', align:'center'},
						{header: "收车单位", dataIndex: 'RECEIVE_DLR_NAME', align:'center'},
						{header: "收车地址", dataIndex: 'ADDRESS', align:'center'},
						{header: "司机名称", dataIndex: 'MOTORMAN', align:'center'},
						{header: "司机电话", dataIndex: 'MOTORMAN_PHONE', align:'center'},
						{header: "单据情况", dataIndex: 'STATUS', align:'center'}
				      ];
		
		__extQuery__(1);
	}
	
	function moveQuery() {
		url = "<%=contextPath%>/sales/ordermanage/delivery/OrderCommandQuery/moveQuery.json";
		
		columns = [
						{header: "序号",align:'center',renderer:getIndex},
						{header: "送车交接单",dataIndex: 'SENDCAR_ORDER_NUMBER',align:'center',renderer:myHrefA},
						{header: "销售订单号",dataIndex: 'ORDER_NUMBER',align:'center'},
						{header: "移库单号",dataIndex: 'ORDER_NO',align:'center'},
						{header: "发运时间", dataIndex: 'FDATE', align:'center'},
						{header: "承运单位", dataIndex: 'SHIP_METHOD_CODE', align:'center'},
						{header: "发车数量", dataIndex: 'DCOUNT', align:'center'},
						{header: "实际在途", dataIndex: 'ECOUNT', align:'center'},
						{header: "平板车号", dataIndex: 'FLATCAR_ID', align:'center'},
						{header: "发车仓库", dataIndex: 'FNAME', align:'center'},
						{header: "目标仓库", dataIndex: 'TNAME', align:'center'},
						{header: "司机名称", dataIndex: 'MOTORMAN', align:'center'},
						{header: "司机电话", dataIndex: 'MOTORMAN_PHONE', align:'center'},
						{header: "单据情况", dataIndex: 'STATUS', align:'center'}
				      ];
		
		__extQuery__(1);
	}
	
	function remarkCode()
	{
		document.getElementById("dealerCode").value = "";
	}
	
	function myHref(value,meta,record){
	      		var link = "<a href='#' onclick='view("+record.data.SENDCAR_ORDER_NUMBER+");'>"+value+"</a>";
	      		return String.format(link);
	}      
	function view(sendCarNo){
		$('sendCarsNo').value=sendCarNo;
      	OpenHtmlWindow("<%=contextPath%>/sales/ordermanage/delivery/OrderCommandQuery/showSendCarsDetail.do?sendCarNo="+sendCarNo,800,500);
      	//OpenHtmlWindow("<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/viewVehiclePinRequest?pinId="+pinId+"&userId="+userId,800,500);
    }
	
	function myHrefA(value,meta,record){
  		var link = "<a href='#' onclick='viewA("+record.data.SENDCAR_ORDER_NUMBER+");'>"+value+"</a>";
  		return String.format(link);
	}      
	function viewA(sendCarNo){
	$('sendCarsNo').value=sendCarNo;
		OpenHtmlWindow("<%=contextPath%>/sales/ordermanage/delivery/OrderCommandQuery/getVechInfo.do?sendCarNo="+sendCarNo,800,500);
		//OpenHtmlWindow("<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/viewVehiclePinRequest?pinId="+pinId+"&userId="+userId,800,500);
	}
</script>
<!--页面列表 end -->
</body>
</html>