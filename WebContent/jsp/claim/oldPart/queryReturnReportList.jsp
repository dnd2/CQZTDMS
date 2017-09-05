<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.infodms.dms.common.Constant" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>索赔件回运清单上报</title>
<% String contextPath = request.getContextPath(); %>
</head>
<BODY onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理 &gt;索赔件回运清单上报</div>
  <form id="fm" name="fm">
  	  <input type="hidden" name="curPage" id="curPage" value="1" />
      <input type="hidden" name="report_id" id="report_id" value="" />
    <TABLE class="table_query">
       <tr>
         <td align="right" nowrap>物流单号：</td>
         <td><input id="back_order_no" name="back_order_no" value="" type="text" class="middle_txt" datatype="1,is_null,16" callFunction="javascript:MyAlert();"></td>
         <td align="right" nowrap>货运方式：</td>
         <td align="left">
          <script type="text/javascript">
            genSelBoxExp("freight_type",<%=Constant.OLD_RETURN_STATUS%>,"",true,"short_sel","","true",'');
          </script>
         </td>
       </tr>
       <tr>
         <td width="7%" align="right" nowrap >建单时间： </td>
         <td nowrap colspan="3">
          <input name="create_start_date" id="create_start_date" value="" type="text" class="short_txt" datatype="1,is_date,10" group="create_start_date,create_end_date" hasbtn="true" callFunction="showcalendar(event, 'create_start_date', false);">
         	&nbsp;至&nbsp;
          <input name="create_end_date" id="create_end_date" value="" type="text" class="short_txt" datatype="1,is_date,10" group="create_start_date,create_end_date" hasbtn="true" callFunction="showcalendar(event, 'create_end_date', false);"></td>
       </tr>
       <tr>
         <td align="center" colspan="4">
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
   var url = "<%=contextPath%>/claim/oldPart/ClaimBackPieceReportManager/queryBackListByCondition.json";
				
   var title = null;

   var columns = [
  				{header: "序号",align:'center',renderer:getIndex},
  				{header: "物流单号",dataIndex: 'return_no',align:'center'},
  				{header: "建单日期", dataIndex: 'create_date', align:'center',renderer:formatDate},
  				{header: "索赔单数", dataIndex: 'wr_amount', align:'center'},
  				{header: "配件项数", dataIndex: 'part_item_amount', align:'center'},
  				{header: "配件数", dataIndex: 'part_amount', align:'center'},
  				{header: "装箱总数", dataIndex: 'parkage_amount', align:'center'},
  				{header: "货运方式", dataIndex: 'freight_type', align:'center'},
  				{header: "处理方式", dataIndex: 'status_desc', align:'center'},
  				{id:'action',header: "操作",sortable: false,dataIndex: 'id',renderer:myLink,align:'center'}
  		      ];
   //超链接设置
   function myLink(value,meta,record){
	  var status=record.data.status;
	  var return_no=record.data.return_no;
	  var boxNumber = record.data.box_number;
	  if(status==<%=Constant.BACK_LIST_STATUS_01%>){
		//return String.format("<a href='#' onClick=\"checkTransNo("+value+");\">[上报]</a><a href='#' onClick=\"returnPrint("+value+");\">[打印]</a>");
		return String.format("<a href=\"#\" onclick='checkTransNo(\""+value+"\",\""+boxNumber+"\")'>[上报]</a>");
	  }
   }
   //格式化时间为YYYY-MM-DD
   function formatDate(value,meta,record) {
	 if (value==""||value==null) {
		return "";
	 }else {
		return value.substr(0,10);
	 }
   }
   //首先检查是否填写货运单号，否则不予上报
   function checkTransNo(report_id,boxNumber){
	   if(boxNumber>0){
	       MyAlert('有数据未填写装箱单号!');
	       return;
	   }else{
		   var url= "<%=contextPath%>/claim/oldPart/ClaimBackPieceReportManager/checkTransNo.json?report_id="+report_id;
		   makeNomalFormCall(url,isReport,'fm','createOrdBtn');
	   }
   }

   function returnPrint(returnId){
	   window.open('<%=contextPath%>/claim/oldPart/ClaimBackPieceReportManager/roMainPrint.do?id='+returnId,"旧件清单打印", "height=700, width=1024, top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=no, status=no");
	 }
   function isReport(json){
	   var isFillTransNo=json.flag;
	   var report_id=json.report_id;
	   if(isFillTransNo=='0'){
           MyAlert("货运单号不能为空!");
           return;
	   }
	   MyConfirm("确认上报吗？",report,[report_id]);
   }
   function report(str){
	   fm.report_id.value=str;
       fm.action="<%=contextPath%>/claim/oldPart/ClaimBackPieceReportManager/reportClaimInfo.do";
       fm.submit();
   }
   function doInit(){
	   loadcalendar();
   }
</script>
</BODY>
</html>