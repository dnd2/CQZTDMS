<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>


<%@ page import="java.util.*"%>

<jsp:include page="${path}/common/jsp_head_new.jsp" />
<%
	String contextPath = request.getContextPath();
	//String expStatusCode = Constant.ORDER_STATUS_00 + "," + Constant.ORDER_STATUS_01 + "," + Constant.ORDER_STATUS_03;
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title>批售订单查询</title>
<script type="text/javascript">
	function disabledGroupSel() {
		document.getElementById("button2").disabled = "";
		document.getElementById("materialCode").disabled = "";

		document.getElementById("button1").disabled = "disabled";
		document.getElementById("groupCode").disabled = "disabled";
	}

	function disabledMaterialSel() {
		document.getElementById("button1").disabled = "";
		document.getElementById("groupCode").disabled = "";
		
		document.getElementById("button2").disabled = "disabled";
		document.getElementById("materialCode").disabled = "disabled";
	}

	function downLoad() 
	{
		document.getElementById('fm').action= "<%=request.getContextPath()%>/sales/storage/sendmanage/BatchOrderManage/queryBatchOrder.do?common=1";
		document.getElementById('fm').submit();
	}
	
	//清空数据
	function txtClr(valueId1,valueId2) {
		document.getElementById(valueId1).value = '' ;
		document.getElementById(valueId2).value = '' ;
	}
</script>
</head>
<body onunload="javascript:destoryPrototype()" onload="">
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理>发运管理>批售订单查询</div>
<div class="form-panel">
	<h2><img src="<%=request.getContextPath()%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>批售订单查询</h2>
