<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.infodms.dms.common.Constant" %>
<jsp:include page="${contextPath}/common/jsp_head_new.jsp" />
<%@taglib uri="/jstl/cout" prefix="c"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>索赔旧件返运明细</title>
<% String contextPath = request.getContextPath(); 
%>
<script type="text/javascript" src="<%=contextPath%>/js/jslib/CalendarZYW.js"></script>

</head>
<BODY onload="doInit();">
<div class="navigation"><img src="<%=contextPath%>/img/nav.gif"/>&nbsp;当前位置： 售后服务管理&gt;索赔旧件管理 &gt;旧件入库返运明细</div>
  <form id="fm" name="fm">
  	  <input type="hidden" name="curPage" id="curPage" value="1" />
    <TABLE class="table_query">
       <tr>
			<td align="right" nowrap="nowrap">服务站名称：</td>
			<td align="left" > 
				<input id="dealer_name" name="dealer_name" value="" type="text" class="middle_txt" maxlength="20">
			</td>
			
			
			<td class="table_query_3Col_label_7Letter">配件名称： </td>
	         <td nowrap="nowrap">
	         <input id="part_name" name="part_name" value="" type="text" class="middle_txt" maxlength="20">
			 </td>
       </tr>
       <tr>
      		 <td class="table_query_3Col_label_7Letter">服务站代码： </td>
	         <td nowrap="nowrap">
	          <input id="dealer_code" name="dealer_code" value="" type="text" class="middle_txt" maxlength="20">
	         </td>
	         <td class="table_query_3Col_label_7Letter">配件代码： </td>
	         <td nowrap="nowrap">
	          <input id="part_code" name="part_code" value="" type="text" class="middle_txt" maxlength="20">
	         </td>
       </tr>
       <tr>
	          <td class="table_query_3Col_label_7Letter">索赔申请时间： </td>
	        <td align="left" nowrap="true">
			<input name="in_start_date" type="text" class="short_time_txt" id="in_start_date" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'in_start_date', false);" />  	
             &nbsp;至&nbsp; <input name="in_end_date" type="text" class="short_time_txt" id="in_end_date" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'in_end_date', false);" /> 
		</td>	
	         <td class="table_query_3Col_label_7Letter">VIN： </td>
	         <td nowrap="nowrap">
	          <input id="vin" name="vin" value="" type="text" class="middle_txt" datatype="1,is_null,30">
	         </td>
       </tr>
       <tr>
       <td class="table_query_3Col_label_7Letter">回运状态： </td>
	         <td nowrap="nowrap">
	        	<select name="return_status" id="return_status" onchange="changeAudit(this.value);" value="" class="short_sel">
	        	<option value="">--请选择--</option>
	        	<option value="0">已签收</option>
	        	<option value="1">在途</option>
	        	<option value="2">未返运</option>
	        	</select>
	         </td>
	        <td class="table_query_3Col_label_7Letter" id="ad"  style="display: none">审核状态： </td>
	         <td nowrap="nowrap" id="add"  style="display: none">
	        	<select name="is_ok" id="is_ok" value="" class="short_sel">
	        	<option value="">--请选择--</option>
	        	<option value="0">已审核</option>
	        	<option value="1">未审核</option>
	        	<option value="2">拒赔</option>
	        	</select>
	         </td>
	         
	         <td align="right">索赔类型：</td>
               <td align="left">
               <script type="text/javascript">
  					genSelBoxExp("claim_type",<%=Constant.CLA_TYPE%>,"",true,"short_sel","","false",'');
  				</script>
		    	</td>
       </tr>
       
       <tr>
       <td  class="table_query_3Col_label_7Letter">索赔审核时间：</td>
      	<td ><input name="beginTime" type="text" id="beginTime" readonly="readonly" onfocus="calendar();" class="short_time_txt"/>
      	至
      	<input name="endTime" type="text" id="endTime"  readonly="readonly" onfocus="calendar();" class="short_time_txt"/></td>
      	<td class="table_query_3Col_label_7Letter">大区： </td>
	         <td nowrap="nowrap">
                <select id="root_org_name" name="root_org_name" class="short_sel">
							<option value="">--请选择--</option>
							<c:forEach var="org" items="${orglist}">
								<option value="${org.ORG_CODE}" title="${org.ORG_NAME}">${org.ORG_NAME}</option>
							</c:forEach>
					</select>
	         </td>
       </tr>
        <tr>
      		 <td class="table_query_3Col_label_7Letter">索赔单号： </td>
	         <td nowrap="nowrap">
	          <input id="claim_no" name="claim_no" value="" type="text" class="middle_txt" maxlength="20">
	         </td>
	         <td class="table_query_3Col_label_7Letter">回运类型： </td>
	         <td nowrap="nowrap">
	         	<select name="urgent" class="short_sel">
	         		<option value="">--请选择--</option>
	         		<option value="0">常规回运</option>
	         		<option value="1">紧急回运</option>
	         	</select>
	         </td> 
       </tr>
       <tr>
      		 <td class="table_query_3Col_label_7Letter">是否出库： </td>
	         <td nowrap="nowrap">
	         	<select name="is_out" class="short_sel">
	         		<option value="">--请选择--</option>
	         		<option value="1">是</option>
	         		<option value="0">否</option>
	         	</select>
	         </td>
	         <td class="table_query_3Col_label_5Letter">供应商名称：</td>
	         <td nowrap="nowrap">
	            <input type="text" name="producer_name" id="producer_name" class="middle_txt"/>
	         </td>
       </tr>
       <tr>
      		 <td class="table_query_3Col_label_7Letter">入库时间： </td>
	         <td align="left" nowrap="true">
			<input name="inhouse_start_date" type="text" class="short_time_txt" id="inhouse_start_date" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'inhouse_start_date', false);" />  	
             &nbsp;至&nbsp; <input name="inhouse_end_date" type="text" class="short_time_txt" id="inhouse_end_date" readonly="readonly"/> 
			<input name="button" value=" " type="button" class="time_ico" onclick="showcalendar(event, 'inhouse_end_date', false);" /> 
		</td>	
	         <td class="table_query_3Col_label_5Letter"></td>
	         <td nowrap="nowrap">
	         </td>
       </tr>
       <tr>
         <td align="center" nowrap="nowrap" colspan="4">
           <input class="normal_btn" type="button" id="qryButton" name="qryButton" value="查询"  onClick="__extQuery__(1);">
           &nbsp;&nbsp;
           <input class="normal_btn" type="button" id="qryButton" name="qryButton" value="导出"  onClick="to_excel();">
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
   var url = "<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/oldPartReturnDetailList.json";
				
   var title = null;
   
   var columns = [
  				{header: "序号",align:'center',renderer:getIndex},
  				{header: "服务站代码", dataIndex: 'DEALER_CODE', align:'center'},
  				{header: "服务站名称", dataIndex: 'DEALER_NAME', align:'center'},
  				{header: "大区", dataIndex: 'ROOT_ORG_NAME', align:'center'},
  				{header: "回运类型", dataIndex: 'URGENT', align:'center',renderer:myLink4},
  				{header: "索赔单号",dataIndex: 'CLAIM_NO',align:'center',renderer:myLink},
  				{header: "索赔单申请时间",dataIndex: 'SUB_TIME',align:'center'},
  				{header: "索赔单审核时间",dataIndex: 'REPORT_TIME',align:'center'},
  				{header: "入库时间",dataIndex: 'IN_WARHOUSE_DATE',align:'center'},
  				{header: "配件代码", dataIndex: 'PART_CODE', align:'center'},
  				{header: "配件名称", dataIndex: 'PART_NAME', align:'center'},
  				{header: "回运状态", dataIndex: 'RETURN_STATUS', align:'center'},
  				{header: "审核状态", dataIndex: 'AUDIT_STATUS', align:'center'},
  				{header: "拒赔原因", dataIndex: 'DEDUCT_DESC', align:'center'},
  				{header: "回运申请时间", dataIndex: 'IN_TIME', align:'center'},
  				{header: "供应商代码",dataIndex: 'DOWN_PRODUCT_CODE',align:'center'},
  				{header: "供应商名称",dataIndex: 'DOWN_PRODUCT_NAME',align:'center'},
  				{header: "车型", dataIndex: 'MODEL_NAME', align:'center'},
  				{header: "VIN", dataIndex: 'VIN', align:'center'},
  				{header: "责任性质", dataIndex: 'RESPONSIBILITY_TYPE', align:'center',renderer:getItemValue},
  				{header: "索赔类型", dataIndex: 'CLAIM_TYPE', align:'center',renderer:getItemValue},
  				{header: "回运单号",dataIndex: 'RETURN_NO',align:'center'},
  				{header: "旧件编号",dataIndex: 'BARCODE_NO',align:'center'}
  		      ];
   function myLink4(value,meta,record){
	   var str="";
	  	if(1==value){
	  		str="紧急调件";
	  	}else{
	  		str="常规回运";
	  	}
	   return String.format(str);
	}
   function myLink(value,meta,record){
   	return String.format( "<a href='#' onClick='claimDetail(\""+record.data.ID+"\",\""+record.data.SUB_TIME+"\",\""+record.data.RO_NO+"\",\""+record.data.CLAIM_TYPE+"\");'>["+record.data.CLAIM_NO+"]");
   }
  
   function claimDetail(id,subtime,rono,claimtype){
	 //新分单时间
		var str ='2015-05-25 19:30:00';
		var st = str.replace(/-/g,"/");
		var date = (new Date(st)).getTime();  
		var st1 =subtime.replace(/-/g,"/");
		var date1 = (new Date(st1)).getTime();
		if(date<date1){//分单后
			OpenHtmlWindow("<%=contextPath%>/ClaimBalanceAction/claimBalanceAuditingPage.do?goBackType=2&view=view&roNo="+rono+"&id="+id +"&claim_type="+claimtype,900,500);
		}else{
			OpenHtmlWindow('<%=contextPath%>/claim/dealerClaimMng/ClaimBillTrack/claimBillDetailForward.do?ID='+id,900,500);
		}
	 }
   function doInit(){
	   loadcalendar();
   }
   //导出功能
   function to_excel(){
	   fm.action = "<%=contextPath%>/claim/oldPart/ClaimOldPartOutStorageManager/toExcel2.do";
	fm.submit();
   }
   function changeAudit(value){
   	if(value==0&&value!=""){
   		$('ad').style.display='';
   		$('add').style.display='';
   	}else{
   		$('ad').style.display='none';
   		$('add').style.display='none';
   		$('is_ok').value='';
   	}
   }
</script>
</BODY>
</html>