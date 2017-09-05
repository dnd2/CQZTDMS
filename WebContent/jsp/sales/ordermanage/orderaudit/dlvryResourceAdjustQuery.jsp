<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>发运指令资源批次调整</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 销售订单管理 &gt; 订单审核 &gt;发运指令资源批次调整</div>
<form method="post" name="fm" id="fm">
<!-- 查询条件 begin -->
	<table class="table_query" >
		<tr>
			<!--<td width="13%"></td>
			<td align="right">订单周度：</td>
			<td align="left">
				<select name="orderYearWeek" id="orderYearWeek">
					<c:forEach items="${dateList}" var="list">
						<option value="${list.code}"><c:out value="${list.name}"/></option>
					</c:forEach>
				</select>
			</td>
			-->
			<td width="13%"></td>
			<td align="right">选择区域：</td>
			<td align="left">
				<input type="text" id="orgCode" name="orgCode" class="middle_txt" value="" size="15" />
				<input name="obtn" id="obtn"  class="mini_btn" type="button" value="&hellip;" onclick="showOrg('orgCode','' ,'true','${orgId}');"/>
				<input class="normal_btn" type="button" value="清空" onclick="clrTxt('orgCode');"/>
			</td>
			<td align="right">选择经销商：</td>
			<td colspan="1">
				<input type="text"  name="dealerCode" class="middle_txt" size="15" value="" id="dealerCode"/>
				<input name="button2" type="button" class="mini_btn" onclick="showOrgDealer('dealerCode','','true');" value="..." />
				<input class="normal_btn" type="button" value="清空" onclick="clrTxt('dealerCode');"/>
			</td>
			<td width="13%"></td>
		</tr>
		<tr>
			<td></td>
			<td align="right">业务范围：</td>
			<td align="left">
				<select name="areaId" id="areaId" class="short_sel">
					<option value="-1">-请选择-</option>
					<c:forEach items="${areaBusList}" var="list">
						<option value="${list.AREA_ID}"><c:out value="${list.AREA_NAME}"/></option>
					</c:forEach>
				</select><input type="hidden" name="area" id="area"/>
			</td>
			<td align="right">选择物料组：</td>
			<td>
				<input type="text"  name="groupCode" class="middle_txt" size="15" readonly="readonly" value="" />
				<input type="hidden" name="groupName" size="20" id="groupName" value="" />
				<input name="button3" type="button" class="mini_btn" onclick="showMaterialGroup('groupCode','groupName','true','3');" value="..." />
				<input class="normal_btn" type="button" value="清空" onclick="clrTxt('groupCode');"/>
			</td>
			<td></td>
		</tr>
		<tr>
			<td></td>
			<td align="right">订单类型：</TD>
			<td>
				<label>
					<script type="text/javascript">
						genSelBoxExp("orderType",<%=Constant.ORDER_TYPE%>,"",true,"short_sel","","false",'');
					</script>
				</label>
			</td>
			<td align="right">销售订单号：</TD>
			<td><input type="text" name="orderNo" class="middle_txt"  value=""/></td>
			<td></td>
		</tr>
		<tr>
			<td></td>
			<td align="right">发运状态：</TD>
			<td>
				<label>
					<script type="text/javascript">
						genSelBoxExp("deliveryStatus",<%=Constant.DELIVERY_STATUS%>,"",true,"short_sel","","false",'<%=Constant.DELIVERY_STATUS_01%>,<%=Constant.DELIVERY_STATUS_02%>,<%=Constant.DELIVERY_STATUS_03%>,<%=Constant.DELIVERY_STATUS_05%>,<%=Constant.DELIVERY_STATUS_09%>,<%=Constant.DELIVERY_STATUS_12%>,<%=Constant.DELIVERY_STATUS_13%>,<%=Constant.DELIVERY_STATUS_06%>');
					</script>
				</label>
			</td>
			<td align="right">发运单号：</TD>
			<td><input type="text" name="dlvNo" class="middle_txt"  value=""/></td>
			<td></td>
		</tr>
		<tr>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td align="right">
				<input type="hidden" name="isCheck" id="isCheck" value="${isCheck}"/>
				<input name="queryBtn" type=button class="cssbutton" onClick="__extQuery__(1);" value="查询">
			</td>
			<td></td>
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
	var url = "<%=contextPath%>/sales/ordermanage/orderaudit/DlvryResourceAdjust/dlvryResourceAdjustQuery.json";
	var title = null;
	var columns = [
				{header: "经销商代码",dataIndex: 'DEALER_CODE',align:'center'},
				{header: "经销商名称",dataIndex: 'DEALER_NAME',align:'center'},
				{id:'action',header: "销售订单号", dataIndex: 'ORDER_NO', align:'center',renderer:myDetail},
				{header: "发运单号",dataIndex: 'DELIVERY_NO',align:'center'},
				{header: "提报日期", dataIndex: 'RAISE_DATE', align:'center'},
				//{header: "订单周度", dataIndex: 'ORDER_WEEK', align:'center',renderer:myText},
				{header: "订单类型", dataIndex: 'ORDER_TYPE', align:'center',renderer:getItemValue},
				{header: "发运状态", dataIndex: 'DELIVERY_STATUS', align:'center',renderer:getItemValue},
				{header: "申请数量", dataIndex: 'REQ_TOTAL_AMOUNT', align:'center'},
				{header: "已保留数量", dataIndex: 'RESERVE_TOTAL_AMOUNT', align:'center'},
				{header: "操作",sortable: false, dataIndex: 'ORDER_ID', align:'center',renderer:myLink}
		      ];
	//超链接设置
	function myLink(value,meta,record){
		var orderType = record.data.ORDER_TYPE;
		var reqId = record.data.REQ_ID;
  		return String.format("<a href='#' onclick='toDetailCheck(\""+ value +"\",\""+ orderType +"\",\""+ reqId +"\")'>[修改批次]</a><input type='hidden' name='ver' value='"+record.data.VER+"'/>");
	}
	function myDetail(value,meta,record){
		return String.format("<a href='#' onclick='orderDetailInfo(\""+ value +"\")'>"+value+"</a>");
	}
	function orderDetailInfo(value){
		OpenHtmlWindow('<%=contextPath%>/sales/ordermanage/orderdetail/OrderDetailInfoQuery/orderDetailInfoQuery.do?&orderNo='+value,700,500);
	}
	//调整链接
	function toDetailCheck(value1, value2, value3){
		$('fm').action= '<%=request.getContextPath()%>/sales/ordermanage/orderaudit/DlvryResourceAdjust/dlvryResourceAdjustDetailQuery.do?orderId='+value1+'&orderType='+value2+'&reqId='+value3;
	 	$('fm').submit();
	}
	//设置年周
	function myText(value,meta,record){
		var data = record.data;
  		return String.format(data.ORDER_YEAR+"."+value);
	}
	//初始化    
	function doInit(){
		if('${dealerCode}')
			document.getElementById("dealerCode").value='${dealerCode}';
		if('${areaId}')
			document.getElementById("areaId").value='${areaId}';
		if('${groupCode}')
			document.getElementById("groupCode").value='${groupCode}';
		if('${orderType}')
			document.getElementById("orderType").value='${orderType}';
		if('${orderNo}')
			document.getElementById("orderNo").value='${orderNo}';
		if('${deliveryStatus}')
			document.getElementById("deliveryStatus").value='${deliveryStatus}';
		if('${orgCode}')
			document.getElementById("orgCode").value='${orgCode}';
		var area = "";
		<c:forEach items="${areaBusList}" var="list">
			var areaId = <c:out value="${list.AREA_ID}"/>
			if(area==""){
				area = areaId;
			}else{
				area = areaId+','+area;
			}
		</c:forEach>
		document.getElementById("area").value=area;
		//__extQuery__(1);
	}
	function clrTxt(txtId){
    	document.getElementById(txtId).value = "";
    }
</script>
<!--页面列表 end -->
</body>
</html>