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
    <title>装箱明细修改</title>
</head>
<script language="javascript">
var temp = "";
var myPage;
var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkgResult/queryUnPkgedPartInfo.json";
var title = null;
var columns = [
    {header: "序号", align: 'center', renderer: getIndex},
    {header: "<input type='checkbox'  name='ckbAll' id='ckbAll' onclick='selAll(this)'  />", dataIndex: 'PART_ID', align: 'center', width: '33px', renderer: seled},
    {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
    {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
    {header: "件号", dataIndex: 'PART_CODE', style: 'text-align:left'},
    {header: "单位", dataIndex: 'UNIT', align: 'center'},
    {header: "出库货位", dataIndex: 'LOC_NAME', align: 'center'},
    {header: "出库批次", dataIndex: 'BATCH_NO', align: 'center'},
    {header: "销售数量", dataIndex: 'SALES_QTY', align: 'center'},
    //{header: "已装箱数量", dataIndex: 'PKGEDQTY', align:'center',renderer:insertPkgedQtyInput},
    {header: "待装箱数量", dataIndex: 'SPKG_QTY', align: 'center', renderer: insertSPkgQtyInput},
    //{header: "装箱数量", dataIndex: 'PKG_QTY', align:'center',renderer:insertPkgQtyInput},
    {header: "备注", dataIndex: 'REMARK', align: 'center', renderer: insertRemarkInput}
];

function seled(value, meta, record) {
    var locId = record.data.LOC_ID;
    var batchNo = record.data.BATCH_NO;
    var ckValue = value + "," + locId+","+batchNo;
    return "<input type='checkbox' value='" + ckValue + "' name='ck' id='ck" + ckValue + "' onclick='chkPart()'/>";
}

function insertSPkgQtyInput(value, meta, record) {
    var partId = record.data.PART_ID;
    var locId = record.data.LOC_ID;
    var batchNo = record.data.BATCH_NO;
    var newId = partId + "," + locId+","+batchNo;
    var salQty = record.data.SALES_QTY;
    var pkgedQty = record.data.PKGEDQTY;
    var sPkgQty = salQty - pkgedQty;
    var output;
    output = '<input type="text" class="middle_txt" onchange="check(this,' + partId + ','+locId+','+batchNo+"," + salQty + ');"  id="SPKG_QTY' + newId + '" name="SPKG_QTY' + newId + '" value="' + sPkgQty + '"/>';
    return output;
}

function insertPkgedQtyInput(value, meta, record) {
    var partId = record.data.PART_ID;
    var locId = record.data.LOC_ID;
    var batchNo = record.data.BATCH_NO;
    var newId = partId + "," + locId+","+batchNo;
    var pkgedQty = record.data.PKGEDQTY;
    var salQty = record.data.SALES_QTY;
    var output;
    output = '<input type="text" class="middle_txt" onchange="check(this,' + newId + ',' + salQty + ');"  id="PKG_QTY' + newId + '" name="PKG_QTY' + newId + '" value="' + pkgedQty + '"/>';
    return output;
}

function insertPkgQtyInput(value, meta, record) {
    var partId = record.data.PART_ID;
    var locId = record.data.LOC_ID;
    var newId = partId + "," + locId;
    var salQty = record.data.SALES_QTY;
    var pkgedQty = record.data.PKGEDQTY;
    var sPkgQty = salQty - pkgedQty;
    var output;
    output = '<input type="text" class="short_txt" onchange="check(this,' + newId + ',' + sPkgQty + ');"  id="PKG_QTY' + newId + '" name="PKG_QTY' + newId + '" value="' + sPkgQty + '"/>';
    return output;
}

function insertRemarkInput(value, meta, record) {
    var partId = record.data.PART_ID;
    var locId = record.data.LOC_ID;
    var newId = partId + "," + locId;
    var output = '<input type="text" class="middle_txt" id="REMARK1' + newId + '" name="REMARK1' + newId + '" value="' + value + '"/>\n';
    return output;
}

/* function check(value,partId,sPkgQty) {
 var pattern1 = /^[1-9][0-9]*$/;
 if (!pattern1.exec($(value).value)) {
 MyAlert("装箱数量只能输入正整数!");
 $(value)[0].value = $(value).value.replace(/\D/g, '');
 $(value)[0].focus();
 }

 var pkgQty = $(value).value;
 if(parseInt(pkgQty)>parseInt(sPkgQty)){
 MyAlert("装箱数量不能大于待装箱数量!");
 $(value)[0].value = "";
 $(value)[0].focus();
 return;
 }
 $("ck"+partId).checked=true;
 chkPart();
 } */

function check(value, partId,locId,batchNo, salQty) {
    var pattern1 = /^[1-9][0-9]*$/;
    if (!pattern1.exec($(value)[0].value)) {
        MyAlert("待装箱数量只能输入正整数!");
        $(value)[0].value = $(value).value.replace(/\D/g, '');
        $(value)[0].focus();
    }

    var pkgedQty = $(value)[0].value;
    if (parseInt(pkgedQty) > parseInt(salQty)) {
        MyAlert("待装箱数量不能大于销售数量!");
        $(value)[0].value = "";
        $(value)[0].focus();
        return;
    }
    $("#ck" + partId+","+locId+","+batchNo)[0].checked = true;
    chkPart();
}

function check1(value, partId,locId,batchNo, salQty) {
    var pattern1 = /^(([1-9][0-9]*)|(0))$/;
    if (!pattern1.exec($(value)[0].value)) {
        MyAlert("已装箱数量只能输入非负整数!");
        $(value)[0].value = $(value).value.replace(/\D/g, '');
        $(value)[0].focus();
    }

    var pkgQty = $(value)[0].value;
    if (parseInt(pkgQty) > parseInt(salQty)) {
        MyAlert("已装箱数量不能大于销售数量!");
        $(value)[0].value = "";
        $(value)[0].focus();
        return;
    }

    $("#cb" + partId + "," +locId+","+batchNo)[0].checked = true;
    chkPart1();
}
/* 	
 function check1(value,partId,sPkgQty) {
 var pattern1 = /^(([1-9][0-9]*)|(0))$/;
 if (!pattern1.exec($(value).value)) {
 MyAlert("装箱数量只能输入非负整数!");
 $(value).value = $(value).value.replace(/\D/g, '');
 $(value).focus();
 }

 var pkgQty = $(value).value;
 if(parseInt(pkgQty)>parseInt(sPkgQty)){
 MyAlert("装箱数量不能大于待装箱数量!");
 $(value).value = "";
 $(value).focus();
 return;
 }
 $("cb"+partId).checked=true;
 chkPart1();
 } */

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

function chkPart1() {
    var cks = document.getElementsByName('cb');
    var flag = true;
    for (var i = 0; i < cks.length; i++) {
        var obj = cks[i];
        if (!obj.checked) {
            flag = false;
        }
    }
    document.getElementById("allChk").checked = flag;
}

function disabledAll() {
    jQuery("input[type=button]").attr('disabled', 'true');
    jQuery("a").attr('disabled', 'true');
}
function enableAll() {
    jQuery("input[type=button]").attr('disabled', 'false');
    jQuery("a").attr('disabled', 'false');
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

function modDtl() {
    var partIds = document.getElementsByName("cb");
    var myPkgNo = $("#PKG_NO")[0].value;
    if (!myPkgNo) {
        MyAlert("请先设置装箱信息!");
        return;
    }
    var cnt = 0;
    for (var i = 0; i < partIds.length; i++) {
        if (partIds[i].checked) {
            cnt++;
            var pattern1 = /^(([1-9][0-9]*)|(0))$/;
            var pattern2 = /^[1-9][0-9]*$/;
            var pkgQty = document.getElementById("pkgQty_" + partIds[i].value).value;
            var salQty = document.getElementById("salQty_" + partIds[i].value).value;
            var pkgNo = document.getElementById("pkgNo_" + partIds[i].value).value;
            if (!pattern1.exec(pkgQty)) {
                MyAlert("第" + (i + 1) + "行，已装箱数量不能为空且只能输入非负整数!");
                return;
            }

            if (parseInt(pkgQty) > parseInt(salQty)) {
                MyAlert("第" + (i + 1) + "行，已装箱数量不能大于销售数量!");
                return;
            }

            if (!pkgNo) {
                if (!pattern2.exec(pkgQty)) {
                    MyAlert("第" + (i + 1) + "行，已装箱数量不能为空且只能输入正整数!");
                    return;
                }
            }

        }
    }
    if (cnt == 0) {
        MyAlert("请选择要修改的配件！");
        return;
    }
    MyConfirm("确定修改明细?",confirmResult);
}

function confirmResult(){
    btnDisable();
    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkgResult/saveModify.json?";
    makeNomalFormCall(url, ajaxResult, 'fm');
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

function queryBoxInfo(obj) {
    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkg/queryBoxInfo.json?pkgNo=" + obj.value;
    makeNomalFormCall(url, getBoxResult, 'fm');
}

function getBoxResult(jsonObj) {
    if (jsonObj) {
    	if (jsonObj.boxLen) {
            $("#BOX_LEN")[0].value = jsonObj.boxLen;
            $("#BOX_WID")[0].value = jsonObj.boxWid;
            $("#BOX_HEI")[0].value = jsonObj.boxHei;
            $("#VOLUME")[0].value = jsonObj.boxVol;
            $("#BOX_WEI")[0].value = jsonObj.boxWei;
            $("#CH_WEIGHT")[0].value = jsonObj.boxCHWei;
            $("#EQ_WEIGHT")[0].value = jsonObj.boxEQWei;

            $("#BOX_LEN")[0].readOnly = true;
            $("#BOX_WID")[0].readOnly = true;
            $("#BOX_HEI")[0].readOnly = true;
            $("#VOLUME")[0].readOnly = true;
            $("#BOX_WEI")[0].readOnly = true;
            $("#CH_WEIGHT")[0].readOnly = true;
            $("#EQ_WEIGHT")[0].readOnly = true;

        } else {
            $("#BOX_LEN")[0].value = "";
            $("#BOX_WID")[0].value = "";
            $("#BOX_HEI")[0].value = "";
            $("#VOLUME")[0].value = "";
            $("#BOX_WEI")[0].value = "";
            $("#CH_WEIGHT")[0].value = "";
            $("#EQ_WEIGHT")[0].value = "";

            $("#BOX_LEN")[0].readOnly = false;
            $("#BOX_WID")[0].readOnly = false;
            $("#BOX_HEI")[0].readOnly = false;
            $("#VOLUME")[0].readOnly = false;
            $("#BOX_WEI")[0].readOnly = false;
            $("#CH_WEIGHT")[0].readOnly = false;
            $("#EQ_WEIGHT")[0].readOnly = false;
        }
    }
}

function checkSize(obj) {
    var patrn = /^((0\.[0-9]*[1-9][0-9]*)|([1-9][0-9]*\.[0-9]+)|([1-9][0-9]*))$/;
    if (!patrn.exec(obj.value)) {
        MyAlert("包装尺寸无效,请重新输入!");
        obj.value = "";
        $("#VOLUME")[0].value = "";
        $("#CH_WEIGHT")[0].value = "";
        $("#EQ_WEIGHT")[0].value = "";
        return;
    } else {
        if (obj.value.indexOf(".") >= 0) {
            var patrn = /^[0-9]{0,8}.[0-9]{0,2}$/;
            if (!patrn.exec(obj.value)) {
                MyAlert("包装尺寸整数部分不能超过8位,且保留精度最大为2位!");
                obj.value = "";
                $("#VOLUME")[0].value = "";
                $("#CH_WEIGHT")[0].value = "";
                $("#EQ_WEIGHT")[0].value = "";
                return;
            }
        } else {
            var patrn = /^[0-9]{0,8}$/;
            if (!patrn.exec(obj.value)) {
                MyAlert("包装尺寸整数部分不能超过8位!");
                obj.value = "";
                $("#VOLUME")[0].value = "";
                $("#CH_WEIGHT")[0].value = "";
                $("#EQ_WEIGHT")[0].value = "";
                return;
            }
        }
    }

   /*  var box_len = $("BOX_LEN").value;
    var box_wid = $("BOX_WID").value;
    var box_hei = $("BOX_HEI").value; */

    var box_len = $("#BOX_LEN")[0].value;
    var box_wid = $("#BOX_WID")[0].value;
    var box_hei = $("#BOX_HEI")[0].value;
    //体积（VOLUME） = 长*宽*高/1000000
    //折合重量（EQ_WEIGHT） = 长*宽*高/6000(跟承运商有关系，默认是6000)
    //单箱重量(BOX_WEI)
    //计费重量（CH_WEIGHT） = 跟箱子长、宽、高有关，三边斗大于60CM 取折合重量和重量取最大值。三边有任一边小于等于60CM，取单箱重量。
    if (box_len && box_wid && box_hei) {
        $("#VOLUME")[0].value = ((box_len * box_wid * box_hei) / (100 * 100 * 100)).toFixed(2);//体积
        var eqWeght = ((box_len * box_wid * box_hei) / 6000).toFixed(2);//折合重量
        $("#EQ_WEIGHT")[0].value = eqWeght;
        calWeight(box_len, box_wid, box_hei, $("#BOX_WEI")[0].value, eqWeght);
    }

}

function checkWeight(obj) {
    var patrn = /^((0\.[0-9]*[1-9][0-9]*)|([1-9][0-9]*\.[0-9]+)|([1-9][0-9]*))$/;
    if (!patrn.exec(obj.value)) {
        MyAlert("单箱重量无效,请重新输入!");
        obj.value = "";
        $("#EQ_WEIGHT")[0].value = "";
        return;
    } else {
        if (obj.value.indexOf(".") >= 0) {
            var patrn = /^[0-9]{0,8}.[0-9]{0,2}$/;
            if (!patrn.exec(obj.value)) {
                MyAlert("单箱重量整数部分不能超过8位,且保留精度最大为2位!");
                obj.value = "";
                $("#EQ_WEIGHT")[0].value = "";
                return;
            }
        } else {
            var patrn = /^[0-9]{0,8}$/;
            if (!patrn.exec(obj.value)) {
                MyAlert("单箱重量整数部分不能超过8位!");
                obj.value = "";
                $("#EQ_WEIGHT")[0].value = "";
                return;
            }
        }
    }

    var box_len = $("#BOX_LEN")[0].value;
    var box_wid = $("#BOX_WID")[0].value;
    var box_hei = $("#BOX_HEI")[0].value;
    //体积（VOLUME） = 长*宽*高/1000000
    //折合重量（EQ_WEIGHT） = 长*宽*高/6000(跟承运商有关系，默认是6000)
    //单箱重量(BOX_WEI)
    //计费重量（CH_WEIGHT） = 跟箱子长、宽、高有关，三边斗大于60CM 取折合重量和重量取最大值。三边有任一边小于等于60CM，取单箱重量。
    if (box_len && box_wid && box_hei) {
        $("#VOLUME")[0].value = ((box_len * box_wid * box_hei) / (100 * 100 * 100)).toFixed(2);//体积
        var eqWeght = ((box_len * box_wid * box_hei) / 6000).toFixed(2);//折合重量
        $("#EQ_WEIGHT")[0].value = eqWeght;
        calWeight(box_len, box_wid, box_hei, $("#BOX_WEI")[0].value , eqWeght);
    }
}

/**
 * 计费重量（CH_WEIGHT） = 跟箱子长、宽、高有关，三边有一边大于60CM 取折合重量和重量取最大值。三边有都小于60CM，取单箱重量。
 * @param c 长
 * @param k 宽
 * @param g 高
 * @param dxzl  单箱重量
 * @param zhzl  折合重量
 */
function calWeight(c, k, g, dxzl, zhzl) {
    //MyAlert("长："+c + " 宽:" + k + " 高：" + g + " 单箱重量:" + dxzl + " 折合重量:" + zhzl);
    if (c >= 60 || k >= 60 || g > 60) {
        if (parseFloat(dxzl) >= parseFloat(zhzl)) {
            $("#CH_WEIGHT")[0].value = dxzl;
        } else {
            $("#CH_WEIGHT")[0].value = zhzl;
        }
    }
    if (c < 60 && k < 60 && g < 60) {
        $("#CH_WEIGHT")[0].value = dxzl;
    }
}
function addCells() {
    var ck = document.getElementsByName('ck');
    var mt = document.getElementById("myTable");
    var cn = 0;
    for (var i = 1; i < mt.rows.length; i++) {
        if (mt.rows[i].cells[1].childNodes[0].checked) {
            cn++;
            var pQty = mt.rows[i].cells[9].childNodes[0].value;
            var pattern1 = /^[1-9][0-9]*$/;
            if (!pattern1.exec(pQty)) {
                MyAlert("第" + i + "行配件：" + mt.rows[i].cells[2].innerText + "的待装箱数量必须是正整数!</r>");
                break;
            }
            var partId = mt.rows[i].cells[1].childNodes[0].value;  //ID
            if (validateCell(partId)) {
                var partOldcode = mt.rows[i].cells[2].innerText;  //配件编码
                var partCname = mt.rows[i].cells[3].innerText;  //配件名称
                var partCode = mt.rows[i].cells[4].innerText;  //件号
                var unit = mt.rows[i].cells[5].innerText;  //单位
                var locName = mt.rows[i].cells[6].innerText;  //出库货位
                var batchNo = mt.rows[i].cells[7].innerText;  //出库批次
                var salQty = mt.rows[i].cells[8].innerText;  //销售数量
                var spKgQty = mt.rows[i].cells[9].childNodes[0].value;  //待装箱数量
                var remark = mt.rows[i].cells[10].childNodes[0].value;  //备注

                if (parseInt(spKgQty) > parseInt(salQty)) {
                    MyAlert("第" + i + "行配件：" + mt.rows[i].cells[2].innerText + "待装箱数量不能大于销售数量!</br>");
                    mt.rows[i].cells[1].firstChild.checked = false;
                    return;
                }

                addCell(partId, partOldcode, partCname, partCode, unit, locName,batchNo, salQty, spKgQty, remark);

            } else {
                MyAlert("第" + i + "行配件：" + mt.rows[i].cells[2].innerText + " 批次："+mt.rows[i].cells[7].innerText+" 货位："+mt.rows[i].cells[6].innerText+" 已存在,可直接修改数量!</br>");
                mt.rows[i].cells[1].firstChild.checked = false;
                break;
            }
        }
    }
    if (cn == 0) {
        MyAlert("请选择要添加的配件!");
    }
}

function addCell(partId, partOldcode, partCname, partCode, unit, locName,batchNo, salQty, spKgQty, remark) {
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

    var str = genMySelBoxExp("pkgType_" + partId, <%=Constant.CAR_FACTORY_PKG_TYPE%>, <%=Constant.CAR_FACTORY_PKG_TYPE_01%>, true, "u-select", "style='width:80px'", "false", '');

    cell1.innerHTML = '<tr><td class="center" nowrap><input  type="checkbox" value="' + partId + '" id="cell_' + (tbl.rows.length - 2) + '" name="cb" checked="true" onclick="chkPart1();"/></td>';
    cell2.innerHTML = '<td class="center" nowrap><span id="orderLine_SEQ" >' + (tbl.rows.length - 2) + '</span><input id="idx_' + partId + '" name="idx_' + partId + '" value="' + (tbl.rows.length - 2) + '" type="hidden" ></td>';
    cell3.innerHTML = '<td style="text-align: left"><input   name="PART_OLDCODE' + partId + '" id="PART_OLDCODE' + partId + '" value="' + partOldcode + '" type="hidden" />' + partOldcode + '</td>';
    cell4.innerHTML = '<td style="text-align: left"><input   name="PART_CNAME' + partId + '" id="PART_CNAME' + partId + '" value="' + partCname + '" type="hidden" />' + partCname + '</td>';
    cell5.innerHTML = '<td style="text-align: left"><input   name="PART_CODE' + partId + '" id="PART_CODE' + partId + '" value="' + partCode + '" type="hidden" />' + partCode + '</td>';
    cell6.innerHTML = '<td style="text-align: left"><input   name="UNIT' + partId + '" id="UNIT' + partId + '" value="' + unit + '" type="hidden" />' + unit + '</td>';
    cell7.innerHTML = '<td style="text-align: left"><input   name="LOC_NAME' + partId + '" id="LOC_NAME' + partId + '" value="' + locName + '" type="hidden" />' + locName + '</td>';
    cell8.innerHTML = '<td style="text-align: left"><input   name="BATCHNO_' + partId + '" id="BATCHNO_' + partId + '" value="' + batchNo + '" type="hidden" />' + batchNo + '</td>';
    cell9.innerHTML = '<td style="text-align: left">' + str + '</td>';
    cell10.innerHTML = '<td class="center"><input   name="salQty_' + partId + '" id="salQty_' + partId + '" value="' + salQty + '" type="hidden" />' + salQty + '</td>';
    cell11.innerHTML = '<td class="center"><input   name="pkgQty_' + partId + '" id="pkgQty_' + partId + '" value="' + spKgQty + '" type="text" style="text-align:center" onblur="check1(this,' + partId + ',' + salQty + ');" class="middle_txt" /></td>';
    cell12.innerHTML = '<td class="center"><input type="hidden" id="pkgNo_' + partId + '" name="pkgNo_' + partId + '" value="'+$("#PKG_NO").val()+'"/>'+$("#PKG_NO").val()+'</td>';
    cell13.innerHTML = '<td class="center"><input class="middle_txt"  name="remark_' + partId + '" id="remark_' + partId + '" value="' + remark + '" type="text"/></td>';
    cell14.innerHTML = '<td><input  type="button" class="short_btn"  name="queryBtn" value="删除" onclick="deleteTblRow(this);" /></td></TR>';

}


function deleteTblRow(obj) {
    var idx = obj.parentElement.parentElement.rowIndex;
    var tbl = document.getElementById('file');
    tbl.deleteRow(idx);
    refreshMtTable('orderLine', 'SEQ');//刷新行号
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

function ajaxResult(jsonObj) {
    btnEnable();
    if (jsonObj != null) {
        var success = jsonObj.success;
        var error = jsonObj.error;
        var exceptions = jsonObj.Exception;
        if (success) {
            MyAlert(success);
            var pickOrderId = $("#pickOrderId")[0].value;
            var whId = $("#whId")[0].value;
            var pkgNo = $("#pkgNo")[0].value;
            window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkgResult/modifyOrderPage.do?pickOrderId=" + pickOrderId;
<%--        window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkgResult/modifyDtlPage.do?pkgNo=" + pkgNo + "&pickOrderId=" + pickOrderId + "&whId=" + whId; --%>
        } else if (error) {
            MyAlert(error);
        }/*  else if (exceptions) {
            MyAlert(exceptions.message);
        } */
    }
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
    str += " onChange=doCusChange(this.value);> ";
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
        if (codeData[i].type == type && flag) {
            str += "<option " + (codeData[i].codeId == selectedKey ? "selected" : "") + " value='" + codeData[i].codeId + "' title = '" + codeData[i].codeDesc + "' >" + codeData[i].codeDesc + "</option>";
        }
    }
    str += "</select>";
    return str;
}

function addPartPkgInfo() {
	$("#BOX_LEN")[0].value = "";
    $("#PKG_NO")[0].value = "";
    $("#BOX_WID")[0].value = "";
    $("#BOX_HEI")[0].value = "";
    $("#VOLUME")[0].value = "";
    $("#BOX_WEI")[0].value = "";
    $("#CH_WEIGHT")[0].value = "";
    $("#EQ_WEIGHT")[0].value = "";
       
    $("#BOX_LEN")[0].readOnly = false;
    $("#PKG_NO")[0].readOnly = false;
    $("#BOX_WID")[0].readOnly = false;
    $("#BOX_HEI")[0].readOnly = false;
    $("#VOLUME")[0].readOnly = false;
    $("#BOX_WEI")[0].readOnly = false;
    $("#CH_WEIGHT")[0].readOnly = false;
    $("#EQ_WEIGHT")[0].readOnly = false;

    var tbl = document.getElementById('file');
    var len = tbl.rows.length;
    if (len > 2) {
        //删除明细,重新选择
        for (var i = tbl.rows.length - 1; i >= 2; i--) {
            tbl.deleteRow(i);
        }
    }
}

function goBack() {
    var pickOrderId = $("#pickOrderId")[0].value;
    window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkgResult/modifyOrderPage.do?pickOrderId=" + pickOrderId;
}
</script>
</head>
<body onunload='javascript:destoryPrototype()'>
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
<input name="pickOrderId" id="pickOrderId" value="${pickOrderId}" type="hidden"/>
<input name="pkgNo" id="pkgNo" value="${pkgNo}" type="hidden"/>
<input name="whId" id="whId" value="${whId}" type="hidden"/>
<input type="hidden" name="status" id="status" value="1"/>

<div class="wbox">
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif"/>&nbsp;当前位置: 配件管理 > 配件销售管理 > 装箱结果修改 > 装箱明细修改</div>
<div>
<div class="form-panel">
     <h2><img class="panel-icon nav" src="<%=contextPath%>/img/subNav.gif"/>
		装箱信息&nbsp;&nbsp;<font style="color: red;font-weight: bold">拣货单：${pickOrderId}</font>
	</h2>
     <div class="form-body">
    <table class="table_query">
        <tr>
            <td align="right">装箱号<font color="red">*</font>：</td>
            <td width="25%">
                <input class="middle_txt" type="text" id="PKG_NO" name="PKG_NO" value="${map.PKG_NO }"
                       onchange="queryBoxInfo(this);" readonly="readonly"/>
            </td>
            <td align="right">包装尺寸<font color="red">*</font>：</td>
            <td width="25%">
                长<input style="width: 50px;height: 15px;" type="text" id="BOX_LEN" name="BOX_LEN" value="${map.LENGTH }"
                        onblur="checkSize(this)" readonly="readonly"/>
                宽<input style="width: 50px;height: 15px" type="text" id="BOX_WID" name="BOX_WID" value="${map.WIDTH }"
                        onblur="checkSize(this)" readonly="readonly"/>
                高<input style="width: 50px;height: 15px" type="text" id="BOX_HEI" name="BOX_HEI" value="${map.HEIGHT }"
                        onblur="checkSize(this)" readonly="readonly"/>CM
            </td>
            <td align="right">体积<font color="red">*</font>：</td>
            <td width="24%">
                <input type="text" style="border:0px;background-color:#F3F4F8;" id="VOLUME" name="VOLUME"
                       value="${map.VOLUME }" readonly="readonly"/>M<sup>3</sup>
            </td>
        </tr>
        <tr>
            <td align="right">单箱重量<font color="red">*</font>：</td>
            <td width="25%">
                <input class="middle_txt" type="text" id="BOX_WEI" name="BOX_WEI" onblur="checkWeight(this)"
                       value="${map.WEIGHT }" readonly="readonly"/>KG
            </td>
            <td align="right">折合重量<font color="red">*</font>：</td>
            <td width="24%">
                <input type="text" style="border:0px;background-color:#F3F4F8;" id="EQ_WEIGHT" name="EQ_WEIGHT"
                       value="${map.EQ_WEIGHT }" readonly="readonly"/>KG
            </td>
            <td align="right">计费重量<font color="red">*</font>：</td>
            <td width="25%">
                <input type="text" style="border:0px;background-color:#F3F4F8;" id="CH_WEIGHT" name="CH_WEIGHT"
                       value="${map.CH_WEIGHT }" readonly="readonly"/>KG
            </td>
        </tr>

<!--         <tr> -->
<!--             <td colspan="6" class="center"> -->
<!--                 <input type="button" class="long_btn" name="addPartPkg" -->
<!--                        id="addPartPkg" value="新增装箱信息" onclick="addPartPkgInfo()"/> -->
<!--             </td> -->
<!--         </tr> -->

    </table>
</div>
</div>
</div>

<FIELDSET>
    <LEGEND
            style="MozUserSelect: none; KhtmlUserSelect: none"
            unselectable="on">
        <th colspan="6">
            <img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav"/>
            配件信息
            <input type="button" class="normal_btn" name="addPartViv"
                   id="addPartViv" value="增加" onclick="addPartDiv()"/>
        </th>
    </LEGEND>
    <div style="display: none; heigeht: 5px" id="partDiv">
        <table class="table_query" width=100% border="0" class="center"
               cellpadding="1" cellspacing="1">
            <tr>

                <td align="right" width="10%"> 配件编码：</td>
                <td width="20%">
                    <input class="middle_txt" id="PART_OLDCODE"
                           datatype="1,is_noquotation,30" name="PART_OLDCODE"
                           onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text"/>
                </td>
                <td align="right" width="10%"> 配件名称：</td>
                <td width="22%">
                    <input class="middle_txt" id="PART_CNAME"
                           datatype="1,is_noquotation,30" name="PART_CNAME"
                           onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text"/>
                </td>
                <td width="10%" align="right">件号：</td>
                <td width="22%">
                    <input class="middle_txt" id="PART_CODE"
                           datatype="1,is_noquotation,30" name="PART_CODE"
                           onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text"/>
                </td>
            </tr>

            <tr>
                <td class="center" colspan="6">
                    <input class="u-button" type="button" name="BtnQuery"
                           id="queryBtn" value="查 询" onclick="__extQuery__(1)"/>
                    <input class="u-button" type="button" name="BtnQuery"
                           id="queryBtn2" value="添加" onclick="addCells()"/>
                </td>
            </tr>

        </table>
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </div>
</FIELDSET>

<table id="file" class="table_list" style="border-bottom:1px solid #DAE0EE">
    <tr>
        <th colspan="13" align="left"><img class="nav" src="<%=contextPath%>/img/subNav.gif"/>配件信息</th>
    </tr>
    <tr>
        <td>
            <input type="checkbox" onclick="selAll2(this)" id="allChk"/>
        </td>
        <td>序号</td>
        <td>配件编码</td>
        <td>配件名称</td>
        <td>件号</td>
        <td>单位</td>
        <td>出库货位</td>
        <td>出库批次</td>
        <td>包装方式</td>
        <td>销售数量</td>
        <td>已装箱数量</td>
        <!-- <td>待装箱数量</td>
        <td>装箱数量</td> -->
        <td>箱号</td>
        <td>备注</td>
    </tr>
    <c:forEach items="${dtlList}" var="data" varStatus="_sequenceNum" step="1">
        <c:if test="${((_sequenceNum.index+1) mod 2) != 0}">
            <tr class="table_list_row1">
        </c:if>
        <c:if test="${((_sequenceNum.index+1) mod 2) == 0}">
            <tr class="table_list_row2">
        </c:if>
        <td class="center">
            <input type="checkbox" value="${data.PART_ID},${data.LOC_ID},${data.BATCH_NO}" name="cb" id="cb${data.PART_ID},${data.LOC_ID},${data.BATCH_NO}" onclick="chkPart1();"/>
            <input type="hidden" id="IS_EXIST${data.PART_ID},${data.LOC_ID},${data.BATCH_NO}"
                   name="IS_EXIST${data.PART_ID},${data.LOC_ID}" value="1"/>
            <input type="hidden" id="PKG_ID${data.PART_ID},${data.LOC_ID},${data.BATCH_NO}" name="PKG_ID${data.PART_ID},${data.LOC_ID},${data.BATCH_NO}"
                   value="${data.PKG_ID}"/>
        </td>
        <td class="center" nowrap>&nbsp;
            <!-- <script type="text/javascript">
              document.write(jQuery('#file tr').length-2);
       </script> -->
            <span id="orderLine_SEQ">${_sequenceNum.index+1}</span>
        </td>
        <td class="center">
            <c:out value="${data.PART_OLDCODE}"/>
            <input type="hidden" id="PART_OLDCODE${data.PART_ID},${data.LOC_ID},${data.BATCH_NO}"
                   name="PART_OLDCODE${data.PART_ID},${data.LOC_ID},${data.BATCH_NO}" value="${data.PART_OLDCODE}"/>
        </td>
        <td class="center">
            <c:out value="${data.PART_CNAME}"/>
            <input type="hidden" id="PART_CNAME${data.PART_ID},${data.LOC_ID},${data.BATCH_NO}"
                   name="PART_CNAME${data.PART_ID},${data.LOC_ID},${data.BATCH_NO}" value="${data.PART_CNAME}"/>
        </td>
        <td class="center">
            <c:out value="${data.PART_CODE}"/>
            <input type="hidden" id="PART_CODE${data.PART_ID},${data.LOC_ID},${data.BATCH_NO}"
                   name="PART_CODE${data.PART_ID},${data.LOC_ID},${data.BATCH_NO}" value="${data.PART_CODE}"/>
        </td>
        <td class="center">
            <c:out value="${data.UNIT}"/>
            <input type="hidden" id="UNIT${data.PART_ID},${data.LOC_ID},${data.BATCH_NO}"
                   name="UNIT${data.PART_ID},${data.LOC_ID},${data.BATCH_NO}" value="${data.PART_CODE}"/>
        </td>
        <td class="center">
            <c:out value="${data.LOC_NAME}"/>
            <input type="hidden" id="LOC_NAME${data.PART_ID},${data.LOC_ID},${data.BATCH_NO}"
                   name="LOC_NAME${data.PART_ID},${data.LOC_ID},${data.BATCH_NO}" value="${data.LOC_NAME}"/>
        </td>
        <td class="center">
            <c:out value="${data.BATCH_NO}"/>
            <input type="hidden" id="BATCHNO_${data.PART_ID},${data.LOC_ID},${data.BATCH_NO}"
                   name="BATCHNO_${data.PART_ID},${data.LOC_ID},${data.BATCH_NO}" value="${data.BATCH_NO}"/>
        </td>
        <td class="center">
            <script type="text/javascript">
                genSelBoxExp(("pkgType_" + '${data.PART_ID},${data.LOC_ID},${data.BATCH_NO}'), <%=Constant.CAR_FACTORY_PKG_TYPE%>, <%=Constant.CAR_FACTORY_PKG_TYPE_01%>, true, "u-select", "style='width:80px'", "false", '');
            </script>
        </td>
        <td class="center">
            <c:out value="${data.SALES_QTY}"/>
            <input type="hidden" id="salQty_${data.PART_ID},${data.LOC_ID},${data.BATCH_NO}" name="salQty_${data.PART_ID},${data.LOC_ID},${data.BATCH_NO}"
                   value="${data.SALES_QTY}"/>
        </td>
        <%-- <td class="center">
        <c:out value="${data.PKG_QTY}" />
        </td>
        <td class="center">
        <c:out value="${data.SALES_QTY-data.PKG_QTY}" />
        <input type="hidden" id="sPkgQty_${data.PART_ID},${data.LOC_ID}" name="sPkgQty_${data.PART_ID},${data.LOC_ID}" value="${data.SALES_QTY-data.PKG_QTY}"/>
        </td> --%>
        <td class="center">
            <input name="pkgQty_${data.PART_ID},${data.LOC_ID},${data.BATCH_NO}" id="pkgQty_${data.PART_ID},${data.LOC_ID},${data.BATCH_NO}"
                   value="${data.PKG_QTY}" type="text" style="text-align:center"  class="middle_txt"
                   onblur="check1(this,${data.PART_ID},${data.LOC_ID},,${data.BATCH_NO},${data.SALES_QTY});"/>
        </td>
        <td class="center">
            <c:out value="${data.PKG_NO}"/>
            <input type="hidden" id="pkgNo_${data.PART_ID},${data.LOC_ID},${data.BATCH_NO}" name="pkgNo_${data.PART_ID},${data.LOC_ID},${data.BATCH_NO}"
                   value="${data.PKG_NO}"/>
        </td>
        <td class="center">
            <input name="remark_${data.PART_ID},${data.LOC_ID},${data.BATCH_NO}" class="middle_txt" value="${data.REMARK}" 
                   id="remark_${data.PART_ID},${data.LOC_ID},${data.BATCH_NO}" type="text" style="text-align:center"/>
        </td>

        </tr>
    </c:forEach>
</table>

<table border="0" class="table_query">
    <tr >
        <td class="center">
            <input class="u-button" type="button" value="修改" id="saveDtl" name="saveDtl" onclick="modDtl();"/>
            <input class="u-button" type="button" value="返 回" onclick="goBack();"/>
        </td>
    </tr>
</table>
</div>
</form>
</body>
</html>