<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.infodms.dms.common.Constant" %>
<%@taglib uri="/jstl/change" prefix="change" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>旧件回运延期申请</title>
<% String contextPath = request.getContextPath(); %>
</head>
<body onload="doInit();notice();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理 &gt;旧件回运延期申请</div>
  <form id="fm" name="fm">
  	  <input type="hidden" name="curPage" id="curPage" value="1" />
      <input type="hidden" name="delId" id="delId" value="" />
      <input type="hidden" name="canAdd" id="canAdd" value="${canAdd }" />
       <input type="hidden" name="dealerLevel" id="dealerLevel" value="${dealerLevel }" />
      <input type="hidden" name="report_id" id="report_id" value="" />
     <input type="hidden" name="dtlIds" id="dtlIds" value="2012042543012287" />
      <input type="hidden" name="msg" id="msg" value="${msg }" />
            
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
   var url = "<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/queryReturnOrderByCondition11lj.json";
				
   var title = null;

   var columns = [
  				{header: "序号",align:'center',renderer:getIndex},
  				{id:'action',header: "操作",sortable: false,dataIndex: 'id',renderer:operateLink,align:'center'},
  				{header: "经销商简称",dataIndex: 'dealer_shortname',align:'center'},
  				{header: "旧件回运清单号",dataIndex: 'old_no',align:'center'},//return_no
  				{header: "状态", dataIndex: 'old_status', align:'center',renderer:getItemValue},
  				{header: "建单时间", dataIndex: 'create_date', align:'center'},
  				{header: "索赔单数量", dataIndex: 'wr_amount', align:'center'},
  				{header: "配件项数", dataIndex: 'part_item_amount', align:'center'},
  				{header: "配件数量", dataIndex: 'part_amount', align:'center'}
  		      ];
  		      __extQuery__(1);
   //超链接设置
   function operateLink(value,meta,record){
	   var width=800;
	   var height=500;
	   var screenW = window.screen.width-30;	
	   var screenH = document.viewport.getHeight();
	   if(screenW!=null && screenW!='undefined')
		   width = screenW;
	   if(screenH!=null && screenH!='undefined')
		   height = screenH;
   	   var status=record.data.status;
   	   var wr_amount = record.data.wr_amount;
   	   var part_item_amount = record.data.part_item_amount;
   	   var dealer_shortname = record.data.dealer_shortname;
   	   var part_amount = record.data.part_amount;
   	   var dealerLevel = record.data.dealer_level;
   	   var dealerId = record.data.dealer_id;
   	   var seflDealerId = record.data.self_dealer_id;
       var returnId = record.data.return_id;//物流单ID
       var box_no = record.data.box_no;//装箱单号
       var old_status = record.data.old_status;//物流单状态
       var return_type = record.data.return_type;//物流单状态
       var borrow_no=record.data.borrow_no;
     //  var status = record.data.old_status;
        var return_no = record.data.old_no;//回运清单号
	if($('dealerLevel').value=='10851002'){
	return String.format(
				   "<a href='#' onClick='OpenHtmlWindow(\"<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/queryBackClaimDetailInfo33.do?ORDER_ID="+returnId+"&oper=query\","+1050+","+500+")'>[明细]</a>"
		   );
	}else{
       if(old_status=='<%=Constant.BACK_LIST_STATUS_01%>' && record.data.return_type == 10731002){
    	   return String.format(
    	    "<a href='#' onClick=\"updateOrder('"+value+"','"+return_no+"');\">[延期申请]</a>" );
       }
   	   }
   	   var url = "";
   	  // var urlView="<%=contextPath%>/OldReturnAction/oldPartApplyInitAutid.do?id="+value+"&type=view";
	    	url+="<b>[已延期]</b>";
	        return String.format(url);
	        
   }
   //返回
  
   function report(str){
	   fm.report_id.value=str;
       var url = "<%=contextPath%>/claim/oldPart/ClaimBackPieceReportManager/reportClaimInfo22.json";
       makeNomalFormCall(url,isReports,'fm','createOrdBtn');
   }
   //延期申请操作
    function updateOrder(value,return_no){	
           var urlReport="<%=contextPath%>/OldReturnAction/oldPartapplycheck.json?value="+value+"&return_no="+return_no;
	        sendAjax(urlReport,function(json){
	    		if(json.succ=="-1"){
	    			var i_url="<%=contextPath %>/claim/oldPart/ClaimBackPieceBackListOrdManager/queryBackClaimDetailInfo11lj.do?return_no="+return_no+"&oper=mod";
                    location=i_url,width,height;
	    		}else{
	    			MyAlert("提示：操作失败，已进行申请操作！");
	    			__extQuery__(1);
	    		}
	    	},'fm');
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
   }
   //变更索赔单状态
   function chanageOrderStatus(returnId,status){
	   var turl = "<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/chanageReturnOrderStatus11.json?returnId="+returnId+"&status="+status;
	   makeNomalFormCall(turl,refreshPage,'fm');
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
	   function notice(){
	   var msg = document.getElementById("msg").value;
	   if(msg!=null&&msg!=""){
	 //  MyAlert(msg);
	   document.getElementById("msg").value="";
	   }
	   }
	    function  returnapply(){
              window.location.href="<%=contextPath%>/OldReturnAction/oldPartApplyList.do";
        }
</script>
 <input type="button" value="&nbsp;&nbsp;返&nbsp;&nbsp;回&nbsp;&nbsp;" onclick="returnapply();" style="margin-left: 45%;margin-right: auto;width: 60px;" />
</body>
</html>