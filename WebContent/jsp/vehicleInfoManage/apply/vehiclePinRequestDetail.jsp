<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/cout" prefix="c" %>
<%@taglib uri="/jstl/fmt" prefix="fmt" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>车辆信息变更申请</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript">
	function doInit(){
   		loadcalendar();  //初始化时间控件
	}
	function modify(obj){
		var vinNo = $('vinNo').value;
		var pinId = $('pinId').value;
		var remark = $('remark').value;
		var flag = $('flag');
		var url = '<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/modifyVehiclePinRequest.json?COMMAND=1';
		if(obj=='1'){
			
    		flag.value = "modify";
		}else if(obj=='2'){
			flag.value = "submit";
		}else{
			flag.value = "invalid";
		}
		makeNomalFormCall(url,showPinNo,"fm");
	}
	function showPinNo(obj){
		var pNo = obj.pNo;
		if(pNo!=null){
		window.location.href = "<%=contextPath%>/vehicleInfoManage/apply/VehicleInfoChangeApply/queryVehiclePinRequest.do";
			//MyAlert("申请单号："+pNo,back);
		}else{
			MyAlert("操作异常，请联系管理员！<br/>"+obj.Exception.message);
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
<img src="<%=contextPath%>/img/nav.gif" />&nbsp;当前位置:售后服务管理&gt;车辆信息管理&gt;车辆PIN订单查看
</div>
<form name="fm" id="fm">
<input name="pinId" type="hidden" id="pinId"  class="middle_txt" value="${ps.PIN_ID }"/>
<input name="pinCode" type="hidden" id="pinCode"  class="middle_txt" value="${ps.PIN_CODE }"/>
<input name="userName" type="hidden" id=""userName""  class="middle_txt" value="${ps.NAME }"/>
<input name="companyName" type="hidden" id=""companyName""  class="middle_txt" value="${ps.COMPANY_NAME }"/>
<input name="companyCode" type="hidden" id="companyCode"  class="middle_txt" value="${ps.COMPANY_CODE }"/>
<!-- 查询条件 begin -->
<table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_edit">
	<tr>
	    <td class="table_query_2Col_label_6Letter">单据编码：</td>
      	<td>${ps.PIN_CODE }</td>
      	<td class="table_query_2Col_label_6Letter">制单人姓名：</td>
      	<td>${ps.NAME }</td>
      	<td class="table_query_2Col_label_6Letter">制单日期：</td>
      	<td><fmt:formatDate  value="${ps.MAKE_DATE }" pattern="yyyy-MM-dd" /></td>
    </tr>
    <tr>
      <td class="table_query_2Col_label_6Letter">维修站编码：</td>
      <td>
      ${ps.DEALER_CODE }
      </td>
      <td class="table_query_2Col_label_6Letter">维修站：</td>
      <td>
		${ps.DEALER_NAME }
      </td>
       <td class="table_query_2Col_label_6Letter">VIN：</td>
      	<td>${ps.VIN }</td>
    </tr>
    <tr>
    	<td class="table_query_2Col_label_6Letter">
    		备注：
    	</td>
    	<td colspan="5">
    		<div style='word-wrap:break-word; word-break:break-all;display:block;width:100%;'>${ps.REMARK }</div>
    	</td>
    </tr>
        <tr>
    	<td class="table_query_2Col_label_6Letter">
    		车辆PIN码：
    	</td>
    	<td colspan="5">
    		${ps.PIN }
    	</td>
    </tr>
        <tr>
    	<td class="table_query_2Col_label_6Letter">
    		回复备注：
    	</td>
    	<td colspan="5">
    		<div style='word-wrap:break-word; word-break:break-all;display:block;width:100%;'>${ps.BACK_REMARK }</div>
    	</td>
    </tr>
    <tr>
    	<td colspan="6" align="center"><input class="normal_btn" type=button name='cloBtn' id='cloBtn' onclick='_hide();' value='关闭'/></td>
    </tr>
</table>
</body>
</html>