<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.util.LinkedList"%>
<%@page import="com.infodms.dms.po.TtAsRepairOrderExtPO"%>
<%@page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@page import="com.infodms.dms.common.Utility"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="change" uri="/jstl/change"%>
<style type="text/css">
.tab{margin-top:50px;}

.tab1{width:115px;height:27px;line-height:27px;float:left;text-align:center;cursor:pointer;}
</style>
<%
	String contextPath = request.getContextPath();
%>
<%		TtAsRepairOrderExtPO tawep = (TtAsRepairOrderExtPO) request.getAttribute("roBean");//主信息

		List<Map<String,Object>> itemLs = (List<Map<String,Object>>) request.getAttribute("itemLs");//工时
		List<Map<String,Object>> partLs = (List<Map<String,Object>>) request.getAttribute("partLs");//配件
		List<Map<String,Object>> otherLs = (List<Map<String,Object>>) request.getAttribute("otherLs");//其他
		
		List<FsFileuploadPO> attachLs = (LinkedList<FsFileuploadPO>)request.getAttribute("attachLs");//附件
		List<Map<String,Object>> mainCodeList = (LinkedList<Map<String,Object>>) request.getAttribute("mainCodeList");//主因件
		List<Map<String,Object>> compensationMoneyList = (LinkedList<Map<String,Object>>) request.getAttribute("compensationMoneyList");//补偿
		List<Map<String,Object>> accessoryDtlList = (LinkedList<Map<String,Object>>) request.getAttribute("accessoryDtlList");//辅料
		
		String id = (String) request.getAttribute("ID");
		int size =(Integer) request.getAttribute("size");
		System.out.println(size+"----------------");
		String pids="";
		%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@page import="com.infodms.dms.common.Constant"%>

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
		<TITLE>索赔单审核</TITLE>
		<SCRIPT LANGUAGE="JavaScript">
		var modelId = <%=tawep.getModelId() %>;
	//返回
	function goBack(){
		window.location.href = "<%=request.getContextPath()%>/claim/dealerClaimMng/ClaimBillMaintainMain/claimBillForward.do";
	}
	
	//时间格式化
	Date.prototype.format = function(format) {   
    	var o = {   
			     "M+" : this.getMonth()+1, //month   
			     "d+" : this.getDate(),    //day   
			     "h+" : this.getHours(),   //hour   
			     "m+" : this.getMinutes(), //minute   
			     "s+" : this.getSeconds(), //second   
			     "q+" : Math.floor((this.getMonth()+3)/3), //quarter   
			     "S" : this.getMilliseconds() //millisecond   
   				}   
	   if(/(y+)/.test(format)) format=format.replace(RegExp.$1,   
	     (this.getFullYear()+"").substr(4 - RegExp.$1.length));   
	   for(var k in o)if(new RegExp("("+ k +")").test(format))   
	     format = format.replace(RegExp.$1,   
	       RegExp.$1.length==1 ? o[k] :    
	         ("00"+ o[k]).substr((""+ o[k]).length));   
	   return format;   
	}  
	//格式化时间为YYYY-MM-DD
	function formatDate(value) {
		if (value==""||value==null) {
			return "";
		}else {
			return value.substr(0,10);
		}
	}
function init(){
	var size = <%=size%>;
	for(var i=0;i<size;i++){
			$('outTable'+i).style.display="none";
			$('outTableS'+i).style.display="none";
		}
	}
	}
