<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
    <title></title>
</head>
<script language="javascript">

//初始化查询TABLE
var myPage;
var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartSalesOrderFinCheck/partSalesOrderFinCheckQuery.json";
var title = null;
var columns = [

    {header: "序号", align: 'center', renderer: getIndex},
    {header: "<input type='checkBox' id='ckAll' name='ckAll' onclick='checkAll(this)'>", align: 'center', renderer: getCb},
    /*{id: 'action', header: "操作", sortable: false, dataIndex: 'SO_ID', renderer: myLink, align: 'center'},*/
    {header: "销售单号", dataIndex: 'SO_CODE', align: 'center',renderer:viewSoOrder},
    {header: "订单类型", dataIndex: 'ORDER_TYPE', align: 'center', renderer: getItemValue},
    {header: "订单号", dataIndex: 'ORDER_CODE', align: 'center',renderer:viewOrder},
    {header: "订货单位编码", dataIndex: 'DEALER_CODE2', align: 'center'},
    {header: "订货单位", dataIndex: 'DEALER_NAME', style: 'text-align:left;'},
    {header: "制单人", dataIndex: 'CREATE_BY_NAME', align: 'center'},
    {header: "销售日期", dataIndex: 'CREATE_DATE', align: 'center'},
    {header: "销售单位", dataIndex: 'SELLER_NAME', align: 'center'},
    {header: "销售金额", dataIndex: 'AMOUNT', align: 'center', style: 'text-align:right'},
    /*{header: "可用金额", dataIndex: 'ACCOUNT_KY', align:'center'},*/
    {header: "出库仓库", dataIndex: 'WH_NAME', align: 'center'},
    {header: "业务提交时间", dataIndex: 'SUBMIT_DATE', align: 'center'},
    /*{header: "财务审核时间", dataIndex: 'FCAUDIT_DATE', align:'center'},*/
    {header: "订单状态", dataIndex: 'STATE', align: 'center', renderer: getItemValue}

];

function getSoCode(value,meta,record)
{
	var isNbdw = record.data.IS_NBDW;
	if('1' == isNbdw)
	{
		return String.format("<span style='background-color: #FAA095; width: 100%;'>"+ value +"</span>");
	}
	else
	{
		return String.format("<span>"+ value +"</span>");
	}
}

function myLink(value, meta, record) {
    return String.format("<a href=\"#\" onclick='detailOrder(\"" + value + "\",\"" + record.data.SO_CODE + "\")'>[查看]</a>");
}
function getCb(value, meta, record) {
    return String.format("<input type='checkBox' id='cb' name='cb' value='" + record.data.SO_ID + "' onclick='clickCheckBox()'>");
}
function countMoney() {
    var tb = document.getElementById("myTable");
    var amount = parseFloat(0);
    for (var i = 1; i < tb.rows.length; i++) {
        if (tb.rows[i].cells[1].firstChild.checked) {
            amount = (parseFloat(amount) + parseFloat(unFormatNum(tb.rows[i].cells[10].innerText))).toFixed(2);
        }
    }
    document.getElementById("saleAmount").value = formatNum(amount);
}
function unFormatNum(str) {
    str = str + "";
    if ((str + "").indexOf(",") > -1) {
        str = str.replace(/\,/g, "");
    }
    return str;
}
function formatNum(str) {
    var len = str.length;
    var step = 3;
    var splitor = ",";
    var decPart = ".";
    if ((str + "").indexOf(".") > -1) {
        var strArr = str.split(".");
        str = strArr[0];
        decPart += strArr[1];
    }
    if (len > step) {
        var l1 = len % step, l2 = parseInt(len / step), arr = [], first = str.substr(0, l1);
        if (first != '') {
            arr.push(first);
        }

        for (var i = 0; i < l2; i++) {
            arr.push(str.substr(l1 + i * step, step));
        }

        str = arr.join(splitor);
        str = str.substr(0, str.length - 1);
    }

    if (decPart != ".") {
        str += decPart;
    }

    return str;
}
//查看
function detailOrder(value, soCode) {
    window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartSalesOrderFinCheck/detailOrder.do?soId=" + value + "&&soCode=" + soCode;
}
function getResult(jsonObj) {
    if (jsonObj != null) {
        var success = jsonObj.success;
        var error = jsonObj.error;
        var exceptions = jsonObj.Exception;
        if (success) {
            MyAlert(success);
            __extQuery__(1);
        } else if (error) {
            MyAlert(error);
        } else if (exceptions) {
            MyAlert(exceptions.message);
        }
    }
}


function doQuery() {
    var msg = "";
    if ("" != document.getElementById("CendDate").value) {
        if ("" == document.getElementById("CstartDate").value) {
            msg += "请选择提交开始时间!</br>";
        }
    }

    if ("" != document.getElementById("CstartDate").value) {
        if ("" == document.getElementById("CendDate").value) {
            msg += "请选择提交结束时间!</br>";
        }
    }

    if (msg != "") {
        MyAlert(msg);
        return;
    }

    __extQuery__(1);

}

