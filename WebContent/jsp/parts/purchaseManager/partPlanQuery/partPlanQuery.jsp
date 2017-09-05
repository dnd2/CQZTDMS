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
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>计划查询</title>

</head>
<script type="text/javascript">
    var myPage;

    var url = "<%=contextPath%>/parts/purchaseManager/partPlanQuery/PartPlanQuery/queryPartPlan.json";

    var title = null;

    var columns = [
        {header: "序号", align: 'center', renderer: getIndex},
        {header: "计划单号", dataIndex: 'PLAN_CODE', align: 'center'},
        {header: "计划员", dataIndex: 'PLANER_NAME', align: 'center'},
        {header: "制单日期", dataIndex: 'CREATE_DATE', align: 'center'},
        {header: "库房", dataIndex: 'WH_NAME', align: 'center'},
     /*   {header: "计划年月", dataIndex: 'YEAR_MONTH', align: 'center'},*/
        {header: "计划类型", dataIndex: 'PLAN_TYPE', align: 'center', renderer: getItemValue},
        {header: "总数量", dataIndex: 'SUM_QTY', align: 'center'},
        {header: "总金额", dataIndex: 'AMOUNT', align: 'center'},
        {header: "提交时间", dataIndex: 'SUBMIT_DATE', align: 'center'},
        {header: "审核时间", dataIndex: 'CHECK_DATE', align: 'center'},
        {header: "确认时间", dataIndex: 'CONFIRM_DATE', align: 'center'},
        {header: "状态", dataIndex: 'STATE', align: 'center', renderer: getItemValue},
        {header: "生成方式", dataIndex: 'CREATE_TYPE', align: 'center', renderer: getItemValue},
        {id: 'action', header: "操作", sortable: false, dataIndex: 'PLAN_ID', renderer: myLink, align: 'center'}
    ];

    function myLink(value, meta, record) {
        return String.format("<a href=\"#\" onclick='view(\"" + value + "\")'>[查看]</a>");
    }

    function view(value) {
        window.location.href = "<%=contextPath%>/parts/purchaseManager/partPlanQuery/PartPlanQuery/partPlanView.do?planId=" + value;
    }
    function getYearSelect(id, name, scope, value) {
        var date = new Date();
        var year = date.getFullYear();    //获取完整的年份
        var str = "";
        str += "<select  id='" + id + "' name='" + name + "'  style='width:60px;'>";
        str += "<option selected value=''>-请选择-</option>";
        for (var i = (year - scope); i <= (year + scope); i++) {
            if (value == "") {
                if (i == year) {
                    str += "<option  selected value =" + i + ">" + i + "</option >";
                } else {
                    str += "<option   value =" + i + ">" + i + "</option >";
                }
            } else {
                str += "<option  " + (i == value ? "selected" : "") + "value =" + i + ">" + i + "</option >";
            }
        }
        str += "</select> 年";
        document.write(str);
    }
    function getMonThSelect(id, name, value) {
        var date = new Date();
        var month = date.getMonth() + 1;
        var str = "";
        str += "<select  id='" + id + "' name='" + name + "'  style='width:50px;'>";
        str += "<option selected value=''>-请选择-</option>";
        for (var i = 1; i <= 12; i++) {
            if (value == "") {
                if (i == month) {
                    str += "<option selected value =" + (i < 10 ? "0" + i : i) + ">" + (i < 10 ? "0" + i : i) + "</option >";
                } else {
                    str += "<option  value =" + (i < 10 ? "0" + i : i) + ">" + (i < 10 ? "0" + i : i) + "</option >";
                }
            } else {
                str += "<option " + (i == value ? "selected" : "") + "value =" + (i < 10 ? "0" + i : i) + ">" + (i < 10 ? "0" + i : i) + "</option >";
            }
        }
        str += "</select> 月";
        document.write(str);
    }
    function doQuery() {
        var msg = "";
        /*	if(document.getElementById("MYYEAR").value!=""){
         if(document.getElementById("MYMONTH").value==""){
         msg  += "请选择计划月!</br>";
         }
         }
         if(document.getElementById("MYMONTH").value!=""){
         if(document.getElementById("MYYEAR").value==""){
         msg += "请选择计划年!</br>";
         }
         }*/
        if (msg != "") {
            MyAlert(msg);
            return;
        }
        __extQuery__(1);
    }
    function exportExcel() {
        fm.action = "<%=contextPath%>/parts/purchaseManager/partPlanQuery/PartPlanQuery/exportExcel.do";
        fm.target = "_self";
        fm.submit();
    }

