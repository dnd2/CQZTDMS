<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

    <title>采购订单结算</title>
<script type="text/javascript">
<!--
var myPage;
var url = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderBalanceManager/queryOrderBalanceMng.json";

var title = null;

var columns = [
               {header: "序号", width: '5%', renderer: getIndex},
               {id: 'action', header: "操作", sortable: false, dataIndex: 'BALANCE_CODE', renderer: myLink, align: 'center'},
               {header: "结算单号", dataIndex: 'BALANCE_CODE', style: 'text-align:left', renderer: changeColor}, 
               {header: "结算日期", dataIndex: 'CREATE_DATE', align: 'center', renderer: formatDate},
               {header: "供应商", dataIndex: 'VENDER_NAME', style: 'text-align:left', renderer: vdInput},
               {header: "采购组织", dataIndex: 'PRODUCE_FAC', style: 'text-align:left'},
               {header: "发票号", dataIndex: 'INVO_NO', align: 'center', renderer: invInput},
               {header: "项数", dataIndex: 'XS', align: 'center'},
               {header: "入库数量", dataIndex: 'IN_QTY', align: 'center'},
               {header: "结算数量", dataIndex: 'BAL_QTY', align: 'center'},
               {header: "不含税入库总金额", dataIndex: 'IN_AMOUNT_NOTAX', style: 'text-align:right'},
               {header: "入库总金额", dataIndex: 'IN_AMOUNT', style: 'text-align:right'},
               {header: "不含税结算总金额", dataIndex: 'BAL_AMOUNT_NOTAX', style: 'text-align:right'},
               {header: "结算总金额", dataIndex: 'BAL_AMOUNT', style: 'text-align:right'},
               {header: "入库与结算差异金额", dataIndex: 'DIFF_AMOUNT', style: 'text-align:right'},
               {header: "无税开票金额", dataIndex: 'BAL_AMOUNT_FINAL', style: 'text-align:right'},
               {header: "临时结算与正式结算差异金额", dataIndex: 'DIFF_AMOUNT_FINAL', style: 'text-align:right'},
               {header: "不含税开票金额", dataIndex: 'INVO_AMOUNT_NOTAX', style: 'text-align:right'},
               {header: "结算与开票差异金额", dataIndex: 'DIFF_INVO_NOTAX_AMOUNT', style: 'text-align:right'},
               {header: "结算员", dataIndex: 'BALANCER', align: 'center'},
               {header: "状态", dataIndex: 'STATE', align: 'center', renderer: getItemValue}
           ];

function query(){
	__extQuery__(1);
}

