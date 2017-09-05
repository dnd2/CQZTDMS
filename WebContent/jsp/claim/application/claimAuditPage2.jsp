<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.util.LinkedList"%>
<%@page import="com.infodms.dms.po.TtAsWrApplicationExtPO"%>
<%@page import="com.infodms.dms.po.TtAsWrNetitemExtPO"%>
<%@page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@page import="com.infodms.dms.bean.ClaimListBean"%>
<%@page import="com.infodms.dms.bean.TtAsWrLabouritemBean"%>
<%@page import="com.infodms.dms.bean.TtAsWrPartsitemBean"%>
<%@page import="com.infodms.dms.common.Utility"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<style type="text/css">
.tab{margin-top:50px;}

.tab1{width:115px;height:27px;line-height:27px;float:left;text-align:center;cursor:pointer;}
</style>
<%
	String contextPath = request.getContextPath();
%>
<%
			TtAsWrApplicationExtPO tawep = (TtAsWrApplicationExtPO) request.getAttribute("application");
			List<ClaimListBean> itemLs = (LinkedList<ClaimListBean>) request.getAttribute("itemLs");
			List<ClaimListBean> partLs = (LinkedList<ClaimListBean>) request.getAttribute("partLs");
			List<TtAsWrNetitemExtPO> otherLs = (LinkedList<TtAsWrNetitemExtPO>) request.getAttribute("otherLs");
			List<FsFileuploadPO> attachLs = (LinkedList<FsFileuploadPO>)request.getAttribute("attachLs");
			List<Map<String,Object>> mainCodeList = (LinkedList<Map<String,Object>>) request.getAttribute("mainCodeList");
			List<Map<String,Object>> compensationMoneyList = (LinkedList<Map<String,Object>>) request.getAttribute("compensationMoneyList");
			List<Map<String,Object>> accessoryDtlList = (LinkedList<Map<String,Object>>) request.getAttribute("accessoryDtlList");
			List<FsFileuploadPO> fileList = (LinkedList<FsFileuploadPO>)request.getAttribute("attachLs");
			request.setAttribute("fileList",fileList);
			String id = (String) request.getAttribute("ID");
			int size =(Integer) request.getAttribute("size");
			int type =Integer.parseInt(request.getAttribute("type")==null?"0":request.getAttribute("type").toString()) ;
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
	if($('claimType').value==<%=Constant.CLA_TYPE_09 %>){
		$('outTable'+i).style.display="";
		$('outTableS'+i).style.display="";
	}else{
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
			<input name="claimId" id ="claimId" type="hidden" value="<%=tawep.getId() %>"/>
			<input name="type" id="type" type="hidden"  value="<%=CommonUtils.checkNull(tawep.getClaimType())%>"/>
			<input name="claimStatus" id="claimStatus" type="hidden"  value="<%=CommonUtils.checkNull(tawep.getStatus())%>"/>
			<input type="hidden" name="claimType" value="<%=tawep.getClaimType()%>"/>
			<input type="hidden" name="VIN" id="VIN" value="<%=CommonUtils.checkNull(tawep.getVin())%>" />
			<input type="hidden" name="RO_NO" id="RO_NO" value="<%=CommonUtils.checkNull(tawep.getRoNo())%>" />
			<input type="hidden" name="V_ID" id="V_ID" value="<%=CommonUtils.checkNull(tawep.getVehicleId())%>" />
			<input type="hidden" name="C_ID" id="C_ID" value="<%=CommonUtils.checkNull(tawep.getCtmId())%>" />
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
						<script type="text/javascript">
							document.write(getItemValue('<%=tawep.getClaimType()%>'));
	       				</script>
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
						配置：
					</td>
					<td >
						<%=CommonUtils.checkNullEx(tawep.getPackageName())%>
					</td>
					<td class="table_edit_2Col_label_7Letter">
						颜色：
					</td>
					<td>
						<%=CommonUtils.checkNull(tawep.getColor())%>
					</td>
					<td class="table_edit_2Col_label_7Letter">
						车辆用途：
					</td>
					<td ><%=CommonUtils.checkNull(tawep.getCarUseType())%></td>
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
						 <%=CommonUtils.checkNull(Utility.handleDate1(tawep
							.getRoStartdate()))%>
					</td>
					<td class="table_edit_2Col_label_7Letter">
						工单结算时间：
					</td>
					<td>
						 <%=CommonUtils.checkNull(Utility.handleDate1(tawep
							.getRoEnddate()))%> 
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
						系统保养次数：
					</td>
					<td >
					<span style="color: red">
					<%=CommonUtils.checkNull(tawep.getFreeTimes())%>
					</span>
					</td>
					
					<td class="table_edit_2Col_label_7Letter">
						单据保养次数：
					</td>
					<td colspan="4">
						<% if(tawep.getFreeMAmount()>0){
						
						out.print(tawep.getFreeMAmount());
					} else{
						out.print("");
					}%>
					</td>
					
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						索  赔  员：
					</td>
					<td>
						 <%=CommonUtils.checkNull(tawep.getReporter())%> 
					</td>
					<td class="table_edit_2Col_label_7Letter">
						索赔主管电话：
					</td>
					<td colspan="4" ><%=CommonUtils.checkNull(tawep.getClaimDirectorTelphone())%></td>
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
						<%=CommonUtils.checkNullEx(tawep.getCtmName())%>
					</td>
					<td class="table_edit_2Col_label_7Letter">
						车主电话：
					</td>
					<td ><%=CommonUtils.checkNull(tawep.getCtmPhone())%>
					</td>
					<td class="table_edit_2Col_label_7Letter">
						车主地址：
					</td>
					<td ><%=CommonUtils.checkNull(tawep.getCtmAddress())%></td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						送修人姓名：
					</td>
					<td >
						<%=CommonUtils.checkNullEx(tawep.getDeliverer())%>
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
				<th colspan="8">
					<img class="nav" src="../../../img/subNav.gif" />
					主因件信息
				</th>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						供应商代码：
					</td>
					<td id="PRODUCT_CODE<%=mainCodeList.get(a).get("PART_ID")%>"  >
						<%=mainCodeList.get(a).get("DOWN_PRODUCT_CODE") %>
					</td>
					<td class="table_edit_2Col_label_7Letter">
						供应商名称：
					</td>
					<td id="PRODUCT_NAME<%=mainCodeList.get(a).get("PART_ID")%>"><%=mainCodeList.get(a).get("DOWN_PRODUCT_NAME") %>
					</td>
					<td class="table_edit_2Col_label_7Letter" rowspan="2">
					<span style="color: red;font-size: 20px">
						状态：
					</span>
					</td>
					<td rowspan="2" id="partStatus<%=mainCodeList.get(a).get("PART_ID") %>" >
					<span style="color: red;font-size: 25px">
						<script type="text/javascript">
							document.write(getItemValue('<%=mainCodeList.get(a).get("AUDIT_STATUS") %>'));
	       				</script>
					</span>
					</td>
					<td class="table_edit_2Col_label_7Letter" rowspan="2">
					<span style="color: red;font-size: 20px">
						操作人：
					</span>
					</td>
					<td rowspan="2" id="partStatusS<%=mainCodeList.get(a).get("PART_ID") %>" >
					<span style="color: red;font-size: 23px">
					 <%=CommonUtils.checkNull((String)mainCodeList.get(a).get("NAME"))%> &nbsp;
					</span>
					</td>
				</tr>
				<tr>
					<td align="center" colspan="4">
					<%
					if(mainCodeList.get(a).get("AUDIT_STATUS").toString().equalsIgnoreCase(Constant.PART_AUDIT_STATUS_01.toString())&&type!=1){%><!-- 审核中 -->
						<input type="button" id="partStatus1<%=mainCodeList.get(a).get("PART_ID") %>" style="display: " onClick='part_to(1,<%=mainCodeList.get(a).get("PART_ID") %>,<%=a %>);' class="normal_btn"  value="挂起"  />
						<input type="button" id="partStatus2<%=mainCodeList.get(a).get("PART_ID") %>" style="display:none "  onClick='part_to(2,<%=mainCodeList.get(a).get("PART_ID") %>,<%=a %>);' class="normal_btn"  value="取消挂起"  />
						<input type="button" id="partStatus3<%=mainCodeList.get(a).get("PART_ID") %>" style="display: " onClick="part_audit(1,<%=mainCodeList.get(a).get("PART_ID") %>,<%=a %>);" class="normal_btn" style="display:"  value="单件审核" />
						<input onclick="selectSupplier('<%=mainCodeList.get(a).get("PART_ID")%>','<%=mainCodeList.get(a).get("PART_CODE")%>');" type="button" id="btn<%=mainCodeList.get(a).get("PART_ID")%>" style="display: " class="normal_btn" value="选择供应商" name="PRODUCT_CODE<%=mainCodeList.get(a).get("PART_ID")%>"  />
						<input type="button" id="partStatus4<%=mainCodeList.get(a).get("PART_ID") %>" onClick="part_audit(2,<%=mainCodeList.get(a).get("PART_ID") %>,<%=a %>);" style="display: " class="normal_btn"  style="display:" value="单件拒赔" />
				<%	}else if(mainCodeList.get(a).get("AUDIT_STATUS").toString().equalsIgnoreCase(Constant.PART_AUDIT_STATUS_02.toString())&&type!=1){%><!-- 挂起 -->
						<input type="button" id="partStatus1<%=mainCodeList.get(a).get("PART_ID") %>" style="display: none" onClick='part_to(1,<%=mainCodeList.get(a).get("PART_ID") %>,<%=a %>);' class="normal_btn"  value="挂起"  />
						<input type="button" id="partStatus2<%=mainCodeList.get(a).get("PART_ID") %>" style="display: "  onClick='part_to(2,<%=mainCodeList.get(a).get("PART_ID") %>,<%=a %>);' class="normal_btn"  value="取消挂起"  />
						<input type="button" id="partStatus3<%=mainCodeList.get(a).get("PART_ID") %>" style="display: none"  onClick="part_audit(1,<%=mainCodeList.get(a).get("PART_ID") %>,<%=a %>);" class="normal_btn"  style="display: none" value="单件审核" /><%} %>
		 				<input onclick="selectSupplier('<%=mainCodeList.get(a).get("PART_ID")%>','<%=mainCodeList.get(a).get("PART_CODE")%>');" type="button" id="btn<%=mainCodeList.get(a).get("PART_ID")%>" style="display:none " class="normal_btn" value="选择供应商" name="PRODUCT_CODE<%=mainCodeList.get(a).get("PART_ID")%>"  />
						<input type="button" id="partStatus4<%=mainCodeList.get(a).get("PART_ID") %>" onClick="part_audit(2,<%=mainCodeList.get(a).get("PART_ID") %>,<%=a %>);" style="display: none" class="normal_btn"  style="display:" value="单件拒赔" />
					</td>
				</tr>
				</table>
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
						<textarea name='ADUIT_REMARK1' style="font-weight: bold;" readonly="readonly" datatype="1,is_textarea,1000" maxlength="1000" rows='3' cols='28'><%=CommonUtils.checkNull(mainCodeList.get(a).get("REMARK")) %></textarea>
					</td>
					
					<td  class="tbwhite">
						原因分析：
					</td>
					<td  class="tbwhite">
						<textarea name='TROUBLE_REASON' style="font-weight: bold;" readonly="readonly" id='TROUBLE_REASON'	datatype="1,is_textarea,1000" maxlength="1000" rows='3' cols='28'><%=CommonUtils.checkNull(mainCodeList.get(a).get("TROUBLE_REASON")) %></textarea>
					</td>
					<td  class="tbwhite">
						处理结果：
					</td>
					<td  class="tbwhite">
						<textarea name='DEAL_METHOD'  style="font-weight: bold;"id='DEAL_METHOD' readonly="readonly"	datatype="1,is_textarea,1000" maxlength="1000" rows='3' cols='28'><%=CommonUtils.checkNull(mainCodeList.get(a).get("DEAL_METHOD")) %></textarea>
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
					<td>
						主因件(关联)
					</td>
				</tr>
				<tbody id="itemTable">
					<%
						for (int i = 0; i < itemLs.size(); i++) {
							ClaimListBean clb = itemLs.get(i);
							TtAsWrLabouritemBean tl = (TtAsWrLabouritemBean)clb.getMain();
							if(tl.getPartCode().equalsIgnoreCase(mainCodeList.get(a).get("PART_CODE").toString())|| tl.getMainPartCode().equalsIgnoreCase(mainCodeList.get(a).get("PART_CODE").toString())){
					%>
					<tr class="table_list_row1">
						<td>
							 <%=CommonUtils .checkNull(tl.getWrLabourcode())%> 
						<input type="hidden" name="WR_LABOURCODE" id="WR_LABOURCODE" value="<%=tl.getWrLabourcode()%>" />
						<input type="hidden" name="labourId<%=mainCodeList.get(a).get("PART_ID").toString() %>" id="<%=tl.getLabourId() %>" value="<%=tl.getLabourId() %>" />
						</td>
						<td>
						 <%=CommonUtils .checkNull(tl.getWrLabourname())%> 
						</td>
						<td>
							 <%=CommonUtils.checkNull(tl .getLabourHours())%> 
						</td>
						<td>
							 <%=CommonUtils.checkNull(tl .getLabourPrice())%> 
						</td>
						<td>
						<input type="hidden" name="labour_id" id="labour_id" value="<%=CommonUtils.checkNull(tl.getLabourId())%> "/>
							 <%=CommonUtils.checkNull(tl.getLabourAmount())%> 
						</td>
					 
						<td>
						<input name="malName" class="long_txt" disabled="disabled" id ="malName "value="<%=CommonUtils.checkNull(tl.getMalName())%>" />
					<%if(mainCodeList.get(a).get("AUDIT_STATUS").toString().equalsIgnoreCase(Constant.PART_AUDIT_STATUS_01.toString())&&type!=1){%>
						<input style="display: "  id="labouraa<%=tl.getLabourId() %>"  type="button" class="middle_btn"value="选择"  onclick="selectMalCode(this);"/>
					<%} %>
						</td>
						<td >
							<script type="text/javascript">
							document.write(getItemValue('<%=tl.getPayType()%>'));
							</script>
							  <input type="hidden" name="PAY_TYPE_ITEM" id="PAY_TYPE_ITEM" value="<%=tl.getPayType()%>" />
						</td>
						<td >
							<%=CommonUtils.checkNull(tl.getShowMainPart())%>
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
						新件供应商
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
				</tr>
				<tbody id="partTable">
					<%
						for (int i = 0; i < partLs.size(); i++) {
							ClaimListBean clb = partLs.get(i);
							TtAsWrPartsitemBean tl = (TtAsWrPartsitemBean)clb.getMain();
							if(tl.getDownPartCode().equalsIgnoreCase(mainCodeList.get(a).get("PART_CODE").toString())||tl.getMainPartCode().equalsIgnoreCase(mainCodeList.get(a).get("PART_CODE").toString())){
					%>
					<tr class="table_list_row1">
						<td>
						 <%=CommonUtils.checkNull(tl.getDownPartCode())%> 
						</td>
						<td>
						 <%=CommonUtils.checkNull(tl.getDownPartName())%> 
						</td>
						<td>
						 <%=CommonUtils.checkNull(tl.getPartCode())%> 
						 <input type="hidden" name="PART_CODE" id="PART_CODE<%=mainCodeList.get(a).get("PART_ID").toString()+i %>" value="<%=CommonUtils.checkNull(tl.getPartCode())%>" />
						</td>
						<!--  <td>
							 <%=CommonUtils.checkNull(tl.getPartName())%> 
						</td>-->
						<td>
							<input type="text" class="short_txt" name="PRODUCER_CODE" readonly value="<%=CommonUtils.checkNull(tl.getProducerCode())%>" id="PRODUCER_CODE<%=i %>" />
							<input type="hidden" readonly datatype="0,is_null" class="short_txt" name="PRODUCER_NAME" id="PRODUCER_NAMES<%=i %>" value="<%=CommonUtils.checkNull(tl.getProducerName())%>"/>
			
			<!--  		<%if(mainCodeList.get(a).get("AUDIT_STATUS").toString().equalsIgnoreCase(Constant.PART_AUDIT_STATUS_01.toString())){%>
						<a href="#" onclick="selectSupplier(this,1,'<%=CommonUtils.checkNull(tl.getPartCode())%>')">选择</a> 
					<%} %>-->
						</td>
						<td>
						 <%=(tl.getQuantity().intValue())%> 
						</td>
						<td>
							 <%=CommonUtils.checkNull(tl.getPrice())%> 
						</td>
						<td>
							 <%=CommonUtils.checkNull(tl .getAmount())%> 
							 <input type="hidden" name="appId" value="<%= tl.getId() %>" />
						     <input type="hidden" name="apppartId"  value="<%= tl.getPartId() %>" />
						</td>
						<td style="">
						 <%=tl.getCodeDesc()%> 
						</td>
						<td style="">
						 <%=tl.getPartUseName() %> 
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
						预授权审批金额
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
                <td><input   name="apply_price<%=mainCodeList.get(a).get("PART_ID") %>" id="apply_price<%=tempid2%>" value="<%=compensationMoneyList.get(i).get("APPLY_PRICE") %>" type="hidden" class="middle_txt" readonly> 
                <%=compensationMoneyList.get(i).get("APPLY_PRICE")%>
                	</td>
                	
                <td>
                <%if(mainCodeList.get(a).get("AUDIT_STATUS").toString().equalsIgnoreCase(Constant.PART_AUDIT_STATUS_01.toString())&&type!=1){%>
					 <input readonly="readonly" name="pass_price<%=mainCodeList.get(a).get("PART_ID")%>" datatype="0,isMoney,10" id="pass_price<%=tempid2%>" 
					 value="<%=compensationMoneyList.get(i).get("PASS_PRICE")%>" type="text" class="middle_txt"></td>
				<%} else{%>
				<input name="pass_price<%=mainCodeList.get(a).get("PART_ID")%>"  datatype="0,isMoney,10" id="pass_price<%=tempid2%>" 
				value="<%=compensationMoneyList.get(i).get("PASS_PRICE")%>" type="text" readonly="readonly" class="middle_txt"></td>
				<%}%>
               
				<td>
				 <%=CommonUtils.checkNull((String)compensationMoneyList.get(i).get("REASON"))%>
				 <input name="PKID<%=mainCodeList.get(a).get("PART_ID")%>" id="PKID<%=tempid2%>" value="<%=compensationMoneyList.get(i).get("PKID")%>" type="hidden" class="middle_txt"> 
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
					if(accessoryDtlList.get(i).get("MAIN_PART_CODE").toString().trim().equalsIgnoreCase(mainCodeList.get(a).get("PART_CODE").toString())){%>
							<tr align="center" class="table_list_row1">
           		<td>
               <%=accessoryDtlList.get(i).get("WORKHOUR_CODE") %>
                </td>
              <td>
             <%=accessoryDtlList.get(i).get("WORKHOUR_NAME") %>
               </td>
               <td>
               <%=accessoryDtlList.get(i).get("PRICE") %>
                <input name="FLID<%=mainCodeList.get(a).get("PART_ID")%>" id="PKID<%=accessoryDtlList.get(i).get("ID") %>" value="<%=accessoryDtlList.get(i).get("ID") %>" type="hidden" class="middle_txt"> 
               </td>
            </tr>
			<%} }%>
            </tbody>
          </table>
			<!-- 辅料结束 -->
			<!-- 其他项目开始 -->
			<table id="otherTableId" align="center" cellpadding="0" <%  if(tawep.getClaimType().intValue()==Constant.CLA_TYPE_09){out.print("style='display:'");}else{out.print("style='display:none'");} %>
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
							审核金额(元)
					</td>
					<td>
						备注
					</td>
				</tr>
				<tbody id="otherTable">
					<%
						for (int i = 0; i < otherLs.size(); i++) {
						 if(otherLs.get(i).getMainPartCode().toString().equalsIgnoreCase(mainCodeList.get(a).get("PART_CODE").toString())){
							String tempid2 = otherLs.get(i).getNetitemId().toString();
						 %> 
					<tr class="table_list_row1" >
						<td>
						 
						   <%=otherLs.get(i).getItemCode()%> 
						   <input type="hidden"  id="ItemCode<%=tempid2%>" name="ItemCode" value="<%=otherLs.get(i).getItemCode()%>" />
						    <input type="hidden"  id="netItem<%=tempid2%>" name="netItem<%=mainCodeList.get(a).get("PART_ID")%>" value="<%=otherLs.get(i).getNetitemId() %>" />
						 
						</td>
						<td>
						 <%=CommonUtils.checkNull(otherLs.get(i).getItemDesc())%> 
						</td>
						<td>
						 <%=CommonUtils.checkNull(otherLs.get(i).getApplyAmount())%> 
						</td>
						<td>
						  <%  if(otherLs.get(i).getItemCode().toString().equals("QT007")){ %> 
						  <input type="text" name="Amount<%=mainCodeList.get(a).get("PART_ID") %>" id="Amount<%=tempid2%>" datatype="0,isMoney,10" maxlength="10"  value="<%=CommonUtils.checkNull(otherLs.get(i).getBalanceAmount())%>"  />
						  <%}else if(mainCodeList.get(a).get("AUDIT_STATUS").toString().equalsIgnoreCase(Constant.PART_AUDIT_STATUS_01.toString())&&type!=1){%>
						  <input type="text"  name="Amount<%=mainCodeList.get(a).get("PART_ID") %>" id="Amount<%=tempid2%>" datatype="0,isMoney,10"  maxlength="10"  value="<%=CommonUtils.checkNull(otherLs.get(i).getBalanceAmount())%>"  />
						  <%} else{  %>
						    <input type="text" readonly="readonly" name="Amount<%=mainCodeList.get(a).get("PART_ID") %>" id="Amount<%=tempid2%>" datatype="0,isMoney,10"  maxlength="10"  value="<%=CommonUtils.checkNull(otherLs.get(i).getBalanceAmount())%>"  />
						  <%}%>
						</td>
						<td title="<%=otherLs.get(i).getRemark()==null?"":otherLs.get(i).getRemark()%>">
						<a href="#" onclick="alInfo('<%=otherLs.get(i).getRemark()==null?"":otherLs.get(i).getRemark()%>');" ><%=otherLs.get(i).getRemark()==null?"":otherLs.get(i).getRemark()%></a>
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
					申请费用：<span style="color:red" id="total">总金额(含税)： <%=tawep.getGrossCredit()%> 元整</span>
				</th>
			</table>
             <c:if test="<%=!tawep.getClaimType().equals(10661002)%>">
				 <!-- 添加附件 -->
			<table class="table_info" border="0" id="file">
				<input type="hidden" id="fjids" name="fjids"/>
			    <tr colspan="8">
			        <th>
					<img class="nav" src="../../../img/subNav.gif" />
					&nbsp;附件列表：
					</th>
				</tr>
				<tr>
      				<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv2.jsp" /></td>
    			</tr>
    			<%for(int i=0;i<attachLs.size();i++) { %>
    			<script type="text/javascript">
    			addUploadRowByDbView('<%=CommonUtils.checkNull(attachLs.get(i).getFilename()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFjid()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFileurl())%>','<%=CommonUtils.checkNull(attachLs.get(i).getFjid()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getPjid()) %>','<%=request.getContextPath()%>/util/FileDownLoad/FileDownLoadQuery.do?fjid=' );
    			</script>
    			<%} %>
 			</table>
 			</c:if>
 			<table class="table_edit">
			<tr>
				<th colspan="10" align="left"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" />
				&nbsp;<a href="#" onclick="hiddenTable('authTable');" >索赔单审核明细</a>
				</th>
		 	</tr>
		 </table>
		 <table class="table_list" style="border-bottom: 1px solid #DAE0EE;display: none" id="authTable">
			 <tr class='table_list_th'>
	        <th nowrap="true">序号</th>
	        <th nowrap="true">授权人</th>
	        <th nowrap="true">日期</th>
	        <th nowrap="true">授权结果</th>
	        <th nowrap="true">授权备注</th>
	      </tr>
	       <c:set var="numSize"  value="1" />
		 <c:forEach var="list" items="${appAuthls}" varStatus="s" >
		  <tr class="${numSize}%2==0?"table_list_row1":"table_list_row2"">
	          
	            <td>${numSize}</td>
	            <td>
	              ${list.NAME}&nbsp;
	          </td>
	          <td>
					${list.AUDIT_TIME}&nbsp;</td>
	          <td>
	              ${list.CODE_DESC}&nbsp;
	          </td>
	          <td width="300">
					${list.AUDIT_REMARK}&nbsp;
				</td>
	           </tr>
	           <c:set var="numSize"  value="${numSize+1}" />
			</c:forEach>
	    </table>
 			 <table class="table_edit"  id="table_edit_remark" > 
        	 <th colspan="11"  ><img src="../../../img/subNav.gif" alt="" class="nav" />
					审核说明
				</th>
				<tr>
					<td colspan="11" class="tbwhite">
						<textarea name='ADUIT_REMARK' id='AUDIT_REMARK'	datatype="1,is_textarea,100" maxlength="100" rows='2' cols='120'></textarea>
					</td>
				</tr>
          </table>
          <c:if test="<%=tawep.getClaimType().equals(10661002)%>">
		 <!-- 添加附件 开始  -->
        <table id="add_file"  width="75%" class="table_info" border="0" id="file">
	    		<tr>
	        		<th>
						<input type="hidden" id="fjids" name="fjids"/>
						<img class="nav" src="<%=contextPath%>/img/subNav.gif" />附件信息
						<font color="red">
							<span id="span1"></span>
						</font>
					</th>
				</tr>
				<tr>
    				<td width="75%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
  				</tr>
  				<%if(fileList!=null){
	  				for(int i=0;i<fileList.size();i++) { %>
					  <script type="text/javascript">
				    		addUploadRowByDbView('<%=CommonUtils.checkNull(fileList.get(i).getFilename()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>','<%=CommonUtils.checkNull(fileList.get(i).getFileurl())%>','<%=CommonUtils.checkNull(fileList.get(i).getFjid()) %>','<%=request.getContextPath()%>/util/FileDownLoad/FileDownLoadQuery.do?fjid=' );
				    	</script>
				<%}}%>
			</table> 
  		<!-- 添加附件 结束 -->
		</br>
		</c:if>
			<table border="0" cellspacing="0" cellpadding="0" class="table_edit">
				<tr>
					<td  colspan="5" align="center">
					<input class="normal_btn" type="button" value="维修历史" name="historyBtn"
		 onclick="openWindowDialog(1);"/>
		
		<input class="normal_btn" type="button" value="授权历史" name="historyBtn"
		 onclick="openWindowDialog(2);"/>
		
		<input class="normal_btn" type="button" value="保养历史" name="historyBtn" 
		onclick="openWindowDialog(3);"/>
		<c:if test="${type!=1}">
		<input type="button" onClick="submit_to(1);" id="btn1" class="normal_btn"  value="审核通过"  />
		 <input type="button" onClick="submit_to(2);" id="btn2"class="normal_btn"   value="审核退回" />
		 <input type="button" onClick="submit_to(3);" id="btn4"class="normal_btn"   value="整单拒赔" />
		</c:if>
		<c:if test="${type==1}">
		<input type="button" onClick="submit_to(4);" id="btn5" class="normal_btn"  value="撤销审核"  />
		</c:if>
		
		 <input type="button" onClick="history.back();" id="btn3" class="normal_btn"   value="返回" />
			 </td>
		 <td>
		 	<input class="long_btn" type="button" id="three_package_set_btn" value="三包判定" onclick="threePackageSet();"/>
		 	<input class="long_btn" type="button" id="salesInfo" value="实销信息" onclick="salesInfoFwd();"/>
			 </td>
				</tr>
			</table>
		</form>
		<script type="text/javascript">
		function salesInfoFwd(){
	var v_id=$('V_ID').value;
	var c_id=$('C_ID').value;
	if(v_id==""){
		MyAlert("未获取到车辆标识!");
		return false;
	}
	if(c_id==""||c_id.length<16){
		MyAlert("该车无客户信息!");
		return false;
	}
	OpenHtmlWindow('<%=contextPath%>/sales/customerInfoManage/SalesReport/customerInfoQuery.do?ctm_id='+c_id+'&vehicle_id='+v_id,900,500);
}
		
		function selectSupplier(id,partcode){
	 		parent.OpenHtmlWindow('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/selectSupplierCodeForward.do?partcode='+partcode+"&id="+id,800,500);
	 	}
		function setMainSupplierCode(code,name,id){
		
		if(confirm("确认修改?")){	
			$('btn1').disabled=true;
			$('btn2').disabled=true;
			$('btn3').disabled=true;
			$('btn4').disabled=true;
			$('partStatus1'+id).disabled=true;
			$('partStatus3'+id).disabled=true;
			$('partStatus4'+id).disabled=true;
			$('btn'+id).disabled=true;
		var url= "<%=contextPath%>/claim/application/ClaimManualAuditing/modifySuppCode.json?ID="+id+"&code="+code;
			makeNomalFormCall(url,molAfter,'fm','');
		}
		}
function molAfter(json){
			var str = json.ACTION_RESULT;
			var id = json.part_id;
		if(str==1){
			MyAlert("修改成功!");
			$('btn1').disabled=false;
			$('btn2').disabled=false;
			$('btn3').disabled=false;
			$('btn4').disabled=false;
			$('partStatus1'+id).disabled=false;
			$('partStatus3'+id).disabled=false;
			$('partStatus4'+id).disabled=false;
			$('btn'+id).disabled=false;
			$('PRODUCT_NAME'+id).innerHTML=json.name;
			$('PRODUCT_CODE'+id).innerHTML=json.code;
		}else{
			MyAlert("审核失败,请联系管理员!");
			$('btn1').disabled=false;
			$('btn2').disabled=false;
			$('btn3').disabled=false;
			$('btn4').disabled=false;
	}
}


		function part_audit(type,part_id,a){
			if(submitForm(fm)==false)return;
			if(checkData("")) return;
			var str =type==1?"单件审核":"单件拒赔";
			if(confirm("确认"+str)){
			 $('partStatus1'+part_id).disabled=true;
			$('partStatus3'+part_id).disabled=true;
			$('partStatus4'+part_id).disabled=true;
			$('btn'+part_id).disabled=true;
			var url= "<%=contextPath%>/claim/application/ClaimManualAuditing/auditPartStatus.json?part_id="+part_id+"&type="+type+"&index="+a;
			makeNomalFormCall(url,auditfter,'fm','');
		}
		}
		function auditfter(json){
			if(json.succ=="succ"){
			var type= json.type;
			var str =type==1?"审核通过":"审核拒赔";
			$('partStatus'+json.part_id).innerHTML='<span style="color: red;font-size: 25px"> '+str+'</span>';  
			$('partStatusS'+json.part_id).innerHTML='<span style="color: red;font-size: 25px">'+json.name+' </span>';
				var bcPass = document.getElementsByName("PKID"+json.part_id);//补偿费判断
				for(var i=0;i<bcPass.length;i++){
					$("pass_price"+bcPass[i].value).readOnly = true;
				}
				var netItem = document.getElementsByName("netItem"+json.part_id);//外出费判断
				for(var i=0;i<netItem.length;i++){
					$("Amount"+netItem[i].value+"").readOnly = true;
				}  
				var labourId = document.getElementsByName("labourId"+json.part_id);//故障代码选择矿
				for(var i=0;i<labourId.length;i++){
					$('labouraa'+labourId[i].value+"").style.display="none";
				}
				 $('partStatus1'+json.part_id).disabled=false;
				$('partStatus3'+json.part_id).disabled=false;
				$('partStatus4'+json.part_id).disabled=false;
				$('btn'+json.part_id).disabled=false;
				$('btn'+json.part_id).style.display="none";
				$('partStatus1'+json.part_id).style.display="none";
			 	$('partStatus3'+json.part_id).style.display="none";
			 	$('partStatus4'+json.part_id).style.display="none";
				MyAlert("操作成功!");
			}else{
			MyAlert("操作失败!");
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
		 	var bcPass = document.getElementsByName("pass_price"+str[i]);//补偿费判断
		 	var bcApp = document.getElementsByName("apply_price"+str[i]);//补偿费判断
		 	for(var j=0;j<bcApp.length;j++){
		 		if(parseFloat(bcPass[j].value)>parseFloat(bcApp[j].value)){
		 			MyAlert("输入补偿费,不能大于申请费用!");
		 			returnBack = true;
		 			break;
		 		}
		 	}
		 }
		 }
		 return returnBack;
		}
		function part_to(type,part_id,a){
		if(submitForm(fm)==false)return;
		if(checkData(part_id))return;
		var str = type==1?"挂起":"取消挂起";
		var fms = "fm"+a;
		if(confirm("确认"+str)){
			$('partStatus'+type+part_id).disabled=true;
			$('partStatus1'+part_id).disabled=true;
			$('partStatus3'+part_id).disabled=true;
			$('partStatus4'+part_id).disabled=true;
			$('btn'+part_id).disabled=true;
			var url= "<%=contextPath%>/claim/application/ClaimManualAuditing/modifyPartStatus.json?part_id="+part_id+"&type="+type+"&index="+a;
			makeNomalFormCall(url,modAfter,'fm','');
		}
		}
		function modAfter(json){
			var type= json.type;
			$('partStatus'+type+json.part_id).disabled=false;
			$('partStatusS'+json.part_id).innerHTML='<span style="color: red;font-size: 25px">'+json.name+' </span>';
			if(type==1){//如果是1，挂起
				$('partStatus2'+json.part_id).style.display="";
				$('partStatus1'+json.part_id).style.display="none";
				$('partStatus3'+json.part_id).style.display="none";
				$('partStatus4'+json.part_id).style.display="none";
				$('btn'+json.part_id).style.display="none";
				$('partStatus'+json.part_id).innerHTML='<span style="color: red;font-size: 25px">挂起 </span>';  
				var bcPass = document.getElementsByName("PKID"+json.part_id);//补偿费判断
				for(var i=0;i<bcPass.length;i++){
					$("pass_price"+bcPass[i].value).readOnly = true;
				}
				var netItem = document.getElementsByName("netItem"+json.part_id);//外出费判断
				for(var i=0;i<netItem.length;i++){
					$("Amount"+netItem[i].value+"").readOnly = true;
				}
				MyAlert("挂起成功!");
			}else{
				$('partStatus2'+json.part_id).style.display="none";
				$('partStatus1'+json.part_id).style.display="";
				$('partStatus3'+json.part_id).style.display="";
				$('partStatus4'+json.part_id).style.display="";
				$('btn'+json.part_id).style.display="";
				$('partStatus1'+json.part_id).disabled=false;
				$('partStatus3'+json.part_id).disabled=false;
				$('partStatus4'+json.part_id).disabled=false;
				$('btn'+json.part_id).disabled=false;
				$('partStatus'+json.part_id).innerHTML='<span style="color: red;font-size: 25px">审核中 </span>';
				var bcPass = document.getElementsByName("PKID"+json.part_id);//补偿费判断
				for(var i=0;i<bcPass.length;i++){
					$("pass_price"+bcPass[i].value).readOnly = false;
				}
					var netItem = document.getElementsByName("netItem"+json.part_id);//外出费判断
				for(var i=0;i<netItem.length;i++){
					$("Amount"+netItem[i].value).readOnly = false;
				}
				MyAlert("取消挂起成功!");
			}
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
		function submit_to(type){
		if(submitForm(fm)==false)return;
		if(checkData("")) return;
			if(type==2&&($('ADUIT_REMARK').value==""||$('ADUIT_REMARK').value==null)){
				MyAlert("审核退回必须填写备注!");
				return ;
			}
			if(type==3&&($('ADUIT_REMARK').value==""||$('ADUIT_REMARK').value==null)){
				MyAlert("审核拒绝必须填写备注!");
				return ;
			}
			if(type==4&&($('ADUIT_REMARK').value==""||$('ADUIT_REMARK').value==null)){
				MyAlert("撤销审核必须填写备注!");
				return ;
			}
			var str = "";
			if(type==1){
				str="通过";
				}else if(type==2){
					str = "退回";
					}else if(type==3){
						str="拒赔";
						}else if(type==4){
						str="撤销";
						}
			 MyConfirm("确认"+str+"?",audit,[type]);
		}
		function audit(type){
		    if(type!=4){
		    $('btn1').disabled=true;
			$('btn2').disabled=true;
			$('btn3').disabled=true;
			$('btn4').disabled=true;
		    }else{
		      $('btn5').disabled=true;
		    }
			var id = $('claimId').value;
			var url= "<%=contextPath%>/claim/dealerClaimMng/DealerClaimReport/checkReportClaimNotOne.json?ID="+id+"&type="+type;
			makeNomalFormCall(url,afterCheckNotone,'fm','');
		}
		function afterCheckNotone(json){
			if(json.succ=="1"){
				var url= "<%=contextPath%>/claim/dealerClaimMng/DealerClaimReport/reportClaim.json?ID="+json.id+"&type="+json.type;
				makeNomalFormCall(url,afterCall,'fm','');
			}else{
				MyAlert("提示：有人在操作或单子已经被操作过了~！");
			}
		}
		function afterCall(json){
			var str = json.ACTION_RESULT;
			if(str==1){
				MyAlert("审核成功!");
				if(json.type!=4){
					window.location.href = "<%=contextPath%>/claim/application/ClaimManualAuditing/claimManualAuditingInit.do";
				}else{
				   window.location.href = "<%=contextPath%>/claim/application/ClaimManualAuditing/claimManualAuditingForInit.do";
				}
			}else{
			 $('btn1').disabled=false;
			$('btn2').disabled=false;
			$('btn3').disabled=false;
			$('btn4').disabled=false;
				MyAlert(str);
				//window.location.href = "<%=contextPath%>/claim/application/ClaimManualAuditing/claimManualAuditingInit.do";
			}
		}
		var myobj="";
function selectMalCode(obj) {
			myobj="";
			myobj = obj.parentNode;
			OpenHtmlWindow('<%=contextPath%>/claim/application/ClaimManualAuditing/selectMalCode.do',700,600);
		}
		
function setMalInfo(v1,v2,v3){
		var tr = this.getRowObj(myobj);
		if(confirm("确认修改?")){
		var id=tr.childNodes[4].childNodes[0].value;	
			$('btn1').disabled=true;
			$('btn2').disabled=true;
			$('btn3').disabled=true;
			$('btn4').disabled=true;
		var url= "<%=contextPath%>/claim/application/ClaimManualAuditing/modifyMalCode.json?ID="+id+"&malId="+v1;
			makeNomalFormCall(url,malAfter,'fm','');
			tr.childNodes[5].childNodes[0].value=v2+"--"+v3;
		}
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
		var RO_NO = $('RO_NO').value;
		if(type==1){
		targetUrl = '<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/maintaimHistory.do?VIN='+vin;
		}else if(type==2){
			targetUrl = '<%=contextPath%>/repairOrder/RoMaintainMain/authDetail.do?VIN='+vin+'&RO_NO='+RO_NO;
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
				var arr = document.getElementsByName('PART_CODE');
				var PAY_TYPE_PART = document.getElementsByName('PAY_TYPE_PART');
				var WR_LABOURCODE= document.getElementsByName('WR_LABOURCODE');
				var PAY_TYPE_ITEM= document.getElementsByName('PAY_TYPE_ITEM');
				var str = ''; 
				for(var i=0;i<arr.length;i++)
					str = str+arr[i].value+"," ;
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
	function alInfo(str){
		MyAlert(str);
	}
</script>
	</BODY>
</html>
