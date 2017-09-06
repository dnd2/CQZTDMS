<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
    String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=7">
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件销售退货入库</title>

<script type="text/javascript">
var myPage;
var url = "<%=contextPath%>/parts/storageManager/partReturnManager/PartDlrReturnInManager/queryPartDlrReturnInfo.json";
var title = null;
var columns = [
    {header: "序号", align: 'center', renderer: getIndex},
    {
        header: "<input type='checkbox'  name='ckbAll' id='ckbAll' checked onclick='selAll(this)'  />",
        dataIndex: 'DTL_ID',
        align: 'center',
        width: '33px',
        renderer: seled
    },
    {header: "配件编码", dataIndex: 'PART_OLDCODE', align: 'center'},
    {header: "配件名称", dataIndex: 'PART_CNAME', align: 'center'},
    {header: "申请退货数量", dataIndex: 'APPLY_QTY', align: 'center'},
    {header: "审核数量", dataIndex: 'CHECK_QTY', align: 'center', renderer: insertChkQtyInput},
    {header: "回运数量", dataIndex: 'CHECK_QTY', align: 'center'},
    {header: "已验收数量", dataIndex: 'IN_QTY', align: 'center'},
    {header: "待验收数量", dataIndex: 'MAX_QTY', align: 'center'},
    {header: "入库数量", dataIndex: 'IN_QTY', align: 'center', renderer: insertInQtyInput},
    {header: "退入货位", dataIndex: 'LOC_CODE', align: 'center', renderer: choice},
    {header: "备注", dataIndex: 'REMARK', align: 'center'}
];

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

function choice(value, meta, record) {
    var partId = record.data.PART_ID;
    var whId = document.getElementById('WH_ID').value;
    var whName = $("#WH_ID").find("option:selected").text();
    var partName = record.data.PART_CNAME;
    var code = record.data.PART_OLDCODE;
    var html = "";
    html += '<input type="hidden" id="OUT_LOC_ID_${list.PART_LOC_ID}" name="OUT_LOC_ID_${list.PART_LOC_ID}" value="${list.LOC_ID}" >';
    html += '<input readonly name="LOC_CODE_'+partId+'" id="LOC_CODE_'+partId+'" class="middle_txt" type="text"';
    html += 'onchange="checkCode(this, \''+partId+'\', \"'+partId+'\', \''+whId+'\', \''+whName+'\');"/>';
    html += '<input name="LOC_ID_'+partId+'" id="LOC_ID_'+partId+'" type="hidden" value=""/>';
    html += '<input name="LOC_NAME_'+partId+'" id="LOC_NAME_'+partId+'" type="hidden" value=""/>';
    html += '<input class="mini_btn" type="button" value="..."';
    html += 'onclick="codeChoice2(\''+partId+'\', \''+partId+'\',\''+code+'\', \''+partName+'\');"/>';           
//  var text = "<input name='LOC_CODE_" + id + "' id='LOC_CODE_" + id + "' class='middle_txt' type='text' value='' onchange='checkCode(this,\"" + id + "\",\"" + whId + "\",\"" + whName + "\");'/>";
//  text = text + "<input name='LOC_ID_" + id + "' id='LOC_ID_" + id + "' type='hidden' value='123' />";
//  text = text + "<input class='mini_btn' type='button' value='...' onclick='codeChoice(\"" + id + "\",\"" + id + "\",\"" + code + "\",\"" + partName + "\");'/>";
	return String.format(html);
}

function codeChoice2(partLocId, id, code, name) {
    var whId2 = document.getElementById("WH_ID").value;
    if (whId2 == '') {
        MyAlert("请先选择库房！")
        return;
    }
    var url = g_webAppName + "/parts/baseManager/partsBaseManager/PartLocation/selectLocationInit.do";
    url += "?partLocId="+partLocId+"&partId="+id+"&partOldcode="+code+"&partCname="+name+"&whId="+whId2;
    OpenHtmlWindow(encodeURI(url), 800, 400);
}