function checkAll(obj) {
    var cb = document.getElementsByName("cb");
    for (var i = 0; i < cb.length; i++) {
        cb[i].checked = obj.checked;
    }
    countMoney();
}
function clickCheckBox() {
    var flag = true;
    var cb = document.getElementsByName("cb");
    for (var i = 0; i < cb.length; i++) {
        if (!cb[i].checked) {
            flag = false;
        }
    }
    document.getElementById("ckAll").checked = flag;
    countMoney();
}
//财务审核确认
function finCheckConfirm(actionCode) {
    var flag = false;
    var cb = document.getElementsByName("cb");
    for (var i = 0; i < cb.length; i++) {
        if (cb[i].checked) {
            flag = true;
        }
    }
    if (!flag) {
        MyAlert("请选择一条记录!");
        return;
    }
    var msg = "";
    if (actionCode == "1") {
        msg = "确认通过?";
    }
    if (actionCode == "2") {
        if ($("textarea1").value == "") {
            MyAlert("请录入驳回原因!");
            return;
        }
        msg = "确认驳回?";
    }
    MyConfirm(msg, finCheck, [actionCode]);
}
//财务审核
function finCheck(actionCode) {
    if (actionCode == "1") {
        var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartSalesOrderFinCheck/partSalesOrderFinCheckActions.json";
        document.getElementById("state").value =<%=Constant.CAR_FACTORY_ORDER_CHECK_STATE_05%>;
    }
    if (actionCode == "2") {
        var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartSalesOrderFinCheck/partSalesOrderFinCheckActions.json";
        document.getElementById("state").value =<%=Constant.CAR_FACTORY_ORDER_CHECK_STATE_06%>;
    }

    diableCheckBox(1);
    diableBtn(1);
    sendAjax(url, getResult, 'fm');

}
function getResult(jsonObj) {

    if (jsonObj != null) {
        var success = jsonObj.success;
        var error = jsonObj.error;
        var exceptions = jsonObj.Exception;
        if (success) {
            MyAlert("操作成功!");
            __extQuery__(1);
            diableBtn(2);
            diableCheckBox(2);
            $('saleAmount').value = 0;
            $('textarea').value = "";
        } else if (error) {
            MyAlert(error);
        } else if (exceptions) {
            MyAlert(exceptions.message);
        }
    }
}
function diableCheckBox(code) {
    if (code == 1) {
        var cb = document.getElementsByName("cb");
        for (var i = 0; i < cb.length; i++) {
            if (!cb[i].checked) {
                cb[i].disabled = true;
            }
        }
    }
    if (code == 2) {
        var cb = document.getElementsByName("cb");
        for (var i = 0; i < cb.length; i++) {
            cb[i].disabled = false;
        }
    }
}
function diableBtn(code) {
    var inputArr = document.getElementsByTagName("input");
    if (code == 1) {

        for (var i = 0; i < inputArr.length; i++) {
            if (inputArr[i].type == "button") {
                inputArr[i].disabled = true;
            }
        }
    }
    if (code == 2) {
        for (var i = 0; i < inputArr.length; i++) {
            if (inputArr[i].type == "button") {
                inputArr[i].disabled = false;
            }
        }
    }
}
function exportExel() {
    fm.action = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartSalesOrderFinCheck/exportSoOrderExcel.do?";
    fm.submit();
}

