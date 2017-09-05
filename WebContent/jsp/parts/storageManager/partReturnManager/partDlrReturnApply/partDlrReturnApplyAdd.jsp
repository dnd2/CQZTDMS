<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
    String contextPath = request.getContextPath();
    String dealerId = (String) request.getAttribute("dealerId");
    String dealerCode = (String) request.getAttribute("dealerCode");
    String dealerName = (String) request.getAttribute("dealerName");
    int vflag = (Integer) request.getAttribute("flag");
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件销售退货申请新增</title>
<script type="text/javascript">
$(function(){
	pushParentOrg();
});
var myPage;
var curType;//当前退货单位类型
var url = "<%=contextPath%>/parts/storageManager/partReturnManager/PartDlrReturnApplyQuery/queryPartInfo.json";
var title = null;
var childOrgId;
var curSoCode;
var columns = [
    {header: "序号", align: 'center', renderer: getIndex, width: '7%'},
    {header: "<input type='checkbox'  name='ckbAll' id='ckbAll' onclick='selAll(this)'  />", dataIndex: 'PART_ID', align: 'center', width: '33px', renderer: seled},
    {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align: center'},
    {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align: center'},
    {header: "件号", dataIndex: 'PART_CODE', style: 'text-align: center'},
    {header: "单位", dataIndex: 'UNIT', style: 'text-align: center'},
    {header: "可用库存", dataIndex: 'NORMAL_QTY', align: 'center',renderer: insertNormalQtyInput},
    {header: "销售价格", dataIndex: 'BUY_PRICE', style: 'text-align:right'},
    {header: "采购数量", dataIndex: 'BUY_QTY', align: 'center'},
    {header: "退货数量<font color='red'>*</font>", dataIndex: 'BUY_QTY', align: 'center', renderer: insertApplyQtyInput}
    //{header: "备注", align: 'center', renderer: insertRemarkInput}
];
function seled(value, meta, record) {
    return "<input type='checkbox' value='" + value + "' name='ck' id='ck"+record.data.PART_ID+"' onclick='chkPart()'/>";
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
    document.getElementById("ckAll").checked = flag;
}

function insertPartCodeInput(value, meta, record) {
    var output = '<input type="hidden"  id="PART_CODE' + record.data.PART_ID + '" name="PART_CODE' + record.data.PART_ID + '" value="' + value + '"/>' + value;
    return output;
}
function insertPartOldCodeInput(value, meta, record) {

    var output = '<input type="hidden"  id="PART_OLDCODE' + record.data.PART_ID + '" name="PART_OLDCODE' + record.data.PART_ID + '" value="' + value + '"/>' + value;
    return output;
}
function insertPartCnameInput(value, meta, record) {

    var output = '<input type="hidden"  id="PART_CNAME' + record.data.PART_ID + '" name="PART_CNAME' + record.data.PART_ID + '" value="' + value + '"/>' + value
            + '<input type="hidden"  id="UNIT' + record.data.PART_ID + '" name="UNIT' + record.data.PART_ID + '" value="' + record.data.UNIT + '"/>';
    return output;
}
function insertBuyQtyInput(value, meta, record) {
    var output = "";
    if (record.data.BUY_QTY) {
        output = '<input type="hidden"  id="BUY_QTY' + record.data.PART_ID + '" name="BUY_QTY' + record.data.PART_ID + '" value="' + value + '"/>' + value;
    } else {
        output = '<input type="hidden"  id="BUY_QTY' + record.data.PART_ID + '" name="BUY_QTY' + record.data.PART_ID + '" value=""/>' + 0;
    }
    return output;
}

//插入文本框
function insertApplyQtyInput(value, meta, record) {
    var partId = record.data.PART_ID;
    var output = '<input type="text" class="short_txt" onchange="check(this,'+partId+');" id="APPLY_QTY1' + partId + '" name="APPLY_QTY1' + partId + '" value="' + value + '" size ="10" style="background-color:#FF9"/>\n';
    return output;
}

function check(obj,partId) {
	var pattern1 = /^[1-9][0-9]*$/;
    if (!pattern1.exec(obj.value)) {
        //MyAlert("请录入正整数且必须大于0！");
        obj.value = obj.value.replace(/\D/g, '');
        obj.focus();
    }
    if (isNumber(obj.value)) {
        if (obj.value == 0) {
            MyAlert("正整数且必须大于0！");
            return;
        }

    }
    $("#ck"+partId)[0].checked=true;
    chkPart();
}

//插入文本框
function insertNormalQtyInput(value, meta, record) {
    var output = '<input type="hidden" id="NORMAL_QTY' + record.data.PART_ID + '" name="NORMAL_QTY' + record.data.PART_ID + '" value="' + value + '"/>\n'+value;
    return output;
}

function insertRemarkInput(value, meta, record) {
    var output = '<input type="text" class="long_txt" id="REMARK' + record.data.PART_ID + '" name="REMARK' + record.data.PART_ID + '" value="' + value + '" size ="10" style="background-color:#FF9"/>\n';
    return output;
}

function showChildOrg(RETURN_DEALER, childorgName, childorgId, WH_ID, WH_NAME) {
    if (!RETURN_DEALER) {
        RETURN_DEALER = null;
    }
    if (!childorgName) {
        childorgName = null;
    }
    if (!childorgId) {
        childorgId = null;
    }
    if (!WH_ID) {
        WH_ID = null;
    }
    if (!WH_NAME) {
        WH_NAME = null;
    }
    OpenHtmlWindow("<%=contextPath%>/jsp/parts/storageManager/partReturnManager/partDlrReturnApply/returnDealerSelect.jsp?RETURN_DEALER=" + RETURN_DEALER + "&childorgName=" + childorgName + "&childorgId=" + childorgId + "&WH_ID=" + WH_ID + "&WH_NAME=" + WH_NAME, 730, 390);
}

function showSoCode(soCode) {
    var childorgId = $("#childorgId")[0].value;
    var saleOrgId = $("#SALE_ORG")[0].value.split(",")[0];
    if (!soCode) {
        soCode = null;
    }
    if (!childorgId) {
        MyAlert("请先选择退货单位!");
        return;
    }
    OpenHtmlWindow("<%=contextPath%>/jsp/parts/storageManager/partReturnManager/partDlrReturnApply/soCodeSelect.jsp?childorgId=" + childorgId + "&soCode=" +soCode+"&saleOrgId="+saleOrgId, 730, 550);
}

var parentOrg = new Array();

function pushParentOrg() {
    var orgId = <%=dealerId%>;
    var saleOrg = document.getElementById("SALE_ORG");//销售单位
    if (saleOrg.options) {//如果销售单位是下拉选择框
        if (parentOrg.length == 0) {
            for (var i = 0; i < saleOrg.options.length; i++) {
                var val = saleOrg.options[i].value;
                var vorgId = val.substr(0, val.indexOf(","));

                if (vorgId != orgId) {//如果销售单位不是当前用户所在单位,那就是车厂
                    parentOrg.push(val);
                }
            }
        }
    }
}

function clearInput() {
    //清空销售单号
    document.getElementById("soCode").value = '';
    changeDiv();
}

function changeDiv() {
    var orgId = $("#childorgId")[0].value;
    var code = $("#soCode")[0].value;
    var partDiv = document.getElementById("partDiv");
    var addPartViv = document.getElementById("addPartViv");
    var tbl = document.getElementById('file');
    var len = tbl.rows.length;

    if (orgId != childOrgId) {
    	$("#soCode")[0].value="";
        if (len > 2) {
            //改变退货单位之后就要删除退货明细,重新选择
            for (var i = tbl.rows.length - 1; i >= 2; i--) {
                tbl.deleteRow(i);
            }
        }
        childOrgId = orgId;
        if (partDiv.style.display == "block") {
            addPartViv.value = "增加";
            partDiv.style.display = "none";
        }
    }
    if (code != curSoCode) {
        if (len > 2) {
            //改变销售单号之后就要删除退货明细,重新选择
            for (var i = tbl.rows.length - 1; i >= 2; i--) {
                tbl.deleteRow(i);
            }
        }
        curSoCode = code;
        if (partDiv.style.display == "block") {
            addPartViv.value = "增加";
            partDiv.style.display = "none";
        }
    }

    var saleOrg = document.getElementById("SALE_ORG");//销售单位
    if (<%=dealerId%>==orgId){//如果退货单位选择的是当前用户所在单位,那该退货单位就是供应中心,那它的销售单位就只是车厂
        saleOrg.options.length = 0;//先删除所有选项
        for (var i = 0; i < parentOrg.length; i++) {
            var objItemText = parentOrg[i].substr(parentOrg[i].lastIndexOf(",") + 1);
            var varItem = new Option(objItemText, parentOrg[i]);
            saleOrg.options.add(varItem);
        }
    }else{
	    if (orgId && saleOrg.options) {//如果是退货单位选的是一般服务商,那销售单位就是当前供应中心
	        saleOrg.options.length = 0;//先删除所有选项
	        var varItem = new Option('<%=dealerName%>', '<%=dealerId%>,<%=dealerCode%>,<%=dealerName%>');
	        saleOrg.options.add(varItem);
	    }
	}

}
//document.getElementById('childorgName').attachEvent('oninput',function(o){
//if(o.propertyName=='value'){//如果是value属性才执行

//}
//});

function changeDiv1(){
	    var partDiv = document.getElementById("partDiv");
	    var addPartViv = document.getElementById("addPartViv");
	    var tbl = document.getElementById('file');
	    var len = tbl.rows.length;

	    $("#soCode")[0].value="";
        if (len > 2) {
            //改变销售单位之后就要删除退货明细,重新选择
            for (var i = tbl.rows.length - 1; i >= 2; i--) {
                tbl.deleteRow(i);
            }
        }
        if (partDiv.style.display == "block") {
            addPartViv.value = "增加";
            partDiv.style.display = "none";
        }
}

function addPartDiv() {
	var flag = $("#myFlag")[0].value;
	var salOrg = $("#SALE_ORG")[0].value;
	if(flag==2&&salOrg==""){
		MyAlert("请选择销售单位!");
		return;
	}
//     if (soPara == "1") {//强制检查销售单号,判断是否填写销售单号
	var paraValue = document.getElementById("soCode").value;
	if (paraValue == null || paraValue == "") {
	    MyAlert("请填写销售单号!");
	    return;
	}
//     }

    if ($("#childorgName")[0].value == "") {
        MyAlert("请选择退货单位!");
        return;
    }
    var whId = $("#WH_ID")[0].value;
    if(!whId){
    	 MyAlert("当前退货单位没有仓库,不能退货!");
         return;
    }
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
    $("#ckAll")[0].checked = true;
    var cn = 0;
    for (var i = 1; i < mt.rows.length; i++) {
        var partId = mt.rows[i].cells[1].firstChild.value;  //ID
        if (mt.rows[i].cells[1].firstChild.checked) {
            cn++;
            var applyQty1 = $("#APPLY_QTY1" + partId)[0].value;
            var pattern1 = /^[1-9][0-9]*$/;
            if (!pattern1.exec(applyQty1)) {
                MyAlert("配件【" + mt.rows[i].cells[2].innerText + "】的退货数量只能输入非零的正整数!");
                return;
            }
            var normalQty = $("#NORMAL_QTY"+partId)[0].value;
            if(parseInt(applyQty1)>parseInt(normalQty)){
            	MyAlert("配件【" + mt.rows[i].cells[2].innerText + "】的退货数量不能大于可用库存!");
                return;
            }
            if (validateCell(partId)) {
                var partOldcode = mt.rows[i].cells[2].innerText;  //配件编码
                var partCname = mt.rows[i].cells[3].innerText;  //配件名称
				var partCode = mt.rows[i].cells[4].innerText;  //件号
                var unit = mt.rows[i].cells[5].innerText;  //单位
                var buyPrice = mt.rows[i].cells[7].innerText;  //销售价格
                var buyQty = mt.rows[i].cells[8].innerText;  //采购数量
                //addCell(partId, partCode, partOldcode, partCname, buyQty, buyPrice, applyQty1,normalQty,unit);
                addCell(partId, partOldcode, partCname, partCode, buyQty, buyPrice, applyQty1,normalQty,unit);
            } else {
                MyAlert("第" + i + "行的配件：" + mt.rows[i].cells[2].innerText + " 已存在于退货明细中!");
                break;
            }
        }
    }
    if (cn == 0) {
        MyAlert("请选择要添加的配件信息!");
    }
}
function validateCell(spartId) {
    var partIds = document.getElementsByName("cb");
    if (partIds && partIds.length > 0) {
        for (var i = 0; i < partIds.length; i++) {
            if (spartId == partIds[i].value) {
                return false;
            }
        }
        return true;
    }
    return true;
}

function addCell(partId, partOldcode, partCname, partCode, buyQty, buyPrice, applyQty1,normalQty,unit) {
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

    cell1.innerHTML = '<tr><td align="center" nowrap><input  type="checkbox" value="' + partId + '" id="cell_' + (tbl.rows.length - 2) + '" name="cb" checked="true" onclick="chkPart1();"/></td>';
    cell2.innerHTML = '<td align="center" nowrap><input id="idx_' + partId + '" name="idx_' + partId + '" value="' + (tbl.rows.length - 2) + '" type="hidden" ></td>' + (tbl.rows.length - 2);
    cell3.innerHTML = '<td align="left" nowrap><input   name="PART_OLDCODE' + partId + '" id="PART_OLDCODE' + partId + '" value="' + partOldcode + '" type="hidden" />' + partOldcode + '</td>';
    cell4.innerHTML = '<td align="left" nowrap><input   name="PART_CNAME' + partId + '" id="PART_CNAME' + partId + '" value="' + partCname + '" type="hidden" class="cname_' + partId + '"/>' + partCname + '</td>';
    cell5.innerHTML = '<td align="left" nowrap><input   name="PART_CODE' + partId + '" id="PART_CODE' + partId + '" value="' + partCode + '" type="hidden"/>' + partCode + '</td>';
    cell6.innerHTML = '<td align="left" nowrap><input   name="UNIT' + partId + '" id="UNIT' + partId + '" value="' + unit + '" type="hidden"/>' + unit + '</td>';
    cell7.innerHTML = '<td align="right" nowrap><input   name="BUY_PRICE' + partId + '" id="BUY_PRICE' + partId + '" value="' + buyPrice + '" type="hidden" />' + buyPrice + '</td>';
    cell8.innerHTML = '<td align="center" nowrap><input   name="BUY_QTY' + partId + '" id="BUY_QTY' + partId + '" value="' + buyQty + '" type="hidden" />' + buyQty + '</td>';
    cell9.innerHTML = '<td align="center" nowrap><input   name="NORMAL_QTY' + partId + '" id="NORMAL_QTY' + partId + '" value="' + normalQty + '" type="hidden"/>' + normalQty + '</td>';
    cell10.innerHTML = '<td align="center" nowrap><input   name="APPLY_QTY' + partId + '" id="APPLY_QTY' + partId + '" value="' + applyQty1 + '" type="text" class="short_txt"/></td>';
    cell11.innerHTML = '<td align="center" nowrap><input  class="short_txt" name="REMARK' + partId + '" id="REMARK' + partId + '" type="text"/></td>';
    cell12.innerHTML = '<td><input  type="button" class="u-button"  name="queryBtn4" value="删除" onclick="deleteTblRow(' + (tbl.rows.length - 1) + ');" /></td></tr>';

}

function deleteTblRow(rowNum) {
    var tbl = document.getElementById('file');
    tbl.deleteRow(rowNum);
    for (var i = rowNum; i < tbl.rows.length; i++) {
        tbl.rows[i].cells[1].innerText = i - 1;
        tbl.rows[i].cells[11].innerHTML = "<input type=\"button\" class=\"u-button\"  name=\"deleteBtn\" value=\"删除\" onclick='deleteTblRow(" + i + ")'/></td></tr>";
        if (i % 2 == 0) {
            tbl.rows[i].className = "table_list_row1";
        } else {
            tbl.rows[i].className = "table_list_row2";
        }
    }
}

function setCheckModel() {
    var returnDealer = document.getElementById("RETURN_DEALER").value;
    if (returnDealer == null || returnDealer == "") {
        MyAlert("退货单位不能为空!");
        return;
    }

    var flag = $("#myFlag")[0].value;
	var salOrg = $("#SALE_ORG")[0].value;
	if(flag==2&&salOrg==""){
		MyAlert("请选择销售单位!");
		return;
	}
	
    var remark = document.getElementById("REMARK").value;
    if (remark == "") {
        MyAlert("请填写退货原因！");
        return;
    }

    var chk = document.getElementsByName("cb");
    var l = chk.length;
    var cnt = 0;
    for (var i = 0; i < l; i++) {
        if (chk[i].checked) {
            cnt++;
            var applyQty = document.getElementById("APPLY_QTY" + chk[i].value).value;//退货数量
            var normalQty = document.getElementById("NORMAL_QTY" + chk[i].value).value;//可用库存
            var pattern1 = /^[1-9][0-9]*$/;
            if (!pattern1.exec(applyQty)) {
                MyAlert("第" + (i + 1) + "行的退货数量只能输入非零的正整数!");
                return;
            }
            if (parseInt(applyQty) > parseInt(normalQty)) {
                MyAlert("第" + (i + 1) + "行的退货数量不能大于可用库存,请重新输入!");
                return;
            }
            var soCode = document.getElementById("soCode").value;
            if (soCode != null && soCode != "") {//如果有销售单号,那么就要判断采购数量和退货数量
                var buyQty = document.getElementById("BUY_QTY" + chk[i].value).value;//采购数量
                if (parseInt(applyQty) > parseInt(buyQty)) {
                    MyAlert("第" + (i + 1) + "行的退货数量不能大于采购数量,请重新输入!");
                    return;
                }
            }
        }
    }
    if (cnt == 0) {
        MyAlert("请选择退货明细！");
        return;
    }

    MyConfirm("确认保存？", saveApply);
}

function saveApply() {
    btnDisable();
    var url = "<%=contextPath%>/parts/storageManager/partReturnManager/PartDlrReturnApplyQuery/saveApply.json";
    makeNomalFormCall(url, getResult, 'fm');
}

function getResult(jsonObj) {
	btnEnable();
    if (jsonObj) {
        var success = jsonObj.success;
        var error = jsonObj.error;
        var exceptions = jsonObj.Exception;
        if (success) {
            MyAlert(success);
            window.location.href = '<%=contextPath%>/parts/storageManager/partReturnManager/PartDlrReturnApplyQuery/partReturnApplyInit.do';
        } else if (error) {
            MyAlert(error);
        } else if (exceptions) {
            MyAlert(exceptions.message);
        }
    }
}

//返回查询页面
function goback() {
    window.location.href = '<%=contextPath%>/parts/storageManager/partReturnManager/PartDlrReturnApplyQuery/partReturnApplyInit.do';
}
</script>
</head>
<body>
	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 配件管理&gt;配件退货管理&gt;销售退货申请&gt;新增申请
		</div>
		<form name="fm" id="fm" method="post" enctype="multipart/form-data">
			<input type="hidden" id="myFlag" value="${flag}" />
			<div class="form-panel">
				<h2>
					<img src="<%=request.getContextPath()%>/img/subNav.gif" /> 信息
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">退货单号：</td>
							<td>${returnCode }
								<input type="hidden" name="returnCode" id="returnCode" value="${returnCode }" />
							</td>
							<td class="right">制单单位：</td>
							<td>${createOrgName }
								<input type="hidden" name="orgId" id="orgId" value="${orgId}" />
								<input type="hidden" name="orgName" id="orgName" value="${createOrgName}" />
							</td>
						</tr>
						<tr>
							<td class="right">退货单位：</td>
							<td>
								<c:if test="${flag!=2}">
									<input class="middle_txt" type="text" readonly="readonly" id="childorgName" name="childorgName" />
									<input class="mark_btn" type="button" value="&hellip;" onclick="showChildOrg('RETURN_DEALER','childorgName','childorgId','WH_ID','WH_NAME');" />
									<input id="RETURN_DEALER" name="RETURN_DEALER" type="hidden" value="">
									<input id="childorgId" name="childorgId" type="hidden" value="">
									<font color="red">*</font>
								</c:if>
								<c:if test="${flag==2}">
									<input id="childorgId" name="childorgId" type="hidden" value="${tmDealerPO.dealerId}">
									<input class="middle_txt" type="hidden" id="childorgName" name="childorgName" value="${tmDealerPO.dealerName}" />
									<input type="hidden" name="RETURN_DEALER" id="RETURN_DEALER" value="${tmDealerPO.dealerId},${tmDealerPO.dealerCode},${tmDealerPO.dealerName}" />
				                    ${tmDealerPO.dealerName}
				                </c:if>
							</td>

							<td class="right">销售单位：</td>
							<td>
								<c:if test="${flag==1}">
									<select id="SALE_ORG" name="SALE_ORG" class="u-select" style="width: 200px;">
										<c:forEach items="${parentOrg}" var="porg">
											<option value="${porg.parentorgId},${porg.parentorgCode},${porg.parentorgName}">${porg.parentorgName }</option>
										</c:forEach>
									</select>
								</c:if>
								<c:if test="${flag==2}">
									<select id="SALE_ORG" name="SALE_ORG" class="u-select" style="width: 200px;" onchange="changeDiv1()">
										<%--<option value="">--请选择--</option>--%>
										<c:forEach items="${parentOrg}" var="porg">
											<option value="${porg.parentorgId},${porg.parentorgCode},${porg.parentorgName}">${porg.parentorgName }</option>
										</c:forEach>
									</select>
									<font color="red">*</font>
								</c:if>
								<c:if test="${flag==0}">
									<input type="hidden" name="SALE_ORG" id="SALE_ORG" value="${tmOrgPO.orgId},${tmOrgPO.orgCode},${tmOrgPO.orgName}" />
				                    ${tmOrgPO.orgName}
				                </c:if>
							</td>
						</tr>
						<tr style="display: none">
							<td class="right">仓库：</td>
							<td>
								<input id="WH_NAME" name="WH_NAME" type="text" class="middle_txt" readonly="readonly" value="${wDefinePO.whName }" />
								<input id="WH_ID" name="WH_ID" type="hidden" class="middle_txt" value="${wDefinePO.whId }" />
							</td>
						</tr>
						<tr>
							<td class="right">入库单号：</td>
							<td>
								<input id="soCode" name="soCode" type="text" class="middle_txt" readonly="readonly" />
								<input class="mark_btn" type="button" value="&hellip;" onclick="showSoCode('soCode');" />
								<font color="red">*</font>
								<input class=u-button onclick="clearInput();" value=清除 type=button name=clrBtn /	>
							</td>
						<tr>
							<td class="right">退货原因：</td>
							<td colspan="3">
								<textarea class="form-control" name="REMARK" id="REMARK" style="width: 80%; display: inline;" rows="4"></textarea>
								<font color="red">*</font>
							</td>
						</tr>
					</table>
				</div>
			</div>
			<table id="file" class="table_list" style="border-bottom: 1px;">
			<caption><img src="<%=contextPath%>/img/nav.gif" />退货明细</caption>
				<tr class="table_list_row0">
					<th>
						<input type="checkbox" onclick="selAll2(this)" id="ckAll" />
					</th>
					<th>序号</th>
					<th>配件编码</th>
					<th>配件名称</th>
					<th>件号</th>
					<th>单位</th>
					<th>销售价格</th>
					<th>采购数量</th>
					<th>可用库存</th>
					<th>退货数量<font color="red">*</font></th>
					<th>备注</th>
					<th>操作</th>
				</tr>
			</table>
			<table style="width: 100%;">
				<tr>
					<td align="center">
						<input type="button" name="saveBtn" id="saveBtn" value="保 存" onclick="setCheckModel();" class="u-button" />
						<input type="button" name="backBtn" id="backBtn" value="返 回" onclick="javascript:goback();" class="u-button" />
					</td>
				</tr>
			</table>
			<FIELDSET>
				<LEGEND style="MozUserSelect: none; KhtmlUserSelect: none" unselectable="on">
					<th colspan="4" style="background-color: #DAE0EE; font-weight: normal; color: #416C9B; padding: 2px; line-height: 1.5em; border: 1px solid #E7E7E7;">
						<img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav" /> <font color="blue">配件信息</font>
						<input type="button" class="u-button" name="addPartViv" id="addPartViv" value="增加" onclick="addPartDiv()" />
					</th>
				</LEGEND>
				<div style="display: none; heigeht: 5px" id="partDiv">
					<table class="table_query" width=100% border="0" align="center" cellpadding="1" cellspacing="1">
						<tr>
							<td class="right">配件编码：</td>
							<td>
								<input class="middle_txt" id="PART_OLDCODE" datatype="1,is_noquotation,30" name="PART_OLDCODE" type="text" />
							</td>
							<td class="right">配件名称：</td>
							<td>
								<input class="middle_txt" id="PART_CNAME" datatype="1,is_noquotation,30" name="PART_CNAME" type="text" />
							</td>
						</tr>
						<tr>
							<td class="center" colspan="6">
								<input class="u-button u-query" type="button" name="BtnQuery" id="queryBtn" value="查 询" onclick="__extQuery__(1)" />
								<input class="u-button" type="button" name="BtnQuery" id="queryBtn" value="添加" onclick="addCells()" />
							</td>
						</tr>
					</table>
					<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
					<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
				</div>
			</FIELDSET>
		</form>
	</div>
</body>
</html>
