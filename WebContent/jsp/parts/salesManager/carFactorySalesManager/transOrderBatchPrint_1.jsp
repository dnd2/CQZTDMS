<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<title>批量打印发运单</title>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<style media=print>
.Noprint {
    display: none;
}
.p_next {
    page-break-after: always;
}
#but_cen_mid_div {
	width: 100%;
	margin: 0px;
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
        body, td{font-size: 16px;color:#000}
    </style>
    <script language="javascript">
        var printStyle = "width:241mm;height:280mm;";
        var printStyle4A4 = "width:210mm;height:297mm;";
        var addrStyle = "text-align: left;width: 400px;line-height: 15px;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;";
        var addrStyle4A4 = "text-align: left;width: 300px;line-height: 15px;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;";
        var phoStyle = "text-align: left;width: 210px;line-height: 15px;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;";
        var phoStyle4A4 = "text-align: left;width: 150px;line-height: 15px;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;";

        function changeStyle(obj, style) {
            var mian_div = document.getElementsByName(obj);
            for (var i = 0; i < mian_div.length; i++) {
                mian_div[i].style.cssText = style;
            }
        }
        function changeToA4Style() {
            changeStyle("mian_div", printStyle4A4);
            changeStyle("addr", addrStyle4A4);
            changeStyle("tel", phoStyle4A4);
        }
        function changeDefaultStyle() {
            changeStyle("mian_div", printStyle);
            changeStyle("addr", addrStyle);
            changeStyle("tel", phoStyle);
        }

        function changeToA4() {
            changeStyle("mian_div", printStyle4A4);
            changeStyle("addr", addrStyle4A4);
            changeStyle("tel", phoStyle4A4);
            document.all.WebBrowser.ExecWB(6, 1);//打印
        }
        function printpreview() {
            changeStyle("mian_div", printStyle);
            changeStyle("addr", addrStyle);
            changeStyle("tel", phoStyle);
            document.all.WebBrowser.ExecWB(7, 1);// 打印页面预览
        }
        function printOrder() {
            changeStyle("mian_div", printStyle);
            changeStyle("addr", addrStyle);
            changeStyle("tel", phoStyle);
            document.all.WebBrowser.ExecWB(6, 1);//打印
        }
        function printpreviewToA4() {
            changeStyle("mian_div", printStyle4A4);
            changeStyle("addr", addrStyle4A4);
            changeStyle("tel", phoStyle4A4);
            document.all.WebBrowser.ExecWB(7, 1);// 打印页面预览
        }
    </script>
</head>
<body style="text-align:center;margin:0px;padding:0px;">
<TABLE border=0 cellpadding=0 cellspacing=0 class="Noprint left page-print-buttons" width=100%>
    <tr style="border: 0px;">
        <td style="border: 0px;">
            <OBJECT id="WebBrowser" classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" style="display:none"></OBJECT>
            <input type=button class="txtToolBarButton" value="打印" data-before="changeDefaultStyle">
            <input type=button class="txtToolBarButton" value="A4打印" data-before="changeToA4Style">
            <input type=button class="txtToolBarButton" value="预览" data-before="changeDefaultStyle">
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
                <img src="<%=request.getContextPath()%>/img/bq_log1.gif" style="float: left;;height: 55px;width: 250px"/>
                <span style="font-size:40px;margin-left:-185px;font-weight:bold">配件发运单</span>
            </div>
            <div id="but_cen_mid_div">
            <table class="tableHead" width="100%" style="text-align: left;">
                <tr>
                    <td colspan="6" align="center" style="font-size: 18px;"> 经销商信息</td>
                </tr>
                <tr>
                    <td>发运单号</td>
                    <td class="left" style="font-size: 30px; "> &nbsp;${tableData.transCode} </td>
                    <td>订单号</td>
                    <td>
                        <div style="width:175px; overflow: hidden; white-space: nowrap; text-overflow:ellipsis;">
                            &nbsp;${tableData.orderCode}(${tableData.orderType})</div>
                    </td>
                    <td>运单类型</td>
                    <td>${tableData.transType}(${tableData.transportOrg})</td>
                </tr>
                </tr>
                <tr>
                    <td  width="85px;">经销商代码</td>
                    <td>&nbsp;${tableData.dealerCode}</td>
                    <td width="85px;">经销商名称</td>
                    <td colspan="3" class="left">&nbsp;${tableData.dealerName}</td>
                </tr>
                <tr>
                    <td>填表日期</td>
                    <td>&nbsp;${tableData.createDate}</td>
                    <td>发运日期</td>
                    <td>&nbsp;${tableData.driveDate}</td>
                    <td width="105px;">预计到货时间</td>
                    <td>&nbsp;${tableData.recvDate}</td>
                </tr>
                <tr>
                    <td>发运地址</td>
                    <td colspan="3" align="left">&nbsp;${tableData.companyName}</td>
                    <td>联系人</td>
                    <td>&nbsp;${tableData.linkName}</td>
                </tr>
                <tr>
                    <td>收货地址</td>
                    <td colspan="3">
                        <div id="addr" name="addr">&nbsp;${tableData.addr}</div>
                    </td>
                    <td>联系电话</td>
                    <td>
                        <div id="tel" name="tel">&nbsp;${tableData.phone}</div>
                    </td>
                </tr>
            </table>
            <table class="tableBody" width="100%">
                <tr>
                    <td colspan="10" class="left" style="font-size: 18px;border-top: 0px;">运单基本信息</td>
                </tr>
                <tr style="text-align: center;">
                    <td style="width: 10mm;" rowspan="2">序号</td>
                    <td rowspan="2">箱号</td>
                    <td style="width: 60mm;" colspan="3">规格（单位：cm）</td>
                    <td style="width: 20mm;" rowspan="2">体积（单位：m³）</td>
                    <td style="width: 20mm;" rowspan="2">重量（KG）</td>
                    <td style="width: 20mm;" rowspan="2">折合重量（KG）</td>
                    <td style="width: 20mm;" rowspan="2">计费重量（KG）</td>
                    <td style="width: 30mm;" rowspan="2">备注</td>
                </tr>
                <tr>
                    <td style="width: 20mm;">长</td>
                    <td style="width: 20mm;">宽</td>
                    <td style="width: 20mm;">高</td>
                </tr>
                <c:forEach items="${data}" var="dataDetail">
                    <tr>
                        <td>&nbsp;${dataDetail.indexNO}</td>
                        <td>&nbsp;${dataDetail.PKG_NO}</td>
                        <td>&nbsp;${dataDetail.LENGTH}</td>
                        <td>&nbsp;${dataDetail.WIDTH}</td>
                        <td>&nbsp;${dataDetail.HEIGHT}</td>
                        <td>&nbsp;${dataDetail.VOLUME}</td>
                        <td>&nbsp;${dataDetail.WEIGHT}</td>
                        <td>&nbsp;${dataDetail.EQ_WEIGHT}</td>
                        <td>&nbsp;${dataDetail.CH_WEIGHT}</td>
                        <td>&nbsp;${dataDetail.REMARK}</td>
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
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                        </tr>
                    </c:forEach>
                </c:if>
                <tr style="height: 12mm; line-height: 12mm;font-weight: bold;">
                    <td colspan="2">第${count.count}页/共${tableData.listAcount}页</td>
                    <td>合计</td>
                    <td colspan="2">${tableData.boxNum}箱</td>
                    <td>${tableData.volume}m³</td>
                    <td>${tableData.weight}KG</td>
                    <td>${tableData.eqWeight}KG</td>
                    <td>${tableData.chWeight}KG</td>
                    <td>&nbsp;</td>
                </tr>
            </table>
            <table class="tableBody" width="100%" style="heigth:30mm;">
                <tr style="height: 5mm;  text-align: center;">
                    <td colspan="8" style="border-top: 0px;">《物流公司收货确认栏》</td>
                </tr>
                <tr style="height: 10mm; text-align: left;">
                    <td colspan="4" width="60%">&nbsp;包装是否全部完好：（是）（否）</td>
                    <td colspan="4">&nbsp;包装破损的箱号：</td>
                </tr>
                <tr style="height: 15mm; text-align: center;">
                    <td width="5%">物流公司签字</td>
                    <td width="20%" colspan="2">&nbsp;</td>
                    <td width="8%">仓库方签字</td>
                    <td width="20%" colspan="2" >&nbsp;</td>
<!--                     <td width="10%">北汽银翔整车物流签字</td> -->
<!--                     <td width="20%">&nbsp;</td> -->
                    <td width="10%">签字日期：</td>
                    <td width="13%">&nbsp;</td>
                </tr>
            </table>
            <table class="tableBody" width="100%" style="height:30mm;">
                <tr style="height: 5mm;  text-align: center;">
                    <td colspan="6" style="border-top: 0px;">《经销店收货确认栏》</td>
                </tr>
                <tr style="height: 10mm; text-align: left;">
                    <td colspan="4" width="60%">&nbsp;包装是否全部完好：（是）（否）</td>
                    <td colspan="2">&nbsp;包装破损的箱号：</td>
                </tr>
                <tr style="height: 15mm; text-align: center;">
                    <td width="10%">经销店签字</td>
                    <td width="13%">&nbsp;</td>
                    <td width="10%">物流公司签字</td>
                    <td width="10%">&nbsp;</td>
                    <td width="30%" style="text-align: left;">&nbsp;签字日期： 年 月 日</td>
                    <td width="13%">&nbsp;</td>
                </tr>
            </table>
            </div>
        </div>
    </div>
    </c:forEach>
    </c:forEach>
</body>
</html>