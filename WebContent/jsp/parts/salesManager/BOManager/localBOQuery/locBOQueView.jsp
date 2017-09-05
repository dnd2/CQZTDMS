<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<% String contextPath = request.getContextPath(); %>
<title>现场BO单查询</title>
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
		  &nbsp;当前位置： 配件销售管理 &gt; 现场BO单查询 &gt; 查看
		  <input type="hidden" name="parentOrgId" id="parentOrgId" value="${parentOrgId }"/>
		  <input type="hidden" name="parentOrgCode" id="parentOrgCode" value="${parentOrgCode }"/>
		  <input type="hidden" value="${map.BO_ID}" name="boId" id="boId"/>
		  <input type="hidden" value="view" name="optionType" id="optionType"/>
		</div>
		<table class="table_query">
		  <th colspan="6"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" />配件订单信息</th>
		  <tr>
	        <td width="10%" class="table_query_2Col_label_4Letter" >现场BO单号：</td>
		    <td width="20%">
		        &nbsp; ${map.BO_CODE}
		    </td>
	      	<td width="10%" class="table_query_2Col_label_4Letter" >BO单状态：</td>
		    <td width="20%">
		      &nbsp; ${map.STATE}
		    </td>
	        <td width="10%" class="table_query_2Col_label_4Letter" >现场BO日期：</td>
		    <td width="20%">
		      &nbsp; ${map.BM_DATE}
		    </td>
	      </tr>
		  <tr>
		      <td width="10%" class="table_query_2Col_label_4Letter" >订单号：</td>
		      <td width="20%" >
		       &nbsp; ${map.ORDER_CODE}
		      </td>
		      <td width="10%" class="table_query_2Col_label_4Letter" >订单类型：</td>
		      <td width="20%" >
		       &nbsp; ${map.ORDER_TYPE}
		      </td>
		      <td width="10%" class="table_query_2Col_label_4Letter" >订货日期：</td>
		      <td width="20%">&nbsp; ${map.OM_DATE}</td>
	      </tr>
	      <tr>
	      	<td width="10%" class="table_query_2Col_label_4Letter">销售单号：</td>
			<td width="20%">
			  &nbsp; ${map.SO_CODE}
			</td>
	        <td width="10%" class="table_query_2Col_label_4Letter">出库单号：</td>
			<td width="20%">
			  &nbsp; ${map.OUT_CODE}
			</td>
			<td width="10%" class="table_query_2Col_label_4Letter">出库仓库：</td>
			<td width="20%">
			  &nbsp; ${map.WH_CNAME}
			</td>
	      </tr>
	      <tr>
	        <td width="10%" class="table_query_2Col_label_4Letter" >订货单位：</td>
		    <td width="20%" >
		      &nbsp; ${map.DEALER_NAME}
		      <input type="hidden" value="${map.RESULT_ID}" name="resultId" id="resultId"/>
		    </td>
	        <td width="10%" class="table_query_2Col_label_4Letter" >销售单位：</td>
		    <td width="20%" >
		      &nbsp; ${map.SELLER_NAME}
		    </td>
	        <td width="10%" class="table_query_2Col_label_4Letter" >接收单位：</td>
		    <td width="20%">
		      &nbsp; ${map.RCV_ORG}
		    </td>
	      </tr>
	      <tr>
	        <td width="10%" class="table_query_2Col_label_4Letter" >BO单备注：</td>
		    <td width="80%" colspan="5">
		      &nbsp; ${map.REMARK}
		    </td>
	      </tr>
		</table>
		<table class="table_query">
		  <th colspan="6"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" />操作</th>
		  <tr>
		    <td align="center">
			  <input class="normal_btn" type="button" value="返 回" onclick="goBack()"/>
			</td>
		  </tr>
		</table>
	</div>
	<th colspan="4"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" />现场BO明细信息</th>
	
	<!-- 查询条件 end -->
	<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	<!--分页 end -->
  </form>
  <script type="text/javascript" >
	var myPage;

	var url = "<%=contextPath%>/parts/salesManager/BOManager/locBOQueAction/partLocBODetailSearch.json";
				
	var title = null;

	var columns = [
				{header: "序号", dataIndex: 'BOLINE_ID', renderer:getIndex,align:'center'},
				{header: "件号", dataIndex: 'PART_CODE', align:'center'},
				{header: "配件编码", dataIndex: 'PART_OLDCODE', align:'center'},
				{header: "配件名称", dataIndex: 'PART_CNAME', align:'center'},
				{header: "单位", dataIndex: 'UNIT'},
				{header: "订单单价(元)", dataIndex: 'BUY_PRICE', align:'center'},
				{header: "订货数量", dataIndex: 'BUY_QTY', align:'center'},
				{header: "订货金额(元)", dataIndex: 'BUY_SUM', align:'center'},
				{header: "出库数量", dataIndex: 'SALES_QTY', align:'center'},
				{header: "出库金额(元)", dataIndex: 'SALES_SUM', align:'center'},
				{header: "现场BO数量", dataIndex: 'BO_QTY', align:'center'},
				{header: "现场BO金额(元)", dataIndex: 'BO_SUM', align:'center'},
				{header: "备注", dataIndex: 'REMARK', align:'center'},
				{header: "是否关闭", dataIndex: 'STATUS', align:'center', renderer:retuIsSolve}
		      ];

	function retuIsSolve(value,meta,record)
	{
		var unClosed = "1";
		var closed = "0";
		var str = "";
		if(unClosed == value)
		{
			str = "<font>未关闭</font>";
		}
		else if(closed == value)
		{
			str = "<font color='red'>已关闭</font>";
		}
		return String.format(str);
	}
	
	function goBack(){
		btnDisable();
		fm.action = "<%=contextPath%>/parts/salesManager/BOManager/locBOQueAction/locBOQueInit.do";
		fm.submit();
	}

	//失效按钮
	function btnDisable(){

	    $$('input[type="button"]').each(function(button) {
	        button.disabled = true;
	    });

	}

	//有效按钮
	function btnEnable(){

	    $$('input[type="button"]').each(function(button) {
	        button.disabled = "";
	    });

	}
  </script>
</body>
</html>