<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="/jstl/change" prefix="change" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<style> 
        .black_overlay{ 
            display: none; 
            position: absolute; 
            top: 0%; 
            left: 0%; 
            width: 100%; 
            height: 100%; 
            background-color: black; 
            z-index:1001; 
            -moz-opacity: 0.8; 
            opacity:.80; 
            filter: alpha(opacity=88); 
        } 
        .white_content { 
            display: none; 
            position: absolute; 
            top: 25%; 
            left: 25%; 
            width: 30%; 
            height: 30%; 
            padding: 20px; 
            border: 10px solid #DAE0EE;
            background-color: white; 
            z-index:1002; 
            overflow: auto; 
        } 
    </style> 
<title>索赔紧急调件查询</title>
<% String contextPath = request.getContextPath(); 
%>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>
</head>
<BODY onload="__extQuery__(1);">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理 &gt;索赔紧急调件库存查询</div>
  <form id="fm1" name="fm1" method="post">
		<input name="ids" id="ids" type="hidden"/>
		<input name="bm" id="bm"  type="hidden">
  </form>
  
  <form id="fm" name="fm">
   <table width=100% border="0" align="center" cellpadding="1" cellspacing="1" class="table_query">
  	<tr>
  		
		<td width="10%" class="table_query_2Col_label_5Letter" nowrap="true">零部件代码：</td>
		<td width="10%"><input name="partCode" class="middle_txt" id="partCode"> </td>
				
		<td width="10%"  class="table_query_2Col_label_8Letter" nowrap="true">零部件名称：</td>
		<td width="10%"><input name="partName" class="middle_txt" id="partName"> </td>
		<td width="10%"  class="table_query_2Col_label_8Letter" nowrap="true">经销商代码：</td>
		<td width="10%"><input name="dealerCode" class="middle_txt" id="dealerCode"> </td>
		<td width="20%"></td>
		<td width="20%"></td>
	</tr>
	 <tr>
	 
		<td width="10%"  class="table_query_2Col_label_5Letter" >经销商名称：</td>
		<td width="10%"><input name="dealerName" class="middle_txt" id="dealerName"> </td>
		<td width="10%"  class="table_query_2Col_label_8Letter"  >供应商代码：</td>
		<td width="10%"><input name="supplyCode" class="middle_txt" id="supplyCode"> </td>
		<td width="10%"  class="table_query_2Col_label_8Letter" >供应商名称：</td>
		<td width="10%"><input name="supplyName" class="middle_txt" id="supplyName"> </td>
		<td width="20%"></td>
		<td width="20%"></td>
	</tr>
	 <tr>
		<td width="10%"  class="table_query_2Col_label_5Letter" >索赔单号：</td>
		<td width="10%"><input name="claimNo" class="middle_txt" id="claimNo"> </td>
		<td width="10%"  class="table_query_2Col_label_8Letter"  >VIN：</td>
		<td width="10%"><input name="VIN" class="middle_txt" id="VIN"> </td>
		 <td align="right" nowrap="nowrap" >借出时间： </td>
         <td align="left" nowrap="true">
			<input name="start_date" type="text" class="short_time_txt" id="start_date" onclick="calendar();" readonly="readonly"/> 
             &nbsp;至&nbsp; <input name="end_date" type="text" class="short_time_txt" id="end_date" onclick="calendar();" readonly="readonly"/> 
		</td>	
		<td width="20%"></td>
	</tr>
	
       <tr>
         <td align="center" nowrap="nowrap" colspan="4">
           <input class="normal_btn" type="button" id="qryButton" name="qryButton" value="查询"  onClick="__extQuery__(1);">
           &nbsp;&nbsp;
           <input class="normal_btn" type="button" id="qryButton1" name="qryButton" value="调入"  onClick="call_in(this);">
         </td>
       </tr>
  </table>
  <!--分页 begin -->
	<jsp:include page="${contextPath}/queryPage/orderHidden.html" />
	<jsp:include page="${contextPath}/queryPage/pageDiv.html" />
  <!--分页 end -->
  
<div id="loader" style='position:absolute;z-index:200;background:#FFCC00;padding:1px;top:4px;display:none;display: none;'></div>
</form>

 <div id="light" class="white_content">

		<tr>
		<td> 请输入归件人：   </td>
		<td>   <input type="text" id="borrowMan" name="borrowMan" value=""/> </td>
		</tr>
		<tr>
		<a href = "javascript:void(0)" onclick = "document.getElementById('light').style.display='none';document.getElementById('fade').style.display='none' ; getPerson(1);"><button >确定</button></a>
		<a href = "javascript:void(0)" onclick = "document.getElementById('light').style.display='none';document.getElementById('fade').style.display='none' ; getPerson(0);"><button >关闭</button></a>
		</tr>
