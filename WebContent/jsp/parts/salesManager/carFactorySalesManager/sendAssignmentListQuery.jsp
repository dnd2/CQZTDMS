<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.infodms.dms.common.Constant" %>
<%@page import="java.util.List" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
    <%
        String contextPath = request.getContextPath();
        List list = (List) request.getAttribute("list_logi");
    %>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title> 发运分派管理 </title>
</head>
<body onload="__extQuery__(1);">
<div class="navigation"><img src="<%=contextPath %>/img/nav.gif"/>&nbsp;当前位置>储运管理>发运管理> 发运分派查询
</div>
<form name="fm" method="post" id="fm">
    <input type="hidden" name="dealerId" id="dealerId" value="${dealerId}"/>
    <!-- 查询条件 begin -->
    <table class="table_query" id="subtab">
        <tr class="csstr" align="center">
            <td align="right">订单号：</td>
            <td align="left">
                <input type="text" id="ORDER_NO" name="ORDER_NO" class="middle_txt" size="15"/>
            </td>
            <td align="right">选择经销商：</td>
            <td align="left">
                <input name="dealerCode" type="hidden" id="dealerCode" class="middle_txt" value=""/>
                <input name="dealerName" type="text" id="dealerName" class="middle_txt" value="" readonly="readonly"/>
                <input name="dlbtn" type="button" class="mini_btn"
                       onclick="showOrgDealer('dealerCode', '', 'true', '', 'true','','<%=Constant.DEALER_TYPE_DVS %>','dealerName');"
                       value="..."/>
                <input type="button" class="normal_btn" onclick="txtClr('dealerCode','dealerName');" value="清 空"
                       id="clrBtn"/>
            </td>
        </tr>
        <tr class="csstr" align="center">
            <td align="right">订单类型：</td>
            <td align="left">
                <label>
                    <script type="text/javascript">
                        genSelBoxExp("ORDER_TYPE", <%=Constant.ORDER_TYPE%>, "", true, "short_sel", "", "false", '');
                    </script>
                </label>
            </td>
            <td align="right" nowrap="true">审核日期：</td>
            <td align="left" nowrap="true">
                <input name="RAISE_STARTDATE" type="text" class="short_time_txt" id="RAISE_STARTDATE"
                       readonly="readonly"/>
                <input name="button" value=" " type="button" class="time_ico"
                       onclick="showcalendar(event, 'RAISE_STARTDATE', false);"/>
                &nbsp;至&nbsp;
                <input name="RAISE_ENDDATE" type="text" class="short_time_txt" id="RAISE_ENDDATE" readonly="readonly"/>
                <input name="button" value=" " type="button" class="time_ico"
                       onclick="showcalendar(event, 'RAISE_ENDDATE', false);"/>
            </td>
        </tr>
        <tr align="center">
            <td colspan="4" align="center">
                <input type="reset" class="cssbutton" id="resetButton" value="重置"/>
                <input type="button" id="queryBtn" class="cssbutton" value="查询" onclick="doQuery();"/>
                <!--     	  <input type="button" id="queryBtn" class="cssbutton"  value="导出" onclick="download_();" />  	 	 -->
            </td>
        </tr>
    </table>
    <table class="table_query" align="center" style="margin-top: 2px;" id="tab_remark" style="display: none">
        <tbody>
        <tr>
            <th nowrap="nowrap" align=left>
                <img class=nav src="<%=contextPath %>/img/subNav.gif"/>
                发运分派信息
            </th>
        </tr>
        </tbody>
    </table>
    <table class=table_query id="tab_remark1" style="display: none">
        <tr align="left">
            <td align="right">
                备注:
            </td>
            <td align="left">
                <input type="text" class="middle_txt" id="REMARK" name="REMARK" style="width: 300px;"/>
                <input class="normal_btn" type="button" id="saveButton" value="确定发运" onclick="retreat();"/>
            </td>
            <td>&nbsp;</td>
        </tr>
    </table>
    <!-- 查询条件 end -->
    <!--分页 begin -->
    <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
    <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    <!--分页 end -->
