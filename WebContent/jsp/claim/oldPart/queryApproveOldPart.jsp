<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.infodms.dms.common.Constant" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@page import="com.infodms.dms.po.TmBusinessAreaPO,java.util.*" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>索赔旧件审批入库查询</title>
<% String contextPath = request.getContextPath();
	String isReutrn=request.getAttribute("isReturn")==null?"":request.getAttribute("isReturn").toString();

	List<TmBusinessAreaPO> list = (List<TmBusinessAreaPO>)request.getAttribute("yieldlyList");
	if(isReutrn.equals("1")) isReutrn="__extQuery__(1);";
%>
</head>
<BODY onload="doInit();<%=isReutrn %>">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理&gt;旧件签收入库查询</div>
  <form id="fm" name="fm">
  	  <input type="hidden" name="curPage" id="curPage" value="1" />
      <input type="hidden" name="delId" id="delId" value="" />
     <TABLE class="table_query">
       <tr>
         <td align="right" nowrap="nowrap"  rowspan="2">经销商代码：</td>            
         <td align="left" nowrap="nowrap" rowspan="2">
            <textarea id="dealerCode"  name="dealerCode" rows="3" cols="20" datatype="1,is_null,2000" disabled="disabled" >${dealerCode }</textarea>
            <input name="button1" type="button" class="mini_btn" style="cursor: pointer;" onclick="showOrgDealer('dealerCode','','true','',true);" value="..." />        
            <input name="button2" type="button" class="normal_btn" onClick="clr();" value="清除"/>
         </td>
         <td align="right" nowrap="nowrap" >经销商名称：</td>
         <td align="left" nowrap="nowrap"><input id="dealerName" name="dealerName" value="${dealerName }" type="text" class="middle_txt" datatype="1,is_name,15" callFunction="javascript:MyAlert();"></td>
       </tr>
       <tr>
          <td align="right" nowrap="nowrap" >旧件回运清单号：</td>
         <td align="left" nowrap="nowrap"><input id="back_order_no" name="back_order_no" value="${back_order_no }" type="text" class="middle_txt"></td>
       </tr>
       <tr>
       		<td align="right" nowrap="nowrap" >生产基地：</td>
			<td align="left" nowrap="nowrap">
			 <script type="text/javascript">
				            genSelBoxExp("YIELDLY_TYPE",<%=Constant.PART_IS_CHANGHE%>,"",true,"short_sel","","false",'');
				        </script>
			<!--  	<select name="YIELDLY_TYPE" id="YIELDLY_TYPE" onChange="showDate(this.value);">
					<option value="">-请选择-</option>
					<%for(int i=0;i<list.size();i++){ %>
						<option value="<%=list.get(i).getAreaId() %>"><%=list.get(i).getAreaName() %></option>
					<%} %>-->
			</td>
	         <td align="right" nowrap="nowrap" >货运方式：</td>
	         <td align="left" nowrap="nowrap">
	          <script type="text/javascript">
	            genSelBoxExp("freight_type",<%=Constant.OLD_RETURN_STATUS%>,"${freight_type }",true,"short_sel","","false",'');
	           </script>
	          </td>
       </tr>
       <tr>
         <td align="right" nowrap="nowrap" >入库时间： </td>
         <td align="left" nowrap="nowrap">
          <input name="create_start_date" readonly="readonly" id="create_start_date" value="${create_start_date }" type="text" class="short_txt" datatype="1,is_date,10" group="create_start_date,create_end_date" hasbtn="true" callFunction="showcalendar(event, 'create_start_date', false);">
         	&nbsp;至&nbsp;
          <input name="create_end_date"  readonly="readonly" id="create_end_date" value="${create_end_date }" type="text" class="short_txt" datatype="1,is_date,10" group="create_start_date,create_end_date" hasbtn="true" callFunction="showcalendar(event, 'create_end_date', false);">
          </td>
         <td align="right" nowrap="nowrap" >提报时间： </td>
         <td align="left" nowrap="nowrap">
          <input name="report_start_date"  readonly="readonly" id="report_start_date" value="${report_start_date }" type="text" class="short_txt" datatype="1,is_date,10" group="report_start_date,report_end_date" hasbtn="true" callFunction="showcalendar(event, 'report_start_date', false);">
         	&nbsp;至&nbsp;
          <input name="report_end_date"  readonly="readonly" id="report_end_date" value="${report_end_date }" type="text" class="short_txt" datatype="1,is_date,10" group="report_start_date,report_end_date" hasbtn="true" callFunction="showcalendar(event, 'report_end_date', false);"></td>
       </tr>
       <tr>
          <td align="right" nowrap="nowrap" >旧件回运清单状态：</td>
          <td align="left" nowrap="nowrap">
            <script type="text/javascript">
             genSelBoxExp("back_type",<%=Constant.BACK_LIST_STATUS%>,"${back_type }",true,"short_sel","","false",'<%=Constant.BACK_LIST_STATUS_05%>');
            </script>
          </td>         
         <td align="right" nowrap="nowrap" >货运单号：</td>
          <td align="left" nowrap="nowrap"><input id="trans_no" name="trans_no" value="${trans_no }" type="text" class="middle_txt">
          </td>         
       </tr>
       <tr>
         <td align="center" colspan="4" nowrap="nowrap">
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
   var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/queryApproveList11.json";
				
   var title = null;

   var columns = [
  				{header: "序号",align:'center',renderer:getIndex},
				{id:'action',header: "操作",sortable: false,dataIndex: 'id',renderer:myLink,align:'center'},
  				{header: "经销商代码",dataIndex: 'dealer_id',align:'center'},
  				{header: "经销商名称", dataIndex: 'dealer_name', align:'center'},
  				{header: "旧件回运清单号", dataIndex: 'claim_no', align:'center'},
  				{header: "旧件回运清单状态", dataIndex: 'back_desc', align:'center'},
  				{header: "提报日期", dataIndex: 'return_date', align:'center',renderer:formatDate},
  				{header: "入库日期", dataIndex: 'create_date', align:'center'},
  				{header: "货运方式", dataIndex: 'return_desc', align:'center'},
  				{header: "旧件回运起止时间", dataIndex: 'wr_start_date', align:'center'},
  				{header: "索赔单数", dataIndex: 'wr_amount', align:'center'},
  				{header: "装箱数", dataIndex: 'parkage_amount', align:'center'},
  				{header: "索赔配件数", dataIndex: 'part_amount', align:'center'},
  				{header: "货运单号", dataIndex: 'trans_no', align:'center'},
  				{header: "三包员电话", dataIndex: 'tel', align:'center'}
  		      ];
   //超链接设置
   function myLink(value,meta,record){
	   var back_type=record.data.back_type;
	   var box_no = record.data.box_no;//装箱单号
	   var currStatus = record.data.status;
		   return String.format("<a href='#' onclick=isCheck1("+value+")>[打印]</a>"+"<a href='#' onClick='OpenHtmlWindow(\"<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/queryDetail11.do?CLAIM_ID="
	               +value+"\",1100,500)'>[明细]</a>");
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
	   /*if(isFillTransNo=='0'){
           MyAlert("货运单号不能为空!");
           return;
	   }*/
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

   function isCheck12(id){
	   var ok = 'ok';
		if(ok=='ok'){
			fm.action="<%=contextPath%>/claim/oldPart/ClaimOldPartApporoveStorageManager/goApproveAndStoredPage41.do?CLAIM_ID="+id+"&types=0";
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
		return value.substr(0,10);
	 }
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