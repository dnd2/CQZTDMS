<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="java.util.*" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>区域外拓计划及总结维护</title>
    <%
        String contextPath = request.getContextPath();
        List attachList = (List) request.getAttribute("attachList");
    %>

    <script type="text/javascript">
        function doInit() {
            loadcalendar();
        }
        function goBack() {
            document.fm.action = '<%=contextPath%>/sales/marketmanage/extendmanage/ExtendPlanAndSummeryMaintain/doInit.do';
            document.fm.submit();
        }
        function doMod(){
            if(submitForm('fm')){
                if(confirm("是否确认保存?")){
                    makeNomalFormCall('<%=contextPath%>/sales/marketmanage/extendmanage/ExtendPlanAndSummeryMaintain/doMod.json',showResult,'fm');
                }
            }
        }
        //回调方法
        function showResult(json){
            debugger;
            if (json.returnValue == '1') {
                MyAlert("操作成功！");
                goBack();
            }
            else {
                MyAlert("操作失败！请联系系统管理员！");
            }
        }
        function calGuestRate(){
            var preGuestNum=document.getElementById("preGuestNum").value;
            var actGuestNum=document.getElementById("actGuestNum").value;
            var guestRate=document.getElementById("guestRate");
            if(preGuestNum && actGuestNum){
                guestRate.value=''+100*Math.round((preGuestNum/actGuestNum)*10000)/10000;
            }
        }
        function calCardRate(){
            var preCardNum=document.getElementById("preCardNum").value;
            var actCardNum=document.getElementById("actCardNum").value;
            var cardRate=document.getElementById("cardRate");
            if(preCardNum &&actCardNum){
                cardRate.value=''+100*Math.round((preCardNum/actCardNum)*10000)/10000;
            }
        }
        function calDealRate(){
            var preDealNum=document.getElementById("preDealNum").value;
            var actDealNum=document.getElementById("actDealNum").value;
            var dealRate=document.getElementById("dealRate");
            if(preDealNum && actDealNum){
                dealRate.value=''+100*Math.round((preDealNum/actDealNum)*10000)/10000;
            }
        }

//删除附件
function delAttach(value){
    var fjId = value;
    var delAttachs = document.getElementById("delAttachs").value;
    document.getElementById(value).style.display = "none";
    if (delAttachs.length == 0) {
        delAttachs = fjId;
    }
    else {
        delAttachs = delAttachs + "," + fjId;
    }
    document.getElementById("delAttachs").value = delAttachs;
}

    </script>

</head>
<body>
<div class="navigation">
    <img src="<%=request.getContextPath()%>/img/nav.gif"/>
    &nbsp;当前位置： 市场活动管理 &gt; 区域外拓计划管理 &gt; 区域外拓计划及总结维护
</div>
<form method="post" name="fm" id="fm">
<div>
    <img class="nav" src="<%=contextPath%>/img/subNav.gif"/>
    &nbsp;活动信息
