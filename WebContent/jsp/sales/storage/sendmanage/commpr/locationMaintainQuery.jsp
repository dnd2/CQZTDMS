<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>在途位置查询</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：储运管理>发运管理>在途位置查询</div>
<form method="post" name="fm" id="fm">
<div class="form-panel">
	<h2><img src="<%=request.getContextPath()%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>在途位置查询</h2>
<div class="form-body">
<!-- 查询条件 begin -->
	<table class="table_query" >		
		<tr>
			<td class="right">交接单号：</td>
			<td align="left">
				<input type="text" maxlength="20"  datatype="1,is_digit_letter,30" maxlength="30" name="bill_no" id="bill_no" class="middle_txt"/>
			</td>
			<td class="right">承运商：</td>
			<td align="left">
		 	<select name="logi_id" id="logi_id" class="u-select" >
		 		<option value="">-请选择-</option>
				<c:if test="${list_logi!=null}">
					<c:forEach items="${list_logi}" var="list_logi">
						<option value="${list_logi.LOGI_ID}">${list_logi.LOGI_NAME}</option>
					</c:forEach>
				</c:if>
	  		</select>
	  		</td>
			<td class="right">绑定日期：</td>
			<td align="left">
				<input class="short_txt" readonly="readonly"  type="text" id="driver_bind_date_s" name="driver_bind_date_s" onFocus="WdatePicker({el:$dp.$('driver_bind_date_s'), maxDate:'#F{$dp.$D(\'driver_bind_date_e\')}'})"  style="cursor: pointer;width: 80px;"/>&nbsp;至&nbsp;
				<input class="short_txt" readonly="readonly"  type="text" id="driver_bind_date_e" name="driver_bind_date_e" onFocus="WdatePicker({el:$dp.$('driver_bind_date_e'), minDate:'#F{$dp.$D(\'driver_bind_date_s\')}'})"  style="cursor: pointer;width: 80px;"/>
			</td>	
		</tr>
		<tr>
			<td class="right">组板号：</td>
			<td align="left">
		 	<input type="text" maxlength="20"  datatype="1,is_digit_letter,30" maxlength="30" name="bo_no" id="bo_no" class="middle_txt"/>
	  		</td>
			<td class="right">是否交车：</td>
			<td align="left">
		 	   <select name="status" class="u-select" >
		 	      <option value="">-请选择-</option>
		 	      <option value="20521001">在途</option>
		 	      <option value="20521002">已交车</option>
		 	   </select>
	  		</td>
	  		<td class="right">最晚发运日期：</td>
			<td align="left">
				<input class="short_txt" readonly="readonly"  type="text" id="dlv_fy_date_s" name="dlv_fy_date_s" onFocus="WdatePicker({el:$dp.$('dlv_fy_date_s'), maxDate:'#F{$dp.$D(\'dlv_fy_date_e\')}'})"  style="cursor: pointer;width: 80px;"/>&nbsp;至&nbsp;
				<input class="short_txt" readonly="readonly"  type="text" id="dlv_fy_date_e" name="dlv_fy_date_e" onFocus="WdatePicker({el:$dp.$('dlv_fy_date_e'), minDate:'#F{$dp.$D(\'dlv_fy_date_s\')}'})"  style="cursor: pointer;width: 80px;"/>
			</td>	
		</tr>
		<tr>
			<td class="right">批售单号：</td>
			<td align="left">
		 	   <input type="text" maxlength="20"  datatype="1,is_digit_letter,30" maxlength="30" name="order_no" id="order_no" class="middle_txt"/>
			</td>
			<td class="right">司机手机号：</td>
			<td align="left">
		 	<input type="text" maxlength="20"  datatype="1,is_digit_letter,30" maxlength="30" name="driver_phone" id="driver_phone" class="middle_txt"/>
	  		</td>
			<td class="right">车架号：</td>
			<td align="left">
		 	   <input type="text" maxlength="20"  datatype="1,is_digit_letter,30" maxlength="30" name="vin" id="vin" class="middle_txt"/>
			</td>
		</tr>
		 <tr>
			<td colspan="6" class="table_query_4Col_input" style="text-align: center">
				<input name="queryBtn" id="queryBtn" type=button class="u-button u-query" onClick="__extQuery__(1);" value="查询">
				<input name="resetBtn" type="reset" class="u-button u-reset"  value="重置">
				<input name="queryBtn" id="queryBtn" type=button class="normal_btn" onClick="outputExcel();" value="导出">
			</td>
		 </tr>
	</table>
	</div>