</div> 

		
        <div id="fade" class="black_overlay"></div> 
<br>
<script type="text/javascript">
   
var myPage;
//查询路径
var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartStorageManager/queryListBorrowCallOutData.json";
var obj="";				
var title = null;

var columns = [
				{header:"<input type=\"checkBox\" id=\"checkBoxAll\" name=\"checkBoxAll\"  onclick='selectAll(this,\"check\")' />全选", align:'center',sortable:false, dataIndex:'ID',width:'2%',renderer:checkBoxShow},
				{header: "配件名称", dataIndex: 'PART_NAME', align:'center'},
				{header: "配件代码", dataIndex: 'PART_CODE', align:'center'},
				{header: "索赔单号", dataIndex: 'CLAIM_NO', align:'center',renderer:mylink},
				{header: "经销商代码", dataIndex: 'DEALER_CODE', align:'center'},
				{header: "经销商名称", dataIndex: 'DEALER_NAME', align:'center'},
				{header: "供应商代码", dataIndex: 'SUPPLY_CODE', align:'center'},
				{header: "供应商名称", dataIndex: 'SUPPLY_NAME', align:'center'},
				{header: "VIN", dataIndex: 'VIN', align:'center'},
				{header: "车型", dataIndex: 'MODEL_NAME', align:'center'},
				{header: "库存", dataIndex: 'ALL_AMOUNT', align:'center'},
				{header: "索赔类型", dataIndex: 'CLAIM_TYPE', align:'center',renderer:getItemValue},
				{header: "借出操作人", dataIndex: 'CREATE_PERSON', align:'center'},
				{header: "借出时间", dataIndex: 'CREATE_DATE', align:'center'},
				{header: "借出人", dataIndex: 'BORROW_MAN', align:'center'}
			
		      ];
		      
	function call_in(obj){
		
		document.getElementById('light').style.display='block';
  		document.getElementById('fade').style.display='block';
//	    obj=ids;
		
		
	}
	
	function getPerson(value){
   		if(value==0){
   			$("contactMan").value="";
   		}
   		var borrowMan=$("borrowMan").value;
   		if(borrowMan==""){
	  		return false;
	  	}
   		

		obj.disabled=true;
		var ids="";
		var checkids=document.getElementsByName("check");
		for(var i=0;i<checkids.length;i++){
			if(checkids[i].checked==true){
  			ids+=checkids[i].value+",";
			}
		}
		if(ids!=""){
	  		var url="<%=contextPath%>/claim/oldPart/ClaimOldPartStorageManager/callInById.json";
  			$("fm1").action=url;
	  		$("ids").value=ids;
	  		$("bm").value=borrowMan;
  			sendAjax(url,backSucc,'fm1');
		}else{
			MyAlert("提示：请选择紧急旧件！");
			obj.disabled=false;
		}
   	}
	function backSucc(json){
		if(json.succ==0){
			$('qryButton1').disabled=false;
			MyAlert("提示：紧急调件调入成功！");
			__extQuery__(1);
		}else{
			$('qryButton1').disabled=false;
			MyAlert("提示：紧急调件调入失败！");
		}
	}
		function checkBoxShow(value,meta,record){//value='" + + "'
			return String.format("<input type='checkbox' id='check' name='check' value='" + record.data.ID + "' />");
		}
		function checkDate(obj,value){
			obj.checked = true;
		}
		function mylink(value,meta,record){
			var width=900;
			var height=500;
			var screenW = window.screen.width-30;	
			var screenH = document.viewport.getHeight();
			if(screenW!=null && screenW!='undefined')
				width = screenW;
			if(screenH!=null && screenH!='undefined')
				height = screenH;
			var roNo = record.data.RO_NO;
			var ID = record.data.ID;
			var claimNo = record.data.CLAIM_NO;
			return String.format("<a href='#' onclick='OpenHtmlWindow(\"<%=contextPath%>/claim/application/ClaimBillStatusTrack/claimBillDetailForward.do?roNo="+roNo+"&ID="
					+ ID + "\","+width+","+height+")' >"+claimNo+"</a>");
		}
</script>
</BODY>
</html>