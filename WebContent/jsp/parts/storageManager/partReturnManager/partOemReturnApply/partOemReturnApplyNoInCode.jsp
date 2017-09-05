<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    String contextPath = request.getContextPath();
%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>配件采购退货申请</title>
</head>
<body>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：
    配件管理&gt;配件退货管理&gt;采购退货申请&gt;无入库单退货申请
</div>
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <input type="hidden" name="ids" value="${ids}"/>
    <table class="table_query" width=100% border="0" align="center" cellpadding="1" cellspacing="1">
        <th colspan="6"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav"/>退货信息</th>
        <tr>
            <td width="18%" class="table_query_right" align="right">退货单号：</td>
            <td width="33%" align="left">${returnCode }
                <input type="hidden" name="returnCode" id="returnCode" value="${returnCode }"/>
            </td>
            <td width="19%" class="table_query_right" align="right">制单单位：</td>
            <td width="30%" align="left">${createOrgName }
                <input type="hidden" name="orgId" id="orgId" value="${orgId}"/>
                <input type="hidden" name="orgCode" id="orgCode" value="${createOrgCode }"/>
                <input type="hidden" name="orgName" id="createOrgName" value="${createOrgName }"/>
            </td>
        </tr>
            <td align="right">退货类型：</td>
            <td align="left">
                <select id="returnTo" name="returnTo" onchange="showSelect();">
                    <option value="">--请选择--</option>
                    <option value="1">退回供应商</option>
                    <option value="2">退回北汽银翔</option>
                </select>
            </td>
            <td width="19%" class="table_query_right" align="right">退出库房：</td>
            <td width="30%" align="left">
                <select id="WH_ID" name="WH_ID" onchange="changeDiv()">
                    <c:forEach items="${list}" var="house">
                        <option value="${house.WH_ID }">${house.WH_NAME }</option>
                    </c:forEach>
                </select>
                <font color="red">*</font>
            </td>
        </tr>
        <tr id="venderTr" style="display: none">
            <td class="table_query_right" align="right">供应商：</td>
            <td colspan="3">
                <input class="middle_txt" type="text" readonly="readonly" id="VENDER_NAME" name="VENDER_NAME"
                       value="${venderName }"/>
                <input class="mark_btn" type="button" value="&hellip;"
                       onclick="showPartMaker('VENDER_NAME','VENDER_ID','false')"/>
                <INPUT class=normal_btn onclick="clearInput();" value=清除 type=button name=clrBtn>
                <input id="VENDER_ID" name="VENDER_ID" type="hidden" value="${venderId }">
            </td>
        </tr>
        <tr id="whId2Tr" style="display: none">
            <td class="table_query_right" align="right">退入库房：</td>
            <td width="30%" align="left">
                <select id="WH_ID2" name="WH_ID2">
                    <option value="">--请选择--</option>
                    <c:forEach items="${list2}" var="house">
                        <option value="${house.WH_ID }">${house.WH_NAME }</option>
                    </c:forEach>
                </select>
            </td>
        </tr>
        <tr>
            <td class="table_query_right" align="right">退货原因：</td>
            <td colspan="3"><textarea name="reason" id="reason" style="width:90%" rows="4"></textarea>
                <font color="red">*</font>
            </td>
        </tr>
    </table>

    <table id="file" class="table_list" style="border-bottom: 1px;">
        <tr>
            <th colspan="10" align="left">
                <img src="<%=contextPath%>/img/nav.gif"/>退货明细
        </tr>
        <tr class="table_list_row0">
            <td>
                <input type="checkbox" onclick="selAll2(this)" id="ckAll"/>
            </td>
            <td>
                序号
            </td>

            <td>
                配件编码
            </td>
            <td>
                配件名称
            </td>
            <td>
                件号
            </td>
            <td>
                货位
            </td>
            <td>
                可用数量
            </td>
            <td>
                退货数量<font color="red">*</font>
            </td>
            <td>
                备注
            </td>
            <td>
                操作
            </td>
        </tr>
    </table>
    <table class="table_edit">
        <tr>
            <td align="center">
                <input type="button" name="saveBtn" id="saveBtn" value="提交" onclick="fmsubmit();" class="normal_btn"/>
                <input type="button" name="saveBtn2" id="saveBtn2" value="返 回" onclick="javascript:goback();"
                       class="normal_btn"/>
            </td>
        </tr>
    </table>

