<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%
    String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>财务金税发票</title>
<script type="text/javascript" >
$(function(){
	__extQuery__(1);
});

var myPage;

var url = "<%=contextPath%>/parts/financeManager/partInvoiceManager/partInvoiceAction/partInvoiceSearch.json";
			
var title = null;

var columns = [
			{header: "序号", dataIndex: 'OUT_ID', renderer:getIndex},
			{header: "<input type='checkbox'  name='ckbAll' onclick='selAll(this)'  />", dataIndex: 'OUT_ID',renderer:seled},
	//		{header: "订货单号", dataIndex: 'ORDER_CODE', align:'center'},
			{header: "销售单号", dataIndex: 'SO_CODE',renderer:getSoCode},
	//		{header: "出库单号", dataIndex: 'OUT_CODE'},
			{header: "订货单位", dataIndex: 'DEALER_NAME',style:'text-align:left;'},
	//		{header: "销售单位", dataIndex: 'SELLER_NAME', align:'center'},
			{header: "销售金额(元)", dataIndex: 'AMOUNT',style:'text-align:right;'},
			{header: "财务审核人", dataIndex: 'F_NAME',style:'text-align:left;'},
			{header: "财务审核日期", dataIndex: 'FCAUDIT_DATE'},
			{header: "出库仓库", dataIndex: 'WH_NAME',style:'text-align:left;'},
			{header: "出库日期", dataIndex: 'CREATE_DATE', align:'center'},
			{header: "导出状态", dataIndex: 'IS_EXPORT',renderer:getItemValue},
			{header: "是否开票", dataIndex: 'IS_INV',renderer:getItemValue},
			{header: "发票号码", dataIndex: 'BILL_NO'},
               {header: "金税导出人", dataIndex: 'NAME',style:'text-align:left;'},
               {header: "导出日期", dataIndex: 'EXPORT_DATE'},
               {header: "订单类型", dataIndex: 'ORDER_TYPE', renderer:getItemValue},
			{header: "开票人", dataIndex: 'BILL_BY',style:'text-align:left;'},
			{header: "开票日期", dataIndex: 'BILL_DATE'},
			{header: "改票人", dataIndex: 'BILL_UPDATE_BY',style:'text-align:left;'},
			{header: "改票日期", dataIndex: 'BILL_UPDATE_DATE'},
			{id:'action',header: "操作",sortable: false,dataIndex: 'OUT_ID',renderer:myLink }
	      ];

//设置超链接
function myLink(value,meta,record)
{
	return String.format("<a href=\"#\" onclick='viewOutOrder(\""+value+"\")'>[查看]</a>");
}

function getSoCode(value,meta,record)
{
	var isInv = record.data.IS_INV;
	var isExp = record.data.IS_EXPORT;
	var isNbdw = record.data.IS_NBDW;
	var invYes = <%=Constant.IF_TYPE_YES %>;
var expState2 = <%=Constant.PART_INVO_OUT_STATE_02 %>;
	if(isInv.toString() == invYes.toString() || '1' == isNbdw)
	{
		return String.format("<span style='background-color: #FAA095; width: 100%;'>"+ value +"</span>");
	}
	else if(isExp.toString() == expState2.toString())
	{
		return String.format("<span style='background-color: #B9FA95; width: 100%;'>"+ value +"</span>");
	}
	else
	{
		return String.format("<span>"+ value +"</span>");
	}
}

function seled(value,meta,record) 
{
	var soCode = record.data.SO_CODE;
	var outCode = record.data.OUT_CODE;
	var dealerName = record.data.DEALER_NAME;
	var invAmout = record.data.AMOUNT;
	var dealerId = record.data.DEALER_ID;
	var isInv = record.data.IS_INV;
	var invYes = <%=Constant.IF_TYPE_YES %>;
	var str = "";
	if(isInv.toString() != invYes.toString())
	{
		str = "<input type='checkbox' value='"+value+"' name='ck' id='ck' checked='checked' />";
	}
	else
	{
		str = "<input type='checkbox' value='"+value+"' name='ck' id='ck' />";
	}
	str += "<input type='hidden' value='"+soCode+"' id='soCode_"+value+"' />";
	str += "<input type='hidden' value='"+outCode+"' id='outCode_"+value+"' />";
	str += "<input type='hidden' value='"+dealerName+"' id='dealerName_"+value+"' />";
	str += "<input type='hidden' value='"+dealerId+"' id='dealerId_"+value+"' />";
	str += "<input type='hidden' value='"+invAmout+"' id='invAmout_"+value+"' />"
	return str;
}

