<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/funccommon/activityfunc.js"></script>
<title>销售订单查询</title>
<script type="text/javascript">
	function doInit(){
		showDate();
		loadcalendar();
		toChangeDataFlag(2);
		_setDate_("t1", "t2", "1", "0") ;
	}
	
	function limitDate(minusWeek, minusMonth) {
		if(document.getElementById("t1")) {
			var startDate = document.getElementById("t1").value ;
			var endDate = document.getElementById("t2").value ;
			
			if(!startDate) {
				MyAlert("请录入提报开始时间!") ;
				
				return false ;
			}
			
			if(!endDate) {
				MyAlert("请录入提报结束时间!") ;
				
				return false ;
			}
			
			var aStartDate = startDate.split("-") ;
			var aEndDate = endDate.split("-") ;
			
			var startYear = aStartDate[0] ;
			var startMonth = aStartDate[1] ;
			
			var endYear = aEndDate[0] ;
			var endMonth = aEndDate[1] ;
			
			if(parseInt(startYear) == parseInt(endYear)) {
				if(parseInt(endMonth) - parseInt(startMonth) >= minusMonth) {
					MyAlert("提报起止时间跨度不能超过" + minusMonth + "个月!") ;
					
					return false ;
				}
			} else if(parseInt(endYear) - parseInt(startYear) == 1) {
				if(parseInt(endMonth) - parseInt(startMonth) + 12 >= minusMonth) {
					MyAlert("提报起止时间跨度不能超过" + minusMonth + "个月!") ;
					
					return false ;
				}
			} else {
				MyAlert("提报起止时间跨度不能超过" + minusMonth + "个月!") ;
				
				return false ;
			}
		}
		
		return true ;
	}
	
	function setDisFalse() {
		var oBtn = document.getElementById('queryBtn') ;
		if(!oBtn.disabled) {
			var aBtn = arguments ;
			var iLen = aBtn.length ;

			for(var i=0; i<iLen; i++) {
				document.getElementById(arguments[i]).disabled = false ;
			}
		} 
	}

	function setDisTrue() {
		var aBtn = arguments ;
		var iLen = aBtn.length ;

		for(var i=0; i<iLen; i++) {
			document.getElementById(arguments[i]).disabled = true ;
		}
	}
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车销售 > 销售订单管理 > 订单查询 > 销售订单查询</div>
<form method="POST" name="fm" id="fm">
  <table class="table_query" align=center width="95%">
	  <tr>  
	  <td width="19%" align="right" nowrap><input type="radio"  name="dataflag" value="1" onClick="toChangeDataFlag(1);"/></td>
        <td align="right" nowrap="nowrap">订单提报周度：</td>
        <td align="left" nowrap="nowrap" id="weekId">
        	<select  id="startYear" name="orderYear1" onchange="showDate();">
        		<c:forEach items="${years}" var="po">
        			<c:choose>
						<c:when test="${po == curYear}">
							<option value="${po}" selected="selected">${po}</option>
						</c:when>
						<c:otherwise>
							<option value="${po}">${po}</option>
						</c:otherwise>
					</c:choose> 
				</c:forEach>
          	</select>
          	年
		  	<select id="startWeek" name="orderWeek1" onchange="showDate();">
        		<c:forEach items="${weeks}" var="po">
					<c:choose>
						<c:when test="${po == curWeek}">
							<option value="${po}" selected="selected">${po}</option>
						</c:when>
						<c:otherwise>
							<option value="${po}">${po}</option>
						</c:otherwise>
					</c:choose> 
				</c:forEach>
		  	</select>
          	至
		   	<select id="endYear" name="orderYear2" onchange="showDate();">
        		<c:forEach items="${years}" var="po">
					<c:choose>
						<c:when test="${po == curYear}">
							<option value="${po}" selected="selected">${po}</option>
						</c:when>
						<c:otherwise>
							<option value="${po}">${po}</option>
						</c:otherwise>
					</c:choose> 
				</c:forEach>
		  	</select>
          	年
  			<select id="endWeek" name="orderWeek2" onchange="showDate();">
        		<c:forEach items="${weeks}" var="po">
					<c:choose>
						<c:when test="${po == curWeek}">
							<option value="${po}" selected="selected">${po}</option>
						</c:when>
						<c:otherwise>
							<option value="${po}">${po}</option>
						</c:otherwise>
					</c:choose> 
				</c:forEach>
			</select>
			<span id="dateInfo" class="innerHTMLStrong"></span>
		</td>
        <td width="19%" align=left nowrap>&nbsp;</td>
    </tr>
     <tr>
        <td align="right" nowrap="nowrap"><input type="radio"  name="dataflag" value="2" checked="checked" onClick="toChangeDataFlag(2);"/></td>
	    <td align="right" class="table_list_th">提报起止时间：</td>
	    <td class="table_list_th" id="timeId">
	    	<div align="left">
       		<input name="beginTime" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);">
       		&nbsp;至&nbsp;
       		<input name="endTime" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);">
   		</div>
        </td>
	    <td>&nbsp;</td>
    </tr>
	  <tr>
        <td align="right" nowrap="nowrap">&nbsp;</td>
	    <td align="right" class="table_list_th">选择业务范围：</td>
	    <td class="table_list_th">
	    	<select class="short_sel" name="areaId">
				<option value="">-请选择-</option>
				<c:forEach items="${areaList}" var="po">
					<option value="${po.AREA_ID}">${po.AREA_NAME}</option>
				</c:forEach>
	        </select>
        </td>
	    <td>&nbsp;</td>
    </tr>
	<tr>
      <td align="right" nowrap><input type="radio"  name="flag" value="1" checked="checked" onClick="toChangeDis1();"/></td>
      <td align="right" nowrap >选择物料组：</td>
      <td align="left" nowrap >
      	<input type="text" class="middle_txt" name="groupCode" size="15"  value="" id="groupCode" />
      	<input type="hidden" name="groupName" size="20" id="groupName" value="" />
		<input name="button1" type="button" class="mini_btn" onclick="showMaterialGroup('groupCode','groupName','true','4');" value="..." />
		<input class="cssbutton" type="button" value="清空" onclick="clrTxt('groupCode');"/>
      </td>
      <td>&nbsp;	   </td> 
    </tr> 
	<tr>
      <td align="right" nowrap><input type="radio"  name="flag" value="2" onClick="toChangeDis2();"/></td>
      <td align="right" nowrap >选择物料：</td>
      <td align="left" nowrap >
      	<input type="text" class="middle_txt" name="materialCode" size="15"  value="" id="materialCode" disabled="disabled"/>
		<input name="button2" type="button" class="mini_btn" onclick="showMaterial('materialCode','','true');" value="..." disabled="disabled"/>
		<input class="cssbutton" type="button" value="清空" onclick="clrTxt('materialCode');"/>
      </td>
      <td>&nbsp;	   </td> 
    </tr> 
	<tr>
	  <TD align=right nowrap>&nbsp;</TD>
        <TD align="right" nowrap>订单类型：</TD>
		<TD align="left" nowrap>
			<script type="text/javascript">
	      		genSelBoxExp("orderType",<%=Constant.ORDER_TYPE%>,"",true,"short_sel","","false",'');
	      	</script>
        </TD>
		<td align=left nowrap>&nbsp;</td>
	</tr>
	<tr>
      <td align="right" nowrap="nowrap">&nbsp;</td>
	  <td align="right" nowrap="nowrap">订单状态：</td>
	  <td align="left" nowrap="nowrap">
			<script type="text/javascript">
	      		genSelBoxExp("orderStatus",<%=Constant.ORDER_STATUS%>,"",true,"short_sel","","false",'<%=Constant.ORDER_STATUS_01%>,<%=Constant.ORDER_STATUS_02%>,<%=Constant.ORDER_STATUS_07%>,<%=Constant.ORDER_STATUS_10%>');
	      	</script>
	  </td>
	  <td align="left" nowrap="nowrap">&nbsp;</td>
    </tr>
	<tr>
      <td align="right" nowrap="nowrap">&nbsp;</td>
	  <td align="right" nowrap="nowrap">销售订单号：</td>
	  <td align="left" nowrap="nowrap"><label>
        <input type="text" class="middle_txt" name="orderNo" />
      </label></td>
	  <td align="left" nowrap="nowrap">&nbsp;</td>
    </tr>
	   <tr>
	    <td align="center" nowrap>&nbsp;</td>
        <td>&nbsp;</td>
	    <td><input name="queryBtn" type=button class="cssbutton" onpropertychange="setDisFalse('queryBtn1','queryBtn2','queryBtn3')" onClick="totalQuery();" value="汇总查询">
          <input name="queryBtn1" type=button class="cssbutton" onClick="detailQuery();" value="明细查询">
          <input name="queryBtn2" type=button class="cssbutton" onClick="totalExport();"value="汇总下载">
          <input name="queryBtn3" type=button class="cssbutton" onClick="detailExport();"value="明细下载"></td>
	    <td align="center" nowrap>&nbsp;</td>
    </TR>
  </table>
  
	<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    <!--分页 end -->
