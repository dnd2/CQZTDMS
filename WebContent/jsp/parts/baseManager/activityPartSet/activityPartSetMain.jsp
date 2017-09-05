<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <% String contextPath = request.getContextPath(); %>
    <title>活动配件明细设置</title>
    <script language="javascript" type="text/javascript">
        function doInit() {
            loadcalendar();  //初始化时间控件
            __extQuery__(1);
        }
    </script>
</head>
<body onunload='javascript:destoryPrototype()'>
<form method="post" name="fm" id="fm">
    <input id="flag" name="flag" type="hidden" value="${flag}">
    <input type="hidden" name="desId" id="desId" value=""/>
    <input type="hidden" name="describe" id="describe" value=""/>
    <input type="hidden" name="startDate" id="startDate" value=""/>
    <input type="hidden" name=endDate id="endDate" value=""/>
    <input type="hidden" name=actCode id="actCode" value=""/>
    <input type="hidden" name=actType id="actType" value=""/>
    <input type="hidden" name=partType id="partType" value=""/>
    <input type="hidden" name="status" id="status" value=""/>

    <div class="wbox">
        <div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif"/>&nbsp;当前位置：
            基础信息管理 &gt; 配件基础信息维护 &gt; 活动配件明细设置
        </div>
        <table class="table_query">
            <th colspan="10"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif"/> 查询条件</th>
            <tr>
                <td width="10%" align="right">活动编号：</td>
                <td width="20%"><input class="middle_txt" type="text" name="act_Code" id="act_Code"/></td>
                <td width="10%" align="right">活动描述：</td>

                <td width="20%"><input class="middle_txt" type="text" name="actDescription" id="actDescription"/></td>
                <td width="10%" align="right">活动类型：</td>
                <td width="7%" align="left"><select name="state" id="state"
                                                    class="short_sel" onchange="deleteTabCell(this)">
                    <option value="">请选择</option>
                    <c:forEach items="${stateMap}" var="stateMap">
                        <option value="${stateMap.key}">${stateMap.value}</option>
                    </c:forEach>
                </select></td>
            </tr>
            <tr>
                <td width="10%" align="right">日期：</td>
                <td width="25%">
                    <input id="checkSDate" class="short_txt" name="checkSDate" datatype="1,is_date,10" maxlength="10"
                           group="checkSDate,checkEDate" readonly="readonly"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'checkSDate', false);" value="?"
                           type="button"/>
                    至
                    <input id="checkEDate" class="short_txt" name="checkEDate" datatype="1,is_date,10" maxlength="10"
                           group="checkSDate,checkEDate" readonly="readonly"/>
                    <input class="time_ico" value=" " onclick="showcalendar(event, 'checkEDate', false);" value="?"
                           type="button"/>
                </td>
                <td width="10%" align="right">配件编码：</td>
                <td width="20%"><input class="middle_txt" type="text" name="partOldcode" id="partOldcode"/></td>
                <td width="10%" align="right">配件名称：</td>
                <td width="20%"><input class="middle_txt" type="text" name="partName" id="partName"/></td>

            </tr>
            <tr>
                <td align="center" colspan="10">
                    <input class="normal_btn" type="button" value="查 询" name="BtnQuery" id="queryBtn"
                           onclick="__extQuery__(1)"/>
                    <input class="normal_btn" type="button" value="新 增" onclick="relationAdd()"/>
                    <input class="normal_btn" type="button" value="导 出" onclick="exportPartSTOExcel()"/>
                    <input name="upload" id="upload" class="normal_btn" type="button" value="强制关闭"
                           onClick="showTab();"/>
                </td>
            </tr>
        </table>
        <table class="table_edit" id="uploadTable" style="display: none">
            <tr>
                <td style="color: red">
                    请输入服务商代码：&nbsp;
                    <input type="text"  id="dealerCode" value=""/> &nbsp;
                    <input type="button" id="upbtn" class="normal_btn" value="关闭" onclick="close1();"/>
                    提醒：点【关闭】后，该服务商相关的活动将全部关闭！
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

    var url = "<%=contextPath%>/parts/baseManager/activityPartSet/activityPartSetAction/activityPartSetSearch.json";

    var title = null;

    var columns = [
        {header: "序号", dataIndex: 'DEF_ID', renderer: getIndex, style: 'text-align:center'},
        {id: 'action', header: "操作", sortable: false, dataIndex: 'ID', renderer: myLink},
        {header: "活动描述", dataIndex: 'DESCRIBE', style: 'text-align:left'},
        {header: "活动编号", dataIndex: 'ACTIVITY_CODE', style: 'text-align:left'},
        {header: "活动类型", dataIndex: 'ACTIVITY_TYPE', renderer: getItemValue, style: 'text-align:left'},
        {header: "活动配件类型", dataIndex: 'PART_TYPE', renderer: getItemValue, style: 'text-align:center'},
        {header: "状态", dataIndex: 'STATE', style: 'text-align:center'}
// 				{header: "配件名称", dataIndex: 'PART_NAME',  style: 'text-align:left'},
//                 {header: "件号", dataIndex: 'PART_CODE',  style: 'text-align:center'},
// 				{header: "建议数量", dataIndex: 'SPEC_QTY',  style: 'text-align:center'},
// 				{header: "是否需要回运", dataIndex: 'ISNEED_FLAG', renderer: getItemValue, style: 'text-align:center'},
// 				{header: "开始日期", dataIndex: 'F_START_DATE',  style: 'text-align:center'},
// 				{header: "结束日期", dataIndex: 'F_END_DATE',  style: 'text-align:center'}
    ];

    //设置超链接
    function myLink(value, meta, record) {
        var defineId = record.data.DEF_ID;
        var desId = record.data.DESCRIBE_ID;
        var describe = record.data.DESCRIBE;
        var startDate = record.data.START_DATE;
        var endDate = record.data.END_DATE;
        var actType = record.data.ACTIVITY_TYPE;
        var actCode = record.data.ACTIVITY_CODE;
        var partType = record.data.PART_TYPE;
        var state = record.data.STATE;
        if (state == '已关闭') {
            return String.format("<a href=\"#\" onclick='formod(\"" + desId + "\",\"" + describe + "\",\"" + startDate + "\",\"" + endDate + "\",\"" + actType + "\",\"" + actCode + "\",\"" + partType + "\",\"" + state + "\")'>[查看]</a><a href=\"#\" onclick='showClosePage(\"" + desId + "\")'>[打开活动]</a>");
        } else {
            return String.format("<a href=\"#\" onclick='formod(\"" + desId + "\",\"" + describe + "\",\"" + startDate + "\",\"" + endDate + "\",\"" + actType + "\",\"" + actCode + "\",\"" + partType + "\")'>[设置]</a>  <a href=\"#\" onclick='showClosePage(\"" + desId + "\")'>[结束活动]</a>");
        }
    }

    //维护
    function formod(desId, describe, startDate, endDate, actType, actCode, partType, status) {
        btnDisable();
        document.getElementById("desId").value = desId;
        document.getElementById("describe").value = describe;
        document.getElementById("startDate").value = startDate;
        document.getElementById("endDate").value = endDate;
        document.getElementById("actType").value = actType;
        document.getElementById("actCode").value = actCode;
        document.getElementById("partType").value = partType;
        document.getElementById("status").value = status;
        document.fm.action = "<%=contextPath%>/parts/baseManager/activityPartSet/activityPartSetAction/activityPartSetFormodInit.do";
        document.fm.target = "_self";
        document.fm.submit();
    }

    //关闭
    function closeActivity(desId, describe, startDate, endDate, actType, actCode, partType) {

        btnDisable();
        document.getElementById("desId").value = desId;
        document.getElementById("describe").value = describe;
        document.getElementById("startDate").value = startDate;
        document.getElementById("endDate").value = endDate;
        document.getElementById("actType").value = actType;
        document.getElementById("actCode").value = actCode;
        document.getElementById("partType").value = partType;
        if (confirm("你确定要关闭此活动？")) {
            document.fm.action = "<%=contextPath%>/parts/baseManager/activityPartSet/activityPartSetAction/closeActivity.do";
            document.fm.target = "_self";
            document.fm.submit();
        }

    }

    //新增
    function relationAdd() {
        btnDisable();
        window.location.href = "<%=contextPath%>/parts/baseManager/activityPartSet/activityPartSetAction/activityPartSetAddInit.do";
    }

    function exportPartSTOExcel() {
        document.fm.action = "<%=contextPath%>/parts/baseManager/activityPartSet/activityPartSetAction/exportActPartExcel.do";
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
    function showClosePage(descId) {
        var url = "<%=contextPath%>/parts/baseManager/activityPartSet/activityPartSetAction/activityPartSetCloseInit.do?descId=" + descId;
        OpenHtmlWindow(url, 800, 600);
    }

    function showTab() {
        if ($("uploadTable").style.display == "none") {
            $("uploadTable").style.display = "block";
        } else {
            $("uploadTable").style.display = "none";
        }
    }

    function close1() {
        var dealerCode = $('dealerCode').value;
        if (dealerCode == "") {
            MyAlert('不许为空！');
            return false;
        }
        MyConfirm("确定关闭?",closeAction,[dealerCode]);
    }

    function closeAction(dealerCode){
        var closeUrl = '<%=contextPath%>/parts/baseManager/activityPartSet/activityPartSetAction/closeActivity.json?dealerCode=' + dealerCode + '&curPage=' + myPage.page + '&flag=' + 3;
        makeNomalFormCall(closeUrl, veiwParts, 'fm');
    }

    function veiwParts(jsonObj) {
        if (jsonObj != null) {
            var success = jsonObj.success;
            var error = jsonObj.error;
            if (success != "" && success != "undefined") {
                MyAlert(success);
            }
            if (error != "" && error != "undefined") {
                MyAlert(error);
            }
            __extQuery__(jsonObj.curPage);
        }
    }
</script>
</body>
</html>