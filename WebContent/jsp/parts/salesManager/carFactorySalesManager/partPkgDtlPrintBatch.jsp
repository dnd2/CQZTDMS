<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>装箱单批量打印</title>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
    <style media=print>
        .Noprint {
            display: none;
        }

        .p_next {
            page-break-after: always;
        }
    </style>
    <style type="text/css">
        .tableHead {
            border-collapse: collapse;
            border-spacing: 0px;
        }

        .tableHead td {
            height: 8mm;
            line-height: 8mm;
            /*border: 1px solid black;*/
        }

        .tableBody {
            border-collapse: collapse;
            border-spacing: 0px;
            border-top: 0px;
            border-bottom: 1px dashed #888;
        }

        .tableBody td {
            white-space: nowrap;
            height: 7mm;
            line-height: 7mm;
            /*border: 1px solid black;*/
        }

        .tableBottom td {
            border-right: 0px;
            border-bottom: 0px;
        }

        #mian_div {
            width: 241mm;
            height: 280mm;
            margin: 0 auto;
            padding: 10mm 15mm 10mm 15mm;
            border: 0px solid blue;
        }

        #cen_mid_div {
            width: 100%;
            height: 100%;
            border: 0px solid red;
        }

        #top_cen_mid_div {
            width: 100%;
            height: 54px;
            margin: 0px;
            text-align: center;
            position: relative
        }

        #top_cen_mid_div span {
            position: absolute;
            bottom: 0px;
            padding: 0px;
            margin: 0px;
            text-align: center;
        }

        table {
            border-collapse: collapse;
            border-spacing: 0;
            border-left: 1px dashed #888;
            border-top: 1px dashed #888;
            /*background: #efefef;*/
        }

        th, td {
            border-right: 1px dashed #888;
            border-bottom: 1px dashed #888;
            /*padding: 5px 15px;*/
        }

        th {
            font-weight: bold;
            /*background: #ccc;*/
        }
        body, td {font-size: 16px}
    </style>
    <script language="javascript">
        var printStyle = "width:241mm;height:280mm;";
        var printStyle4A4 = "width:210mm;height:297mm;";
        var addrStyle = "text-align: left;width: 380px;line-height: 15px;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;";
        var addrStyle4A4 = "text-align: left;width: 300px;line-height: 15px;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;";
        //        var phoStyle = "text-align: left;width: 150px;line-height: 15px;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;";
        //        var phoStyle4A4 = "text-align: left;width: 100px;line-height: 15px;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;";

        function changeStyle(obj, style) {
            var mian_div = document.getElementsByName(obj);
            for (var i = 0; i < mian_div.length; i++) {
                mian_div[i].style.cssText = style;
            }
        }
        function changeToA4Style() {
            changeStyle("mian_div", printStyle4A4);
            changeStyle("addr", addrStyle4A4);
        }
        function changeDefaultStyle() {
            changeStyle("mian_div", printStyle);
            changeStyle("addr", addrStyle);
        }

        function changeToA4() {
            changeStyle("mian_div", printStyle4A4);
            changeStyle("addr", addrStyle4A4);
//            changeStyle("tel", phoStyle4A4);
            document.all.WebBrowser.ExecWB(6, 1);//打印
        }
        function printpreview() {
            changeStyle("mian_div", printStyle);
            changeStyle("addr", addrStyle);
//            changeStyle("tel", phoStyle);
            document.all.WebBrowser.ExecWB(7, 1);// 打印页面预览
        }
        function printOrder() {
            changeStyle("mian_div", printStyle);
            changeStyle("addr", addrStyle);
//            changeStyle("tel", phoStyle);
            document.all.WebBrowser.ExecWB(6, 1);//打印
        }
        function printpreviewToA4() {
            changeStyle("mian_div", printStyle4A4);
            changeStyle("addr", addrStyle4A4);
//            changeStyle("tel", phoStyle4A4);
            document.all.WebBrowser.ExecWB(7, 1);// 打印页面预览
        }
    </script>
</head>
<body style="text-align:center;margin:0px;padding:0px;font-size:16px;color:#000">
<TABLE border=0 cellpadding=0 cellspacing=0 class="Noprint center page-print-buttons" width=100%>
    <tr style="border: 0px;">
        <td style="border: 0px;">
            <input type=button class="txtToolBarButton" value="打印" data-before="changeDefaultStyle">
            <input type=button class="txtToolBarButton" value="A4打印"  data-before="changeToA4Style">
            <input type=button class="txtToolBarButton" value="预览"  data-before="changeDefaultStyle">
            <input type=button class="txtToolBarButton" value="A4预览" data-before="changeToA4Style">
        </td>
    </tr>
