<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@page import="java.util.List"%>
<%@page import="com.infodms.dms.po.TmDealerPO"%><html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.infodms.dms.common.Constant" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>索赔件回运清单管理</title>
<% String contextPath = request.getContextPath(); %>
</head>


<%
	String dealerLevel = (String)request.getAttribute("dealerLevel");
%>


<body onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理 &gt;索赔件回运清单管理</div>
  <form id="fm" name="fm">
  	  <input type="hidden" name="curPage" id="curPage" value="1" />
      <input type="hidden" name="delId" id="delId" value="" />
    <table class="table_query">
       <tr>
         <td align="right" nowrap="nowrap">回运清单号：</td>
         <td><input id="back_order_no" name="back_order_no" value="" type="text" class="middle_txt" datatype="1,is_null,16" callFunction="javascript:MyAlert();"></td>
		 <td align="right" nowrap="nowrap">处理状态：</td>
         <td>
           <script type="text/javascript">
            genSelBoxExp("ord_status",<%=Constant.RETURNORDER_TYPE%>,"",true,"short_sel","","true",'');
           </script>
         </td>
       </tr>
       <tr>
          <td align="right" nowrap="nowrap" >建单时间：<input type="hidden" id="dealerLevel"  name= "dealerLevel" value="<%=dealerLevel %>"/> </td>
          <td align="left" nowrap="nowrap">
           <input name="create_start_date" id="create_start_date" value="" type="text" class="short_txt" datatype="1,is_date,10" group="create_start_date,create_end_date" hasbtn="true" callFunction="showcalendar(event, 'create_start_date', false);">
         	&nbsp;至&nbsp;
           <input name="create_end_date" id="create_end_date" value="" type="text" class="short_txt" datatype="1,is_date,10" group="create_start_date,create_end_date" hasbtn="true" callFunction="showcalendar(event, 'create_end_date', false);"></td>
          <td align="right" nowrap="nowrap">上报时间： </td>
          <td align="left" nowrap="nowrap">
           <input name="report_start_date" id="report_start_date" value="" type="text" class="short_txt" datatype="1,is_date,10" group="report_start_date,report_end_date" hasbtn="true" callFunction="showcalendar(event, 'report_start_date', false);">
         	&nbsp;至&nbsp;
           <input name="report_end_date" id="report_end_date" value="" type="text" class="short_txt" datatype="1,is_date,10" group="report_start_date,report_end_date" hasbtn="true" callFunction="showcalendar(event, 'report_end_date', false);">
          </td>
       </tr>
       <tr>
       	   <td align="right" nowrap="nowrap">经销商：</td>
       	   <td align="left">
       	   		<select name="dealeId" class="short_sel">
       	   			<option value="">请选择</option>
       	   		<% List dealerList = (List)request.getAttribute("dealerList"); 
       	   		   if(dealerList!=null){
       	   		   for(int i=0;i<dealerList.size();i++){
       	   			   TmDealerPO dealerPO = (TmDealerPO)dealerList.get(i);
       	   		   	%>
       	   			   <option value="<%=dealerPO.getDealerId()%>"><%=dealerPO.getDealerShortname()%></option>
       	   		<% }}%>
       	   		</select>
       	   </td>
       </tr>
       <tr>
         <td align="center" colspan="4"><input class="normal_btn" type="button" name="qryButton" value="查询"  onClick="__extQuery__(1);">
           <input class="normal_btn" type="button" id="addButton" name="addButton" value="新增"  onClick="addBackOrd();"></td>
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
   var url = "<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/queryReturnOrderByCondition.json";
				
   var title = null;

   var columns = [
  				{header: "序号",align:'center',renderer:getIndex},
  				{header: "经销商简称",dataIndex: 'dealer_name',align:'center'},
  				{header: "旧件清单号",dataIndex: 'return_no',align:'center'},
  				{header: "产地",dataIndex: 'yieldly',align:'center',renderer:getItemValue},
  				{header: "类型",dataIndex: 'dealer_level',align:'center',renderer:getItemValue},
  				{header: "建单日期", dataIndex: 'create_date', align:'center',renderer:formatDate},
  				{header: "提报日期", dataIndex: 'return_date', align:'center',renderer:formatDate},
  				{header: "开始-结束时间", dataIndex: 'wr_start_date', align:'center'},
  				{header: "索赔单数量", dataIndex: 'wr_amount', align:'center'},
  				{header: "配件项数", dataIndex: 'part_item_amount', align:'center'},
  				{header: "配件数量", dataIndex: 'part_amount', align:'center'},
  				{header: "处理状态", dataIndex: 'status_desc', align:'center'},
  				{id:'action',header: "操作",sortable: false,dataIndex: 'id',renderer:operateLink,align:'center'}
  		      ];
   //超链接设置
   function operateLink(value,meta,record){
   	  var status=record.data.status;
   	  var dealerLevel = record.data.dealer_level;
   	  var dealerId = record.data.dealer_id;
   	  var seflDealerId = record.data.self_dealer_id;
   	  var resultStr = "";
   	  var dealerLevel11 = document.getElementById("dealerLevel").value;//得到到底是几级经销商进来的
	  //判断1级2级进来的经销商执行不同的方法
   	  if(dealerLevel11=='<%=Constant.DEALER_LEVEL_01%>'){
   	   	  if(dealerId==seflDealerId && (status=='<%=Constant.RETURNORDER_TYPE_02%>' || (dealerLevel==<%=Constant.DEALER_LEVEL_01%> && status=='<%=Constant.RETURNORDER_TYPE_01%>'))){
   	   	   	  resultStr = resultStr + "<a href='#' onClick=\"completeOrder("+value+");\">[完成]</a>";
   	   	  }
          if(status=='<%=Constant.RETURNORDER_TYPE_02%>'){
              resultStr = resultStr + "<a href='#' onClick=\"returnOrder("+value+");\">[退回]</a>"; 
          }
          if(status=='<%=Constant.RETURNORDER_TYPE_01%>' && dealerLevel==<%=Constant.DEALER_LEVEL_01%>){
              resultStr = resultStr + "<a href='#' onClick=\"deleteOrder("+value+");\">[删除]</a>";
          }
   	  }else{
   		  if(dealerId==seflDealerId && (status=='<%=Constant.RETURNORDER_TYPE_01%>' || (dealerLevel==<%=Constant.DEALER_LEVEL_02%> && status=='<%=Constant.RETURNORDER_TYPE_01%>'))){
 	   	      resultStr = resultStr + "<a href='#' onClick=\"completeOrder("+value+");\">[完成]</a>";
 	   	  }else if(dealerId!=seflDealerId && status=='<%=Constant.RETURNORDER_TYPE_01%>'){
 	   		  resultStr = resultStr + "<a href='#' onClick=\"reportOrder("+value+");\">[上报]</a>";
 	 	  }
   	   	  if(status=='<%=Constant.RETURNORDER_TYPE_01%>'){//未上报
   	   	   	  resultStr = resultStr + "<a href='#' onClick=\"deleteOrder("+value+");\">[删除]</a>";
	   	  }
   	  }
   	  resultStr = resultStr + String.format("<a href='#' onClick=\"queryDetail("+value+");\">[明细]</a>");
   	  return resultStr;
   }

   //打印
   function returnPrint(returnId){
	   window.open('<%=contextPath%>/claim/oldPart/ClaimBackPieceReportManager/roMainPrint.do?id='+returnId,"旧件清单打印", "height=700, width=1024, top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=no, status=no");
   }
   
   //格式化时间为YYYY-MM-DD
   function formatDate(value,meta,record) {
	 if (value==""||value==null) {
		return "";
	 }else {
		return value.substr(0,10);
	 }
   }
   //删除对应回运清单
   function deleteOrder(del_id){
	   MyConfirm("确认删除？",delReturnInfo,[del_id]);
       
   }

   //删除对应回去清单
   function delReturnInfo(returnId){
	   var turl11 = "<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/deleteReturnOrder1.json?returnId="+returnId;
	   makeNomalFormCall(turl11,refreshPage11,'fm');
	   //var turl = "<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/deleteReturnOrder.json?returnId="+returnId;
	   //makeNomalFormCall(turl,refreshPage,'fm');
   }

   function refreshPage11(json){
	   var msg=json.msg;
	   var returnId=json.returnId;
	   if(msg=='ok'){
		   var turl = "<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/deleteReturnOrder.json?returnId="+returnId;
		   makeNomalFormCall(turl,refreshPage,'fm');
	   }else{
		   MyAlert('请删除之前月份数据,不能跨月删除!');
	   }
   }
   
   function doInit(){
	   loadcalendar();
   }

   //点击“退回”时动作
   function returnOrder(returnId){
	   MyConfirm("确认退回？",chanageOrderStatus,[returnId,<%=Constant.RETURNORDER_TYPE_01%>]);
   }

   //点击“完成”时动作
   function completeOrder(returnId){
	   MyConfirm("确认完成？",chanageOrderStatus,[returnId,<%=Constant.RETURNORDER_TYPE_03%>]);
   }

   //点击“上报”时动作
   function reportOrder(returnId){
	   MyConfirm("确认上报？",chanageOrderStatus,[returnId,<%=Constant.RETURNORDER_TYPE_02%>]);
   }

   //变更索赔单状态
   function chanageOrderStatus(returnId,status){
	   var turl = "<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/chanageReturnOrderStatus.json?returnId="+returnId+"&status="+status;
	   makeNomalFormCall(turl,refreshPage,'fm');
   }

   //回运清单明细
   function queryDetail(returnId){
		var tarUrl = "<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/queryReturnOrderDetail.do?ORDER_ID="+returnId;
		var width=900;
		var height=500;

		var screenW = window.screen.width-30;	
		var screenH = document.viewport.getHeight();

		if(screenW!=null && screenW!='undefined')
			width = screenW;
		if(screenH!=null && screenH!='undefined')
			height = screenH;

		OpenHtmlWindow(tarUrl,screenW,screenH);
   }
   
   //新增索赔件清单
   function addBackOrd(){
	   fm.action = "<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/addReturnOrderPage.do";
	   fm.submit();
   }
   
   //刷新本页面，供子页面使用
   function refreshPage(json){
	   if(json.SUCCESS=='FAILURE')
		   MyAlert('操作失败，请重新操作！');
	   	   __extQuery__(1);
   }
</script>
</body>
</html>