function queryDtl() {
	$("#uploadTable")[0].style.display = "none";
    $("#allQuery")[0].style.display = "none";
    $("#dtlQuery")[0].style.display = "";
    url = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderBalanceManager/queryOrderBalanceInfo.json";
    columns = [
        {header: "序号", width: '5%', renderer: getIndex},
        {header: "验收单号", dataIndex: 'CHECK_CODE', style: 'text-align:left'},
        {header: "入库单号", dataIndex: 'IN_CODE', style: 'text-align:left'},
        {header: "结算单号", dataIndex: 'BALANCE_CODE', style: 'text-align:left'},
        {header: "配件类型", dataIndex: 'PART_TYPE', align: 'center', renderer: getItemValue},
        {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
        {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
        {header: "配件件号", dataIndex: 'PART_CODE', style: 'text-align:left'},
        {header: "入库数量", dataIndex: 'IN_QTY', align: 'center'},
        {header: "退货数量", dataIndex: 'RETURN_QTY', align: 'center'},
        {header: "结算数量", dataIndex: 'BAL_QTY', align: 'center'},
        {header: "供应商名称", dataIndex: 'VENDER_NAME', style: 'text-align:left'},
        {header: "制造商名称", dataIndex: 'MAKER_NAME', style: 'text-align:left'},
        {header: "入库库房", dataIndex: 'WH_NAME', align: 'center'},
        {header: "入库人员", dataIndex: 'IN_NAME', align: 'center'},
        {header: "结算人员", dataIndex: 'NAME', align: 'center'},
        {header: "计划价", dataIndex: 'PLAN_PRICE', style: 'text-align:right'},
        {header: "计划金额", dataIndex: 'PLAN_AMOUNT', style: 'text-align:right'},
        {header: "采购价", dataIndex: 'BUY_PRICE', style: 'text-align:right'},
        {header: "采购金额", dataIndex: 'BAL_AMOUNT', style: 'text-align:right'},
        {header: "采购员", dataIndex: 'BUYER', align: 'center'},
        {header: "状态", dataIndex: 'STATE', align: 'center', renderer: getItemValue}
        //{id: 'action', header: "操作", sortable: false, dataIndex: 'IN_ID', renderer: myLink, align: 'center'}
    ];
}
var len = columns.length;
//设置超链接  begin
function selAll(obj) {
    var cks = document.getElementsByName('ids');
    for (var i = 0; i < cks.length; i++) {
        if (obj.checked) {
            cks[i].checked = true;
        } else {
            cks[i].checked = false;
        }
    }
}

function chkPart(){
	var cks = document.getElementsByName('ids');
	var flag = true;
	for(var i =0 ;i<cks.length;i++){
		var obj  = cks[i];
		if(!obj.checked){
			flag = false;
		}
	}
	document.getElementById("checkAll").checked = flag;
}


//全选checkbox
function myCheckBox(value, metaDate, record) {
    return String.format("<input type='checkbox' name='ids' id='ids" + value + "' value='" + value + "' onclick='chkPart()'/>");
}

function changeColor(value, meta, record) {
    var chgId = record.data.CHG_ID;
    if (chgId) {
        return String.format("<input type='text' style='color:red;border: none' name='balCode' id='balCode" + value + "' value='" + value + "'' readonly='readonly' />");
    } else {
        return String.format("<input type='text' style='border: none' name='balCode' id='balCode" + value + "' value='" + value + "'' readonly='readonly' />");
    }
}
function myLink(value, meta, record) {
	var state = record.data.STATE;
	var invoNo = record.data.INVO_NO;
	var output = "<a href=\"#\" onclick='view(\"" + value + "\","+state+")'>[查看]</a>";
	
	if(state==<%=Constant.PART_PURCHASE_ORDERBALANCE_STATUS_06%>||state==<%=Constant.PART_PURCHASE_ORDERBALANCE_STATUS_02%>){
		output+="<a href=\"#\" onclick='updateBal(\"" + value + "\",\""+invoNo+"\")'>[修改]</a><a href=\"#\" onclick='submitBal(\"" + value + "\")'>[提交]</a><a href=\"#\" onclick='delBal(\"" + value + "\")'>[作废]</a>";
	}
	
	if(invoNo){
		output+="<a href=\"#\" onclick='printBal(\"" + value + "\")'>[打印]</a>";
	}
	
    return String.format(output);
}

//查看
function view(value,state) {
    window.location.href = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderBalanceManager/purchaseOrderInStockInit.do?balCode="+value+"&state="+state;
}

//修改
function updateBal(value,invoNo) {
    window.location.href = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderBalanceManager/purOrderBalUpdateInit.do?balCode="+value+"&invoNo="+invoNo;
}

//作废
function delBal(value) {
	MyConfirm("确定作废?",function(){
		var url = '<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderBalanceManager/delBalanceOrder.json?balCode='+value+'&curPage='+myPage.page;
		sendAjax(url,getResult,'fm');
	})
}

//打印
function printBal(value) {
	window.open("<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderBalanceManager/opPrintHtml.do?balCode=" + value,"","toolbar,menubar,scrollbars,resizable,status,location,directories,copyhistory,height=600,width=500");
}

//提交
function submitBal(value) {
	var inveNo = $("#inv_"+value)[0].value;
	if(!inveNo){
		OpenHtmlWindow('<%=contextPath%>/jsp/parts/purchaseOrderManager/purchaseOrderBalance/purchaseOrderSetInvoice.jsp?balCode='+value,600,250);
	} else{
	MyConfirm("确定提交?",function(){
	    var url = '<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderBalanceManager/submitBalanceOrder.json?balCode='+value+'&curPage='+myPage.page;
		sendAjax(url,getResult,'fm');
	})
	}
}


function vdInput(value, metaDate, record) {
    var chkId = record.data.CHECK_ID;
    return String.format("<input type='hidden' name='vd_" + chkId + "' id='vd_" + chkId + "'  value='" + value + "'/>" + value);
}
function bcInput(value, metaDate, record) {
    var chkId = record.data.CHECK_ID;
    return String.format("<input type='hidden' name='bc_" + chkId + "' id='bc_" + chkId + "'  value='" + value + "'/>" + value);
}
function invInput(value, metaDate, record) {
    var balCode = record.data.BALANCE_CODE;
    var state = record.data.STATE;
    var output = "<input type='hidden' name='inv_" + balCode + "' id='inv_" + balCode + "'  value='" + value + "'/>"+
    "<input type='hidden' name='STATE_" + balCode + "' id='STATE_" + balCode + "'  value='" + state + "'/>" + value;
    return output;
}
//增加
function addInit(){
	window.location.href = '<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderBalanceManager/addInit.do';
}


function getResult(jsonObj) {
    if (jsonObj != null) {
        var success = jsonObj.success;
        var exceptions = jsonObj.Exception;
        if (success) {
            MyAlert(success);
            __extQuery__(jsonObj.curPage);
        } else if (exceptions) {
            MyAlert(exceptions.message);
        }
    }
}


//格式化日期
function formatDate(value, meta, record) {
    var output = value.substr(0, 10);
    return output;
}

//清空选定供应商
function clearInput() {
    document.getElementById("VENDER_ID").value = '';
    document.getElementById("VENDER_NAME").value = '';
}

//清空选定供应商
function clearInput1() {
    document.getElementById("VENDER_ID2").value = '';
    document.getElementById("VENDER_NAME2").value = '';
}

//导出
function exportOrderBalExcel(val) {
	  if (val == "1") {
          fm.action = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderBalanceManager/exportOrderBalExcel1.do";
      } else if (val == "2") {
          fm.action = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderBalanceManager/exportOrderBalExcel.do";
      }
    fm.target = "_self";
    fm.submit();
}
function myback() {
    window.location.href = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderBalanceManager/purchaseOrderBalanceQueryInit.do";
}

Date.prototype.format = function(format)
{
 var o = {
 "M+" : this.getMonth()+1, //month
 "d+" : this.getDate(),    //day
 "h+" : this.getHours(),   //hour
 "m+" : this.getMinutes(), //minute
 "s+" : this.getSeconds(), //second
 "q+" : Math.floor((this.getMonth()+3)/3),  //quarter
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
$(document).ready(function(){
	__extQuery__(1);
});

function getInvoNo(value){
	__extQuery__(1);
	 MyConfirm("确定提交结算单?",function(){
		    var url = '<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderBalanceManager/submitBalanceOrder.json?balCode='+value+'&curPage='+myPage.page;
			sendAjax(url,getResult,'fm');
	 })
}

</script>
</head>
<body >
<div class="wbox">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif"/>&nbsp;
    当前位置：配件管理&gt;采购订单管理&gt; 采购订单结算
</div>
<form method="post" name="fm" id="fm" enctype="multipart/form-data">
<div class="form-panel">
				<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
				<div class="form-body">
    <input type="hidden" name="curPage" id="curPage"/>
    <input type="hidden" name="partId" id="partId"/>
    <table class="table_query" width=100% border="0" class="center" cellpadding="1" cellspacing="1" id="allQuery">
        <tr>
            <td class="right">结算单号：</td>
            <td ><input class="middle_txt" type="text" name="BALANCE_CODE"/></td>
             <td class="right">采购组织：</td>
                <td >
                    <script type="text/javascript">
                        genSelBoxExp("PURCHASE_WAY", <%=Constant.PURCHASE_WAY %>, "", true, "", "", "false", "");
                    </script>
                </td>
                <td class="right">采购方式：</td>
                <td >
                    <script type="text/javascript">
                        genSelBoxExp("PART_PRODUCE_STATE", <%=Constant.PART_PRODUCE_STATE%>, "", true, "", "", "false", '');
                    </script>
                </td>
            
            </tr>
        <tr>
            <td class="right">配件编码：</td>
            <td ><input name="DPART_OLDCODE" type="text" class="middle_txt" id="PART_OLDCODE"/></td>
            <td class="right">配件名称：</td>
            <td ><input name="DPART_CNAME" type="text" class="middle_txt" id="PART_CNAME"/></td>
            <td class="right">配件件号：</td>
            <td ><input name="DPART_CODE" type="text" class="middle_txt" id="PART_CODE"/></td>
        </tr>
        <tr>
        <td  class="right">结算员：</td>
            <td >
                <select id="BALANCER_ID" name="BALANCER_ID"  class="u-select">
                    <option value="">-请选择-</option>
                    <c:forEach items="${planerList}" var="planerList">
                      <c:choose>
						<c:when test="${curUserId eq planerList.USER_ID}">
						  <option selected="selected" value="${planerList.USER_ID }" >${planerList.USER_NAME }</option>
						</c:when>
						<c:otherwise>
						  <option value="${planerList.USER_ID }" >${planerList.USER_NAME }</option>
						</c:otherwise>
					  </c:choose>
                    </c:forEach>
                </select>
            </td>
            <td class="right">单据状态：</td>
            <td >
                <script type="text/javascript">
                    genSelBoxExp("STATE", <%=Constant.PART_PURCHASE_ORDERBALANCE_STATUS %>, "<%=Constant.PART_PURCHASE_ORDERBALANCE_STATUS_02 %>", true, "", "", "false", "<%=Constant.PART_PURCHASE_ORDERBALANCE_STATUS_01 %>");
                </script>
            </td>
            <td class="right">发票号：</td>
            <td ><input name="INVO_NO1" type="text" class="middle_txt" id="INVO_NO1"/></td>
        </tr>
        <tr>
           <td class="right">供应商：</td>
            <td >
                <input class="middle_txt" type="text" readonly="readonly" id="VENDER_NAME" name="VENDER_NAME"/>
                <input class="mark_btn" type="button" value="&hellip;"
                       onclick="showPartVender('VENDER_NAME','VENDER_ID','false')"/>
                <INPUT class="middle_btn u-button" onclick="clearInput();" value=清除 type=button name=clrBtn>
                <input id="VENDER_ID" name="VENDER_ID" type="hidden" value="">
            </td>
          <td class="right">结算日期：</td>
            <td colspan="3">
                <input name="beginTime" id="beginTime" value="${old}" type="text" class="middle_txt" datatype="1,is_date,10"
                       group="beginTime,endTime" style="width: 90px;">
                <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间" />
                	至
                <input name="endTime" id="endTime" value="${now}" type="text" class="middle_txt" datatype="1,is_date,10"
                       group="beginTime,endTime" style="width: 90px;">
                <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间" />
            </td>
         
            </tr>
        <tr>
            <td class="center" colspan="6">
                <input name="BtnQuery" id="queryBtn" type="button" class="u-button" onclick="query();" value="查 询"/>
                <input name="dtlQueryBtn" id="dtlQueryBtn" class="u-button switch-list-btn" type="button" value="明细查询" onclick="queryDtl();" data-id="dtlQuery"/>
                &nbsp;<input class="u-button" type="button" value="增加" onclick="addInit();"/>
                &nbsp;<input name="button" type="button" class="u-button" onclick="exportOrderBalExcel('1');"
                                 value="导出"/>
        </tr>
    </table>
    
    <table class="table_query" width=100% border="0" class="center" cellpadding="1" cellspacing="1"  id="dtlQuery"
           style="display:none">
        <tr>
            <td class="right">验收单号：</td>
            <td ><input class="middle_txt" type="text" name="CHECK_CODE"/></td>
            <td class="right">入库单号：</td>
            <td ><input class="middle_txt" type="text" name="IN_CODE"/></td>
            <td class="right">结算单号：</td>
            <td ><input class="middle_txt" type="text" name="BALANCE_CODE1"/></td>
        </tr>
        <tr>

            <td class="right">库房：</td>
            <td >
                <select id="WH_ID2" name="WH_ID2" class="u-select">
                    <option value="">-请选择-</option>
                    <c:forEach items="${wareHouses}" var="wareHouse">
                        <option value="${wareHouse.whId }">${wareHouse.whName }</option>
                    </c:forEach>
                </select>
            </td>
            <td class="right">配件种类：</td>
            <td >
                <script type="text/javascript">
                    genSelBoxExp("PART_TYPE", <%=Constant.PART_BASE_PART_TYPES %>, "", true, "", "", "false", '');
                </script>
            </td>
            <td class="right">供应商：</td>
            <td >
                <input class="middle_txt" type="text" readonly="readonly" id="VENDER_NAME2" name="VENDER_NAME"/>
                <input class="mark_btn" type="button" value="&hellip;"
                       onclick="showPartVender('VENDER_NAME2','VENDER_ID2','false')"/>
                <INPUT class="middle_btn u-button" onclick="clearInput1();" value=清除 type=button name=clrBtn>
                <input id="VENDER_ID2" name="VENDER_ID2" type="hidden" value="">
            </td>
        </tr>

        <tr>
            <td  class="right">配件编码：</td>
            <td ><input name="PART_OLDCODE" type="text" class="middle_txt" id="PART_OLDCODE"/></td>
            <td  class="right">配件名称：</td>
            <td ><input name="PART_CNAME" type="text" class="middle_txt" id="PART_CNAME"/></td>
            <td class="right">配件件号：</td>
            <td ><input name="PART_CODE" type="text" class="middle_txt" id="PART_CODE"/></td>
        </tr>
       <tr>
            <td class="right">验收日期：</td>
            <td >
                <input name="chkBeginTime" id="t3" value="" type="text" class="middle_txt" datatype="1,is_date,10"
                       group="t3,t4" style="width: 90px;">
                <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间" />
                	至
                <input name="chkEndTime" id="t4" value="" type="text" class="middle_txt" datatype="1,is_date,10"
                       group="t3,t4" style="width: 90px;">
                <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间" />
            </td>
            <td class="right">入库日期：</td>
            <td colspan="2">
                <input name="inBeginTime" id="t5" value="" type="text" class="middle_txt" datatype="1,is_date,10"
                       group="t5,t6" style="width: 90px;">
                <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间" />
                &nbsp;至&nbsp;
                <input name="inEndTime" id="t6" value="" type="text" class="middle_txt" datatype="1,is_date,10"
                       group="t5,t6" style="width: 90px;">
                <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间" />
            </td>
        </tr>
        <tr>
          <td class="right">结算日期：</td>
            <td >
                <input name="balBeginTime" id="balBeginTime" value="${old}" type="text" class="middle_txt" datatype="1,is_date,10"
                       group="balBeginTime,balEndTime" style="width: 90px;">
                <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间" />
                至
                <input name="balEndTime" id="balEndTime" value="${now}" type="text" class="middle_txt" datatype="1,is_date,10"
                       group="balBeginTime,balEndTime" style="width: 90px;">
                <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间" />
            </td>
        </tr>
        <tr>
            <td class="center" colspan="6">
            <input name="BtnQuery" id="queryBtn2" type="button"  class="u-button"
                                                  onclick="__extQuery__(1);" value="查 询"/>
                <%--      &nbsp;<input name="button" type="button" class="long_btn" onclick="displayInvoice();" value="设置发票号"/>--%>
                &nbsp;<input name="button" type="button" class="u-button" onclick="exportOrderBalExcel('2');"
                             value="导出"/>
                &nbsp;<input name="button" type="button" class="u-button" onclick="myback();"
                             value="返回"/>
                             </td>
        </tr>
    </table>
    <table class="table_query" id="uploadTable" style="display: none">
        <tr>
            <td class="right" nowrap="">发票号:</td>
            <td nowrap=""><input type="text" id="INVO_NO" name="INVO_NO" class="middle_txt"/>
                <input type="button" class="normal_btn" onclick="setInvoice();" value="设置"/></td>
        </tr>
    </table>
        	</div>
</div>
    <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
    <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
</form>
</div>
</body>
</html>