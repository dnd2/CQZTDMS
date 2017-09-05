<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
    String contextPath = request.getContextPath();
%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript">
//获取选择框的值
function getCode(value){
	var str = getItemValue(value);

	document.write(str);
}
//获取序号
function getIdx(){
	document.write(document.getElementById("file").rows.length-2);
}
//获取序号
function getIdx1(){
	document.write(document.getElementById("file1").rows.length-2);
}
//返回
function goBack(){
	btnDisable();
	window.location.href = '<%=contextPath%>/parts/financeManager/partInvoiceManager/partInvoiceAction/partInvoiceQueryInit.do';
}
</script>
</head>
<body>
	<div class="wbox">
		<div class="navigation">
			<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 配件财务管理 &gt; 财务金税发票 &gt; 销售单查看
		</div>

		<div class="form-panel">
			<h2>
				<img src="<%=contextPath%>/img/subNav.gif"/> 销售单信息
			</h2>
			<div class="form-body">
				<table class="table_query">
					<tr>
						<td class="right">销售单号：</td>
						<td>${mainMap.SO_CODE}</td>
						<td class="right">销售日期：</td>
						<td>${mainMap.SALE_DATE}</td>
						<td class="right">销售制单人：</td>
						<td>${mainMap.CREATE_BY_NAME}</td>
					</tr>
					<tr>
						<td class="right">销售单位：</td>
						<td>${mainMap.SELLER_NAME}</td>
						<td class="right">订货单位：</td>
						<td>${mainMap.DEALER_NAME}</td>
						<td class="right">订货制单人：</td>
						<td>${mainMap.orderCreateBy}</td>
					</tr>
					<tr>
						<td class="right">订货日期：</td>
						<td>${mainMap.orderCreateDate}</td>
						<td class="right">出库仓库：</td>
						<td>${mainMap.WH_NAME}</td>
						<td class="right">接收单位：</td>
						<td>${mainMap.CONSIGNEES}</td>
					</tr>
					<tr>
						<td class="right">接收地址：</td>
						<td>${mainMap.ADDR}</td>
						<td class="right">接收人：</td>
						<td>${mainMap.RECEIVER}</td>
						<td class="right">
							<span>接收人电话：</span>
						</td>
						<td>${mainMap.TEL}</td>
					</tr>
					<tr>
						<td class="right">邮政编码：</td>
						<td>${mainMap.POST_CODE}</td>
						<td class="right">到站名称：</td>
						<td>${mainMap.STATION}</td>
						<td class="right">发运方式：</td>
						<td>
							<script type="text/javascript">
								getCode('${mainMap.TRANS_TYPE}');
							</script>
						</td>
					</tr>
					<tr>
						<td class="right">付款方式：</td>
						<td>
							<script type="text/javascript">
								getCode('${mainMap.PAY_TYPE}');
							</script>
						</td>
						<td class="right">订单类型：</td>
						<td>
							<script type="text/javascript">
								getCode('${mainMap.ORDER_TYPE}');
							</script>
						</td>
						<td class="right">总金额：</td>
						<td>${mainMap.F_AMOUNT}</td>
					</tr>
					<tr>
						<td class="right">运费支付方式：</td>
						<td>
							<script type="text/javascript">
								getCode('${mainMap.TRANSPAY_TYPE}');
							</script>
						</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td class="right">备注：</td>
						<td colspan="5">${mainMap.REMARK}</td>
					</tr>
				</table>
			</div>
		</div>

		<table id="file" class="table_list" style="border-bottom: 1px solid #DAE0EE">
			<caption><img class="panel-icon nav" src="<%=contextPath%>/img/subNav.gif"/>配件信息</caption>
			<tr>
				<th align="center">序号</th>
				<th align="center">配件编码</th>
				<th align="center">配件名称</th>
				<th align="center">件号</th>
				<th align="center">最小包装量</th>
				<th align="center">单位</th>
				<th align="center">采购数量</th>
				<th align="center">销售数量</th>
				<th align="center">销售单价</th>
				<th align="center">销售金额</th>
				<th align="center">备注</th>
			</tr>
			<c:forEach items="${detailList}" var="data">
				<tr class="table_list_row1">
					<td align="center">
						&nbsp;
						<script type="text/javascript">
       				getIdx();
				</script>
					</td>
					<td align="left">
						&nbsp;&nbsp;
						<c:out value="${data.PART_OLDCODE}" />
					</td>
					<td align="left">
						&nbsp;&nbsp;
						<c:out value="${data.PART_CNAME}" />
					</td>
					<td align="left">
						&nbsp;&nbsp;
						<c:out value="${data.PART_CODE}" />
					</td>
					<td align="center">
						<c:out value="${data.MIN_PACKAGE}" />
					</td>
					<td align="center">
						<c:out value="${data.UNIT}" />
					</td>
					<td align="center">
						<c:out value="${data.BUY_QTY}" />
					</td>
					<td align="center">
						<c:out value="${data.SALES_QTY}" />
					</td>
					<td class="right">
						<c:out value="${data.BUY_PRICE}" />
						&nbsp;&nbsp;
					</td>
					<td class="right">
						<c:out value="${data.BUY_AMOUNT}" />
						&nbsp;&nbsp;
					</td>
					<td align="left">
						&nbsp;
						<c:out value="${data.REMARK}" />
					</td>
				</tr>
			</c:forEach>
		</table>
		<table id="file1" class="table_list" style="border-bottom: 1px solid #DAE0EE">
			<caption><img class="panel-icon nav" src="<%=contextPath%>/img/subNav.gif"/>单据操作信息</caption>
			<tr>
				<th align="center" width="3%">序号</th>
				<th align="center" width="10%">操作人</th>
				<th align="center" width="13%">操作时间</th>
				<th align="center" width="14%">环节</th>
				<th align="center" width="8%">状态</th>

			</tr>
			<c:forEach items="${historyList}" var="data" varStatus="_sequenceNum">
				<tr class="table_list_row1">
					<td align="center">${_sequenceNum.index+1}</td>
					<td align="center">
						<c:out value="${data.OPT_NAME}" />
					</td>
					<td align="center">
						<c:out value="${data.OPT_DATE}" />
					</td>
					<td align="center">
						<c:out value="${data.WHAT}" />
					</td>
					<td>
						&nbsp;
						<script type="text/javascript">
							getCode('${data.STATUS}');
			  			</script>
					</td>
				</tr>
			</c:forEach>
		</table>
		<table border="0" class="table_query">
			<tr>
				<td class="center">
					<input class="normal_btn" type="button" value="返 回" onclick="history.back()" />
				</td>
			</tr>
		</table>
	</div>
</body>
</html>


</body>
</html>