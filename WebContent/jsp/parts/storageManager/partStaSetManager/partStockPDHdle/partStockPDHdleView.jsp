<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<% String contextPath = request.getContextPath(); %>
<title>库存盘点调整查询</title>
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
		  &nbsp;当前位置： 配件仓库管理&gt;库存状态变更&gt;库存盘点封存处理&gt;查看
		  <input type="hidden" name="parentOrgId" id="parentOrgId" value="${parentOrgId }"/>
		  <input type="hidden" name="parentOrgCode" id="parentOrgCode" value="${parentOrgCode }"/>
		  <input type="hidden" value="${map.HANDLE_TYPE}" name="resolveType" id="resolveType"/>
		</div>
		<table class="table_query">
		  <th colspan="6"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" />基本信息</th>
		  <tr>
			<td width="10%"   align="right">盘点单号：</td>
			<td width="20%">
			  ${map.CHANGE_CODE}
			</td>
			<td width="10%"   align="right">导入人：</td>
			<td width="20%">
			  ${map.IMP_NAME}
			</td>
			<td width="10%"   align="right">导入日期：</td>
			<td width="20%">
			  ${map.CREATE_DATE}
			</td>
		  </tr>
		  <tr>
			<td width="10%"   align="right">申请单号：</td>
			<td width="20%">
			  ${map.RESULT_CODE}
		      <input type="hidden" value="${map.RESULT_ID}" name="resultId" id="resultId"/>
			</td>
			<td width="10%"   align="right">提交人：</td>
			<td width="20%">
			  ${map.COMM_NAME}
			</td>
			<td width="10%"   align="right">提交日期：</td>
			<td width="20%">
			  ${map.COMMIT_DATE}
			</td>
		  </tr>
		  <tr>
			<td width="10%"   align="right">盘点仓库：</td>
			<td width="20%">
			  ${map.WH_CNAME}
			</td>
			<td width="10%"   align="right">审核人：</td>
			<td width="20%">
			  ${map.CHE_NAME}
			</td>
			<td width="10%"   align="right">审核日期：</td>
			<td width="20%">
			  ${map.CHECK_DATE}
			</td>
		  </tr>
		  <tr>
			<td width="10%"   align="right">盘点类型：</td>
			<td width="20%">
			  ${map.CHECK_TYPE}
			</td>
			<td width="10%"   align="right">处理人：</td>
			<td width="20%">
			  ${map.HAN_NAME}
			</td>
			<td width="10%"   align="right">处理日期：</td>
			<td width="20%">
			  ${map.HANDLE_DATE}
			</td>
		  </tr>
		  <tr>
			<td width="10%"   align="right">是否完全处理：</td>
			<td width="20%">
			  ${map.JF_STATE}
			</td>
			<td width="10%"   align="right"></td>
			<td width="20%"></td>
			<td width="10%"   align="right"></td>
			<td width="20%"></td>
		  </tr>
		</table>
		<table class="table_query">
		  <th colspan="6"><img class="nav" src="<%=request.getContextPath()%>/img/subNav.gif" />盘点结果明细</th>
		</table>
	</div>
	
	<!-- 查询条件 end -->
	<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	<!--分页 end -->
	<table class="table_query">
	  <tr>
	    <td align="center">
		  <input class="normal_btn" type="button" value="返 回" onclick="goBack()"/>
		</td>
	  </tr>
	</table>
  </form>
  <script type="text/javascript" >
	var myPage;

	var url = "<%=contextPath%>/parts/storageManager/partStaSetManager/partStockPDHdleAction/partStockDetailSearch.json";
				
	var title = null;

	var columns = [
				{header: "序号", dataIndex: 'PART_ID', renderer:getIndex,align:'center'},
				{header: "配件编码", dataIndex: 'PART_OLDCODE', style:'text-align: left;'},
				{header: "配件名称", dataIndex: 'PART_CNAME', style:'text-align: left;'},
				{header: "件号", dataIndex: 'PART_CODE',style:'text-align: left;'},
				{header: "单位", dataIndex: 'UNIT'},
				{header: "账面库存", dataIndex: 'ITEM_QTY', align:'center'},
				{header: "盘点库存", dataIndex: 'CHECK_QTY', align:'center'},
				{header: "盈亏库存", dataIndex: 'DIFF_QTY', align:'center'},
				{header: "盘点结果", dataIndex: 'CHECK_RESULT', align:'center',renderer:getItemValue},
				{header: "备注", dataIndex: 'REMARK', style:'text-align: left;'},
				{header: "处理方式", dataIndex: 'HANDLE_TYPE', align:'center',renderer:resolveTypes},
				{header: "是否处理", dataIndex: 'JF_STATE', align:'center',renderer:getItemValue},
				{header: "已处理数量", dataIndex: 'JF_QTY', align:'center'},
				{header: "可处理数量", dataIndex: 'UN_JF_QTY', align:'center'},
				{header: "处理原因", dataIndex: 'JF_REMARK', style:'text-align:left;'}
		      ];

	function resolveTypes(value,meta,record)
	{
		var detailId = record.data.DTL_ID;
		var checkResult = record.data.CHECK_RESULT;
		var inveProfile = <%=Constant.PART_STOCK_STATUS_BUSINESS_TYPE_02 %>;
		var inveLosses = <%=Constant.PART_STOCK_STATUS_BUSINESS_TYPE_03 %>;
		
		var resolveType = document.getElementById("resolveType").value;
		var str = "";

		if("" == resolveType)
		{
			str = "";
		}
		else if("处理" != resolveType)
		{
			str = "封 存";
		}
		else if(inveProfile == checkResult)
		{
			str = "入 库";
		}
		else if(inveLosses == checkResult)
		{
			str = "出 库";
		}
		
		return String.format(str);
	}
	
	function handleState(value,meta,record)
	{
		var isOver = "0";
		var str = "";
		if(isOver == value)
		{
			str = "未处理";
		}
		else
		{
			str = "<font color='red'>已处理</font>";
		}
		return String.format(str);
	}
	
	function goBack(){
		btnDisable();
		//window.location = "<%=contextPath%>/parts/storageManager/partStaSetManager/partStockPDHdleAction/partStockPDHdleInit.do";
		window.history.back();
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