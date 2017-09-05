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
</script>
</head>

<body>
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;发运订单查询</div>
<form method="post" name="fm" id="fm">
<table border="0" align="center" class="table_query">
  <tr>
    <td align="right" nowrap="nowrap" class="table_query_2Col_label_6Letter">起止时间：</td>
    <td align="left" nowrap="nowrap" class="table_add_2Col_input">
    	<div align="left">
       		<input name="beginTime" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);">
       		&nbsp;至&nbsp;
       		<input name="endTime" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);">
   		</div>
    </td>
    <td align="right" nowrap="nowrap" class="table_query_2Col_label_6Letter"> 选择业务范围：</td>
    <td align="left" nowrap="nowrap" class="table_edit_2Col_input">
    	<select name="areaId" class="short_sel" id="areaId">
    		<option value="">-请选择-</option>
			<c:if test="${areaList!=null}">
				<c:forEach items="${areaList}" var="list">
					<option value="${list.AREA_ID}">${list.AREA_NAME}</option>
				</c:forEach>
			</c:if>
		</select>
	</td>
    </tr>
   <tr>
    <td align="right" class="table_query_2Col_label_6Letter">运送方式：</td>
    <td class="table_edit_2Col_input">
    	<script type="text/javascript">
 			genSelBoxExp("transType",<%=Constant.TRANSPORT_TYPE%>,"",true,"short_sel","","false",'');
		</script>
    </td>
    <td height="25" align="right" class="table_query_2Col_label_6Letter">状态：</td>
    <td class="table_edit_2Col_input">
    	<script type="text/javascript">
 			genSelBoxExp("reqStatus",<%=Constant.ORDER_REQ_STATUS%>,"",true,"short_sel","","false",'');
		</script>    	
    </td>
    </tr>
    <tr>
	    <td align="right" class="table_query_2Col_label_6Letter">订单号：</td>
	    <td class="table_edit_2Col_input">
	    	<input name="orderNo" type="text" class="middle_txt"  value=""/>
	    </td>
	    <td align="right" nowrap="nowrap" class="table_query_2Col_label_6Letter">原始订单号：</td>
	    <td class="table_add_2Col_input">
	    	<input name="initOrderNo" type="text" class="middle_txt"  value=""/>
	    </td>
   	</tr>
    <tr>
	    <td align="right" class="table_query_2Col_label_6Letter">发运订单号：</td>
	    <td class="table_edit_2Col_input">
	    	<input name="dlvryOrderNo" type="text" class="middle_txt"  value=""/>
	    </td>
	    <td align="right" nowrap="nowrap" class="table_query_2Col_label_6Letter">是否代交车</td>
	    <td class="table_add_2Col_input">
		    <script type="text/javascript">
	 			genSelBoxExp("isFleet",<%=Constant.IF_TYPE%>,"",true,"short_sel","","false",'');
			</script> 
		</td>
   	</tr>
	<tr>
	  <td align="right" class="table_query_2Col_label_6Letter">订单类型：</td>
	  <td class="table_add_2Col_input">
		<script type="text/javascript">
	 			genSelBoxExp("orderType",<%=Constant.ORDER_TYPE%>,"",true,"short_sel","","false",'');
			</script> 
		</td>
	  <td align="right" class="table_query_2Col_label_6Letter">&nbsp;</td>
	  <td class="table_edit_2Col_input">
	  	  <input name="queryBtn" type="button" class="normal_btn" onclick="__extQuery__(1);" value="查询" />
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
	var url = "<%=contextPath%>/sales/ordermanage/orderquery/DlvryOrderQuery/dlvryOrderQuery.json";
				
	var title = null;

	var columns = [
				{header: "采购单位", dataIndex: 'DEALER_NAME',align:'center'},
				{header: "开票单位代码", dataIndex: 'DEALER_CODE',align:'center'},
				{header: "开票单位", dataIndex: 'DEALER_NAME1',align:'center'},
				{header: "订单类型", dataIndex: 'ORDER_TYPE', align:'center',renderer:getItemValue},
				{header: "启票时间", dataIndex: 'REQ_DATE', align:'center'},
				{header: "启票订单号码", dataIndex: 'DLVRY_REQ_NO', align:'center',renderer:myLink},
				{header: "订单号码", dataIndex: 'ORDER_NO', align:'center'},
				{header: "原订单号码", dataIndex: 'INIT_ORDER_NO', align:'center'},
				{header: "申请数量", dataIndex: 'REQ_TOTAL_AMOUNT', align:'center'},
				{header: "保留资源数量", dataIndex: 'RESERVE_AMOUNT', align:'center'},
				{header: "状态", dataIndex: 'REQ_STATUS', align:'center',renderer:getItemValue},
				{header: "运送方式", dataIndex: 'DELIVERY_TYPE', align:'center',renderer:getItemValue},
				{header: "代交车", dataIndex: 'IS_FLEET', align:'center'},
				{header: "收货方", dataIndex: 'DEALER_NAME2',align:'center'}
		      ];
		      
//设置超链接  begin      
	
	//修改的超链接
	function myLink(value,meta,record){
  		return String.format("<a href='#' onclick='searchServiceInfo("+ record.data.REQ_ID +","+ record.data.ORDER_TYPE +")'>"+ value +"</a>");
	}
	
	function searchServiceInfo(value,value2){
		$('fm').action= "<%=contextPath%>/sales/ordermanage/orderquery/DlvryOrderQuery/dlvryOrderDetail.do?reqId="+value+"&orderType="+value2;
		$('fm').submit();
	}
//设置超链接 end
	
</script>
<!--页面列表 end -->


</body>
</html>
