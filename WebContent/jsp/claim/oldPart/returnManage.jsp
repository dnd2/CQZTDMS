<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.infodms.dms.common.Constant" %>
<%@ page import="com.infodms.dms.common.FileConstant"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<title>索赔件回运清单管理</title>
<script type="text/javascript">
   var myPage;
   //查询路径
   var url = "<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/queryReturnOrderByCondition11.json";
				
   var title = null;

   var columns = [
  				{header: "序号",align:'center',renderer:getIndex},
  				{id:'action',header: "操作",sortable: false,dataIndex: 'id',renderer:operateLink,align:'center'},
  				{header: "状态", dataIndex: 'status_desc', align:'center'},
  				{header: "经销商简称",dataIndex: 'dealer_shortname',align:'center'},
  				{header: "旧件回运清单号",dataIndex: 'return_no',align:'center'},//return_no
  				{header: "旧件基地",dataIndex: 'yieldlyName',align:'center'}, 				 				
  				{header: "索赔单数量", dataIndex: 'wr_amount', align:'center'},
  				{header: "配件项数", dataIndex: 'part_item_amount', align:'center'},
  				{header: "配件数量", dataIndex: 'part_amount', align:'center'},  				
/*   			{header: "申报运费", dataIndex: 'price', align:'center',renderer:amountFormat},
  				{header: "审核运费", dataIndex: 'auth_price', align:'center',renderer:amountFormat}, */
  				{header: "回运时间段", dataIndex: 'wr_start_date', align:'center'},
  				{header: "建单日期", dataIndex: 'create_date', align:'center',renderer:formatDate},
  				{header: "提报日期", dataIndex: 'return_date', align:'center',renderer:formatDate}
  		      ];
   //页面初始化	
   function doInit(){
     //loadcalendar();
     __extQuery__(1);
   }
   function amountFormat() {}
   //超链接设置
   function operateLink(value,meta,record){
	   var width=800;
	   var height=500;
	   var screenW = 800;	
	   var screenH = 500;
	   if(screenW!=null && screenW!='undefined')
		   width = screenW;
	   if(screenH!=null && screenH!='undefined')
		   height = screenH;
   	   var status=record.data.status;
   	   var dealerLevel = record.data.dealer_level;
   	   var dealerId = record.data.dealer_id;
   	   var seflDealerId = record.data.self_dealer_id;
       var returnId = record.data.return_id;//物流单ID
       var box_no = record.data.box_no;//装箱单号
       var old_status = record.data.status;//物流单状态
       var return_type = record.data.return_type;//物流单状态
       var borrow_no=record.data.borrow_no;
       //var  IS_DELAY =record.data.is_delay;//是否延期
       //var  re_status =record.data.re_status;//冻结状态
       var str = "";
       if(old_status=='<%=Constant.BACK_LIST_STATUS_01%>'){//未上报
    	   str = "<a href='#' onclick=\"boxOper("+returnId+","+borrow_no+","+width+","+height+")\">[装箱]</a>"
   	    	   + "<a href=\"#\" onclick='checkTransNo(\""+returnId+"\",\""+box_no+"\")'>[上报]</a>"
   	    	   + "<a href='#' onclick=\"reportOrder1("+returnId+","+return_type+","+box_no+","+width+","+height+")\">[补录]</a>";
   	    	       //+"<a href='#' onClick=\"deleteOrder("+value+");\">[删除]</a>"
   	   	          ;
       }else if(old_status=='<%=Constant.BACK_LIST_STATUS_02%>'){//已上报
    	   str = "<a href='#' onclick=\"reportOrder1("+returnId+","+return_type+","+box_no+","+width+","+height+")\">[补录]</a>"
    	       + "<a href='#' onClick='OpenHtmlWindow(\"<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/queryBackClaimDetailInfo33.do?ORDER_ID="+returnId+"&oper=query\","+1050+","+500+")'>[明细]</a>"
			   + "<a href='<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/queryBoxNo.do?id="+returnId+"'>[打印]</a>";
   	   }else{
   		str = "<a href='#' onClick='OpenHtmlWindow(\"<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/queryBackClaimDetailInfo33.do?ORDER_ID="+returnId+"&oper=query\","+1050+","+500+")'>[明细]</a>"
		    + "<a href='<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/queryBoxNo.do?id="+returnId+"'>[打印]</a>";
   	   }
       return String.format(str);
   }
   
   //首先检查是否填写货运单号，否则不予上报
   function checkTransNo(report_id,boxNumber){
	   if(boxNumber>0){
	       MyAlert('有数据未填写装箱单号!');
	       return;
	   }else{
		   //验证补录运货信息
		   //var url= "<%=contextPath%>/claim/oldPart/ClaimBackPieceReportManager/checkTransNo.json?report_id="+report_id;
		   //makeNomalFormCall(url,isReport,'fm','createOrdBtn');
		   //不须验证补录信息
		   MyConfirm("确认上报吗？",report,[report_id]);
	   }
   }
   function isReport(json){
	   var isFillTransNo=json.flag;
	   var report_id=json.report_id;
	   var str = json.package;
	   var note = json.note;
	    var price = json.price;
	   //暂时去掉限制货运单号的限制(要放开去掉注释就OK)
	   /*if(isFillTransNo=='0'){
           MyAlert("货运单号不能为空!");
           return;
	   }*/
	   if(str=="false"){
	    MyAlert("装箱总数与实际装箱数不符!");
           return;
	   }else if(note!=""){
	   		MyAlert(note);
	   		return false;
	   }else if(price==0) {
	   		MyConfirm("运费为0,确认上报吗？",report,[report_id]);
	   }else{
	    	MyConfirm("确认上报吗？",report,[report_id]);
	   }
   }
   function report(str){
	   fm.report_id.value=str;
      // fm.action="<%=contextPath%>/claim/oldPart/ClaimBackPieceReportManager/reportClaimInfo22.do";
      // fm.submit();
       var url = "<%=contextPath%>/claim/oldPart/ClaimBackPieceReportManager/reportClaimInfo22.json";
       makeNomalFormCall(url,isReports,'fm','createOrdBtn');
   }