</TABLE>
<c:forEach items="${batchDatas}" var="tableData" varStatus="countAll">
<c:forEach items="${tableData.detailList}" var="data" varStatus="count">
<c:choose>
<c:when test="${!(countAll.last==true && count.last==true)}">
<div id="mian_div" name="mian_div" class="p_next">
    </c:when>
    <c:otherwise>
    <div id="mian_div" name="mian_div">
        </c:otherwise>
        </c:choose>
        <div id="cen_mid_div" name="cen_mid_div">
            <div id="top_cen_mid_div">
                <img src="<%=request.getContextPath()%>/img/bq_log1.gif" style="float: left;height: 55px;width: 250px;margin-bottom:5px"/>
                <span style="font-size:40px;margin-left:-160px;font-weight:bold">装箱单</span>
            </div>
            <table class="tableHead" width="100%">
                <tr>
                    <td colspan="6" class="center" style="font-size: 18px;">装箱单抬头信息</td>
                </tr>
                <tr style="height: 10mm; line-height: 10mm;">
                    <td>拣货单号</td>
                    <td class="center" style="font-size: 30px; ">&nbsp;${tableData.pickOrderId}</td>
                    <td>订单号</td>
                    <td class="center"> &nbsp;${tableData.orderCode}</td>
                    <td>订单类型</td>
                    <td>&nbsp;${tableData.orderType}</td>
                </tr>
                <tr>
                    <td>经销商代码</td>
                    <td >&nbsp;${tableData.dealerCode}</td>
                    <td>经销商名称</td>
                    <td colspan="3" style="text-align: center;">&nbsp;${tableData.dealerName}</td>
                </tr>
                <tr>
                    <td>发货工厂</td>
                    <td style="text-align: center;">&nbsp;${tableData.companyName}</td>
                    <td>收货地址</td>
                    <td colspan="3"
                    <div id="addr" name="addr"
                         style="text-align: left;width: 380px;line-height: 15px;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;">
                        &nbsp;${tableData.addr}</div>
                    </td>
                </tr>
                <tr>
                    <td>装箱日期</td>
                    <td>&nbsp;${tableData.date}</td>
                    <td>制表日期</td>
                    <td>&nbsp;${tableData.createDate}</td>
                    <td>制表人</td>
                    <td>&nbsp;${tableData.creater}</td>
                </tr>
            </table>
            <table class="tableBody" width="100%" style="border-top: 0px;">
                <tr>
                    <td colspan="8" class="center" style="font-size: 18px; border-top: 0px;"> 装箱单细节信息</td>
                </tr>
                <tr style="text-align: center;">
                    <td style="width: 10mm;">序号</td>
                    <td style="width: 30mm;">箱号</td>
                    <td style="width: 40mm;">配件号</td>
                    <td style="">配件名称</td>
                    <td style="width: 10mm;">数量</td>
                    <td style="width: 15mm;">单价</td>
                    <td style="width: 20mm;">金额</td>
                    <td style="width: 30mm;">备注</td>
                </tr>
                <c:forEach items="${data}" var="dataDetail">
                    <tr style="text-align: center;">
                        <td>&nbsp;${dataDetail.indexNO}</td>
                        <td>&nbsp;${dataDetail.PKG_NO}</td>
                        <td align="left">&nbsp;${dataDetail.PART_OLDCODE}</td>
                        <td align="left">&nbsp;${dataDetail.PART_CNAME}</td>
                        <td>&nbsp;${dataDetail.PKG_QTY}</td>
                        <td style="text-align: right;">${dataDetail.BUY_PRICE}&nbsp;</td>
                        <td style="text-align: right;">${dataDetail.PKG_AMOUNT}&nbsp;</td>
                        <td align="left">&nbsp;${dataDetail.REMARK}</td>
                    </tr>
                </c:forEach>
                <c:if test="${count.last}">
                    <c:forEach items="${tableData.dtlListAct}" var="data2">
                        <tr>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                        </tr>
                    </c:forEach>
                </c:if>
<!--                 <tr> -->
<!--                     <td style="width: 10mm;">合计</td> -->
<!--                     <td style="width: 30mm;"></td> -->
<!--                     <td style="width: 40mm;"></td> -->
<!--                     <td style=""></td> -->
<%--                     <td style="width: 10mm;text-align: right;">${tableData.SUM_NUM}</td> --%>
<!--                     <td style="width: 15mm;"></td> -->
<%--                     <td style="width: 20mm;text-align: right;">${tableData.SUM_MONEY}</td> --%>
<!--                     <td style="width: 30mm;"></td> -->
<!--                 </tr> -->
            </table>
            <table class="tableBottom" width="100%" style="border-top: 0px;border-left: 0px">
                <tr style="height: 10mm; line-height: 10mm; text-align: right;">
                    <td align="right" colspan="9">系统员：___________________</td>
                </tr>
                <tr style="height: 10mm; line-height: 10mm; text-align: right;">
                    <td align="right" colspan="9">装箱员：___________________</td>
                </tr>
            </table>
        </div>
    </div>
    </c:forEach>
    </c:forEach>
</body>
</html>