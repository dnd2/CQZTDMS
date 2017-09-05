<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@page import="com.infodms.dms.util.CommonUtils"%><html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.infodms.dms.common.Constant" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="com.infodms.dms.po.TmBusinessAreaPO,java.util.*" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>索赔旧件审批入库</title>
<% String contextPath = request.getContextPath();
	String isReutrn=request.getAttribute("isReturn")==null?"":request.getAttribute("isReturn").toString();

	List<TmBusinessAreaPO> list = (List<TmBusinessAreaPO>)request.getAttribute("yieldlyList");
	if(isReutrn.equals("1")) isReutrn="__extQuery__(1);";
%>
</head>
<BODY onload="doInit();<%=isReutrn %>">
	<div class="wbox">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理&gt;旧件签收</div>
  <form id="fm" name="fm">
  	  <input type="hidden" name="curPage" id="curPage" value="1" />
      <input type="hidden" name="delId" id="delId" value="" />
      <div class="form-panel">
				<h2><img src="<%=contextPath%>/jmstyle/img/search-ico.png" style="margin-bottom: -13px;"/>查询条件</h2>
				<div class="form-body">
    <TABLE class="table_query">
       <tr>
         <td class="right"  >经销商代码：</td>            
         <td  >
        <input id="dealerCode" name="dealerCode" value="${dealerCode }" type="text" class="middle_txt">
         </td>
         <td class="right" >经销商名称：</td>
         <td ><input id="dealerName" name="dealerName" value="${dealerName }" type="text" class="middle_txt" datatype="1,is_name,15" callFunction="javascript:MyAlert();"></td>
       </tr>
       <tr>
          <td class="right" >旧件回运清单号：</td>
         <td ><input id="back_order_no" name="back_order_no" value="${back_order_no }" type="text" class="middle_txt"></td>
       <td class="right" >发运单号：</td>
          <td ><input id="trans_no" name="transport_no" value="${transport_no }" type="text" class="middle_txt">
          </td> 
       </tr>
       <tr>
       		<td class="right"  style="display: none">生产基地：</td>
			<td  style="display: none">
			 <script type="text/javascript">
				            genSelBoxExp("YIELDLY_TYPE",<%=Constant.PART_IS_CHANGHE%>,"<%=request.getAttribute("yieldly")%>",true,"","","false",'');
				        </script>
			</td>
	         <td class="right" >货运方式：</td>
	         <td >
	          <script type="text/javascript">
	            genSelBoxExp("freight_type",<%=Constant.OLD_RETURN_STATUS%>,"${freight_type }",true,"","","false",'');
	           </script>
	          </td>
        
         <td class="right" >提报时间： </td>
         <td align="left" nowrap="true">
			<input name="report_start_date" type="text" class="middle_txt" id="report_start_date" style="width: 80px;"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'report_start_date', false);" />  	
             &nbsp;至&nbsp; <input name="report_end_date" type="text" class="middle_txt" id="report_end_date" style="width: 80px;"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'report_end_date', false);" /> 
		</td>
         </tr>
       <tr>
          <td class="right" >旧件回运清单状态：</td>
          <td >
            <script type="text/javascript">
             genSelBoxExp("back_type",<%=Constant.BACK_LIST_STATUS%>,"${back_type }",true,"","","false",'<%=Constant.BACK_LIST_STATUS_01%>,<%=Constant.BACK_LIST_STATUS_04%>');
            </script>
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
   var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/queryApproveList11.json";
				
   var title = null;

   var columns = [
  				{header: "序号",align:'center',renderer:getIndex},
				{id:'action',header: "操作",sortable: false,dataIndex: 'id',renderer:myLink,align:'center'},
  				{header: "经销商代码",dataIndex: 'dealer_id',align:'center'},
  				{header: "经销商名称", dataIndex: 'dealer_name', align:'center'},
  				{header: "旧件回运清单号", dataIndex: 'claim_no', align:'center'},
  				{header: "旧件回运清单状态", dataIndex: 'back_desc', align:'center'},
  				{header: "基地", dataIndex: 'yieldly', align:'center',renderer:setYieldlyName},//getItemValue
  				{header: "提报日期", dataIndex: 'return_date', align:'center',renderer:formatDate},
  				{header: "签收日期", dataIndex: 'create_date', align:'center'},
  				{header: "签收人", dataIndex: 'in_warhouse_name', align:'center'},
  				//{header: "签收流水号", dataIndex: 'sign_no', align:'center'},
  				//{header: "回运类型", dataIndex: 'return_type', align:'center',renderer:getItemValue},
  				{header: "货运方式", dataIndex: 'return_desc', align:'center'},
  				{header: "旧件回运起止时间", dataIndex: 'wr_start_date', align:'center'},
  				//{header: "索赔单数", dataIndex: 'wr_amount', align:'center'},
  				{header: "装箱数", dataIndex: 'parkage_amount', align:'center'},
  			//	{header: "索赔配件数", dataIndex: 'part_amount', align:'center'},
  				{header: "发运单号", dataIndex: 'transport_no', align:'center'},
  				{header: "三包员电话", dataIndex: 'tel', align:'center'}
  		      ];
  		      __extQuery__(1);
   //写死基地
   function setYieldlyName(value,meta,record){
	   return String.format("长沙");
   }
   //超链接设置
   function myLink(value,meta,record){
	   var back_type=record.data.back_type;
	   var box_no = record.data.box_no;//装箱单号
	   var currStatus = record.data.status;
 	  var   MonthFirstDay=new   Date(2012,5,1);     
	 
	  
	   if(back_type=='<%=Constant.BACK_LIST_STATUS_02%>'){
		   //return String.format("<a href='#' onclick=isCheck12("+value+")>[签收入库]</a>"+"<a href='#' onclick=isCheck1("+value+")>[打印]</a>"+"<a href='#' onClick='OpenHtmlWindow(\"<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/queryDetail11.do?CLAIM_ID="
	       //        +value+"\",900,500)'>[明细]</a>");
	       return String.format("<a href='#' onclick=isCheck12("+value+",0)>[签收]</a>"+"<a href='#' onClick='detailForwerd(\""+record.data.tawor_id+"\")'>[装箱明细]</a>");
	}else if(back_type=='<%=Constant.BACK_LIST_STATUS_03%>')
	{
	     return String.format("<a href='#' onclick=isCheck12("+value+",1)>[撤销签收]</a>"+"<a href='#' onClick='detailForwerd(\""+record.data.tawor_id+"\")'>[装箱明细]</a>");
	}
	else{
		   return String.format("<a href='#' onClick='detailForwerd(\""+record.data.tawor_id+"\")'>[装箱明细]</a>");
	
	}
   }
