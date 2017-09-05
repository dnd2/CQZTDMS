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
<title>财务开票信息查询</title>
<script type="text/javascript" >
$(function(){
	pageSearch('normal');
}) 


var myPage;

var url = "<%=contextPath%>/parts/financeManager/partInvoiceQuery/invoiceQueryAction/invoiceSearch.json";

var title = null;
var columns = null;

function pageSearch(searchType)
{
	document.getElementById("searchType").value = searchType;

	if("normal" == searchType)
	{
		document.getElementById("countQuery").style.cssText = "display: none;";
		document.getElementById("normalQuery").style.cssText = "display: block;";
		columns = [
					{header: "序号", dataIndex: 'BILL_ID', renderer:getIndex},
                       //{id:'action',header: "操作",sortable: false,dataIndex: 'BILL_ID',renderer:myLink },
                    {header: "销售单号", dataIndex: 'SO_CODE'},
	   				{header: "发票号码", dataIndex: 'BILL_NO',style:'text-align: center;'},
                    {header: "含税开票金额(元)", dataIndex: 'BILL_AMOUNT',style:'text-align:right;'},
                    {header: "订货单位编码", dataIndex: 'DEALER_CODE'},
					{header: "订货单位", dataIndex: 'DEALER_NAME',style:'text-align: center;'},
					//{header: "税额(元)", dataIndex: 'TAX_AMOUNT',style:'text-align:right; padding-right:1%;'},
					//{header: "无税开票金额(元)", dataIndex: 'BILL_AMOUNTNOTAX',style:'text-align:right; padding-right:1%;'},
					//{header: "税率", dataIndex: 'TAX',style:'text-align:right; padding-right:1%;'},
					{header: "开票人", dataIndex: 'BILL_BY',style:'text-align: center;'},
					{header: "开票日期", dataIndex: 'BILL_DATE', align:'center'},
					{header: "开票类型", dataIndex: 'INV_TYPE', align:'center', renderer:getItemValue}
			      ];
	}
	else
	{
		document.getElementById("normalQuery").style.cssText = "display: none;";
		document.getElementById("countQuery").style.cssText = "display: block;";
		columns = [
					{header: "序号", dataIndex: 'DEALER_ID', renderer:getIndex},
					{header: "服务商编码", dataIndex: 'DEALER_CODE'},
					{header: "服务商名称", dataIndex: 'DEALER_NAME',style:'text-align: center;'},
					{header: "开票金额(元)", dataIndex: 'BILL_AMOUNT',style:'text-align: center;'},
					{header: "开票年月", dataIndex: 'BILL_MONTH'}
				/*	{header: "打印日期", dataIndex: 'PRINT_DATE', align:'center'},
					{header: "打印次数", dataIndex: 'PRINT_TIMES', align:'center'}*/
			      ];
	}
	__extQuery__(1);
}

//设置超链接
function myLink(value,meta,record)
{
	return String.format("<a href=\"#\" onclick='viewOutOrder(\""+value+"\")'>[查看]</a>");
}

function updatePage()
{
	__extQuery__(1);
}


//查看开票详情
function viewOutOrder(parms)
{
	document.getElementById("billId").value = parms;
	btnDisable();
	disableAllA();
	document.fm.action="<%=contextPath%>/parts/financeManager/partInvoiceQuery/invoiceQueryAction/viewInvPartDetail.do";
	document.fm.target="_self";
	document.fm.submit();
}

//导出财务开票信息
function exportEx(){
	fm.action = "<%=contextPath%>/parts/financeManager/partInvoiceQuery/invoiceQueryAction/exportInvoiceExcel.do";
	fm.submit();
}

// 明细下载 add zhumingwei 2013-09-28
function exportExDetail(){
	fm.action = "<%=contextPath%>/parts/financeManager/partInvoiceQuery/invoiceQueryAction/exportInvoiceExcelDetail.do";
	fm.submit();
}

