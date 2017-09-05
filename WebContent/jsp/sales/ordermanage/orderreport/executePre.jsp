<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>历史数据导入</title>
<script type="text/javascript"><!--

function importSave(){
	disableBtn($("queryBtn"));
	makeNomalFormCall('<%=request.getContextPath()%>/sales/ordermanage/orderreport/ErpDataImport/execute.json',showResult,'fm');
}

function showResult(json){
	if(json.returnValue == '1'){
		MyAlert("导入成功！");
	}else{
		MyAlert("导入失败！");
	}
	useableBtn($("queryBtn"));
}


--></script>
</head>
<body>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置：整车销售 > 销售订单管理 > 订单提报 > 历史数据导入</div>
<form method="POST" name="fm" id="fm">
  <TABLE class=table_query>
    <tr class=cssTable >
      <td align=left>&nbsp;</td>
      <td colspan="3" align=left>
	      <input class="cssbutton" id="queryBtn" name="baocun" type="button"  value="导入" onClick="importSave();">
      </td>
    </tr>
 </table>
  <BR>
</form>
</body>
</html>
