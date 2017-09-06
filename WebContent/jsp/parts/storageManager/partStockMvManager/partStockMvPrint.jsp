<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>配件移位单</title>
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
        body, td {font-size: 16px;color: #000}
    </style>
    <script language="javascript">
        function changeToA4() {
            var mian_div = document.getElementsByName("mian_div");
            for (var i = 0; i < mian_div.length; i++) {
                mian_div[i].style.cssText = "width:210mm;height:297mm;";
            }
            //document.all.WebBrowser.ExecWB(6, 1);//打印
        }
        function printpreview() {
            document.all.WebBrowser.ExecWB(7, 1);// 打印页面预览
        }
        function printOrder() {
            var mian_div = document.getElementsByName("mian_div");
            for (var i = 0; i < mian_div.length; i++) {
                mian_div[i].style.cssText = "width:241mm;height:280mm;";
            }
            //document.all.WebBrowser.ExecWB(6, 1);//打印
        }
        function printpreviewToA4() {
            var mian_div = document.getElementsByName("mian_div");
            for (var i = 0; i < mian_div.length; i++) {
                mian_div[i].style.cssText = "width:210mm;height:297mm;";
            }
            //document.all.WebBrowser.ExecWB(7, 1);// 打印页面预览
        }

    </script>
</head>
<body style="text-align:center;margin:0px;padding:0px;">
<TABLE border=0 cellpadding=0 cellspacing=0 class="Noprint page-print-buttons" align="center" width=100%>
    <tr style="border: 0px;">
        <td style="border: 0px;">
            <input type=button class="txtToolBarButton" value="打印" data-before="printOrder">
            <input type=button class="txtToolBarButton" value="A4打印" data-before="changeToA4">
            <input type=button class="txtToolBarButton" value="预览">
            <input type=button class="txtToolBarButton" value="A4预览" data-before="printpreviewToA4">
        </td>
    </tr>
</TABLE>
<table width="100%">
    <tr>
        <td align="center"><h1>调拨单</h1></td>
    </tr>
</table>
<table class="main" cellpadding="0" cellspacing="0" border="0">
    <tr>
        <td class="titleName">移库组织：</td>
        <td class="titleContent" colspan="4">&nbsp;${main.ORG_NAME }</td>
        <td class="titleName">移库日期：</td>
        <td class="titleContent" colspan="4">&nbsp;${Date}</td>
    </tr>
    <tr>
        <td class="titleName">移出仓库：</td>
        <td class="titleContent" colspan="4">&nbsp;${main.WH_NAME }</td>
        <td class="titleName">移入仓库：</td>
        <td class="titleContent" colspan="4">&nbsp;${main.TOWH_NAME }</td>
    </tr>
     <tr>
        <td class="titleName">备注：</td>
        <td class="titleContent" colspan="9" style="text-align: left">${main.CHG_CODE}&nbsp;${main.REMARK}</td>
    </tr>
    <tr>
        <td class="list">序号</td>
        <td class="longList" colspan="2">配件编码</td>
        <td class="longList" colspan="2">配件名称</td>
        <td class="list">件号</td>
        <td class="list">移出库位</td>        
        <td class="list">移出数量</td>
        <td class="list">移入库位</td>
        <td class="list">移入数量</td>
    </tr>
    <c:forEach items="${list}" var="data" varStatus="status">
        <tr>
            <td class="list">${status.index+1 }</td>
            <td class="longList" colspan="2">&nbsp;${data.PART_OLDCODE }</td>
            <td class="longList" colspan="2">&nbsp;${data.PART_CNAME }</td>
            <td class="list">&nbsp;${data.PART_CODE }</td>
            <td class="list">&nbsp;${data.LOC_CODE }</td>
            <td class="list">&nbsp;${data.PRINT_QTY }</td>
            <td class="list">&nbsp;${data.TOLOC_CODE }</td>
            <td class="list">&nbsp;${data.PRINT_QTY }</td>
        </tr>
    </c:forEach>
    <c:if test="${listSize > 0}">
        <c:forEach begin="0" end="${listSize}">
            <tr>
                <td class="list">&nbsp;</td>
                <td class="longList" colspan="2">&nbsp;</td>
                <td class="longList" colspan="2">&nbsp;</td>
                <td class="list">&nbsp;</td>
                <td class="list">&nbsp;</td>
                <td class="list">&nbsp;</td>
                <td class="list">&nbsp;</td>
                <td class="list">&nbsp;</td>
            </tr>
        </c:forEach>
    </c:if>
    <tr>
        <td class="total" colspan="7">本单合计</td>
        <td class="list">${partSum }</td>
        <td class="list">&nbsp;</td>
        <td class="list">${partSum }</td>
    </tr>
</table>
<div>
    &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
    移出人： &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
    移入人： &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
    部门审核： &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
    制单人： &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
</div>
</body>
</html>