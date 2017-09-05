<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
	String contextPath = request.getContextPath();
	String isSpecialCar = String.valueOf(request.getAttribute("isSpecialCar"));
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>经销商新增地址申请--已维护地址列表</title>
<script type="text/javascript">
function doInit()
{  
	
}
</script>
</head>
<body>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;已维护地址信息列表</div>
<form id="fm" name="fm" method="post">
<input type="hidden" name="address_s" id="address_s" value="${address_s }" />
<input type="hidden" name="curPage" id="curPage" value="1" /> <input type="hidden" id="dlrId" name="dlrId" value="" />
<table class="table_list" border="0" id="activeTable">
	<c:if test="${addList!=null}">
	<tr class="table_list_th">
		<th>选择</th>
		<th>状态</th>
		<th>地址名称</th>
		<th>联系人</th>
		<th>联系电话</th>
		<th>省</th>
		<th>市</th>
		<th>县</th>
		<th>备注</th>
	</tr>
	<c:forEach items="${addList}" var="addList" varStatus="vstatus">
		<tr align="center" class="table_list_row2">
			<td><input type="radio" name="addr" onclick="selAddr(${vstatus.index});" /></td>
			<td><script>document.write(getItemValue(${addList.STATUS}));</script></td>
			<td>${addList.ADDRESS}</td>
			<td>${addList.LINK_MAN}</td>
			<td>${addList.TEL}</td>
			<td>${addList.PROVINCE_NAME}</td>
			<td>${addList.CITY_NAME}</td>
			<td>${addList.AREA_NAME}</td>
			<td><span id="remark${vstatus.index}">${addList.REMARK}</span>
				<span id="province${vstatus.index}" style="display: none;">${addList.PROVINCE_ID}</span>
				<span id="city${vstatus.index}" style="display: none;">${addList.CITY_ID}</span>
				<span id="area${vstatus.index}" style="display: none;">${addList.AREA_ID}</span>
			</td>
		</tr>
	</c:forEach>
	</c:if>
	
</table>
</form>
</div>
<script type="text/javascript">

	function selAddr(value){
		var tab = document.getElementById("activeTable");
		var address_s = document.getElementById("address_s").value ;
		var aAddress = address_s.split(",") ;
		
		parent.document.getElementById('inIframe').contentWindow.document.getElementById("address").value = aAddress[value];				//地址名称
		parent.document.getElementById('inIframe').contentWindow.document.getElementById("linkMan").value = tab.rows[value+1].cells[3].innerHTML;				//联系人
		parent.document.getElementById('inIframe').contentWindow.document.getElementById("tel").value = tab.rows[value+1].cells[4].innerHTML;					//电话
		parent.document.getElementById('inIframe').contentWindow.document.getElementById("remark").value = document.getElementById("remark"+value).innerHTML;	//备注
		var province = document.getElementById("province"+value).innerHTML;	//省
		var city = document.getElementById("city"+value).innerHTML;			//市
		var area = document.getElementById("area"+value).innerHTML			//县
		parent.document.getElementById('inIframe').contentWindow.province(province,city,area);
		parent._hide();
	}
	
	
</script>
</body>
</html>