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
</head>
<body onunload='javascript:destoryPrototype()' onload="__extQuery__(1);loadcalendar();">
<div class="wbox">
    <div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif"/>&nbsp;当前位置：配件管理 &gt; 基础信息管理 &gt;
        仓储相关信息维护&gt; 配件移位&gt;移位历史查询
    </div>
    <form method="post" name="fm" id="fm" method="post" enctype="multipart/form-data">
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
                <td align="right">操作日期：</td>
                <td>
                    <input name="TstartDate" type="text" class="short_time_txt" id="TstartDate" value="${old}"
                           style="width:65px"/>
                    <input name="button2" type="button" value=" " class="time_ico"
                           onclick="showcalendar(event, 'TstartDate', false);"/>
                    至
                    <input name="TendDate" type="text" class="short_time_txt" id="TendDate" value="${now}"
                           style="width:65px"/>
                    <input name="button2" type="button" value=" " class="time_ico"
                           onclick="showcalendar(event, 'TendDate', false);"/>
            </tr>
            <tr>
                <td align="center" colspan="6">
                    <input class="normal_btn" type="button" value="查询" name="BtnQuery" id="queryBtn"
                           onClick="__extQuery__(1);"/>
                    <%--<input class="normal_btn" type="button" value="导出" name="BtnQueryh" id="BtnQueryh"
                           onClick="__extQuery__(1);"/>--%>
                    <input class="normal_btn" type="button" value="关闭" name="BtnClose" id="BtnClose" onClick="_hide();"/>
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
        {header: "操作",  align:'center',sortable: false,dataIndex: 'WH_ID',renderer:miscLink},
        {header: "仓库", dataIndex: 'WH_NAME', style: 'text-align:center'},
        {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:center'},
        {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
//        {header: "库存数量", dataIndex: 'ITEM_QTY', style: 'text-align:left', renderer: numItemQty},
//        {header: "可用数量", dataIndex: 'NORMAL_QTY', style: 'text-align:left'},
        {header: "移出货位", dataIndex: 'LOC_CODE', style: 'text-align:left'},
        {header: "移出数量", dataIndex: 'PART_NUM', style: 'text-align:center'},
        {header: "移入仓库", dataIndex: 'WH_NAME2', style: 'text-align:center'},
        {header: "移入货位", dataIndex: 'TOLOC_CODE', style: 'text-align:left'},
        {header: "移入数量", dataIndex: 'TOPART_NUM', style: 'text-align:center'},
        {header: "操作日期", dataIndex: 'CREATE_DATE', style: 'text-align:center'}
//        {header: "操作", dataIndex: 'PART_ID', style: 'text-align:left', renderer: myLink}
    ];
    function numItemQty(value, meta, record) {
        var id = record.data.PART_ID + "," + record.data.LOC_ID;
        var text = "<input type='hidden' value='" + value + "' name='qty_" + id + "' id='qty_" + id + "' />";
        return String.format(text + value);
    }
    function moveNum(value, meta, record) {
        var id = record.data.PART_ID + "," + record.data.LOC_ID;
        var text = "<input class='middle_txt' type='text' name='num_" + id + "' id='num_" + id + "' />";
        return String.format(text);
    }
    function choice(value, meta, record) {
        var id = record.data.PART_ID + "," + record.data.LOC_ID;
        var whId = record.data.WH_ID;
        var whName = record.data.WH_NAME;
        var text = "<input name='LOC_CODE_" + id + "' id='LOC_CODE_" + id + "' class='middle_txt' type='text' value='' onchange='checkCode(this,\"" + id + "\",\"" + whId + "\",\"" + whName + "\");'/>";
        text = text + "<input name='LOC_ID_" + id + "' id='LOC_ID_" + id + "' type='hidden' value='123' />";
        text = text + "<input class='mini_btn' type='button' value='...' onclick='codeChoice(\"" + id + "\",\"" + whId + "\");'/>";
        return String.format(text);
    }
    //设置超链接
    function myLink(value, meta, record) {
        var id = record.data.PART_ID + "," + record.data.LOC_ID;
        var whId = record.data.WH_ID;
        var text = "<input type='hidden' value='" + id + "' />";
        text = text + "<input name='WH_ID_" + id + "' id='WH_ID_" + id + "' type='hidden' value='" + whId + "'>";
        var yiwei = "<input class='normal_btn' type='button' value='[保存]' onClick='moveSeat(\"" + id + "\");'/>";
        return String.format(text + yiwei);
    }
    jQuery.noConflict();
    function codeChoice(id, whId) {
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
    function checkCode(th, partId, whId, whName) {
        var loc_code = th.value;
        var url2 = "<%=contextPath%>/parts/storageManager/partDistributeMgr/PartDistributeMgr/checkSeatExist.json";
        var para = "LOC_CODE=" + loc_code + "&PART_ID=" + partId + "&whId=" + whId + "&whName=" + whName;
        makeCall(url2, forBack2, para);
    }
    function forBack2(json) {
        if (json.returnValue != 1) {
            var partId = json.PART_ID;
            if (partId != "") {
                document.getElementById("LOC_CODE_" + partId).value = "";
            }
            parent.MyAlert("该货位编码在仓库【" + json.whName + "】中不存在,请先维护在操作！");
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
                var pl = mt.rows[i].cells[9].firstChild.value;
                if (pl == id) {
                    var partCode = mt.rows[i].cells[2].innerText;  //件号
                    var partCname = mt.rows[i].cells[3].innerText;  //配件名称
                    var itemQty = mt.rows[i].cells[4].innerText;  //配件库存
                    var locCode = mt.rows[i].cells[6].innerText;  //货位编码
                    var number = document.getElementById("num_" + id).value;  //移位数量
                    var loc = document.getElementById("LOC_CODE_" + id).value;  //货位信息
                    var whId = document.getElementById("WH_ID_" + id).value;  //货位信息

                    var url2 = g_webAppName + "/parts/baseManager/partsBaseManager/PartMoveSeat/moveSeat.json";
                    var paramas = "PART_ID=" + partId + "&LOC_ID=" + locId + "&PART_CODE=" + partCode + "&PART_NAME=" + partCname + "&MOVE_NUM=" + number + "&LOC_CODE=" + loc + "&WH_ID=" + whId;
                    makeCall(url2, moveCallBack, paramas);
                }
            }
        }
    }
    function moveCallBack(jsonObj) {
        if (jsonObj != null) {
            var success = jsonObj.success;
            var error = jsonObj.error;
            var exceptions = jsonObj.Exception;
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

    //打印
    function miscLink(value, meta, record){
        var recId = record.data.REC_ID;
        var formatString = "<a href='<%=contextPath%>/parts/baseManager/partsBaseManager/PartMoveSeat/partMoveSeatPrint.do?value="+value+"&recId="+recId+"' >[打印标准杂收发单]</a>";
        return String.format("<a href=\"#\" onclick='PrintView(\"" + value + "\"," + recId + ")' >[打印配件移库单]</a>");
	}

	function PrintView(value,recId){
        window.open("<%=contextPath%>/parts/baseManager/partsBaseManager/PartMoveSeat/partMoveSeatPrint.do?value=" + value + "&recId=" + recId);
    }
    
</script>
</html>