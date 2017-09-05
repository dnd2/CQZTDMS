<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
		document.write(document.getElementById("file1").rows.length-2);
	}
	//获取序号
	function getIdx1(){
		document.write(document.getElementById("file2").rows.length-2);
	}
	//返回
	function goBack(){
		window.location.href = '<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartReplacedDlrOrder/partDlrOrderCheckInit.do?flag=true';
	}

	//zhumingwei 2013-09-16
	//关闭
	function goClose(){
		_hide();
	}

	//明细下载
	function exportDetl()
	{
		document.fm.action="<%=contextPath%>/parts/salesManager/carFactorySalesManager/PartReplacedDlrOrder/exportOrderExcel.do";
		document.fm.target="_self";
		document.fm.submit();
	}
</script>
</head>

<body>
	<form name="fm" id="fm" method="post" enctype="multipart/form-data">
		<input type="hidden" name="STATE" id="STATE" value="${mainMap.STATE}" />
		<input type="hidden" name="orderId" id="orderId" value="${orderId }" />
		<input type="hidden" name="orderCode" id="orderCode"
			value="${mainMap.ORDER_CODE}" />
		<div class="wbox">
			<div class="navigation">
				<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：
				配件管理>配件仓储管理>配件替换管理>切换订单明细查看
			</div>

			<table class="table_add" border="0"
				style="BORDER-RIGHT: #859aff 1px solid; BORDER-TOP: #859aff 1px solid; BORDER-LEFT: #859aff 1px solid; BORDER-BOTTOM: #859aff 1px solid; border-color: #859aff;"
				cellSpacing=1 cellPadding=1 width="100%">
				<tr>
					<th colspan="6"><img class="nav"
						src="<%=contextPath%>/img/subNav.gif" /> 采购订单信息</th>
				</tr>
				<tr>
					<td width="10%" align="right">订单号：</td>
					<td width="20%">${mainMap.ORDER_CODE}</td>
					<td width="10%" align="right">订货单位：</td>
					<td width="20%">${mainMap.DEALER_NAME}</td>
					<td width="10%" align="right">制单人：</td>
					<td width="20%">${mainMap.NAME}</td>
				</tr>
				<tr>
					<td width="10%" align="right">制单日期：</td>
					<td width="20%">${mainMap.CREATE_DATE}</td>
					<td width="10%" align="right">销售单位：</td>
					<td width="20%">${mainMap.SELLER_NAME}</td>
					<td width="10%" align="right">接收单位：</td>
					<td width="20%">${mainMap.RCV_ORG}</td>
				</tr>
				<tr>
					<td width="10%" align="right">接收地址：</td>
					<td width="20%">${mainMap.ADDR}</td>
					<td width="10%" align="right"> 接收人：</td>
					<td width="20%">${mainMap.RECEIVER}</td>
					<td width="10%" align="right">接收人电话：</td>
					<td width="20%">${mainMap.TEL}</td>
				</tr>
				<tr>
					<td width="10%" align="right">邮政编码：</td>
					<td width="20%">${mainMap.POST_CODE}</td>
					<td width="10%" align="right">到站名称：</td>
					<td>${mainMap.STATION}</td>
					<td width="10%" align="right">发运方式：</td>
					<td width="20%">${mainMap.TRANS_TYPE}</td>

				</tr>
				<tr style="display: none">
					<td width="10%" align="right">付款方式：</td>
					<td width="20%"><script type="text/javascript">
				getCode(${mainMap.PAY_TYPE});;
	  	</script></td>
					<td width="10%" align="right">订单类型：</td>
					<td width="20%"><script type="text/javascript">
				getCode(${mainMap.ORDER_TYPE});;
	  	</script></td>
					<td width="10%" align="right">订单总金额：</td>
					<td width="20%">${mainMap.ORDER_AMOUNT}</td>
				</tr>
				<tr style="display: none">
					<td width="10%" align="right">配件金额：</td>
					<td width="20%"><script type="text/javascript">
              document.write((${mainMap.AMOUNT}-${mainMap.FREIGHT}).toFixed(2));
          </script></td>
					<td width="10%" align="right">运费金额：</td>
					<td width="20%">${mainMap.FREIGHT}</td>
				</tr>
				<tr>
					<td width="10%" align="right">备注：</td>
					<td colspan="5">${mainMap.REMARK}</td>
				</tr>
			</table>
			<table id="file1" class="table_list"
				style="border-bottom: 1px solid #DAE0EE">
				<tr>
					<th colspan="13" align="left"><img class="nav"
						src="<%=contextPath%>/img/subNav.gif" />配件信息</th>
				</tr>
				<c:if test="${loginUser==2010010100070674 }">
				<tr bgcolor="#FFFFCC">
					<td>序号</td>
					<td>配件编码</td>
					<td>配件名称</td>
					<c:if test="${loginUser!=2010010100070674 }">
						<td>当前库存数量</td>
					</c:if>
