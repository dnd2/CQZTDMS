<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.infodms.dms.common.Constant" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>索赔件回运清单维护</title>
<% String contextPath = request.getContextPath(); %>
</head>
<body onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理 &gt;索赔件回运清单维护</div>
  <form id="fm" name="fm">
  	  <input type="hidden" name="curPage" id="curPage" value="1" />
      <input type="hidden" name="delId" id="delId" value="" />
    <TABLE class="table_query">
       <tr>
         <td align="right" nowrap="nowrap">物流单号：</td>
         <td><input id="back_order_no" name="back_order_no" value="" type="text" class="middle_txt" datatype="1,is_null,16" callFunction="javascript:MyAlert();"></td>
         <td align="right" nowrap="nowrap">货运方式：</td>
         <td align="left" >
          <script type="text/javascript">
            genSelBoxExp("freight_type",<%=Constant.OLD_RETURN_STATUS%>,"",true,"short_sel","","true",'');
          </script>
         </td>
       </tr>
       <tr>
         <td align="right" nowrap="nowrap">建单时间： </td>
         <td align="left" nowrap="nowrap">
          <input name="create_start_date" id="create_start_date" value="" type="text" class="short_txt" datatype="1,is_date,10" group="create_start_date,create_end_date" hasbtn="true" callFunction="showcalendar(event, 'create_start_date', false);">
         	&nbsp;至&nbsp;
          <input name="create_end_date" id="create_end_date" value="" type="text" class="short_txt" datatype="1,is_date,10" group="create_start_date,create_end_date" hasbtn="true" callFunction="showcalendar(event, 'create_end_date', false);"></td>
          <td align="right" nowrap="nowrap">提报时间： </td>
         <td align="left" nowrap="nowrap">
          <input name="report_start_date" id="report_start_date" value="" type="text" class="short_txt" datatype="1,is_date,10" group="report_start_date,report_end_date" hasbtn="true" callFunction="showcalendar(event, 'report_start_date', false);">
         	&nbsp;至&nbsp;
          <input name="report_end_date" id="report_end_date" value="" type="text" class="short_txt" datatype="1,is_date,10" group="report_start_date,report_end_date" hasbtn="true" callFunction="showcalendar(event, 'report_end_date', false);"></td>
       </tr>
       <tr>
         <td align="right" nowrap="nowrap">处理状态：</td>
         <td align="left">
           <script type="text/javascript">
            genSelBoxExp("ord_status",<%=Constant.BACK_LIST_STATUS%>,"",true,"short_sel","","true",'');
           </script>
         </td>
         <td align="right" nowrap="nowrap">发运地：</td>
         <td align="left">
           <script type="text/javascript">
            genSelBoxExp("yieldly",<%=Constant.SERVICEACTIVITY_CAR_YIELDLY%>,"",true,"short_sel","","true",'');
           </script>
         </td>
       </tr>
       <tr>
         <td align="center" nowrap="nowrap" colspan="4"> <font color="red">*【若服务中心采用自运旧件方式，货运单号填写：00000】</font></td>
       </tr>
       <tr>
         <td align="center" colspan="4">
           <input class="normal_btn" type="button" name="qryButton" value="查询"  onClick="__extQuery__(1);">&nbsp;&nbsp;
           <input class="normal_btn" type="button" id="addButton" name="addButton" value="新增"  onClick="addBackOrd();">
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
   var url = "<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/queryBackListByCondition.json";
				
   var title = null;

   var columns = [
  				{header: "序号",align:'center',renderer:getIndex},
  				{header: "物流单号",dataIndex: 'return_no',align:'center'},
  				{header: "建单日期", dataIndex: 'create_date', align:'center',renderer:formatDate},
  				{header: "提报日期", dataIndex: 'return_date', align:'center',renderer:formatDate},
  				{header: "索赔单数", dataIndex: 'wr_amount', align:'center'},
  				{header: "回运配件项数", dataIndex: 'part_item_amount', align:'center'},
  				{header: "回运配件数", dataIndex: 'part_amount', align:'center'},
  				{header: "装箱总数", dataIndex: 'parkage_amount', align:'center'},
  				{header: "货运方式", dataIndex: 'freight_type', align:'center'},
  				{header: "处理状态", dataIndex: 'status_desc', align:'center'},
  				{id:'action',header: "操作",sortable: false,dataIndex: 'id',renderer:myLink,align:'center'}
  		      ];
   //超链接设置
   function myLink(value,meta,record){
	  var width=800;
	  var height=500;
		
	  var screenW = window.screen.width-30;	
	  var screenH = document.viewport.getHeight();
		
	  if(screenW!=null && screenW!='undefined')
		  width = screenW;
      if(screenH!=null && screenH!='undefined')
		  height = screenH;
	  
	  var status=record.data.status;
	  if(status==<%=Constant.BACK_LIST_STATUS_01%>){
		  return String.format(
			       "<a href='#' onclick=\"boxOper("+value+","+width+","+height+")\">[装箱]</a>"+
					"<a href='#' onClick=\"isDel("+value+");\">[删除]</a>"+
					"<a href='<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/queryBoxNo.do?id="+value+"'>[打印]</a>"
					//"<a href=\"#\" onclick='returnPrint(\""+value+"\")'>[打印]</a>"
					);
	  }else{
		  return String.format(
	               "<a href='#' onClick='OpenHtmlWindow(\"<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/queryBackClaimDetailInfo.do?ORDER_ID="
	               +value+"&oper=query\","+800+","+500+")'>[明细]</a>"+
	               "<a href='<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/queryBoxNo.do?id="+value+"'>[打印]</a>"
	               );
	  }
   }
   //装箱操作
   function boxOper(value,width,height){		
        var i_url="<%=contextPath %>/claim/oldPart/ClaimBackPieceBackListOrdManager/queryBackClaimDetailInfo.do?ORDER_ID="+value+"&oper=mod";
        OpenHtmlWindow(i_url,width,height);
   }
   //打印
   //function returnPrint(returnId){
	   //window.open('<%=contextPath%>/claim/oldPart/ClaimBackPieceReportManager/roMainPrint.do?id='+returnId,"旧件清单打印", "height=700, width=1024, top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=no, status=no");
   //}
   //格式化时间为YYYY-MM-DD
   function formatDate(value,meta,record) {
	 if (value==""||value==null) {
		return "";
	 }else {
		return value.substr(0,10);
	 }
   }
   function isDel(del_id){
	   MyConfirm("确认删除？",delReturnInfo,[del_id]);
       
   }
   
   function delReturnInfo(str){
	   var delUrl = "<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/deleteLogisticOrder.json?returnId="+str;
	   makeNomalFormCall(delUrl,refreshPage,'fm','');
	   //屏蔽原来的删除方法
	   //fm.delId.value=str;
       //fm.action="/claim/oldPart/ClaimBackPieceBackListOrdManager/delBackClaimInfo.do";
       //fm.submit();
   }
   function doInit(){
	   loadcalendar();
   }
   //新增索赔件清单
   function addBackOrd(){
	   //2010-10-11 修改 创建回运单改为从经销商上报的旧件清单统计生成（除一级经销商无回运权利）
	   fm.action = "<%=contextPath%>/claim/oldPart/ClaimBackPieceBackListOrdManager/createLogisticsOrderPage.do";
	   //原来：经销商自己根据索赔单统计回运
	   //fm.action = "/claim/oldPart/ClaimBackPieceBackListOrdManager/addClaimBackOrdPage.do";
	   fm.submit();
   }
   //刷新本页面，供子页面使用
   function refreshPage(){
	   __extQuery__(1);
   }
</script>
</body>
</html>