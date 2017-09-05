<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>月度常规订单审核查询(销售部)</title>
<script type="text/javascript">
function doInit(){
	document.getElementById("startYear").value = ${year};
	document.getElementById("endYear").value = ${year};
	document.getElementById("startMonth").value = ${month};
	document.getElementById("endMonth").value = ${month};
}
function txtClr(valueId) {
	document.getElementById(valueId).value = '' ;
}
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车销售 > 销售订单管理 > 订单审核 > 月度常规订单审核查询(销售部)</div>
<form method="POST" name="fm" id="fm">
  <table class="table_query" align=center width="95%">
	<tr>
      <td align="right" nowrap >订单月度：</td>
      <td width="43%" class="table_query_2Col_input" nowrap>
		    <select name="startYear"  id="startYear" >
	         	<c:forEach items="${yearList}" var="yearList">
					<option value="${yearList}">${yearList}</option>
			  	</c:forEach>
	        </select>年
	       <select name="startMonth" id="startMonth">
	       		<%
	       			for(int i=1;i<=12;i++){
	       		%>
	       		<option value="<%=i %>"><%=i %></option>
	       		<%
	       			}
	       		%>
		   </select>月~
		    <select name="endYear"  id="endYear" >
	         	<c:forEach items="${yearList}" var="yearList">
					<option value="${yearList}">${yearList}</option>
			  	</c:forEach>
	        </select>年
	       <select name="endMonth" id="endMonth">
	       		<%
	       			for(int i=1;i<=12;i++){
	       		%>
	       		<option value="<%=i %>"><%=i %></option>
	       		<%
	       			}
	       		%>
		   </select>
        </td>
     
      <td align="right" nowrap width="15%" >业务范围：</td>
      <td nowrap width="20%">
			<select name="areaId">
				<c:forEach items="${areaList}" var="po">
					<option value="${po.AREA_ID}">${po.AREA_NAME}</option>
				</c:forEach>
            </select>
        </td>
        <td align="right" nowrap ></td>
    </tr>
    <tr>
    	<td align="right">选择物料组：</td>
		<td align="left">
			<input type="text" class="middle_txt" name="groupCode" size="15" id="groupCode" value="" />
			<input name="button3" type="button" class="mini_btn" onclick="toShowMaterial('groupCode','','true','4')" value="..." />
			<input type="button" class="normal_btn" onclick="txtClr('groupCode');" value="清 空" id="clrBtn" />
			
		</td>
		<td align="right"nowrap>选择经销商：</td>
		<td align="left"nowrap>
			<input type="text" class="middle_txt" name="dealerCode" size="15" value="" id="dealerCode"/>
			<input name="button2" type="button" class="mini_btn" onclick="showOrgDealer('dealerCode','','true');" value="..." />
			<input type="button" class="normal_btn" onclick="txtClr('dealerCode');" value="清 空" id="clrBtn" />
			
		</td>
		<td></td>
    </tr>
    <tr>
    	<td align="right">销售订单号：</TD>
		<td align="left"><input type="text" class="middle_txt" name="orderNo"  value="" size="22"/></td>
		<td align="right">订单状态：</TD>
		<td align="left">
			<label>
				<script type="text/javascript">
					genSelBoxExp("orderStatus",<%=Constant.ORDER_STATUS%>,"",true,"short_sel","","false",'<%=Constant.ORDER_STATUS_01%>,<%=Constant.ORDER_STATUS_02%>,<%=Constant.ORDER_STATUS_03%>,<%=Constant.ORDER_STATUS_06%>,<%=Constant.ORDER_STATUS_07%>,');
				</script>
			</label>
		</td>
		<td align="left" ></td>
         <td align="right" nowrap ></td>
    </tr>
    <tr>
    	<td align="right">驻外机构：</td>
			<td align="left">
				<input type="text" id="orgCode" style="width:100px" name="orgCode" value="" size="15" class="middle_txt" readonly="readonly" />
				<input name="obtn" id="obtn"  class="mini_btn" type="button" value="&hellip;" onclick="_showOrgNew_('','orgId','orgCode' ,'true','');"/>
				<input type="hidden" id="orgId" name="orgId" />
				<input class="normal_btn" type="button" value="清空" onclick="txtClr('orgCode');txtClr('orgId');"/>
			</td>
		<td align="right"></TD>
		<td align="left"></td>
		<td align="left" >
      		<input id="queryBtn" name="button2" type=button class="cssbutton" onClick="sumQuery();" value="汇总查询" />
      		<input id="queryBtn" name="button2" type=button class="cssbutton" onClick="detailQuery();" value="明细查询" />
      		<input id="queryBtn" name="button2" type=button class="cssbutton" onClick="sumQueryLoad();" value="汇总下载" />
      		<input id="queryBtn" name="button2" type=button class="cssbutton" onClick="detailQueryLoad();" value="明细下载" />
        </td>
         <td align="right" nowrap ></td>
    </tr>
  </table>
  
	<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    <!--分页 end -->
