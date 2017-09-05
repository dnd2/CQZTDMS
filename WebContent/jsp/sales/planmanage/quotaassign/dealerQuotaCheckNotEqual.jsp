<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>经销商配额导入 </title>
</head>
<body>
<div class="wbox">
	<div class="navigation"><img src="<%=contextPath %>/img/nav.gif" />&nbsp;整车销售 > 计划管理 > 配额调整 > 经销商配额导入 </div>
<table class="table_list" style="border-bottom:1px solid #DAE0EE">
  <tr align="center" class="table_list_th">
    <td colspan="5"><img class="nav" src="<%=contextPath %>/img/subNav.gif" />&nbsp;以下配置的导入数量与车厂下发数量不相符，请调整导入文件后重新导入&nbsp;</td>
  </tr>
  <tr align="center">
    <th width="20%"><strong>配置代码</strong></th>
    <th width="19%"><strong>配置名称</strong></th>
    <th width="19%"><strong>周度</strong></th>
    <th width="22%" ><strong>车厂下发数量</strong></th>
    <th width="20%" ><strong>导入汇总数量</strong></th>
  </tr>
  <c:forEach items="${notEqualList}" var="po" >
	  <tr class="table_list_row1">
	    <td align="center" nowrap="nowrap">${po.GROUP_CODE}</td>
	    <td align="center" >${po.GROUP_NAME}</td>
	    <td nowrap="nowrap">${po.QUOTA_WEEK}</td>
	    <td align="center">${po.AQUOTA_AMT}</td>
	    <td align="center">${po.BQUOTA_AMT}</td>
	  </tr>
  </c:forEach>
</table>

<jsp:include page="${contextPath}/queryPage/orderHidden.html" /></div>
<form>
</form>
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
<form  name="frm" id="frm">
<table class="table_query" width="85%" align="center" border="0"  id="roll">	
	<tr align="center" >
		<th colspan="6">
			<div align="left">
				<input class="cssbutton" type='button' name='saveResButton' onclick='importSave();' value='确定' />
			</div>
		</th>	
  	</tr>
</table>
</form>
<script type="text/javascript">
function importSave() {
	if(submitForm('frm')){
	    var url='<%=contextPath %>/sales/planmanage/QuotaAssign/DealerQuotaImport/dealerQuotaImportPre.do';
		frm.action = url;
		frm.submit();
	}
}
</script>
</body>
</html>
