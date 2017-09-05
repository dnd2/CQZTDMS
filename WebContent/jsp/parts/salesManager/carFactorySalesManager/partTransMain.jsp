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
    <title>配件发运单查询</title>
</head>
<script language="javascript">
var objArr = [];
//初始化查询TABLE
var myPage;
var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartTrans/partTransQuery.json";
var title = null;
var columns = null;

var transTypeArray = new Array(); //创建一个新的运单类型数组
<c:forEach var= "list" items="${transList}" varStatus="sta"> //得到有数据的数组集合
transTypeArray.push(['${list.fixName}&&${list.fixName}']);//得到数组的内容（实体bean)加入到新的数组里面
</c:forEach>

var transCompanyArray = new Array(); //创建一个新的承运物流数组
<c:forEach var= "list" items="${transCompList}" varStatus="sta"> //得到有数据的数组集合
transCompanyArray.push(['${list.fixName}&&${list.fixName}']); //得到数组的内容（实体bean)加入到新的数组里面
</c:forEach>

function myLink(value, meta, record) {
    var isCheck = record.data.IS_CHECK;
    var pickOrderId = record.data.PICK_ORDER_ID;
    var transPrintStr = "<a href=\"#\" onclick='printTransOrder(\"" + pickOrderId + "\",\"" + value + "\")'>[打印发运单]</a>";
    if (isCheck == 0) {
        return String.format("<a href=\"#\" onclick='confirmSave(\"" + value + "\")'>[保存]</a><a href=\"#\" onclick='detailOrder(\"" + value + "\")'>[查看]</a>" + transPrintStr);
    } else {
        return String.format("<a href=\"#\" onclick='confirmSave(\"" + value + "\")'>[保存]</a><a href=\"#\" onclick='detailOrder(\"" + value + "\")'>[查看]</a>" + transPrintStr);
    }
}
//设置承运物流下拉框
function returnPlanerSelect(value, meta, record) {
    var pickId = record.data.TRANS_ID;
    var str = "<select class=\"short_sel\" size = '1' name = 'transOrgSelect_" + pickId + "' id = 'transOrgSelect_" + pickId + "' onmouseover='addPlannerList(\"transOrgSelect_" + pickId + "\")' onclick='addPlannerList(\"transOrgSelect_" + pickId + "\")'><option value='" + value + "'>" + value + "</option>";
    str = str + "</select>";
    return String.format(str);
}

function addPlannerList(parms) {
    var obj = document.getElementById(parms);
    if (obj.options.children.length < 2) {
        var strTemp;
        var strsTemp = new Array();
        for (var i = 0; i < transCompanyArray.length; i++) {
            strTemp = transCompanyArray[i].toString();
            //定义一数组
            strsTemp = strTemp.split("&&"); //字符分割
            var uID = strsTemp[0];
            var uName = strsTemp[1];
            if (uID != obj.options.children[0].value) {
                obj.options.add(new Option(uName, uID));
            }
        }
    }

}

//设置运单类型下拉框
function returnBuyerSelect(value, meta, record) {
    var pickID = record.data.TRANS_ID;
    var str = "<select class=\"short_sel\" size = '1' name='transTypeSelect_" + pickID + "' id='transTypeSelect_" + pickID + "' onmouseover='addPurchaserList(\"transTypeSelect_" + pickID + "\")' onclick='addPurchaserList(\"transTypeSelect_" + pickID + "\")'><option value='" + value + "'>" + value + "</option>";
    str = str + "</select>";
    return String.format(str);
}

function addPurchaserList(parms) {
    var obj = document.getElementById(parms);
    if (obj.options.children.length < 2) {
        var strTemp;
        var strsTemp = new Array();
        for (var i = 0; i < transTypeArray.length; i++) {
            strTemp = transTypeArray[i].toString();
            //定义一数组
            strsTemp = strTemp.split("&&"); //字符分割
            var uID = strsTemp[0];
            var uName = strsTemp[1];
            if (uID != obj.options.children[0].value) {
                obj.options.add(new Option(uName, uID));
            }
        }
    }
}