function isReports(json){
  if(json.SUCCESS=='true'){
		   MyAlert('上报成功！');
		   __extQuery__(1);
	   }else{
	   	    MyAlert('上报失败！');
	   	     __extQuery__(1);
	   }
}
   //装箱操作
   //function boxOper(value,width,height){		
        //var i_url="<%=contextPath %>/claim/oldPart/ClaimBackPieceBackListOrdManager/queryBackClaimDetailInfo11.do?ORDER_ID="+value+"&oper=mod";
        //OpenHtmlWindow(i_url,width,height);
   //}
   function boxOper(value,borrow_no){		
        var i_url="<%=contextPath %>/claim/oldPart/ClaimBackPieceBackListOrdManager/queryBackClaimDetailInfo11.do?ORDER_ID="+value+"&oper=mod"+"&borrow_no="+borrow_no;
        location=i_url,width,height;
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
		return value.substr(0,16);
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
   }

   function refreshPage11(json){
	   var msg=json.msg;
	   var returnId=json.returnId;
	   if(msg=='ok'){
	   		// MyAlert("删除成功!");
		   var turl = "<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/deleteReturnOrder11.json?returnId="+returnId;
		   makeNomalFormCall(turl,refreshPage,'fm');
	   }else{
		   MyAlert('请从回运开始-结束日期由大到小删除!');
		   return false;
	   }
   }
   
	$(document).ready(function(){
		//__extQuery__(1);
		notice();
	});

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
	   MyConfirm("确认上报？",chanageOrderStatus,[returnId,<%=Constant.RETURNORDER_TYPE_05%>]);
   }
   //补录
   function reportOrder1(value,return_type,box_no){	
	 //  if(Number(box_no)>0){
	//	   MyAlert('有数据未填写装箱单号!');
	  //     return;
	  // }else{
		   var i_url="<%=contextPath %>/claim/oldPart/ClaimBackPieceBackListOrdManager/queryBackClaimDetailInfo22.do?ORDER_ID="+value+"&return_type="+return_type;
		   location.href=i_url;
	  // }
   }

   //变更索赔单状态
   function chanageOrderStatus(returnId,status){
	   var turl = "<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/chanageReturnOrderStatus11.json?returnId="+returnId+"&status="+status;
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
   var canAdd = document.getElementById("canAdd").value;
   var dealerLevel = document.getElementById("dealerLevel").value
   if(dealerLevel=="10851002"){
			MyAlert("二级经销商不能创建回运单!");
			return false;
		}
  // if(canAdd=="notOpen"){
  // MyAlert("请在每月的4号(不包括)后新增回运单!");
 //  return false;
//	}else{ 
	fm.action = "<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/addOldReturnOrder.do";
	   fm.submit();
  // }
   }
   //刷新本页面，供子页面使用
   function refreshPage(json){
	   if(json.SUCCESS=='FAILURE'){
		   MyAlert('操作失败，请重新操作！');
		   return false;
	   }else{
	   MyAlert("操作成功！");
	   	   __extQuery__(1);
	   }
   }


   //测试旧件打印
   function barCode(){
	   document.forms[0].target="_blank";
	   document.forms[0].method="post";
	   fm.action = "<%=contextPath%>/claim/dealerClaimMng/ClaimBillTrack/barcodePrintDoGet.do?";
	 
	   fm.submit();
	   }
	   
	   function notice(){
	   var msg = document.getElementById("msg").value;
	   if(msg!=null&&msg!=""){
	 //  MyAlert(msg);
	   document.getElementById("msg").value="";
	   }
	   }
