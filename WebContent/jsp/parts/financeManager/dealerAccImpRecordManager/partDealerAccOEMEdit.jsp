<%@ page import="com.infodms.dms.common.Constant" %>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ taglib uri="/jstl/fmt" prefix="fmt" %>
<%
    String contextPath = request.getContextPath();
%>
<HEAD>
    <TITLE>打款登记编辑</TITLE>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link href="<%=contextPath%>/style/content.css" rel="stylesheet" type="text/css"/>
    <link href="<%=contextPath%>/style/calendar.css" rel="stylesheet" type="text/css"/>
    <link href="<%=contextPath%>/style/page-info.css" rel="stylesheet" type="text/css"/>
    <link href="<%=contextPath%>/style/dtree1.css" rel="stylesheet" type="text/css"/>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <SCRIPT type=text/javascript>
        function doInit() {
            loadcalendar();//日期控件初始化
        }
    </SCRIPT>
</HEAD>
<BODY>

<DIV class=navigation><IMG src="<%=request.getContextPath()%>/img/nav.gif">&nbsp;当前位置：配件管理 &gt; 配件财务管理 &gt; 配件首批款登记 &gt;
    登记
</DIV>
<FORM id=fm method=post name=fm>
    <input type="hidden" name="flag" id="flag" value="1"/>
    <input type="hidden" name="hid" id="hid" value="${hid}"/>
    <input type="hidden" name="jp" id="jp"/>
    <TABLE class=table_query>
        <th colspan="4"/>
        <tr>
            <td align="right">服务商：</td>
            <td>
                <input name="dealerName" class="middle_txt" id="dealerName" value="${dealerName}" type="text"
                       size="20" readonly="readonly"/>
                <input name="dealerCode" id="dealerCode" value="${dealerCode}" type="hidden"/>
                <input name="dealerId" id="dealerId" value="${dealerId}" type="hidden"/>
                <input name="button2" type="button" class="mini_btn" value="..."
                       onclick="showOrgDealer('dealerCode','dealerId','false','','','','<%=Constant.DEALER_TYPE_DWR%>','dealerName');"/>
               <span style="color: red">*</span>
            </td>
            <td align="right">首批金额：</td>
            <td align="left">
                <input class="middle_txt" type="text" datatype="0,isMoney,10" maxlength="10" value="${amount}"
                       id="amount" name="amount">
            </td>
            <td align="right">首批日期：</td>
            <td align="left">
                <input class="short_time_txt" id="checkSDate" name="checkSDate" datatype="1,is_null,10"
                       value="${dk_date}" readonly
                       group="checkSDate,ECREATE_DATE"/>
                <input class="time_ico" value=" " onclick="showcalendar(event, 'checkSDate', false);" type="button"/>
        </tr>
        <tr>
            <td align="right">备注：</td>
            <td align="left" colspan="4">
                <textarea style="width:75%;" name="remark" id="remark" rows="3">${remark}</textarea>
            </td>
        </tr>
    </TABLE>
    <table class=table_query>
        <tr>
            <TD align="center">
                <INPUT id="saveBtn" onclick="checkFormAddOrUpdate();"
                       class=normal_btn value=保存 type=button>
                <INPUT id="backBtn" onclick="gobackConfirm();"
                       class=normal_btn value=返回 type=button>
            </TD>
        </tr>
    </table>
    <!--分页 end --></FORM>
<script type="text/javascript">
    function saveCredit() {
        btnDisable();
        makeNomalFormCall('<%=contextPath%>/parts/financeManager/dealerAccImpRecordManager/partDealerAccImpRecAction/partDealerAccQuerySave.json', showResult, 'fm');
        btnEable();
    }
    function showResult(json) {
        if (json.ACTION_RESULT == '1') {
            MyAlert("保存成功!", goback);
        } else {
            MyAlert(json.ACTION_RESULT);
            btnEable();
        }
    }
    function checkFormAddOrUpdate() {
        var dealerId = document.getElementById("dealerId").value;
        var amount = document.getElementById("amount").value;
        var checkDate = document.getElementById("checkSDate").value;
        var reg = /^[1-9]{1}\d*(\.\d{1,2})?$/;

        if (dealerId == "") {
            MyAlert("服务商不能为空");
            return;
        }
        if (amount == "") {
            MyAlert("金额不能为空！");
            return;
        }

        if (checkDate == "") {
            MyAlert("日期不能为空！");
            return;
        }
        if (!reg.test(amount)) {
            MyAlert("金额录入不对！");
            return;
        }
        if (!submitForm('fm')) {
            return false;
        }
        MyConfirm("是否确认保存?", saveCredit);
    }


    function showSubmitResult(json) {
        if (json.ACTION_RESULT == '1') {
            MyAlert("保存成功!");
            goback();
        } else {
            MyAlert(json.ACTION_RESULT);
            btnEable();
        }
    }

    function gobackConfirm() {
        MyConfirm("确认返回?", goback);

    }
    function goback() {
        window.location.href = "<%=contextPath%>/parts/financeManager/dealerAccImpRecordManager/partDealerAccImpRecAction/partDealerAccOEMinit.do";
    }
</script>

</BODY>
</HTML>