<%@ page import="com.infodms.dms.common.Constant"%>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
    String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>配件销售退货入库</title>
<script type="text/javascript">
$(function(){__extQuery__(1);});
var myPage;

var url = "<%=contextPath%>/parts/storageManager/partReturnManager/PartDlrReturnInManager/queryPartDlrReturnApplyInfo.json";

var title = null;

var columns = [
    {header: "序号", align: 'center', renderer: getIndex},
    {id: 'action', header: "操作", sortable: false, dataIndex: 'RETURN_ID', renderer: myLink, align: 'center'},
    {header: "退货单号", dataIndex: 'RETURN_CODE', align: 'center'},
    {header: "退货单位编码", dataIndex: 'DEALER_CODE', align: 'center'},
    {header: "退货单位", dataIndex: 'DEALER_NAME', style: 'text-align: left;'},
    {header: "承运物流", dataIndex: 'WL', style: 'text-align: left;'},
    {header: "物流单号", dataIndex: 'WLNO', style: 'text-align: left;'},
    {header: "回运日期", dataIndex: 'WL_DATE', style: 'text-align: left;'},
    {header: "退货原因", dataIndex: 'REMARK', style: 'text-align: left;'},
    {header: "状态", dataIndex: 'STATE', align: 'center', renderer: getItemValue}
];

//设置超链接
function myLink(value, meta, record) {
    if (record.data.STATE ==<%=Constant.PART_DLR_RETURN_STATUS_05%>) {
        return String.format("<a href=\"#\" onclick='inPartReturn(\"" + value + "\")'>[入库]</a>" + "<a href=\"#\" onclick='confirmCloseOrder(\"" + value + "\")'>[关闭]</a>");
    }
    return String.format("<a href=\"#\" onclick='view(\"" + value + "\")'>[查看]</a>");
}
//销售退货出库
function inPartReturn(value) {
    window.location.href = '<%=contextPath%>/parts/storageManager/partReturnManager/PartDlrReturnInManager/queryPartDlrReturnInInit.do?returnId=' + value;
}
//格式化日期
function formatDate(value, meta, record) {
    var output = value.substr(0, 10);
    return output;
}
//关闭
function confirmCloseOrder(v1) {
    MyConfirm("确定要关闭?", closeOrder, [v1])
}
function closeOrder(value) {
    var url = "<%=contextPath%>/parts/storageManager/partReturnManager/PartDlrReturnInManager/closeReturnOrder.json?orderId=" + value;
    sendAjax(url, getResult2, 'fm');
}

function getResult2(jsonObj) {
    if (jsonObj != null) {
        var success = jsonObj.success;
        var error = jsonObj.error;
        var exceptions = jsonObj.Exception;
        if (success) {
            layer.msg(success, {icon: 1});
            __extQuery__(1);
        } else if (error) {
            MyAlert(error);
        } else if (exceptions) {
        	MyAlert(exceptions.message);
        }
    }
}
//查看申请详细页面
function view(value) {
    OpenHtmlWindow('<%=contextPath%>/parts/storageManager/partReturnManager/PartDlrReturnApplyQuery/queryReturnApplyDetailInit.do?returnId=' + value + "&flag=1", 800, 400);
}
</script>

</head>
<body>
	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 配件管理&gt;配件仓库管理 >配件退货管理&gt;销售退货入库
		</div>
		<form name="fm" id="fm" method="post">
			<div class="form-panel">
				<h2>
					<img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;" />查询条件
				</h2>
				<div class="form-body">
					<table class="table_query" width=100% border="0" align="center" cellpadding="1" cellspacing="1">
						<tr>

							<td class="right">退货单号：</td>
							<td>
								<input class="middle_txt" type="text" name="RETURN_CODE" id="RETURN_CODE" />
							</td>
							<td class="right">退货单位：</td>
							<td>
								<input class="middle_txt" type="text" name="DEALER_NAME" id="DEALER_NAME" />
							</td>
							<td class="right">退货单位编码：</td>
							<td>
								<input class="middle_txt" type="text" name="DEALER_CODE" id="DEALER_CODE" />
							</td>
						</tr>
						<tr>
							<td class="right">物流单号：</td>
							<td>
								<input class="middle_txt" type="text" name="wlNo" id="wlNo" />
							</td>
							<td class="right">状态：
							</td>
							<td>
								<select name="state" id="state" class="u-select">
									<c:forEach items="${stateMap}" var="stateMap">
										<option value="${stateMap.key}">${stateMap.value}</option>
									</c:forEach>
								</select>
							</td>
						</tr>
						<tr>
							<td colspan="6" class="center">
								<input name="BtnQuery" id="queryBtn" class="u-button" type="button" value="查 询" onclick="__extQuery__(1);" />
							</td>
						</tr>
					</table>
				</div>
			</div>
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		</form>
	</div>
</body>
</html>
