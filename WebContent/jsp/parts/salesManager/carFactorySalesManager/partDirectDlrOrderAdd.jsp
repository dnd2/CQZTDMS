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
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>配件委托订单提报-新增</title>
    <link href="<%=contextPath%>/style/calendar.css" type="text/css" rel="stylesheet"/>
    <script type="text/javascript">
        var myPage;
        var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrder/showPartBase.json";
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
            {header: "单位", dataIndex: 'UNIT', align: 'center'},
            {header: "采购数量", align: 'center', renderer: getSaleText, dataIndex: 'PART_ID'},
            {header: "服务商库存", dataIndex: 'ITEM_QTY', align: 'center'},
            {header: "最小包装量", dataIndex: 'MIN_PACKAGE', align: 'center'},
            {header: "订购单价", dataIndex: 'SALE_PRICE1', align: 'center', renderer: getDefaultValue},
            {header: "上级库存量", dataIndex: 'UPORGSTOCK', align: 'center'},
            {header: "是否紧缺", dataIndex: 'IS_LACK', align: 'center', renderer: inputText},
            {header: "是否可替代", dataIndex: 'IS_REPLACED', align: 'center', renderer: inputText}

        ];
        function getSaleText(value) {
            return "<input name='partSalesQty_" + value + "' id='partSalesQty_" + value + "' type='text' value='' onchange='onchangeVlidateSaleQty(this)' style='background-color:#FFFFCC;text-align:center' class='short_txt'   >";
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
            $('#Amount')[0].value = formatNum((parseFloat(unFormatNum($('#Amount')[0].value)) + parseFloat(unFormatNum(obj.value))).toFixed(2));
        }
        function onchangeVlidateSaleQty(obj) {
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
        function cleanOption(id) {
            var delOpArr = [];
            for (var i = 1; i < $(id).options.length; i++) {
                $(id).options[1] = null;
            }
        }
        function getBrand(id) {
            var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrder/getBrand.json?venderId=" + id;
            makeNomalFormCall(url, getBrandResult, 'fm');
        }
        function getBrandResult(jsonObj) {
            cleanOption("brand");
            if (jsonObj != null) {
                var brandList = jsonObj.brandList;
                var error = jsonObj.error;
                var exceptions = jsonObj.Exception;
                if (error) {
                    MyAlert(error);
                } else if (exceptions) {
                    MyAlert(exceptions.message);
                }
                for (var i = 0; i < brandList.length; i++) {
                    var name = brandList[i].BRAND;
                    var brandOption = new Option(name, name);
                    $('brand').add(brandOption);
                }
            }
        }
        //打开、收起配件按钮
        function addPartDiv() {
            var partDiv = document.getElementById("partDiv");
            var addPartViv = document.getElementById("addPartViv");
            if ($('#SELLER_ID')[0].value == "") {
                MyAlert("请先选择销售单位");
                return;
            }

            /*if($('brand').value == ""){
             MyAlert("请先选择品牌");
             return;
             }*/
            if (partDiv.style.display == "block") {
                addPartViv.value = "增加";
                partDiv.style.display = "none";
            } else {
                addPartViv.value = "收起";
                partDiv.style.display = "block";
                __extQuery__(1);
            }
        }
        function getDefaultValue(value, meta, record) {
            if (record.data.SALE_PRICE1 == null) {
                return "<font color='RED'>无订购单价</font>";
            } else {
                return record.data.SALE_PRICE1;
            }
        }
        function inputText(value, meta, record) {
            var info = getItemValue(value);
            return "<input type='hidden' value='" + value + "' />" + info;
        }
        function seled(value, meta, record) {
            if (record.data.SALE_PRICE1 != null) {
                return "<input type='checkbox' value='" + value + "' name='ck' onclick='cPartCb()' id='ck' />";
            } else {
                return "<input type='checkbox' disabled value='" + value + "' name='ck' id='ck' />";
            }
        }
        function cPartCb() {
            var flag = true;
            var ck = document.getElementsByName("ck");
            for (var i = 0; i < ck.length; i++) {
                if (!ck[i].checked && !ck[i].disabled) {
                    flag = false;
                }
            }
            document.getElementById("ckbAll").checked = flag;
        }
        function deleteTblRow(obj) {
            var idx = obj.parentElement.parentElement.rowIndex;
            var tbl = document.getElementById('file');
            tbl.deleteRow(idx);
            countAll();
            reNumIdx();
        }
        function reNumIdx() {
            var tbl = document.getElementById("file");
            for (var i = 2; i < tbl.rows.length; i++) {
                tbl.rows[i].cells[1].innerHTML = (i - 1);
            }
        }

        //选择经销商(1) 接收单位(2)地址(3)
        function selSales(inputId, inputCode, inputName, inputLinkMan, inputTel, inputPostCode, inputStation, dealerId, type) {
            if (type == 2 && $('#SELLER_ID')[0].value == "") {
                MyAlert("请先选择销售单位!");
                return;
            }
            if (type == 3 && $('#RCV_ORGID')[0].value == "") {
                MyAlert("请先选择接收单位!");
                return;
            }

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
            OpenHtmlWindow(g_webAppName + '/jsp/parts/salesManager/carFactorySalesManager/selSales.jsp?dealerId=' + dealerId + '&type=' + type + '&inputName=' + inputName + '&inputId=' + inputId + '&inputCode=' + inputCode + '&inputLinkMan=' + inputLinkMan + '&inputTel=' + inputTel + '&inputPostCode=' + inputPostCode + '&inputStation=' + inputStation, 700, 400);
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
                        var unit = mt.rows[i].cells[4].innerText;  //单位
                        var saleQty = mt.rows[i].cells[5].firstChild.value;  //销售数量
                        var itemQty = mt.rows[i].cells[6].innerText;  //当前库存
                        var minPackage = mt.rows[i].cells[7].innerText;  //最小包装量
                        var buyPrice = mt.rows[i].cells[8].innerText;  //订购单价
                        var upOrgStock = mt.rows[i].cells[9].innerText;  //上级库存量
                        var isLack = mt.rows[i].cells[10].firstChild.value;  //是否紧缺
                        var isReplaced = mt.rows[i].cells[11].firstChild.value;  //是否可替代
                        
                      	//判断订购单价
                        if(parseFloat(buyPrice) <= 0){
                        	 MyAlert("配件[" + partCname + "]订购单价需大于0!</br>");
                        	 continue;
                        }
                        addCell(partId, partOldcode, partCname, unit, itemQty, minPackage, buyPrice, upOrgStock, isLack, isReplaced, saleQty);
                    } else {
                        MyAlert("配件：" + mt.rows[i].cells[2].innerText + " 已存在!</br>");
                        break;
                    }
                }
            }
            countAll();
        }

        function addCell(partId, partOldcode, partCname, unit, itemQty, minPackage, buyPrice, upOrgStock, isLack, isReplaced, saleQty) {
            var tbl = document.getElementById('file');
            var rowObj = tbl.insertRow(tbl.rows.length);
            if (tbl.rows.length % 2 == 0) {
                rowObj.className = "table_list_row2";
            } else {
                rowObj.className = "table_list_row1";
            }
            if (upOrgStock == "Y") {
                upOrgStock = "Y";
            } else {
                upOrgStock = "N";
            }
            //获取折扣率
            var discount = document.getElementById("DISCOUNT").value;

            var buyPrice1 = formatNum((parseFloat(unFormatNum(buyPrice)) * parseFloat(discount) * discount).toFixed(2));

            var hiddenHtml = "<input type='hidden' name='stockQty_" + partId + "' id='stockQty_" + partId + "' value='" + itemQty + "' >";
            var hiddenHtmlIsLack = "<input type='hidden' name='isLack_" + partId + "' id='isLack_" + partId + "' value='" + isLack + "' >";
            var hiddenHtmlIsReplaced = "<input type='hidden' name='isReplaced_" + partId + "' id='isReplaced_" + partId + "' value='" + isReplaced + "' >";
            createCells(rowObj, 0, ("cell_" + (tbl.rows.length - 1)), "cb", "", "", "checkbox", partId, true, false, "");
            createCells(rowObj, 1, "", "", "", "", "idx", (tbl.rows.length - 1), false, false, "");
            createCells(rowObj, 2, ("partOldcode_" + partId), ("partOldcode_" + partId), "", "", "hidden", partOldcode, false, false, "");
            createCells(rowObj, 3, ("partCname_" + partId), ("partCname_" + partId), "", "", "hidden", partCname, false, false, "");
            //createCells(rowObj, 4, ("partCode_" + partId), ("partCode_" + partId), "", "", "hidden", partCode, false, false, "");
            createCells(rowObj, 4, ("unit_" + partId), ("unit_" + partId), "", "", "hidden", unit, false, false, "");
            createCells(rowObj, 5, ("minPackage_" + partId), ("minPackage_" + partId), "", "", "hidden", minPackage, false, false, "");
            createCells(rowObj, 6, ("buyQty_" + partId), ("buyQty_" + partId), "countMoney(this,'" + buyPrice + "'," + partId + ")", "background-color:#FFFFCC;text-align:center", "text", saleQty, false, false, "");
            createCells(rowObj, 7, ("buyPrice_" + partId), ("buyPrice_" + partId), "", "", "hidden", buyPrice, false, false, "");
            createCells(rowObj, 8, ("buyPrice1_" + partId), ("buyPrice1_" + partId), "", "", "hidden", buyPrice1, false, false, "");
            createCells(rowObj, 9, ("buyAmount_" + partId), ("buyAmount_" + partId), "", "", "hidden", "0", false, false, "");
            createCells(rowObj, 10, ("upOrgStock_" + partId), ("upOrgStock_" + partId), "", "", "hidden", upOrgStock, false, false, hiddenHtmlIsReplaced);
            createCells(rowObj, 11, ("remark_" + partId), ("remark_" + partId), "", "", "text", "", false, false, hiddenHtmlIsLack);
            createCells(rowObj, 12, "", "", "", "", "button", "", false, true, hiddenHtml);

            if (saleQty != "") {
                var money = (parseFloat(unFormatNum(buyPrice)) * parseFloat(saleQty) * discount).toFixed(2);
                rowObj.cells[9].innerHTML = createCellsInnerHtml(("buyAmount_" + partId), ("buyAmount_" + partId), "", "", "hidden", formatNum(money), false, false, "");
            }
        }
        //校验是否重复生成
        function validateCell(value) {
            var flag = true;
            var tbl = document.getElementById('file');
            for (var i = 1; i <= tbl.rows.length - 1; i++) {
                if (value == tbl.rows[i].cells[0].firstChild.value) {
                    flag = false;
                    break;
                }
            }
            return flag;
        }


        //生成CELL
        function createCells(obj, idx, id, name, onchangeEvent, style, type, value, trHFlag, trEFlag, hiddenHtml) {
            var cell = obj.insertCell(idx);
            cell.innerHTML = createCellsInnerHtml(id, name, onchangeEvent, style, type, value, trHFlag, trEFlag, hiddenHtml);

        }

        //计算所有CHECK的金额
        function countAll() {
            var flag = true;
            var amountCount = parseFloat(0);
            var cb = document.getElementsByName("cb");
            for (var i = 0; i < cb.length; i++) {
                if ("" != cb[i].value && null != document.getElementById("buyAmount_" + cb[i].value) && "" != document.getElementById("buyAmount_" + cb[i].value).value) {
                    //只有CHECKED的才能计算unFormatNum
                    if (cb[i].checked) {
                        amountCount = (parseFloat(unFormatNum(amountCount)) + parseFloat(unFormatNum(document.getElementById("buyAmount_" + cb[i].value).value))).toFixed(2);
                    }
                }
                if (!cb[i].checked) {
                    flag = false;
                }
            }
            document.getElementById("ckAll").checked = flag;
            document.getElementById("Amount").value = formatNum(amountCount);
            getFreight(amountCount);
        }
        //生成CELL中的HTML
        //&quot;
        function createCellsInnerHtml(id, name, onchangeEvent, style, type, value, trHFlag, trEFlag, hiddenHtml) {
            value = (value + "").replace(new RegExp("\"", "gm"), "&quot;");
            var tdStrHead = trHFlag == true ? '<tr><td align="center" nowrap>' : '<td align="center" nowrap>';
            var tdStrEnd = trEFlag == true ? '</td></TR>' : '</td>';
            var inputHtml = "";
            if (type == 'button') {
                inputHtml = '<input  type="button" class="normal_btn"  name="queryBtn" value="删除" onclick="deleteTblRow(this);" />';
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
        //计算金额
        function countMoney(obj, price, partId) {
            price = unFormatNum(price);
            //获取折扣率
            var discount = document.getElementById("DISCOUNT").value;
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
            var money = (parseFloat(unFormatNum(price)) * parseFloat(value) * discount).toFixed(2);
            tbl.rows[idx].cells[9].innerHTML = createCellsInnerHtml(("buyAmount_" + partId), ("buyAmount_" + partId), "", "", "hidden", formatNum(money), false, false, "");
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
            document.getElementById("Amount").value = formatNum(amountCount);
            getFreight(amountCount);
        }
        //清除数据重新计算
        function cleanData(partId, idx) {
            var tbl = document.getElementById('file');
            tbl.rows[idx].cells[9].innerHTML = createCellsInnerHtml(("buyAmount_" + partId), ("buyAmount_" + partId), "", "", "hidden", "0", false, false, "");
            var amountCount = parseFloat(0);
            var cb = document.getElementsByName("cb");
            for (var i = 0; i < cb.length; i++) {
                if ("" != cb[i].value && null != document.getElementById("buyAmount_" + cb[i].value) && "" != document.getElementById("buyAmount_" + cb[i].value).value) {
                    amountCount = (parseFloat(amountCount) + parseFloat(unFormatNum(document.getElementById("buyAmount_" + cb[i].value).value))).toFixed(2);
                }
            }
            document.getElementById("Amount").value = formatNum(amountCount);
        }

        //保存订单确认
        function saveOrderConfirm() {
            MyConfirm('确定保存订单?', saveOrder, []);
            var cb = document.getElementsByName("cb");
            for (var i = 0; i < cb.length; i++) {
                cb[i].disabled = false;
            }
        }
        function validateFm() {
            var msg = "";
            if ($("#SELLER_ID")[0].value == "") {
                msg += "请选择销售单位!</br>";
            }

            if ($("#RCV_ORGID")[0].value == "") {
                msg += "请选择接收单位!</br>";
            }

            if ($("#ADDR_ID")[0].value == "") {
                msg += "请选择接收地址!</br>";
            }

            if ($("#TRANS_TYPE")[0].value == "") {
                msg += "请选择发运方式!</br>";
            }

            if ($("#PAY_TYPE")[0].value == "") {
                msg += "请选择付款方式!</br>";
            }

            //提交时,将其属性设置成DISABLED,从而达到过滤选择的目的
            var cb = document.getElementsByName("cb");
            if (cb.length <= 0) {
                msg += "请添加订货明细!</br>";
            }
            for (var i = 0; i < cb.length; i++) {
                if (cb[i].checked) {
                    cb[i].disabled = false;
                    //需要校验计划量是否填写
                    if (document.getElementById("buyQty_" + cb[i].value).value == "" || parseFloat(document.getElementById("buyQty_" + cb[i].value).value) == parseFloat(0)) {
                        msg += "请填写第" + (i + 1) + "行的订货数量!</br>";
                    } else {
                        var mod = document.getElementById("buyQty_" + cb[i].value).value % document.getElementById("minPackage_" + cb[i].value).value;
                        if (mod != 0) {
                            msg += "第" + (i + 1) + "行的订货数量必须为最小包装量的整数倍!</br>";
                            flag = true;
                        }
                    }
                } else {
                    cb[i].disabled = true;
                }
            }
            var flag = false;
            for (var i = 0; i < cb.length; i++) {
                if (cb[i].checked) {
                    if (document.getElementById("buyPrice1_" + cb[i].value).value == "" || parseFloat(document.getElementById("buyPrice1_" + cb[i].value).value) == 0) {
                        msg += "配件" + document.getElementById("partCname_" + cb[i].value).value + "单价不能为0!";
                    }else if(document.getElementById("buyPrice1_" + cb[i].value).value == "" || parseFloat(document.getElementById("buyPrice1_" + cb[i].value).value) < 0){
                    	msg += "配件" + document.getElementById("partCname_" + cb[i].value).value + "单价不能为负数!";
                    }
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                msg += "请选择订货明细!</br>";
            }
            if (msg != "") {
                MyAlert(msg);
                for (var i = 0; i < cb.length; i++) {
                    cb[i].disabled = false;
                }

                return false;
            }

            return true;
        }
        //提报并保存订单确认
        function repOrderConfirm() {
            var msg = '确定提报并保存订单?';
            if (parseFloat(unFormatNum($('freight').value)) > parseFloat(0)) {
                msg = "<font color='red'>运费：" + $('freight').value + "</font></br>" + msg;
            }
            MyConfirm(msg, repOrder, []);
             
            var cb = document.getElementsByName("cb");
            for (var i = 0; i < cb.length; i++) {
                cb[i].disabled = false;
            }
        }
        //保存订单
        function saveOrder() {
            disableAllBtn();
            document.getElementById("state").value =<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_01%>;
            var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrder/saveOrder.json";
            makeNomalFormCall(url, getResult, 'fm');
        }
        //提报订单
        function repOrder() {
            disableAllBtn();
            document.getElementById("state").value =<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02%>;
            var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrder/saveOrder.json";
            makeNomalFormCall(url, getResult, 'fm');
        }
        function getResult(jsonObj) {
            enableAllBtn();
            if (jsonObj != null) {
                var success = jsonObj.success;
                var error = jsonObj.error;
                var exceptions = jsonObj.Exception;
                if (success) {
                    MyAlert(success);
                    window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrder/partDirectDlrOrderInit.do';
                } else if (error) {
                    MyAlert(error);
                } else if (exceptions) {
                    MyAlert(exceptions.message);
                }
            }
        }
        //显示余额
        function showAcount(accountId, accountSum, accountKy, accountDj) {
            if (accountId != "") {
                document.getElementById("accountSumNameTd").innerHTML = "账户余额：";
                document.getElementById("accountSumTextTd").innerHTML = '<input id="accountSum" style="border:0px;background-color:#F3F4F8;"  name="accountSum" type="text" class="middle_txt" value="' + accountSum + '" readonly /><font color="blue">元</font>';
                document.getElementById("accountKyNameTd").innerHTML = "可用金额：";
                document.getElementById("accountKyTextTd").innerHTML = '<input id="accountKy" style="border:0px;background-color:#6F9;"  name="accountKy" type="text" value="' + accountKy + '" class="middle_txt" readonly /><font color="blue">元</font>';
                document.getElementById("accountDjNameTd").innerHTML = "冻结金额：";
                document.getElementById("accountDjTextTd").innerHTML = '<input id="accountDj"  name="accountDj" type="text" style="border:0px;background-color:#F3F4F8;" value="' + accountDj + '" class="middle_txt" readonly /><font color="blue">元</font>';
                document.getElementById("accountId").value = accountId;
            } else {
                document.getElementById("accountSumNameTd").innerHTML = "";
                document.getElementById("accountSumTextTd").innerHTML = "";
                document.getElementById("accountKyNameTd").innerHTML = "";
                document.getElementById("accountKyTextTd").innerHTML = "";
                document.getElementById("accountDjNameTd").innerHTML = "";
                document.getElementById("accountDjTextTd").innerHTML = "";
                document.getElementById("accountId").value = "";
            }
        }
        //返回
        function goBack() {
            disableAllBtn();
            window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrder/partDirectDlrOrderInit.do';
        }

        function showUpload() {
            if ($('#SELLER_ID')[0].value == "") {
                MyAlert("请先选择销售单位");
                return;
            }
            var uploadDiv = document.myIframe.uploadDiv;
            if (uploadDiv.style.display == "block") {
                uploadDiv.style.display = "none";
            } else {
                uploadDiv.style.display = "block";
            }
        }

        function exportExcelTemplate() {
            fm.action = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrder/exportExcelTemplate.do";
            fm.submit();
        }

        function checkFormInput(handle) {
            if (validateobjarr.length == 0) {
                return true;
            }
            clearTip();
            //modified by and.ten@tom.com 解决handle参数可能是form id字符串，也可能是form对象情况 begin
            var controlList;
            if (typeof(handle) == "object") {
                controlList = handle.getElementsByTagName("INPUT");
            } else {
                var form = document.getElementById(handle);
                controlList = form.getElementsByTagName("INPUT");
            }
            //end
            var controlObj;
            var rest = true;
            var issele = null; // select标记ID=
            for (var i = 0; i < controlList.length; i++) {
                var tipid = getTip();
                if (tipid == null) {
                    return false;
                }
                controlObj = controlList[i];
                if (isControlVisible(controlObj)) {
                    if (controlObj.type == 'text' || controlObj.type == 'password' || controlObj.type == 'textarea' || controlObj.type == 'select-one' || controlObj.type == 'file' || controlObj.type == 'checkbox') {
                        if (controlObj.id != "uploadFile1") {
                            controlObj.value = controlObj.value.trim();
                            if (controlObj.datatype != null) {
                                if (controlObj.type == 'checkbox' && controlObj.checked == false) {
                                    showTip(controlObj, "此处不能为空.", tipid);
                                    issele == null ? (document.getElementById(controlObj).select(), issele = controlObj.id) : "";
                                    rest = false;
                                    continue;
                                } else if (controlObj.type == 'checkbox' && controlObj.checked == true) {
                                    rest = true;
                                    continue;
                                }

                                tmptypeStr = controlObj.datatype;
                                str = tmptypeStr.split(",");
                                nullAble = str[0];
                                typeStr = str[1];
                                maxLength = str[2];
                                errm = controlObj.errmsg;

                                minLength = "0";
                                if (str.length == 4) {
                                    minLength = str[3];
                                }

                                if (controlObj.value == null || controlObj.value == "") {
                                    if (nullAble == "0") {
                                        hideTip(tipid);
                                        if (controlObj.hint == null || controlObj.hint == "") {
                                            if (controlObj.type == 'select-one' && getIEVer() <= 6) {
                                                showTip(controlObj, "此处不能为空.CL30", tipid);
                                            } else {
                                                showTip(controlObj, "此处不能为空.", tipid);
                                            }
                                            issele == null ? (document.getElementById(controlObj).select(), issele = controlObj.id) : "";
                                            rest = false;
                                            continue;
                                        }
                                    }
                                } else {
                                    if (typeStr == "is_yuan" || typeStr == "is_wan") {
                                        maxLength = 1000;
                                    }
                                    if (typeStr == "is_phone") {
                                        maxLength = 15;
                                    }

                                    if (typeStr == "is_date") {
                                        if (controlObj.group) {
                                            var gr = controlObj.group;
                                            var sdd = gr.split(",");
                                            var startd = document.getElementById(sdd[0]).value;
                                            var endd = document.getElementById(sdd[1]).value;
                                            if (startd != null && startd != "" && endd != null && endd != "") {
                                                if (!checkDate(startd, endd)) {
                                                    hideTip(tipid);
                                                    showTip(document.getElementById(sdd[0]), "开始时间不能大于结束时间.", tipid);
                                                    issele == null ? (document.getElementById(controlObj).select(), issele = controlObj.id) : "";
                                                    rest = false;
                                                    continue;
                                                }
                                            }
                                        }
                                    }
                                    //wjb add by 2010-07-12 验证日期不能大于当前日期 begin
                                    if (typeStr == "is_date_now") {
                                        var valDate = controlObj.value;
                                        var nowDate = (new Date()).Format("yyyy-MM-dd");
                                        if (valDate != null && valDate != "" && nowDate != null && nowDate != "") {
                                            if (!checkDate(valDate, nowDate)) {
                                                hideTip(tipid);
                                                showTip(controlObj, "不能大于当前日期.", tipid);
                                                issele == null ? (document.getElementById(controlObj).select(), issele = controlObj.id) : "";
                                                rest = false;
                                                continue;
                                            }
                                        }
                                    }
                                    //wjb add by 2010-07-12 end

                                    if (typeStr == "is_textarea") {
                                        if (controlObj.value.length > maxLength) {
                                            hideTip(tipid);
                                            showTip(controlObj, "不能超过" + maxLength + "个字符.", tipid);
                                            issele == null ? (document.getElementById(controlObj).select(), issele = controlObj.id) : "";
                                            rest = false;
                                            continue;
                                        }
                                    }

                                    if (typeStr == "is_double") {
                                        if (controlObj.decimal) {
                                            var tempMsg = eval("" + typeStr + "(controlObj);");
                                            if (tempMsg == true) {
                                                var tt2 = checkDecimal(controlObj.value, controlObj.decimal);
                                                if (tt2 != true) {
                                                    hideTip(tipid);
                                                    showTip(controlObj, "" + tt2, tipid);
                                                    issele == null ? (document.getElementById(controlObj).select(), issele = controlObj.id) : "";
                                                    rest = false;
                                                    continue;
                                                }
                                            } else {
                                                hideTip(tipid);
                                                showTip(controlObj, "" + tempMsg, tipid);
                                                issele == null ? (document.getElementById(controlObj).select(), issele = controlObj.id) : "";
                                                rest = false;
                                                continue;
                                            }
                                        }
                                    }

                                    if (controlObj.value.length < minLength) {
                                        hideTip(tipid);
                                        showTip(controlObj, "不能少于" + minLength + "个字符.", tipid);
                                        issele == null ? (document.getElementById(controlObj).select(), issele = controlObj.id) : "";
                                        rest = false;
                                        continue;
                                    } else if (controlObj.value.length > maxLength) {
                                        hideTip(tipid);
                                        showTip(controlObj, "不能超过" + maxLength + "个字符.", tipid);
                                        issele == null ? (document.getElementById(controlObj).select(), issele = controlObj.id) : "";
                                        rest = false;
                                        continue;
                                    } else {
                                        var tempMsg = eval("" + typeStr + "(controlObj);");
                                        if (tempMsg != true) {
                                            hideTip(tipid);
                                            if (errm != null && errm != "") {
                                                showTip(controlObj, errm, tipid);
                                            } else {
                                                showTip(controlObj, "" + tempMsg, tipid);
                                            }
                                            issele == null ? (document.getElementById(controlObj).select(), issele = controlObj.id) : "";
                                            rest = false;
                                            continue;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return rest;
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

        function genMySelBoxExp(id, type, selectedKey, setAll, _class_, _script_, nullFlag, expStr) {
            var str = "";
            var arr;
            if (expStr.indexOf(",") > 0)
                arr = expStr.split(",");
            else {
                expStr = expStr + ",";
                arr = expStr.split(",");
            }
            str += "<select id='" + id + "' name='" + id + "' class='" + _class_ + "' " + _script_;
            if (nullFlag && nullFlag == "true") {
                str += " datatype='0,0,0' ";
            }
            str += "  ";
            if (setAll) {
                str += genDefaultOpt();
            }
            for (var i = 0; i < codeData.length; i++) {
                var flag = true;
                for (var j = 0; j < arr.length; j++) {
                    if (codeData[i].codeId == arr[j]) {
                        flag = false;
                    }
                }
                //过滤调拨
                if (codeData[i].codeId ==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_05%>) {
                    continue;
                }
                if (codeData[i].type == type && flag) {
                    str += "<option " + (codeData[i].codeId == selectedKey ? "selected" : "") + " value='" + codeData[i].codeId + "' title = '" + codeData[i].codeDesc + "' >" + codeData[i].codeDesc + "</option>";
                }
            }
            str += "</select>";
            document.write(str);
        }

        function checkAll(obj) {
            var cb = document.getElementsByName("cb");
            for (var i = 0; i < cb.length; i++) {
                cb[i].checked = obj.checked;
            }
            countAll();
        }
        function changeOrderType(obj) {
            //如果直发=委托
            if (obj.value ==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04%>) {
                //如果上级机构不是车厂   就是供应中心    如果上级机构是供应中心  转换
                if (<%=Constant.OEM_ACTIVITIES%>!="${dataMap.parentId}"){
                    directTransport();
                }
            } else {
                if (<%=Constant.OEM_ACTIVITIES%>!="${dataMap.parentId}"){
                    //temp如果为直发  如果是直发   还原数据  如果不是   不处理
                    if (document.getElementById("temp").value ==<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04%>) {
                        document.getElementById("dlbtn1").disabled = false;
                        document.getElementById("SELLER_ID").value = "";
                        document.getElementById("SELLER_CODE").value = "";
                        document.getElementById("SELLER_NAME").value = "";
                        document.getElementById("dealerId").value = "${dataMap.dealerId}";
                        document.getElementById("dealerCode").value = "${dataMap.dealerCode}";
                        document.getElementById("dealerName").value = "${dataMap.dealerName}";
                        document.getElementById("RCV_ORG").value = "";
                        document.getElementById("RCV_CODE").value = "";
                        document.getElementById("RCV_ORGID").value = "";
                        document.getElementById("dlbtn2").disabled = false;
                        document.getElementById("accountSumNameTd").innerHTML = "";
                        document.getElementById("accountSumTextTd").innerHTML = "";
                        document.getElementById("accountKyNameTd").innerHTML = "";
                        document.getElementById("accountKyTextTd").innerHTML = "";
                        document.getElementById("accountDjNameTd").innerHTML = "";
                        document.getElementById("accountDjTextTd").innerHTML = "";
                        document.getElementById("accountId").value = "";
                        changeSeller();
                    }
                }
            }
            document.getElementById("temp").value = obj.value;
        }
        function directTransport() {
            //将销售单位设置为车厂 并设置为不可修改
            document.getElementById("SELLER_ID").value = "${dataMap.oemId}";
            document.getElementById("SELLER_CODE").value = "${dataMap.oemCode}";
            document.getElementById("SELLER_NAME").value = "${dataMap.oemName}";
            document.getElementById("dlbtn1").disabled = true;
            //将订购单位改为 供应中心
            document.getElementById("dealerId").value = "${dataMap.parentId}";
            document.getElementById("dealerCode").value = "${dataMap.parentCode}";
            document.getElementById("dealerName").value = "${dataMap.parentName}";

            //接收单位改为自己 并设置为不可修改
            document.getElementById("RCV_ORG").value = "${dataMap.dealerName}";
            document.getElementById("RCV_CODE").value = "${dataMap.dealerCode}";
            document.getElementById("RCV_ORGID").value = "${dataMap.dealerId}";
            document.getElementById("dlbtn2").disabled = true;
            changeSeller();

            //显示供应中心在车厂的账户
            document.getElementById("accountSumNameTd").innerHTML = "账户余额：";
            document.getElementById("accountSumTextTd").innerHTML = '<input id="accountSum" style="border:0px;background-color:#F3F4F8;"  name="accountSum" type="text" class="middle_txt" value="' + ${acountMap.ACCOUNT_SUM} +'" readonly /><font color="blue">元</font>';
            document.getElementById("accountKyNameTd").innerHTML = "可用金额：";
            document.getElementById("accountKyTextTd").innerHTML = '<input id="accountKy" style="border:0px;background-color:#6F9;"  name="accountKy" type="text" value="' + ${acountMap.ACCOUNT_KY} +'" class="middle_txt" readonly /><font color="blue">元</font>';
            document.getElementById("accountDjNameTd").innerHTML = "冻结金额：";
            document.getElementById("accountDjTextTd").innerHTML = '<input id="accountDj"  name="accountDj" type="text" style="border:0px;background-color:#F3F4F8;" value="' + ${acountMap.ACCOUNT_DJ} +'" class="middle_txt" readonly /><font color="blue">元</font>';
            document.getElementById("accountId").value = "${acountMap.ACCOUNT_ID}";

        }
        function changeSeller() {
            if (document.getElementById("sellerTemp").value != document.getElementById("SELLER_ID").value) {
                var tbl = document.getElementById("file");
                for (var i = (tbl.rows.length - 1); i >= 2; i--) {
                    tbl.deleteRow(i);
                }
            }
            document.getElementById("sellerTemp").value = document.getElementById("SELLER_ID").value;
            var partDiv = document.getElementById("partDiv");
            var addPartViv = document.getElementById("addPartViv");

            if (partDiv.style.display == "block") {
                if ($('SELLER_ID').value == "") {
                    MyAlert("请先选择销售单位");
                    addPartViv.value = "增加";
                    partDiv.style.display = "none";
                    return;
                }
                __extQuery__(1);
            }
        }
       
        function initDivAndPartInfo() {
            var partDiv = document.getElementById("partDiv");
            var addPartViv = document.getElementById("addPartViv");
            addPartViv.value = "增加";
            partDiv.style.display = "none";
            var tbl = document.getElementById('file');
            //需要重置下面配件信息
            for (var i = (tbl.rows.length - 1); i >= 2; i--) {
                tbl.deleteRow(i);
            }
            countAll();
        }
        
        function changeOrderType(value) {
            initDivAndPartInfo();
            document.getElementById("Amount").value = "0";
        }

        function partReplaceValidate(code) {
            if (!validateFm()) {
                return;
            }

            if (code == "save") {
                saveOrderConfirm();
            }
            if (code == "rep") {

                repOrderConfirm();
            }
            return;


        }
        function changeBreand() {
            var tbl = document.getElementById('file');
            if ($('#SELLER_ID')[0].value == "") {
                MyAlert("请先选择销售单位");
                return;
            }
            //需要重置下面配件信息
            for (var i = (tbl.rows.length - 1); i >= 2; i--) {
                tbl.deleteRow(i);
            }
            __extQuery__(1);
            countAll();
        }
        function doQuery() {
            /* if ($('brand').value == "") {
             MyAlert("请先选择品牌");
             return;
             }*/
            __extQuery__(1);
        }
        function getFreight(amountCount) {

            disableAllBtn();
            if ($('#SELLER_ID')[0].value !=<%=Constant.OEM_ACTIVITIES%>) {
                enableAllBtn();
                $('#freight')[0].value = "0.00";
                countAllWithoutFreight();
                return;
            }
            if ($('#TRANS_TYPE')[0].value == "3") {
                enableAllBtn();
                $('#freight')[0].value = "0.00";
                countAllWithoutFreight();
                return;
            }
            var getFreightUrl = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrderCheck/getFreight.json?money=" + amountCount;
            makeNomalFormCall(getFreightUrl, getFreightResult, 'fm');
        }
        function getFreightResult(jsonObj) {
            enableAllBtn();

            if (jsonObj != null) {
                $('#freight')[0].value = jsonObj.freight;
                $('#Amount')[0].value = formatNum((parseFloat(unFormatNum($('#Amount')[0].value)) + parseFloat(unFormatNum(jsonObj.freight))).toFixed(2));

            }
        }
        function changeProduceFac() {
            var tbl = document.getElementById('file');
            var partDiv = document.getElementById("partDiv");
            //需要重置下面配件信息
            for (var i = (tbl.rows.length - 1); i >= 2; i--) {
                tbl.deleteRow(i);
            }
            if (partDiv.style.display == "block") {
                __extQuery__(1);
            }
            countAll();
        }
        function changeTrans(obj) {
            var tbl = document.getElementById("file");
            var partDiv = document.getElementById("partDiv");
            for (var i = (tbl.rows.length - 1); i >= 2; i--) {
                tbl.deleteRow(i);
            }
            if (partDiv.style.display == "block") {
                var partDiv = document.getElementById("partDiv");
                __extQuery__(1);
            }
            countAll();
        }
        function countAllWithoutFreight() {
            var flag = true;
            var amountCount = parseFloat(0);
            var cb = document.getElementsByName("cb");
            for (var i = 0; i < cb.length; i++) {
                if ("" != cb[i].value && null != document.getElementById("buyAmount_" + cb[i].value) && "" != document.getElementById("buyAmount_" + cb[i].value).value) {
                    //只有CHECKED的才能计算unFormatNum
                    if (cb[i].checked) {
                        amountCount = (parseFloat(unFormatNum(amountCount)) + parseFloat(unFormatNum(document.getElementById("buyAmount_" + cb[i].value).value))).toFixed(2);
                    }
                }
                if (!cb[i].checked) {
                    flag = false;
                }
            }
            document.getElementById("ckAll").checked = flag;
            document.getElementById("Amount").value = formatNum(amountCount);
        }


        var saveFlag = false;
        function getUploadData(flag) {
            saveFlag = flag;
            fm.action = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrOrder/getUploadExcel.do";
            fm.submit();
        }

        function getUploadResult(jsonObj) {
            var data = jsonObj.excelData;
            enableAllBtn();
            if (saveFlag) {
                $('#file')[0].style.display = "none";
            }
            for (var i = 0; i < data.length; i++) {
                var tbl = document.getElementById('file');
                var flag = true;
                for (var j = 0; j < tbl.rows.length; j++) {
                    if (data[i].PART_ID == tbl.rows[j].cells[0].firstChild.value) {
                        error += "第" + (j) + "行配件：" + data[i].PART_CNAME + " 已存在!</br>";
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    addCell(data[i].PART_ID, data[i].PART_CODE, data[i].PART_OLDCODE, data[i].PART_CNAME, data[i].UNIT == null ? "" : data[i].UNIT, data[i].ITEM_QTY == null ? "" : data[i].ITEM_QTY, data[i].MIN_PACKAGE == null ? "" : data[i].MIN_PACKAGE, data[i].SALE_PRICE1 == null ? "" : data[i].SALE_PRICE1, data[i].UPORGSTOCK == null ? "" : data[i].UPORGSTOCK, data[i].IS_LACK == null ? "" : data[i].IS_LACK, data[i].IS_REPLACED == null ? "" : data[i].IS_REPLACED, data[i].BUYQTY == null ? "0" : data[i].BUYQTY);
                    if (data[i].BUYQTY != "") {
                        document.getElementById("buyQty_" + data[i].PART_ID).value = parseFloat(data[i].BUYQTY).toFixed(0);
                        countAllWithoutFreight(document.getElementById("buyQty_" + data[i].PART_ID), data[i].SALE_PRICE1, data[i].PART_ID);
                    }
                }
            }
            if (data.length > 0) {
                countAll();
            }
            if (saveFlag) {
                saveOrder();
            }
        }
        
        $(document).ready(function(){
        	enableAllBtn();
        	$('#TRANS_TYPE')[0].value = 1;
            //初始化默认值
            var accountIdInit = $('#accountIdInit')[0].value;
            var accountSumInit = $('#accountSumInit')[0].value;
            var accountKyInit = $('#accountKyInit')[0].value;
            var accountDjInit = $('#accountDjInit')[0].value;
         
            showAcount(accountIdInit, accountSumInit, accountKyInit, accountDjInit);
        });
    </script>
</head>

<body>
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <input type="hidden" name="dealerCode" id="dealerCode" value="${dataMap.dealerCode}"/>
    <input type="hidden" name="dealerId" id="dealerId" value="${dataMap.dealerId}"/>
    <input type="hidden" name="accountId" id="accountId"/>
    <input type="hidden" name="temp" id="temp"/>
    <input type="hidden" name="sellerTemp" id="sellerTemp"/>
    <input type="hidden" name="seq" id="seq" value="">
    <input type="hidden" name="state" id="state">
    <input type="hidden" name="ORDER_TYPE" id="ORDER_TYPE" value="<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04%>"/>
    <input type="hidden" name="accountIdInit" id="accountIdInit" value="${dataMap.accountId}">
    <input type="hidden" name="accountSumInit" id="accountSumInit" value="${dataMap.accountSum}">
    <input type="hidden" name="accountKyInit" id="accountKyInit" value="${dataMap.accountKy}">
    <input type="hidden" name="accountDjInit" id="accountDjInit" value="${dataMap.accountDj}">
    <input type="hidden" name="produceFac" id="produceFac" value="<%=Constant.YIELDLY_01%>">
    <input type="hidden" name="PAY_TYPE" id="PAY_TYPE" value="<%=Constant.CAR_FACTORY_SALES_PAY_TYPE_01%>">
    <div class="wbox">
        <div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：配件管理&gt;配件采购管理&gt;配件委托订单提报&gt;新增</div>
        <div class="form-panel">
		     <h2><img class="panel-icon nav" src="<%=contextPath%>/img/subNav.gif"/>采购订单信息</h2>
		     <div class="form-body">
	        <table class="table_add" border="0" style="line-height:30px;" cellSpacing=1 cellPadding=1 width="100%">
	            <tr>
	                <td align="right" nowrap>销售单位：</td>
	                <td>${dataMap.SELLER_NAME}
	                    <input name="SELLER_NAME" class="middle_txt" id="SELLER_NAME" value="${dataMap.SELLER_NAME}"
	                           type="hidden"
	                           size="20" readonly="true"/>
	                    <input name="SELLER_CODE" class="middle_txt" id="SELLER_CODE" type="hidden"
	                           value="${dataMap.SELLER_CODE}"
	                           readonly="true"/>
	                    <input name="SELLER_ID" class="middle_txt" id="SELLER_ID" type="hidden" value="${dataMap.SELLER_ID}"
	                           readonly="true"/>
	                </td>
	                <td align="right" nowrap>付款方式：</td>
	                <td>
	                    <script type="text/javascript">
	                        genSelBoxExp("PAY_TYPE", <%=Constant.CAR_FACTORY_SALES_PAY_TYPE%>, "<%=Constant.CAR_FACTORY_SALES_PAY_TYPE_01%>", true, "u-select", "disabled", "false", '');
	                    </script>
	                </td>
	                <td align="right">配件：</td>
	                <td>
	                    <script type="text/javascript">
	                        genSelBoxExp("produceFac", <%=Constant.YIELDLY%>, "", false, "u-select", "disabled", "false", '');
	                    </script>
	                </td>
	            </tr>
	            <tr>
	                <td align="right">订货单位：</td>
	                <td>${dataMap.dealerName}
	                    <input readonly type="hidden" style="border:0px;background-color:#F3F4F8;"
	                           value="${dataMap.dealerName}" name="dealerName" id="dealerName"/></td>
	                <td align="right">订货人：</td>
	                <td>${dataMap.buyerName}
	                	<input readonly type="hidden" style="border:0px;background-color:#F3F4F8;"
	                           value="${dataMap.buyerName}"
	                           name="buyerName" id="buyerName"/></td>
	                <td align="right">订货日期：</td>
	                <td>${dataMap.now}
	                	<input readonly type="hidden" style="border:0px;background-color:#F3F4F8;" value="${dataMap.now}"
	                           name="now"
	                           id="now"/>
	                </td>
	            </tr>
	            <tr>
	                <td align="right">接收单位：</td>
	                <td colspan="3">${defaultValueMap.defaultRcvOrg}
	                    <input name="RCV_ORG" class="middle_txt" id="RCV_ORG" type="hidden"
	                           value="${defaultValueMap.defaultRcvOrg}"
	                           size="20" readonly="true"/>
	                    <input name="RCV_CODE" class="middle_txt" id="RCV_CODE" type="hidden" size="20" readonly="true"/>
	                    <input name="RCV_ORGID" class="middle_txt" id="RCV_ORGID" type="hidden"
	                           value="${defaultValueMap.defaultRcvOrgid}" size="20" readonly="true"/>
	                    <font color="RED">*</font>
	                </td>
	                <td align="right" style="display: none;">发运方式：</td>
	                <td style="display: none;">
	                    <select name="TRANS_TYPE" id="TRANS_TYPE"  class="u-select">
	                        <%--<option value="">-请选择-</option>--%>
	                        <c:if test="${transList!=null}">
	                            <c:forEach items="${transList}" var="list">
	                                <option value="${list.fixValue }">${list.fixName }</option>
	                            </c:forEach>
	                        </c:if>
	                    </select>
	                    <font color="RED">*</font>
	                </td>
	                <td align="right" style="display: none">品牌：</td>
	                <td style="display: none">
	                    <select name="brand" id="brand" class="short_sel" style="width:170px" onchange="changeBreand()">
	                        <c:if test="${brandList!=null}">
	                            <c:forEach items="${brandList}" var="list">
	                                <option value="${list.BRAND}">${list.BRAND} (最小订货箱数:${list.CRITERION}箱)</option>
	                            </c:forEach>
	                        </c:if>
	                    </select>
	                    <font color="RED">*</font>
	                </td>
	                <td align="right" ></td>
	                <td  ></td>
	            </tr>
	            <tr>
	                <td align="right">接收地址：</td>
	                <td colspan="3">
	                    <input name="ADDR" class="middle_txt" id="ADDR" type="text" size="20" style="width: 450px;"
	                           value="${defaultValueMap.defaultAddr}" readonly="readonly"/>
	                    <input name="ADDR_ID" class="middle_txt" id="ADDR_ID" type="hidden" size="20"
	                           value="${defaultValueMap.defaultAddrId}" readonly="readonly"/>
	                    <input name='dlbtn3' id='dlbtn3' class='normal_btn' type='button' value='其他地址'
	                           onclick="selSales('ADDR_ID','','ADDR','RECEIVER','TEL','POST_CODE','STATION',$('#RCV_ORGID')[0].value,'3')"/>
	                    <font color="RED">*</font>
	                </td>
	                <td align="right">到站名称：</td>
	                <td><input id="STATION" name="STATION" type="text" class="middle_txt"
	                           value="${defaultValueMap.defaultStation}"
	                           readonly value=""/>
	                    <font color="RED">*</font></td>
	            </tr>
	            <tr>
	                <td align="right">接收人：</td>
	                <td><input id="RECEIVER" name="RECEIVER" readonly type="text" value="${defaultValueMap.defaultLinkman}"
	                           class="middle_txt"/>
	                    <font color="RED">*</font></td>
	                <td align="right"><span align="right">接收人电话：</span></td>
	                <td><input id="TEL" name="TEL" type="text" class="middle_txt" value="${defaultValueMap.defaultTel}"
	                           readonly
	                           value=""/>
	                    <font color="RED">*</font></td>
	                <td align="right">邮政编码：</td>
	                <td><input id="POST_CODE" name="POST_CODE" type="text" class="middle_txt"
	                           value="${defaultValueMap.defaultPostCode}" readonly/>
	                    <font color="RED">*</font></td>
	            </tr>
	            <tr id="tr2">
	                <td id="accountSumNameTd" align="right">
	                    <c:if test="${defaultValueMap.flag=='upload'}">
	                       	 账户余额：
	                    </c:if>
	                </td>
	                <td id="accountSumTextTd">
	                    <c:if test="${defaultValueMap.flag=='upload'}">
	                        <input id="accountSum" style="border:0px;background-color:#F3F4F8;" name="accountSum"
	                               type="text"
	                               class="middle_txt" value="${defaultValueMap.accountSum}" readonly/><font
	                            color="blue">元</font>
	                    </c:if>
	                </td>
	                <td id="accountKyNameTd" align="right">
	                    <c:if test="${defaultValueMap.flag=='upload'}">
	                        	可用金额：
	                    </c:if>
	                </td>
	                <td id="accountKyTextTd">
	                    <c:if test="${defaultValueMap.flag=='upload'}">
	                        <input id="accountKy" style="border:0px;background-color:#F3F4F8;" name="accountKy" type="text"
	                               class="middle_txt" value="${defaultValueMap.accountKy}" readonly/><font
	                            color="blue">元</font>
	                    </c:if>
	                </td>
	                <td id="accountDjNameTd" align="right">
	                    <c:if test="${defaultValueMap.flag=='upload'}">
	                        	冻结金额：：
	                    </c:if>
	                </td>
	                <td id="accountDjTextTd">
	                    <c:if test="${defaultValueMap.flag=='upload'}">
	                        <input id="accountDj" style="border:0px;background-color:#F3F4F8;" name="accountDj" type="text"
	                               class="middle_txt" value="${defaultValueMap.accountDj}" readonly/><font
	                            color="blue">元</font>
	                    </c:if>
	                </td>
	                <td id="AmountNameTd" align="right"></td>
	                <td id="AmountTextTd"></td>
	            </tr>
	            <tr id="tr3">
	                <td align="right" valign="middle">订单总金额：</td>
	                <td valign="middle"><input readonly type="text" value="0.00" class="middle_txt"
	                                           name="Amount" id="Amount"
	                                           style="background-color: #ffff80;border:none"/><font
	                        color="blue">元</font></td>
	                <td align="right"><span align="right">运费金额：</span></td>
	                <td><input id="freight" name="freight" type="text" value="0"
	                           style="background-color: #ffff80;border:none" class="middle_txt"
	                           readonly/>
	                    <font color="blue">元</font></td>
	                <td id="DISCOUNTNameTd" align="right">折扣率：</td>
	                <td id="DISCOUNTTextTd"><input readonly type="text" class="middle_txt"
	                                               value="${dataMap.discount}" name="DISCOUNT" id="DISCOUNT"
	                                               style="border: none"/>
	                </td>
	            </tr>
	            <tr>
	                <td align="right">备注：</td>
	                <td colspan="5"><textarea name="textarea" cols="80" rows="4"></textarea>
	                    <font color="red">提醒：箱数=订货数量/最小包装量</font>
	                </td>
	            </tr>
	        </table>
			</div>
		</div>
        <table id="file" class="table_list" border="1" cellSpacing=1 cellPadding=1 width="100%">
            <caption><img class="panel-icon nav" src="<%=contextPath%>/img/subNav.gif"/>配件信息</caption>
            <tr>
                <th>
                    <input type="checkbox" id="ckAll" name="ckAll" onclick="checkAll(this)"/>
                </th>
                <th>序号</th>
				<th>配件编码</th>
				<th>配件名称</th>
				<th>单位</th>
				<th>最小包装量</th>
				<th>订货数量</th>
				<th>订货单价(折扣前)<font color="blue">(元)</font></th>
				<th>订货单价(折扣后)<font color="blue">(元)</font></th>
				<th>订货金额<font color="blue">(元)</font></th>
				<th>上级是否有库存</th>
				<th>备注</th>
				<th>操作</th>
            </tr>
            <c:forEach items="${detailList}" var="data">
                <tr>
                    <td>
                        <script type="text/javascript">
                            document.write('<input  type="checkbox" onclick="countAll()"  id="cell_' + (document.getElementById('file').rows.length - 2) + '"  name="cb"  checked="true" value="'+${data.PART_ID}+'" />');
                        </script>
                    </td>
                    <td>
                        <script type="text/javascript">
                            document.write((document.getElementById('file').rows.length - 1));
                        </script>
                    </td>
                    <td nowrap>
                        <input type="hidden" id="partOldcode_${data.PART_ID}" name="partOldcode_${data.PART_ID}"
                               value="${data.PART_OLDCODE}"/>
                            ${data.PART_OLDCODE}
                    </td>
                    <td>
                            ${data.PART_CNAME}
                        <input type="hidden" id="partCname_${data.PART_ID}" name="partCname_${data.PART_ID}"
                               value="${data.PART_CNAME}"/>
                    </td>
                    <td>
                            ${data.UNIT}
                        <input type="hidden" id="unit_${data.PART_ID}" name="unit_${data.PART_ID}"
                               value="${data.UNIT}"/>
                    </td>
                    <td>
                        <input type="hidden" id="minPackage_${data.PART_ID}" name="minPackage_${data.PART_ID}"
                               value="${data.MIN_PACKAGE}"/>${data.MIN_PACKAGE}
                    </td>
                    <td>
                        <input type="text" class="short_txt" value="${data.BUYQTY}"
                               style="background-color:#FFFFCC;text-align:center" id="buyQty_${data.PART_ID}"
                               name="buyQty_${data.PART_ID}"
                               onchange="countMoney(this,${data.SALE_PRICE1},${data.PART_ID})"/>

                    </td>
                    <td>
                        <input type="hidden" id="buyPrice_${data.PART_ID}" name="buyPrice_${data.PART_ID}"
                               value="${data.SALE_PRICE1}"/>${data.SALE_PRICE1}
                    </td>
                    <td>
                        <input type="hidden" id="buyPrice1_${data.PART_ID}" name="buyPrice1_${data.PART_ID}"
                               value="${data.SALE_PRICE1}"/>
                        <script type="text/javascript">
                            document.getElementById('buyPrice1_' +${data.PART_ID}).value = (parseFloat('${data.SALE_PRICE1}') * parseFloat(document.getElementById("DISCOUNT").value)).toFixed(2);
                            document.write((parseFloat('${data.SALE_PRICE1}') * parseFloat(document.getElementById("DISCOUNT").value)).toFixed(2));
                        </script>
                    </td>
                    <td>
                        <input type="hidden" id="buyAmount_${data.PART_ID}" name="buyAmount_${data.PART_ID}"
                               value="${data.BUY_AMOUNT}"/>${data.BUY_AMOUNT}
                    </td>
                    <td>
                        <input type="hidden" id="upOrgStock_${data.PART_ID}" name="upOrgStock_${data.PART_ID}"
                               value="${data.UPORGSTOCK}"/>${data.UPORGSTOCK}
                        <input type='hidden'
                               name='isReplaced_${data.PART_ID}'
                               id='isReplaced_${data.PART_ID}'
                               value='${data.IS_REPLACED}'/>
                    </td>
                    <td>
                        <input type="text" class="short_txt" value="" style="" id="remark_${data.PART_ID}"
                               name="remark_${data.PART_ID}"/><input type='hidden' name='isLack_${data.PART_ID}'
                                                                     id='isLack_${data.PART_ID}'
                                                                     value='${data.IS_LACK}'>
                    </td>
                    <td>
                        <input type="button" class="short_btn" name="queryBtn" value="删除"
                               onclick="deleteTblRow(this);"/><input
                            type='hidden' name='stockQty_${data.PART_ID}' id='stockQty_${data.PART_ID}'
                            value='${data.ITEM_QTY}'>
                        <script type="text/javascript">
                            document.getElementById('TRANS_TYPE').value =${transType};
                            countMoney(document.getElementById('buyQty_' +${data.PART_ID}), '${data.SALE_PRICE1}', '${data.PART_ID}');
                            countAll();
                        </script>
                    </td>
                </tr>
            </c:forEach>
        </table>
        <table border="0" class="table_query">
            <tr>
                <td  class="center">
<!--                     <input class="cssbutton" type="button" value="上传文件" name="button1" -->
<!--                            onclick="showUpload();"> &nbsp; -->
                    <input class="normal_btn" type="button" value="保存" name="button1"
                           onclick="partReplaceValidate('save');">
                    &nbsp;
                    <input class="normal_btn" type="button" value="返 回" onclick="goBack()"/>
<!--                     &nbsp; -->
<!--                     <input class="normal_btn" type="button" value="提 报" onclick="partReplaceValidate('rep');"/></td> -->
            </tr>
        </table>
        <FIELDSET>
            <LEGEND
                    style="MozUserSelect: none; KhtmlUserSelect: none"
                    unselectable="on">
                <th colspan="6"
                    style="background-color:#DAE0EE;font-weight:normal;color:#416C9B;border-color: #859aff;padding:2px;line-height:1.5em;border: 1px solid #E7E7E7;">
                    <img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav"/>
                   	 配件查询
                    <input type="button" class="normal_btn" name="addPartViv"
                           id="addPartViv" value="增加" onclick="addPartDiv()"/>
                </th>
            </LEGEND>
            <div style="display: none; heigeht: 5px" id="partDiv">
                <table class="table_query" width=100% border="0" align="center"
                       cellpadding="1" cellspacing="1">
                    <tr>
                        <td class="right" width="13%"> 配件编码：</td>
                        <td class="left" width="20%">
                            &nbsp;
                            <input class="middle_txt" id="PART_OLDCODE"
                                   datatype="1,is_noquotation,30" name="PART_OLDCODE"
                                   onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text"/>
                        </td>
                        <td class="right" width="13%">配件名称：</td>
                        <td class="left" width="21%">
                            &nbsp;
                            <input class="middle_txt" id="PART_CNAME"
                                   datatype="1,is_noquotation,30" name="PART_CNAME"
                                   onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text"/>
                        </td>
                    </tr>
                </table>
                <table width="100%">
                    <tr>

                        <td align="center">
                            <input class="normal_btn" align="right" type="button" name="queryBtn"
                                   id="queryBtn" value="查 询" onclick="doQuery()"/>&nbsp;&nbsp;&nbsp;&nbsp;
                            <input class="normal_btn" type="button" name="BtnQuery2"
                                   id="BtnQuery2" value="添加" onclick="addCells()"/>
                        </td>
                    </tr>
                </table>
                <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
                <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
            </div>
        </FIELDSET>
        <iframe frameborder="0" name="myIframe" id="myIframe"
                src="<%=request.getContextPath() %>/jsp/parts/salesManager/carFactorySalesManager/uploadFile.jsp"
                height="100%"
                width="100%" scrolling="auto" align="middle">
        </iframe>
    </div>
</form>
</body>
<script type="text/javascript">
    document.getElementById("PAY_TYPE").value =<%=Constant.CAR_FACTORY_SALES_PAY_TYPE_01%>;
</script>
</html>