function updatePage()
{
	var invWayObj = document.getElementById("invWay");
	var isInvObj = document.getElementById("isInv");
	var ivnHadBtn = document.getElementById("invByHandleBtn");
	var expInvTxtObj = document.getElementById("expInvTxt");
	var invoOutState = document.getElementById("invoOutState");
	
	__extQuery__(1);
}

function selAll(obj){
	var cks = document.getElementsByName('ck');
	for(var i =0 ;i<cks.length;i++){
		if(obj.checked){
			cks[i].checked = true;
		}else{
			cks[i].checked = false;
		}
	}
}

//查看出库单详情
function viewOutOrder(parms)
{
	document.getElementById("viewOutId").value = parms; 
	btnDisable();
	disableAllA();
	document.fm.action="<%=contextPath%>/parts/financeManager/partInvoiceManager/partInvoiceAction/viewOutOrdDetail.do";
	document.fm.target="_self";
	document.fm.submit();
}

//导出财务金税发票
function exportPartInvoiceExcel(){
	document.fm.action="<%=contextPath%>/parts/financeManager/partInvoiceManager/partInvoiceAction/exportPartInvoiceExcel.do";
	document.fm.target="_self";
	document.fm.submit();
}

function viewInvCount()
{
	OpenHtmlWindow('<%=contextPath%>/parts/financeManager/partInvoiceManager/partInvoiceAction/expInvCountInit.do', 950, 500);
}

//手工开票
function invByHandle()
{
	var cks = document.getElementsByName('ck');
	var outIds = "";
	var dealerIdArr = new Array();
	//var outCode = "";
	var soCodes = "";
	//var dealerName = "";
	//var invAmout = "";
	
	var count = 0;
	for(var i =0 ;i<cks.length;i++){
		if(cks[i].checked)
		{
			outIds += cks[i].value + ",";
			soCodes += document.getElementById("soCode_"+cks[i].value).value + ",";
			dealerIdArr[count] = document.getElementById("dealerId_"+cks[i].value).value;
			count ++;
		}
	}
	
	if(count <= 0)
	{
		MyAlert("请勾选同一订货单位至少一个单号进行手工开票!")
		return flase;
	}
	else
	{
		for( var j = 0; j < dealerIdArr.length; j ++)
		{
			if(dealerIdArr[0] != dealerIdArr[j])
			{
				MyAlert("请勾选同一订货单位单号进行手工开票!")
				return flase;
			}
		}
		//outCode = document.getElementById("outCode_"+outId).value;
		//soCode = document.getElementById("soCode_"+outId).value;
		//dealerName = document.getElementById("dealerName_"+outId).value;
		//invAmout = document.getElementById("invAmout_"+outId).value;
	}
	OpenHtmlWindow('<%=contextPath%>/parts/financeManager/partInvoiceManager/partInvoiceAction/invByHandle.do?outIds='+outIds+'&soCodes='+soCodes,950, 400);
}

//导出金税文本验证
function confirmExpInvTxt(){
	btnDisable();
	var cks = document.getElementsByName('ck');
	var outOrdIds = "";
	var count = 0;
	for(var i =0 ;i<cks.length;i++){
		if(cks[i].checked)
		{
			count ++;
			outOrdIds += cks[i].value + ",";
		}
	}
	if(count > 0)
	{
		document.getElementById("checkedOptions").value = outOrdIds;
		var url = "<%=contextPath%>/parts/financeManager/partInvoiceManager/partInvoiceAction/checkInvoTxtOP.json";	
		sendAjax(url,checkResult,'fm');
	}
	else
	{
		MyAlert("请先选择导出金税文本的单据!");
		btnEnable();
	}
}

function checkResult(json){
	btnEnable();
	if(null != json){
        if (json.success != null) {
        	MyConfirm(json.success,exportPartInvoTaxTxt,[]);
        } else {
            MyAlert("导出验证失败，请联系管理员!");
        }
	}
}

