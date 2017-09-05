<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>月度常规订单提报</title>
<script type="text/javascript">
function doInit(){
	var timeFlag = ${timeFlag};
	if(timeFlag+""=="2"){
		document.getElementById("queryBtn").disabled = "disabled";
		document.getElementById("queryBtn1").disabled = "disabled";
		MyAlert("已过提报时间("+${beginDate }+"日-"+${endDate }+"日)"+"，无法提报订单!");
	}
}
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车销售 > 销售订单管理 > 订单提报 > 月度常规订单提报</div>
<form method="POST" name="fm" id="fm">
  <table class="table_query" align=center width="95%">
	<tr>
      <td align="right" nowrap >订单月度：</td>
      <td nowrap>
		    <select name="orderMonth"  id="orderMonth" >
		      <c:forEach items="${dateList}" var="po">
					<option value="${po.code}">${po.name}</option>
			  </c:forEach>
	        </select>
        </td>
      <td align="left" nowrap >提报起止日期:${beginDate }日-${endDate }日</td>
      <td align="left" nowrap ></td>
      <td align="right" nowrap >业务范围：</td>
      <TD class="table_query_2Col_input" nowrap>
			<select name="areaId">
				<c:forEach items="${areaList}" var="po">
					<option value="${po.AREA_ID}|${po.DEALER_ID}|${po.AREA_NAME}">${po.AREA_NAME}</option>
				</c:forEach>
            </select>
        </TD>
      <td align="center">
      	<input id="queryBtn" name="button2" type=button class="cssbutton" onClick="checkQuery();" value="查询">
      	<input id="queryBtn1" name="button1" type=button class="cssbutton" onClick="check_addMonthreportInit();" value="新增">
      	<!-- 用户选择的业务范围名称 -->
      	<input type="hidden" id="areaName" name="areaName" />
      	<!-- 本月订单是否已提交 -->
      	<input type="hidden" id="isCommited" name="isCommited" />
      </td>
    </tr>
  </table>
  
	<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    <!--分页 end -->
</form>
<script type="text/javascript"> 

    function checkQuery(){
    	makeNomalFormCall('<%=request.getContextPath()%>/sales/ordermanage/orderreport/MonthOrderReport/check_addMonthreportInit.json',showCheckQuery,'fm');
    }

    function showCheckQuery(json){
    	if(json.returnValue =='2'){
			MyAlert("本月订单已提交,无法再次操作！");
			return;
		}else{
			__extQuery__(1);
		}
    }
	var myPage;
	//查询路径
	var url = "<%=request.getContextPath()%>/sales/ordermanage/orderreport/MonthOrderReport/getCanMonthReport.json";
	var title = null;
	var columns = [
				{header: "销售订单号", dataIndex: 'ORDER_NO', align:'center'},
				{header: "订单月度", dataIndex: 'ORDER_MONTH', align:'center'},
				{header: "业务范围", dataIndex: 'AREA_NAME', align:'center'},
				{header: "订单状态", dataIndex: 'ORDER_STATUS', align:'center',renderer:getItemValue},
				{header: "提报数量", dataIndex: 'ORDER_AMONUT', align:'center'},
				{id:'action',header: "操作",sortable: false,dataIndex: 'ORDER_ID',renderer:myLink ,align:'center'}
		      ];	
	
	
	//修改的超链接
	function myLink(value,meta,record){
		var data = record.data;
		var order_status = data.ORDER_STATUS;
		if(order_status+"" == "10211004"){
			return String.format("<a href='#' onclick='toModify(\""+ data.ORDER_ID +"\",\""+ data.ORDER_NO +"\")'>[修改]</a>"+
					"<a href='#' onclick='toSubmit(\""+ data.ORDER_ID +"\",\""+ data.ORDER_NO +"\",\""+ data.AREA_ID +"\",\""+ data.ORDER_AMONUT +"\")'>[提报]</a>");
		}else{
			return String.format("<a href='#' onclick='toModify(\""+ data.ORDER_ID +"\",\""+ data.ORDER_NO +"\")'>[修改]</a>"+
					"<a href='#' onclick='toDel(\""+ data.ORDER_ID +"\")'>[删除]</a>"+
					"<a href='#' onclick='toSubmit(\""+ data.ORDER_ID +"\",\""+ data.ORDER_NO +"\",\""+ data.AREA_ID +"\",\""+ data.ORDER_AMONUT +"\")'>[提报]</a>");
		}
		
	}
	
	function toModify(order_id,order_no){
		var areaId = document.getElementById("areaId").value;
		var areaIds = areaId.split("|");
		document.getElementById("areaName").value = areaIds[2];
		$('fm').action= "<%=request.getContextPath()%>/sales/ordermanage/orderreport/MonthOrderReport/updateMonthreportInit.do?order_id="+order_id+'&order_no='+order_no;
		$('fm').submit();
		
	}
	function check_addMonthreportInit(){
		makeNomalFormCall('<%=request.getContextPath()%>/sales/ordermanage/orderreport/MonthOrderReport/check_addMonthreportInit.json',showCanReport,'fm');
	}
	function showCanReport(json){
		if(json.returnValue =='2'){
			MyAlert("本月订单已提交,无法再次操作！");
			return;
		}else{
			$('fm').action= "<%=request.getContextPath()%>/sales/ordermanage/orderreport/MonthOrderReport/addMonthreportInit.do";
			$('fm').submit();
		}
	}

	function toDel(order_id){
		MyConfirm("是否删除?",toDelAction,[order_id]);
	}
	function toDelAction(order_id){
		makeNomalFormCall('<%=request.getContextPath()%>/sales/ordermanage/orderreport/MonthOrderReport/delOrder.json?order_id='+order_id,showDelInfo,'fm');
	}
	function showDelInfo(json){
		if(json.returnValue == '1'){
			MyAlert("删除成功");
			__extQuery__(1);
		}else{
			MyAlert("删除失败，请重新操作或联系管理员!");
		}
	}

	function toSubmit(order_id,order_no,area_id,order_amonut){
		MyConfirm("是否提交?",toSubmitAction,[order_id,order_no,area_id,order_amonut]);
	}

	function toSubmitAction(order_id,order_no,area_id,order_amonut){
		makeNomalFormCall('<%=request.getContextPath()%>/sales/ordermanage/orderreport/MonthOrderReport/orderCommit.json?order_id='+order_id+'&order_no='+order_no+'&area_id='+area_id+'&order_amonut='+order_amonut,showSubInfo,'fm');
	}
	function showSubInfo(json){
		if(json.returnValue == '1'){
			MyAlert("提交成功");
			__extQuery__(1);
		}else{
			MyAlert("提交失败，请重新操作或联系管理员!");
		}
	}
</script>
</body>
</html>