function changeClick() {
    var state=$("orderstate").value;
    __extQuery__(1);
    if(state==<%=Constant.CAR_FACTORY_ORDER_CHECK_STATE_05%>){
        $("actiondiv").innerHTML="<input class=\"normal_btn\" type=\"button\" value=\"驳 回\" onclick=\"finCheckConfirm(2);\"/>";
    }
    if(state==<%=Constant.CAR_FACTORY_ORDER_CHECK_STATE_02%>){
        $("actiondiv").innerHTML="<input class=\"normal_btn\" type=\"button\" value=\"通 过\" onclick=\"finCheckConfirm(1);\"/>&nbsp;<input class=\"normal_btn\" type=\"button\" value=\"驳 回\" onclick=\"finCheckConfirm(2);\"/>";
    }    
}
function viewSoOrder(value,meta,record){
    var soId= record.data.SO_ID;
    var soCode = record.data.SO_CODE;
    if(soCode != null){
        return String.format("<a href=\"#\" title='查看销售单明细' onclick='detailOrder(\"" + soId + "\",\"" + soCode + "\")' >"+soCode+"</a>");
    }else{
        return String.format("");
    }
}
function detailOrder(value, code) {
    //disableAllClEl();
    //window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartSoManage/detailOrder.do?soId=" + value + "&&soCode=" + code;
    var buttonFalg = "disabled";
    OpenHtmlWindow("<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartSoManage/detailOrder.do?soId=" + value + "&&soCode=" + code+"&buttonFalg="+buttonFalg,800,400);
}
function viewOrder(value,meta,record){
    var ORDER_ID= record.data.ORDER_ID;
    var ORDER_CODE = record.data.ORDER_CODE;
    if(ORDER_CODE != null){
        return String.format("<a href=\"#\" title='查看订单明细' onclick='viewOrderDtl(\"" + ORDER_ID + "\",\"" + ORDER_CODE + "\")' >"+ORDER_CODE+"</a>");
    }else{
        return String.format("");
    }
}
function viewOrderDtl(ORDER_ID,ORDER_CODE) {
    var buttonFalg = "disabled";//用于判断跳转页面是返回还是关闭
    OpenHtmlWindow('<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/detailDlrOrder.do?orderId=' + ORDER_ID + '&orderCode=' + ORDER_CODE + '&buttonFalg=' + buttonFalg, 800, 440);
}
</script>
</head>
<body onload="loadcalendar();diableBtn(2);diableCheckBox(2);autoAlertException();__extQuery__(1);">
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <input type="hidden" name="state" id="state"/>

    <div class="wbox">
        <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" alt=""/>&nbsp;当前位置: 配件管理 &gt; 配件销售管理 &gt;
            财务审核查询
        </div>
        <table border="0" class="table_query">
            <th colspan="6" width="100%"><img class="nav" src="<%=contextPath%>/img/subNav.gif"/> 查询条件</th>
            <tr>
                <td width="10%" align="right">订单号:</td>
                <td width="20%"><input class="middle_txt" name="orderCode" id="orderCode" type="text"/></td>
                <td width="10%" align="right">订货单位编码:</td>
                <td width="20%"><input class="long_txt" id="buyerCode" name="buyerCode" type="text"/></td>
                <td width="10%" align="right">订货单位:</td>
                <td width="20%"><input class="middle_txt" id="buyerName" name="buyerName" type="text"/></td>
            </tr>
            <tr>
                <td width="10%" align="right">销售单号:</td>
                <td width="20%"><input class="middle_txt" id="salesOrderId" name="salesOrderId" type="text"/></td>
                <td width="10%" align="right">提交日期:</td>
                <td width="20%">
                <input id="CstartDate" class="short_txt"
				name="CstartDate" datatype="1,is_date,10" maxlength="10"
				group="CstartDate,CendDate" value="${old}"/> <input class="time_ico"
				onclick="showcalendar(event, 'CstartDate', false);" value=" "
				type="button" />至 <input id="CendDate"
				class="short_txt" name="CendDate" datatype="1,is_date,10"
				maxlength="10" group="CstartDate,CendDate" value="${now}"/> <input
				class="time_ico" onclick="showcalendar(event, 'CendDate', false);"
				value=" " type="button" />
               </td>
	           <td width="10%" align="right">订单状态:</td>
	           <td width="20%" align="left">
		        <select name="orderstate" id="orderstate" onchange="changeClick();" class="short_sel">
                <option value="<%=Constant.CAR_FACTORY_ORDER_CHECK_STATE_02%>" selected>已提交</option>
	            <option value="<%=Constant.CAR_FACTORY_ORDER_CHECK_STATE_05%>" >财务审核通过</option>
	            </select>
	           </td>                            
            </tr>
            <tr>
                <td width="10%" align="right" style="display: none">是否内部单位：</td>
				<td width="20%" style="display: none">
				  <select name="isNbdw" id="isNbdw" class="short_sel" onchange="updatePage()">
					<option value="">-请选择-</option>
					<option value="1">是</option>
					<option value="0" selected="selected">否</option>
				  </select>
				</td>
				<td width="10%" align="right"></td>
				<td width="20%">
				</td>
				<td width="10%" align="right"></td>
				<td width="20%">
				</td>
            </tr>
            <tr>
                <td colspan="6" align="center"><input class="normal_btn" type="button" value="查 询"
                                                      onclick="doQuery();"/>
                   <%-- &nbsp;
                    <input type="button" class="normal_btn" value="导 出" onclick="exportExel()"/></td>--%>
            </tr>
        </table>
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
        <table border="0" class="table_query">
            <tr align="left">
                <td width=60%>备注：
                    <textarea id="textarea1" name="textarea1" cols="50" rows="2" class="tb_list"></textarea>
                    所选记录价格汇总为：
                    <input name="saleAmount" type="text" class="short_txt" id="saleAmount" style="background-color:#FF9"
                           value="0" readonly="readonly"/>
                 </td>
                 <td width=40% id="actiondiv">
                    <input class="normal_btn" type="button" value="通 过" onclick="finCheckConfirm(1);"/>
                    <input class="normal_btn" type="button" value="驳 回" onclick="finCheckConfirm(2);"/>
                 </td>
            </tr>
        </table>
    </div>
</form>
</body>
</html>