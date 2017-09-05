<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%String contextPath = request.getContextPath();%>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <script type="text/javascript" src="<%=contextPath%>/js/web/jquery-1.8.0.min.js"></script>
    <title>接收入库</title>
    <script type="text/javascript">
        var wareHouse = new Array(); //创建一个新的数组
        <c:forEach var="list" items="${wareHouseList}" varStatus="sta">
        wareHouse.push(["${list.WH_ID}&&${list.WH_NAME}"]);//得到数组的内容（实体bean)加入到新的数组里面
        </c:forEach>
        var partId = "";
    </script>
</head>

<body onload="loadcalendar();__extQuery__(1);">
<div class="navigation">
    <img src="<%=contextPath %>/img/nav.gif"/>&nbsp;当前位置：配件管理>配件仓储管理>配件接收入库>配件接收入库
</div>
<form name="fm" method="post" id="fm">
    <!-- 查询条件 begin -->
    <table class="table_query" id="subtab">
        <tr class="csstr" align="center">
            <td align="right">配件编码：</td>
            <td align="left">
                <input type="text" id="PART_CODE" name="PART_CODE" class="middle_txt" value="" size="15"/>
            </td>
            <td align="right">配件名称：</td>
            <td align="left">
                <input type="text" id="PART_NAME" name="PART_NAME" class="middle_txt" value="" size="15"/>
            </td>
            <td align="right">入库日期：</td>
            <td align="left">
                <input name="TstartDate" type="text" class="short_time_txt" id="TstartDate" value=""/>
                <input name="button2" type="button" class="time_ico"
                       onclick="showcalendar(event, 'TstartDate', false);" value=" "/>
                至
                <input name="TendDate" type="text" class="short_time_txt" id="TendDate" value="${now}"/>
                <input name="button2" type="button" class="time_ico"
                       onclick="showcalendar(event, 'TendDate', false);" value=" "/>
            </td>
        </tr>
        <tr class="csstr" align="center">
            <td colspan="6" align="center">
                <input type="button" id="queryBtn" class="cssbutton" value="查询" onclick="__extQuery__(1);"/>
                <input type="reset" class="cssbutton" id="resetButton" value="重置"/>
                <input type="button" class="normal_btn" id="saveButton" style="width: 90px;" onclick="storageRecord()" value="入库记录查询"/>
                <input type="button" class="normal_btn" id="saveButton" onclick="batchEditSave()" value="批量分配"/>
                待入库总数为:<span style="color: red;font-weight: bold;"> ${map.WAIT_STORAGE_NUM_TOTAL }</span>
            </td>
        </tr>
    </table>
    <!-- 查询条件 end -->
    <!--分页 begin -->
    <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
    <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    <!--分页 end -->