//导出金税文本
function exportPartInvoTaxTxt(){
	document.fm.action="<%=contextPath%>/parts/financeManager/partInvoiceManager/partInvoiceAction/exportPartInvoTaxTxt.do";
	document.fm.target="_self";
	document.fm.submit();
	window.setTimeout(function () { updatePage(); }, 15000);
}

//上传
function uploadTxt(){
	btnDisable();
	fm.action = "<%=contextPath%>/parts/financeManager/partInvoiceManager/partInvoiceAction/invoImpUploadNew.do";
	fm.submit();
}

//上传检查和确认信息
function confirmUpload()
{
	if(vilidate()){
		MyConfirm("确定导入开票信息?",uploadTxt,[]);
   	}
	
}

function vilidate(){
	var importFileName = $("importFile").value;
	if(importFileName==""){
	    MyAlert("请选择上传文件");
		return false;
	}
	var index = importFileName.lastIndexOf(".");
	var suffix = importFileName.substr(index+1,importFileName.length).toLowerCase();
	if(suffix != "txt"){
	MyAlert("请选择txt格式文件");
		return false;
	}
	return true;
}

function showUpload(){
	var uploadDiv = document.getElementById("uploadDiv");
	if(uploadDiv.style.display=="block" ){
		uploadDiv.style.display = "none";
	}else {
	uploadDiv.style.display = "block";
	}
}

//失效A标签
function disableAllA(){
	var inputArr = document.getElementsByTagName("a");
	for(var i=0;i<inputArr.length;i++){
		inputArr[i].disabled=true;
	}
}

//下载上传模板
function exportExcelTemplate(){
	fm.action = "<%=contextPath%>/parts/financeManager/partInvoiceManager/partInvoiceAction/exportExcelTemplate.do";
	fm.submit();
}

