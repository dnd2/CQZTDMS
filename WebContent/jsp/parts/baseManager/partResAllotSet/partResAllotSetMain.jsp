<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="com.infodms.dms.common.Constant" %>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <% String contextPath = request.getContextPath(); %>
    <title>配件资源分配设置</title>
    <script language="javascript" type="text/javascript">
        function doInit() {
            //loadcalendar();  //初始化时间控件
            __extQuery__(1);
        }
    </script>
</head>
<body>
<form method="post" name="fm" id="fm">
    <div class="wbox">
        <div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif"/>&nbsp;当前位置：
            基础信息管理 &gt; 配件基础信息维护 &gt; 配件资源分配设置
        </div>
        <table class="table_query">
            <th colspan="6"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif"/> 查询条件</th>
            <tr>
                <td width="10%" align="right">配件编码：</td>
                <td width="20%"><input class="middle_txt" type="text" name="partOldcode" id="partOldcode"/></td>
                <td width="10%" align="right">配件名称：</td>
                <td width="20%"><input class="middle_txt" type="text" name="partName" id="partName"/></td>
                <td width="10%" align="right">件号：</td>
                <td width="20%"><input class="middle_txt" type="text" name="partCode" id="partCode"/></td>
            </tr>
            <tr>
                <td align="center" colspan="6">
                    <input class="normal_btn" type="button" value="查 询" name="BtnQuery" id="queryBtn"
                           onclick="__extQuery__(1)"/>
                    <input class="normal_btn" type="button" value="导 出" onclick="exportResAllotExcel()"/>
                </td>
            </tr>
        </table>
    </div>

    <!-- 查询条件 end -->
    <!--分页 begin -->
    <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
    <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    <!--分页 end -->
