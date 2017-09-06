<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
    String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件销售退货申请修改</title>
<style type="text/css">
.table_list_row0 td {
	background-color: #FFFFCC;
	border: 1px solid #DAE0EE;
	white-space: nowrap;
}
</style>
<script type="text/javascript">
$(function(){
	__extQuery__(1);
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
    {header: "可用库存", dataIndex: 'NORMAL_QTY', align: 'center'},
    {header: "销售价格", dataIndex: 'BUY_PRICE', style: 'text-align:right'},
    {header: "采购数量", dataIndex: 'BUY_QTY', align: 'center'},
    {header: "已退货数量", dataIndex: 'RETURN_QTY', align: 'center'},
    {header: "退货数量<font color='red'>*</font>", dataIndex: 'BUY_QTY', align: 'center', renderer: insertApplyQtyInput}
    //{header: "备注", align: 'center', renderer: insertRemarkInput}
];
function seled(value, meta, record) {
    return "<input type='checkbox' value='" + value + "' name='ck' id='ck"+record.data.PART_ID+"' onclick='chkPart()'/>";
}

//插入文本框
function insertApplyQtyInput(value, meta, record) {
    var partId = record.data.PART_ID;
    var output = '<input type="text" class="short_txt"  id="APPLY_QTY1' + partId + '" name="APPLY_QTY1' + partId + '" size ="10" style="background-color:#FF9"';
    output += ' onchange="check(this,'+partId+', \'ck\');" />';
    output += '<input type="hidden" id="BUY_QTYck' + record.data.PART_ID + '" value="'+record.data.BUY_QTY+'"/>';
    output += '<input type="hidden" id="NORMAL_QTYck' + record.data.PART_ID + '" value="'+record.data.NORMAL_QTY+'"/>';
    output += '<input type="hidden" id="RETURN_QTYck' + record.data.PART_ID + '" value="'+record.data.RETURN_QTY+'"/>';
    return output;
}

function check(obj,partId, h) {
	var pattern1 = /^[1-9][0-9]*$/;
    if (!pattern1.exec(obj.value)) {
        //MyAlert("请录入正整数且必须大于0！");
        obj.value = obj.value.replace(/\D/g, '');
        obj.focus();
    }
    if (isNumber(obj.value)) {
        if (obj.value == 0) {
            MyAlert("请输入正整数且必须大于0！");
    	    obj.value="";
            return;
        }

    }
    var normalQty = $('#NORMAL_QTY'+h+partId).val();
    if(parseInt(obj.value) > parseInt(normalQty)){
	    MyAlert("退货数量不能大于可用库存！");
	    obj.value="";
	    return;
    }
    
    var buyQty = $('#BUY_QTY'+h+partId).val();
    var retrurnQty = $('#RETURN_QTY'+h+partId).val();
    if(parseInt(obj.value) > parseInt(buyQty) - parseInt(retrurnQty)){
	    MyAlert("退货数量不能大于采购数量与已退货数量的差！");
	    obj.value="";
	    return;
    }
    h = !h || h == '' ? 'cb' : h;
    $("#"+h+partId)[0].checked=true;
    chkPart(h);
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

function chkPart(h) {
    var cks = document.getElementsByName(h);
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

function showSoCode(soCode, soId) {
    var childorgId = $("#childorgId")[0].value;
    var saleOrgId = $("#SALE_ORG")[0].value.split(",")[0];
    if (!soCode) {
        soCode = null;
    }
    if (!childorgId) {
        MyAlert("请先选择退货单位!");
        return;
    }
    var url = "<%=contextPath%>/jsp/parts/storageManager/partReturnManager/partDlrReturnApply/soCodeSelect.jsp";
    url += "?childorgId=" + childorgId;
    url += "&soId=" + soId;
    url += "&soCode=" + soCode;
    url += "&saleOrgId=" + saleOrgId;
    OpenHtmlWindow(url, 730, 550);
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

    if(!code && code == ''){
		addPartViv.value = "增加";
		partDiv.style.display = "none";
    }else{
		addPartViv.value = "收起";
		partDiv.style.display = "block";
		__extQuery__(1);
    }
	if (len > 1) {
		//改变退货单位之后就要删除退货明细,重新选择
		for (var i = tbl.rows.length - 1; i >= 1; i--) {
		    tbl.deleteRow(i);
		}
	}
//     if (orgId != childOrgId) {
//     	$("#soCode")[0].value="";
//         if (len > 1) {
//             //改变退货单位之后就要删除退货明细,重新选择
//             for (var i = tbl.rows.length - 1; i >= 1; i--) {
//                 tbl.deleteRow(i);
//             }
//         }
//         childOrgId = orgId;
//         if (partDiv.style.display == "block") {
//             addPartViv.value = "增加";
//             partDiv.style.display = "none";
//         }
//     }
//     if (code != curSoCode) {
//         if (len > 2) {
//             //改变销售单号之后就要删除退货明细,重新选择
//             for (var i = tbl.rows.length - 1; i >= 2; i--) {
//                 tbl.deleteRow(i);
//             }
//         }
//         curSoCode = code;
//         if (partDiv.style.display == "block") {
//             addPartViv.value = "增加";
//             partDiv.style.display = "none";
//         }
//     }

//     var saleOrg = document.getElementById("SALE_ORG");//销售单位
<%--     if (<%=dealerId%>==orgId){//如果退货单位选择的是当前用户所在单位,那该退货单位就是供应中心,那它的销售单位就只是车厂 --%>
//         saleOrg.options.length = 0;//先删除所有选项
//         for (var i = 0; i < parentOrg.length; i++) {
//             var objItemText = parentOrg[i].substr(parentOrg[i].lastIndexOf(",") + 1);
//             var varItem = new Option(objItemText, parentOrg[i]);
//             saleOrg.options.add(varItem);
//         }
//     }else{
// 	    if (orgId && saleOrg.options) {//如果是退货单位选的是一般服务商,那销售单位就是当前供应中心
// 	        saleOrg.options.length = 0;//先删除所有选项
<%-- 	        var varItem = new Option('<%=dealerName%>', '<%=dealerId%>,<%=dealerCode%>,<%=dealerName%>'); --%>
// 	        saleOrg.options.add(varItem);
// 	    }
// 	}

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
            if (validateCell(partId)) {
                var partOldcode = mt.rows[i].cells[2].innerText;  //配件编码
                var partCname = mt.rows[i].cells[3].innerText;  //配件名称
				var partCode = mt.rows[i].cells[4].innerText;  //件号
                var unit = mt.rows[i].cells[5].innerText;  //单位
                var normalQty = mt.rows[i].cells[6].innerText;  // 可用库存
                var buyPrice = mt.rows[i].cells[7].innerText;  //销售价格
                var buyQty = mt.rows[i].cells[8].innerText;  //采购数量
                var returnQty = mt.rows[i].cells[9].innerText;  //已退货数量
				var applyQty = $("#APPLY_QTY1" + partId)[0].value;
                addCell(partId, partOldcode, partCname, partCode, buyQty, buyPrice, applyQty, normalQty, unit, returnQty);
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

function addCell(partId, partOldcode, partCname, partCode, buyQty, buyPrice, applyQty,normalQty,unit, returnQty) {
    var tbl = document.getElementById('file');
    var rowObj = tbl.insertRow(tbl.rows.length);
    rowObj.className = "table_list_row2";
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

    cell1.innerHTML = '<tr><td align="center" nowrap><input  type="checkbox" value="' + partId + '" id="cell_' + (tbl.rows.length - 1) + '" name="cb" checked="true" onclick="chkPart1();"/></td>';
    cell2.innerHTML = '<td align="center" nowrap><input id="idx_' + partId + '" name="idx_' + partId + '" value="' + (tbl.rows.length - 1) + '" type="hidden" ></td>' + (tbl.rows.length - 1);
    cell3.innerHTML = '<td align="left" nowrap><input name="PART_OLDCODE' + partId + '" id="PART_OLDCODE' + partId + '" value="' + partOldcode + '" type="hidden" />' + partOldcode + '</td>';
    cell4.innerHTML = '<td align="left" nowrap><input name="PART_CNAME' + partId + '" id="PART_CNAME' + partId + '" value="' + partCname + '" type="hidden" class="cname_' + partId + '"/>' + partCname + '</td>';
    cell5.innerHTML = '<td align="left" nowrap><input name="PART_CODE' + partId + '" id="PART_CODE' + partId + '" value="' + partCode + '" type="hidden"/>' + partCode + '</td>';
    cell6.innerHTML = '<td align="left" nowrap><input name="UNIT' + partId + '" id="UNIT' + partId + '" value="' + unit + '" type="hidden"/>' + unit + '</td>';
    cell7.innerHTML = '<td align="right" nowrap><input name="BUY_PRICE' + partId + '" id="BUY_PRICE' + partId + '" value="' + buyPrice + '" type="hidden" />' + buyPrice + '</td>';
    cell8.innerHTML = '<td align="center" nowrap><input name="BUY_QTY' + partId + '" id="BUY_QTY' + partId + '" value="' + buyQty + '" type="hidden" />' + buyQty + '</td>';
    cell9.innerHTML = '<td align="center" nowrap><input name="NORMAL_QTY' + partId + '" id="NORMAL_QTY' + partId + '" value="' + normalQty + '" type="hidden"/>' + normalQty + '</td>';
    cell10.innerHTML = '<td align="center" nowrap><input name="RETURN_QTY' + partId + '" id="RETURN_QTY' + partId + '" value="' + returnQty + '" type="hidden"/>' + returnQty + '</td>';
    cell11.innerHTML = '<td align="center" nowrap><input name="APPLY_QTY' + partId + '" id="APPLY_QTY' + partId + '" value="' + applyQty + '" type="text" class="short_txt" onchange="check(this,\''+partId+'\', \'\');" /></td>';
    cell12.innerHTML = '<td align="center" nowrap><input class="short_txt" name="REMARK' + partId + '" id="REMARK' + partId + '" type="text"/></td>';
    cell13.innerHTML = '<td><input  type="button" class="u-button" name="queryBtn4" value="删除" onclick="deleteTblRow(' + (tbl.rows.length - 1) + ');" /></td></tr>';

}

function deleteTblRow(rowNum) {
    var tbl = document.getElementById('file');
    tbl.deleteRow(rowNum);
    for (var i = rowNum; i < tbl.rows.length; i++) {
        tbl.rows[i].cells[1].innerText = i;
        tbl.rows[i].cells[12].innerHTML = "<input type=\"button\" class=\"u-button\"  name=\"deleteBtn\" value=\"删除\" onclick='deleteTblRow(" + i + ")'/></td></tr>";
    }
}

function setCheckModel() {
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
            var partId = chk[i].value;
            var obj = document.getElementById('APPLY_QTY'+partId);
        	var pattern1 = /^[1-9][0-9]*$/;
            if (!pattern1.exec(obj.value)) {
                //MyAlert("请录入正整数且必须大于0！");
                obj.value = obj.value.replace(/\D/g, '');
                obj.focus();
            }
            if (isNumber(obj.value)) {
                if (obj.value == 0) {
                    MyAlert("第" + (i + 1) + "行请输入正整数且必须大于0！");
            	    obj.value="";
                    return;
                }

            }
            var normalQty = $('#NORMAL_QTY'+partId).val();
            if(parseInt(obj.value) > parseInt(normalQty)){
        	    MyAlert("第" + (i + 1) + "行退货数量不能大于可用库存！");
        	    obj.value="";
        	    return;
            }
            
            var buyQty = $('#BUY_QTY'+partId).val();
            var retrurnQty = $('#RETURN_QTY'+partId).val();
            if(parseInt(obj.value) > parseInt(buyQty) - parseInt(retrurnQty)){
        	    MyAlert("第" + (i + 1) + "行退货数量不能大于采购数量与已退货数量的差！");
        	    obj.value="";
        	    return;
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
    var url = "<%=contextPath%>/parts/storageManager/partReturnManager/PartDlrReturnApplyQuery/updateApply.json";
    sendAjax(url, getResult, 'fm');
}

function getResult(jsonObj) {
	btnEnable();
    if (jsonObj) {
        var success = jsonObj.success;
        var error = jsonObj.error;
        var exceptions = jsonObj.Exception;
        if (success) {
            MyAlert(success, function(){
	            window.location.href = '<%=contextPath%>/parts/storageManager/partReturnManager/PartDlrReturnApplyQuery/partReturnApplyInit.do';
            });
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
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 配件管理&gt;配件退货管理&gt;销售退货申请&gt;修改申请
		</div>
		<form name="fm" id="fm" method="post" enctype="multipart/form-data">
			<input type="hidden" name="returnId" value="${po['RETURN_ID']}" />
			<input type="hidden" name="inCode" id="inCode" value="${po['IN_CODE']}" />
			<input type="hidden" name="saleOrgId" id="saleOrgId" value="${po['SELLER_ID']}" />
			<input type="hidden" name="childorgId" id="childorgId" value="${po['DEALER_ID']}">
			<input type="hidden" name="delDtlId" id="delDtlId">
			<input type="hidden" name="upDtlId" id="upDtlId">

			<div class="form-panel">
				<h2>
					<img src="<%=request.getContextPath()%>/img/subNav.gif" /> 信息
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">退货单号：</td>
							<td>${po["RETURN_CODE"]}</td>
							<td class="right">制单单位：</td>
							<td>${po["ORG_NAME"]}</td>
						</tr>
						<tr>
							<td class="right">退货单位：</td>
							<td>${po["DEALER_NAME"]}</td>

							<td class="right">销售单位：</td>
							<td>${po["SELLER_NAME"]}</td>

						</tr>
						<tr style="display: none">
							<td class="right">仓库：</td>
							<td width="30%">
								<input id="WH_ID" name="WH_ID" type="hidden" class="middle_txt" value="${po['STOCK_OUT']}" />
							</td>
						</tr>
						<tr>
							<td class="right">入库单号：</td>
							<td>${po["IN_CODE"]}</td>

						</tr>
						<tr>
							<td class="right">退货原因：</td>
							<td colspan="3">
								<textarea class="form-control" name="REMARK" id="REMARK" style="width: 80%" rows="4">${po["REMARK"]}</textarea>
							</td>
						</tr>
					</table>
				</div>
			</div>

			<table id="file" class="table_list" style="border-bottom: 1px;">
				<caption><img src="<%=contextPath%>/img/nav.gif" />退货明细</caption>
				<tr class="table_list_row0">
					<th>
						<input type="checkbox" onclick="selAll2(this)" id="ckAll" checked="checked" />
					</th>
					<th>序号</th>
					<th>配件编码</th>
					<th>配件名称</th>
					<th>件号</th>
					<th>单位</th>
					<th>销售价格</th>
					<th>采购数量</th>
					<th>可用库存</th>
					<th>已退货数量</th>
					<th>退货数量<font color="red">*</font></th>
					<th>备注</th>
					<th>操作</th>
				</tr>

				<c:if test="${list !=null}">
					<c:forEach items="${list}" var="list" varStatus="_sequenceNum" step="1">
						<c:if test="${((_sequenceNum.index+1) mod 2) != 0}">
							<tr class="table_list_row1">
						</c:if>
						<c:if test="${((_sequenceNum.index+1) mod 2) == 0}">
							<tr class="table_list_row2">
						</c:if>
						<td align="center" nowrap>
							<input type="checkbox" value="${list.PART_ID}" id="cb${list.PART_ID}" name="cb" checked="checked" onclick="chkPart1();" />
						</td>
						<td align="center" nowrap>${_sequenceNum.index+1}
							<input id="idx_${list.PART_ID}" name="idx_${list.PART_ID}" value="${_sequenceNume.index+1}" type="hidden">
							<input id="dtlId_${list.PART_ID}" name="dtlId_${list.PART_ID}" value="${list.DTL_ID}" type="hidden">
						</td>
						<td align="center">
							<input name="PART_OLDCODE${list.PART_ID}" id="PART_OLDCODE${list.PART_ID}" value="${list.PART_OLDCODE}" type="hidden" />${list.PART_OLDCODE}
						</td>
						<td align="center" nowrap>
							<input name="PART_CNAME${list.PART_ID}" id="PART_CNAME${list.PART_ID}" value="${list.PART_CNAME}" type="hidden" class="cname_${list.PART_ID}" />${list.PART_CNAME}
						</td>
						<td align="center" nowrap>
						  <input name="PART_CODE${list.PART_ID}" id="PART_CODE${list.PART_ID}" value="${list.PART_CODE}" type="hidden" />${list.PART_CODE}
						</td>
						<td align="center" nowrap>
							<input name="UNIT${list.PART_ID}" id="UNIT${list.PART_ID}" value="${list.UNIT}" type="hidden" />${list.UNIT}
						</td>
						<td align="center" nowrap>
							<input name="BUY_PRICE${list.PART_ID}" id="BUY_PRICE${list.PART_ID}" value="${list.BUY_PRICE}" type="hidden" />${list.BUY_PRICE}
						</td>
						<td align="center" nowrap>
							<input name="BUY_QTY${list.PART_ID}" id="BUY_QTY${list.PART_ID}" value="${list.BUY_QTY}" type="hidden" />${list.BUY_QTY}
						</td>
						<td align="center" nowrap>
							<input name="NORMAL_QTY${list.PART_ID}" id="NORMAL_QTY${list.PART_ID}" value="${list.NORMAL_QTY}" type="hidden" />${list.NORMAL_QTY}
						</td>
						<td align="center" nowrap>
							<input name="RETURN_QTY${list.PART_ID}" id="RETURN_QTY${list.PART_ID}" value="${list.IN_RETURN_QTY}" type="hidden" />${list.IN_RETURN_QTY}
						</td>
						<td align="center" nowrap>
							<input class="short_txt" name="APPLY_QTY${list.PART_ID}" id="APPLY_QTY${list.PART_ID}" value="${list.APPLY_QTY}" type="text" onchange="check(this,'${list.PART_ID}', '');" />
						</td>
						<td align="center" nowrap>
							<input class="short_txt" name="REMARK${list.PART_ID}" id="REMARK${list.PART_ID}" value="${list.REMARK}" type="text" />
						</td>
						<td>
							<input type="button" class="u-button" value="删除" onclick="deleteDtl('${list.DTL_ID}','${_sequenceNum.index+1}','${list.RETURN_ID}');" />
						</td>
						</tr>
					</c:forEach>
				</c:if>
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
					</th>
				</LEGEND>
				<div id="partDiv">
					<table class="table_query" width=100% border="0" align="center" cellpadding="1" cellspacing="1">
						<tr>
							<td class="right">编码：</td>
							<td>
								<input class="middle_txt" id="PART_OLDCODE" datatype="1,is_noquotation,30" name="PART_OLDCODE" type="text" />
							</td>

							<td class="right">名称：</td>
							<td>
								<input class="middle_txt" id="PART_CNAME" datatype="1,is_noquotation,30" name="PART_CNAME" type="text" />
							</td>
						</tr>
						<tr>
							<td class="center" colspan="6">
								<input class="u-button" type="button" name="BtnQuery" id="queryBtn" value="查 询" onclick="__extQuery__(1)" />
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
