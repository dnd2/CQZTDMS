<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>


<script type="text/javascript">
//获取序号
function getIdx() {
    document.write(document.getElementById("file").rows.length - 2);
}
//获取CHECKBOX
function getCb(partId) {
    document.write("<input type='checkbox' id='cell_" + (document.getElementById("file").rows.length - 3) + "' name='cb' checked onclick='countAllQty()' value='" + partId + "' />");
}

function MyAlert(info) {
    var owner = getTopWinRef();
    try {
        _dialogInit();
        var height = 200;
        if (info.split('</br>').length >= 6) {
            height = height + (info.split('</br>').length - 6) * 10;
        }
        if (info.split('</br>').length > 6) {   //如果有6个就会过长
            var infoR = "";
            var infoArr = info.split('</br>');
            for (var i = 0; i < 6; i++) {
                infoR += infoArr[i] + "</br>";
            }
            infoR += "......</br>";
            info = infoR;
        }
        owner.getElementById('dialog_content_div').innerHTML = '\
		    <div style="font-size:12px;">\
		     <div style="background-color:#08327e; padding:8px 5px 5px 10px; color:#ffffff;">\
		      <b>信息</b>\
		     </div>\
		     <div id="dialog_alert_info" style="padding:10px; line-height:2em"></div>\
		     <div style="padding:2px;text-align:center;background:#D0BFA1;">\
		      <input id="dialog_alert_button" type="button" value="确定" style="padding:2px 0px 0px 0px; background-color:#08327e;color:#FFF;font-size:12px;border-top:1px solid #DDD;border-left:1px solid #DDD;border-right:1px solid #333;border-bottom:1px solid #333;"/>\
		     </div>\
		    </div>';
        owner.getElementById('dialog_alert_info').innerHTML = info;
        owner.getElementById('dialog_alert_button').onclick = _hide;

        _setSize(300, height);

        _show();
    } catch (e) {
        MyAlert('MyAlert : ' + e.name + '=' + e.message);
    } finally {
        owner = null;
    }
}

function validateQty(obj, partId) {
    var value = obj.value;
    if (isNaN(value)) {
        MyAlert("请输入数字!");
        obj.value = "";
        countAllQty();
        return;
    }
    var re = /^[1-9]+[0-9]*$/;
    if (!re.test(obj.value)) {
        MyAlert("请输入正整数!");
        obj.value = "";
        countAllQty();
        return;
    }

    if (parseFloat(document.getElementById("saleQty_" + partId).value) > parseFloat(document.getElementById("buyQty_" + partId).value)) {
        MyAlert("提报数量不得大于订货数量!");
        obj.value = "";
        countAllQty();
        return;
    }
    countAllQty();
}

function createSalesOrderConfirm() {
    var msg = "";
    var uncheckedId = "";
    if ("" == document.getElementById("wh_id").value) {
        msg += "请选择出库仓库!</br>";
    }

    var planDate = $("planDate").value;

    if (!planDate) {
        msg += "随车最终发运日期不能为空!</br>";
    }
    if (checkSys_Sel_Date($("now").value, planDate)) {
        msg += "随车最终发运日期不能小于当前日期!</br>";
    }

    var selFlag = false;

    var cb = document.getElementsByName("cb");
    for (var i = 0; i < cb.length; i++) {
        if (cb[i].checked) {
            selFlag = true;
            //需要校验销售量是否填写
            if (document.getElementById("saleQty_" + cb[i].value).value == "") {
                msg += "请填写第" + (document.getElementById("saleQty_" + cb[i].value).parentElement.parentElement.rowIndex - 1) + "行的提报数量!</br>";
            } else {
                if (parseFloat(document.getElementById("saleQty_" + cb[i].value).value) > parseFloat(document.getElementById("buyQty_" + cb[i].value).value)) {
                    msg += "第" + (document.getElementById("saleQty_" + cb[i].value).parentElement.parentElement.rowIndex - 1) + "行的提报数量不得大于订货数量!</br>";
                }
            }

            if (document.getElementById("pkgNo_" + cb[i].value).value == "") {
                msg += "请填写第" + (document.getElementById("pkgNo_" + cb[i].value).parentElement.parentElement.rowIndex - 1) + "行的包装号!</br>";
            }

        } else {
            uncheckedId += "," + cb[i].value;
            document.getElementById("saleQty_" + cb[i].value).value = 0;
            cb[i].disabled = true;
        }
    }

    if (!selFlag) {
        msg += "请选择明细!</br>";
    }

    if (msg != "") {
        MyAlert(msg);
        enableCb();
        return;
    }
    MyConfirm("确定保存?", createSaleOrder, [uncheckedId]);
    enableCb()
}