</form>
<script type="text/javascript">
	var myPage;
	var url;
	var title = null;
	var columns;		
	var calculateConfig;

	function detailQuery(){
		calculateConfig = {};
		url = "<%=request.getContextPath()%>/sales/ordermanage/orderaudit/MonthOrderCheckQuery/monthOrderCheckQuery_SAL.json";
		columns = [
					{header: "采购单位代码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "采购单位名称", dataIndex: 'DEALER_NAME', align:'center'},
				{header: "开票单位代码", dataIndex: 'DEALER_CODE1', align:'center'},
				{header: "开票单位名称", dataIndex: 'DEALER_NAME1', align:'center'},
				{header: "销售订单号", dataIndex: 'ORDER_NO', align:'center'},
				{header: "物料编号", dataIndex: 'MATERIAL_CODE', align:'center'},
				{header: "提报日期", dataIndex: 'RAISE_DATE', align:'center'},
				{header: "订单状态", dataIndex: 'ORDER_STATUS', align:'center',renderer:getItemValue},
				{header: "提报数量", dataIndex: 'ORDER_AMOUNT', align:'center'},
				{header: "审核数量", dataIndex: 'CHECK_AMOUNT', align:'center'},
				{header: "申请数量", dataIndex: 'CALL_AMOUNT', align:'center'}
			      ];		
		__extQuery__(1);

	}
	function sumQueryLoad(){
		$('fm').action= "<%=request.getContextPath()%>/sales/ordermanage/orderaudit/MonthOrderCheckQuery/monthOrderCheckQuery_SAL_Sum_Load.json";
		$('fm').submit();
	}
	function detailQueryLoad(){
		$('fm').action= "<%=request.getContextPath()%>/sales/ordermanage/orderaudit/MonthOrderCheckQuery/monthOrderCheckQuery_SAL_Load.json";
		$('fm').submit();
	}

	function sumQuery(){
		calculateConfig = {bindTableList:"myTable",subTotalColumns:"GROUP_NAME|COLOR_NAME",totalColumns:"COLOR_NAME"};
	    url = "<%=request.getContextPath()%>/sales/ordermanage/orderaudit/MonthOrderCheckQuery/monthOrderCheckQuery_SAL_Sum.json";
		columns = [
					{header: "车系", dataIndex: 'GROUP_NAME', align:'center'},
					{header: "物料编号", dataIndex: 'MATERIAL_CODE', align:'center'},
					{header: "物料名称", dataIndex: 'MATERIAL_NAME', align:'center'},
					{header: "颜色名称", dataIndex: 'COLOR_NAME', align:'center'},
					{header: "提报数量", dataIndex: 'ORDER_AMOUNT', align:'center'},
					{header: "审核数量", dataIndex: 'CHECK_AMOUNT', align:'center'},
					{header: "申请数量", dataIndex: 'CALL_AMOUNT', align:'center'}
			      ];		
		__extQuery__(1);

	}

	
	//修改的超链接
	function myLink(value,meta,record){
		var data = record.data;
		return String.format("<a href='#' onclick='toDetail(\""+ data.CHECK_ID +"\",\""+ data.ORDER_ID +"\")'>[详细]</a>");
	}
	
	function toDetail(check_id,orderId){
		OpenHtmlWindow('<%=request.getContextPath()%>/sales/ordermanage/orderaudit/MonthOrderCheckQuery/monthOrderCheckQuery_SAL_Query_Detail.do?orderId='+orderId+'&check_id='+check_id,700,550);
	}
	function toShowMaterial(){
		var areaId = document.getElementById("areaId").value;
		showMaterialByAreaId_Mini('groupCode','','true',areaId,'');
	}
</script>
</body>
</html>
