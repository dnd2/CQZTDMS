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
function doInit()
{  
	genLocSel('txt1','txt2','txt3','${province}','${city}','${area}'); // 加载省份城市和县
	document.getElementById("txt1").disabled = "disabled";
	document.getElementById("txt2").disabled = "disabled";
	document.getElementById("txt3").disabled = "disabled";
}
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
		<td class="tblopt">
			<div class="right">地址代码：</div>
		</td>
		<td>${addressInfo.ADD_CODE }</td>
		<td class="tblopt">
			<div class="right">地址名称：</div>
		</td>
		<td>${addressInfo.ADDRESS }</td>
	</tr>
	<tr>
		<td class="tblopt">
			<div class="right">联系人：</div>
		</td>
		<td>${addressInfo.LINK_MAN }</td>
		<td class="tblopt">
			<div class="right">电话：</div>
		</td>
		<td>${addressInfo.TEL }</td>
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
        <td class="tblopt"><div class="right">地级市：</div></td>
	    <td><select class="u-select" id="txt2" name="city"  onchange="_genCity(this,'txt3')" ></select></td>  
	</tr>
	<tr>
	    <td class="tblopt"><div class="right">县：</div></td>
	    <td><select class="u-select" id="txt3" name="area" ></select></td>
	    <td></td>
	    <td></td>
	</tr>
	<tr>
		<td class="tblopt">
			<div class="right">备注：</div>
		</td>
		<td colspan="3">${addressInfo.REMARK }</td>
	</tr>
	<tr>
		<td class="tblopt">
		<div class="right"></div>
		</td>
		<td ></td>
		<td class="table_query_3Col_input">
			<input type="button" class="normal_btn" onclick="checkSubmit(1);" value="通 过" id="queryBtn" />
			<input type="button" class="normal_btn" onclick="checkSubmit(2);" value="驳 回" id="queryBtn" />
			<input type="hidden" id="addressId" name="addressId" value="${addressInfo.ID }" />
		</td>
	</tr>
</table>
<jsp:include page="${contextPath}/queryPage/orderHidden.html" /> 
<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
</form>
</div>
<script type="text/javascript">

	function checkSubmit(flag){
		if("1" == flag){
			MyConfirm("是否通过?",checkSubmitAction,[flag]);
		}else{
			MyConfirm("是否驳回?",checkSubmitAction,[flag]);
		}
	}
    
    function checkSubmitAction(flag){
		document.getElementById('fm').action= "<%=request.getContextPath()%>/sales/storageManage/DealerAddressCheck/checkSubmitLogisticsUrl.do?flag="+flag;
		document.getElementById('fm').submit();
    }
</script>
</body>
</html>