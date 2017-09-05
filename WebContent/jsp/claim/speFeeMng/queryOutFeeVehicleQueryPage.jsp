<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.infodms.dms.common.Constant" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>巡航服务线路查询</title>
<% String contextPath = request.getContextPath(); %>
</head>
<BODY onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;特殊费用管理 &gt;巡航服务线路查询</div>
  <form id="fm" name="fm">
  	  <input type="hidden" name="curPage" id="curPage" value="1" />
      <input type="hidden" name="delId" id="delId" value="" />
    <TABLE class="table_query">
       <tr>
          <td align="right" nowrap="true">经销商代码：</td>
			<td align="left" nowrap="true">
                
				<textarea rows="3" cols="23" id="dealerCode"  name="dealerCode"></textarea>
	            <input name="showBtn" type="button" class="mini_btn" style="cursor: pointer;" onclick="showOrgDealer('dealerCode','','true','',true);" value="..." />        
	            <input name="clrBtn" type="button" class="normal_btn" onclick="clearInput();" value="清除"/>
			</td>
			<td align="right" nowrap="true">经销商名称：</td>
			<td align="left" nowrap="true">
				<input type="text" name="dealerName" id="dealerName" datatype="1,is_digit_letter_cn,30" class="middle_txt" value=""/>
			</td>
       </tr>
       <tr>
          <td  class="table_query_2Col_label_8Letter">外出费用单据：</td>
          <td><input id="ts_order_no" name="ts_order_no" value="" type="text" class="middle_txt" datatype="1,is_digit_letter,20" callFunction="javascript:MyAlert();"></td>
          <td  class="table_query_2Col_label_8Letter">巡航服务单据：</td>
          <td><input id="xh_order_no" name="xh_order_no" value="" type="text" class="middle_txt" datatype="1,is_digit_letter,20" callFunction="javascript:MyAlert();"></td>
       </tr>
       <tr>
          <td  class="table_query_2Col_label_8Letter">外出费用上报日期： </td>
          <td nowrap>
          <input name="report_start_date" id="report_start_date" value="" type="text" class="short_txt" datatype="1,is_date,10" group="report_start_date,report_end_date" hasbtn="true" callFunction="showcalendar(event, 'report_start_date', false);">
         	&nbsp;至&nbsp;
          <input name="report_end_date" id="report_end_date" value="" type="text" class="short_txt" datatype="1,is_date,10" group="report_start_date,report_end_date" hasbtn="true" callFunction="showcalendar(event, 'report_end_date', false);"></td>
          <td class="table_query_2Col_label_8Letter">外出单据状态：</td>
          <td  align="left" >
            <script type="text/javascript">
            genSelBoxExp("ord_status",<%=Constant.SPE_OUTFEE_STATUS%>,"",true,"short_sel","","true",'<%=Constant.SPE_OUTFEE_STATUS_01%>');
            </script>
          </td>
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
   var url = "<%=contextPath%>/claim/speFeeMng/OutFeeQueryVehicleManager/queryOutFeeInfoOrdList.json";
				
   var title = null;

   var columns = [
                {header: "经销商代码", dataIndex: 'dealer_code', align:'center'},
                {header: "经销商名称", dataIndex: 'dealer_name', align:'center'},
  				{header: "外出费用单据",sortable: false,dataIndex: 'fee_no',align:'center',renderer:ordTSDetail},
  				{header: "巡航服务单据", dataIndex: 'cr_no', align:'center',renderer:ordXHDetail},
  				{header: "外出费用上报日期", dataIndex: 'make_date', align:'center'},
  				{header: "巡航目的地", dataIndex: 'cr_whither', align:'center'},
  				{header: "总费用(元)", dataIndex: 'total_fee', align:'center',renderer:amountFormat},
  				{header: "外出单据状态", dataIndex: 'status_desc', align:'center'}
  		      ];
   function doInit(){
	  loadcalendar();
   }
   //日期格式化：
   function formatDate(value,metaDate,record){
	 return String.format(value.substring(0,10));
   }
   //查询巡航单明细
   function ordXHDetail(value,metaDate,record){
	   var id=record.data.cr_id;
	   return String.format("<a href='#' onclick='queryXHDetail("+id+");'>["+value+"]</a>");
   }
   function queryXHDetail(id){
	   var submit_url="<%=contextPath%>/claim/speFeeMng/CruiServicePathVehicleQueryManager/queryOrdDetailInfoPage.do?ord_id="+ id;
	   OpenHtmlWindow(submit_url,900,500);
   }
   //查询特殊费用单明细
   function ordTSDetail(value,metaDate,record){
	   var id=record.data.cr_id;
	   return String.format("<a href='#' onclick='queryTSDetail("+id+");'>["+value+"]</a>");
   }
   function queryTSDetail(id){
	   var submit_url="<%=contextPath%>/claim/speFeeMng/OutFeeQueryVehicleManager/queryOrdDetailInfoPage.do?ord_id="+ id;
	   OpenHtmlWindow(submit_url,900,500);
   }
    //清空经销商框
	function clearInput(){
		var target = document.getElementById('dealerCode');
		target.value = '';
	}
</script>
</BODY>
</html>