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
            document.write("<input type='checkbox' id='cell_" + (document.getElementById("file").rows.length - 3) + "' name='cb' checked onclick='countAllQty()' value='" + partId + "' />");
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

            //获取折扣率
            var discount = document.getElementById("DISCOUNT").value;
            if (discount == null || discount == "") {
                discount = 1;
            }

            var value = obj.value;
            if (value == '') {
                obj.value = "0";
                countMoney(obj, price, partId);
                return;
            }
            if (isNaN(value)) {
                MyAlert("请输入数字!");
                obj.value = "0";
                countMoney(obj, price, partId);
                return;
            }
            var re = /^[0-9]+[0-9]*]*$/;
            if (!re.test(obj.value)) {
                MyAlert("请输入正整数!");
                obj.value = "0";
                countMoney(obj, price, partId);
                return;
            }

            if (parseFloat(document.getElementById("saleQty_" + partId).value) > parseFloat(document.getElementById("buyQty_" + partId).value)) {
                MyAlert("审核数量不得大于订货数量!");
                obj.value = "0";
                countMoney(obj, price, partId);
                return;
            }

            var money = (parseFloat(price) * parseFloat(value) * discount).toFixed(2);
            $("buyAmount_" + partId).value = formatNum(money);

            var amountCount = parseFloat(0);
            var cb = document.getElementsByName("cb");
            for (var i = 0; i < cb.length; i++) {
                if ("" != cb[i].value && null != document.getElementById("buyAmount_" + cb[i].value) && "" != document.getElementById("buyAmount_" + cb[i].value).value) {
                    //只有CHECKED的才能计算
                    if (cb[i].checked) {
                        var buyAmount = document.getElementById("buyAmount_" + cb[i].value).value;
                        amountCount = (parseFloat(amountCount) + parseFloat(unFormatNum(buyAmount))).toFixed(2);
                    }
                }
            }
            document.getElementById("orderAmount").value = formatNum(amountCount);
            $('orderA').value = formatNum(amountCount);
            //getFreight(amountCount);数据量大时速度太慢
        }


        function validateQty(obj, price, partId) {
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
                MyAlert("审核数量不得大于订货数量!");
                obj.value = "";
                countAllQty();
                return;
            }
            countAllQty();
            countMoney(obj, price, partId);
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
        function createSalesOrderConfirm() {
            var msg = "";
            var uncheckedId = "";
            if ("" == document.getElementById("wh_id").value) {
                msg += "请选择出库仓库!</br>";
            }
            if ("" == document.getElementById("RCV_ORG").value) {
                msg += "请选择接收单位!</br>";
            }
            if ("" == document.getElementById("ADDR").value) {
                msg += "请选择接收地址!</br>";
            }
            if ("" == document.getElementById("RECEIVER").value) {
                msg += "接收人不能为空!</br>";
            }
            if ("" == document.getElementById("TEL").value) {
                msg += "接收人电话不能为空!</br>";
            }
            if ("" == document.getElementById("POST_CODE").value) {
                msg += "邮政编码不能为空!</br>";
            }
            if ("" == document.getElementById("STATION").value) {
                msg += "到站名称不能为空!</br>";
            }
            if ("" == document.getElementById("TRANS_TYPE").value) {
                msg += "请选择发运方式!</br>";
            }
            if ("" == document.getElementById("PAY_TYPE").value) {
                msg += "请选择付款方式!</br>";
            }
            if ("" == document.getElementById("ORDER_TYPE").value) {
                msg += "请选择订单类型!</br>";
            }
            if ("" == document.getElementById("transpayType").value) {
                msg += "请选择运费支付方式!</br>";
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
                        msg += "请填写第" + (document.getElementById("saleQty_" + cb[i].value).parentElement.parentElement.rowIndex - 1) + "行的审核数量!</br>";
                    } else {
                        if (parseFloat(document.getElementById("saleQty_" + cb[i].value).value) > parseFloat(document.getElementById("buyQty_" + cb[i].value).value) && (!giftFlag)) {
                            msg += "第" + (document.getElementById("saleQty_" + cb[i].value).parentElement.parentElement.rowIndex - 1) + "行的审核数量不得大于订货数量!</br>";
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
            /* if (parseFloat(zeroFlag) <= parseFloat(0)) {
             MyAlert("销售配件总数不能为0!");
             return;
             }*/

            if (!selFlag) {
                msg += "请选择明细!</br>";
            }

            if (msg != "") {
                MyAlert(msg);
                enableCb();
                return;
            }
            MyConfirm("确定生成发运计划?", createSaleOrder, [uncheckedId]);
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
            var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/createPlanOrder.json?uncheckedId=" + uncheckedId;
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
                    window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/partGxDlrOrderCheckInit.do?flag=true';
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


        function doInit() {
            document.getElementById("TRANS_TYPE").value = "${mainMap.TRANS_TYPE}";
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
            window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/partGxDlrOrderCheckInit.do?flag=true';
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

        function checkSys_Sel_Date(date1, date2) {
            var flag = "";
            if (date1 <= date2) {
                flag = false;
            } else {
                flag = true;
            }
            return flag;
        }
    </script>
</head>
<body onload="loadcalendar();doInit();enableAllBtn();countAllQty();">
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <input name="orderCode" id="orderCode" type="hidden" value="${mainMap.ORDER_CODE}"/>
    <input name="dealerId" id="dealerId" type="hidden" value="${mainMap.DEALER_ID}"/>
    <input name="dealerCode" id="dealerCode" type="hidden" value="${mainMap.DEALER_CODE}"/>
    <input name="dealerName" id="dealerName" type="hidden" value="${mainMap.DEALER_NAME}"/>
    <input name="sellerId" id="sellerId" type="hidden" value="${mainMap.SELLER_ID}"/>
    <input name="SELLER_ID" id="SELLER_ID" type="hidden" value="${mainMap.SELLER_ID}"/>
    <input name="sellerCode" id="sellerCode" type="hidden" value="${mainMap.SELLER_CODE}"/>
    <input name="sellerName" id="sellerName" type="hidden" value="${mainMap.SELLER_NAME}"/>
    <input name="buyerId" id="buyerId" type="hidden" value="${mainMap.BUYER_ID}"/>
    <input name="buyerName" id="buyerName" type="hidden" value="${mainMap.BUYER_NAME}"/>
    <input name="orderId" id="orderId" type="hidden" value="${mainMap.ORDER_ID}"/>
    <input name="soCode" id="soCode" type="hidden" value="${mainMap.soCode}"/>
    <input name="planFlag" id="planFlag" type="hidden" value="${planFlag}"/>
    <input name="orderType" id="orderType" type="hidden" value="${mainMap.ORDER_TYPE}"/>
    <input name="isLock" id="isLock" type="hidden" value="${mainMap.LOCK_FREIGHT}"/>
    <input name="lockFreight" id="lockFreight" type="hidden" value="${mainMap.FREIGHT2}"/>
    <input name="allBuyQty" id="allBuyQty" type="hidden" value="${allBuyQty}"/>

    <div class="wbox">
        <div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置:

            配件管理 > 配件销售管理 &gt;广宣订单审核 &gt;生成发运计划单
        </div>
        <FIELDSET>
            <LEGEND
                    style="MozUserSelect: none; KhtmlUserSelect: none"
                    unselectable="on">
                <th colspan="6"
                    style="background-color:#DAE0EE;font-weight:normal;color:#416C9B;border-color: #859aff;padding:2px;line-height:1.5em;border: 1px solid #E7E7E7;">
                    <img class="nav" src="<%=contextPath%>/img/subNav.gif"/> 配件销售信息
                    <input type="button" class="normal_btn" name="orderDiv"
                           id="orderDiv" value="打开" onclick="openDiv()"/>
                </th>
            </LEGEND>
            <table id="queryT" class="table_query" border="0" style="display:none"
                   style="BORDER-RIGHT: #859aff 1px solid; BORDER-TOP:#859aff 1px solid; BORDER-LEFT:#859aff 1px solid; BORDER-BOTTOM:#859aff 1px solid;border-color: #859aff;"
                   cellSpacing=1 cellPadding=1 width="100%">
                <tr>
                    <td align="right">销售日期:</td>
                    <td>${mainMap.CREATE_DATE}</td>
                    <td align="right">销售制单人:</td>
                    <td>${mainMap.saleName}</td>
                    <td align="right">采购制单人:</td>
                    <td>${mainMap.NAME}</td>

                </tr>
                <tr>
                    <td align="right">销售单位:</td>
                    <td>${mainMap.SELLER_NAME}</td>
                    <td align="right">订货单位:</td>
                    <td>${mainMap.DEALER_NAME}</td>
                    <td align="right">订货单位编码码:</td>
                    <td>${mainMap.DEALER_CODE}</td>
                </tr>
                <tr>
                    <td align="right">采购日期:</td>
                    <td>${mainMap.CREATE_DATE}</td>
                    <td align="right">出库仓库:</td>
                    <td>
                        <select name="wh_id" id="wh_id" class="short_sel" onchange="getPartQty();">
                            <c:forEach items="${wareHouseList}" var="wareHouse">
                                <option value="${wareHouse.WH_ID}">${wareHouse.WH_NAME}</option>
                            </c:forEach>
                        </select>
                        <font color="RED">*</font></td>
                    <td align="right">接收单位:</td>
                    <td><input name="RCV_ORG" class="middle_txt" id="RCV_ORG" type="text" value="${mainMap.RCV_ORG}"
                               size="20"
                               readonly="readonly"/>
                        <input name="RCV_CODE" class="middle_txt" id="RCV_CODE" type="hidden"
                               value="${mainMap.RCV_CODE}"
                               size="20" readonly="readonly"/>
                        <input name="RCV_ORGID" class="middle_txt" id="RCV_ORGID" type="hidden"
                               value="${mainMap.RCV_ORGID}"
                               size="20" readonly="readonly"/>
                        <input name='dlbtn2' id='dlbtn2' class='mini_btn' type='button' value='...'
                               onclick="selSales('RCV_ORGID','RCV_CODE','RCV_ORG','','','','',${mainMap.DEALER_ID},'2')"/>
                    </td>
                </tr>
                <tr>
                    <td align="right">接收地址:</td>
                    <td colspan="3"><input name="ADDR" class="maxlong_txt" id="ADDR" type="text" size="20"
                                           value="${mainMap.ADDR}" readonly="readonly"/>
                        <input name="ADDR_ID" class="middle_txt" id="ADDR_ID" value="${mainMap.ADDR_ID}" type="hidden"
                               size="20"
                               readonly="readonly"/>
                        <input name='dlbtn2' id='dlbtn2' class='mini_btn' type='button' value='...'
                               onclick="selSales('ADDR_ID','','ADDR','RECEIVER','TEL','POST_CODE','STATION',${mainMap.DEALER_ID},'3')"/>
                    </td>
                    <td align="right">接收人:</td>
                    <td><input id="RECEIVER" name="RECEIVER" type="text" value="${mainMap.RECEIVER}"
                               class="normal_txt"/>
                        <font color="RED">*</font></td>
                </tr>
                <tr>
                    <td align="right">接收人电话:</td>
                    <td><input id="TEL" name="TEL" type="text" class="normal_txt" readonly value="${mainMap.TEL}"/>
                        <font color="RED">*</font></td>
                    <td align="right">邮政编码:</td>
                    <td><input id="POST_CODE" name="POST_CODE" type="text" class="normal_txt"
                               value="${mainMap.POST_CODE=='null'?'':mainMap.POST_CODE}" readonly/>
                        <font color="RED">*</font></td>
                    <td align="right">到站名称:</td>
                    <td><input id="STATION" name="STATION" type="text" class="normal_txt" value="${mainMap.STATION}"
                               readonly/>
                        <font color="RED">*</font></td>
                </tr>
                <tr>
                    <td align="right">发运方式:</td>
                    <td>
                        <select name="TRANS_TYPE" id="TRANS_TYPE" class="short_sel" onchange="countAll();"
                                style="background-color: #fffb48">
                            <c:if test="${transList!=null}">
                                <c:forEach items="${transList}" var="list">
                                    <option value="${list.fixValue }" selected>${list.fixName }</option>
                                </c:forEach>
                            </c:if>
                        </select>
                        <font color="RED">*</font>
                    <td align="right">付款方式:</td>
                    <td>
                        <script type="text/javascript">
                            genSelBoxExp("PAY_TYPE", <%=Constant.CAR_FACTORY_SALES_PAY_TYPE%>, ${mainMap.PAY_TYPE}, false, "short_sel", "", "false", '');
                        </script>
                        <font color="RED">*</font></td>
                    <td align="right">订单类型:</td>
                    <td>
                        <script type="text/javascript">
                            genSelBoxExp("ORDER_TYPE", <%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE%>, ${mainMap.ORDER_TYPE}, false, "short_sel", " disabled", "false", '');
                        </script>
                    </td>
                </tr>
                <tr>
                    <td align="right">运费支付方式:</td>
                    <td>
                        <script type="text/javascript">
                            genSelBoxExp("transpayType", <%=Constant.CAR_FACTORY_ORDER_CHECK_PAY_TRANS%>, <%=Constant.CAR_FACTORY_ORDER_CHECK_PAY_TRANS_02%>, false, "short_sel", "onchange=countAll();", "false", '');
                        </script>
                    </td>
                    <td align="right">折扣:</td>
                    <td><input readonly class="phone_txt" type="text" style="border:0px;background-color:#F3F4F8;"
                               value="${mainMap.discount}" name="DISCOUNT" id="DISCOUNT"/>
                        <font color="RED">*</font></td>
                    <td align="right">订单总金额:</td>
                    <td><input readonly class="phone_txt" type="text" style="border:0px;background-color:#F3F4F8;"
                               value="${mainMap.AMOUNT}" name="orderAmount" id="orderAmount"/>
                        <font color="RED">*</font></td>
                </tr>
                <!-- <tr>
                    <td align="right">销售总金额:</td>
                    <td><input id="Amount" name="Amount" type="text" style="border:0px;background-color:#F3F4F8;" value="0.00" onchange="countAll();"
                               class="normal_txt" readonly/><font color="blue">元</font></td>

                    <td align="right">销售金额:</td>
                    <td><input readonly  type="text" style="border:0px;background-color:#F3F4F8;"
                               value="0.00" name="partAmount" id="partAmount"/>
                        <font color="blue">元</font></td>
                    <td align="right">运费金额:</td>
                    <td>
                        <input id="freight" name="freight" type="text"  value="0"
                               style="background-color: #ffff80" onchange="validateFreight(this)"/>
                        <font color="blue">元</font>
                    </td>
                </tr>
                <tr>
                    <td align="right" id="accountTd1">
                    </td>
                    <td id="accountTd2" align="left">
                    </td>
                    <td align="right" id="accountTd3">
                    </td>
                    <td id="accountTd4">
                    </td>
                    <td align="right" id="accountTd5">
                    </td>
                    <td id="accountTd6">
                    </td>
                </tr> -->
                <tr>
                    <td align="right">
                        服务商联系人:
                    </td>
                    <td align="left">
                        ${linkMan}
                    </td>
                    <td align="right">
                        服务商电话:
                    </td>
                    <td>
                        ${phone}
                    </td>
                </tr>
                <input readonly type="hidden" value="0.00" no_border_txt name="orderA" id="orderA"/>
            </table>

            <table class="table_query" width=100% border="0" align="center"
                   cellpadding="1" cellspacing="1">
                <tr>
                    <td align="right">要求最终发运日期:</td>
                    <td align="left">
                        <input name="planDate" id="planDate" type="text" class="short_txt" datatype="1,is_date,10"
                               readonly>
                        <input name='button3' value=" " type='button' class='time_ico' title="点击选择时间"
                               onclick="showcalendar(event, 'planDate', false);"/>
                        <font color="RED">*</font>
                        <input type="hidden" id="now" value="${now}"/>
                    </td>
                    <td align="right">发运方式:</td>
                    </td>
                    <td>
                        <script type="text/javascript">
                            genSelBox("OUT_TYPE", <%=Constant.PART_GX_ORDER_OUT_TYPE%>, "<%=Constant.PART_GX_ORDER_OUT_TYPE_01%>", false, "short_sel", "", "false", '');
                        </script>
                    </td>
                </tr>
                <tr>
                    <td align="right">备注说明:</td>
                    <td colspan="3"><textarea id="REMARK2" name="REMARK2" cols="50" rows="3">${mainMap.REMARK}</textarea></td>
                </tr>
            </table>
        </FIELDSET>

        <table id="file" class="table_list" border="0"
               style="BORDER-RIGHT: #859aff 1px solid; BORDER-TOP:#859aff 1px solid; BORDER-LEFT:#859aff 1px solid; BORDER-BOTTOM:#859aff 1px solid;border-color: #859aff;"
               cellSpacing=1 cellPadding=1 width="100%">
            <tr>
                <th colspan="15" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif"/>配件信息 <span
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
                <td>当前库存</td>
                <td>提报数量</td>
                <td>已审核数量</td>
                <td>审核数量<font color="RED">*</font></td>
                <td>单价<font color="blue">(元)</font></td>
                <td>金额<font color="blue">(元)</font></td>
                <td>包装号<font color="RED">*</font></td>
                <td>备注</td>
            </tr>
            <c:forEach items="${detailList}" var="data">
                <tr class="table_list_row1">
                    <td align="center">
                        <script type="text/javascript">
                            getCb(${data.PART_ID});
                        </script>
                    </td>
                    <td align="center">&nbsp;
                        <script type="text/javascript">
                            getIdx();
                        </script>
                    </td>
                    <td align="center">
                        <c:out value="${data.PART_OLDCODE}"/>
                        <input id="partOldCode_${data.PART_ID}" name="partOldCode_${data.PART_ID}" type="hidden"
                               value="${data.PART_OLDCODE}"/>
                    </td>
                    <td align="center">
                        <c:out value="${data.PART_CNAME}"/>
                        <input id="partCname_${data.PART_ID}" name="partCname_${data.PART_ID}" type="hidden"
                               value="${data.PART_CNAME}"/>
                    </td>
                    <td align="center">
                        <c:out value="${data.PART_CODE}"/>
                        <input id="partCode_${data.PART_ID}" name="partCode_${data.PART_ID}" type="hidden"
                               value="${data.PART_CODE}"/>
                    </td>
                    <td>&nbsp;<c:out value="${data.MIN_PACKAGE}"/><input id="minPackage_${data.PART_ID}"
                                                                         name="minPackage_${data.PART_ID}" type="hidden"
                                                                         value="${data.MIN_PACKAGE}"/></td>
                    <td>&nbsp;<c:out value="${data.UNIT}"/>
                        <input id="unit_${data.PART_ID}" name="unit_${data.PART_ID}" type="hidden"
                               value="${data.UNIT}"/>
                    </td>
                    <td>&nbsp;<input id="stockQty_${data.PART_ID}" name="stockQty_${data.PART_ID}" readonly
                                     style="width:90px;border:0px;background-color:#F3F4F8;text-align:center;"
                                     type="text"
                                     value="${data.STOCK_QTY}"/></td>
                    <td>&nbsp;<input id="buyQty_${data.PART_ID}" name="buyQty_${data.PART_ID}" readonly
                                     style="width:90px;border:0px;background-color:#F3F4F8;text-align:center;"
                                     type="text"
                                     value="${data.BUY_QTY}"/></td>
                    <td>&nbsp;<input id="checkQty_${data.PART_ID}" name="checkQty_${data.PART_ID}" readonly
                                     style="width:90px;border:0px;background-color:#F3F4F8;text-align:center;"
                                     type="text"
                                     value="${data.checkQty}"/></td>
                    <td>&nbsp;<input id="saleQty_${data.PART_ID}" name="saleQty_${data.PART_ID}" type="text"
                                     class="short_txt"
                                     value="${data.BUY_QTY-data.checkQty}" style="background-color: #ffff80;text-align:center"
                                     onchange="validateQty(this,'${data.BUY_PRICE}','${data.PART_ID}')"></td>

                    <td>&nbsp;
                        <input id="buyPrice_${data.PART_ID}" name="buyPrice_${data.PART_ID}" readonly
                               style="width:90px;border:0px;background-color:#F3F4F8;text-align:center;" type="text"
                               value="${data.BUY_PRICE}"/>
                    </td>
                    <td>&nbsp;
                        <input id="buyAmount_${data.PART_ID}" name="buyAmount_${data.PART_ID}" readonly
                               style="width:90px;border:0px;background-color:#F3F4F8;text-align:center;" type="text"
                               value=""/>
                        <script type="text/javascript">
                            $('buyAmount_${data.PART_ID}').value = formatNum((parseFloat(unFormatNum(${data.BUY_QTY-data.checkQty})) * parseFloat(unFormatNum('${data.BUY_PRICE}'))).toFixed(2));
                        </script>
                    </td>
                    <td>&nbsp;<input id="pkgNo_${data.PART_ID}" name="pkgNo_${data.PART_ID}" type="text"
                                     class="short_txt"
                                     value="" style="background-color: #ffff80;text-align:center"></td>
                    <td>&nbsp;<input id="remark_${data.PART_ID}" name="remark_${data.PART_ID}" type="text"
                                     class="normal_txt"
                                     value="" style="background-color: #ffff80;text-align:center"></td>
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
                    <input class="long_btn" type="button" value="生成发运计划" onclick="createSalesOrderConfirm();"/>
                    &nbsp;&nbsp;
                    <input class="normal_btn" type="button" value="返 回" name="back" id="back" onclick="goBack()"/></td>
            </tr>
        </table>
    </div>
</form>
</body>
</html>