</div>
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
	var url = "<%=contextPath%>/sales/storage/sendmanage/OnTheWayAction/locationMaintainQueryInit.json?query=query";
	var title = null;
	var columns = [
					{header: "序号", dataIndex: '', align:'center', renderer:getIndex},
		            {header: "操作",sortable: false, dataIndex: 'DTL_ID', align:'center',renderer:myLink},
	 	            {header: "VIN",dataIndex: 'VIN',align:'center'},
	 	            {header: "经销商/收货仓库",dataIndex: 'DEALER_NAME',align:'center'},
	              	{header: "组板号",dataIndex: 'BO_NO',align:'center'},
	                //{header: "交接单号",dataIndex: 'BILL_NO',align:'center'},
	                {
						header: "交接单号", dataIndex: 'BILL_NO', align:'center', 
						renderer: function(value, metaData, record) {
							var url = '<%=contextPath%>/sales/storage/sendmanage/DlvWayBillManage/showBillDetailInit.do?billId=' + record.data.BILL_ID;
							return "<a href='javascript:;' onclick='viewOrderInfo(\""+url+"\")'>"+value+"</a>";
						}
					},
	                {header: "批售单号",dataIndex: 'ORDER_NO',align:'center'},
	                {header: "调拨单号",dataIndex: 'ORDER_NO',align:'center'},
	                {header: "车型",dataIndex: 'MODEL_NAME',align:'center'},
	                {header: "配置",dataIndex: 'PACKAGE_NAME',align:'center'},
	                {header: "颜色",dataIndex: 'COLOR_NAME',align:'center'},
	                {header: "是否交车",dataIndex: 'STATUS',align:'center',renderer:getItemValue},
	                {header: "司机手机号",dataIndex: 'DRIVER_PHONE',align:'center'},
	                {header: "绑定日期",dataIndex: 'DRIVER_BIND_DATE',align:'center'},
	                {header: "位置上报日期",dataIndex: 'REPORT_DATE',align:'center'},
	                {header: "最新上报地址",dataIndex: 'REPORT_ADDRESS',align:'center'},
	                {header: "详细地址",dataIndex: 'ADDRESS',align:'center'}
// 	                {header: "发运经销商电话",dataIndex: 'PHONE',align:'center'},
// 	                {header: "发运经销商联系人",dataIndex: 'LINK_MAN',align:'center'},
// 	                {header: "发运地址",dataIndex: 'ADDRESS',align:'center'},
// 	                {header: "发运省市",dataIndex: 'SEND_ADD',align:'center'},
// 	                {header: "运单生成日期",dataIndex: 'BILL_CRT_DATE',align:'center'},
					
		      ];
	//超链接设置
    function myLink(value,meta,record){
    	return String.format("<a href='javascript:void(0);' class='u-anchor' onclick='toDetailCheck(\""+ value +"\")'>查看</a>");
	}
	//明细链接
	function toDetailCheck(dtl_id){
		OpenHtmlWindow('<%=request.getContextPath()%>/sales/storage/sendmanage/OnTheWayAction/showOntheWayAddress.do?dtl_id='+dtl_id,800,300);
<%-- 		//OpenHtmlWindow('<%=contextPath%>/sales/ordermanage/orderreport/SpecialNeedQuery/specialNeedDetailQuery.do?&reqId='+value+'&buttonFalg='+buttonFalg,800,500); --%>
<%-- 		window.location.href = '<%=contextPath%>/sales/storage/sendmanage/CommprManage/locationMaintainDetailInit.do?billId='+value+'&COMMO=1'; --%>
	}
	
	function clrTxt(txtId){
    	document.getElementById(txtId).value = "";
    }
	function txtClr(txtId){
    	document.getElementById(txtId).value = "";
    }

	//初始化    
	function doInit(){
		__extQuery__(1);
		  //初始化时间控件
		 genLocSel('txt1','','');//支持火狐
	}
	//清空数据
	function txtClr(valueId1,valueId2) {
		document.getElementById(valueId1).value = '' ;
		document.getElementById(valueId2).value = '' ;
	}
	
	function outputExcel(){
		fm.action='<%=contextPath%>/sales/storage/sendmanage/OnTheWayAction/locationMaintainQueryInit.do?query=excel';  
		fm.submit();
	}
	function viewOrderInfo(url)
	{
		OpenHtmlWindow(url,1000,450);
	}
</script>
<!--页面列表 end -->
</body>
</html>