</form>
<script type="text/javascript"><!--
	var myPage;
	//查询路径
	var url;
				
	var title = null;

	var columns;		
	         
	var calculateConfig;
	
	function totalQuery(){
		calculateConfig = {bindTableList:"myTable",subTotalColumns:"ORDER_TYPE|ORDER_TYPE",totalColumns:"ORDER_TYPE"};
		url = "<%=request.getContextPath()%>/sales/ordermanage/orderquery/DealerSalesOrderQuery/dealerSalesOrderTotalQuery.json";
		columns = [
				{header: "物料编号", dataIndex: 'MATERIAL_CODE', align:'center'},
				{header: "物料名称", dataIndex: 'MATERIAL_NAME', align:'center'},
				{header: "订单类型", dataIndex: 'ORDER_TYPE', align:'center' ,renderer:getItemValue},
				{header: "提报数量", dataIndex: 'ORDER_AMOUNT', align:'center'},
				{header: "审核数量", dataIndex: 'CHECK_AMOUNT', align:'center'},
				{header: "开票数量", dataIndex: 'BILLING_AMOUNT', align:'center'},
				{header: "发运数量", dataIndex: 'DELIVERY_AMOUNT', align:'center'}
		      ];
		
		if(!limitDate(0, 3)) {
			return false ;
		}
		__extQuery__(1);
		
		setDisTrue('queryBtn1','queryBtn2','queryBtn3') ;
	}
	
	function detailQuery(){
		calculateConfig = {};
		url = "<%=request.getContextPath()%>/sales/ordermanage/orderquery/DealerSalesOrderQuery/dealerSalesOrderDetailQuery.json";
		columns = [
				{header: "订货方编码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "订货方", dataIndex: 'ORDER_ORG_NAME', align:'center'},
				{header: "开票方", dataIndex: 'BILLING_ORG_NAME', align:'center'},
				{header: "销售订单号", dataIndex: 'ORDER_NO', align:'center' ,renderer:myDetail},
				// {header: "发运单号", dataIndex: 'DELIVERY_NO', align:'center'},
				{header: "提报日期", dataIndex: 'RAISE_DATE', align:'center'},
				{header: "提报类型", dataIndex: 'ORDER_TYPE', align:'center' ,renderer:getItemValue},
				{header: "提报状态", dataIndex: 'ORDER_STATUS', align:'center' ,renderer:getItemValue},
				{header: "提报数量", dataIndex: 'ORDER_AMOUNT', align:'center'},
				{header: "审核数量", dataIndex: 'CHECK_AMOUNT', align:'center'},
				{header: "开票数量", dataIndex: 'BILLING_AMOUNT', align:'center'},
				{header: "发运数量", dataIndex: 'DELIVERY_AMOUNT', align:'center' },//,renderer:myMatchDetail
				{header: "发运方式", dataIndex: 'DETYPE', align:'center',renderer:getItemValue},
				{header: "运送地址", dataIndex: 'DEADDR', align:'center'}
		      ];
		
		if(!limitDate(0, 3)) {
			return false ;
		}
		
		__extQuery__(1);
		
		setDisTrue('queryBtn1','queryBtn2','queryBtn3') ;
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
	    return String.format("<a href='#' onclick='matchDetailInfo(\""+ record.data.ORDER_NO +"\")'>"+value+"</a>");
	}
	//配车明细链接
	function matchDetailInfo(value){ 
		OpenHtmlWindow('<%=request.getContextPath()%>/sales/ordermanage/orderdetail/MatchDetailInfoQuery/matchDetailInfoQuery.do?&orderNo='+value,700,500);
	}
	
	function myLink2(value,meta,record){
		return String.format("<a href='#'>"+value+"</a>");
	}
	
	function totalExport(){
		if(!limitDate(0, 3)) {
			return false ;
		}
		
		$('fm').action= "<%=request.getContextPath()%>/sales/ordermanage/orderquery/DealerSalesOrderQuery/dealerSalesOrderTotalExport.do";
		$('fm').submit();
	}
	
	function detailExport(){
		if(!limitDate(0, 3)) {
			return false ;
		}
		
		$('fm').action= "<%=request.getContextPath()%>/sales/ordermanage/orderquery/DealerSalesOrderQuery/dealerSalesOrderDetailExport.do";
		$('fm').submit();
	}
	
	function clrTxt(txtId){
    	document.getElementById(txtId).value = "";
    }
    
    function toChangeDis1(){
		document.getElementById("materialCode").disabled = "disabled";
		document.getElementById("button2").disabled = "disabled";
		document.getElementById("groupCode").disabled = "";
		document.getElementById("button1").disabled = "";
		document.getElementById("groupCode").value = "";
		document.getElementById("materialCode").value = "";
	}
	
	function toChangeDis2(){
		document.getElementById("materialCode").disabled = "";
		document.getElementById("button2").disabled = "";
		document.getElementById("groupCode").disabled = "disabled";
		document.getElementById("button1").disabled = "disabled";
		document.getElementById("groupCode").value = "";
		document.getElementById("materialCode").value = "";
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
	function toChangeDataFlag(value){
		var weekId = document.getElementById("weekId");
		var timeId = document.getElementById("timeId");
		var timeId_t1 = document.getElementById("t1");
		var timeId_t2 = document.getElementById("t2");
		
		if(value+"" == "1"){
			weekId.disabled = false;
			timeId.disabled = true;
			//timeId_t1.value = "";
			//timeId_t2.value = "";
			timeId_t1.disabled = true;
			timeId_t2.disabled = true;
		}else{
			weekId.disabled = true;
			timeId.disabled = false;
			timeId_t1.disabled = false;
			timeId_t2.disabled = false;
		}
	}
</script>
</body>
</html>