</script>
</head>
<body>
	<div class="wbox">
		<form name="fm" id="fm" method="post" enctype="multipart/form-data">
			<div class="navigation">
				<img src="<%=request.getContextPath()%>/img/nav.gif" /> &nbsp;当前位置： 配件财务管理 &gt; 财务金税发票
				<input type="hidden" name="parentOrgId" id="parentOrgId" value="${parentOrgId }" />
				<input type="hidden" name="parentOrgCode" id="parentOrgCode" value="${parentOrgCode }" />
				<input type="hidden" name="resultId" id="resultId" value="" />
				<input type="hidden" name="viewOutId" id="viewOutId" value="" />
				<input type="hidden" name="checkedOptions" id="checkedOptions" value="" />
			</div>

			<div class="form-panel">
				<h2>
					<img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;" />查询条件
				</h2>
				<div class="form-body">
					<table class="table_query">
						<th colspan="6">
							<div style="width: 70%; float: left;">
								<div style="width: 10px; height: 10px; border: 1px solid black; background-color: #FAA095; float: left;"></div>
								<span style="float: left;">&nbsp;已开票</span>
								<div style="width: 10%; float: left;">&nbsp;&nbsp;</div>
								<div style="width: 10px; height: 10px; border: 1px solid black; background-color: #B9FA95; float: left;"></div>
								<span style="float: left;">&nbsp;已导出</span>
								<div style="width: 10%; float: left;">&nbsp;</div>
								<div style="width: 10px; height: 10px; border: 1px solid black; float: left;"></div>
								<span style="float: left;">&nbsp;未导出</span>
							</div>
						</th>
						<tr>
							<td class="right">订货单位：</td>
							<td>
								<input class="middle_txt" type="text" name="dealerName" id="dealerName" />
							</td>
							<td class="right">财务审核日期：</td>
							<td>
								<input id="finChkSDate" style="width: 80px;" class="short_txt" name="finChkSDate" datatype="1,is_date,10" maxlength="10" group="finChkSDate,finChkEDate" />
								<input class="time_ico" onclick="showcalendar(event, 'finChkSDate', false);" value=" " type="button" />
								至
								<input id="finChkEDate" style="width: 80px;" class="short_txt" name="finChkEDate" datatype="1,is_date,10" maxlength="10" group="finChkSDate,finChkEDate" />
								<input class="time_ico" onclick="showcalendar(event, 'finChkEDate', false);" value=" " type="button" />
							</td>
							<td class="right">订单类型：</td>
							<td>
								<script type="text/javascript">
									genSelBoxExp("orderType", <%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE%>, "", true, "", "onchange=updatePage()", "false", "");
								</script>
							</td>
						</tr>
						<tr>
							<td class="right">销售单号：</td>
							<td>
								<input class="middle_txt" type="text" name="sellCode" id="sellCode" />
							</td>
							<td class="right">销售单位：</td>
							<td>
								<input class="middle_txt" type="text" name="sellerName" id="sellerName" />
							</td>
							<td class="right">导出状态：</td>
							<td>
								<script type="text/javascript">
									genSelBoxExp("invoOutState", <%=Constant.PART_INVO_OUT_STATE%>, <%=Constant.PART_INVO_OUT_STATE_01%>, true, "", "onchange=updatePage()", "false", "");
								</script>
							</td>
						</tr>
						<tr>
							<td class="right">是否内部单位：</td>
							<td>
								<select name="isNbdw" id="isNbdw" class="u-select" onchange="updatePage()">
									<option value="">-请选择-</option>
									<option value="1">是</option>
									<option value="0" selected="selected">否</option>
								</select>
							</td>
							<td class="right">出库日期：</td>
							<td>
								<input id="checkSDate" style="width: 80px;" class="short_txt" name="checkSDate" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" />
								<input class="time_ico" onclick="showcalendar(event, 'checkSDate', false);" value=" " type="button" />
								至
								<input id="checkEDate" style="width: 80px;" class="short_txt" name="checkEDate" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" />
								<input class="time_ico" onclick="showcalendar(event, 'checkEDate', false);" value=" " type="button" />
							</td>
							<td class="right">是否开票：</td>
							<td>
								<script type="text/javascript">
									genSelBoxExp("isInv", <%=Constant.IF_TYPE%>, <%=Constant.IF_TYPE_NO%>, true, "", "onchange=updatePage()", "false", '');
								</script>
							</td>
						</tr>
						<tr>
							<td class="right">开票类型：</td>
							<td>
								<script type="text/javascript">
									genSelBoxExp("dlrInvTpe", <%=Constant.DLR_INVOICE_TYPE%>, <%=Constant.DLR_INVOICE_TYPE_01%>, true, "", "onchange=updatePage()", "false", "");
								</script>
							</td>
							<td class="right">开票方式：</td>
							<td>
								<script type="text/javascript">
									genSelBoxExp("invWay", <%=Constant.INVOICE_WAY%>, "", true, "", "onchange=updatePage()", "false", "");
								</script>
							</td>
							<td class="right">发票号：</td>
							<td>
								<input class="middle_txt" type="text" name="inVo" id="inVo" />
							</td>
						</tr>
						<tr>
							<td class="center" colspan="6">
								<input class="u-button" type="button" value="查 询" name="BtnQuery" id="queryBtn" onclick="updatePage()" />
								<input class="u-button" type="button" value="汇总查询" onclick="viewInvCount()" />
								<input class="u-button" type="reset" value="重 置">
								<input class="u-button" type="button" value="导 出" onclick="exportPartInvoiceExcel()" />
								<input class="u-button" type="button" value="导出金税文本" id="expInvTxt" onclick="confirmExpInvTxt()" />
								<input class="u-button" type="button" value="导入开票信息" id="inInvTxt" id="upload_button" name="button1" onclick="showUpload();">
								<input class="u-button" type="button" value="手工开票" id="invByHandleBtn" onclick="invByHandle()" />
							</td>
						</tr>
					</table>
				</div>
			</div>
			<div style="display: none; heigeht: 5px" id="uploadDiv">
				<table class="table_query">
					<th colspan="6">
						<img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 上传文件
					</th>
					<tr>
						<td colspan="2">
							<font color="red"> &nbsp;&nbsp; 文件选择后,点&quot;确定&quot;按钮,完成上传操作：&nbsp;</font>
							<input type="file" name="importFile" id="importFile" style="width: 250px" datatype="0,is_null,2000" value="" />
							&nbsp;
							<input type="button" id="upbtn" class="normal_btn" value="确 定" onclick="confirmUpload()" />
						</td>
					</tr>
				</table>
			</div>

			<!-- 查询条件 end -->
			<!--分页 begin -->
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
			<!--分页 end -->
		</form>
	</div>
</body>
</html>