function closeResult(jsonObj) {
    if (jsonObj != null) {
        var success = jsonObj.success;
        var error = jsonObj.error;
        var exceptions = jsonObj.Exception;
        if (success) {
            MyAlert(success);
            window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/partDlrOrderCheckInit.do?flag=true';
        } else if (error) {
            MyAlert(error);
        } else if (exceptions) {
            MyAlert(exceptions.message);
        }
    }
}
function enableCb() {
    var cb = document.getElementsByName("cb");
    for (var i = 0; i < cb.length; i++) {
        cb[i].disabled = false;
    }
}

function createSaleOrder(uncheckedId) {
    disableAllBtn();
    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartGxPlan/modGxPlan.json";
    sendAjax(url, getResult, 'fm');
}
function getResult(jsonObj) {
    enableAllBtn();
    var cb = document.getElementsByName("cb");
    for (var i = 0; i < cb.length; i++) {
        cb[i].disabled = false;
    }
    if (jsonObj != null) {
        var success = jsonObj.success;
        var error = jsonObj.error;
        var exceptions = jsonObj.Exception;
        if (success) {
            MyAlert(success);
            window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartGxPlan/partGxPlanInit.do';
        } else if (error) {
            MyAlert(error);
        } else if (exceptions) {
            MyAlert(exceptions.message);
        }
    }
}

function ckAllBox(obj) {
    var cb = document.getElementsByName("cb");
    for (var i = 0; i < cb.length; i++) {
        cb[i].checked = obj.checked;
    }
    countAllQty();
}
function disableAllBtn() {
    var inputArr = document.getElementsByTagName("input");
    for (var i = 0; i < inputArr.length; i++) {
        if (inputArr[i].type == "button") {
            inputArr[i].disabled = true;
        }
    }
}
function enableAllBtn() {
    var inputArr = document.getElementsByTagName("input");
    for (var i = 0; i < inputArr.length; i++) {
        if (inputArr[i].type == "button") {
            inputArr[i].disabled = false;
        }
    }
}


function countAllQty() {
    var allQty = parseInt(0);
    var cb = document.getElementsByName("cb");
    for (var i = 0; i < cb.length; i++) {
        if (cb[i].checked) {
            var value = document.getElementById("saleQty_" + cb[i].value).value;
            if (!value) {
                value = 0;
            }
            var dtlQty = parseInt(value);
            allQty += dtlQty;
        }
    }
    $("allQty").value = allQty;
}

function goBack() {
    window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartGxPlan/partGxPlanInit.do';
}

//获取选择框的值
function getCode(value) {
    var str = getItemValue(value);
    document.write(str);
}
function addPartDiv() {
    var partDiv = document.getElementById("partDiv");
    var addPartViv = document.getElementById("addPartViv");

    if (partDiv.style.display == "block") {
        addPartViv.value = "增加";
        partDiv.style.display = "none";
    } else {
        addPartViv.value = "收起";
        partDiv.style.display = "block";
        __extQuery__(1);
    }
}

