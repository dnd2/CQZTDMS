<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<% String contextPath = request.getContextPath(); %>
<title>库存盘点</title>
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
		  &nbsp;当前位置： 配件仓库管理&gt;库存盘点管理&gt;库存盘点&gt;库存初盘打印
		  <input type="hidden" name="changeId" id="changeId" value="${changeId }"/>
		  <input type="hidden" name="invType" id="invType" value="${invType }"/>
		  <input type="hidden" name="optType" id="optType" value="prevPrint"/>
		</div>
		<table class="table_query">
		  <th style="text-align: center;">配件初盘信息</th>
		  <tr>
		   <td align="center">
		     <input type="button" class="normal_btn" value="导 出" onclick="exportPartStockExcel()" >
		   </td>
		  </tr>
		</table>
	</div>
	
	<!-- 查询条件 end -->
	<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	<!--分页 end -->
	<table class="table_query">
	  <tr align="center">
	    <td colspan="4">
	      <input class="normal_btn" type="button" value="打印页面" onclick="printInfos()"/>
		  <input class="normal_btn" type="button" value="返 回" onclick="goBack()"/>
		</td>
	  </tr>
	</table>
  </form>
  <script type="text/javascript" >
	var myPage;
	var pageMark = "print";

	var url = "<%=contextPath%>/parts/storageManager/partStoInveManager/stockInventoryAction/partStockDetailSearch.json?pageMark=" + pageMark;
				
	var title = null;

	var columns = [
				{header: "序号", dataIndex: 'PART_ID', renderer:getIndex,align:'center'},
				{header: "盘点单号", dataIndex: 'CHANGE_CODE', align:'center'},
				{header: "盘点仓库", dataIndex: 'WH_CNAME', align:'center'},
				{header: "件号", dataIndex: 'PART_CODE', align:'center'},
				{header: "配件编码", dataIndex: 'PART_OLDCODE', align:'center'},
				{header: "配件名称", dataIndex: 'PART_CNAME', align:'center'},
				{header: "单位", dataIndex: 'UNIT'},
				{header: "货位", dataIndex: 'LOC_NAME', align:'center'},
				{header: "账面库存", dataIndex: 'ITEM_QTY', align:'center'},
				{header: "备注", dataIndex: 'REMARK', align:'center'}
		      ];

	function goBack(){
		btnDisable();
		fm.action = "<%=contextPath%>/parts/storageManager/partStoInveManager/stockInventoryAction/stockInventoryInit.do";
		document.fm.target="_self";
		fm.submit();
	}

	//下载
	function exportPartStockExcel(){
		document.fm.action="<%=contextPath%>/parts/storageManager/partStoInveManager/stockInventoryAction/expPrevInvExcel.do";
		document.fm.target="_self";
		document.fm.submit();
	}

	//打印页面
	function printInfos()
	{
		document.fm.action="<%=contextPath%>/parts/storageManager/partStoInveManager/stockInventoryAction/prevOrAginInveInit.do";
		document.fm.target="_blank";
		document.fm.submit();
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