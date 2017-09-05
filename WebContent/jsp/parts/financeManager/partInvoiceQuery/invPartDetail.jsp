<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<% String contextPath = request.getContextPath(); %>
<title>配件零售/领用单</title>
<script language="javascript" type="text/javascript">
	function doInit(){
		__extQuery__(1);
	}
</script>
</head>
<body>
  <form method="post" name ="fm" id="fm">
	<div class="wbox">
		<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />
		  &nbsp;当前位置： 配件财务管理 &gt; 财务开票信息查询 &gt; 查看
		  <input type="hidden" name="billId" id="billId" value="${invMap.BILL_ID }"/>
		</div>
		<table class="table_query">
		  	<th colspan="6"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 基本信息</th>
			<tr>
				<td width="10%" align="right">发票号码：</td>
				<td width="20%">
				${invMap.BILL_NO }
				</td>
				<td width="10%" align="right">开票日期：</td>
				<td width="20%">
				${invMap.BILL_DATE }
				</td>
				<td width="10%" align="right">开票人：</td>
				<td width="20%">
				${invMap.BILL_BY }
				</td>
			</tr>
			<tr>
				<%--<td width="10%" align="right">销售单号：</td>
				<td width="20%">
				  <div style="width:200px; overflow: hidden; white-space: nowrap; text-overflow:ellipsis;">
				  ${invMap.SO_CODE }
				  </div>
				</td>--%>
				<td width="10%" align="right">订货单位：</td>
				<td width="20%">
				${invMap.DEALER_NAME }
				</td>
				<td width="10%" align="right" >开票类型：</td>
				<td width="20%" >
				${invMap.INV_TYPE }
				</td>
			</tr>
			<tr>
				<td width="10%" class="table_query_3Col_label_8Letter">含税开票金额：</td>
				<td width="20%">
				${invMap.BILL_AMOUNT }
				</td>
				<td width="10%" align="right">税额：</td>            
		        <td width="20%">
		        ${invMap.TAX_AMOUNT }
		        </td>
		        <td width="10%" class="table_query_3Col_label_8Letter">无税开票金额：</td>
				<td width="20%">
				${invMap.BILL_AMOUNTNOTAX }
				</td>
	        </tr>
	        <tr>            
		        <td width="10%" align="right">税率：</td>
				<td width="20%">
				${invMap.TAX }
				</td>
		        <td width="10%" align="right">导入时间</td>
				<td width="20%">
				${invMap.CREATE_DATE }
				</td>
				<td width="10%" align="right"></td>
				<td width="20%">
				</td>
	        </tr>
		</table>
	</div>
	<th colspan="4"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" /> 明细信息</th>
	
	<!-- 查询条件 end -->
	<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	<!--分页 end -->
	<table class="table_query">
	  <tr align="center">
	    <td colspan="4">
		  <input class="normal_btn" type="button" value="返 回" onclick="goBack()"/>
		</td>
	  </tr>
	</table>
  </form>
  <script type="text/javascript" >
	var myPage;
	var valType = "query";

	var url = "<%=contextPath%>/parts/financeManager/partInvoiceQuery/invoiceQueryAction/viewInvPartDetail.json?query="+valType;
				
	var title = null;

	var columns = [
				{header: "序号", dataIndex: 'PART_ID', renderer:getIndex,align:'center'},
	//			{header: "配件编码", dataIndex: 'PART_OLDCODE', align:'center'},
			/*	{header: "配件名称", dataIndex: 'PART_CNAME', align:'center'},
				{header: "件号", dataIndex: 'PART_CODE', align:'center'},
				{header: "单位", dataIndex: 'UNIT', align:'center'},
				{header: "销售数量", dataIndex: 'BILL_QTY', align:'center'},
				{header: "税率", dataIndex: 'TAX', align:'center'},
				{header: "折扣率", dataIndex: 'DISCOUNT', align:'center'},
				{header: "含税开票金额(元)", dataIndex: 'BILL_AMUNT', align:'center'},
				{header: "无税开票金额(元)", dataIndex: 'BILL_AMOUNTNOTAX', align:'center'},
				{header: "税额(元)", dataIndex: 'TAX_AMOUNT', align:'center'},*/
                {header: "销售单号", dataIndex: 'SO_CODE', align:'center'},
                {header: "销售金额", dataIndex: 'AMOUNT', align:'center'},
                {header: "销售日期", dataIndex: 'CREATE_DATE', align:'center'}
		      ];

	function goBack(){
		btnDisable();
		var actionURL = "<%=contextPath%>/parts/financeManager/partInvoiceQuery/invoiceQueryAction/invoiceQueryInit.do";
		fm.action = actionURL;
		fm.submit();
	}

	function btnDisable(){

	    $$('input[type="button"]').each(function(button) {
	        button.disabled = true;
	    });
	}

	function btnEnable(){

	    $$('input[type="button"]').each(function(button) {
	        button.disabled = "";
	    });
	}
  </script>
</body>
</html>