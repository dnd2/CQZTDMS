<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/change" prefix="change" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>索赔紧急调件查询</title>
<% String contextPath = request.getContextPath(); 
%>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>
</head>
<BODY onload="__extQuery__(1);">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理 &gt;索赔紧急调件库存查询</div>
  <form id="fm" name="fm">
    <TABLE class="table_query">
     <tr>
  		
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">零部件代码：</td>
		<td width="10%"><input name="partCode" class="middle_txt" id="partCode"> </td>
		<td width="10%"  class="table_query_2Col_label_8Letter" nowrap="true">零部件名称：</td>
		<td width="10%"><input name="partName" class="middle_txt" id="partName"> </td>
		<td width="10%"  class="table_query_2Col_label_8Letter" nowrap="true">经销商代码：</td>
		<td width="10%"><input name="dealerCode" class="middle_txt" id="dealerCode"> </td>
		<td width="20%"></td>
		<td width="20%"></td>
	</tr>
	 <tr>
	 
		<td width="10%"  class="table_query_2Col_label_5Letter" >经销商名称：</td>
		<td width="10%"><input name="dealerName" class="middle_txt" id="dealerName"> </td>
		<td width="10%"  class="table_query_2Col_label_8Letter"  >供应商代码：</td>
		<td width="10%"><input name="supplyCode" class="middle_txt" id="supplyCode"> </td>
		<td width="10%"  class="table_query_2Col_label_8Letter" >供应商名称：</td>
		<td width="10%"><input name="supplyName" class="middle_txt" id="supplyName"> </td>
		<td width="20%"></td>
		<td width="20%"></td>
	</tr>
	 <tr>
	 
		<td width="10%"  class="table_query_2Col_label_5Letter" >索赔单号：</td>
		<td width="10%"><input name="claimNo" class="middle_txt" id="claimNo"> </td>
		<td width="10%"  class="table_query_2Col_label_8Letter"  >VIN：</td>
		<td width="10%"><input name="VIN" class="middle_txt" id="VIN"> </td>
		 <td align="right" nowrap="nowrap" >借出时间： </td>
         <td align="left" nowrap="true">
			<input name="start_date" type="text" class="short_time_txt" id="start_date" onclick="calendar();" readonly="readonly"/> 
             &nbsp;至&nbsp; <input name="end_date" type="text" class="short_time_txt" id="end_date" onclick="calendar();" readonly="readonly"/> 
		</td>	
		<td width="20%"></td>
	</tr>
       <tr>
         <td align="center" nowrap="nowrap" colspan="4">
           <input class="normal_btn" type="button" id="qryButton" name="qryButton" value="查询"  onClick="__extQuery__(1);">
           &nbsp;&nbsp;
           <input class="normal_btn" type="button" id="qryButton" name="qryButton" value="新增"  onClick="call_add();">
           &nbsp;&nbsp;
         </td>
       </tr>
  </table>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end -->
  
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
<br>
<script type="text/javascript">
   
   var myPage;
   //查询路径
   var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartStorageManager/queryListBorrowCallOutData.json";
				
   var title = null;
   
   var columns = [
  				{header: "配件名称", dataIndex: 'PART_NAME', align:'center'},
  				{header: "配件代码", dataIndex: 'PART_CODE', align:'center'},
  				{header: "索赔单号", dataIndex: 'CLAIM_NO', align:'center',renderer:mylink},
  				{header: "经销商代码", dataIndex: 'DEALER_CODE', align:'center'},
  				{header: "经销商名称", dataIndex: 'DEALER_NAME', align:'center'},
  				{header: "供应商代码", dataIndex: 'SUPPLY_CODE', align:'center'},
  				{header: "供应商名称", dataIndex: 'SUPPLY_NAME', align:'center'},
  				{header: "VIN", dataIndex: 'VIN', align:'center'},
  				{header: "车型", dataIndex: 'MODEL_NAME', align:'center'},
  				{header: "库存", dataIndex: 'ALL_AMOUNT', align:'center'},
  				{header: "索赔类型", dataIndex: 'CLAIM_TYPE', align:'center',renderer:getItemValue},
  				{header: "借出人", dataIndex: 'BORROW_MAN', align:'center'},
				{header: "借出时间", dataIndex: 'CREATE_DATE', align:'center'}
  		      ];
   	function mylink(value,meta,record){
   		var width=900;
		var height=500;
		var screenW = window.screen.width-30;	
		var screenH = document.viewport.getHeight();
		if(screenW!=null && screenW!='undefined')
			width = screenW;
		if(screenH!=null && screenH!='undefined')
			height = screenH;
		var roNo = record.data.RO_NO;
		var ID = record.data.ID;
		var claimNo = record.data.CLAIM_NO;
		return String.format("<a href='#' onclick='OpenHtmlWindow(\"<%=contextPath%>/claim/application/ClaimBillStatusTrack/claimBillDetailForward.do?roNo="+roNo+"&ID="
				+ ID + "\","+width+","+height+")' >"+claimNo+"</a>");
   	}
  	function call_add(){
  		fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartStorageManager/borrowCaladd.do";
        fm.submit();
  	}
</script>
</BODY>
</html>