</script>
<body onload="__extQuery__(1)">
<form name="fm" id="fm" method="post" enctype="multipart/form-data">

    <div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>当前位置&gt;配件管理&gt;采购计划管理&gt;计划查询</div>
    </td>
    <table class="table_query">
        <tr>
            <th width="100%" colspan="6"><img class="nav" src="<%=contextPath%>/img/subNav.gif"/> 查询条件</th>
        </tr>
        <tr>
        <tr>
            <td width="10%" align="right">配件编码:</td>
            <td width="20%"><input name="PART_OLDCODE" type="text" class="middle_txt" id="PART_OLDCODE"/></td>
            <td width="10%" align="right">配件名称:</td>
            <td width="20%"><input name="PART_CNAME" type="text" class="middle_txt" id="PART_CNAME"/></td>
            <td width="10%" align="right">件号:</td>
            <td width="20%"><input name="PART_CODE" type="text" class="middle_txt" id="PART_CODE"/></td>
        </tr>
            <td width="10%"   align="right">计划年月:</td>
            <td width="20%">
                <script type="text/javascript">
                    getYearSelect("MYYEAR", "MYYEAR", 1, '');
                </script>
                <script type="text/javascript">
                    getMonThSelect("MYMONTH", "MYMONTH", '');
                </script>
            </td>
            <td width="10%"   align="right">计划单号:</td>
            <td width="20%">
                <INPUT class="middle_txt" type="text" id="PLAN_CODE" name="PLAN_CODE"></td>
            <td width="10%"   align="right">计划员:</td>
            <td width="20%">
                <%--  <input class="phone_txt" type="text" name="planerId" id="planerId" /></td>--%>
                <select id="planerId" name="planerId" class="short_sel">
                    <option value="">-请选择-</option>
                    <c:forEach items="${planerList}" var="planerList">
                        <option value="${planerList.USER_ID }">${planerList.USER_NAME }</option>
                    </c:forEach>
                </select></td>
        </tr>
        <tr>
            <td width="10%"   align="right">库房:</td>
            <td width="20%">
                <select name="WH_ID" id="WH_ID" class="short_sel">
                    <option selected value=''>-请选择-</option>
                    <c:forEach items="${wareHouseList}" var="wareHouse">
                        <option value="${wareHouse.WH_ID}">${wareHouse.WH_NAME}</option>
                    </c:forEach>
                </select>
            </td>
            <td width="10%"   align="right">计划类型:</td>
            <td width="20%">
                <script type="text/javascript">
                    genSelBoxExp("PLAN_TYPE", <%=Constant.PART_PURCHASE_PLAN_TYPE%>, "", true, "short_sel", "", "false", '');
                </script>
            </td>
            <td width="10%"   align="right">生成方式:</td>
            <td width="20%">
                <script type="text/javascript">
                    genSelBoxExp("CREATE_TYPE", <%=Constant.PART_PURCHASE_PLAN_CREATE_TYPE %>, "", true, "short_sel", "", "false", '');
                </script>
            </td>
        </tr>
        <tr>
            <td width="10%"   align="right">制单日期:</td>
            <td width="20%">
                <input class="short_txt" id="SCREATE_DATE" name="SCREATE_DATE" datatype="1,is_date,10" maxlength="10"
                       group="SCREATE_DATE,ECREATE_DATE"/>
                <input class="time_ico" value=" " onclick="showcalendar(event, 'SCREATE_DATE', false);" type="button"/>
                至
                <input class="short_txt" id="ECREATE_DATE" name="ECREATE_DATE" datatype="1,is_date,10" maxlength="10"
                       group="SCREATE_DATE,ECREATE_DATE"/>
                <input class="time_ico" value=" " onclick="showcalendar(event, 'ECREATE_DATE', false);" type="button"/></td>

            <td width="10%"   align="right">审核时间:</td>
            <td width="20%">
                <input class="short_txt" id="SCHECK_DATE" NAME="SCHECK_DATE" datatype="1,is_date,10" maxlength="10"
                       group="SCHECK_DATE,ECHECK_DATE"/>
                <input class="time_ico" value=" " onclick="showcalendar(event, 'SCHECK_DATE', false);" type="button"/>
                至
                <input class="short_txt" name="ECHECK_DATE" id="ECHECK_DATE" datatype="1,is_date,10" maxlength="10"
                       group="SCHECK_DATE,ECHECK_DATE"/>
                <input class="time_ico" value=" " onclick="showcalendar(event, 'ECHECK_DATE', false);" type="button"/></td>

            <td width="10%"   align="right">状态:</td>
            <td width="20%">
                <script type="text/javascript">
                    genSelBoxExp("STATE", <%=Constant.PART_PURCHASE_PLAN_CHECK_STATUS%>, "", true, "short_sel", "", "false", '');
                </script>

            </td>
        </tr>
        <tr>
            <td width="10%"   align="right">确认时间:</td>
            <td width="20%">
                <input class="short_txt" id="SCONFIRM_DATE" name="SCONFIRM_DATE" datatype="1,is_date,10" maxlength="10"
                       group="SCONFIRM_DATE,ECONFIRM_DATE"/>
                <input class="time_ico" value=" " onclick="showcalendar(event, 'SCONFIRM_DATE', false);" type="button"/>
                至
                <input class="short_txt" id="ECONFIRM_DATE" name="ECONFIRM_DATE" datatype="1,is_date,10" maxlength="10"
                       group="SCONFIRM_DATE,ECONFIRM_DATE"/>
                <input class="time_ico" value=" " onclick="showcalendar(event, 'ECONFIRM_DATE', false);" type="button"/></td>
            <td width="10%"   align="right">提交时间:</td>
            <td width="20%">
                <input class="short_txt" id="SSUBMIT_DATE" name="SSUBMIT_DATE" datatype="1,is_date,10" maxlength="10"
                       group="SSUBMIT_DATE,ESUBMIT_DATE"/>
                <input class="time_ico" value=" " onclick="showcalendar(event, 'SSUBMIT_DATE', false);" type="button"/>
                至
                <input class="short_txt" id="ESUBMIT_DATE" name="ESUBMIT_DATE" datatype="1,is_date,10" maxlength="10"
                       group="SSUBMIT_DATE,ESUBMIT_DATE"/>
                <input class="time_ico" value=" " onclick="showcalendar(event, 'ESUBMIT_DATE', false);" type="button"/></td>
        </tr>
        <tr align="center" >
            <td colspan="6"><input name="BtnQuery" id="queryBtn" type="button" class="normal_btn" value="查 询" onclick="doQuery()"/>

                <input class="cssbutton" type="button" value="导 出" name="button2" onclick="exportExcel()"/></td>
        </tr>
    </table>

    <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
    <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
</form>
</body>
<script type=text/javascript>
    loadcalendar();  //初始化时间控件
</script>
</html>