</form>
</body>
<script type="text/javascript">
var myPage;
//查询路径
var url = "<%=contextPath%>/parts/storageManager/partDistributeMgr/PartDistributeMgr/query.json";
var title = null;
var columns = [
    {header: "序号", align: 'center', renderer: getIndex},
    {header: "<input name='cbAll' id='cbAll' onclick='selectAll(this,\"cb\")' type='checkbox' />", dataIndex: 'PART_CODE', align: 'center', renderer: checkLink},
    {header: "操作", sortable: false, dataIndex: 'DISTRIBUTE_ID', align: 'center', renderer: myLink},
    {header: "配件编码", dataIndex: 'PART_CODE', style: 'text-align: left;'},
    {header: "配件名称", dataIndex: 'PART_NAME', style: 'text-align: left;'},
    {header: "库房", dataIndex: 'WH_NAME', style: 'text-align:center;'},
    {header: "U9入库数量", dataIndex: 'ERP_STORAGE_NUM', style: 'text-align:center;'},
    {header: "已入库数量", dataIndex: 'FINISH_STORAGE_NUM', style: 'text-align:center;'},
    {header: "待入库数量", dataIndex: 'WAIT_STORAGE_NUM', style: 'text-align:center;'},
    {header: "货位入库数量", dataIndex: 'WAIT_STORAGE_NUM', style: 'text-align:center;', renderer: insertInputSalePrice},
    {header: "入库仓库", style: 'text-align:center;', renderer: insertInputSelect},
    {header: "入库货位", style: 'text-align:center;', renderer: insertInputlocCode},
    {header: "U9入库日期", dataIndex: 'STORAGE_DATE', align: 'center'}
];
function myLink(value, meta, record) {
    return String.format("<a href=\"#\" onclick='sel(\"" + value + "\")'>[分配货位]</a>");
}
function sel(value) {
    window.location.href = '<%=contextPath%>/parts/storageManager/partDistributeMgr/PartDistributeMgr/distributeInit.do?Id=' + value;
}
//插入文本框SalePrice
function insertInputSalePrice(value, meta, record) {
    var pId = record.data.PART_CODE;
    var output = "<input name='PART_NUM' type='text' class='short_time_txt' id='PART_NUM_" + pId + "' value='" + value + "' style='background-color:#FF9'>" +
            "<input type='hidden' id='wait_num_" + pId + "' name='WAIT_STORAGE_NUM' value='" + record.data.WAIT_STORAGE_NUM + "'/>" +
            "<input type='hidden' id='DISTRIBUTE_ID_" + pId + "' value='" + record.data.DISTRIBUTE_ID + "'/>" +
            "<input type='hidden' id='PART_ID_" + pId + "' value='" + record.data.PART_ID + "'/>" +
            "<input type='hidden' id='PART_NAME_" + pId + "' value='" + record.data.PART_NAME + "'/>" +
            "<input type='hidden' id='ERP_STORAGE_NUM_" + pId + "' value='" + record.data.ERP_STORAGE_NUM + "'/>" +
            "<input type='hidden' id='FINISH_STORAGE_NUM_" + pId + "' value='" + record.data.FINISH_STORAGE_NUM + "'/>";

    return output;
}

//插入下拉框
function insertInputSelect(value, meta, record) {
    var pId = record.data.PART_CODE;
    var whId = record.data.WH_ID;
    var output = "<select class=\"short_sel\" size = '1' id = 'sel_" + pId + "' onmouseover='addWareHouseList(\"sel_" + pId + "\")' onclick='addWareHouseList(\"sel_" + pId + "\")'>";
    output = output + "<option value='" + whId + "'>" + record.data.WH_NAME + "</option>";
    output = output + "</select>";
    return String.format(output);
}