</script>
</head>
<body >
<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理 &gt;索赔件回运清单管理</div>
  <form id="fm" name="fm">
   <div class="form-panel">
				<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
				<div class="form-body">
  	  <input type="hidden" name="curPage" id="curPage" value="1" />
      <input type="hidden" name="delId" id="delId" value="" />
      <input type="hidden" name="canAdd" id="canAdd" value="${canAdd }" />
       <input type="hidden" name="dealerLevel" id="dealerLevel" value="${dealerLevel }" />
      <input type="hidden" name="report_id" id="report_id" value="" />
     <input type="hidden" name="dtlIds" id="dtlIds" value="2012042543012287" />
      <input type="hidden" name="msg" id="msg" value="${msg }" />
    <table class="table_query">
       <tr>
         <td class="right">回运清单号：</td>
         <td>
           <input id="back_order_no" name="back_order_no" value="" type="text" maxlength="20" class="middle_txt" datatype="1,is_null,50" callFunction="javascript:MyAlert();">
         </td>
       	 <td class="right">回运清单状态：</td>
         <td>
           <script type="text/javascript"> 
             genSelBoxExp("ord_status",<%=Constant.BACK_LIST_STATUS%>,"","true","u-select","","false",'');
           </script>
         </td>
       </tr>
       <tr>
          <td class="right">提报时间： </td>
          <td class="left">
            <input id="report_start_date" name="report_start_date" readonly class="Wdate" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="display: inline-block;min-width: 60px;width: 150px;height: 30px;padding: 4px 5px;margin: 0 4px;border: #b8b8b8 solid 1px;border-radius: 3px;box-shadow: 0 1px 3px #ececec inset;background-color: #fefefe;color: #5a5a5a;outline: none;box-sizing: border-box;"/>
            &nbsp;至&nbsp; 
		    <input id="report_end_date" name="report_end_date" readonly class="Wdate" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="display: inline-block;min-width: 60px;width: 150px;height: 30px;padding: 4px 5px;margin: 0 4px;border: #b8b8b8 solid 1px;border-radius: 3px;box-shadow: 0 1px 3px #ececec inset;background-color: #fefefe;color: #5a5a5a;outline: none;box-sizing: border-box;"/>
		  </td>
          <td class="right" >建单时间：</td>
          <td class="left">
            <input id="create_start_date" name="create_start_date" readonly class="Wdate" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="display: inline-block;min-width: 60px;width: 150px;height: 30px;padding: 4px 5px;margin: 0 4px;border: #b8b8b8 solid 1px;border-radius: 3px;box-shadow: 0 1px 3px #ececec inset;background-color: #fefefe;color: #5a5a5a;outline: none;box-sizing: border-box;"/>
            &nbsp;至&nbsp; 
            <input id="create_end_date" name="create_end_date" readonly class="Wdate" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" style="display: inline-block;min-width: 60px;width: 150px;height: 30px;padding: 4px 5px;margin: 0 4px;border: #b8b8b8 solid 1px;border-radius: 3px;box-shadow: 0 1px 3px #ececec inset;background-color: #fefefe;color: #5a5a5a;outline: none;box-sizing: border-box;"/>
		</td>	
        	
       </tr>
       <tr>
         <td colspan="11" class="center">
         <input class="u-button u-query" type="button" name="qryButton" value="查询"  onClick="__extQuery__(1);">
          <!-- <input class="u-button u-submit" type="button" id="addButton" name="addButton" value="新增"  onClick="addBackOrd();"> -->
          <!-- <input class="normal_btn" type="button" name="Button" style="width: 200px;" value="条码打印"  onClick="barCode();"> --> 
           </td>
       </tr>
  </table>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end --> 
</form> 
</body>
</html>