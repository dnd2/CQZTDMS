<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>


<%@ page import="java.util.*"%>

<jsp:include page="${path}/common/jsp_head_new.jsp" />
<%
	String contextPath = request.getContextPath();
	//String expStatusCode = Constant.ORDER_STATUS_00 + "," + Constant.ORDER_STATUS_01 + "," + Constant.ORDER_STATUS_03;
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title>挂账单查询</title>
<script type="text/javascript">
	
	//清空数据
	function txtClr(valueId1,valueId2) {
		document.getElementById(valueId1).value = '' ;
		document.getElementById(valueId2).value = '' ;
	}
</script>
</head>
<body>
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;当前位置：储运管理>结算管理>挂账单查询</div>
	
	<form method="post" name="fm" id="fm">
	<div class="form-panel">
	<h2><img src="<%=request.getContextPath()%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>挂账单查询</h2>
	<div class="form-body">
		<table class="table_query">
		    <tr>
		       <!-- 
		       <td  class="right">挂账单状态：</td>
		       <td>
		       	 <script type="text/javascript">
						genSelBoxExp("status",<%=Constant.BAL_ORDER_STATUS%>,"-1",true,"u-select",'',"false",'');
				</script>
		       </td> -->
		       <td  class="right">承运商：</td>
		       <td>
		         <select name="logi_id" id="logi_id" class="u-select" >
		 		<option value="">-请选择-</option>
				<c:if test="${list_logi!=null}">
					<c:forEach items="${list_logi}" var="list_logi">
						<option value="${list_logi.LOGI_ID}">${list_logi.LOGI_NAME}</option>
					</c:forEach>
				</c:if>
		       </td>
				<td class="right">挂账单号：</td>
				<td>
				   <input type="text" name="bal_no" id="bal_no" class="middle_txt" />
				</td>
			</tr>
			<tr>
				<td colspan="4" class="table_query_4Col_input" style="text-align: center">
					<input type="button" value="查询" id="queryBtn" class="u-button u-query" onclick="__extQuery__(1);"/>
					&nbsp;
					<input type="reset" id="resetButton" class="u-button u-reset" value="重置"/>
					&nbsp;
					<input type="button" value="导出" class="normal_btn" onclick="downLoad();"/>
				</td>
			</tr>
		</table>
	</div>
</div>
		<!-- 分页 begin -->
		<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
		<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
		<!-- 分页 end -->
	</form>

	<script>
		var myPage;
	
		var url = "<%=request.getContextPath()%>/sales/storage/balancemanage/SalesBalanceAction/billQueryList.json?query=query";
	
		var title = null;
	
		//设置列名属性
		var columns = [
				{header: "序号", dataIndex: '', align:'center', renderer:getIndex},
				{header: "操作", dataIndex: 'BAL_ID', align:'center',renderer:mylink},
				{header: "交接单号", dataIndex: 'BILL_NO', align:'center'},
				 {
					header: "挂账单号", dataIndex: 'BAL_NO', align:'center',
					renderer: function(value, metaData, record) {
						var url = '<%=contextPath%>/sales/storage/balancemanage/SalesBalanceAction/showBalanceView.do?balId=' + record.data.BAL_ID;
						return "<a href='javascript:void(0);' onclick='viewOrderInfo(\""+url+"\")'>"+value+"</a>";
					}
				},
				 {header: "承运商", dataIndex: 'LOGI_NAME', align:'center'},
				 {header: "承运商代码", dataIndex: 'LOGI_CODE', align:'center'},
				 {header: "挂账量", dataIndex: 'BAL_COUNT', align:'center'},
				 {header: "挂账日期", dataIndex: 'BAL_DATE', align:'center'},
				 {header: "挂账月份", dataIndex: 'BAL_MONTH', align:'center'},
				 {header: "详细收货地址", dataIndex: 'ADDRESS_INFO', style:'text-align:left;'},
				 {header: "结算地址", dataIndex: 'BALANCE_ADDRESS', align:'center'},
				 {header: "状态", dataIndex: 'STATUS_NAME', align:'center'},
				 {header: "挂账金额", dataIndex: 'BAL_AMOUNT', align:'center'},
				 {header: "交接单挂账金额", dataIndex: 'BILL_AMOUNT', align:'center'}
		];
		
		function mylink(value,meta,record){
			var str = "<a href='javascript:void(0);' class='u-anchor' onclick='exportDtl("+value+");'>明细导出</a>";
			return String.format(str);
		}
		
		function viewOrderInfo(url)
		{
			OpenHtmlWindow(url,1000,450);
		}
		function downLoad() 
		{
			document.getElementById('fm').action= "<%=request.getContextPath()%>/sales/storage/balancemanage/SalesBalanceAction/billQueryList.do?query=download";
			document.getElementById('fm').submit();
		}
		//明细导出
		function exportDtl(value) 
		{
			document.getElementById('fm').action= "<%=request.getContextPath()%>/sales/storage/balancemanage/SalesBalanceAction/billExportDtl.do?balId="+value;
			document.getElementById('fm').submit();
		}
	</script>
</body>
</html>


