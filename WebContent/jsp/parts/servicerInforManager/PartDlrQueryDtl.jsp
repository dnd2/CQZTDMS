<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String contextPath = request.getContextPath();
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>适提车型选择</title>
</head>
<body onload="sel();">
<div class="navigation">
    <img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 基础信息管理 &gt; 配件基础信息维护 &gt; 服务商配件分网车型维护 &gt;适提车型选择
</div>
<form method="post" name="fm" >
    <input id="dealerId" name="dealerId" type="hidden" value="${dealerId}"/>
    <input id="carTypes" name="carTypes" type="hidden" value=""/>
    <input id="mcurPage" name="mcurPage" type="hidden" value="${curPage}"/>
    <input id="dlrCarTypes" name="dlrCarTypes" type="hidden" value="${dlrCarTypes}"/>
    <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query" id="businessTable">
        <tr>
            <td colspan="10" align="left">
                <input type="checkbox" id="checkTypeAll" name="checkTypeAll" onclick="selAll(this);"/>
                适用车型
            </td>
        </tr>
        <tr>
            <c:choose>
            <c:when test="${!empty list}">
            <c:forEach items="${list}" var="brandList" varStatus="brandsta" step="1">
            <c:forEach items="${brandList.carTypeList}" var="carTypeList" varStatus="carTypesta" step="1">
            <td align="left">
                <input type="checkbox" value="${carTypeList.FIX_VALUE}" name="checkType" id="checkType_${carTypeList.FIX_VALUE}"
                       onclick="chkDtlType();"/>&nbsp;${carTypeList.FIX_NAME}&nbsp;
            </td>
            <c:if test="${((carTypesta.index+1)%3)==0}">
        </tr>
        <tr>
            </c:if>

            </c:forEach>
        </tr>
        <tr>
            <c:if test="${((carTypesta.index+1)%3)==0}">
            </c:if>
            </c:forEach>
            </c:when>
            <c:otherwise>
                <td><font color="red">没有满足条件的数据!</font></td>
            </c:otherwise>
            </c:choose>
        </tr>
    </table>
</form>
<div style="margin-top:25px;float: left" id="sel">
    <input name="queren1" type="button" class="cssbutton" onclick="checkAll();" value="全选"/>
    <input class="cssbutton" type="button" name="queren2" value="全不选" onclick="doDisAllClick()"/>
    <input name="queren3" type="button" class="cssbutton" onclick="doConfirm();" value="确认"/>
    <input class="normal_btn" type="button" value="关闭" onclick="parent._hide()"/>
</div>
<script type="text/javascript">
    function selAll(obj) {
        var cb = document.getElementsByName('checkType');
        for (var i = 0; i < cb.length; i++) {
            if (obj.checked) {
                cb[i].checked = true;
            } else {
                cb[i].checked = false;
            }
        }
    }
    function chkDtlType() {
        var cks = document.getElementsByName('checkType');
        var flag = true;
        for (var i = 0; i < cks.length; i++) {
            var obj = cks[i];
            if (!obj.checked) {
                flag = false;
            }
        }
        $("checkTypeAll").checked = flag;
    }


    function doDisAllClick() {
        $("checkTypeAll").checked = false;
        var groupCheckBoxs = document.getElementsByName("checkType");
        if (!groupCheckBoxs) return;
        for (var i = 0; i < groupCheckBoxs.length; i++) {
            groupCheckBoxs[i].checked = false;
        }
    }

    function chkDtlType() {
        var cks = document.getElementsByName('checkType');
        var flag = true;
        for (var i = 0; i < cks.length; i++) {
            var obj = cks[i];
            if (!obj.checked) {
                flag = false;
            }
        }
        $("checkTypeAll").checked = flag;
    }


    function checkAll() {
        $("checkTypeAll").checked = true;
        var groupCheckBoxs = document.getElementsByName("checkType");
        if (!groupCheckBoxs) return;
        for (var i = 0; i < groupCheckBoxs.length; i++) {
            groupCheckBoxs[i].checked = true;
        }

    }

    function sel(){
        var selType = $("dlrCarTypes").value;
        for(var i= 0 ; i< selType.split(",").length;i++){
            $("checkType_"+selType.split(",")[i]).checked =true;
        }
    }
    function doConfirm() {
        var dealerId = $("dealerId").value;
        var mcurPage = $("mcurPage").value;
        var chk = document.getElementsByName("checkType");
        var cnt = 0;
        var carTypes = "";
        for (var i = 0; i < chk.length; i++) {
            if (chk[i].checked) {
                cnt++;
            }
        }
        if (cnt == 0) {
            MyDivAlert("请选择车型！");
        } else {
            for (var i = 0; i < chk.length; i++) {
                if (chk[i].checked) {
                    var carType = chk[i].value;
                    carTypes += carType + ",";
                }
            }

            carTypes = carTypes.substr(0, carTypes.length - 1);
            if (parent.$('inIframe') == null) {//修复不在inIframe中时参数赋值问题
                top.updateDlrCarType(dealerId, carTypes, curPage);
            } else {
                parentContainer.updateDlrCarType(dealerId, carTypes, mcurPage);
            }

            parent._hide();
        }
    }
</script>
</body>
</html>
