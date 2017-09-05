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
    <style media=print>
        .Noprint {
            display: none;
        }
        .p_next {
            page-break-after: always;
        }

    </style>
    <style type="text/css">
        body table td {
            padding: 6px; 
        }
        
        .cen {
        	text-align: left;           
        	margin-left: 140px;
        }

        span {
            padding-right: 100px;
            padding-top: 40px;
        }

        .right {
            border-right: #666666 solid 1px;
        }

        .caption {
            background-color: #CCCCCC;
            font-weight: bold;
            font-size: 15px;
            padding: 8px;
            text-align: center;
            border-left: #666666 solid 1px;
            border-top: #666666 solid 1px;
        }

        .name {
            font-weight: bold;
            font-size: 26px;
            padding: 14px;
            border: none;
        }

        .item, .value, .rightValue, .b, .remarks {
            border-left: #666666 solid 1px;
            border-top: #666666 solid 1px;
        }

        .item {
            width: 100px;
            text-align: center;
        }

        .value {
            width: 160px;
            text-align: center;
        }

        .rightValue {
            width: 220px;
            text-align: center;
        }
        
        .remarks {
        	border-bottom: #666666 solid 1px;
        	text-align: center;
        }
        
        .notes {
        	border-left: #666666 solid 1px;
        }

        .blank {
            padding-top: 120px;
            border-left: #666666 solid 1px;
        }

        .yaer {
            text-align: right;
            padding-right: 60px;
            border-left: #666666 solid 1px;
            border-bottom: #666666 solid 1px;
        }
        #top_cen_mid_div {
            width: 100%;
            height: 54px;
            margin: 0px;
            text-align: center;
            position:relative
        }
    </style>
</head>
<script language="javascript">
    //获取选择框的值
    function getCode(value) {
        var str = getItemValue(value);
        document.write(str);
    }

    var index = 0;
    function getIndex() {
        index++;
        document.write(index);
    }
    function changeToA4() {
        var mian_div = document.getElementsByName("mian_div");
        for (var i = 0; i < mian_div.length; i++) {
            mian_div[i].style.cssText = "width:210mm;height:297mm;";
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
<body onload="">
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <input name="pickOrderId" id="pickOrderId" value="${dataMap.pickOrderId}" type="hidden"/>
    <input type=textarea name=datapackager style="display:none" value=''>
<%--    <table class="cen" border=0 cellpadding=3 cellspacing=0>
        <tr>
            <td class="name">
                <img align="left" src="<%=request.getContextPath()%>/img/bq_log.gif" style="margin-right: 260px;"/>
                配件赔偿申请清单
            </td>
        </tr>
    </table>--%>

     <div id="top_cen_mid_div">
         <img src="<%=request.getContextPath()%>/img/bq_log1.gif" style="width: 250px; height: 55px;" />
         <span style="font-size:30px;margin-left:-120px;font-weight:bold">配件赔偿申请清单</span>
     </div>
    <table id="file" class="right" border=0 cellpadding=3 align="center" cellspacing=0>
        <tr>
            <td colspan="8" class="caption">基本信息</td>
        </tr>
        <tr>
            <td class="item">服务商编码</td>
            <td class="value">&nbsp;${mainMap.DEALER_CODE}</td>
            <td class="item">&nbsp;服务商名称</td>
            <td class="value">&nbsp;${mainMap.DEALER_NAME}</td>
            <td class="item">&nbsp;赔偿申请单号</td>
            <td class="value">&nbsp;${mainMap.IN_CODE}</td>
            <td class="item">申请赔偿类型</td>
            <td class="rightValue">
                A. 多件、少件
                &nbsp;&nbsp;
                B. 错件
                &nbsp;&nbsp;
                C. 损坏
                &nbsp;&nbsp;
                D. 质量原因
            </td>
        </tr>
        <tr>
            <td class="item">订单号</td>
            <td class="value">&nbsp;${mainMap.ORDER_CODE}</td>
            <td class="item">发运单号</td>
            <td class="value">&nbsp;${mainMap.TRANS_CODE}</td>
            <td class="item">到货时间</td>
            <td class="value">&nbsp;${mainMap.CREATE_DATE}</td>
            <td class="item">申请时间</td>
            <td class="rightValue">&nbsp;${mainMap.date}</td>
        </tr>
        <tr>
            <td colspan="8" class="caption">赔偿件信息</td>
        </tr>
        <tr>
            <td class="item">序号</td>
            <td class="value">配件编码</td>
            <td class="item">配件名称</td>
            <td class="value">单价</td>
            <td class="item">订货数量</td>
            <td class="value">申请赔偿数量</td>
            <td class="item">合计金额</td>
            <td class="remarks">备注</td>
        </tr>
        <c:forEach items="${inList}" var="data">
            <tr>
                <td class="item">
                    <script language="javascript">
                        getIndex();
                    </script>
                </td>
                <td class="value">${data.PART_OLDCODE}</td>
                <td class="item">${data.PART_CNAME}</td>
                <td class="value">${data.SALE_PRICE }</td>
                <td class="item">${data.OUTSTOCK_QTY }</td>
                <td class="value">${data.EXCEPTION_NUM }</td>
                <td class="item">${data.SUM_PRICE }</td>
                <td class="notes">&nbsp;</td>
            </tr>
        </c:forEach>
        <tr>
            <td colspan="3" class="caption">服务商签章</td>
            <td colspan="3" class="caption">北汽幻速配件管理部</td>
            <td colspan="2" class="caption">物流部配件库</td>
        </tr>
        <tr>
            <td colspan="3" class="b"><span>配件主管:</span><span>联系方式：</span></td>
            <td colspan="3" class="b"><span>是否规定时间回传：（ Y / N ）</span></td>
            <td colspan="2" class="b"><span>是否赔付：（ Y / N ）</span></td>
        </tr>
        <tr>
            <td colspan="3" class="b"><span>服务经理:</span><span>公章：</span></td>
            <td colspan="3" class="b"><span>配件管理部意见：</span></td>
            <td colspan="2" class="b"><span>拒赔原因及处理意见;</span></td>
        </tr>
        <tr>
            <td colspan="3" class="blank">&nbsp;</td>
            <td colspan="3" class="blank">接收人：</td>
            <td colspan="2" class="blank">处理人员： &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;配件库主管：</td>
        </tr>
        <tr>
            <td colspan="3" class="yaer">年&nbsp;&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;&nbsp;日</td>
            <td colspan="3" class="yaer">年&nbsp;&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;&nbsp;日</td>
            <td colspan="2" class="yaer">年&nbsp;&nbsp;&nbsp;&nbsp;月&nbsp;&nbsp;&nbsp;&nbsp;日</td>
        </tr>
    </TABLE>
    <TABLE border=0 cellpadding=0 cellspacing=0 class="Noprint" align="center" width=100%>
        <tr style="border: 0px;" align="center">
            <td style="border: 0px;">
                <OBJECT id="WebBrowser" classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2"
                        style="display:none"></OBJECT>
                <input type=button class="txtToolBarButton" value="打印" onClick="changeToA4()">
                <input type=button class="txtToolBarButton" value="预览" onClick="printpreviewToA4()">
            </td>
        </tr>
    </TABLE>
</form>
</form>
</body>
</html>