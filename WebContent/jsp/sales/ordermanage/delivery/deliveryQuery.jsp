<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>订单发运查询</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 销售订单管理 &gt; 订单发运 &gt;订单发运查询</div>
<form method="post" name="fm" id="fm">
<!-- 查询条件 begin -->
	<table class="table_query" >
		<!--<tr>
			<td width="13%"></td>
			<td align="right" nowrap="nowrap">订单周度：</td>
			<td align="left" nowrap="nowrap">
				<select id="startYear"  name="startYear" onchange="showDate();">
			       <c:forEach items="${yearList}" var="list1">
				       	<option value="${list1.orderYear}" <c:if test="${list1.orderYear==year}">selected</c:if>>
				       		<c:out value="${list1.orderYear}"/>
				       	</option>
			       </c:forEach>
				</select>年
				<select id="startWeek" name="startWeek" onchange="showDate();">
					<c:forEach items="${weekList}" var="list2">
			      	 	<option value="${list2.orderWeek}" <c:if test="${list2.orderWeek==week}">selected</c:if>>
			      	 		<c:out value="${list2.orderWeek}"/>
			      	 	</option>
			       </c:forEach>
				</select>周&nbsp;至
				<select id="endYear" name="endYear" onchange="showDate();">
				   <c:forEach items="${yearList}" var="list3">
			      	 	<option value="${list3.orderYear}" <c:if test="${list3.orderYear==year}">selected</c:if>>
			      	 		<c:out value="${list3.orderYear}"/>
			      	 	</option>
			       </c:forEach>
				</select>年
				<select id="endWeek" name="endWeek" onchange="showDate();">
					<c:forEach items="${weekList}" var="list4">
			      	 	<option value="${list4.orderWeek}" <c:if test="${list4.orderWeek==week}">selected</c:if>>
			      	 		<c:out value="${list4.orderWeek}"/>
			      	 	</option>
			       </c:forEach>  
				</select>周
				<span id="dateInfo" class="innerHTMLStrong"></span>
			</td>
			<td width="13%" align="right">订单类型：</TD>
			<td width="35%">
				<label>
					<script type="text/javascript">
						genSelBoxExp("orderType",<%=Constant.ORDER_TYPE%>,"-1",true,"short_sel",'',"false",'');
					</script>
				</label>
			</td>
		</tr>
		-->
		<tr>
			<td></td>
			<td align="right">发运日期：</td>
			<td align="left">
				<input class="short_txt"  type="text" id="t1" name="startDate" datatype="1,is_date,10" group="t1,t2"  value="" />
				<input class="time_ico" type="button" onClick="showcalendar(event, 't1', false);" value="&nbsp;" />&nbsp;至&nbsp;
				<input class="short_txt"  type="text" id="t2" name="endDate" datatype="1,is_date,10" group="t1,t2" value="" />
				<input class="time_ico" type="button" onClick="showcalendar(event, 't2', false);" value="&nbsp;" />
			</td>
			<td align="right">选择物料组：</TD>
			<td>
				<input type="text" class="middle_txt" name="groupCode" size="15" readonly="readonly" value="" />
				<input name="button3" type="button" class="mini_btn" onclick="showMaterialGroup('groupCode','','true','3');" value="..." />
			</td>
		</tr>
		<tr>
			<td></td>
			<td align="right">订单类型：</td>
			<td align="left">
				<label>
					<script type="text/javascript">
						genSelBoxExp("orderType",<%=Constant.ORDER_TYPE%>,"-1",true,"short_sel",'',"false",'');
					</script>
				</label>
			</td>
			<td align="right">运送方式：</TD>
			<td>
				<label>
					<script type="text/javascript">
						genSelBoxExp("transportType",<%=Constant.TRANSPORT_TYPE%>,"-1",true,"short_sel",'',"false",'');
					</script>
				</label>
			</td>
		</tr>
		<tr>
			<td></td>
			<td align="right">销售订单号：</TD>
			<td><input type="text" class="middle_txt" name="orderNo"  value=""/></TD>
			<td align="right">发运状态：</td>
			<td align="left">
				<label>
					<script type="text/javascript">
						genSelBoxExp("deliveryStatus",<%=Constant.DELIVERY_STATUS%>,"-1",true,"short_sel",'',"false",'');
					</script>
				</label>
			</td>
		</tr>
		<tr>
			<td></td>
			<td align="right">业务范围：</td>
			<td>
				<select name="areaId" id="areaId" class="short_sel">
					<option value="">-请选择-</option>
					<c:forEach items="${areaBusList}" var="list">
						<option value="${list.AREA_ID}"><c:out value="${list.AREA_NAME}"/></option>
					</c:forEach>
				</select>
			</td>
			<TD></TD>
			<td>
			<input name="queryBtn" type=button class="cssbutton" onClick="__extQuery__(1);" value="查询">
				<input name="button3" type=button class="cssbutton" onClick="detailExport();" value="下载">
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
	var url = "<%=contextPath%>/sales/ordermanage/delivery/DeliveryQuery/orderDeliveryQuery.json";
	var title = null;
	var columns = [
				{header: "组织代码",dataIndex: 'CODE',align:'center'},
				{header: "组织名称",dataIndex: 'TTNAME',align:'center'},
				{header: "订单类型", dataIndex: 'ORDER_TYPE', align:'center',renderer:getItemValue},
				{header: "销售订单号", dataIndex: 'ORDER_NO', align:'center',renderer:myDetail},
				{header: "订单周度", dataIndex: 'ORDER_WEEK', align:'center',renderer:myText},
				{header: "发运单号", dataIndex: 'DELIVERY_NO', align:'center'},
				{header: "订做车批次号", dataIndex: 'SPECIAL_BATCH_NO', align:'center'},
				{header: "发运数量", dataIndex: 'DELIVERY_AMOUNT', align:'center'},
				{header: "配车数量", dataIndex: 'MATCH_AMOUNT', align:'center'},
				{header: "总价", dataIndex: 'TOTAL_PRICE', align:'center'},
				{header: "运送方式", dataIndex: 'DELIVERY_TYPE', align:'center',renderer:getItemValue},
				{header: "运送地址", dataIndex: 'ADDRESS', align:'center'},
				{header: "状态", dataIndex: 'DELIVERY_STATUS', align:'center',renderer:getItemValue},
				{header: "发运时间", dataIndex: 'DELIVERY_DATE', align:'center'},
				{header: "操作",sortable: false, dataIndex: 'DELIVERY_ID', align:'center',renderer:myMatchDetail}
		      ];
	//设置超链接  begin 
	//超链接设置
	function myText(value,meta,record){
		return String.format(record.data.ORDER_YEAR+"."+value);
	}
	function detailExport(){
		$('fm').action= "<%=contextPath%>/sales/ordermanage/delivery/DeliveryQuery/orderDeliveryLoad.json";
		$('fm').submit();
	}
	
	
	//设置链接
	function myDetail(value,meta,record){
	    return String.format("<a href='#' onclick='orderDetailInfo(\""+ value +"\")'>"+value+"</a>");
	}
	//订单明细链接
	function orderDetailInfo(value){ 
		OpenHtmlWindow('<%=request.getContextPath()%>/sales/ordermanage/orderdetail/OrderDetailInfoQuery/orderDetailInfoQuery.do?&orderNo='+value,700,500);
	}
	//设置链接
	function myMatchDetail(value,meta,record){
	    return String.format("<a href='#' onclick='matchDetailInfo("+value+")'>[车辆明细]</a>");
	}
	//配车明细链接
	function matchDetailInfo(value){ 
		OpenHtmlWindow('<%=request.getContextPath()%>/sales/ordermanage/orderdetail/MatchDetailInfoQuery/matchInfoQuery.do?&deliveryId='+value,700,500);
	}   
	function doInit(){
   		loadcalendar();  //初始化时间控件
   		showDate();
	}

	function showDate(){
		var startYear = document.getElementById("startYear").value;
		var startWeek = document.getElementById("startWeek").value;
		var endYear = document.getElementById("endYear").value;
		var endWeek = document.getElementById("endWeek").value;

		makeNomalFormCall('<%=request.getContextPath()%>/sales/showDate/ShowDateInfo/showDate.json?startYear='+startYear+'&startWeek='+startWeek+'&endYear='+endYear+'&endWeek='+endWeek,showDateInfo,'fm');
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