//汇总导出
function exportCtInfo(){
	fm.action = "<%=contextPath%>/parts/financeManager/partInvoiceQuery/invoiceQueryAction/exportCountInfo.do";
	fm.submit();
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

function getDate()
{
	var dateS = "";
	var dateE = "";
	var myDate = new Date();
    var year = myDate.getFullYear();   //获取完整的年份(4位,1970-????)
    var moth = myDate.getMonth();      //获取当前月份(0-11,0代表1月)
    if(moth < 10)
    {
    	if(0 < moth)
	    {
	    	moth = "0" + moth;
	    }
	    else
	    {
	    	year = myDate.getFullYear() - 1;
	    	moth = moth + 12;
	    	if(moth < 10)
		    {
	    		moth = "0" + moth;
		    }
	    }
    }
    var day = myDate.getDate();       //获取当前日(1-31)
    if(day < 10)
    {
    	day = "0" + day;
    }

    dateS = year + "-" + moth + "-" + day;

    moth = myDate.getMonth() + 1;	//获取当前月份(0-11,0代表1月)
    if(moth < 10)
    {
    	moth = "0" + moth;
    }

    dateE = myDate.getFullYear() + "-" + moth + "-" + day;

    document.getElementById("checkSDate").value = dateS;
    document.getElementById("checkEDate").value = dateE;
}

function printInvoicePrintInfo(){

	OpenHtmlWindow("<%=contextPath%>/parts/financeManager/partInvoiceQuery/invoiceQueryAction/invoicePrintPage.do",950, 500);
<%-- 	window.showModalDialog("<%=request.getContextPath()%>/parts/financeManager/partInvoiceQuery/invoiceQueryAction/invoicePrintPage.do", "", 'edge: Raised; center: Yes; help: Yes; resizable: Yes; status: No;dialogHeight:540px;dialogWidth:850px'); --%>
}
</script>

</head>
<body>
	<div class="wbox">
		<form name="fm" id="fm" method="post" enctype="multipart/form-data">
			<input type="hidden" name="searchType" id="searchType" value="" />
			<div class="navigation">
				<img src="<%=request.getContextPath()%>/img/nav.gif" /> &nbsp;当前位置： 配件财务管理 &gt; 财务开票信息查询
				<input type="hidden" name="billId" id="billId" value="" />
			</div>
			<div id="normalQuery">
				<div class="form-panel">
					<h2>
						<img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;" />查询条件
					</h2>
					<div class="form-body">
						<table class="table_query">
							<tr>
								<td class="right">销售单号：</td>
								<td>
									<input class="middle_txt" type="text" name="sellCode" id="sellCode" />
								</td>
								<td class="right">订货单位：</td>
								<td>
									<input class="middle_txt" type="text" name="dealerName" id="dealerName" />
								</td>
								<td class="right">开票类型：</td>
								<td>
									<script type="text/javascript">
										genSelBoxExp("dlrInvTpe",
									<%=Constant.DLR_INVOICE_TYPE%>
										,
									<%=Constant.DLR_INVOICE_TYPE_01%>
										, true, "", "onchange=updatePage()", "false", "");
									</script>
								</td>
							</tr>
							<tr>
								<td class="right">发票号码：</td>
								<td>
									<input class="middle_txt" type="text" name="invCodeNum" id="invCodeNum" />
								</td>
								<td class="right">开票日期：</td>
								<td>
									<input id="checkSDate" style="width: 80px;" class="short_txt" name="checkSDate" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" />
									<input class="time_ico" onclick="showcalendar(event, 'checkSDate', false);" value=" " type="button" />
									至
									<input id="checkEDate" style="width: 80px;" class="short_txt" name="checkEDate" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" />
									<input class="time_ico" onclick="showcalendar(event, 'checkEDate', false);" value=" " type="button" />
								</td>
								<td class="right">开票人：</td>
								<td>
									<input class="middle_txt" type="text" name="invPerson" id="invPerson" />
								</td>
							</tr>
							<tr>
								<td class="center" colspan="6">
									<input class="u-button" type="button" value="查 询" name="BtnQuery" id="queryBtn" onclick="pageSearch('normal')" />
									<input class="u-button" type="button" value="导 出" onclick="exportEx();" />
									<input class="u-button" type="button" value="汇总查询" name="BtnQuery" id="queryCtBtn" onclick="pageSearch('count')" />
									<input class="u-button" type="button" value="汇总导出" onclick="exportCtInfo();" />
									<input class="u-button" type="reset" value="重 置" />
									<input class="u-button" type="button" value="明细下载" onclick="exportExDetail();" />
									<input class="u-button" type="button" value="打印发票邮寄信息" name="invoicePrintInfo" id="invoicePrintInfo" onclick="printInvoicePrintInfo();" />
								</td>
							</tr>
						</table>
					</div>
				</div>
			</div>
			<div id="countQuery" style="display: none;">
				<div class="form-panel">
					<h2>
						<img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;" />查询条件
					</h2>
					<div class="form-body">
						<table class="table_query">
							<tr>
								<td class="right">服务商编码：</td>
								<td>
									<input class="middle_txt" type="text" name="dealerCode" id="dealerCode" />
								</td>
								<td class="right">服务商名称：</td>
								<td>
									<input class="middle_txt" type="text" name="dealerName2" id="dealerName2" />
								</td>
							</tr>
							<tr>
								<td class="right">汇总年份：</td>
								<td>
									<select id="year1" onchange="getPlanVer();" name="year1" class="u-select" style="width: 80px;">
										<%
										    String year = (String) request.getAttribute("curYear");
										    if (null == year || "".equals(year)) {
										        year = "0";
										    }
										    int y = Integer.parseInt(year);
										%>
										<option value="<%=y - 5%>"><%=y - 5%>
										</option>
										<option value="<%=y - 4%>"><%=y - 4%>
										</option>
										<option value="<%=y - 3%>"><%=y - 3%>
										</option>
										<option value="<%=y - 2%>"><%=y - 2%>
										</option>
										<option value="<%=y - 1%>"><%=y - 1%>
										</option>
										<option value="<%=y%>" selected="selected"><%=y%>
										</option>
										<option value="<%=y + 1%>"><%=y + 1%>
										</option>
										<option value="<%=y + 2%>"><%=y + 2%>
										</option>
										<option value="<%=y + 3%>"><%=y + 3%>
										</option>
									</select>
								</td>
								<td class="right">汇总月份：</td>
								<td>
									<select name="month1" id="month1" class="u-select" style="width: 60px;">
										<option value='1' <c:if test="${curMonth==1}">selected="selected"</c:if>>01</option>
										<option value='2' <c:if test="${curMonth==2}">selected="selected"</c:if>>02</option>
										<option value='3' <c:if test="${curMonth==3}">selected="selected"</c:if>>03</option>
										<option value='4' <c:if test="${curMonth==4}">selected="selected"</c:if>>04</option>
										<option value='5' <c:if test="${curMonth==5}">selected="selected"</c:if>>05</option>
										<option value='6' <c:if test="${curMonth==6}">selected="selected"</c:if>>06</option>
										<option value='7' <c:if test="${curMonth==7}">selected="selected"</c:if>>07</option>
										<option value='8' <c:if test="${curMonth==8}">selected="selected"</c:if>>08</option>
										<option value='9' <c:if test="${curMonth==9}">selected="selected"</c:if>>09</option>
										<option value='10' <c:if test="${curMonth==10}">selected="selected"</c:if>>10</option>
										<option value='11' <c:if test="${curMonth==11}">selected="selected"</c:if>>11</option>
										<option value='12' <c:if test="${curMonth==12}">selected="selected"</c:if>>12</option>
									</select> &nbsp;&nbsp;至&nbsp;&nbsp; <select name="month2" id="month2" class="u-select" style="width: 60px;">
										<option value='1' <c:if test="${curMonth==1}">selected="selected"</c:if>>01</option>
										<option value='2' <c:if test="${curMonth==2}">selected="selected"</c:if>>02</option>
										<option value='3' <c:if test="${curMonth==3}">selected="selected"</c:if>>03</option>
										<option value='4' <c:if test="${curMonth==4}">selected="selected"</c:if>>04</option>
										<option value='5' <c:if test="${curMonth==5}">selected="selected"</c:if>>05</option>
										<option value='6' <c:if test="${curMonth==6}">selected="selected"</c:if>>06</option>
										<option value='7' <c:if test="${curMonth==7}">selected="selected"</c:if>>07</option>
										<option value='8' <c:if test="${curMonth==8}">selected="selected"</c:if>>08</option>
										<option value='9' <c:if test="${curMonth==9}">selected="selected"</c:if>>09</option>
										<option value='10' <c:if test="${curMonth==10}">selected="selected"</c:if>>10</option>
										<option value='11' <c:if test="${curMonth==11}">selected="selected"</c:if>>11</option>
										<option value='12' <c:if test="${curMonth==12}">selected="selected"</c:if>>12</option>
									</select>
								</td>
							</tr>
							<tr>
								<td class="center" colspan="6">
									<input class="u-button" type="button" value="查 询" name="BtnQuery" id="queryBtn" onclick="pageSearch('normal')" />
									<input class="u-button" type="button" value="导 出" onclick="exportPartInvoiceExcel()" />
									<input class="u-button" type="button" value="汇总查询" name="BtnQuery" id="queryCtBtn" onclick="pageSearch('count')" />
									<input class="u-button" type="button" value="汇总导出" onclick="exportCtInfo();" />
									<input class="u-button" type="reset" value="重 置" />
									<input class="u-button" type="button" value="明细下载" onclick="exportExDetail();" />
									<input class="u-button" type="button" value="打印发票邮寄信息" name="invoicePrintInfo" id="invoicePrintInfo" onclick="printInvoicePrintInfo();" />
								</td>
							</tr>
						</table>
					</div>
				</div>
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