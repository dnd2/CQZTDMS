<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.infodms.dms.common.Constant" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>索赔旧件抵扣</title>
<% String contextPath = request.getContextPath(); %>
</head>
<BODY onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理 &gt;索赔旧件抵扣</div>
  <form id="fm" name="fm">
  	  <input type="hidden" name="curPage" id="curPage" value="1" />
      <input type="hidden" name="delId" id="delId" value="" />
    <TABLE class="table_query">
       <tr>
         <td class="table_query_2Col_label_6Letter" rowspan="2">经销商代码：</td>            
         <td rowspan="2">
			<textarea id="dealerCode"  name="dealerCode" rows="3" cols="20" datatype="1,is_null,2000"></textarea>
            <input name="button1" type="button" class="mini_btn" style="cursor: pointer;" onclick="showOrgDealer('dealerCode','','true','',true);" value="..." />        
            <input name="button2" type="button" class="normal_btn" onClick="clr();" value="清除"/>
         </td>
         <td  class="table_query_2Col_label_6Letter">经销商名称：</td>
         <td><input id="dealerName" name="dealerName" value="" type="text" class="middle_txt" datatype="1,is_null,15" callFunction="javascript:MyAlert();"></td>
       </tr>
       <tr>
 		  <td class="table_query_2Col_label_6Letter">回运清单号：</td>
          <td><input id="back_order_no" name="back_order_no" value="" type="text" class="middle_txt" datatype="1,is_null,16" callFunction="javascript:MyAlert();"></td>
       </tr>
       <tr>
         <td  class="table_query_2Col_label_6Letter">建单日期： </td>
         <td nowrap="nowrap">
          <input name="create_start_date" id="create_start_date" value="" type="text" class="short_txt" datatype="1,is_date,10" group="create_start_date,create_end_date" hasbtn="true" callFunction="showcalendar(event, 'create_start_date', false);">
         	&nbsp;至&nbsp;
          <input name="create_end_date" id="create_end_date" value="" type="text" class="short_txt" datatype="1,is_date,10" group="create_start_date,create_end_date" hasbtn="true" callFunction="showcalendar(event, 'create_end_date', false);"></td>
         <td class="table_query_2Col_label_6Letter">提报日期： </td>
         <td nowrap="nowrap">
          <input name="report_start_date" id="report_start_date" value="" type="text" class="short_txt" datatype="1,is_date,10" group="report_start_date,report_end_date" hasbtn="true" callFunction="showcalendar(event, 'report_start_date', false);">
         	&nbsp;至&nbsp;
          <input name="report_end_date" id="report_end_date" value="" type="text" class="short_txt" datatype="1,is_date,10" group="report_start_date,report_end_date" hasbtn="true" callFunction="showcalendar(event, 'report_end_date', false);"></td>
       </tr>
       <tr>
          <td class="table_query_2Col_label_6Letter">入库日期： </td>
          <td nowrap="nowrap">
          <input name="store_start_date" id="store_start_date" value="" type="text" class="short_txt" datatype="1,is_date,10" group="store_start_date,store_end_date" hasbtn="true" callFunction="showcalendar(event, 'store_start_date', false);">
         	&nbsp;至&nbsp;
          <input name="store_end_date" id="store_end_date" value="" type="text" class="short_txt" datatype="1,is_date,10" group="store_start_date,store_end_date" hasbtn="true" callFunction="showcalendar(event, 'store_end_date', false);"></td>
          <td class="table_query_2Col_label_6Letter">货运方式：</td>
          <td  align="left" >
          <script type="text/javascript">
            genSelBoxExp("transport_type",<%=Constant.OLD_RETURN_STATUS%>,"",true,"short_sel","","true",'');
           </script>
          </td>
       </tr>
       <tr>
       		<td align="right" nowrap="true">生产基地：</td>
			<td align="left" nowrap="true">
				<script type="text/javascript">
					genSelBoxContainStr("yieldly",<%=Constant.SERVICEACTIVITY_CAR_YIELDLY%>,"",true,"short_sel","","true",'<%=CommonUtils.checkNull(request.getAttribute("yieldly"))%>');
			    </script>
			</td>
       </tr>
       <tr>
         <td align="center" colspan="4" nowrap="nowrap"><input class="normal_btn" id="queryBtn" type="button" name="qryButton" value="查询"  onClick="__extQuery__(1);">
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
   var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartDeduceManager/queryOldPartDeduceList.json";
				
   var title = null;

   var columns = [
  				{header: "序号",align:'center',renderer:getIndex},
  				{header: "经销商代码",dataIndex: 'dealer_code',align:'center'},
  				{header: "经销商名称", dataIndex: 'dealer_name', align:'center'},
  				{header: "回运清单号", dataIndex: 'return_no', align:'center'},
  				{header: "货运类型", dataIndex: 'transport_type', align:'center',renderer:getItemValue},
  				{header: "建单日期", dataIndex: 'create_date', align:'center'},
  				{header: "提报日期", dataIndex: 'report_date', align:'center'},
  				{header: "入库日期", dataIndex: 'store_date', align:'center'},
  				//{header: "索赔单数", dataIndex: 'wr_amount', align:'center'},
  				{header: "索赔配件数", dataIndex: 'part_amount', align:'center'},
  				{header: "差异数", dataIndex: 'diff_amount', align:'center'},
  				{header: "已抵扣金额", dataIndex: 'deduct_amount', align:'center'},
  				{header: "应抵扣金额", dataIndex: 'price', align:'center'},
  				{header: "结算金额", dataIndex: 'balance_amount', align:'center'},
  				{id:'action',header: "操作",sortable: false,dataIndex: 'return_id',renderer:myLink,align:'center'}
  		      ];
   //超链接设置
   function myLink(value,meta,record){
	   return String.format("<a href='#' onclick='window.open(\"<%=contextPath%>/claim/oldPart/ClaimOldPartDeduceManager/searchDetailInfo.do?RETURN_ID="
				+ value + "\")'>[明细]</a>");
   }
   function doInit(){
	  loadcalendar();
   }
   //清除经销商代码
   function clr() {
  	  document.getElementById('dealerCode').value = "";
   }
</script>
</BODY>
</html>