//运单
function printTransOrder(pickOrderId, value) {
    window.open("<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPickOrder/printTransOrder.do?pickOrderId=" + pickOrderId+"&transId="+value, '', 'left=0,top=0,width=' + screen.availWidth + '- 10,height=' + screen.availHeight + '-50,toolbar=no,resizable=yes,menubar=no,scrollbars=yes,location=no');
    __extQuery__(1);
}
function getPkgNumText(value, meta, record) {
    var pickOrderId = record.data.TRANS_ID;
    return String.format("<input id='pkgNum_" + pickOrderId + "' name='pkgNum_" + pickOrderId + "' value='" + value + "' type='text'  style='width:30px;background-color:#FFFFCC;text-align:center' />");
}
function getWeightText(value, meta, record) {
    var pickOrderId = record.data.TRANS_ID;
    return String.format("<input id='weight_" + pickOrderId + "' name='weight_" + pickOrderId + "' value='" + value + "' type='text'  style='width:30px;background-color:#FFFFCC;text-align:center' />");
}
function getPkgByText(value, meta, record) {
    var pickOrderId = record.data.TRANS_ID;
    return String.format("<input id='pkgBy_" + pickOrderId + "' name='pkgBy_" + pickOrderId + "' value='" + value + "' type='text' style='background-color:#FFFFCC;text-align:center' />");
}
function getPickByText(value, meta, record) {
    var pickOrderId = record.data.TRANS_ID;
    return String.format("<input id='pickBy_" + pickOrderId + "' name='pickBy_" + pickOrderId + "' value='" + value + "' type='text' onclick='showMessage(\"pickBy_" + pickOrderId + "\")'  style='background-color:#FFFFCC;text-align:center' />");
}
function getLogisticsNoText(value, meta, record) {
    var pickOrderId = record.data.TRANS_ID;
    return String.format("<input id='logisticsNo_" + pickOrderId + "' name='logisticsNo_" + pickOrderId + "' value='" + value + "' type='text'  style='background-color:#FFFFCC;text-align:center' />");
}
function confirmSave(id) {
   /* var re = /^[0-9]+[0-9]*]*$/;
    if (!re.test($('pkgNum_' + id).value)) {
        MyAlert("请填入正确格式件数!");
        return;
    }*/
    MyConfirm("确定保存?", save, [id]);
}
function completChk(id) {
    MyConfirm("确定完成登记?", cmp, [id]);
}
function cmp(id) {
    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartTrans/cmpCheckIn.json?pickOrderId=" + id;
    makeNomalFormCall(url, getResult, 'fm');
}
function showMessage(pickById) {
    document.getElementById("pickById").value = pickById;
    var url = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartTrans/queryTransMessInit.do?pickById=' + pickById;
    OpenHtmlWindow(url, 700, 500);
}

function setMessage(messages) {
    var pickById = document.getElementById("pickById").value;
    var pickByIdObj = document.getElementById(pickById);
    var strTemp = "";
    for (var i = 0; i < messages.length; i++) {
        strTemp += messages[i].toString() + ", ";
    }
    pickByIdObj.value = strTemp;
}

function save(id) {
    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartTrans/modifyInfo.json?TRANS_ID=" + id;
    makeNomalFormCall(url, getResult, 'fm');
}
function getResult(jsonObj) {
    if (jsonObj != null) {
        var success = jsonObj.success;
        var error = jsonObj.error;
        var exceptions = jsonObj.Exception;
        if (success) {
        	MyAlert(success);
            __extQuery__(1);
        } else if (error) {
            MyAlert(error);
        } else if (exceptions) {
            MyAlert(exceptions.message);
        }
    }
}
function detailOrder(pickOrderId) {
    disableAllClEl();
    window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartTrans/detailOrder.do?pickOrderId=' + pickOrderId;
}
function printTreansOrder(value, event) {

}

function checkAll(obj) {
    var cb = document.getElementsByName("cb");
    for (var i = 0; i < cb.length; i++) {
        cb[i].checked = obj.checked;
    }
}
function getCb(value, meta, record) {
    return String.format("<input type='checkBox' id='cb' name='cb' value='" + record.data.TRANS_ID + "' >");
}

