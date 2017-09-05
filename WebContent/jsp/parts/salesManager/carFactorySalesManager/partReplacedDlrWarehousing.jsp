<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<script type="text/javascript" src="<%=contextPath%>/js/web/jquery-1.8.0.min.js"></script>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
    <script type="text/javascript">
        var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/getGift.json";
        var title = null;
        var columns = [
            {header: "序号", align: 'center', renderer: getIndex},
            {
                header: "<input type='checkbox'  name='ckbAll' id='ckbAll' onclick='selAll(this," + '"' + "ck" + '"' + ")'  />",
                dataIndex: 'PART_ID',
                align: 'center',
                width: '33px',
                renderer: seled
            },
            {header: "配件编码", dataIndex: 'PART_OLDCODE', align: 'center'},
            {header: "配件名称", dataIndex: 'PART_CNAME', align: 'center'},
            {header: "件号", dataIndex: 'PART_CODE', align: 'center'},
            {header: "单位", dataIndex: 'UNIT', align: 'center'},
            {header: "最小包装量", dataIndex: 'MIN_PACKAGE', align: 'center'},
            {header: "赠送数量", align: 'center', renderer: getSaleText, dataIndex: 'PART_ID'}
        ];
        function getSaleText(value) {
            return "<input name='partSalesQty_" + value + "' id='partSalesQty_" + value + "' type='text' value='' onchange='onchangeVlidateSaleQty(this)' style='background-color:#FFFFCC;text-align:center' class='short_txt'   >";
        }
        //为下面TABLE生成数据
        function addCells() {
            var ck = document.getElementsByName('ck');
            var mt = document.getElementById("myTable");
            for (var i = 1; i < mt.rows.length; i++) {
                var partId = mt.rows[i].cells[1].firstChild.value;  //ID
                if (mt.rows[i].cells[1].firstChild.checked) {
                    if (validateCell(partId)) {
                        var partOldcode = mt.rows[i].cells[2].innerText;  //配件编码
                        var partCname = mt.rows[i].cells[3].innerText;  //配件名称
                        var partCode = mt.rows[i].cells[4].innerText;  //件号
                        var unit = mt.rows[i].cells[5].innerText;  //单位
                        var minPkg = mt.rows[i].cells[6].innerText;
                        var giftQty = mt.rows[i].cells[7].firstChild.value;
                        addCell(partId, partCode, partOldcode, partCname, unit, minPkg, giftQty);
                    } else {
                        MyAlert("配件：" + mt.rows[i].cells[3].innerText + "  已存在!</br>");
                        break;
                    }
                }
            }
            getPartQty();
        }
        function validateFreight(obj) {
            if (isNaN(obj.value)) {
                MyAlert("请输入数字!");
                countAll();
                return;
            }
            if (parseFloat(obj.value) < parseFloat(0)) {
                MyAlert("请输入正数!");
                countAll();
                return;
            }
            obj.value = parseFloat(obj.value).toFixed(2);
            countAllWithoutFreight();
            //$('Amount').value =formatNum((parseFloat(unFormatNum($('Amount').value))+ parseFloat(unFormatNum(obj.value))).toFixed(2));
        }

        function countAllWithoutFreight() {

            var flag = true;
            var amountCount = parseFloat(0);
            var cb = document.getElementsByName("cb");
            for (var i = 0; i < cb.length; i++) {
                if ("" != cb[i].value && null != document.getElementById("buyAmount_" + cb[i].value) && "" != document.getElementById("buyAmount_" + cb[i].value).value) {
                    //只有CHECKED的才能计算
                    if (cb[i].checked) {
                        amountCount = (parseFloat(unFormatNum(amountCount)) + parseFloat(unFormatNum(document.getElementById("buyAmount_" + cb[i].value).value))).toFixed(2);
                    }
                }
            }
            $('Amount').value = (parseFloat(unFormatNum(amountCount)) + parseFloat($('freight').value)).toFixed(2);
            $('orderA').value = (parseFloat(unFormatNum(amountCount)) ).toFixed(2);
        }
        //校验是否重复生成
        function validateCell(value) {
            var flag = true;
            var tbl = document.getElementById('file');
            var cb = document.getElementsByName('cb');
            for (var i = 0; i < cb.length; i++) {
                if (value == cb[i].value) {
                    flag = false;
                    break;
                }
            }
            return flag;
        }

        function codeChoice(index) {
            var v = document.getElementById("whId").value.split(",");
            var whId = v[0];
            OpenHtmlWindow(g_webAppName + "/parts/storageManager/partDistributeMgr/PartDistributeMgr/selectLocationInit.do?loc_id=" + index + "&whId=" + whId, 700, 400);
        }
        function checkCode(th, partId) {
            var loc_code = th.value;
            var v = document.getElementById("whId").value.split(",");
            var whId = v[0];
            var whName = v[1];
            var url2 = "<%=contextPath%>/parts/storageManager/partDistributeMgr/PartDistributeMgr/checkSeatExist.json";
            var para = "LOC_CODE=" + loc_code + "&PART_ID=" + partId + "&whId=" + whId + "&whName=" + whName;
            makeCall(url2, forBack3, para);
        }
        function forBack3(json) {
            if (json.returnValue != 1) {
                var partId = json.PART_ID;
                if (partId != "") {
                    document.getElementById("LOC_CODE_T_" + partId).value = "";
                }
                parent.MyAlert("该货位编码不存在！");
            } else {
                LOC_ID = json.PART_ID;
                codeSet(json.LOC_ID, json.LOC_CODE, json.LOC_CODE);
            }
        }
        function addCell(partId, partCode, partOldcode, partCname, unit, minPackage, giftQty) {

            var tbl = document.getElementById('file');
            var rowObj = tbl.insertRow(tbl.rows.length);
            rowObj.className = "table_list_row1";
            var html1 = '<input id="stockQty_' + partId + '" name="stockQty_' + partId + '" readonly style="width:90px;border:0px;background-color:#F3F4F8;text-align:center;" type="text" value="0" /> ';
            var html2 = '<input id="buyQty_' + partId + '" name="buyQty_' + partId + '" readonly style="width:90px;border:0px;background-color:#F3F4F8;text-align:center;" type="text" value="0" /> ';
            var html3 = '<input id="saleQty_' + partId + '" name="saleQty_' + partId + '"  style="background-color: #ffff80;text-align:center;" class="short_txt" type="text" value="' + giftQty + '" /> ';

            var html4 = ' 0';
            var html5 = '<input id="buyAmount_' + partId + '" name="buyAmount_' + partId + '" readonly style="width:90px;border:0px;background-color:#F3F4F8;text-align:center;" type="text" value="0" /> ';
            var html6 = '<input id="gift" name="gift" readonly type="hidden" value="' + partId + '" /> ';
            createCells(rowObj, 0, ("cell_" + (tbl.rows.length - 2)), "cb", "", "", "checkbox", partId, true, false, "");
            createCells(rowObj, 1, "", "", "", "", "idx", " " + (tbl.rows.length - 2), false, false, "");
            createCells(rowObj, 2, ("partOldcode_" + partId), ("partOldcode_" + partId), "", "", "hidden", partOldcode, false, false, "");
            createCells(rowObj, 3, ("partCname_" + partId), ("partCname_" + partId), "", "", "hidden", partCname, false, false, "");
            createCells(rowObj, 4, ("partCode_" + partId), ("partCode_" + partId), "", "", "hidden", partCode, false, false, "");
            createCells(rowObj, 5, ("minPackage_" + partId), ("minPackage_" + partId), "", "", "hidden", minPackage, false, false, "");
            createCells(rowObj, 6, ("unit_" + partId), ("unit_" + partId), "", "", "hidden", unit, false, false, "");
            createCells(rowObj, 7, "", "", "", "", "", "", false, false, html1);
            createCells(rowObj, 8, "", "", "", "", "", "", false, false, html2);
            createCells(rowObj, 9, "", "", "", "", "", "", false, false, html3);
            createCells(rowObj, 10, "", "", "", "", "", "", false, false, html4);
            createCells(rowObj, 11, "", "", "", "", "", "", false, false, html5);
            createCells(rowObj, 12, "", "", "", "", "button", "", false, true, html6);

        }
        function deleteTblRow(obj) {
            var idx = obj.parentElement.parentElement.rowIndex;
            var tbl = document.getElementById('file');
            tbl.deleteRow(idx);
            reNumIdx();
        }
        function reNumIdx() {
            var tbl = document.getElementById("file");
            for (var i = 2; i < tbl.rows.length; i++) {
                tbl.rows[i].cells[1].innerHTML = (i - 1);
            }
        }
        //生成CELL
        function createCells(obj, idx, id, name, onchangeEvent, style, type, value, trHFlag, trEFlag, hiddenHtml) {
            var cell = obj.insertCell(idx);
            cell.innerHTML = createCellsInnerHtml(id, name, onchangeEvent, style, type, value, trHFlag, trEFlag, hiddenHtml);

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
        function onchangeVlidateSaleQty(obj) {
            if (obj.value == "") {
                return;
            }
            if (isNaN(obj.value)) {
                MyAlert("请输入数字!");
                obj.value = "0";
                return;
            }
            var re = /^[0-9]+[0-9]*]*$/;
            if (!re.test(obj.value)) {
                MyAlert("请输入正整数!");
                obj.value = "0";
                return;
            }

            var idx = obj.parentElement.parentElement.rowIndex;
            var tbl = document.getElementById('myTable');

            if (obj.value != '0') {
                tbl.rows[idx].cells[1].firstChild.checked = true;
            } else {
                tbl.rows[idx].cells[1].firstChild.checked = false;
            }
        }
        function openDiv() {
            var orderDiv = document.getElementById("orderDiv");
            var qt = document.getElementById("queryT");

            if (qt.style.display == "block") {
                orderDiv.value = "打开";
                qt.style.display = "none";
            } else {
                orderDiv.value = "收起";
                qt.style.display = "block";
            }
        }
        function selAll(obj, selObjId) {
            var cb = document.getElementsByName(selObjId);
            for (var i = 0; i < cb.length; i++) {
                if (cb[i].disabled) {
                    continue;
                }
                cb[i].checked = obj.checked;
            }
        }
        function seled(value, meta, record) {
            return "<input type='checkbox' value='" + value + "' name='ck' onclick='cPartCb()' id='ck' />";
        }
        //选择经销商(1) 接收单位(2)地址(3)
        function selSales(inputId, inputCode, inputName, inputLinkMan, inputTel, inputPostCode, inputStation, dealerId, type) {
            if (!inputId) {
                inputId = null;
            }
            if (!inputCode) {
                inputCode = null;
            }
            if (!inputName) {
                inputName = null;
            }
            if (!inputLinkMan) {
                inputLinkMan = null;
            }
            if (!inputTel) {
                inputTel = null;
            }
            if (!inputPostCode) {
                inputPostCode = null;
            }
            if (!inputStation) {
                inputStation = null;
            }
            if (!dealerId) {
                dealerId = null;
            }
            if (!type) {
                type = null;
            }
            OpenHtmlWindow(g_webAppName + '/jsp/parts/salesManager/carFactorySalesManager/selSales.jsp?dealerId=' + dealerId + '&type=' + type + '&inputName=' + inputName + '&inputId=' + inputId + '&inputCode=' + inputCode + '&inputLinkMan=' + inputLinkMan + '&inputTel=' + inputTel + '&inputPostCode=' + inputPostCode + '&inputStation=' + inputStation, 730, 390);
        }
        //获取序号
        function getIdx() {
            document.write(document.getElementById("file").rows.length - 2);
        }
        //获取CHECKBOX
        function getCb(partId) {
            document.write("<input type='checkbox' id='cell_" + (document.getElementById("file").rows.length - 3) + "' name='cb' checked onclick='countAll()' value='" + partId + "' />");
        }
        //计算所有CHECK的金额1
        function countAll() {
            var flag = true;
            var amountCount = parseFloat(0);
            var cb = document.getElementsByName("cb");
            for (var i = 0; i < cb.length; i++) {
                if ("" != cb[i].value && null != document.getElementById("buyAmount_" + cb[i].value) && "" != document.getElementById("buyAmount_" + cb[i].value).value) {
                    //只有CHECKED的才能计算
                    if (cb[i].checked) {
                        amountCount = (parseFloat(unFormatNum(amountCount)) + parseFloat(unFormatNum(document.getElementById("buyAmount_" + cb[i].value).value))).toFixed(2);
                    }
                }
                if (!cb[i].checked) {
                    flag = false;
                }
            }
            document.getElementById("ckAll").checked = flag;
            document.getElementById("partAmount").value = formatNum(amountCount);
            document.getElementById("Amount").value = formatNum(amountCount);
            $('orderA').value = formatNum(amountCount);
            if ($("isLock").value == "1") {
                $('freight').value = $('lockFreight').value;
                $('Amount').value = formatNum((parseFloat(unFormatNum(amountCount)) + parseFloat(unFormatNum($('lockFreight').value)) ).toFixed(2));
                $('orderA').value = formatNum((parseFloat(unFormatNum(amountCount))).toFixed(2));
            } else {
                getFreight(amountCount);
            }
        }
        function getFreight(amountCount) {
            if ($('SELLER_ID').value ==<%=Constant.OEM_ACTIVITIES%>) {
                //disableAllBtn();
                /* if ($('SELLER_ID').value !=
                <%=Constant.OEM_ACTIVITIES%>) {
                 enableAllBtn();
                 $('freight').value = "0.00";
                 return;
                 }*/
                if ($('TRANS_TYPE').value == "3") {
                    enableAllBtn();
                    $('freight').value = "0.00";
                    countAllWithoutFreight();
                    return;
                }
                if ($('transpayType').value == <%=Constant.CAR_FACTORY_ORDER_CHECK_PAY_TRANS_01%>) {
                    $('freight').value = "0.00";
                    countAllWithoutFreight();
                    return;
                }
                if ($('transpayType').value != <%=Constant.CAR_FACTORY_ORDER_CHECK_PAY_TRANS_01%> && ${mainMap.IS_TRANSFREE} ==<%=Constant.IF_TYPE_YES%>) {
                    MyAlert("此单免运费!");
                    $('transpayType').value = <%=Constant.CAR_FACTORY_ORDER_CHECK_PAY_TRANS_01%>;
                    $('freight').value = "0.00";
                    countAllWithoutFreight();
                    return;
                }
                var getFreightUrl = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/getFreight.json?money=" + amountCount;
                sendAjax(getFreightUrl, getFreightResult, 'fm');
            } else {
                $('Amount').value = formatNum((parseFloat(unFormatNum($('Amount').value)) + parseFloat(unFormatNum($('freight').value))).toFixed(2));
                ;
                return;
            }
        }
        function getFreightResult(jsonObj) {
            enableAllBtn();
            if (jsonObj != null) {
                $('freight').value = jsonObj.freight;
                $('Amount').value = formatNum((parseFloat(unFormatNum(jsonObj.amountCount)) + parseFloat(unFormatNum(jsonObj.freight)) ).toFixed(2));
                $('orderA').value = formatNum((parseFloat(unFormatNum(jsonObj.amountCount))).toFixed(2));
            }
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
        function countMoney(obj, price, partId) {
            price = unFormatNum(price);
            //校验库存
            var stockQty = document.getElementById("stockQty_" + partId).value;
            if (parseFloat(stockQty) < parseFloat(obj.value) && (document.getElementById('ORDER_TYPE').value !=<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04%>)) {
                MyAlert("销售数量不得大于库存数量!");
                obj.value = "0";
                countMoney(obj, price, partId);
                return;
            }
            //获取折扣率
            var discount = document.getElementById("DISCOUNT").value;
            if (discount == null || discount == "") {
                discount = 1;
            }
            var idx = obj.parentElement.parentElement.rowIndex;
            var tbl = document.getElementById('file');
            var value = obj.value;
            if (value == '') {
                obj.value = "0";
                countMoney(obj, price, partId)
                return;
            }
            if (isNaN(value)) {
                MyAlert("请输入数字!");
                obj.value = "0";
                countMoney(obj, price, partId)
                return;
            }
            var re = /^[0-9]+[0-9]*]*$/;
            if (!re.test(obj.value)) {
                MyAlert("请输入正整数!");
                obj.value = "0";
                countMoney(obj, price, partId)
                return;
            }

            if (parseFloat(document.getElementById("saleQty_" + partId).value) > parseFloat(document.getElementById("buyQty_" + partId).value)) {
                MyAlert("销售数量不得大于订货数量!");
                obj.value = "0";
                countMoney(obj, price, partId);
                return;
            }

            var money = (parseFloat(price) * parseFloat(value) * discount).toFixed(2);
            if ($("orderType").value ==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04%>) {
                tbl.rows[idx].cells[12].innerHTML = createCellsInnerHtml(("buyAmount_" + partId), ("buyAmount_" + partId), "", "", "hidden", formatNum(money), false, false, "");
            } else {
                tbl.rows[idx].cells[11].innerHTML = createCellsInnerHtml(("buyAmount_" + partId), ("buyAmount_" + partId), "", "", "hidden", formatNum(money), false, false, "");
            }
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
            document.getElementById("Amount").value = formatNum(amountCount);
            $('orderA').value = formatNum(amountCount);
            //getFreight(amountCount);数据量大时速度太慢
        }
        //生成CELL中的HTML
        function createCellsInnerHtml(id, name, onchangeEvent, style, type, value, trHFlag, trEFlag, hiddenHtml) {
            var tdStrHead = trHFlag ? '<tr><td align="center" nowrap>' : '<td align="center" nowrap>';
            var tdStrEnd = trEFlag ? '</td></TR>' : '</td>';
            var inputHtml = "";
            if (type == 'button') {
                inputHtml = '<input  type="button" class="short_btn"  name="queryBtn" value="删除" onclick="deleteTblRow(this);" />';
            }
            if (type == 'text') {
                onchangeEvent = onchangeEvent == '' ? '' : ('onchange="' + onchangeEvent + '"');
                inputHtml = '<input  type="text" class="short_txt"  id="' + id + '" name="' + name + '" ' + onchangeEvent + ' />';
            }
            if (type == 'hidden') {
                inputHtml = '<input  type="hidden"  id="' + id + '" name="' + name + '" value="' + value + '" />' + value;
            }
            if (type == 'checkbox') {
                inputHtml = '<input  type="checkbox"   id="' + id + '" name="cb" checked="true" value="' + value + '" />';
            }
            if (type == 'idx') {
                inputHtml = value;
            }
            if (hiddenHtml != "") {
                inputHtml += hiddenHtml;
            }
            return tdStrHead + inputHtml + tdStrEnd;
        }
        function repToFactoryConfirm() {
            MyConfirm("确定提交给车厂?", repToFactory, []);
        }
        function repToFactory() {
            disableAllBtn();
            var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/repToFactory.json?";
            sendAjax(url, getResult, 'fm');
        }
        function check(obj) {
            var re = /^[0-9]+[0-9]*]*$/;
            if (!re.test(obj)) {
                return false;
            } else {
                return true;
            }
        }
        function createSalesOrderConfirm() {
            var msg = "";
            var uncheckedId = "";
            var gift = document.getElementsByName("gift");

            var selFlag = false;
            var zeroFlag = 0;
            var cb = document.getElementsByName("cb");
            for (var i = 0; i < cb.length; i++) {
                selFlag = true;
                var giftFlag = false;
                for (var j = 0; j < gift.length; j++) {
                    if (gift[j].value == cb[i].value) {
                        giftFlag = true;
                        break;
                    }
                }

                //需要校验验收入库数量是否填写
                if (document.getElementById("WAREHOUSING_QTY_" + cb[i].value).value == "") {
                    msg += "请填写第" + (document.getElementById("WAREHOUSING_QTY_" + cb[i].value).parentElement.parentElement.rowIndex - 1) + "行的验收入库数量!</br>";
                } else {
                    if (!check(document.getElementById("WAREHOUSING_QTY_" + cb[i].value).value)) {
                        msg += "第" + (i + 1) + "行的验收入库数量必须是正整数!</br>";
                        MyAlert(msg);
                        return false;
                    }
                }
            }
            if (msg != "") {
                MyAlert(msg);
                enableCb();
                return;
            }
            MyConfirm("确定生成入库单?", createSaleOrder, [uncheckedId]);
            enableCb()
        }

        function closeOrder(id) {
            var closeUrl = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/closeOrderAction.json?orderId=" + id;
            sendAjax(closeUrl, closeResult, 'fm');
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
        function getPartQty() {
            if ($("wh_id").value == "") {
                MyAlert("请选择库房");
                return;
            }
            var whId = $("wh_id").value;
            var partId = "";
            var cb = document.getElementsByName("cb");
            for (var i = 0; i < cb.length; i++) {
                partId += "," + cb[i].value;
            }
            getPartItemStock(whId, partId);
        }
        function getPartItemStock(whId, partId) {
            disableAllBtn();
            var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/getPartItemStock.json?whId=" + whId + "&partId=" + partId;
            sendAjax(url, getPartResult, 'fm');
        }
        function getPartResult(jsonObj) {
            if (jsonObj != null || jsonObj.list != null) {
                var reAr = jsonObj.list;
                for (var i = 0; i < reAr.length; i++) {
                    var partId = reAr[i].PART_ID;
                    var qty = reAr[i].NORMAL_QTY;
                    document.getElementById("stockQty_" + partId).value = qty;
                }
            }
            enableAllBtn();
        }
        function getPartQtyOnLoad() {
            if ($("wh_id").value == "") {
                MyAlert("请选择库房");
                return;
            }
            var whId = $("wh_id").value;
            var partId = "";
            var cb = document.getElementsByName("cb");
            for (var i = 0; i < cb.length; i++) {
                partId += "," + cb[i].value;
            }
            getPartItemStockOnLoad(whId, partId);
        }
        //
        function getPartQtyOnLoad2() {
            if ($("wh_id").value == "") {
                MyAlert("请选择库房");
                return;
            }
            var cb = document.getElementsByName("cb");
            for (var i = 0; i < cb.length; i++) {
                var partId = cb[i].value;
                if (${mainMap.ORDER_TYPE}==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04%>) {
                    $('saleQty_' + partId).value = $('buyQty_' + partId).value;
                } else {
                    if (parseFloat($('stockQty_' + partId).value) < parseFloat($('buyQty_' + partId).value)) {
                        $('saleQty_' + partId).value = $('stockQty_' + partId).value
                    } else {
                        $('saleQty_' + partId).value = $('buyQty_' + partId).value;
                    }
                }

                countMoney($('saleQty_' + partId), $('buyPrice_' + partId).value, partId);
            }
        }
        function getPartItemStockOnLoad(whId, partId) {
            disableAllBtn();
            var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/getPartItemStock.json?whId=" + whId + "&partId=" + partId;
            sendAjax(url, getPartResultOnLoad, 'fm');
        }
        function getPartResultOnLoad(jsonObj) {
            if (jsonObj != null || jsonObj.list != null) {
                var reAr = jsonObj.list;
                for (var i = 0; i < reAr.length; i++) {
                    var partId = reAr[i].PART_ID;
                    var qty = reAr[i].NORMAL_QTY;
                    document.getElementById("stockQty_" + partId).value = qty;
                    if (${mainMap.ORDER_TYPE}==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04%>) {
                        $('saleQty_' + partId).value = $('buyQty_' + partId).value;
                    } else {
                        if (parseFloat(qty) < parseFloat($('buyQty_' + partId).value)) {
                            $('saleQty_' + partId).value = qty;
                        } else {
                            $('saleQty_' + partId).value = $('buyQty_' + partId).value;
                        }
                    }

                    countMoney($('saleQty_' + partId), $('buyPrice_' + partId).value, partId);
                }
            } else {
                var cb = document.getElementsByName("cb");
                for (var i = 0; i < cb.length; i++) {
                    document.getElementById("stockQty_" + cb[i].value).value = 0;
                }
            }
            enableAllBtn();
        }

        function createSaleOrder(uncheckedId) {
            disableAllBtn();
            var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartReplacedDlrOrder/createWarehousing.json?uncheckedId=" + uncheckedId;
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
                    var replace = jsonObj.replace;
                    if (replace) {
                        MyAlert(replace);
                    }
                    MyAlert(success);
                    window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartReplacedDlrOrder/partDlrOrderWarehousingInit.do?flag=true';
                } else if (error) {
                    MyAlert(error);
                } else if (exceptions) {
                    MyAlert(exceptions.message);
                }
            }
        }
        function initAccount() {
            if (${mainMap.accountFlag}) {
                var td1 = document.getElementById("accountTd1");
                var td2 = document.getElementById("accountTd2");
                var td3 = document.getElementById("accountTd3");
                var td4 = document.getElementById("accountTd4");
                var td5 = document.getElementById("accountTd5");
                var td6 = document.getElementById("accountTd6");
                var str1 = '<td>账户余额:</td>';
                var str2 = '<td><input id="accountSum" style="border:0px;background-color:#F3F4F8;"  name="accountSum" type="text" class="normal_txt" value="${mainMap.accountSumNow}" readonly /></td>';
                var str3 = '<td>可用金额:</td>';
                var str4 = '<td><input id="accountKy" style="border:0px;background-color:#6F9;"  name="accountKy" type="text" value="${mainMap.accountKyNow}" class="normal_txt" readonly /></td>';
                var str5 = '<td>冻结金额:</td>';
                var str6 = '<td><input id="accountDj"  name="accountDj" type="text" style="border:0px;background-color:#F3F4F8;" value="${mainMap.accountDjNow}" class="normal_txt" readonly /></td>';
                td1.innerHTML = str1;
                td2.innerHTML = str2;
                td3.innerHTML = str3;
                td4.innerHTML = str4;
                td5.innerHTML = str5;
                td6.innerHTML = str6;
            }
        }
        function ckAllBox(obj) {
            var cb = document.getElementsByName("cb");
            for (var i = 0; i < cb.length; i++) {
                cb[i].checked = obj.checked;
            }
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
                ;
                for (var i = 0; i < l2; i++) {
                    arr.push(str.substr(l1 + i * step, step));
                }
                ;
                str = arr.join(splitor);
                str = str.substr(0, str.length - 1);
            }
            ;
            if (decPart != ".") {
                str += decPart;
            }
            return str;
        }
        function unFormatNum(str) {
            str = str + "";
            if (str.indexOf(",") > -1) {
                str = str.replace(/\,/g, "");
            }
            return str;
        }
        function doInit() {
            document.getElementById("TRANS_TYPE").value = 1;
            document.getElementById("transpayType").value = ${mainMap.IS_TRANSFREE} == 10041001 ? 92381001 : 92381002;
            if (${flag==2}) {
                //将所有BUTTON disable 除了  goBack  和sub
                var inputArr = document.getElementsByTagName("input");
                for (var i = 0; i < inputArr.length; i++) {
                    if (inputArr[i].type == "button") {
                        if (inputArr[i].id == "back" || inputArr[i].id == "sub" || inputArr[i].id == "orderDiv") {
                            continue;
                        }
                        inputArr[i].disabled = true;
                    }
                }
                //将所有TEXT disable
                for (var i = 0; i < inputArr.length; i++) {
                    if (inputArr[i].type == "text") {
                        inputArr[i].disabled = true;
                    }
                }
                for (var i = 0; i < inputArr.length; i++) {
                    if (inputArr[i].type == "checkbox") {
                        inputArr[i].disabled = true;
                    }
                }
                //将所有TEXT select
                var selArr = document.getElementsByTagName("select");
                for (var i = 0; i < selArr.length; i++) {
                    selArr[i].disabled = true;
                }
            }

            //如果是计划或者直发
            /*    if (
            ${mainMap.ORDER_TYPE}==
            <%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04%>||
            ${mainMap.ORDER_TYPE}==
            <%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_03%>) {
             var defaultOption;
             for (var i = 0; i < $('ORDER_TYPE').options.length; i++) {
             if ($('ORDER_TYPE').options[i].value ==
            ${mainMap.ORDER_TYPE}) {
             defaultOption = $('ORDER_TYPE').options[i];
             }
             }
             $('ORDER_TYPE').options.length = 0;
             $('ORDER_TYPE').add(defaultOption);
             } else {
             var defaultOptionArr = [];
             for (var i = 0; i < $('ORDER_TYPE').options.length; i++) {
             if ($('ORDER_TYPE').options[i].value ==
            <%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_01%>
             || $('ORDER_TYPE').options[i].value ==
            <%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_02%>) {
             defaultOptionArr.push($('ORDER_TYPE').options[i]);
             }
             }
             $('ORDER_TYPE').options.length = 0;
             for (var i = 0; i < defaultOptionArr.length; i++) {
             $('ORDER_TYPE').add(defaultOptionArr[i]);
             }
             }*/

            //默认仓库
            //getPartQtyOnLoad2();
            countAll();
        }
        function goBack() {
            window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartReplacedDlrOrder/partDlrOrderWarehousingInit.do?flag=true';
        }

        /*
         function validateFreight(obj){
         var reg=/^-?\d+\.?\d{0,2}$/;
         if(!reg.test(obj.value)){
         obj.value="0";
         MyAlert("请输入正确的金额!");
         return;
         }
         }
         */
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

        //添加
        jQuery.noConflict();
        function editSave() {
            if ($("whId").value == "") {
                MyAlert("请选择仓库!");
                return;
            }
            var mt = document.getElementById("file");
            var partId = document.getElementsByName("partIdList");
            for (var i = 2; i < mt.rows.length; i++) {
                var loc_code = jQuery("#LOC_CODE_T_" + partId[i - 2].value).val();
                if (loc_code == "") {
                    MyAlert("货位信息不能为空！");
                    return;
                } else {
                    var url2 = "<%=contextPath%>/parts/storageManager/partDistributeMgr/PartDistributeMgr/checkSeatExist.json";
                    var para = "LOC_CODE=" + loc_code + "&PART_ID=" + partId;
                    makeCall(url2, forBack3, para);
                }
            }
            if (!submitForm("fm")) {
                return;
            }
            createSalesOrderConfirm();
        }
        function forBack2(json) {
            if (json.returnValue == 1) {

            } else {
                MyAlert("该货位编码不存在，请录入正确的货位信息！");
                return;
            }
        }
        var LOC_ID = null;//part_id
        function codeSet(i, c, n) {
            var v = i + "," + c + "," + n;
            jQuery("#LOC_CODE_T_" + LOC_ID).val(c);
            jQuery("#LOC_CODE_" + LOC_ID).val(v);
        }
    </script>
