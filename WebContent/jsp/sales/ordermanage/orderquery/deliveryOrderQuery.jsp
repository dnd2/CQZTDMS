<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/funccommon/activityfunc.js"></script>
<head>
<% String contextPath = request.getContextPath(); %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>销售订单查询</title>
<script type="text/javascript">
	function doInit()
	{
   		loadcalendar();  //初始化时间控件
   		//_setDate_("t1", "t2", "1", "0") ;
   		_setDate_("tw3","tw2","1","0");
   		$("ts2").click();
	}
	function txtClr(valueId) {
		document.getElementById(valueId).value = '' ;
	}
	function reportSelect(a){
		if(a.checked){
			$("tw3").value="";
			$("tw2").value="";
			$("tw3").disabled=true;
			$("tw2").disabled=true;
			$("t1").disabled=false;
			$("t2").disabled=false;
		}
		
	}
	function auditSelect(b){
		if(b.checked){
			$("t1").value="";
			$("t2").value="";
			$("t1").disabled=true;
			$("t2").disabled=true;
			$("tw3").disabled=false;
			$("tw2").disabled=false;
			
		}
	}
</script>
</head>

<body onload="genLocSel('txt1','','','','','');">
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;发运订单查询</div>
<form method="post" name="fm" id="fm">
<table border="0" align="center" class="table_query">
  <tr>
    <td align="right" nowrap="nowrap"><input type="radio" name="timeSelect" id="ts1" onclick="reportSelect(this);"/>发运申请提报时间：</td>
    <td align="left" nowrap="nowrap" class="table_add_2Col_input">
    	<div align="left">
       		<input name="beginTime" id="t1" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't1', false);">
       		&nbsp;至&nbsp;
       		<input name="endTime" id="t2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 't2', false);">
   		</div>
    </td>
    <td align="right" nowrap="nowrap"> 选择业务范围：</td>
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
    <td align="right" nowrap="nowrap"><input type="radio" name="timeSelect" id="ts2" onclick="auditSelect(this);"/>资源最终审核时间：</td>
    <td align="left" nowrap="nowrap" class="table_add_2Col_input">
    	<div align="left">
       		<input name="startDate" id="tw3" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 'tw3', false);">
       		&nbsp;至&nbsp;
       		<input name="endDate" id="tw2" value="" type="text" class="short_txt" datatype="1,is_date,10" group="t1,t2" hasbtn="true" callFunction="showcalendar(event, 'tw2', false);">
   		</div>
    </td>
    <td align="right" nowrap="nowrap"> </td>
    <td align="left" nowrap="nowrap" class="table_edit_2Col_input">
	</td>
    </tr>
   <tr>
    <td align="right">运送方式：</td>
    <td class="table_edit_2Col_input">
    	<script type="text/javascript">
 			genSelBoxExp("transType",<%=Constant.TRANSPORT_TYPE%>,"",true,"short_sel","","false",'');
		</script>
    </td>
    <td height="25" align="right">状态：</td>
    <td class="table_edit_2Col_input">
    	<script type="text/javascript">
 			genSelBoxExp("reqStatus",<%=Constant.ORDER_REQ_STATUS%>,"<%=Constant.ORDER_REQ_STATUS_FINAL%>",true,"short_sel","","false",'');
		</script>    	
    </td>
    </tr>
  <tr>
    <td align="right">发运单号：</td>
    <td class="table_edit_2Col_input">
    	<input name="orderNo" type="text" class="middle_txt"  value=""/>
    </td>
  	<td align="right">是否代交车：</td>
	<td>
		<label>
			<script type="text/javascript">
					genSelBoxExp("isDaijiao",<%=Constant.IF_TYPE%>,"",true,"short_sel",'',"false",'');
			</script>
		</label>
	</td>
  </tr>
   <%
  	String dutyType = String.valueOf(request.getAttribute("dutyType"));
    if(dutyType.equals(String.valueOf(Constant.DUTY_TYPE_DEALER))){	
    }
    else{
   %>
<!--   <tr>-->
<!--     <td align="right">下级经销商销售订单号:</td>-->
<!--	 <td class="table_edit_2Col_input">-->
<!--	 	<input name="oldOrderNo" type="text" class="middle_txt"  value=""/>-->
<!--	 </td>-->
<!--    <td align="right" nowrap="nowrap">开票方：</td>-->
<!--    <td class="table_edit_2Col_input">-->
<!--    	<input name="orderNo" type="text" class="middle_txt"  value=""/>-->
<!--    </td>-->
<!--   </tr>-->
   <%
    }
   %>  
