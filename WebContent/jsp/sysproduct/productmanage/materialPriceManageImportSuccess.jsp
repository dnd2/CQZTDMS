<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>物料价格维护</title>
</head>
<body onunload='javascript:destoryPrototype()' onload="loadcalendar();showSub();">
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：系统管理>产品维护>物料价格导入</div>
<form method="POST" name="fm" id="fm">
	<!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
    <!--分页 end -->
	<table class="table_query" width="85%" align="center" border="0"  id="roll">	
		<tr align="center" >
			<th colspan="6">
				<div align="left">
					<input class="cssbutton" type="button" name='saveResButton' onclick='importSave();' value='保存' />
					<input class="cssbutton" type='button' name='saveResButton' onclick='history.back();' value='返回' />
				</div>
			</th>	
	  	</tr>
	</table>
</form>
<script type="text/javascript">
	document.getElementById("roll").style.display = "none";
	var HIDDEN_ARRAY_IDS=['roll'];
	
	var myPage;
	//查询路径
	var url = "<%=request.getContextPath()%>/sysproduct/productmanage/MaterialPriceMaintenanceImport/materialPriceManageTempQuery.json";
				
	var title = null;

	var columns = [
				{header: "行号", dataIndex: 'NUMBER_NO', align:'center'},
				{header: "物料代码", dataIndex: 'GROUP_CODE', align:'center'},
				{header: "物料价格", dataIndex: 'SALES_PRICE', align:'center'},
				{header: "当前用户", dataIndex: 'NAME', align:'center'}
		      ];	
	
	function doInit(){
		__extQuery__(1);
	}
	
	function importSave() {
		if(submitForm('fm')){
			fm.action = "<%=request.getContextPath() %>/sysproduct/productmanage/MaterialPriceMaintenanceImport/importExcel.do";
			fm.submit();
		}
	}
</script>
</body>
</html>
