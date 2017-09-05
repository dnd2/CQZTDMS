<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
    <title>入库单打印</title>
    <style media=print>
        .noprint {
            display: none;
        }
    </style>
    <style type="text/css">
        html, body {
            font-size: 12px;
            margin: 0px;
            height: 100%;
        }
    </style>
</head>
<script language="javascript">
    //获取选择框的值
    function getCode(value) {
        var str = getItemValue(value);
        document.write(str);
    }
    function getIndex() {
        document.write(document.getElementById("file").rows.length - 1);
    }
    function printOrder() {
        document.all.WebBrowser.ExecWB(6, 1);//打印
    }
    function printpreview() {
        document.all.WebBrowser.ExecWB(7, 1);// 打印页面预览
    }
</script>
</head>
<body>
<form name="fm" id="fm">
    <OBJECT id="WebBrowser" classid=CLSID:8856F961-340A-11D0-A96B-00C04FD705A2 style="display: none"></OBJECT>

    <TABLE border=0 bordercolor=black cellpadding=3 cellspacing=0 width="90%"
           style="margin-top: 15px;margin-bottom: 15px">
        <br>
        <center><font size="+1"><b>
            入库清单
        </b></font></center>
    </TABLE>
    <table border=0 cellpadding=3 align="center" cellspacing=0 width="91%"
           style="margin-top: 15px;margin-bottom: 15px">
        <tr>
            <td width="28%">
                单号：&nbsp;${mainMap.IN_CODE}
            </td>

            <td width="40%">
                购货单位：&nbsp;${mainMap.DEALER_NAME}
            </td>

            <td width="25%">
                制单时间：&nbsp;${mainMap.date}
            </td>

        </tr>
    </table>
    <TABLE id="file" border=1 bordercolor=black cellpadding=3 align="center" cellspacing=0 width="91%">
        <tr height="18px">
            <td width=3% style="text-align: center" colspan=1>序号</td>
            <td width=15% style="text-align: center" colspan=1>配件编码</td>
            <td width=10% style="text-align: center" colspan=1> 配件名称</td>
            <td width=10% style="text-align: center" colspan=1> 件号</td>
            <td width=10% style="text-align: center" colspan=1> 数量</td>
            <td width=5% style="text-align: center" colspan=1> 单位</td>
        </tr>
        <c:forEach items="${inList}" var="data">
            <tr>
                <td style="text-align: center">
                    <script language="javascript">
                        getIndex()
                    </script>
                </td>
                <td style="text-align: center">
                    &nbsp;${data.PART_OLDCODE}
                </td>
                <td style="text-align: center">
                    &nbsp;${data.PART_CNAME}
                </td>
                <td style="text-align: center">
                    &nbsp;${data.PART_CODE}
                </td>
                <td style="text-align: center">
                    <font size="4">
                        &nbsp;${data.IN_QTY}
                    </font>
                </td>
                <td style="text-align: center">
                    &nbsp;${data.UNIT}
                </td>
            </tr>
        </c:forEach>

    </TABLE>
    <TABLE id="print" border="0" align="center" cellspacing="0" width="91%">
        <tr>
            <td style="border: 0px;" colspan="6" align="center">
                <input type=button id="printBtn" class="noprint" value="打印" onClick="printOrder()">
                <input type=button id="printBtn2" class="noprint" value="预览" onClick="printpreview()">
            </td>
        </tr>
    </TABLE>
</form>
</body>
</html>