<%-- 						<c:if test="${mainMap.STATE=='92161002'||mainMap.STATE=='92161003'}"> --%>
							<td>切换件编码</td>
							<td>切换件名称</td>
<%-- 						</c:if> --%>
						<c:if test="${loginUser==2010010100070674 }">
						<td>当前库存数量</td>
					</c:if>
					<td>切换数量</td>
					<c:if
						test="${mainMap.STATE=='92161003'||mainMap.STATE=='92161013'||mainMap.STATE=='92161014' }">
						<td>审核数量</td>
					</c:if>
					<c:if
						test="${mainMap.STATE=='92161013'||mainMap.STATE=='92161014'}">
						<td>回运数量</td>
						<td>切换件数量</td>
					</c:if>
					<c:if test="${mainMap.STATE=='92161014' }">
						<td>验收入库数量</td>
						<td>切换件验收入库数量</td>
					</c:if>
					<td>备注</td>
				</tr>
				</c:if>
				<c:if test="${loginUser!=2010010100070674 }">
				<tr bgcolor="#FFFFCC">
					<td>序号</td>
					<td>配件编码</td>
					<td>配件名称</td>
					<c:if test="${loginUser!=2010010100070674 }">
						<td>当前库存数量</td>
					</c:if>
					<td>切换数量</td>
					<c:if
						test="${mainMap.STATE=='92161003'||mainMap.STATE=='92161013'||mainMap.STATE=='92161014' }">
						<td>审核数量</td>
					</c:if>
					<c:if
						test="${mainMap.STATE=='92161013'||mainMap.STATE=='92161014'}">
						<td>回运数量</td>
						<td>切换件回运数量</td>
					</c:if>
					<c:if test="${mainMap.STATE=='92161014' }">
						<td>验收入库数量</td>
						<td>切换件验收入库数量</td>
					</c:if>
					<td>切换件编码</td>
					<td>切换件名称</td>
					<c:if test="${loginUser==2010010100070674 }">
						<td>当前库存数量</td>
					</c:if>
					<td>活动配件类型</td>
					<td>是否需要回运</td>
					<td>备注</td>
				</tr>
				</c:if>
				<c:if test="${loginUser!=2010010100070674 }">
				<c:forEach items="${detailList}" var="data">
					<tr class="table_list_row1">
						<td align="center"><script type="text/javascript">
       				getIdx();
				</script></td>
						<td align="center"><c:out value="${data.PART_OLDCODE}" /></td>
						<td align="left"><c:out value="${data.PART_CNAME}" /></td>
						<c:if test="${loginUser!=2010010100070674 }">
							<td align="center">&nbsp;<c:out value="${data.STOCK_QTY}" /></td>
						</c:if>

						<td align="center">&nbsp;<c:out value="${data.BUY_QTY}" /></td>
						<c:if
							test="${mainMap.STATE=='92161003'||mainMap.STATE=='92161013' ||mainMap.STATE=='92161014'}">
							<td align="center">&nbsp;<c:out value="${data.CHECK_QTY}" /></td>
						</c:if>
						<!-- 						回运 -->
						<c:if
							test="${mainMap.STATE=='92161013'||mainMap.STATE=='92161014' }">
							<td align="center">&nbsp;<c:out value="${data.BACKHAUL_QTY}" /></td>
							<td align="center">&nbsp;<c:out value="${data.NBACK_QTY}" /></td>
						</c:if>
						<c:if test="${mainMap.STATE=='92161014' }">
							<td align="center">&nbsp;<c:out value="${data.WAREHOUSING_QTY}" /></td>
							<td align="center">&nbsp;<c:out value="${data.NINSTOCK_QTY}" /></td>
						</c:if>
						<td align="center"><c:out value="${data.REPART_OLDCODE}" /></td>
						<td align="left"><c:out value="${data.REPART_NAME}" /></td>
						<c:if test="${loginUser==2010010100070674 }">
							<td align="center">&nbsp;<c:out
									value="${data.REPART_STOCK_QTY}" /></td>
						</c:if>
						<td align="center">${PART_TYPE}</td>
						 <c:if test="${data.ISNEED_FLAG==10041001}">
						<td align="center">&nbsp;<c:out value="是" /></td>
						</c:if>
												 <c:if test="${data.ISNEED_FLAG==10041002}">
						<td align="center">&nbsp;<c:out value="否" /></td>
						</c:if>
						<td align="left">&nbsp;<c:out value="${data.REMARK}" /></td>
					</tr>
				</c:forEach></c:if>
				<c:if test="${loginUser==2010010100070674 }">
								<c:forEach items="${detailList}" var="data">
					<tr class="table_list_row1">
						<td align="center"><script type="text/javascript">
       				getIdx();
				</script></td>
						<td align="center"><c:out value="${data.PART_OLDCODE}" /></td>
						<td align="left"><c:out value="${data.PART_CNAME}" /></td>
						<c:if test="${!(mainMap.STATE=='92161002')}">
