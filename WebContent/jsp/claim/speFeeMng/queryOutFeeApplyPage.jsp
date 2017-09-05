<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.infodms.dms.common.Constant" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>特殊外出费用申报</title>
<% String contextPath = request.getContextPath(); %>
</head>
<BODY onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;特殊费用管理 &gt;特殊外出费用申报</div>
  <form id="fm" name="fm">
  	  <input type="hidden" name="curPage" id="curPage" value="1" />
      <input type="hidden" name="delId" id="delId" value="" />
    <TABLE class="table_query">
       <tr>
          <td  class="table_query_2Col_label_6Letter">巡航单据：</td>
          <td><input id="xh_order_no" name="xh_order_no" value="" type="text" class="middle_txt" datatype="1,is_digit_letter,20" callFunction="javascript:MyAlert();"></td>
          <td  class="table_query_2Col_label_6Letter">上报日期： </td>
          <td nowrap>
          <input name="report_start_date" id="report_start_date" value="" type="text" class="short_txt" datatype="1,is_date,10" group="report_start_date,report_end_date" hasbtn="true" callFunction="showcalendar(event, 'report_start_date', false);">
         	&nbsp;至&nbsp;
          <input name="report_end_date" id="report_end_date" value="" type="text" class="short_txt" datatype="1,is_date,10" group="report_start_date,report_end_date" hasbtn="true" callFunction="showcalendar(event, 'report_end_date', false);"></td>
       </tr>
       <tr>
          <td  class="table_query_2Col_label_6Letter">巡航目的地：</td>
          <td>
            <input id="xh_aim_area" name="xh_aim_area" value="" type="text" class="middle_txt" datatype="1,is_digit_letter_cn,30" callFunction="javascript:MyAlert();">
          </td>
          <td  class="table_query_2Col_label_6Letter">批复日期： </td>
          <td nowrap>
          <input name="approve_start_date" id="approve_start_date" value="" type="text" class="short_txt" datatype="1,is_date,10" group="approve_start_date,approve_end_date" hasbtn="true" callFunction="showcalendar(event, 'approve_start_date', false);">
         	&nbsp;至&nbsp;
          <input name="approve_end_date" id="approve_end_date" value="" type="text" class="short_txt" datatype="1,is_date,10" group="approve_start_date,approve_end_date" hasbtn="true" callFunction="showcalendar(event, 'approve_end_date', false);"></td>
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
   var url = "<%=contextPath%>/claim/speFeeMng/OutFeeApplyManager/queryOutFeeCruiInfoList.json";
				
   var title = null;

   var columns = [
  				{header: "巡航单据",dataIndex: 'cr_no',align:'center',renderer:ordDetail},
  				{header: "上报日期", dataIndex: 'make_date', align:'center'},
  				{header: "巡航目的地", dataIndex: 'cr_whither', align:'center'},
  				{header: "巡航服务负责人", dataIndex: 'cr_principal', align:'center'},
  				{header: "批复日期", dataIndex: 'audit_date', align:'center'},
  				{id:'action',header: "操作",sortable: false,dataIndex: 'id',renderer:myLink,align:'center'}
  		      ];
   //超链接设置
   function myLink(value,meta,record){
	   var status=record.data.status;
	   if(status==null||status==''){
		   return String.format(
			         "<a href=\"<%=contextPath%>/claim/speFeeMng/OutFeeApplyManager/applySpeFeeOrdPage.do?ord_id="
						+ value + "\">[申报]</a>");
	   }else if(status=='<%=Constant.SPE_OUTFEE_STATUS_01%>'){
		   return String.format(
			         "<a href=\"<%=contextPath%>/claim/speFeeMng/OutFeeApplyManager/modifySpeFeeOrdPage.do?ord_id="
						+ value + "\">[修改]</a>");
	   }else if(status=='<%=Constant.SPE_OUTFEE_STATUS_03%>'||status=='<%=Constant.SPE_OUTFEE_STATUS_06%>'){
		   return String.format(
			         "<a href=\"<%=contextPath%>/claim/speFeeMng/OutFeeApplyManager/modifySpeFeeOrdPage.do?ord_id="
						+ value + "\">[重新上报]</a>");
	   }
	   
   }
   function doInit(){
	  loadcalendar();
   }
   function ordDetail(value,metaDate,record){
	   var id=record.data.id;
	   return String.format("<a href='#' onclick='queryDetail("+id+");'>["+value+"]</a>");
   }
   function queryDetail(id){
	   var submit_url="<%=contextPath%>/claim/speFeeMng/CruiServicePathQueryManager/queryOrdDetailInfoPage.do?ord_id="+ id;
	   OpenHtmlWindow(submit_url,900,500);
   }
</script>
</BODY>
</html>