function seled(value, meta, record) {
    return "<input type='checkbox' value='" + value + "' name='ck' id='ck" + value + "' onclick='chkPart()' checked/>";
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

function insertChkQtyInput(value, meta, record) {
    var dtlId = record.data.DTL_ID;
    var partId = record.data.PART_ID;
    var output = '<input type="hidden" id="CHECK_QTY' + dtlId + '" name="CHECK_QTY' + dtlId + '" value="' + value + '"/>\n' + value
            + '<input type="hidden" id="PART_ID_' + dtlId + '"  name="PART_ID_' + dtlId + '" value="' + partId + '"/>\n';
    return output;
}

function insertInQtyInput(value, meta, record) {
    var chkQty = record.data.MAX_QTY;
    var maxQty = record.data.MAX_QTY;
    var dtlId = record.data.DTL_ID;
    var output = '<input type="hidden" id="MAX_QTY' + dtlId + '" name="MAX_QTY' + dtlId + '" value="' + maxQty + '"/>' +
            '<input type="text" onchange="check(this,' + dtlId + ');" class="short_txt" id="IN_QTY' + dtlId + '" name="IN_QTY' + dtlId + '" value="' + chkQty + '" size ="10" style="background-color:#FF9"/>\n';
    return output;
}

function check(obj, dtlId) {
    var pattern1 = /^[1-9][0-9]*$/;
    if (!pattern1.exec(obj.value)) {
//         MyAlert("请录入正整数且必须大于0！");
        obj.value = obj.value.replace(/\D/g, '');
        obj.focus();
    }
    if (isNumber(obj.value)) {
        if (obj.value == 0) {
            MyAlert("正整数且必须大于0！");
            obj.value = "";
            obj.focus();
            return;
        }

    }
    var maxQty = $('#MAX_QTY'+dtlId).val();
    if(parseInt(maxQty) < parseInt(obj.value)){
        MyAlert("入库数量不能大于待验收数量：" + maxQty + "！");
        obj.value = "";
        obj.focus();
        return;
    }
    $("ck" + dtlId).checked = true;
    chkPart();
}

//入库
function inPartDlrReturn() {
    var whId = $("#WH_ID").val();
    if (whId == null || whId == "") {
        MyAlert("请选择仓库!");
        return;
    }

    var ck = document.getElementsByName('ck');
    var l = ck.length;
    var cnt = 0;
    for (var i = 0; i < l; i++) {
        if (ck[i].checked) {
            cnt++;
            var dtlId = ck[i].value;
            var chkQty = document.getElementById("CHECK_QTY" + dtlId).value;//审核数量
            var inQty = document.getElementById("IN_QTY" + dtlId).value;//录入验收数量
            var maxQty = document.getElementById("MAX_QTY" + dtlId).value;//待验收数量
            var partId = document.getElementById("PART_ID_" + dtlId).value;
            var locCode = document.getElementById("LOC_CODE_" + partId).value;

            var pattern1 = /^[1-9][0-9]*$/;
            if (!pattern1.exec(inQty)) {
                MyAlert("第" + (i + 1) + "行的入库数量只能输入非零的正整数!");
                return;
            }
            if (parseInt(inQty) > parseInt(chkQty)) {
                MyAlert("第" + (i + 1) + "行的入库数量不能大于审核数量!");
                return;
            }
            if (parseInt(inQty) > parseInt(maxQty)) {
                MyAlert("第" + (i + 1) + "行的入库数量不能大于待验收数量：" + maxQty + "！");
                return;
            }
            if (locCode == '') {
                MyAlert("第" + (i + 1) + "行的入库货位不能为空!");
                return;
            }
        }
    }
    if (cnt == 0) {
        MyAlert("请选择要入库的配件！");
        return;
    }
    
    MyConfirm("确定入库?", function(){
        btnDisable();
        var url = '<%=contextPath%>/parts/storageManager/partReturnManager/PartDlrReturnInManager/inPartDlrReturn.json';
        sendAjax(url, getResult, 'fm');
    });

}

function getResult(jsonObj) {
	btnEnable();
    if (jsonObj != null) {
        var success = jsonObj.success;
        var errors = jsonObj.errors;
        var dtlError = jsonObj.dtlError;
        var exceptions = jsonObj.Exception;
        if (success) {
            MyAlert(success, function(){
	            window.location.href = '<%=contextPath%>/parts/storageManager/partReturnManager/PartDlrReturnInManager/queryPartReturnApplyInit.do';
            });
        } else if (errors && errors != "") {
            MyAlert(errors, function(){
	            window.location.href = '<%=contextPath%>/parts/storageManager/partReturnManager/PartDlrReturnInManager/queryPartReturnApplyInit.do';
            });
        } else if (dtlError && dtlError != "") {
            MyAlert(dtlError);
            __extQuery__(1);
        } else if (exceptions) {
            MyAlert(exceptions.message);
        }
    }
}

//返回查询页面
function goback() {
    window.location.href = '<%=contextPath%>/parts/storageManager/partReturnManager/PartDlrReturnInManager/queryPartReturnApplyInit.do';
}

function codeChoice(id, whId) {
    var whId2 = document.getElementById("WH_ID").value;
    if (whId2 == '') {
        MyAlert("请先选择库房！")
        return;
    }
    OpenHtmlWindow(g_webAppName + "/parts/storageManager/partDistributeMgr/PartDistributeMgr/selectLocationInit.do?loc_id=" + id + "&whId=" + whId2, 700, 400);
}

var LOC_ID = null;
function codeSet(i, c, n) {
    var v = i + "," + c + "," + n;
    if ("query" == LOC_ID) {
        $("#LOC_CODE").val(c);
    } else {
        document.getElementById("LOC_CODE_" + LOC_ID).value = c;
        document.getElementById("LOC_ID_" + LOC_ID).value = v;
    }
}
function codeSet2(i, c, pcId) {
    document.getElementById("LOC_CODE_" + pcId).value = c;
    document.getElementById("LOC_ID_" + pcId).value = i;
}
function checkCode(th, partLocId, partId, whId, whName) {
    var loc_code = th.value;
    var url2 = "<%=contextPath%>/parts/storageManager/partStockMvManager/partStockMvAction/checkSeatExist.json";
    var para = "LOC_CODE=" + loc_code + "&partLocId=" + partLocId + "&partId=" + partId + "&whId=" + whId + "&whName=" + whName;
    makeCall(url2, forBack2, para);
}
function forBack2(json) {
    if (json.returnValue != 1) {
        document.getElementById("LOC_CODE_" + json.partLocId).value = "";
        parent.MyAlert("货位【" + json.LOC_CODE + "】在仓库【" + json.whName + "】中不存在,请先维护在操作！");
    } else {
        codeSet2(json.LOC_ID, json.LOC_CODE, json.partLocId);
    }
}

$(function(){
	__extQuery__(1);
});

</script>
</head>
<style type="text/css">
#myTable {
	margin-top: 0px !important;
}

