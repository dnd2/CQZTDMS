<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.Map"%>
<%
	String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>服务商详细信息</title>
<script type="text/javascript">var dealerLevel=<%=Constant.DEALER_LEVEL_01%>;
// function doInit()
// {  
//    	var dl=<c:out value="${map.DEALER_LEVEL}"/>;
//    	MyAlert(dl);
// 	if(dealerLevel==dl)
// 	{
// 		document.getElementById("sJDealerCode").disabled="true";
// 		document.getElementById("dealerbu").disabled="true";
// 		document.getElementById("orgCode").disabled="";
// 		document.getElementById("orgbu").disabled="";
		
// 	}else
// 	{
// 		document.getElementById("sJDealerCode").disabled="";
// 		document.getElementById("dealerbu").disabled="";
// 		document.getElementById("orgCode").disabled="true";
// 		document.getElementById("orgbu").disabled="true";		
// 	}
// 	genLocSel('txt1','txt2','txt3','<c:out value="${map.PROVINCE_ID}"/>','<c:out value="${map.CITY_ID}"/>','<c:out value="${map.COUNTIES}"/>'); // 加载省份城市和县
// 	document.getElementById("priceId").value="${map.PRICE_ID}";
// }


// 检验当前修改服务商下是否存在下级服务商，若存在，则不能执行当前操作，并在前台提示错误
function chkDealer(flag) {
	var url = '<%=contextPath%>/sysmng/dealer/DealerInfo/chkDealer.json' ;
	var iDealerId = document.getElementById('DEALER_ID').value ;
	
	makeCall(url, showErr, {dealerId: iDealerId, flag: flag}) ;
}
// 返回检验服务商错误信息
function showErr(json) {
	var dlrLel = document.getElementById('DEALERLEVEL') ;
	var dlrSta = document.getElementById('DEALERSTATUS') ;
	
	if(json.errInfo == 1) {
		if (json.flag == 0) {
			retDlrLel() ;
		}
		if (json.flag == 1) {
			retDlrSta() ;
		}
		MyAlert('服务商信息有误，请确认服务商信息的完整！') ;
	}else if(json.errInfo == 2) {
		MyAlert('该服务商存在下级服务商，注意修改下级服务商信息！') ;
		changeDealerlevel(dlrLel.value) ;
	} else {
		if (json.flag == 0) {
			changeDealerlevel(dlrLel.value) ;
		}
	}
}

// 若有检验服务商有错误信息，则返回服务商原有级别
function retDlrLel() {
	var dlrLel = document.getElementById('DEALERLEVEL') ;

	if (dlrLel.value != ${map.DEALER_LEVEL}) {
		dlrLel.value = ${map.DEALER_LEVEL} ;
	}
}

// 若有检验服务商有错误信息，则返回原有服务商状态
function retDlrSta() {
	var dlrSta = document.getElementById('DEALERSTATUS') ;

	if (dlrSta.value != ${map.STATUS}) {
		dlrSta.value = ${map.STATUS} ;
	}
}

// 对选择上级服务商是否当前修改服务商验证
function chkDlr() {
	var dlrA = document.getElementById('DEALER_ID') ;
	var dlrB = document.getElementById('sJDealerId') ;
	var dlrCode = document.getElementById('sJDealerCode') ;

	if(dlrA.value == dlrB.value) {
		dlrB.value = "" ;
		dlrCode.value = "" ;

		MyAlert("选择上级服务商不能为当前服务商！") ;
	}
}

//验证输入服务商代码是否已存在
function chkDLRA(dlrCode) {
	dlrId = ${map.DEALER_ID} ;
	url = "<%=contextPath%>/sysmng/dealer/DealerInfo/chkDlr.json" ;
	makeCall(url, printErr, {dlrCode : dlrCode, dlrId : dlrId}) ;
}

function printErr(json) {
	if(json.errInfo == 1) {
		setText("DEALER_CODE") ;
		MyAlert("输入服务商代码已存在，请重新输入") ;
	}
}

