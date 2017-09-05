<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<head>
<title>装箱单打印</title>
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
    </style>
</head>
<script language="javascript">
    var idx = 0;
    function getIndex() {
        idx += 1
        document.write(idx);
    }
    function printOrder() {
        document.all.WebBrowser.ExecWB(6, 1);//打印
    }
    function printpreview() {
        document.all.WebBrowser.ExecWB(7, 1);// 打印页面预览
    }
</script>

<body style="margin: 0px;padding: 0px;text-align: center;">
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <OBJECT id=WebBrowser classid=CLSID:8856F961-340A-11D0-A96B-00C04FD705A2 style="display: none"></OBJECT>
    <input name="pickOrderId" id="pickOrderId" value="${dataMap.pickOrderId}" type="hidden"/>
    <div id="fot_div">
        <TABLE border=0 cellpadding=0 cellspacing=0 class="Noprint"
               class="center" width=100%>
            <tr style="border: 0px;">
                <td style="border: 0px;">
                    <input type=button id="printBtn" class="txtToolBarButton"
                           value="打印" onClick="printOrder()">
                    <input type=button id="printBtn2" class="txtToolBarButton"
                           value="预览" onClick="printpreview()">
                </td>
            </tr>
        </TABLE>
    </div>
    <c:forEach items="${dataMap.detailList}" var="data" varStatus="countter">
    <c:choose>
    <c:when test="${!(countter.last==true)}">
    <div id="mian_div" name="thisblock" class="p_next">
        </c:when>
        <c:otherwise>
        <div id="mian_div" name="thisblock">
            </c:otherwise>
            </c:choose>
            <div id="cen_mid_div">
                <div id="top_cen_mid_div">
                    <img src="<%=request.getContextPath()%>/img/bq_log1.gif" style="float: left;height: 55px;width: 250px"/>
                    <span style="font-size:40px;margin-left:-160px;font-weight:bold">装箱单</span>
                </div>
                <div id="but_cen_mid_div">
                    <table class="tableHead" width="100%">
                        <tr>
                            <td colspan="6" class="center" style="font-size: 18px;">装箱单抬头信息</td>
                        </tr>
                        <tr style=" height: 10mm; line-height: 10mm;">
                            <td>拣货单号</td>
                            <td class="center" style="font-size: 30px;">&nbsp;${dataMap.pickOrderId}</td>
                            <td> 订单号</td>
                            <td class="center">&nbsp;${dataMap.orderCode}</td>
                            <td>订单类型</td>
                            <td>&nbsp;${dataMap.orderType}</td>
                        </tr>
                        <tr>
                            <td>经销商代码</td>
                            <td >&nbsp;${dataMap.dealerCode}</td>
                            <td> 经销商名称</td>
                            <td colspan="3">&nbsp;${dataMap.dealerName}</td>
                        </tr>
                        <tr>
                            <td>发货工厂</td>
                            <td >&nbsp;${dataMap.companyName}</td>
                            <td> 收货地址</td>
                            <td colspan="3">&nbsp;${dataMap.addr}</td>
                        </tr>
                        <tr>
                            <td>装箱日期</td>
                            <td>&nbsp;${dataMap.date}</td>
                            <td>制表日期</td>
                            <td>&nbsp;${dataMap.createDate}</td>
                            <td>制表人</td>
                            <td>&nbsp;${dataMap.userName}</td>
                        </tr>
                    </table>
                    <table class="tableBody" width="100%" style="text-align: center;">
                        <tr>
                            <td colspan="8" class="center" style="font-size: 18px; border-top: 0px;">装箱单细节信息</td>
                        </tr>
                        <tr>
                            <td style="width: 35px;">序号</td>
                            <td style="width:100px;">箱号</td>
                            <td style="width:170px;">配件号</td>
                            <td>配件名称</td>
                            <td style="width: 50px;">数量</td>
                            <td style="width: 70px;">单价</td>
                            <td style="width: 70px;">金额</td>
                            <td style="width: 100px;">备注</td>
                        </tr>
                        <c:forEach items="${data}" var="data">
                            <tr style="text-align: center;">
                                <td>
                                    <script language="javascript">getIndex();</script>
                                </td>
                                <td>&nbsp;${data.PKG_NO}</td>
                                <td align="left">&nbsp;${data.PART_OLDCODE}</td>
                                <td align="left">&nbsp;${data.PART_CNAME}</td>
                                <td>&nbsp;${data.PKG_QTY}</td>
                                <td style="text-align: right;">${data.BUY_PRICE}&nbsp;</td>
                                <td style="text-align: right;">${data.PKG_AMOUNT}&nbsp;</td>
                                <td align="left">&nbsp;${data.REMARK}</td>
                            </tr>
                        </c:forEach>
                        <c:if test="${countter.last}">
                            <%
                                Object dtlListAct = request.getAttribute("dtlListAct");
                                int count = 0;
                                int cyclNum = 0;
                                int fyNum = 20;
                                if (null != dtlListAct && 0 != Integer.parseInt(dtlListAct.toString())) {
                                    cyclNum = fyNum - Integer.parseInt(dtlListAct.toString());
                                }
                                for (int i = 0; i < cyclNum; i++) {
                            %>
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
                            <% } %>
                        </c:if>
                    </table>
                    <table class="tableBottom" width="100%" style="border-top: 0px;border-left: 0px">
                        <tr style="height: 10mm; line-height: 10mm;  text-align: right;">
                            <td style="border: 0px;">系统员：___________________</td>
                        </tr>
                        <tr style="height: 10mm; line-height: 10mm;  text-align: right;">
                            <td style="border: 0px;">装箱员：___________________</td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
        </c:forEach>
</form>
</body>
</html>
