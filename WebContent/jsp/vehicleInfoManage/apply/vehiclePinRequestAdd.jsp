<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>车辆信息变更申请</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
	function add(){
		var userName = $('userName').value;
		var companyName = $('companyName').value; 
		var companyCode = document.getElementById('companyCode').value;
		var vinNo = $('vinNo').value;
		var remark = $('remark').value;
		if (vinNo==null||vinNo==''||vinNo=='null') {
			MyAlert('VIN不能为空！');
			return;
		}
		if (remark==null||remark==''||remark=='null') {
			MyAlert('请添加备注！');
			return;
		}
		if(remark.length>1000){
			MyAlert('备注字数超过限制！');
			$('remark').value=''
			return;
		}
		MyConfirm('确认提报！',addConfirm);
	}
	function addConfirm(){
		var url = '<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/addVehiclePinRequest.json?COMMAND=1';
		makeNomalFormCall(url,showPinNo,"fm");
	}
	function showPinNo(obj){
		var pNo = obj.pNo;
		if(pNo!=null){
		window.location.href = "<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/queryVehiclePinRequest.do";
			//MyAlert("申请单号："+pNo,back);
		}else{
			MyAlert("操作异常，请联系管理员！<br/>"+"aa"+obj.Exception.message);
		}
	}
	function back(){
	MyAlert('aaaaaa');
		//window.location.href = "<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/queryVehiclePinRequest.do?COMMAND=1";
	}
</script>
</head>
<body>
<div class="navigation">
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置:售后服务管理&gt;车辆信息管理&gt;车辆PIN订单申请
</div>
<form name="fm" id="fm">
<input name="userName" type="hidden" id=""userName""  class="middle_txt" value="${ps.NAME }"/>
<input name="companyName" type="hidden" id=""companyName""  class="middle_txt" value="${ps.COMPANY_NAME }"/>
<input name="companyCode" type="hidden" id="companyCode"  class="middle_txt" value="${ps.COMPANY_CODE }"/>
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_edit">
	<tr>
      	<td class="table_query_2Col_label_6Letter">制单人姓名：</td>
      	<td>${ps.NAME }</td>
      	<td class="table_query_2Col_label_6Letter">制单日期：</td>
      	<td>${ps.nowDate }</td>
      	<td class="table_query_2Col_label_5Letter"></td>
      	<td></td>
    </tr>
    <tr>
      <td class="table_query_2Col_label_5Letter">维修站编码：</td>
      <td>
      ${ps.DEALER_CODE }
      </td>
      <td class="table_query_2Col_label_6Letter">维修站：</td>
      <td>
		${ps.DEALER_NAME }
      </td>
       <td class="table_query_2Col_label_6Letter">VIN：</td>
      	<td><input name="vinNo" type="text" id="vinNo" datatype="0,is_vin"  blurback="true" class="middle_txt"/></td>
    </tr>
    <tr>
    	<td>
    		备注：
    	</td>
    	<td colspan="5">
    		<textarea rows="5" cols="100" id="remark" name="remark" datatype="0,is_digit_letter_cn,1000"></textarea>
    	</td>
    </tr>
    <tr>
    	<td align="center" colspan="6">
    		<input type="button" name="BtnAdd" id="commitBtn"  value="提报"  class="normal_btn" onClick="add()" >
    		<input type="button" name="BtnBack" id="backBtn"  value="返回"  class="normal_btn" onClick="window.location.href='<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/queryVehiclePinRequest.do'" >
    	</td>
    </tr>
</table>
</body>
</html>