function setText(obj,setValue) {
    if(!setValue) {
    	setValue = "" ;
    }
    
	document.getElementById(obj).value = setValue ;
} 
--></script>
</head>
<body>
	<div class="navigation">
		<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 配件管理 &gt;
		配件基础信息维护 &gt;服务商详细信息
	</div>
	<form method="post" name="fm">
		<%--  <input id="COMPANY_ID" name="COMPANY_ID" type="hidden" value="<c:out value="${map.COMPANY_ID}"/>"/> --%>
	  <input id="DEALER_ID" name="DEALER_ID" type="hidden" value="<c:out value="${map.DEALER_ID}"/>"/>
	  <input id="DEALER_CODE" name="DEALER_CODE" type="hidden" value="<c:out value="${map.DEALER_CODE}"/>"/>
	  <input id="DEALER_NAME" name="DEALER_NAME" type="hidden" value="<c:out value="${map.DEALER_NAME}"/>"/>
		<%--  <input type="hidden"  name="sJDealerId"  value="<c:out value="${map.SJDEALERID}"/>"  id="sJDealerId" onpropertychange="chkDlr();" /> --%>
		<%--  <input type="hidden"  name="orgId"  value="<c:out value="${map.ORG_ID}"/>"  id="orgId" /> --%>
		<table width=100% border="0" align="center" cellpadding="1"
			cellspacing="1" class="table_query">
			<tr>
				<TH colSpan=4 align=left><IMG class=nav
					src="<%=contextPath%>/img/subNav.gif"> 服务商基础信息</TH>
			<tr>
			<tr>
				<td class="table_query_2Col_label_6Letter">服务商代码：</td>

				<td><input type='text' class="middle_txt" name="DEALER_CODE"
					id="DEALER_CODE" datatype="0,is_name,20"
					value="<c:out value="${map.DEALER_CODE}"/>" maxlength="20"
					onchange="chkDLRA(this.value);" disabled=true /></td>
				<td class="table_query_2Col_label_6Letter">服务商名称：</td>
				<td><input type='text' class="middle_txt" name="DEALER_NAME"
					id="DEALER_NAME" datatype="0,is_null,150"
					value="<c:out value="${map.DEALER_NAME}"/>" maxlength="150"
					disabled=true /></td>
			</tr>
			<tr>
				<td class="table_query_2Col_label_6Letter">服务商简称：</td>
				<td><input type='text' class="middle_txt" name="SHORT_NAME"
					id="SHORT_NAME" datatype="0,is_null,75"
					value="<c:out value="${map.DEALER_SHORTNAME}"/>" maxlength="75"
					disabled=true /></td>
				<td class="table_query_2Col_label_6Letter">系统开通时间：</td>
				<td><label> ${map.CREATE_DATE} </label></td>
				<!-- <td class="table_query_2Col_label_6Letter">经销商等级：</td> -->

				<label style="display: block;"> <script
						type="text/javascript">
					genSelBoxExp("DEALERLEVEL",<%=Constant.DEALER_LEVEL%>,"<c:out value="${map.DEALER_LEVEL}"/>",'',"short_sel","onchange='chkDealer(0); '","false",'');
				</script> </label style="display: block;">
				<label> <script type="text/javascript">
					genSelBoxExp("DEALERSTATUS",<%=Constant.STATUS%>,"<c:out value="${map.STATUS}"/>",true,"short_sel","onchange='chkDealer(1); '","false",'');
				</script> </label>
			</tr>
			<tr>
				<td class="table_query_2Col_label_6Letter">服务商类型：</td>
				<td><label> <script type="text/javascript">
					genSelBoxExp("PDEALERTYPE",<%=Constant.PART_SALE_PRICE_DEALER_TYPE%>,"<c:out value="${map.PDEALER_TYPE}"/>",true,"short_sel","onchange='pdealerType(); '","false",'');
				</script> </label></td>
				<td class="table_query_2Col_label_6Letter">服务商公司：</td>
				<td><input class="middle_txt" id="COMPANY_NAME"
					style="cursor: pointer;" name="COMPANY_NAME" type="text"
					value="<c:out value="${map.COMPANY_SHORTNAME}"/>"
					disabled="disabled" /></td>
			</tr>
			<tr>
				<td class="table_query_2Col_label_6Letter">服务商状态：</td>
				<td><label> <script type="text/javascript">
					genSelBoxExp("DEALERSTATUS",<%=Constant.STATUS%>,"<c:out value="${map.STATUS}"/>",true,"short_sel","onchange='chkDealer(1); ' disabled=true ","false",'');
				</script> </label></td>
				<td class="table_query_2Col_label_6Letter">省份：</td>
				<td><select class="min_sel" id="txt1" name="province"
					onchange="_genCity(this,'txt2')" disabled=true></select></td>
			</tr>
			<tr>
				<td class="table_query_2Col_label_6Letter">联系人：</td>
				<td><input type="text" class="middle_txt" name="linkMan"
					id="linkMan" datatype="1,is_name,10"
					value="<c:out value="${map.LINK_MAN}"/>" maxlength="10"
					disabled=true />
				</td>
				<td class="table_query_2Col_label_6Letter">电话：</td>
				<td><input type="text" class="middle_txt" name="phone"
					id="phone" datatype="1,is_null,100"
					value="<c:out value="${map.PHONE}"/>" maxlength="25" disabled=true />
				</td>
			</tr>
			<tr>
				<td class="table_query_2Col_label_6Letter">传真：</td>
				<td><input type="text" class="middle_txt" name="faxNo"
					id="faxNo" value="<c:out value="${map.FAX_NO}"/>"
					datatype="1,is_null,50" maxlength="25" disabled=true />
				</td>
				<td class="table_query_2Col_label_6Letter">Email：</td>
				<td><input type="text" class="middle_txt" name="email"
					id="email" datatype="1,is_email,100"
					value="<c:out value="${map.EMAIL}"/>" maxlength="100"
					/ disabled=true>
				</td>
			</tr>
			<tr>
				<td class="table_query_2Col_label_6Letter">详细地址：</td>
				<td><textarea name="address" id="address" cols="40" rows="2"
						datatype="1,is_textarea,50" disabled=true>
						<c:out value="${map.ADDRESS}" />
					</textarea>
				</td>
				<td class="table_query_2Col_label_6Letter">备注：</td>
				<td><textarea name="remark" id="remark" cols="40" rows="2"
						datatype="1,is_textarea,1000" disabled=true>
						<c:out value="${map.REMARK}" />
					</textarea>
				</td>
			</tr>
			<TR>
				<TH colSpan=4 align=left><IMG class=nav
					src="<%=contextPath%>/img/subNav.gif"> 开票信息</TH>
			<tr>
			<tr>
				<td class="table_query_3Col_label_6Letter" nowrap="nowrap">开票名称：</td>
				<td class="table_query_4Col_input" nowrap="nowrap"><input
					type="text" class="middle_txt" id="erpCode" name="erpCode"
					value="${invoiceListMap.TAX_NAME}" /></td>
				<td class="table_query_3Col_label_6Letter" nowrap="nowrap">税号：</td>
				<td class="table_query_4Col_input" nowrap="nowrap"><input
					type="text" class="middle_txt" id="taxesNo" name="taxesNo"
					value="${invoiceListMap.TAX_NO }" datatype="0,is_textarea,30"/>
				</td>
			</tr>
			<tr>
				<td class="table_query_3Col_label_6Letter" nowrap="nowrap">开户行：</td>
				<td class="table_query_4Col_input" nowrap="nowrap"><input
					type="text" class="middle_txt" id="BANK" name="BANK"
					value="${invoiceListMap.BANK}" /></td>
				<td class="table_query_3Col_label_6Letter" nowrap="nowrap">账号：</td>
				<td class="table_query_4Col_input" nowrap="nowrap"><input
					type="text" class="middle_txt" id="INVOICE_ACCOUNT"
					name="INVOICE_ACCOUNT" value="${invoiceListMap.ACCOUNT}" />
				</td>
			</tr>
			<tr>
				<td class="table_query_3Col_label_6Letter" nowrap="nowrap">地址：</td>
				<td class="table_query_4Col_input" nowrap="nowrap"><input  style="width:500px;"
					type="text" class="middle_txt" id="INVOICE_ADD" name="INVOICE_ADD"
					value="${invoiceListMap.ADDR}" lang="500" /></td>
				<td class="table_query_3Col_label_6Letter" nowrap="nowrap">电话：</td>
				<td class="table_query_4Col_input" nowrap="nowrap"><input
					type="text" class="middle_txt" id="INVOICE_PHONE"
					name="INVOICE_PHONE" value="${invoiceListMap.TEL}" />
				</td>
			</tr>
		</table>
		<table class=table_query>
			<tr>
				<td><input type="button" value="修改" name="saveBtn"
					class="normal_btn" onclick="saveDealerInfo();" /> <input
					type="button" value="返回" name="cancelBtn" class="normal_btn"
					onclick="goBack();" /></td>
			</tr>
		</table>

		
		
		<table width=100% border="0" align="center" cellpadding="1"
			cellspacing="1" class="table_query" id="addressTable">
		</table>
		<br />
		<table width=100% border="0" align="center" cellpadding="1"
			cellspacing="1" class="table_query" id="businessTable">
			<tr>
				<th colspan="7"><img src="<%=contextPath%>/img/nav.gif" />服务商地址列表
				<input type="button" value="添加服务商地址" name="saveBtn" class="long_btn" onclick="addAddress();"/>
				</th>
			</tr>
			<tr>
				<th>地址名称</th>
				<th>联系人</th>
				<th>电话</th>

				<th>状态</th>
				<th>操作</th>

			</tr>
			<c:forEach items="${addressList}" var="al">
				<c:if test="${al.STATUS==10011001}">
					<tr class="table_list_row1">
						<td><c:out value="${al.ADDR}" />
						</td>
						<td><c:out value="${al.LINKMAN}" />
						</td>
						<td><c:out value="${al.TEL}" />
						</td>

						<td><script>document.write(getItemValue(${al.STATUS }));</script>
						</td>
						 <td><a href="#" onClick="modifyAdd('<c:out value="${al.ADDR_ID}"/>')" >修改</a></td>

					</tr>
				</c:if>
				<c:if test="${al.STATUS==10011002}">
					<tr class="table_list_row1">
						<td><FONT color="red"><c:out value="${al.ADDR}" />
						</FONT>
						</td>
						<td><FONT color="red"><c:out value="${al.LINKMAN}" />
						</FONT>
						</td>
						<td><FONT color="red"><c:out value="${al.TEL}" />
						</FONT>
						</td>

						<td><script>document.write(getItemValue(${al.STATUS }));</script>
						</td>
						 <td><a href="#" onClick="modifyAdd('<c:out value="${al.ADDR_ID}"/>')" >修改</a></td>
					</tr>
				</c:if>
			</c:forEach>
		</table>
	</form>

	<script type="text/javascript">