<!--   <c:if test="${dutyType != nowDuty }">-->
<!--   <tr>-->
<!--			<td  align="right">事业部：</td>-->
<!--			<td align="left">-->
<!--				<select id="orgId" name="orgId" class="short_sel">-->
<!--				<option value="">--请选择--</option>-->
<!--					<c:forEach items="${orgList}" var="orgList">-->
<!--						<option value="${orgList.ORG_ID }">${orgList.ORG_NAME }</option>-->
<!--					</c:forEach>-->
<!--				</select>-->
<!--			</td>-->
<!--			<td align="right">省份：</td>-->
<!--			<td align="left">-->
<!--					<select class="short_sel" id="txt1" name="downtown"></select>-->
<!--			</td>-->
<!--		</tr>-->
<!--		</c:if>-->
		 <tr>
    <td align="right">订单类型：</td>
    <td class="table_edit_2Col_input">
    	<script type="text/javascript">
 			genSelBoxExp("myOrderType",<%=Constant.ORDER_TYPE%>,"",true,"short_sel","","false",'');
		</script>
    </td>
    <td height="25" align="right">销售订单号：</td>
    <td class="table_edit_2Col_input">
    	<input name="myOrderNo" type="text" class="middle_txt"  value=""/>
    </td>
    </tr>
    <tr>
    	<td align="right">选择经销商：</td>
			<td colspan="1">
				<input type="text" class="middle_txt" style="width:100px" name="dealerCode" size="15" value="" id="dealerCodes"/>
				<!-- <input name="button2" type="button" class="mini_btn" onclick="showOrgDealer('dealerCode','','true');" value="..." /> -->
				<c:if test="${dutyType==10431004}">
            	 	<input name="button2" id="dbtn" class="normal_btn" type="button" value="&hellip;" onclick="showOrgDealer('dealerCodes','','true', '${orgId}');" />
           		 </c:if>
            	<c:if test="${dutyType!=10431004}">
            		<input name="button2" id="dbtn" class="normal_btn" type="button" value="&hellip;" onclick="showOrgDealer3('dealerCodes','','true', '${orgId}','true');" />
        		</c:if>
				<input class="normal_btn" type="button" value="清空" onclick="clrTxt('dealerCodes');"/>
			</td>
		<td></td>
		<td></td>
    </tr>
    <tr>

		<td align="right" colspan="5">
			<input name="queryBtn" type="button"  class="normal_btn"	onclick="__extQuery__(1);" value="查询" />
			<input name="button" type="button"  class="normal_btn"	onclick="doExport();" value="下载" />
            页面大小：<input name="pageSize" id="pageSize" type="text" class="mini_txt" value="10" datatype="0,isDigit,3"/>
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
	var url = "<%=contextPath%>/sales/ordermanage/orderquery/OemSalesOrderQuery/deliveryOrderQueryByJson.json";
				
	var title = null;

	var columns = [
				{header: "采购单位", dataIndex: 'DEALER_NAME',align:'center'},
				{header: "开票单位代码", dataIndex: 'DEALER_CODE',align:'center'},
				{header: "开票单位", dataIndex: 'DEALER_NAME1',align:'center'},
				{header: "订单类型", dataIndex: 'ORDER_TYPE', align:'center',renderer:getItemValue},
				{header: "提报时间", dataIndex: 'REQ_DATE', align:'center'},
				{header: "资源最终审核日期", dataIndex: 'UPDATE_DATE', align:'center'},
				{header: "发运单号", dataIndex: 'DLVRY_REQ_NO', align:'center',renderer:myLink},
				{header: "销售订单号", dataIndex: 'ORDER_NO', align:'center'},
				<%
				if(!dutyType.equals(String.valueOf(Constant.DUTY_TYPE_DEALER))){	
				%>
				//{header: "下级经销商订单号", dataIndex: 'OLD_ORDER_NO', align:'center'},
				<%
    			}
				%>
				{header: "申请数量", dataIndex: 'REQ_TOTAL_AMOUNT', align:'center'},
				{header: "保留资源数量", dataIndex: 'RESERVE_AMOUNT', align:'center'},
				{header: "状态", dataIndex: 'REQ_STATUS', align:'center',renderer:getItemValue},
				{header: "运送方式", dataIndex: 'DELIVERY_TYPE', align:'center',renderer:getItemValue},
				{header: "收货方", dataIndex: 'DEALER_NAME2',align:'center'}
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
		$('fm').action= "<%=request.getContextPath()%>/sales/ordermanage/orderquery/OemSalesOrderQuery/deliveryOrderExportByJson.json";
		$('fm').submit();
	}
	function clrTxt(curId){
		document.getElementById(curId).value="";
	}
//设置超链接 end
	
</script>
<!--页面列表 end -->


</body>
</html>
