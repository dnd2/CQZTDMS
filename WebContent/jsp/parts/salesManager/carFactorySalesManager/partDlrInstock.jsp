<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
    String contextPath = request.getContextPath();
%>

<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>配件接收入库-生成入库单</title>
<script type="text/javascript" charset=utf-8>
function getIdx() {
    document.write(document.getElementById("file").rows.length - 1);
}
//获取CHECKBOX
function getCb(trlineId) {
    document.write("<input type='checkbox' id='cell_" + (document.getElementById("file").rows.length - 3) + "' name='cb' onclick='countAll()' checked value='" + trlineId + "' />");
}
function instockConfirm() {
    if (!validateData()) {
        return;
    }
    MyConfirm("确定要接收入库?", instock, []);
}
function instock() {
    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrInstock/createInstock.json?";
    disableAllClEl();
    makeNomalFormCall(url, getResult, 'fm');
}
function getResult(jsonObj) {
    enableAllClEl();
    if (jsonObj != null) {
        var success = jsonObj.success;
        var error = jsonObj.error;
        var exceptions = jsonObj.Exception;
        var inId = jsonObj.inId;
        if (success) {
            MyAlert(success, function() {
	            if (jsonObj.expFlag == "1") {
	            	MyConfirm("有异常入库数据，确认要打印?",confirmResultYes,[inId],confirmResultNo,[]);
	                /* if (confirm("有异常入库数据，确认要打印?")) {
	                    payApplyPrint(inId);
	                } else {
	                    disableAllClEl();
	                    back();
	                } */
	            } else{
	                back();
	            }
			});
        } else if (error) {
            MyAlert(error);
        } else if (exceptions) {
            MyAlert(exceptions.message);
        }
    }
}


function confirmResultYes(inId){
	payApplyPrint(inId);
}
function confirmResultNo(){
	disableAllClEl();
    back();
}

function validateData() {
    if ("" == document.getElementById("whId").value) {
        MyAlert("请选择入库仓库!</br>");
        return;
    }
    var msg = "";
    if ("" == document.getElementById("RDate").value) {
        msg += "请选择到货时间!</br>";
    }
    if ("" == document.getElementById("whId").value) {
        msg += "请选择入库仓库!</br>";
    }

    var trlineArr = document.getElementsByName("partId");
    var totalQty = 0;
    for (var i = 0; i < trlineArr.length; i++) {
        var trlineId = trlineArr[i].value;
        if ("" == document.getElementById("inType_" + trlineId).value) {
            msg += "请选择第" + (i + 1) + "行的验货状态!</br>";
        } else {
            if (document.getElementById("inType_" + trlineId).value !=<%=Constant.CAR_FACTORY_INSTOCK_APPROVAL_STATE_01%>) {
                        if ("" == document.getElementById("exceptionNum_" + trlineId).value) {
                            msg += "请选择第<font color='red' >" + (i + 1) + "</font>行的缺/坏件数量!</br>";
                        }
                        if ("" == document.getElementById("remark_" + trlineId).value) {
                            msg += "请选择第<font color='red' >" + (i + 1) + "</font>行的备注!</br>";
                        }
                    }
                }
                if ("" == document.getElementById("inQty_" + trlineId).value) {
                    msg += "请填写第" + (i + 1) + "行的入库数量!</br>";
                } else {
                    totalQty = parseFloat(totalQty) + parseFloat(document.getElementById("inQty_" + trlineId).value)
                }
//                 if ("" == document.getElementById("locId_" + trlineId).value) {
//                     msg += "第" + (i + 1) + "行配件不存在货位，请维护货位!</br>";
//                 }

            }
            if (parseFloat(totalQty) <= parseFloat(0)) {
                msg += "请填写入库总量不能为0!";
            }
            if ("" != msg) {
                MyAlert(msg);
                return false;
            }
            return true;
        }
        function goBack() {
            window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrInstock/partDlrInstockInit.do";
}
function disableAllA() {
    var inputArr = document.getElementsByTagName("a");
    for (var i = 0; i < inputArr.length; i++) {
        inputArr[i].disabled = true;
    }
}
function enableAllA() {

    var inputArr = document.getElementsByTagName("a");
    for (var i = 0; i < inputArr.length; i++) {
        inputArr[i].disabled = false;
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
function disableAllClEl() {
    disableAllA();
    disableAllBtn();
}
function enableAllClEl() {
    enableAllBtn();
    enableAllA();
}
function validateQry(obj, qty) {
    var re = /^[0-9]+[0-9]*]*$/;
    if (!re.test(obj.value)) {
        MyAlert("请输入正整数!");
        obj.value = "0";
        return;
    }
    if (obj.value > qty) {
        MyAlert("入库数量要小于等于发运数量!");
        obj.value = "0";
        return;
    }
}

function getLoc(whId, transId) {
    disableAllClEl();
    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrInstock/getLoc.json?whId=" + whId + "&transId=" + transId;
    makeNomalFormCall(url, getPartLocResult, 'fm');
}
function getPartLocResult(jsonObj) {
    if (jsonObj != null) {

        var exceptions = jsonObj.Exception;
        var partAr = [];
        partAr = jsonObj.locList;
        if (exceptions) {
            MyAlert(exceptions.message);
            return;
        }
        for (var i = 0; i < partAr.length; i++) {
            if (partAr[i].PART_ID != "") {
                var trlineId = partAr[i].PART_ID;
                if (document.getElementById("locName_" + trlineId)) {
                    document.getElementById("locName_" + trlineId).value = partAr[i].LOC_NAME;
                    document.getElementById("locId_" + trlineId).value = partAr[i].LOC_ID;
                    document.getElementById("stock_" + trlineId).value = partAr[i].QTY;
                }
            }
        }
    }
    enableAllClEl();
}
function changePartState(obj, trId) {
    if (obj.value != "" && obj.value !=<%=Constant.CAR_FACTORY_INSTOCK_APPROVAL_STATE_01%>) {
        document.getElementById("exceptionNum_" + trId).disabled = false;
        document.getElementById("exceptionNum_" + trId).style.background = "#FF0033";
    } else {
        document.getElementById("exceptionNum_" + trId).disabled = true;
        document.getElementById("exceptionNum_" + trId).style.background = "";
        document.getElementById("exceptionNum_" + trId).value = "";
    }
}

function validateNum(obj, partId) {
    var re = /^[1-9]+[0-9]*]*$/;
    if (!re.test(obj.value)) {
        MyAlert("请输入正整数!");
        obj.value = "";
        return;
    }
    var inQty = $('#inQty_'+partId).val();
    if(!inQty || inQty == ""){
        MyAlert("请先输入入库数量!");
        obj.value = "";
        return;
    }
    if(parseInt(obj.value) > parseInt(inQty)){
        MyAlert("缺/破件个数不能大于入库数量!");
        obj.value = "";
        return;
    }
}