function changeDealerlevel(value)
{
	if(dealerLevel==value)
	{
		document.getElementById("sJDealerCode").disabled="true";
		document.getElementById("dealerbu").disabled="true";
		document.getElementById("orgCode").disabled="";
		document.getElementById("orgbu").disabled="";
		document.getElementById("sJDealerCode").value="";
		document.getElementById("sJDealerId").value="";
		
	}else
	{
		document.getElementById("sJDealerCode").disabled="";
		document.getElementById("dealerbu").disabled="";
		document.getElementById("orgCode").disabled="true";
		document.getElementById("orgbu").disabled="true";
		document.getElementById("orgCode").value="";
		document.getElementById("orgId").value="";		
	}
}

function go_Price(){

		if(confirm("确认同步服务商资金账户,同步完需等待5分钟？"))
		{
			fm.action = '<%=contextPath%>/sysmng/dealer/DealerInfo/modifyDealerPrice.do';
		 	fm.submit();
		}
}

//修改服务商信息
function saveDealerInfo()
{   
 	var PDEALERTYPE=document.getElementById("PDEALERTYPE").value;
 	
 	var erpCode=document.getElementById("erpCode").value;
 	var taxesNo=document.getElementById("taxesNo").value;
 	var BANK=document.getElementById("BANK").value;
 	var INVOICE_ACCOUNT=document.getElementById("INVOICE_ACCOUNT").value;
 	var INVOICE_ADD=document.getElementById("INVOICE_ADD").value;
 	var INVOICE_PHONE=document.getElementById("INVOICE_PHONE").value;
 	if(PDEALERTYPE==""){
 		MyAlert("服务商类型不能为空！");
 		return;
		}
 	if(erpCode==""){
 		MyAlert("开票名称不能为空！");
 		return;
		}
 	if(taxesNo==""){
 		MyAlert("税号不能为空！");
 		return;
		}
 	if(BANK==""){
 		MyAlert("开户行不能为空！");
 		return;
		}
 	if(INVOICE_ACCOUNT==""){
 		MyAlert("账号不能为空！");
 		return;
		}
 	if(INVOICE_ADD==""){
 		MyAlert("地址不能为空！");
 		return;
		}
 	if(INVOICE_PHONE==""){
 		MyAlert("电话不能为空！");
 		return;
		}
 	if(INVOICE_PHONE.length>15){
 		MyAlert("电话长度不合法！");
 		return;
		}
	if(confirm("确认修改服务商信息吗？")){
		fm.action = '<%=contextPath%>/parts/baseManager/partServicerManager/ServicerInfo/modifyDealerInfo.do';
<%-- 		//makeNomalFormCall('<%=contextPath%>/parts/baseManager/partServicerManager/ServicerInfo/modifyDealerInfo.json',updateBack,'fm',''); --%>
		fm.submit();
	}
	function updateBack(json){
		if(json.success != null && json.success=='success'){
			MyAlertForFun("修改成功",sendPage);
		}else{
			MyAlert("修改失败！请联系管理员");
		}
	}
	
	
// 	var dl=document.getElementById("DEALERLEVEL").value;
	
// 		if(confirm("确认修改服务商信息吗？"))
// 		{   
<%-- 			if(dl==<%=Constant.DEALER_LEVEL_01%>){ --%>
// 				MyAlert("if");
<%-- 				if(PDEALERTYPE==<%=Constant.PDEALER_TYPE_01%>||PDEALERTYPE==<%=Constant.PDEALER_TYPE_02%>){ --%>
// 					MyAlert("if2");
<%-- 					    if(DEALERSTATUS==<%=Constant.STATUS_ENABLE%>){ --%>
// 					    	 MyAlert("if3333333333333");
<%-- 							 sendAjax('<%=contextPath%>/parts/baseManager/partServicerManager/ServicerInfo/querySameBusiness.json',showResultCodeCheck11,'fm'); --%>
// 						}
// 						else{
// 							MyAlert("if3_else");
<%-- 							fm.action = '<%=contextPath%>/parts/baseManager/partServicerManager/ServicerInfo/modifyDealerInfo.do'; --%>
// 						 	fm.submit();
// 							}
// 					}
// 				else{
<%-- 					fm.action = '<%=contextPath%>/parts/baseManager/partServicerManager/ServicerInfo/modifyDealerInfo.do'; --%>
// 				 	fm.submit();
// 					}
// 			}

// 		else{
// 			MyAlert("else");
<%-- 			fm.action = '<%=contextPath%>/sysmng/dealer/DealerInfo/modifyDealerInfo.do'; --%>
// 		 	fm.submit();
// 			}
		//}
}
//跳转到添加地址信息的页面
function addAddress(){

	var dealerId=document.getElementById("DEALER_ID").value;
	var dealerCode=document.getElementById("DEALER_CODE").value;
	var dealerName=document.getElementById("DEALER_NAME").value;
 	OpenHtmlWindow('<%=contextPath%>/parts/baseManager/partServicerManager/ServicerInfo/addAddressInit.do?dealerId='+dealerId+'&dealerCode='+dealerCode+'&dealerName='+dealerName,800,400); 
}

function showResultCodeCheck11(obj){

	var msg=obj.msg;
	if(msg=='true'){
		fm.action = '<%=contextPath%>/sysmng/dealer/DealerInfo/modifyDealerInfo.do';
	 	fm.submit();
				
	}else if(msg=='false1')
		{
		
		MyAlert("同一服务商不能维护到多个基地");
	}
	else if(msg=='false2'){
		MyAlert("同一基地同一服务商公司不为售后已存在一级服务商");
		}
}
function addBusiness()
{
	var dealerId=document.getElementById("DEALER_ID").value;
	var DEALERLEVEL=document.getElementById("DEALERLEVEL").value;
	var DEALERTYPE=document.getElementById("DEALERTYPE").value;
	var DEALERSTATUS=document.getElementById("DEALERSTATUS").value;
	OpenHtmlWindow('<%=contextPath%>/sysmng/dealer/DealerInfo/addBusiness.do?dealerId='+dealerId+'&DEALERLEVEL='+DEALERLEVEL+'&DEALERTYPE='+DEALERTYPE+'&DEALERSTATUS='+DEALERSTATUS,600,400);
}

function addPrice()
{
	var dealerId=document.getElementById("DEALER_ID").value;
	OpenHtmlWindow('<%=contextPath%>/sysmng/dealer/DealerInfo/addPrice.do?dealerId='+dealerId,700,500);
}
//修改地址信息
function modifyAdd(id)
{
	var dealerId=document.getElementById("DEALER_ID").value;
	OpenHtmlWindow('<%=contextPath%>/parts/baseManager/partServicerManager/ServicerInfo/modifyAdd.do?dealerId='+dealerId+'&id='+id,800,400);
}
function delAdd(id)
{
	if(confirm("确认删除该地址吗？"))
	{
		fm.action = '<%=contextPath%>/sysmng/dealer/DealerInfo/delAdd.do?id='+id;
	 	fm.submit();
	}
}
function delPrice(relationId)
{
	if(confirm("确认删除该价格吗？"))
	{
		fm.action = '<%=contextPath%>/sysmng/dealer/DealerInfo/delPrice.do?relationId='+relationId;
	 	fm.submit();
	}
}
function defaultPrice(relationId,priceId)
{
	var dealerId=document.getElementById("DEALER_ID").value;
	OpenHtmlWindow('<%=contextPath%>/sysmng/dealer/DealerInfo/defaultMyPriceView.do?relationId='+relationId+'&priceId='+priceId+'&dealerId='+dealerId,800,400);
	//if(confirm("确认设置当前价格为默认价格？"))
	//{
	// 	fm.submit();
//	}
}
function delBusiness(relationId)
{
	if(confirm("确认删除该服务的业务范围吗？"))
	{
		fm.action = '<%=contextPath%>/sysmng/dealer/DealerInfo/delBusiness.do?relationId='+relationId;
		fm.submit();
	}
 	
}
function parentMonth()
{
	fm.action='<%=contextPath%>/sysmng/dealer/DealerInfo/queryDealerInfoDetail.do';
	fm.submit();
}
function goBack()
{
	fm.action='<%=contextPath%>/parts/baseManager/partServicerManager/ServicerInfo/queryServicerInfoInit.do';
	fm.submit();
}
function pdealerType(){
	if($('PDEALERTYPE').value==<%=Constant.DEALER_TYPE_DWR%>){
		$('labour_id').style.display='';
		$('labour_id2').style.display='';
	}
	else{
		$('labour_id').style.display='none';
		$('labour_id2').style.display='none';
	}
}
dealerType();
</script>

</body>
</html>
