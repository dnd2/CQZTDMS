<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=7">
    <title>服务商返利导入</title>
</head>
<body onload="autoAlertException();">
<div class=navigation><img src="<%=request.getContextPath()%>/img/nav.gif">当前位置&gt;财务管理&gt;返利管理&gt;配件季度返利导入</div>
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <table class="table_query">
        <tr>
            <td class="table_query_label" colspan="2">1、请选择要导入的年和季度：
                <select name='year' id="year" onchange="createMonthOptions();" class="table_query_label">
                    <%
                        String year = (String) request.getAttribute("curYear");
                        if (null == year || "".equals(year)) {
                            year = "0";
                        }
                        int y = Integer.parseInt(year);
                    %>
                    <option value="<%=y-1 %>"><%=y - 1 %>
                    </option>
                    <option selected value="<%=y %>"><%=y %>
                    </option>
                    <option value="<%=y+1 %>"><%=y + 1 %>
                    </option>
                </select>
                <select name='month' id="month" class="table_query_label">
                </select></td>
        </tr>
        <tr>
            <td class="table_query_label" colspan="2">
                2、点“<font color="#FF0000">浏览</font>”按钮，找到您所要导入的服务商季度返利文件,请确定文件的格式为“<strong style="color: red">服务商代码—返利类型代码-返利金额</strong>”：&nbsp;&nbsp;&nbsp;
            </td>
        </tr>
        <tr>
            <td class="table_query_input" colspan="2">&nbsp;&nbsp;&nbsp;&nbsp;
                <input type="file" name="uploadFile" id="uploadFile" style="width: 250px" value=""/></td>
        </tr>
        <tr>
            <td class="table_query_label" width="30%">3、选择好文件后, 点“<font color="#FF0000">确定</font>”按钮完成粘贴。</td>
        </tr>
        <tr>
            <td class="table_query_label" width="30%">4、注意：重复导入时后面导入的数据将会覆盖先前导入的数据。</td>
            <td class="table_query_label" align="left" width="56%">
                <input type="button" class="cssbutton" name="queryBtn" value="确定" onclick="upload();"/>
                <input type="button" class="long_btn" name="downloadBtn" value="导入模板下载" onclick="downloadFile();"/></td>
        </tr>
    </table>
</form>
</div>
<script type="text/javascript">

    function upload() {
        var filevalue = fm.uploadFile.value;
        if (filevalue == '') {
            MyAlert('导入文件不能空!');
            return false;
        }
        var fi = filevalue.substring(filevalue.length - 3, filevalue.length);
        if (fi != 'xls') {
            MyAlert('导入文件格式不对,请导入xls文件格式');
            return false;
        }
        if (confirm("确认上传?")) {
            $('fm').action = "<%=contextPath%>/parts/financeManager/dealerRateManager/RebateManager/monthPlanUpload.do";
            $("fm").submit();
        }
    }
    //下载模板
    function downloadFile() {
        fm.action = '<%=contextPath%>/parts/financeManager/dealerRateManager/RebateManager/download.do';
        fm.submit();
    }

    function doInit() {
        createMonthOptions();
    }
    //创建月份OPTION
    function createMonthOptions() {
        var curyear =${curYear};
        var year = document.getElementById("year").value;
        var month =${curMonth};
        var obj = document.getElementById("month");
        clrOptions(obj);
        if (year != curyear) {
            month = 1;
        }
        for (var i = 1; i < 5; i++) {
            var opt = document.createElement("option");
            opt.value = i;
            opt.appendChild(document.createTextNode(i));
            obj.appendChild(opt);
        }
    }
    //清空月份OPTION
    function clrOptions(obj) {
        obj.options.length = 0;
    }
</script>
</body>
</html>
