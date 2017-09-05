<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page import="com.infodms.dms.common.Constant"%>
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>车辆历史信息查询</title>
<% String contextPath = request.getContextPath(); %>
</head>
<body onload="setMyBtnStyle();">
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
<div class="navigation"><img src="<%=request.getContextPath()%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;车辆信息管理&gt;车辆历史信息查询</div>
  <form method="post" name="fm" id="fm">
   <!-- 查询条件 begin -->
  <table class="table_query">
       <tr>
         <td align="right" nowrap >VIN码：</td>
         <td colspan="6" nowrap>
         	<input type="text" name="VIN" class="middle_txt" id="VIN" datatype="0,is_vin,17"/>
         </td>
         <td>&nbsp;</td> 
         <td align="left" nowrap>
         	<input class="normal_btn" type="button" id="queryBtn" name="button1" value="查询"  onclick="isVin();"/>
         </td>
       </tr>
  </table>
  <table border="0" cellspacing="0" cellpadding="0" class="table_edit">
				<tr>
					<td colspan="2">&nbsp;</td>
					<td colspan="2" align=center>
						<input class="normal_btn" type="button" id="btn1" value="维修历史" onclick="maintaimHistory();"/>&nbsp;
		                <input class="normal_btn" type="button" id="btn2" value="授权历史" onclick="auditingHistory();"/>&nbsp;
		                <input class="normal_btn" type="button" id="btn3" value="保养历史" onclick="freeMaintainHistory();"/>&nbsp;
					</td>
				</tr>
			</table>
</form>
</body>
<script type="text/javascript">
function setMyBtnStyle(){
	$('btn1').disabled = 'disabled';
	$('btn2').disabled = 'disabled';
	$('btn3').disabled = 'disabled';
}
function isVin(){
	if(submitForm('fm')){
		var vin = document.getElementById('VIN').value;
		var url="<%=contextPath%>/repairOrder/RoMaintainMain/vehicleHistoryInfoQuery1.json";
		makeCall(url,verEngineNo,{vin:vin});
	}
}
function verEngineNo(json){
	var ok = json.ok;
	if(ok=='ok'){
		$('btn1').disabled = '' ;
		$('btn2').disabled = '' ;
		$('btn3').disabled = '' ;
	}else{
		MyAlert("输入的VIN码不存在!");
	}
}
//维修历史按钮方法
function maintaimHistory(){
	var vin = $('VIN').value ;
	window.open('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/maintaimHistory.do?VIN='+vin);
}
//授权历史按钮方法
function auditingHistory(){
	var vin = $('VIN').value ;
	window.open('<%=contextPath%>/repairOrder/RoMaintainMain/authDetail.do?VIN='+vin);
}
//保养历史按钮方法
function freeMaintainHistory(){
	var vin = $('VIN').value ;
	window.open('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/freeMaintainHistory.do?VIN='+vin);
}
</script>
</html>