</head>
<body onload="loadcalendar();initAccount();doInit();enableAllBtn();">
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <input name="orderCode" id="orderCode" type="hidden"
           value="${mainMap.ORDER_CODE}"/> <input name="dealerId"
                                                  id="dealerId" type="hidden" value="${mainMap.DEALER_ID}"/> <input
        name="dealerCode" id="dealerCode" type="hidden"
        value="${mainMap.DEALER_CODE}"/> <input name="dealerName"
                                                id="dealerName" type="hidden" value="${mainMap.DEALER_NAME}"/> <input
        name="sellerId" id="sellerId" type="hidden"
        value="${mainMap.SELLER_ID}"/> <input name="SELLER_ID"
                                              id="SELLER_ID" type="hidden" value="${mainMap.SELLER_ID}"/> <input
        name="sellerCode" id="sellerCode" type="hidden"
        value="${mainMap.SELLER_CODE}"/> <input name="sellerName"
                                                id="sellerName" type="hidden" value="${mainMap.SELLER_NAME}"/> <input
        name="buyerId" id="buyerId" type="hidden" value="${mainMap.BUYER_ID}"/>
    <input name="buyerName" id="buyerName" type="hidden"
           value="${mainMap.BUYER_NAME}"/> <input name="orderId" id="orderId"
                                                  type="hidden" value="${mainMap.ORDER_ID}"/> <input
        name="orderAmount" id="orderAmount" type="hidden"
        value="${mainMap.ORDER_AMOUNT}"/> <input name="soCode" id="soCode"
                                                 type="hidden" value="${mainMap.soCode}"/> <input name="planFlag"
                                                                                                  id="planFlag"
                                                                                                  type="hidden"
                                                                                                  value="${planFlag}"/>
    <input
            name="orderType" id="orderType" type="hidden"
            value="${mainMap.ORDER_TYPE}"/> <input name="isLock" id="isLock"
                                                   type="hidden" value="${mainMap.LOCK_FREIGHT}"/> <input
        name="lockFreight" id="lockFreight" type="hidden"
        value="${mainMap.FREIGHT2}"/> <input id="accountSum"
                                             name="accountSum" type="hidden" value="${mainMap.accountSumNow}"/>
    <input id="accountKy" name="accountKy" type="hidden"
           value="${mainMap.accountKyNow}"/> <input id="accountDj"
                                                    name="accountDj" type="hidden" value="${mainMap.accountDjNow}"/>


    <div class="wbox">
        <div class="navigation">
            <img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置:
            配件管理>配件仓储管理>配件替换管理&gt;切换件验收入库&gt;验收入库
        </div>
        <FIELDSET>
            <table class="table_add" border="0"
                   style="BORDER-RIGHT: #859aff 1px solid; BORDER-TOP: #859aff 1px solid; BORDER-LEFT: #859aff 1px solid; BORDER-BOTTOM: #859aff 1px solid; border-color: #859aff;"
                   cellSpacing=1 cellPadding=1 width="100%">
                <tr>
                    <th colspan="6" width="100%"><img class="nav"
                                                      src="<%=contextPath%>/img/subNav.gif"/> 采购订单信息
                    </th>
                </tr>
                <tr>
                    <!-- 					<td align="right" nowrap>销售单位：</td> -->
                    <td><input name="SELLER_NAME" class="middle_txt"
                               id="SELLER_NAME" type="hidden" value="${mainMap.SELLER_NAME}"/>
                        <input name="SELLER_CODE" class="middle_txt" id="SELLER_CODE"
                               type="hidden" value="${mainMap.SELLER_CODE}" size="20"
                               readonly="readonly"/> <input name="SELLER_ID"
                                                            class="middle_txt" id="SELLER_ID" type="hidden"
                                                            value="${mainMap.SELLER_ID}" size="20" readonly="readonly"/>
                        <!-- <input name='dlbtn1' id='dlbtn1' class='mini_btn'  type='button' value='...'  onclick="selSales('SELLER_ID','SELLER_CODE','SELLER_NAME','','','','',${mainMap.DEALER_ID},'1')"/> -->
                        <font color="RED"></font></td>
                    <td><input type="hidden" value="${mainMap.ORDER_ID}"
                               name="orderId" id="orderId"/><input readonly type="hidden"
                                                                   value="${mainMap.ORDER_CODE}" name="orderCode"
                                                                   id="orderCode"/></td>
                </tr>
                <tr>
                    <td align="right">换货单位：</td>
                    <td><input readonly class="middle_txt" type="hidden"
                               value="${mainMap.DEALER_NAME}" name="dealerName" id="dealerName"/>${mainMap.DEALER_NAME}
                    </td>
                    <td align="right">换货人：</td>
                    <td><input readonly class="middle_txt" type="hidden"
                               value="${mainMap.BUYER_NAME}" name="buyerName" id="buyerName"/>${mainMap.BUYER_NAME}</td>
                    <td align="right">换货日期：</td>
                    <td><input readonly type="hidden"
                               value="${mainMap.CREATE_DATE}" name="now" id="now"/>${mainMap.CREATE_DATE}</td>
                </tr>
                <!-- 					<tr> -->
                <!-- 						<td align="right">接收单位：</td> -->
                <!-- 						<td> -->
                <input readonly class="middle_txt" type="hidden"
                       value="${mainMap.DEALER_NAME}" name="RCV_ORG" id="RCV_ORG"/>
                <!-- 						</td> -->

                <!-- 					</tr> -->
                <!-- 					<tr> -->
                <!-- 						<td align="right">接收地址：</td> -->
                <!-- 						<td colspan="3"> -->
                <input name="ADDR" class="maxlong_txt" id="ADDR" type="hidden"
                       size="20" value="${mainMap.ADDR}" readonly="readonly"/>
                <input name="ADDR_ID" class="middle_txt" id="ADDR_ID"
                       value="${mainMap.ADDR_ID}" type="hidden" size="20"
                       readonly="readonly"/>
                <input name="PAY_TYPE" class="middle_txt" id="PAY_TYPE"
                       value="${mainMap.PAY_TYPE}" type="hidden" size="20"
                       readonly="readonly"/>
                <!-- 						</td> -->
                <!-- 						<td align="right">到站名称：</td> -->
                <!-- 						<td> -->
                <input id="STATION" name="STATION" type="hidden" class="normal_txt"
                       value="${mainMap.STATION}" readonly/>
                <font color="RED"></font>
                </td>
                <!-- 					</tr> -->
                <!-- 					<tr> -->
                <!-- 						<td align="right">接收人：</td> -->
                <!-- 						<td> -->
                <input id="RECEIVER" name="RECEIVER" type="hidden"
                       value="${mainMap.RECEIVER}" class="normal_txt"/>
                <font color="RED"></font>
                <!-- 							</td> -->
                <!-- 						<td align="right"><span align="right">接收人电话：</span></td> -->
                <!-- 						<td> -->
                <input id="TEL" name="TEL" type="hidden" class="normal_txt"
                       readonly value="${mainMap.TEL}"/>
                <font color="RED"></font>
                <!-- 							</td> -->
                <!-- 						<td align="right">邮政编码：</td> -->
                <!-- 						<td> -->
                <input id="POST_CODE" name="POST_CODE" type="hidden"
                       class="normal_txt" value="${mainMap.POST_CODE}" readonly/>
                <font color="RED"></font>
                <!-- 							</td> -->
                <!-- 					</tr> -->
                <!-- 					<tr id="tr1"> -->
                <!-- 					<td id="accountSumNameTd" align="right">账户余额：</td> -->
                <!-- 						<td id="accountSumTextTd"> -->
                <input id="accountSum" name="accountSum" style="border: none"
                       type="hidden" class="normal_txt"
                       value="${dataMap.currAcountMap.ACCOUNT_SUM}" readonly/>
                <!-- 							</td> -->
                <!-- 					<td id="accountKyNameTd" align="right">可用金额：</td> -->
                <!-- 						<td id="accountKyTextTd"> -->
                <input id="accountKy" style="border: 0px; background-color: #6F9;"
                       name="accountKy" type="hidden"
                       value="${dataMap.currAcountMap.ACCOUNT_KY}" class="normal_txt"
                       readonly/>
                <!-- 							<font color="blue"></font></td> -->
                <!-- 					<td id="accountDjNameTd" align="right"><span align="right">冻结金额：</span></td> -->
                <!-- 						<td id="accountDjTextTd"> -->
                <input id="accountDj" name="accountDj" type="hidden"
                       style="border: none" value="${dataMap.currAcountMap.ACCOUNT_DJ}"
                       class="normal_txt" readonly/>
                <!-- 							<font color="blue"></font></td> -->
                <!-- 					</tr> -->
                <!-- 					<tr id="tr2"> -->
                <!-- 					<td align="right">订单总金额：</td> -->
                <!-- 						<td> -->
                <input id="Amount" name="Amount" type="hidden"
                       value="${mainMap.ORDER_AMOUNT} " class="normal_txt" readonly
                       style="background-color: #ffff80; border: none"/>
                <!-- 							<font -->
                <!-- 							color="blue"></font></td> -->
                <!-- 					<td align="right">订单金额：</td> -->
                <!-- 						<td> -->
                <input id="partAmount" name="partAmount" type="hidden"
                       value="0.00 " class="normal_txt" readonly
                       style="background-color: #ffff80; border: none"/>
                <!-- 							<font -->
                <!-- 							color="blue"></font></td> -->
                <!-- 					<td align="right">运费金额：</td> -->
                <!-- 						<td> -->
                <input id="freight" name="freight" type="hidden"
                       style="background-color: #ffff80; border: none"
                       value="${mainMap.FREIGHT}" readonly/>
                <!-- 							</td> -->
                </tr>
                <!-- 					<tr id="tr3"> -->
                <!-- 					<td id="DISCOUNTNameTd" align="right">折扣率：</td> -->
                <!-- 						<td id="DISCOUNTTextTd"> -->
                <input readonly class="middle_txt" type="hidden"
                       style="border: none" value="${mainMap.DISCOUNT}" name="DISCOUNT"
                       id="DISCOUNT"/>
                <!-- 							</td> -->
                <!-- 					<td class="checked" colspan="5">提示：订单总金额=订单金额+运费金额</td> -->
                <!-- 					</tr> -->
                <tr>
                    <td width="10%" align="right">库房：</td>
                    <td width="20%"><select name="whId" id="whId"
                                            class="short_sel">
                        <!-- 								<option value="">-请选择-</option> -->
                        <c:forEach items="${wareHouseList}" var="wareHouse">
                            <option value="${wareHouse.WH_ID},${wareHouse.WH_NAME}">${wareHouse.WH_NAME}</option>
                        </c:forEach>
                    </select></td>
                    <%--						<td align="center">入库货位<font color="RED">*</font></td>--%>
                    <%--						<td>--%>
                    <%--   		 	<input id="LOC_CODE_T" class="middle_txt" type="text" value="">--%>
                    <%--   		 	<input name="LOC_CODE" id="LOC_CODE" type="hidden" value="">--%>
                    <%--            <input class='mini_btn' type='button' value='...' onclick="codeChoice();"/></td>--%>
                </tr>
                <tr>
                    <td align="right">服务商备注：</td>
                    <td colspan="5"><textarea name="textarea" cols="80" rows="4"
                                              readonly="readonly">${mainMap.REMARK}</textarea></td>
                </tr>
            </table>
        </FIELDSET>
        <table id="file" class="table_list" border="0"
               style="BORDER-RIGHT: #859aff 1px solid; BORDER-TOP: #859aff 1px solid; BORDER-LEFT: #859aff 1px solid; BORDER-BOTTOM: #859aff 1px solid; border-color: #859aff;"
               cellSpacing=1 cellPadding=1 width="100%">
            <tr>
                <th colspan="14" align="left"><img class="nav"
                                                   src="<%=contextPath%>/img/subNav.gif"/>配件信息 <span
                        class="checked" style="text-align: left"/></th>
            </tr>
            <tr bgcolor="#FFFFCC">

                <td><input type="hidden" checked onclick="ckAllBox(this)"
                           id="ckAll" name="ckAll"/></td>
                <td>序号</td>
                <td>配件编码</td>
                <td>配件名称</td>
                <td>当前库存数量</td>
                <td>切换数量</td>
                <td>审核数量</td>
                <td>回运数量</td>
                <td>验收入库数量</td>
                <td align="center">入库货位<font color="RED">*</font></td>
                <!-- 					<td>切换件编码</td> -->
                <!-- 					<td>切换件名称</td> -->
                <td>验收说明</td>
                <!-- 					<td>操作</td> -->
            </tr>
            <c:forEach items="${detailList}" var="data" varStatus="status">
                <%--<c:if test="${data.CHECK_QTY!=0&&data.BACKHAUL_QTY!=0}">--%>
                <tr>
                    <td>
                        <input name="partIdList" type="hidden" value="${data.PART_ID}"/>
                        <script type="text/javascript">
                            document.write('<input  type="hidden" onclick="countAll()"  id="cell_' + (document.getElementById('file').rows.length - 2) + '"  name="cb"  checked="true" value="'+${data.PART_ID}+
                            '" />'
                            )
                            ;
                        </script>
                    </td>
                    <td>
                        <script type="text/javascript">
                            document.write((document.getElementById('file').rows.length - 2));
                        </script>
                    </td>
                    <td nowrap>${data.PART_OLDCODE}</td>
                    <input type="hidden" id="part_Oldcode_${data.PART_ID}"
                           name="part_Oldcode_${data.PART_ID}" value="${data.PART_OLDCODE}"/>
                    <td align="left">${data.PART_CNAME}<input type="hidden"
                                                              id="part_Cname_${data.PART_ID}"
                                                              name="part_Cname_${data.PART_ID}"
                                                              value="${data.PART_CNAME}"/>
                    </td>
                    <td align="center">${data.STOCK_QTY}<input type="hidden"
                                                               id="item_qty_${data.PART_ID}"
                                                               name="item_qty_${data.PART_ID}"
                                                               value="${data.STOCK_QTY}"/>
                    </td>
                    <td align="center">${data.BUY_QTY}<input type="hidden"
                                                             class="short_txt" value="${data.BUY_QTY}"
                                                             style="background-color: #FFFFCC; text-align: center"
                                                             id="buyQty_${data.PART_ID}" name="buyQty_${data.PART_ID}"/>

                    </td>
                    <td align="center">${data.CHECK_QTY}<input type="hidden"
                                                               class="short_txt" value="${data.CHECK_QTY}"
                                                               style="background-color: #FFFFCC; text-align: center"
                                                               id="check_Qty_${data.PART_ID}"
                                                               name="check_Qty_${data.PART_ID}"/>
                    </td>
                    <td align="center">${data.BACKHAUL_QTY}<input type="hidden"
                                                                  class="short_txt" value="${data.BACKHAUL_QTY}"
                                                                  style="background-color: #FFFFCC; text-align: center"
                                                                  id="BACKHAUL_QTY_${data.PART_ID}"
                                                                  name="BACKHAUL_QTY_${data.PART_ID}"/>
                    </td>
                    <td><input type="text" class="short_txt"
                               value="${data.BACKHAUL_QTY}"
                               style="background-color: #FFFFCC; text-align: center"
                               id="WAREHOUSING_QTY_${data.PART_ID}"
                               name="WAREHOUSING_QTY_${data.PART_ID}"/></td>
                    <td>
                        <input id="LOC_CODE_T_${data.PART_ID}" class="middle_txt" type="text" value=""
                               onchange="checkCode(this,'${data.PART_ID}');">
                        <input name="LOC_CODE_${data.PART_ID}" id="LOC_CODE_${data.PART_ID}" type="hidden" value="">
                        <input id='${status.index}' class='mini_btn' type='button' value='...'
                               onclick="codeChoice(${data.PART_ID});"/></td>

                        <%-- 							<td>${data.REPART_OLDCODE}</td> --%>
                        <%-- 							<td align="left">${data.REPART_NAME}</td> --%>
                    <input type="hidden" class="short_txt"
                           value="${data.REPART_OLDCODE}" style=""
                           id="repart_oldcode_${data.PART_ID}"
                           name="repart_oldcode_${data.PART_ID}"/>
                    <input type="hidden" class="short_txt" value="${data.PART_CODE}"
                           style="" id="part_code_${data.PART_ID}"
                           name="part_code_${data.PART_ID}"/>
                    <input type="hidden" id="buyAmount_${data.PART_ID}"
                           name="buyAmount_${data.PART_ID}" value="${data.BUY_AMOUNT}"/>
                    <input type="hidden" id="upOrgStock_${data.PART_ID}"
                           name="upOrgStock_${data.PART_ID}" value="${data.UPORGSTOCK}"/>
                    <input type='hidden' name='isReplaced_${data.PART_ID}'
                           id='isReplaced_${data.PART_ID}' value='${data.IS_REPLACED}'/>
                    <input type='hidden' name='LINE_ID_${data.PART_ID}'
                           id='LINE_ID_${data.PART_ID}' value="${data.LINE_ID}"/>
                    <input type='hidden' name='isFlag_${data.PART_ID}'
                           id='isFlag_${data.FLAG}' value='${data.FLAG}'/>
                    <td><input type="text" class="short_txt"
                               value="" style="text-align: left"
                               id="yanshou_remark_${data.PART_ID}" name="yanshou_remark_${data.PART_ID}"/>
                        <input type="hidden" class="short_txt"
                               value="${data.REMARK}" style="text-align: left"
                               id="remark_${data.PART_ID}" name="remark_${data.PART_ID}"/><input
                                type='hidden' name='isLack_${data.PART_ID}'
                                id='isLack_${data.PART_ID}' value='${data.IS_LACK}'></td>
                    <!-- 						<td><input type="button" class="short_btn" name="queryBtn" -->
                    <!-- 							value="删除" onclick="deleteTblRow(this);" /><input type='hidden' -->
                        <%-- 							name='stockQty_${data.PART_ID}' id='stockQty_${data.PART_ID}' --%>
                        <%-- 							value='${data.ITEM_QTY}'></td> --%>

                </tr>
                <%--</c:if>--%>
            </c:forEach>
        </table>

        <table border="0" class="table_query">
            <tr align="center">
                <td>&nbsp; <%-- 					<c:if test="${flag!=2}"> --%> <input
                        class="normal_btn" type="button" value="验收入库"
                        onclick="editSave();"/> &nbsp;&nbsp; <input
                        class="normal_btn" type="button" value="返 回" name="back" id="back"
                        onclick="goBack()"/></td>
            </tr>
        </table>
    </div>
</form>
</body>
</html>