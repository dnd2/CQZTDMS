<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
	String contextPath = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<script type="text/javascript">
	function doInit(){
   		
	}
</script>
<title>销售单信息</title>
</head>
<body onunload='javascript:destoryPrototype();' onload="ChangeDateToString() ;">
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：
采购管理 &gt; 库存管理 &gt; 车辆验收 &gt; 销售单信息</div>
<table class="table_query" border="0">
 <th colspan="11"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;销售单信息</th>
	<tr>
		<td width="20%" class="tblopt" class="right">
			<span>销售单号：</span>
		</td>
		<td width="20%">
			<span id="sendcarId">${dlvryErp.sendcarOrderNumber }</span>
		</td>
		<td width="20%" class="tblopt" class="right">
			<span>承载单位编号：</span>
		</td>
		<td width="20%">
			<span id="shMeCode">${dlvryErp.shipMethodCode }</span>
		</td>
	</tr>
	<tr>
		<td width="20%" class="tblopt"  class="right">
			<span>司机名称：</span>
		</td>
		<td width="20%">
			<span id="shMeCode">${dlvryErp.motorman }</span>
		</td>
		<td width="20%" class="tblopt"  class="right">
			<span>驾驶员电话号码：</span>
		</td>
		<td width="20%">
			<span id="shMeCode">${dlvryErp.motormanPhone }</span>
		</td>
	</tr>
	<tr>
		<td width="20%" class="tblopt"  class="right">
			<span>发运车车牌号：</span>
		</td>
		<td width="20%">
			<span id="shMeCode">${dlvryErp.flatcarId }</span>
		</td>
		<td width="20%" class="tblopt"  class="right">
			<span>发运数量：</span>
		</td>
		<td width="20%">
			<span id="shMeCode">${dlvryErp.sendcarAmount }</span>
		</td>
	</tr>
</table>
<br />
<br />
<table class="table_query" border="0">
<tr>
<th align="center">物料代码</th>
<th align="center">物料名称</th>
<th align="center">VIN</th>
<th align="center">发动机号</th>
</tr>
	<c:forEach var="dlvryErpDltList" items="${dlvryErpDltList }">
		<tr>
			<td>${dlvryErpDltList.MATERIAL_CODE }</td>
			<td>${dlvryErpDltList.MATERIAL_NAME }</td>
			<td>${dlvryErpDltList.VIN }</td>
			<td>${dlvryErpDltList.ENGINER_ID }</td>
		</tr>
	</c:forEach>
</table>
<br />
<table class="table_query" width="90%" border="0" align="center">
	<tr>
		<td align="center"><input name="button" type="button"
			class="normal_btn" onclick="_hide() ;" value="关闭" />
		</td>
	</tr>
</table>
</div>
</body>
</html>