function addWareHouseList(parms) {
    var obj = document.getElementById(parms);
    if (obj.options.children.length < 3) {
        var strTemp;
        for (var i = 0; i < wareHouse.length; i++) {
            var strsTemp = new Array();
            strTemp = wareHouse[i].toString();
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
jQuery.noConflict();
//插入入库货位选择框
function insertInputlocCode(value, meta, record) {
    var pId = record.data.PART_CODE;
    var id = record.data.PART_ID;
    var output = "<input name ='LOC_CODE_T' id='LOC_CODE_T_" + pId + "' class='middle_txt' type='text'  onchange='checkCode(this,\"" + id + "\",\"" + pId + "\");'>" +
            "<input name='LOC_CODE' id='LOC_CODE_" + pId + "' type='hidden' value=''>&nbsp;&nbsp;" +
            "<input class='mini_btn' type='button' value='...' onclick='codeChoice(\"" + pId + "\");'/>";
    return output;
}

function codeChoice(pId) {
    var id = "sel_" + pId;
    partId = pId;
    var whId = document.getElementById(id).value;
    OpenHtmlWindow(g_webAppName + "/parts/storageManager/partDistributeMgr/PartDistributeMgr/selectLocationInit.do?whId=" + whId + "&pId=" + pId, 700, 400);
}

//多选框
function checkLink(value, meta, record) {
    var output = '<input name="cb" type="checkbox" value="' + value + '" />';

    return String.format(output);
}
//分配
function batchEditSave() {
    var pIds = new Array();
    var index = new Array();
    var whIds = new Array();
    var flag = false;
    var cb = document.getElementsByName('cb');

    for (var i = 0; i < cb.length; i++) {
        if (cb[i].checked) {
            var locCode = document.getElementById("LOC_CODE_T_" + cb[i].value).value;
            var whId = document.getElementById("sel_" + cb[i].value).value;
            var partNum = document.getElementById("PART_NUM_" + cb[i].value).value;//货位入库数量
            var waitNum = document.getElementById("wait_num_" + cb[i].value).value;//待入库数量
            if (Number(partNum) > Number(waitNum)) {
                parent.MyAlert("第" + (i + 1) + "行的入库数量不能大于待入库数量!");
                return;
            }
            if (whId == "") {
                parent.MyAlert("第" + (i + 1) + "行的入库仓库不能为空！");
                return;
            }
            if (locCode == "") {
                MyAlert("第" + (i + 1) + "行的货位不能为空!");
                return;
            }

            flag = true;
        }
    }
    if (!flag) {
        MyAlert("请选择需要批量分配的配件！");
        return;
    }
    MyConfirm("确认分配！", save);
}

function save() {
    btnDisable();
    var ar = getCbArr();
    makeNomalFormCall("<%=contextPath%>/parts/storageManager/partDistributeMgr/PartDistributeMgr/editAllSave.json?cbAr=" + ar, saveBack, 'fm', 'saveButton');
}

function saveBack(json) {
	btnEnable();
    if (json.returnValue == 1) {
        parent.MyAlert("操作成功！");
        window.location.href = "<%=contextPath%>/parts/storageManager/partDistributeMgr/PartDistributeMgr/init.do";
    } else if (json.returnValue == 2) {
        MyAlert("货位分配数量出现异常，请尝试刷新该页面后重新分配！");
    } else {
        MyAlert(112);
        MyAlert(json.error);
    }
}

function codeSet(i, c, n) {
    var v = i + "@" + c + "@" + n;
    document.getElementById("LOC_CODE_T_" + partId).value = c;
    document.getElementById("LOC_CODE_" + partId).value = v;

}

function getCbArr() {
    var cbArr = [];
    var cb = document.getElementsByName("cb");
    for (var i = 0; i < cb.length; i++) {
        if (cb[i].checked) {
            var partNum = document.getElementById("PART_NUM_" + cb[i].value).value;//货位入库数量
            var waitNum = document.getElementById("wait_num_" + cb[i].value).value;
            var disNum = document.getElementById("DISTRIBUTE_ID_" + cb[i].value).value;
            var PART_ID = document.getElementById("PART_ID_" + cb[i].value).value;
            //var PART_NAME_ = document.getElementById("PART_NAME_" + cb[i].value).value;
            var ERP_STORAGE_NUM = document.getElementById("ERP_STORAGE_NUM_" + cb[i].value).value;
            var FINISH_STORAGE_NUM = document.getElementById("FINISH_STORAGE_NUM_" + cb[i].value).value;
            var sel = document.getElementById("sel_" + cb[i].value).value;
            //var LOC_CODE_T_ = document.getElementById("LOC_CODE_T_" + cb[i].value).value;
            var LOC_CODE = document.getElementById("LOC_CODE_" + cb[i].value).value;

            cbArr.push(cb[i].value + "@" + partNum + "@" + waitNum + "@" + disNum + "@" + PART_ID + "@" + ERP_STORAGE_NUM + "@" + FINISH_STORAGE_NUM +
                    "@" + sel + "@" + LOC_CODE);
        }
    }
    return cbArr;
}

function checkCode(th, partId, partCode) {
    var loc_code = th.value;
    var whId = jQuery("#sel_" + partCode).val();
    var whName = jQuery("#sel_" + partCode).find("option:selected").text();
    var url2 = "<%=contextPath%>/parts/storageManager/partDistributeMgr/PartDistributeMgr/checkSeatExist.json";
    var para = "LOC_CODE=" + loc_code + "&PART_ID=" + partId + "&whId=" + whId + "&whName=" + whName + "&partCode=" + partCode;
    makeCall(url2, forBack3, para);
}
function forBack3(json) {
    if (json.returnValue != 1) {
        var partCode = json.partCode;
        if (partCode != "") {
            document.getElementById("LOC_CODE_T_" + partCode).value = "";
        }
        parent.MyAlert("货位【" + json.LOC_CODE + "】在仓库【" + json.whName + "】中不存在,请先维护再操作！ ");
    } else {
        partId = json.partCode;
        codeSet(json.LOC_ID, json.LOC_CODE, json.LOC_CODE);
    }
}
//弹出层
function storageRecord() {
    var buttonFalg = "disabled";
    OpenHtmlWindow('<%=contextPath%>/parts/storageManager/partDistributeMgr/PartDistributeMgr/queryStorageRecordInit.do?flag=1&buttonFalg=' + buttonFalg, 900, 500);
}
</script>
</html>