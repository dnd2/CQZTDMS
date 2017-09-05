<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%
    String contextPath = request.getContextPath();
%>
<title>配件移库入库</title>
<script type="text/javascript">
$(function(){__extQuery__(1);});
var myPage;
var url = "<%=contextPath%>/parts/storageManager/partStockMvManager/partStockMvAction/partMVSearch.json";
var title = null;

var columns = [
    {header: "序号", dataIndex: 'RETAIL_ID', renderer: getIndex, align: 'center'},
    {id: 'action', header: "操作", sortable: false, dataIndex: 'ID', renderer: myLink, align: 'center'},
    {header: "移库单号", dataIndex: 'CHG_CODE', align: 'center'},
    {header: "移库单位", dataIndex: 'ORG_NAME', align: 'center'},
    {header: "备注", dataIndex: 'REMARK', align: 'center'},
    {header: "移出仓库", dataIndex: 'WH_NAME', align: 'center'},
    {header: "移入仓库", dataIndex: 'TOWH_NAME', align: 'center'},
    {header: "制单人", dataIndex: 'NAME', align: 'center'},
    {header: "制单日期", dataIndex: 'CREATE_DATE', align: 'center'},
    {header: "状态", dataIndex: 'STATE', align: 'center', renderer: getItemValue}
];

//设置超链接
function myLink(value, meta, record) {
    var changeId = record.data.CHG_ID;
    var state = record.data.STATE;
    var str = "";
    if ('<%=Constant.PART_MV_STATUS_04 %>' == state) {
        str = "<a href=\"#\" onclick='viewOrder(\"" + changeId + "\")'>[查看]</a>&nbsp;<a href=\"#\" onclick='inStockOrder(\"" + changeId + "\")'>[入库]</a>";
    }
    else {
        str = "<a href=\"#\" onclick='viewOrder(\"" + changeId + "\")'>[查看]</a>&nbsp;";
    }

    return String.format(str);
}

function getResult(json) {
	btnEnable();
    if (null != json) {
        if (json.error != null) {
            MyAlert(json.error);
            __extQuery__(json.curPage);
        } else if (json.success == "success") {
            MyAlert("操作成功!");
            __extQuery__(json.curPage);
        } else {
            MyAlert("操作失败，请联系管理员!");
        }
    }
}

//查看
function viewOrder(parms) {
    var optionType = "view";
    OpenHtmlWindow("<%=contextPath%>/parts/storageManager/partStockMvManager/partStockMvAction/viewMVDtlInint.do?chgId=" + parms + "&optionType=" + optionType, 1000, 500);
}

//审核
function inStockOrder(value) {
    var optionType = "inStock";
    fm.action = "<%=contextPath%>/parts/storageManager/partStockMvManager/partStockMvAction/viewMVDtlInint.do?CHG_ID=" + value + "&optionType=" + optionType;
    fm.target = "_self"
    fm.submit();
}

</script>
</head>
<body>
	<div class="wbox">
		<form method="post" name="fm" id="fm">
			<div class="navigation">
				<img src="<%=request.getContextPath()%>/img/nav.gif" /> &nbsp;当前位置： 配件管理 &gt; &gt;配件仓储管理 &gt; 配件调拨入库
				<input type="hidden" name="orgId" id="orgId" value="${parentOrgId }" />
				<input type="hidden" name="orgCode" id="orgCode" value="${parentOrgCode }" />
				<input type="hidden" name="orgName" id="orgName" value="${companyName }" />
				<input type="hidden" name="orderState" id="orderState" value="<%=Constant.PART_MV_STATUS_04%>" />
			</div>
			<div class="form-panel">
				<h2>
					<img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;" />查询条件
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">移库单号：</td>
							<td>
								<input class="middle_txt" type="text" name="changeCode" id="changeCode" />
							</td>
							<td class="right">制单日期：</td>
							<td>
								<input id="checkSDate" class="short_txt" style="" name="checkSDate" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" />
								<input class="time_ico" onclick="showcalendar(event, 'checkSDate', false);" value=" " type="button" />
								至
								<input id="checkEDate" class="short_txt" style="width: 80px;" name="checkEDate" datatype="1,is_date,10" maxlength="10" group="checkSDate,checkEDate" />
								<input class="time_ico" onclick="showcalendar(event, 'checkEDate', false);" value=" " type="button" />
							</td>
<!-- 							<td class="right">状态：</td> -->
<!-- 							<td> -->
<!-- 								<script type="text/javascript"> -->
<%--                         			genSelBoxExp("orderState", <%=Constant.PART_MV_STATUS%>, <%=Constant.PART_MV_STATUS_04%>, true, "", "", "false", ""); --%>
<!-- 								</script> -->
<!-- 							</td> -->
						</tr>
						<tr>
							<td class="right">移出仓库：</td>
							<td>
								<select name="whId" id="whId" class="u-select">
									<option value="">-请选择-</option>
									<c:if test="${WHList!=null}">
										<c:forEach items="${WHList}" var="list">
											<option value="${list.WH_ID }">${list.WH_CNAME }</option>
										</c:forEach>
									</c:if>
								</select>
							</td>
							<td class="right">移入仓库：</td>
							<td>
								<select name="toWhId" id="toWhId" class="u-select">
									<option value="">-请选择-</option>
									<c:if test="${WHList2!=null}">
										<c:forEach items="${WHList2}" var="list2">
											<option value="${list2.WH_ID }">${list2.WH_CNAME }</option>
										</c:forEach>
									</c:if>
								</select>
							</td>
						</tr>
						<tr>
							<td class="center" colspan="4">
								<input class="u-button" type="button" value="查 询" name="BtnQuery" id="queryBtn" onclick="__extQuery__(1)" />
							</td>
						</tr>
					</table>
				</div>
			</div>

			<!-- 查询条件 end -->
			<!--分页 begin -->
			<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
			<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
			<!--分页 end -->
		</form>
	</div>
</body>
</html>