<%-- 						<td align="center">&nbsp;<c:out value="${data.STOCK_QTY}" /></td> --%>
						</c:if>
<%-- 						<c:if test="${loginUser!=2010010100070674 }"> --%>
<%-- 							<td align="center">&nbsp;<c:out value="${data.STOCK_QTY}" /></td> --%>
<%-- 						</c:if> --%>
<%-- 							<c:if test="${mainMap.STATE=='92161002'}"> --%>
							<td align="center"><c:out value="${data.REPART_OLDCODE}" /></td>
							<td align="left"><c:out value="${data.REPART_NAME}" /></td>
													<c:if test="${loginUser==2010010100070674 }">
							<td align="center">&nbsp;<c:out
									value="${data.REPART_STOCK_QTY}" /></td>
						</c:if>
<%-- 						</c:if> --%>
<%-- 						<c:if test="${loginUser==2010010100070674 }"> --%>
<%-- 							<td align="center">&nbsp;<c:out --%>
<%-- 									value="${data.REPART_STOCK_QTY}" /></td> --%>
<%-- 						</c:if> --%>
						<td align="center">&nbsp;<c:out value="${data.BUY_QTY}" /></td>
						<c:if
							test="${mainMap.STATE=='92161003'||mainMap.STATE=='92161013' ||mainMap.STATE=='92161014'}">
							<td align="center">&nbsp;<c:out value="${data.CHECK_QTY}" /></td>
						</c:if>
						<!-- 						回运 -->
						<c:if
							test="${mainMap.STATE=='92161013'||mainMap.STATE=='92161014' }">
							<td align="center">&nbsp;<c:out value="${data.BACKHAUL_QTY}" /></td>
							<td align="center">&nbsp;<c:out value="${data.NBACK_QTY}" /></td>
						</c:if>
						<c:if test="${mainMap.STATE=='92161014' }">
							<td align="center">&nbsp;<c:out value="${data.WAREHOUSING_QTY}" /></td>
							<td align="center">&nbsp;<c:out value="${data.NINSTOCK_QTY}" /></td>
						</c:if>
<%-- 						<td align="left">&nbsp;<c:out value="${data.ISNEED_FLAG}" /></td> --%>
						<td align="left">&nbsp;<c:out value="${data.REMARK}" /></td>
					</tr>
				</c:forEach></c:if>
			</table>
			<table id="file2" class="table_list"
				style="border-bottom: 1px solid #DAE0EE">
				<tr>
					<th colspan="6" align="left"><img class="nav"
						src="<%=contextPath%>/img/subNav.gif" />单据操作信息</th>
				</tr>
				<tr bgcolor="#FFFFCC">
					<td align="center" width="3%">序号</td>
					<td align="center" width="10%">操作人</td>
					<td align="center" width="13%">操作时间</td>
					<td align="center" width="14%">环节</td>
					<td align="center" width="8%">状态</td>

				</tr>
				<c:forEach items="${historyList}" var="data">
					<tr class="table_list_row1">
						<td align="center"><script type="text/javascript">
       				getIdx1();
				</script></td>
						<td align="left"><c:out value="${data.OPT_NAME}" /></td>
						<td align="center"><c:out value="${data.OPT_DATE}" /></td>
						<td align="left"><c:out value="${data.WHAT}" /></td>
						<td align="center">&nbsp; <script type="text/javascript">
					getCode(${data.STATUS});
	  			</script>
						</td>
					</tr>
				</c:forEach>
			</table>
			<table border="0" width="100%">
				<tr align="center" width="100%">
					<td><input class="normal_btn" type="button" value="明细下载"
						onclick="exportDetl()" /> <input class="normal_btn" type="button"
						value="关 闭" onclick="goClose()" /></td>
				</tr>
			</table>
		</div>
	</form>
</body>
</html>

