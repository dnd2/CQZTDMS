<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.infodms.dms.common.Constant" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title>索赔旧件出库</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script> 
<script type="text/javascript" src="<%=contextPath%>/js/claim/oldPart/addPageListByMix.js"></script> 
</head>
<body>
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理 &gt;索赔旧件出库新增</div>
  <form id="fm" name="fm" method="post">
  <input type="hidden" id="checkedFlag"/>
  <input type="hidden" id="hideCheckedDealerId"/>
  <input type="hidden" name="yieldly" id="yieldly" value="${yieldly }" />
  <input type="hidden" name="isHs" id="isHs" value="" />
    <table class="table_query">
       <tr>
       	 <td class="table_query_3Col_label_5Letter">供应商代码： </td>
	          <td nowrap="nowrap" >
	          <input id="supply_code" name="supply_code" value="" type="text" class="middle_txt" datatype="1,is_null,30">
	          <span style="color:red">*</span>
	         </td>
	         <td class="table_query_3Col_label_5Letter">供应商简称： </td>
	         <td nowrap="nowrap" >
	          <input id="supply_name" name="supply_name" value="" type="text" class="middle_txt" datatype="1,is_null,30">
	          <span style="color:red">*</span>
	         </td>
	         <td class="table_query_3Col_label_5Letter">服务站代码： </td>
	         <td nowrap="nowrap" >
	          <input id="dealer_code" name="dealer_code" value="" type="text" class="middle_txt">
	         </td>
	        
       </tr>
        <tr>
	         <td class="table_query_3Col_label_5Letter">服务站名称： </td>
	          <td nowrap="nowrap" >
	          <input id="dealer_name" name="dealer_name" value="" type="text" class="middle_txt">
	         </td>
        <td class="table_query_3Col_label_5Letter">配件代码：</td>
	         <td nowrap="nowrap">
	            <input id="part_code" name="part_code" value="" type="text" class="middle_txt" datatype="1,is_null,30">
	         </td>
	     <td class="table_query_3Col_label_5Letter">配件名称： </td>
	   <td nowrap="nowrap">
	          <input id="part_name" name="part_name" value="" type="text" class="middle_txt" datatype="1,is_null,30">
	    </td>
       </tr>
       <tr>
         <td class="table_query_3Col_label_5Letter">索赔单号： </td>
	         <td nowrap="nowrap">
	          <input id="claim_no" name="claim_no" value="" type="text" class="middle_txt" datatype="1,is_null,30">
	         </td>
	         <td class="table_query_3Col_label_5Letter">每页显示数：</td>
         	<td> 
			<select name="page_amount" id="page_amount" value="" onchange="checkData();" class="short_sel">
			<option value="15" selected="selected">15</option>
			<option value="50" >50</option>
			<option value="100">100</option>
			<option value="200" >200</option>
			<option value="500">500</option>
			</select>
			</td>
			
			
	         <td class="table_query_3Col_label_5Letter">入库时间： </td>
	        <td align="left" nowrap="true">
			<input name="in_start_date" type="text" class="short_time_txt" id="in_start_date" readonly="readonly" onclick="calendar();"/> 
             &nbsp;至&nbsp; <input name="in_end_date" type="text" class="short_time_txt" id="in_end_date" readonly="readonly" onclick="calendar();"/> 
       </tr>
       <tr>
         <td align="center" nowrap="nowrap" colspan="6">
           <input class="normal_btn" type="button" id="qryButton" name="qryButton" value="查询"  onClick="checkData();">
         </td>
       </tr>
       <tr>
        <td class="table_query_3Col_label_5Letter">出库类型： </td>
         <td align="left"  nowrap="nowrap" >
           <script type="text/javascript">
			genSelBoxExp("OUT_CLAIM_TYPE",<%=Constant.OUT_CLAIM_TYPE%>,"",true,"short_sel","","true",'');
 			</script>
 			 </td>
 			 <td  nowrap="nowrap">
         <span style="color: red">出库方式：</span>
       <input type="radio" id="noType" name="noType" checked="checked"  value="0">    混合出库
         </td>
          <td  nowrap="nowrap" id="retrun_amount">
          	
         </td>
          <td  nowrap="nowrap" id="no_retrun_amount" >
          	实件库存数：<span style="color: red" id="retrun_amount_temp">${retrun_amount}</span>&nbsp;&nbsp;
	   	         无件库存数：<span style="color: red" id="no_retrun_amount_temp">${no_return_amount}</span>
         </td>
       </tr>
  </table>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end -->
  
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
  <table  class="table_list">
	  <tr>
        <th height="12" align="center">
         <input type="button" onclick="outStore();" id="save_btn" class="normal_btn" style="width=8%" value="出库"/>
         &nbsp;&nbsp;
         <input type="button" onclick="history.back();" class="normal_btn" style="width=8%" value="返回"/></th>
	  </tr>
  </table>
</form>
<br>
</body>
</html>