</SCRIPT>
	</HEAD>
	<BODY onload="init();" onkeydown="keyListnerResp();">
		<div class="navigation">
			<img src="../../../img/nav.gif" />
			&nbsp;当前位置：售后服务管理&gt;索赔申请&gt;索赔申请审核
		</div>

		<form method="post" name="fm" id="fm">
			<input type="hidden" name="roId" id ="roId" value="<%=tawep.getId()%>" />
			<input name="CLAIM_TYPE" id="CLAIM_TYPE" type="hidden"  value="${claimType }"/>
			<input type="hidden" id="claimType" name="claimType" value="${claimType }"/>
			<input type="hidden" name="VIN" id="VIN" value="<%=CommonUtils.checkNull(tawep.getVin())%>" />
			<input type="hidden" name="RO_NO" id="RO_NO" value="<%=CommonUtils.checkNull(tawep.getRoNo())%>" />
			<input type='hidden' name='LINE_NO' id='LINE_NO' value='<%=CommonUtils.checkNull(tawep.getLineNo())%>' />
			<input type="hidden" name="GUARANTEE_DATE" id="GUARANTEE_DATE"  class="short_txt"value='<%=CommonUtils.checkNull(Utility.handleDate(tawep
							.getGuaranteeDate()))%>' />
			<input type='hidden' name='SERVE_ADVISOR' id='SERVE_ADVISOR' value='<%=CommonUtils.checkNull(tawep.getServiceAdvisor())%>' />		
			<input type="hidden" id="CAMPAIGN_FEE" name="CAMPAIGN_FEE" value="<%=tawep.getCampaignFee()%>" />
			<input type="hidden" id="CAMPAIGN_CODE" name="CAMPAIGN_CODE" value="<%=CommonUtils.checkNull(tawep.getCamCode())%>" />
			<input type="hidden" name="FREE_M_AMOUNT" id="FREE_M_AMOUNT"  value="<%=tawep.getFreeTimes()%>" />
			<input type="hidden" name="FREE_M_PRICE" value="<%=tawep.getFree()%>" id="FREE_M_PRICE" />
			
			<input type="hidden" name="attIds" id="attIds" value=""/><!-- 删除附件隐藏 -->
			<input type="hidden" name="BRAND_NAME0" id="BRAND_NAME0" value="<%=CommonUtils.checkNull(tawep.getBrandName())%>"/>
			<input type="hidden" name="SERIES_NAME0" id="SERIES_NAME0" value="<%=CommonUtils.checkNull(tawep.getSeriesName())%>"/>
			<input type="hidden" name="MODEL_NAME0" id="MODEL_NAME0" value="<%=CommonUtils.checkNull(tawep.getModelName())%>"/>
			<input type="hidden" name="ENGINE_NO" id="ENGINE_NO" value="<%=CommonUtils.checkNull(tawep.getEngineNo())%>"/>
			<input type="hidden" name="REARAXLE_NO0" id="REARAXLE_NO0" value="<%=CommonUtils.checkNull(tawep.getGearboxNo())%>"/>
			<input type="hidden" name="GEARBOX_NO0" id="GEARBOX_NO0" value="<%=CommonUtils.checkNull(tawep.getGearboxNo())%>"/>
			<input type="hidden" name="ACTIVITY_AMOUNT_PARTS" id="ACTIVITY_AMOUNT_PARTS" value="${actityAmountPart }" />
			<input type="hidden" name="ACTIVITY_AMOUNT_LABOURS" id="ACTIVITY_AMOUNT_LABOURS" value="${actityAmountLabour }" />
				
			<input type="hidden" name="YIELDLY_TYPE" id="YIELDLY_TYPE" value="<%=CommonUtils.checkNull(tawep.getBalanceYieldly())%>" />
			<input type="hidden" name="YIELDLY" id="YIELDLY" value="<%=CommonUtils.checkNull(tawep.getYieldly())%>" />
			<input type="hidden" value="<%=CommonUtils.checkNull(tawep.getLicenseNo())%>" name="LICENSE_NO" id="LICENSE_NO" />
			
			<input type="hidden" name="TRANSFER_NO0" id="TRANSFER_NO0" value="<%=CommonUtils.checkNull(tawep.getTransferNo())%>"/>
			<input type="hidden" name="BRAND_CODE0" id="BRAND_CODE0" value="<%=tawep.getBrandCode()%>"/>
			<input type="hidden" name="SERIES_CODE0" id="SERIES_CODE0" value="<%=tawep.getSeriesCode()%>"/>
			<input type="hidden" name="MODEL_CODE0" id="MODEL_CODE0" value="<%=tawep.getModelCode()%>"/>
			
			<table border="0" align="center" cellpadding="1" cellspacing="1"
				class="table_edit">
				<th colspan="6">
					<img class="nav" src="../../../img/subNav.gif" />
					基本信息
				</th>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						VIN：
					</td>
					
					<td>
						 <%=CommonUtils.checkNull(tawep.getVin())%> 
					</td>
					
					<td class="table_edit_2Col_label_7Letter">
						发动机号：
					</td>
					
					<td>
						<%=CommonUtils.checkNull(tawep.getEngineNo())%>
					</td>
					
					<td class="table_edit_2Col_label_7Letter">
						索赔类型：
					</td>
					<td>
						<span style="color: red">
							<change:change  type="1066" val="${claimType }"/>
						</span>
					</td>
				</tr>
				
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						品牌：
					</td>
					<td >
						<%=CommonUtils.checkNullEx(tawep.getBrandName())%>
					</td>
					<td class="table_edit_2Col_label_7Letter">
						车系：
					</td>
					<td ><%=CommonUtils.checkNull(tawep.getSeriesName())%>
					</td>
					<td class="table_edit_2Col_label_7Letter">
						车型：
					</td>
					<td ><%=CommonUtils.checkNull(tawep.getModelName())%></td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						工单号：
					</td>
					<td>
						<%=CommonUtils.checkNullEx(tawep.getRoNo())%>
					</td>
					<td class="table_edit_2Col_label_7Letter">
						工单开始时间：
					</td>
					<td nowrap="nowrap">
						 <%=CommonUtils.checkNull(Utility.handleDate1(tawep.getRoCreateDate()))%>
							<input type="hidden" name="RO_STARTDATE" id="RO_STARTDATE" class="short_txt"style="width: 100px"
							value='<%=CommonUtils.checkNull(Utility.handleDate1(tawep
							.getRoCreateDate()))%>' readonly />
					</td>
					<td class="table_edit_2Col_label_7Letter">
						工单结算时间：
					</td>
					<td>
						 <%=CommonUtils.checkNull(Utility.handleDate1(tawep
							.getForBalanceTime()))%> 
							<input type="hidden" name="RO_ENDDATE" id="RO_ENDDATE" class="short_txt" readonly style="width: 100px"
							value='<%=CommonUtils.checkNull(Utility.handleDate1(tawep
							.getForBalanceTime()))%>' />
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						系统里程：
					</td>
					<td>
					<span style="color: red">
						 <fmt:formatNumber value="<%=CommonUtils.checkNull(tawep.getMileage())%>" maxIntegerDigits="20" maxFractionDigits="10"  pattern="0"/>
					</span>
					</td>
					<td class="table_edit_2Col_label_7Letter">
						进厂里程：
					</td>
					<td>
						 <fmt:formatNumber value="<%=CommonUtils.checkNull(tawep.getInMileage())%>" maxIntegerDigits="20" maxFractionDigits="10"  pattern="0"/>
					<input type="hidden" name="IN_MILEAGE" id="IN_MILEAGE" value="<%=CommonUtils.checkNull(tawep.getInMileage())%>" />
					</td>
					<td class="table_edit_2Col_label_7Letter">
						活动代码：
					</td>
					<td><%=CommonUtils.checkNull(tawep.getCampaignName())%></td>
				</tr>
				
				<tr>
					
					<td class="table_edit_2Col_label_7Letter">
						单据保养次数：
					</td>
					<td colspan="4">
						<% if(tawep.getFreeTimes()>0){
						
						out.print(tawep.getFreeTimes());
					} else{
						out.print("");
					}%>
					</td>
				<%-- <td class="table_edit_2Col_label_7Letter">
						系统保养次数：
					</td>
					<td >
					<span style="color: red">
					<%=CommonUtils.checkNull(tawep.getFreeTimes())%>
					</span>
					</td> --%>
					
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						索  赔  员：
					</td>
					<td>
						<input type="hidden" name='SERVE_ADVISOR' value='<%=CommonUtils.checkNull(tawep.getServiceAdvisor())%>'/><%=CommonUtils.checkNull(tawep.getServiceAdvisor())%>
					</td>
					<td class="table_edit_2Col_label_7Letter">
						索赔主管电话：
					</td>
					<td colspan="3" ><%=CommonUtils.checkNull(tawep.getClaimDirectorTelphone())%></td>
				</tr>
				</table>
			<table border="0" align="center" cellpadding="1" cellspacing="1"
				class="table_edit">
				<th colspan="6">
					<img class="nav" src="../../../img/subNav.gif" />
					用户信息
				</th>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						车主姓名：
					</td>
					<td >
						<%=CommonUtils.checkNull(tawep.getCtmName())%>&nbsp;
					</td>
					<td class="table_edit_2Col_label_7Letter">
						车主电话：
					</td>
					<td ><%=CommonUtils.checkNull(tawep.getCtmPhone())%>&nbsp;
					</td>
					<td class="table_edit_2Col_label_7Letter">
						车主地址：
					</td>
					<td ><%=CommonUtils.checkNull(tawep.getCtmAddress())%>&nbsp;</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						送修人姓名：
					</td>
					<td >
						<%=CommonUtils.checkNull(tawep.getDeliverer())%>
					</td>
					<td class="table_edit_2Col_label_7Letter">
						送修人手机：
					</td>
					<td colspan="4" ><%=CommonUtils.checkNull(tawep.getDelivererPhone())%>
					</td>
					
				</tr>
			</table>
	
	
	<!-- 生成主因件按钮tittle -->
	<%
	for(int i=0;i<mainCodeList.size();i++){
		pids = pids+","+mainCodeList.get(i).get("PART_ID").toString();
	%>
	<div id="bg" class="xixi1">
		<input type="button" id="mainBtn<%=i %>"  onclick="setStyles('<%=i %>');"
		<% if(i==0){
			out.println("style=\"background-color: #E0E0E0\"" );
		}else{
			out.println("style=\"background-color: #FFD2D2\"" );
		} %>
		class="tab1"  value="主因件<%=i+1 %>"/>
		</div>
	<%}
	if(!"".equalsIgnoreCase(pids)){
		pids = pids.substring(1,pids.length());
		out.print("<input type='hidden' id='pids' name='pids' value='"+pids+"' > ");
	}
	%>
	<!-- 生成主因件按钮tittle 结束 -->
	
	<!-- 循环主因件,生成对应的form 表单 -->
	<%
	for(int a=0;a<mainCodeList.size();a++){
	if(a==0){%>
		<div  id="div<%=a %>"  style="display: ">
	<%}else{%>
		<div id="div<%=a %>" style="display: none">
	<%}
	%>
	    	
				<table border="0" align="center" cellpadding="1" cellspacing="1"
				class="table_edit">
				<th colspan="6">
					<img class="nav" src="../../../img/subNav.gif" />
				申请内容
				</th>
				<tr>
					<td  class="tbwhite">
						故障描述：
					</td>
					<td  class="tbwhite">
						<textarea name='ADUIT_REMARK<%=mainCodeList.get(a).get("PART_ID").toString() %>' id='AUDIT_REMARK'	datatype="0,is_null,1000" maxlength="1000" rows='3' cols='28'><%=CommonUtils.checkNull(mainCodeList.get(a).get("TROUBLE_DESCRIBE")) %></textarea>
					</td>
					
					<td  class="tbwhite">
						原因分析：
					</td>
					<td  class="tbwhite">
						<textarea name='TROUBLE_REASON<%=mainCodeList.get(a).get("PART_ID").toString() %>'  id='TROUBLE_REASON'	datatype="0,is_null,1000" maxlength="1000" rows='3' cols='28'><%=CommonUtils.checkNull(mainCodeList.get(a).get("TROUBLE_REASON")) %></textarea>
					</td>
					<td  class="tbwhite">
						处理结果：
					</td>
					<td  class="tbwhite">
						<textarea name='DEAL_METHOD<%=mainCodeList.get(a).get("PART_ID").toString() %>' id='DEAL_METHOD' datatype="0,is_null,1000" maxlength="1000" rows='3' cols='28'><%=CommonUtils.checkNull(mainCodeList.get(a).get("DEAL_METHOD")) %></textarea>
					</td>
				</tr>
				
				</table>
				
				<table id="itemTableId" border="0" align="center" cellpadding="0"
				cellspacing="1" class="table_list"
				style="border-bottom: 1px solid #DAE0EE">
				<th colspan="11" align="left">
					<img class="nav" src="../../../img/subNav.gif" />
					索赔维修项目
				</th>
				<tr align="center" class="table_list_row1">
			
					<td>
						作业代码
					</td>
					<td>
						工时名称
					</td>
					<td>
						工时定额
					</td>
					<td>
						工时单价
					</td>
					<td >
						工时金额(元)
					</td >
					<td>
						故障代码/名称
					</td>
					<td>
						是否索赔
					</td>
				</tr>
				<tbody id="itemTable">
					<%
						for (int i = 0; i < itemLs.size(); i++) {
							
							if(itemLs.get(i).get("PART_NO").toString().equalsIgnoreCase(mainCodeList.get(a).get("PART_CODE").toString())|| itemLs.get(i).get("MAIN_PART_CODE").toString().equalsIgnoreCase(mainCodeList.get(a).get("PART_CODE").toString())){
					%>
					<tr class="table_list_row1">
						<td>
						<input type="hidden" name="WR_LABOURCODE<%=mainCodeList.get(a).get("PART_ID").toString() %>" id="WR_LABOURCODE<%=CommonUtils .checkNull(itemLs.get(i).get("ID").toString())%>" value="<%=CommonUtils .checkNull(itemLs.get(i).get("WR_LABOURCODE").toString())%>" />
						<input type="hidden" name="labourId<%=mainCodeList.get(a).get("PART_ID").toString() %>" id="<%=CommonUtils .checkNull(itemLs.get(i).get("ID").toString())%> " value="<%=CommonUtils .checkNull(itemLs.get(i).get("ID").toString())%>" />
						 <%=CommonUtils .checkNull(itemLs.get(i).get("WR_LABOURCODE").toString())%> 
						</td>
						
						<td>
						 <%=CommonUtils .checkNull(itemLs.get(i).get("WR_LABOURNAME").toString())%> 
						 <input name="WR_LABOURNAME<%=mainCodeList.get(a).get("PART_ID").toString() %>" id="WR_LABOURNAME<%=CommonUtils .checkNull(itemLs.get(i).get("ID").toString())%>" value="<%=CommonUtils .checkNull(itemLs.get(i).get("WR_LABOURNAME").toString())%>" type="hidden" />
						</td>
						
						<td>
							 <%=CommonUtils .checkNull(itemLs.get(i).get("LABOUR_HOUR").toString())%> 
						 <input name="LABOUR_HOUR<%=mainCodeList.get(a).get("PART_ID").toString() %>" id="LABOUR_HOUR<%=CommonUtils .checkNull(itemLs.get(i).get("ID").toString())%>" value="<%=CommonUtils .checkNull(itemLs.get(i).get("LABOUR_HOUR").toString())%>" type="hidden" />
						</td>
						
						<td>
							 <%=CommonUtils .checkNull(itemLs.get(i).get("LABOUR_PRICE").toString())%> 
						 	<input name="LABOUR_PRICE<%=mainCodeList.get(a).get("PART_ID").toString() %>" id="LABOUR_PRICE<%=CommonUtils .checkNull(itemLs.get(i).get("ID").toString())%>" value="<%=CommonUtils .checkNull(itemLs.get(i).get("LABOUR_PRICE").toString())%>" type="hidden" />
						</td>
						<td>
							 <%=CommonUtils .checkNull(itemLs.get(i).get("LABOUR_AMOUNT").toString())%> 
							 <input name="LABOUR_AMOUNT<%=mainCodeList.get(a).get("PART_ID").toString() %>" id="LABOUR_AMOUNT<%=CommonUtils .checkNull(itemLs.get(i).get("ID").toString())%>" value="<%=CommonUtils .checkNull(itemLs.get(i).get("LABOUR_AMOUNT").toString())%>" type="hidden" />
						</td>
					 
						<td>
						<input name="malName" class="long_txt" readonly="readonly" id ="malName<%=CommonUtils .checkNull(itemLs.get(i).get("ID").toString())%>" value="<%=CommonUtils.checkNull(itemLs.get(i).get("MAL_CODE").toString())%>" />
						 <input name="MAL_ID<%=mainCodeList.get(a).get("PART_ID").toString() %>" id="MAL_ID<%=CommonUtils .checkNull(itemLs.get(i).get("ID").toString())%>" value="<%=CommonUtils .checkNull(itemLs.get(i).get("MAL_ID").toString())%>" type="hidden" />
						<input name="MALS" id="MALS" value="选择" onclick="selectMalCode(this,'<%=CommonUtils .checkNull(itemLs.get(i).get("ID").toString())%>');" class="normal_btn"  type="button"/>
						
						</td>
						
						<td>
							<script type="text/javascript">
							document.write(getItemValue('<%=CommonUtils .checkNull(itemLs.get(i).get("PAY_TYPE").toString())%>'));
							</script>
							  <input type="hidden" name="PAY_TYPE_ITEM" id="PAY_TYPE_ITEM" value="<%=CommonUtils .checkNull(itemLs.get(i).get("PAY_TYPE").toString())%>" />
						</td>
						</tr>
						<% 
						}}
						%>
						
				</tbody>
			</table>
			<!-- 配件开始 -->
			<table id="partTableId" border="0" align="center" cellpadding="0"
				cellspacing="1" class="table_list">

				<th colspan="16" align="left">
					<img class="nav" src="../../../img/subNav.gif" />
					维修配件
				</th>
				<tr align="center" class="table_list_row1">
					<td>
						旧件代码
					</td>
					<td>
						旧件名称
					</td>
					<td>
						新件代码
					</td>
					<td>
						供应商
					</td>
					<td>
						新件数量
					</td>
					<td>
						单价
					</td>
					<td>
						金额(元)
					</td>
					<td>
					责任性质
					</td>
					<td>
					维修方式
					</td>
					<td>
					自费工单
					</td>
				</tr>
				<tbody id="partTable">
					<%
						for (int i = 0; i < partLs.size(); i++) {
							if(partLs.get(i).get("PART_NO").toString().equalsIgnoreCase(mainCodeList.get(a).get("PART_CODE").toString())||partLs.get(i).get("MAIN_PART_CODE").toString().equalsIgnoreCase(mainCodeList.get(a).get("PART_CODE").toString())){
					%>
					<tr class="table_list_row1">
						<td>
						 <input name="DOWN_PART_CODE<%=mainCodeList.get(a).get("PART_ID").toString() %>" id="DOWN_PART_CODE<%=CommonUtils.checkNull(partLs.get(i).get("ID").toString())%> " value="<%=CommonUtils.checkNull(partLs.get(i).get("PART_NO").toString())%>" type="text" class="middle_txt" readonly="readonly" />
							<%-- <a href="#" onclick="selectPartCode(this,'<%=CommonUtils.checkNull(partLs.get(i).get("ID"))%>')">选择</a> --%> 
						</td>
						<td>
						 <input name="DOWN_PART_NAME<%=mainCodeList.get(a).get("PART_ID").toString() %>" id="DOWN_PART_NAME<%=CommonUtils.checkNull(partLs.get(i).get("ID").toString())%> " value="<%=CommonUtils.checkNull(partLs.get(i).get("PART_NAME").toString())%>" type="text" class="middle_txt" readonly="readonly" />
						</td>
						<td>
						 <%=CommonUtils.checkNull(partLs.get(i).get("PART_NO").toString())%> 
						 <input name="PART_CODE<%=mainCodeList.get(a).get("PART_ID").toString() %>" id="PART_CODE<%=CommonUtils.checkNull(partLs.get(i).get("ID").toString())%>" value="<%=CommonUtils.checkNull(partLs.get(i).get("PART_NO").toString())%>" type="hidden" />
						 <input name="PART_NAME<%=mainCodeList.get(a).get("PART_ID").toString() %>" id="PART_NAME<%=CommonUtils.checkNull(partLs.get(i).get("ID").toString())%>" value="<%=CommonUtils.checkNull(partLs.get(i).get("PART_NAME").toString())%>" type="hidden" />
						</td>
						<td>
							<input name="PRODUCER_CODE<%=mainCodeList.get(a).get("PART_ID").toString() %>" readonly value="" id="PRODUCER_CODE<%=CommonUtils.checkNull(partLs.get(i).get("ID").toString())%>" type="text" datatype="0,is_null,1000" class="short_txt" />
							<a href="#" onclick="selectSupplier(this,'<%=CommonUtils.checkNull(partLs.get(i).get("ID").toString())%>')">选择</a> 
						</td>
						
						<td>
						<input  name="PRODUCER_NAME<%=mainCodeList.get(a).get("PART_ID").toString() %>" id="PRODUCER_NAME<%=CommonUtils.checkNull(partLs.get(i).get("ID").toString())%>" datatype="0,is_null,1000"  type="hidden" readonly/>
						
							 <%=CommonUtils.checkNull(partLs.get(i).get("PART_QUANTITY").toString())%> 
						 	<input name="PART_QUANTITY<%=mainCodeList.get(a).get("PART_ID").toString() %>" id="PART_QUANTITY<%=CommonUtils.checkNull(partLs.get(i).get("ID").toString())%> " value="<%=CommonUtils.checkNull(partLs.get(i).get("PART_QUANTITY").toString())%>" type="hidden" />
					
						</td>
						<td>
							 <%=CommonUtils.checkNull(partLs.get(i).get("PART_COST_PRICE").toString())%> 
						 	<input name="PART_PRICE<%=mainCodeList.get(a).get("PART_ID").toString() %>" id="PART_PRICE<%=CommonUtils.checkNull(partLs.get(i).get("ID").toString())%> " value="<%=CommonUtils.checkNull(partLs.get(i).get("PART_COST_PRICE").toString())%>" type="hidden" />
					
						</td>
						<td>
							 <%=CommonUtils.checkNull(partLs.get(i).get("PART_COST_AMOUNT").toString())%> 
						 	<input name="PART_AMOUNT<%=mainCodeList.get(a).get("PART_ID").toString() %>" id="PART_AMOUNT<%=CommonUtils.checkNull(partLs.get(i).get("ID").toString())%> " value="<%=CommonUtils.checkNull(partLs.get(i).get("PART_COST_AMOUNT").toString())%>" type="hidden" />
						</td>
						<td >
						<script type="text/javascript">
							document.write(getItemValue('<%=partLs.get(i).get("RESPONS_TYPE")%>'));
	       				</script>
						 
						 	<input name="RESPONS_NATURE<%=mainCodeList.get(a).get("PART_ID").toString() %>" id="RESPONS_NATURE<%=CommonUtils.checkNull(partLs.get(i).get("ID").toString())%> " value="<%=partLs.get(i).get("RESPONS_TYPE")%>" type="hidden" />
						</td>
						<td>
						<script type="text/javascript">
							document.write(getItemValue('<%=CommonUtils.checkNull(partLs.get(i).get("PART_USE_TYPE").toString())%>'));
	       				</script>
						
						 	<input name="PART_USE_TYPE<%=mainCodeList.get(a).get("PART_ID").toString() %>" id="PART_USE_TYPE<%=CommonUtils.checkNull(partLs.get(i).get("ID").toString())%> " value="<%=CommonUtils.checkNull(partLs.get(i).get("PART_USE_TYPE").toString())%>" type="hidden" />
							<input name="LABOUR_CODE<%=mainCodeList.get(a).get("PART_ID").toString() %>" id="LABOUR_CODE<%=CommonUtils.checkNull(partLs.get(i).get("ID").toString())%> " value="<%=CommonUtils.checkNull(partLs.get(i).get("LABOUR_CODE").toString())%>" type="hidden" />
						 	<input name="PART_ID<%=mainCodeList.get(a).get("PART_ID").toString() %>" id="<%=CommonUtils.checkNull(partLs.get(i).get("ID").toString())%> " value="<%=CommonUtils.checkNull(partLs.get(i).get("ID").toString())%> " type="hidden" />
							<input name="MAIN_PART_CODE<%=mainCodeList.get(a).get("PART_ID").toString() %>" id="MAIN_PART_CODE<%=CommonUtils.checkNull(partLs.get(i).get("ID").toString())%> " value="<%=CommonUtils.checkNull(partLs.get(i).get("MAIN_PART_CODE").toString())%>" type="hidden" />
						</td>
						
						<td>
							 <%=CommonUtils.checkNull(partLs.get(i).get("ZF_RONO").toString())%> 
						 	<input name="ZF_RONO<%=mainCodeList.get(a).get("PART_ID").toString() %>" id="ZF_RONO<%=CommonUtils.checkNull(partLs.get(i).get("ID").toString())%> " value="<%=CommonUtils.checkNull(partLs.get(i).get("ZF_RONO").toString())%>" type="hidden" />
						</td>
						
					</tr>
					<% 
						}}
					%>
				</tbody>
			</table>
			<!-- 配件结束 -->
			<!-- 补偿费开始 -->
		
			<table class="table_list" id="compensationMoneya" cellpadding="0" cellspacing="1">
              <th colspan="4" align="left" >
              <img class="nav" src="../../../img/subNav.gif" />
              	补偿费
              	</th>
              	<tr align="center" class="table_list_row1">
              		<td>
						申请金额
					</td>
					<td>
						预授权审核金额
					</td>
					
					<td>
						申请备注
					</td>
				</tr>
				<tbody id="compensationMoney">
				<%for(int i=0;i<compensationMoneyList.size();i++){
					if(compensationMoneyList.get(i).get("PART_CODE").toString().trim().equalsIgnoreCase(mainCodeList.get(a).get("PART_CODE").toString())){
					String tempid2 = compensationMoneyList.get(i).get("PKID").toString();
					%>
			
			<tr align="center" class="table_list_row1">
				<td><input name="apply_price<%=mainCodeList.get(a).get("PART_ID") %>" id="apply_price<%=tempid2%>" value="<%=compensationMoneyList.get(i).get("APPLY_PRICE")%>" type="hidden" class="middle_txt" readonly> 
                <%=compensationMoneyList.get(i).get("APPLY_PRICE")%>
                	</td>
                <td><input name="pass_price<%=mainCodeList.get(a).get("PART_ID") %>" id="pass_price<%=tempid2%>" value="<%=compensationMoneyList.get(i).get("PASS_PRICE")%>" type="hidden" class="middle_txt" readonly> 
                <%=compensationMoneyList.get(i).get("PASS_PRICE")%>
                	</td>
				<td>
				<input name="REASON<%=mainCodeList.get(a).get("PART_ID")%>" id="REASON<%=tempid2%>"  value="<%=CommonUtils.checkNull(compensationMoneyList.get(i).get("REASON"))%>" type="text" class="middle_txt">
				 <input name="PKID<%=mainCodeList.get(a).get("PART_ID")%>" id="<%=tempid2%>" value="<%=compensationMoneyList.get(i).get("PKID")%>" type="hidden" class="middle_txt"> 
				<input name="COM_MAIN_CODE<%=mainCodeList.get(a).get("PART_ID")%>" id="COM_MAIN_CODE<%=tempid2%>"  value="<%=CommonUtils.checkNull(compensationMoneyList.get(i).get("PART_CODE"))%>" type="hidden" class="middle_txt">
				<input name="COM_SUPP_CODE<%=mainCodeList.get(a).get("PART_ID")%>" id="COM_SUPP_CODE<%=tempid2%>"  value="<%=CommonUtils.checkNull(compensationMoneyList.get(i).get("SUPPLIER_CODE"))%>" type="hidden" class="middle_txt">
				</td>
            </tr>
			<%} }%>
            </tbody>
          </table>
			<!-- 补偿费结束 -->
			<!-- 辅料开始 -->
			<table class="table_list"id="accessoriess"  cellpadding="0" cellspacing="1">
              <th colspan="4" align="left" >
              <img class="nav" src="../../../img/subNav.gif" />
              	辅料
              	</th>
              	<tr align="center" class="table_list_row1">
					<td>
						辅料代码
					</td>
					<td>
						辅料名称
					</td>
					<td>
						辅料价格
					</td>
				</tr>
				<tbody id="accessories">
				<%for(int i=0;i<accessoryDtlList.size();i++){
					if(accessoryDtlList.get(i).get("MAIN_PART_CODE").toString().trim().equalsIgnoreCase(mainCodeList.get(a).get("PART_CODE").toString())){
						String tempid2 = accessoryDtlList.get(i).get("ID").toString();
					%>
							<tr align="center" class="table_list_row1">
           		<td>
               		<%=accessoryDtlList.get(i).get("WORKHOUR_CODE") %>
               		<input name="WORKHOUR_CODE<%=mainCodeList.get(a).get("PART_ID")%>" id="WORKHOUR_CODE<%=tempid2%>"  value="<%=accessoryDtlList.get(i).get("WORKHOUR_CODE")%>" type="hidden">
				
                </td>
              <td>
             		<%=accessoryDtlList.get(i).get("WORKHOUR_NAME") %>
             		<input name="WORKHOUR_NAME<%=mainCodeList.get(a).get("PART_ID")%>" id="WORKHOUR_NAME<%=tempid2%>"  value="<%=accessoryDtlList.get(i).get("WORKHOUR_NAME")%>" type="hidden">
				
               </td>
               <td>
              	 <%=accessoryDtlList.get(i).get("PRICE") %>
                <input name="WORKHOUR_PRICE<%=mainCodeList.get(a).get("PART_ID")%>" id="WORKHOUR_PRICE<%=tempid2%>"  value="<%=accessoryDtlList.get(i).get("PRICE")%>" type="hidden">
				<input name="FLID<%=mainCodeList.get(a).get("PART_ID")%>" id="<%=accessoryDtlList.get(i).get("ID") %>" value="<%=accessoryDtlList.get(i).get("ID")%>" type="hidden" class="middle_txt"> 
               <input name="WORKHOUR_MAIN_CODE<%=mainCodeList.get(a).get("PART_ID")%>" id="WORKHOUR_MAIN_CODE<%=tempid2%>"  value="<%=accessoryDtlList.get(i).get("MAIN_PART_CODE")%>" type="hidden">
				 </td>
            </tr>
			<%} }%>
            </tbody>
          </table>
			<!-- 辅料结束 -->
			<!-- 其他项目开始 -->
			<table id="otherTableId" align="center" cellpadding="0"
				cellspacing="1" class="table_list">
				<th colspan="8" align="left">
					<img class="nav" src="../../../img/subNav.gif" />
					&nbsp;<a href="#" onclick="hiddenTable('otherTable');" >外出费用</a>
				</th>
				<tr align="center" class="table_list_row1">
					<td>
							项目代码
					</td>
					<td>
							项目名称
					</td>
					<td>
							申请金额(元)
					</td>
					<td>
						备注
					</td>
				</tr>
				<tbody id="otherTable">
					<%
						for (int i = 0; i < otherLs.size(); i++) {
						 if(otherLs.get(i).get("MAIN_PART_CODE").toString().equalsIgnoreCase(mainCodeList.get(a).get("PART_CODE").toString())){
							String tempid2 = otherLs.get(i).get("ID").toString();
						 %> 
					<tr class="table_list_row1" >
						<td>
						 
						   <%=otherLs.get(i).get("ADD_ITEM_CODE").toString()%> 
						   <input type="hidden"  id="ItemCode<%=tempid2%>" name="ItemCode<%=mainCodeList.get(a).get("PART_ID")%>" value="<%=otherLs.get(i).get("ADD_ITEM_CODE").toString()%>" />
						    <input type="hidden"  id="netItem<%=tempid2%>" name="netItem<%=mainCodeList.get(a).get("PART_ID")%>" value="<%=otherLs.get(i).get("ID").toString()%>" />
						 
						</td>
						<td>
						  <%=otherLs.get(i).get("ADD_ITEM_NAME").toString()%> 
						   <input type="hidden"  id="ItemName<%=tempid2%>" name="ItemName<%=mainCodeList.get(a).get("PART_ID")%>" value="<%=otherLs.get(i).get("ADD_ITEM_NAME").toString()%>" />
						 
						</td>
						<td>
						  <%=otherLs.get(i).get("ADD_ITEM_AMOUNT").toString()%> 
						   <input type="hidden"  id="ItemAmount<%=tempid2%>" name="ItemAmount<%=mainCodeList.get(a).get("PART_ID")%>" value="<%=otherLs.get(i).get("ADD_ITEM_AMOUNT").toString()%>"  />
						 
						</td>
						<td >
						<input type="text"  id="ItemRemark<%=tempid2%>" name="ItemRemark<%=mainCodeList.get(a).get("PART_ID")%>" value="<%=CommonUtils.checkNull((String)otherLs.get(i).get("REMARK"))%>" class="middle_txt" />
						<input type="hidden"  id="ItemMainCode<%=tempid2%>" name="ItemMainCode<%=mainCodeList.get(a).get("PART_ID")%>" value="<%=CommonUtils.checkNull((String)otherLs.get(i).get("MAIN_PART_CODE"))%>" class="middle_txt" />
						 </td>
					</tr>
					<%
					} }
					%>
				</tbody>
			</table>
			
			<!-- 其他项目结束 -->
			<!-- 外出信息开始 -->
			<table class="table_edit" id="outTableS<%=a %>">
			<tr>
				<th colspan="6" align="left"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" />
				&nbsp;<a href="#" onclick="hiddenTable('outTable<%=a %>');" >外出维修</a>
				</th>
		 	</tr>
		 </table>
            <table class="table_edit" style="border-bottom: 1px solid #DAE0EE;display: " id="outTable<%=a %>">
            <tr>
					<td class="table_edit_2Col_label_7Letter">
						开始时间：
					</td>
					
					<td>
						 <%=CommonUtils.checkNull(Utility.handleDate(tawep.getStartTime())) %> 
					</td>
					
					<td class="table_edit_2Col_label_7Letter">
						结束时间：
					</td>
					<td> <%=CommonUtils.checkNull(Utility.handleDate(tawep.getEndTime())) %> </td>
					<td class="table_edit_2Col_label_7Letter">
						派车车牌号：
					</td>
					<td>
						 <%=CommonUtils.checkNull(tawep.getOutLicenseno()) %> 
					</td>
           		</tr>
            <tr>
           <tr>
					<td class="table_edit_2Col_label_7Letter">
							外出人：
					</td>
					<td>
						  <%=CommonUtils.checkNull(tawep.getOutPerson())%> 
					</td>
					<td class="table_edit_2Col_label_7Letter">
						出差目的地：
					</td>
					<td>  <%=CommonUtils.checkNull(tawep.getOutSite()) %>  </td>
					<td class="table_edit_2Col_label_7Letter">
							单程里程：
					</td>
					<td>
						 <%=tawep.getOutMileages()%> 
					</td>
           		</tr>
          </table>  
          <!-- 外出信息结束 -->
			</div>
	<%}
	%>
	
	<!-- 样式表格结束 -->
			
		<table border="0" align="center" cellpadding="1" cellspacing="1"
				class="table_edit">
				<th colspan="8">
					<img class="nav" src="../../../img/subNav.gif" />
					申请费用：<span style="color:red" id="total">总金额(含税)：${tatol } 元整</span>
				</th>
			</table>
            <!-- 添加附件 -->
			<table class="table_info" border="0" id="file">
				<input type="hidden" id="fjids" name="fjids"/>
			    <tr colspan="8">
			        <th>
					<img class="nav" src="../../../img/subNav.gif" />
					&nbsp;附件列表
					</th>
					<th><span align="left"><input type="button" disabled="disabled"  class="normal_btn"  onclick="showUpload('<%=contextPath%>')" value ='添加附件'/></span>
					</th>
				</tr>
				<tr>
      				<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv2.jsp" /></td>
    			</tr>
    			<%for(int i=0;i<attachLs.size();i++) { %>
    			<!--  <tr>
    			<td><a  target='_blank' href='<%=CommonUtils.checkNull(attachLs.get(i).getFileurl())%>'/><%=CommonUtils.checkNull(attachLs.get(i).getFilename()) %></a><input type='hidden' value='<%=CommonUtils.checkNull(attachLs.get(i).getFjid()) %>' name='uploadFileId' /> <input type='hidden' value='<%=CommonUtils.checkNull(attachLs.get(i).getFilename()) %>' name='uploadFileName' /></td>
    			<td><input disabled type=button onclick='delUploadFile(this)' class='normal_btn' value='删 除' /></td>
    			</tr>-->
    			<script type="text/javascript">
    			//	addUploadRowByDb('<%=CommonUtils.checkNull(attachLs.get(i).getFilename()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFjid()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFileurl())%>');
    			addUploadRowByDbView('<%=CommonUtils.checkNull(attachLs.get(i).getFilename()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFjid()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFileurl())%>','<%=CommonUtils.checkNull(attachLs.get(i).getFjid()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getPjid()) %>','<%=request.getContextPath()%>/util/FileDownLoad/FileDownLoadQuery.do?fjid=' );
    			</script>
    			<% }%>
 			</table>
 			
			<table border="0" cellspacing="0" cellpadding="0" class="table_edit">
				<tr>
					<td  colspan="5" align="center">
					<input class="normal_btn" type="button" value="维修历史" name="historyBtn"
		 onclick="openWindowDialog(1);"/>
		
		<input class="normal_btn" type="button" value="授权历史" name="historyBtn"
		 onclick="openWindowDialog(2);"/>
		
		<input class="normal_btn" type="button" value="保养历史" name="historyBtn" 
		onclick="openWindowDialog(3);"/>
		 <input class="normal_btn" type="button" id="saveBtn" value="保存" onclick="save();"/>
		
		 <input class="normal_btn" type="button" value="返回" onclick="goback();"/>
			 </td>
		 <td>
		 	<input class="long_btn" type="button" id="three_package_set_btn" value="三包判定" onclick="threePackageSet();"/>
			 </td>
				</tr>
			</table>
		</form>
		<script type="text/javascript">
		var myobj="";
		var tempId="";
		function save(){
			if(submitForm(fm)==false){
				return;
			}
			if(checkData('')){
				return;
			};
			sendAjax('<%=request.getContextPath()%>/claim/dealerClaimMng/ClaimBillMaintainMain/checkStoreBypartCodeAndDealerid.json',backCheck,'fm');
		}
		function backCheck(json){
			if(json.check==""){
				MyConfirm("确认保存?",audit,[]);
			}else{
				MyAlert(json.check);
			}
		}
		function audit(){
		    $('saveBtn').disabled=true;
			var fm = document.getElementById('fm');
			fm.action='<%=request.getContextPath()%>/claim/dealerClaimMng/ClaimBillMaintainMain/applicationInsert.do';
			fm.submit();
		}
		function selectPartCode(obj,id){
			tempId="";
			tempId=id;
			var vin=$('VIN').value;
			var partCode = obj.parentNode.parentNode.childNodes[0].childNodes[0].value;
			parent.OpenHtmlWindow('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/selectDownPartCodeForward.do?GROUP_ID='+tempId+'&partCode='+partCode+'&vin='+vin,800,500);
		}
		function setDownPartCode(partId,partCode,partName,stockPrice,supplierCode,supplierName) {
			//由于为了不修改以前的选择弹出框,所以返回参数较多，但是只使用到了 前3个
			$('DOWN_PART_CODE'+tempId).value=partCode;
			$('DOWN_PART_NAME'+tempId).value=partName;
		}	
		
		function selectSupplier(obj,id){
			myobj = obj;
			var partCode = obj.parentNode.parentNode.childNodes[0].childNodes[0].value;
	 		parent.OpenHtmlWindow('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/selectSupplierCodeForward.do?partcode='+partCode+"&id="+id,800,500);
	 	}
		function setMainSupplierCode(code,name,id){
		//	$('PRODUCER_CODE'+id).value=code;
		//	$('PRODUCER_NAME'+id).value=name;
		myobj.parentNode.parentNode.childNodes[3].childNodes[0].value=code;
		myobj.parentNode.parentNode.childNodes[4].childNodes[0].value=name;
		}
		
 function setStyles ( i ){
		 	for( var j=0; j< <%=mainCodeList.size()%> ; j++ ){
		 		if(j==i){
		 			$('div'+j).style.display='';
		 			$('mainBtn'+j).style.background='#E0E0E0';
		 		}else{
		 			$('div'+j).style.display='none';
		 			$('mainBtn'+j).style.background='#FFD2D2';
		 		}
		 	}
		}
		
		function checkData(part_id){
		 var pid="";
		 var returnBack = false;
		 pid= $('pids').value;//得到主因件的id 
		 
		 if(part_id!=""&&part_id!=null){
		 	 pid=part_id+",";
		}
		 var str = pid.split(",");
		 for(var i=0;i<str.length;i++){
		 if(str[i]!=""){
			 var desc = $('ADUIT_REMARK'+str[i]).value;
			 var reson = $('TROUBLE_REASON'+str[i]).value;
			 var deal = $('DEAL_METHOD'+str[i]).value;
			 if(desc==""||desc==null){
			 	MyAlert('故障描述不能为空!');
			 	returnBack=true;
			 	break;
			 }
			  if(reson==""||reson==null){
			 	MyAlert('原因分析不能为空!');
			 	returnBack=true;
			 	break;
			 }
			  if(deal==""||deal==null){
			 	MyAlert('处理结果不能为空!');
			 	returnBack=true;
			 	break;
			 }
			var codes = document.getElementsByName('PRODUCER_CODE'+str[i]);
			 if(codes.length>0){
			 	for(var k=0;k<codes.length;k++){
			 		if(codes[k].value==""){
			 			MyAlert("请选择供应商!");
			 			returnBack=true;
			 			break;
			 		}
			 	}
			 }
		 }
		 }
		 return returnBack;
		}
		
