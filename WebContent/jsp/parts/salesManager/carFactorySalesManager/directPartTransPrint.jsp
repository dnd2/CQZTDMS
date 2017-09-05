<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
    <title></title>
    <style type="text/css">
        html, body {
            font-size: 12px;
            margin: 0px;
            height: 100%;
        }

        .mesWindow {
            border: #666 1px solid;
            background: #fff;
        }

        .mesWindowTop {
            border-bottom: #eee 1px solid;
            margin-left: 4px;
            padding: 3px;
            font-weight: bold;
            text-align: left;
            font-size: 12px;
        }

        .mesWindowContent {
            margin: 4px;
            font-size: 12px;
        }

        .mesWindow .close {
            height: 15px;
            width: 28px;
            border: none;
            cursor: pointer;
            text-decoration: underline;
            background: #fff
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
        var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartSoManage/updateDirOrderPrint.json";
        //$('printBtn1').style.cssText = "display: none;";
        //$('printBtn2').style.cssText = "display: none;";
        sendAjax(url, getResult, 'fm');
    }
    function getResult(jsonObj) {
        if (jsonObj != null) {
            var error = jsonObj.error;
            var exceptions = jsonObj.Exception;
            if (error) {
                MyAlert(error);
                _hide();
                return;
            } else if (exceptions) {
                MyAlert(exceptions.message);
                _hide();
                return;
            }
            document.getElementById("printBtn1").style.cssText = "display: none;";
            window.print();
            document.getElementById("printBtn1").style.cssText = "display: block;";

            //  $('printBtn1').style.cssText = "display: block;";
            //  $('printBtn2').style.cssText = "display: block;";
            //  var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartSoManage/updateDirOrderPrint.json?";
            //  sendAjax(url, getQxtResult, 'fm');
            //  _hide();
        }
    }
    function getQxtResult(jsonObj) {
        MyAlert();
    }

</script>
</head>
<body onload="">
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
<input name="soId" id="soId" value="${mainMap.SO_ID}" type="hidden"/>
<input type=textarea name=datapackager style="display:none" value=''>

