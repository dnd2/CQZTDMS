<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
    String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=7">
    <title>切换订单批量导入预览 </title>
</head>
<body onload="autoAlertException();">
<div class=navigation><img src="<%=request.getContextPath()%>/img/nav.gif">&nbsp;切换订单提报导入信息查看</div>
<form name="fm" method="post" id="fm">
    <table class="table_list">
        <tr style="background: #FFFFCC">
            <td align="center" width="4%">序号</td>
            <td width="15%" align="center">活动编码</td>
            <td width="15%" align="center">服务商代码</td>
            <td width="15%" align="center">配件编码</td>
            <td width="15%" align="center">切换件编码</td>
            <td width="10%" align="center">数量</td>
            <td width="20%" align="center">备注</td>
        </tr>
        <c:forEach items="${relList}" var="data" varStatus="_sequenceNum">
            <c:if test="${(_sequenceNum.index+1)%2 == 0}">
                <tr class="table_list_row1">
            </c:if>
            <c:if test="${(_sequenceNum.index+1)%2 != 0}">
                <tr class="table_list_row2">
            </c:if>
            <td class="tBorder" align="center">${_sequenceNum.index+1}
            </td>
            <td class="tBorder" align="center">${data.activityCode}
            </td>
            <td class="tBorder" align="center">${data.dealerCode}
            </td>
            <td class="tBorder" align="center">${data.partOldcode}
            </td>
            <td class="tBorder" align="center">${data.repartOldcode}
            </td>
            <td class="tBorder" align="center">${data.partNum}
            </td>
            <td align="center">${data.remarks}
            </td>
            </tr>
        </c:forEach>
    </table>
    <input type="hidden" id="uploadId" name="uploadId" value="${uploadId}">
    <table width="95%" align="center" class="table_query">
        <tr>
            <td align="center">
                订单备注:<input type="text" id="orderRemakr" name="orderRemark" value="${orderRemark}"
                       style="width: 300px;color: red"/>
            </td>
        </tr>
        <tr>
            <td align="center">
                <input class="normal_btn" type="button" value="确定" name="button1" onClick="orderImport();">
                <input class="normal_btn" type="button" value="返回" name="button1" onClick="goBack();">
            </td>
        </tr>
    </table>
</form>
<script type="text/javascript">
    //返回
    function goBack() {
        btnDisable();
        fm.action = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartReplacedDlrOrder/partDlrOrderCheckInit.do";
        fm.submit();
    }
    //重复错误显示
    function goError() {
        btnDisable();
        fm.action = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartReplacedDlrOrder/goError.do";
        fm.submit();
    }
    //确定导入
    function orderImport() {
        MyConfirm("确定要导入？", savePkgStk, []);
    }
    function savePkgStk() {
        btnDisable();
        var uploadId = document.getElementById("uploadId").value;
        var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartReplacedDlrOrder/saveData.json?uploadId=" + uploadId ;
        sendAjax(url, getResult, 'fm');
    }
    //失效按钮
    function getResult(jsonObj) {
        btnEable();
        if (jsonObj != null) {
            var success = jsonObj.success;
            var error = jsonObj.error;
            var exceptions = jsonObj.Exception;
            if (success) {
                MyAlert(success);
                goBack();
            } else if (error) {
                MyAlert(error);
                goError();
            } else if (exceptions) {
                MyAlert(exceptions.message);
                $('file').style.display = "block";
                hideWait();
            }
        }
    }
</script>
</body>
</html>
