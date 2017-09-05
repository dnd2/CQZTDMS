<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.infodms.dms.common.Constant" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>特殊外出费用确认单</title>
<% String contextPath = request.getContextPath(); %>
</head>
<body onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;特殊费用管理 &gt;特殊外出费用确认单</div>
  <form id="fm" name="fm">
  	  <input type="hidden" name="curPage" id="curPage" value="1" />
      <input type="hidden" name="delId" id="delId" value="" />
    <TABLE class="table_query">
       <tr>
          <td  class="table_query_2Col_label_6Letter">特殊外出单据：</td>
          <td><input id="ts_order_no" name="ts_order_no" value="" type="text" class="middle_txt" datatype="1,is_digit_letter,20" callFunction="javascript:MyAlert();"></td>
          <td  class="table_query_2Col_label_6Letter">巡航单据：</td>
          <td><input id="xh_order_no" name="xh_order_no" value="" type="text" class="middle_txt" datatype="1,is_digit_letter,20" callFunction="javascript:MyAlert();"></td>
       </tr>
       <tr>
         <td class="table_query_2Col_label_6Letter">经销商代码：</td>
		 <td align="left" nowrap>
            <textarea rows="3" cols="23" id="dealerCode"  name="dealerCode"></textarea>
            <input name="showBtn" type="button" class="mini_btn" style="cursor: pointer;" onclick="showOrgDealer('dealerCode','','true','',true);" value="..." />        
            <input name="clrBtn" type="button" class="normal_btn" onclick="clearInput();" value="清除"/>
		 </td>
		 <td class="table_query_2Col_label_6Letter">经销商名称：</td>
		 <td align="left" nowrap>
			<input type="text" name="dealerName" id="dealerName" datatype="1,is_digit_letter_cn,30" class="middle_txt" value=""/>
		 </td>
       </tr>
       <tr>
          <td  class="table_query_2Col_label_8Letter" nowrap>特殊外出上报日期： </td>
          <td colspan="3">
          <input name="report_start_date" id="report_start_date" value="" type="text" class="short_txt" datatype="1,is_date,10" group="report_start_date,report_end_date" hasbtn="true" callFunction="showcalendar(event, 'report_start_date', false);">
         	&nbsp;至&nbsp;
          <input name="report_end_date" id="report_end_date" value="" type="text" class="short_txt" datatype="1,is_date,10" group="report_start_date,report_end_date" hasbtn="true" callFunction="showcalendar(event, 'report_end_date', false);"></td>
       </tr>
       <tr>
         <td align="center" colspan="4" nowrap>
          <input class="normal_btn" type="button" name="qryButton" value="查询"  onClick="__extQuery__(1);">
         </td>
       </tr>
  </table>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form> 
<br>
<script type="text/javascript">
   var myPage;
   //查询路径
   var url = "<%=contextPath%>/claim/speFeeMng/OutFeeConfirmManager/queryOutFeeConfirmInfoList.json";
				
   var title = null;

   var columns = [
                {header: "经销商代码",dataIndex: 'dealer_code',align:'center'},
                {header: "经销商名称",dataIndex: 'dealer_name',align:'center'},
  				{header: "特殊外出单据",dataIndex: 'fee_no',align:'center'},
  				{header: "巡航单据",dataIndex: 'cr_no',align:'center'},
  				{header: "特殊外出上报日期", dataIndex: 'make_date', align:'center'},
  				{header: "巡航目的地", dataIndex: 'cr_whither', align:'center'},
  				{header: "总费用(元)", dataIndex: 'total_fee', align:'center',renderer:amountFormat},
  				{id:'action',header: "操作",sortable: false,dataIndex: 'id',renderer:myLink,align:'center'}
  		      ];
   //超链接设置
   function myLink(value,meta,record){
	   var status=record.data.status;
	   return String.format(
		         "<a href='#' onclick='goApprovePage("+value+")'>[审核]</a>");
	   
   }
   //审核页面
   function goApprovePage(value){
       var submit_url="<%=contextPath%>/claim/speFeeMng/OutFeeConfirmManager/approveSpeFeePage.do?order_id="+value;
       OpenHtmlWindow(submit_url,900,500);
   }
   function doInit(){
	  loadcalendar();
   }
    //清空经销商框
	function clearInput(){
		var target = document.getElementById('dealerCode');
		target.value = '';
	}
	//刷新页面
	function refreshPage(){
		__extQuery__(1);
	}
</script>
</body>
</html>