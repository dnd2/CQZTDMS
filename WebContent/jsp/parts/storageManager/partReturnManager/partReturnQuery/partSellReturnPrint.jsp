<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib prefix="fmt" uri="/jstl/fmt" %>
<head>
    <title>退货单</title>
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
            border-bottom: 0px;
        }

        .tableHead td {
            height: 10mm;
            line-height: 10mm;
            border: 1px solid black;
        }

        .tableBody {
            border-collapse: collapse;
            border-spacing: 0px;
        }

        .tableBody td {
            white-space: nowrap;
            word-break: nowrap;
            height: 8mm;
            line-height: 8mm;
            border: 1px solid black;
        }

        #mian_div {
            width: 241mm;
            height: 280mm;
            margin: 0 auto;
            padding: 10mm 15mm 10mm 15mm;
            border: 0px solid blue;
        }
        body, td{font-size: 16px;color:#000}
        table caption{margin-bottom: 5px;padding:0;border:0;background-color:transparent;font-weight: normal;background-image: none;text-align:center}

    </style>
</head>

<script language="javascript">
    //获取选择框的值
    function getCode(value) {
        var str = getItemValue(value);

        document.write(str);
    }
    var idx = 0;
    function getIndex() {
        idx += 1
        document.write(idx);
    }

    function changeToA4() {
        var mian_div = document.getElementsByName("mian_div");
        for (var i = 0; i < mian_div.length; i++) {
            mian_div[i].style.cssText = "width:210mm;height:297mm;";
        }
       // document.all.WebBrowser.ExecWB(6, 1);//打印
    }
    function printpreview() {
        document.all.WebBrowser.ExecWB(7, 1);// 打印页面预览
    }
    function printOrder() {
        var mian_div = document.getElementsByName("mian_div");
        for (var i = 0; i < mian_div.length; i++) {
            mian_div[i].style.cssText = "width:241mm;height:280mm;";
        }
       // document.all.WebBrowser.ExecWB(6, 1);//打印
    }
    function printpreviewToA4() {
        var mian_div = document.getElementsByName("mian_div");
        for (var i = 0; i < mian_div.length; i++) {
            mian_div[i].style.cssText = "width:210mm;height:297mm;";
        }
       // document.all.WebBrowser.ExecWB(7, 1);// 打印页面预览
    }

</script>
<body onload="">
<TABLE border=0 cellpadding=0 cellspacing=0 class="Noprint page-print-buttons" align="center" width=100%>
    <tr style="border: 0px;">
        <td style="border: 0px;" align="center">
            <input type=button class="txtToolBarButton" value="打印" data-before="printOrder">
            <input type=button class="txtToolBarButton" value="A4打印" data-before="changeToA4">
            <input type=button class="txtToolBarButton" value="预览">
            <input type=button class="txtToolBarButton" value="A4预览" data-before="printpreviewToA4">
        </td>
    </tr>
</TABLE>
<div id="mian_div" name="mian_div">
    <br/>
    <table class="tableHead" width="100%" style="border-bottom:0px;">
        <caption style="font-size:29px;">君马新能源退货单</caption>
        <br/>
        <tr tyle="height:10mm; line-height: 10mm;">
            <td width="12%">退货单号：</td>
            <td align="center" style="font-size:20px; width:38%;">${dataMap.return_code}
            </td>
            <td width="12%">退货日期：</td>
            <td align="center" style="width:38%;">${dataMap.dealer_date}
            </td>
        </tr>
        <tr>
            <td width="10%">退货单位：</td>
            <td align="center" width="40%">${dataMap.dealer_name}
            </td>
            <td>退货部门：</td>
            <td align="center">&nbsp;
            </td>
        </tr>
        <tr>
            <td>备注：</td>
            <td colspan="6" style="text-align:left">${dataMap.remark}

            </td>
        </tr>
    </table>


    <table class="tableBody" width="100%" style="border-top: 0px;">
        <tr style="text-align:center; height:10mm;">
            <td style="width:10mm;">序号</td>
            <td style="width:30mm;">物料编码</td>
            <td style="width:35mm;">物料名称</td>
            <td style="width:25mm;">仓库</td>
            <td style="width:20mm;">库位</td>
            <td style="width:30mm;">单位</td>
            <td style="width:15mm;">数量</td>
        </tr>
        <c:forEach items="${subList}" var="data">
            <tr style="text-align:center; height:10mm;">
                <td>${data.indexNO}</td>
                <td>${data.PART_OLDCODE}</td>
                <td>${data.PART_CNAME}</td>
                <td>${data.WH_NAME}</td>
                <td>${data.LOC_CODE}</td>
                <td>${data.UNIT}</td>
                <td>${data.IN_QTY}</td>
            </tr>
        </c:forEach>
        <c:if test="${listSize > 0}">
            <c:forEach begin="0" end="${listSize}">
                <tr>
                    <td class="list">&nbsp;</td>
                    <td class="list">&nbsp;</td>
                    <td class="list">&nbsp;</td>
                    <td class="list">&nbsp;</td>
                    <td class="list">&nbsp;</td>
                    <td class="list">&nbsp;</td>
                    <td class="list">&nbsp;</td>
                </tr>
            </c:forEach>
        </c:if>
        <tr>
            <td style="width:" colspan="6" align="center">合计：</td>
            <td style="width:">${sumapplyQty.SUM_IN_QTY}</td>
        </tr>
    </table>
    <br/>
    <table border="0" cellpadding="0" cellspacing="0" width="100%">
        <tr>
            <td align="left">&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;</td>
            <td align="left">签收人：&nbsp; &nbsp;</td>
            <td align="left">发料人：&nbsp;&nbsp; </td>
            <td align="left" colspan="3">部门审核：&nbsp; &nbsp;</td>
            <td align="left">制单人：&nbsp; &nbsp;</td>
        </tr>
    </table>
</div>
</body>
</html>