</form>
<!--页面列表 begin -->
<script type="text/javascript">
    var myPage;
    //查询路径

    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartPkg/sendAssignmentQuery.json";
    var title = null;
    var columns = [
        {header: "序号", align: 'center', renderer: getIndex},
        {id: 'action', header: "<input type='checkbox' name='checkAll' onclick='selectAll(this,\"groupIds\")'/>", sortable: false, dataIndex: 'ORDER_ID', renderer: myCheckBox},
//    {header: "操作", dataIndex: 'ORDER_ID', sortable: false, align: 'center', renderer: myLink},
        {header: "经销商代码", dataIndex: 'DEALER_CODE', align: 'center'},
        {header: "经销商名称", dataIndex: 'DEALER_NAME', align: 'center'},
        {header: "发运省市", dataIndex: 'PC_NAME', align: 'center'},
        {header: "订单号", dataIndex: 'ORDER_NO', align: 'center', renderer: myOrderNos},
        //{header: "提报日期",dataIndex: 'RAISE_DATE',align:'center'},
        {header: "审核日期", dataIndex: 'PLAN_CHK_DATE', align: 'center'},
        {header: "订单类型", dataIndex: 'ORDER_TYPE', align: 'center', renderer: getItemValue},
        {header: "发运类型", dataIndex: 'SEND_TYPE', align: 'center', renderer: getItemValue},
        {header: "订单数量", dataIndex: 'CHK_NUM', align: 'center'},
        {header: "已分派数量", dataIndex: 'ASS_NUM', align: 'center'},
        {header: "未发运分派数量", dataIndex: 'THIS_ASS_NUM', align: 'center'},
        {header: "未分派数量", dataIndex: 'NOT_ASS_NUM', align: 'center'},
        {header: "资源审核备注", dataIndex: 'ORDER_REMARK', align: 'center'}
//    {header: "操作", dataIndex: 'ORDER_ID', sortable: false, align: 'center', renderer: myLink1}
        //{header: "资源分配描述",dataIndex: 'ZYFP_DESC',align:'center'}

    ];
    function myOrderNos(value, meta, record) {
        var orderRemark = record.data.ORDER_REMARK;
        var colorr = record.data.COLOR;
        var labell = "<span style='background-color: " + colorr + "'>" + value + "</span>";
        //暂时不开放
        //  if(parseInt(orderNum)==sunAss){
        //link+="<a href=\"#\" onclick='canelOrder(\""+value+"\",\""+orderNo+"\")'>[驳回订单]</a>";
        //}
        return String.format(labell);
    }
    function myLink(value, meta, record) {
        var orderNum = record.data.CHK_NUM;
        var orderNo = record.data.ORDER_NO;
        var thisAss = record.data.THIS_ASS_NUM;
        var noAss = record.data.NOT_ASS_NUM;
        var dealerId = record.data.DEALER_ID;
        var sunAss = parseInt(thisAss) + parseInt(noAss);
        var link = "<a href=\"#\" onclick='assSend(\"" + value + "\",\"" + dealerId + "\")'>[分派]</a>";
        //暂时不开放
        //  if(parseInt(orderNum)==sunAss){
        //link+="<a href=\"#\" onclick='canelOrder(\""+value+"\",\""+orderNo+"\")'>[驳回订单]</a>";
        //}
        return String.format(link);
    }
    function myLink1(value, meta, record) {
        var orderNum = record.data.CHK_NUM;
        var orderNo = record.data.ORDER_NO;
        var thisAss = record.data.THIS_ASS_NUM;
        var noAss = record.data.NOT_ASS_NUM;
        var sunAss = parseInt(thisAss) + parseInt(noAss);
        var link = "";
        //暂时不开放
        if (parseInt(orderNum) == sunAss) {
            link += "<a href=\"#\" onclick='canelOrder(\"" + value + "\",\"" + orderNo + "\")'>[驳回订单]</a>";
        }
        return String.format(link);
    }
    //驳回订单
    function canelOrder(orderId, orderNo) {
        MyConfirm("确定驳回订单【" + orderNo + "】", confirmCanel, [orderId]);
    }
    function confirmCanel(orderId) {
        var url = "<%=request.getContextPath()%>/sales/storage/sendmanage/SendAssignment/canelOrder.json";
        makeCall(url, canelBoOrderBack, {orderId: orderId});
    }
    function canelBoOrderBack(json) {
        if (json.returnValue == 1) {
            parent.MyAlert("操作成功！");
            doQuery();
        } else if (json.returnValue == 33) {
            MyAlert(json.logInfo);
        } else {
            MyAlert(json.getException());
        }
    }
    //初始化
    function doInit() {
        loadcalendar();//日期控件初始化
        doQuery();
    }
    function doQuery() {
        document.getElementById("a1").innerHTML = '';
        document.getElementById("a2").innerHTML = '';
        document.getElementById("a3").innerHTML = '';
        makeNomalFormCall("<%=contextPath%>/sales/storage/sendmanage/SendAssignment/sendAssignmentQuery.json?common=1", function (json) {
            document.getElementById("a1").innerHTML = json.valueMap.CHK_NUM == null ? '0' : json.valueMap.CHK_NUM;
            document.getElementById("a2").innerHTML = json.valueMap.ASS_NUM == null ? '0' : json.valueMap.ASS_NUM;
            document.getElementById("a4").innerHTML = json.valueMap.THIS_ASS_NUM == null ? '0' : json.valueMap.THIS_ASS_NUM;
            document.getElementById("a3").innerHTML = json.valueMap.NOT_ASS_NUM == null ? '0' : json.valueMap.NOT_ASS_NUM;
        }, 'fm');
        __extQuery__(1);
    }
    function customerFunc() {
        var arrayObj = new Array();
        arrayObj = document.getElementsByName("groupIds");
        if (arrayObj.length > 0) {//大于0表表示有数据，备注显示
            document.getElementById("tab_remark").style.display = "";
            document.getElementById("tab_remark1").style.display = "";
        } else {
            document.getElementById("tab_remark").style.display = "none";
            document.getElementById("tab_remark1").style.display = "none";
        }
    }
    //清空数据
    function txtClr(valueId1, valueId2) {
        document.getElementById(valueId1).value = '';
        document.getElementById(valueId2).value = '';
    }
    function assSend(value, dealerId) {
        fm.action = "<%=contextPath%>/sales/storage/sendmanage/SendAssignment/addSendAssignmentInit.do?ORDER_ID=" + value + "&dealerId=" + dealerId;
        fm.submit();
    }
    //全选checkbox
    function myCheckBox(value, metaDate, record) {
        var thisAssNum = record.data.THIS_ASS_NUM;
        var sendType = record.data.SEND_TYPE;
        //var dAddId=record.data.DELIV_ADD_ID;//发运地址
        //var dealerId=record.data.DEALER_ID;//发运经销商
        if (thisAssNum > 0) {
            var str = "<input type='checkbox' id='groupIds' name='groupIds' value='" + value + "' />";
            str += "<input type='hidden' id='sendType' name='sendType' value='" + sendType + "' />";
            //str+="<input type='hidden' id='dd' name='dd' value='" + dAddId + "_"+dealerId+""' />";
            return String.format(str);
        } else {
            return String.format("<input type='checkbox' disabled='disabled'/>");
        }

    }
    //确定发运
    function retreat() {
        var b = 0;
        var arrayObj = new Array();
        arrayObj = document.getElementsByName("groupIds");
        var arrayObj1 = new Array();
        arrayObj1 = document.getElementsByName("sendType");
        //var arrayObj2 = new Array();
        //arrayObj2=document.getElementsByName("dd");
        var kk = 0;
        //var kkdd=0;
        var arrValue = 0;
        //var ddValue='0';//同一经销商是否是同一发运地址
        for (var i = 0; i < arrayObj.length; i++) {
            if (arrayObj[i].checked) {
                if (arrValue != 0) {
                    if (arrValue != arrayObj1[i].value) {
                        kk = 1;//自提单和普通单不能生成同一运单
                    }
                }
                //if(ddValue!='0'){
                //if(ddValue!=arrayObj2[i].value){
                //kkdd=1;//同一经销商不同地址不能生成同一派单
                //}
                //}
                arrValue = arrayObj1[i].value;
                b = 1;//有选中
            }
        }
        if (b == 0) {
            MyAlert("请选择发运分派的信息！");
            return;
        }
        if (kk == 1) {
            MyAlert("自提订单和常规订单不能生成同一张分派单！");
            return;
        }
        //if(kkdd==1){
        //	MyAlert("同一经销商不同发运地址不能生成同一派单");
        //return ;
        //}
        MyConfirm("确认发运分派！", sendAssignment);
    }

    function sendAssignment() {
        disabledButton(["saveButton"], true);
        makeNomalFormCall("<%=contextPath%>/sales/storage/sendmanage/SendAssignment/sendMain.json", sendAssignmentBack, 'fm', 'queryBtn');
    }

    function sendAssignmentBack(json) {
        if (json.returnValue == 1) {
            parent.MyAlert("操作成功！");
            fm.action = "<%=contextPath%>/sales/storage/sendmanage/SendAssignment/sendAssignmentInit.do";
            fm.submit();
        } else {
            disabledButton(["saveButton"], false);
            MyAlert("操作失败！请联系系统管理员！");
        }
    }
    function download_() {
        fm.action = "<%=contextPath%>/sales/storage/sendmanage/SendAssignment/sendAssignmentQuery.do?common=2";
        fm.submit();
    }
</script>
</body>
</html>