<FIELDSET>
    <LEGEND
            style="MozUserSelect: none; KhtmlUserSelect: none"
            unselectable="on">
        <th colspan="6"
            style="background-color:#DAE0EE;font-weight:normal;color:#416C9B;padding:2px;line-height:1.5em;border: 1px solid #E7E7E7;">
            <img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav"/>
            <font color="blue">配件信息</font>
            <input type="button" class="normal_btn" name="addPartViv"
                   id="addPartViv" value="增加" onclick="addPartDiv()"/>
        </th>
    </LEGEND>
    <div style="display: none; heigeht: 5px" id="partDiv">
        <table class="table_query" width=100% border="0" align="center"
               cellpadding="1" cellspacing="1">
            <tr>
                <td align="right" width="10%">
                    配件编码：
                </td>
                <td align="left" width="20%">
                    <input class="middle_txt" id="PART_OLDCODE"
                           datatype="1,is_noquotation,30" name="PART_OLDCODE"
                           type="text"/>
                </td>
                <td align="right" width="10%">
                    配件名称：
                </td>
                <td align="left" width="20%">
                    <input class="middle_txt" id="PART_CNAME"
                           datatype="1,is_noquotation,30" name="PART_CNAME"
                           type="text"/>
                </td>
                <td align="right" width="10%">
                    件号：
                </td>
                <td width="20%" align="left">
                    <input class="middle_txt" id="PART_CODE"
                           datatype="1,is_noquotation,30" name="PART_CODE" type="text"/>
                </td>
            </tr>
            <tr>
                <td align="center" colspan="6">
                    <input class="normal_btn" type="button" name="BtnQuery"
                           id="queryBtn" value="查 询" onclick="__extQuery__(1)"/>
                    <input class="normal_btn" type="button" name="BtnQuery"
                           id="addBtn" value="添加" onclick="addCells()"/>
                </td>
            </tr>
        </table>
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </div>
</FIELDSET>
</form>
<script type="text/javascript">
var myPage;

var url = "<%=contextPath%>/parts/storageManager/partReturnManager/PartOemReturnApplyManager/queryPartInfo.json";

var title = null;

