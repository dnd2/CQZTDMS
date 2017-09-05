<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.util.LinkedList"%>
<%@page import="com.infodms.dms.po.TtAsWrApplicationExtPO"%>
<%@page import="com.infodms.dms.po.TtAsWrNetitemExtPO"%>
<%@page import="com.infodms.dms.po.FsFileuploadPO"%>
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
			List<TtAsWrNetitemExtPO> otherLs = (LinkedList<TtAsWrNetitemExtPO>) request.getAttribute("otherLs");
			List<FsFileuploadPO> attachLs = (LinkedList<FsFileuploadPO>)request.getAttribute("attachLs");
			String id = (String) request.getAttribute("ID");
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
	if($('claimType').value==<%=Constant.CLA_TYPE_02 %>){
		$('otherTableId').style.display="none";
	}else{
	$('otherTableId').style.display="";
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
			<input name="ID" id ="ID" type="hidden" value="<%=tawep.getId() %>"/>
			<input name="CLAIM_TYPE" id="CLAIM_TYPE" type="hidden"  value="<%=CommonUtils.checkNull(tawep.getClaimType())%>"/>
			<input type="hidden" name="claimType" value="<%=tawep.getClaimType()%>"/>
			<input type="hidden" name="VIN" id="VIN" value="<%=CommonUtils.checkNull(tawep.getVin())%>" />
			<input type="hidden" name="RO_NO" id="RO_NO" value="<%=CommonUtils.checkNull(tawep.getRoNo())%>" />
			<input type='hidden' name='LINE_NO' id='LINE_NO' value='<%=CommonUtils.checkNull(tawep.getLineNo())%>' />
			<input type="hidden" name="GUARANTEE_DATE" id="GUARANTEE_DATE"  class="short_txt"value='<%=CommonUtils.checkNull(Utility.handleDate(tawep
							.getGuaranteeDate()))%>' />
			<input type='hidden' name='SERVE_ADVISOR' id='SERVE_ADVISOR' value='<%=CommonUtils.checkNull(tawep.getServeAdvisor())%>' />		
			<input type="hidden" id="IS_FIX" name="IS_FIX" value="<%=tawep.getIsFixing() %>" />
			<input type="hidden" id="CAMPAIGN_FEE" name="CAMPAIGN_FEE" value="<%=tawep.getCampaignFee() %>" />
			<input type="hidden" id="CAMPAIGN_CODE" name="CAMPAIGN_CODE" value="<%=CommonUtils.checkNull(tawep.getCampaignCode())%>" />
			<input type="hidden" name="FREE_M_AMOUNT" id="FREE_M_AMOUNT"  value="<%=tawep.getFreeMAmount() %>" />
			<input type="hidden" name="FREE_M_PRICE" value="<%=tawep.getFreeMPrice() %>" id="FREE_M_PRICE" />
			
			<input type="hidden" name="attIds" id="attIds" value=""/><!-- 删除附件隐藏 -->
			<input type="hidden" name="BRAND_NAME0" id="BRAND_NAME0" value="<%=CommonUtils.checkNull(tawep.getBrandName())%>"/>
			<input type="hidden" name="SERIES_NAME0" id="SERIES_NAME0" value="<%=CommonUtils.checkNull(tawep.getSeriesName())%>"/>
			<input type="hidden" name="MODEL_NAME0" id="MODEL_NAME0" value="<%=CommonUtils.checkNull(tawep.getModelName())%>"/>
			<input type="hidden" name="ENGINE_NO" id="ENGINE_NO" value="<%=CommonUtils.checkNull(tawep.getEngineNo())%>"/>
			<input type="hidden" name="REARAXLE_NO0" id="REARAXLE_NO0" value="<%=CommonUtils.checkNull(tawep.getGearboxNo())%>"/>
			<input type="hidden" name="GEARBOX_NO0" id="GEARBOX_NO0" value="<%=CommonUtils.checkNull(tawep.getGearboxNo())%>"/>
			<input type="hidden" name="ACTIVITY_AMOUNT_PARTS" id="ACTIVITY_AMOUNT_PARTS" value="<%=CommonUtils.formatPrice(tawep.getPartDown()) %>" />
			<input type="hidden" name="ACTIVITY_AMOUNT_LABOURS" id="ACTIVITY_AMOUNT_LABOURS" value="<%=CommonUtils.formatPrice(tawep.getLabourDown()) %>" />
				
			<input type="hidden" name="YIELDLY" id="YIELDLY" value="<%=CommonUtils.checkNull(tawep.getYieldly()) %>" />
			<input type="hidden" value="<%=CommonUtils.checkNull(tawep.getLicenseNo())%>" name="LICENSE_NO" id="LICENSE_NO" />
			
			<input type="hidden" name="TRANSFER_NO0" id="TRANSFER_NO0" value="<%=CommonUtils.checkNull(tawep.getTransferNo()) %>"/>
			<input type="hidden" name="BRAND_CODE0" id="BRAND_CODE0" value="<%=tawep.getBrandCode() %>"/>
			<input type="hidden" name="SERIES_CODE0" id="SERIES_CODE0" value="<%=tawep.getSeriesCode() %>"/>
			<input type="hidden" name="MODEL_CODE0" id="MODEL_CODE0" value="<%=tawep.getModelCode() %>"/>
			
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
							<input type="hidden" name="RO_STARTDATE" id="RO_STARTDATE" class="short_txt"style="width: 100px"
							value='<%=CommonUtils.checkNull(Utility.handleDate1(tawep
							.getRoStartdate()))%>' readonly />
					</td>
					<td class="table_edit_2Col_label_7Letter">
						工单结算时间：
					</td>
					<td>
						 <%=CommonUtils.checkNull(Utility.handleDate1(tawep
							.getRoEnddate()))%> 
							<input type="hidden" name="RO_ENDDATE" id="RO_ENDDATE" class="short_txt" readonly style="width: 100px"
							value='<%=CommonUtils.checkNull(Utility.handleDate1(tawep
							.getRoEnddate()))%>' />
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
						<% if(tawep.getFreeMAmount()>0){
						
						out.print(tawep.getFreeMAmount());
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
						<input type="hidden" name='SERVE_ADVISOR' value='<%=CommonUtils.checkNull(tawep.getServeAdvisor())%>'/><%=CommonUtils.checkNull(tawep.getServeAdvisor())%>
					</td>
					<td class="table_edit_2Col_label_7Letter">
						索赔主管电话：
					</td>
					<td ><%=CommonUtils.checkNull(tawep.getClaimDirectorTelphone())%></td>
					<td class="table_edit_2Col_label_7Letter">
						索赔单号：
					</td>
					<td ><%=CommonUtils.checkNull(tawep.getClaimNo())%></td>
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
			<table id="otherTableId" align="center" cellpadding="0"
				cellspacing="1" class="table_list">
				<tr align="center" class="table_list_row1">
					<td>
							项目代码
					</td>
					<td>
							项目名称
					</td>
					<td>
							金额(元)
					</td>
					<td>
						备注
					</td>
				</tr>
				<tbody id="otherTable">
					<%
						for (int i = 0; i < otherLs.size(); i++) {%> 
					<tr class="table_list_row1" >
						<td>
						 
						   <%=otherLs.get(i).getItemCode()%> 
						   <input type="hidden"  id="ITEM_CODE" name="ITEM_CODE" value="<%=otherLs.get(i).getItemCode()%>" />
						 
						</td>
						<td>
						 <%=CommonUtils.checkNull(otherLs.get(i).getItemDesc())%> 
						 <input type="hidden"  id="ITEM_NAME" name="ITEM_NAME" value=" <%=CommonUtils.checkNull(otherLs.get(i).getItemDesc())%> " />
						</td>
						<td>
						  <input type="text"  class="little_txt" name="ITEM_AMOUNT" id="ITEM_AMOUNT" 

datatype="0,isMoney,30" readonly="readonly" maxlength="10"  value="<%=CommonUtils.checkNull(otherLs.get(i).getAmount())%>"  />
						</td>
						<td title="<%=otherLs.get(i).getRemark()==null?"":otherLs.get(i).getRemark()%>">
						<input type="text" name="ITEM_REMARK"  id="ITEM_REMARK<%=otherLs.get(i).getItemCode()%>" datatype="0,is_digit_letter_cn,100" value="<%=otherLs.get(i).getRemark()%>" class="middle_txt"  />
						</td>
					</tr>
					<%
						}
					%>
				</tbody>
			</table>
		<table border="0" align="center" cellpadding="1" cellspacing="1"
				class="table_edit">
				<th colspan="6">
					<img class="nav" src="../../../img/subNav.gif" />
					申请费用：<span style="color:red">总金额(含税)：<%=CommonUtils.checkNull(tawep.getGrossCredit())%>元整</span>
				</th>
			</table>
            <!-- 添加附件 -->
			<table class="table_info" border="0" id="file">
				<input type="hidden" id="fjids" name="fjids"/>
			    <tr colspan="8">
			        <th>
					<img class="nav" src="../../../img/subNav.gif" />
					&nbsp;附件列表：
					</th>
					<th><span align="left"><input type="button"  class="normal_btn"  onclick="showUpload('<%=contextPath%>')" value ='添加附件'/></span>
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
			<table border="0" cellspacing="0" cellpadding="0" class="table_edit">
				<tr>
					<td  colspan="5" align="center">
					<input class="normal_btn" type="button" value="维修历史" name="historyBtn"
		 onclick="openWindowDialog(1);"/>
		
		<input class="normal_btn" type="button" value="授权历史" name="historyBtn"
		 onclick="openWindowDialog(2);"/>
		
		<input class="normal_btn" type="button" value="保养历史" name="historyBtn" 
		onclick="openWindowDialog(3);"/>
		
		<input type="button" onClick="save();" id="btn1" class="normal_btn"  value="保存"  />
		
		 <input type="button" onClick="history.back();" id="btn3" class="normal_btn"   value="返回" />
			 </td>
		 <td>
		 	<input class="long_btn" type="button" id="three_package_set_btn" value="三包判定" onclick="threePackageSet();"/>
			 </td>
				</tr>
			</table>
		</form>
<script type="text/javascript">
function save(){
	 var claimRemark = document.getElementsByName('ITEM_REMARK');
			if($('CLAIM_TYPE').value==<%=Constant.CLA_TYPE_11%>  ){
					for(var k=0;k<claimRemark.length;k++){
					if(claimRemark[k].value=="" || claimRemark[k].value==null){
					MyAlert("PDI索赔必须填写项目备注!");
					return;
						}
						}
				}
			 MyConfirm("确认保存?",audit,[]);
		}
		function audit(){
		    $('btn1').disabled=true;
			var fm = document.getElementById('fm');
			fm.action='<%=request.getContextPath()%>/claim/dealerClaimMng/ClaimBillMaintainMain/applicationUpdate.do';
			fm.submit();
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

	function alInfo(str){
		MyAlert(str);
	}
	function hiddenTable(str){
	if($(str).style.display=='none'){
		$(str).style.display='';
	}else{
		$(str).style.display='none';
	 }
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
</script>
	</BODY>
</html>
