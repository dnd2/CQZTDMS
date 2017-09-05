<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>物料价格维护</title>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：系统管理 > 产品维护 > 物料价格维护</div>
<form method="POST" name="fm" id="fm">
  	<table class="table_query" border="0">
		<tr>
			<td class="table_query_2Col_label_6Letter" nowrap="nowrap">配置代码：</td>
			<td class="table_query_4Col_input" nowrap="nowrap">
				<input class="middle_txt" type="text" name="groupCode" readOnly="readOnly" size="20" id="groupCode" value="" />
				<input name="button3" type="button" class="mini_btn" onclick="showMaterialGroup('groupCode','groupId','false','4','true')" value="..." />
				<input type="hidden" name="groupId" id="groupId" value="" />
				<input class="normal_btn" type="button" value="清空" onclick="clrTxt('groupCode');clrTxt('groupId');"/>
			</td>
		</tr>
		<tr>
		<td class="table_query_2Col_label_6Letter" nowrap="nowrap">价格类型：</td>
			<td class="table_query_4Col_input" nowrap="nowrap">
			<input type="text" value="" id="priceDesc" name="priceDesc" class="maxlong_txt"></input>
			</td>
		</tr>
		<tr>
		<td >&nbsp;</td>
		<td >&nbsp;</td>
			<td colspan="2" class="table_query_4Col_input" style="text-align: center">
			
			<input name="button2" type="hidden" class="normal_btn" onclick="window.location.href='<%=request.getContextPath()%>/sysproduct/productmanage/MaterialPriceManage/materialPriceManageImportPre.do'" value="价格导入" />&nbsp; 
				<input name="button2" type="hidden" class="normal_btn" onclick="loginWindow();" value="类型维护" />
				<input name="queryBtn" type="button" class="normal_btn" onclick="__extQuery__(1);" value="查 询" id="queryBtn" /> &nbsp; 
				<input type="button" class="normal_btn" onclick="reset();" value="重 置"/> &nbsp; 
			</td>
		</tr>
	</table>
  
	<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    <!--分页 end -->
</form>
<script type="text/javascript">

	var myPage;
	//查询路径
	var url = "<%=request.getContextPath()%>/sysproduct/productmanage/MaterialPriceManage/materialPriceManageQuery.json";;
				
	var title = null;

	var columns = [
					{header: "序号", align:'center', renderer:getIndex},
					{header: "配置代码", dataIndex: 'GROUP_CODE', align:'center'},
					{header: "配置名称", dataIndex: 'GROUP_NAME', align:'center'},
					{header: "价格类型", dataIndex: 'PRICE_DESC', align:'center'},
					{header: "销售价格", dataIndex: 'SALES_PRICE', align:'center'},
					{header: "不含税价格", dataIndex: 'NO_TAX_PRICE', align:'center'}
			      ];	
	
	function clrTxt(txtId){
    	document.getElementById(txtId).value = "";
    }
    
    function loginWindow() {
		OpenHtmlWindow("<%=request.getContextPath()%>/sysproduct/productmanage/MaterialPriceManage/materialPriceManageTypeQuery.do",800,500);
	}
</script>
</body>
</html>