</div>
<table id="table1" width=100% border="0" align="center"
       cellpadding="1" cellspacing="1" class="table_edit">
    <tr>
        <td align="right">
            活动方式：
        </td>
        <td align="left">
            <script type="text/javascript">
                genSelBoxExp("planType",1330, "${t.planType}", false, "", "", "false", '')
            </script>
        </td>
        <td align="right">
            执行负责人：
        </td>
        <td align="left">
            <input type="text" name="charge" id="charge"
                    datatype="0,is_null,10" value="${t.charge}"/>
        </td>

    </tr>
    <tr>
        <td align="right">
            联系电话：
        </td>
        <td align="left">
            <input type="text" name="tel" id="tel"
                  datatype="0,is_phone,15" value="${t.tel}"/>
        </td>
    </tr>
    <tr>
        <td align="right">
            活动地点：
        </td>
        <td align="left" colspan="5">
            <input type="text" name="place" id="place"
                   datatype="0,is_null,60" size="60" value="${t.place}"/>
        </td>
        <td></td>
    </tr>
    <tr>
        <td align="right">
            活动开始日期：
        </td>
        <td align="left">
            <input class="short_txt" type="text"
                   id="beginDate" name="beginDate"
                   group="beginDate,endDate" datatype="0,is_date,10" value="${beginDate}"/>
            <input class="time_ico" type="button" onClick="showcalendar(event, 'beginDate', false);" value="&nbsp;" />
        </td>
        <td align="right">
            活动结束日期：
        </td>
        <td align="left">
            <input class="short_txt" type="text"
                   id="endDate" name="endDate"
                   group="beginDate,endDate" datatype="0,is_date,10" value="${endDate}"/>
            <input class="time_ico" type="button" onClick="showcalendar(event, 'endDate', false);" value="&nbsp;" />
        </td>
        <td></td>
    </tr>
    <tr>
        <td align="right">
            预计客流数：
        </td>
        <td align="left">
            <input type="text" name="preGuestNum" id="preGuestNum"
                   datatype="0,is_digit,10" onchange="calGuestRate()" value="${t.preGuestNum}"/>
        </td>
        <td align="right">
            实际客流数：
        </td>
        <td align="left">
            <input type="text" name="actGuestNum" id="actGuestNum"
                   datatype="0,is_digit,10" onchange="calGuestRate()" value="${t.actGuestNum}"/>
        </td>

    </tr>
    <tr>
        <td align="right">
            预计建卡数：
        </td>
        <td align="left">
            <input type="text" name="preCardNum" id="preCardNum"
                   datatype="0,is_digit,10" onchange="calCardRate();" value="${t.preCardNum}"/>
        </td>
        <td align="right">
            实际建卡数：
        </td>
        <td align="left">
            <input type="text" name="actCardNum" id="actCardNum"
                   datatype="0,is_digit,10" onchange="calCardRate();" value="${t.actCardNum}"/>
        </td>

    </tr>
    <tr>
        <td align="right">
            预计成交量：
        </td>
        <td align="left">
            <input type="text" name="preDealNum" id="preDealNum"
                   datatype="0,is_digit,10" onchange="calDealRate();" value="${t.preDealNum}"/>
        </td>
        <td align="right">
            实际成交量：
        </td>
        <td align="left">
            <input type="text" name="actDealNum" id="actDealNum"
                   datatype="0,is_digit,10" onchange="calDealRate();" value="${t.actDealNum}"/>
        </td>

    </tr>
    <tr>
        <td align="right">
            达成率(客流)：
        </td>
        <td colspan="4" align="left">
            <input type="text" name="guestRate" id="guestRate" readonly="readonly" class="short_txt" value="${t.guestRate}"/>%
        </td>
    </tr>
    <tr>
        <td align="right">
            达成率(建卡)：
        </td>
        <td colspan="4" align="left">
            <input type="text" name="cardRate" id="cardRate" readonly="readonly" class="short_txt" value="${t.cardRate}"/>%
        </td>
    </tr>
    <tr>
        <td align="right">
            达成率(成交)：
        </td>
        <td colspan="4" align="left">
            <input type="text" name="dealRate" id="dealRate" readonly="readonly" class="short_txt" value="${t.dealRate}"/>%
        </td>
    </tr>
</table>

<!-- 添加附件start -->
<div>
    <img class="nav" src="<%=contextPath%>/img/subNav.gif" />
    &nbsp;附件
</div>
<table class="table_info" border="0" id="file">
    <tr>
        <th>
            附件列表：
            <input type="hidden" id="fjids" name="fjids" />
            <span> <input type="button" class="cssbutton"
                    onclick="showUpload('<%=contextPath%>')" value='添加附件' /> </span>
        </th>
    </tr>
    <tr>
        <td width="100%" colspan="2"><jsp:include
                page="${contextPath}/uploadDiv.jsp" /></td>
    </tr>
</table>
<table id="attachTab" class="table_info">
    <%
        if (attachList != null && attachList.size() != 0) {
    %>
    <c:forEach items="${attachList}" var="attls">
        <tr class="table_list_row1" id="${attls.FJID}">
            <td>
                <a target="_blank" href="${attls.FILEURL}">${attls.FILENAME}</a>
            </td>
            <td>
                <input type=button onclick="delAttach('${attls.FJID}')"
                    class="normal_btn" value="删 除" />
                <input type="hidden" name="delAttachs" id="delAttachs" value="" />
            </td>
        </tr>
    </c:forEach>
    <%
        }
    %>
</table>
<!-- 添加附件end -->

<table width=100% border="0" align="center" cellpadding="1"
       cellspacing="1" class="table_query">
    <tr>
        <td colspan="4" align="center">
            <input type="hidden" name="planId" value="${t.planId}"/>
            <input type="button" class="cssbutton" name="saveBtn"
                   onClick="doMod();" value="保存"/>
            <input type="button" class="cssbutton" name="backBtn"
                   onClick="goBack();" value="返回"/>
        </td>
    </tr>
</table>
</form>
</body>
</html>