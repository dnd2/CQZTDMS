<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
    String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
<title>生成发运计划-装箱号选择框</title>
<script type="text/javascript">
    function doDisAllClick() {
        $("#checkWscAll")[0].checked = false;
//         $("#checkYscAll")[0].checked = false;
        var groupCheckBoxs = document.getElementsByName("checkWsc");
        if (!groupCheckBoxs) return;
        for (var i = 0; i < groupCheckBoxs.length; i++) {
            groupCheckBoxs[i].checked = false;
        }
//         var groupCheckBoxs1 = document.getElementsByName("checkYsc");
//         if (!groupCheckBoxs1) return;
//         for (var i = 0; i < groupCheckBoxs1.length; i++) {
//             groupCheckBoxs1[i].checked = false;
//         }
    }

    function selAll(obj) {
        var cb = document.getElementsByName('checkWsc');
        for (var i = 0; i < cb.length; i++) {
            if (obj.checked) {
                cb[i].checked = true;
            } else {
                cb[i].checked = false;
            }
        }
    }

//     function selAll2(obj) {
//         var cb = document.getElementsByName('checkYsc');
//         for (var i = 0; i < cb.length; i++) {
//             if (obj.checked) {
//                 cb[i].checked = true;
//             } else {
//                 cb[i].checked = false;
//             }
//         }
//     }

    function chkDtlWsc() {
        var cks = document.getElementsByName('checkWsc');
        var flag = true;
        for (var i = 0; i < cks.length; i++) {
            var obj = cks[i];
            if (!obj.checked) {
                flag = false;
            }
        }
        $("#checkWscAll")[0].checked = flag;
    }

//     function chkDtlYsc() {
//         var cks = document.getElementsByName('checkYsc');
//         var flag = true;
//         for (var i = 0; i < cks.length; i++) {
//             var obj = cks[i];
//             if (!obj.checked) {
//                 flag = false;
//             }
//         }
//         $("#checkYscAll")[0].checked = flag;
//     }

    function checkAll() {
        $("#checkWscAll")[0].checked = true;
//         $("#checkYscAll")[0].checked = true;
        var groupCheckBoxs = document.getElementsByName("checkWsc");
        if (!groupCheckBoxs) return;
        for (var i = 0; i < groupCheckBoxs.length; i++) {
            groupCheckBoxs[i].checked = true;
        }
//         var groupCheckBoxs1 = document.getElementsByName("checkYsc");
//         if (!groupCheckBoxs1) return;
//         for (var i = 0; i < groupCheckBoxs1.length; i++) {
//             groupCheckBoxs1[i].checked = true;
//         }
    }

    function doConfirm() {
        var flag = $("#flag")[0].value;
        var chk = document.getElementsByName("checkWsc");
        var chk1 = document.getElementsByName("checkYsc");
        var l = chk.length;
        var cnt = 0;
        var l2 = chk1.length;
        var cnt2 = 0;
        for (var i = 0; i < l; i++) {
            if (chk[i].checked) {
                cnt++;
            }
        }
        for (var i = 0; i < l2; i++) {
            if (chk1[i].checked) {
                cnt2++;
            }
        }
        if (cnt == 0 && cnt2 == 0) {
            MyAlert("请选择箱号！");
        } else {
            var pkgNos = "";
            for (var i = 0; i < l; i++) {
                if (chk[i].checked) {
                    var pkgNo = chk[i].value;
                    pkgNos += pkgNo + ",";
                }
            }

            for (var i = 0; i < l2; i++) {
                if (chk1[i].checked) {
                    var pkgNo = chk1[i].value;
                    pkgNos += pkgNo + ",";
                }
            }

            pkgNos = pkgNos.substr(0, pkgNos.length - 1);
            if (pkgNos && pkgNos.length > 0) {
                $('#PKG_NOS')[0].value = pkgNos;
            }

            var pkgNos = $('#PKG_NOS')[0].value;
            var pickOrderId = $('#pickOrderId')[0].value;
            if (parent.$('#inIframe')[0] == null) {//修复不在inIframe中时参数赋值问题
                if (flag == 1) {
                    top.pkgDtlPrint(pickOrderId, pkgNos);
                } else {
                    top.pkgPart(pickOrderId, pkgNos);
                }
            } else {
                if (flag == 1) {
                    parentContainer.pkgDtlPrint(pickOrderId, pkgNos);
                } else {
                    parentContainer.pkgPart(pickOrderId, pkgNos);
                }
            }

            parent._hide();
        }
    }
</script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif"/>&nbsp;当前位置： 选择装箱号</div>
<form method="post" name="fm" id="fm">
    <input id="PKG_NOS" name="PKG_NOS" type="hidden" value=""/>
    <input id="pickOrderId" name="pickOrderId" type="hidden" value="${param.pickOrderId }"/>
    <input id="flag" name="flag" type="hidden" value="${flag }"/>

    <%--20170803目前无随车发运箱号<table id="file" class="table_list" style="border-bottom:1px solid #DAE0EE">
        <tr>
            <td colspan="10" align="left">
                <input type="checkbox" id="checkYscAll" name="checkYscAll" onclick="selAll2(this);"/>
               	 随车未发运箱号
            </td>
        </tr>
        <tr>
            <c:choose>
            <c:when test="${!empty list}">
            <c:forEach items="${list}" var="po" varStatus="sta" step="1">
            <td align="left">
                <input type="checkbox" value="${po.BOX_ID}" name="checkYsc"
                       onclick="chkDtlYsc();"/>&nbsp;${po.PKG_NO}&nbsp;
            </td>
            <c:if test="${((sta.index+1)%10)==0}">
        </tr>
        <tr>
            </c:if>
            </c:forEach>
            </c:when>
            <c:otherwise>
                <td><font color="red">没有满足条件的数据!</font></td>
            </c:otherwise>
            </c:choose>
        </tr>
    </table> --%>

    <table id="file1" class="table_list" style="border-bottom:1px solid #DAE0EE">
        <tr>
            <td align="left" colspan="10">
                <input type="checkbox" id="checkWscAll" name="checkWscAll" onclick="selAll(this);"/>
<!--                 	未随车未发运箱号 -->
				选择发运箱号
            </td>
        </tr>
        <tr>
            <c:choose>
            <c:when test="${!empty list1}">
            <c:forEach items="${list1}" var="po1" varStatus="sta" step="1">
            <td align="left">
                <input type="checkbox" value="${po1.BOX_ID }" name="checkWsc"
                       onclick="chkDtlWsc();"/>&nbsp;${po1.PKG_NO}&nbsp;
            </td>
            <c:if test="${((sta.index+1)%10)==0}">
        </tr>
        <tr>
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
    <input name="queren1" type="button" class="u-button" onclick="checkAll();" value="全选"/>
    <input class="u-button" type="button" name="queren2" value="全不选" onclick="doDisAllClick()"/>
    <input name="queren3" type="button" class="u-button" onclick="doConfirm();" value="确认"/>
    <input class="u-button" type="button" value="关闭" onclick="parent._hide()"/>
</div>

</body>
</html>