function checkSys_Sel_Date(date1, date2) {
    var flag = "";
    if (date1 <= date2) {
        flag = false;
    } else {
        flag = true;
    }
    return flag;
}
function countMoney(obj, price, partId) {
    price = unFormatNum(price);
    //获取折扣率
    var discount = '1';
    if (discount == null || discount == "") {
        discount = 1;
    }
    var amout = 0;
    var idx = obj.parentElement.parentElement.rowIndex;
    var tbl = document.getElementById('file');
    var value = obj.value;
    if (value == '') {
        cleanData(partId, idx);
        return;
    }
    if (isNaN(value)) {
        MyAlert("请输入数字!");
        obj.value = "0";
        cleanData(partId, idx);
        return;
    }
    value = (parseFloat(value)).toFixed(0);
    obj.value = value;
    var re = /^[0-9]+[0-9]*]*$/;
    if (!re.test(obj.value)) {
        MyAlert("请输入正整数!");
        obj.value = "0";
        cleanData(partId, idx);
        return;
    }
    var money = (parseFloat(price) * parseFloat(value) * discount).toFixed(2);
    tbl.rows[idx].cells[10].innerHTML = createCellsInnerHtml(("buyAmount_" + partId), ("buyAmount_" + partId), "", "", "hidden", formatNum(money), false, false, "");
    var amountCount = parseFloat(0);
    var cb = document.getElementsByName("cb");
    for (var i = 0; i < cb.length; i++) {
        if ("" != cb[i].value && null != document.getElementById("buyAmount_" + cb[i].value) && "" != document.getElementById("buyAmount_" + cb[i].value).value) {
            //只有CHECKED的才能计算
            if (cb[i].checked) {
                amountCount = (parseFloat(amountCount) + parseFloat(unFormatNum(document.getElementById("buyAmount_" + cb[i].value).value))).toFixed(2);
            }
        }
    }
    document.getElementById("partAmount").value = formatNum(amountCount);
