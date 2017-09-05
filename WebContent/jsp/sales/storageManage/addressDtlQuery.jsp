<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
	String contextPath = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"><%@ page import="com.infodms.dms.common.Constant"%><%@ page import="com.infodms.dms.common.FileConstant"%><%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>经销商地址更改审核</title>
<script type="text/javascript">
<!--
function doInit()
{  
	genLocSel('txt1','txt2','txt3','${province}','${city}','${area}'); // 加载省份城市和县
	document.getElementById("txt1").disabled = "disabled";
	document.getElementById("txt2").disabled = "disabled";
	document.getElementById("txt3").disabled = "disabled";
	setLimitTimeDisplay() ;
}

function setLimitTimeDisplay() {
	var sLimitValue = document.getElementById("limit").value ;
	
	if(sLimitValue == ${conMap.perp}) {
		setStyleDisplay("limitLable", "none") ;
	} else if(sLimitValue == ${conMap.temp}) {
		setStyleDisplay("limitLable", "inline") ;
	} else {
		setStyleDisplay("limitLable", "none") ;
	}
}

function setStyleDisplay(objId, paramValue) {
	document.getElementById(objId).style.display = paramValue ;
}
//-->
</script>
</head>
<body>
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置：经销商地址更改审核</div>
<form id="fm" name="fm" method="post">
<input type="hidden" name="curPage" id="curPage" value="1" /> <input type="hidden" id="dlrId" name="dlrId" value="" />
<table class="table_query" border="0">
	<tr>
		<td width="20%" class="tblopt">
		<div class="right">经销商名称：</div>
		</td>
		<td width="20%">
			${addressInfo.DEALER_SHORTNAME }
		</td>
		<td  width="20%"></td>
		<td  width="40%"></td>
	</tr>
	<tr>
		<td width="20%" class="tblopt">
		<div class="right">业务范围：</div>
		</td>
		<td width="20%">
			${addressInfo.AREA_NAME }
		</td>
		<td  width="20%"></td>
		<td  width="40%"></td>
	</tr>
	<tr>
		<td class="tblopt">
			<div class="right">时限：</div>
			<input type="hidden" name="limit" id="limit" value="${addressInfo.LIMIT_TYPE }" />
		</td>
		<td><script>document.write(getItemValue(${addressInfo.LIMIT_TYPE }));</script></td>
		<td></td>
		<td></td>
	</tr>
	<tr id="limitLable">
		<td class="tblopt">
			<div class="right">有效期：</div>
		</td>
		<td>${addressInfo.START_TIME }&nbsp;至&nbsp;${addressInfo.END_TIME }</td>
		<td></td>
		<td></td>
	</tr>
	<tr>
		<td class="tblopt">
			<div class="right">地址代码：</div>
		</td>
		<td width="60%">${addressInfo.ADD_CODE }</td>
		<td></td>
		<td  width="40%"></td>
	</tr>
	<tr>
		<td class="tblopt">
			<div class="right">街道地址：</div>
		</td>
		<td>${addressInfo.ADDRESS }</td>
		<td></td>
		<td></td>
	</tr>
	<tr>
		<td class="tblopt">
			<div class="right">联系人：</div>
		</td>
		<td>${addressInfo.LINK_MAN }</td>
		<td></td>
		<td></td>
	</tr>
	<tr>
		<td class="tblopt">
			<div class="right">电话：</div>
		</td>
		<td>${addressInfo.TEL }</td>
		<td></td>
		<td></td>
	</tr>
	<tr>
		<td class="tblopt">
			<div class="right">收货单位：</div>
		</td>
		<td colspan="3">${addressInfo.RECEIVE_ORG }</td>
	</tr>
	<tr>
	    <td class="tblopt"><div class="right">省份：</div></td>
	    <td><select class="u-select" id="txt1" name="province" onchange="_genCity(this,'txt2')"></select> </td>
        <td></td>
	    <td></td>
	</tr>
	<tr>
	     <td class="tblopt"><div class="right">地级市：</div></td>
	    <td><select class="u-select" id="txt2" name="city"  onchange="_genCity(this,'txt3')" ></select></td>  
       <td></td>
	    <td></td>
	</tr>
	<tr>
	    <td class="tblopt"><div class="right">区县：</div></td>
	    <td><select class="u-select" id="txt3" name="area" ></select></td>
	    <td></td>
	    <td></td>
	</tr>
	<tr>
		<td class="tblopt">
			<div class="right">备注：</div>
		</td>
		<td colspan="3">${addressInfo.REMARK }</td>
		<td></td>
	    <td></td>
	</tr>
	<tr>
		<td class="tblopt">
			<div class="right">发运地址用途：</div>
		</td>
		<td colspan="3">${addressInfo.ADDRESS_USE }</td>
		<td></td>
	    <td></td>
	</tr>
	</table>
	<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
		<tr>
	        <th colspan="4">&nbsp;审批记录</th>
		</tr>
		<tr align="center">
			<td>审核人员</td>
			<td>审批状态</td>
			<td>审批描述</td>
			<td>审批时间</td>
		</tr>
		<c:forEach items="${logList}" var="list3">
			<tr class="table_list_row2" align="center">
				<td>${list3.NAME}</td>
				<td>
					<script>document.write(getItemValue(${list3.CHECK_STATUS }));</script>
				</td>
				<td>${list3.CHECK_DESC}</td>
				<td>${list3.CREATE_DATE}</td>
			</tr>
		</c:forEach>
	</table>
	<br />
	<table align="center">
	<tr>
		<td align="center" colspan="2">
			<input type="button" class="normal_btn" onclick="history.back();" value="返回" id="queryBtn" />
			<input type="hidden" id="addressId" name="addressId" value="${addressInfo.ID }" />
		</td>
	</tr>
</table>
<jsp:include page="${contextPath}/queryPage/orderHidden.html" /> 
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
</div>
</body>
</html>