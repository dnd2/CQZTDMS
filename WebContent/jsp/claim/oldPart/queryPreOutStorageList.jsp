<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.infodms.dms.common.Constant" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>索赔旧件出库</title>
<% String contextPath = request.getContextPath(); %>
<script type="text/javascript" src="<%=contextPath%>/js/claim/oldPart/queryPreOutStorageList.js"></script> 
</head>
<body onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理 &gt;索赔旧件出库新增</div>
  <form id="fm" name="fm" method="post">
  <input type="hidden" name="yieldly" id="yieldly" value="${yieldly }" />
  <input type="hidden" name="isHs" id="isHs" value="" />
    <table class="table_query">
    <tr>
     	<td  align="center" colspan="4">
	         <span style="color: red">注：供应商名称,代码以及服务站名称,代码至少输入一个才能查询!</span>
	     </td>
	  </tr>
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
	        
       </tr>
        <tr>
	         <td class="table_query_3Col_label_5Letter">服务站代码： </td>
	         <td nowrap="nowrap" >
	          <input id="dealer_code" name="dealer_code" value="" type="text" class="middle_txt" datatype="1,is_null,30">
	          <span style="color:red">*</span>
	         </td>
	         <td class="table_query_3Col_label_5Letter">服务站名称： </td>
	          <td nowrap="nowrap" >
	          <input id="dealer_name" name="dealer_name" value="" type="text" class="middle_txt" datatype="1,is_null,30">
	          <span style="color:red">*</span>
	         </td>
       </tr>
       <tr>
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
	         <td align="right">每页显示数：</td>
         	<td> 
			<select name="page_amount" id="page_amount" value="" onchange="checkDate();" class="short_sel">
			<option value="10">10</option>
			<option value="20">20</option>
			<option value="50" selected="selected">50</option>
			<option value="100">100</option>
			</select>
			</td>
       </tr>
       <tr>
	         <td class="table_query_3Col_label_5Letter">入库时间： </td>
	        <td align="left" nowrap="true">
			<input name="in_start_date" type="text" class="short_time_txt" id="in_start_date" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'in_start_date', false);" />  	
             &nbsp;至&nbsp; <input name="in_end_date" type="text" class="short_time_txt" id="in_end_date" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'in_end_date', false);" /> 
       </tr>
       <tr>
         <td align="center" nowrap="nowrap" colspan="4">
           <input class="normal_btn" type="button" id="qryButton" name="qryButton" value="查询"  onClick="checkDate();">
           <input type="button" onclick="backTo();" class="normal_btn" style="width=8%" value="返回"/>
         </td>
       </tr>
       <tr>
        <td class="table_query_3Col_label_5Letter">出库类型： </td>
         <td align="left"  nowrap="nowrap" >
           <script type="text/javascript">
			genSelBoxExp("OUT_CLAIM_TYPE",<%=Constant.OUT_CLAIM_TYPE%>,"",true,"short_sel","","true",'');
 			</script>
 			 </td>
 			  <td class="table_query_3Col_label_5Letter">关联退赔单： </td>
         	<td align="left"  nowrap="nowrap" id="testNo">
         	<select name="relationOutNo" value="" id ="relationOutNo"   class="short_sel">
         	<option value="-1">--请选择--</option>
         	</select>
			<input name="bb" id ="aa" value="查询单号" type="button" class="normal_btn" onclick="getNo();">
 			 </td>
 			 <td style="display: none" align="left" nowrap="nowrap" >出库件类型： </td>
 			 <td style="display: none">
 			 <script type="text/javascript">
			genSelBoxExp("OUT_PART_TYPE",<%=Constant.OUT_PART_TYPE%>,"",true,"short_sel","","true",'<%=Constant.OUT_PART_TYPE_03%>,<%=Constant.OUT_PART_TYPE_04%>');
 			</script>
 			</td>
       </tr>
      <tr>
         <td align="center" nowrap="nowrap" colspan="4">
         <span style="color: red">出库方式：</span>
       <input type="radio" id="noType" name="noType"  value="0">    混合出库
       &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;<input type="radio" id="noType" name="noType" value="1"> 分单号出库
         </td>
       </tr>
  </table>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end -->
  
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>
<form name="form1" id="form1" style="display:none">
  <table id="bt" class="table_list">
	  <tr>
        <th height="12" align="center">
         <input type="button" onclick="preChecked();" id="save_btn" class="normal_btn" style="width=8%" value="出库"/>
         <input type="button" onclick="printDetail();"  class="normal_btn" style="width=8%" value="打印"/>
         &nbsp;&nbsp;
         <input type="button" onclick="history.back();" class="normal_btn" style="width=8%" value="返回"/></th>
	  </tr>
  </table>
</form>
<br>
</body>
</html>