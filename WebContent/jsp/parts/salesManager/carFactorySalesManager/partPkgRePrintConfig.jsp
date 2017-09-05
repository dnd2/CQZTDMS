<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<head>
    <title></title>

</head>
<script language="javascript">
    function doPrint() {
        var printNum = document.getElementById('printNum').value;
        var re = /^[1-9]+[0-9]*]*$/;
        if (!re.test(printNum)) {
            MyAlert("打印张数应为正整数!");  
            return;
        }

        var pkgNo=document.getElementById('pkgNo').value;
        var dealerId=document.getElementById('dealerId').value;
        parentContainer.doPrint(0, printNum ,null, dealerId,pkgNo);
        <%-- window.location.href='<%=contextPath%>/jsp/parts/salesManager/carFactorySalesManager/PartPkgRePrint/printQxt.do?printNum='+printNum+'&dealerId='+dealerId+'&transId='+transId; --%>
        _hide();
    }
</script>
</head>
<body>
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
<input type="hidden" id="pkgNo" value="${list.PKG_NO }"/>
<input type="hidden" id="dealerId" value="${list.CONSIGNEES_ID }"/>
    <table width="100%" height="100%">
        <tr height="20px">
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td style="text-align: left;padding-left:28%;font-size: 14px">经销商编码：${list.CONSIGNEES_CODE }</td>
        </tr>
        <tr>
            <td style="text-align: left;padding-left:28%;font-size: 14px">经销商名称：${list.CONSIGNEES }</td>
        </tr>
         <tr>
            <td style="text-align: left;padding-left:28%;font-size: 14px">地址：${list.ADDR }</td>
         </tr>
         <tr>
            <td style="text-align: left;padding-left:28%;font-size: 14px">出库单号：${list.OUT_CODE }</td>
        </tr>
         <tr>
            <td style="text-align: left;padding-left:28%;font-size: 14px"> 发运单号：${list.TRANS_CODE }</td>
        </tr>
         <tr>
            <td style="text-align: left;padding-left:28%;font-size: 14px">装箱单号：${list.PKG_NO }</td>
        </tr>
         <tr>
            <td style="text-align: left;padding-left:28%;font-size: 14px"> 发货方式：${list.TRANS_TYPE }</td>
        </tr>
         <tr>
            <td style="text-align: left;padding-left:28%;font-size: 14px"> 发货仓储：${list.WH_NAME }</td>
        </tr>
        <tr>
            <br/>
        </tr>
        <tr>
            <td style="text-align: left;padding-left:28%;font-size: 14px">打印张数：
                <input type="text" id="printNum" name="printNum" class="middle_txt"
                       style="width: 100px; text-align: center"/>
            </td>
        </tr>
    </table>
    <br>
    <div align="center">
        <input id="btn" name="btn" type="button" value="确  定" class="u-button"
               style="width: 60px" onclick="doPrint();"/>
        &nbsp;&nbsp;
        <input id="btn" name="btn" type="button" value="关  闭"  class="u-button"
               style="width: 60px" onclick="_hide()"/>
    </div>
</form>
</body>
</html>