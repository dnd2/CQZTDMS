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
    <title>修改</title>
    <style type="text/css">
        .table_list_row0 td {
            background-color: #FFFFCC;
            border: 1px solid #DAE0EE;
            white-space: nowrap;
        }
    </style>
</head>
<body onunload='javascript:destoryPrototype()'>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 配件管理 &gt; 本部销售管理  &gt; 广宣品发运方式调整&gt; 修改</div>
<form name="fm" id="fm" method="post" enctype="multipart/form-data">
    <input type="hidden" name="planId" id="planId" value="${po['PLAN_ID'] }"/>
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
                        genSelBox("TRANS_TYPE", <%=Constant.PART_GX_ORDER_OUT_TYPE%>, '${po.TRANS_TYPE}', false, "short_sel", "", "false", '');
                </script>
            </td>
            <td align="right">承运物流：</td>
            <td>
                <select name="transportOrg" id="transportOrg">
                        <c:forEach items="${listc}" var="obj">
                            <!-- <option value="">--请选择--</option> -->
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
    
    <table border="0" class="table_query">
    <tr align="center">
        <td>&nbsp;
            <input class="normal_btn" type="button" value="保存" onclick="saveGx();"/>
            &nbsp;&nbsp;
            <input class="normal_btn" type="button" value="返 回" name="back" id="back" onclick="goBack()"/></td>
    </tr>
</table>
    
</div>
</form>
</body>

<script type=text/javascript>

function goBack() {
	window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartGxTransTypeLogMsgCheck/partGxTransTypeAdjustInit.do';
}

function saveGx(){
	var uncheckedId = "";
	MyConfirm("确定保存?", updatePartGxPlan, [uncheckedId]);
}

function updatePartGxPlan(uncheckedId) {
	btnDisable();
    var url = "<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartGxTransTypeLogMsgCheck/updatePartGxPlan.json";
    sendAjax(url, getResult, 'fm');
}


function getResult(jsonObj) {
	btnEable();
    if (jsonObj != null) {
        var success = jsonObj.success;
        var error = jsonObj.error;
        var exception = jsonObj.Exception;
        if (success) {
            MyAlert(success);
            window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartGxTransTypeLogMsgCheck/PartGxTransTypeLogMsgCheckInit.do';
        }else if(error){
        	MyAlert(error);
        }
        else if(exception){
	    	MyAlert(exception.message);
		}
    }
}
</script>

</html>
