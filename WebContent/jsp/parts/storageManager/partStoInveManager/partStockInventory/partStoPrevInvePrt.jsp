<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@ page import="com.infodms.dms.common.Constant"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<% String contextPath = request.getContextPath(); %>
<script language="javascript" type="text/javascript">
	function doInit(){
		__extQuery__(1);
	}
</script>
</head>
<body>
  <form method="post" name ="fm" id="fm">
    <input type="hidden" name="changeId" id="changeId" value="${changeId }"/>
	<input type="hidden" name="invType" id="invType" value="${invType }"/>
	<br/>
	<div class="wbox" style="text-align: center; font-size: 20px; font-weight: bold;">
	配件初盘信息 
	</div>
	<br/>
	<!-- 查询条件 end -->
	<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
	<!--分页 end -->
	<br/>
	<div class="wbox" style="text-align: center;">
	  <input class="txtToolBarButton" type="button" value="打 印" onclick="prtPage(this)"/>
	</div>
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
				{header: "当前账面库存", dataIndex: 'ITEM_QTY', align:'center'},
				{header: "备注", dataIndex: 'REMARK', align:'center'}
		      ];

	function prtPage(obj)
	{
		obj.style.display="none";  
		window.print();//打印  
		obj.style.display="";  
	}
  </script>
</body>
</html>