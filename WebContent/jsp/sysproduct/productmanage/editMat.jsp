<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<%
	String contextPath = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>物料修改</title>
<script type="text/javascript" >
function _save()
{ 
	makeNomalFormCall("<%=contextPath%>/sysproduct/productmanage/MaterialPriceMaintenance/editMat.json",editMatBack,'fm','queryBtn'); 
}

function editMatBack(json)
{
	if(json.returnValue == 1)
	{
		_hide();
		parent.MyAlert("操作成功！");
		parentContainer._seah();
	}else
	{
		_hide();
		parent.MyAlert("操作失败！");
		parentContainer._seah();
	}
}
</script>
</head>
<body onunload='javascript:destoryPrototype();'>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;物料修改</div>
<form name="fm" method="post" id="fm">
<table class="table_list">
	<tr>
		<th align="right">物料代码：</th>
		<th align="left">${map.MATERIAL_CODE }</th>
		
	</tr>
	<tr>
		<th align="right">物料名称：</th>
		<th align="left">${map.MATERIAL_NAME }</th>
	</tr>
	<tr>
		<th align="right">物料价格：</th>
		<th align="left"><input name="VHCL_PRICE" id="VHCL_PRICE" datatype="0,is_money_z,10"  value="${map.VHCL_PRICE }"/>
		<input type="hidden" name="MATERIAL_ID" id="MATERIAL_ID" value="${map.MATERIAL_ID }"/></th>
	</tr>
</table>
<table  class="table_query">
    <tr align="center">      
      <td>
      	<input class="normal_btn" type="button" name="button" value="保存"  onclick="_save();" />
      	<input class="normal_btn" type="button" name="button" value="关闭"  onclick="_hide();" />
      </td>
  	</tr>	
   </table> 
   </form> 
</div>
</body>
</html>