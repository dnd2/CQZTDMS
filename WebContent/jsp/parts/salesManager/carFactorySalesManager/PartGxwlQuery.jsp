<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/cout" prefix="c" %>
<%
    String contextPath = request.getContextPath();
%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <jsp:include page="${contextPath}/common/jsp_head_new.jsp"/>
    <title>查看</title>
    <style type="text/css">
        .table_list_row0 td {
            background-color: #FFFFCC;
            border: 1px solid #DAE0EE;
            white-space: nowrap;
        }
    </style>
</head>
<body onunload='javascript:destoryPrototype()' onload="__extQuery__(1);">
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 配件管理 &gt; 本部销售管理  &gt; 广宣品发运方式调整&gt; 查看</div>
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <input type="hidden" name="planCode" id="planCode" value="${param.planCode}"/>
    <input type="hidden" name="pkgNo" id="pkgNo" value="${param.pkgNo}"/>
    <table class="table_query">
        <tr>
            <th colspan="6"><img class="nav" src="<%=contextPath%>/img/nav.gif"/>广宣品发运计划单信息</th>
        </tr>
        <tr>
            <td align="right">发运计划单号：</td>
            <td width="20%">
                ${po['PLAN_CODE'] }
            </td>
            <td  align="right">服务商代码：</td>
            <td width="20%">
                ${po['DEALER_CODE'] }
            </td>
            <td align="right">服务商名称：</td>
            <td width="20%">
                ${po['DEALER_NAME'] }
            </td>
        </tr>
        <tr>
            <td align="right">装箱号：</td>
            <td>
                ${po['PKG_NO'] }
            </td>
            <td align="right">发运方式：</td>
            <td>
                <script type="text/javascript">
                        genSelBox("TRANS_TYPE", <%=Constant.PART_GX_ORDER_OUT_TYPE%>, ${po['TRANS_TYPE'] }, true, "short_sel", "disabled", "false", '');
                    </script>
            </td>
            <td align="right">承运物流：</td>
            <td>
                <select name="transportOrg" id="transportOrg" disabled="disabled">
                        <c:forEach items="${listc}" var="obj">
                            <option value="">--请选择--</option>
                            <option value="${obj.fixValue}" ${po['TRANS_ORG'] eq obj.fixValue?"selected":"" }>${obj.fixName}</option>
                        </c:forEach>
               </select>
            </td>
        </tr>
        <tr>
            <td align="right">计划生成日期：</td>
            <td>
                ${po['CREATE_DATE'] }
            </td>
            <td align="right">要求最终发运日期：</td>
            <td>
                ${po['FINAL_DATE'] }
            </td>
           
        </tr>
    </table>
    <table class="table_query">
        <th colspan="6" width="100%"><img src="<%=contextPath%>/img/subNav.gif" alt="" class="nav"/>配件信息</th>
        <tr>
            <td align="right">
                配件编码：
            </td>
            <td align="left">
                <input class="middle_txt" id="PART_OLDCODE"
                       name="PART_OLDCODE"
                       type="text"/>
            </td>
            <td align="right">
                配件名称：
            </td>
            <td align="left">
                <input class="middle_txt" id="PART_CNAME"
                       name="PART_CNAME"
                       type="text"/>
            </td>
            <td align="right">件号：</td>
            <td align="left">
                <input class="middle_txt" id="PART_CODE"
                       datatype="1,is_noquotation,30" name="PART_CODE"
                       type="text"/>
            </td>
        </tr>
        <tr>
            <td align="center" colspan="6">
                <input class="normal_btn" type="button" name="BtnQuery"
                       id="queryBtn" value="查 询" onclick="__extQuery__(1)"/>
            </td>
        </tr>
    </table>
    <jsp:include page="${contextPath}/queryPage/orderHidden.html"/>
    <jsp:include page="${contextPath}/queryPage/pageDiv.html"/>
    <table class="table_edit">
        <tr align="center">
            <td>

                <c:choose>
                    <c:when test="${'disabled' eq buttonFalg}">
                    	<!-- <input class="normal_btn" type="button" value="明细下载" onclick="exportDetl()"/> -->
                        <input class="normal_btn" type="button" value="关 闭" onclick="_hide();"/>
                    </c:when>
                    <c:otherwise>
                        <input class="normal_btn" type="button" value="返 回" onclick="goBack()"/>
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
    </table>
</form>
<script type=text/javascript>

var myPage;
var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartGxTransTypeAdjust/queryPartGxDetail.json";

var title = null;

var columns = [
    {header: "序号", align: 'center', renderer: getIndex, width: '7%'},
    {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align:left'},
    {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align:left'},
    {header: "件号", dataIndex: 'PART_CODE', style: 'text-align:left'},
    {header: "发运数量", dataIndex: 'REPORT_QTY', align: 'center'},
    {header: "装箱号", dataIndex: 'PKG_NO', align: 'center'},
    {header: "备注", dataIndex: 'REMARK', align: 'center'}
];

function goBack() {
	_hide();
}

//格式化日期
function formatDate(value, meta, record) {
    var output = value.substr(0, 10);
    return output;
}

</script>
</div>
</body>
</html>
