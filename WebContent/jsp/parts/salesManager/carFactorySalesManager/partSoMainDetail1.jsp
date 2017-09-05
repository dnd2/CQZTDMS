<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
    String contextPath = request.getContextPath();
%>
<html>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>销售快报</title>
</head>
<script type="text/javascript">
    function doInit() {
        loadcalendar();  //初始化时间控件
    }
</script>
<body onload="__extQuery__(1);">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：配件管理 &gt; 配件销售管理 &gt; 配件销售单 &gt; 销售快报</div>
<form method="post" name="fm" id="fm">

    <table class="table_query" border="0">
        <tr>
            <td align="right">销售日期:</td>
            <td><input name="SstartDate" type="text" class="short_time_txt"
                       id="SstartDate" value="${SstartDate}"/>
                <input name="button" value=" " type="button" class="time_ico"
                       onclick="showcalendar(event, 'SstartDate', false);"/>
                至
                <input name="SendDate" type="text" class="short_time_txt" id="SendDate"
                       value="${SendDate}"/>
                <input name="button" value=" " type="button" class="time_ico"
                       onclick="showcalendar(event, 'SendDate', false);"/></td>
        </tr>
        <tr>
            <td colspan="2" align="center">
                <input type="button" onclick="__extQuery__(1);" class="normal_btn" value="查询" name="BtnQuery"
                       id="queryBtn"/>
                <input type="button" onclick="_hide();" class="normal_btn" value="关闭" name="BtnClose" id="BtnClose"/>
                 <font color="red"></font>
            </td>
        </tr>
    </table>
    <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
    <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    <div>
        <font color="red" size="+1">
            Note:</br>
            1、本快报中的金额全部是指无税金额;且包含直发油品销售数据;</br>
            2、本快报相关数据是以合格销售单为依据关联处于各个状态的销售单数据，和实际的出库数据存在一定误差，误差主要产生在现场BO、直发油品、以及统计尺度(如销售日期和出库日期)的不一致;</br>
            3、财务审核金额=已出库金额+待出库金额;</br>
            4、已出库金额=已开票金额+待开票金额;</br>
            5、已出库金额包含直发油品服务商点入库金额;</br>
            6、待出库金额包含直发油品服务商未点入库金额;</br>
            <%--5、待开票金额=包括常规、紧急订单已出库待开票金额+直发订单待确认金额+强制出库金额</br>--%>
        </font>
    </div>
</form>
<script type="text/javascript">
    var flag = true;

    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartSoManage/queryMakersDialog.json";

    var title = null;

    var columns = [
        {header: '序号', align: 'center', renderer: getIndex},
        {header: "合格销售单金额", sortable: false, dataIndex: 'XS_AMOUNT', align: 'center'},
        {header: "财务审核金额", sortable: false, dataIndex: 'CW_AMOUNT', align: 'center'},
        {header: "已出库金额", sortable: false, dataIndex: 'CK_AMOUNT', align: 'center'},
        {header: "待出库金额", sortable: false, dataIndex: 'DCK_AMOUNT', align: 'center'},
        {header: "已开票金额", sortable: false, dataIndex: 'INVO_AMOUNT', align: 'center'},
        {header: "待开票金额", sortable: false, dataIndex: 'NOINVO_AMOUNT', align: 'center'}
       /* {header: "常规出库待开票", sortable: false, dataIndex: 'CG_NOINVOAMOUNT', align: 'center'},
        {header: "直发待确认", sortable: false, dataIndex: 'NOINVO_AMOUNT', align: 'center'},
        {header: "强制关闭金额", sortable: false, dataIndex: 'NOINVO_AMOUNT', align: 'center'},*/

    ];


    function setMainPartCode(v1) {
        //调用父页面方法
        var v1 = v1;
        if (!v1) return;
        if (flag) {
            for (var i = 0; i < v1.length; i++) {
                MyAlert("checked: " + v1);
                v[i].checked = true;
            }
            flag = false;
        } else if (!flag) {
            for (var i = 0; i < v1.length; i++) {
                MyAlert("unchecked: " + v1);
                v1[i].checked = false;
            }
            flag = true;
        }
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