function payApplyPrint(value) {
    fm.action = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrInstock/payApplyPrintHtml.do?transId=" + value;
    fm.target = "_blank";
    fm.submit();
    back();
}
function back() {
    window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartDlrInstock/partDlrInstockInit.do";
}

$(function(){
    if ('${mainMap.ORDER_TYPE}'=="<%=Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04%>"){
        var tl = document.getElementsByName('trlineId');
        for (var i = 0; i < tl.length; i++) {
            $("#inQty_" + tl[i].value)[0].readOnly = true;
        }
    }
});

</script>
</head>
<body>
	<div class="wbox" style="min-width: 1025px;">
		<form name="fm" id="fm" method="post" enctype="multipart/form-data">
			<input type="hidden" name="transId" id="transId" value="${mainMap.transId}" />
			<div class="navigation">
				<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 配件管理 > 配件采购管理 &gt; 配件接收入库&gt; 入库单
			</div>
			<div class="form-panel">
				<h2>
					<img src="<%=request.getContextPath()%>/img/subNav.gif" /> 入库信息
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">入库日期：</td>
							<td>${mainMap.createDate}</td>
							<td class="right">制单人：</td>
							<td>${mainMap.createBy}</td>
						</tr>
						<tr>
							<td class="right">入库仓库：</td>
							<td>
								<select name="whId" id="whId" class="u-select">
									<c:forEach items="${wareHouseList}" var="wareHouse">
										<option value="${wareHouse.WH_ID}">${wareHouse.WH_NAME}</option>
									</c:forEach>
								</select> <font color="RED">*</font>
							</td>
							<td class="right">到货日期：</td>
							<td>
								<input name="RDate" type="text" class="middle_txt" id="RDate" value="${now}" style="width: 80px" />
								<input name="button" type="button" class="time_ico" onclick="showcalendar(event, 'RDate', false);" value=" " />
								<font color="RED">*</font>
							</td>
						</tr>
						<tr>
							<td class="right">备注：</td>
							<td colspan="3">
								<textarea class="form-control" style="width: 80%;" rows="4" id="remark" name="remark"></textarea>
							</td>
						</tr>
					</table>
				</div>
			</div>
			<div style="overflow-x: auto;">
				<table id="file" class="table_list" style="border-bottom: 1px solid #DAE0EE">
					<caption><img class="panel-icon nav" src="<%=contextPath%>/img/subNav.gif"/>配件信息</caption>
					<tr class="table_list_row1">
						<th align="center" width="3%">序号</th>
						<th align="center" width="11%">配件编码</th>
						<th align="center" width="11%">配件名称</th>
						<th align="center" width="9%">件号</th>
						<th align="center" width="9%">最小包装量</th>
						<th width="7%" align="center">单位</th>
						<!-- <th width="8%" align="center">入库货位</th> -->
						<!-- <th width="8%" align="center">入库批次</th> -->
						<th width="7%" align="center">当前库存</th>
						<th width="7%" align="center">发运数量</th>
						<th width="8%" align="center">入库数量<font color="RED">*</font> </th>
						<th align="center" width="6%">验收状态</th>
						<th align="center" width="6%">缺/破件个数</th>
						<th align="center" width="12%">备注</th>
					</tr>
					<c:forEach items="${detailList}" var="data">
						<tr class="table_list_row1">
							<td align="center" style="text-align: center">
								&nbsp;
								<input name="partId" id="partId" type="hidden" value="${data.PART_ID}">
								<script type="text/javascript">getIdx();</script>
							</td>
							<td align="center">
								<c:out value="${data.PART_OLDCODE}" />
							</td>
							<td align="center">
								<c:out value="${data.PART_CNAME}" />
							</td>
							<td align="center">
								<c:out value="${data.PART_CODE}" />
							</td>
							<td align="center">
								<c:out value="${data.MIN_PACKAGE}" />
							</td>
							<td align="center">
								<c:out value="${data.UNIT}" />
							</td>
							<%-- <td align="center">
								${data.LOC_NAME}
								<input type="hidden" name="locName_${data.PART_ID}" id="locName_${data.PART_ID}" value="${data.LOC_NAME}" />
								<input type="hidden" name="locId_${data.PART_ID}" id="locId_${data.PART_ID}" value="${data.LOC_ID}" />
							</td>
							<td align="center">
								${data.BATCH_NO}
								<input type="hidden" name="batchNo_${data.PART_ID}" id="batchNo_${data.PART_ID}" value="${data.BATCH_NO}" />
							</td> --%>
							<td align="center">
								<input name="stock_${data.PART_ID}" id="stock_${data.PART_ID}" type="hidden" value="${data.NORMAL_QTY_NOW}" />
								${data.NORMAL_QTY_NOW}
							</td>
							<td align="center">
								<input name="transQty_${data.PART_ID}" id="transQty_${data.PART_ID}" type="hidden" value="${data.TRANS_QTY}" />
								<input name="buyQty_${data.PART_ID}" id="buyQty_${data.PART_ID}" type="hidden" value="${data.BUY_QTY}" />
								<c:out value="${data.TRANS_QTY}" />
							</td>
							<td align="center">
								<input name="inQty_${data.PART_ID}" id="inQty_${data.PART_ID}" type="text" class="middle_txt" readonly onchange="validateQry(this,${data.TRANS_QTY})" value="${data.TRANS_QTY}"
									style="background-color: #FF9; width: 60px; text-align: center" />
							</td>
							<td align="center">
								<script type="text/javascript">
	                            	genSelBoxExp("inType_" +'${data.PART_ID}', <%=Constant.CAR_FACTORY_INSTOCK_APPROVAL_STATE%>, <%=Constant.CAR_FACTORY_INSTOCK_APPROVAL_STATE_01%>, true, "u-select", "style='width:70px' onchange=changePartState(this,${data.PART_ID})", "false", '');
	                       		</script>
							</td>
							<td align="center">
								<input name="exceptionNum_${data.PART_ID}" id="exceptionNum_${data.PART_ID}" type="text" disabled style="width: 60px" onchange="validateNum(this, '${data.PART_ID}')" class="middle_txt"/>
							</td>
							<td>
								<input name="remark_${data.PART_ID}" id="remark_${data.PART_ID}" type="text" class="middle_txt" value="" />
							</td>
						</tr>
					</c:forEach>
				</table>
			</div>
			<table border="0" class="table_query">
				<tr>
					<td class="center">
						<input class="u-button" type="button" value="生成入库单" onclick="instockConfirm();" />
						<input class="u-button" type="button" value="返 回" onclick="goBack()" />
					</td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>