<div class="form-body">
	<form method="post" name="fm" id="fm">
		<table class="table_query">
			<tr>				
				<td class="right" width="15%">收货经销商：</td>
				<td>
					<input name="dealerName" type="text" maxlength="20"  id="dealerName" class="middle_txt" value=""  readonly="readonly"/>
		            <input name="dlbtn" type="button" class="mini_btn" onclick="showOrgDealer('dealerCode', '', 'true', '', 'true','','<%=Constant.DEALER_TYPE_DVS %>,<%=Constant.DEALER_TYPE_DP %>','dealerName');" value="..." />
		    		<input type="button" class="normal_btn" onclick="txtClr('dealerCode','dealerName');" value="清 空" id="clrBtn" />
					<input name="dealerCode" type="hidden" id="dealerCode" class="middle_txt" value="" />
				</td>
				<td class="right" width="15%">发运仓库：</td>
				<td>
				  <select name="warehouseId" id="warehouseId" class="u-select" >
				 	<option value="">-请选择-</option>
						<c:if test="${list!=null}">
							<c:forEach items="${list}" var="list">
								<option value="${list.WAREHOUSE_ID}">${list.WAREHOUSE_NAME}</option>
							</c:forEach>
						</c:if>
			  		</select>
				</td>
			</tr>
			<tr>				
				<td class="right" width="15%">批售单号：</td>
				<td>
					<input name="orderNo" id="orderNo" type="text" maxlength="20"  class="middle_txt">
				</td>
				<td class="right" width="15%">发运方式：</td>
				<td>
					<script type="text/javascript">
						genSelBoxExp("transType",<%=Constant.TRANSPORT_TYPE%>,"-1",true,"u-select",'',"false",'');
					</script>
				</td>
			</tr>
			<tr>
				<td class="right" width="15%">申请单号：</td>
				<td>
					<input name="reqNo" id="reqNo" type="text" maxlength="20"  class="middle_txt">
				</td>
				<td class="right" width="15%">订单状态：</td>
				<td>
					<script type="text/javascript">
						genSelBoxExp("orderStatus",<%=Constant.ORDER_STATUS %>,"-1",true,"u-select",'',"false",'');
					</script>
				</td>
			</tr>
			<tr>
				<td class="right" width="15%">申请日期：</td>
				<td>
					<input class="short_txt" readonly="readonly"  type="text" id="subStartdate" name="subStartdate" onFocus="WdatePicker({el:$dp.$('subStartdate'), maxDate:'#F{$dp.$D(\'subEndDate\')}'})"  style="cursor: pointer;width: 80px;"/>&nbsp;至&nbsp;
					<input class="short_txt" readonly="readonly"  type="text" id="subEndDate" name="subEndDate" onFocus="WdatePicker({el:$dp.$('subEndDate'), minDate:'#F{$dp.$D(\'subStartdate\')}'})"  style="cursor: pointer;width: 80px;"/>
				</td>
				<td class="right" width="15%">最晚到货日期：</td>
				<td>
					<input class="short_txt" readonly="readonly"  type="text" id="lastStartdate" name="lastStartdate" onFocus="WdatePicker({el:$dp.$('lastStartdate'), maxDate:'#F{$dp.$D(\'lastEndDate\')}'})"  style="cursor: pointer;width: 80px;"/>&nbsp;至&nbsp;
					<input class="short_txt" readonly="readonly"  type="text" id="lastEndDate" name="lastEndDate" onFocus="WdatePicker({el:$dp.$('lastEndDate'), minDate:'#F{$dp.$D(\'lastStartdate\')}'})"  style="cursor: pointer;width: 80px;"/>
				</td>
				<td></td>
				<td>
				</td>
			</tr>
			<tr>
				<td colspan="4" class="table_query_4Col_input" style="text-align: center">
					<input type="button" value="查询" id="queryBtn" class="u-button u-query" onclick="__extQuery__(1);"/>&nbsp;
					<input type="reset" class="u-button u-reset" id="resetButton"  value="重置"/> 
					<input type="button" value="导出" class="normal_btn" onclick="downLoad()"/>
				</td>
			</tr>
		</table>

		<!-- 分页 begin -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		<!-- 分页 end -->
	</form>

	<script>
		var myPage;
	
		var url = "<%=request.getContextPath()%>/sales/storage/sendmanage/BatchOrderManage/queryBatchOrder.json";
	
		var title = null;
	
		//设置列名属性
		var columns = [
				{header: "序号", dataIndex: '', align:'center', renderer:getIndex},
				{
					header: "批售单号", dataIndex: 'ORD_NO', align:'center', 
					renderer: function(value, metaData, record) {
						var url = '<%=contextPath%>/sales/storage/sendmanage/BatchOrderManage/showOrderReport.do?orderId=' + record.data.REQ_ID;
						return "<a href='javascript:;' onclick='viewOrderInfo(\""+url+"\")'>"+value+"</a>";
					}
				},
				 {header: "申请单号", dataIndex: 'REQ_NO', align:'center'},
				 {header: "经销商简称", dataIndex: 'DEALER_NAME', align:'center'},
				 {header: "发运仓库", dataIndex: 'WAREHOUSE_NAME', align:'center'},
				 {header: "发运方式", dataIndex: 'DELIVERY_TYPE', align:'center', renderer:getItemValue},
				 {header: "申请日期", dataIndex: 'SUB_DATE', align:'center'},
				 {header: "最晚到货日期", dataIndex: 'JJ_DATE', align:'center'},
				 {header: "申请量", dataIndex: 'SUB_NUM', align:'center'},
				 {header: "分派量", dataIndex: 'FP_NUM', align:'center'},
				 {header: "组板量", dataIndex: 'BD_NUM', align:'center'},
				 {header: "发运量", dataIndex: 'FY_NUM', align:'center'},
				 {header: "交接量", dataIndex: 'JJ_NUM', align:'center'},
				 {header: "验收量", dataIndex: 'YS_NUM', align:'center'},
				 {header: "在途量", dataIndex: 'ZT_NUM', align:'center'},
				 {header: "订单状态", dataIndex: 'DLV_STATUS', align:'center', renderer:getItemValue}
		];
		
		function viewOrderInfo(url)
		{
			OpenHtmlWindow(url,1000,450);
		}
		
		
	</script>
</body>
</html>


