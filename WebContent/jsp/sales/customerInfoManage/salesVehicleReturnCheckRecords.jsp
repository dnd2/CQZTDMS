<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="com.infodms.dms.util.CommonUtils"%>
<%@ page import="com.infodms.dms.po.TtDealerActualSalesPO"%>
<%@ page import="com.infodms.dms.po.TtCustomerPO"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%
	String contextPath = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />


<title>实销退车审批记录</title>
</head>
<body>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：实销退车审核记录查询</div>
<form id="fm" name="fm" method="post"><input type="hidden" name="curPage" id="curPage" value="1" /> <input type="hidden" id="dlrId" name="dlrId" value="" />

<table  width=100% border="0" align="center" cellpadding="1"
    cellspacing="1" class="table_list">
        <th>
            审核组织
        </th>
        <th>
            审核人
        </th>
        <th>
            审核日期
        </th>
        <th>
            审核结果
        </th>
        <th>
            审核意见
        </th>
        <tbody>
            <c:forEach items="${checkRecords}" var="list">
                <tr class="table_list_row">
                    <td>
                       ${list.ORG_NAME}
                    </td>
                    <td>
                       ${list.NAME}
                    </td>
                    <td>
                       ${list.CHK_DATE}
                    </td>
                    <td>
                        <script>
                            document.write(getItemValue(${list.STATUS}));
                       </script>
                    </td>
                    <td>
                            ${list.CHK_DESC}
                    </td>
                </tr>
        </c:forEach>
        </tbody>
</table>
<table  class="table_edit" align="center" >
	<tr class="cssTable">
		<td align="center" colspan="6">
			<input type="button" class="normal_btn" value="关闭" onclick="_hide();" />
		</td>
	</tr>
</table>
</form>
</div>
</body>
</html>