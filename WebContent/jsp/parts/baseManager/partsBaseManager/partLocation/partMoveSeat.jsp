<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
    String contextPath = request.getContextPath();
%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=7">
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <script type="text/javascript" src="<%=contextPath%>/js/web/jquery-1.8.0.min.js"></script>
    <title>配件移位</title>
    <script type="text/javascript">
        var wareHouse = new Array(); //创建一个新的数组
        <c:forEach var="list" items="${list}" varStatus="sta">
        wareHouse.push(["${list.WH_ID}&&${list.WH_NAME}"]);//得到数组的内容（实体bean)加入到新的数组里面
        </c:forEach>
    </script>
</head>
<body onunload='javascript:destoryPrototype()' onload="__extQuery__(1);loadcalendar();">
<div class="wbox">
    <div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif"/>&nbsp;当前位置：配件管理 > 基础信息管理 > 仓储相关信息维护 >
        配件移位
    </div>
    <form method="post" name="fm" id="fm" method="post" enctype="multipart/form-data">
        <input type="hidden" name="FLAG" id="FLAG" value="${FLAG}"/>
        <table class="table_query">
            <th colspan="6"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif"/> 查询条件</th>
            <tr>
                <td align="right">库房：</td>
                <td align="left">
                    <select name="WH_ID" id="WH_ID" class="short_sel" onchange="changeSub();">
                        <c:forEach items="${list}" var="wareHouse">
                            <option value="${wareHouse.WH_ID}">${wareHouse.WH_NAME}</option>
                        </c:forEach>
                    </select>
                </td>
                <td align="right">配件编码：</td>
                <td><input class="middle_txt" type="text" name="PART_CODE"/></td>
                <td align="right">配件名称：</td>
                <td><input class="middle_txt" type="text" name="PART_NAME"/></td>
                <%--<td align="right">货位编码：</td>
                <td><input name="LOC_CODE" id="LOC_CODE" class="middle_txt" type="text" value="">
                    <input class='mini_btn' type='button' value='...' onclick="codeChoice('query');"/>
                </td>--%>
            </tr>
            <tr>
                <td align="center" colspan="6">
                    <input class="normal_btn" type="button" value="查询" name="BtnQuery" id="queryBtn"
                           onClick="__extQuery__(1);"/>
                    <input class="normal_btn" type="button" value="查询历史" name="BtnQueryh" id="BtnQueryh"
                           onClick="viewRecord();"/>
                </td>
            </tr>
        </table>
        <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
        <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    </form>