function detailForwerd(value){
 			 fm.action= "<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/queryBackClaimDetailInfo33Dealer.do?ORDER_ID="+value+"&oper=query";
 			 
 				 fm.method="post";
      	fm.submit();
}
   //首先检查是否填写货运单号，否则不予上报
   function checkTransNo(report_id,boxNumber){
	   if(boxNumber>0){
	       MyAlert('有数据未填写装箱单号!');
	       return;
	   }else{
		   var url= "<%=contextPath%>/claim/oldPart/ClaimBackPieceReportManager/checkTransNo.json?report_id="+report_id;
		   makeNomalFormCall(url,isReport,'fm','');
	   }
   }
   function isReport(json){
	   var isFillTransNo=json.flag;
	   var report_id=json.report_id;
	   //暂时去掉限制货运单号的限制(要放开去掉注释就OK)
	   if(isFillTransNo=='0'||isFillTransNo==null){
           MyAlert("货运单号不能为空!");
           return;
	   }
	   MyConfirm("确认上报吗？",report,[report_id]);
   }
   function report(str){
       fm.action="<%=contextPath%>/claim/oldPart/ClaimBackPieceReportManager/reportClaimInfo11.do?report_id="+str;
       fm.method="post";
       fm.submit();
   }
   
   
   function isCheck(id){
	   fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/goApproveAndStoredPage6.do?CLAIM_ID="+id;
       fm.method="post";
       fm.submit();
   }

   function isCheck12(id,temp){
	   var ok = 'ok';
		if(ok=='ok'){
			fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/goApproveAndStoredPage41.do?CLAIM_ID="+id+"&types=0"+"&temp="+temp;
		    fm.method="post";
		    fm.submit();
		}else{
			MyAlert('前期有未审核的旧件!');
		}
   }
   
   
   function isCheck1221(id){
	   var ok = 'ok';
		if(ok=='ok'){
			fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/goApproveAndStoredPage41.do?CLAIM_ID="+id+"&types=1";
		    fm.method="post";
		    fm.submit();
		}else{
			MyAlert('前期有未审核的旧件!');
		}
   }
   function showDetail21(json){
		var ok = json.ok;
		var id = json.id;
		if(ok=='ok'){
			fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/goApproveAndStoredPage41.do?CLAIM_ID="+id;
		    fm.method="post";
		    fm.submit();
		}else{
			MyAlert('前期有未审核的旧件!');
		}
	}

   function isCheck13(id){
	   fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/goApproveAndStoredPage51.do?CLAIM_ID="+id;
	   fm.method="post";
	   fm.submit();
	}
	
   function isCheck1(id){
     fm.action="<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/queryBoxNo.do?id="+id;
	   fm.method="post";
	   fm.submit();
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
</script>
</BODY>
</html>