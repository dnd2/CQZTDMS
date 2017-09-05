<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>标准杂发单</title>
    <style media=print>
        .Noprint {
            display: none;
        }

        .p_next {
            page-break-after: always;
        }
    </style>
    <style type="text/css">

        .main {
            border-bottom: solid 1px #666666;
            border-right: solid 1px #666666;
            margin: 0 auto;
        }

        .main td {
            text-align: center;
        }

        .titleName, .titleContent, .list, .longList, .total {
            border-top: solid 1px #666666;
        }

        .titleName, .list, .longList, .total {
            border-left: solid 1px #666666;
        }

        .titleName {
            border-right: solid 1px #666666;
        }

        .titleName, .titleContent {
            padding: 7px 2px 7px 2px;
        }

        .list, .longList, .total {
            padding: 5px 5px 5px 5px;
        }

    </style>
    <script language="javascript">
        function changeToA4() {
            var mian_div = document.getElementsByName("mian_div");
            for (var i = 0; i < mian_div.length; i++) {
                mian_div[i].style.cssText = "width:210mm;height:297mm;";
            }
            document.all.WebBrowser.ExecWB(6, 1);//打印
        }
        function printpreview() {
            document.all.WebBrowser.ExecWB(7, 1);// 打印页面预览
        }
        function printOrder() {
            var mian_div = document.getElementsByName("mian_div");
            for (var i = 0; i < mian_div.length; i++) {
                mian_div[i].style.cssText = "width:241mm;height:280mm;";
            }
            document.all.WebBrowser.ExecWB(6, 1);//打印
        }
        function printpreviewToA4() {
            var mian_div = document.getElementsByName("mian_div");
            for (var i = 0; i < mian_div.length; i++) {
                mian_div[i].style.cssText = "width:210mm;height:297mm;";
            }
            document.all.WebBrowser.ExecWB(7, 1);// 打印页面预览
        }

    </script>
</head>
<body style="text-align:center;margin:0px;padding:0px;">
<TABLE border=0 cellpadding=0 cellspacing=0 class="Noprint" align="center" width=100%>
    <tr style="border: 0px;">
        <td style="border: 0px;">
            <OBJECT id="WebBrowser" classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" style="display:none"></OBJECT>
            <input type=button class="txtToolBarButton" value="打印" onClick="printOrder()">
            <input type=button class="txtToolBarButton" value="A4打印" onClick="changeToA4()">
            <input type=button class="txtToolBarButton" value="预览" onClick="printpreview()">
            <input type=button class="txtToolBarButton" value="A4预览" onClick="printpreviewToA4()">
        </td>
    </tr>
</TABLE>
<table width="100%">
    <tr>
        <td align="center"><h1>标准杂发单</h1></td>
    </tr>
</table>
<table class="main" cellpadding="0" cellspacing="0" border="0">
    <tr>
        <td class="titleName">出库组织：</td>
        <td class="titleContent" colspan="3">${dataMap.org_name}</td>
        <td class="titleName">出库日期：</td>
        <td class="titleContent" colspan="2">${dataMap.create_date}</td>
        <td class="titleName">出库单号：</td>
        <td class="titleContent" colspan="2">${dataMap.misc_order_code}</td>
    </tr>
    <tr>
        <td class="titleName">杂发部门：</td>
        <td class="titleContent" colspan="3">${dataMap.department_name }</td>
        <td class="titleName">出库类别：</td>
        <td class="titleContent" colspan="2">${dataMap.b_type }</td>
        <td class="titleName">出库仓库：</td>
        <td class="titleContent" colspan="2">${dataMap.wh_name }</td>
    </tr>
    <tr>
        <td class="list">备注：</td>
        <td class="titleContent" colspan="9">&nbsp;${dataMap.remark}</td>
    </tr>
    <tr>
        <td class="list">序号</td>
        <td class="longList" colspan="2">物料编码</td>
        <td class="longList" colspan="2">物料名称</td>
        <td class="longList" colspan="2">件号</td>
        <td class="list">单位</td>
        <td class="list">库位</td>
        <td class="list">数量</td>
    </tr>
    <c:forEach items="${miscList}" var="data">
        <tr>
            <td class="list">${data.indexNO }</td>
            <td class="longList" colspan="2">${data.PART_OLDCODE }</td>
            <td class="longList" colspan="2">${data.PART_CNAME}</td>
            <td class="longList" colspan="2">${data.PART_CODE }</td>
            <td class="list">${data.UNIT }</td>
            <td class="list">${data.LOC_CODE }</td>
            <td class="list">${data.IN_QTY }</td>
        </tr>
    </c:forEach>
    <c:if test="${listSize > 0}">
        <c:forEach begin="0" end="${listSize}">
            <tr>
                <td class="list">&nbsp;</td>
                <td class="longList" colspan="2">&nbsp;</td>
                <td class="longList" colspan="2">&nbsp;</td>
                <td class="longList" colspan="2">&nbsp;</td>
                <td class="list">&nbsp;</td>
                <td class="list">&nbsp;</td>
                <td class="list">&nbsp;</td>
            </tr>
        </c:forEach>
    </c:if>
    <tr>
        <td class="total" colspan="9">本单合计</td>
        <td class="list">${sumInQty.SUM_IN_QTY }</td>
    </tr>
</table>
<div>
    &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
    领料人： &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
    发料人： &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
    部门审核： &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
    制单人： &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
</div>
</body>
</html>