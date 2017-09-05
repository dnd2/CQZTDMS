<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
    <title>配件运费结算汇总表</title>
    <script type="text/javascript">
        var tCode = new Array(); //承运商
        <c:forEach items="${listc}" var="obj">
        tCode.push(["${obj.fixName}&&${obj.fixName}"]);
        </c:forEach>

        var tType = new Array(); //承运商
        <c:forEach items="${listf}" var="obj">
        tType.push(["${obj.fixName}&&${obj.fixName}"]);
        </c:forEach>
    </script>
    <script language="javascript">
        var myObjArr = [];
        //初始化查询TABLE
        var myPage;
        var url = g_webAppName + "/report/partReport/partStockReport/PartFreightSummaryReport/queryData.json";
        var title = null;
        var columns = [
            {header: "序号", align: 'center', renderer: getIndex},
            {header: "省份", dataIndex: 'PROVINCE', style: 'text-align:left'},
            {header: "城市", dataIndex: 'CITY', style: 'text-align:left'},
            {header: "经销商编号", dataIndex: 'DEALER_CODE', style: 'text-align:center'},
            {header: "经销商名称", dataIndex: 'DEALER_NAME', style: 'text-align:left'},
            {header: "发运单号", dataIndex: 'TRPLAN_CODE', style: 'text-align:left'},
            {header: "承运商", dataIndex: 'TRANSPORT_ORG', style: 'text-align:center', renderer: getSelTCode},
            {header: "运输方式", dataIndex: 'TRANS_TYPE', style: 'text-align:center', renderer: getSelTType},
            {header: "箱号", dataIndex: 'PKG_NO', style: 'text-align:right'},
            {header: "实际重量(kg)", dataIndex: 'WEIGHT', style: 'text-align:center'},
            {header: "折合重量(kg)", dataIndex: 'EQ_WEIGHT', style: 'text-align:center'},
            {header: "计费重量(kg)", dataIndex: 'CH_WEIGHT', style: 'text-align:center'},
            {header: "小数进位</p>重量(元/kg)", dataIndex: 'CH_WEIGHT2', style: 'text-align:center'},
//            {header: "计费首重(元)", dataIndex: 'FIRST_WEIGHT', style: 'text-align:right'},
//            {header: "计费续重(元)", dataIndex: 'ADDITIONAL_WEIGHT', style: 'text-align:right'},
            {header: "运费(元)", dataIndex: 'WEIGHT_AMOUNT', style: 'text-align:right'},
            {header: "备注", dataIndex: 'REMARK', align: 'center'}
        ];

        function exportExcel() {
            document.fm.action = g_webAppName + "/report/partReport/partStockReport/PartFreightSummaryReport/exportExcel.do";
            document.fm.target = "_self";
            document.fm.submit();
        }

        //生成下拉框
        function getSelTCode(value, meta, record) {
            var tc = record.data.TRPLAN_CODE;
            var output = "<select class=\"short_sel\" size = '1' id = 'sel_o_" + tc + "' onchange='save(" + tc + ")' onmouseover='addList(tCode,\"sel_o_" + tc + "\")' onclick='addList(tCode,\"sel_o_" + tc + "\")'>";
            output = output + "<option value='" + record.data.TRANSPORT_ORG + "'>" + record.data.TRANSPORT_ORG + "</option>";
            output = output + "</select>";
            return String.format(output);
        }
        function getSelTType(value, meta, record) {
            var tc = record.data.TRPLAN_CODE;
            var output = "<select class=\"short_sel\" size = '1' id = 'sel_t_" + tc + "' onchange='save(" + tc + ")' onmouseover='addList(tType,\"sel_t_" + tc + "\")' onclick='addList(tType,\"sel_t_" + tc + "\")'>";
            output = output + "<option value='" + record.data.TRANS_TYPE + "'>" + record.data.TRANS_TYPE + "</option>";
            output = output + "</select>";
            return String.format(output);
        }

        function addList(arr, parms) {
            var obj = document.getElementById(parms);
            if (obj.options.children.length < 3) {
                var strTemp;
                for (var i = 0; i < arr.length; i++) {
                    var strsTemp = new Array();
                    strTemp = arr[i].toString();
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

        function save(tc) {

            var tOrg = document.getElementById("sel_o_" + tc).value;
            var type = document.getElementById("sel_t_" + tc).value;

            if (tOrg == null || tOrg == "") {
                MyAlert("承运商不能为空!");
                document.getElementById("sel_o_" + tc).focus();
                return;
            }else{
                $("TRANSPORT_ORG2").value = tOrg;
            }
            if (type == null || type == "") {
                MyAlert("运输方式不能为空!");
                document.getElementById("sel_t_" + tc).focus();
                return;
            }else{
                $("TRANS_TYPE2").value = type;
            }
            if (confirm("确定保存?")) {
                var url = g_webAppName + '/report/partReport/partStockReport/PartFreightSummaryReport/updateSave.json?TRPLAN_CODE2=' + tc+'&curPage='+pageTemp;
                sendAjax(url, result, 'fm');
            }
        }

        function result(jsonObj) {
            btnEable();
            if (jsonObj != null) {
                var success = jsonObj.success;
                var error = jsonObj.error;
                var exceptions = jsonObj.Exception;
                if (success) {
                    MyAlert(success);
                    __extQuery__(jsonObj.curPage);
                } else if (error) {
                    MyAlert(error);
                } else if (exceptions) {
                    MyAlert(exceptions.message);
                }
            }
        }
    </script>
</head>

<body onload="__extQuery__(1);loadcalendar();">
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <input type="hidden" name="TRANSPORT_ORG2" id="TRANSPORT_ORG2"/>
    <input type="hidden" name="TRANS_TYPE2" id="TRANS_TYPE2"/>

    <div class="wbox">
        <div class="navigation"><img src="<%=contextPath%>/img/nav.gif" alt=""/>&nbsp;当前位置: 报表管理 &gt; 配件仓储报表 &gt;配件运费结算汇总表
        </div>
        <table border="0" class="table_query">
            <th colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif"/> 查询条件</th>
            <tr>
                <td align="right">经销商编号：</td>
                <td><input type="text" id="DEALER_CODE" name="DEALER_CODE" class="middle_txt">
                </td>
                <td align="right">经销商名称：</td>
                <td><input class="middle_txt" type="text" id="DEALER_NAME" name="DEALER_NAME"/></td>
                <td align="right">发运日期：</td>
                <td>
                    <input name="fstartDate" type="text" class="short_time_txt" id="fstartDate" value="${old}"/>
                    <input name="button" value=" " type="button" class="time_ico"
                           onclick="showcalendar(event, 'fstartDate', false);"/>
                    至
                    <input name="fsendDate" type="text" class="short_time_txt" id="fsendDate" value="${now}"/>
                    <input name="button" value=" " type="button" class="time_ico"
                           onclick="showcalendar(event, 'fsendDate', false);"/>
                </td>
            </tr>
            <tr>
                <td align="right">发运单号：</td>
                <td><input class="middle_txt" type="text" id="TRPLAN_CODE" name="TRPLAN_CODE"/></td>
                <td width="10%" align="right">承运物流：</td>
                <td width="20%">
                    <select name="transportOrg" id="transportOrg" onclick="" class="short_sel">
                        <option value="">--请选择--</option>
                        <c:forEach items="${listc}" var="obj">
                            <option value="${obj.fixName}">${obj.fixName}</option>
                        </c:forEach>
                    </select>
                </td>
                <td width="10%" align="right">发运方式：</td>
                <td width="20%">
                    <select name="transType" id="transType" onclick="" class="short_sel">
                        <option value="">--请选择--</option>
                        <c:forEach items="${listf}" var="obj">
                            <option value="${obj.fixName}">${obj.fixName}</option>
                        </c:forEach>
                    </select>
                </td>

            </tr>

            <tr>
                <td colspan="6" align="center">
                    <input name="BtnQuery" id="queryBtn" class="normal_btn" type="button" value="查 询"
                           onclick="__extQuery__(1);"/>
                    <input name="BtnQuery" id="exportBtn" class="normal_btn" type="button" value="导出"
                           onclick="exportExcel();"/>
                </td>
            </tr>
        </table>
    </div>
    <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
    <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
</html>
</form>
</body>
</html>