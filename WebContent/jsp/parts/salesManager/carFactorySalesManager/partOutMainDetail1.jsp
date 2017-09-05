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
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置：配件管理 &gt; 配件销售管理 &gt; 配件出库单 &gt; 出库快报</div>
<form method="post" name="fm" id="fm">
    <table class="table_query" border="0">
        <tr>
            <td align="right">日期:</td>
            <td><input name="SstartDate" type="text" class="short_time_txt"
                       id="SstartDate" value="${SstartDate}"/>
                <input name="button" value=" " type="button" class="time_ico"
                       onclick="showcalendar(event, 'SstartDate', false);"/>
                至
                <input name="SendDate" type="text" class="short_time_txt" id="SendDate"
                       value="${SendDate}"/>
                <input name="button" value=" " type="button" class="time_ico"
                       onclick="showcalendar(event, 'SendDate', false);"/></td>
            <td >
                <input type="button" onclick="__extQuery__(1);" class="normal_btn" value="查询" name="BtnQuery" id="queryBtn"/>
                <input type="button" onclick="_hide();" class="normal_btn" value="关闭" name="BtnClose" id="BtnClose"/>
            </td>
        </tr>
    </table>

    <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
    <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
</form>
<div>
    <font color="red" size="+1">
        NOTE:</br>
        1、本快报所有金额为无税金额;</br>
        2、本快报中待出库金额、未合并金额、装箱中金额、装箱中金额、已装箱金额是以销售单销售日期进行统计;</br>
        3、本快报中已出库金额、已收取运费、强制出库金额是以出库单出库日期进行统计;</br>
        4、待出库金额=未合并金额+装箱中金额+已装箱金额;</br>
        5、已出库金额中已经包含已收取运费;</br>
    </font>

</div>
<script type="text/javascript">
    var flag = true;

    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartOutstock/queryOutRepoDtl.json";

    var title = null;

    var columns = [
        {header: '序号', align: 'center', renderer: getIndex},
        {header: "待出库金额", sortable: false, dataIndex: 'DCK_AMOUNT', align: 'center'},
        {header: "未合并金额", sortable: false, dataIndex: 'WHB_AMOUNT', align: 'center'},
        {header: "装箱中金额", sortable: false, dataIndex: 'ZXZ_AMOUNT', align: 'center'},
        {header: "已装箱金额", sortable: false, dataIndex: 'YZX_AMOUNT', align: 'center'},
        {header: "已出库金额", sortable: false, dataIndex: 'CK_AMOUNT', align: 'center'},
        {header: "已收取运费", sortable: false, dataIndex: 'YF_AMOUNT', align: 'center'},
        {header: "强制出库金额", sortable: false, dataIndex: 'QZGB_AMOUNT', align: 'center'}

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