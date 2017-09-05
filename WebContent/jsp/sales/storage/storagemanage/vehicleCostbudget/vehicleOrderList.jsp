<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>

<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>

<head>
<%
String contextPath=request.getContextPath();
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title> 车辆成本预算管理 </title>

</head>

<body>
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：&gt;订单关联&gt;财务相关&gt;龙江开票查询
	</div>
<form name="fm" method="post" id="fm">
<!-- 查询条件 begin -->
<table class="table_query" id="subtab" >  
  <tr align="center">
  		<td width="80">票据单号：</td>
  		<td width="100"><input type='text' class='middletxt' name="vOrderId" id="vOrderId"/></td>
  		<td width="80">保存日期：</td>
  		<td width="250">
  			<input name="sdate" type="text" maxlength="20"  class="middle_txt" id="ORG_STORAGE_STARTDATE"  readonly="readonly" /> 
			<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'sdate', false);" /> 
             &nbsp;至&nbsp;
             <input name="edate" type="text" maxlength="20"  class="middle_txt"   id="ORG_STORAGE_ENDDATE" readonly="readonly" /> 
			<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'edate', false);" /> 
  		</td>
  		<td>
  			产地：
  			<select id="area" name="area">
				<c:forEach items="${area }" var="list">
					<option value="${list.AREA_ID}">${list.AREA_NAME}</option>
				</c:forEach>
			</select>
  		</td>
  		<td align="left">
  			 <input type="button" id="queryBtn" class="u-button u-query" value="查询" onclick="__extQuery__(1);" /> 
  		</td>
  		<td>&nbsp;</td>
  </tr>
  <tr>
    <td colspan="6" class="table_query_4Col_input" style="text-align: center">
    	<span style="display:none" id="expMsg">本次共导出<font id="num" color="red"></font>条记录</span>
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
	var url = "<%=contextPath%>/sales/storage/storagemanage/VehicleCostbudget/vehicleOrderQuery.json";
	var title = null;
	var columns = [
				{header: "序号",align:'center',renderer:getIndex},
				{
					header: "操作",align:'center',dataIndex:'V_ORDER_ID', renderer:function(value,metaDate,record){
						return "<a href='javascript:void(0);' onclick=\"viewConnect('"+record.data.V_ORDER_ID+"')\">[车辆明细]</a>";
						//return "<a href='javascript:void(0);' onclick=\"viewConnect('"+record.data.V_ORDER_ID+"')\">[车辆明细]</a>&nbsp;<a href='javascript:void(0);' onclick=\"delConnect('"+record.data.V_ORDER_ID+"')\">[票据作废]</a>";
					}
				},
				{header: "票据单号",dataIndex: 'V_ORDER_ID',align:'center'},
				{header: "车系名称",dataIndex: 'SERIES_NAME',align:'center'},
				{header: "配置名称",dataIndex: 'PACKAGE_NAME',align:'center'},
				{header: "配置代码",dataIndex: 'PACKAGE_CODE',align:'center'},
				{header: "发票号",dataIndex: 'INVOICE_NO',align:'center'},
				{header: "车辆总数",dataIndex: 'V_COUNT',align:'center'},
				{header: "订单总价",dataIndex: 'COST_PRICE',align:'center'},
				{header: "保存日期",dataIndex: 'CREATE_DATE',align:'center'},
				{header: "是否已关联",dataIndex: 'IS_CONNECT',align:'center'}
		      ];
	//初始化    
	function doInit(){
		//日期控件初始化
		__extQuery__(1);
	}
	//全选checkbox
	function myCheckBox(value,metaDate,record){
		return String.format("<input type='checkbox' id='groupIds' name='groupIds' value='" + value + "' />");
	}
	//跳转新增页面
	function add()
	{
		var b=0;
		var arrayObj = new Array(); 
		arrayObj=document.getElementsByName("groupIds");
		for(var i=0;i<arrayObj.length;i++){
			if(arrayObj[i].checked){
				b=1;//有选中
			}
		}
		if(b==0){
			MyAlert("请选择车辆信息！");
			return ;
		}
		fm.action = "<%=contextPath%>/sales/storage/storagemanage/VehicleCostbudget/addVehicleCostbudgetInit.do";
		fm.submit();	
	}
	//清空数据
	function clrTxt(txtId){
    	document.getElementById(txtId).value = "";
    }
	// 导出金税文本
	function exportTxt() {
		var sels = document.getElementsByName('groupIds');
		if(sels.length == 0) {MyAlert('请选择要导出的票据!'); return;}
		document.getElementById('expMsg').style.display = '';
		document.getElementById('num').innerHTML = sels.length;
		
		fm.action = "<%=contextPath%>/sales/storage/storagemanage/VehicleCostbudget/vehicleInvoiceExport.json";
		fm.submit();
	}
	
	function importData() {
		var url = "<%=contextPath%>/sales/storage/storagemanage/VehicleCostbudget/vehicleInvoiceImportInit.do";
		OpenHtmlWindow(url,1000,450);
	}
	
	// 删除单据
	function changeNo(id) { 
		var changeUrl = "<%=contextPath%>/sales/storage/storagemanage/VehicleCostbudget/vehicleInvoiceDel.json";
		makeCall(changeUrl, function(json){
			if (json.Exception) {
				MyAlert(json.Exception.message);
			} else {
				MyAlert('删除单据成功!');
				__extQuery__(1);
			}
		}, {vOrderId : id});
	}
	
	function viewConnect(orderId){
		var viewUrl = "<%=request.getContextPath()%>/jsp/sales/storage/storagemanage/vehicleCostbudget/vehicleConnectedList.jsp?orderId=" + orderId;
		OpenHtmlWindow(viewUrl,1000,450);
	}
	
	function delConnect(orderId) {
		var delUrl = "<%=contextPath%>/sales/storage/storagemanage/VehicleCostbudget/vehicleConnectDel.json";
		
		if(window.confirm("是否要作废该票据?")) {
				makeCall(changeUrl, function(json){
				if (json.Exception) {
					MyAlert(json.Exception.message);
				} else {
					MyAlert('票据作废成功!');
					__extQuery__(1);
				}
			}, {vOrderId : orderId});
		}
	}
</script>
</body>
</html>
