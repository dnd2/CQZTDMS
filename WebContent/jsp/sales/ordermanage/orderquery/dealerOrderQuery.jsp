<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<% String contextPath = request.getContextPath(); %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>销售订单查询</title>
<script type="text/javascript">
	function doInit()
	{
   		loadcalendar();  //初始化时间控件
	}
	function txtClr(valueId) {
		document.getElementById(valueId).value = '' ;
	}
	function clrTxt(txtId){
    	document.getElementById(txtId).value = "";
    }
</script>
</head>

<body>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;代交车查询</div>
<form method="post" name="fm" id="fm">
<table border="0" align="center" class="table_query">
  <tr>
    <td align="right" >起止时间：</td>
    <td align="left" nowrap="nowrap" class="table_add_2Col_input">
    	<div align="left">
       		<input name="startDate" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);">
       		&nbsp;至&nbsp;
       		<input name="endDate" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);">
   		</div>
    </td>
    <td align="right">发运单号：</td>
    <td class="table_edit_2Col_input">
    	<input name="orderNo" type="text" class="middle_txt"  value=""/>
    </td>
	</tr>
	<tr>
	
	<td align="right">采购经销商：</td>
			<td align="left">
				<input type="text" class="middle_txt" name="dealerCode" size="15" value="" readonly="readonly" id="orderOrgCode"/>
				<input name="button3" type="button" class="mini_btn" onclick="showOrgDealer('orderOrgCode', '', 'false', '', 'true')" value="..." />
           		<input class="cssbutton" type="button" value="清空" onclick="clrTxt('dealerCode');"/>
			</td>
   </tr>
    <tr>
	    <td align="right" class="table_query_2Col_label_6Letter"></td>
	    <td class="table_edit_2Col_input"></td>
	  	<td align="right"></td>
		<td>
			<input name="button" id="queryBtn" type="button"  class="normal_btn"	onclick="__extQuery__(1);" value="查询" />
			<input name="button" type="button"  class="normal_btn"	onclick="doExport();" value="下载" />
		</td>
  	</tr>
</table>
  <!-- 查询条件 end -->
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end -->
  </form> 
  <form name="form1" style="display:none">
   <table class="table_list" id="table1" >
  	  <tr>
  	  	<th align="left">
    		<input class="normal_btn" type="button" value="上 报" onclick="putForwordConfirm()">&nbsp;
    		<input class="normal_btn" type="button" value="删 除" onclick="deleteConfirm()">
       </th>
  	  </tr>
   </table>
  </form>
<!--页面列表 begin -->
<script type="text/javascript" >

	var myPage;
//查询路径
	var url = "<%=contextPath%>/sales/ordermanage/orderquery/DealerDaQuotaQuery/commandQuery.json";
				
	var title = null;

	var columns = [
				{header: "采购经销商代码", dataIndex: 'DEALER_CODE',align:'center'},
				{header: "采购单位", dataIndex: 'DEALER_NAME',align:'center'},
				{header: "发运日期", dataIndex: 'DELIVERY_DATE',align:'center'},
				{header: "发运数量", dataIndex: 'DELIVERY_AMOUNT',align:'center'},
				{header: "发运单号", dataIndex: 'DELIVERY_NO',align:'center'},
				{header: "状态", dataIndex: 'ORDER_STATUS', align:'center',renderer:getItemValue},
				{header: "集团客户", dataIndex: 'FLEET_NAME', align:'center'},
				{header: "集团客户地址", dataIndex: 'FLEET_ADDRESS',align:'center'}
		      ];
		      
//设置超链接  begin      
	
	//修改的超链接
	function myLink(value,meta,record){
  		return String.format("<a href='#' onclick='searchServiceInfo("+ record.data.REQ_ID +","+ record.data.ORDER_TYPE +")'>"+ value +"</a>");
	}
	
	function searchServiceInfo(value,value2){
		$('fm').action= "<%=contextPath%>/sales/ordermanage/delivery/OrderCommandQuery/commandQueryInfo.do?reqId="+value+"&orderType="+value2;
		$('fm').submit();
	}
	
	function doExport(){
		$('fm').action= "<%=request.getContextPath()%>/sales/ordermanage/orderquery/DealerDaQuotaQuery/commandQueryDownLoad.json";
		$('fm').submit();
	}
//设置超链接 end
	
</script>
<!--页面列表 end -->


</body>
</html>
