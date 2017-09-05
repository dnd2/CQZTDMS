<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.repairorder.edwclient.RepairItem" %>
<%@ page import="java.util.ArrayList" %>
<%
	String contextPath = request.getContextPath();
	String vin = (String)request.getAttribute("vin");
	String orderNo = (String)request.getAttribute("orderNo");
	String theTime = (String)request.getAttribute("theTime");
	ArrayList repairItemList = (ArrayList)request.getAttribute("repairItemList");
	ArrayList repairMaterialList = (ArrayList)request.getAttribute("repairMaterialList");
	ArrayList sellMaterialList = (ArrayList)request.getAttribute("sellMaterialList");
	ArrayList appendItemList = (ArrayList)request.getAttribute("appendItemList");
	ArrayList assistantMngList = (ArrayList)request.getAttribute("assistantMngList");
	RepairItem valuepo = null;
%>
<head>
	<jsp:include page="${path}/common/jsp_head_new.jsp" />
</head>
<body onunload='javascript:destoryPrototype()' >
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：SGM维修历史 > SGM维修历史明细</div>
<form id="fm" method="post">
<input type="hidden" name="theTime" id="theTime" value="<%=theTime %>"/>
<input type="hidden" name="vin" id="vin" value="<%=vin %>"/>


<div class="wbox" style="border-top:1px solid #DAE0EE;">	
	<table border="0" align="right">
	<tr>
		<td colspan="2" id="showTD">
			
		</td>
	</tr>
	<tr>	
		<td colspan="2" align="right">
			<input class="normal_btn" type="button" value="上一条" onclick="pre();"/>
			<input class="normal_btn" type="button" value="下一条" onclick="next();"/>			
		</td>
	</tr>
	</table>	
		<div style="clear:both"></div>		
		<div style="margin:5px; border-bottom:1px solid #CCC;">	    								
			<span style="float:left">维修项目</span>
			<div style="clear:both"></div>
		</div>		
		<table class="table_list" style="border-bottom:1px solid #DAE0EE">
			<th nowrap="nowrap">序号</th>		
			<th nowrap="nowrap">故障描述</th>		
			<th nowrap="nowrap">故障原因</th>		
			<th nowrap="nowrap">项目代码</th>
			<th nowrap="nowrap">项目名称</th>
			<th nowrap="nowrap">标准工时</th>
			<th nowrap="nowrap" width="60">附加工时</th>
			<th nowrap="nowrap" width="60">收费区分</th>		
		<%if(repairItemList.size()>0){
			for(int i=0;i<repairItemList.size();i++){
				valuepo = (RepairItem)repairItemList.get(i); %>
				<tr class="table_list_row1">
					<td><%=i+1 %></td>
					<td><%=valuepo.getTroubleDesc() %></td>
					<td><%=valuepo.getTroubleCause() %></td>
					<td><%=valuepo.getCode() %></td>
					<td><%=valuepo.getName() %></td>
					<td><%=valuepo.getStdLabourHour() %></td>
					<td><%=valuepo.getAddLabourHour() %></td>
					<td><%=valuepo.getChargeMode() %></td>
				</tr>
		 <%}}%>
		</table>		
		<div style="margin:5px; border-bottom:1px solid #CCC;">	    								
			<span style="float:left">维修材料</span>
			<div style="clear:both"></div>
		</div>
		<table class="table_list" style="border-bottom:1px solid #DAE0EE">
			<th nowrap="nowrap">序号</th>		
			<th nowrap="nowrap">配件代码</th>		
			<th nowrap="nowrap">配件名称</th>		
			<th nowrap="nowrap">数量</th>
			<th nowrap="nowrap">收费区分</th>
		<%if(repairMaterialList.size()>0){
			for(int i=0;i<repairMaterialList.size();i++){
				valuepo = (RepairItem)repairMaterialList.get(i); %>
				<tr class="table_list_row1">
					<td><%=i+1 %></td>
					<td><%=valuepo.getCode() %></td>
					<td><%=valuepo.getName() %></td>
					<td><%=valuepo.getPartQuantity() %></td>
					<td><%=valuepo.getChargeMode() %></td>
				</tr>
		 <%}}%>
		</table>
		<div style="margin:5px; border-bottom:1px solid #CCC;">	    								
			<span style="float:left">销售材料</span>
			<div style="clear:both"></div>
		</div>
		<table class="table_list" style="border-bottom:1px solid #DAE0EE">
		<th nowrap="nowrap">序号</th>		
			<th nowrap="nowrap">序号</th>		
			<th nowrap="nowrap">配件代码</th>		
			<th nowrap="nowrap">配件名称</th>		
			<th nowrap="nowrap">数量</th>
		<%if(sellMaterialList.size()>0){
			for(int i=0;i<sellMaterialList.size();i++){
				valuepo = (RepairItem)sellMaterialList.get(i); %>
				<tr class="table_list_row1">
					<td><%=i+1 %></td>
					<td><%=valuepo.getCode() %></td>
					<td><%=valuepo.getName() %></td>
					<td><%=valuepo.getPartQuantity() %></td>
				</tr>
		 <%}}%>
	</table>
		
		<div style="margin:5px; border-bottom:1px solid #CCC;">	    								
			<span style="float:left">附加项目</span>
			<div style="clear:both"></div>
		</div>
		<table class="table_list" style="border-bottom:1px solid #DAE0EE">
		<th nowrap="nowrap">序号</th>		
			<th nowrap="nowrap">序号</th>		
			<th nowrap="nowrap">代码</th>				
			<th nowrap="nowrap">说明</th>
		<%if(appendItemList.size()>0){
			for(int i=0;i<appendItemList.size();i++){
				valuepo = (RepairItem)appendItemList.get(i); %>
				<tr class="table_list_row1">
					<td><%=i+1 %></td>
					<td><%=valuepo.getCode() %></td>
					<td><%=valuepo.getRemark() %></td>
				</tr>
		 <%}}%>		
		</table>
		<div style="margin:5px; border-bottom:1px solid #CCC;">	    								
			<span style="float:left">辅料管理项目</span>
			<div style="clear:both"></div>
		</div>
		<table class="table_list" style="border-bottom:1px solid #DAE0EE">
		<th nowrap="nowrap">序号</th>		
			<th nowrap="nowrap">序号</th>		
			<th nowrap="nowrap">代码</th>				
			<th nowrap="nowrap">名称</th>
		<%if(assistantMngList.size()>0){
			for(int i=0;i<assistantMngList.size();i++){
				valuepo = (RepairItem)assistantMngList.get(i); %>
				<tr class="table_list_row1">
					<td><%=i+1 %></td>
					<td><%=valuepo.getCode() %></td>
					<td><%=valuepo.getName() %></td>
				</tr>
		 <%}}else{%><tr><td></td></tr><%} %>		
	</table>
</div>
</form>
<script type="text/javascript">
	var bnos = null;
	var bnoslist = null;
	function gonext(theTimes){
		var vin = $("vin").value;
		var blandNo = bnoslist[parseInt(theTimes)];
		var url = "<%=request.getContextPath()%>/common/MaintainHistoryAction/MaintainHistoryDetailSearch.do?theTime="+theTimes+"&vin="+vin+"&orderNo="+blandNo;
		fm.action = url;
		fm.submit();
	} 
	function next(){
		var theTimes = $("theTime").value;
		var nolengt = bnoslist.length -1;
		if(nolengt == theTimes){
			theTimes = 0;
		}	
		gonext(parseInt(theTimes) + 1);
	}
	function getTotalValue(){
		bnos = parentContainer.bNO;
		bnoslist = bnos.split(",");
		$("showTD").innerHTML = "当前第"+'<%=theTime%>'+"条,共:"+(bnoslist.length-1)+"条";
	}
	window.onload = function(){
		getTotalValue();
	}
	function pre(){
		var theTimes = $("theTime").value;
		var nolengt = bnoslist.length -1;	
		if(theTimes == "1"){
			theTimes = nolengt + 1;
		}
		gonext(parseInt(theTimes)-1);
	}
</script>
</body>
</html>
