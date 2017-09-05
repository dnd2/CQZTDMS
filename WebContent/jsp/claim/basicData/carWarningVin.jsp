<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@page import="com.infodms.dms.common.Constant"%>
<%String contextPath = request.getContextPath();%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>车辆进站预警检查</title>
</head>
<body>
<form method="post" name='fm' id='fm'>
  <div class="navigation">
  <img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置： 售后服务管理&gt;维修登记&gt;车辆进站预警检查</div>
   <table class="table_query">
          <tr>
            <td align="right" nowrap="nowrap">VIN：</td>
            <td>
                 <input name="VIN" id="VIN" datatype="0,is_null,30"  type="text" class="long_txt"/>
            </td>
            <td align="left" colspan="2">
            	<input  type="button" value="车辆进站预警检查" name="add" onclick="warningQuery();"/><!-- class="normal_btn" -->
            </td>
          </tr>
  </table>
</form>
<script type="text/javascript" >
//新增
//function subFun(){
    //location="<%=contextPath%>/claim/basicData/WarningRepairMain/WarningRepairAddInit.do";
//}


function warningQuery(){
	var vin = $('VIN').value ;
	if (vin==null||vin==''||vin=='null') {
		MyAlert("车辆VIN不能为空!");
		return;
	}
	var url = '<%=contextPath%>/claim/basicData/WarningRepairMain/isWarning.json?VIN='+vin;
	makeNomalFormCall(url,addBack,'fm','');
}

function addBack(json) {
	if(json.gameValue != null && json.gameValue == 60061001) {
		var vin = json.vin;
		window.open('<%=contextPath%>/claim/dealerClaimMng/ClaimBillMaintainMain/lsQueryVinWarningHistory.do?VIN='+vin);
	} else {
		MyAlert("该车不在预警范围!");
		return;
	}
}
</script>
</body>
</html>