function selectMalCode(obj,id) {
			tempId="";
			tempId=id;
			OpenHtmlWindow('<%=contextPath%>/claim/application/ClaimManualAuditing/selectMalCode.do',700,600);
		}
		
function setMalInfo(v1,v2,v3){
		$('malName'+tempId).value=v2+"--"+v3;
		$('MAL_ID'+tempId).value=v1;
		}
		
function malAfter(json){
			var str = json.ACTION_RESULT;
		if(str==1){
			MyAlert("修改成功!");
			$('btn1').disabled=false;
			$('btn2').disabled=false;
			$('btn3').disabled=false;
			$('btn4').disabled=false;
		}else{
			MyAlert("审核失败,请联系管理员!");
			$('btn1').disabled=false;
			$('btn2').disabled=false;
			$('btn3').disabled=false;
			$('btn4').disabled=false;
	}
}
		function openWindowDialog(type){
		var targetUrl="";
		var vin = $('VIN').value;
		if(type==1){
		targetUrl = '<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/maintaimHistory.do?VIN='+vin;
		}else if(type==2){
			targetUrl = '<%=contextPath%>/repairOrder/RoMaintainMain/authDetail.do?VIN='+vin;
		}else{
		targetUrl = '<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/freeMaintainHistory.do?VIN='+vin;
		}
		  var height = 500;
		  var width = 800;
		  var iTop = (window.screen.availHeight-30-height)/2;  //获得窗口的垂直位置      
		  var iLeft = (window.screen.availWidth-10-width)/2;   //获得窗口的水平位置
		  var params = "width="+width+",height="+height+",top="+iTop+",left="+iLeft+",scrollbars=yes,resizable=yes,status=no";
		  window.open(targetUrl,null,params);
	  	}
		//配件三包判定按钮方法
		function threePackageSet(){
				var roNo = $('RO_NO').value ;
				var vin = document.getElementById("VIN").value;
				var inMileage = document.getElementById("IN_MILEAGE").value;
				//var arr = document.getElementsByName('PART_CODE');
				var PAY_TYPE_PART = document.getElementsByName('PAY_TYPE_PART');
				var WR_LABOURCODE= document.getElementsByName('WR_LABOURCODE');
				var PAY_TYPE_ITEM= document.getElementsByName('PAY_TYPE_ITEM');
				var str = ''; //配件集合
				 var pid=$('pids').value;//得到主因件的id 
		 		if(pid!=""&&pid!=null){
		 		 var str2 = pid.split(',');
				 for(var a=0;a<str2.length;a++){
		 			if(str2[a]!=""){
		 				var partid = document.getElementsByName('PART_ID'+str2[a]);
		 				
		 				for(var i=0;i<partid.length;i++)
		 				{
		 				var partCode = document.getElementById('DOWN_PART_CODE'+partid[i].value).value;
							str = str+partCode+"," ;
						}
		 			}
		 			}
		 		}
				var codes = str.substr(0,str.length-1);
				str = '';
				for(var i=0;i<PAY_TYPE_PART.length;i++)
					str = str+PAY_TYPE_PART[i].value+"," ;
				var codes_type = str.substr(0,str.length-1);
				
				var strcode = '';
				for(var i=0;i<WR_LABOURCODE.length;i++)
					strcode = strcode+WR_LABOURCODE[i].value+"," ;	
				var labcodes = strcode.substr(0,strcode.length-1);
				strcode = '';
				for(var i=0;i<PAY_TYPE_ITEM.length;i++)
					strcode = strcode+PAY_TYPE_ITEM[i].value+"," ;	
				var labcodes_type = strcode.substr(0,strcode.length-1);
				
				if (vin==null||vin==''||vin=='null') {
					MyAlert("车辆VIN不能为空！");
				}else if (inMileage==null||inMileage==''||inMileage=='null') {
					MyAlert("进厂里程数不能为空！");
				}else{
					window.open('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/threePackageSet.do?VIN='+vin+'&mile='+inMileage+'&codes='+codes+'&codes_type='+codes_type+'&labcodes='+labcodes+'&labcodes_type='+labcodes_type+'&roNo='+roNo);
				}
		}
	function getRowObj(obj)
		{
		   var i = 0;
		   while(obj.tagName.toLowerCase() != "tr"){
		    obj = obj.parentNode;
		    if(obj.tagName.toLowerCase() == "table")
		  return null;
		   }
		   return obj;
		}
		function hiddenTable(str){
	if($(str).style.display=='none'){
		$(str).style.display='';
	}else{
		$(str).style.display='none';
	 }
	}
	function goback(){
		window.location.href = "<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/claimBillForward.do";
	}
</script>
	</BODY>
</html>
