<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
	String contextPath = request.getContextPath();
%>
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ page import="com.infodms.dms.common.Constant"%>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>二次抵扣索赔单查询</title>
</head>
<body onload="doInit();">
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理&gt;二次抵扣</div>
<form id="fm" name="fm">
<!-- 隐藏域 -->
  <input type="hidden" name="deductionId" id="deductionId" value="${oldPartDeductionSecondInfo.DEDUCTION_ID}"/>
  <!-- 二次抵扣信息 -->
  <div class="form-panel">
  <h2>二次抵扣信息</h2>
    <div class="form-body">
    <table border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
	<tr>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;">抵扣单号：</td>
	  <td align="left" style="width:245px">${oldPartDeductionSecondInfo.DEDUCTION_NO}</td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;">抵扣类型：</td>
	  <td align="left" style="width:245px">${oldPartDeductionSecondInfo.DEDUCTION_TYPE_NAME}</td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;">通知日期：</td>
	  <td align="left" style="width:245px">${oldPartDeductionSecondInfo.CREATE_DATE}</td>
	</tr>
	<tr>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;">抵扣费用：</td>
	  <td align="left" style="width:245px">${oldPartDeductionSecondInfo.SECOND_DEDUCTION_AMOUNT}</td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;">抵扣状态：</td>
	  <td align="left" style="width:245px">${oldPartDeductionSecondInfo.STATUS_NAME}</td>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;">结算单号：</td>
	  <td align="left" style="width:245px">${oldPartDeductionSecondInfo.BALANCE_NO}</td>	  
	</tr>
	<tr>
	  <td class="table_add_2Col_label_5Letter" style="text-align:right;width:125px;vertical-align:top">抵扣备注：</td>
	  <td colspan=5 align="left">${oldPartDeductionSecondInfo.SECOND_DEDUCTION_REMARK}</td>
	</tr>
	</table>
	</div>
  </div>
  <!-- 功能按钮 -->
  <table id="bt" class="table_query">
	 <tr>
		<td style="text-align:center" >
			<input type="button"  onclick="_hide()" id="sureBtn" class="normal_btn" value="确定" />&nbsp;&nbsp;
		</td>
	</tr>
  </table>
</form> 
</div>
<br>
</body>
<script type="text/javascript">
   
   function doInit(){
	  //loadcalendar();
   }
   
</script>

</html>