<div name=thisblock id=thisblock>
    <TABLE border=0 cellpadding=3 align="center" cellspacing=0 width="95%" height="20px">
        <tr>
            <td>
                &nbsp;
            </td>
        </tr>
    </TABLE>
    <TABLE border=0 bordercolor=black cellpadding=3 align="center" cellspacing=5 style="border-top:#aedef2 thin solid"
           width="95%">
        <br>
        <center>
            <font size="5"><b>精品配件发运单</b></font>
        </center>
    </TABLE>
    <TABLE border=0 cellpadding=3 align="center" cellspacing=0 width="95%" height="20px">
        <tr>
            <td>
                &nbsp;
            </td>
        </tr>
    </TABLE>
    <table border=0 cellpadding=3 align="center" cellspacing=0 width="95%" style="font-size: 25px;">
        <tr style="height: 25px; line-height: 25px;">
            <td width="33%" style="font-size: 15px;">

                订单号：&nbsp;${mainMap.ORDER_CODE}

            </td>
        </tr>
        <tr style="height: 25px; line-height: 25px;">
            <td width="33%" style="font-size: 15px;">

                购货单位：&nbsp;${mainMap.DEALER_NAME}

            </td>
        </tr>
        <tr style="height: 25px; line-height: 25px;">
            <td width="33%" style="font-size: 15px;">

                发运地址：&nbsp;${mainMap.ADDR}

            </td>
        </tr>
        <tr style="height: 25px; line-height: 25px;">
            <td width="33%" style="font-size: 15px;">

                收货人及联系电话：&nbsp;${mainMap.RECEIVER}&nbsp;&#40;${mainMap.TEL}&#41;

            </td>
        </tr>
        <tr style="height: 25px; line-height: 25px;">
            <td width="33%" style="font-size: 15px;">

                订货人及订货时间：&nbsp;${mainMap.DEALER_NAME}&nbsp;&#40;${mainMap.CREATE_DATE_FM}&#41;

            </td>
        </tr>
    </table>
    <TABLE border=0 cellpadding=3 align="center" cellspacing=0 width="95%" height="10px">
        <tr>
            <td>
                &nbsp;
            </td>
        </tr>
    </TABLE>
    <TABLE id="file" border=1 bordercolor=black cellpadding=3 align="center" cellspacing=0 width="95%">
        <tr style="height: 25px; line-height: 25px;">
            <td width=5% style="text-align: center" colspan=1>序号</td>
            <td width=10% style="text-align: center" colspan=1>配件编码</td>
            <td width=25% style="text-align: center" colspan=1>配件名称</td>
            <td width=15% style="text-align: center" colspan=1>件号</td>
            <td width=5% style="text-align: center" colspan=1>单位</td>
            <td width=10% style="text-align: center" colspan=1>订货数量</td>
            <td width=8% style="text-align: center" colspan=1>单价</td>
            <td width=12% style="text-align: center" colspan=1>销售金额</td>
            <td width=8% style="text-align: center" colspan=1>备注</td>
        </tr>
        <c:forEach items="${detailList}" var="data">
            <tr style="min-height: 25px; line-height: 25px;">
                <td style="text-align: center">

                    <script language="javascript">
                        getIndex()
                    </script>

                </td>

                <td style="text-align: center">
                        ${data.PART_OLDCODE}
                </td>
                <td style="text-align: center">
                        ${data.PART_CNAME}
                </td>
                <td style="text-align: center">
                        ${data.PART_CODE}
                </td>
                <td style="text-align: center">
                        ${data.UNIT}
                </td>
                <td style="text-align: center">
                        ${data.BUY_QTY}
                </td>
                <td style="text-align: center">
                        ${data.CONVERS_PRICE}
                </td>
                <td style="text-align: center">
                        ${data.CONVERS_AMOUNT}
                </td>
                <td style="text-align: center">
                        ${data.PKG_NUM}箱
                </td>
            </tr>
        </c:forEach>
        <tr style="min-height: 25px; line-height: 25px; ">
            <td style="text-align: center; " colspan=5>
                合计
            </td>
            <td style="text-align: center" colspan=4>
                ${mainMap.CONVERS_AMOUNT}
            </td>
        </tr>
    </TABLE>
    <TABLE border=0 cellpadding=3 align="center" cellspacing=0 width="95%" height="10px">
        <tr>
            <td>
                &nbsp;
            </td>
        </tr>
    </TABLE>
    <TABLE border=0 cellpadding=3 align="center" cellspacing=10 width="95%">
        <tr style="height: 30px; line-height: 30px;">
            <td colspan=2 style="text-align: left; font-size: 15px;">

                &nbsp;相关人员签字：

            </td>
        </tr>
        <tr style="height: 30px; line-height: 30px;">
            <td width="50%" style="font-size: 15px;">

                &nbsp;&nbsp;&nbsp;&nbsp;配件销售员:${mainMap.CREATE_BY_NAME} <%--&nbsp;&nbsp; ${mainMap.FCAUDIT_DATE}--%>

            </td>
            <td width="50%" style="font-size: 15px;">

                &nbsp;计划员：

            </td>
        </tr>
        <tr style="height: 40px; line-height: 40px;">
            <td width="50%" style="font-size: 15px;">

                &nbsp;&nbsp;&nbsp;&nbsp;销售室主任：

            </td>
            <td width="50%" style="font-size: 15px;">

                &nbsp;计划室主任：

            </td>
        </tr>
        <tr style="height: 30px; line-height: 30px;">
            <td width="50%" style="font-size: 15px;">

                &nbsp;&nbsp;&nbsp;&nbsp;处长批准：

            </td>
            <td width="50%">
                &nbsp;
            </td>
        </tr>
        <tr style="height: 30px; line-height: 30px;">
            <td colspan=2 style="text-align: left;font-size: 15px;">

                &nbsp;&nbsp;&nbsp;&nbsp;财务审核(并锁定余额)：

            </td>
        </tr>
    </TABLE>
    <TABLE border=0 cellpadding=3 align="center" cellspacing=0 width="95%" height="10px">
        <tr style="height: 30px; line-height: 30px;">
            <td style="font-size: 15px;">

                &nbsp;&nbsp;&nbsp;备注： &nbsp;&nbsp;&nbsp;&nbsp;${mainMap.REMARK}

            </td>
        </tr>
        <tr>
            <td style="font-size: 15px; text-align: right;">
                ${locYM }&nbsp;第${seqNum}号直发订单
                &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;
            </td>
        </tr>
    </TABLE>
</div>
	<SPAN width="90%" ID="PRINT">
	<TABLE border=0 cellpadding=0 cellspacing=0 height=30>
        <tr>
            <td width=1000px>
                &nbsp;
            </td>
            <td width=5%>
                <table border=0 width=100%>
                    <tr>
                        <td align="center">
                            <input type=button id="printBtn1" class="txtToolBarButton" style="font-size: 15px;"
                                   value="打 印" onClick="printOrder()">
                        </td>
                        <!--
                        <td>
                            <input type=button id="printBtn2" class="txtToolBarButton" style="font-size: 20px;" value="关 闭" onClick="_hide();">
                        </td>
                         -->
                    </tr>
                </table>
            </td>
            <td width=5%>
                &nbsp;
            </td>
        </tr>
    </TABLE>
	</SPAN>
</form>
</form>
</body>
</html>