#_page {
	margin-top: 0px !important;
}
</style>
<body>
	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 配件管理&gt;配件仓库管理 >配件退货管理&gt;销售退货入库&gt;入库
		</div>
		<form name="fm" id="fm" method="post">
			<input type="hidden" name="returnId" value="${po['RETURN_ID']}" />
			<input type="hidden" name="soCode" value="${po['SO_CODE']}" />
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
							<td a>${po["ORG_NAME"]}</td>
						</tr>
						<tr>
							<td class="right">退货单位：</td>
							<td>${po["DEALER_NAME"]}</td>
							<td class="right">销售单号：</td>
							<td>${po["SO_CODE"]}</td>
						</tr>
						<tr>
							<td class="right">销售单位：</td>
							<td>${po["SELLER_NAME"]}</td>
							<td class="right">入库仓库：</td>
							<td>
								<select id="WH_ID" name="WH_ID" class="u-select">
									<%--<option value="">--请选择--</option>--%>
									<c:forEach items="${wareHouses}" var="wareHouse">
										<option value="${wareHouse.whId }">${wareHouse.whName }</option>
									</c:forEach>
								</select>
							</td>
						</tr>
						<tr>
							<td class="right">退货原因：</td>
							<td colspan="3">${po["REMARK"]}</td>
						</tr>
					</table>
				</div>
			</div>
			<div class="form-panel" style="margin-top: 2px; margin-bottom: 0px;">
				<h2 style="border-bottom: 0px;">
					<img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 配件信息
				</h2>
			</div>
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
			<table class="table_query">
				<tr>
					<td class="center">
						<input class="u-button" type="button" name="saveBtn" id="saveBtn" value="入库" onclick="inPartDlrReturn();" />
						&nbsp;
						<input class="u-button" type="button" value="返 回" onclick="javascript:goback();" />
						&nbsp;
					</td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>