var columns = [
    {header: "序号", align: 'center', renderer: getIndex, width: '7%'},
    {header: "<input type='checkbox'  name='ckbAll' id='ckbAll' onclick='selAll(this)'  />", dataIndex: 'PART_ID', align: 'center', width: '33px', renderer: seled},
    {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
    {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
    {header: "件号", dataIndex: 'PART_CODE', style: 'text-align:left'},
    {header: "货位", dataIndex: 'LOC_NAME', align: 'center'},
    {header: "可用数量", dataIndex: 'NORMAL_QTY', align: 'center'},
    {header: "退货数量<font color='red'>*</font>", align: 'center', renderer: insertApplyQtyInput}
];

function seled(value, meta, record) {
    var partId = record.data.PART_ID+","+record.data.LOC_ID;
    return "<input type='checkbox' value='" + partId + "' name='ck' id='ck" + partId + "' onclick='chkPart()'/>";
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

function chkPart1() {
    var cks = document.getElementsByName('cb');
    var flag = true;
    for (var i = 0; i < cks.length; i++) {
        var obj = cks[i];
        if (!obj.checked) {
            flag = false;
        }
    }
    document.getElementById("ckAll").checked = flag;
}

//插入文本框
function insertApplyQtyInput(value, meta, record) {
    var partId = "'"+record.data.PART_ID+","+record.data.LOC_ID+"'";
    var output = '<input type="text" class="short_txt" onchange="check(this,' + partId + ');" id="APPLY_QTY1' + record.data.PART_ID+","+record.data.LOC_ID + '" name="APPLY_QTY1' + record.data.PART_ID+","+record.data.LOC_ID + '" value="" size ="10" style="background-color:#FF9"/>\n';
    output += '<input type="hidden"  id="LOC_ID' + record.data.PART_ID+","+record.data.LOC_ID + '" name="LOC_ID' + record.data.PART_ID+","+record.data.LOC_ID + '" value="' + record.data.LOC_ID + '" />\n';
    return output;
}

function check(obj, partId) {
    var pattern1 = /^[1-9][0-9]*$/;
    if (obj.value == 0 || obj.value == "") {
        MyAlert("退货数量是正整数且必须大于0！");
        return;
    }
    if (!pattern1.exec(obj.value)) {
        //MyAlert("请录入正整数且必须大于0！");
        obj.value = obj.value.replace(/\D/g, '');
        obj.focus();
    }
    $("ck" + partId).checked = true;
    chkPart();
}

function addPartDiv() {
    var partDiv = document.getElementById("partDiv");
    var addPartViv = document.getElementById("addPartViv");
    if ($("#WH_ID").val() == "") {
        MyAlert("请选择仓库!");
        return;
    }
    if (partDiv.style.display == "block") {
        addPartViv.value = "增加";
        partDiv.style.display = "none";
    } else {
        addPartViv.value = "收起";
        partDiv.style.display = "block";
        __extQuery__(1);
    }

}

function changeDiv() {
    var partDiv = document.getElementById("partDiv");
    var addPartViv = document.getElementById("addPartViv");
    if (partDiv.style.display == "block") {
        addPartViv.value = "增加";
        partDiv.style.display = "none";
    }
    var tbl = document.getElementById('file');
    var len = tbl.rows.length;
    if (len > 2) {
        //改变仓库之后就要删除退货明细,重新选择
        for (var i = tbl.rows.length - 1; i >= 2; i--) {
            tbl.deleteRow(i);
        }
    }
}

function addCells() {
    var ck = document.getElementsByName('ck');
    var mt = document.getElementById("myTable");
    $("#ckAll")[0].checked = true;
    var cn = 0;
    for (var i = 1; i < mt.rows.length; i++) {
        var partId = mt.rows[i].cells[1].firstChild.value;  //ID
        if (mt.rows[i].cells[1].firstChild.checked) {
            cn++;
            var applyQty1 = $("#APPLY_QTY1" + partId).val();  //退货数量
            var noramlQty = mt.rows[i].cells[6].innerText;  //可用数量
            var locId = $("#LOC_ID" + partId).val();  //货位ID
            var locName = mt.rows[i].cells[5].innerText;  //货位
            var pattern1 = /^[1-9][0-9]*$/;
            if (applyQty1 == 0 || applyQty1 == "") {
                MyAlert("第" + i + "行的退货数量是正整数且必须大于0！");
                return;
            }
            if (!pattern1.exec(applyQty1)) {
                //MyAlert("请录入正整数且必须大于0！");
                applyQty1 = applyQty1.replace(/\D/g, '');
                $("#APPLY_QTY1" + partId).focus();
            }
            if (parseInt(applyQty1) > parseInt(noramlQty)) {
                MyAlert("第" + (i) + "行的退货数量不能大于可用数量,请重新输入!");
                return;
            }
            if (validateCell(partId, locName)) {
                var partCode = mt.rows[i].cells[4].innerText;  //件号
                var partOldcode = mt.rows[i].cells[2].innerText;  //配件编码
                var partCname = mt.rows[i].cells[3].innerText;  //配件名称
                addCell(partId, partCode, partOldcode, partCname, applyQty1, noramlQty, locName,locId);
            } else {
                MyAlert("第" + i + "行的配件：" + mt.rows[i].cells[4].innerText + " 已存在于退货明细中!");
                $("#ck" + partId)[0].checked = false;
                break;
            }
        }
    }
    if (cn == 0) {
        MyAlert("请选择要添加的配件信息!");
    }
}
function validateCell(spartId, locName) {
    var partIds = document.getElementsByName("cb");
    if (partIds && partIds.length > 0) {
        for (var i = 0; i < partIds.length; i++) {
            var locName2 = document.getElementById("LOC_NAME" + partIds[i].value).value;
            if (spartId == partIds[i].value && locName == locName2) {
                return false;
            }
        }
        return true;
    }
    return true;
}

function addCell(partId, partCode, partOldcode, partCname, applyQty1, noramlQty, locName,locId) {
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

    cell1.innerHTML = '<tr><td align="center" nowrap><input  type="checkbox" value="' + partId + '" id="cell_' + (tbl.rows.length - 2) + '" name="cb" checked="true" onclick="chkPart1();"/></td>';
    cell2.innerHTML = '<td align="center" nowrap><input id="idx_' + partId + '" name="idx_' + partId + '" value="' + (tbl.rows.length - 2) + '" type="hidden" ></td>' + (tbl.rows.length - 2);
    cell3.innerHTML = '<td align="center"><input   name="PART_OLDCODE' + partId + '" id="PART_OLDCODE' + partId + '" value="' + partOldcode + '" type="hidden" />' + partOldcode + '</td>';
    cell4.innerHTML = '<td align="center" nowrap><input   name="PART_CNAME' + partId + '" id="PART_CNAME' + partId + '" value="' + partCname + '" type="hidden" class="cname_' + partId + '"/>' + partCname + '</td>';
    cell5.innerHTML = '<td align="center" nowrap><input   name="PART_CODE' + partId + '" id="PART_CODE' + partId + '" value="' + partCode + '" type="hidden"/>' + partCode + '</td>';
    cell6.innerHTML = '<td align="center" nowrap><input   name="LOC_NAME' + partId + '" id="LOC_NAME' + partId + '" value="' + locName + '" type="hidden"/>' +
            '<input   name="LOC_ID' + partId + '" id="LOC_ID' + partId + '" value="' + locId + '" type="hidden"/>' + locName + '</td>';
    cell7.innerHTML = '<td align="center" nowrap><input   name="NORMAL_QTY' + partId + '" id="NORMAL_QTY' + partId + '" value="' + noramlQty + '" type="hidden" />' + noramlQty + '</td>';
    cell8.innerHTML = '<td align="center" nowrap><input   name="APPLY_QTY' + partId + '" id="APPLY_QTY' + partId + '" value="' + applyQty1 + '" type="text" class="short_txt"/></td>';
    cell9.innerHTML = '<td align="center" nowrap><input class="short_txt" name="REMARK' + partId + '" id="REMARK' + partId + '" type="text"/></td>';
    cell10.innerHTML = '<td><input  type="button" class="cssbutton"  name="queryBtn4" value="删除" onclick="deleteTblRow(' + (tbl.rows.length - 1) + ');" /></td></TR>';

}

function deleteTblRow(rowNum) {
    var tbl = document.getElementById('file');
    tbl.deleteRow(rowNum);
    for (var i = rowNum; i < tbl.rows.length; i++) {
        tbl.rows[i].cells[1].innerText = i - 1;
        tbl.rows[i].cells[8].innerHTML = "<input type=\"button\" class=\"cssbutton\"  name=\"deleteBtn\" value=\"删除\" onclick='deleteTblRow(" + i + ")'/></td></tr>";
        if (i % 2 == 0) {
            tbl.rows[i].className = "table_list_row1";
        } else {
            tbl.rows[i].className = "table_list_row2";
        }
    }
}
//提交申请
function fmsubmit() {
    if ($("#returnTo").val() == "1" && $("#VENDER_NAME").val() == "") {
        MyAlert("请选择供应商!")
        return;
    }
    if ($("#returnTo").val() == "2" && $("#WH_ID2").val() == "") {
        MyAlert("请选择退入库房!")
        return;
    }
    if ($("#reason").val() == "") {
        MyAlert("请填写退货原因!");
        return;
    }
    if ($("#returnTo").val() == "") {
        MyAlert("请选择采购退货类型!");
        return;
    }
    var chk = document.getElementsByName('cb');
    var l = chk.length;
    var cnt = 0;
    for (var i = 0; i < l; i++) {
        if (chk[i].checked) {
            cnt++;
            var applyQty = document.getElementById("APPLY_QTY" + chk[i].value).value;//退货数量
            var pattern1 = /^[1-9][0-9]*$/;
            if (!pattern1.exec(applyQty)) {
                MyAlert("第" + (i + 1) + "行的退货数量只能输入非零的正整数!");
                return;
            }
            var normalQty = document.getElementById("NORMAL_QTY" + chk[i].value).value;//库存数量
            if (parseInt(applyQty) > parseInt(normalQty)) {
                MyAlert("第" + (i + 1) + "行的退货数量不能大于可用数量,请重新输入!");
                return;
            }
        }
    }
    if (cnt == 0) {
        MyAlert("请选择退货明细！");
        return;
    }

    MyConfirm("确定提交?", function(){
        btnDisable();
        var url = '<%=contextPath%>/parts/storageManager/partReturnManager/PartOemReturnApplyManager/submitReturnApplyNoCode.json?';
        sendAjax(url, getResult, 'fm');
    	
    });
}
function getResult(jsonObj) {
	btnEnable();
    if (jsonObj != null) {
        var success = jsonObj.success;
        var error = jsonObj.error;
        var exceptions = jsonObj.Exception;
        if (success) {
            MyAlert(success);
            window.location.href = '<%=contextPath%>/parts/storageManager/partReturnManager/PartOemReturnApplyManager/queryPartOemReturnApplyInit.do';
        }
        else if (error) {
            MyAlert(error);
        }
        else if (exceptions) {
            MyAlert(exceptions.message);
        }
    }
}

//返回查询页面
function goback() {
    window.location.href = '<%=contextPath%>/parts/storageManager/partReturnManager/PartOemReturnApplyManager/queryPartOemReturnApplyInit.do';
}
function clearInput() {//清空选定供应商
    var venderId = document.getElementById("VENDER_ID").value;
    if (venderId != null && venderId != "") {
        document.getElementById("VENDER_ID").value = '';
        document.getElementById("VENDER_NAME").value = '';
        changeDiv();
    }
}
function showSelect(value) {
    var returnTo = document.getElementById("returnTo").value;
    if (returnTo == "1") {
        document.getElementById("venderTr").style.display = "";
        document.getElementById("whId2Tr").style.display = "none";
    } else if (returnTo == "2") {
        document.getElementById("venderTr").style.display = "none";
        document.getElementById("whId2Tr").style.display = "";
    } else {
        document.getElementById("venderTr").style.display = "none";
        document.getElementById("whId2Tr").style.display = "none";
    }
}
</script>
</div>
</body>
</html>