</div>
</body>
<script type="text/javascript">
    var myPage;
    var url = "<%=contextPath%>/parts/baseManager/partsBaseManager/PartMoveSeat/query.json";
    var title = null;
    var columns = [
        {header: "序号", style: 'text-align:center', renderer: getIndex},
        {header: "仓库", dataIndex: 'WH_NAME', style: 'text-align:left'},
        {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
        {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
        {header: "库存数量", dataIndex: 'ITEM_QTY', style: 'text-align:left', renderer: numItemQty},
        {header: "可用数量", dataIndex: 'NORMAL_QTY', style: 'text-align:left'},
//					{header: "修改日期", dataIndex: 'CREATE_DATE', style: 'text-align:center'},
        {header: "货位编码", dataIndex: 'LOC_CODE', style: 'text-align:left'},
        {header: "移入仓库", style: 'text-align:center;', renderer: insertInputSelect},
        {header: "移入货位", dataIndex: 'LOC_CODE', style: 'text-align:left', renderer: choice},
        {header: "移位数量", dataIndex: 'ITEM_QTY', style: 'text-align:left', renderer: moveNum},
        {header: "操作", dataIndex: 'PART_ID', style: 'text-align:left', renderer: myLink}
    ];
    jQuery.noConflict();
    function numItemQty(value, meta, record) {
        var id = record.data.PART_ID + "," + record.data.LOC_ID;
        var text = "<input type='hidden' value='" + value + "' name='qty_" + id + "' id='qty_" + id + "' />";
        return String.format(text + value);
    }
    function moveNum(value, meta, record) {
        var id = record.data.PART_ID + "," + record.data.LOC_ID;
        var text = "<input class='short_txt' type='text' name='num_" + id + "' id='num_" + id + "'/>";
        return String.format(text);
    }
    function choice(value, meta, record) {
        var id = record.data.PART_ID + "," + record.data.LOC_ID;
        var pId = record.data.PART_ID + "" + record.data.LOC_ID;
        var text = "<input name='LOC_CODE_" + id + "' id='LOC_CODE_" + id + "' class='short_txt' type='text' value='' onchange='checkCode(this,\"" + id + "\",\"" + pId + "\");'/>";
        text = text + "<input name='LOC_ID_" + id + "' id='LOC_ID_" + id + "' type='hidden' value='123' />";
        text = text + "<input class='mini_btn' type='button' value='...' onclick='codeChoice(\"" + id + "\",\"" + pId + "\");'/>";
        return String.format(text);
    }
    //设置超链接
    function myLink(value, meta, record) {
        var id = record.data.PART_ID + "," + record.data.LOC_ID;
        var whId = record.data.WH_ID;
        var text = "<input type='hidden' value='" + id + "' />";
        text = text + "<input name='WH_ID_" + id + "' id='WH_ID_" + id + "' type='hidden' value='" + whId + "'>";
        var yiwei = "<input class='normal_btn' type='button' value='[移位]' onClick='moveSeat(\"" + id + "\");'/>";
        return String.format(text + yiwei);
    }

    function codeChoice(id, pId) {
        var whId = jQuery("#sel_" + pId).val();
        OpenHtmlWindow(g_webAppName + "/parts/storageManager/partDistributeMgr/PartDistributeMgr/selectLocationInit.do?loc_id=" + id + "&whId=" + whId, 700, 400);
    }
    var LOC_ID = null;
    function codeSet(i, c, n) {
        var v = i + "," + c + "," + n;
        if ("query" == LOC_ID) {
            jQuery("#LOC_CODE").val(c);
        } else {
            document.getElementById("LOC_CODE_" + LOC_ID).value = c;
            document.getElementById("LOC_ID_" + LOC_ID).value = v;
        }
    }
    function checkCode(th, partId, partCode) {
        var loc_code = th.value;
        var whId = jQuery("#sel_" + partCode).val();
        var whName = jQuery("#sel_" + partCode).find("option:selected").text();
        var url2 = "<%=contextPath%>/parts/storageManager/partDistributeMgr/PartDistributeMgr/checkSeatExist.json";
        var para = "LOC_CODE=" + loc_code + "&PART_ID=" + partId + "&whId=" + whId + "&whName=" + whName + "&partCode=" + partCode;
        makeCall(url2, forBack2, para);
    }
    function forBack2(json) {
        if (json.returnValue != 1) {
            var partId = json.PART_ID;
            if (partId != "") {
                document.getElementById("LOC_CODE_" + partId).value = "";
            }
            parent.MyAlert("货位【"+json.LOC_CODE+"】在仓库【" + json.whName + "】中不存在,请先维护在操作！");
        } else {
            codeSet(json.LOC_ID, json.LOC_CODE, json.LOC_CODE);
        }
    }
    function moveSeat(id) {
        if (validate(id)) {
            var partId = id.split(",")[0];
            var locId = id.split(",")[1];
            var mt = document.getElementById("myTable");
            for (var i = 1; i < mt.rows.length; i++) {
                var pl = mt.rows[i].cells[10].firstChild.value;
                if (pl == id) {
                    var partOldCode = mt.rows[i].cells[2].innerText;  //配件编码
                    var partCname = mt.rows[i].cells[3].innerText;  //配件名称
                    var itemQty = mt.rows[i].cells[4].innerText;  //配件库存
                    var locCode = mt.rows[i].cells[6].innerText;  //货位编码
                    var number = document.getElementById("num_" + id).value;  //移位数量
                    var loc = document.getElementById("LOC_CODE_" + id).value;  //移入的货位信息
                    var whId = document.getElementById("WH_ID_" + id).value;  //移出的仓库信息
                    var yRwhId = document.getElementById("sel_" + partId+locId).value;  //移入的仓库信息
                    if (number <= 0) {
                        MyAlert("移位数量必须大于0!");
                        return;
                    }
                    if (confirm("确定移位?")) {
                        btnDisable();
                        var url2 = g_webAppName + "/parts/baseManager/partsBaseManager/PartMoveSeat/moveSeat.json";
                        var paramas = "PART_ID=" + partId + "&LOC_ID=" + locId + "&PARTOLDCODE=" + partOldCode + "&PART_CNAME=" + partCname + "&MOVE_NUM=" + number + "&LOC_CODE=" + loc + "&WH_ID=" + whId + "&LOC_CODE2=" + locCode+"&yRwhId="+yRwhId;
                        makeCall(url2, moveCallBack, paramas);
                    }
                }
            }
        }
    }
    function partMoveSeat() {

    }
    function moveCallBack(jsonObj) {
        if (jsonObj != null) {
            var success = jsonObj.success;
            var error = jsonObj.error;
            var exceptions = jsonObj.Exception;
            btnEable();
            if (null != error && error.length > 0) {
                MyAlert(error);
            } else if (null != success && success.length > 0) {
                MyAlert(success);
                __extQuery__(1);
            } else {
                MyAlert(exceptions.message);
            }
        }

    }
    function validate(id) {
        var loc = document.getElementById("LOC_CODE_" + id).value;
        var num = document.getElementById("num_" + id).value;
        var qty = document.getElementById("qty_" + id).value;
        if (loc == "") {
            MyAlert("请先选择移入货位！");
            return false;
        }
        if (Number(qty) < Number(num)) {
            MyAlert("移位数量不能大于库存数量！");
            return false;
        }
        if (num == "") {
            MyAlert("请输入移位数量！");
            return false;
        }
        return true;
    }
    function viewRecord() {
        OpenHtmlWindow("<%=contextPath%>/parts/baseManager/partsBaseManager/PartMoveSeat/init.do?FLAG=" + 2, 900, 500);
    }
    //插入下拉框
    function insertInputSelect(value, meta, record) {
        var pId = record.data.PART_ID + "" + record.data.LOC_ID;
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
</script>
</html>