</form>
<script type="text/javascript">
    var myPage;

    var url = "<%=contextPath%>/parts/baseManager/partResAllotSet/partResAllotSetAction/partResAllotSetSearch.json";

    var title = null;

    var columns = [
        {header: "序号", dataIndex: 'DEFT_ID', renderer: getIndex,  style: 'text-align:left'},
        {id: 'action', header: "操作", sortable: false, dataIndex: 'PART_ID', renderer: myLink,  style: 'text-align:left'},
        {header: "配件编码", dataIndex: 'PART_OLDCODE',  style: 'text-align:left'},
        {header: "配件名称", dataIndex: 'PART_CNAME',  style: 'text-align:left'},
        {header: "件号", dataIndex: 'PART_CODE',  style: 'text-align:left'},
        {header: "分配比例", dataIndex: 'ALLOT_RATIO',  style: 'text-align:center', renderer: getRateText},
        {header: "分配数量", dataIndex: 'ALLOT_NUM',  style: 'text-align:center', renderer: getNumText},
        {header: "是否允许BO转销售", dataIndex: 'BO_TOSALES',  style: 'text-align:center', renderer: getBOCheckbox},
        {header: "是否有效", dataIndex: 'STATE',  style: 'text-align:center', renderer: getItemValue}

    ];


    //设置超链接
    function myLink(value, meta, record) {
        var defineId = record.data.DEF_ID;
        var state = record.data.STATE;
        var disableValue = <%=Constant.STATUS_DISABLE%>;
        var enableValue = <%=Constant.STATUS_ENABLE%>;
        if (disableValue == state) {
            return String.format("<a href=\"#\" onclick='enableData(\"" + defineId + "\")'>[有效]</a>");
        } else if (enableValue == state) {
            return String.format("<a href=\"#\" onclick='cel(\"" + defineId + "\")'>[失效]</a>&nbsp;<a href=\"#\" onclick='updateData(\"" + value + "\",\"" + defineId + "\")'>[保存]</a>");
        } else {
            return String.format("<a href=\"#\" onclick='updateData(\"" + value + "\",\"" + defineId + "\")'>[保存]</a>");
        }

    }

    function getRateText(value, meta, record) {
        var partID = record.data.PART_ID;
        return String.format("<input type='text' id='allotRatio_" + partID + "' style='text-align: right;' value=\"" + value + "\" onchange='dataTypeCheck1(this,\"" + partID + "\")' />");
    }

    function getNumText(value, meta, record) {
        var partID = record.data.PART_ID;
        return String.format("<input type='text' id='allotNum_" + partID + "' style='text-align: right;' value=\"" + value + "\" onchange='dataTypeCheck(this,\"" + partID + "\")' />");
    }

    function getBOCheckbox(value, meta, record) {
        var partID = record.data.PART_ID;
        var checkedValue = <%=Constant.IF_TYPE_YES%>;
        var unCheckedValue = <%=Constant.IF_TYPE_NO%>;
        if (checkedValue == value) {
            return String.format("<input type='checkbox' id='boToSales_" + partID + "' value=\"" + value + "\" checked />");
        }
        else
            return String.format("<input type='checkbox' id='boToSales_" + partID + "' value=\"" + unCheckedValue + "\" />");
    }

    //数据验证
    function dataTypeCheck(obj, partId) {
        var value = obj.value;
        if (isNaN(value)) {
            MyAlert("请输入数字!");
            obj.value = "";
            return;
        }
        var re = /^[1-9]+[0-9]*]*$/;
        if (!re.test(obj.value)) {
            MyAlert("请输入正整数!");
            obj.value = "";
            return;
        }
        if (0 < value) {
            document.getElementById("allotRatio_" + partId).value = (0.00).toFixed(2);
        }
    }

    function dataTypeCheck1(obj, partId) {
        var value = obj.value;
        if (isNaN(value)) {
            MyAlert("请输入数字!");
            obj.value = (0.00).toFixed(2);
            return;
        }
        if (1 < value) {
            MyAlert("分配比例不能大于 1 !");
            obj.value = "";
            return;
        }
        if (0 < value) {
            obj.value = parseFloat(value).toFixed(2);
            document.getElementById("allotNum_" + partId).value = 0;
        }
    }

    //设置失效：
    function cel(parms) {
        if (confirm("确定失效该数据?")) {
            btnDisable();
            var url = '<%=contextPath%>/parts/baseManager/partResAllotSet/partResAllotSetAction/celPartResAllotSet.json?disabeParms=' + parms + '&curPage=' + myPage.page;
            makeFormCall(url, showResult, 'fm');
        }
    }

    //设置有效：
    function enableData(parms) {
        if (confirm("确定有效该数据?")) {
            btnDisable();
            var url = '<%=contextPath%>/parts/baseManager/partResAllotSet/partResAllotSetAction/enablePartResAllotSet.json?enableParms=' + parms + '&curPage=' + myPage.page;
            makeFormCall(url, showResult, 'fm');
        }
    }

    //保存
    function updateData(partId, defineId) {
        var allotRatio = document.getElementById("allotRatio_" + partId).value;
        var allotNum = document.getElementById("allotNum_" + partId).value;
        var objBO = document.getElementById("boToSales_" + partId);
        var boToSales = <%=Constant.IF_TYPE_YES%>;
        if (!objBO.checked) {
            boToSales = <%=Constant.IF_TYPE_NO%>;
        }
        if ("" == allotRatio || isNaN(allotRatio) || 1 < allotRatio) {
            MyAlert("请正确设置分配比例!");
            return;
        }

        if ("" == allotNum || isNaN(allotNum)) {
            MyAlert("请正确设置分配数量!");
            return;
        }

        if (confirm("确定保存设置?")) {
            btnDisable();
            var url = '<%=contextPath%>/parts/baseManager/partResAllotSet/partResAllotSetAction/updatePartResAllotSet.json?defId=' + defineId + '&partId=' + partId + '&allotRatio=' + allotRatio + '&allotNum=' + allotNum + '&boToSales=' + boToSales + '&curPage=' + myPage.page;
            makeFormCall(url, showResult, 'fm');
        }
    }

    function showResult(json) {
        btnEnable();
        if (json.errorExist != null && json.errorExist.length > 0) {
            MyAlert(json.errorExist);
        } else if (json.success != null && json.success == "true") {
            MyAlert("操作成功!");
            __extQuery__(json.curPage);
        } else {
            MyAlert("操作失败，请联系管理员!");
        }
    }

    //导出
    function exportResAllotExcel() {
        document.fm.action = "<%=contextPath%>/parts/baseManager/partResAllotSet/partResAllotSetAction/exportPartResAllotExcel.do";
        document.fm.target = "_self";
        document.fm.submit();
    }

    //失效按钮
    function btnDisable() {

        $$('input[type="button"]').each(function (button) {
            button.disabled = true;
        });

    }

    //有效按钮
    function btnEnable() {

        $$('input[type="button"]').each(function (button) {
            button.disabled = "";
        });

    }
</script>
</body>
</html>