function genSelBoxExpMy(id, type, selectedKey, setAll, _class_, _script_, nullFlag, expStr) {
    var str = "";
    var arr;
    if (expStr.indexOf(",") > 0)
        arr = expStr.split(",");
    else {
        expStr = expStr + ",";
        arr = expStr.split(",");
    }
    str += "<select id='" + id + "' name='" + id + "' class='" + _class_ + "' " + _script_;
    // modified by lishuai@infoservice.com.cn 2010-05-18 解决select下拉框中增加属性datatype判断bug begin
    if (nullFlag && nullFlag == "true") {
        str += " datatype='0,0,0' ";
    }
    // end
    str += " onChange=doCusChange(this.value);> ";
    if (setAll) {
        str += genDefaultOpt();
    }
    for (var i = 0; i < codeData.length; i++) {
        var flag = true;
        for (var j = 0; j < arr.length; j++) {
            if (codeData[i].codeId == arr[j] && codeData[i].codeId !=<%=Constant.CAR_FACTORY_PKG_STATE_01%>) {
                flag = false;
            }
        }
        if (codeData[i].type == type && flag && codeData[i].codeId !=<%=Constant.CAR_FACTORY_PKG_STATE_01%>) {
            str += "<option " + (codeData[i].codeId == selectedKey ? "selected" : "") + " value='" + codeData[i].codeId + "' title = '" + codeData[i].codeDesc + "' >" + codeData[i].codeDesc + "</option>";
        }
    }
    str += "</select>";

    document.write(str);
}
function doQuery(parms) {
    var msg = "";
    //校验时间范围
    if (document.getElementById("SstartDate").value == "") {
        if (document.getElementById("SendDate").value == "") {
            msg += "发运日期不能为空!</br>"
        }
    }
    if (document.getElementById("SstartDate").value != "") {
        if (document.getElementById("SendDate").value == "") {
            msg += "请填写发运结束日期!</br>"
        }
    }
    if (document.getElementById("SendDate").value != "") {
        if (document.getElementById("SstartDate").value == "") {
            msg += "请填写发运开始日期!</br>"
        }
    }
    if (document.getElementById("SendDate").value != "") {
        if (document.getElementById("SstartDate").value != "") {
            if (document.getElementById("SendDate").value < document.getElementById("SstartDate").value) {
                msg += "发运开始日期不能大于发运结束时间!</br>"
            }
        }
    }
    if (msg != "") {
        //弹出提示
        MyAlert(msg);
        return;
    }

    var searchTypeObj = document.getElementById("searchType");
    var dtlObj = document.getElementById("detail_table");
    var norObj = document.getElementById("normal_table");
    if ("normal" == parms) {
        searchTypeObj.value = parms;
        dtlObj.style.cssText = "display: none;";
        norObj.style.cssText = "display: block;";
        columns = [
            {header: "序号", align: 'center', renderer: getIndex},
//            {id: 'action', header: "操作", sortable: false, dataIndex: 'TRANS_ID', renderer: myLink, align: 'center'},
            //{header: "发运方式", dataIndex: 'FIX_NAME', align: 'center'},
            {header: "发运单号", dataIndex: 'LOGISTICS_NO', align: 'center'/*, renderer: getLogisticsNoText*/},
            {header: "发运日期", dataIndex: 'TRANS_DATE', align: 'center'},
            {header: "拣货单号", dataIndex: 'PICK_ORDER_ID', align: 'center'},
            {header: "订货单位编码", dataIndex: 'DEALER_CODE', align: 'center'},
            {header: "订货单位", dataIndex: 'DEALER_NAME', style: 'text-align: left;'},
            {header: "承运物流", dataIndex: 'TRANS_ORG', style: 'text-align:center'/*, renderer: returnPlanerSelect*/},
            {header: "发运方式", dataIndex: 'ORDER_TRANS_TYPE', style: 'text-align:center'/*, renderer: returnBuyerSelect*/},
            {header: "出库数量", dataIndex: 'OUTSTOCK_QTY', align: 'center'},
            {header: "出库金额", dataIndex: 'SALE_AMOUNT', style: 'text-align: right;'},
            {header: "出库人", dataIndex: 'CREATE_BY_NAME', align: 'center'},
            {header: "箱数", dataIndex: 'PKG_NUM', align: 'center'/*, renderer: getPkgNumText*/},
            {header: "重量", dataIndex: 'WEIGHT', align: 'center'/*, renderer: getWeightText*/},
//            {header: "验货人", dataIndex: 'PKG_BY', align: 'center', renderer: getPkgByText},
//            {header: "装箱人", dataIndex: 'CHECK_PICK_BY', align: 'center', renderer: getPickByText},
            {header: "出库仓库", dataIndex: 'WH_NAME', align: 'center'}
//            {header: "备注", dataIndex: 'REMARK', align: 'center'},
//            {header: "运单打印次数", dataIndex: 'TRANS_PRINT_NUM', align: 'center'/*, renderer: getPrint2*/}

        ];
    }
    else {
        searchTypeObj.value = parms;
        norObj.style.cssText = "display: none;";
        dtlObj.style.cssText = "display: block;";
        columns = [
            {header: "序号", align: 'center', renderer: getIndex},
            {header: "拣货单号", dataIndex: 'PICK_ORDER_ID', align: 'center'},
            //{header: "发运方式", dataIndex: 'FIX_NAME', align: 'center'},
            {header: "货位", dataIndex: 'LOC_CODE', align: 'center'},
            {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align: left;'},
            {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align: left;'},
            {header: "配件件号", dataIndex: 'PART_CODE', style: 'text-align: left;'},
            {header: "出库数量", dataIndex: 'OUTSTOCK_QTY', align: 'center'},
            {header: "单价", dataIndex: 'SALE_PRICE', style: 'text-align: right;'},
            {header: "出库金额", dataIndex: 'SALE_AMOUNT', style: 'text-align: right;'},
            {header: "出库仓库", dataIndex: 'WH_NAME', align: 'center'},
            {header: "装箱人", dataIndex: 'PKG_BY', align: 'center'},
            {header: "捡货人", dataIndex: 'CHECK_PICK_BY', align: 'center'}
        ];
    }
    __extQuery__(1);
}

function getPrint2(num) {
    if (num > 0) {
        return "是";
    }
    return "否";
}

function exportExcel() {
    document.fm.action = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartTrans/exportExcel.do";
    document.fm.submit();
}

function expTransDtlExcel() {
    document.fm.action = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartTrans/expTransDtlExcel.do";
    document.fm.submit();
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

function enableCb() {
    var cb = document.getElementsByName("cb");
    for (var i = 0; i < cb.length; i++) {
        cb[i].disabled = false;
    }
}
$(document).ready(function(){
	doQuery('normal');
	enableAllClEl();
});
</script>
</head>
<body>
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <input type="hidden" id="pickById" name="pickById" value="">
    <input type="hidden" id="searchType" name="searchType" value="">

    <div class="wbox">
        <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" alt=""/>&nbsp;当前位置：配件管理 &gt; 配件销售管理 &gt;配件发运单
        </div>
        <div class="form-panel">
        	<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
			<div class="form-body">
	        <table border="0" class="table_query" id="normal_table">
	            <tr>
	                <td width="10%" class="right">拣货单号：
	                </td>
	                <td width="25%"><input class="middle_txt" type="text" id="pickOrderId" name="pickOrderId"/></td>
	                <td width="10%" class="right">订货单位：
	                </td>
	                <td width="20%"><input type="text" class="middle_txt" id="dealerName" name="dealerName"></td>
	                <td width="10%" class="right">订货单位编码：</td>
	                <td width="20%"><input class="middle_txt" type="text" id="dealerCode" name="dealerCode"/></td>
	            </tr>
	            <tr>
	                <td width="10%" class="right">发运日期：</td>
	                <td width="25%">
	                	<input name="SstartDate" type="text" class="short_txt" id="SstartDate" style="width:80px;"
	                                       value="${old}"/>
	                    <input name="button23" value=" " type="button" class="time_ico"/>
	                    至
	                    <input name="SendDate" type="text" class="short_txt" id="SendDate" value="${now}" style="width:80px;"/>
	                    <input name="button222" value=" " type="button" class="time_ico"/></td>
	                <td width="10%" class="right">发运单号：</td>
	                <td width="20%"><input class="middle_txt" type="text" id="TransCode" name="TransCode"/></td>
	                <td width="10%" class="right">出库仓库：
	                </td>
	                <td width="20%">
	                    <select name="whId" id="whId" class="u-select">
	                        <option selected value=''>-请选择-</option>
	                        <c:forEach items="${wareHouseList}" var="wareHouse">
	                            <option value="${wareHouse.WH_ID}">${wareHouse.WH_NAME}</option>
	                        </c:forEach>
	                    </select>
	                </td>
	            </tr>
	            <tr>
	                <td width="10%" class="right">已登记发运：
	                </td>
	                <td width="20%">
	                    <script type="text/javascript">
	                        genSelBoxExp("isChkIn", <%=Constant.PART_BASE_FLAG%>, "", true, "u-select", "", "false", '');
	                    </script>
	                </td>
	                <td width="10%" class="right">发运单打印：</td>
	                <td width="20%">
	                    <script type="text/javascript">
	                        genSelBoxExp("TransFlag", <%=Constant.PART_BASE_FLAG%>, "", true, "u-select", "", "false", '');
	                    </script>
	                </td>
	                <td class="right"><span class="right">运单类型</span>：</td>
	                <td width="20%">
	                    <select name="TRANS_TYPE" id="TRANS_TYPE" class="u-select">
	                        <option value="">-请选择-</option>
	                        <c:if test="${transList!=null}">
	                            <c:forEach items="${transList}" var="list">
	                                <option value="${list.fixName }">${list.fixName }</option>
	                            </c:forEach>
	                        </c:if>
	                    </select>
	                </td>
	                </td>
	            </tr>
	            <tr>
	                <td colspan="6" class="center">
	                    <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查 询"
	                           onclick="doQuery('normal');"/>
	                    <input class="normal_btn" type="button" value="导 出" onclick="exportExcel()"/>
	                    <%--<input class="normal_btn" type="button" value="明细查询" name="queryDtlBtn" id="queryDtlBtn"
	                           onclick="doQuery('detail')"/>--%>
	                    <%--<input class="normal_btn" type="button" value="明细导出" id="expGQuery" onclick="expTransDtlExcel()"/>--%>
	                </td>
	            </tr>
	        </table>
	    </div>    
	    </div>    
        <table border="0" class="table_query" id="detail_table" style="display: none;">
            <th colspan="6" width="100%"><img class="nav" src="<%=contextPath%>/img/subNav.gif"/> 查询条件</th>
            <tr>
                <td width="10%" class="right">拣货单号：
                </td>
                <td width="20%"><input class="middle_txt" type="text" id="pickOrderId2" name="pickOrderId2"/></td>
                <td width="10%" class="right">配件编码：
                </td>
                <td width="20%"><input type="text" class="middle_txt" id="partOldcode" name="partOldcode"></td>
                <td width="10%" class="right">配件名称：</td>
                <td width="20%"><input class="middle_txt" type="text" id="partName" name="partName"/></td>
            </tr>
            <tr>
                <td width="10%" class="right">发运日期：</td>
                <td width="25%">
                	<input name="SstartDate2" type="text" class="short_txt" id="SstartDate2"   style="width:80px;" 
                                       value="${old}"/>
                    <input name="button23" value=" " type="button" class="time_ico" />
                    至
                    <input name="SendDate2" type="text" class="short_txt" id="SendDate2" value="${now}" style="width:80px;" />
                    <input name="button222" value=" " type="button" class="time_ico" /></td>
                <td width="10%" class="right">出库仓库：
                </td>
                <td width="20%">
                    <select name="whId2" id="whId2" class="u-select">
                        <option selected value=''>-请选择-</option>
                        <c:forEach items="${wareHouseList}" var="wareHouse">
                            <option value="${wareHouse.WH_ID}">${wareHouse.WH_NAME}</option>
                        </c:forEach>
                    </select>
                </td>
                <td width="10%" class="right">是否强制出库：
                </td>
                <td width="24%">
                    <select name="isForce" id="isForce" class="u-select">
                        <option selected value=''>-请选择-</option>
                        <option value="1">是</option>
                    </select>
                </td>
                <!-- 
                <td class="right"><span class="right">发运方式</span>：</td>
                <td width="24%">
                    <select name="TRANS_TYPE" id="TRANS_TYPE" class="short_sel">
                        <option value="">-请选择-</option>
                        <c:if test="${transList!=null}">
                            <c:forEach items="${transList}" var="list">
                                <option value="${list.fixName }">${list.fixName }</option>
                            </c:forEach>
                        </c:if>
                    </select>
                </td>
                 -->
            </tr>
            <tr>
                <td colspan="6" class="center">
                    <%--<input class="normal_btn" type="button" value="导 出" onclick="exportExcel()"/>--%>
                    <input class="normal_btn" type="button" value="明细查询" name="queryDtlBtn" id="queryDtlBtn"
                           onclick="doQuery('detail')"/>
                    <input class="normal_btn" type="button" value="明细导出" id="expGQuery" onclick="expTransDtlExcel()"/>
                    <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="返回"
                           onclick="doQuery('normal');"/>
                </td>
            </tr>
        </table>
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </div>
</form>
</body>
</html>