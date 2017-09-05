<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.infodms.dms.common.Constant" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>索赔旧件审批入库</title>
<% String contextPath = request.getContextPath();
%>
</head>
<BODY onload="doInit();">
	<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理&gt;旧件签收查询</div>
  <form id="fm" name="fm">
  	 <div class="form-panel">
				<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
				<div class="form-body">
    <TABLE class="table_query">
       <tr>
         <td class="right" nowrap="nowrap" >经销商代码：</td>            
         <td  nowrap="nowrap" >
         <input id="dealerCode" name="dealerCode" value="" type="text" class="middle_txt"/>
         </td>
         <td class="right" nowrap="nowrap" >经销商名称：</td>
         <td  nowrap="nowrap"><input id="dealerName" name="dealerName" value="" type="text" class="middle_txt" datatype="1,is_name,15" callFunction="javascript:MyAlert();"></td>
       </tr>
       <tr>
          <td class="right" nowrap="nowrap" >旧件回运清单号：</td>
         <td  nowrap="nowrap"><input id="back_order_no" name="back_order_no" value="" type="text" class="middle_txt"></td>
     <td class="right" nowrap="nowrap" >发运单号：</td>
          <td  nowrap="nowrap"><input id="transport_no" name="transport_no" value="${transport_no }" type="text" class="middle_txt">
          </td>  
       </tr>
       <tr>
       		<td class="right" nowrap="nowrap"  style="display: none">生产基地：</td>
			<td  nowrap="nowrap" style="display: none">
			 <script type="text/javascript">
				            genSelBoxExp("YIELDLY_TYPE",<%=Constant.PART_IS_CHANGHE%>,"<%=request.getAttribute("yieldly")%>",true,"","","false",'');
				        </script>
			</td>
	         <td class="right" nowrap="nowrap" >货运方式：</td>
	         <td  nowrap="nowrap">
	          <script type="text/javascript">
	            genSelBoxExp("freight_type",<%=Constant.OLD_RETURN_STATUS%>,"",true,"","","false",'');
	           </script>
	          </td>
	           <td class="right" nowrap="nowrap" >提报时间： </td>
         <td  nowrap="true">
			<input name="report_start_date" type="text" class="middle_txt" id="report_start_date" readonly="readonly" style="width: 80px;"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'report_start_date', false);" />  	
             &nbsp;至&nbsp; <input name="report_end_date" type="text" class="middle_txt" id="report_end_date" readonly="readonly" style="width: 80px;"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'report_end_date', false);" /> 
		</td>	
       </tr>
       <tr>
         <td class="center" colspan="4" nowrap="nowrap">
           <input class="u-button u-query" type="button" name="qryButton" value="查询"  onClick="__extQuery__(1);">
         </td>
       </tr>
  </table>
  </div>
  </div>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form> 
</div>
<br>
<script type="text/javascript">
   var myPage;
   //查询路径
	 var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/oldPartSignQueryList.json";
   var title = null;

   var columns = [
  				{header: "序号",align:'center',renderer:getIndex},
				{id:'action',header: "操作",sortable: false,dataIndex: 'id',renderer:myLink,align:'center'},
  				{header: "经销商代码",dataIndex: 'dealer_id',align:'center'},
  				{header: "经销商名称", dataIndex: 'dealer_name', align:'center'},
  				{header: "旧件回运清单号", dataIndex: 'claim_no', align:'center'},
  				{header: "旧件回运清单状态", dataIndex: 'back_desc', align:'center'},
  				{header: "基地", dataIndex: 'yieldly', align:'center',renderer:getItemValue},
  				{header: "提报日期", dataIndex: 'return_date', align:'center',renderer:formatDate},
  				{header: "签收日期", dataIndex: 'sign_date', align:'center'},
  				{header: "签收人", dataIndex: 'sign_name', align:'center'},
  				{header: "签收流水号", dataIndex: 'sign_no', align:'center'},
  				{header: "货运方式", dataIndex: 'return_desc', align:'center'},
  				{header: "旧件回运起止时间", dataIndex: 'wr_start_date', align:'center'},
  			//	{header: "索赔单数", dataIndex: 'wr_amount', align:'center'},
  				{header: "装箱数", dataIndex: 'parkage_amount', align:'center'},
  				//{header: "索赔配件数", dataIndex: 'part_amount', align:'center'},
  				{header: "发运单号", dataIndex: 'transport_no', align:'center'},
  				{header: "三包员电话", dataIndex: 'tel', align:'center'}
  		      ];
   //超链接设置
   function myLink(value,meta,record){
	   var back_type=record.data.back_type;
	   var box_no = record.data.box_no;//装箱单号
	   var currStatus = record.data.status;
 	  var   MonthFirstDay=new   Date(2012,5,1);     
	 
		 //  return String.format("<a href='#' onclick=isCheck1("+value+")>[打印]</a>"+"<a href='#' onClick='detailForwerd("+value+")'>[明细]</a>");
	 return String.format("<a href='#' onClick='detailForwerd("+value+")'>[明细]</a><a href='#' onClick='detailForwerd1("+record.data.tawor_id+")'>[装箱明细]</a>");
	
   }
   
   
   function detailForwerd1(value){
		 fm.action= "<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/queryBackClaimDetailInfo33Dealer.do?ORDER_ID="+value+"&oper=query";
			 fm.method="post";
	    fm.submit();
}
function detailForwerd(value){
 			 fm.action= "<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/oldPartSignQueryDetail.do?CLAIM_ID="+value;
 			 fm.method="post";
       	fm.submit();
}


	
   function isCheck1(id){
     window.open('<%=contextPath%>/claim/oldPart/ClaimBackPieceReportManager/oldPartSignQueryDetailPrint.do?id='+id,"旧件清单打印", "height=700, width=1024, top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=no, status=no");
   }
	
   //格式化时间为YYYY-MM-DD
   function formatDate(value,meta,record) {
	 if (value==""||value==null) {
		return "";
	 }else {
		return value.substr(0,16);
	 }
   }
   function doInit(){
	  loadcalendar();
	  __extQuery__(1);
   }
</script>
</BODY>
</html>