//    document.getElementById("Amount").value = formatNum(amountCount);
    $('Amount').value = formatNum((parseFloat(unFormatNum($('Amount').value))).toFixed(2));
    return;
}
function createCellsInnerHtml(id, name, onchangeEvent, style, type, value, trHFlag, trEFlag, hiddenHtml) {
    value = (value + "").replace(new RegExp("\"", "gm"), "&quot;");
    var tdStrHead = trHFlag == true ? '<tr><td align="center" nowrap>' : '<td align="center" nowrap>';
    var tdStrEnd = trEFlag == true ? '</td></TR>' : '</td>';
    var inputHtml = "";
    if (type == 'button') {
        inputHtml = '<input  type="button" class="short_btn"  name="queryBtn" value="删除" onclick="deleteTblRow(this);" />';
    }
    if (type == 'text') {
        onchangeEvent = onchangeEvent == '' ? '' : ('onchange="' + onchangeEvent + '"');
        inputHtml = '<input  type="text" class="short_txt" value="' + value + '" style="' + style + '"  id="' + id + '" name="' + name + '" ' + onchangeEvent + ' />';
    }
    if (type == 'hidden') {
        inputHtml = '<input  type="hidden"  id="' + id + '" name="' + name + '" value="' + value + '" />' + value;
    }
    if (type == 'checkbox') {
        inputHtml = '<input  type="checkbox" onclick="countAll()"  id="' + id + '" name="cb" checked="true" value="' + value + '" />';
    }
    if (type == 'idx') {
        inputHtml = value;
    }
    if (hiddenHtml != "") {
        inputHtml += hiddenHtml;
    }
    return tdStrHead + inputHtml + tdStrEnd;
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
function unFormatNum(str) {
    str = str + "";
    if ((str + "").indexOf(",") > -1) {
        str = str.replace(/\,/g, "");
    }
    return str;
}
</script>
</head>
<body onload="loadcalendar();doInit();enableAllBtn();countAllQty();">
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <input name="orderId" id="orderId" type="hidden" value="${mainPO.orderId}"/>
    <input name="planId" id="planId" type="hidden" value="${mainPO.planId}"/>
    <input name="allBuyQty" id="allBuyQty" type="hidden" value="${allBuyQty}"/>

    <div class="wbox">
        <div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置:

            配件管理 > 配件销售管理 &gt;广宣品发运计划 &gt;修改
        </div>
        <FIELDSET>
            <LEGEND
                    style="MozUserSelect: none; KhtmlUserSelect: none"
                    unselectable="on">
                <th colspan="6"
                    style="background-color:#DAE0EE;font-weight:normal;color:#416C9B;border-color: #859aff;padding:2px;line-height:1.5em;">
                    <img class="nav" src="<%=contextPath%>/img/subNav.gif"/> 配件发运信息
                </th>
            </LEGEND>
            <table id="queryT" class="table_query" border="0"
                   cellSpacing=1 cellPadding=1 width="100%">
                <tr>
                    <td align="right">发运计划单号:</td>
                    <td>${mainPO.planCode}</td>
                    <td align="right">制单日期:</td>
                    <td>
                        <fmt:formatDate value="${mainPO.createDate}" pattern="yyyy-MM-dd"/>
                    </td>
                    <td align="right">制单人:</td>
                    <td>${name}</td>
                </tr>
                <tr>
                    <td align="right">订货单位:</td>
                    <td>${mainPO.dealerName}</td>
                    <td align="right">出库仓库:</td>
                    <td>
                        <select name="wh_id" id="wh_id" class="short_sel">
                            <c:forEach items="${wareHouseList}" var="wareHouse">
                                <option value="${wareHouse.WH_ID}" ${wareHouse.WH_ID eq mainPO.whId?"selected":"" }>${wareHouse.WH_NAME}</option>
                            </c:forEach>
                        </select>
                        <font color="RED">*</font>
                    </td>

                    <td align="right">出库类型:</td>
                    <td>
                        <script type="text/javascript">
                            genSelBox("OUT_TYPE", <%=Constant.PART_GX_ORDER_OUT_TYPE%>, ${mainPO.outType}, true, "short_sel", "", "false", '');
                        </script>
                    </td>
                </tr>
                <tr>
                    <td align="right">随车最终发运日期:</td>
                    <td>
                        <input name="planDate" id="planDate" value="${planDate }" type="text" class="short_txt"
                               datatype="1,is_date,10" readonly>
                        <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"
                               onclick="showcalendar(event, 'planDate', false);"/>
                        <font color="RED">*</font>
                        <input type="hidden" id="now" value="${now}"/>
                    </td>
                    <td align="right"><span align="right">总金额：</span></td>
                    <td><input id="partAmount" name="partAmount" type="text" value="${mainPO.amount}" readonly
                               style="background-color: #ffff80;border:none"/>
                        <font color="blue">元</font></td>
                </tr>
                <tr>
                    <td align="right">备注说明:</td>
                    <td><textarea id="REMARK2" name="REMARK2" cols="50" rows="3">${mainPO.remark}</textarea></td>
                </tr>
            </table>

        </FIELDSET>

        <table id="file" class="table_list" border="0"
               style="BORDER-RIGHT: #859aff 1px solid; BORDER-TOP:#859aff 1px solid; BORDER-LEFT:#859aff 1px solid; BORDER-BOTTOM:#859aff 1px solid;border-color: #859aff;"
               cellSpacing=1 cellPadding=1 width="100%">
            <tr>
                <th colspan="14" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif"/>配件信息 <span
                        class="checked"
                        style="text-align:left"/>
                </th>
            </tr>
            <tr bgcolor="#FFFFCC">

                <td><input type="checkbox" checked onclick="ckAllBox(this)" id="ckAll" name="ckAll"/></td>
                <td>序号</td>
                <td>配件编码</td>
                <td>配件名称</td>
                <td>件号</td>
                <td>最小包装量</td>
                <td>单位</td>
                <td>订货数量</td>
                <td>提报数量<font color="RED">*</font></td>
                <td>单价<font color="RED">*</font></td>
                <td>金额<font color="RED">*</font></td>
                <td>包装号<font color="RED">*</font></td>
                <td>备注</td>
            </tr>
            <c:forEach items="${detailList}" var="data">
                <tr class="table_list_row1">
                    <td align="center">
                        <script type="text/javascript">
                            getCb(${data.partId});
                        </script>
                        <input type="hidden" name="dlineId_${data.partId}" value="${data.dlineId }"/>
                    </td>
                    <td align="center">&nbsp;
                        <script type="text/javascript">
                            getIdx();
                        </script>
                    </td>
                    <td align="center">
                        <c:out value="${data.partOldcode}"/>
                        <input id="partOldCode_${data.partId}" name="partOldCode_${data.partId}" type="hidden"
                               value="${data.partOldcode}"/>
                    </td>
                    <td align="center">
                        <c:out value="${data.partCname}"/>
                        <input id="partCname_${data.partId}" name="partCname_${data.partId}" type="hidden"
                               value="${data.partCname}"/>
                    </td>
                    <td align="center">
                        <c:out value="${data.partCode}"/>
                        <input id="partCode_${data.partId}" name="partCode_${data.partId}" type="hidden"
                               value="${data.partCode}"/>
                    </td>
                    <td>&nbsp;<c:out value="${data.minPackage}"/><input id="minPackage_${data.partId}"
                                                                        name="minPackage_${data.partId}" type="hidden"
                                                                        value="${data.minPackage}"/></td>
                    <td>&nbsp;<c:out value="${data.unit}"/>
                        <input id="unit_${data.partId}" name="unit_${data.partId}" type="hidden" value="${data.unit}"/>
                    </td>

                    <td>&nbsp;<input id="buyQty_${data.partId}" name="buyQty_${data.partId}" readonly
                                     style="width:90px;border:0px;background-color:#F3F4F8;text-align:center;"
                                     type="text"
                                     value="${data.buyQty}"/></td>

                    <td>&nbsp;
                        <input id="saleQty_${data.partId}" name="saleQty_${data.partId}" type="text" class="short_txt"
                               value="${data.reportQty}" style="background-color: #ffff80;text-align:center"
                               onchange="countMoney(this,${data.buyPrice},${data.partId})">
                        <input type="hidden" name="reportQty_${data.partId}" value="${data.reportQty}"/>
                    </td>
                    <td>
                        <input id="buyPrice_${data.partId}" name="buyPrice_${data.partId}" type="text" class="short_txt"
                               readonly style="border: none"
                               value="${data.buyPrice}">
                    </td>
                    <td>
                        <input id="buyAmount_${data.partId}" name="buyAmount_${data.partId}" type="text"
                               class="short_txt" readonly style="border: none"
                               value="${data.buyAmount}">
                    </td>
                    <td>&nbsp;<input id="pkgNo_${data.partId}" name="pkgNo_${data.partId}" type="text" class="short_txt"
                                     value="${data.pkgNo }" style="background-color: #ffff80;text-align:center"></td>
                    <td>&nbsp;<input id="remark_${data.partId}" name="remark_${data.partId}" type="text"
                                     class="normal_txt"
                                     value="${data.remark }" style="background-color: #ffff80;text-align:center"></td>
                </tr>
            </c:forEach>
        </table>

        <table border="0" class="table_query">

            <tr align="center">
                <td><font style="font-weight: bold;">合计:</font>
                    <input id="allQty" type="text"
                           style="width:90px;border:0px;background-color:#F3F4F8;text-align:center;" value="" readonly/>
                </td>
            </tr>
            <tr align="center">
                <td>&nbsp;
                    <input class="long_btn" type="button" value="保存" onclick="createSalesOrderConfirm();"/>
                    &nbsp;&nbsp;
                    <input class="normal_btn" type="button" value="返 回" name="back" id="back" onclick="goBack()"/></td>
            </tr>
        </table>
    </div>
</form>
</body>
</html>