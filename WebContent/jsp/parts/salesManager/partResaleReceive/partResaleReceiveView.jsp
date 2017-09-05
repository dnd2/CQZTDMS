<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%
    String contextPath = request.getContextPath();
%>
<title>配件零售/领用单</title>
<script type="text/javascript">
$(function(){
	__extQuery__(1);
});
var myPage;
var url = "<%=contextPath%>/parts/salesManager/partResaleReceiveManager/partResRecAction/partOrderDetailSearch.json";
var title = null;

var columns = [
    {header: "序号", dataIndex: 'PART_ID', renderer: getIndex, align: 'center'},
    {header: "配件编码", dataIndex: 'PART_OLDCODE', style: 'text-align: left;'},
    {header: "配件名称", dataIndex: 'PART_CNAME', style: 'text-align: left;'},
    {header: "件号", dataIndex: 'PART_CODE', style: 'text-align: left;'},
    {header: "单位", dataIndex: 'UNIT', align: 'center'},
    {header: "货位", dataIndex: 'LOC_CODE', align: 'center'},
    {header: "批次号", dataIndex: 'BATCH_NO', align: 'center'},
    {header: "可用库存", dataIndex: 'STOCK_QTY', align: 'center'},
    {
        header: '<c:if test="${map.CHG_TYPE != null}"><c:choose><c:when test="${map.CHG_TYPE  eq '领用' }">领用</c:when><c:otherwise>零售</c:otherwise></c:choose></c:if>数量',
        dataIndex: 'QTY',
        align: 'center'
    },
    {
        header: '<c:if test="${map.CHG_TYPE != null}"><c:choose><c:when test="${map.CHG_TYPE  eq '领用' }">领用</c:when><c:otherwise>零售</c:otherwise></c:choose></c:if>单价',
        dataIndex: 'SALE_PRICE',
        align: 'center'
    },
    {
        header: '<c:if test="${map.CHG_TYPE != null}"><c:choose><c:when test="${map.CHG_TYPE  eq '领用' }">领用</c:when><c:otherwise>零售</c:otherwise></c:choose></c:if>金额(元)',
        dataIndex: 'SALE_AMOUNT',
        align: 'center'
    },
    {header: "已出库", dataIndex: 'OUT_QTY', align: 'center'},
    {header: "可出库", dataIndex: 'OUTABLE_QTY', align: 'center'},
    {header: "备注", dataIndex: 'REMARK', align: 'center'}
];
</script>
</head>
<style type="text/css">
#myTable {
	margin-top: 0px !important;
}

#_page {
	margin-top: 0px !important;
}
</style>
<body>
	<div class="wbox">
		<form method="post" name="fm" id="fm">
			<div class="navigation">
				<img src="<%=request.getContextPath()%>/img/nav.gif" />
				<c:if test="${oemId != null}">
					<c:choose>
						<c:when test="${oemId  eq parentOrgId }">&nbsp;当前位置：配件管理 &gt; 内部领用管理 &gt; 内部领用单&gt; 查看</c:when>
						<c:otherwise>&nbsp;当前位置：配件管理 &gt; 零售领用管理 &gt; 配件${map.CHG_TYPE}单 &gt; 查看</c:otherwise>
					</c:choose>
				</c:if>
				<input type="hidden" name="parentOrgId" id="parentOrgId" value="${parentOrgId }" />
				<input type="hidden" name="parentOrgCode" id="parentOrgCode" value="${parentOrgCode }" />
				<input type="hidden" name="actionURL" id="actionURL" value="${actionURL}" />
			</div>
			<div class="form-panel">
				<h2>
					<img src="<%=contextPath%>/img/subNav.gif" /> 基本信息
				</h2>
				<div class="form-body">
					<table class="table_query">
						<tr>
							<td class="right">
								<c:if test="${map.CHG_TYPE != null}">
									<c:choose>
										<c:when test="${map.CHG_TYPE  eq '领用' }">
                                领用单号：
                            </c:when>
										<c:otherwise>
                                零售单号：
                            </c:otherwise>
									</c:choose>
								</c:if>
							</td>
							<td>
								${map.RETAIL_CODE}
								<input type="hidden" value="${map.RETAIL_ID}" name="changeId" id="changeId" />
							</td>
							<td class="right">制单人：</td>
							<td>${map.NAME}</td>
							<td class="right">制单时间：</td>
							<td>${map.CREATE_DATE}</td>
						</tr>
						<tr>
							<td class="right">
								<c:if test="${map.CHG_TYPE != null}">
									<c:choose>
										<c:when test="${map.CHG_TYPE  eq '领用' }">
                                领用人：
                            </c:when>
										<c:otherwise>
                                采购单位：
                            </c:otherwise>
									</c:choose>
								</c:if>
							</td>
							<td>${map.LINKMAN}</td>
							<td class="right">联系电话：</td>
							<td>${map.TEL}</td>
							<td class="right">用途：</td>
							<td>${map.PURPOSE}</td>
						</tr>
						</tr>
						<tr>
							<td class="right">备注：</td>
							<td width="90%" colspan="5">${map.REMARK}</td>
						</tr>
						<tr>
							<td colspan="6" class="center">
								<input class="normal_btn" type="button" value="关 闭" onclick="_hide();" />
							</td>
						
						</tr>
					</table>
				</div>
			</div>

			<div class="form-panel" style="margin-top: 2px; margin-bottom: 0px;">
				<h2 style="border-bottom: 0px;">
					<img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 明细信息
				</h2>
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