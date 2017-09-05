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
    <title>采购单结算</title>
    <style>
    	table.order-accounting-form{width: 880px;background-color:transparent} 
    	#page{margin: 0 5px;min-width:30px;width:40px}
    	.form-fieldset{margin-top: 10px;margin-bottom: 5px;}
    </style>
<script type="text/javascript">
    var myPage;

    var url = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderBalanceManager/queryPurOrderInInfo.json";

    var title = null;

    var columns = [
        {header: "序号", align: 'center', renderer: getIndex},
        {
            header: "<input type='checkbox'  name='ckbAll' id='ckbAll' onclick='selAll(this)'  />",
            dataIndex: 'IN_ID',
            align: 'center',
            width: '33px',
            renderer: seled
        },

        {header: "入库单号", dataIndex: 'IN_CODE', style: 'text-align:left'},
        {header: "备件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
        {header: "备件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
        {header: "件号", dataIndex: 'PART_CODE', style: 'text-align:left'},
        {header: "单位", dataIndex: 'UNIT', align: 'center'},
        {header: "无税单价", dataIndex: 'BUY_PRICE_NOTAX', align: 'center'},
        {header: "待结算数量", dataIndex: 'SPARE_QTY', align: 'center', renderer: insertBalQtyInput},
        {header: "入库数量", dataIndex: 'IN_QTY', align: 'center', renderer: insertInQtyInput},
        {header: "退货数量", dataIndex: 'RETURN_QTY', align: 'center', renderer: insertReQtyInput},
        {header: "已结算数量", dataIndex: 'BAL_QTY', align: 'center', renderer: insertBaledQtyInput},
        {header: "是否暂估", dataIndex: 'IS_GUARD', align: 'center', renderer: reGauge},
        {header: "采购员", dataIndex: 'BUY_NAME', align: 'center'},
        {header: "供应商", dataIndex: 'VENDER_NAME', style: 'text-align:left'},
        {header: "入库时间", dataIndex: 'IN_DATE', align: 'center'},
        {header: "所属库房", dataIndex: 'WH_NAME', style: 'text-align:left'},
        {header: "采购组织", dataIndex: 'PRODUCE_FAC', style: 'text-align:left', renderer: getItemValue},

        {header: "备注", dataIndex: 'REMARK', align: 'center', renderer: insertRemarkInput},
      //  {header: "验收单号", dataIndex: 'CHECK_CODE', style: 'text-align:left'},
        {header: "订单单号", dataIndex: 'ORDER_CODE', style: 'text-align:left'},

        {header: "财务供应商", dataIndex: 'VENDER_NAME1', style: 'text-align:left'}

    ];


    function seled(value, meta, record) {
        if (record.data.IS_GAUGE == '10041001') {
            return "<input type='hidden' value='" + value + "'  id='ck" + value + "'/>";
        }
        return "<input type='checkbox' value='" + value + "' name='ck' id='ck" + value + "' onclick='chkPart()'/>";
    }

    function reGauge(value, meta, record) {
        if (value == '10041001') {
            return "<font color='black'>是</font>";
        }
        return "否";
    }
    function chkPart() {
        var cks = document.getElementsByName('ck');
        var flag = true;
        for (var i = 0; i < cks.length; i++) {
            var obj = cks[i];
            if (!obj.checked) {
                flag = false;
            }
        }
        document.getElementById("ckbAll").checked = flag;
    }

    function selAll(obj) {
        var cks = document.getElementsByName('ck');
        for (var i = 0; i < cks.length; i++) {
            if (obj.checked) {
                cks[i].checked = true;
            } else {
                cks[i].checked = false;
            }
        }
    }

    function insertBalQtyInput(value, meta, record) {
        var inId = record.data.IN_ID;
        var inQty = record.data.IN_QTY;
        var reQty = record.data.RETURN_QTY;
        var baledQty = record.data.BAL_QTY;
        var output;
        output = '<input type="text" class="short_txt" onchange="check(this,' + inId + ',' + inQty + ',' + reQty + ',' + baledQty + ');"  id="BAL_QTY' + inId + '" name="BAL_QTY' + inId + '" value="' + value + '" onkeydown="toNext(' + inId + ')"/>';
        return output;
    }

    function insertBaledQtyInput(value, meta, record) {
        var inId = record.data.IN_ID;
        var output;
        output = '<input type="hidden"  id="BALED_QTY1' + inId + '" name="BALED_QTY1' + inId + '" value="' + value + '"/>' + value;
        return output;
    }

    function toNext(inId) {
        var ck = document.getElementsByName("ck");
        var obj = $("#BAL_QTY" + inId);
        var idx = obj.parentElement.parentElement.rowIndex - 1;
        if (event.keyCode == 40) {
            var balQty = obj.value;
            var pattern1 = /^[1-9][0-9]*$/;
            if (!pattern1.exec(balQty)) {
                obj.value = obj.value.replace(/\D/g, '');
            }
            $("#ck" + inId)[0].checked = true;
            chkPart();
            if (ck[idx + 1] && $("BAL_QTY" + (ck[idx + 1].value))) {
                var val = $("#BAL_QTY" + (ck[idx + 1].value))[0].value;
                $("#BAL_QTY" + (ck[idx + 1].value))[0].focus();
                $("#BAL_QTY" + (ck[idx + 1].value))[0].value = "";
                $("#BAL_QTY" + (ck[idx + 1].value))[0].value = val;
            }
        }
        if (event.keyCode == 38) {
            if (ck[idx - 1] && $("#BAL_QTY" + (ck[idx - 1].value))) {
                var val = $("#BAL_QTY" + (ck[idx - 1].value))[0].value;
                $("#BAL_QTY" + (ck[idx - 1].value))[0].focus();
                $("#BAL_QTY" + (ck[idx - 1].value))[0].value = "";
                $("#BAL_QTY" + (ck[idx - 1].value))[0].value = val;
            }
        }
    }

    function check(value, inId, inQty, reQty, baledQty) {
        var pattern1 = "";
        var balQty = $(value).value;
        if(balQty>0){
            pattern1 = /^[1-9][0-9]*$/;
        }else{
            pattern1 = /^(-)?[1-9][0-9]*$/;
        }
        if (!pattern1.exec($(value).value)) {
            //MyAlert("请录入正整数且必须大于0！");
            $(value).value = $(value).value.replace(/\D/g, '');
            $(value).focus();
        }
        if (isNumber($(value).value)) {
            if ($(value).value == 0) {
                MyAlert("结算数量是正整数且必须大于0！");
                $(value).value = "";
                $(value).focus();
                return;
            }
        }
        if(balQty>0) {
            if (parseInt(balQty) > (parseInt(inQty) - parseInt(reQty) - parseInt(baledQty))) {
                MyAlert("结算数量不能大于入库数量与退货数量、已结算之差!");
                $(value).value = "";
                $(value).focus();
                return;
            }
        }else{
            if (parseInt(balQty) -parseInt(inQty)!=0) {
                MyAlert("第" + i + "行配件：" + mt.rows[i].cells[5].innerText + "结算数量需等于入库数量与退货数量、已结算数量之差!</br>");
                $(value).value = "";
                $(value).focus();
                return;
            }
        }
        $("ck" + inId).checked = true;
        chkPart();
    }

    function insertInQtyInput(value, meta, record) {
        var inId = record.data.IN_ID;
        var output;
        output = '<input type="hidden"  id="IN_QTY1' + inId + '" name="IN_QTY1' + inId + '" value="' + value + '"/>' + value;
        return output;
    }

    function insertReQtyInput(value, meta, record) {
        var inId = record.data.IN_ID;
        var venderCode = record.data.VENDER_CODE1;
        var venderId = record.data.VENDER_ID1;
        var output;
        output = '<input type="hidden"  id="RE_QTY1' + inId + '" name="RE_QTY1' + inId + '" value="' + value + '"/> <input type="hidden"  id="VENDER_CODE' + inId + '"  value="' + venderCode + '"/> <input type="hidden"  id="VENDER_ID' + inId + '"  value="' + venderId + '"/>' + value;
        return output;
    }

    function insertRemarkInput(value, meta, record) {
        var pid = record.data.POLINE_ID;
        var output = '<input type="text" class="middle_txt" id="REMARK1' + pid + '" name="REMARK1' + pid + '" value="' + value + '"/>\n';
        return output;
    }

    //结算
    function agreeApply() {
        /* var part_balance_type = $("PART_BALANCE_TYPE").value;
         if(!part_balance_type){
         MyAlert("请选择结算类型!");
         return;
         } */
        var inIds = document.getElementsByName("cb");
        var l = inIds.length;
        var cnt = 0;
        for (var i = 0; i < l; i++) {
            if (inIds[i].checked) {
                cnt++;
                var pattern1 ="";
                var balQty = document.getElementById("BAL_QTY1" + inIds[i].value).value;//结算数量
                var inQty = document.getElementById("IN_QTY" + inIds[i].value).value;//入库数量
                var reQty = document.getElementById("RE_QTY" + inIds[i].value).value;//退货数量
                var baledQty = document.getElementById("BALED_QTY" + inIds[i].value).value;//已结算数量
                if(balQty>0){
                    pattern1 = /^[1-9][0-9]*$/;
                }else{
                    pattern1 = /^(-)?[1-9][0-9]*$/;
                }
                if (!pattern1.exec(balQty)) {
                    MyAlert("第" + (i + 1) + "行，结算数量不能为空且只能输入整数!");
                    return;
                }
                if(inQty>0) {
                    if (parseInt(balQty) > (parseInt(inQty) - parseInt(reQty) - parseInt(baledQty))) {
                        MyAlert("第" + (i + 1) + "行，结算数量不能大于入库数量与退货数量、已结算数量之差!");
                        return;
                    }
                }else {
                    if (Math.abs(parseInt(balQty)) > Math.abs(parseInt(inQty) + parseInt(reQty) - parseInt(baledQty))) {
                        MyAlert("第" + (i + 1) + "行，结算数量不能大于入库数量与退货数量、已结算数量之差!");
                        return;
                    }
                }
            }
        }
        if (cnt == 0) {
            MyAlert("请选择详细信息！");
            return;
        }

       	MyConfirm("确定生成结算单?",function(){
        	url = '<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderBalanceManager/balanceOrder.json';
        	sendAjax(url, closeResult, 'fm');
        })
    }
    
      function agreeApplyAll() {
		//MyAlert("功能开发中，敬请期待！");
        MyConfirm("确定一键生成结算单?",function(){
        	var url = '<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderBalanceManager/balanceOrder.json?all=all';
            sendAjax(url, closeResult, 'fm');
        })
    }
    
    function closeResult(jsonObj){
    if (jsonObj != null) {
        var success = jsonObj.success;
        var error = jsonObj.error;
        var exceptions = jsonObj.Exception;
        if (success) {
            MyAlert(success);
            if(jsonObj.tz!=0){
                OpenHtmlWindow("<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderBalanceManager/showChangeCostInit.do?changeList="+jsonObj.changeId,730,390);
                }
                    window.location.href = '<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderBalanceManager/purchaseOrderBalanceQueryInit.do';
        } else if (error) {
            MyAlert(error);
        } else if (exceptions) {
            MyAlert(exceptions.message);
        }
    }
}

    function getResult(jsonObj) {
        btnEable();
        if (jsonObj != null) {
            var success = jsonObj.success;
            var error = jsonObj.error;
            var flag = jsonObj.flag;
            var exceptions = jsonObj.Exception;
            if (success) {
                MyAlert(success);
                window.location.href = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderBalanceManager/purchaseOrderBalanceQueryInit.do";
            } else if (error) {
                MyAlert(error);
            } else if (exceptions) {
                MyAlert(exceptions.message);
            }

        }
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

    function addCells() {
        var ck = document.getElementsByName('ck');
        var mt = document.getElementById("myTable");
        var cn = 0;
        for (var i = 1; i < mt.rows.length; i++) {
            if (mt.rows[i].cells[1].childNodes[0].checked) {
                cn++;
                var pQty = mt.rows[i].cells[8].childNodes[0].value;
                var pattern1="";
                if(pQty>0){
                    pattern1 = /^[1-9][0-9]*$/;
                }else{
                    pattern1 = /^(-)?[1-9][0-9]*$/;
                }
                if (!pattern1.exec(pQty)) {
                    MyAlert("第" + i + "行配件：" + mt.rows[i].cells[5].innerText + " 结算数量必须是整数!</r>");
                    break;
                }
                
                var inId = mt.rows[i].cells[1].childNodes[0].value;  //ID
                var vdenderCode = document.getElementById("VENDER_CODE" + inId).value;
                var venderId = document.getElementById("VENDER_ID" + inId).value;
                var buyName = mt.rows[i].cells[13].innerText;//采购员
                var venderName = mt.rows[i].cells[14].innerText;  //供应商名称
                var venderName1 = mt.rows[i].cells[20].innerText;  //财务供应商名称
                var whName = mt.rows[i].cells[16].innerText;  //库房名称
               // var chkCode = mt.rows[i].cells[19].innerText;  //验收单号
                var partOldcode = mt.rows[i].cells[3].innerText;  //备件编码
                var partCname = mt.rows[i].cells[4].innerText;  //备件名称
                var partCode = mt.rows[i].cells[5].innerText;  //件号
                var unit = mt.rows[i].cells[6].innerText;  //单位
                var makerName = mt.rows[i].cells[17].innerText;  //采购组织
                var balQty = mt.rows[i].cells[8].childNodes[0].value;  //结算数量
                var inQty = mt.rows[i].cells[9].innerText;  //入库数量
                var reQty = mt.rows[i].cells[10].innerText;  //退货数量
                var baledQty = mt.rows[i].cells[11].innerText;  //已结算数量
                var remark = mt.rows[i].cells[18].childNodes[0].value;  //备注
                var inCode = mt.rows[i].cells[2].innerText;  //入库单号
                var orderCode = mt.rows[i].cells[19].innerText;  //订单单号
                var inDate = mt.rows[i].cells[15].innerText;  //入库时间
                var zg = mt.rows[i].cells[12].innerText;
                if (zg == "是") {
                    MyAlert("是暂估价无法结算，请重新选择");
                    return;
                }
                if(inQty>0) {
                    if (parseInt(balQty) > (parseInt(inQty) - parseInt(reQty) - parseInt(baledQty))) {
                        MyAlert("第" + i + "行配件：" + mt.rows[i].cells[5].innerText + "结算数量不能大于入库数量与退货数量、已结算数量之差!</br>");
                        mt.rows[i].cells[1].firstChild.checked = false;
                        return;
                    }
                }else{
                    if (Math.abs(parseInt(balQty)) > Math.abs(parseInt(inQty) + parseInt(reQty)-parseInt(baledQty))) {
                        MyAlert("第" + i + "行配件：" + mt.rows[i].cells[5].innerText + "结算数量需等于入库数量与退货数量、已结算数量之差!</br>");
                        mt.rows[i].cells[1].firstChild.checked = false;
                        return;
                    }
                }
                addCell(inId, partOldcode, partCname, partCode, unit, venderName, venderName1, vdenderCode, venderId, buyName, makerName, inCode, orderCode, balQty, inQty, reQty, baledQty, remark, inDate, checkDate, whName);
            }
        }
        if (cn == 0) {
            MyAlert("请选择要添加的备件!");
        }
    }
    function validateCell(value) {
        var flag = true;
        var cb = document.getElementsByName("cb");
        for (var i = 0; i < cb.length; i++) {
            var val = cb[i].value;
            if (value == val) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    function validateVenderName(venderName) {
        var venderNames = document.getElementsByName("VENDER_NAME1");
        if (venderNames && venderNames.length > 0) {
            for (var i = 0; i < venderNames.length; i++) {
                if (venderName != venderNames[i].value) {
                    return false;
                }
            }
            return true;
        }
        return true;
    }

    function addCell(inId, partOldcode, partCname, partCode, unit, venderName, venderName1, venderCode, venderId, buyName, makerName, inCode, orderCode, balQty, inQty, reQty, baledQty, remark, inDate, checkDate, whName) {
        var tbl = document.getElementById('file');
        var rowObj = tbl.insertRow(tbl.rows.length);
        if (tbl.rows.length % 2 == 0) {
            rowObj.className = "table_list_row2";
        } else {
            rowObj.className = "table_list_row1";
        }
        var cell1 = rowObj.insertCell(0);
        var cell2 = rowObj.insertCell(1);
        var cell3 = rowObj.insertCell(2);
        var cell4 = rowObj.insertCell(3);
        var cell5 = rowObj.insertCell(4);
        var cell6 = rowObj.insertCell(5);
        var cell7 = rowObj.insertCell(6);
        var cell8 = rowObj.insertCell(7);
        var cell9 = rowObj.insertCell(8);
        var cell10 = rowObj.insertCell(9);
        var cell11 = rowObj.insertCell(10);
        var cell12 = rowObj.insertCell(11);
        var cell13 = rowObj.insertCell(12);
        var cell14 = rowObj.insertCell(13);
        var cell15 = rowObj.insertCell(14);
        var cell16 = rowObj.insertCell(15);
        var cell17 = rowObj.insertCell(16);
        var cell18 = rowObj.insertCell(17);
        var cell19 = rowObj.insertCell(18);
        var cell20 = rowObj.insertCell(19);
       // var cell21 = rowObj.insertCell(20);

        cell1.innerHTML = '<tr><td align="center" nowrap><input  type="checkbox" value="' + inId + '" id="cell_' + (tbl.rows.length - 1) + '" name="cb" checked="true"/></td>';
        cell2.innerHTML = '<td calss="center" nowrap><span id="orderLine_SEQ" >' + (tbl.rows.length - 1) + '</span><input id="idx_' + inId + '" name="idx_' + inId + '" value="' + (tbl.rows.length - 1) + '" type="hidden" ></td>';
        cell3.innerHTML = '<td><input  type="button" class="normal_btn"  name="queryBtn" value="删除" onclick="deleteTblRow(this);" /></td></TR>';
        cell4.innerHTML = '<td style="text-align: left"><input   name="IN_CODE' + inId + '" id="IN_CODE' + inId + '" value="' + inCode + '" type="hidden" />' + inCode + '</td>';
        cell5.innerHTML = '<td style="text-align: left"><input   name="PART_OLDCODE' + inId + '" id="PART_OLDCODE' + inId + '" value="' + partOldcode + '" type="hidden" />' + partOldcode + '</td>';
        cell6.innerHTML = '<td style="text-align: left"><input   name="PART_CNAME' + inId + '" id="PART_CNAME' + inId + '" value="' + partCname + '" type="hidden" />' + partCname + '</td>';
        cell7.innerHTML = '<td style="text-align: left"><input   name="PART_CODE' + inId + '" id="PART_CODE' + inId + '" value="' + partCode + '" type="hidden" />' + partCode + '</td>';
        cell8.innerHTML = '<td align="center" nowrap><input   name="UNIT' + inId + '" id="UNIT' + inId + '" value="' + unit + '" type="hidden" />' + unit + '</td>';
        cell9.innerHTML = '<td align="center" nowrap><input   name="buyName' + inId + '" id="buyName' + inId + '" value="' + buyName + '" type="hidden" />' + buyName + '</td>';
        cell10.innerHTML = '<td align="center" nowrap><input   name="VENDER_NAME1" id="VENDER_NAME1' + inId + '" value="' + venderName + '" type="hidden" />' + venderName + '</td>';
        cell11.innerHTML = '<td align="center" nowrap><input   name="VENDER_NAME2' + inId + '" id="VENDER_NAME2' + inId + '" value="' + venderName1 + '" type="hidden" /><input   name="VENDER_CODE' + inId + '" value="' + venderCode + '" type="hidden" /><input   name="VENDER_ID' + inId + '" value="' + venderId + '" type="hidden" />' + venderName1 + '</td>';
        cell12.innerHTML = '<td align="center" nowrap><input   name="WH_NAME" id="WH_NAME' + inId + '" value="' + whName + '" type="hidden" />' + whName + '</td>';
        cell13.innerHTML = '<td align="center" nowrap><input   name="PRODUCE_FAC" id="PRODUCE_FAC' + inId + '" value="' + makerName + '" type="hidden" />' + makerName + '</td>';
        cell14.innerHTML = '<td align="center" nowrap><input   name="BAL_QTY1' + inId + '" id="BAL_QTY1' + inId + '" value="' + balQty + '" type="text" class="short_txt" /></td>';
        cell15.innerHTML = '<td align="center" nowrap><input   name="IN_QTY' + inId + '" id="IN_QTY' + inId + '" value="' + inQty + '" type="hidden" />' + inQty + '</td>';
        cell16.innerHTML = '<td align="center" nowrap><input   name="RE_QTY' + inId + '" id="RE_QTY' + inId + '" value="' + reQty + '" type="hidden" />' + reQty + '</td>';
        cell17.innerHTML = '<td align="center" nowrap><input   name="BALED_QTY' + inId + '" id="BALED_QTY' + inId + '" value="' + baledQty + '" type="hidden" />' + baledQty + '</td>';
        cell18.innerHTML = '<td align="center" nowrap><input class="short_txt"  name="REMARK' + inId + '" id="REMARK' + inId + '" value="' + remark + '" type="text"/></td>';
        //cell19.innerHTML = '<td style="text-align: left"><input   name="CHK_CODE' + inId + '" id="CHK_CODE' + inId + '" value="' + chkCode + '" type="hidden" />' + chkCode + '</td>';
        cell19.innerHTML = '<td style="text-align: left"><input   name="ORDER_CODE' + inId + '" id="ORDER_CODE' + inId + '" value="' + orderCode + '" type="hidden" />' + orderCode + '</td>';
        cell20.innerHTML = '<td align="center" nowrap><input   name="IN_DATE' + inId + '" id="IN_DATE' + inId + '" value="' + inDate + '" type="hidden" />' + inDate + '</td>';
    }

    Array.prototype.indexOf = function (val) {
        for (var i = 0; i < this.length; i++) {
            if (this[i] == val) return i;
        }
        return -1;
    }

    function deleteTblRow(obj) {
        var idx = obj.parentElement.parentElement.rowIndex;
        var tbl = document.getElementById('file');
        tbl.deleteRow(idx);
        refreshMtTable('orderLine', 'SEQ');//刷新行号
    }

    function delteTab(tab) {
        var tb = document.getElementById(tab);
        var rowNum = tb.rows.length;
        for (i = 2; i < rowNum; i++) {
            tb.deleteRow(i);
            rowNum = rowNum - 1;
            i = i - 1;
        }
    }
    //刷新行号
    function refreshMtTable(mtId, strType) {
        if (strType == "SEQ") {
            var oSeq = eval("document.all." + mtId + "_SEQ");
            if (oSeq != null && oSeq.length != null) {
                for (var i = 0; i < oSeq.length; i++) {
                    oSeq[i].innerText = (i + 1);
                }
            }
        }
    }

    function selAll2(obj) {
        var cb = document.getElementsByName('cb');
        for (var i = 0; i < cb.length; i++) {
            if (obj.checked) {
                cb[i].checked = true;
            } else {
                cb[i].checked = false;
            }
        }
    }

    function showUpload() {
        var uploadDiv = $("uploadDiv");
        if (uploadDiv.style.display == "block") {
            uploadDiv.style.display = "none";
        } else {
            uploadDiv.style.display = "block";
        }
    }

    function exportExcelTemplate() {
        fm.action = "<%=contextPath%>/parts/purchaseManager/PurchaseOrderManager/exportExcelTemplate.do";
        fm.submit();
    }

    function uploadExcel() {
        fm.action = "<%=contextPath%>/parts/purchaseManager/PurchaseOrderManager/uploadPartPlanExcel.do?";
        fm.submit();
    }

    //清空选定供应商
    function clearInput() {
        document.getElementById("VENDER_ID").value = '';
        document.getElementById("VENDER_NAME1").value = '';
    }

    function clearMInput() {//清空选定制造商
        var makerId = document.getElementById("MAKER_ID").value;
        if (makerId != null && makerId != "") {
            document.getElementById("MAKER_ID").value = '';
            document.getElementById("MAKER_NAME").value = '';
        }
    }

    function showPartMaker1(inputCode, inputId, isMulti) {
        if (!inputCode) {
            inputCode = null;
        }
        if (!inputId) {
            inputId = null;
        }
        OpenHtmlWindow("<%=contextPath%>/jsp/parts/purchaseOrderManager/purchaseOrderBalance/makerSelect.jsp?INPUTCODE=" + inputCode + "&INPUTID=" + inputId + "&ISMULTI=" + isMulti, 730, 390);
    }

    function expSpareBalDtl() {
        fm.action = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderBalanceManager/expSpareBalDtl.do";
        fm.submit();
    }

    //返回查询页面
    function goback() {
        window.location.href = "<%=contextPath%>/parts/purchaseOrderManager/PurchaseOrderBalanceManager/purchaseOrderBalanceQueryInit.do";
    }

    $(function() {
        var form = $('#fm'),
            grid = $('#myGrid');
        $(window).resize(function() {
            grid.width( form.width() - 26 );
        });    
    });
</script>
</head>
<body>
<div class="wbox">
    <div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：采购计划管理&gt;采购订单结算&gt;增加</div>
    <form name="fm" id="fm" method="post" enctype="multipart/form-data">
        <div class="form-panel">
            <h2><img src="<%=contextPath%>/img/nav.gif"/>结算单信息</h2>
            <div class="form-body">
                <table class="table_query">
                    <tr>
                        <td width="10%" class="right">结算单号:</td>
                        <td  align="left" width="24%">&nbsp;${balanceCode}
                            <input type="hidden" name="balanceCode" value="${balanceCode}"/>
                        </td>
                        <td class="right">结算人员:</td>
                        <td width="32%">${balancer}</td>
                        <td class="right">结算日期:</td>
                        <td>${balanceDate}</td>
                    </tr>
                    <tr>
                        <td class="right">采购组织:</td>
                        <td  align="left" width="24%">
                            <script type="text/javascript">
                                genSelBoxExp("PURCHASE_WAY", <%=Constant.PURCHASE_WAY %>, "", true, "", "", "false", "");
                            </script>
                            <font color="red">*</font>
                        </td>
                        <td class="right">
                        	    财务供应商:
                        </td>
						 <td  >
                            <input class="middle_txt" type="text" readonly="readonly" id="VENDER_NAME1" value=""
                                name="VENDER_NAME1"/>
                            <input class="mark_btn" type="button" value="&hellip;"
                                onclick="showPartVender('VENDER_NAME1','VENDER_ID','false')"/>
                            <INPUT class="normal_btn" onclick="clearInput();" value=清除 type=button name=clrBtn>
                            <input id="VENDER_ID" name="VENDER_ID" type="hidden" value=""><font color="red">*</font>

                        </td>
                     <!--    <td  class="right">采购方式:</td>
                        <td >
                         <select id="PART_PRODUCE_STATE" name="PART_PRODUCE_STATE"  class="u-select">
                                <option value="">-请选择-</option>
                                <option value="92631001">自制</option>
                                <option value="92631002">外购</option>
                            </select>
                        <td class="right" ></td> -->
                    </tr>
                </table>
            </div>
                
            </div>
            <FIELDSET class="form-fieldset">
                <LEGEND style="MozUserSelect: none; KhtmlUserSelect: none" unselectable="on">
                    <th colspan="6">
                        <img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav"/>
                        待结算明细
                        <input type="button" class="normal_btn" name="addPartViv" id="addPartViv" value="增加"
                            onclick="addPartDiv()"/>
                    </th>
                </LEGEND>
                <div style="display: none;" id="partDiv">
                    <table class="table_query order-accounting-form" width=100% border="0"                       cellpadding="1" cellspacing="1">
                        <tr>
                            <td class="right" >
                                配件编码：
                            </td>
                            <td >
                                <input class="middle_txt" id="PART_OLDCODE"
                                    datatype="1,is_noquotation,30" name="PART_OLDCODE"
                                    onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text"/>
                            </td>
                            <td class="right" >
                            配件名称：
                            </td>
                            <td >
                                <input class="middle_txt" id="PART_CNAME"
                                    datatype="1,is_noquotation,30" name="PART_CNAME"
                                    onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text"/>
                            </td>
                            <td  class="right">
                                件号：
                            </td>
                            <td >
                                <input class="middle_txt" id="PART_CODE" datatype="1,is_noquotation,30" name="PART_CODE"
                                    onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text"/>
                            </td>
                        </tr>
                        <tr>
                            <td width="13%" class="right">
                                入库时间（<font style="color: red">确认</font>）：
                            </td>
                            <td >
                                <input name="inSDate" id="inSDate" value="" type="text" class="middle_txt"
                                    datatype="1,is_date,10" group="inSDate,inEDate" style="width: 70px;">
                                <input name='button3' value="?" type='button' class='time_ico' title="点击选择时间"/>
                                至
                                <input name="inEDate" id="inEDate" value="" type="text" class="middle_txt"
                                    datatype="1,is_date,10" group="inSDate,inEDate" style="width: 70px;">
                                <input name='button3' value="?" type='button' class='time_ico' title="点击选择时间"/>
                            </td>
                            <td class="right" >
                                订单单号：
                            </td>
                            <td >
                                <input class="middle_txt" id="orderCodeS" name="orderCodeS" type="text"/>
                            </td>
                            <td class="right" >
                                入库单号：
                            </td>
                            <td >
                                <input class="middle_txt" id="inCodeS" name="inCodeS" type="text"/>
                            </td>
                        </tr>
                        <tr>
                            <td  class="right">
                                来源：
                            </td>
                            <td >
                                <script type="text/javascript">
                                    genSelBoxExp("ORIGIN_TYPE", <%=Constant.ORDER_ORIGIN_TYPE %>, "", true, "", "", "false", "");
                                </script>
                            </td>

                            <td  class="right">是否暂估:</td>
                            <td >
                                <select id="IS_GUARD" name="IS_GUARD"  class="u-select">
                                    <option value="">-请选择-</option>
                                    <option value="1">是</option>
                                    <option value="2">否</option>
                                </select>
                            </td>
                            <!-- <td  class="right">采购方式:</td>
                            <td >
                            <select id="PART_PRODUCE_STATE" name="PART_PRODUCE_STATE"  class="u-select">
                                    <option value="">-请选择-</option>
                                    <option value="0">自制</option>
                                    <option value="1">外购</option>
                                </select>
                            </td> -->
                        </tr>
                        <c:if test="${0<checkManager}">
                    <tr>
                    <td class="right">采购员：</td>
                    <td>
                        <select id="BALANCER_ID" name="BALANCER_ID" class="">
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
                    </td></tr>
                </c:if>
                    <c:if test="${0==checkManager}">
    <tr style="display: none;">
        <td    class="right">采购员：</td>
            <td >
                <select id="BALANCER_ID" name="BALANCER_ID" class="">
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
            </td></tr>
            </c:if>
                    <tr>
                        <td class="center" colspan="6">
                            <input class="u-button u-query" type="button" name="BtnQuery" id="queryBtn" value="查 询"
                                   onclick="__extQuery__(1)"/>
                            <input class="u-button" type="button" name="BtnQuery" id="queryBtn2" value="添加"
                                   onclick="addCells()"/>
                            <!--  <input class="long_btn" type="button" name="BtnQuery"
                             id="queryBtn3" value="待结算明细导出" onclick="expSpareBalDtl()"/> -->
                        </td>
                    </tr>
                    <tr>
                        <td colspan="6" class="center">
                           	 每页<input type="text" value='15' id='page' name='page' class="middle_txt"/>行
                        </td>
                    </tr>

                </table>
                <div class="table-wrap">
                    <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
                    <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
                </div>
            </div>
        </FIELDSET>
         <FIELDSET class="form-fieldset">
            <LEGEND style="MozUserSelect: none; KhtmlUserSelect: none" unselectable="on">
                <th colspan="6">
                    <img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav"/>
                    详细信息
       
                </th>
            </LEGEND>
            <table id="file"  class="table_list">
                <tr >
					<th>
                        <input type="checkbox" onclick="selAll2(this)" checked/>
                    </th>
                    <th style="text-align: center;">序号</th>
                    <th  class="noSort">操作</th>
                    <th  class="noSort">入库单号</th>
                    <th  class="noSort">备件编码</th>
                    <th  class="noSort">备件名称</th>
                    <th  class="noSort">件号</th>
                    <th  class="noSort">单位</th>
                    <th  class="noSort">采购员</th>
                    <th  class="noSort">供应商</th>
                    <th  class="noSort">财务供应商</th>
                    <th  class="noSort">所属库房</th>
                    <th  class="noSort">采购组织</th>
                    <th  class="noSort">结算数量</th>
                    <th  class="noSort">入库数量</th>
                    <th  class="noSort">退货数量</th>
                    <th  class="noSort">已结算数量</th>
                    <th  class="noSort">备注</th>
                    <th  class="noSort">订单单号</th>
                    <th  class="noSort">入库时间</th>
                </tr>
            </table>
             </FIELDSET>
            <br />
        <table border="0" class="table_query">
            <tr >
                <td class="center">
                    <input name="agreeBtn" id="agreeBtn" class="normal_btn" type="button" value="保 存"
                           onclick="agreeApply();"/>
                    &nbsp;
                    <input class="normal_btn" type="button" value="返 回" onclick="javascript:goback();"/>&nbsp;
                     <input class="normal_btn" type="button" value="一键保存" onclick="agreeApplyAll();"/></td>
            </tr>
        </table>
</form>
</div>    
</div>
</body>
</html>
