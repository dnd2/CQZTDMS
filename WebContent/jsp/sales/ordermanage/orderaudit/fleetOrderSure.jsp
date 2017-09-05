<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>订单确认</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
	function doInit(){
		showDate();
	}
	function txtClr(valueId) {
		document.getElementById(valueId).value = '' ;
	}
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 销售订单管理 &gt; 订单审核 &gt;集团客户代交车确认</div>
<form method="post" name="fm" id="fm">
<!-- 查询条件 begin -->
	<table class="table_query" >
		<tr>
			<td align="right">订单类型：</TD>
			<td>
				<label>
					<script type="text/javascript">
						genSelBoxExp("orderType",<%=Constant.ORDER_TYPE%>,"",true,"short_sel",'-1',"false",'');
					</script>
				</label>
			</td>
			<td></td>
		</tr>
		<tr>
			<td align="right">业务范围：</td>
			<td>
				<select name="areaId" class="short_sel">
					<option value="">-请选择-</option>
					<c:forEach items="${areaBusList}" var="list">
						<option value="${list.AREA_ID}"><c:out value="${list.AREA_NAME}"/></option>
					</c:forEach>
				</select>
			</td>
			<td></td>
		</tr>
		<tr>
			<td align="right">开票经销商：</td>
			<td>
				<input type="text" class="middle_txt" name="dealerCode" size="15" value="" id="dealerCode"/>
				<input name="button2" type="button" class="mini_btn" onclick="showOrgDealer('dealerCode','','true')" value="..." />
				<input type="button" class="normal_btn" onclick="txtClr('dealerCode');" value="清 空" id="clrBtn" />
				
			</td>
			<td></td>
		</tr>
		<tr>
			<td align="right">收车经销商：</td>
			<td>
				<input type="text" class="middle_txt" name="dlrCode" size="15" value="" id="dlrCode"/>
				<input name="button3" type="button" class="mini_btn" onclick="showOrgDealer('dlrCode','','true', '${orgId}', 'true')" value="..." />
				<input type="button" class="normal_btn" onclick="txtClr('dlrCode');" value="清 空" id="clrBtn" />
				
			</td>
			<td></td>
		</tr>
		<tr>
			<td align="right">物料组：</td>
			<td>
				<input type="text" class="middle_txt" name="groupCode" size="15"  value="" id="groupCode" />
				<input type="hidden" name="groupName" size="20" id="groupName" value="" />
				<input name="button3" type="button" class="mini_btn" onclick="showMaterialGroup('groupCode','groupName','true','3');" value="..." />
				<input type="button" class="normal_btn" onclick="txtClr('groupCode');" value="清 空" id="clrBtn" />
				
			</td>
			<td></td>
		</tr>
		<tr>
			<td align="right"></TD>
			<td></TD>
			<td align="left"><input name="queryBtn" type=button class="cssbutton" onClick="__extQuery__(1);" value="查询"></td>
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
	var url = "<%=contextPath%>/sales/ordermanage/orderaudit/FleetOrderSure/fleetOrderSureQuery.json";
	var tableInfo = {cls:'table_list2',width:'140%'};
	var title = null;
	var columns = [
				{header: "经销商代码", dataIndex: 'DEALER_CODE',align:'center'},
				{header: "经销商名称", dataIndex: 'DEALER_NAME',align:'center'},
				{id:'action',header: "销售订单号", dataIndex: 'ORDER_NO', align:'center',renderer:myDetail},
				{header: "发运申请单号", dataIndex: 'DLVRY_REQ_NO',align:'center'},
				{header: "提报日期", dataIndex: 'RAISE_DATE', align:'center'},
				{header: "订单周度", dataIndex: 'ORDER_WEEK', align:'center',renderer:myText},
				{header: "订单类型", dataIndex: 'ORDER_TYPE', align:'center',renderer:getItemValue},
				{header: "代交车地址", dataIndex: 'FLEET_ADDRESS', align:'center'},
				{id:'action',header:"操作",sortable: false,dataIndex:'REQ_ID',align:'center',renderer:myLink}
		      ];
	//设置超链接  begin
	function myDetail(value,meta,record){
		return String.format("<a href='#' onclick='orderDetailInfo(\""+ value +"\")'>"+value+"</a>");
	}
	var rowObjNum = null;
	var rowObj = null;
	function orderDetailInfo(value){
		OpenHtmlWindow('<%=contextPath%>/sales/ordermanage/orderdetail/OrderDetailInfoQuery/orderDetailInfoQuery.do?&orderNo='+value,700,500);
	}   
	//设置文本
	function myText(value,meta,record){
		return String.format(""+record.data.ORDER_YEAR+"."+value);
	} 
	//超链接设置
	function myLink(value,meta,record){
  		return String.format("<a href='#' onclick='searchServiceInfo(\""+ value +"\","+ record.data.VER+")'>[确认]</a>");
	}
	// 修改操作
	function searchServiceInfo(value,ver){
		rowObjNum = window.event.srcElement.parentElement.parentElement.rowIndex;
		rowObj = window.event.srcElement.parentElement.parentElement;
		OpenHtmlWindow('<%=contextPath%>/sales/ordermanage/orderaudit/FleetOrderSure/fleetOrderDetailSureQuery.do?&reqId='+value+'&ver='+ver,900,500);
	}

	function showDate(){
		var startYear = document.getElementById("startYear").value;
		var startWeek = document.getElementById("startWeek").value;
		var endYear = document.getElementById("endYear").value;
		var endWeek = document.getElementById("endWeek").value;

		makeNomalFormCall('<%=contextPath%>/sales/showDate/ShowDateInfo/showDate.json?startYear='+startYear+'&startWeek='+startWeek+'&endYear='+endYear+'&endWeek='+endWeek,showDateInfo,'fm');
	}
	function showDateInfo(json){
		if(json.returnValue == '1'){
			document.getElementById("dateInfo").innerHTML = json.showDate;
		}
	}
</script>
<!--页面列表 end -->
</body>
</html>