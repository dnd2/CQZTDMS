<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib prefix="fmt" uri="/jstl/fmt" %>
<%
    String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
    <title>配件出库单-生成出库单(出库)</title>
    <style>.form-panel{margin-bottom: 10px}#partDiv table{background-color: transparent}table.table_query td.bottom-button{padding-bottom:10px;}</style>
</head>
<script language="javascript">
var temp = "";
var myPage;
var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartOutstock/queryPartLocBo.json";
var title = null;
var columns = [
    {header: "序号", align: 'center', renderer: getIndex},
    {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
    {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
    {header: "件号", dataIndex: 'PART_CODE', style: 'text-align:left'},
    {header: "货位", dataIndex: 'LOC_NAME', align: 'center'},
    {header: "销售数量", dataIndex: 'SALES_QTY', align: 'center'},
    {header: "装箱数量", dataIndex: 'PKG_QTY', align: 'center'},
    {header: "现场BO数量", dataIndex: 'LOC_BO_QTY', align: 'center'},
    {header: "状态", dataIndex: 'STATUS', align: 'center'}
];

//获取选择框的值
function getCode(value) {
    var str = getItemValue(value);
    document.write(str);
}
//获取序号
function getIdx() {
    document.write(document.getElementById("file").rows.length - 2);
}
function createPartOutstockOrderConfirm() {
//     var pattern2 = /^[0-9][0-9]*$/;
    var pattern2 = /^\d+(\.\d{1,2})?$/;
	var actualAmount=document.getElementById("actualAmount").value;
	if (!pattern2.exec(actualAmount)) {
        MyAlert("发运金额金额不能为空，金额只能输入整数或两位小数!");
        return;
    }
    var partIds = document.getElementsByName("cb");
    var cnt = 0;
    for (var i = 0; i < partIds.length; i++) {
        if (partIds[i].checked) {
            cnt++;
            //var pattern1 = /^(([1-9][0-9]*)|(0))$/;
//             var pattern2 = /^[0-9][0-9]*$/;
            var pkgQty = document.getElementById("pkgedNo_" + partIds[i].value).value;
            var normalQty = document.getElementById("normalQty_" + partIds[i].value).value;
            var outstockQty = document.getElementById("outstockNo_" + partIds[i].value).value;
            if (!pattern2.exec(outstockQty)) {
                MyAlert("第" + (i + 1) + "行，出库数量只能输入正整数!");
                return;
            }
            if (parseInt(outstockQty) > parseInt(pkgQty)) {
                MyAlert("第" + (i + 1) + "行，出库数量不能大于装箱数量!");
                return;
            }
            if (parseInt(outstockQty) > parseInt(normalQty)) {
                MyAlert("第" + (i + 1) + "行，出库数量不能大于当前库存!");
                return;
            }
        }
    }
    if (cnt == 0) {
        MyAlert("请选择要出库的配件！");
        return;
    }

    MyConfirm("确定生成出库单?", createPartOutstockOrder, []);
}
function createPartOutstockOrder() {
    btnDisable();
    disableAllClEl();
    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartOutstock/createPartOutstockOrder.json?";
    makeNomalFormCall(url, getResult, 'fm');

}
function getResult(jsonObj) {
    enableAllClEl();
    if (jsonObj != null) {
        var success = jsonObj.success;
        var error = jsonObj.error;
        var exceptions = jsonObj.Exception;
        if (success) {
        	MyAlert(success);
            window.location.href = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartOutstock/partOutstockInit.do";
        } else if (error) {
            unDisabledCb();
            MyAlert(error);
        } else if (exceptions) {
            unDisabledCb();
            /*MyAlert(exceptions.message);  */
        }
    }
}
function disabledCb() {
    var cbs = document.getElementsByName("cb");
    var disabledSlineId = "";
    for (var i = 0; i < cbs.length; i++) {
        cbs[i].disabled = (!cbs[i].checked);
        if (!cbs[i].checked) {
            disabledSlineId += "," + cbs[i].value;
        }
    }
    document.getElementById("disabledSlineId").value = disabledSlineId;
}
function unDisabledCb() {
    var cbs = document.getElementsByName("cb");
    for (var i = 0; i < cbs.length; i++) {
        cbs[i].disabled = false;
    }

}
//返回
function goBack() {
    window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartOutstock/partOutstockInit.do';
}

function checkAll(obj) {
    var cb = document.getElementsByName("cb");
    for (var i = 0; i < cb.length; i++) {
        cb[i].checked = obj.checked;
    }
    countMoney();
}
function clickCheckBox() {
    var flag = true;
    var cb = document.getElementsByName("cb");
    for (var i = 0; i < cb.length; i++) {
        if (!cb[i].checked) {
            flag = false;
        }
    }
    document.getElementById("ckAll").checked = flag;

}
function countBo(partId) {
    var re = /^[0-9]+[0-9]*]*$/;
    if (!re.test(document.getElementById("outstockNo_" + partId).value)) {
        MyAlert("请输入正整数!");
        document.getElementById("outstockNo_" + partId).value = "0";
        document.getElementById("boQty_" + partId).value = document.getElementById("saleQty_" + partId).value - document.getElementById("outstockNo_" + partId).value;
        return;
    }
    if (parseFloat(document.getElementById("outstockNo_" + partId).value) > parseFloat(document.getElementById("pkgedNo_" + partId).value)) {
        MyAlert("出库数量不得大于装箱数量!");
        document.getElementById("outstockNo_" + partId).value = "0";
        document.getElementById("boQty_" + partId).value = document.getElementById("saleQty_" + partId).value - document.getElementById("outstockNo_" + partId).value;
        return;
    }
    if (parseFloat(document.getElementById("outstockNo_" + partId).value) > parseFloat(document.getElementById("normalQty_" + partId).value)) {
        MyAlert("出库数量不得大于当前库存!");
        document.getElementById("outstockNo_" + partId).value = "0";
        document.getElementById("boQty_" + partId).value = document.getElementById("saleQty_" + partId).value - document.getElementById("outstockNo_" + partId).value;
        return;
    }
    document.getElementById("boQty_" + partId).value = document.getElementById("saleQty_" + partId).value - document.getElementById("outstockNo_" + partId).value;
}

function disableAllA() {
    var inputArr = document.getElementsByTagName("a");
    for (var i = 0; i < inputArr.length; i++) {
        inputArr[i].disabled = true;
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

function enableAllA() {
    var inputArr = document.getElementsByTagName("a");
    for (var i = 0; i < inputArr.length; i++) {
        inputArr[i].disabled = false;
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
function setStyle(obj, flag) {
    obj.readOnly = flag;
}

function divDataVali(obj, saleQty) {
    var re = /^[0-9]+[0-9]*]*$/;
    if (!re.test(obj.value)) {
        MyAlert("请输入正整数!");
        obj.value = "0";
        return;
    }
    if (obj.value > saleQty) {
        MyAlert("出库数量不能大于销售数量!");
        obj.value = "0";
        return;
    }
}

function confMyDiv(obj, lineId) {
    obj.value = $('#myDivId')[0].value;
    closeDiv();
    countBo(lineId);
}

function closeDiv() {
    if ($('#myDiv')[0].style.display == "block") {
        $('#myDiv')[0].style.display = "none";
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

function addPartDiv() {
    var partDiv = document.getElementById("partDiv");
    var addPartViv = document.getElementById("addPartViv");
    if (partDiv.style.display == "block") {
        addPartViv.value = "查看";
        partDiv.style.display = "none";
    } else {
        addPartViv.value = "收起";
        partDiv.style.display = "block";
        __extQuery__(1);
    }
}

$(document).ready(function(){
	enableAllClEl();
});

</script>
</head>
<body>
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
<input type="hidden" name="pickOrderId" id="pickOrderId" value="${pickOrderId}"/>
<input type="hidden" name="whId" id="whId" value="${mainMap.WH_ID}"/>
<input type="hidden" name="pkgNos" id="pkgNos" value="${pkgNos}"/>
<input type="hidden" name="trplanId" id="trplanId" value="${trplanId}"/>
<input type="hidden" name="forwardFlag" id="forwardFlag" value="1"/>
<input type="hidden" name="disabledSlineId" id="disabledSlineId"/>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：配件管理 &gt; 配件销售管理 &gt;配件出库单 &gt;出库</div>
<div class="form-panel">
     <h2><img class="panel-icon nav" src="<%=contextPath%>/img/subNav.gif"/>配件销售信息</h2>
     <div class="form-body">
	<table border="0" class="table_query">
	    <tr>
	        <td width="10%" class="right">拣货单号：</td>
	        <td width="25%">${mainMap.PICK_ORDER_ID}</td>
	        <td width="10%" class="right">出库日期：</td>
	        <td width="25%">${mainMap.nowDate} </td>
	        <td width="10%" class="right">制单人：</td>
	        <td width="25%">${mainMap.logUserName}</td>
	    </tr>
	    <tr>
	        <td width="10%" class="right">销售单位：</td>
	        <td width="25%">${mainMap.SELLER_NAME}
	            <input type="hidden" name="SELLER_ID" value="${mainMap.SELLER_ID}"/>
	            <input type="hidden" name="SELLER_CODE" value="${mainMap.SELLER_CODE}"/>
	            <input type="hidden" name="SELLER_NAME" value="${mainMap.SELLER_NAME}"/>
	        </td>
	        <td width="10%" class="right">订货单位：</td>
	        <td width="25%">${mainMap.DEALER_NAME}
	            <input type="hidden" name="DEALER_ID" value="${mainMap.DEALER_ID}"/>
	            <input type="hidden" name="DEALER_CODE" value="${mainMap.DEALER_CODE}"/>
	            <input type="hidden" name="DEALER_NAME" value="${mainMap.DEALER_NAME}"/>
	        </td>
	        <td width="10%" class="right">订单类型：</td>
	        <td width="25%">
	            <script type="text/javascript">getCode('${mainMap.ORDER_TYPE}');</script>
	            <input type="hidden" name="ORDER_TYPE" value="${mainMap.ORDER_TYPE}"/>
	        </td>
	    </tr>
	    <%--  <tr>
	         &lt;%&ndash; <td  width="10%"  class="right">发运方式：</td>
	          <td width="25%">
	              ${mainMap.TRANS_TYPE}
	              <input type="hidden" name="TRANS_TYPE" value="${mainMap.TRANS_TYPE}"/>
	          </td>&ndash;%&gt;
	          <td width="10%"   class="right">箱数：</td>
	          <td width="25%">
	              ${mainMap.boxAllNo}
	          </td>
	          <td width="10%"  class="right"></td>
	          <td width="25%">
	          </td>
	      </tr>
	      <tr>
	          <td width="10%" class="right">箱号：</td>
	          <td colspan="5">
	              ${pkgNos}
	          </td>
	      </tr>--%>
	    <tr>
	        <td width="10%" class="right">备注：</td>
	        <td colspan="5">
	            <textarea name="REMARK" class="form-control remark" rows="4">${mainMap.REMARK}</textarea>
	        </td>
	    </tr>
	</table>
</div>
</div>
<div class="form-panel">
     <h2><img class="panel-icon nav" src="<%=contextPath%>/img/subNav.gif"/>配件发运信息</h2>
     <div class="form-body">
	<table border="0" class="table_query">
	    <tr>
	        <td width="10%" class="right">发运预估金额：</td>
	        <td width="25%">
	        	${estimatedAmount}
	        	<input type="hidden" id="estimatedAmount" name="estimatedAmount" value="${estimatedAmount}"/>
	        </td>
	        <td width="10%" class="right">发运实际金额：</td>
	        <td width="25%">
	        	<input type="text" id="actualAmount" name="actualAmount" class="middle_txt" /><font color="red">&nbsp;*</font>
	        </td>
	        <td width="10%" class="right">物流单号：</td>
	        <td width="25%">
	        	<input class="middle_txt" type="text" id="WULIU_CODE" name="WULIU_CODE"  value="" maxlength="50"/>
	        </td>
	    </tr>
	</table>
</div>
</div>
<FIELDSET class="form-fieldset">
    <LEGEND style="MozUserSelect: none; KhtmlUserSelect: none" unselectable="on">
        <th colspan="6">
            <img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav"/>
            	现场BO信息
            <input type="button" class="normal_btn" name="addPartViv"
                   id="addPartViv" value="查看" onclick="addPartDiv()"/>
        </th>
    </LEGEND>
    <div style="display: none;" id="partDiv" class="grid-resize">
        <table class="table_query" width=100% border="0" class="center"
               cellpadding="1" cellspacing="1">
            <tr>
                <td class="right" width="10%">配件编码：</td>
                <td width="20%">
                    <input class="middle_txt" id="PART_OLDCODE"
                           datatype="1,is_noquotation,30" name="PART_OLDCODE"
                           onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text"/>
                </td>
                <td class="right" width="10%"> 配件名称：</td>
                <td width="22%">
                    <input class="middle_txt" id="PART_CNAME"
                           datatype="1,is_noquotation,30" name="PART_CNAME"
                           onblur="isCloseDealerTreeDiv(event,this,'pan')" type="text"/>
                </td>
                <td width="10%" class="right"> 件号：</td>
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
                </td>
            </tr>
        </table>
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </div>
</FIELDSET>
<br/>
<table id="file" class="table_list" width="100%">
    <caption><img class="panel-icon nav" src="<%=contextPath%>/img/subNav.gif"/>配件信息</caption>
    <tr>
        <th class="center" style="display: none" width="3%">
            <input type="checkbox" name="ckAll" id="ckAll" onclick="selAll2(this)" checked/>
        </th>
        <th class="center">序号</th>
        <th class="center">配件编码</th>
        <th class="center">配件名称</th>
        <th class="center">件号</th>
        <th class="center">单位</th>
        <th class="center">最小包装量</th>
        <th class="center">出库货位</th>
        <th class="center">出库批次</th>
        <th class="center">当前库存</th>
        <th class="center">销售数量</th>
        <th class="center">装箱数量</th>
        <th class="center">出库数量</th>
    </tr>
    <c:forEach items="${detailList}" var="data">
        <tr class="table_list_row1">
            <td style="display: none" class="center">
                <input type="checkbox" value="${data.PART_ID},${data.LOC_ID}" name="cb" onclick="chkPart1();" checked/>
            </td>
            <td class="center">&nbsp;
                <script type="text/javascript">getIdx();</script>
            </td>
            <td class="center">
                <c:out value="${data.PART_OLDCODE}"/>
                <input type="hidden" name="PART_OLDCODE_${data.PART_ID},${data.LOC_ID}" value="${data.PART_OLDCODE}"/>
            </td>
            <td class="center">
                <c:out value="${data.PART_CNAME}"/>
                <input type="hidden" name="PART_CNAME_${data.PART_ID},${data.LOC_ID}" value="${data.PART_CNAME}"/>
            </td>
            <td class="center">
                <c:out value="${data.PART_CODE}"/>
                <input type="hidden" name="PART_CODE_${data.PART_ID},${data.LOC_ID}" value="${data.PART_CODE}"/>
            </td>
            <td>
				<c:out value="${data.UNIT}"/>
                <input type="hidden" name="UNIT_${data.PART_ID},${data.LOC_ID}" value="${data.UNIT}"/>
            </td>
            <td>
				<c:out value="${data.MIN_PACKAGE}"/>
                <input type="hidden" name="MIN_PACKAGE_${data.PART_ID},${data.LOC_ID}" value="${data.MIN_PACKAGE}"/>
            </td>
            <td style="text-align: center"> 
                <c:out value="${data.LOC_NAME}"/>
                <input type="hidden" name="LOC_NAME_${data.PART_ID},${data.LOC_ID}" value="${data.LOC_NAME}"/>
                <input type="hidden" name="LOC_CODE_${data.PART_ID},${data.LOC_ID}" value="${data.LOC_CODE}"/>
            </td>
            <td style="text-align: center"> 
                <c:out value="${data.BATCH_NO}"/>
                <input type="hidden" name="BATCH_NO_${data.PART_ID}" value="${data.BATCH_NO}"/>
            </td>
            <td> 
                <input type="text" name="normalQty_${data.PART_ID},${data.LOC_ID}"
                       style="border:0px;background-color:#FFFFFF;text-align:center;width: 50px;" readonly
                       id="normalQty_${data.PART_ID},${data.LOC_ID}" value="${data.NORMAL_QTY_NOW}"/>
            </td>
            <td>
				<input type="text" name="saleQty_${data.PART_ID},${data.LOC_ID}"
                       style="border:0px;background-color:#FFFFFF;text-align:center;width: 50px;" readonly
                       id="saleQty_${data.PART_ID},${data.LOC_ID}" value="${data.SALES_QTY}"/></td>
            <td>
				<input type="text" name="pkgedNo_${data.PART_ID},${data.LOC_ID}"
                       style="border:0px;background-color:#FFFFFF;text-align:center;width: 50px;" readonly
                       id="pkgedNo_${data.PART_ID},${data.LOC_ID}" value="${data.PKGEDQTY}" ondbclick=""/></td>
            <td>
				<input type="text" style="border:0px;background-color:#FFFFFF;width:70px;text-align:center"
                       name="outstockNo_${data.PART_ID},${data.LOC_ID}"
                       id="outstockNo_${data.PART_ID},${data.LOC_ID}"
                       value="${data.PKGEDQTY}" onkeyup="countBo('${data.PART_ID},${data.LOC_ID}')"
                       onchange="countBo('${data.PART_ID},${data.LOC_ID}')" readonly/>
            </td>
        </tr>
    </c:forEach>
    </tr>
</table> 
<table border="0" class="table_query">
    <tr>
        <td class="center bottom-button"><input class="u-button" type="button" value="生成出库单" onclick="createPartOutstockOrderConfirm();"/>
            <input class="normal_btn" type="button" value="返 回" onclick="goBack();"/></td>
    </tr>
</table>
</div>
</form>
</body>
</html>