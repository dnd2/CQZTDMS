<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.util.LinkedList"%>
<%@page import="com.infodms.dms.po.TtAsWrApplicationExtPO"%>
<%@page import="com.infodms.dms.po.TtAsWrNetitemExtPO"%>
<%@page import="com.infodms.dms.po.TtAsActivityPO"%>
<%@page import="com.infodms.dms.po.FsFileuploadPO"%>
<%@page import="com.infodms.dms.po.TtAsWrGamefeePO"%>
<%@page import="com.infodms.dms.bean.ClaimListBean"%>
<%@page import="com.infodms.dms.bean.TtAsWrLabouritemBean"%>
<%@page import="com.infodms.dms.bean.TtAsWrPartsitemBean"%>
<%@page import="com.infodms.dms.common.Utility"%>
<%@page import="com.infodms.dms.util.CommonUtils"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%
	String contextPath = request.getContextPath();
 
%>
<%
			/** 格式化金钱时保留的小数位数 */
			int minFractionDigits = 2;
    		/** 当格式化金钱为空时，默认返回值 */
    		String defaultValue = "0";

			TtAsWrApplicationExtPO tawep = (TtAsWrApplicationExtPO) request.getAttribute("application");
			List<ClaimListBean> itemLs = (LinkedList<ClaimListBean>) request.getAttribute("itemLs");
			List<ClaimListBean> partLs = (LinkedList<ClaimListBean>) request.getAttribute("partLs");
			List<TtAsWrNetitemExtPO> otherLs = (LinkedList<TtAsWrNetitemExtPO>) request.getAttribute("otherLs");
			List<FsFileuploadPO> attachLs = (LinkedList<FsFileuploadPO>)request.getAttribute("attachLs");
			String id = (String) request.getAttribute("ID");
		%>
		<%
			//List feeTypeList = (List) request.getAttribute("FEETYPE");//保养费用集合 
			List<TtAsWrGamefeePO> feeTypeList = (List<TtAsWrGamefeePO>) request.getAttribute("FEE"); //保养费用参数对应的值
			String maintainFeeStr = "";
			String maintainfeeOrderStr = "";
			if (feeTypeList!=null){
				if (feeTypeList.size()>0) {
					for(int i=0;i<feeTypeList.size();i++) {
						maintainFeeStr += ","+feeTypeList.get(i).getManintainFee();
						maintainfeeOrderStr += ","+feeTypeList.get(i).getMaintainfeeOrder();
					}
				}
			}
			TtAsActivityPO tap = (TtAsActivityPO) request.getAttribute("ACTIVITY");
		%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@page import="com.infodms.dms.common.Constant"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
		<TITLE>索赔单编辑修改</TITLE>
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
function doInit(){
	var type=$('type').value;
	if(type==<%=Constant.CLA_TYPE_02 %>){
		$('feeTableId').style.display="";
		$('feeId').style.display="";
	}esle{
	if(type==<%=Constant.CLA_TYPE_06 %>){
		$('activityTableId').style.display="";
		$('activityTableId0').style.display="";
	}
	}
}
</SCRIPT>
	</HEAD>
	<BODY onload="doInit();" onkeydown="keyListnerResp();">
		<div class="navigation">
			<img src="../../../img/nav.gif" />
			&nbsp;当前位置：售后服务管理&gt;索赔申请&gt;索赔申请审核
		</div>

		<form method="post" name="fm" id="fm">
		<input name="claimId" id ="claimId" type="hidden" value="<%=tawep.getId() %>"/>
		<input name="type" id="type" type="hidden"  value="<%=CommonUtils.checkNull(tawep.getClaimType())%>"/>
			<table border="0" align="center" cellpadding="1" cellspacing="1"
				class="table_edit">
				<th colspan="6">
					<img class="nav" src="../../../img/subNav.gif" />
					基本信息
				</th>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						经销商代码：
					</td>
					<td>
						<%=CommonUtils.checkNull(tawep.getDealerCode())%>
					</td>
					<td class="table_edit_2Col_label_7Letter">
						经销商名称：
					</td>
					<td>
						<%=CommonUtils.checkNull(tawep.getDealerName())%>
					</td>
					
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						索赔申请单号：
					</td>
					<td>
						<%=CommonUtils.checkNull(tawep.getClaimNo())%>
					</td>
					<td class="table_edit_2Col_label_7Letter">
						维修工单号：
					</td>
					<td align="left">
						 <%=CommonUtils.checkNull(tawep.getRoNo())%> 
						 <input type="hidden" name="RO_NO" id="RO_NO" value="<%=CommonUtils.checkNull(tawep.getRoNo())%>" />
					</td>
					</tr>
					<tr>
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
						进厂里程数：
					</td>
					<td>
						 <fmt:formatNumber value="<%=CommonUtils.checkNull(tawep.getInMileage())%>" maxIntegerDigits="20" maxFractionDigits="10"  pattern="0.00"/>
					<input type="hidden" name="IN_MILEAGE" id="IN_MILEAGE" value="<%=CommonUtils.checkNull(tawep.getInMileage())%>" />
					</td>
					<td class="table_edit_2Col_label_7Letter">
						索  赔  员：
					</td>
					<td>
						 <%=CommonUtils.checkNull(tawep.getReporter())%> 
					</td>
					
				</tr>
				<tr>
				<td class="table_edit_2Col_label_7Letter">
						结算基地：
					</td>
				<td>
				<script type="text/javascript">
						document.write(getItemValue('<%=CommonUtils.checkNull(tawep.getBalanceYieldly())%>'));
				 </script>&nbsp;
					</td>
					<td class="table_edit_2Col_label_7Letter">
						质量等级：
					</td>
					<td>
					 <script type="text/javascript">
					 document.write(getItemValue('<%=CommonUtils.checkNull(tawep.getQuelityGrate())%>'));
				        </script>
					</td>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_7Letter">
						索赔主管电话：
					</td>
					<td colspan="3"><%=CommonUtils.checkNull(tawep.getClaimDirectorTelphone())%></td>
				</tr>
				</table>
				<table class="table_edit">
				<th colspan="6">
					<img class="nav" src="../../../img/subNav.gif" />
					车辆信息
				</th>
				<tr>
					<td class="table_edit_3Col_label_5Letter" >
						VIN：
					</td>
					<td align="left">
						 <%=CommonUtils.checkNull(tawep.getVin())%> 
					 <input type="hidden" name="VIN" id="VIN" value="<%=CommonUtils.checkNull(tawep.getVin())%>" />
					</td>
					<td class="table_edit_3Col_label_7Letter">
						<span class="zi">发动机号：</span>
					</td>
					<td align="left" id="ENGINE_NOTD">
					<%=CommonUtils.checkNull(tawep.getEngineNo())%>
					</td>
					<td class="table_edit_3Col_label_7Letter">
						<span class="zi">牌照号：</span>
					</td>
					<td id="LICENSE_NOTD">
					<%=CommonUtils.checkNull(tawep.getLicenseNo())%>
					</td>
				</tr>
				<tr>
					<td class="table_edit_3Col_label_5Letter">
						品牌：
					</td>
					<td id="BRAND_NAME">
						<%=CommonUtils.checkNullEx(tawep.getBrandName())%>
					</td>
					<td class="table_edit_3Col_label_7Letter">
						车系：
					</td>
					<td id="SERIES_NAME"><%=CommonUtils.checkNull(tawep.getSeriesName())%>
					</td>
					<td class="table_edit_3Col_label_7Letter">
						车型：
					</td>
					<td id="MODEL_NAME"><%=CommonUtils.checkNull(tawep.getModelName())%></td>
				</tr>
				<tr>
				<td class="table_edit_3Col_label_5Letter">
						<span class="zi">产地：</span>
					</td>
					<td id="YIELDLY">
					<%=CommonUtils.checkNull(tawep.getYieldlyName())%>
					</td>
					<td class="table_edit_3Col_label_7Letter">
						<span class="zi">购车日期：</span>
					</td>
					<td id="GUARANTEE_DATE_ID">
						<%=CommonUtils.checkNull(Utility.handleDate(tawep.getGuaranteeDate()))%>
					</td>
					<td class="table_edit_2Col_label_7Letter">
						<span class="zi">生产日期：</span>
					</td>
					<td id="PRODUCT_DATE_ID">
					    <%=CommonUtils.checkNull(Utility.handleDate(tawep.getProductDate()))%>
					</td>
					<input type="hidden" name="GUARANTEE_DATE" id="GUARANTEE_DATE"  class="short_txt"
							value='<%=CommonUtils.checkNull(Utility.handleDate(tawep
							.getGuaranteeDate()))%>'
							 hasbtn="true"
							 callFunction="showcalendar(event, 'GUARANTEE_DATE', false);" />
				</tr>
				<tr>
					<td class="table_edit_3Col_label_5Letter">
						<span class="zi">颜色：</span>
					</td>
					<td nowrap="true">
						<%=CommonUtils.checkNull(tawep.getColor())%>
					</td>
				</tr>
				</table>
			<table border="0" align="center" cellpadding="0" cellspacing="1"
				class="table_edit">
				<th colspan="8">
					<img class="nav" src="../../../img/subNav.gif" />
					申请内容
				</th>
				<tr>
					<td class="table_edit_2Col_label_5Letter">
							索赔类型：
					</td>
					<td class="tbwhite">
						<input type="hidden" name="claimType" value="<%=tawep.getClaimType()%>"/>
						<script type="text/javascript">
							document.write(getItemValue('<%=tawep.getClaimType()%>'));
	       				</script>
					</td>
					<td id="feeTableId" class="table_edit_2Col_label_5Letter" style="display: none">
					保养次数：
					</td>
					<td id="feeId" style="display: none" >
					 <%=tawep.getFreeMAmount() %> 
					</td>
					<td id="activityTableId" class="table_edit_2Col_label_5Letter" style="display: none">
						活动编号：</td>
					<td id="activityTableId0" style="display: none">
					 <%=tawep.getCampaignName()%> 
						</td>
				</tr>
				
				<tr>
					<td class="table_edit_2Col_label_5Letter">
							故障描述：
					</td>
					<td class="tbwhite" >
						<textarea name='TROUBLE_DESC' id='TROUBLE_DESC' style="font-weight: bold;" 
							datatype="1,is_textarea,100" rows='3' cols='50'><%=tawep.getTroubleDesc() == null ? "" : tawep
					.getTroubleDesc()%></textarea>
					</td>
				
					<td class="table_edit_2Col_label_5Letter">
							故障原因：
					</td>
					<td colspan="3" class="tbwhite">
						<textarea name='TROUBLE_REASON'  style="font-weight: bold;" datatype="1,is_textarea,100" 
							id='TROUBLE_REASON' rows='3' cols='50'><%=tawep.getTroubleReason() == null ? "" : tawep
					.getTroubleReason()%></textarea>
				</tr>
				<tr>
					<td class="table_edit_2Col_label_5Letter">
							维修措施：
					</td>
					<td  class="tbwhite">
						<textarea name='REPAIR_METHOD' style="font-weight: bold;" datatype="1,is_textarea,100" 
							id='REPAIR_METHOD' rows='3' cols='50'><%=tawep.getRepairMethod() == null ? "" : tawep
					.getRepairMethod()%></textarea>
					</td>
					<td class="table_edit_2Col_label_5Letter">
							申请备注：
					</td>
					<td colspan="3" class="tbwhite">
						<textarea datatype="1,is_textarea,100" style="font-weight: 50px;" name='APP_REMARK' 
							id='APP_REMARK' rows='3' cols='50'><%=CommonUtils.checkNull(tawep.getRemark()) %></textarea>
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
					<!-- zhumingwei 2011-02-12 end -->
					<td style="display:none">
						付费类型
					</td>
					<td style="">
						是否索赔
					</td>
					<td style="display:none">
						<input type="button" class="normal_btn" value="新增" style="display:none" id="itembutton"
							name="button422" onClick="javascript:addRow('itemTable');" />
					</td>
				</tr>
				<tbody id="itemTable">
					<%
						for (int i = 0; i < itemLs.size(); i++) {
					%>
					<tr class="table_list_row1">
						<%
							ClaimListBean clb = itemLs.get(i);
						TtAsWrLabouritemBean tl = (TtAsWrLabouritemBean)clb.getMain();
						 %>
						
						<td>
							 <%=CommonUtils .checkNull(tl.getWrLabourcode())%> 
						<input type="hidden" name="WR_LABOURCODE" id="WR_LABOURCODE" value="<%=tl.getWrLabourcode()%>" />
						</td>
						<td>
						 <%=CommonUtils .checkNull(tl.getWrLabourname())%> 
							</span>
						</td>
						<td>
							 <%=CommonUtils.checkNull(tl .getLabourHours())%> 
						</td>
						<td>
							 <%=CommonUtils.checkNull(tl .getLabourPrice())%> 
						</td>
						<td>
						<input type="hidden" name="mal_id" id="mal_id" value="<%=CommonUtils.checkNull(tl.getLabourId())%> "/>
							 <%=CommonUtils.checkNull(tl.getLabourAmount())%> 
						</td>
					 
						<td>
						<input name="malName" class="long_txt" disabled="disabled" id ="malName "value="<%=CommonUtils.checkNull(tl.getMalName())%>" />
						<input type="button" class="middle_btn"value="选择"  onclick="selectMalCode(this);"/>
						</td>
						<td >
							<script type="text/javascript">
							document.write(getItemValue('<%=tl.getPayType()%>'));
							</script>
							  <input type="hidden" name="PAY_TYPE_ITEM" id="PAY_TYPE_ITEM" value="<%=tl.getPayType()%>" />
						</td>
						</tr>
						<% 
						}
						%>
						
				</tbody>
			</table>

			<table id="partTableId" border="0" align="center" cellpadding="0"
				cellspacing="1" class="table_list">

				<th colspan="16" align="left">
					<img class="nav" src="../../../img/subNav.gif" />
					索赔配件
				</th>
				<tr align="center" class="table_list_row1">
					<td>
						是否三包
					</td>
					<td>
						新件代码
					</td>
					<td>
						旧件
					</td>
					<td>
						新件名称
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
						新件供应商代码
					</td>
					<td>
						旧件供应商代码
					</td>
					<td>
						故障描述
					</td>
					<td>
						索赔工时
					</td>
					<td>
					是否索赔
					</td>
					<td>
					关联主因件
					</td>
					<td>
					责任性质
					</td>
					<td>
					处理方式
					</td>
					<%-- 
					<td>
					是否回运
					</td>
					--%>
				</tr>
				<tbody id="partTable">
					<%
						for (int i = 0; i < partLs.size(); i++) {
					%>
					<tr class="table_list_row1">
					<%
							ClaimListBean clb = partLs.get(i);
							TtAsWrPartsitemBean tl = (TtAsWrPartsitemBean)clb.getMain();
						%>
						<td>
						<%if (tl.getIsGua()==1) {%>
						 <input type="checkbox" name="IS_GUA" disabled checked/>
						<%}else{  %>
						 <input type="checkbox" disabled /><input type="hidden" name="IS_GUA" value="off"/>
						<%} %>
						</td>
						<td>
						 <%=CommonUtils.checkNull(tl.getPartCode())%> 
						 <input type="hidden" name="PART_CODE" id="PART_CODE" value=" <%=CommonUtils.checkNull(tl.getPartCode())%> " />
						</td>
						<td>
						 <%=CommonUtils.checkNull(tl.getDownPartCode())%> 
						</td>
						<td>
							 <%=CommonUtils.checkNull(tl.getPartName())%> 
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
						<td>
						 <input type="text" class="short_txt" name="PRODUCER_CODE" readonly value="<%=CommonUtils.checkNull(tl.getProducerCode())%>" id="PRODUCER_CODE<%=i %>" /><input type="hidden"/><input type="hidden" readonly datatype="0,is_null" class="short_txt" name="PRODUCER_NAME" id="PRODUCER_NAMES<%=i %>" value="<%=CommonUtils.checkNull(tl.getProducerName())%>"/><a href="#" onclick="selectSupplier(this,1,'<%=CommonUtils.checkNull(tl.getPartCode())%>')">选择</a> 
						</td>
						
						<td>
						 <input type="text" class="short_txt" name="PRODUCER_CODE1" readonly value="<%=CommonUtils .checkNull(tl.getDownProductCode())%>" id="PRODUCER_CODE0<%=i %>" /><input type="hidden"/><input type="hidden" readonly datatype="0,is_null"  class="short_txt" name="PRODUCER_NAME1" id="PRODUCER_NAMESs0<%=i %>" value="<%=CommonUtils.checkNull(tl.getDownProductName())%>"/><a href="#" onclick="selectSupplier(this,2,'<%=CommonUtils.checkNull(tl.getDownPartCode())%>')">选择</a>
						</td>
						<td title="<%=CommonUtils .checkNull(tl.getRemark())%> ">
						<%
						String str = tl.getRemark();
						if(Utility.testString(str)){
							if(str.length()>10){
								str=str.substring(0,10)+"...";
								//out.print(str);
							}
						} %>
						<a href="#" onclick="altInfo('<%=CommonUtils .checkNull(tl.getRemark())%>')" ><%=str %></a>
						</td>
						
						<td >
					 <%=tl.getWrLabourname()%> 
						</td>
						<td style="">
							<script type="text/javascript">
							document.write(getItemValue('<%=tl.getPayType()%>'));
							</script>
						 <input type="hidden" name="PAY_TYPE_PART" id="PAY_TYPE_PART" value="<%=tl.getPayType()%>" />
						</td>
						<td style="">
						 <%
						if("-1".equalsIgnoreCase(tl.getMainPartCode())){
							out.print("无");
						}else{
							out.print(tl.getMainPartCode());
						}
						%> 
						</td>
						<td style="">
						 <%=tl.getCodeDesc()%> 
						</td>
						<td style="">
						 <%=tl.getPartUseName() %> 
						</td>
						<%-- 
						<td >
						<%
							if(tl.getIsReturn()==null || tl.getIsReturn()!=95361001){
								%>
									是
								<%
							}else{
								%>
								否
								<%
							}
						%>
						</td>
						--%>
					</tr>
					<% 
						}
					%>
				</tbody>
			</table>
			<table class="table_edit" id="compensationMoney" style="">
              <th colspan="10"  ><img class="nav" src="../../../img/subNav.gif" />
              	补偿
               <c:forEach items="${compensationMoneyList}" var="compensationMoneyList" varStatus="status">
            <tr>
                <td  nowrap="true">供应商代码：</td>
                <td nowrap="true">
                <input id="supplier_code" name="supplier_code" value="${compensationMoneyList.SUPPLIER_CODE}" readonly="readonly" class="middle_txt" /><a href="#" name="selectSupplierCode"  onclick="selectSupplierCode(this,'${compensationMoneyList.PART_CODE}','${compensationMoneyList.PKID}');">选择</a>
                </td>
                <td  nowrap="true">补偿费申请金额：</td>
                <td><input name="apply_price" id="apply_price" value="${compensationMoneyList.APPLY_PRICE}" type="text" class="middle_txt" readonly> </td>
                <td  nowrap="true">审批金额<span style="color: red;">(可改)</span>：</td>
                <td><input name="pass_price" id="pass_price" value="${compensationMoneyList.PASS_PRICE}" type="text" class="middle_txt" onblur="checkPrice(this,'${compensationMoneyList.PKID}','${compensationMoneyList.PASS_PRICE}','${compensationMoneyList.APPLY_PRICE}');" ></td>
				 <td  nowrap="true">补偿关联主因件：</td>
                <td><input name="part_code_temp" id="part_code_temp" value="${compensationMoneyList.PART_CODE}" type="text" class="middle_txt" readonly></td>
					<td><div align="left"><input disabled="true" type="button" class="normal_btn"  value="删除"  name="button42" onClick="delItemOther(this);"/></div></td>
            </tr>
            </c:forEach>
          </table>
		<table class="table_edit" id="accessories" style="">
              <th colspan="10"  ><img class="nav" src="../../../img/subNav.gif" />
              	辅料
               <c:forEach items="${accessoryDtlList}" var="accessoryDtlList" varStatus="status">
            <tr>
                <td  align="right">辅料代码：</td>
                <td>
                <input id="workHourCodeMap" name="workHourCodeMap"  value="${accessoryDtlList.WORKHOUR_CODE}" readonly="readonly" class="middle_txt" />
                </td>
                <td  align="right">辅料名称：</td>
                <td><input name="workhour_name" id="workhour_name" value="${accessoryDtlList.WORKHOUR_NAME}" type="text" class="middle_txt" readonly="readonly"> </td>
                <td  align="right">辅料金额：</td>
                <td><input name="accessoriesPrice" id="accessoriesPrice" value="${accessoryDtlList.PRICE}" type="text" class="middle_txt"readonly="readonly"></td>
				<td  align="right">辅料主因件：</td>
                <td><input name="code" id="code" value="${accessoryDtlList.MAIN_PART_CODE}" type="text" class="middle_txt" readonly="readonly"></td>
					
				<td><div align="left"><input disabled="true" type="button" class="normal_btn"  value="删除"  name="button42" onClick="delItemOther(this);"/></div></td>
            </tr>
            </c:forEach>
          </table>
			<table id="otherTableId" align="center" cellpadding="0"
				cellspacing="1" class="table_list">
				<th colspan="8" align="left">
					<img class="nav" src="../../../img/subNav.gif" />
					其他项目
				</th>
				<tr align="center" class="table_list_row1">
					<td>
						<div align="center">
							项目代码
						</div>
					</td>
					<td>
						<div align="center">
							项目名称
						</div>
					</td>
					<td>
						<div align="center">
							金额(元)
						</div>
					</td>
					<td>
						备注
					</td>
					<td >
						付费类型
					</td>
				</tr>
				<tbody id="otherTable">
					<%
						for (int i = 0; i < otherLs.size(); i++) {
					%>
					<tr class="table_list_row1" >
						<td>
						
						  <%  if(otherLs.get(i).getItemCode().toString().equals("QT007")){ %> 
						   <%=otherLs.get(i).getItemCode()%> 
						   <input type="hidden"  id="ItemCode" name="ItemCode" value="<%=otherLs.get(i).getItemCode()%>" />
						  <%}else{%>
						   <%=otherLs.get(i).getItemCode()%> 
						  <input type="hidden"  id="ItemCode" name="ItemCode" value="<%=otherLs.get(i).getItemCode()%>" />
						  <%}  %>
						 
						 
						</td>
						<td>
						 <%=CommonUtils.checkNull(otherLs.get(i).getItemDesc())%> 
						</td>
						<td>
						  <%  if(otherLs.get(i).getItemCode().toString().equals("QT007")){ %> 
						  <%=CommonUtils.checkNull(otherLs.get(i).getAmount())%> 
						  <input type="hidden" name="Amount" id="Amount<%=otherLs.get(i).getItemCode()%>" datatype="0,isMoney,30" blurback="true" maxlength="10"  value="<%=CommonUtils.checkNull(otherLs.get(i).getAmount())%>"  />
						  
						  <%}else{%>
						   <input type="text" name="Amount" id="Amount<%=otherLs.get(i).getItemCode()%>" datatype="0,isMoney,30" blurback="true" maxlength="10"  value="<%=CommonUtils.checkNull(otherLs.get(i).getAmount())%>"  />
						  <%}  %>
						 
						 
						</td>
						<td title="<%=otherLs.get(i).getRemark()==null?"":otherLs.get(i).getRemark()%>">
						
						  <input class="supper_txt" type="text" disabled="disabled" readonly="readonly" name="AUDIT_CON" value="<%=otherLs.get(i).getRemark()==null?"":otherLs.get(i).getRemark()%>" id="AUDIT_CON" />
						  
						 
						</td>
						<td>
						 <script type="text/javascript">
								document.write(getItemValue('<%=otherLs.get(i).getPayType()%>'));
							</script>&nbsp;
						</td>
					</tr>
					<%
						}
					%>
				</tbody>
			</table>
			 <table class="table_edit" id="outId" <%  if(tawep.getClaimType().intValue()==Constant.CLA_TYPE_09){out.print("style='display:'");}else{out.print("style='display:none'");} %> >
          	<tr>
              <th colspan="10"  ><img src="../../../img/subNav.gif" alt="" class="nav" />
              外出维修</th>
            </tr>
            <tr>
          	<td align="right">
          	开始时间：
          	</td>
          	<td nowrap="nowrap">
          	 <%=CommonUtils.checkNull(Utility.handleDate(tawep.getStartTime())) %> 
			</td>
          	<td align="right">
          	结束时间：
          	</td>
          	<td align="left">
          	 <%=CommonUtils.checkNull(Utility.handleDate(tawep.getEndTime())) %> 
            
          	</td>
          	<td align="right">
          	派车车牌号：
          	</td>
          	<td align="left">
          	 <%=CommonUtils.checkNull(tawep.getOutLicenseno()) %> 
          	</td>
          	</tr>
          	<tr>
          	<td align="right">
          	外出人：
          	</td>
          	<td align="left">
           <%=CommonUtils.checkNull(tawep.getOutPerson())%> 
          	</td>
          	<td align="right">
          	出差目的地：
          	</td>
          	<td align="left">
          	 <%=CommonUtils.checkNull(tawep.getOutSite()) %> 
          	</td>
          	
          	<td align="right">
          	单程里程：
          	</td>
          	<td align="left"> 
          	 <%=tawep.getOutMileages()%> 
          	</td>
          	</tr>
          	<tr>
          	<td align="right">外出主因件:</td>
          	<td colspan="5" align="left"><%if(otherLs!=null&&otherLs.size()>0){out.print(otherLs.get(0).getMainPartCode());} %></td>
          	</tr>
          </table>  
			<TABLE class="table_edit">
						<tr>
              <th colspan="10"  ><img src="../../../img/subNav.gif" alt="" class="nav" />
              申请费用</th>
            </tr>
            </TABLE>
			<table class="table_edit">
            <tr>
              <td class="table_edit_4Col_label_7Letter" nowrap="nowrap" >基本工时：</td>
              <td  id="BASE_LABOUR"><%=tawep.getLabourHours() %></td>
              <td class="table_edit_4Col_label_7Letter"  nowrap="nowrap" style="display:none">基本工时金额：</td>
              <td  nowrap="nowrap" id="BASE_LABOUR_AMOUNT" style="display:none"><%=CommonUtils.formatPrice(tawep.getStdLabourAmount()) %></td>
              <td  class="table_edit_4Col_label_7Letter" nowrap="nowrap"  style="display:none" >附加工时：</td>
              <td id="ADD_LABOUR"  style="display:none" ><%=tawep.getReinforceHours() %></td>
              <td class="table_edit_4Col_label_7Letter"  nowrap="nowrap"  style="display:none">附加工时金额：</td>
              <td id="ADD_LABOUR_AMOUNT"  style="display:none" ><%=CommonUtils.formatPrice(tawep.getReinLabourAmount()) %></td>
              <td class="table_edit_4Col_label_7Letter"  nowrap="nowrap" >配件金额：</td>
              <td id="ALL_PART_AMOUNT"><%=CommonUtils.formatPrice(tawep.getPartAmount()) %></td>
              <td class="table_edit_4Col_label_7Letter"  nowrap="nowrap" >工时金额：</td>
              <td id="ALL_LABOUR_AMOUNT"><%=CommonUtils.formatPrice(tawep.getLabourAmount()) %></td>
              <td class="table_edit_4Col_label_7Letter"  nowrap="nowrap" >其它费用金额：</td>
              <td id="OTHER_AMOUNT"><%=CommonUtils.formatPrice(tawep.getNetitemAmount()) %></td>
            </tr>
            <tr>
              
              <td class="table_edit_4Col_label_7Letter"  nowrap="nowrap" >保养金额：</td>
              <td id="GAME_AMOUNT"><%=CommonUtils.formatPrice(tawep.getFreeMPrice()) %></td>
              <td class="table_edit_4Col_label_7Letter"  nowrap="nowrap" >服务活动金额：</td>
              <td id="ACTIVITY_AMOUNT"><%=CommonUtils.formatPrice(tawep.getCampaignFee()) %></td>
              <td class="table_edit_4Col_label_7Letter"  nowrap="nowrap" >索赔申请金额：</td>
              <td id="APPLY_AMOUNT"><%=CommonUtils.formatPrice(tawep.getRepairTotal()) %></td>
              <td class="table_edit_4Col_label_7Letter"  nowrap="nowrap" >总金额(含税)：</td>
              <td id="ALL_AMOUNT"><script type="text/javascript">document.write(formatCurrency('<%=CommonUtils.formatPrice(tawep.getGrossCredit()) %>'))</script></td>
            </tr>
            <tr>
              <td class="table_edit_4Col_label_7Letter"  nowrap="nowrap"  style="display:none" >税额：</td>
              <td id="TAX"  style="display:none"><%=CommonUtils.formatPrice(tawep.getTaxSum()) %></td>
              
            </tr>
            <tr >
					<td class="table_edit_4Col_label_7Letter" nowrap="nowrap"  > 活动配件打折： </td>
					<td id="ACTIVITY_AMOUNT_PART"  > <%=CommonUtils.formatPrice(tawep.getPartDown()) %></td>
					<td class="table_edit_4Col_label_7Letter"  nowrap="nowrap" > 活动工时打折： </td> 
					<td id="ACTIVITY_AMOUNT_LABOUR"   ><%=CommonUtils.formatPrice(tawep.getLabourDown()) %> </td>
					</tr>
          </table>   
            <!-- 添加附件 -->
			<table class="table_info" border="0" id="file">
				<input type="hidden" id="fjids" name="fjids"/>
			    <tr colspan="8">
			        <th>
					<img class="nav" src="../../../img/subNav.gif" />
					&nbsp;附件列表：
					</th>
					<th><span align="left"><input type="button" disabled="disabled" class="normal_btn"  onclick="showUpload('<%=contextPath%>')" value ='添加附件'/></span>
					</th>
				</tr>
				<tr>
      				<td width="100%" colspan="2"><jsp:include page="${contextPath}/uploadDiv.jsp" /></td>
    			</tr>
    			<%for(int i=0;i<attachLs.size();i++) { %>
    			<!--  <tr>
    			<td><a  target='_blank' href='<%=CommonUtils.checkNull(attachLs.get(i).getFileurl())%>'/><%=CommonUtils.checkNull(attachLs.get(i).getFilename()) %></a><input type='hidden' value='<%=CommonUtils.checkNull(attachLs.get(i).getFjid()) %>' name='uploadFileId' /> <input type='hidden' value='<%=CommonUtils.checkNull(attachLs.get(i).getFilename()) %>' name='uploadFileName' /></td>
    			<td><input disabled type=button onclick='delUploadFile(this)' class='normal_btn' value='删 除' /></td>
    			</tr>-->
    			<script type="text/javascript">
    			//	addUploadRowByDb('<%=CommonUtils.checkNull(attachLs.get(i).getFilename()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFjid()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFileurl())%>');
    			addUploadRowByDbView('<%=CommonUtils.checkNull(attachLs.get(i).getFilename()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFjid()) %>','<%=CommonUtils.checkNull(attachLs.get(i).getFileurl())%>','<%=CommonUtils.checkNull(attachLs.get(i).getFjid()) %>','<%=request.getContextPath()%>/util/FileDownLoad/FileDownLoadQuery.do?fjid=' );
    			</script>
    			<%} %>
 			</table>
 			<table class="table_edit">
			<tr>
				<th colspan="10" align="left"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" />
				&nbsp;<a href="#" >索赔单审核明细</a>
				</th>
		 	</tr>
		 </table>
		 <table class="table_list" style="border-bottom: 1px solid #DAE0EE;" id="authTable">
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
					审核备注
				</th>
				<tr>
					<td colspan="11" class="tbwhite">
						<textarea name='ADUIT_REMARK' id='AUDIT_REMARK'	datatype="1,is_textarea,100" maxlength="100" rows='3' cols='28'></textarea>
					</td>
				</tr>
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
			 </td>
				</tr>
			</table>
		</form>
		<script type="text/javascript">
		function submit_to(type){
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
		//zyw 2014-9-11
		function afterCheckNotone(json){
			if(json.succ=="1"){
				var type=json.type;
				var id = $('claimId').value;
				var url= "<%=contextPath%>/claim/dealerClaimMng/DealerClaimReport/reportClaim.json?ID="+id+"&type="+type;
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
				MyAlert(str);
				window.location.href = "<%=contextPath%>/claim/application/ClaimManualAuditing/claimManualAuditingInit.do";
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
		tr.childNodes[5].childNodes[0].value=v2+"--"+v3;
		var id=tr.childNodes[4].childNodes[0].value;	
			$('btn1').disabled=true;
			$('btn2').disabled=true;
			$('btn3').disabled=true;
			$('btn4').disabled=true;
		var url= "<%=contextPath%>/claim/application/ClaimManualAuditing/modifyMalCode.json?ID="+id+"&malId="+v1;
			makeNomalFormCall(url,malAfter,'fm','');
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
		
		function selectSupplier(obj,type,partCode) {
			typeTemp=0;
			if (type==1) {
				typeTemp=1;
			} else if(type==2) {
				typeTemp=2;
			} 
			myobj = obj.parentNode;
			OpenHtmlWindow('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/selectSupplierForward.do?type='+type+'&partCode='+partCode,800,500);
		}
		
		function setSupplier(id,code,name,partCode) {
			myobj.childNodes[0].value=code;
			myobj.childNodes[2].value=name;
			if(typeTemp==1){
				myobj.nextSibling.childNodes[0].value=code;
				myobj.nextSibling.childNodes[2].value=name;
			}
		}
	 function selectSupplierCode(obj,partcode,id){
	 		myobj="";
	 		myobj = obj.parentNode;
	 		parent.OpenHtmlWindow('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/selectSupplierCodeForward.do?partcode='+partcode+"&id="+id,800,500);
	 	}
	 function setMainSupplierCode(v1,v2,id){
    	 var flag =true;
			var tr = this.getRowObj(myobj);
			var supplierCode =document.getElementsByName('supplier_code');
			
				for(var i=0;i<supplierCode.length;i++){
					if(supplierCode[i].value==v1){
						MyAlert("提示：不能添加相同的供应商!");
						flag = false;
						break;;
						}
					}
			if(flag){
				tr.childNodes[1].childNodes[0].value=v1;
				 var url='<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/changValSupplierCode.json?id='+id+'&supplierCode='+v1;
				 makeCall(url,backchangVal,null);
			}
     }
	 function backchangVal(json){
		 if(json.succ=="1"){
			 MyAlert("提示：修改成功！");
		 }else{
			 MyAlert("提示：修改失败！");
		 }
	 }
   //============zyw 2014-8-4
   //验证并赋值的操作
    function checkPrice(obj,id,price1,apply_price){
	 var price=obj.value;
   	 if(price!="" ){
   		 if(price1==price){
   			 return;
   		 }
   		 if(price>apply_price){
   			 MyAlert("提示：请输入小于申请金额的费用！返回原数据");
   			 obj.value=price1;
   			 return;
   		 }
   		 var patten = /^\d+\.?\d{0,2}$/;
   		 if(!patten.test(price)){
   		 	MyAlert("提示：请输入正小数并最多保留两位并且要小于等于申请金额！填错返回申请金额！");
   		 	obj.value=price1;
   		 	return;
   		 }else{
   		 	var ro_no=$('RO_NO').value;
   		 	var pass_prices=document.getElementsByName("pass_price");
   		 	var countPrice=0.0;
   		 	for(var i=0;i<pass_prices.length;i++){
   		 		countPrice+=pass_prices[i].value;
   		 	}
   			var url='<%=contextPath%>/repairOrder/RoMaintainMain/checkChangePrice.json?ro_no='+ro_no+"&id="+id+"&pass_price="+price+"&countPrice="+countPrice;
   			makeCall(url,updateBack,null);
   		 }
   	 }
   }
	 function updateBack(josn){
		 if(josn.pass_price=="1"){
			 MyAlert("提示：修改成功！");
		 }else{
			 MyAlert("提示：修改失败！");
		 }
	 }
</script>
	</BODY>
</html>
